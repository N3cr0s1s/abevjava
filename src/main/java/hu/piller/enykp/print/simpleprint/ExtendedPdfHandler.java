package hu.piller.enykp.print.simpleprint;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfWriter;
import hu.piller.enykp.alogic.orghandler.OrgInfo;
import hu.piller.enykp.alogic.orghandler.OrgResource;
import hu.piller.enykp.datastore.Elem;
import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.gui.model.BookModel;
import hu.piller.enykp.gui.model.DataFieldModel;
import hu.piller.enykp.gui.model.FormModel;
import hu.piller.enykp.gui.model.SimpleViewModel;
import hu.piller.enykp.interfaces.IDataStore;
import hu.piller.enykp.print.MainPrinter;
import hu.piller.enykp.util.base.PropertyList;
import hu.piller.enykp.util.base.Result;
import hu.piller.enykp.util.base.Tools;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.JEditorPane;
import javax.swing.table.DefaultTableModel;

public class ExtendedPdfHandler {
   private BookModel bookModel;
   private String autoFillPdfPrevFileName;
   private boolean batchPrint2OnePdf;
   private Document document;
   private PdfWriter writer;
   int ciklusVege = 1;
   int ciklusEleje = 0;
   private Hashtable nyomtatandoLapnevek;
   private FormModel formModel;
   private boolean elektronikus;
   private boolean notVKPrint;
   public static int[] seq;
   private String backgroundImage = "bg.gif";
   public static String headerFromJar;
   public static String headerImageName;
   public static String kivPrTdFontSize = "10";
   int titleHeight = -1;
   int titleWidth = -1;
   double jepCorrection;
   public static final String GEN_PARAM_KESZULT_DATUM = "Készítés dátuma";
   private static final String GEN_PARAM_ADOEV = "Adómegállapítási időszak";
   private static final String GEN_PARAM_BARKOD = "Adómegállapítás bárkódja";
   private static final String GEN_PARAM_FELDOLGOZAS_DATUM = "Feldolgozás dátuma";
   private static final String GEN_PARAM_NEV_1 = "Név titulus";
   private static final String GEN_PARAM_NEV_2 = "Vezetékneve";
   private static final String GEN_PARAM_NEV_3 = "Keresztneve";
   private static final String GEN_PARAM_ADOAZONOSITO = "Adózó adóazonosító jele";
   private static final String GEN_PARAM_ADOSZAM = "Adózó adószáma";
   public static final String LANDSCAPE_PAGE_SEPARATOR = "#LANDSCAPE#";
   public static final String PRINT_ONLY_BARCODE_PAGE = "#ONLY_BARCODE_PAGE#";
   private HashSet an = new HashSet(Arrays.asList("0", "2", "3", "6", "8"));

   public ExtendedPdfHandler(BookModel var1, String var2, boolean var3, Document var4, PdfWriter var5, int var6, int var7, Hashtable var8, FormModel var9, boolean var10, boolean var11, String var12) {
      this.bookModel = var1;
      this.autoFillPdfPrevFileName = var2;
      this.batchPrint2OnePdf = var3;
      this.document = var4;
      this.writer = var5;
      this.ciklusVege = var6;
      this.ciklusEleje = var7;
      this.nyomtatandoLapnevek = var8;
      this.formModel = var9;
      this.elektronikus = var10;
      this.notVKPrint = var11;
      this.backgroundImage = var12;
   }

