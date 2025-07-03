package org.bouncycastle.crypto;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class CipherOutputStream extends FilterOutputStream {
   private Cipher c;
   private byte[] oneByte;

   public CipherOutputStream(OutputStream os, Cipher c) {
      super(os);
      this.oneByte = new byte[1];
      this.c = c;
   }

   protected CipherOutputStream(OutputStream os) {
      this(os, new NullCipher());
   }

   public void write(int b) throws IOException {
      this.oneByte[0] = (byte)b;
      byte[] bytes = this.c.update(this.oneByte, 0, 1);
      if (bytes != null) {
         this.out.write(bytes, 0, bytes.length);
      }

   }

   public void write(byte[] b) throws IOException {
      this.write(b, 0, b.length);
   }

   public void write(byte[] b, int off, int len) throws IOException {
      try {
         byte[] bytes = this.c.update(b, off, len);
         if (bytes != null) {
            this.out.write(bytes, 0, bytes.length);
         }
      } catch (IllegalStateException var5) {
         var5.printStackTrace();
      }

   }

   public void flush() throws IOException {
      super.flush();
   }

   public void close() throws IOException {
      this.flush();
      super.close();
   }

   public void doFinal() throws Exception {
      byte[] bytes = this.c.doFinal();
      if (bytes != null) {
         this.out.write(bytes, 0, bytes.length);
      }

      this.flush();
      this.out.flush();
   }
}
