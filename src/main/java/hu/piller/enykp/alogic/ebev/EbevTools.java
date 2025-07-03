package hu.piller.enykp.alogic.ebev;

import com.lowagie.text.pdf.AcroFields;
import com.lowagie.text.pdf.PdfReader;
import hu.piller.enykp.alogic.fileloader.xml.XMLPost;
import hu.piller.enykp.alogic.filepanels.attachement.AttachementTool;
import hu.piller.enykp.alogic.filesaver.enykinner.ENYKClipboardHandler;
import hu.piller.enykp.alogic.filesaver.enykinner.EnykInnerSaver;
import hu.piller.enykp.alogic.filesaver.xml.EnykXmlSaver;
import hu.piller.enykp.alogic.filesaver.xml.EnykXmlSaverParts;
import hu.piller.enykp.alogic.fileutil.DatastoreKeyToXml;
import hu.piller.enykp.alogic.fileutil.FileNameResolver;
import hu.piller.enykp.alogic.fileutil.XsdChecker;
import hu.piller.enykp.alogic.metainfo.MetaInfo;
import hu.piller.enykp.alogic.metainfo.MetaStore;
import hu.piller.enykp.alogic.orghandler.OrgHandler;
import hu.piller.enykp.alogic.orghandler.OrgInfo;
import hu.piller.enykp.alogic.orghandler.SuccessorException;
import hu.piller.enykp.alogic.settingspanel.SettingsStore;
import hu.piller.enykp.alogic.templateutils.TemplateUtils;
import hu.piller.enykp.datastore.Elem;
import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.gui.framework.MainFrame;
import hu.piller.enykp.gui.model.BookModel;
import hu.piller.enykp.gui.model.FormModel;
import hu.piller.enykp.util.base.ErrorList;
import hu.piller.enykp.util.base.PropertyList;
import hu.piller.enykp.util.base.Result;
import hu.piller.enykp.util.base.Tools;
import hu.piller.xml.abev.element.CsatolmanyInfo;
import hu.piller.xml.abev.element.DocMetaData;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Vector;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

public class EbevTools {
   public static Vector orgParams = new Vector();

   public static Vector doCheckAtcFile(BookModel var0, String var1, String var2, int var3) throws AttachementException, Exception {
      Hashtable var4 = var0.get_templateheaddata();
      Hashtable var5 = (Hashtable)var4.get("docinfo");
      if (var0.get(var2).attachement != null) {
         if (var0.splitesaver.equals("false") && var0.isSingle()) {
            var5.put("attachment", getAtcMax(var0));
         } else {
            var5.put("attachment", var0.get(var2).attachement);
         }
      }

      try {
         if (var5.get("attachment").equals("0")) {
            return new Vector();
         }
      } catch (Exception var14) {
         return new Vector();
      }

      String var6;
      String var7;
      if (var1.toLowerCase().indexOf(".frm.enyk") > -1) {
         var7 = var1.substring(0, var1.toLowerCase().indexOf(".frm.enyk")) + ".atc";
         var6 = var1.substring(0, var1.toLowerCase().indexOf(".frm.enyk")) + "_" + (var0.splitesaver.equals("true") ? var2 : var0.get(var2).name) + ".atc";
      } else if (var1.toLowerCase().indexOf(".xml") > -1) {
         var7 = var1.substring(0, var1.toLowerCase().indexOf(".xml")) + ".atc";
         var6 = var1.substring(0, var1.toLowerCase().indexOf(".xml")) + "_" + (var0.splitesaver.equals("true") ? var2 : var0.get(var2).name) + ".atc";
      } else {
         var7 = "";
         var6 = "";
      }

      File var9 = new File(var6);
      if (!var9.exists()) {
         var9 = new File(var7);
         if (!var9.exists()) {
            String var8 = Tools.getTempDir() + File.separator + (new File(var7)).getName();
            var9 = new File(var8);
            if (!var9.exists()) {
               if (var5.get("attachment").equals("1")) {
                  if (var0.isAvdhModel()) {
                     return handleAVDHIfNoAttachment(var0, var3);
                  }

                  return new Vector();
               }

               if (var5.get("attachment").equals("2")) {
                  throw new AttachementException("A nyomtatványhoz kötelező csatolmányt csatolni, de hiányzik a csatolmány-leíró fájl");
               }
            }
         }
      }

      Result var10 = AttachementTool.fullCheck(var0, var9, var2);
      if (!var10.isOk()) {
         throw new AttachementException((String)var10.errorList.get(0));
      } else if (var9.exists() && var10.errorList.size() > 0) {
         Attachements var11 = new Attachements(var0);
         var11.createXml(var10.errorList.get(0), var9);

         try {
            if (PropertyList.getInstance().get("prop.dynamic.ebev_call_from_menu") != null || PropertyList.getInstance().get("prop.dynamic.ebev_call_from_xmlpost") != null) {
               AttachementTool.setAttachmentList(var10.errorList);
            }
         } catch (Exception var13) {
            Tools.eLog(var13, 0);
         }

         Vector var12 = AttachementTool.crateCsatolmanyInfo2DocMetaData(var0.cc.getLoadedfile(), var3);
         return var12;
      } else if (getAttachementInfo(var0, var2).equals("2")) {
         throw new AttachementException("Ehhez a nyomtatványhoz kötelező csatolmányt csatolni!");
      } else {
         return new Vector();
      }
   }

