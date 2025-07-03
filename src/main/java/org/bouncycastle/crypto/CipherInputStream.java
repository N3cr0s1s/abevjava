package org.bouncycastle.crypto;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class CipherInputStream extends FilterInputStream {
   private Cipher c;
   private byte[] buf;
   private byte[] inBuf;
   private int bufOff;
   private int maxBuf;
   private boolean finalized;
   private static final int INPUT_BUF_SIZE = 2048;

   public CipherInputStream(InputStream is, Cipher c) {
      super(is);
      this.c = c;
      this.buf = new byte[c.getOutputSize(2048)];
      this.inBuf = new byte[2048];
   }

   protected CipherInputStream(InputStream is) {
      this(is, new NullCipher());
   }

   private int nextChunk() throws IOException {
      int available = super.available();
      if (available <= 0) {
         available = 1;
      }

      if (available > this.inBuf.length) {
         available = super.read(this.inBuf, 0, this.inBuf.length);
      } else {
         available = super.read(this.inBuf, 0, available);
      }

      if (available < 0) {
         if (this.finalized) {
            return -1;
         }

         try {
            this.buf = this.c.doFinal();
         } catch (Exception var4) {
            throw new IOException("error processing stream: " + var4.toString());
         }

         this.bufOff = 0;
         if (this.buf != null) {
            this.maxBuf = this.buf.length;
         } else {
            this.maxBuf = 0;
         }

         this.finalized = true;
         if (this.bufOff == this.maxBuf) {
            return -1;
         }
      } else {
         this.bufOff = 0;

         try {
            this.maxBuf = this.c.update(this.inBuf, 0, available, this.buf, 0);
         } catch (Exception var3) {
            throw new IOException("error processing stream: " + var3.toString());
         }

         if (this.maxBuf == 0) {
            return this.nextChunk();
         }
      }

      return this.maxBuf;
   }

   public int read() throws IOException {
      return this.bufOff == this.maxBuf && this.nextChunk() < 0 ? -1 : this.buf[this.bufOff++] & 255;
   }

   public int read(byte[] b) throws IOException {
      return this.read(b, 0, b.length);
   }

   public int read(byte[] b, int off, int len) throws IOException {
      if (this.bufOff == this.maxBuf && this.nextChunk() < 0) {
         return -1;
      } else {
         int available = this.maxBuf - this.bufOff;
         if (len > available) {
            System.arraycopy(this.buf, this.bufOff, b, off, available);
            this.bufOff = this.maxBuf;
            return available;
         } else {
            System.arraycopy(this.buf, this.bufOff, b, off, len);
            this.bufOff += len;
            return len;
         }
      }
   }

   public long skip(long n) throws IOException {
      if (n <= 0L) {
         return 0L;
      } else {
         int available = this.maxBuf - this.bufOff;
         if (n > (long)available) {
            this.bufOff = this.maxBuf;
            return (long)available;
         } else {
            this.bufOff += (int)n;
            return (long)((int)n);
         }
      }
   }

   public int available() throws IOException {
      return this.maxBuf - this.bufOff;
   }

   public void close() throws IOException {
      super.close();
   }

   public boolean markSupported() {
      return false;
   }
}
