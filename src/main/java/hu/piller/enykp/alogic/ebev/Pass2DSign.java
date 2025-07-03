package hu.piller.enykp.alogic.ebev;

import hu.piller.enykp.alogic.filepanels.attachement.AttachementTool;
import hu.piller.enykp.alogic.fileutil.DatastoreKeyToXml;
import hu.piller.enykp.alogic.orghandler.OrgInfo;
import hu.piller.enykp.alogic.orghandler.SuccessorException;
import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.gui.framework.MainFrame;
import hu.piller.enykp.gui.model.BookModel;
import hu.piller.enykp.gui.model.FormModel;
import hu.piller.enykp.interfaces.IPropertyList;
import hu.piller.enykp.util.base.PropertyList;
import hu.piller.enykp.util.base.Result;
import hu.piller.enykp.util.base.Tools;
import hu.piller.xml.abev.element.DocMetaData;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;

public class Pass2DSign {
   private static SendParams sp;
   private BookModel bookModel;
   private IPropertyList mpl = PropertyList.getInstance();

   public Pass2DSign(BookModel var1, SendParams var2, IPropertyList var3) {
      sp = var2;
      this.bookModel = var1;
      this.mpl = var3;
   }

   public Result pass() {
      return this.pass(false);
   }

   public Result pass(boolean var1) {
      Result var2 = new Result();
      String var3 = this.bookModel.cc.getLoadedfile().getAbsolutePath();
      String var4 = null;

      Vector var5;
      label427: {
         Result var43;
         try {
            Result var6;
            try {
               PropertyList.getInstance().set("prop.dynamic.pass2DSign", true);
               PropertyList.getInstance().set("prop.dynamic.ebev_call_from_menu", Boolean.TRUE);
               boolean var42 = false;
               if (this.bookModel.isSingle()) {
                  var5 = EbevTools.doCheckAtcFile(this.bookModel, var3, this.bookModel.get_formid(), 0);
               } else {
                  new Vector();
                  Object var7 = this.bookModel.cc.getActiveObject();
                  int var8 = Math.min(this.bookModel.forms.size(), this.bookModel.cc.size());

                  for(int var9 = 0; var9 < var8; ++var9) {
                     try {
                        FormModel var10 = (FormModel)this.bookModel.forms.elementAt(var9);
                        Result var11 = EbevTools.checkAttachment(this.bookModel, var3, var10.id);
                        var2.errorList.addAll(var11.errorList);
                        if (!var11.isOk()) {
                           var42 = true;
                           Result var12 = var11;
                           return var12;
                        }
                     } catch (Exception var35) {
                        Tools.eLog(var35, 0);
                        var42 = true;
                     }
                  }

                  if (var42) {
                     var2.setOk(false);
                     var2.errorList.clear();
                     var2.errorList.add("Hiba történt a csatolmányok feldolgozásakor!");
                     Result var46 = var2;
                     return var46;
                  }

                  try {
                     var5 = AttachementTool.mergeCstFiles(var2.errorList);
                  } catch (Exception var34) {
                     var2.setOk(false);
                     var2.errorList.clear();
                     var2.errorList.add(var34.getMessage());
                     Result var47 = var2;
                     return var47;
                  }

                  if (var5.size() > 0) {
                     var5 = EbevTools.createCstFile(this.bookModel, this.bookModel.main_document_id, var5, new File(var3), 0);
                  }
               }

               if (PropertyList.getInstance().get("prop.dynamic.ebev_call_from_menu") != null || PropertyList.getInstance().get("prop.dynamic.ebev_call_from_xmlpost") != null) {
                  AttachementTool.setAttachmentList(var2.errorList);
               }

               if (var5 == null) {
                  var2.setOk(false);
                  var2.errorList.add("A nyomtatvány átadása nem sikerült! Nem található a csatolmány-leíróban szereplő fájl!");
                  var43 = var2;
                  return var43;
               }

               var2 = EbevTools.saveFile(this.bookModel, true, ".xml", var1);
               if (!var2.isOk()) {
                  try {
                     EbevTools.delFile(sp.srcPath + PropertyList.USER_IN_FILENAME + var4);
                  } catch (Exception var33) {
                     Tools.eLog(var33, 0);
                  }

                  var43 = var2;
                  return var43;
               }

               var4 = ((String)var2.errorList.get(0)).toLowerCase();
               var2 = EbevTools.checkXSD(sp.srcPath + var4);
               if (var2.isOk()) {
                  var2 = EbevTools.checkPanids(this.bookModel);
                  if (!var2.isOk()) {
                     EbevTools.delFile(sp.srcPath + var4);
                     var43 = var2;
                     return var43;
                  }

                  try {
                     DocMetaData var44 = EbevTools.getDMD(this.bookModel, this.bookModel.id, (String)this.bookModel.docinfo.get("org"), var3, var5, 0, true);
                     this.createMFFile(this.bookModel, sp.srcPath + var4.substring(var4.lastIndexOf(File.separator) + 1, var4.lastIndexOf(".xml")) + ".mf", var44, var5);
                     break label427;
                  } catch (Exception var36) {
                     var2.setOk(false);
                     var2.errorList.add("Nem sikerült összeállítani a nyomtatványt kísérő adatokat! Hibás szervezet azonosító! (" + this.bookModel.docinfo.get("org") + ")");

                     try {
                        EbevTools.delFile(sp.srcPath + PropertyList.USER_IN_FILENAME + var3.substring(var3.lastIndexOf(File.separator) + 1));
                     } catch (Exception var32) {
                        Tools.eLog(var36, 0);
                     }

                     Result var45 = var2;
                     return var45;
                  }
               }

               EbevTools.delFile(sp.srcPath + var4);
               var43 = var2;
            } catch (AttachementException var37) {
               var2.setOk(false);
               var2.errorList.add(var37.getMessage());

               try {
                  EbevTools.delFile(sp.srcPath + PropertyList.USER_IN_FILENAME + var3.substring(var3.lastIndexOf(File.separator) + 1));
               } catch (Exception var28) {
                  Tools.eLog(var37, 0);
               }

               var6 = var2;
               return var6;
            } catch (FileNotFoundException var38) {
               try {
                  EbevTools.delFile(sp.srcPath + PropertyList.USER_IN_FILENAME + var3.substring(var3.lastIndexOf(File.separator) + 1));
               } catch (Exception var31) {
                  Tools.eLog(var38, 0);
               }

               var2.setOk(false);
               var2.errorList.add("A nyomtatvány átadása nem sikerült!");
               var6 = var2;
               return var6;
            } catch (IOException var39) {
               try {
                  EbevTools.delFile(sp.srcPath + PropertyList.USER_IN_FILENAME + var3.substring(var3.lastIndexOf(File.separator) + 1));
               } catch (Exception var30) {
                  Tools.eLog(var39, 0);
               }

               var2.setOk(false);
               var2.errorList.add("A nyomtatvány átadása nem sikerült!");
               var6 = var2;
               return var6;
            } catch (Exception var40) {
               try {
                  EbevTools.delFile(sp.srcPath + PropertyList.USER_IN_FILENAME + var3.substring(var3.lastIndexOf(File.separator) + 1));
               } catch (Exception var29) {
                  Tools.eLog(var40, 0);
               }

               var2.setOk(false);
               var2.errorList.add("Hiányzó paraméter, a nyomtatvány átadása nem lehetséges !");
               var6 = var2;
               return var6;
            }
         } finally {
            PropertyList.getInstance().set("prop.dynamic.pass2DSign", (Object)null);
         }

         return var43;
      }

      if (!var1) {
         var5 = new Vector();
         var5.add("Az aláírandó adatokat tartalmazó fájl létrehozása megtörtént. A fájl neve:");
         var5.add(" " + Tools.beautyPath(sp.srcPath + var4));
         var5.add("Csak ezt kell aláírnia, a(z)");
         var5.add(" " + Tools.beautyPath(var4.substring(var4.lastIndexOf(File.separator) + 1, var4.indexOf(".xml"))) + ".mf fájlt NEM.");
         var5.addAll(EbevTools.getAtcListFromVectorAsVector(this.bookModel.isAvdhModel()));
         EbevTools.showResultDialog(var5);
      }

      var2.errorList.add(Tools.beautyPath(sp.srcPath + var4));
      return var2;
   }