   private static Object getAttachementInfo(BookModel var0, String var1) {
      Hashtable var2 = var0.docinfo;
      if (var0.get(var1).attachement != null) {
         if (var0.splitesaver.equals("false") && var0.isSingle()) {
            var2.put("attachment", getAtcMax(var0));
         } else {
            var2.put("attachment", var0.get(var1).attachement);
         }
      }

      return var2.get("attachment") == null ? "0" : var2.get("attachment");
   }

   public static Result saveFile(BookModel var0, boolean var1, String var2, boolean var3) throws Exception {
      return saveFile(var0, var1, var2, var3, (SendParams)null, (String)null);
   }

   public static Result saveFile(BookModel var0, boolean var1, String var2, boolean var3, SendParams var4, String var5) throws Exception {
      String var6 = null;
      Result var7 = new Result();
      if (var4 != null) {
         var6 = var5;
      } else if (var0.cc.getLoadedfile() != null) {
         var6 = var0.cc.getLoadedfile().getAbsolutePath();
      } else {
         FileNameResolver var8 = new FileNameResolver(var0);
         var6 = var8.generateFileName();
      }

      if (var1) {
         if (var0.splitesaver.equals("true")) {
            EnykXmlSaverParts var9 = new EnykXmlSaverParts(var0, 0);
            if (var6.toLowerCase().indexOf(".frm.enyk") > -1) {
               var6 = var6.substring(0, var6.toLowerCase().indexOf(".frm.enyk")) + var2;
            }

            var6 = PropertyList.USER_IN_FILENAME + (new File(var6)).getName();
            var7 = var9.save(var6, var3, true, var4, var5);
            if (!var7.isOk()) {
               return var7;
            }
         } else {
            EnykXmlSaver var10 = new EnykXmlSaver(var0);
            if (var6.toLowerCase().indexOf(".frm.enyk") > -1) {
               var6 = var6.substring(0, var6.toLowerCase().indexOf(".frm.enyk")) + var2;
            }

            var6 = PropertyList.USER_IN_FILENAME + (new File(var6)).getName();
            var7 = var10.save(var6, var3, true, var4, var5);
            if (!var7.isOk()) {
               return var7;
            }
         }
      } else {
         EnykInnerSaver var11 = new EnykInnerSaver(var0);
         if (!var11.save(var6, var3)) {
            var7.setOk(false);
            var7.errorList.add("Hiba a mentéskor");
            return var7;
         }
      }

      var7.setOk(true);
      if (var0.splitesaver.equals("true")) {
         return var7;
      } else {
         var7.errorList.removeAllElements();
         var7.errorList.add(var6);
         return var7;
      }
   }

   public static DocMetaData getDMD(BookModel var0, String var1, String var2, String var3, Vector var4, int var5) {
      return getDMD(var0, var1, var2, var3, var4, var5, true);
   }

   public static DocMetaData getDMD(BookModel var0, String var1, String var2, String var3, Vector var4, int var5, boolean var6) {
      orgParams.clear();
      DocMetaData var7 = new DocMetaData();
      getOrgInfo(var2);
      Hashtable var8 = new Hashtable(orgParams.size() / 3);

      for(int var9 = 0; var9 < orgParams.size() / 3; ++var9) {
         var8.put(orgParams.get(3 * var9 + 1), "");
      }

      String var18 = getMainFormId(var0);
      var8 = (Hashtable)TemplateUtils.getInstance().getEnvelopeData(var18, var8, var0);

      for(int var10 = 0; var10 < orgParams.size() / 3; ++var10) {
         orgParams.setElementAt(var8.get(orgParams.get(3 * var10 + 1)), 3 * var10 + 1);
      }

      putParamIntoOrgParams("abevverzio", "3.44.0");
      String var19 = "";
      if (var6) {
         var19 = (String)((String)(var0.get_main_elem().getEtc().get("orignote") != null ? var0.get_main_elem().getEtc().get("orignote") : ""));
         var7.setDokTipusAzonosito(var1);
         var7.setDokTipusLeiras(var0.name);
         var7.setDokTipusVerzio((String)var0.docinfo.get("ver"));
      } else {
         Elem var11 = (Elem)var0.cc.get(var5);
         var19 = (String)((String)(var11.getEtc().get("orignote") != null ? ((Elem)var0.cc.get(var5)).getEtc().get("orignote") : ""));
         var7.setDokTipusAzonosito(var11.getType());
         var7.setDokTipusLeiras(var1);
         FormModel var12 = var0.get(var11.getType());

         try {
            var7.setDokTipusVerzio((String)var12.get_docinfodoc().get("ver"));
         } catch (Exception var17) {
            ErrorList.getInstance().writeError(new Long(4001L), "Hibás sablon! Nem határozható meg a nyomtatvány verziója.", var17, (Object)null);
         }
      }

      String var20 = (String)var0.docinfo.get("org");
      if (OrgInfo.getInstance().hasSuccessor(var20)) {
         try {
            var20 = OrgInfo.getInstance().getSuccessorOrgId(var20);
         } catch (SuccessorException var16) {
            System.out.println("EbevTools : " + var16.getMessage());
         }
      }

      String var21 = OrgInfo.getInstance().getKRCimzett(var20);
      var7.setCimzett(var21);
      String var13 = var3.indexOf(File.separator) > -1 ? var3.substring(var3.lastIndexOf(File.separator) + 1) : var3;
      if (var13.endsWith(".kr")) {
         var13 = var13.substring(0, var13.length() - ".kr".length()) + ".xml";
      }

      if (var13.endsWith(".frm.enyk")) {
         var13 = var13.substring(0, var13.length() - ".frm.enyk".length()) + ".xml";
      }

      var7.setFileNev(var13);
      var19 = DatastoreKeyToXml.htmlCut(var19);
      var7.setMegjegyzes(var19);
      Properties var14 = var7.getParamList();

      int var15;
      for(var15 = 0; var15 < orgParams.size() / 3; ++var15) {
         if (((String)orgParams.get(3 * var15 + 2)).equalsIgnoreCase("true")) {
            var14.put(orgParams.get(3 * var15), DatastoreKeyToXml.htmlConvert(orgParams.get(3 * var15 + 1).toString()));
         } else if (!orgParams.get(3 * var15 + 1).equals("")) {
            var14.put(orgParams.get(3 * var15), DatastoreKeyToXml.htmlConvert(orgParams.get(3 * var15 + 1).toString()));
         }
      }

      handlePdfSignInAttachment(var14);
      PropertyList.getInstance().set("prop.dynamic.attached_pdf_names", (Object)null);
      var7.setParamLista(var14);
      if (var4 != null && var4.size() > 0) {
         if (var0.splitesaver.equals("true")) {
            if (var4.elementAt(var5) != null) {
               var7.addCsatInfo((CsatolmanyInfo)var4.elementAt(var5));
            }
         } else {
            for(var15 = 0; var15 < var4.size(); ++var15) {
               if (var4.elementAt(var15) != null) {
                  var7.addCsatInfo((CsatolmanyInfo)var4.elementAt(var15));
               }
            }
         }
      }

      return var7;
   }

