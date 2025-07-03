package hu.piller.enykp.alogic.upgrademanager_v2_0.downloader;

import hu.piller.enykp.alogic.upgrademanager_v2_0.Directories;
import hu.piller.enykp.alogic.upgrademanager_v2_0.UpgradeBusinessException;
import hu.piller.enykp.alogic.upgrademanager_v2_0.UpgradeLogger;
import hu.piller.enykp.alogic.upgrademanager_v2_0.UpgradeTechnicalException;
import hu.piller.enykp.alogic.upgrademanager_v2_0.components.DownloadableComponents;
import hu.piller.enykp.alogic.upgrademanager_v2_0.versiondataconverters.VersionData;
import hu.piller.enykp.util.base.PropertyList;
import hu.piller.enykp.util.base.Tools;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.Stack;
import java.util.Vector;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

public class FileDownloader {
   private static final int NOT_FOUND = -1;
   private DownloadableComponents pipeLine;
   public static final boolean CLEAN_RECURSIVE = true;
   public static final boolean CLEAN_NOTRECURSIVE = false;

   public void setPipeLine(DownloadableComponents var1) {
      this.pipeLine = var1;
   }

   public void install(VersionData var1, String var2, String var3) throws UpgradeBusinessException, UpgradeTechnicalException {
      Stack var4 = new Stack();
      byte[] var5 = new byte[4096];
      FileOutputStream var7 = null;
      File var8 = null;
      JarFile var9 = null;
      Exception var10 = null;
      boolean var11 = false;
      boolean var28 = false;

      String var41;
      UpgradeTechnicalException var42;
      label575: {
         try {
            var28 = true;
            var8 = new File(var2, var1.getFileNameByType("jar"));
            var9 = new JarFile(var8);
            Enumeration var12 = var9.entries();
            int var13 = 0;

            while(true) {
               while(true) {
                  ZipEntry var14;
                  String var15;
                  do {
                     do {
                        if (!var12.hasMoreElements()) {
                           var28 = false;
                           break label575;
                        }

                        if (this.isStopped()) {
                           var28 = false;
                           break label575;
                        }

                        var14 = (ZipEntry)var12.nextElement();
                     } while(!var14.getName().startsWith("application"));

                     var15 = var14.getName().replaceFirst("application/", "");
                     if (var15.startsWith("segitseg")) {
                        var15 = var15.replaceFirst("segitseg", Directories.getHelpsPath());
                        break;
                     }

                     if (var15.startsWith("nyomtatvanyok")) {
                        var15 = var15.replaceFirst("nyomtatvanyok", Directories.getTemplatesPath());
                        break;
                     }

                     if (var15.startsWith("abev")) {
                        var15 = var15.replaceFirst("abev", Directories.getAbevPath());
                        break;
                     }

                     if (var15.startsWith("eroforrasok")) {
                        var15 = var15.replaceFirst("eroforrasok", Directories.getOrgresourcesPath());
                        break;
                     }
                  } while("".equals(var15));

                  InputStream var16 = var9.getInputStream(var14);
                  File var17 = new File(var15);
                  if (var14.isDirectory()) {
                     var4.push(var17);
                  } else {
                     this.validateDestination(var17);

                     try {
                        var7 = new FileOutputStream(var17);

                        int var6;
                        while((var6 = var16.read(var5)) != -1 && !this.isStopped()) {
                           var7.write(var5, 0, var6);
                        }

                        var7.flush();
                     } finally {
                        var4.push(var17);
                        if (var7 != null) {
                           var7.close();
                           var7 = null;
                        }

                        var16.close();
                        DownloadableComponents var10000 = this.pipeLine;
                        StringBuilder var10003 = new StringBuilder();
                        ++var13;
                        var10000.fireComponentProcessedEvent(var1, (byte)99, var10003.append(var13).append(" fájl").toString());
                     }
                  }
               }
            }
         } catch (Exception var39) {
            UpgradeLogger.getInstance().log("Telepítési hiba " + var1.toString(), var39);
            var10 = var39;
            var11 = true;
            var28 = false;
         } finally {
            if (var28) {
               try {
                  if (var9 != null) {
                     var9.close();
                  }
               } catch (IOException var35) {
                  Tools.eLog(var35, 0);
               }

               if (var8 != null) {
                  var8.delete();
               }

               if (this.isStopped() || var11) {
                  String var20 = this.cleanUp(var4);
                  if (!"".equals(var20)) {
                     UpgradeTechnicalException var21 = var11 ? new UpgradeTechnicalException("Telepítési hiba! Kérem törölje az alábbi fájlokat: \n" + var20, var10) : new UpgradeTechnicalException("Telepítési hiba! Kérem törölje az alábbi fájlokat: \n" + var20);
                     throw var21;
                  }

                  if (var11) {
                     throw new UpgradeTechnicalException("Telepítési hiba!", var10);
                  }
               }

            }
         }

         try {
            if (var9 != null) {
               var9.close();
            }
         } catch (IOException var36) {
            Tools.eLog(var36, 0);
         }

         if (var8 != null) {
            var8.delete();
         }

         if (!this.isStopped() && !var11) {
            return;
         }

         var41 = this.cleanUp(var4);
         if (!"".equals(var41)) {
            var42 = var11 ? new UpgradeTechnicalException("Telepítési hiba! Kérem törölje az alábbi fájlokat: \n" + var41, var10) : new UpgradeTechnicalException("Telepítési hiba! Kérem törölje az alábbi fájlokat: \n" + var41);
            throw var42;
         }

         if (var11) {
            throw new UpgradeTechnicalException("Telepítési hiba!", var10);
         }

         return;
      }

      try {
         if (var9 != null) {
            var9.close();
         }
      } catch (IOException var37) {
         Tools.eLog(var37, 0);
      }

      if (var8 != null) {
         var8.delete();
      }

      if (this.isStopped() || var11) {
         var41 = this.cleanUp(var4);
         if (!"".equals(var41)) {
            var42 = var11 ? new UpgradeTechnicalException("Telepítési hiba! Kérem törölje az alábbi fájlokat: \n" + var41, var10) : new UpgradeTechnicalException("Telepítési hiba! Kérem törölje az alábbi fájlokat: \n" + var41);
            throw var42;
         }

         if (var11) {
            throw new UpgradeTechnicalException("Telepítési hiba!", var10);
         }
      }

   }

