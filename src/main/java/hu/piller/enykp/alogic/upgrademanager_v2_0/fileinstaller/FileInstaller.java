package hu.piller.enykp.alogic.upgrademanager_v2_0.fileinstaller;

import hu.piller.enykp.alogic.orghandler.OrgInfo;
import hu.piller.enykp.alogic.upgrademanager_v2_0.Directories;
import hu.piller.enykp.alogic.upgrademanager_v2_0.UpgradeLogger;
import me.necrocore.abevjava.NecroFile;
import me.necrocore.abevjava.NecroFileOutputStream;
import me.necrocore.abevjava.NecroFileWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import javax.swing.SwingUtilities;

public class FileInstaller {
   private File[] files;
   private JFileInstallDialog fileInstallDialog;
   private LinkedList<String> installed = new LinkedList();
   boolean hasNewFramework;

   public void setFiles(File[] var1) {
      this.files = var1;
      this.installed.clear();
   }

   public File[] getFiles() {
      return this.files;
   }

   public void setFileInstallDialog(JFileInstallDialog var1) {
      this.fileInstallDialog = var1;
   }

   public JFileInstallDialog getFileInstallDialog() {
      return this.fileInstallDialog;
   }

   public void install() {
      this.hasNewFramework = false;
      ArrayList var1 = new ArrayList();
      File[] var2 = this.getFiles();
      int var3 = var2.length;

      label331:
      for(int var4 = 0; var4 < var3; ++var4) {
         File var5 = var2[var4];
         if ("abevjava_install.jar".equals(var5.getName())) {
            this.hasNewFramework = true;
            var5.renameTo(new NecroFile(Directories.getUpgradePath() + File.separator + "abevjava_install.jar"));
            this.notifyGUI(var5, FileInstallStatus.SIKER);
         } else {
            JarFile var6 = null;
            var1.clear();

            try {
               String var9;
               try {
                  this.notifyGUI(var5, FileInstallStatus.TELEPITES_ALATT);
                  var6 = new JarFile(var5);
                  Enumeration var7 = var6.entries();

                  while(true) {
                     ZipEntry var32;
                     do {
                        do {
                           do {
                              if (!var7.hasMoreElements()) {
                                 this.notifyGUI(var5, FileInstallStatus.SIKER);
                                 this.installed.addAll(var1);
                                 continue label331;
                              }

                              var32 = (ZipEntry)var7.nextElement();
                           } while(!var32.getName().startsWith("application"));

                           var9 = var32.getName().replaceFirst("application/", "");
                           if (var9.startsWith("segitseg")) {
                              var9 = var9.replaceFirst("segitseg", Directories.getHelpsPath());
                              break;
                           }

                           if (var9.startsWith("nyomtatvanyok")) {
                              var9 = var9.replaceFirst("nyomtatvanyok", Directories.getTemplatesPath());
                              break;
                           }

                           if (var9.startsWith("abev")) {
                              var9 = var9.replaceFirst("abev", Directories.getAbevPath());
                              break;
                           }

                           if (var9.startsWith("eroforrasok")) {
                              var9 = var9.replaceFirst("eroforrasok", Directories.getOrgresourcesPath());
                              break;
                           }
                        } while("".equals(var9));
                     } while(var32.isDirectory());

                     InputStream var10 = var6.getInputStream(var32);
                     File var11 = new NecroFile(var9);
                     var11.getParentFile().mkdirs();
                     FileOutputStream var12 = null;
                     byte[] var14 = new byte[2048];

                     try {
                        var12 = new NecroFileOutputStream(var11);

                        int var13;
                        while((var13 = var10.read(var14)) != -1) {
                           var12.write(var14, 0, var13);
                        }

                        var12.flush();
                        var1.add(var11.getAbsolutePath());
                     } finally {
                        if (var12 != null) {
                           var12.close();
                        }

                        var10.close();
                     }
                  }
               } catch (Exception var30) {
                  UpgradeLogger.getInstance().log(var30);
                  this.notifyGUI(var5, FileInstallStatus.HIBA);
                  Iterator var8 = var1.iterator();

                  while(var8.hasNext()) {
                     var9 = (String)var8.next();
                     (new NecroFile(var9)).delete();
                  }
               }
            } finally {
               try {
                  if (var6 != null) {
                     var6.close();
                  }
               } catch (IOException var28) {
                  var28.printStackTrace();
               }

            }
         }
      }

      this.writeUninstallLog();
      OrgInfo.getInstance().mountDir(OrgInfo.getInstance().getResourcePath());
      this.notifyGUI((File)null, (FileInstallStatus)null);
   }

   private void notifyGUI(final File var1, final FileInstallStatus var2) {
      if (this.fileInstallDialog != null) {
         if (var1 == null && var2 == null) {
            this.fileInstallDialog.endOfInstall(this.hasNewFramework);
            return;
         }

         SwingUtilities.invokeLater(new Runnable() {
            public void run() {
               FileInstaller.this.fileInstallDialog.setStatus(var1, var2);
            }
         });
      }

   }

   private void writeUninstallLog() {
      PrintWriter var1 = null;

      try {
         var1 = new PrintWriter(new NecroFileWriter(this.getUninstallPath(), true));
         String var2 = null;

         while((var2 = (String)this.installed.poll()) != null) {
            var1.format("%1$s\n", var2);
            var1.flush();
         }
      } catch (IOException var6) {
         var6.printStackTrace();
      } finally {
         if (var1 != null) {
            var1.close();
         }

      }

   }

   private String getUninstallPath() {
      return Directories.getAbevrootPath() + "/uninstall.enyk";
   }
}
