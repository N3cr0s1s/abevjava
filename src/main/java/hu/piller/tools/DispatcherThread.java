package hu.piller.tools;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DispatcherThread implements Runnable {
   private InputStream in;
   private OutputStream[] outs;
   private boolean read = true;

   public DispatcherThread(InputStream in, OutputStream[] outs) {
      this.in = in;
      this.outs = outs;
   }

   public void run() {
      try {
         byte[] buffer = new byte[128];

         while(true) {
            int k;
            do {
               if ((k = this.in.read(buffer)) == -1) {
                  return;
               }
            } while(!this.canRead());

            for(int i = 0; i < this.outs.length; ++i) {
               OutputStream out = this.outs[i];
               out.write(buffer, 0, k);
            }
         }
      } catch (IOException var5) {
         var5.printStackTrace();
      }
   }

   private synchronized boolean canRead() {
      if (!this.read) {
         try {
            this.wait();
         } catch (InterruptedException var2) {
            var2.printStackTrace();
         }
      }

      return this.read;
   }

   public synchronized void suspendDispatch() {
      this.read = false;
   }

   public synchronized void restartDispatch() {
      this.read = true;
      this.notify();
   }
}
