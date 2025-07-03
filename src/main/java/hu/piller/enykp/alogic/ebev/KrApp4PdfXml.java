package hu.piller.enykp.alogic.ebev;

import hu.piller.enykp.alogic.calculator.CalculatorManager;
import hu.piller.enykp.alogic.fileloader.xml.XmlLoader;
import hu.piller.enykp.alogic.filepanels.batch.FileLoader4BatchCheck;
import hu.piller.enykp.alogic.filesaver.xml.ErrorListListener4XmlSave;
import hu.piller.enykp.alogic.fileutil.DatastoreKeyToXml;
import hu.piller.enykp.alogic.orghandler.OrgInfo;
import hu.piller.enykp.alogic.orghandler.SuccessorException;
import hu.piller.enykp.alogic.templateutils.TemplateUtils;
import hu.piller.enykp.gui.model.BookModel;
import hu.piller.enykp.interfaces.IErrorList;
import hu.piller.enykp.interfaces.IResult;
import hu.piller.enykp.util.base.ErrorList;
import hu.piller.enykp.util.base.Result;
import hu.piller.enykp.util.base.eventsupport.IEventSupport;
import hu.piller.kripto.keys.KeyWrapper;
import hu.piller.xml.abev.element.DocMetaData;
import java.io.File;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Vector;

public class KrApp4PdfXml {
   private String xmlFilename;
   private String pdfFilename;
   private String krFilename;

   public KrApp4PdfXml(String var1, String var2, String var3) {
      this.xmlFilename = var1;
      this.pdfFilename = var2;
      this.krFilename = var3;
   }

   public Result loadFromFileAndCheck() {
      Result var1 = new Result();
      FileLoader4BatchCheck var2 = new FileLoader4BatchCheck();
      var2.setFileToList(this.xmlFilename);
      XmlLoader var3 = new XmlLoader();
      Hashtable var4 = var2.loadHeadData(new File(this.xmlFilename), var3);

      Hashtable var5;
      try {
         var5 = (Hashtable)var4.get("docinfo");
         if (var5.size() == 0) {
            throw new Exception();
         }
      } catch (Exception var10) {
         var1.errorList.add("Nem sikerült az xml feldolgozása (docinfo)");
         return var1;
      }

      String var6 = var5.containsKey("id") ? (String)var5.get("id") : "";
      String var7 = var5.containsKey("templatever") ? (String)var5.get("templatever") : "";
      String var8 = var5.containsKey("org") ? (String)var5.get("org") : "";
      BookModel var9 = var3.load(this.xmlFilename, var6, var7, var8);
      if (var3.headcheckfatalerror) {
         var9.hasError = true;
         var9.errormsg = var3.errormsg;
      }

      if (var3.fatalerror) {
         var9.hasError = true;
         var9.errormsg = var3.errormsg;
      }

      if (var9.hasError) {
         var1.setOk(false);
         var1.errorList.add(var9.errormsg);
         return var1;
      } else {
         var1 = this.check();
         if (!var1.isOk()) {
            var1.errorList.insertElementAt("Az xml állomány súlyos hibát tartalmaz. A művelet nem folytatható", 0);
            return var1;
         } else {
            return this.createKrFile(var9);
         }
      }
   }

   public Result createKrFile(BookModel var1) {
      Result var2 = EbevTools.checkXSD(this.xmlFilename, false);
      if (!var2.isOk()) {
         return var2;
      } else {
         var2 = EbevTools.checkPanids(var1);
         if (!var2.isOk()) {
            return var2;
         } else {
            try {
               DocMetaData var3 = this.getDMD(var1, var1.id, (String)var1.docinfo.get("org"));
               Enigma var4 = new Enigma(var1);

               try {
                  var2 = var4.getKeyWrappers();
                  if (!var2.isOk()) {
                     return var2;
                  }

                  KeyWrapper[] var5 = (KeyWrapper[])((KeyWrapper[])var2.errorList.remove(0));
                  var4.encrypt(var3, this.pdfFilename, this.krFilename, var5, true, var2);
               } catch (Exception var6) {
                  var2.setOk(false);
                  var2.errorList.add("Nem sikerült a titkosítás!");
               }
            } catch (Exception var7) {
               var2.setOk(false);
               var2.errorList.add("Nem sikerült összeállítani a nyomtatványt kísérő adatokat! Hibás szervezet azonosító! (" + var1.docinfo.get("org") + ")");
            }

            return var2;
         }
      }
   }