   public Result doNewSimplePrint2Pdf(String var1, int var2, String var3, boolean var4, boolean var5) {
      Result var6 = new Result();

      try {
         PropertyList.getInstance().set("brCountError", (Object)null);
         Object[] var7 = this.getData4SimplePrint2();
         if (var7 == null) {
            var6.setOk(false);
            var6.errorList.insertElementAt("Nem sikerült előállítani a kivonatolt nyomtatási adatokat", 0);
            return var6;
         }

         Vector[] var8 = this.getRows();
         String var9 = (String)var7[0];

         try {
            var9 = var9.replaceAll("#13", "<br>");
         } catch (Exception var16) {
            Tools.eLog(var16, 0);
         }

         long var10 = System.currentTimeMillis();

         try {
            if (this.autoFillPdfPrevFileName != null && !this.autoFillPdfPrevFileName.equals("")) {
               var3 = this.autoFillPdfPrevFileName;
            }

            ExtendedJep2Pdf var12 = new ExtendedJep2Pdf(var3, var9, var1);
            var12.setLastPartIndex(var7.length);
            var12.setFooterVector(var8[1]);
            var12.setHeaderVector(var8[0]);
            if (this.batchPrint2OnePdf) {
               if (var2 == this.ciklusEleje) {
                  this.document = new Document();
                  this.writer = PdfWriter.getInstance(this.document, new FileOutputStream(var3));
                  this.document.open();
               }

               this.doPdfPrintLoop(var12, var7, var8[1]);
               if (var2 == this.ciklusVege - 1 && !var4) {
                  this.document.close();
                  this.document = null;
                  var12 = null;
               }

               var6.errorList.insertElementAt(this.document, 0);
               var6.errorList.insertElementAt(this.writer, 1);
            } else {
               ByteArrayOutputStream var13 = new ByteArrayOutputStream();
               this.document = new Document();
               this.writer = PdfWriter.getInstance(this.document, new FileOutputStream(var3));
               this.document.open();
               this.doPdfPrintLoop(var12, var7, var8[1]);
               this.document.close();
               var6.errorList.add(var13.toByteArray());
               this.document = null;
               var12 = null;
            }
         } catch (NumberFormatException var14) {
            PropertyList.getInstance().set("brCountError", var14.getMessage());
            var6.errorList.insertElementAt(-3, 0);
            var6.setOk(false);
            return var6;
         } catch (Exception var15) {
            Tools.exception2SOut(var15);
            var6.errorList.insertElementAt("Hiba történt a pdf előállítás során", 0);
            var6.setOk(false);
            return var6;
         }
      } catch (Exception var17) {
         Tools.exception2SOut(var17);
         var6.errorList.insertElementAt("Nem sikerült előállítani a kivonatolt nyomtatási adatokat", 0);
         var6.setOk(false);
      }

      return var6;
   }

   private void doPdfPrintLoop(ExtendedJep2Pdf var1, Object[] var2, Vector var3) throws FileNotFoundException, DocumentException {
      int var5 = var1.printJep2Pdf(this.document, this.writer, false, 0, 0);

      for(int var6 = 1; var6 < var2.length; ++var6) {
         String var4 = (String)var2[var6];
         var4 = var4.replaceAll("#13", "<br>");
         if (var4.startsWith("#LANDSCAPE#")) {
            var4 = var4.substring(11);
            var1.reInitPane(var4, 0);
            var5 += var1.printLandscapeJep2Pdf(this.document, this.writer, var6 == var2.length, var6, var5);
            if (var5 > 0) {
               var5 -= var6;
            }
         } else {
            var1.reInitPane(var4, 1);
            Vector var7 = new Vector(var3);

            String var8;
            try {
               var8 = (String)var7.remove(0);
               var8 = var8.substring("Adóbevallási tervezet - ".length());
            } catch (Exception var10) {
               var8 = "";
            }

            var7.insertElementAt(var8, 0);
            var5 += var1.printJep2Pdf(this.document, this.writer, var6 == var2.length, var6, var5);
            if (var5 > 0) {
               var5 -= var6;
            }
         }
      }

      var1.printBarcode(this.document, this.writer, var5);
   }

   public Object[] getData4SimplePrint2() throws Exception {
      SimpleViewModel var1 = new SimpleViewModel(this.bookModel);
      if (this.nyomtatandoLapnevek.size() == 0) {
         return null;
      } else {
         Vector var2 = var1.getResult(this.nyomtatandoLapnevek);
         ArrayList var3 = this.convert2MultipleSimpleData(var2);
         String var4 = this.tableModels2DummyHtmlTable((Vector)var3.get(0));
         JEditorPane var5 = new JEditorPane("text/html", var4);
         this.jepCorrection = this.getCorrection(var5.getMinimumSize().getWidth());
         var5 = null;
         String[] var6 = new String[var3.size()];
         seq = new int[var3.size()];

         for(int var7 = 0; var7 < var6.length; ++var7) {
            var6[var7] = this.tableModels2HtmlTable((Vector)var3.get(var7), this.jepCorrection, var7);
            if (((Vector)var3.get(var7)).get(0) instanceof String) {
               String var8 = (String)((Vector)var3.get(var7)).get(0);
               if (var8.startsWith("#LANDSCAPE#")) {
                  var6[var7] = "#LANDSCAPE#" + var6[var7];
               }
            }
         }

         return var6;
      }
   }