   private void createMFFile(BookModel var1, String var2, DocMetaData var3, Vector var4) throws Exception {
      String var5 = (String)var1.docinfo.get("org");
      if (OrgInfo.getInstance().hasSuccessor(var5)) {
         try {
            var5 = OrgInfo.getInstance().getSuccessorOrgId(var5);
         } catch (SuccessorException var13) {
            System.out.println("Pass2DSign : " + var13.getMessage());
         }
      }

      String var6 = OrgInfo.getInstance().getKRCimzett(var5);
      if (var6 == null) {
         throw new Exception("A nyomtatvány KR címzettje ismeretlen!");
      } else if (var1.id == null) {
         throw new Exception();
      } else {
         byte[] var7 = EbevTools.getCertBytes((String)var1.docinfo.get("org"));
         FileOutputStream var8 = new FileOutputStream(var2);

         try {
            var8.write(("<?xml version=\"1.0\" encoding=\"ISO-8859-2\"?>\n<abev:Boritek xmlns:abev='http://iop.gov.hu/2006/01/boritek'>\n<abev:Fejlec>\n  <abev:Cimzett>" + var6 + "</abev:Cimzett>\n" + "  <abev:DokTipusAzonosito>" + var3.getDokTipusAzonosito() + "</abev:DokTipusAzonosito>\n" + "  <abev:DokTipusLeiras>" + var3.getDokTipusLeiras() + "</abev:DokTipusLeiras>\n" + "  <abev:DokTipusVerzio>" + var3.getDokTipusVerzio() + "</abev:DokTipusVerzio>\n" + "  <abev:FileNev>" + PropertyList.USER_IN_FILENAME + var3.getFileNev() + "</abev:FileNev>\n" + "  <abev:Megjegyzes>" + var3.getMegjegyzes() + "</abev:Megjegyzes>\n" + this.getParamLista(var3.getParamList()) + this.getCsatolmanyInfo(var4) + "\n</abev:Fejlec>\n" + "<abev:CimzettNyilvanosKulcs>\n").getBytes("ISO-8859-2"));
            var8.write(var7);
            var8.write("\n</abev:CimzettNyilvanosKulcs>\n</abev:Boritek>".getBytes("ISO-8859-2"));
            var8.close();
         } catch (IOException var12) {
            try {
               var8.close();
            } catch (IOException var11) {
               Tools.eLog(var12, 0);
            }
         }

      }
   }

