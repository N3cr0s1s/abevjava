package hu.piller.enykp.alogic.archivemanager.archivemanagerpanel;

import hu.piller.enykp.alogic.archivemanager.ArchiveManagerDialogPanel;
import hu.piller.enykp.alogic.archivemanager.archivemanagerpanel.ExecHandler.DefaultExecHandler;
import hu.piller.enykp.alogic.archivemanager.archivemanagerpanel.archivefilepanel.filepanel.ListItem;
import hu.piller.enykp.alogic.ebev.SendParams;
import hu.piller.enykp.util.base.PropertyList;
import hu.piller.enykp.util.base.Tools;
import me.necrocore.abevjava.NecroFile;
import me.necrocore.abevjava.NecroFileOutputStream;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Hashtable;

public class Archiver extends DefaultExecHandler {
   private boolean test = false;
   public static final String ARCHIV = "archiv";
   public static final String RELOAD = "reload";
   public static final String COPY = "copy";
   public static final String USER_STOPPED = "A felhasználó leállította!";
   public static final String FILE_ARCHIVE_ERROR = "Hiba az arhiválás során!";
   public static final String MSG_FILE_OVERWRITE_CONFIRM = "Állomány felülírás jóváhagyása";
   public static final String MSG_FILE_EXISTS = "Az állomány létezik!\n";
   public static final String MSG_OVERWRITE = "\nFelülírja?";
   public static final String BAD_FILE_NAME = "Hibás állománynév: ";
   public static final String FILE_DOESNT_EXISTS = "Nem létezik az állomány: ";
   public static final String PAR_FUNCTION = "function";
   public static final String PAR_SOURCE = "source";
   public static final String PAR_DESTPATH = "destpath";
   public static final String PAR_AMDP = "component";
   public static final String FILE_EXTENSION_SEPARATOR = ".";
   public static final String FILE_ENYK_POSTFIX = "enyk";
   public static final String AHF_EXTENSION = "arh";
   public static final String AhF_SEPARATOR = ".";
   public static final int AhF_INFO_SIZE = 24;
   public static final int MAX_UNIQ_NUMBER = 100000;
   public static final int BUFFER_SIZE = 65536;
   private ErrorHandler errorhandler = null;
   private TimeHandler timeh;
   private Hashtable params;
   private Object[] sources;
   private String destPath;
   private String function;
   private int uniqId;
   private ArchiveManagerDialogPanel amdp;
   public static final int RESULT_SIZE = 6;
   public static final int RESULT_TYPE = 0;
   public static final int RESULT_RES = 1;
   public static final int RESULT_MSG = 2;
   public static final int RESULT_DATA = 3;
   public static final int RESULT_FUNCTION = 4;
   public static final int RESULT_INFO = 5;
   private static transient boolean answerExists;
   private static int MAXCNT = 100;
   private static transient int actcnt = 0;
   private static transient Object[] result;
   private static transient Object[] answer;
   private static boolean fileCopySucces;

   public Archiver(ErrorHandler var1) {
      this.errorhandler = var1;
      this.timeh = new TimeHandler();
   }

   public void setParams(Hashtable var1) {
      this.params = var1;
   }

   public void loadParams(Hashtable var1) {
      this.setFunction((String)this.getHt(var1, "function"));
      this.setSources((Object[])((Object[])this.getHt(var1, "source")));
      this.setDestPath((String)this.getHt(var1, "destpath"));
      this.setAmdp((ArchiveManagerDialogPanel)this.getHt(var1, "component"));
   }

   public void setAmdp(ArchiveManagerDialogPanel var1) {
      this.amdp = var1;
   }

   private Object getHt(Hashtable var1, Object var2) {
      if (var1 == null) {
         return null;
      } else {
         return !var1.containsKey(var2) ? null : var1.get(var2);
      }
   }

   private static String getPath(String var0, String var1) {
      return var0 != null && var0.length() != 0 ? var1 + var0 : "";
   }

   public void setFunction(String var1) {
      this.function = var1;
   }

