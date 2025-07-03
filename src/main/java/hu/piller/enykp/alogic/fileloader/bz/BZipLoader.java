package hu.piller.enykp.alogic.fileloader.bz;

import hu.piller.tools.bzip2.CBZip2InputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Hashtable;

public class BZipLoader {
   private FileInputStream fin;

   public Hashtable getUnzippedData(Object var1) {
      Hashtable var2 = new Hashtable();
      if (var1 instanceof File) {
         int var3 = this.isZippedFile((File)var1);
         switch(var3) {
         case -1:
            var2.put("errMsg", "hiba a fájl feldolgozásakor");
         case 0:
         default:
            break;
         case 1:
            var2.put("errMsg", "A megadott név egy könyvtár");
            break;
         case 2:
            var2.put("errMsg", "Híbás kiterjesztés (nem .bz és nem .cst)");
            break;
         case 3:
            var2.put("errMsg", "Hibás fejléc információ");
         }

         if (var3 != 0) {
            return var2;
         }

         try {
            FileInputStream var4 = new FileInputStream((File)var1);
            var2.put("unzippedData", this.loadBzipData(var4));
         } catch (Exception var6) {
            var2.put("errMsg", "hiba a fájl feldolgozásakor: " + var6.toString());
         }
      } else if (var1 instanceof InputStream) {
         try {
            InputStream var7 = (InputStream)var1;
            var7.skip(2L);
            var2.put("zipInputStream", new CBZip2InputStream(var7));
         } catch (Exception var5) {
            var5.printStackTrace();
            var2.put("errMsg", "hiba a fájl feldolgozásakor: " + var5.toString());
         }
      }

      return var2;
   }

   public int isZippedFile(File var1) {
      if (var1.isDirectory()) {
         return 1;
      } else if (!var1.toString().toLowerCase().endsWith(".bz") && !var1.toString().toLowerCase().endsWith(".cst")) {
         return 2;
      } else {
         try {
            FileInputStream var2 = new FileInputStream(var1);
            byte[] var3 = new byte[2];
            var2.read(var3);
            String var4 = new String(var3);
            var2.close();
            return !var4.equalsIgnoreCase("bz") ? 3 : 0;
         } catch (Exception var5) {
            return -1;
         }
      }
   }

   private byte[] loadBzipFile(File var1) throws Exception {
      FileInputStream var2 = new FileInputStream(var1);
      var2.skip(2L);
      ByteArrayOutputStream var3 = new ByteArrayOutputStream();
      InputStreamReader var4 = new InputStreamReader(new CBZip2InputStream(this.fin), "ISO-8859-2");

      int var5;
      while((var5 = var4.read()) > 0) {
         var3.write(var5);
      }

      var4.close();
      var2.close();
      return var3.toByteArray();
   }

   private byte[] loadBzipData(InputStream var1) throws Exception {
      var1.skip(2L);
      ByteArrayOutputStream var2 = new ByteArrayOutputStream();
      InputStreamReader var3 = new InputStreamReader(new CBZip2InputStream(var1), "ISO-8859-2");

      int var4;
      while((var4 = var3.read()) > 0) {
         var2.write(var4);
      }

      var3.close();
      var1.close();
      return var2.toByteArray();
   }
}
