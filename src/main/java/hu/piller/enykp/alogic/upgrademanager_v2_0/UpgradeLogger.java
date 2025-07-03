package hu.piller.enykp.alogic.upgrademanager_v2_0;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.FileHandler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class UpgradeLogger {
   private static final int TEN_MB = 10485760;
   private static final int NUM_ROTATING_LOGS = 25;
   private static final String SEPARATOR = "|";
   private static UpgradeLogger instance = new UpgradeLogger();
   private ReentrantLock lock = new ReentrantLock();
   private BlockingQueue<String> messages = new LinkedBlockingQueue();

   public static UpgradeLogger getInstance() {
      return instance;
   }

   public void log(String var1) {
      this.lock.lock();

      try {
         if (var1 != null) {
            try {
               this.messages.put(var1);
            } catch (InterruptedException var6) {
               var6.printStackTrace();
            }
         }
      } finally {
         this.lock.unlock();
      }

   }

   public void log(Exception var1) {
      this.lock.lock();

      try {
         if (var1 != null) {
            StringWriter var2 = new StringWriter();
            var1.printStackTrace(new PrintWriter(var2));

            try {
               this.messages.put(var2.toString());
            } catch (InterruptedException var7) {
               var7.printStackTrace();
            }
         }
      } finally {
         this.lock.unlock();
      }

   }

   public void log(String var1, Exception var2) {
      this.lock.lock();

      try {
         if (var1 == null) {
            this.log(var2);
         } else if (var2 == null) {
            this.log(var1);
         } else {
            StringBuffer var3 = (new StringBuffer(var1)).append("\n");
            StringWriter var4 = new StringWriter();
            var2.printStackTrace(new PrintWriter(var4));
            var3.append(var4.toString()).append("\n");

            try {
               this.messages.put(var3.toString());
            } catch (InterruptedException var9) {
               var9.printStackTrace();
            }
         }
      } finally {
         this.lock.unlock();
      }

   }

   public void log(String var1, String var2) {
      this.lock.lock();

      try {
         if (var1 == null) {
            this.log(var2);
         } else if (var2 == null) {
            this.log(var1);
         } else {
            StringBuffer var3 = (new StringBuffer(var1)).append("|").append(var2);

            try {
               this.messages.put(var3.toString());
            } catch (InterruptedException var8) {
               var8.printStackTrace();
            }
         }
      } finally {
         this.lock.unlock();
      }

   }

   private UpgradeLogger() {
      (new Thread(() -> {
         String var1 = this.getLogPath();
         System.out.println("frissités napló könyvtár : " + var1);
         Logger var2 = this.setUpFileLogger(var1);

         while(true) {
            while(true) {
               try {
                  String var3 = (String)this.messages.take();
                  if (var2 != null) {
                     var2.info(var3);
                  }
               } catch (InterruptedException var4) {
                  var4.printStackTrace();
               }
            }
         }
      }, "UPG_LOG")).start();
   }

   private String getLogPath() {
      String var1 = System.getProperty("log.path");
      if (var1 == null) {
         var1 = Directories.getNaploPath();
      }

      return var1;
   }

   private Logger setUpFileLogger(String var1) {
      Logger var3 = null;

      FileHandler var2;
      try {
         var2 = new FileHandler(var1 + File.separator + "upgrade.log", 10485760, 25, true);
         var2.setFormatter(new SimpleFormatter() {
            private static final String format = "[%1$tF %1$tT] [%2$-7s] %3$s %n";

            public synchronized String format(LogRecord var1) {
               return String.format("[%1$tF %1$tT] [%2$-7s] %3$s %n", new Date(var1.getMillis()), var1.getLevel().getLocalizedName(), var1.getMessage());
            }
         });
      } catch (IOException var5) {
         System.out.println("A frissítés napló inicializálása sikertelen : " + var5.getMessage());
         var2 = null;
      }

      if (var2 != null) {
         var3 = Logger.getLogger(UpgradeLogger.class.getName());
         var3.setUseParentHandlers(false);
         var3.addHandler(var2);
      }

      return var3;
   }
}
