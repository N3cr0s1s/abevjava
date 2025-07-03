package hu.piller.tools.bzip2;

import java.io.InputStream;
import java.io.OutputStream;

public class InflatorThread implements Runnable {
   InputStream in;
   OutputStream out;
   int BUFFER_SIZE = 512;
   boolean bzFormat = false;

   public InflatorThread(InputStream in, OutputStream out) {
      this.in = in;
      this.out = out;
   }

   public void run() {
      try {
         int b = this.in.read();
         int z = this.in.read();
         this.bzFormat = b == 66 && z == 90;
         if (!this.bzFormat) {
            this.out.write(b);
            this.out.write(z);
            byte[] buf = new byte[this.BUFFER_SIZE];
            boolean var8 = false;

            int count;
            while((count = this.in.read(buf)) != -1) {
               this.out.write(buf, 0, count);
            }
         } else {
            CBZip2InputStream bz2is = new CBZip2InputStream(this.in);
            byte[] buf = new byte[this.BUFFER_SIZE];
            boolean var5 = false;

            int count;
            while((count = bz2is.read(buf)) != -1) {
               this.out.write(buf, 0, count);
            }
         }

         this.out.flush();
         this.out.close();
      } catch (Exception var6) {
         var6.printStackTrace();
      }

   }
}