   private ArrayList<Vector> convert2MultipleSimpleData(Vector var1) {
      ArrayList var2 = new ArrayList();
      Vector var3 = new Vector();
      PageTitle var4 = this.getFirstPageData(var1);

      for(int var5 = 0; var5 < var1.size(); ++var5) {
         if (var1.get(var5) instanceof PageTitle) {
            PageTitle var6 = (PageTitle)var1.get(var5);
            if (this.needPageBreak(var4, var6)) {
               var2.add(var3);
               var3 = new Vector();
            }

            if (var6.isLandscape()) {
               var3.add("#LANDSCAPE#" + var6.getTitleString());
            } else {
               var3.add(var6.getTitleString());
            }

            var4 = var6;
         } else {
            var3.add(var1.get(var5));
         }
      }

      var2.add(var3);
      return var2;
   }

   private PageTitle getFirstPageData(Vector var1) {
      for(int var2 = 0; var2 < var1.size(); ++var2) {
         if (var1.get(var2) instanceof PageTitle) {
            return (PageTitle)var1.get(var2);
         }
      }

      return null;
   }

   private boolean needPageBreak(PageTitle var1, PageTitle var2) {
      if (var2.isLandscape() != var1.isLandscape()) {
         return true;
      } else if (var2.getPageBreak() == KPrintFormFeedType.NO) {
         return false;
      } else if (var2.getPageBreak() == KPrintFormFeedType.ALL) {
         return true;
      } else if (var2.getPageBreak() == KPrintFormFeedType.FIRST) {
         return var2.getPageType() != var1.getPageType();
      } else {
         return false;
      }
   }

   private double getCorrection(double var1) {
      double var3 = 1.0D;
      if (var1 > MainPrinter.defaultPageFormat4htmlPrint.getImageableWidth()) {
         var3 = MainPrinter.defaultPageFormat4htmlPrint.getImageableWidth() / var1;
      }

      return Math.min(var3, 1.0D);
   }

   public String tableModels2DummyHtmlTable(Vector var1) {
      StringBuffer var2 = new StringBuffer("<html><head>");
      var2.append("<STYLE TYPE=\"text/css\">").append("#tdr {text-align:right; padding-left:2px;}").append("#tdc {text-align:center;}").append("td {").append(" padding-top: -2px;").append(" padding-bottom: -2px;").append(" padding-left: 2px;").append(" padding-right: 2px;").append(" font-size: ").append(MainPrinter.kivPrTdFontSize).append("px;").append(" }").append("table {").append(" border-collapse: collapse;").append("}").append("</STYLE>");
      var2.append("</head><body style=\"font-size: 6px;\">");
      int var3 = -1;
      boolean var4 = false;
      String var5 = "";

      for(int var6 = 0; var6 < var1.size(); ++var6) {
         if (var1.get(var6) instanceof String) {
            if (var4) {
               var2.delete(var3, var2.length());
            }

            var3 = var2.length();
            var2.append("<table width=\"100%\" style=\"padding-top:6px; padding-bottom:3px\"><tr><td><div id=\"tdc\" style=\"font-size: ").append(MainPrinter.kivPrCenterTdFontSize).append("px;\"><b>").append(var1.get(var6)).append("</b></div></td></tr></table>");
            if (var6 > 0) {
               var4 = true;
            }
         } else {
            var4 = false;
            DefaultTableModel var7 = (DefaultTableModel)var1.get(var6);
            var2.append("<table border=\"1\" cellpadding=\"").append(MainPrinter.kivPrTdCellPadding).append("\" cellspacing=\"0\" width=\"100%\">");
            int var8 = var7.getColumnCount();

            for(int var9 = 0; var9 < var7.getRowCount(); ++var9) {
               var2.append("<tr>");
               int var10;
               if (var9 == 0) {
                  if (var7.getValueAt(0, 0) != null && ((String)var7.getValueAt(0, 0)).startsWith("<th>")) {
                     var5 = " bgcolor=\"" + MainPrinter.thBgColor + "\"";
                  }

                  var2.append("<td valign=\"top\"").append(var5).append(">").append(var7.getValueAt(0, 0) == null ? "&nbsp;" : var7.getValueAt(0, 0).toString().replaceAll("<th>", "")).append("</td>");

                  for(var10 = 1; var10 < var8; ++var10) {
                     var2.append("<td valign=\"top\"").append(var5).append(">").append(var7.getValueAt(var9, var10) == null ? "&nbsp;" : var7.getValueAt(var9, var10)).append("</td>");
                  }
               } else {
                  for(var10 = 0; var10 < var8; ++var10) {
                     var5 = "";
                     var2.append("<td valign=\"top\"").append(var5).append(">").append(var7.getValueAt(var9, var10) == null ? "&nbsp;" : var7.getValueAt(var9, var10)).append("</td>");
                  }
               }

               var2.append("</tr>");
            }

            var2.append("</table>");
         }
      }

      if (var4) {
         var2.delete(var3, var2.length());
      }

      var2.append("</body></html>");
      return var2.toString();
   }

