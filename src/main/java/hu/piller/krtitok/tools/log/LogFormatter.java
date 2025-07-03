package hu.piller.krtitok.tools.log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetAddress;
import java.text.FieldPosition;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.MissingResourceException;
import java.util.StringTokenizer;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class LogFormatter extends Formatter {
   private boolean commentEachLine = false;
   private MessageFormat formatter;
   private SimpleDateFormat sdf = null;
   private String hostName = null;
   private String prefix = null;

   public LogFormatter() {
      this.sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss ");
      this.setHostName();
   }

   public void setHostName() {
      try {
         this.hostName = InetAddress.getLocalHost().getHostName();
      } catch (Exception var2) {
      }

   }

   public void setHostName(String hostName) {
      this.hostName = hostName;
   }

   public void setLoggingProcessPrefix(String prefix) {
      this.prefix = prefix;
   }

   public void setCommentEachLine(boolean value) {
      this.commentEachLine = value;
   }

   public String format(LogRecord logRecord) {
      StringBuffer str = new StringBuffer();
      String msg = null;
      int levelInt = logRecord.getLevel().intValue();
      String levelStr = "";
      if (levelInt == Level.SEVERE.intValue()) {
         levelStr = "ERR";
      } else if (levelInt == Level.WARNING.intValue()) {
         levelStr = "WNG";
      } else if (levelInt == Level.INFO.intValue()) {
         levelStr = "INF";
      } else if (levelInt == Level.CONFIG.intValue()) {
         levelStr = "CNF";
      } else if (levelInt == Level.FINE.intValue()) {
         levelStr = "DBG";
      } else if (levelInt == Level.FINER.intValue()) {
         levelStr = "FNR";
      } else if (levelInt == Level.FINEST.intValue()) {
         levelStr = "FST";
      } else if (levelInt == Level.ALL.intValue()) {
         levelStr = "ALL";
      } else if (levelInt == Level.OFF.intValue()) {
         levelStr = "OFF";
      }

      if (logRecord.getResourceBundle() != null) {
         try {
            msg = logRecord.getResourceBundle().getString(logRecord.getMessage());
            levelStr = levelStr + "_";
         } catch (MissingResourceException var11) {
            msg = logRecord.getMessage();
            levelStr = levelStr + " ";
         }
      } else {
         msg = logRecord.getMessage();
         levelStr = levelStr + " ";
      }

      String linePrefix = this.sdf.format(new Date(logRecord.getMillis())) + levelStr + (this.prefix == null ? "" : this.prefix + " ");
      this.formatter = new MessageFormat(msg);
      this.formatter.format(logRecord.getParameters(), str, (FieldPosition)null);
      if (logRecord.getThrown() != null) {
         try {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            logRecord.getThrown().printStackTrace(pw);
            pw.close();
            str.append("\n\t" + sw.toString());
         } catch (Exception var10) {
         }
      }

      if (!this.commentEachLine) {
         str.insert(0, linePrefix);
         return str.toString() + "\n";
      } else {
         StringTokenizer st = new StringTokenizer(str.toString(), "\n");
         StringBuffer stb = new StringBuffer();
         String logLine = null;

         while(st.hasMoreElements()) {
            logLine = st.nextToken();
            stb.append(linePrefix + logLine + "\n");
         }

         return stb.toString();
      }
   }
}
