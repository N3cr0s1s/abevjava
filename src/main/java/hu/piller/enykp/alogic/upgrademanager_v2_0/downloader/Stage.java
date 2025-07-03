package hu.piller.enykp.alogic.upgrademanager_v2_0.downloader;

import hu.piller.enykp.alogic.upgrademanager_v2_0.UpgradeBusinessException;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Vector;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public abstract class Stage<In, Out> extends ThreadPoolExecutor {
   protected static final int ADMISSION_CONTROLLER_ID = -1;
   private String stageName;
   protected BlockingQueue<In> inputQueue;
   protected BlockingQueue<Out> outputQueue;
   protected BlockingQueue<In> errorQueue;
   protected Vector<In> cancelled;
   protected Hashtable<Integer, Stage<In, Out>.RegistryEntry> runningTasks;
   protected Thread admissionController;
   private ReentrantLock lock_Seq;
   private int seq = 1000;

   private int getNextSeq() {
      int var1;
      try {
         this.lock_Seq.lock();
         var1 = this.seq++;
      } finally {
         this.lock_Seq.unlock();
      }

      return var1;
   }

   public Stage(String var1, int var2, int var3, boolean var4, boolean var5, boolean var6) {
      super(var2, var3, 1L, TimeUnit.SECONDS, new LinkedBlockingQueue());
      this.stageName = var1;
      if (var4) {
         this.inputQueue = this.initInputQueue();
      }

      if (var5) {
         this.outputQueue = this.initOutputQueue();
      }

      if (var6) {
         this.errorQueue = this.initErrorQueue();
      }

      this.runningTasks = new Hashtable();
      this.admissionController = this.createAdmissionController();
      this.lock_Seq = new ReentrantLock();
   }

   protected BlockingQueue<In> initInputQueue() {
      return new LinkedBlockingQueue();
   }

   protected BlockingQueue<In> initErrorQueue() {
      return new LinkedBlockingQueue();
   }

   protected BlockingQueue<Out> initOutputQueue() {
      return new LinkedBlockingQueue();
   }

   public void addTasks(Collection<In> var1) {
      this.inputQueue.addAll(var1);
   }

   public void startStage() throws UpgradeBusinessException {
      if (this.isAnyQueueNull()) {
         StringBuilder var1 = new StringBuilder();
         var1.append("Stage=").append(this.stageName).append(" queues: ").append("INPUT - ").append(this.inputQueue == null ? "NOT SET" : "OK").append(", ").append("OUTPUT - ").append(this.outputQueue == null ? "NOT SET" : "OK").append(", ").append("ERROR - ").append(this.errorQueue == null ? "NOT SET" : "OK");
         throw new UpgradeBusinessException(var1.toString());
      } else {
         this.admissionController.start();
      }
   }

   private boolean isAnyQueueNull() {
      return this.inputQueue == null || this.outputQueue == null || this.errorQueue == null;
   }

   public void stopStage() {
      if (this.admissionController != null) {
         this.admissionController.interrupt();
      }

      this.shutdownNow();
   }

   public String getStageName() {
      return this.stageName;
   }

   public void setInputQueue(BlockingQueue<In> var1) {
      this.inputQueue = var1;
   }

   public BlockingQueue<In> getInputQueue() {
      return this.inputQueue;
   }

   public void setErrorQueue(BlockingQueue<In> var1) {
      this.errorQueue = var1;
   }

   public BlockingQueue<In> getErrorQueue() {
      return this.errorQueue;
   }

   public void setOutputQueue(BlockingQueue<Out> var1) {
      this.outputQueue = var1;
   }

   public BlockingQueue<Out> getOutputQueue() {
      return this.outputQueue;
   }

   protected abstract Callable<Out> getCallable(In var1);

   protected void onError(In var1, String var2) {
   }

   protected void onSuccess(In var1, Out var2) {
   }

   private Stage<In, Out>.QueueingFuture createFuture(In var1) {
      Callable var2 = this.getCallable(var1);
      return new Stage.QueueingFuture(var2, this.getNextSeq());
   }

   private Thread createAdmissionController() {
      return new Thread(new Runnable() {
         public void run() {
            while(!Stage.this.isTerminating()) {
               try {
                  Object var1 = Stage.this.inputQueue.take();
                  Stage.QueueingFuture var2 = Stage.this.createFuture((In) var1);
                  Stage.RegistryEntry var3 = Stage.this.new RegistryEntry((QueueingFuture) var2, (In) var1);
                  Stage.this.runningTasks.put(var2.getSeq(), var3);
                  Stage.this.execute(var2);
               } catch (InterruptedException var4) {
               }
            }

         }
      });
   }

   protected class RegistryEntry {
      In input;
      Stage<In, Out>.QueueingFuture future;

      RegistryEntry(Stage<In, Out>.QueueingFuture var2, In var3) {
         this.input = var3;
         this.future = var2;
      }

      public Stage<In, Out>.QueueingFuture getQueueingFuture() {
         return this.future;
      }

      public In getInput() {
         return this.input;
      }
   }

   private class QueueingFuture extends FutureTask<Out> {
      private int seq;

      QueueingFuture(Callable<Out> var2, int var3) {
         super(var2);
         this.seq = var3;
      }

      protected void done() {
         boolean var1 = false;
         String var2 = "";
         if (!Stage.this.runningTasks.containsKey(this.seq)) {
            System.err.println("Érvénytelen kulcs: " + this.seq);
         } else {
            Object var3 = ((Stage.RegistryEntry)Stage.this.runningTasks.get(this.seq)).getInput();
            Object var4 = null;

            try {
               var4 = ((Stage.RegistryEntry)Stage.this.runningTasks.get(this.seq)).getQueueingFuture().get();
            } catch (InterruptedException var11) {
               var1 = true;
               var2 = "megszakítva";
            } catch (ExecutionException var12) {
               Object var6 = var12;
               var1 = true;
               if (var12.getCause() != null && var12.getCause().getMessage() != null) {
                  var6 = var12.getCause();
               }

               var2 = ((Throwable)var6).getMessage() == null ? "nem elvégezhető feladat" : ((Throwable)var6).getMessage();
            } finally {
               Stage.this.runningTasks.remove(this.seq);
               if (var1) {
                  Stage.this.errorQueue.add((In) var3);
                  Stage.this.onError((In) var3, var2);
               } else {
                  Stage.this.outputQueue.add((Out) var4);
                  Stage.this.onSuccess((In) var3, (Out) var4);
               }

            }

         }
      }

      public int getSeq() {
         return this.seq;
      }
   }
}
