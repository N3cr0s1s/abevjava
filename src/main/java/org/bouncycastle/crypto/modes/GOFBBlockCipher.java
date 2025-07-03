package org.bouncycastle.crypto.modes;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.params.ParametersWithIV;

public class GOFBBlockCipher implements BlockCipher {
   private byte[] IV;
   private byte[] ofbV;
   private byte[] ofbOutV;
   private int blockSize;
   private BlockCipher cipher = null;
   private boolean encrypting;
   boolean firstStep = true;
   int C1 = 16843012;
   int C2 = 16843009;
   int N3;
   int N4;

   public GOFBBlockCipher(BlockCipher cipher) {
      this.cipher = cipher;
      this.blockSize = cipher.getBlockSize();
      if (this.blockSize != 8) {
         throw new IllegalArgumentException("GTCR only for 64 bit block ciphers");
      } else {
         this.IV = new byte[cipher.getBlockSize()];
         this.ofbV = new byte[cipher.getBlockSize()];
         this.ofbOutV = new byte[cipher.getBlockSize()];
      }
   }

   public BlockCipher getUnderlyingCipher() {
      return this.cipher;
   }

   public void init(boolean encrypting, CipherParameters params) throws IllegalArgumentException {
      this.encrypting = encrypting;
      this.firstStep = true;

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
      return this.cipher.getAlgorithmName() + "/GCTR";
   }

   public int getBlockSize() {
      return this.blockSize;
   }

   public int processBlock(byte[] in, int inOff, byte[] out, int outOff) throws DataLengthException, IllegalStateException {
      if (inOff + this.blockSize > in.length) {
         throw new DataLengthException("input buffer too short");
      } else if (outOff + this.blockSize > out.length) {
         throw new DataLengthException("output buffer too short");
      } else {
         if (this.firstStep) {
            this.firstStep = false;
            this.cipher.processBlock(this.ofbV, 0, this.ofbOutV, 0);
            this.N3 = this.bytesToint(this.ofbOutV, 0);
            this.N4 = this.bytesToint(this.ofbOutV, 4);
         }

         this.N3 += this.C2;
         this.N4 += this.C1;
         this.intTobytes(this.N3, this.ofbV, 0);
         this.intTobytes(this.N4, this.ofbV, 4);
         this.cipher.processBlock(this.ofbV, 0, this.ofbOutV, 0);

         for(int i = 0; i < this.blockSize; ++i) {
            out[outOff + i] = (byte)(this.ofbOutV[i] ^ in[inOff + i]);
         }

         System.arraycopy(this.ofbV, this.blockSize, this.ofbV, 0, this.ofbV.length - this.blockSize);
         System.arraycopy(this.ofbOutV, 0, this.ofbV, this.ofbV.length - this.blockSize, this.blockSize);
         return this.blockSize;
      }
   }

   public void reset() {
      System.arraycopy(this.IV, 0, this.ofbV, 0, this.IV.length);
      this.cipher.reset();
   }

   private int bytesToint(byte[] in, int inOff) {
      return (in[inOff + 3] << 24 & -16777216) + (in[inOff + 2] << 16 & 16711680) + (in[inOff + 1] << 8 & '\uff00') + (in[inOff] & 255);
   }

   private void intTobytes(int num, byte[] out, int outOff) {
      out[outOff + 3] = (byte)(num >>> 24);
      out[outOff + 2] = (byte)(num >>> 16);
      out[outOff + 1] = (byte)(num >>> 8);
      out[outOff] = (byte)num;
   }
}