   public static void writeContent(String var0, String var1, String var2) throws UpgradeTechnicalException {
      FileOutputStream var3 = null;

      try {
         var3 = new FileOutputStream(new File(var1 + "/" + var2));
         var3.write(var0.getBytes("UTF-8"));
      } catch (IOException var15) {
         UpgradeLogger.getInstance().log("A " + (new File(var1 + "/" + var2)).getAbsolutePath() + " -t nem lehet menteni!", (Exception)var15);
         throw new UpgradeTechnicalException("A " + (new File(var1 + "/" + var2)).getAbsolutePath() + " -t nem lehet menteni!");
      } finally {
         try {
            if (var3 != null) {
               var3.flush();
            }
         } catch (IOException var14) {
            Tools.eLog(var14, 0);
         }

         try {
            if (var3 != null) {
               var3.close();
            }
         } catch (IOException var13) {
            Tools.eLog(var13, 0);
         }

      }

   }

   public void download(String var1, VersionData var2, String var3) throws UpgradeTechnicalException, UpgradeBusinessException {
      InputStream var5 = null;
      FileOutputStream var8 = null;
      File var9 = null;
      boolean var10 = false;

      URL var4;
      try {
         var4 = this.getLocationByOpmode(var2, var1);
         var4.openConnection();
      } catch (IOException var26) {
         UpgradeLogger.getInstance().log("A letöltési helyhez nem lehet hozzáférni " + var2.toString(), (Exception)var26);
         throw new UpgradeBusinessException("A letöltési helyhez nem lehet hozzáférni!");
      }

      try {
         var9 = new File(var3 + "/" + var2.getFileNameByType(var1));
         this.validateDestination(var9);

         try {
            var5 = var4.openStream();
            var8 = new FileOutputStream(var9);
            byte[] var6 = new byte[4096];
            int var11 = 0;

            int var7;
            while((var7 = var5.read(var6)) != -1 && !this.isStopped()) {
               var8.write(var6, 0, var7);
               ++var11;
               if (var11 % 4 == 0) {
                  this.pipeLine.fireComponentProcessedEvent(var2, (byte)99, var11 * 4 + " Kb");
               }
            }
         } catch (IOException var27) {
            UpgradeLogger.getInstance().log("Lekérdezési hiba " + var2.toString(), (Exception)var27);
            var10 = true;
            throw new UpgradeBusinessException("Lekérdezési hiba!");
         }
      } finally {
         try {
            if (var5 != null) {
               var5.close();
            }
         } catch (IOException var25) {
            Tools.eLog(var25, 0);
         }

         try {
            if (var8 != null) {
               var8.flush();
            }
         } catch (IOException var24) {
            Tools.eLog(var24, 0);
         }

         try {
            if (var8 != null) {
               var8.close();
            }
         } catch (IOException var23) {
            Tools.eLog(var23, 0);
         }

         if ((this.isStopped() || var10) && var9 != null && var9.exists()) {
            var9.delete();
         }

      }

   }

