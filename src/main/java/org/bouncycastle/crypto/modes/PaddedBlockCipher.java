package org.bouncycastle.crypto.modes;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.BufferedBlockCipher;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.InvalidCipherTextException;

/** @deprecated */
public class PaddedBlockCipher extends BufferedBlockCipher {
   public PaddedBlockCipher(BlockCipher cipher) {
      this.cipher = cipher;
      this.buf = new byte[cipher.getBlockSize()];
      this.bufOff = 0;
   }

   public int getOutputSize(int len) {
      int total = len + this.bufOff;
      int leftOver = total % this.buf.length;
      if (leftOver == 0) {
         return this.forEncryption ? total + this.buf.length : total;
      } else {
         return total - leftOver + this.buf.length;
      }
   }

   public int getUpdateOutputSize(int len) {
      int total = len + this.bufOff;
      int leftOver = total % this.buf.length;
      return leftOver == 0 ? total - this.buf.length : total - leftOver;
   }

   public int processByte(byte in, byte[] out, int outOff) throws DataLengthException, IllegalStateException {
      int resultLen = 0;
      if (this.bufOff == this.buf.length) {
         resultLen = this.cipher.processBlock(this.buf, 0, out, outOff);
         this.bufOff = 0;
      }

      this.buf[this.bufOff++] = in;
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
            return resultLen;
         }
      }
   }

   public int doFinal(byte[] out, int outOff) throws DataLengthException, IllegalStateException, InvalidCipherTextException {
      int blockSize = this.cipher.getBlockSize();
      int resultLen = 0;
      if (this.forEncryption) {
         if (this.bufOff == blockSize) {
            if (outOff + 2 * blockSize > out.length) {
               throw new DataLengthException("output buffer too short");
            }

            resultLen = this.cipher.processBlock(this.buf, 0, out, outOff);
            this.bufOff = 0;
         }

         for(byte code = (byte)(blockSize - this.bufOff); this.bufOff < blockSize; ++this.bufOff) {
            this.buf[this.bufOff] = code;
         }

         resultLen += this.cipher.processBlock(this.buf, 0, out, outOff + resultLen);
      } else {
         if (this.bufOff != blockSize) {
            throw new DataLengthException("last block incomplete in decryption");
         }

         resultLen = this.cipher.processBlock(this.buf, 0, this.buf, 0);
         this.bufOff = 0;
         int count = this.buf[blockSize - 1] & 255;
         if (count < 0 || count > blockSize) {
            throw new InvalidCipherTextException("pad block corrupted");
         }

         resultLen -= count;
         System.arraycopy(this.buf, 0, out, outOff, resultLen);
      }

      this.reset();
      return resultLen;
   }
}
