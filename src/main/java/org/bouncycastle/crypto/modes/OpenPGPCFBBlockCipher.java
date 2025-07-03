package org.bouncycastle.crypto.modes;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;

public class OpenPGPCFBBlockCipher implements BlockCipher {
   private byte[] IV;
   private byte[] FR;
   private byte[] FRE;
   private byte[] tmp;
   private BlockCipher cipher;
   private int count;
   private int blockSize;
   private boolean forEncryption;

   public OpenPGPCFBBlockCipher(BlockCipher cipher) {
      this.cipher = cipher;
      this.blockSize = cipher.getBlockSize();
      this.IV = new byte[this.blockSize];
      this.FR = new byte[this.blockSize];
      this.FRE = new byte[this.blockSize];
      this.tmp = new byte[this.blockSize];
   }

   public BlockCipher getUnderlyingCipher() {
      return this.cipher;
   }

   public String getAlgorithmName() {
      return this.cipher.getAlgorithmName() + "/OpenPGPCFB";
   }

   public int getBlockSize() {
      return this.cipher.getBlockSize();
   }

   public int processBlock(byte[] in, int inOff, byte[] out, int outOff) throws DataLengthException, IllegalStateException {
      return this.forEncryption ? this.encryptBlock(in, inOff, out, outOff) : this.decryptBlock(in, inOff, out, outOff);
   }

   public void reset() {
      this.count = 0;

      for(int i = 0; i != this.FR.length; ++i) {
         this.FR[i] = this.IV[i];
      }

      this.cipher.reset();
   }

   public void init(boolean forEncryption, CipherParameters params) throws IllegalArgumentException {
      this.forEncryption = forEncryption;
      this.reset();
      this.cipher.init(true, params);
   }

   private byte encryptByte(byte data, int blockOff) {
      return (byte)(this.FRE[blockOff] ^ data);
   }

   private int encryptBlock(byte[] in, int inOff, byte[] out, int outOff) throws DataLengthException, IllegalStateException {
      if (inOff + this.blockSize > in.length) {
         throw new DataLengthException("input buffer too short");
      } else if (outOff + this.blockSize > out.length) {
         throw new DataLengthException("output buffer too short");
      } else {
         int n;
         if (this.count > this.blockSize) {
            this.FR[this.blockSize - 2] = out[outOff] = this.encryptByte(in[inOff], this.blockSize - 2);
            this.FR[this.blockSize - 1] = out[outOff + 1] = this.encryptByte(in[inOff + 1], this.blockSize - 1);
            this.cipher.processBlock(this.FR, 0, this.FRE, 0);

            for(n = 2; n < this.blockSize; ++n) {
               out[outOff + n] = this.encryptByte(in[inOff + n], n - 2);
            }

            System.arraycopy(out, outOff + 2, this.FR, 0, this.blockSize - 2);
         } else if (this.count == 0) {
            this.cipher.processBlock(this.FR, 0, this.FRE, 0);

            for(n = 0; n < this.blockSize; ++n) {
               out[outOff + n] = this.encryptByte(in[inOff + n], n);
            }

            System.arraycopy(out, outOff, this.FR, 0, this.blockSize);
            this.count += this.blockSize;
         } else if (this.count == this.blockSize) {
            this.cipher.processBlock(this.FR, 0, this.FRE, 0);
            out[outOff] = this.encryptByte(in[inOff], 0);
            out[outOff + 1] = this.encryptByte(in[inOff + 1], 1);
            System.arraycopy(this.FR, 2, this.FR, 0, this.blockSize - 2);
            System.arraycopy(out, outOff, this.FR, this.blockSize - 2, 2);
            this.cipher.processBlock(this.FR, 0, this.FRE, 0);

            for(n = 2; n < this.blockSize; ++n) {
               out[outOff + n] = this.encryptByte(in[inOff + n], n - 2);
            }

            System.arraycopy(out, outOff + 2, this.FR, 0, this.blockSize - 2);
            this.count += this.blockSize;
         }

         return this.blockSize;
      }
   }

   private int decryptBlock(byte[] in, int inOff, byte[] out, int outOff) throws DataLengthException, IllegalStateException {
      if (inOff + this.blockSize > in.length) {
         throw new DataLengthException("input buffer too short");
      } else if (outOff + this.blockSize > out.length) {
         throw new DataLengthException("output buffer too short");
      } else {
         int n;
         if (this.count > this.blockSize) {
            System.arraycopy(in, inOff, this.tmp, 0, this.blockSize);
            out[outOff + 0] = this.encryptByte(this.tmp[0], this.blockSize - 2);
            out[outOff + 1] = this.encryptByte(this.tmp[1], this.blockSize - 1);
            System.arraycopy(this.tmp, 0, this.FR, this.blockSize - 2, 2);
            this.cipher.processBlock(this.FR, 0, this.FRE, 0);

            for(n = 2; n < this.blockSize; ++n) {
               out[outOff + n] = this.encryptByte(this.tmp[n], n - 2);
            }

            System.arraycopy(this.tmp, 2, this.FR, 0, this.blockSize - 2);
         } else if (this.count == 0) {
            this.cipher.processBlock(this.FR, 0, this.FRE, 0);

            for(n = 0; n < this.blockSize; ++n) {
               this.FR[n] = in[inOff + n];
               out[n] = this.encryptByte(in[inOff + n], n);
            }

            this.count += this.blockSize;
         } else if (this.count == this.blockSize) {
            System.arraycopy(in, inOff, this.tmp, 0, this.blockSize);
            this.cipher.processBlock(this.FR, 0, this.FRE, 0);
            out[outOff + 0] = this.encryptByte(this.tmp[0], 0);
            out[outOff + 1] = this.encryptByte(this.tmp[1], 1);
            System.arraycopy(this.FR, 2, this.FR, 0, this.blockSize - 2);
            this.FR[this.blockSize - 2] = this.tmp[0];
            this.FR[this.blockSize - 1] = this.tmp[1];
            this.cipher.processBlock(this.FR, 0, this.FRE, 0);

            for(n = 2; n < this.blockSize; ++n) {
               this.FR[n - 2] = in[inOff + n];
               out[outOff + n] = this.encryptByte(in[inOff + n], n - 2);
            }

            this.count += this.blockSize;
         }

         return this.blockSize;
      }
   }
}
