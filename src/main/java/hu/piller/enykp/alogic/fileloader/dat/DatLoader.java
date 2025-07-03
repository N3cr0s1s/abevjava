package hu.piller.enykp.alogic.fileloader.dat;

import hu.piller.enykp.alogic.fileutil.TemplateChecker;
import hu.piller.enykp.alogic.metainfo.MetaInfo;
import hu.piller.enykp.datastore.Elem;
import hu.piller.enykp.gui.model.BookModel;
import hu.piller.enykp.gui.model.FormModel;
import hu.piller.enykp.interfaces.IDataStore;
import hu.piller.enykp.interfaces.ILoadManager;
import hu.piller.enykp.util.base.Result;
import me.necrocore.abevjava.NecroFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

public class DatLoader implements ILoadManager {
   private static final String KEY_TS_HEAD = "head";
   private static final String KEY_TS_TYPE = "type";
   private static final String KEY_TS_SAVED = "save_date";
   private static final String KEY_TS_SAVED1 = "saved";
   private static final String KEY_TS_DOC = "doc";
   private static final String KEY_TS_DOCINFO = "docinfo";
   private Hashtable fejTabla = new Hashtable();
   private Hashtable headInfoTabla = new Hashtable();
   private final FilenameFilter file_filter = new DatLoader.DatFileFilter();

   public BookModel load(String var1, String var2, String var3, String var4, BookModel var5) {
      return this.load(var1, var2, var3, var4);
   }

   public BookModel load(String var1, String var2, String var3, String var4) {
      try {
         if (!var1.toLowerCase().endsWith(".dat") && !var1.toLowerCase().endsWith(".abv") && !var1.toLowerCase().endsWith(".elk") && !var1.toLowerCase().endsWith(".kat")) {
            throw new Exception("Nem megfelelő fájl kiterjesztés");
         } else {
            Result var5 = this.loadDat(var1, var2, var3, var4, false, false);
            if (var5.isOk()) {
               return (BookModel)var5.errorList.get(0);
            } else {
               throw new Exception(var5.errorList.get(0).toString());
            }
         }
      } catch (Exception var7) {
         BookModel var6 = new BookModel();
         var6.errormsg = var7.getMessage();
         var6.hasError = true;
         return var6;
      }
   }

   public String[] getFileList(String var1) {
      File var2;
      try {
         var2 = new NecroFile(var1);
      } catch (Exception var8) {
         try {
            var2 = new NecroFile(new URI(var1));
         } catch (Exception var7) {
            var2 = null;
         }
      }

      String[] var3;
      try {
         var3 = var2.list(this.file_filter);
      } catch (Exception var6) {
         var3 = null;
      }

      return var3 == null ? new String[0] : var3;
   }

   public synchronized boolean isLoadableFile(String var1, String var2) {
      try {
         return var1.toLowerCase().startsWith("file") ? this.isLoadableFile(new NecroFile(new URI(var1)), var2) : this.isLoadableFile(new NecroFile(var1), var2);
      } catch (Exception var4) {
         return false;
      }
   }

   public String getId() {
      return "dat_data_loader_v1";
   }

   public String getDescription() {
      return "Abev állomány";
   }

   public Hashtable getHeadData(File var1) {
      try {
         if (!this.isLoadableFile((File)var1, (String)null)) {
            return new Hashtable();
         } else {
            this.fejTabla.clear();
            this.headInfoTabla.clear();
            Result var2 = this.loadDat(var1.getAbsolutePath(), (String)null, (String)null, (String)null, true, true);
            if (!var2.isOk()) {
               throw new Exception();
            } else {
               return this.dataFromHIT();
            }
         }
      } catch (Exception var3) {
         return new Hashtable();
      }
   }

   public String getFileNameSuffix() {
      return ".dat,.abv,.kat,.elk";
   }

   public String createFileName(String var1) {
      return !var1.toLowerCase().endsWith(".dat") && !var1.toLowerCase().endsWith(".abv") && !var1.toLowerCase().endsWith(".elk") && !var1.toLowerCase().endsWith(".kat") ? var1 + ".dat" : var1;
   }

