package hu.piller.enykp.alogic.masterdata.sync.syncdir;

import hu.piller.enykp.alogic.masterdata.sync.logic.transformation.NavToAnykStructureConverter;
import hu.piller.enykp.error.EnykpBusinessException;
import hu.piller.enykp.error.EnykpTechnicalException;
import hu.piller.enykp.util.base.ErrorList;
import hu.piller.enykp.util.base.PropertyList;
import hu.piller.enykp.util.content.ContentUtil;
import hu.piller.enykp.util.content.ContentUtilException;
import hu.piller.enykp.util.oshandler.OsFactory;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class SyncDirHandler {
   public static void checkSyncDirExists() {
      File var0 = new File(getSyncFolderPath());
      if (!var0.exists()) {
         var0.mkdirs();
      }

   }

   public static void checkSyncArchiveDirExists() {
      File var0 = new File(getSyncFolderPath() + File.separator + "archive");
      if (!var0.exists()) {
         var0.mkdirs();
      }

   }

   public static void createFolderForRequest(String var0) {
      File var1 = new File(getSyncFolderPath() + File.separator + var0);
      var1.mkdirs();
   }

   public static void createTemporaryResultsForRefusedIds(String[] var0) throws SyncDirException {
      String var1 = getSyncFolderPath() + File.separator + getQueryId();
      ArrayList var2 = new ArrayList();
      String[] var3 = var0;
      int var4 = var0.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         String var6 = var3[var5];

         try {
            ContentUtil.writeTextToFile("A lekérdezési kérelem benyújtójának nincs jogosultsága az adózó törzsadatainak lekérdezéshez.", var1 + File.separator + "TERR" + var6 + ".txt", "ISO-8859-2");
         } catch (ContentUtilException var8) {
            var2.add(var6 + ": " + var8.getMessage());
         }
      }

      if (var2.size() > 0) {
         StringBuffer var9 = new StringBuffer("Nem hozható létre az ideiglenes hibaállomány kérelem elutasításáról az alábbi adózókhoz:\n");
         Iterator var10 = var2.iterator();

         while(var10.hasNext()) {
            String var11 = (String)var10.next();
            var9.append(var11).append("\n");
         }

         ErrorList.getInstance().writeError("Törzsadat karbantartó", var9.toString(), (Exception)null, (Object)null);
         throw new SyncDirException("Temporáris hibafájl létrehozási hiba! Részleteket a Szerviz->Üzenetek hibalistában találja.");
      }
   }

   public static boolean saveResult(byte[] var0) throws SyncDirException {
      String var1 = getSyncFolderPath() + File.separator + getQueryId();
      ZipInputStream var2 = new ZipInputStream(new ByteArrayInputStream(var0));
      boolean var3 = false;

      try {
         for(ZipEntry var4 = var2.getNextEntry(); var4 != null; var4 = var2.getNextEntry()) {
            String var5 = var4.getName();

            try {
               if (var5.startsWith("OK")) {
                  String var6 = ContentUtil.readTextFromStream(var2, "ISO-8859-2");
                  String var7 = NavToAnykStructureConverter.getInstance().convert(var6);
                  ContentUtil.writeTextToFile(var7, var1 + File.separator + var5, "ISO-8859-2");
               } else {
                  byte[] var24 = ContentUtil.readBytesFromStream(var2);
                  ContentUtil.writeBytesToFile(var24, var1 + File.separator + var5);
               }
            } catch (EnykpBusinessException var19) {
               ErrorList.getInstance().writeError(1000, var5 + " NAV válasz feldolgozása: " + var19.getMessage(), var19, (Object)null);
               var3 = true;
            } catch (EnykpTechnicalException var20) {
               ErrorList.getInstance().writeError(1001, var5 + " NAV válasz feldolgozása: " + var20.getMessage(), var20, (Object)null);
               var3 = true;
            }
         }

         String[] var23 = entriesInFolder(var1, (String[])null);
         String[] var25 = var23;
         int var26 = var23.length;

         for(int var8 = 0; var8 < var26; ++var8) {
            String var9 = var25[var8];
            if (var9.startsWith("TERR")) {
               (new File(var1, var9)).renameTo(new File(var1, var9.replaceAll("TERR", "ERR")));
            }
         }
      } catch (IOException var21) {
         throw new SyncDirException("Válasz archívum feldolgozási hiba: " + var21.getMessage());
      } finally {
         if (var2 != null) {
            try {
               var2.closeEntry();
               var2.close();
            } catch (IOException var18) {
               var18.printStackTrace();
            }
         }

      }

      return var3;
   }

   public static Set<String> getSuccessIds() throws SyncDirException {
      return getIdsWithResultFilePrefix("OK");
   }

   public static Set<String> getErrIds() throws SyncDirException {
      return getIdsWithResultFilePrefix("ERR");
   }

   public static String getResultFileContent(String var0) throws SyncDirException {
      String[] var1 = entriesInFolder(getSyncFolderPath(), getFilteredInSyncDir());
      if (var1.length == 1) {
         String var2 = var1[0];
         var1 = entriesInFolder(getSyncFolderPath() + File.separator + var2, (String[])null);
         String[] var3 = var1;
         int var4 = var1.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            String var6 = var3[var5];
            if (var6.equals("OK" + var0 + ".xml") || var6.equals("ERR" + var0 + ".txt")) {
               String var7 = getSyncFolderPath() + File.separator + var2 + File.separator + var6;
               String var8 = ContentUtil.getXmlFileEncoding(var7);

               try {
                  return ContentUtil.readTextFromFile(var7, var8);
               } catch (ContentUtilException var10) {
                  throw new SyncDirException(var10.getMessage());
               }
            }
         }
      }

      throw new SyncDirException("Nincsen válasz az azonosítóhoz");
   }

   public static SyncDirStatus getSyncDirStatus() throws SyncDirException {
      String[] var0 = entriesInFolder(getSyncFolderPath(), getFilteredInSyncDir());
      if (var0.length > 1) {
         throw new SyncDirException("Önnek több, párhuzamosan futó lekérdezése is van, kérem lépjen kapcsolatba az ügyfélszolgálattal!");
      } else if (var0.length == 0) {
         return SyncDirStatus.NO_QUERY;
      } else {
         String var1 = var0[0];
         var0 = entriesInFolder(getSyncFolderPath() + File.separator + var1, "TERR");
         return var0.length == 0 ? SyncDirStatus.UNSERVED_QUERY : SyncDirStatus.SERVED_QUERY;
      }
   }

   public static String getQueryId() throws SyncDirException {
      String[] var0 = entriesInFolder(getSyncFolderPath(), getFilteredInSyncDir());
      if (var0.length > 1) {
         throw new SyncDirException("Önnek több, párhuzamosan futó lekérdezése is van, kérem lépjen kapcsolatba az ügyfélszolgálattal!");
      } else {
         return var0.length == 0 ? null : var0[0];
      }
   }

   public static void archive() throws SyncDirException {
      String var0 = getQueryId();
      String var1 = getSyncFolderPath() + File.separator + "archive" + File.separator + var0;
      String var2 = getSyncFolderPath() + File.separator + var0;
      File var3 = new File(var1);
      var3.mkdirs();
      File var4 = new File(var2);
      File[] var5 = var4.listFiles();
      if (var5 == null) {
         var3.delete();
         throw new SyncDirException("Az aktuális adategyeztetés munkakönyvtára üres!");
      } else {
         File[] var6 = var5;
         int var7 = var5.length;

         for(int var8 = 0; var8 < var7; ++var8) {
            File var9 = var6[var8];
            var9.renameTo(new File(var3.getPath(), var9.getName()));
         }

         deleteDir(var4);
      }
   }

   private static String[] getFilteredInSyncDir() {
      return OsFactory.getOsHandler().getOsName().toUpperCase().contains("OS X") ? new String[]{"archive", "filter", ".DS_Store"} : new String[]{"archive", "filter"};
   }

   private static void deleteDir(File var0) throws SyncDirException {
      if (!var0.isDirectory()) {
         throw new SyncDirException("A " + var0 + " nem könyvtár.");
      } else if (!var0.exists()) {
         throw new SyncDirException("A " + var0 + " könyvtár nem létezik.");
      } else if (!var0.canWrite()) {
         throw new SyncDirException("A " + var0 + " könyvtár nem törölhető.");
      } else if (var0.listFiles().length > 0) {
         throw new SyncDirException("A " + var0 + " könyvtár nem üres.");
      } else if (!var0.delete()) {
         throw new SyncDirException("A " + var0 + " könyvtár törlése sikertelen!");
      }
   }

   private static Set<String> getIdsWithResultFilePrefix(String var0) throws SyncDirException {
      HashSet var1 = new HashSet();
      String[] var2 = entriesInFolder(getSyncFolderPath(), getFilteredInSyncDir());
      if (var2 != null && var2.length == 1) {
         var2 = entriesInFolder(getSyncFolderPath() + File.separator + var2[0], (String[])null);
         String[] var3 = var2;
         int var4 = var2.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            String var6 = var3[var5];
            if (var6.startsWith(var0)) {
               var1.add(var6.replace(var0, "").replace(".txt", "").replace(".xml", ""));
            }
         }
      }

      return var1;
   }

   private static final String[] entriesInFolder(String var0, final String... var1) throws SyncDirException {
      File var2 = new File(var0);
      if (!var2.exists()) {
         throw new SyncDirException("A(z) " + var0 + " könyvtár nem létezik!");
      } else if (!var2.isDirectory()) {
         throw new SyncDirException("A(z) " + var0 + " nem könyvtár!");
      } else {
         String[] var3 = var2.list(new FilenameFilter() {
            public boolean accept(File var1x, String var2) {
               if (var1 == null) {
                  return true;
               } else {
                  String[] var3 = var1;
                  int var4 = var3.length;

                  for(int var5 = 0; var5 < var4; ++var5) {
                     String var6 = var3[var5];
                     if (var2.toUpperCase().startsWith(var6.toUpperCase())) {
                        return false;
                     }
                  }

                  return true;
               }
            }
         });
         return var3;
      }
   }

   private static final String getSyncFolderPath() {
      return (String)PropertyList.getInstance().get("prop.usr.primaryaccounts") + File.separator + "sync";
   }

   private static final void saveFile(String var0, String var1, String var2) {
      String var3 = var2;

      try {
         if (var1.startsWith("OK")) {
            var3 = NavToAnykStructureConverter.getInstance().convert(var2);
         }

         String var4 = ContentUtil.getXmlContentEncoding(var2);
         if (var4 == null) {
            ContentUtil.writeTextToFile(var3, var0 + File.separator + var1);
         } else {
            ContentUtil.writeTextToFile(var3, var0 + File.separator + var1, var4);
         }
      } catch (EnykpBusinessException var5) {
         System.err.print(var1 + ": " + var5.getMessage());
      } catch (EnykpTechnicalException var6) {
         System.err.print(var1 + ": " + var6.getMessage());
      }

   }
}
