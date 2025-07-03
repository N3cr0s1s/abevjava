package hu.piller.enykp.alogic.ebev;

import hu.piller.enykp.alogic.filepanels.mohu.LoginDialog;
import hu.piller.enykp.alogic.filepanels.mohu.LoginDialogFactory;
import hu.piller.enykp.alogic.fileutil.DatastoreKeyToXml;
import hu.piller.enykp.alogic.orghandler.OrgHandler;
import hu.piller.enykp.alogic.settingspanel.SettingsStore;
import hu.piller.enykp.alogic.signer.AnykCsatolmanyLenyomatSignController;
import hu.piller.enykp.alogic.signer.CancelledByUserException;
import hu.piller.enykp.alogic.signer.SignerException;
import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.gui.framework.MainFrame;
import hu.piller.enykp.gui.model.BookModel;
import hu.piller.enykp.interfaces.IErrorList;
import hu.piller.enykp.kauclient.KauAuthMethod;
import hu.piller.enykp.kauclient.KauAuthMethods;
import hu.piller.enykp.niszws.util.DapSessionHandler;
import hu.piller.enykp.niszws.util.GateType;
import hu.piller.enykp.niszws.util.KauAuthHelper;
import hu.piller.enykp.niszws.util.KauSessionTimeoutHandler;
import hu.piller.enykp.util.JavaInfo;
import hu.piller.enykp.util.base.Base64;
import hu.piller.enykp.util.base.ErrorList;
import hu.piller.enykp.util.base.PropertyList;
import hu.piller.enykp.util.base.Tools;
import hu.piller.tools.bzip2.CBZip2OutputStream;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Vector;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import javax.swing.border.BevelBorder;

import me.necrocore.abevjava.NecroFile;
import me.necrocore.abevjava.NecroFileOutputStream;
import org.apache.commons.codec.binary.Base64InputStream;

public class Attachements {
   private CBZip2OutputStream tempCstFile;
   private File cstFile;
   private BookModel bookModel;
   private SendParams sp = new SendParams(PropertyList.getInstance());
   Object cmdObject;

   public Attachements(BookModel var1) {
      this.bookModel = var1;
   }