   private static String getMainFormId(BookModel var0) {
      try {
         int var1 = var0.get_main_index();
         if (var1 > -1) {
            String var2 = ((Elem)var0.cc.get(var1)).getType();
            return var0.get(var2).toString();
         }
      } catch (Exception var3) {
         Tools.eLog(var3, 0);
      }

      return var0.get().toString();
   }

   public static void getOrgInfo(String var0) {
      OrgInfo var1 = OrgInfo.getInstance();
      Hashtable var2 = (Hashtable)var1.getOrgInfo(OrgHandler.getInstance().getReDirectedOrgId(var0));
      req(var2);
   }

   private static void req(Hashtable var0) {
      if (var0.containsKey("attributes") && var0.get("attributes") instanceof Hashtable) {
         req((Hashtable)var0.get("attributes"));
      }

      if (var0.containsKey("children") && var0.get("children") instanceof Vector) {
         Vector var1 = (Vector)var0.get("children");

         for(int var2 = 0; var2 < var1.size(); ++var2) {
            Object var3 = var1.elementAt(var2);
            if (var3 instanceof Hashtable) {
               req((Hashtable)var1.elementAt(var2));
            }
         }
      }

      if (var0.containsKey("parameter") && var0.get("parameter") instanceof Hashtable) {
         req((Hashtable)var0.get("parameter"));
      }

      if (var0.containsKey("nev")) {
         if (var0.get("nev") instanceof Hashtable) {
            req((Hashtable)var0.get("nev"));
         } else {
            orgParams.add(var0.get("nev"));
         }
      }

      if (var0.containsKey("ertek")) {
         if (var0.get("ertek") instanceof Hashtable) {
            req((Hashtable)var0.get("ertek"));
         } else {
            orgParams.add(var0.get("ertek"));
         }
      }

      if (var0.containsKey("req")) {
         if (var0.get("req") instanceof Hashtable) {
            req((Hashtable)var0.get("req"));
         } else {
            orgParams.add(var0.get("req"));
         }
      }

   }

   public static void delFile(String var0) {
      try {
         File var1;
         if (var0.endsWith(".frm.enyk")) {
            var0 = var0.substring(0, var0.length() - ".frm.enyk".length());

            for(int var2 = 0; var2 < 3; ++var2) {
               var1 = new File(var0 + "_" + var2 + "_p" + ".kr");
               var1.delete();
               var1 = new File(var0 + "_" + var2 + "_p" + ".xml");
               var1.delete();
               var1 = new File(var0 + "_" + var2 + "_p" + ".mf");
               var1.delete();
            }
         } else {
            var1 = new File(var0);
            var1.delete();
         }
      } catch (Exception var3) {
         Tools.eLog(var3, 0);
      }

   }

   public static byte[] getCertBytes(String var0) {
      byte[] var1 = null;

      try {
         if (OrgInfo.getInstance().hasSuccessor(var0)) {
            var0 = OrgInfo.getInstance().getSuccessorOrgId(var0);
         }

         var1 = (byte[])((byte[])OrgInfo.getInstance().getCertName(var0));
      } catch (SuccessorException var3) {
         System.out.println("EbevTools.getCertBytes() : " + var3.getMessage());
      } catch (Exception var4) {
         Tools.eLog(var4, 0);
      }

      return var1;
   }

