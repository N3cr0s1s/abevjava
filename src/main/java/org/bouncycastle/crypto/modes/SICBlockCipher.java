package org.bouncycastle.crypto.modes;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.params.ParametersWithIV;

public class SICBlockCipher implements BlockCipher {
   private BlockCipher cipher = null;
   private int blockSize;
   private byte[] IV;
   private byte[] counter;
   private byte[] counterOut;
   private boolean encrypting;

   public SICBlockCipher(BlockCipher c) {
      this.cipher = c;
      this.blockSize = this.cipher.getBlockSize();
      this.IV = new byte[this.blockSize];
      this.counter = new byte[this.blockSize];
      this.counterOut = new byte[this.blockSize];
   }

   public BlockCipher getUnderlyingCipher() {
      return this.cipher;
   }

   public void init(boolean forEncryption, CipherParameters params) throws IllegalArgumentException {
      this.encrypting = forEncryption;
      if (params instanceof ParametersWithIV) {
         ParametersWithIV ivParam = (ParametersWithIV)params;
         byte[] iv = ivParam.getIV();
         System.arraycopy(iv, 0, this.IV, 0, this.IV.length);
         this.reset();
         this.cipher.init(true, ivParam.getParameters());
      }

   }

   public String getAlgorithmName() {
      return this.cipher.getAlgorithmName() + "/SIC";
   }

   public int getBlockSize() {
      return this.cipher.getBlockSize();
   }

   public int processBlock(byte[] in, int inOff, byte[] out, int outOff) throws DataLengthException, IllegalStateException {
      this.cipher.processBlock(this.counter, 0, this.counterOut, 0);

      for(int i = 0; i < this.counterOut.length; ++i) {
         out[outOff + i] = (byte)(this.counterOut[i] ^ in[inOff + i]);
      }

      int carry = 1;

      for(int i = this.counter.length - 1; i >= 0; --i) {
         int x = (this.counter[i] & 255) + carry;
         if (x > 255) {
            carry = 1;
         } else {
            carry = 0;
         }

         this.counter[i] = (byte)x;
      }

      return this.counter.length;
   }

   public void reset() {
      System.arraycopy(this.IV, 0, this.counter, 0, this.counter.length);
      this.cipher.reset();
   }
}
