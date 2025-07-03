package hu.piller.enykp.alogic.ebev;

import hu.piller.enykp.gui.model.BookModel;
import hu.piller.enykp.interfaces.IErrorList;
import hu.piller.enykp.util.base.ErrorList;
import hu.piller.enykp.util.base.Result;
import hu.piller.enykp.util.base.Tools;
import hu.piller.kripto.keys.KeyWrapper;
import hu.piller.kripto.keys.StoreManager;
import hu.piller.kripto.keys.StoreWrapper;
import hu.piller.tools.Utils;
import hu.piller.tools.bzip2.DeflatorThread;
import hu.piller.xml.abev.BoritekBuilder;
import hu.piller.xml.abev.element.DocMetaData;
import me.necrocore.abevjava.NecroFile;
import me.necrocore.abevjava.NecroFileOutputStream;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.Properties;
import java.util.Vector;

public class Enigma {
   private BookModel bookModel;
   private String formId;
   private String orgId;

   public Enigma(BookModel var1) {
      this.bookModel = var1;
      this.formId = var1.id;
      this.orgId = (String)var1.docinfo.get("org");
   }

   public void setFormName(String var1) {
      this.formId = var1;
   }

   public Result doEncrypt(String var1, String var2, Vector var3, int var4) {
      return this.doEncrypt(var1, var2, var3, var4, true);
   }

   public Result doEncrypt(String var1, String var2, Vector var3, int var4, boolean var5) {
      Result var6 = new Result();
      if (Tools.checkIf2PathsEqual(var1, var2)) {
         var6.setOk(false);
         var6.errorList.add("Hibás program paraméterek! A 'digitális aláírás könyvtára' nem lehet azonos a 'küldendő fájlok helye' könyvtárral!\nA 'digitális aláírás könyvtára'-t a 'Beállítások' menüpontban a 'Működés' fülön tudja megváltoztatni.");
         return var6;
      } else if (this.formId == null) {
         var6.setOk(false);
         var6.errorList.add("Nem található a formazonosító");
         return var6;
      } else if (this.orgId == null) {
         var6.setOk(false);
         var6.errorList.add("Nem található a szervezet azonosító");
         return var6;
      } else {
         try {
            var6 = this.getKeyWrappers();
            if (!var6.isOk()) {
               return var6;
            }

            KeyWrapper[] var7 = (KeyWrapper[])((KeyWrapper[])var6.errorList.remove(0));
            String var8 = var1.lastIndexOf(File.separator) > -1 ? var1.substring(var1.lastIndexOf(File.separator) + 1) : var1;
            DocMetaData var9 = EbevTools.getDMD(this.bookModel, this.formId, this.orgId, var8, var3, var4, var5);
            this.encrypt(var9, var1, var2, var7, true, var6);
         } catch (Exception var10) {
            var6.setOk(false);
            var6.errorList.add("Nem sikerült a titkosítás!");
         }

         return var6;
      }
   }

   private void hiba(String var1, Exception var2, int var3) {
      System.out.println("hiba : " + var3 + " - " + var1);
      var2.printStackTrace();
   }

   public void encrypt(DocMetaData var1, String var2, String var3, KeyWrapper[] var4, boolean var5, Result var6) {
      Thread var7 = null;
      PipedOutputStream var8 = null;
      PipedInputStream var9 = null;
      FileInputStream var10 = null;
      FileOutputStream var11 = null;

      try {
         BoritekBuilder var12 = new BoritekBuilder();
         if (var4 != null) {
            for(int var13 = 0; var13 < var4.length; ++var13) {
               var12.addRecipient(var4[var13]);
            }
         }

         var12.setMetaData(var1);
         var10 = new FileInputStream(var2);
         if (!var5) {
            var12.setPlainSrc(var10);
         } else {
            var9 = new PipedInputStream();
            var8 = new PipedOutputStream(var9);
            var7 = new Thread(new DeflatorThread(var10, var8));
            var12.setPlainSrc(var9);
         }

         var11 = new NecroFileOutputStream(var3);
         var12.setDest(var11);
         if (var5) {
            var7.start();
         }

         var12.build();
         if (var5) {
            var7.join();
         }

         byte[] var36 = var12.getEncryptedDataHash();
         Utils.replace(var3, "0000000000000000000000000000000000000000".getBytes(), Utils.toHexString(var36).getBytes());
      } catch (Exception var34) {
         ErrorList.getInstance().writeError(new Long(4001L), "Hiba a küldendő csomag elkészítésekor.", var34, (Object)null);
         var6.setOk(false);
         var6.errorList.add("Hiba a titkosítás közben: " + var34.getMessage());
      } finally {
         try {
            var8.flush();
         } catch (Exception var33) {
            Tools.eLog(var33, 0);
         }

         try {
            var8.close();
         } catch (Exception var32) {
            Tools.eLog(var32, 0);
         }

         try {
            var10.close();
         } catch (Exception var31) {
            Tools.eLog(var31, 0);
         }

         try {
            var11.close();
         } catch (Exception var30) {
            Tools.eLog(var30, 0);
         }

      }

   }