   public void move(VersionData var1, String var2, String var3) throws UpgradeTechnicalException, UpgradeBusinessException {
      File var4 = new File(var2 + File.separator + var1.getFileNameByType("jar"));
      File var5 = new File(var3 + File.separator + var1.getFileNameByType("jar"));
      if (!var4.renameTo(var5)) {
         throw new UpgradeTechnicalException("Can not move " + var4.getPath() + " to " + var5.getPath());
      }
   }

   private URL getLocationByOpmode(VersionData var1, String var2) throws UpgradeBusinessException {
      URL var3 = null;
      String var4 = null;

      try {
         if ((Vector)PropertyList.getInstance().get("prop.const.mode") != null) {
            var4 = (String)((Vector)PropertyList.getInstance().get("prop.const.mode")).get(0);
         }

         if (var4 != null && "offline".equalsIgnoreCase(var4)) {
            var3 = (new File(Directories.getDownloadPath() + "/" + var1.getFileNameByType(var2))).toURL();
         } else {
            String var5 = var1.getFileNameByTypeAndPath(var2);
            if (var5.charAt(0) == '/') {
               var3 = new URL(var1.getLocation() + var5);
            } else {
               var3 = new URL(var1.getLocation() + "/" + var5);
            }
         }

         return var3;
      } catch (Exception var9) {
         UpgradeLogger.getInstance().log("Nem határozható meg a letöltési hely " + var1.toString(), var9);

         String var6;
         try {
            String var7 = System.getProperty("log.path");
            if (var7 == null) {
               Directories.getNaploPath();
            }

            var6 = "(z) " + var7 + File.separator + "upgrade.log naplófájlban találhatók)!";
         } catch (Exception var8) {
            var6 = "(z) upgrade.log naplófájlban találhatók)!";
         }

         throw new UpgradeBusinessException("Lekérdezési hiba (a hiba technikai részletei a" + var6);
      }
   }

   private void validateDestination(File var1) {
      File var2 = var1.getParentFile();
      var2.mkdirs();
   }

   private boolean isStopped() {
      return Thread.currentThread().isInterrupted();
   }

   private String cleanUp(Stack var1) {
      StringBuffer var2 = new StringBuffer();

      while(!var1.isEmpty()) {
         File var3 = (File)var1.pop();
         if (var3.exists() && !var3.delete()) {
            var2.append(var3.getAbsolutePath());
            var2.append("\n");
         }
      }

      return var2.toString();
   }

   public static void cleanDirectory(String var0, boolean var1) throws UpgradeBusinessException {
      File var2 = new File(var0);
      if (var2.isDirectory()) {
         if (!var2.canRead() || !var2.canWrite()) {
            throw new UpgradeBusinessException("Nincs elegendő jog a könyvtár előkészítéséhez: " + var0);
         }

         File[] var3 = var2.listFiles();

         for(int var4 = 0; var4 < var3.length; ++var4) {
            if (var3[var4].isDirectory() && var1) {
               cleanDirectory(var3[var4].getPath() + File.separator + var3[var4].getName(), var1);
               var3[var4].delete();
            } else {
               var3[var4].delete();
            }
         }
      }

   }
}