   public void setSources(Object[] var1) {
      this.sources = var1;
   }

   public void setDestPath(String var1) {
      this.destPath = var1;
   }

   public void clean(Object var1) {
      try {
         if (var1 != null) {
            File var2 = new NecroFile(this.destPath + getPath(((File)((IListItem)var1).getItem()).getPath(), File.separator));
            removeFile(var2);
         }
      } catch (Exception var3) {
         Tools.eLog(var3, 1);
      }

   }

   public static synchronized boolean removeFile(File var0) {
      try {
         return var0.isDirectory() && var0.listFiles().length > 0 ? false : var0.delete();
      } catch (Exception var2) {
         return false;
      }
   }

   private boolean getboolean(Hashtable var1, Object var2) {
      if (var1 == null) {
         return false;
      } else if (!var1.containsKey(var2)) {
         return false;
      } else {
         return ((String)var1.get(var2)).compareTo("true") == 0;
      }
   }

   private Object getTable(Hashtable var1, Object var2) {
      if (var1 == null) {
         return null;
      } else {
         return !var1.containsKey(var2) ? null : var1.get(var2);
      }
   }

   private void readUniqId() {
      File var1 = new NecroFile(this.destPath);
      this.uniqId = var1.listFiles().length + 1;
   }

   public boolean copyFiles(Object[] var1, String var2) throws Exception {
      this.readUniqId();
      Tools.startCopy();

      for(int var6 = 0; var6 < var1.length; ++var6) {
         fileCopySucces = false;
         ListItem var3 = (ListItem)var1[var6];
         File var4 = (File)var3.getItem();
         if (!var4.exists()) {
            this.errorhandler.errAdmin("9065", "Nem létezik az állomány: ", (Exception)null, var4);
            Tools.endCopy();
            throw new Exception("Nem létezik az állomány: " + var4);
         }

         File var5 = this.getDestFile(var4);
         if (var5.exists()) {
            String var7 = "Az állomány létezik!\n" + var5.getPath() + "\nFelülírja?";
            this.inFormQuestion(new Object[]{var7, "Állomány felülírás jóváhagyása"});
            Object[] var8 = this.getAnswer();
            if (var8[0] instanceof Integer) {
               Integer var9 = (Integer)var8[0];
               if (var9 == 1) {
                  Integer var10 = (Integer)var8[1];
                  if (var10 == 1) {
                     continue;
                  }
               }
            }
         }

         try {
            this.copyFile(var4, var5);
            if (var2.compareTo("copy") != 0 && fileCopySucces) {
               boolean var12 = removeFile(var4);
            }

            if (!this.interrupted) {
               this.inFormEnd(var2, true, (String)null, var5.getPath(), 0L, var6, var3);
            }
         } catch (Exception var11) {
            this.errorhandler.errAdmin("9065", "Hiba az arhiválás során!", var11, var5);
            if (var2.compareTo("copy") != 0 && fileCopySucces) {
               removeFile(var4);
               this.inFormEnd(var2, true, (String)null, var5.getPath(), 0L, var6, var3);
            }

            if (!this.interrupted) {
               this.inFormEnd(var2, false, var11.getMessage(), (String)null, 0L, var6, var3);
            }

            if (this.stop) {
               Tools.endCopy();
               throw new Exception("A felhasználó leállította!");
            }
         }

         SendParams var13 = new SendParams(PropertyList.getInstance());
         if (var2.equals("archiv")) {
            Tools.archiveAtc(var4.getAbsolutePath(), this.destPath, var5.getName());
            Tools.archiveDSIG(var13, var4.getAbsolutePath(), this.destPath, var5.getName());
         } else {
            String var14 = PropertyList.getInstance().get("prop.usr.root") + File.separator + PropertyList.getInstance().get("prop.usr.archive") + File.separator;
            Tools.resetDSIG(var13, var4.getName(), var14, true, var4.getName().substring(var5.getName().length() - ".frm.enyk".length()));
            Tools.resetAtc(var4.getName(), var14, true, var4.getName().substring(var5.getName().length() - ".frm.enyk".length()));
         }
      }

      Tools.endCopy();
      return true;
   }