   public Result getKeyWrappers() throws Exception {
      Result var1 = new Result();
      StoreWrapper var2 = this.getUserPKIData();
      KeyWrapper[] var3;
      if (var2 == null) {
         var3 = new KeyWrapper[1];
      } else {
         try {
            KeyWrapper var4 = (KeyWrapper)var2.listKeys().elementAt(0);
            var3 = new KeyWrapper[]{null, var4};
         } catch (Exception var6) {
            ErrorList.getInstance().writeError(new Long(4001L), "Hiba a saját kulcs kezelésekor. A titkosítást csak a szervezet publikus kulcsával végezzük el!", var6, (Object)null);
            var3 = new KeyWrapper[1];
         }
      }

      try {
         byte[] var8 = EbevTools.getCertBytes(this.orgId);
         if (var8 == null) {
            throw new Exception();
         }

         StoreWrapper var5 = StoreManager.loadStore((InputStream)(new ByteArrayInputStream(var8)), (char[])null);
         var3[0] = (KeyWrapper)var5.listKeys().elementAt(0);
      } catch (Exception var7) {
         var1.setOk(false);
         var1.errorList.add("Nem sikerült a titkosító kulcs megszerzése!");
         return var1;
      }

      var1.errorList.insertElementAt(var3, 0);
      return var1;
   }

   private StoreWrapper getUserPKIData() throws Exception {
      String var1 = System.getProperty("user.home") + File.separator + ".krtitok.ini";
      File var2 = new NecroFile(var1);
      if (!var2.exists()) {
         ErrorList.getInstance().writeError(new Long(4001L), "A .krtitok.ini fájl nem létezik. A titkosítást csak a szervezet publikus kulcsával végezzük el!", IErrorList.LEVEL_WARNING, (Exception)null, (Object)null);
         return null;
      } else {
         String var3 = this.getUserPublicKeyFileName(var1);
         if (var3 == null) {
            return null;
         } else {
            var2 = new NecroFile(var3);
            if (!var2.exists()) {
               ErrorList.getInstance().writeError(new Long(4001L), "A .krtitok.ini fájlban megadott publikus kulcsot tartalmazó fájl nem létezik: " + var3, (Exception)null, (Object)null);
               throw new Exception("A .krtitok.ini fájlban megadott publikus kulcsot tartalmazó fájl nem létezik: " + var3);
            } else {
               return StoreManager.loadStore((InputStream)(new FileInputStream(var2)), (char[])null);
            }
         }
      }
   }

   private String getUserPublicKeyFileName(String var1) throws Exception {
      Properties var2 = new Properties();
      var2.load(new FileInputStream(var1));
      boolean var3 = false;
      String var4 = null;
      if (var2.containsKey("SAJAT_NYILVANOS_KULCS")) {
         var4 = var2.getProperty("SAJAT_NYILVANOS_KULCS").replace("\\:", ":");
      }

      if (var2.containsKey("SAJAT_AUTOMATIKUS")) {
         try {
            var3 = new Boolean(var2.getProperty("SAJAT_AUTOMATIKUS"));
            if (var3 && var4 == null) {
               var4 = "";
            }
         } catch (Exception var6) {
            var3 = false;
         }
      }

      return var3 ? var4 : null;
   }
}
