package hu.piller.enykp.alogic.checkpanel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.SimpleTimeZone;
import javax.swing.JProgressBar;

public class TimerProgressBar extends JProgressBar {
   private long startTime;
   private long lastTime;
   private static Date date = new Date();
   private static SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

   public TimerProgressBar() {
      this.setStartTime();
      sdf.setTimeZone(new SimpleTimeZone(0, "GMT+00"));
   }

   public void setStartTime() {
      this.startTime = System.currentTimeMillis();
      this.lastTime = System.currentTimeMillis();
      date.setTime(0L);
   }

   public void setString(String var1) {
      this.lastTime = System.currentTimeMillis();
      date.setTime(this.lastTime - this.startTime);
      var1 = var1 + "  [" + sdf.format(date) + "]";
      super.setString(var1);
   }
}
