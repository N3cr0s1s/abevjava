package org.bouncycastle.crypto.modes;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.BufferedBlockCipher;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.InvalidCipherTextException;

public class CTSBlockCipher extends BufferedBlockCipher {
   private int blockSize;

   public CTSBlockCipher(BlockCipher cipher) {
      if (!(cipher instanceof OFBBlockCipher) && !(cipher instanceof CFBBlockCipher)) {
         this.cipher = cipher;
         this.blockSize = cipher.getBlockSize();
         this.buf = new byte[this.blockSize * 2];
         this.bufOff = 0;
      } else {
         throw new IllegalArgumentException("CTSBlockCipher can only accept ECB, or CBC ciphers");
      }
   }

   public int getUpdateOutputSize(int len) {
      int total = len + this.bufOff;
      int leftOver = total % this.buf.length;
      return leftOver == 0 ? total - this.buf.length : total - leftOver;
   }

   public int getOutputSize(int len) {
      return len + this.bufOff;
   }

   public int processByte(byte in, byte[] out, int outOff) throws DataLengthException, IllegalStateException {
      int resultLen = 0;
      if (this.bufOff == this.buf.length) {
         resultLen = this.cipher.processBlock(this.buf, 0, out, outOff);
         System.arraycopy(this.buf, this.blockSize, this.buf, 0, this.blockSize);
         this.bufOff = this.blockSize;
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
               System.arraycopy(this.buf, blockSize, this.buf, 0, blockSize);
               this.bufOff = blockSize;
               len -= gapLen;

               for(inOff += gapLen; len > blockSize; inOff += blockSize) {
                  System.arraycopy(in, inOff, this.buf, this.bufOff, blockSize);
                  resultLen += this.cipher.processBlock(this.buf, 0, out, outOff + resultLen);
                  System.arraycopy(this.buf, blockSize, this.buf, 0, blockSize);
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
      if (this.bufOff + outOff > out.length) {
         throw new DataLengthException("output buffer to small in doFinal");
      } else {
         int blockSize = this.cipher.getBlockSize();
         int len = this.bufOff - blockSize;
         byte[] block = new byte[blockSize];
         int i;
         if (this.forEncryption) {
            this.cipher.processBlock(this.buf, 0, block, 0);
            if (this.bufOff < blockSize) {
               throw new DataLengthException("need at least one block of input for CTS");
            }

            for(i = this.bufOff; i != this.buf.length; ++i) {
               this.buf[i] = block[i - blockSize];
            }

            for(i = blockSize; i != this.bufOff; ++i) {
               byte[] var10000 = this.buf;
               var10000[i] ^= block[i - blockSize];
            }

            if (this.cipher instanceof CBCBlockCipher) {
               BlockCipher c = ((CBCBlockCipher)this.cipher).getUnderlyingCipher();
               c.processBlock(this.buf, blockSize, out, outOff);
            } else {
               this.cipher.processBlock(this.buf, blockSize, out, outOff);
            }

            System.arraycopy(block, 0, out, outOff + blockSize, len);
         } else {
            byte[] lastBlock = new byte[blockSize];
            if (this.cipher instanceof CBCBlockCipher) {
               BlockCipher c = ((CBCBlockCipher)this.cipher).getUnderlyingCipher();
               c.processBlock(this.buf, 0, block, 0);
            } else {
               this.cipher.processBlock(this.buf, 0, block, 0);
            }

            for(int ix = blockSize; ix != this.bufOff; ++ix) {
               lastBlock[ix - blockSize] = (byte)(block[ix - blockSize] ^ this.buf[ix]);
            }

            System.arraycopy(this.buf, blockSize, block, 0, len);
            this.cipher.processBlock(block, 0, out, outOff);
            System.arraycopy(lastBlock, 0, out, outOff + blockSize, len);
         }

         i = this.bufOff;
         this.reset();
         return i;
      }
   }
}