   public Object createXml(Object var1, File var2) throws Exception {
      Vector var3 = new Vector();
      String var4 = null;
      BufferedOutputStream var5 = null;
      String var6 = null;
      boolean var7 = false;

      try {
         if (var1 instanceof String) {
            try {
               ErrorList.getInstance().writeError(new Long(4001L), "Hiba a csatolmány-leíró betöltésekor:" + var1, IErrorList.LEVEL_ERROR, (Exception)null, (Object)null);
            } catch (Exception var63) {
               System.out.println("Hiba a csatolmány-leíró betöltésekor (_)");
            }

            throw new AttachementException("Hiba a csatolmány-leíró betöltésekor:\n" + var1);
         }

         var3 = (Vector)var1;
         if (var1 != null) {
            if (var3.size() == 0) {
               throw new AttachementException("A(z) " + var2.getAbsolutePath() + " leíró fájlban lévő csatolmány nem található!");
            }
         } else {
            var3 = new Vector();
         }

         String var72 = this.bookModel.cc.getLoadedfile().getAbsolutePath();
         if (var72.endsWith(".frm.enyk")) {
            var72 = var72.substring(0, var72.length() - ".frm.enyk".length());
         } else {
            var72 = var72.substring(0, var72.length() - ".xml".length());
         }

         this.cstFile = new NecroFile(var72 + ".cst");

         try {
            var6 = SettingsStore.getInstance().get("attachments", "attachment.temporary.directory");
            if (var6.equals("")) {
               throw new Exception();
            }

            if (!(new NecroFile(var6)).exists()) {
               throw new Exception();
            }

            var6 = var6 + "\\AnykTempZipFile.tmp";
         } catch (Exception var64) {
            var6 = null;
         }

         File var9 = new NecroFile(var6 == null ? var72 + ".cst" : var6);
         if (PropertyList.getInstance().get("batch.kr.pass2dsign") == null) {
            var9.deleteOnExit();
            this.cstFile.deleteOnExit();
         }

         if (PropertyList.getInstance().get("prop.dynamic.signWithExternalTool") == null) {
            String var10 = this.checkAnykAsicInAtcList(var3);
            if (var10 != null) {
               throw new Exception(var10);
            }
         } else {
            this.addAsicsToAttachmentList(var3);
         }

         if (this.bookModel.isAvdhModel() && PropertyList.getInstance().get("prop.dynamic.pass2DSign") == null) {
            if (PropertyList.getInstance().get("prop.dynamic.signWithExternalTool") == null) {
               this.handleFirstAVDH(var3, true);
            } else {
               this.handleExternal(var3);
            }
         }

         this.handleIfExternalSignedXmlHash(var3);
         Mark2Send.nyomtatvanyCim.setText("Csatolmányok előkészítése a titkosításhoz");
         var5 = new BufferedOutputStream(new NecroFileOutputStream(var9), 524288);
         var5.write("BZ".getBytes("ISO-8859-2"));
         this.tempCstFile = new CBZip2OutputStream(var5);
         this.createCsXmlHead(var3.size(), (new NecroFile(var72 + ".cst")).getName());
         this.createCstInfoData(var3);
         this.createCstFileData(var3);
      } catch (SignerException var65) {
         SignerException var71 = var65;

         try {
            ErrorList.getInstance().writeError(new Long(4001L), "Hiba az avdh aláírás elkészítésekor \n(" + var71.getMessage() + ")", IErrorList.LEVEL_ERROR, (Exception)null, (Object)null);
         } catch (Exception var62) {
            System.out.println("Hiba az avdh aláírás elkészítésekor (_)");
         }

         var4 = "AVDH_Hiba az avdh aláírás elkészítésekor \n(" + var65.getMessage() + ")";
      } catch (CancelledByUserException var66) {
         try {
            ErrorList.getInstance().writeError(new Long(4001L), "Felhasználói megszakítás", IErrorList.LEVEL_MESSAGE, (Exception)null, (Object)null);
         } catch (Exception var61) {
            System.out.println("Felhasználói megszakítás az avdh aláírás készítésekor");
         }

         var4 = "Felhasználói megszakítás az avdh aláírás elkészítésekor";
      } catch (AttachementNameException var67) {
         AttachementNameException var70 = var67;
         var7 = true;

         try {
            ErrorList.getInstance().writeError(new Long(4001L), "Hiba a csatolmány xml készítésekor\n(" + var70.getMessage() + ")", IErrorList.LEVEL_ERROR, (Exception)null, (Object)null);
         } catch (Exception var60) {
            System.out.println("Hiba a csatolmány xml készítésekor (_)");
         }

         var4 = "Hiba a csatolmány xml készítésekor\n(" + var67.getMessage() + ")";
      } catch (Exception var68) {
         Exception var8 = var68;
         System.out.println("Ismeretlen hiba _ avdh");
         var68.printStackTrace();

         try {
            ErrorList.getInstance().writeError(new Long(4001L), "Hiba a csatolmány xml készítésekor\n(" + var8.getMessage() + ")", IErrorList.LEVEL_ERROR, (Exception)null, (Object)null);
         } catch (Exception var59) {
            System.out.println("Hiba a csatolmány xml készítésekor (_)");
         }

         var4 = "Hiba a csatolmány xml készítésekor\n(" + var68.getMessage() + ")";
      } finally {
         try {
            this.tempCstFile.flush();
         } catch (Exception var58) {
            Tools.eLog(var58, 0);
         }

         try {
            this.tempCstFile.close();
         } catch (Exception var57) {
            Tools.eLog(var57, 0);
         }

         try {
            var5.flush();
         } catch (Exception var56) {
            Tools.eLog(var56, 0);
         }

         try {
            var5.close();
         } catch (Exception var55) {
            Tools.eLog(var55, 0);
         }

         try {
            if (var6 != null) {
               EbevTools.copyFile(var6, this.cstFile.getAbsolutePath(), true);
            }
         } catch (Exception var54) {
            var54.printStackTrace();
         }

      }

      if (var4 != null) {
         if (var7) {
            throw new AttachementNameException(var4);
         } else {
            throw new AttachementException(var4);
         }
      } else {
         if (PropertyList.getInstance().get("prop.dynamic.ebev_call_from_menu") != null) {
            Vector var74 = new Vector(var3.size());

            for(int var73 = 0; var73 < var3.size(); ++var73) {
               String[] var75 = (String[])((String[])var3.elementAt(var73));
               var74.add(var75[0]);
            }

            PropertyList.getInstance().set("prop.dynamic.ebev_call_from_menu", var74);
         }

         return var3;
      }
   }

