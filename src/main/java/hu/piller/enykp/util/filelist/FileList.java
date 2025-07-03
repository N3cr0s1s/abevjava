package hu.piller.enykp.util.filelist;

import hu.piller.enykp.util.base.PropertyList;
import hu.piller.enykp.util.base.Tools;
import me.necrocore.abevjava.NecroFile;
import me.necrocore.abevjava.NecroFileOutputStream;
import me.necrocore.abevjava.NecroFileWriter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

public class FileList {
   boolean debugOn = true;
   String fileListFilename;
   IFileInfo[] fileInfos;
   String actPath;
   private static String workingDirectory;
   private static final String TYPE_UNKNOWN = "NONE";
   private static final String JAVA_HEADLESS_MODE = "java.awt.headless";
   private static final String JAVA_HEADLESS_TRUE = "true";
   private Boolean headLessMode = null;
   private static String PID = System.getProperty("pid");
   private static String USER = System.getProperty("user.name");
   public static final String KEY_LASTMODDATE = "lastmoddate";
   public static final String KEY_FILELIST = "filelist";
   public static final String OBJECT_STREAM_FORMAT = "OSF-v1.0";
   String[] KEYS_FILEINFO = new String[]{"filetype", "type", "saved", "head", "save_date"};
   public static final String FILE_FORMAT_ERROR = "File format error";
   public static final String EXT_TAG_DOCS = "docs";

   public FileList(String var1, IFileInfo[] var2) {
      this.fileInfos = var2;
      this.fileListFilename = var1;
   }

   private boolean getHeadLessMode() {
      if (this.headLessMode == null) {
         String var1 = System.getProperty("java.awt.headless");
         if (var1 != null && var1.equals("true")) {
            this.headLessMode = Boolean.TRUE;
         } else {
            this.headLessMode = Boolean.FALSE;
         }
      }

      return this.headLessMode;
   }

   private static String getWorkingDirectory() {
      try {
         if (workingDirectory == null) {
            workingDirectory = (new NecroFile((String)PropertyList.getInstance().get("prop.usr.root"), (String)PropertyList.getInstance().get("prop.usr.tmp"))).getPath();
         }

         return workingDirectory;
      } catch (Exception var1) {
         return null;
      }
   }

   public Object[] list(String var1, IFileInfo[] var2) {
      try {
         long var3 = System.currentTimeMillis();
         this.actPath = var1;
         if (var2 == null) {
            return null;
         } else if (this.getFileDir(var1) == null) {
            return null;
         } else {
            this.actPath = this.getFileDir(var1).toString();
            boolean var6 = false;
            boolean var7 = false;
            File var8 = this.getIndexFile(var1, this.fileListFilename);
            Hashtable var5 = this.loadFileList(var8);
            if (var5 == null) {
               var5 = this.createFileList(var1);
               var6 = true;
            }

            if (var5 == null) {
               return null;
            } else {
               if (!var6) {
                  var7 = this.updateFileList(var5, var1);
               }

               Object[] var9 = this.searchByRequest((Hashtable)var5.get("filelist"), var2);
               if (var6 || var7) {
                  this.writeFileList(var8, var5);
               }

               if (this.debugOn) {
                  System.out.println("FileListTime = " + (System.currentTimeMillis() - var3) + "  " + var1);
               }

               return var9;
            }
         }
      } catch (Exception var10) {
         var10.printStackTrace();
         return null;
      }
   }

   private File getIndexFile(String var1, String var2) {
      String var3 = (new NecroFile(var1)).getPath();
      int var4 = var3.indexOf(":");
      if (var4 > -1) {
         var3 = var3.substring(var4 + 1);
      }

      if (var3.startsWith(File.separator)) {
         var3 = var3.substring(1);
      }

      var4 = var3.indexOf(File.separatorChar);
      StringBuffer var5 = new StringBuffer();
      var5.append(var2);
      var5.append('_');
      if (var4 > -1) {
         var5.append(var3.substring(0, var4));
         var5.append('_');
         var5.append(var3.substring(var3.lastIndexOf(File.separatorChar) + 1));
      } else {
         var5.append(var3.substring(0));
      }

      var5.append(USER == null ? "" : "_" + USER);
      var5.append(PID == null ? "" : "_" + PID);
      return new NecroFile(getWorkingDirectory(), var5.toString());
   }

