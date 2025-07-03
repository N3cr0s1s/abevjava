package org.bouncycastle.crypto;

public class BufferedBlockCipher {
   protected byte[] buf;
   protected int bufOff;
   protected boolean forEncryption;
   protected BlockCipher cipher;
   protected boolean partialBlockOkay;
   protected boolean pgpCFB;

   protected BufferedBlockCipher() {
   }

   public BufferedBlockCipher(BlockCipher cipher) {
      this.cipher = cipher;
      this.buf = new byte[cipher.getBlockSize()];
      this.bufOff = 0;
      String name = cipher.getAlgorithmName();
      int idx = name.indexOf(47) + 1;
      this.pgpCFB = idx > 0 && name.startsWith("PGP", idx);
      if (this.pgpCFB) {
         this.partialBlockOkay = true;
      } else {
         this.partialBlockOkay = idx > 0 && (name.startsWith("CFB", idx) || name.startsWith("OFB", idx) || name.startsWith("OpenPGP", idx) || name.startsWith("SIC", idx) || name.startsWith("GCTR", idx));
      }

   }

   public BlockCipher getUnderlyingCipher() {
      return this.cipher;
   }

   public void init(boolean forEncryption, CipherParameters params) throws IllegalArgumentException {
      this.forEncryption = forEncryption;
      this.reset();
      this.cipher.init(forEncryption, params);
   }

   public int getBlockSize() {
      return this.cipher.getBlockSize();
   }

   public int getUpdateOutputSize(int len) {
      int total = len + this.bufOff;
      int leftOver;
      if (this.pgpCFB) {
         leftOver = total % this.buf.length - (this.cipher.getBlockSize() + 2);
      } else {
         leftOver = total % this.buf.length;
      }

      return total - leftOver;
   }

   public int getOutputSize(int len) {
      int total = len + this.bufOff;
      int leftOver;
      if (this.pgpCFB) {
         leftOver = total % this.buf.length - (this.cipher.getBlockSize() + 2);
      } else {
         leftOver = total % this.buf.length;
         if (leftOver == 0) {
            return total;
         }
      }

      return total - leftOver + this.buf.length;
   }

   public int processByte(byte in, byte[] out, int outOff) throws DataLengthException, IllegalStateException {
      int resultLen = 0;
      this.buf[this.bufOff++] = in;
      if (this.bufOff == this.buf.length) {
         resultLen = this.cipher.processBlock(this.buf, 0, out, outOff);
         this.bufOff = 0;
      }

      return resultLen;
   }

   public int processBytes(byte[] in, int inOff, int len, byte[] out, int outOff) throws DataLengthException, IllegalStateException {
      if (len < 0) {
         throw new IllegalArgumentException("Can't have a negative input length!");
      } else {
         int blockSize = this.getBlockSize();
         int length = this.getUpdateOutputSize(len);
         if (length > 0 && outOff + length > out.length) {
            throw new DataLengthException("output buffer too short");
         } else {
            int resultLen = 0;
            int gapLen = this.buf.length - this.bufOff;
            if (len > gapLen) {
               System.arraycopy(in, inOff, this.buf, this.bufOff, gapLen);
               resultLen += this.cipher.processBlock(this.buf, 0, out, outOff);
               this.bufOff = 0;
               len -= gapLen;

               for(inOff += gapLen; len > this.buf.length; inOff += blockSize) {
                  resultLen += this.cipher.processBlock(in, inOff, out, outOff + resultLen);
                  len -= blockSize;
               }
            }

            System.arraycopy(in, inOff, this.buf, this.bufOff, len);
            this.bufOff += len;
            if (this.bufOff == this.buf.length) {
               resultLen += this.cipher.processBlock(this.buf, 0, out, outOff + resultLen);
               this.bufOff = 0;
            }

            return resultLen;
         }
      }
   }

   public int doFinal(byte[] out, int outOff) throws DataLengthException, IllegalStateException, InvalidCipherTextException {
      int resultLen = 0;
      if (outOff + this.bufOff > out.length) {
         throw new DataLengthException("output buffer too short for doFinal()");
      } else {
         if (this.bufOff != 0 && this.partialBlockOkay) {
            this.cipher.processBlock(this.buf, 0, this.buf, 0);
            resultLen = this.bufOff;
            this.bufOff = 0;
            System.arraycopy(this.buf, 0, out, outOff, resultLen);
         } else if (this.bufOff != 0) {
            throw new DataLengthException("data not block size aligned");
         }

         this.reset();
         return resultLen;
      }
   }

   public void reset() {
      for(int i = 0; i < this.buf.length; ++i) {
         this.buf[i] = 0;
      }

      this.bufOff = 0;
      this.cipher.reset();
   }
}