   private void createCsXmlHead(int var1, String var2) throws Exception {
      StringBuffer var3 = new StringBuffer();
      var3.append("<?xml version=\"1.0\" encoding=\"").append("ISO-8859-2").append("\"?>\n").append("<csatolmanyok xmlns=\"http://www.apeh.hu/abev/csatolmanyok/2007/01\">\n").append(" <nyomtatvany azonosito=\"1\">\n").append("  <csatolmanyinformaciok>\n").append("    <csatolmanyokszama>").append(var1).append("</csatolmanyokszama>\n").append("    <nyomtatvanyfile>").append(DatastoreKeyToXml.htmlConvert(var2)).append("</nyomtatvanyfile>\n").append("    <fileinformaciok>\n");
      this.tempCstFile.write(var3.toString().getBytes("ISO-8859-2"));
   }

   private void createCstInfoData(Vector var1) throws Exception {
      for(int var2 = 0; var2 < var1.size(); ++var2) {
         String[] var3 = (String[])((String[])var1.elementAt(var2));
         this.tempCstFile.write(("      <fileinformacio azonosito=\"" + (var2 + 1) + "\">\n").getBytes("ISO-8859-2"));
         this.tempCstFile.write(("        <filenev>" + DatastoreKeyToXml.htmlConvert((new NecroFile(var3[0])).getName()) + "</filenev>\n").getBytes("ISO-8859-2"));
         this.tempCstFile.write(("        <megjegyzes>" + DatastoreKeyToXml.htmlConvert(var3[1]) + "</megjegyzes>\n").getBytes("ISO-8859-2"));
         this.tempCstFile.write(("        <tipus>" + DatastoreKeyToXml.htmlConvert(var3[2]) + "</tipus>\n").getBytes("ISO-8859-2"));
         this.tempCstFile.write("      </fileinformacio>\n".getBytes("ISO-8859-2"));
      }

      this.tempCstFile.write("    </fileinformaciok>\n  </csatolmanyinformaciok>\n".getBytes("ISO-8859-2"));
   }

   private void createCstFileData(Vector var1) throws Exception {
      boolean var2 = true;

      try {
         if ("Stream".equals(OrgHandler.getInstance().getBase64DecoderClass((String)this.bookModel.docinfo.get("org")))) {
            var2 = false;
         }
      } catch (Exception var7) {
         Tools.eLog(var7, 0);
      }

      this.tempCstFile.write("  <csatolmanyfileok>\n".getBytes("ISO-8859-2"));

      for(int var3 = 0; var3 < var1.size(); ++var3) {
         String[] var4 = (String[])((String[])var1.elementAt(var3));
         this.tempCstFile.write(("    <csatolmanyfile azonosito=\"" + (var3 + 1) + "\">").getBytes("ISO-8859-2"));
         long var5 = System.currentTimeMillis();
         if (var2) {
            this.tempCstFile.write(this.getBase64EncodedString(var4[0]));
         } else {
            this.apacheB64Encoding(var4[0], this.tempCstFile);
         }

         this.tempCstFile.write("</csatolmanyfile>\n".getBytes("ISO-8859-2"));
      }

      this.tempCstFile.write("  </csatolmanyfileok>\n </nyomtatvany>\n</csatolmanyok>\n".getBytes("ISO-8859-2"));
   }

   private byte[] getBase64EncodedString(String var1) throws Exception {
      return Base64.encodeFromFile(var1).getBytes("ISO-8859-2");
   }