   public String tableModels2HtmlTable(Vector var1, double var2, int var4) throws Exception {
      String var5 = "";
      OrgInfo var6 = OrgInfo.getInstance();
      Hashtable var7 = (Hashtable)((Hashtable)var6.getOrgInfo(this.bookModel.docinfo.get("org"))).get("attributes");
      String var8 = ((OrgResource)((Hashtable)var6.getOrgList()).get(this.bookModel.docinfo.get("org"))).getVersion().toString();
      String var9 = var7.get("prefix") + "Resources_" + var8;

      try {
         URI var10 = new URI(this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath());
         this.backgroundImage = (new File(var10.getPath())).getParent() + "/abevjava.jar!/resources/print/bg.gif";
         this.backgroundImage = this.backgroundImage.startsWith("/") ? "file:" + this.backgroundImage : "file:/" + this.backgroundImage;
         this.backgroundImage = "jar:" + this.backgroundImage;
      } catch (URISyntaxException var25) {
         System.out.println("Kivonatolt nyomtatási kép hiba!");
         var25.printStackTrace();
      }

      String var26 = "<td width=\"1\" bgcolor=\"" + MainPrinter.thBgColor + "\" style=\"width:1px; padding:0;\"><img src=\"" + this.backgroundImage + "\" width=\"1\" height=\"1\" border=\"0\"></td>";
      String var11 = "<td width=\"1\" bgcolor=\"white\" style=\"width:1px; padding:0;\"><img src=\"" + this.backgroundImage + "\" width=\"1\" height=\"1\" border=\"0\"></td>";
      String var12 = PropertyList.getInstance().get("prop.sys.root").toString().replaceAll("\\\\", "/");
      var12 = var12.startsWith("/") ? "file:" + var12 : "file:/" + var12;
      var12 = var12 + "/eroforrasok";
      Hashtable var13 = this.formModel.kvprintht;
      if (var13 != null) {
         this.titleHeight = Integer.parseInt((String)var13.get("kiv_fejlec_height"));
         this.titleWidth = Integer.parseInt((String)var13.get("kiv_fejlec_width"));
         double var14 = this.getCorrection((double)this.titleWidth);
         this.titleHeight = (int)((double)this.titleHeight * var14 / var2);
         this.titleWidth = (int)((double)this.titleWidth * var14 / var2);
      }

      String var27 = null;
      if (var13 != null) {
         headerImageName = (String)var13.get("kiv_fejlec_file_name");
         headerFromJar = "jar:" + var12 + "/" + var9 + ".jar!/pictures/" + headerImageName;
         var27 = "jar:" + var12 + "/" + var9 + ".jar!/pictures/" + var13.get("kiv_fejlec_file_name");
         var5 = "<table width=\"" + this.titleWidth + "\" height=\"" + this.titleHeight + "\"><tr><td><img src=\"" + var27 + "\"  width=\"" + this.titleWidth + "\" height=\"" + this.titleHeight + "\" border=\"0\"></td></tr></table>";
      }

      StringBuffer var15 = new StringBuffer("<html><head>");
      var15.append("<STYLE TYPE=\"text/css\">").append("#tdl {text-align:left; padding-left:2px;}").append("#tdr {text-align:right; padding-left:2px;}").append("#tdc {text-align:center;}").append("#ntdl {text-align:left; padding-left:2px; width: ").append("200").append("px;}").append("#ntdr {text-align:right; padding-left:2px; width: ").append("200").append("px;}").append("#ntdc {text-align:center; width: 100px;}").append("td {").append(" padding-top: -2px;").append(" padding-bottom: -2px;").append(" padding-left: 2px;").append(" padding-right: 2px;").append(" font-size: ").append(kivPrTdFontSize).append("px;").append(" }").append("table {").append(" border-collapse: collapse;").append("}").append("</STYLE>");
      var15.append("</head><body style=\"font-size: 6px;\">");
      if (!this.elektronikus && !this.notVKPrint && var4 == 0) {
         var15.append(var5);
      }

      int var16 = -1;
      boolean var17 = false;
      String var18 = "";

      for(int var19 = 0; var19 < var1.size(); ++var19) {
         if (var1.get(var19) instanceof String) {
            if (var17) {
               this.handleSeq(var15, var16, var4);
               var15.delete(var16, var15.length());
            }

            var16 = var15.length();
            String var28 = GuiUtil.getPlainLabelText((String)var1.get(var19));
            if (var28.startsWith("#LANDSCAPE#")) {
               var28 = var28.substring("#LANDSCAPE#".length());
            }

            var15.append("<table width=\"100%\" style=\"padding-top:6px; padding-bottom:3px\"><tr><td class=\"").append(seq[var4]++).append("\"><div id=\"tdc\" style=\"font-size: 8px;\"><b>").append(var28).append("</b></div></td></tr></table>");
            if (var19 > 0) {
               var17 = true;
            }
         } else {
            var17 = false;
            DefaultTableModel var20 = (DefaultTableModel)var1.get(var19);
            var15.append("<table border=\"0\" cellpadding=\"1\" cellspacing=\"0\" width=\"100%\">");
            if (var20.getValueAt(0, 0) != null && ((String)var20.getValueAt(0, 0)).startsWith("<th>")) {
               var20.setValueAt(((String)var20.getValueAt(0, 0)).replaceAll("<th>", ""), 0, 0);
               var18 = " bgcolor=\"" + MainPrinter.thBgColor + "\"";
            }

            int var21 = var20.getColumnCount();

            for(int var23 = 0; var23 < var20.getRowCount(); ++var23) {
               var15.append(this.getHBorder(2 * var21 + 1)).append("<tr>").append(var26);
               Object var22;
               int var24;
               String var29;
               if (var20.getValueAt(var23, 0) != null && var20.getValueAt(var23, 0).toString().startsWith("***")) {
                  if (var20.getValueAt(var23, 0) != null && var20.getValueAt(var23, 0).toString().startsWith("***#")) {
                     for(var24 = 0; var24 < var21; ++var24) {
                        var18 = "";
                        var15.append("<td class=\"").append(seq[var4]++).append("\" valign=\"top\"").append(var18).append(">");
                        var22 = var20.getValueAt(var23, var24);
                        if (var22 == null) {
                           var22 = "&nbsp;";
                        } else {
                           if (var24 == 0) {
                              var22 = var22.toString().substring(4);
                           }

                           if (var22.toString().startsWith(" id=\"ntd")) {
                              var15.deleteCharAt(var15.length() - 1);
                              var22 = var22.toString().substring(0, 5) + var22.toString().substring(6);
                           }
                        }

                        var29 = GuiUtil.getPlainLabelText(var22.toString());
                        var15.append(var29.toString().replaceAll("@#@#", "&nbsp;")).append("</td>").append(var24 == var21 - 1 ? var26 : var11);
                     }
                  } else {
                     var18 = "";

                     for(var24 = 0; var24 < var21; ++var24) {
                        var15.append("<td class=\"").append(seq[var4]++).append("\" valign=\"top\"").append(var18).append(">");
                        var22 = var20.getValueAt(var23, var24);
                        if (var22 == null) {
                           var29 = "&nbsp;";
                        } else {
                           if (var24 == 0) {
                              var22 = var22.toString().substring(3);
                           }

                           if (var22.toString().startsWith(" id=\"ntd")) {
                              var15.deleteCharAt(var15.length() - 1);
                           }

                           var29 = GuiUtil.getPlainLabelText(var22.toString());
                        }

                        var15.append(var29.toString().replaceAll("@#@#", "&nbsp;")).append("</td>").append(var24 == var21 - 1 ? var26 : var11);
                     }
                  }
               } else if (var23 == 0) {
                  for(var24 = 0; var24 < var21; ++var24) {
                     var15.append("<td class=\"").append(seq[var4]++).append("\" valign=\"top\"").append(var18).append(">");
                     var22 = var20.getValueAt(var23, var24);
                     if (var22 == null) {
                        var22 = "&nbsp;";
                     } else if (var22.toString().startsWith(" id=\"ntd")) {
                        var15.deleteCharAt(var15.length() - 1);
                     }

                     var29 = GuiUtil.getPlainLabelText(var22.toString());
                     var15.append(var29.toString().replaceAll("@#@#", "&nbsp;")).append("</td>").append(var26);
                  }
               } else {
                  for(var24 = 0; var24 < var21; ++var24) {
                     var18 = "";
                     var15.append("<td class=\"").append(seq[var4]++).append("\" valign=\"top\"").append(var18).append(">");
                     var22 = var20.getValueAt(var23, var24);
                     if (var22 == null) {
                        var22 = "&nbsp;";
                     } else if (var22.toString().startsWith(" id=\"ntd")) {
                        var15.deleteCharAt(var15.length() - 1);
                     }

                     var29 = GuiUtil.getPlainLabelText(var22.toString());
                     var15.append(var29.toString().replaceAll("@#@#", "&nbsp;")).append("</td>").append(var26);
                  }
               }

               var15.append("</tr>");
            }

            var15.append(this.getHBorder(2 * var21 + 1)).append("</table>");
         }
      }

      if (var17) {
         this.handleSeq(var15, var16, var4);
         var15.delete(var16, var15.length());
      }

      var15.append("</body></html>");
      return var15.toString();
   }

