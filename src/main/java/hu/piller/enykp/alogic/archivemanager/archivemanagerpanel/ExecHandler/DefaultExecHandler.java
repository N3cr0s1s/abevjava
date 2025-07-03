package hu.piller.enykp.alogic.archivemanager.archivemanagerpanel.ExecHandler;

public abstract class DefaultExecHandler implements ExecControll {
   protected volatile boolean stop = false;
   protected volatile boolean stopped = false;
   protected volatile boolean stopon = false;
   protected volatile boolean interrupted = false;

   public void stop() {
      this.stop = true;
   }

   public boolean isStopped() {
      return this.stopped;
   }

   public void setInterrupted() {
      this.interrupted = true;
   }
}
