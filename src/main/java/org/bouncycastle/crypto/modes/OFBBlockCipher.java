package org.bouncycastle.crypto.modes;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.params.ParametersWithIV;

public class OFBBlockCipher implements BlockCipher {
   private byte[] IV;
   private byte[] ofbV;
   private byte[] ofbOutV;
   private int blockSize;
   private BlockCipher cipher = null;
   private boolean encrypting;

   public OFBBlockCipher(BlockCipher cipher, int blockSize) {
      this.cipher = cipher;
      this.blockSize = blockSize / 8;
      this.IV = new byte[cipher.getBlockSize()];
      this.ofbV = new byte[cipher.getBlockSize()];
      this.ofbOutV = new byte[cipher.getBlockSize()];
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
      return this.cipher.getAlgorithmName() + "/OFB" + this.blockSize * 8;
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
}