   private Vector[] parseRows(Vector[] var1) {
      Vector[] var2 = new Vector[var1.length];

      for(int var3 = 0; var3 < var1.length; ++var3) {
         var2[var3] = new Vector();
         String var4 = var3 == 0 ? "" : "_____________________________";
         Elem var5 = (Elem)this.bookModel.cc.getActiveObject();
         IDataStore var6 = (IDataStore)var5.getRef();

         for(int var7 = 0; var7 < var1[var3].size(); ++var7) {
            Hashtable var8 = (Hashtable)var1[var3].elementAt(var7);
            String var9 = this.parseText((String)var8.get("txt"), var6, this.bookModel.get(var5.getType()), var4);
            var2[var3].add(var9);
         }
      }

      return var2;
   }

   private String parseText(String var1, IDataStore var2, FormModel var3, String var4) {
      String[] var5 = var1.split("\\{");
      Vector var6 = new Vector();

      for(int var7 = 0; var7 < var5.length; ++var7) {
         String[] var8 = var5[var7].split("\\*\\}");

         for(int var9 = 0; var9 < var8.length; ++var9) {
            var6.add(var8[var9]);
         }
      }

      StringBuilder var10 = new StringBuilder();

      for(int var11 = 0; var11 < var6.size(); ++var11) {
         if (!var6.elementAt(var11).toString().startsWith("*")) {
            var10.append(var6.elementAt(var11));
         } else if (var2.get("0_" + var6.elementAt(var11).toString().substring(1)) != null) {
            var10.append(this.getFormattedData(var6.elementAt(var11), var3, var2, var4));
         } else {
            var10.append(var4);
         }
      }

      return var10.toString();
   }

