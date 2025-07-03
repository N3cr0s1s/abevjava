package hu.piller.enykp.util.base;

import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.interfaces.IEventLog;
import hu.piller.enykp.interfaces.IOsHandler;
import hu.piller.enykp.util.base.eventsupport.DefaultEventSupport;
import hu.piller.enykp.util.base.eventsupport.Event;
import hu.piller.enykp.util.base.eventsupport.IEventListener;
import hu.piller.enykp.util.base.eventsupport.IEventSupport;
import hu.piller.enykp.util.oshandler.OsFactory;
import java.awt.Component;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileFilter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.imageio.stream.FileImageOutputStream;
import javax.swing.SwingUtilities;

public class EventLog extends List implements IEventLog, IEventSupport {
   private static final String CHARSET = "UTF-8";
   private static final String DATEFORMAT = "[yyyy.MM.dd] [kk:mm:ss.SSS]";
   private static final String DATEFORMAT_2 = "_yyyyMMdd_kkmmss";
   private static final String DATEFORMAT_2_SUFF = "_enyklog";
   private static final String PATTERN = "_########_######_enyklog";
   private static IEventLog instance;
   boolean loggingOn = true;
   EventLog.SeekableOutputStream sof;
   OutputStreamWriter osw;
   SimpleDateFormat sdf = new SimpleDateFormat("[yyyy.MM.dd] [kk:mm:ss.SSS]");
   SimpleDateFormat sdf2 = new SimpleDateFormat("_yyyyMMdd_kkmmss");
   private DefaultEventSupport des = new DefaultEventSupport(false, 512);
   private static final int EVENTLOG_MAX_SIZE = 500;
   private static IOsHandler os = OsFactory.getOsHandler();
   private static String PID = System.getProperty("pid");
   private static String USER = System.getProperty("user.name");
   private static String EVENT_LOG_FILE_URI;

   public static IEventLog getInstance() throws IOException, UnsupportedEncodingException {
      return getInstance(-1, (String)null);
   }

   public static IEventLog getInstance(int var0, String var1) throws IOException, UnsupportedEncodingException {
      if (instance == null) {
         instance = new EventLog(var0 < 0 ? 500 : var0, var1 == null ? EVENT_LOG_FILE_URI : var1);
      }

      return instance;
   }

   private EventLog(int var1, String var2) throws IOException, UnsupportedEncodingException {
      super(var1);
      System.out.println("EventLogFile:" + var2);
      File var3 = new File(var2);
      this.maintainLogFile(var3);
      this.sof = new EventLog.SeekableOutputStream(var3);
      this.osw = new OutputStreamWriter(this.sof, "UTF-8");
      this.sof.seek(var3.length());
   }

   private void maintainLogFile(File var1) {
      if (var1.exists()) {
         long var2 = var1.length();
         if (var2 > 5242880L) {
            String var4 = var1.getPath();
            String var5 = this.sdf2.format(new Date()) + "_enyklog";
            int var6 = var4.lastIndexOf(46);
            var4 = var4.substring(0, var6) + var5 + var4.substring(var6);
            File var7 = new File(var4);
            var1.renameTo(var7);

            try {
               Thread.sleep(50L);
            } catch (InterruptedException var10) {
            }

            System.out.println(">Log file át lett nevezve: " + var7);
         }
      }

      File var11 = var1.getParentFile();
      File[] var3 = var11.listFiles(new EventLog.FileFilterByLastModificationTime((new Date()).getTime() - 1296000000L));
      if (var3 != null && var3.length > 0) {
         int var12 = 0;

         for(int var13 = var3.length; var12 < var13; ++var12) {
            var3[var12].delete();

            try {
               Thread.sleep(50L);
            } catch (InterruptedException var9) {
            }

            System.out.println(">Log töröltük: " + var3[var12]);
         }
      }

   }

   protected boolean compare(Object var1, Object var2) {
      return false;
   }

   public boolean logEvent(Object var1) {
      boolean var4 = true;
      String var2 = this.sdf.format(new Date());
      String var3 = var2 + " " + (var1 == null ? "" : var1.toString());
      var3 = var3.replaceAll("\n", " ");
      var3 = var3.replaceAll("\r", " ");
      System.out.println(var3);
      if (this.loggingOn) {
         try {
            this.osw.write(var3);
            this.sof.newLine();
            this.osw.flush();
         } catch (IOException var6) {
            var4 = false;
         }
      }

      this.des.fireEvent(this, "update", "logevent");
      return var4;
   }

   public boolean logEvent(Object var1, Integer var2, Component var3, String var4) {
      boolean var5 = this.logEvent(var1);
      if (IEventLog.LEVEL_WARNING.equals(var2)) {
         this.showMessageDialog(var3, var1.toString(), var4 == null ? "Figyelem !" : var4, 2);
      } else if (IEventLog.LEVEL_ERROR.equals(var2)) {
         this.showMessageDialog(var3, var1.toString(), var4 == null ? "Hiba !" : var4, 0);
      } else if (var2 != null) {
         this.showMessageDialog(var3, var1.toString(), var4 == null ? "Üzenet" : var4, 1);
      }

      return var5;
   }

