package org.bouncycastle.crypto.modes;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.params.ParametersWithIV;

public class PGPCFBBlockCipher implements BlockCipher {
   private byte[] IV;
   private byte[] FR;
   private byte[] FRE;
   private byte[] tmp;
   private BlockCipher cipher;
   private int count;
   private int blockSize;
   private boolean forEncryption;
   private boolean inlineIv;

   public PGPCFBBlockCipher(BlockCipher cipher, boolean inlineIv) {
      this.cipher = cipher;
      this.inlineIv = inlineIv;
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
      return this.inlineIv ? this.cipher.getAlgorithmName() + "/PGPCFBwithIV" : this.cipher.getAlgorithmName() + "/PGPCFB";
   }

   public int getBlockSize() {
      return this.cipher.getBlockSize();
   }

   public int processBlock(byte[] in, int inOff, byte[] out, int outOff) throws DataLengthException, IllegalStateException {
      if (this.inlineIv) {
         return this.forEncryption ? this.encryptBlockWithIV(in, inOff, out, outOff) : this.decryptBlockWithIV(in, inOff, out, outOff);
      } else {
         return this.forEncryption ? this.encryptBlock(in, inOff, out, outOff) : this.decryptBlock(in, inOff, out, outOff);
      }
   }

   public void reset() {
      this.count = 0;

      for(int i = 0; i != this.FR.length; ++i) {
         if (this.inlineIv) {
            this.FR[i] = 0;
         } else {
            this.FR[i] = this.IV[i];
         }
      }

      this.cipher.reset();
   }

   public void init(boolean forEncryption, CipherParameters params) throws IllegalArgumentException {
      this.forEncryption = forEncryption;
      if (params instanceof ParametersWithIV) {
         ParametersWithIV ivParam = (ParametersWithIV)params;
         byte[] iv = ivParam.getIV();
         if (iv.length < this.IV.length) {
            System.arraycopy(iv, 0, this.IV, this.IV.length - iv.length, iv.length);

            for(int i = 0; i < this.IV.length - iv.length; ++i) {
               this.IV[i] = 0;
            }
         } else {
            System.arraycopy(iv, 0, this.IV, 0, this.IV.length);
         }

         this.reset();
         this.cipher.init(true, ivParam.getParameters());
      } else {
         this.reset();
         this.cipher.init(true, params);
      }

   }

   private byte encryptByte(byte data, int blockOff) {
      return (byte)(this.FRE[blockOff] ^ data);
   }

   private int encryptBlockWithIV(byte[] in, int inOff, byte[] out, int outOff) throws DataLengthException, IllegalStateException {
      if (inOff + this.blockSize > in.length) {
         throw new DataLengthException("input buffer too short");
      } else if (outOff + this.blockSize > out.length) {
         throw new DataLengthException("output buffer too short");
      } else {
         int n;
         if (this.count == 0) {
            this.cipher.processBlock(this.FR, 0, this.FRE, 0);

            for(n = 0; n < this.blockSize; ++n) {
               out[outOff + n] = this.encryptByte(this.IV[n], n);
            }

            System.arraycopy(out, outOff, this.FR, 0, this.blockSize);
            this.cipher.processBlock(this.FR, 0, this.FRE, 0);
            out[outOff + this.blockSize] = this.encryptByte(this.IV[this.blockSize - 2], 0);
            out[outOff + this.blockSize + 1] = this.encryptByte(this.IV[this.blockSize - 1], 1);
            System.arraycopy(out, outOff + 2, this.FR, 0, this.blockSize);
            this.cipher.processBlock(this.FR, 0, this.FRE, 0);

            for(n = 0; n < this.blockSize; ++n) {
               out[outOff + this.blockSize + 2 + n] = this.encryptByte(in[inOff + n], n);
            }

            System.arraycopy(out, outOff + this.blockSize + 2, this.FR, 0, this.blockSize);
            this.count += 2 * this.blockSize + 2;
            return 2 * this.blockSize + 2;
         } else {
            if (this.count >= this.blockSize + 2) {
               this.cipher.processBlock(this.FR, 0, this.FRE, 0);

               for(n = 0; n < this.blockSize; ++n) {
                  out[outOff + n] = this.encryptByte(in[inOff + n], n);
               }

               System.arraycopy(out, outOff, this.FR, 0, this.blockSize);
            }

            return this.blockSize;
         }
      }
   }

   private int decryptBlockWithIV(byte[] in, int inOff, byte[] out, int outOff) throws DataLengthException, IllegalStateException {
      if (inOff + this.blockSize > in.length) {
         throw new DataLengthException("input buffer too short");
      } else if (outOff + this.blockSize > out.length) {
         throw new DataLengthException("output buffer too short");
      } else {
         int n;
         if (this.count == 0) {
            for(n = 0; n < this.blockSize; ++n) {
               this.FR[n] = in[inOff + n];
            }

            this.cipher.processBlock(this.FR, 0, this.FRE, 0);
            this.count += this.blockSize;
            return 0;
         } else if (this.count == this.blockSize) {
            System.arraycopy(in, inOff, this.tmp, 0, this.blockSize);
            System.arraycopy(this.FR, 2, this.FR, 0, this.blockSize - 2);
            this.FR[this.blockSize - 2] = this.tmp[0];
            this.FR[this.blockSize - 1] = this.tmp[1];
            this.cipher.processBlock(this.FR, 0, this.FRE, 0);

            for(n = 0; n < this.blockSize - 2; ++n) {
               out[outOff + n] = this.encryptByte(this.tmp[n + 2], n);
            }

            System.arraycopy(this.tmp, 2, this.FR, 0, this.blockSize - 2);
            this.count += 2;
            return this.blockSize - 2;
         } else {
            if (this.count >= this.blockSize + 2) {
               System.arraycopy(in, inOff, this.tmp, 0, this.blockSize);
               out[outOff + 0] = this.encryptByte(this.tmp[0], this.blockSize - 2);
               out[outOff + 1] = this.encryptByte(this.tmp[1], this.blockSize - 1);
               System.arraycopy(this.tmp, 0, this.FR, this.blockSize - 2, 2);
               this.cipher.processBlock(this.FR, 0, this.FRE, 0);

               for(n = 0; n < this.blockSize - 2; ++n) {
                  out[outOff + n + 2] = this.encryptByte(this.tmp[n + 2], n);
               }

               System.arraycopy(this.tmp, 2, this.FR, 0, this.blockSize - 2);
            }

            return this.blockSize;
         }
      }
   }

   private int encryptBlock(byte[] in, int inOff, byte[] out, int outOff) throws DataLengthException, IllegalStateException {
      if (inOff + this.blockSize > in.length) {
         throw new DataLengthException("input buffer too short");
      } else if (outOff + this.blockSize > out.length) {
         throw new DataLengthException("output buffer too short");
      } else {
         this.cipher.processBlock(this.FR, 0, this.FRE, 0);

         int n;
         for(n = 0; n < this.blockSize; ++n) {
            out[outOff + n] = this.encryptByte(in[inOff + n], n);
         }

         for(n = 0; n < this.blockSize; ++n) {
            this.FR[n] = out[outOff + n];
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
         this.cipher.processBlock(this.FR, 0, this.FRE, 0);

         int n;
         for(n = 0; n < this.blockSize; ++n) {
            out[outOff + n] = this.encryptByte(in[inOff + n], n);
         }

         for(n = 0; n < this.blockSize; ++n) {
            this.FR[n] = in[inOff + n];
         }

         return this.blockSize;
      }
   }
}