   private String getFormattedData(Object var1, FormModel var2, IDataStore var3, String var4) {
      String var5 = var1.toString().substring(1);

      try {
         String var6 = var3.get("0_" + var5);
         return ((DataFieldModel)var2.fids.get(var5)).features.get("datatype").toString().equalsIgnoreCase("date") ? var6.substring(0, 4) + "." + var6.substring(4, 6) + "." + var6.substring(6) : var6;
      } catch (Exception var8) {
         return var4;
      }
   }

   private void handleSeq(StringBuffer var1, int var2, int var3) {
      try {
         String var4 = var1.substring(var2);
         String[] var5 = var4.split("class=");
         seq[var3] -= var5.length - 1;
      } catch (Exception var6) {
         Tools.eLog(var6, 0);
      }

   }

   private String getHBorder(int var1) {
      return "<tr><td colspan=\"" + var1 + "\" bgcolor=\"" + MainPrinter.thBgColor + "\" style=\"height:1px; padding:0;\"><img src=\"" + this.backgroundImage + "\" width=\"1\" height=\"1\" border=\"0\"></td></tr>";
   }

   private Vector[] getRows() {
      Vector[] var1 = new Vector[2];
      Hashtable var2 = this.formModel.kvprintht;
      if (var2 != null) {
         var1[0] = (Vector)var2.get("hrows");
         var1[1] = (Vector)var2.get("frows");
         this.titleHeight = Integer.parseInt((String)var2.get("kiv_fejlec_height"));
         this.titleWidth = Integer.parseInt((String)var2.get("kiv_fejlec_width"));
         double var3 = this.getCorrection((double)this.titleWidth);
         this.titleHeight = (int)((double)this.titleHeight * var3 / this.jepCorrection);
         this.titleWidth = (int)((double)this.titleWidth * var3 / this.jepCorrection);
      } else {
         var1 = new Vector[]{new Vector(), new Vector()};
      }

      return this.parseRows(var1);
   }

   public static int getSeq(int var0) {
      return seq[var0];
   }
}
