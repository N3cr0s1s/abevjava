package hu.piller.enykp.util.filelist;

import hu.piller.enykp.util.base.Tools;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class defaultFileInfo implements IFileInfo {
   public Object getFileInfo(File var1) {
      Object[] var2 = new Object[2];
      BufferedReader var3 = null;

      try {
         var3 = new BufferedReader(new FileReader(var1));
         String var4 = var3.readLine();
         var2[0] = var4;
         var2[1] = "const";
         var3.close();
         return var2;
      } catch (FileNotFoundException var8) {
         try {
            if (var3 != null) {
               var3.close();
            }
         } catch (IOException var7) {
            Tools.eLog(var8, 0);
         }

         var8.printStackTrace();
      } catch (IOException var9) {
         try {
            if (var3 != null) {
               var3.close();
            }
         } catch (IOException var6) {
            Tools.eLog(var9, 0);
         }

         var9.printStackTrace();
      }

      return null;
   }

   public String getFileInfoId() {
      return "default";
   }

   public Object getFileInfoObject() {
      return this;
   }
}
