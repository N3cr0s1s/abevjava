package org.bouncycastle.crypto.modes;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.params.ParametersWithIV;

public class CFBBlockCipher implements BlockCipher {
   private byte[] IV;
   private byte[] cfbV;
   private byte[] cfbOutV;
   private int blockSize;
   private BlockCipher cipher = null;
   private boolean encrypting;

   public CFBBlockCipher(BlockCipher cipher, int bitBlockSize) {
      this.cipher = cipher;
      this.blockSize = bitBlockSize / 8;
      this.IV = new byte[cipher.getBlockSize()];
      this.cfbV = new byte[cipher.getBlockSize()];
      this.cfbOutV = new byte[cipher.getBlockSize()];
   }

   public BlockCipher getUnderlyingCipher() {
      return this.cipher;
   }

   public void init(boolean encrypting, CipherParameters params) throws IllegalArgumentException {
      this.encrypting = encrypting;
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

   public String getAlgorithmName() {
      return this.cipher.getAlgorithmName() + "/CFB" + this.blockSize * 8;
   }

   public int getBlockSize() {
      return this.blockSize;
   }

   public int processBlock(byte[] in, int inOff, byte[] out, int outOff) throws DataLengthException, IllegalStateException {
      return this.encrypting ? this.encryptBlock(in, inOff, out, outOff) : this.decryptBlock(in, inOff, out, outOff);
   }

   public int encryptBlock(byte[] in, int inOff, byte[] out, int outOff) throws DataLengthException, IllegalStateException {
      if (inOff + this.blockSize > in.length) {
         throw new DataLengthException("input buffer too short");
      } else if (outOff + this.blockSize > out.length) {
         throw new DataLengthException("output buffer too short");
      } else {
         this.cipher.processBlock(this.cfbV, 0, this.cfbOutV, 0);

         for(int i = 0; i < this.blockSize; ++i) {
            out[outOff + i] = (byte)(this.cfbOutV[i] ^ in[inOff + i]);
         }

         System.arraycopy(this.cfbV, this.blockSize, this.cfbV, 0, this.cfbV.length - this.blockSize);
         System.arraycopy(out, outOff, this.cfbV, this.cfbV.length - this.blockSize, this.blockSize);
         return this.blockSize;
      }
   }

   public int decryptBlock(byte[] in, int inOff, byte[] out, int outOff) throws DataLengthException, IllegalStateException {
      if (inOff + this.blockSize > in.length) {
         throw new DataLengthException("input buffer too short");
      } else if (outOff + this.blockSize > out.length) {
         throw new DataLengthException("output buffer too short");
      } else {
         this.cipher.processBlock(this.cfbV, 0, this.cfbOutV, 0);
         System.arraycopy(this.cfbV, this.blockSize, this.cfbV, 0, this.cfbV.length - this.blockSize);
         System.arraycopy(in, inOff, this.cfbV, this.cfbV.length - this.blockSize, this.blockSize);

         for(int i = 0; i < this.blockSize; ++i) {
            out[outOff + i] = (byte)(this.cfbOutV[i] ^ in[inOff + i]);
         }

         return this.blockSize;
      }
   }

   public void reset() {
      System.arraycopy(this.IV, 0, this.cfbV, 0, this.IV.length);
      this.cipher.reset();
   }
}
