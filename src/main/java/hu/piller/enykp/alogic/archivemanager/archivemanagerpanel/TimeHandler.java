package hu.piller.enykp.alogic.archivemanager.archivemanagerpanel;

import java.util.Calendar;
import java.util.Date;

public class TimeHandler {
   public String getTimeString() {
      Calendar var9 = Calendar.getInstance();
      var9.setTime(new Date());
      String var1 = String.valueOf(var9.get(1));
      String var2 = String.valueOf(var9.get(2) + 100).substring(1);
      String var3 = String.valueOf(var9.get(5) + 100).substring(1);
      String var4 = String.valueOf(var9.get(11) + 100).substring(1);
      String var5 = String.valueOf(var9.get(12) + 100).substring(1);
      String var6 = String.valueOf(var9.get(13) + 100).substring(1);
      String var7 = String.valueOf(var9.get(14) + 1000).substring(1);
      return var1 + var2 + var3 + var4 + var5 + var6 + var7;
   }
}
