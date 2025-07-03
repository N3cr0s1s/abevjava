package org.bouncycastle.crypto;

public class BufferedAsymmetricBlockCipher {
   protected byte[] buf;
   protected int bufOff;
   private boolean forEncryption;
   private AsymmetricBlockCipher cipher;

   public BufferedAsymmetricBlockCipher(AsymmetricBlockCipher cipher) {
      this.cipher = cipher;
   }

   public AsymmetricBlockCipher getUnderlyingCipher() {
      return this.cipher;
   }

   public int getBufferPosition() {
      return this.bufOff;
   }

   public void init(boolean forEncryption, CipherParameters params) {
      this.forEncryption = forEncryption;
      this.reset();
      this.cipher.init(forEncryption, params);
      this.buf = new byte[this.cipher.getInputBlockSize()];
      this.bufOff = 0;
   }

   public int getInputBlockSize() {
      return this.cipher.getInputBlockSize();
   }

   public int getOutputBlockSize() {
      return this.cipher.getOutputBlockSize();
   }

   public void processByte(byte in) {
      if (this.bufOff > this.buf.length) {
         throw new DataLengthException("attempt to process message to long for cipher");
      } else {
         this.buf[this.bufOff++] = in;
      }
   }

   public void processBytes(byte[] in, int inOff, int len) {
      if (len != 0) {
         if (len < 0) {
            throw new IllegalArgumentException("Can't have a negative input length!");
         } else if (this.bufOff + len > this.buf.length) {
            throw new DataLengthException("attempt to process message to long for cipher");
         } else {
            System.arraycopy(in, inOff, this.buf, this.bufOff, len);
            this.bufOff += len;
         }
      }
   }

   public byte[] doFinal() throws InvalidCipherTextException {
      byte[] out = this.cipher.processBlock(this.buf, 0, this.bufOff);
      this.reset();
      return out;
   }

   public void reset() {
      if (this.buf != null) {
         for(int i = 0; i < this.buf.length; ++i) {
            this.buf[0] = 0;
         }
      }

      this.bufOff = 0;
   }
}
