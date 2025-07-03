package hu.piller.enykp.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class TimeStamp {
   public static String getNow() {
      return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
   }
}