   private void apacheB64Encoding(String var1, OutputStream var2) throws IOException {
      Base64InputStream var3 = new Base64InputStream(new FileInputStream(var1), true, 0, (byte[])null);
      byte[] var4 = new byte[2048];
      boolean var5 = false;

      int var6;
      while((var6 = var3.read(var4)) > -1) {
         var2.write(var4, 0, var6);
      }

      var3.close();
      var2.flush();
   }

   public void handleFirstAVDH(Vector var1, boolean var2) throws Exception {
      this.handleFirstAVDH(var1, var2, true);
   }

   public void handleFirstAVDH(Vector var1, boolean var2, boolean var3) throws Exception {
      boolean var4 = PropertyList.getInstance().get("prop.dynamic.mohu_user_and_pass_from_batch") != null;
      if (PropertyList.getInstance().get("prop.dynamic.avdhWithNoAuth") != null) {
         if (!var4) {
            PropertyList.getInstance().set("prop.dynamic.avdhWithNoAuth", (Object)null);
         }

      } else {
         LoginDialog var5 = LoginDialogFactory.create(GateType.UGYFELKAPU, 0, false, this.bookModel.getTaxNumberHKAzonFromDocInfo());
         if (var5 == null) {
            throw new Exception(JavaInfo.getNoJFXMessageLines());
         } else {
            var5.setLocationRelativeTo(MainFrame.thisinstance);
            if (!var4 && KauAuthMethods.getSelected() != KauAuthMethod.KAU_DAP) {
               var5.showIfNeed();
               if (var5.getState() == 0) {
                  throw new CancelledByUserException("AVDH_Felhasználói megszakítás");
               }

               if (var5.getState() == 1) {
                  throw new Exception("Hiba az azonosításkor");
               }

               if (var5.getState() == 2) {
                  return;
               }
            }

            Vector var6 = new Vector();
            ArrayList var7 = new ArrayList();

            for(int var8 = 0; var8 < var1.size(); ++var8) {
               String[] var9 = (String[])((String[])var1.elementAt(var8));
               var6.add(var9);
               var7.add(new NecroFile(var9[0]));
            }

            String var14 = this.bookModel.cc.getLoadedfile().getName();
            if (var14.endsWith(".frm.enyk")) {
               var14 = var14.substring(0, this.bookModel.cc.getLoadedfile().getName().length() - ".frm.enyk".length()) + ".kr";
            } else {
               var14 = var14.substring(0, this.bookModel.cc.getLoadedfile().getName().length() - ".xml".length()) + ".kr";
            }

            var14 = this.changeFileExtension(this.sp.srcPath + var14, "kr", "xml");
            if (var3) {
               var7.add(new NecroFile(var14));
            }

            var1.clear();
            System.out.println("Lenyomat hitelesítés indul");
            long var15 = System.currentTimeMillis();
            if (var7.size() == 0) {
               System.out.println("Nincs mit aláírni");
            } else {
               if (var4) {
                  AnykCsatolmanyLenyomatSignController var11 = new AnykCsatolmanyLenyomatSignController(var7);
                  var11.controlledSign();
               } else {
                  this.doSignInBackground(var7, true);
               }

               String var16;
               if (this.cmdObject != null) {
                  var16 = this.cmdObject.toString();
                  this.cmdObject = null;
                  throw new SignerException(var16);
               } else {
                  System.out.println("Lenyomat hitelesítés kész: " + (System.currentTimeMillis() - var15) / 1000L + "másodperc");
                  if (var2) {
                     this.changeFileExtension(var14, "xml", "kr");
                  }

                  try {
                     var16 = this.getAndCreateAtcPath(this.bookModel);
                  } catch (FileNotFoundException var13) {
                     var16 = ((File)var7.get(0)).getParentFile().getAbsolutePath() + File.separator;
                     GuiUtil.showMessageDialog(MainFrame.thisinstance, var13.getMessage() + "\nA fájlok a " + var16 + " mappában maradnak, kérjük gondoskodjon róluk!", "Üzenet", 1);
                  }

                  this.removeTempXmlFromAVDHFiles(var7, var14);

                  for(int var12 = 0; var12 < var7.size(); ++var12) {
                     var6.add(new String[]{((File)var7.get(var12)).getAbsolutePath() + ".anyk.ASiC", ".ASiC", "Hitelesített csatolmány lenyomat"});
                  }

                  if (var2) {
                     this.addTempXml2AVDHFiles(var6, var14, var16);
                  }

                  var1.addAll(var6);
               }
            }
         }
      }
   }