   private boolean isLoadableFile(File var1, String var2) {
      if (var2 == null) {
         if (!var1.toString().toLowerCase().endsWith(".dat") && !var1.toString().toLowerCase().endsWith(".abv") && !var1.toString().toLowerCase().endsWith(".kat") && !var1.toString().toLowerCase().endsWith(".elk")) {
            return false;
         } else {
            try {
               FileInputStream var9 = new FileInputStream(var1);
               byte[] var10 = new byte[2];
               var9.read(var10);
               String var11 = new String(var10);
               return var11.equals("D4");
            } catch (Exception var7) {
               return false;
            }
         }
      } else if (!var2.toString().toLowerCase().endsWith(".dat") && !var2.toString().toLowerCase().endsWith(".abv") && !var2.toString().toLowerCase().endsWith(".kat") && !var2.toString().toLowerCase().endsWith(".elk")) {
         return false;
      } else {
         try {
            File var3 = new NecroFile(var2);
            if (!var3.exists()) {
               var3 = new NecroFile(var1.toString() + "\\" + var2);
            }

            FileInputStream var4 = new FileInputStream(var3);
            byte[] var5 = new byte[2];
            var4.read(var5);
            String var6 = new String(var5);
            return var6.equals("D4");
         } catch (Exception var8) {
            return false;
         }
      }
   }

   private Result loadDat(String var1, String var2, String var3, String var4, boolean var5, boolean var6) throws IOException {
      Result var9 = new Result();
      var9.setOk(true);
      File var10 = new NecroFile(var1);
      FileInputStream var11 = new FileInputStream(var10);

      try {
         try {
            this.datFejlecOlvasas(var11, var1);
         } catch (Exception var34) {
            var9.setOk(false);
            var9.errorList.add("dat fejléc olvasás hiba");
            var11.close();
            return var9;
         }

         if (var5) {
            return var9;
         }

         String[] var12 = TemplateChecker.getInstance().getTemplateFileNames(var2, var3, var4).getTemplateFileNames();
         String var13 = var12[0];
         File var14 = new NecroFile(var13);
         File var15 = new NecroFile(var1);
         if (!var14.exists()) {
            var9.setOk(false);
            var9.errorList.add(var12[2] + " : " + var13);
            return var9;
         }

         if (!var15.exists()) {
            var9.setOk(false);
            var9.errorList.add("Nem található az adat-fájl: " + var1);
            return var9;
         }

         BookModel var16 = new BookModel(var14, true);
         if (var16.hasError) {
            var9.setOk(false);
            var9.errorList.add("Nem sikerült a sablon-fájl betöltése");
            return var9;
         }

         var16.addForm(var16.get(var2));
         IDataStore var17 = var16.get_datastore();
         int var7 = this.getint(var11);

         int var18;
         for(var18 = 0; var18 < var7; ++var18) {
            int var19 = this.getint(var11);
            String var20 = this.getString(var11);
            Object[] var21 = new Object[]{null, null, Integer.toString(var19)};
            Object var22 = MetaInfo.getInstance().getIds(var21, var2);

            try {
               String var23 = (String)((Object[])((Object[])var22))[0];
               var17.set("0_" + var23, var20);
            } catch (Exception var35) {
               if (var16.errormsg == null) {
                  var16.errormsg = "A sablon nem tartalmazza az adatállományban található (" + var19 + " mezőkódú) mezőt. Ez az adat nem kerül betöltésre. Ellenőrizze a nyomtatványt!;";
               } else {
                  var16.errormsg = var16.errormsg + "A sablon nem tartalmazza az adatállományban található (" + var19 + " mezőkódú) mezőt. Ez az adat nem kerül betöltésre. Ellenőrizze a nyomtatványt!;";
               }
            }
         }

         int var8 = this.getint(var11);

         for(var18 = 0; var18 < var8; ++var18) {
            String var37 = this.getString(var11);
            Elem var38 = (Elem)var16.cc.getActiveObject();
            FormModel var39 = var16.get(var38.getType());
            int var40 = var39.getPageindex(var37);
            int var41 = this.getint(var11);
            int[] var24 = (int[])((int[])var38.getEtc().get("pagecounts"));
            var24[var40] = var41;

            for(int var25 = 0; var25 < var41; ++var25) {
               int var26 = this.getint(var11);

               for(int var27 = 0; var27 < var26; ++var27) {
                  int var28 = this.getint(var11);
                  String var29 = this.getString(var11);
                  Object[] var30 = new Object[]{null, null, Integer.toString(var28)};
                  Object var31 = MetaInfo.getInstance().getIds(var30, var2);

                  try {
                     String var32 = (String)((Object[])((Object[])var31))[0];
                     var17.set(var25 + "_" + var32, var29);
                  } catch (Exception var33) {
                     var16.errormsg = var16.errormsg + "A sablon nem tartalmazza az adatállományban található (" + var28 + " mezőkódú) mezőt. Ez az adat nem kerül betöltésre. Ellenőrizze a nyomtatványt!;";
                  }
               }
            }
         }

         var16.cc.l_megjegyzes = (String)this.fejTabla.get("note");
         var9.errorList.add(var16);
      } catch (Exception var36) {
         var9.setOk(false);
         var9.errorList.removeAllElements();
         var9.errorList.add(var36.toString());
         var11.close();
         return var9;
      }

      var11.close();
      return var9;
   }

