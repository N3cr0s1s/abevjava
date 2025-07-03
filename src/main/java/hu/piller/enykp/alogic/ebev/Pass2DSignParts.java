package hu.piller.enykp.alogic.ebev;

import hu.piller.enykp.alogic.filesaver.xml.EnykXmlSaverParts;
import hu.piller.enykp.alogic.fileutil.DatastoreKeyToXml;
import hu.piller.enykp.alogic.orghandler.OrgInfo;
import hu.piller.enykp.alogic.orghandler.SuccessorException;
import hu.piller.enykp.datastore.Elem;
import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.gui.framework.MainFrame;
import hu.piller.enykp.gui.model.BookModel;
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

public class Pass2DSignParts {
   private static SendParams sp;
   private BookModel bm;
   private IPropertyList mpl = PropertyList.getInstance();

   public Pass2DSignParts(BookModel var1, SendParams var2, IPropertyList var3) {
      sp = var2;
      this.bm = var1;
      this.mpl = var3;
   }

   public Result pass() {
      return this.pass(false);
   }

   public Result pass(boolean var1) {
      Result var2 = new Result();
      String var3 = this.bm.cc.getLoadedfile().getAbsolutePath();
      Vector var4 = null;

      int var44;
      try {
         Result var6;
         try {
            PropertyList.getInstance().set("prop.dynamic.pass2DSign", true);
            Vector var5 = new Vector();

            for(var44 = 0; var44 < this.bm.cc.size(); ++var44) {
               Result var8;
               try {
                  Vector var7 = EbevTools.doCheckAtcFile(this.bm, var3, ((Elem)this.bm.cc.get(var44)).getType(), var44);
                  if (var7 == null) {
                     var2.setOk(false);
                     var2.errorList.add("A nyomtatvány átadása nem sikerült! Nem található a csatolmány-leíróban szereplő fájl!");
                     var8 = var2;
                     return var8;
                  }

                  if (var7.size() == 0) {
                     var5.add((Object)null);
                  } else {
                     var5.addAll(var7);
                  }

                  var5.addAll(var7);
               } catch (AttachementException var34) {
                  var2.setOk(false);
                  var2.errorList.add(var34.getMessage());
                  var8 = var2;
                  return var8;
               } catch (Exception var35) {
                  var2.setOk(false);
                  var2.errorList.add(var35.getMessage());
                  var8 = var2;
                  return var8;
               }
            }

            EnykXmlSaverParts var46 = new EnykXmlSaverParts(this.bm, 0);
            var2 = var46.saveFile(this.bm, ".xml", var1);
            if (var2.isOk()) {
               Result var45;
               if (!var2.isOk()) {
                  var45 = var2;
                  return var45;
               }

               var4 = var2.errorList;
               var2 = EbevTools.checkPanids(this.bm);
               if (!var2.isOk()) {
                  var45 = var2;
                  return var45;
               }

               for(int var43 = 0; var43 < var4.size(); ++var43) {
                  try {
                     var2 = EbevTools.checkXSD(sp.srcPath + var4.elementAt(var43));
                     if (!var2.isOk()) {
                        throw new XsdException();
                     }

                     String var47 = this.bm.get(((Elem)this.bm.cc.get(var43)).getType()).name;
                     DocMetaData var48 = EbevTools.getDMD(this.bm, var47, (String)this.bm.docinfo.get("org"), (String)var4.elementAt(var43), var5, var43, false);
                     String var10 = (String)var4.elementAt(var43);
                     this.createMFFile(this.bm, sp.srcPath + var10.substring(0, var10.lastIndexOf(".xml")) + ".mf", var48, var5, var43);
                  } catch (Exception var33) {
                     var2.setOk(false);
                     var2.errorList.add("Nem sikerült összeállítani a nyomtatványt kísérő adatokat! (" + this.bm.docinfo.get("org") + ")");

                     try {
                        EbevTools.delFile(sp.srcPath + PropertyList.USER_IN_FILENAME + var3.substring(var3.lastIndexOf(File.separator) + 1));
                     } catch (Exception var32) {
                        Tools.eLog(var33, 0);
                     }

                     Result var9 = var2;
                     return var9;
                  }
               }
            }
         } catch (AttachementException var36) {
            var2.setOk(false);
            var2.errorList.add(var36.getMessage());

            try {
               EbevTools.delFile(sp.srcPath + PropertyList.USER_IN_FILENAME + var3.substring(var3.lastIndexOf(File.separator) + 1));
            } catch (Exception var29) {
               Tools.eLog(var36, 0);
            }

            var6 = var2;
            return var6;
         } catch (FileNotFoundException var37) {
            try {
               EbevTools.delFile(sp.srcPath + PropertyList.USER_IN_FILENAME + var3.substring(var3.lastIndexOf(File.separator) + 1));
            } catch (Exception var28) {
               Tools.eLog(var37, 0);
            }

            var2.setOk(false);
            var2.errorList.add("A nyomtatvány átadása nem sikerült!");
            var6 = var2;
            return var6;
         } catch (IOException var38) {
            try {
               EbevTools.delFile(sp.srcPath + PropertyList.USER_IN_FILENAME + var3.substring(var3.lastIndexOf(File.separator) + 1));
            } catch (Exception var31) {
               Tools.eLog(var38, 0);
            }

            var2.setOk(false);
            var2.errorList.add("A nyomtatvány átadása nem sikerült!");
            var6 = var2;
            return var6;
         } catch (XsdException var39) {
            try {
               EbevTools.delFile(sp.srcPath + PropertyList.USER_IN_FILENAME + var3.substring(var3.lastIndexOf(File.separator) + 1));
            } catch (Exception var30) {
               Tools.eLog(var30, 0);
            }

            var2.errorList.add("Xsd hiba");
            var2.setOk(false);
            var6 = var2;
            return var6;
         } catch (Exception var40) {
            try {
               EbevTools.delFile(sp.srcPath + PropertyList.USER_IN_FILENAME + var3.substring(var3.lastIndexOf(File.separator) + 1));
            } catch (Exception var27) {
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

      if (!var1 && var4 != null) {
         String var42 = "Az aláírandó adatokat tartalmazó fájlok létrehozása megtörtént\nAz alábbi fájlok elérhetőek a \n" + sp.srcPath + "\nmappában, \n";

         for(var44 = 0; var44 < var4.size(); ++var44) {
            var42 = var42 + var4.elementAt(var44) + "\n";
         }

         var42 = var42 + "néven.\nCsak ezeket a fájlokat kell aláírnia, az ugyanitt lévő mf fájlokat NEM.";
         GuiUtil.showMessageDialog(MainFrame.thisinstance, var42, "Átadás", 1);
         var2.errorList.add(var42);
      } else if (var4 != null) {
         var2.errorList.add(this.getStringFromVector(var4));
      }

      return var2;
   }

   private void createMFFile(BookModel var1, String var2, DocMetaData var3, Vector var4, int var5) throws Exception {
      String var6 = (String)var1.docinfo.get("org");
      if (OrgInfo.getInstance().hasSuccessor(var6)) {
         try {
            var6 = OrgInfo.getInstance().getSuccessorOrgId(var6);
         } catch (SuccessorException var14) {
            System.out.println("Pass2DSign : " + var14.getMessage());
         }
      }

      String var7 = OrgInfo.getInstance().getKRCimzett(var6);
      if (var7 == null) {
         throw new Exception("A nyomtatvány KR címzettje ismeretlen!");
      } else if (var1.id == null) {
         throw new Exception();
      } else {
         byte[] var8 = EbevTools.getCertBytes((String)var1.docinfo.get("org"));
         FileOutputStream var9 = new FileOutputStream(var2);

         try {
            var9.write(("<?xml version=\"1.0\" encoding=\"ISO-8859-2\"?>\n<abev:Boritek xmlns:abev='http://iop.gov.hu/2006/01/boritek'>\n<abev:Fejlec>\n  <abev:Cimzett>" + var7 + "</abev:Cimzett>\n" + "  <abev:DokTipusAzonosito>" + var3.getDokTipusAzonosito() + "</abev:DokTipusAzonosito>\n" + "  <abev:DokTipusLeiras>" + var3.getDokTipusLeiras() + "</abev:DokTipusLeiras>\n" + "  <abev:DokTipusVerzio>" + var3.getDokTipusVerzio() + "</abev:DokTipusVerzio>\n" + "  <abev:FileNev>" + PropertyList.USER_IN_FILENAME + var3.getFileNev() + "</abev:FileNev>\n" + "  <abev:Megjegyzes>" + var3.getMegjegyzes() + "</abev:Megjegyzes>\n" + this.getParamLista(var3.getParamList()) + this.getCsatolmanyInfo(var4, var5) + "\n</abev:Fejlec>\n" + "<abev:CimzettNyilvanosKulcs>\n").getBytes("ISO-8859-2"));
            var9.write(var8);
            var9.write("\n</abev:CimzettNyilvanosKulcs>\n</abev:Boritek>".getBytes("ISO-8859-2"));
            var9.close();
         } catch (IOException var13) {
            try {
               var9.close();
            } catch (IOException var12) {
               Tools.eLog(var13, 0);
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

   private String getCsatolmanyInfo(Vector var1, int var2) {
      if (var1 == null) {
         return "";
      } else if (var1.size() == 0) {
         return "";
      } else if (var1.get(var2) == null) {
         return "";
      } else {
         StringBuffer var3 = new StringBuffer();
         var3.append(var1.get(var2));
         return var3.toString();
      }
   }

   public Result export(String var1) {
      if (var1 == null) {
         var1 = sp.importPath + "pelda.xml";
      }

      Result var2 = new Result();

      try {
         var2 = EbevTools.saveFile(this.bm, true, ".xml", false, sp, var1);
         if (var2.isOk()) {
            var2 = EbevTools.checkXSD(var1);
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

      if (var2.isOk()) {
         GuiUtil.showMessageDialog(MainFrame.thisinstance, "Az xml fájl létrehozása megtörtént\nA fájl neve: " + Tools.beautyPath(var1), "XML export", 1);
      }

      var2.errorList.add(Tools.beautyPath(var1));
      return var2;
   }

   private String getStringFromVector(Vector var1) {
      String var2 = "";

      for(int var3 = 0; var3 < var1.size(); ++var3) {
         var2 = var2 + Tools.beautyPath(sp.srcPath + var1.elementAt(var3)) + ", ";
      }

      return var2.length() > 0 ? var2.substring(0, var2.length() - 2) : var2;
   }
}