   public void handleOtherAVDH(ArrayList<File> var1) throws Exception {
      LoginDialog var2 = LoginDialogFactory.create(GateType.UGYFELKAPU, 0, false, this.bookModel.getTaxNumberHKAzonFromDocInfo());
      if (var2 != null) {
         var2.setLocationRelativeTo(MainFrame.thisinstance);
         if (KauAuthMethods.getSelected() != KauAuthMethod.KAU_DAP) {
            var2.showIfNeed();
            if (var2.getState() == 0) {
               throw new Exception("Felhasználói megszakítás");
            }

            if (var2.getState() == 1) {
               throw new Exception("Hiba az azonosításkor");
            }

            if (var2.getState() == 2) {
               return;
            }
         }

         System.out.println("ASIC-ok további hitelesítése indul");
         long var3 = System.currentTimeMillis();
         this.doSignInBackground(var1, false);
         if (this.cmdObject != null) {
            GuiUtil.showMessageDialog(MainFrame.thisinstance, this.cmdObject, "Hiba", 0);
            String var5 = this.cmdObject.toString();
            this.cmdObject = null;
            throw new Exception(var5);
         } else {
            System.out.println("ASIC-ok további hitelesítése kész: " + (System.currentTimeMillis() - var3) / 1000L + "másodperc");
         }
      }
   }

   private void handleExternal(Vector var1) throws Exception {
      boolean var2 = PropertyList.getInstance().get("prop.dynamic.mohu_user_and_pass_from_batch") != null;
      if (PropertyList.getInstance().get("prop.dynamic.avdhWithNoAuth") != null) {
         if (!var2) {
            PropertyList.getInstance().set("prop.dynamic.avdhWithNoAuth", (Object)null);
         }

      } else {
         Vector var3 = new Vector();
         String var4 = this.bookModel.cc.getLoadedfile().getName();
         var4 = var4.substring(0, var4.length() - ".frm.enyk".length());
         String var5 = this.sp.srcPath + var4 + File.separator + "alairt" + File.separator;
         var5 = Tools.beautyPath(var5);
         String[] var6 = (new NecroFile(var5)).list();
         if (var6.length > 1) {
            throw new Exception("A(z) " + var5 + " mappában egynél több fájl található.\nÍgy nem dönthető el, melyik a nyomtatványhoz tartozó aláírt lenyomat. A művelet nem folytatható!");
         } else if (!this.checkUniqueAttachmentName(var1, var6[0])) {
            throw new AttachementNameException("A(z) " + var5 + " mappában lévő fájl neve megegyezik egy korábban már csatolt fájléval.\nKérjük nevezze át és próbálja újra a műveletet!");
         } else {
            var3.add(new String[]{var5 + var6[0], var6[0].substring(var6[0].lastIndexOf(".")), "Hitelesített csatolmány lenyomat"});
            var1.addAll(var3);
         }
      }
   }

   private String checkAnykAsicInAtcList(Vector var1) {
      StringBuffer var2 = new StringBuffer("");

      for(int var3 = 0; var3 < var1.size(); ++var3) {
         String[] var4 = (String[])((String[])var1.elementAt(var3));
         if (var4[0].toLowerCase().endsWith(".anyk.ASiC".toLowerCase()) || var4[0].toLowerCase().endsWith(".urlap.anyk.xml".toLowerCase())) {
            var2.append(var4[0]).append(", ");
         }
      }

      if (var2.length() > 0) {
         var2.delete(0, 2);
         return "A csatolmányok között .anyk.ASiC vagy .urlap.anyk.xml kiterjesztésű fájl is szerepel. \n- " + var2.toString() + " -\n" + "Az ilyen típusú fájlokat nem lehet csatolmányként feladni.\nHa mégis csatolni kívánja őket, javasoljuk, hogy hagyja el az '.anyk'-t a fájl nevéből (kiterjesztéséből)." + "\nAz átnevezett csatolmányokat kezelni kell a 'Adatok/Csatolmányok kezelése' menüpontban!'";
      } else {
         return null;
      }
   }