   public void setFileInfos(IFileInfo[] var1) {
      this.fileInfos = var1;
   }

   private File getFileDir(String var1) {
      try {
         File var2 = new NecroFile(var1);
         return var2.exists() && var2.isDirectory() ? var2 : null;
      } catch (Exception var3) {
         return null;
      }
   }

   private Hashtable loadFileList(File var1) {
      Hashtable var2 = null;
      long var3 = System.currentTimeMillis();
      ObjectInputStream var5 = null;

      try {
         var5 = new ObjectInputStream(new FileInputStream(var1));
         var2 = this.readByWalkAlong(var5);
         var5.close();
         if (this.debugOn) {
            System.out.println("ReadHandedObjectStreamTime = " + (System.currentTimeMillis() - var3) + " " + var1.getName());
         }

         return var2;
      } catch (Exception var10) {
         if (var5 != null) {
            try {
               var5.close();
            } catch (IOException var9) {
               Tools.eLog(var10, 0);
            }
         }

         try {
            if (var1.exists()) {
               System.out.println("Indexfile megsérült: " + var1.getAbsolutePath());
               System.out.println("Indexfile törlése " + (var1.delete() ? "sikeres" : "sikertelen"));
            }
         } catch (Exception var8) {
            Tools.eLog(var8, 0);
         }

         return null;
      }
   }

   private boolean writeFileList(File var1, Hashtable var2) {
      ObjectOutputStream var3 = null;

      try {
         long var4 = System.currentTimeMillis();
         var3 = new ObjectOutputStream(new NecroFileOutputStream(var1));
         this.writeByWalkAlong(var2, var3);
         var3.flush();
         var3.close();
         if (this.debugOn) {
            System.out.println("WriteHandedObjectStreamTime = " + (System.currentTimeMillis() - var4));
         }

         return true;
      } catch (Exception var7) {
         System.out.println("FileList.writeFileList");
         var7.printStackTrace();
         if (var3 != null) {
            try {
               var3.close();
            } catch (IOException var6) {
               Tools.eLog(var7, 0);
            }
         }

         var1.delete();
         return false;
      }
   }

   private void writeByWalkAlong(Hashtable var1, ObjectOutputStream var2) throws Exception {
      var2.writeUTF("OSF-v1.0");
      Long var3 = (Long)var1.get("lastmoddate");
      var2.writeLong(var3);
      Hashtable var4 = (Hashtable)var1.get("filelist");
      var2.writeInt(var4.size());
      Iterator var5 = var4.values().iterator();

      while(var5.hasNext()) {
         Object var6 = var5.next();
         FileItem var7 = null;

         try {
            var7 = (FileItem)var6;
            String var8 = var7.getFileName();
            var2.writeUTF(var8);
            String var9 = var7.getType();
            var2.writeUTF(var9);
            Long var10 = var7.getLastModTime();
            var2.writeLong(var10);
            Hashtable var11 = (Hashtable)var7.getFileInfo();
            var2.writeBoolean(var11 != null);
            if (var11 != null) {
               this.defaultFileInfoWrite(var11, var2);
               this.writeDocInfo(var11, var2);
            }
         } catch (Exception var12) {
            System.out.println("error in data = " + (var7 == null ? "" : var7.toString()));
            throw var12;
         }
      }

   }

   private void writeDocInfo(Hashtable var1, ObjectOutputStream var2) throws Exception {
      Hashtable var3 = (Hashtable)var1.get("docinfo");
      if (var3 == null) {
         var2.writeInt(0);
      } else {
         Enumeration var4 = var3.keys();
         boolean var5 = var3.containsKey("docs");
         var2.writeInt(var5 ? var3.size() - 1 : var3.size());

         while(var4.hasMoreElements()) {
            String var6 = (String)var4.nextElement();
            if (!var6.equalsIgnoreCase("docs")) {
               var2.writeUTF(var6);
               if (var6.equalsIgnoreCase("hasComment")) {
                  Boolean var7 = (Boolean)var3.get(var6);
                  var2.writeBoolean(var7);
               } else {
                  String var8 = (String)var3.get(var6);
                  var2.writeUTF(var8);
               }
            }
         }

      }
   }

