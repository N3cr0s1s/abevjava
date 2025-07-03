package hu.piller.enykp.alogic.filepanels.attachement;

import hu.piller.enykp.alogic.fileloader.bz.BZipLoader;
import hu.piller.enykp.datastore.Elem;
import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.gui.framework.MainFrame;
import hu.piller.enykp.gui.model.BookModel;
import hu.piller.enykp.util.base.Base64;
import hu.piller.enykp.util.base.PropertyList;
import hu.piller.enykp.util.base.Tools;
import hu.piller.tools.bzip2.CBZip2InputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Vector;
import java.util.zip.ZipFile;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import me.necrocore.abevjava.NecroFile;
import me.necrocore.abevjava.NecroFileOutputStream;
import org.apache.commons.codec.binary.Base64InputStream;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class CstParser {
   public static final String FILE_INFOS = "fileinformaciok";
   public static final String FILE_INFO = "fileinformacio";
   public static final String FILE_CONTENTS = "csatolmanyfileok";
   public static final String FILE_CONTENT = "csatolmanyfile";
   public static final String TAG_FILENAME = "filenev";
   public static final String TAG_COMMENT = "megjegyzes";
   public static final String TAG_TYPE = "tipus";
   public static final String ATTR_AZONOSITO = "azonosito";
   public static final String FILE_DB = "csatolmanyokszama";
   public static final String BASE64_TMP_FILE_SUFFIX = "_b64";
   public static final String ENCODING = "ISO-8859-2";
   static String root;
   static String attachPath;

   public CstParser() throws Exception {
      try {
         root = Tools.fillPath((String)PropertyList.getInstance().get("prop.usr.root"));
      } catch (Exception var3) {
         throw new Exception("*prop.usr.root");
      }

      try {
         attachPath = Tools.fillPath(root + PropertyList.getInstance().get("prop.usr.attachment"));
      } catch (Exception var2) {
         throw new Exception("*prop.usr.attachment");
      }

      attachPath = Tools.beautyPath(attachPath);
   }

   public void parse(BookModel var1) throws Exception {
      Hashtable var2 = var1.get_templateheaddata();
      Hashtable var3 = (Hashtable)var2.get("docinfo");
      String var4 = Tools.getFormIdFromFormAttrs(var1);
      if (var4 != null) {
         var3.put("attachment", var4);
      }

      String var5;
      if (var3.get("attachment") == null) {
         var5 = "0";
      } else {
         var5 = (String)var3.get("attachment");
      }

      if (var5.equals("0")) {
         int var6 = var1.getCalcelemindex();
         if (var6 == 0) {
            return;
         }

         var1.setCalcelemindex(0);
         var2 = var1.get_templateheaddata();
         var3 = (Hashtable)var2.get("docinfo");
         var4 = Tools.getFormIdFromFormAttrs(var1);
         if (var4 != null) {
            var3.put("attachment", var4);
         }

         if (var3.get("attachment") == null) {
            var5 = "0";
         } else {
            var5 = (String)var3.get("attachment");
         }

         var1.setCalcelemindex(var6);
         if (var5.equals("0")) {
            return;
         }
      }

      File var16 = new NecroFile(attachPath + "tmp");
      if (var16.exists()) {
         emptyDir(var16);
      } else {
         var16.mkdir();
      }

      String var7 = var1.cc.getLoadedfile().getAbsolutePath().substring(0, var1.cc.getLoadedfile().getAbsolutePath().length() - 4);

      for(int var8 = 0; var8 < Math.min(var1.forms.size(), var1.cc.size()); ++var8) {
         String var9 = "";
         var1.cc.setActiveObject(var1.cc.get(var8));

         try {
            var9 = this.getRightCSTFilename(var7, var1, var5);
         } catch (Exception var14) {
            var9 = "";
         }

         if (var9.length() != 1) {
            File var10 = new NecroFile(var9);
            int var11 = this.isZippedFile(var10);
            if (var11 == -1) {
               this.hiba("Nem sikerült a csatolmány fájl olvasása: " + var9);
            } else {
               if (var11 == 1) {
                  try {
                     byte[] var12 = (byte[])((byte[])(new BZipLoader()).getUnzippedData(var10).get("unzippedData"));
                     var9 = this.writePlainData(var12, var10);
                  } catch (Exception var15) {
                     this.hiba("Hiba történt a tömörített adatok kicsomagolásakor. Valószínűleg sérült a fájl.");
                     continue;
                  }
               }

               try {
                  this.parseCst(var9);
               } catch (Exception var13) {
                  this.hiba("Hiba történt a csatolmány-leíró fájl feldolgozásakor. " + var13.getMessage());
               }
            }
         }
      }

   }

   private byte[] getPlainDataFromZip(String var1) throws Exception {
      File var2 = new NecroFile(var1);
      FileInputStream var3 = new FileInputStream(var2);
      var3.skip(2L);
      ByteArrayOutputStream var4 = new ByteArrayOutputStream();
      InputStreamReader var5 = new InputStreamReader(new CBZip2InputStream(var3), "ISO-8859-2");

      int var6;
      for(int var7 = 0; (var6 = var5.read()) > 0 && (long)var7 < var2.length(); ++var7) {
         var4.write(var6);
      }

      var5.close();
      var3.close();
      return var4.toByteArray();
   }

   private byte[] getPlainData(String var1) throws Exception {
      FileInputStream var2 = new FileInputStream(var1);
      ByteArrayOutputStream var3 = new ByteArrayOutputStream();

      int var4;
      while((var4 = var2.read()) > 0) {
         var3.write(var4);
      }

      var2.close();
      return var3.toByteArray();
   }

   private void parseCst(String var1) throws ParserConfigurationException, IOException, SAXException, Exception {
      String var2 = attachPath + "tmp" + File.separator;
      HashSet var4 = new HashSet();
      FileInputStream var5 = new FileInputStream(var1);
      String var6 = PropertyList.getInstance().get("prop.usr.root") + File.separator + PropertyList.getInstance().get("prop.usr.tmp") + File.separator;
      HashMap var3 = this.parseCstWithSax(var5, var6);
      var4.clear();
      Vector var7 = new Vector(var3.keySet());
      FileOutputStream var8 = null;
      Object var9 = null;
      var1 = var1.substring(var1.lastIndexOf(File.separator) + 1, var1.length() - 3);

      try {
         var8 = new NecroFileOutputStream(var2 + var1 + "atc");

         for(int var10 = 0; var10 < var7.size(); ++var10) {
            String var11 = var2 + ((String[])var3.get(var7.get(var10)))[0];
            File var12 = new NecroFile(var11);
            if (var12.exists()) {
               int var13 = var11.lastIndexOf(".");
               if (var13 > -1) {
                  var11 = var11.substring(0, var13) + "_" + System.currentTimeMillis() + var11.substring(var13);
               }
            }

            FileOutputStream var21 = new NecroFileOutputStream(var11);

            try {
               this.decodeB64WithApache(new FileInputStream(var6 + ((String[])var3.get(var7.get(var10)))[0] + "_b64"), var21);
            } catch (Exception var19) {
               System.out.println("Nem sikerült kibontani a csatolmány fájlt a leíróból : " + var11);
               var19.printStackTrace();
               Tools.eLog(var19, 0);
               continue;
            }

            try {
               this.checkAndFixAttachment(var11);
            } catch (Exception var18) {
               System.out.println("zip or rtf attachment fix error");
               Tools.eLog(var18, 0);
            }

            try {
               File var14 = new NecroFile(var6 + ((String[])var3.get(var7.get(var10)))[0] + "_b64");
               var14.delete();
            } catch (Exception var17) {
               Tools.eLog(var17, 0);
            }

            String[] var22 = (String[])var3.get(var7.get(var10));
            var8.write((var2 + var22[0] + ";" + var22[1] + ";" + var22[2] + "\n").getBytes("ISO-8859-2"));
         }

         var8.close();
      } catch (Exception var20) {
         try {
            ((FileOutputStream)var9).close();
         } catch (IOException var16) {
            Tools.eLog(var20, 0);
         }

         try {
            var8.close();
         } catch (IOException var15) {
            Tools.eLog(var20, 0);
         }

         throw var20;
      }
   }

   private HashMap<String, String[]> parseCstWithSax(InputStream var1, String var2) {
      try {
         CstHandler var3 = new CstHandler(var2, false);
         InputSource var4 = new InputSource(var1);
         var4.setEncoding("ISO-8859-2");
         SAXParserFactory var5 = SAXParserFactory.newInstance();
         var5.setNamespaceAware(true);
         var5.setValidating(true);
         SAXParser var6 = var5.newSAXParser();
         var6.parse(var4, var3);
         var1.close();
         return var3.getCstFileData();
      } catch (ParserConfigurationException var7) {
         var7.printStackTrace();
      } catch (SAXException var8) {
         var8.printStackTrace();
      } catch (IOException var9) {
         var9.printStackTrace();
      }

      return null;
   }

   private CstParser.CstInfo parseFI(Node var1, HashSet var2) {
      CstParser.CstInfo var3 = new CstParser.CstInfo();
      var3.key = var1.getAttributes().item(0).getNodeValue();
      NodeList var4 = var1.getChildNodes();

      for(int var5 = 0; var5 < var4.getLength(); ++var5) {
         Node var6 = var4.item(var5);
         if (var6.hasChildNodes()) {
            if (var6.getNodeName().equalsIgnoreCase("filenev")) {
               var3.fileName = var6.getTextContent();
            }

            if (var2.contains(var3.fileName)) {
               var3.fileName = var3.fileName + System.currentTimeMillis();
               var2.add(var3.fileName);
            }

            var3.value = var3.value + ";" + var6.getFirstChild().getNodeValue();
         } else if (var6.getNodeName().equals("megjegyzes")) {
            var3.value = var3.value + ";" + var6.getTextContent();
         }
      }

      return var3;
   }

   private byte[] getBase64DecodedString(String var1) throws Exception {
      return Base64.decode(var1);
   }

   private int isZippedFile(File var1) {
      FileInputStream var2 = null;

      byte var4;
      try {
         var2 = new FileInputStream(var1);
         byte[] var3 = new byte[2];
         var2.read(var3);
         String var18 = new String(var3);
         byte var5;
         if (!"bz".startsWith(var18.toLowerCase())) {
            var5 = 0;
            return var5;
         }

         var5 = 1;
         return var5;
      } catch (IOException var16) {
         var4 = -1;
      } finally {
         try {
            var2.close();
         } catch (Exception var15) {
            Tools.eLog(var15, 0);
         }

      }

      return var4;
   }

   private void hiba(String var1) {
      GuiUtil.showMessageDialog(MainFrame.thisinstance, var1, "Csatolmány kezelési hiba", 0);
   }

   public static void clean() {
      try {
         root = Tools.fillPath((String)PropertyList.getInstance().get("prop.usr.root"));
         attachPath = Tools.fillPath(root + PropertyList.getInstance().get("prop.usr.attachment"));
         File var0 = new NecroFile(attachPath + "tmp");
         emptyDir(var0);
      } catch (Exception var1) {
         Tools.eLog(var1, 0);
      }

   }

   private static void emptyDir(File var0) {
      try {
         if (!var0.isDirectory()) {
            return;
         }

         File[] var1 = var0.listFiles();

         for(int var2 = 0; var2 < var1.length; ++var2) {
            var1[var2].delete();
         }
      } catch (Exception var3) {
         Tools.eLog(var3, 0);
      }

   }

   public Vector parse(File var1) throws Exception {
      return this.parse(var1, true);
   }

   public Vector parse(File var1, boolean var2) throws Exception {
      if (!var1.exists()) {
         this.hiba(var1.getAbsolutePath() + " fájl nem található");
         throw new FileNotFoundException(var1.getAbsolutePath() + " fájl nem található");
      } else {
         int var3 = this.isZippedFile(var1);
         if (var3 == -1) {
            this.hiba("Nem sikerült a csatolmány fájl olvasása / nem megfelelő formátum: " + var1.getAbsolutePath());
            throw new FileNotFoundException(var1.getAbsolutePath() + " fájl olvasása sikertelen.");
         } else {
            byte[] var4;
            if (var3 == 1) {
               try {
                  var4 = (byte[])((byte[])(new BZipLoader()).getUnzippedData(var1).get("unzippedData"));
               } catch (Exception var8) {
                  this.hiba("Hiba történt a tömörített adatok kicsomagolásakor. Valószínűleg sérült a fájl.");
                  throw new Exception("Hiba történt a tömörített adatok kicsomagolásakor. Valószínűleg sérült a fájl.");
               }
            } else {
               try {
                  var4 = this.getPlainData(var1.getAbsolutePath());
               } catch (Exception var7) {
                  this.hiba("Hiba történt a csatolmány-leíró fájl olvasásakor. Valószínűleg sérült a fájl.");
                  return null;
               }
            }

            try {
               return this._parseCst(var4, var2 ? var1.getAbsolutePath() : null);
            } catch (Exception var6) {
               this.hiba("Hiba történt a csatolmány-leíró fájl feldolgozásakor. " + var6.getMessage());
               return null;
            }
         }
      }
   }

   private String getRightCSTFilename(String var1, BookModel var2, String var3) {
      File var4 = new NecroFile(var1);
      String var5 = var1 + "_" + (var2.splitesaver.equals("true") ? var2.get_formid() : var2.get(((Elem)var2.cc.getActiveObject()).getType()).name) + ".cst";
      File var6 = new NecroFile(var5);
      if (var6.exists()) {
         return var5;
      } else {
         var5 = var1 + ".xml_csatolmanyai" + File.separator + var4.getName() + "_" + (var2.splitesaver.equals("true") ? var2.get_formid() : var2.get(((Elem)var2.cc.getActiveObject()).getType()).name) + ".cst";
         var6 = new NecroFile(var5);
         if (var6.exists()) {
            return var5;
         } else {
            var5 = var1 + ".cst";
            var6 = new NecroFile(var5);
            if (var6.exists()) {
               return var5;
            } else {
               var5 = var1 + ".xml_csatolmanyai" + File.separator + var4.getName() + ".cst";
               var6 = new NecroFile(var5);
               if (var6.exists()) {
                  return var5;
               } else {
                  if (var1.endsWith("_p")) {
                     var1 = var1.substring(0, var1.length() - 4);
                     var5 = var1 + "_" + (var2.splitesaver.equals("true") ? var2.get_formid() : var2.get(((Elem)var2.cc.getActiveObject()).getType()).name) + ".cst";
                     var6 = new NecroFile(var5);
                     if (var6.exists()) {
                        return var5;
                     }

                     var5 = var1 + ".xml_csatolmanyai" + File.separator + var4.getName() + "_" + (var2.splitesaver.equals("true") ? var2.get_formid() : var2.get(((Elem)var2.cc.getActiveObject()).getType()).name) + ".cst";
                     var6 = new NecroFile(var5);
                     if (var6.exists()) {
                        return var5;
                     }

                     var5 = var1 + ".cst";
                     var6 = new NecroFile(var5);
                     if (var6.exists()) {
                        return var5;
                     }

                     var5 = var1 + ".xml_csatolmanyai" + File.separator + var4.getName() + ".cst";
                     var6 = new NecroFile(var5);
                     if (var6.exists()) {
                        return var5;
                     }
                  }

                  return var3.equals("1") ? "1" : "0";
               }
            }
         }
      }
   }

   private String writePlainData(byte[] var1, File var2) throws IOException {
      String var3 = var2.getAbsolutePath() + "_plain";
      File var4 = new NecroFile(var3);
      var4.deleteOnExit();
      FileOutputStream var5 = new NecroFileOutputStream(var4);
      var5.write(var1);
      var5.close();
      return var3;
   }

   private String writeTempFile(String var1, int var2) throws IOException {
      String var3 = PropertyList.getInstance().get("prop.usr.root") + File.separator + PropertyList.getInstance().get("prop.usr.tmp") + File.separator + System.currentTimeMillis() + "_" + var2 + ".tmp";
      File var4 = new NecroFile(var3);
      var4.deleteOnExit();
      FileOutputStream var5 = null;

      try {
         var5 = new NecroFileOutputStream(var4);
         var5.write(var1.getBytes());
      } catch (Exception var15) {
         Tools.eLog(var15, 0);
         var15.printStackTrace();
      } finally {
         try {
            var5.close();
         } catch (IOException var14) {
            Tools.eLog(var14, 0);
         }

      }

      return var3;
   }

   private Vector _parseCst(byte[] var1, String var2) throws ParserConfigurationException, IOException, SAXException, Exception {
      Vector var3 = new Vector();
      String var4 = attachPath + "tmp" + File.separator;

      try {
         var2 = var2.substring(var2.lastIndexOf(File.separator) + 1, var2.length() - 3);
      } catch (Exception var25) {
         Tools.eLog(var25, 0);
      }

      Hashtable var5 = new Hashtable();
      Hashtable var6 = new Hashtable();
      HashSet var7 = new HashSet();
      DocumentBuilderFactory var8 = DocumentBuilderFactory.newInstance();
      ByteArrayInputStream var9 = new ByteArrayInputStream(var1);
      DocumentBuilder var10 = var8.newDocumentBuilder();
      Document var11 = var10.parse(var9);
      NodeList var12 = var11.getElementsByTagName("fileinformaciok");
      Node var13 = var12.item(0);
      NodeList var14 = var13.getChildNodes();
      HashSet var15 = new HashSet();

      int var16;
      Node var17;
      CstParser.CstInfo var18;
      for(var16 = 0; var16 < var14.getLength(); ++var16) {
         var17 = var14.item(var16);
         if (var17.getNodeName().equalsIgnoreCase("fileinformacio")) {
            var18 = this.parseFI(var17, var15);
            if (!var7.contains(var18.fileName)) {
               var7.add(var18.fileName);
               var5.put(var18.key, attachPath + "tmp" + File.separator + var18.value.substring(1));
            }
         }
      }

      var7.clear();
      var12 = var11.getElementsByTagName("csatolmanyfileok");
      var13 = var12.item(0);
      var14 = var13.getChildNodes();

      for(var16 = 0; var16 < var14.getLength(); ++var16) {
         var17 = var14.item(var16);
         if (var17.getNodeName().equalsIgnoreCase("csatolmanyfile")) {
            var18 = new CstParser.CstInfo();
            var18.key = var17.getAttributes().item(0).getNodeValue();
            if (var17.hasChildNodes()) {
               var18.value = var17.getFirstChild().getNodeValue();
               if (var5.containsKey(var18.key)) {
                  if (var18.value.startsWith("\n")) {
                     var6.put(var18.key, var18.value.substring(1));
                  } else {
                     var6.put(var18.key, var18.value);
                  }
               }
            }
         }
      }

      Vector var27 = new Vector(var5.keySet());
      Collections.sort(var27);

      try {
         var3 = new Vector(var5.values());
      } catch (Exception var24) {
         Tools.eLog(var24, 0);
      }

      if (var2 == null) {
         return var3;
      } else {
         var17 = null;
         var18 = null;

         try {
            String var19 = var4 + var2 + "atc";
            var19 = var19.replaceAll("\\?", "_");
            FileOutputStream var28 = new NecroFileOutputStream(var19);

            for(int var20 = 0; var20 < var27.size(); ++var20) {
               String var21 = ((String)var5.get(var27.get(var20))).split(";")[0];
               var21 = var21.replaceAll("\\?", "_");
               FileOutputStream var29 = new NecroFileOutputStream(var21);
               var29.write(this.getBase64DecodedString((String)var6.get(var27.get(var20))));
               var29.close();
               this.checkAndFixAttachment(var21);
               var28.write((var5.get(var27.get(var20)) + "\n").getBytes("ISO-8859-2"));
            }

            var28.close();
            return var3;
         } catch (Exception var26) {
            try {
               var9.close();
            } catch (IOException var23) {
               Tools.eLog(var26, 0);
            }

//            try {
               // TODO: ?? what is tihs
//               var17.close();
//            } catch (IOException var22) {
//               Tools.eLog(var26, 0);
//            }

            throw var26;
         }
      }
   }

   private void checkAndFixAttachment(String var1) throws Exception {
      if (var1.toLowerCase().endsWith("rtf")) {
         int var2 = this.checkRtfPairs(var1);
         if (var2 > 0) {
            this.fixRtfFile(var1, var2);
         }

      } else if (var1.toLowerCase().endsWith("zip")) {
         if (!this.zipChecker(var1)) {
            this.fixZipFile(var1);
         }

         if (!this.zipChecker(var1)) {
            this.fixZipFile(var1);
         }

      }
   }

   private int checkRtfPairs(String var1) throws IOException {
      FileReader var2 = new FileReader(var1);
      int var3 = 0;
      char[] var5 = new char[2048];

      int var4;
      while((var4 = var2.read(var5)) > -1) {
         for(int var6 = 0; var6 < var4; ++var6) {
            if (var5[var6] == '{') {
               ++var3;
            }

            if (var5[var6] == '}') {
               --var3;
            }
         }
      }

      return var3;
   }

   private void fixRtfFile(String var1, int var2) throws IOException {
      RandomAccessFile var3 = new RandomAccessFile(var1, "rw");
      var3.seek(var3.length());
      String var4 = "}";
      if (var2 > 1) {
         var4 = "}}";
      }

      var3.writeChars(var4);
      var3.close();
   }

   private boolean zipChecker(String var1) throws IOException {
      ZipFile var2 = null;

      boolean var4;
      try {
         var2 = new ZipFile(var1);
         boolean var3 = true;
         return var3;
      } catch (Exception var14) {
         var4 = false;
      } finally {
         try {
            if (var2 != null) {
               var2.close();
               var2 = null;
            }
         } catch (Exception var13) {
         }

      }

      return var4;
   }

   private void fixZipFile(String var1) throws IOException {
      RandomAccessFile var2 = new RandomAccessFile(var1, "rw");
      var2.seek(var2.length());
      var2.write(new byte[]{0});
      var2.close();
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

   private class CstInfo {
      public String key = null;
      public String fileName = "";
      public String value = "";

      public CstInfo() {
      }
   }
}