   private void addAsicsToAttachmentList(Vector var1) {
      Vector var2 = new Vector();
      boolean var3 = true;

      for(int var4 = 0; var4 < var1.size(); ++var4) {
         String[] var5 = (String[])((String[])var1.elementAt(var4));
         if (var5[0].toLowerCase().endsWith(".anyk.ASiC".toLowerCase()) || var5[0].toLowerCase().endsWith(".urlap.anyk.xml".toLowerCase())) {
            var3 = false;
         }

         if ((new NecroFile(var5[0] + ".anyk.ASiC")).exists()) {
            var2.add(new String[]{var5[0] + ".anyk.ASiC", ".ASiC", "Hitelesített csatolmány lenyomat"});
         }
      }

      if (var3) {
         var1.addAll(var2);
      }

   }

   private void removeTempXmlFromAVDHFiles(ArrayList<File> var1, String var2) {
      File var3 = new NecroFile(var2);
      var1.remove(var3);
   }

   private void addTempXml2AVDHFiles(Vector var1, String var2, String var3) throws Exception {
      File var4 = new NecroFile(var2 + ".anyk.ASiC");
      String var5 = this.bookModel.cc.getLoadedfile().getName();
      var5 = var5.substring(0, var5.length() - ".frm.enyk".length());
      File var6 = new NecroFile(this.sp.srcPath + var5 + File.separator);
      if (!var6.exists()) {
         var6.mkdir();
      }

      var6 = new NecroFile(var6.getAbsolutePath() + File.separator + "alairt");
      if (!var6.exists()) {
         var6.mkdir();
      }

      var6 = new NecroFile(var6.getAbsolutePath() + File.separator + var5 + ".urlap.anyk.ASiC");
      if (var6.exists()) {
         var6.delete();
      }

      if (var4.renameTo(var6)) {
         var1.add(new String[]{var6.getAbsolutePath(), "ASiC", "Hitelesített nyomtatvány lenyomat"});
      } else {
         throw new Exception("Nem sikerült az átnevezés");
      }
   }

   private void dummyAvdhSign(ArrayList<File> var1, String var2) throws IOException {
      for(int var3 = 0; var3 < var1.size(); ++var3) {
         File var4 = new NecroFile(((File)var1.get(var3)).getAbsolutePath() + ".anyk.ASiC");
         FileOutputStream var5 = new NecroFileOutputStream(var4);
         FileInputStream var6 = new FileInputStream(var2 + "\\1signer.asic");
         byte[] var7 = new byte[2048];

         int var8;
         while((var8 = var6.read(var7)) > -1) {
            var5.write(var7, 0, var8);
         }

         var5.close();
         var6.close();
      }

   }

   private void dummyAvdhSign2(ArrayList<File> var1, String var2) {
      for(int var3 = 0; var3 < var1.size(); ++var3) {
         try {
            File var4 = (File)var1.get(var3);
            FileOutputStream var5 = new NecroFileOutputStream(var4);
            FileInputStream var6 = new FileInputStream(var2 + "\\2signer.asic");
            byte[] var7 = new byte[2048];

            int var8;
            while((var8 = var6.read(var7)) > -1) {
               var5.write(var7, 0, var8);
            }

            var5.close();
            var6.close();
         } catch (FileNotFoundException var9) {
            var9.printStackTrace();
         } catch (IOException var10) {
            var10.printStackTrace();
         }
      }

   }

