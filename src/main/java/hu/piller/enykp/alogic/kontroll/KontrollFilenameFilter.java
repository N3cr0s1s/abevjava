package hu.piller.enykp.alogic.kontroll;

import java.io.File;
import java.io.FilenameFilter;

public class KontrollFilenameFilter implements FilenameFilter {
   private String extension;

   public KontrollFilenameFilter(String var1) {
      this.extension = var1;
   }

   public boolean accept(File var1, String var2) {
      if ((new File(var1, var2)).isDirectory()) {
         return false;
      } else {
         try {
            return var2.endsWith(this.extension);
         } catch (Exception var4) {
            return false;
         }
      }
   }
}