   private void datFejlecOlvasas(FileInputStream var1, String var2) throws Exception {
      byte[] var3 = new byte[32];
      this.fejTabla.put("state", "Módosítható");
      this.fejTabla.put("org", "Nem definiált");
      int var4 = var1.read(var3, 0, 27);
      if (var4 == 27) {
         int var5 = var2.lastIndexOf("\\");
         if (var5 == -1) {
            var5 = var2.lastIndexOf("/");
         }

         ++var5;
         this.fejTabla.put("filename", var2.substring(var5));
         String var6 = new String(var3, 0, 2);
         if (!var6.equals("D4")) {
            throw new Exception("Hibás adatfájl formátum");
         } else {
            this.fejTabla.put("fileidstr", var6);
            this.fejTabla.put("ver", new Byte(var3[2]));
            this.fejTabla.put("ver", "");
            int var7 = var3[4] * 256 + var3[3];
            this.fejTabla.put("progver", new String(var3, 5, var7));
            boolean var8 = var3[25] == 1;
            this.fejTabla.put("hasComment", new Boolean(var8));
            this.fejTabla.put("id", this.getString(var1));
            this.fejTabla.put("tax_number", this.getString(var1));
            this.fejTabla.put("person_name", this.getString(var1));
            this.fejTabla.put("from_date", this.getString(var1));
            this.fejTabla.put("to_date", this.getString(var1));
            this.fejTabla.put("save_date", this.getString(var1));
            this.headInfoTabla.put("save_date", this.fejTabla.get("save_date"));
            this.fejTabla.put("saved", this.fejTabla.get("save_date"));
            this.headInfoTabla.put("saved", this.fejTabla.get("save_date"));
            this.fejTabla.put("name", this.getString(var1));
            this.fejTabla.put("account_name", this.getString(var1));
            if (var8) {
               this.fejTabla.put("comment", this.getString(var1));
               this.fejTabla.put("note", this.fejTabla.get("comment"));
            } else {
               this.fejTabla.put("comment", "");
               this.fejTabla.put("note", "");
            }

            this.fejTabla.put("name", this.fejTabla.get("id"));
            this.fejTabla.put("templatever", "");
            this.fejTabla.put("information", "");
            this.headInfoTabla.put("doc", new Vector());
            this.headInfoTabla.put("type", "single");
            this.headInfoTabla.put("head", "");
            this.headInfoTabla.put("docinfo", this.fejTabla);
            String var9 = (String)this.headInfoTabla.get("save_date");
            var9 = var9.replaceAll("\\.", "");
            this.headInfoTabla.put("save_date", var9);
            this.headInfoTabla.put("saved", var9);
         }
      }
   }

   private String getString(InputStream var1) throws IOException {
      int var2 = this.getint(var1);
      byte[] var3 = new byte[var2];
      int var4 = var1.read(var3, 0, var2);
      if (var4 != var2) {
         throw new IOException();
      } else {
         return new String(var3);
      }
   }

   private int getint(InputStream var1) throws IOException {
      int var2 = var1.read();
      if (255 < var2) {
         var2 -= 256;
      }

      int var3 = var1.read();
      if (255 < var3) {
         var3 -= 256;
      }

      return var3 * 256 + var2;
   }

   private Hashtable dataFromHIT() {
      Hashtable var1 = new Hashtable();
      var1.put("doc", new Vector());
      var1.put("type", "single");
      var1.put("head", "");
      var1.put("save_date", this.headInfoTabla.get("save_date"));
      var1.put("saved", this.headInfoTabla.get("saved"));
      Enumeration var2 = ((Hashtable)this.headInfoTabla.get("docinfo")).keys();
      Hashtable var3 = new Hashtable();

      while(var2.hasMoreElements()) {
         Object var4 = var2.nextElement();
         var3.put(var4, ((Hashtable)this.headInfoTabla.get("docinfo")).get(var4));
      }

      var1.put("docinfo", var3);
      return var1;
   }

   private class DatFileFilter implements FilenameFilter {
      private DatFileFilter() {
      }

      public boolean accept(File var1, String var2) {
         return DatLoader.this.isLoadableFile(var1, var2);
      }

      // $FF: synthetic method
      DatFileFilter(Object var2) {
         this();
      }
   }
}