   private String getAndCreateAtcPath(BookModel var1) throws FileNotFoundException {
      String var2 = (String)PropertyList.getInstance().get("prop.usr.root");
      if (!var2.endsWith("\\") && !var2.endsWith("/")) {
         var2 = var2 + File.separator;
      }

      String var3 = (String)PropertyList.getInstance().get("prop.usr.attachment");
      var3 = var2 + var3;
      if (!var3.endsWith("\\") && !var3.endsWith("/")) {
         var3 = var3 + File.separator;
      }

      String var4 = var1.cc.getLoadedfile().getName();
      if (var4.endsWith(".frm.enyk")) {
         var4 = var4.substring(0, var4.length() - ".frm.enyk".length());
      } else if (var4.indexOf(".") > -1) {
         var4 = var4.substring(0, var4.indexOf("."));
      }

      var3 = var3 + var4 + File.separator + var1.get_formid();
      File var5 = new NecroFile(var3);
      if (var5.exists()) {
         return Tools.beautyPath(var3 + File.separator);
      } else if (!var5.mkdirs()) {
         throw new FileNotFoundException("Nem hozható létre " + var3 + " mappa a hitelesítő állománynak.");
      } else {
         return Tools.beautyPath(var3 + File.separator);
      }
   }

   private String changeFileExtension(String var1, String var2, String var3) {
      if (var1.endsWith(var2)) {
         String var4 = var1.substring(0, var1.length() - var2.length()) + var3;
         File var5 = new NecroFile(var4);
         if (var5.exists()) {
            var5.delete();
         }

         File var6 = new NecroFile(var1);
         return var6.renameTo(var5) ? var4 : var1;
      } else {
         return var1;
      }
   }

   private void doSignInBackground(final ArrayList<File> var1, final boolean var2) {
      final JDialog var3 = new JDialog(MainFrame.thisinstance, "AVDH aláírás", true);
      var3.setDefaultCloseOperation(0);
      final boolean[] var4 = new boolean[]{false};
      final SwingWorker var5 = new SwingWorker() {
         public Object doInBackground() throws InterruptedException {
            Thread.sleep(2000L);

            String var2x;
            try {
               System.out.println("SIGN COUNT : " + var1.size());
               AnykCsatolmanyLenyomatSignController var1x = new AnykCsatolmanyLenyomatSignController(var1);
               KauAuthHelper.getInstance().setSaveAuthData(true);
               if (var2) {
                  var1x.controlledSign();
               } else {
                  var1x.controlledSignAsic();
               }

               var2x = null;
               return var2x;
            } catch (Exception var6) {
               var6.printStackTrace();
               var2x = var6.getMessage();
            } finally {
               if (PropertyList.getInstance().get("prop.dynamic.mohu_user_and_pass_from_batch") == null) {
                  KauAuthHelper.getInstance().setSaveAuthData(false);
                  KauSessionTimeoutHandler.getInstance().reset();
                  DapSessionHandler.getInstance().reset();
               }

            }

            return var2x;
         }

         public void done() {
            try {
               Attachements.this.cmdObject = this.get();
            } catch (Exception var3x) {
               var3x.printStackTrace();
               Attachements.this.cmdObject = null;
            }

            var4[0] = true;

            try {
               var3.setVisible(false);
            } catch (Exception var2x) {
               Tools.eLog(var2x, 0);
            }

         }
      };
      var3.addWindowListener(new WindowAdapter() {
         public void windowOpened(WindowEvent var1) {
            var5.execute();
         }
      });
      int var6 = MainFrame.thisinstance.getX() + MainFrame.thisinstance.getWidth() / 2 - 250;
      if (var6 < 0) {
         var6 = 0;
      }

      int var7 = MainFrame.thisinstance.getY() + MainFrame.thisinstance.getHeight() / 2 - 200;
      if (var7 < 0) {
         var7 = 0;
      }

      var3.setBounds(var6, var7, 600, 100);
      var3.setSize(600, 100);
      JPanel var8 = new JPanel((LayoutManager)null, true);
      var8.setLayout(new BoxLayout(var8, 1));
      BevelBorder var9 = new BevelBorder(0);
      var8.setBorder(var9);
      JLabel var10 = new JLabel("A nyomtatvány és a csatolmányok aláírása az AVDH szolgáltatással folyamatban...");
      int var11 = Math.min(GuiUtil.getW(var10, var10.getText()), (int)((double)GuiUtil.getScreenW() * 0.8D));
      Dimension var12 = new Dimension(var11, 70);
      var8.setPreferredSize(var12);
      var10.setPreferredSize(var12);
      var10.setAlignmentX(0.5F);
      var8.add(var10);
      var8.setVisible(true);
      var3.getContentPane().add(var8);
      var4[0] = false;
      var3.pack();
      var3.setVisible(true);
      if (var4[0]) {
         var3.setVisible(false);
      }

   }