   private File getDestFile(File var1) throws Exception {
      try {
         StringBuffer var2 = new StringBuffer();
         String var3 = getFileNameWithoutExtension(var1);
         String var4 = getFileExtension(var1);
         if (this.function.compareTo("archiv") == 0) {
            var2.append(this.destPath);
            var2.append(File.separator);
            var2.append(var3);
            var2.append(".");
            var2.append(this.timeh.getTimeString());
            var2.append(".");
            var2.append(this.getFileUniqId());
            var2.append(".");
            var2.append(var4);
         } else {
            var2.append(this.destPath);
            var2.append(File.separator);
            var2.append(getOriginalFileName(var1));
         }

         return new NecroFile(var2.toString());
      } catch (Exception var5) {
         throw new Exception("Hibás állománynév: " + var1);
      }
   }

   private String getFileUniqId() {
      return String.valueOf(++this.uniqId + 100000).substring(1);
   }

   public static String getFileNameWithoutExtension(File var0) {
      String var1 = var0.getName();
      if (var1.toLowerCase().endsWith("enyk")) {
         var1 = var1.substring(0, var1.lastIndexOf("."));
      }

      return var1.substring(0, var1.lastIndexOf("."));
   }

   public static String getFileExtension(File var0) {
      String var1 = var0.getName();
      if (var1.toLowerCase().endsWith("enyk")) {
         String var2 = var1.substring(var1.lastIndexOf(".") + 1);
         var1 = var1.substring(0, var1.lastIndexOf("."));
         return var1.substring(var1.lastIndexOf(".") + 1) + "." + var2;
      } else {
         return var1.substring(var1.lastIndexOf(".") + 1);
      }
   }

   public static synchronized String getOriginalFileName(File var0) {
      String var1 = getFileNameWithoutExtension(var0);
      String var2 = getFileExtension(var0);
      String var3 = var1;
      if (var1.length() > 24) {
         var3 = var1.substring(0, var1.length() - 24);
      }

      return var3 + "." + var2;
   }

   public synchronized void copyFile(File var1, File var2) throws Exception {
      fileCopySucces = false;
      if (this.test) {
         System.out.println("sourceFile = " + var1.toString());
         System.out.println("destFile = " + var2.toString());
      }

      BufferedOutputStream var3 = null;
      BufferedInputStream var4 = null;

      try {
         var4 = new BufferedInputStream(new FileInputStream(var1));
         var3 = new BufferedOutputStream(new NecroFileOutputStream(var2));
         byte[] var5 = new byte[65536];

         int var6;
         while((var6 = var4.read(var5, 0, 65536)) != -1) {
            if (this.stop) {
               this.stopon = true;
               throw new Exception("A felhasználó leállította!");
            }

            var3.write(var5, 0, var6);
         }

         var3.flush();
         var3.close();
         var4.close();
         fileCopySucces = true;
      } catch (Exception var9) {
         try {
            if (var4 != null) {
               var4.close();
            }
         } catch (Exception var8) {
            Tools.eLog(var9, 0);
         }

         try {
            if (var3 != null) {
               var3.close();
            }
         } catch (Exception var7) {
            Tools.eLog(var9, 0);
         }

         removeFile(var2);
         throw var9;
      }
   }

