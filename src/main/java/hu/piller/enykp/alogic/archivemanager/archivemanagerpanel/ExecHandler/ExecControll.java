package hu.piller.enykp.alogic.archivemanager.archivemanagerpanel.ExecHandler;

import java.util.Hashtable;

public interface ExecControll extends Runnable {
   void setParams(Hashtable var1);

   void stop();

   boolean isStopped();

   void clean(Object var1);

   void setInterrupted();

   Object[] getResult();

   void setAnswer(Object[] var1);
}
