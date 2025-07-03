package hu.piller.enykp.interfaces;

import java.awt.Component;
import java.util.Iterator;

public interface IEventLog {
   Integer LEVEL_MESSAGE = new Integer(1024);
   Integer LEVEL_WARNING = new Integer(1025);
   Integer LEVEL_ERROR = new Integer(1026);

   boolean logEvent(Object var1);

   boolean logEvent(Object var1, Integer var2, Component var3, String var4);

   Iterator getIterator();

   void writeLog(Object var1);

   void writeLog(Object var1, Integer var2);

   void writeLog(Object var1, Integer var2, Component var3, String var4);

   void setLoggingOff();

   void setLoggingOn();
}
