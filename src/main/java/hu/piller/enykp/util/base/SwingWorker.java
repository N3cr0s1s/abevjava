package hu.piller.enykp.util.base;

import javax.swing.SwingUtilities;

public abstract class SwingWorker {
   private Object value;
   private Thread thread;
   private SwingWorker.ThreadVar threadVar;

   protected synchronized Object getValue() {
      return this.value;
   }

   private synchronized void setValue(Object var1) {
      this.value = var1;
   }

   public abstract Object construct();

   public void finished() {
   }

   public void interrupt() {
      Thread var1 = this.threadVar.get();
      if (var1 != null) {
         var1.interrupt();
      }

      this.threadVar.clear();
   }

   public Object get() {
      while(true) {
         Thread var1 = this.threadVar.get();
         if (var1 == null) {
            return this.getValue();
         }

         try {
            var1.join();
         } catch (InterruptedException var3) {
            Thread.currentThread().interrupt();
            return null;
         }
      }
   }

   public SwingWorker() {
      final Runnable var1 = new Runnable() {
         public void run() {
            SwingWorker.this.finished();
         }
      };
      Runnable var2 = new Runnable() {
         public void run() {
            try {
               SwingWorker.this.setValue(SwingWorker.this.construct());
            } finally {
               SwingWorker.this.threadVar.clear();
            }

            SwingUtilities.invokeLater(var1);
         }
      };
      Thread var3 = new Thread(var2);
      this.threadVar = new SwingWorker.ThreadVar(var3);
   }

   public void start() {
      Thread var1 = this.threadVar.get();
      if (var1 != null) {
         var1.start();
      }

   }

   private static class ThreadVar {
      private Thread thread;

      ThreadVar(Thread var1) {
         this.thread = var1;
      }

      synchronized Thread get() {
         return this.thread;
      }

      synchronized void clear() {
         this.thread = null;
      }
   }
}