   public synchronized void run() {
      String var1 = "";
      Hashtable var2 = new Hashtable();

      try {
         this.loadParams(this.params);
         result = new Object[MAXCNT];
         if (!this.checkPath(this.destPath)) {
            StringBuffer var5 = new StringBuffer();
            if (this.destPath == null) {
               var5.append("Hiányzik a cél elérési út!");
            } else {
               var5.append("A célkönyvtár nem létezik! (");
               var5.append(this.destPath);
               var5.append(")");
            }

            if (!this.interrupted) {
               this.setResult(this.function, false, var5.toString(), var2);
            }

            this.sources = null;
            this.stopped = true;
            return;
         }

         boolean var3 = this.copyFiles(this.sources, this.function);
         if (!this.interrupted) {
            this.setResult(this.function, var3, "", var2);
         }

         this.sources = null;
         this.stopped = true;
         this.sources = null;
      } catch (Exception var4) {
         this.sources = null;
         if (this.stopon) {
            var1 = "A felhasználó leállította!";
         } else {
            var1 = var4.getMessage();
         }

         if (!this.interrupted) {
            this.setResult(this.function, false, "Az archíválás eredménye: " + var1, var2);
         }
      }

   }

   private void sleep(long var1) {
      try {
         Thread.sleep(var1);
      } catch (InterruptedException var4) {
         Tools.eLog(var4, 0);
      }

   }

   private boolean checkPath(String var1) {
      if (var1 == null) {
         return false;
      } else {
         try {
            File var2 = new NecroFile(this.destPath);
            return var2.exists() && var2.isDirectory();
         } catch (Exception var3) {
            return false;
         }
      }
   }

   public synchronized void inFormStart(String var1, String var2, String var3, long var4, int var6, Object var7) {
      while(actcnt == MAXCNT) {
         if (this.test) {
            System.out.println("Várakozik");
         }

         try {
            this.wait();
         } catch (InterruptedException var9) {
            Tools.eLog(var9, 0);
         }
      }

      Object[] var8 = new Object[]{new Integer(1), null, var2, var7, var1, var3};
      this.resultHandler(var8);
      this.notify();
   }

   public synchronized void inFormEnd(String var1, boolean var2, String var3, String var4, long var5, int var7, Object var8) {
      while(actcnt == MAXCNT) {
         if (this.test) {
            System.out.println("Várakozik");
         }

         try {
            this.wait();
         } catch (InterruptedException var11) {
            Tools.eLog(var11, 0);
         }
      }

      ListItem var9 = (ListItem)var8;
      Object[] var10 = new Object[]{new Integer(2), var2, var3, var8, var1, var4};
      this.resultHandler(var10);
      this.notify();
   }

   public synchronized void setResult(String var1, boolean var2, String var3, Hashtable var4) {
      while(actcnt == MAXCNT) {
         if (this.test) {
            System.out.println("Várakozik");
         }

         try {
            this.wait();
         } catch (InterruptedException var6) {
            Tools.eLog(var6, 0);
         }
      }

      Object[] var5 = new Object[]{new Integer(4), var2, var3, null, var1, null};
      this.resultHandler(var5);
      this.notify();
   }

   public synchronized void inFormQuestion(Object[] var1) {
      while(actcnt == MAXCNT) {
         if (this.test) {
            System.out.println("Várakozik");
         }

         try {
            this.wait();
         } catch (InterruptedException var3) {
            Tools.eLog(var3, 0);
         }
      }

      Object[] var2 = new Object[]{new Integer(3), null, null, var1, null, null};
      this.resultHandler(var2);
      this.notify();
   }

   public synchronized Object[] getAnswer() {
      while(!answerExists) {
         if (this.test) {
            System.out.println("Várakozik");
         }

         try {
            this.wait();
         } catch (InterruptedException var2) {
            Tools.eLog(var2, 0);
         }
      }

      Object[] var1 = answer;
      answer = null;
      answerExists = false;
      this.notify();
      return var1;
   }

   private synchronized Object[] resultHandler(Object[] var1) {
      if (var1 == null) {
         if (actcnt > 0) {
            Object[] var2 = result;
            result = new Object[MAXCNT];
            actcnt = 0;
            this.notify();
            return var2;
         } else {
            return null;
         }
      } else {
         result[actcnt++] = var1;
         return null;
      }
   }

   public synchronized Object[] getResult() {
      return this.resultHandler((Object[])null);
   }

   public synchronized void setAnswer(Object[] var1) {
      answer = var1;
      answerExists = true;
      this.notify();
   }
}
