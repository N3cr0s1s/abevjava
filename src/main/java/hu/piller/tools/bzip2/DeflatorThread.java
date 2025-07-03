package hu.piller.tools.bzip2;

import java.io.InputStream;
import java.io.OutputStream;

public class DeflatorThread implements Runnable {
   InputStream in;
   OutputStream out;
   int BUFFER_SIZE = 512;

   public DeflatorThread(InputStream in, OutputStream out) {
      this.in = in;
      this.out = out;
   }

   public void run() {
      try {
         this.out.write(66);
         this.out.write(90);
         CBZip2OutputStream os = new CBZip2OutputStream(this.out);
         byte[] buf = new byte[this.BUFFER_SIZE];
         boolean var3 = false;

         int count;
         while((count = this.in.read(buf)) != -1) {
            os.write(buf, 0, count);
         }

         os.flush();
         os.close();
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }
}
