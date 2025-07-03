package hu.piller.krtitok.tools.log;

import java.io.FileWriter;
import java.io.IOException;
import java.util.MissingResourceException;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import javax.swing.JTextArea;

public class Logger {
   private FileWriter fw = null;
   private LogFormatter formatter = null;
   private ResourceBundle prb = null;
   private String processId = null;
   private String hostName = null;
   private JTextArea textArea = null;
   private boolean debug = false;

   public Logger() {
      this.formatter = new LogFormatter();
   }

   public Logger(boolean debug) {
      this.debug = debug;
   }

   public void setTextArea(JTextArea textArea) {
      this.textArea = textArea;
   }

   public void setPrefix(String prefix) {
      this.formatter.setLoggingProcessPrefix(prefix);
   }

   public void setCommentEachLine(boolean value) {
      this.formatter.setCommentEachLine(value);
   }

   public void setResource(PropertyResourceBundle prb) {
      this.prb = prb;
   }

   public Logger(String logFileName) {
      try {
         this.fw = new FileWriter(logFileName, true);
         this.formatter = new LogFormatter();
      } catch (SecurityException var3) {
         var3.printStackTrace();
      } catch (IOException var4) {
         var4.printStackTrace();
      }

   }

   protected void finalize() {
      try {
         this.fw.flush();
         this.fw.close();
      } catch (IOException var2) {
         var2.printStackTrace();
      }

   }

   public void debug(String msg, Object[] params) {
      try {
         if (this.prb != null) {
            msg = this.prb.getString(msg);
         }
      } catch (MissingResourceException var4) {
      }

      LogRecord lr = new LogRecord(Level.FINE, msg);
      lr.setParameters(params);
      this.log(lr);
   }

   public void debug(String msg, Object param1) {
      try {
         if (this.prb != null) {
            msg = this.prb.getString(msg);
         }
      } catch (MissingResourceException var5) {
      }

      LogRecord lr = new LogRecord(Level.FINE, msg);
      Object[] params = new Object[]{param1};
      lr.setParameters(params);
      this.log(lr);
   }

   public void debug(String msg) {
      try {
         if (this.prb != null) {
            msg = this.prb.getString(msg);
         }
      } catch (MissingResourceException var3) {
      }

      LogRecord lr = new LogRecord(Level.FINE, msg);
      this.log(lr);
   }

   public void info(String msg, Object[] params) {
      try {
         if (this.prb != null) {
            msg = this.prb.getString(msg);
         }
      } catch (MissingResourceException var4) {
      }

      LogRecord lr = new LogRecord(Level.INFO, msg);
      lr.setParameters(params);
      this.log(lr);
   }

   public void info(String msg, Object param1) {
      try {
         if (this.prb != null) {
            msg = this.prb.getString(msg);
         }
      } catch (MissingResourceException var5) {
      }

      LogRecord lr = new LogRecord(Level.INFO, msg);
      Object[] params = new Object[]{param1};
      lr.setParameters(params);
      this.log(lr);
   }

   public void info(String msg) {
      try {
         if (this.prb != null) {
            msg = this.prb.getString(msg);
         }
      } catch (MissingResourceException var3) {
      }

      LogRecord lr = new LogRecord(Level.INFO, msg);
      this.log(lr);
   }

   public void warning(String msg, Object[] params) {
      try {
         if (this.prb != null) {
            msg = this.prb.getString(msg);
         }
      } catch (MissingResourceException var4) {
      }

      LogRecord lr = new LogRecord(Level.WARNING, msg);
      lr.setParameters(params);
      this.log(lr);
   }

   public void warning(String msg, Object param1) {
      try {
         if (this.prb != null) {
            msg = this.prb.getString(msg);
         }
      } catch (MissingResourceException var5) {
      }

      LogRecord lr = new LogRecord(Level.WARNING, msg);
      Object[] params = new Object[]{param1};
      lr.setParameters(params);
      this.log(lr);
   }

   public void warning(String msg) {
      try {
         if (this.prb != null) {
            msg = this.prb.getString(msg);
         }
      } catch (MissingResourceException var3) {
      }

      LogRecord lr = new LogRecord(Level.WARNING, msg);
      this.log(lr);
   }

   public void severe(String msg, Object[] params) {
      try {
         if (this.prb != null) {
            msg = this.prb.getString(msg);
         }
      } catch (MissingResourceException var4) {
      }

      LogRecord lr = new LogRecord(Level.SEVERE, msg);
      lr.setParameters(params);
      this.log(lr);
   }

   public void severe(String msg, Object param1) {
      try {
         if (this.prb != null) {
            msg = this.prb.getString(msg);
         }
      } catch (MissingResourceException var5) {
      }

      LogRecord lr = new LogRecord(Level.SEVERE, msg);
      Object[] params = new Object[]{param1};
      lr.setParameters(params);
      this.log(lr);
   }

   public void severe(String msg) {
      try {
         if (this.prb != null) {
            msg = this.prb.getString(msg);
         }
      } catch (MissingResourceException var3) {
      }

      LogRecord lr = new LogRecord(Level.SEVERE, msg);
      this.log(lr);
   }

   private synchronized void log(LogRecord lr) {
      try {
         synchronized(this) {
            String msg = this.formatter.format(lr);
            if (this.fw == null) {
               System.out.print(msg);
            } else {
               this.fw.write(msg);
               this.fw.flush();
            }

            if (this.textArea != null) {
               this.textArea.append(msg);
            }

            if (this.debug) {
               System.err.println(msg);
            }
         }
      } catch (IOException var5) {
         var5.printStackTrace();
      }

   }

   public synchronized void debug(Throwable t) {
      LogRecord lr = new LogRecord(Level.FINE, t.toString());
      lr.setThrown(t);
      if (this.debug) {
         this.log(lr);
      }

   }
}