   private String getParamLista(Properties var1) {
      String var2 = "  <abev:ParameterLista>\n";
      Enumeration var3 = var1.keys();

      while(var3.hasMoreElements()) {
         String var4 = (String)var3.nextElement();
         if (!var4.equalsIgnoreCase("dokhash")) {
            var2 = var2 + "    <abev:Parameter Nev=\"" + var4 + "\" Ertek=\"" + DatastoreKeyToXml.htmlConvert(var1.getProperty(var4)) + "\"/>\n";
         }
      }

      var2 = var2 + "  </abev:ParameterLista>";
      return var2;
   }

   private String getCsatolmanyInfo(Vector var1) {
      if (var1 == null) {
         return "\n";
      } else if (var1.size() == 0) {
         return "\n";
      } else {
         StringBuffer var2 = new StringBuffer();

         for(int var3 = 0; var3 < var1.size(); ++var3) {
            var2.append(var1.get(var3));
         }

         return var2.toString();
      }
   }

   public Result export(String var1) {
      if (var1 == null) {
         var1 = sp.importPath + "pelda.xml";
      }

      Result var2 = new Result();

      try {
         var2 = EbevTools.saveFile(this.bookModel, true, ".xml", false, sp, var1);
         if (var2.isOk()) {
            if (this.bookModel.splitesaver.equals("true")) {
               for(int var3 = 0; var3 < var2.errorList.size(); ++var3) {
                  Result var4 = EbevTools.checkXSD((new File(var1)).getParent() + File.separator + var2.errorList.elementAt(var3));
                  var2.setOk(var2.isOk() && var4.isOk());
               }
            } else {
               var2 = EbevTools.checkXSD(var1);
            }
         }
      } catch (Exception var6) {
         try {
            EbevTools.delFile(var1);
         } catch (Exception var5) {
            Tools.eLog(var6, 0);
         }

         var2.setOk(false);
         var2.errorList.add("A nyomtatvány exportálása nem sikerült ! " + var6.toString());
         return var2;
      }

      if (this.bookModel.splitesaver.equals("true")) {
         if (var2.isOk()) {
            String var7 = "Az xml fájl";
            if (this.bookModel.cc.size() > 1) {
               var7 = var7 + "ok";
            }

            var7 = var7 + " létrehozása megtörtént.\nA fájl";
            if (this.bookModel.cc.size() > 1) {
               var7 = var7 + "ok";
            }

            var7 = var7 + " neve";
            if (this.bookModel.cc.size() > 1) {
               var7 = var7 + "i";
            }

            var7 = var7 + ":";

            for(int var8 = 0; var8 < var2.errorList.size(); ++var8) {
               var7 = var7 + "\n" + Tools.beautyPath((String)var2.errorList.elementAt(var8));
            }

            GuiUtil.showMessageDialog(MainFrame.thisinstance, var7, "XML export", 1);
         }
      } else {
         if (var2.isOk()) {
            GuiUtil.showMessageDialog(MainFrame.thisinstance, "Az xml fájl létrehozása megtörtént\nA fájl neve: " + Tools.beautyPath(var1), "XML export", 1);
         }

         var2.errorList.add(Tools.beautyPath(var1));
      }

      return var2;
   }
}