   private void doDummySignInBackground(final ArrayList<File> var1, final boolean var2) {
      final JDialog var3 = new JDialog(MainFrame.thisinstance, "AVDH aláírás", true);
      var3.setDefaultCloseOperation(0);
      final boolean[] var4 = new boolean[]{false};
      final SwingWorker var5 = new SwingWorker() {
         public Object doInBackground() throws InterruptedException {
            Thread.sleep(2000L);

            try {
               if (var2) {
                  Attachements.this.dummyAvdhSign(var1, (String)((Vector)PropertyList.getInstance().get("prop.const.avdhDummyDir")).get(0));
               } else {
                  Attachements.this.dummyAvdhSign2(var1, (String)((Vector)PropertyList.getInstance().get("prop.const.avdhDummyDir")).get(0));
               }

               return null;
            } catch (Exception var2x) {
               return var2x.getMessage();
            }
         }

         public void done() {
            try {
               Attachements.this.cmdObject = this.get();
            } catch (Exception var3x) {
               var3x.printStackTrace();
               Attachements.this.cmdObject = null;
            }

            var4[0] = true;

            try {
               var3.setVisible(false);
            } catch (Exception var2x) {
               Tools.eLog(var2x, 0);
            }

         }
      };
      var3.addWindowListener(new WindowAdapter() {
         public void windowOpened(WindowEvent var1) {
            var5.execute();
         }
      });
      int var6 = MainFrame.thisinstance.getX() + MainFrame.thisinstance.getWidth() / 2 - 250;
      if (var6 < 0) {
         var6 = 0;
      }

      int var7 = MainFrame.thisinstance.getY() + MainFrame.thisinstance.getHeight() / 2 - 200;
      if (var7 < 0) {
         var7 = 0;
      }

      JPanel var8 = new JPanel((LayoutManager)null, true);
      var8.setLayout(new BoxLayout(var8, 1));
      BevelBorder var9 = new BevelBorder(0);
      var8.setBorder(var9);
      Dimension var10 = new Dimension(600, 70);
      var8.setPreferredSize(var10);
      JLabel var11 = new JLabel("A nyomtatvány és a csatolmányok aláírása az AVDH szolgáltatással...");
      int var12 = GuiUtil.getW(var11, var11.getText());
      var3.setBounds(var6, var7, var12, 100);
      var3.setSize(600, 100);
      var11.setPreferredSize(var10);
      var11.setAlignmentX(0.5F);
      var8.add(var11);
      var8.setVisible(true);
      var3.getContentPane().add(var8);
      var4[0] = false;
      var3.pack();
      var3.setVisible(true);
      if (var4[0]) {
         var3.setVisible(false);
      }

   }

   private void handleIfExternalSignedXmlHash(Vector<String[]> var1) {
      if (var1.size() >= 1) {
         File var2 = (new NecroFile(((String[])var1.get(0))[0])).getParentFile();
         HashSet var3 = new HashSet();

         for(int var4 = 0; var4 < var1.size(); ++var4) {
            String[] var5 = (String[])var1.elementAt(var4);
            var3.add((new NecroFile(var5[0])).getName().toLowerCase());
         }

         String[] var7 = var2.list();

         for(int var8 = 0; var8 < var7.length; ++var8) {
            if (!var3.contains(var7[var8].toLowerCase())) {
               String var6 = "Ismeretlen csatomanytipus";
               if (var7[var8].toLowerCase().endsWith(".anyk.ASiC".toLowerCase())) {
                  var6 = "Hitelesített csatolmány lenyomat";
               }

               var1.add(new String[]{var2.getAbsolutePath() + File.separator + var7[var8], "", var6});
            }
         }

      }
   }

   private boolean checkUniqueAttachmentName(Vector var1, String var2) {
      for(int var3 = 0; var3 < var1.size(); ++var3) {
         String[] var4 = (String[])((String[])var1.get(var3));
         if (var4[0].endsWith(var2)) {
            return false;
         }
      }

      return true;
   }
}
