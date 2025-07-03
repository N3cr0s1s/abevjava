package hu.piller.enykp.alogic.calculator.matrices.matrixproviderimpl;

import hu.piller.enykp.alogic.calculator.matrices.MREF;
import hu.piller.enykp.alogic.orghandler.OrgInfo;
import hu.piller.enykp.util.base.Version;
import me.necrocore.abevjava.NecroFile;

import java.io.File;
import java.io.FilenameFilter;

public class MatrixProviderOrganization extends MatrixProviderFile {
   protected String getPath(final MREF var1) {
      File[] var2 = (new NecroFile(OrgInfo.getInstance().getResourcePath())).listFiles(new FilenameFilter() {
         public boolean accept(File var1x, String var2) {
            String var3 = var2.substring(0, var1.getScope().length());
            return var3.equals(var1.getScope());
         }
      });
      if (var2.length == 0) {
         return null;
      } else {
         File var3 = var2[0];
         File[] var4 = var2;
         int var5 = var2.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            File var7 = var4[var6];
            String var8 = var3.getName().replaceAll(var1.getScope() + "Resources_", "").replaceAll("\\.jar", "");
            String var9 = var7.getName().replaceAll(var1.getScope() + "Resources_", "").replaceAll("\\.jar", "");
            if ((new Version(var9)).compareTo(new Version(var8)) > 0) {
               var3 = var7;
            }
         }

         return var3.getAbsolutePath();
      }
   }
}