   public static Result checkPanids(BookModel var0) {
      Result var1 = new Result();
      if (OrgHandler.getInstance().isNotGeneralOrg((String)var0.docinfo.get("org"))) {
         MetaStore var2 = MetaInfo.getInstance().getMetaStore(var0.get_main_formmodel().id);
         Vector var3 = var2.getFilteredFieldMetas_And(new Vector(Arrays.asList("panids")));
         if (var3.size() == 0) {
            var1.setOk(false);
            var1.errorList.add("A nyomtatvány nem adható fel. A nyomtatvány-sablonban nincs megjelölt törzsadat mező.");
            return var1;
         }

         boolean var4 = false;
         boolean var5 = false;
         boolean var6 = false;
         boolean var7 = false;
         boolean var8 = false;

         for(int var9 = 0; var9 < var3.size(); ++var9) {
            Hashtable var10 = (Hashtable)var3.elementAt(var9);
            String var11 = (String)var10.get("panids");
            if (var11.indexOf("Adózó adószáma") > -1) {
               try {
                  var4 = !getData(var0, (String)var10.get("fid")).equals("");
               } catch (Exception var17) {
                  var4 = false;
               }
            }

            if (var11.indexOf("Adózó adóazonosító jele") > -1) {
               try {
                  var5 = !getData(var0, (String)var10.get("fid")).equals("");
               } catch (Exception var16) {
                  var5 = false;
               }
            }

            if (var11.indexOf("Közösségi adószám") > -1) {
               try {
                  var6 = !getData(var0, (String)var10.get("fid")).equals("");
               } catch (Exception var15) {
                  var6 = false;
               }
            }

            if (var11.indexOf("Technikai azonosítás") > -1) {
               try {
                  var7 = !getData(var0, (String)var10.get("fid")).equals("");
               } catch (Exception var14) {
                  var7 = false;
               }
            }

            if (var11.indexOf("VPID") > -1) {
               try {
                  var8 = !getData(var0, (String)var10.get("fid")).equals("");
               } catch (Exception var13) {
                  var8 = false;
               }
            }
         }

         var1.setOk(var5 || var4 || var6 || var7 || var8);
         if (!var1.isOk()) {
            var1.errorList.add("Meg kell adnia az adóazonosító-jelet/számot vagy VPID-t!  A megjelölés nem lehetséges!");
         }
      }

      return var1;
   }

   private static String getData(BookModel var0, String var1) {
      return var0.get_main_document().get("0_" + var1);
   }

   public static Result checkXSD(String var0) {
      return checkXSD(var0, true);
   }

   public static Result checkXSD(String var0, boolean var1) {
      Result var2 = new Result();

      try {
         XsdChecker var11 = new XsdChecker();
         File var4 = new File(var0);
         String var5 = var11.getEncoding(var4);
         var2 = var11._load(new FileInputStream(var4), var5);
         if (!var2.isOk()) {
            String var6 = "";

            try {
               var6 = (String)var2.errorList.elementAt(0);
            } catch (Exception var9) {
               Tools.eLog(var9, 0);
            }

            throw new Exception(var6 == null ? "" : var6);
         }
      } catch (Exception var10) {
         Exception var3 = var10;
         var2.setOk(false);
         delFile(var0);

         try {
            System.out.println("xsd ell.: " + var3.getMessage());
            ErrorList.getInstance().writeError(new Long(4001L), "XSD hiba", var3, (Object)null);
         } catch (Exception var8) {
            Tools.eLog(var8, 0);
         }

         if (XMLPost.xmleditnemjo) {
            var2.errorList.insertElementAt("Ebből az adat állományból nem lehet megfelelő formátumú XML állományt előállítani.\nA hibát okozhatja pl: név, adószám, adóazonosítójel, időszak kitöltöttségének hiánya.\nXSD hiba: Részletes leírást a Szerviz/Üzenetek menüpontban talál.", 0);
         } else {
            var2.errorList.insertElementAt("Ebből a nyomtatvány-sablonból nem készíthető megfelelő formátumú xml állomány!\nA nyomtatvány nem jelölhető meg feladásra.\nXSD hiba" + (var1 ? " : Részletes leírást a Szerviz/Üzenetek menüpontban talál" : ""), 0);
         }
      }

      return var2;
   }

   public static String getFileEncoding(String var0) {
      try {
         XsdChecker var1 = new XsdChecker();
         File var2 = new File(var0);
         return var1.getEncoding(var2);
      } catch (Exception var3) {
         return "utf-8";
      }
   }