   private Result check() {
      IErrorList var1 = ErrorList.getInstance();
      ErrorListListener4XmlSave var2 = new ErrorListListener4XmlSave(-1);
      Vector var3 = null;
      CalculatorManager var4 = CalculatorManager.getInstance();
      var2.clearErrorList();
      if (var3 != null) {
         var3.clear();
      }

      ((IEventSupport)var1).addEventListener(var2);
      CalculatorManager.xml = true;
      var4.do_check_all((IResult)null, var2);
      CalculatorManager.xml = false;
      ((IEventSupport)var1).removeEventListener(var2);
      var3 = var2.getErrorList();
      int var5 = var2.getFatalError();
      Result var6 = new Result();
      var6.setOk(var5 < 1);
      var6.setErrorList(var3);
      return var6;
   }

   private DocMetaData getDMD(BookModel var1, String var2, String var3) {
      EbevTools.orgParams.clear();
      DocMetaData var4 = new DocMetaData();
      EbevTools.getOrgInfo(var3);
      Hashtable var5 = new Hashtable(EbevTools.orgParams.size() / 3);

      for(int var6 = 0; var6 < EbevTools.orgParams.size() / 3; ++var6) {
         var5.put(EbevTools.orgParams.get(3 * var6 + 1), "");
      }

      String var12 = var1.get().toString();
      var5 = (Hashtable)TemplateUtils.getInstance().getEnvelopeData(var12, var5, var1);

      for(int var7 = 0; var7 < EbevTools.orgParams.size() / 3; ++var7) {
         EbevTools.orgParams.setElementAt(var5.get(EbevTools.orgParams.get(3 * var7 + 1)), 3 * var7 + 1);
      }

      EbevTools.putParamIntoOrgParams("abevverzio", "3.44.0");
      String var13 = "";
      var13 = (String)((String)(var1.get_main_elem().getEtc().get("orignote") != null ? var1.get_main_elem().getEtc().get("orignote") : ""));
      var4.setDokTipusAzonosito(var2);
      var4.setDokTipusLeiras(var1.name);
      var4.setDokTipusVerzio((String)var1.docinfo.get("ver"));
      if (OrgInfo.getInstance().hasSuccessor(var3)) {
         try {
            var3 = OrgInfo.getInstance().getSuccessorOrgId(var3);
         } catch (SuccessorException var11) {
            System.out.println("KrApp4PdfXml : " + var11.getMessage());
         }
      }

      String var8 = OrgInfo.getInstance().getKRCimzett(var3);
      var4.setCimzett(var8);
      var4.setFileNev((new File(this.pdfFilename)).getName());
      var13 = DatastoreKeyToXml.htmlCut(var13);
      var4.setMegjegyzes(var13);
      Properties var9 = var4.getParamList();

      for(int var10 = 0; var10 < EbevTools.orgParams.size() / 3; ++var10) {
         if (((String)EbevTools.orgParams.get(3 * var10 + 2)).equalsIgnoreCase("true")) {
            var9.put(EbevTools.orgParams.get(3 * var10), EbevTools.orgParams.get(3 * var10 + 1));
         } else if (!EbevTools.orgParams.get(3 * var10 + 1).equals("")) {
            var9.put(EbevTools.orgParams.get(3 * var10), EbevTools.orgParams.get(3 * var10 + 1));
         }
      }

      var4.setParamLista(var9);
      return var4;
   }
}
