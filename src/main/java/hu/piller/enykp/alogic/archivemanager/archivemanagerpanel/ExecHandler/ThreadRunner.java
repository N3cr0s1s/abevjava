package hu.piller.enykp.alogic.archivemanager.archivemanagerpanel.ExecHandler;

import hu.piller.enykp.alogic.archivemanager.archivemanagerpanel.ErrorHandler;
import hu.piller.enykp.alogic.archivemanager.archivemanagerpanel.IReport;
import hu.piller.enykp.util.base.Tools;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class ThreadRunner implements ActionListener {
   public static final int RESULT_NOTHING = 0;
   public static final int RESULT_DATA_START = 1;
   public static final int RESULT_DATA_END = 2;
   public static final int RESULT_QUESTION = 3;
   public static final int RESULT_END = 4;
   public static final int ACT_START_ITEM = 0;
   public static final int ACT_END_ITEM = 1;
   public static final int ANSWER_END = 0;
   public static final int ANSWER_QUESTION = 1;
   public static final int TIMER_INT = 1;
   boolean test = false;
   Hashtable params;
   Hashtable runParams;
   ErrorHandler err;
   boolean result = false;
   String errmsg = "";
   volatile boolean stopped = false;
   Vector finishedData;
   Object lastData;
   int lastRow;
   volatile int pb = 0;
   ExecControll exec = null;
   Thread execThread = null;
   Hashtable resultData;
   IReport executor = null;
   ProgressPanel pbpanel = null;
   JProgressBar pball = null;
   JProgressBar pbrun = null;
   public static final String PAR_RUNABLE = "runable";
   public static final String PAR_LENGTH = "length";
   private Timer timer;

   public ThreadRunner(ErrorHandler var1) {
      this.err = var1;
      this.createTimer();
   }

   public void initProgressBarAll(JProgressBar var1) {
      this.pball = var1;
   }

   public void initProgressBarRun(JProgressBar var1) {
      this.pbrun = var1;
   }

   public void cancel() throws Exception {
      if (this.execThread != null && this.execThread.isAlive()) {
         this.exec.stop();
      }

      if (!this.finishedData.contains(this.lastData)) {
         try {
            this.exec = (ExecControll)this.runParams.get("runable");
            this.exec.clean(this.lastData);
         } catch (Exception var2) {
            Tools.eLog(var2, 0);
         }
      }

   }

   public Hashtable getResult() {
      return this.resultData;
   }

   public void setRunParams(Hashtable var1) {
      this.runParams = var1;
   }

   public void setParams(Hashtable var1) {
      this.params = var1;
   }

   public IReport getMainReport() {
      return this.executor;
   }

   public void setMainReport(IReport var1) {
      this.executor = var1;
   }

   public void run() {
      try {
         this.stopped = false;
         this.lastRow = -1;
         this.resultData = new Hashtable();
         this.finishedData = new Vector();
         this.exec = (ExecControll)this.runParams.get("runable");
         this.exec.setParams(this.runParams);
         if (this.pball != null) {
            this.pball.setMaximum((Integer)this.runParams.get("length"));
            this.pball.setValue(this.pb = 0);
         }

         if (this.pbrun != null) {
            this.pbrun.setIndeterminate(true);
         }

         this.execThread = new Thread(this.exec);
         this.execThread.start();
         this.startTimer();
      } catch (Exception var2) {
         var2.printStackTrace();
      }

   }

   private void sleep(long var1) {
      try {
         Thread.sleep(var1);
      } catch (InterruptedException var4) {
      }

   }

   private Object[] setResult(Object[] var1) {
      return this.executor.setResult(var1);
   }

   private void createTimer() {
      this.timer = new Timer(1, this);
   }

   private void startTimer() {
      this.timer.start();
   }

   private void stopTimer() {
      this.timer.stop();
   }

   public void actionPerformed(ActionEvent var1) {
      this.stopTimer();
      if (this.test) {
         System.out.println("e.toString() = " + var1.toString());
         System.out.println(SwingUtilities.isEventDispatchThread());
      }

      Object[] var2 = this.exec.getResult();
      if (var2 != null) {
         Object[] var3 = this.setResult(var2);
         if (var3 != null) {
            int var4 = (Integer)var3[0];
            if (var4 == 1) {
               this.exec.setAnswer(var3);
            }

            if (var4 == 0) {
               this.execThread = null;
               this.executor.sendEnd();
               return;
            }
         }
      }

      this.startTimer();
   }
}