   public static void putParamIntoOrgParams(String var0, String var1) {
      try {
         int var2 = orgParams.indexOf(var0);
         if (var2 != -1 && var2 % 3 == 0) {
            orgParams.setElementAt(var1, var2 + 1);
         } else {
            orgParams.add(var0);
            orgParams.add(var1);
            orgParams.add("true");
         }
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }

   private static String getAtcMax(BookModel var0) {
      int var1 = 0;

      for(int var2 = 0; var2 < var0.size(); ++var2) {
         try {
            var1 = Math.max(Integer.parseInt(var0.get(var2).attachement), var1);
         } catch (Exception var4) {
            var1 = 0;
         }
      }

      return "" + var1;
   }

   public static void handleClipboard(File var0) {
      String var1 = null;

      try {
         var1 = (String)((Vector)PropertyList.getInstance().get("prop.const.fileNameToClipBoard")).get(0);
      } catch (Exception var5) {
         var1 = "";
      }

      if ("igen".equals(var1.toLowerCase())) {
         ENYKClipboardHandler var2 = new ENYKClipboardHandler();
         var2.setClipboardContents(var0.toString());
         String var3 = var2.getClipboardContents();
         String var4;
         if (var3.equals(var0.toString())) {
            var4 = "Az állomány nevének vágólapra másolása sikeres!\n\n" + var0.getParent() + "\n" + var0.getName();
            GuiUtil.showMessageDialog(MainFrame.thisinstance, var4, "Üzenet", 1);
         } else {
            var4 = "Az állomány nevének vágólapra másolása sikertelen!";
            GuiUtil.showMessageDialog(MainFrame.thisinstance, var4, "Üzenet", 0);
         }
      }

   }

   public static boolean eSendDisabled(BookModel var0) {
      Hashtable var1 = MetaInfo.getInstance().getFieldAttributes(var0.get_main_formmodel().id, "panids", true);
      String var2 = null;
      Enumeration var3 = var1.keys();

      while(var3.hasMoreElements() && var2 == null) {
         Object var4 = var3.nextElement();
         if (var1.get(var4).equals("Elektronikus feladás tiltása")) {
            var2 = (String)var4;
         }
      }

      if (var2 == null) {
         return false;
      } else {
         String var5 = var0.get_main_document().get("0_" + var2);
         return var5 == null ? false : var5.equalsIgnoreCase("true");
      }
   }

   public static String createNewFilename(String var0, BookModel var1) {
      var0 = var0.substring(0, var0.length() - 3) + "_" + System.currentTimeMillis() + ".kr";
      var1.cc.docinfo.put("krfilename", var0);
      return var0;
   }

   public static void delCstFile(BookModel var0) {
      try {
         String var1 = var0.cc.getLoadedfile().getAbsolutePath();
         var1 = var1.substring(0, var1.length() - ".frm.enyk".length());
         File var2 = new File(var1);
         File[] var3 = var2.listFiles(new EbevTools.CSTFilenameFilter());

         for(int var4 = 0; var4 < var3.length; ++var4) {
            if (!var3[var4].delete() && !var3[var4].delete()) {
               System.out.println("A " + var3[var4] + " fájl törlése nem sikerült!");
            }
         }
      } catch (Exception var5) {
         Tools.eLog(var5, 0);
      }

   }

   public static Result checkAttachment(BookModel var0, String var1, String var2) throws AttachementException, Exception {
      Result var3 = new Result();
      Hashtable var4 = var0.get_templateheaddata();
      Hashtable var5 = (Hashtable)var4.get("docinfo");
      if (var0.get(var2).attachement == null) {
         return var3;
      } else {
         if (var0.splitesaver.equals("false") && var0.isSingle()) {
            var5.put("attachment", getAtcMax(var0));
         } else {
            var5.put("attachment", var0.get(var2).attachement);
         }

         try {
            if (var5.get("attachment").equals("0")) {
               return var3;
            }
         } catch (Exception var11) {
            return var3;
         }

         String var6;
         String var7;
         if (var1.toLowerCase().indexOf(".frm.enyk") > -1) {
            var7 = var1.substring(0, var1.toLowerCase().indexOf(".frm.enyk")) + ".atc";
            var6 = var1.substring(0, var1.toLowerCase().indexOf(".frm.enyk")) + "_" + (var0.splitesaver.equals("true") ? var2 : var0.get(var2).name) + ".atc";
         } else if (var1.toLowerCase().indexOf(".xml") > -1) {
            var7 = var1.substring(0, var1.toLowerCase().indexOf(".xml")) + ".atc";
            var6 = var1.substring(0, var1.toLowerCase().indexOf(".xml")) + "_" + (var0.splitesaver.equals("true") ? var2 : var0.get(var2).name) + ".atc";
         } else {
            var7 = "";
            var6 = "";
         }

         File var9 = new File(var6);
         if (!var9.exists()) {
            var9 = new File(var7);
            if (!var9.exists()) {
               String var8 = Tools.getTempDir() + File.separator + (new File(var7)).getName();
               var9 = new File(var8);
               if (!var9.exists()) {
                  boolean var10 = checkRequiredAttrib(var0, var2);
                  if (var5.get("attachment").equals("1") && !var10) {
                     return var3;
                  }

                  if (var5.get("attachment").equals("2") || var10) {
                     var3.errorList.clear();
                     var3.setOk(false);
                     var3.errorList.add("A nyomtatványhoz (" + var2 + ") kötelező csatolmányt csatolni, de ez nem történt meg.");
                     return var3;
                  }
               }
            }
         }

         return AttachementTool.fullCheck(var0, var9, var2);
      }
   }

   public static Vector createCstFile(BookModel var0, String var1, Object var2, File var3, int var4) throws AttachementException {
      if (var3.exists()) {
         Attachements var5 = new Attachements(var0);

         try {
            var5.createXml(var2, var3);
            Vector var6 = AttachementTool.crateCsatolmanyInfo2DocMetaData(var0.cc.getLoadedfile(), var4);
            return var6;
         } catch (Exception var7) {
         }
      }

      if (getAttachementInfo(var0, var1).equals("2")) {
         throw new AttachementException("Ehhez a nyomtatványhoz kötelező csatolmányt csatolni!");
      } else {
         return new Vector();
      }
   }

   private static boolean checkRequiredAttrib(BookModel var0, String var1) {
      try {
         FormModel var2 = var0.get(var1);
         int var3 = var0.getIndex(var2);
         Vector var4 = (Vector)var0.attachementsall.elementAt(var3);
         Hashtable var5 = (Hashtable)var4.elementAt(1);
         return var5.get("required").equals("1");
      } catch (Exception var6) {
         return false;
      }
   }

   public static String getAtcListFromVector(boolean var0) {
      String var1 = "";

      Vector var2;
      try {
         var2 = (Vector)PropertyList.getInstance().get("prop.dynamic.ebev_call_from_menu");
      } catch (Exception var5) {
         var2 = new Vector();
      }

      try {
         for(int var3 = 0; var3 < var2.size(); ++var3) {
            String var4 = (String)var2.elementAt(var3);
            var1 = var1 + "\n " + var4;
         }

         if (var1.length() > 0) {
            if (var0 && var2.size() == 2) {
               var1 = "\nA beküldött állomány lenyomata és annak elektronikus aláírása az alábbi helyen található:" + var1;
            }

            var1 = "\nAz alábbi csatolmányokat adtuk a titkosított állományhoz:" + var1;
         }
      } catch (Exception var6) {
         var1 = "";
      }

      PropertyList.getInstance().set("prop.dynamic.ebev_call_from_menu", (Object)null);
      return var1;
   }

   public static Vector getAtcListFromVectorAsVector(boolean var0) {
      Vector var1;
      try {
         var1 = (Vector)PropertyList.getInstance().get("prop.dynamic.ebev_call_from_menu");
      } catch (Exception var3) {
         var1 = new Vector();
      }

      if (var1.size() > 0) {
         if (var0 && var1.size() == 2) {
            var1.insertElementAt("A beküldött állomány lenyomata és annak elektronikus aláírása az alábbi helyen található:", 0);
         } else {
            var1.insertElementAt("Az alábbi csatolmányokat adtuk a titkosított állományhoz:", 0);
         }
      }

      PropertyList.getInstance().set("prop.dynamic.ebev_call_from_menu", (Object)null);
      return var1;
   }

   private static void handlePdfSignInAttachment(Properties var0) {
      boolean var1 = false;

      for(int var2 = 0; var2 < orgParams.size() / 3; ++var2) {
         if (((String)orgParams.get(3 * var2)).equalsIgnoreCase("alairtjelzo")) {
            var0.put(orgParams.get(3 * var2), "0");
            var1 = true;
            break;
         }
      }

      if (var1) {
         if (PropertyList.getInstance().get("prop.dynamic.attached_pdf_names") != null) {
            Vector var7 = (Vector)PropertyList.getInstance().get("prop.dynamic.attached_pdf_names");
            boolean var3 = false;

            for(int var4 = 0; var4 < var7.size(); ++var4) {
               try {
                  var3 = var3 || verifyPdf((String)var7.elementAt(var4));
               } catch (Exception var6) {
                  var3 = var3;
               }
            }

            var0.put("alairtjelzo", var3 ? "1" : "0");
         }
      }
   }

   private static boolean verifyPdf(String var0) {
      try {
         PdfReader var1 = new PdfReader(var0);
         AcroFields var2 = var1.getAcroFields();
         return var2.getSignatureNames().size() > 0;
      } catch (Exception var3) {
         return false;
      }
   }

   public static String getKrTargetFilename(String var0) {
      String var1;
      try {
         var1 = SettingsStore.getInstance().get("attachments", "attachment.temporary.directory");
         if (var1.equals("")) {
            throw new Exception();
         }

         if (!(new File(var1)).exists()) {
            throw new Exception();
         }

         var1 = var1 + "\\AnykTempKRFile.tmp";
      } catch (Exception var3) {
         var1 = var0;
      }

      return var1;
   }

   public static void copyFile(String var0, String var1, boolean var2) throws IOException {
      File var3 = new File(var0);
      FileInputStream var4 = new FileInputStream(var3);
      FileOutputStream var5 = new FileOutputStream(var1);
      byte[] var6 = new byte[2048];
      boolean var7 = false;

      int var8;
      while((var8 = var4.read(var6)) > -1) {
         var5.write(var6, 0, var8);
      }

      var4.close();
      var5.close();
      if (var2) {
         var3.delete();
      }

   }

   public static String checkIfFileHasAtc(BookModel var0) {
      String var1 = null;

      try {
         var1 = var0.cc.getLoadedfile().getAbsolutePath();
      } catch (Exception var11) {
         return "Beküldés előtt kérem mentse a nyomtatványt! Addig a művelet nem folytatható.";
      }

      String var2 = var0.get_formid();
      Hashtable var3 = var0.get_templateheaddata();
      Hashtable var4 = (Hashtable)var3.get("docinfo");
      if (var0.get(var2).attachement != null) {
         if (var0.splitesaver.equals("false") && var0.isSingle()) {
            var4.put("attachment", getAtcMax(var0));
         } else {
            var4.put("attachment", var0.get(var2).attachement);
         }
      }

      try {
         if (var4.get("attachment").equals("1")) {
            return null;
         }
      } catch (Exception var12) {
         return "Sablon hiba! Nem lehet ellenőrizni, hogy kötelező e csatolmányt csatolni a nyomtatványhoz.";
      }

      String var5;
      String var6;
      if (var1.toLowerCase().indexOf(".frm.enyk") > -1) {
         var6 = var1.substring(0, var1.toLowerCase().indexOf(".frm.enyk")) + ".atc";
         var5 = var1.substring(0, var1.toLowerCase().indexOf(".frm.enyk")) + "_" + (var0.splitesaver.equals("true") ? var2 : var0.get(var2).name) + ".atc";
      } else if (var1.toLowerCase().indexOf(".xml") > -1) {
         var6 = var1.substring(0, var1.toLowerCase().indexOf(".xml")) + ".atc";
         var5 = var1.substring(0, var1.toLowerCase().indexOf(".xml")) + "_" + (var0.splitesaver.equals("true") ? var2 : var0.get(var2).name) + ".atc";
      } else {
         var6 = "";
         var5 = "";
      }

      File var8 = new File(var5);
      if (!var8.exists()) {
         var8 = new File(var6);
         if (!var8.exists()) {
            String var7 = Tools.getTempDir() + File.separator + (new File(var6)).getName();
            var8 = new File(var7);
            if (!var8.exists()) {
               try {
                  if (var4.get("attachment").equals("2")) {
                     return "A nyomtatványhoz nem tartozik csatolmány, pedig a sablon szerint kötelező csatolni! A művelet így nem folytatható.";
                  }
               } catch (Exception var10) {
                  return "Sablon hiba! Nem lehet ellenőrizni, hogy kötelező e csatolmányt csatolni a nyomtatványhoz.";
               }
            }
         }
      }

      return null;
   }

   public static void showResultDialog(Vector var0) {
      int var1 = Math.min(660, var0.size() * (GuiUtil.getCommonItemHeight() + 4) + 80);
      final JDialog var2 = new JDialog(MainFrame.thisinstance, "Üzenet", true);
      JList var3 = new JList(var0);
      var3.setAlignmentX(0.0F);
      JScrollPane var4 = new JScrollPane(var3, 20, 30);
      var4.setBorder(new EmptyBorder(15, 15, 15, 15));
      int var5 = getListWidthInPixel(var0, var3) + 50;
      var4.setPreferredSize(new Dimension(var5, var1));
      final JButton var6 = new JButton("Ok");
      var6.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            var2.setVisible(false);
            var2.dispose();
         }
      });
      JPanel var7 = new JPanel();
      var3.setBackground(var7.getBackground());
      var3.setFont(var6.getFont());
      var7.add(var6);
      var2.getContentPane().add(var4, "Center");
      var2.getContentPane().add(var7, "South");
      var2.setSize(var5, var1 + 50);
      var2.setPreferredSize(var2.getSize());
      var2.setLocationRelativeTo(MainFrame.thisinstance);
      var2.setResizable(true);
      var2.addWindowListener(new WindowAdapter() {
         public void windowActivated(WindowEvent var1) {
            var6.requestFocus();
         }
      });
      var2.setVisible(true);
   }

   private static int getListWidthInPixel(Vector var0, Component var1) {
      short var2 = 400;
      String var3 = "";

      for(int var4 = 0; var4 < var0.size(); ++var4) {
         if (var0.get(var4).toString().length() > var3.length()) {
            var3 = var0.get(var4).toString();
         }
      }

      var3 = var3 + "WWW";

      int var6;
      try {
         var6 = Math.max(var2, SwingUtilities.computeStringWidth(MainFrame.thisinstance.getGraphics().getFontMetrics(var1.getFont()), var3));
      } catch (Exception var5) {
         return (int)((double)GuiUtil.getScreenW() * 0.8D);
      }

      return Math.min(var6, (int)((double)GuiUtil.getScreenW() * 0.8D));
   }

   private static Vector handleAVDHIfNoAttachment(BookModel var0, int var1) throws Exception {
      File var2 = var0.cc.getLoadedfile();
      Attachements var3 = new Attachements(var0);
      var3.createXml((Object)null, var2);
      Vector var4 = AttachementTool.crateCsatolmanyInfo2DocMetaData(var2, var1);
      return var4;
   }

   public static void showExtendedResultDialog(JDialog var0, String var1, Vector var2, int var3) {
      String var6 = "OptionPane.informationIcon";
      switch(var3) {
      case 0:
         var6 = "OptionPane.errorIcon";
         break;
      case 2:
         var6 = "OptionPane.warningIcon";
      }

      final JDialog var4;
      if (var0 == null) {
         var4 = new JDialog(MainFrame.thisinstance, "Üzenet", true);
      } else {
         var4 = new JDialog(var0, "Üzenet", true);
      }

      EmptyBorder var7 = new EmptyBorder(15, 15, 15, 15);
      var4.getContentPane().setLayout(new BoxLayout(var4.getContentPane(), 1));
      JPanel var8 = new JPanel(new BorderLayout());
      Icon var9 = UIManager.getIcon(var6);
      JLabel var10 = new JLabel(var9);
      var10.setBorder(var7);
      var8.add(var10, "West");
      JPanel var11 = getMessagePanel(var1);
      var11.setBorder(var7);
      int var5 = (int)Math.max((double)UIManager.getIcon(var6).getIconHeight(), var11.getSize().getHeight()) + 30;
      var8.add(var11, "Center");
      var4.getContentPane().add(var8);
      JPanel var12 = new JPanel(new BorderLayout());
      JPanel var13 = new JPanel();
      var13.setLayout(new BoxLayout(var13, 0));
      final JButton var14 = new JButton("Ok");
      var14.setAlignmentX(0.5F);
      var14.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            var4.setVisible(false);
            var4.dispose();
         }
      });
      final JButton var15 = new JButton("Részletek >>>");
      int var16 = 400;
      final int var17 = Math.min(400, (int)((double)(var2.size() + 2) * 1.5D * (double)GuiUtil.getCommonItemHeight()));
      if (var2.size() > 0) {
         var13.add(Box.createHorizontalStrut(20));
         var13.add(var15);
         var13.add(Box.createHorizontalGlue());
         var13.add(var14);
         var13.add(Box.createHorizontalStrut(20));
         var12.add(var13, "North");
         JList var18 = new JList(var2);
         var18.setAlignmentX(0.0F);
         var18.setBackground(var15.getBackground());
         var18.setForeground(var15.getForeground());
         final JScrollPane var19 = new JScrollPane(var18, 20, 30);
         var19.setBorder(var7);
         var16 = getListWidthInPixel(var2, var18) + 50;
         var19.setPreferredSize(new Dimension(var16, var17));
         var19.setAlignmentX(0.5F);
         var18.setFont(var14.getFont());
         var12.add(var19, "Center");
         var19.setVisible(false);
         final boolean[] var20 = new boolean[]{false};
         var15.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent var1) {
               if (var20[0]) {
                  var4.setSize((int)var4.getSize().getWidth(), (int)var4.getSize().getHeight() - var17);
                  var19.setVisible(false);
                  var15.setText("Részletek >>>");
               } else {
                  var4.setSize((int)var4.getSize().getWidth(), (int)var4.getSize().getHeight() + var17);
                  var19.setVisible(true);
                  var15.setText("Részletek <<<");
               }

               var20[0] = !var20[0];
            }
         });
      } else {
         var13.add(Box.createHorizontalGlue());
         var13.add(var14);
         var13.add(Box.createHorizontalGlue());
         var4.getContentPane().add(Box.createVerticalStrut(40));
         var12.add(var13, "North");
      }

      var4.getContentPane().add(var12);
      var5 += 4 * GuiUtil.getCommonItemHeight();
      var4.setSize(var16, var5);
      var4.setPreferredSize(var4.getSize());
      var4.setMinimumSize(var4.getSize());
      var4.setLocationRelativeTo(MainFrame.thisinstance);
      var4.setResizable(true);
      var4.addWindowListener(new WindowAdapter() {
         public void windowActivated(WindowEvent var1) {
            var14.requestFocus();
         }
      });
      var4.setVisible(true);
   }

   private static int calculateLabelHeight(JLabel var0, String var1, int var2) {
      try {
         int var3 = 0;
         int var4 = var1.indexOf("\n");
         int var5 = 0;
         FontMetrics var6 = MainFrame.thisinstance.getGraphics().getFontMetrics(var0.getFont());

         int var7;
         for(var7 = 0; var4 > -1; ++var3) {
            var7 += var6.stringWidth(var1.substring(var5, var4)) / var2;
            var1 = var1.replaceFirst("\n", "");
            var5 = var4;
            var4 = var1.indexOf("\n");
         }

         var7 += var6.stringWidth(var1.substring(var5)) / var2;
         return (var7 + var3 + 1) * (var6.getHeight() + 5);
      } catch (Exception var8) {
         return 0;
      }
   }

   private static JPanel getMessagePanel(String var0) {
      JPanel var1 = new JPanel();
      var1.setLayout(new BoxLayout(var1, 1));
      String[] var2 = var0.split("\n");
      String var3 = "";

      for(int var4 = 0; var4 < var2.length; ++var4) {
         if (var2[var4].length() > var3.length()) {
            var3 = var2[var4];
         }

         var1.add(new JLabel(var2[var4]));
      }

      var1.setSize(new Dimension(GuiUtil.getW("WWW" + var3 + "WWW"), var2.length * (GuiUtil.getCommonItemHeight() + 4)));
      return var1;
   }

   static class CSTFilenameFilter implements FilenameFilter {
      public boolean accept(File var1, String var2) {
         return this.isCSTFilename(var2);
      }

      private boolean isCSTFilename(String var1) {
         return var1.endsWith(".cst");
      }
   }
}
