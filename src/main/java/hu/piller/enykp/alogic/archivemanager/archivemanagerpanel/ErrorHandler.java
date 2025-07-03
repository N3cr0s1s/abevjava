package hu.piller.enykp.alogic.archivemanager.archivemanagerpanel;

import hu.piller.enykp.interfaces.IErrorList;
import hu.piller.enykp.interfaces.IEventLog;
import hu.piller.enykp.util.base.ErrorList;
import hu.piller.enykp.util.base.EventLog;
import hu.piller.enykp.util.base.Tools;
import java.io.IOException;

public class ErrorHandler {
   IErrorList errorlist = ErrorList.getInstance();
   IEventLog eventlog;
   boolean debugOn = false;

   public ErrorHandler() {
      try {
         this.eventlog = EventLog.getInstance();
      } catch (IOException var2) {
         Tools.eLog(var2, 0);
      }

   }

   public String errAdmin(String var1, String var2, Exception var3, Object var4) {
      String var5;
      if (this.debugOn) {
         var5 = "(" + var1 + ") " + var2 + "  " + (var4 == null ? var3.toString() : var4.toString());
      } else {
         var5 = "(" + var1 + ") " + var2 + "  " + (var4 == null ? "" : var4.toString());
      }

      this.errorlist.store(var1, var2, var3, var4);
      this.eventlog.logEvent(var5);
      return var5;
   }
}
