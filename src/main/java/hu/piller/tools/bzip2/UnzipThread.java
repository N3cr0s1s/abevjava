package hu.piller.tools.bzip2;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.logging.Logger;

public class UnzipThread implements Runnable {
   CBZip2InputStream bz2is;
   InputStream in;
   OutputStream blob = null;
   OutputStream clob = null;
   Writer clob_w = null;
   private volatile boolean stop = false;

   public UnzipThread(InputStream in, OutputStream blob, Writer clob_w, Logger logger) {
      this.in = in;
      this.blob = blob;
      this.clob_w = clob_w;
   }

   public void run() {
      try {
         int b = this.in.read();
         int z = this.in.read();
         if (b != 66 || z != 90) {
            throw new InflateFailedException();
         }

         this.bz2is = new CBZip2InputStream(this.in);
         byte[] buffer = new byte[8192];
         int count = this.bz2is.read(buffer);
         if ((new String(buffer)).startsWith("<?xml")) {
            int k = 0;
            int total = 0;

            do {
               this.clob_w.write(new String(buffer, 0, count, "ISO-8859-2"));
               ++k;
               total += count;
            } while((count = this.bz2is.read(buffer)) != -1);

            this.clob_w.flush();
         } else {
            do {
               this.blob.write(buffer, 0, count);
            } while((count = this.bz2is.read(buffer)) != -1);

            this.blob.flush();
         }
      } catch (Exception var7) {
         ByteArrayOutputStream baos = new ByteArrayOutputStream();
         PrintWriter pw = new PrintWriter(baos);
         var7.printStackTrace(pw);
         pw.flush();
      }

   }
}
