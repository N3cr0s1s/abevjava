package hu.piller.enykp.alogic.kontroll;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Hashtable;

public class GeneratedKontrollFilenameFilter implements FilenameFilter {
   private Hashtable extension;

   public GeneratedKontrollFilenameFilter(Hashtable var1) {
      this.extension = var1;
   }

   public boolean accept(File var1, String var2) {
      if ((new File(var1, var2)).isDirectory()) {
         return false;
      } else {
         try {
            return this.extension.containsKey(var2.substring(var2.lastIndexOf(".") + 1));
         } catch (Exception var4) {
            return false;
         }
      }
   }
}
