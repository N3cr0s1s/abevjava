package hu.piller.enykp.alogic.upgrademanager_v2_0.fileinstaller;

import hu.piller.enykp.alogic.upgrademanager_v2_0.Directories;
import hu.piller.enykp.alogic.upgrademanager_v2_0.UpgradeLogger;
import hu.piller.enykp.util.base.ErrorList;
import hu.piller.enykp.util.base.Version;
import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Attributes.Name;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

public class JInstallerFileChooser extends JFileChooser {
   public JInstallerFileChooser() {
      super(Directories.getAbevrootPath());
      this.init();
   }

   private void init() {
      this.setDialogTitle("Telepítőcsomagok kiválasztása");
      this.addChoosableFileFilter(new JInstallerFileChooser.InstallerFileFilter());
      this.setMultiSelectionEnabled(true);
      this.setAcceptAllFileFilterUsed(false);
   }

   private boolean isUpgradeForCurrentABEV(File var1) throws IOException {
      String var2 = "hu/";
      Name var3 = new Name("Implementation-Version");
      JarFile var4 = null;

      boolean var6;
      try {
         var4 = new JarFile(var1);
         boolean var5 = var4.getManifest().getAttributes(var2) == null ? false : var4.getManifest().getAttributes(var2).containsKey(var3);
         if (var5) {
            Version var12 = new Version(var4.getManifest().getAttributes(var2).getValue(var3));
            Version var7 = new Version("3.44.0");
            boolean var8;
            if (var7.compareTo(var12) < 0) {
               var8 = true;
               return var8;
            }

            var8 = false;
            return var8;
         }

         var6 = false;
      } finally {
         if (var4 != null) {
            var4.close();
         }

      }

      return var6;
   }

   class InstallerFileFilter extends FileFilter {
      public boolean accept(File var1) {
         return var1.isDirectory() ? true : this.isAnykInstaller(var1);
      }

      public String getDescription() {
         return "ÁNyK telepítőcsomag";
      }

      private boolean isAnykInstaller(File var1) {
         boolean var2 = false;
         String[] var3 = var1.getAbsolutePath().split("\\.");
         if ("jar".equalsIgnoreCase(var3[var3.length - 1])) {
            JarFile var4 = null;

            try {
               if ("abevjava_install.jar".equals(var1.getName())) {
                  if (JInstallerFileChooser.this.isUpgradeForCurrentABEV(var1)) {
                     var2 = true;
                  }
               } else {
                  var4 = new JarFile(var1);
                  Enumeration var5 = var4.entries();

                  while(var5.hasMoreElements()) {
                     JarEntry var6 = (JarEntry)var5.nextElement();
                     if (var6.getName().startsWith("application")) {
                        var2 = true;
                        break;
                     }
                  }
               }
            } catch (Exception var15) {
               UpgradeLogger.getInstance().log("Hiba a telepítő file megnyitasakor: " + var1.getName());
               ErrorList.getInstance().store(ErrorList.LEVEL_SHOW_WARNING, "Telepítési hiba (" + var1.getAbsolutePath() + ")", var15, (Object)null);
            } finally {
               try {
                  if (var4 != null) {
                     var4.close();
                  }
               } catch (IOException var14) {
                  var14.printStackTrace();
               }

            }
         }

         return var2;
      }
   }
}