   private void showMessageDialog(final Component var1, final Object var2, final String var3, final int var4) {
      if (SwingUtilities.isEventDispatchThread()) {
         GuiUtil.showMessageDialog(var1, var2, var3, var4);
      } else {
         try {
            SwingUtilities.invokeAndWait(new Runnable() {
               public void run() {
                  GuiUtil.showMessageDialog(var1, var2, var3, var4);
               }
            });
         } catch (Exception var6) {
            var6.printStackTrace();
         }
      }

   }

   public Iterator getIterator() {
      return new EventLog.EventLogIterator();
   }

   public void close() throws IOException {
      this.osw.flush();
      this.osw.close();
   }

   public void addEventListener(IEventListener var1) {
      this.des.addEventListener(var1);
   }

   public void removeEventListener(IEventListener var1) {
      this.des.removeEventListener(var1);
   }

   public Vector fireEvent(Event var1) {
      return this.des.fireEvent(var1);
   }

   public void writeLog(Object var1) {
      this.logEvent(var1);
   }

   public void writeLog(Object var1, Integer var2) {
      this.writeLog(var1, var2, (Component)null, (String)null);
   }

   public void writeLog(Object var1, Integer var2, Component var3, String var4) {
      this.logEvent(var1, var2, var3, var4);
   }

   public void setLoggingOff() {
      this.loggingOn = false;
   }

   public void setLoggingOn() {
      this.loggingOn = true;
   }

   static {
      EVENT_LOG_FILE_URI = os.getUserHomeDir() + File.separator + "abevjava" + (USER == null ? "" : "_" + USER) + (PID == null ? "" : "_" + PID) + ".log";
   }

   private class EventLogIterator implements Iterator {
      private Object[] items;
      private int size;
      private int index;

      private EventLogIterator() {
         this.items = EventLog.super.items();
         this.size = EventLog.super.size();
         this.index = 0;
      }

      public void remove() {
      }

      public boolean hasNext() {
         return this.index < this.size;
      }

      public Object next() {
         return this.items[this.index++];
      }

      // $FF: synthetic method
      EventLogIterator(Object var2) {
         this();
      }
   }

   private static class FileFilterByLastModificationTime implements FileFilter {
      private long time;
      private Pattern file_name_pattern;
      private int pattern_length = "_########_######_enyklog".length();

      FileFilterByLastModificationTime(long var1) {
         this.time = var1;
         String var3 = "";
         int var4 = 0;

         for(int var5 = this.pattern_length; var4 < var5; ++var4) {
            if ("_########_######_enyklog".charAt(var4) == '#') {
               var3 = var3 + "[0-9]";
            } else {
               var3 = var3 + "_########_######_enyklog".charAt(var4);
            }
         }

         this.file_name_pattern = Pattern.compile(var3);
      }

      public boolean accept(File var1) {
         if (var1.isFile() && this.isLogFile(var1.getName())) {
            return var1.lastModified() < this.time;
         } else {
            return false;
         }
      }

      private boolean isLogFile(String var1) {
         int var2 = var1.lastIndexOf(46);
         int var3 = var2 - this.pattern_length;
         if (var3 >= 0) {
            String var4 = var1.substring(var3, var2);
            Matcher var5 = this.file_name_pattern.matcher(var4);
            return var5.matches();
         } else {
            return false;
         }
      }
   }

   private class SeekableOutputStream extends OutputStream {
      private FileImageOutputStream fios;
      private FileChannel channel;
      private FileDescriptor fd;
      private byte[] lineSeparator;

      public SeekableOutputStream(File var2) throws IOException {
         String var3;
         try {
            var3 = System.getProperty("line.separator");
         } catch (Exception var5) {
            var3 = null;
         }

         if (var3 == null) {
            this.lineSeparator = "\n".getBytes();
         } else {
            this.lineSeparator = var3.getBytes();
         }

         this.fios = new FileImageOutputStream(var2);
         this.fd = new FileDescriptor();
      }

      public void write(int var1) throws IOException {
         this.fios.write(var1);
      }

      public void write(byte[] var1) throws IOException {
         this.fios.write(var1, 0, var1.length);
      }

      public void write(byte[] var1, int var2, int var3) throws IOException {
         this.fios.write(var1, var2, var3);
      }

      public void flush() throws IOException {
         this.fios.flush();
      }

      public void close() throws IOException {
         this.fios.close();
      }

      public void seek(long var1) throws IOException {
         this.fios.seek(var1);
      }

      public void newLine() throws IOException {
         this.write(this.lineSeparator);
      }
   }
}
