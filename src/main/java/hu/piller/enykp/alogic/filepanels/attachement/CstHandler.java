package hu.piller.enykp.alogic.filepanels.attachement;

import hu.piller.enykp.util.base.Tools;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

import me.necrocore.abevjava.NecroFile;
import me.necrocore.abevjava.NecroFileOutputStream;
import org.apache.commons.codec.binary.Base64InputStream;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class CstHandler extends DefaultHandler {
   private HashMap<String, String[]> cstFileData = new HashMap();
   private boolean inCstDarab = false;
   private boolean inFilenev = false;
   private boolean inMegjegyzes = false;
   private boolean inTipus = false;
   private boolean inFileContent = false;
   private String currentFileId;
   private String[] currentCstData = new String[]{"", "", ""};
   private FileOutputStream currentB64DecodedFileStream;
   private boolean needDecodeB64;
   private String cstPath;
   private HashSet<String> filenames = new HashSet();

   public CstHandler(String var1, boolean var2) {
      this.cstPath = var1;
      this.needDecodeB64 = var2;
   }

   public HashMap<String, String[]> getCstFileData() {
      return this.cstFileData;
   }

   public void startElement(String var1, String var2, String var3, Attributes var4) throws SAXException {
      if ("csatolmanyokszama".equals(var3)) {
         this.inCstDarab = true;
      } else if ("fileinformacio".equals(var3)) {
         this.currentFileId = new String(var4.getValue("azonosito"));
         this.currentCstData = new String[]{"", "", ""};
      } else if ("filenev".equals(var3)) {
         this.inFilenev = true;
      } else if ("megjegyzes".equals(var3)) {
         this.inMegjegyzes = true;
      } else if ("tipus".equals(var3)) {
         this.inTipus = true;
      } else if ("csatolmanyfile".equals(var3)) {
         this.currentFileId = var4.getValue("azonosito");

         try {
            String[] var5 = (String[])this.cstFileData.get(this.currentFileId);
            if (var5[0].indexOf("?") > -1) {
               var5[0] = var5[0].replaceAll("\\?", "_");
               this.cstFileData.put(this.currentFileId, var5);
            }

            this.currentB64DecodedFileStream = this.openFile(((String[])this.cstFileData.get(this.currentFileId))[0]);
         } catch (Exception var6) {
            throw new SAXException(this.currentFileId + " -azonositoju csatolmany tartalom olvasasa sikertelen! " + var6.toString());
         }

         this.inFileContent = true;
      }

   }

   public void endElement(String var1, String var2, String var3) throws SAXException {
      if ("csatolmanyokszama".equals(var3)) {
         this.inCstDarab = false;
      } else if ("filenev".equals(var3)) {
         this.inFilenev = false;
         if (this.filenames.contains(this.currentCstData[0])) {
            this.currentCstData[0] = this.getUniquFilename(this.currentCstData[0]);
         }

         this.filenames.add(this.currentCstData[0]);
      } else {
         if ("fileinformacio".equals(var3)) {
            this.cstFileData.put(this.currentFileId, this.currentCstData);
         }

         if ("megjegyzes".equals(var3)) {
            this.inMegjegyzes = false;
         } else if ("tipus".equals(var3)) {
            this.inTipus = false;
         } else if ("csatolmanyfile".equals(var3)) {
            this.inFileContent = false;

            try {
               this.closeFile(this.currentB64DecodedFileStream);
            } catch (Exception var6) {
            }

            try {
               if (this.needDecodeB64) {
                  File var4 = new NecroFile(this.cstPath + this.cstFileData.get(this.currentFileId) + "_b64");
                  this.decodeB64WithApache(new FileInputStream(var4), new NecroFileOutputStream(this.cstPath + this.cstFileData.get(this.currentFileId)));
                  var4.delete();
               }
            } catch (Exception var5) {
               throw new SAXException(this.currentFileId + " -azonositoju csatolmany tartalom dekodolasa sikertelen! " + var5.toString());
            }
         }
      }

   }

   public void characters(char[] var1, int var2, int var3) throws SAXException {
      if (!this.inCstDarab) {
         StringBuilder var10000;
         String[] var10002;
         if (this.inFilenev) {
            var10000 = new StringBuilder();
            var10002 = this.currentCstData;
            var10002[0] = var10000.append(var10002[0]).append(new String(var1, var2, var3)).toString();
         } else if (this.inMegjegyzes) {
            var10000 = new StringBuilder();
            var10002 = this.currentCstData;
            var10002[1] = var10000.append(var10002[1]).append(new String(var1, var2, var3)).toString();
         } else if (this.inTipus) {
            var10000 = new StringBuilder();
            var10002 = this.currentCstData;
            var10002[2] = var10000.append(var10002[2]).append(new String(var1, var2, var3)).toString();
         } else if (this.inFileContent) {
            try {
               this.writeContentToFile(this.currentB64DecodedFileStream, new String(var1, var2, var3));
            } catch (Exception var5) {
               throw new SAXException(this.currentFileId + " -azonositoju csatolmany tartalom mentese sikertelen! " + var5.toString());
            }
         }
      }

   }

   private FileOutputStream openFile(String var1) throws Exception {
      FileOutputStream var2 = new NecroFileOutputStream(this.cstPath + var1 + "_b64");
      return var2;
   }

   private void writeContentToFile(FileOutputStream var1, String var2) throws Exception {
      var1.write(var2.getBytes("ISO-8859-2"));
   }

   private void closeFile(FileOutputStream var1) throws Exception {
      var1.close();
   }

   private void decodeB64WithApache(FileInputStream var1, FileOutputStream var2) throws IOException {
      Base64InputStream var3 = new Base64InputStream(var1);

      try {
         byte[] var4 = new byte[2048];
         boolean var5 = false;

         int var20;
         while((var20 = var3.read(var4)) > -1) {
            var2.write(var4, 0, var20);
         }
      } catch (Exception var18) {
         Tools.eLog(var18, 0);
      } finally {
         try {
            var3.close();
         } catch (Exception var17) {
         }

         try {
            var2.close();
         } catch (Exception var16) {
         }

      }

   }

   private String getUniquFilename(String var1) {
      if (var1.indexOf(".") == 1) {
         return var1 + "_" + System.currentTimeMillis();
      } else {
         String[] var2 = var1.split("\\.");
         return var2[0] + "_" + System.currentTimeMillis() + "." + var2[1];
      }
   }
}