   private void defaultFileInfoWrite(Hashtable var1, ObjectOutputStream var2) throws Exception {
      String[] var3 = this.KEYS_FILEINFO;
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         String var6 = var3[var5];
         String var7 = (String)var1.get(var6);
         var2.writeUTF(var7 == null ? "" : var7);
      }

   }

   private Hashtable readByWalkAlong(ObjectInputStream var1) throws Exception {
      String var2 = var1.readUTF();
      if (!"OSF-v1.0".equalsIgnoreCase(var2)) {
         throw new Exception("File format error:OSF-v1.0");
      } else {
         Hashtable var3 = new Hashtable(2);
         Long var4 = var1.readLong();
         var3.put("lastmoddate", var4);
         int var5 = var1.readInt();
         Hashtable var6 = new Hashtable(var5);
         var3.put("filelist", var6);

         for(int var7 = 0; var7 < var5; ++var7) {
            String var8 = var1.readUTF();
            String var9 = var1.readUTF();
            Long var10 = var1.readLong();
            Hashtable var11 = null;
            Boolean var12 = var1.readBoolean();
            if (var12) {
               var11 = this.defaultFileInfoReader(var1);
               this.readDocInfo(var11, var1);
            }

            FileItem var13 = new FileItem(var9, var8, var10, var11);
            var6.put(var8, var13);
         }

         return var3;
      }
   }

   private void readDocInfo(Hashtable var1, ObjectInputStream var2) throws Exception {
      int var3 = var2.readInt();
      Hashtable var4 = new Hashtable(var3);
      var1.put("docinfo", var4);

      for(int var5 = 0; var5 < var3; ++var5) {
         String var6 = var2.readUTF();
         if (var6.equalsIgnoreCase("hasComment")) {
            Boolean var7 = var2.readBoolean();
            var4.put(var6, var7);
         } else {
            String var8 = var2.readUTF();
            var4.put(var6, var8);
         }
      }

   }

   private Hashtable defaultFileInfoReader(ObjectInputStream var1) throws Exception {
      Hashtable var2 = new Hashtable(this.KEYS_FILEINFO.length + 1);
      String[] var3 = this.KEYS_FILEINFO;
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         String var6 = var3[var5];
         var2.put(var6, var1.readUTF());
      }

      var2.put("doc", Collections.unmodifiableCollection(new Vector()));
      return var2;
   }

   private void writeTxtFile(File var1, Hashtable var2) {
      File var3 = new NecroFile(var1.getPath() + "_txt");
      PrintWriter var4 = null;
      int var5 = 1;

      try {
         var4 = new PrintWriter(new NecroFileWriter(var3));
         var4.println("lastmoddate:" + var2.get("lastmoddate"));
         Hashtable var6 = (Hashtable)var2.get("filelist");
         Enumeration var7 = var6.keys();

         while(var7.hasMoreElements()) {
            String var8 = (String)var7.nextElement();
            var4.println(var5++ + ":" + var6.get(var8).toString());
         }

         var4.flush();
         var4.close();
      } catch (IOException var9) {
         System.out.println("FileList.writeTxtFile");
         var9.printStackTrace();
         if (var4 != null) {
            var4.close();
         }

         var3.delete();
      }

   }

   private boolean updateFileList(Hashtable var1, String var2) {
      boolean var3 = false;
      var1.put("lastmoddate", this.getLastModified(var2));
      Hashtable var4 = (Hashtable)var1.get("filelist");
      File[] var5 = this.getDirList(var2);
      if (var5 == null) {
         return false;
      } else {
         this.initCheck(var4);

         for(int var6 = 0; var6 < var5.length; ++var6) {
            File var7 = var5[var6];
            String var8 = var7.getName();
            if (var8.toLowerCase().indexOf(this.fileListFilename.toLowerCase()) < 0) {
               if (!var4.containsKey(var8)) {
                  this.addNewFileItem(var4, var7);
                  var3 = true;
               } else {
                  FileItem var9 = (FileItem)var4.get(var8);
                  if (var7.lastModified() == var9.getLastModTime()) {
                     var9.setChecked(true);
                  } else {
                     this.updateFileItem(var4, var7);
                     var3 = true;
                  }
               }
            }
         }

         boolean var10 = this.deleteDirtyItems(var4);
         return var3 || var10;
      }
   }

   private Hashtable createFileList(String var1) {
      File[] var2 = this.getDirList(var1);
      if (var2 == null) {
         return null;
      } else {
         Hashtable var3 = new Hashtable();
         var3.put("lastmoddate", this.getLastModified(var1));
         Hashtable var4 = new Hashtable(var2.length);
         var3.put("filelist", var4);

         for(int var5 = 0; var5 < var2.length; ++var5) {
            File var6 = var2[var5];
            if (var6.getAbsolutePath().toLowerCase().indexOf(this.fileListFilename.toLowerCase()) < 0) {
               this.addNewFileItem(var4, var6);
            }
         }

         return var3;
      }
   }

   private Long getLastModified(String var1) {
      return (new NecroFile(var1)).lastModified();
   }

   private boolean isDirModified(Hashtable var1, String var2) {
      return this.getLastModified(var2) > (Long)var1.get("lastmoddate");
   }

   private File[] getDirList(String var1) {
      File var2 = new NecroFile(var1);
      return var2.exists() && var2.isDirectory() ? var2.listFiles() : null;
   }

   private void initCheck_old(Hashtable var1) {
      Enumeration var2 = var1.keys();

      while(var2.hasMoreElements()) {
         Object var4 = var2.nextElement();
         FileItem var3 = (FileItem)var1.get(var4);
         var3.setChecked(false);
      }

   }

   private void initCheck(Hashtable var1) {
      Iterator var2 = var1.keySet().iterator();

      while(var2.hasNext()) {
         FileItem var3 = (FileItem)var1.get(var2.next());
         var3.setChecked(false);
      }

   }

   private boolean deleteDirtyItems(Hashtable var1) {
      boolean var2 = false;
      Enumeration var3 = var1.keys();

      while(var3.hasMoreElements()) {
         Object var4 = var3.nextElement();
         if (!((FileItem)var1.get(var4)).isChecked()) {
            var1.remove(var4);
            var2 = true;
         }
      }

      return var2;
   }

   private boolean addNewFileItem(Hashtable var1, File var2) {
      String var3 = var2.getName();
      Object[] var4 = this.getFileInfo(var2);
      if (var4 != null) {
         var1.put(var3, new FileItem((String)var4[0], var3, var2.lastModified(), var4[1]));
      } else {
         var1.put(var3, new FileItem("NONE", var3, var2.lastModified(), (Object)null));
      }

      return true;
   }

   private boolean updateFileItem(Hashtable var1, File var2) {
      String var3 = var2.getName();
      FileItem var4 = (FileItem)var1.get(var3);
      Object[] var5 = this.getFileInfo(var2);
      if (var5 != null) {
         var4.update((String)var5[0], var2.lastModified(), var5[1]);
      } else {
         var4.update("NONE", var2.lastModified(), (Object)null);
      }

      return true;
   }

   private Object[] getFileInfo(File var1) {
      for(int var2 = 0; var2 < this.fileInfos.length; ++var2) {
         IFileInfo var3 = this.fileInfos[var2];
         Object var4 = var3.getFileInfo(var1);
         if (var4 != null && ((Hashtable)var4).size() > 0) {
            String var5 = var3.getFileInfoId();
            return new Object[]{var5, var4};
         }
      }

      return null;
   }

   private Object[] searchByRequest(Hashtable var1, IFileInfo[] var2) {
      Vector var3 = new Vector(var1.size());
      Enumeration var4 = var1.keys();

      while(var4.hasMoreElements()) {
         String var5 = (String)var4.nextElement();
         FileItem var6 = (FileItem)var1.get(var5);
         IFileInfo var7 = this.matchType(var2, var6);
         if (var7 != null) {
            var3.add(new Object[]{(new NecroFile(this.actPath, var5)).toString(), var6.getFileInfo(), var7.getFileInfoObject()});
         }
      }

      return var3.toArray();
   }

   private IFileInfo matchType(IFileInfo[] var1, FileItem var2) {
      for(int var3 = 0; var3 < var1.length; ++var3) {
         IFileInfo var4 = var1[var3];
         if (var2.getType().equalsIgnoreCase(var4.getFileInfoId())) {
            return var4;
         }
      }

      return null;
   }

   private void writeError(String var1) {
      System.out.println("message = " + var1);
   }
}
