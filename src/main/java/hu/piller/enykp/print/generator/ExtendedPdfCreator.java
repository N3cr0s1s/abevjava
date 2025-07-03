package hu.piller.enykp.print.generator;

import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfWriter;
import hu.piller.enykp.alogic.calculator.CalculatorManager;
import hu.piller.enykp.alogic.orghandler.OrgInfo;
import hu.piller.enykp.alogic.orghandler.OrgResource;
import hu.piller.enykp.datastore.Elem;
import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.gui.framework.MainFrame;
import hu.piller.enykp.gui.model.BookModel;
import hu.piller.enykp.gui.model.PageModel;
import hu.piller.enykp.gui.model.SimpleViewModel;
import hu.piller.enykp.gui.viewer.FormPrinter;
import hu.piller.enykp.interfaces.IDataStore;
import hu.piller.enykp.print.Lap;
import hu.piller.enykp.print.LapMetaAdat;
import hu.piller.enykp.print.Utils;
import hu.piller.enykp.print.simpleprint.KPrintFormFeedType;
import hu.piller.enykp.print.simpleprint.PageTitle;
import hu.piller.enykp.util.base.PropertyList;
import hu.piller.enykp.util.base.Result;
import hu.piller.enykp.util.base.Tools;
import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.PrinterException;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.JEditorPane;
import javax.swing.table.DefaultTableModel;

public class ExtendedPdfCreator extends APdfCreator {
   public ExtendedPdfCreator(BookModel var1, boolean var2) {
      this.kivonatolt = var2;
      bookModel = var1;
      this.formModel = var1.get(((Elem)var1.cc.getActiveObject()).getType());
   }

   public Result createAndCheck() throws Exception {
      Result var1 = this.create();
      if (var1.isOk()) {
         if (this.isHeaderWidthOk((byte[])((byte[])var1.errorList.get(0)), "1300")) {
            return var1;
         }

         var1 = this.create();
         if (var1.isOk()) {
            System.out.println("2. probalkozas");
            if (!this.isHeaderWidthOk((byte[])((byte[])var1.errorList.get(0)), "1300")) {
               System.out.println("De ez sem sikerult!");
               var1.setOk(false);
               var1.errorList.clear();
               var1.errorList.add("Header error!");
            }
         }
      }

      return var1;
   }

   private Result create() throws Exception {
      this.time = System.currentTimeMillis();
      Tools.log("----------- PDF creation started");
      String var1 = MainFrame.role;
      MainFrame.role = "0";
      Result var2 = new Result();

      try {
         try {
            sablonVerzio = (String)bookModel.docinfo.get("ver");
         } catch (Exception var22) {
            if (sablonVerzio == null) {
               sablonVerzio = "1.0";
            }
         }

         bookModel.cc.setActiveObject(bookModel.cc.get(0));
         Elem var3 = (Elem)bookModel.cc.getActiveObject();
         IDataStore var4 = (IDataStore)var3.getRef();
         this.formModel = bookModel.get(((Elem)bookModel.cc.get(0)).getType());
         formId = this.formModel.id;
         if (formId == null) {
            throw new Exception("Nem határozható meg a formId!");
         }

         this.setKVPrintParams();
         Utils var5 = new Utils(bookModel);
         Vector var6 = var5.getMetadataV(0);
         this.lapok = new Lap[var6.size()];
         int[] var7 = (int[])((int[])((Elem)bookModel.cc.get(0)).getEtc().get("pagecounts"));
         Vector var8 = new Vector();

         int var12;
         for(int var9 = 0; var9 < var7.length; ++var9) {
            String var10 = bookModel.get(formId).get(var9).pid;
            Object[] var11 = CalculatorManager.getInstance().check_page(var10);

            for(var12 = 0; var12 < var7[var9]; ++var12) {
               var8.add(var11[1]);
            }
         }

         Vector var27 = new Vector();

         int var28;
         for(var28 = 0; var28 < var7.length; ++var28) {
            PageModel var29 = bookModel.get(formId).get(var28);
            var12 = var29.role;
            int var13 = var29.getmask();
            boolean var14 = (var12 & var13) == 0;

            for(int var15 = 0; var15 < var7[var28]; ++var15) {
               var27.add(var14);
            }
         }

         for(var28 = 0; var28 < this.lapok.length; ++var28) {
            this.lapok[var28] = new Lap();
            this.lapok[var28].setLma((LapMetaAdat)var6.get(var28));
            Utils.needPrint(this.lapok[var28], var4, formId);
            if (this.lapok[var28].getLma().lapSzam > 0 && this.lapok[var28].getLma().isNyomtatando()) {
               this.lapok[var28 - this.lapok[var28].getLma().lapSzam].getLma().setNyomtatando(true);
            }

            this.lapok[var28].getLma().isGuiEnabled = (Boolean)var8.get(var28);
            this.lapok[var28].getLma().disabledByRole = (Boolean)var27.get(var28);
            if (!this.lapok[var28].getLma().isGuiEnabled) {
               this.lapok[var28].getLma().setNyomtatando(false);
            }

            if (this.lapok[var28].getLma().disabledByRole) {
               this.lapok[var28].getLma().setNyomtatando(false);
            }
         }

         this.kulonNyomtatandoLapok(this.kivonatolt);
         if (!this.vankijeloltLap()) {
            throw new APdfCreator.NoSelectedPageException();
         }

         boolean var30 = this.doPrintPreview();
         if (this.kivonatolt) {
            if (!var30) {
               var2 = this.handleSPPrint();
            }
         } else {
            var2 = this.doPrint();
         }

         Tools.log("PDF created: " + (System.currentTimeMillis() - this.time));
         if (!var2.isOk()) {
            throw new Exception((String)var2.errorList.get(0));
         }

         Utils.setBookModel((BookModel)null);
         var5 = null;
      } catch (APdfCreator.PrintCancelledException var23) {
         Tools.eLog(var23, 0);
      } catch (PrinterException var24) {
         throw var24;
      } catch (Exception var25) {
         Tools.exception2SOut(var25);
         this.hiba("panel inicializálási hiba!", var25, 0);
         throw new Exception("Inicializálási hiba (2)");
      } finally {
         MainFrame.role = var1;
      }

      return var2;
   }

   private boolean doPrintPreview() throws Exception {
      int var1 = 0;
      boolean var2 = false;

      for(int var4 = 0; var4 < this.lapok.length; ++var4) {
         if (this.lapok[var4].getLma().lapSzam <= 0 && this.lapok[var4].getLma().isNyomtatando() && !this.lapok[var4].getLma().uniquePrintable) {
            ++var1;
         }
      }

      int var3 = this.object4UniquePrint.getUniqueDataPages().size();
      if (var1 == 0) {
         if (var3 == 0) {
            throw new APdfCreator.NoSelectedPageException();
         }

         var1 = var3;
         var2 = true;
      }

      this.book = new Book();
      this.object4UniquePrint.setUniquePrintablePages(new Vector());
      if (var1 > 0) {
         Paper var11 = new Paper();
         var11.setSize(594.0D, 841.0D);
         var11.setImageableArea(10.0D, 10.0D, 584.0D, 831.0D);
         PageFormat var6 = new PageFormat();
         var6.setPaper(var11);
         PageFormat var7 = new PageFormat();
         var7.setPaper(var11);
         var6.setOrientation(1);
         var7.setOrientation(0);
         defaultPageFormat4htmlPrint = var6;
         FormPrinter var8 = new FormPrinter(bookModel);
         var8.setIndex(0);

         for(int var9 = 0; var9 < this.lapok.length; ++var9) {
            if (this.lapok[var9].getLma().isGuiEnabled && !this.lapok[var9].getLma().disabledByRole) {
               PageFormat var5;
               if (this.lapok[var9].getLma().alloLap) {
                  var5 = var6;
               } else {
                  var5 = var7;
               }

               this.lapok[var9].setPrintable(var8);
               this.lapok[var9].setPf(var5);
               this.lapok[var9].setxArany(100.0D);
               if (this.lapok[var9].getLma().isNyomtatando() || this.lapok[var9].getLma().uniquePrintable) {
                  if (this.lapok[var9].getLma().uniquePrintable) {
                     Book var10 = new Book();
                     var10.append(this.lapok[var9], var5);
                     this.object4UniquePrint.getUniquePrintablePages().add(var10);
                  } else {
                     this.book.append(this.lapok[var9], var5);
                  }
               }
            }
         }
      }

      if (this.object4UniquePrint.getUniquePrintablePages().size() > 0) {
         this.object4UniquePrint.setNormalPrintAfterKPrint(true);
      }

      return var2;
   }

   private Result doPrint() {
      Result var1 = new Result();

      try {
         try {
            PdfPrint4Calculation var2 = new PdfPrint4Calculation(this.book);
            ByteArrayOutputStream var3 = null;
            this.document = new Document();
            var3 = new ByteArrayOutputStream();
            this.writer = PdfWriter.getInstance(this.document, var3);
            this.document.open();
            var2.print2PDF(this.document, this.writer);
            this.document.close();
            var1.errorList.add(var3.toByteArray());
         } catch (PrinterException var5) {
            try {
               this.document.close();
            } catch (Exception var4) {
               Tools.eLog(var4, 0);
            }
         }
      } catch (Exception var6) {
         var1.setOk(false);
         var1.errorList.insertElementAt(var6.toString(), 0);
         Tools.exception2SOut(var6);
      }

      return var1;
   }

   private Result handleSPPrint() {
      Result var1 = new Result();
      if (this.checkIfOnlyUniquePrintablePages()) {
         var1.setOk(false);
         var1.errorList.insertElementAt("Csak nyomtatványképesen nyomtatandó lapja van a nyomtatványnak (EGYSZA)", 0);
         return var1;
      } else {
         return this.doSimplePrint2Pdf();
      }
   }

   private Result doSimplePrint2Pdf() {
      Result var1 = new Result();

      try {
         Object[] var2 = this.getData4SimplePrint2();
         if (var2 == null) {
            var1.setOk(false);
            var1.errorList.insertElementAt("Nem sikerült előállítani a kivonatolt nyomtatási adatokat", 0);
            return var1;
         }

         Vector[] var3 = this.getGenHeaderAndFooter();
         String var4 = (String)var2[0];

         try {
            var4 = var4.replaceAll("#13", "<br>");
         } catch (Exception var12) {
            Tools.eLog(var12, 0);
         }

         this.time = System.currentTimeMillis();

         try {
            Jep2Pdf4Calculation var5 = new Jep2Pdf4Calculation(var4, this.seq);
            var5.setFooterVector(var3[2]);
            var5.setHeaderVector2(var3[1]);
            var5.setHeaderVector(var3[0]);
            ByteArrayOutputStream var6 = new ByteArrayOutputStream();
            this.document = new Document();
            this.writer = PdfWriter.getInstance(this.document, var6);
            this.document.open();
            var5.printJep2Pdf(this.document, this.writer, false, 0);

            for(int var7 = 1; var7 < var2.length; ++var7) {
               var4 = ((String)var2[var7]).replaceAll("#13", "<br>");
               if (var4.startsWith("#LANDSCAPE#")) {
                  var4 = var4.substring(11);
                  var5.reInitPane(var4, 0);
                  var5.printLandscapeJep2Pdf(this.document, this.writer, var7 == var2.length, var7);
               } else {
                  var5.reInitPane(var4, 1);
                  Vector var8 = new Vector(var3[1]);

                  String var9;
                  try {
                     var9 = (String)var8.remove(0);
                     var9 = var9.substring("Adóbevallási tervezet - ".length());
                  } catch (Exception var11) {
                     var9 = "";
                  }

                  var8.insertElementAt(var9, 0);
                  var5.setHeaderVector2(var8);
                  var5.printJep2Pdf(this.document, this.writer, var7 == var2.length, var7);
               }
            }

            this.document.close();
            var1.errorList.add(var6.toByteArray());
            this.document = null;
            this.writer.close();
            this.writer = null;
            var5 = null;
         } catch (Exception var13) {
            Tools.exception2SOut(var13);
            var1.errorList.insertElementAt("Hiba történt a pdf előállítás során", 0);
            var1.setOk(false);
            return var1;
         }
      } catch (Exception var14) {
         Tools.exception2SOut(var14);
         var1.errorList.insertElementAt("Nem sikerült előállítani a kivonatolt nyomtatási adatokat", 0);
         var1.setOk(false);
      }

      return var1;
   }

   private Object[] getData4SimplePrint2() throws Exception {
      SimpleViewModel var1 = new SimpleViewModel(bookModel);
      Hashtable var2 = this.getNyomtatandoLapnevek();
      if (var2.size() == 0) {
         return null;
      } else {
         Vector var3 = var1.getResult(var2);
         ArrayList var4 = this.convert2MultipleSimpleData(var3);
         String var5 = this.tableModels2DummyHtmlTable((Vector)var4.get(0));
         var5 = var5.replaceAll("#13", "<br>");
         JEditorPane var6 = new JEditorPane("text/html", var5);
         double var7 = this.getCorrection(var6.getMinimumSize().getWidth());
         var6 = null;
         String[] var9 = new String[var4.size()];
         this.seq = new int[var4.size()];

         for(int var10 = 0; var10 < var9.length; ++var10) {
            var9[var10] = this.tableModels2HtmlTable((Vector)var4.get(var10), var7, var10);
            if (((Vector)var4.get(var10)).get(0) instanceof String) {
               String var11 = (String)((Vector)var4.get(var10)).get(0);
               if (var11.startsWith("#LANDSCAPE#")) {
                  var9[var10] = "#LANDSCAPE#" + var9[var10];
               }
            }
         }

         return var9;
      }
   }

   private String tableModels2DummyHtmlTable(Vector var1) {
      StringBuffer var2 = new StringBuffer("<html><head>");
      var2.append("<STYLE TYPE=\"text/css\">").append("#tdr {text-align:right; padding-left:2px;}").append("#tdc {text-align:center;}").append("td {").append(" padding-top: -2px;").append(" padding-bottom: -2px;").append(" padding-left: 2px;").append(" padding-right: 2px;").append(" font-size: ").append(kivPrTdFontSize).append("px;").append(" }").append("table {").append(" border-collapse: collapse;").append("}").append("</STYLE>");
      var2.append("</head><body style=\"font-size: 6px;\">");
      byte var3 = -1;
      boolean var4 = false;
      String var5 = "";
      String var6 = "<td width=\"1\" bgcolor=\"" + this.thBgColor + "\" style=\"width:1px; padding:0;\"><img src=\"" + this.backgroundImage + "\" width=\"1\" height=\"1\" border=\"0\"></td>";
      String var7 = "<td width=\"1\" bgcolor=\"white\" style=\"width:1px; padding:0;\"><img src=\"" + this.backgroundImage + "\" width=\"1\" height=\"1\" border=\"0\"></td>";
      boolean var8 = false;
      boolean var9 = false;

      for(int var10 = 0; var10 < var1.size(); ++var10) {
         if (var1.get(var10) instanceof String) {
            String var15 = (String)var1.get(var10);
            if (!var15.startsWith("AF") && !var15.startsWith("17SZJA-AF") && !var15.startsWith("18SZJA-AF")) {
               if (var8) {
                  var8 = false;
               }
            } else {
               var8 = true;
            }

            if (!var15.startsWith("JOV") && !var15.startsWith("17SZJA-JOV") && !var15.startsWith("18SZJA-JOV")) {
               if (var9) {
                  var9 = false;
               }
            } else {
               var9 = false;
            }

            var2.append("<br>");
         } else {
            var4 = false;
            DefaultTableModel var11 = (DefaultTableModel)var1.get(var10);
            var2.append("<br><table border=\"1\" cellpadding=\"").append(kivPrTdCellPadding).append("\" cellspacing=\"0\" width=\"100%\">");
            int var12 = var11.getColumnCount();
            if (!var8 && !var9) {
               for(int var13 = 0; var13 < var11.getRowCount(); ++var13) {
                  var2.append("<tr>");
                  int var14;
                  if (var13 == 0) {
                     if (var11.getValueAt(0, 0) != null && ((String)var11.getValueAt(0, 0)).startsWith("<th>")) {
                        var5 = " bgcolor=\"" + this.thBgColor + "\"";
                     }

                     var2.append("<td valign=\"top\"").append(var5).append(">").append(var11.getValueAt(0, 0) == null ? "&nbsp;" : var11.getValueAt(0, 0).toString().replaceAll("<th>", "")).append("</td>");

                     for(var14 = 1; var14 < var12; ++var14) {
                        var2.append("<td valign=\"top\"").append(var5).append(">").append(var11.getValueAt(var13, var14) == null ? "&nbsp;" : var11.getValueAt(var13, var14)).append("</td>");
                     }
                  } else {
                     for(var14 = 0; var14 < var12; ++var14) {
                        var5 = "";
                        var2.append("<td valign=\"top\"").append(var5).append(">").append(var11.getValueAt(var13, var14) == null ? "&nbsp;" : var11.getValueAt(var13, var14)).append("</td>");
                     }
                  }

                  var2.append("</tr>");
               }

               var2.append("</table>");
            }
         }
      }

      if (var4) {
         var2.delete(var3, var2.length());
      }

      var2.append("</body></html>");
      return var2.toString();
   }

   private String tableModels2HtmlTable(Vector var1, double var2, int var4) throws Exception {
      int var5 = 0;
      String var6 = "";
      OrgInfo var7 = OrgInfo.getInstance();
      Hashtable var8 = (Hashtable)((Hashtable)var7.getOrgInfo(bookModel.docinfo.get("org"))).get("attributes");
      String var9 = ((OrgResource)((Hashtable)var7.getOrgList()).get(bookModel.docinfo.get("org"))).getVersion().toString();
      String var10 = var8.get("prefix") + "Resources_" + var9;
      if (PropertyList.getInstance().get("prop.const.db.pdf_gen.bgImage") != null) {
         this.backgroundImage = (String)PropertyList.getInstance().get("prop.const.db.pdf_gen.bgImage");
      } else {
         URI var11 = new URI(this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath());
         this.backgroundImage = (new File(var11.getPath())).getParent() + "/abevjava.jar!/resources/print/bg.gif";
         this.backgroundImage = this.backgroundImage.startsWith("/") ? "file:" + this.backgroundImage : "file:/" + this.backgroundImage;
         this.backgroundImage = "jar:" + this.backgroundImage;
      }

      String var30 = "<td width=\"1\" bgcolor=\"" + this.thBgColor + "\" style=\"width:1px; padding:0;\"><img src=\"" + this.backgroundImage + "\" width=\"1\" height=\"1\" border=\"0\"></td>";
      String var12 = "<td width=\"1\" bgcolor=\"white\" style=\"width:1px; padding:0;\"><img src=\"" + this.backgroundImage + "\" width=\"1\" height=\"1\" border=\"0\"></td>";
      String var13 = PropertyList.getInstance().get("prop.sys.root").toString().replaceAll("\\\\", "/");
      var13 = var13.startsWith("/") ? "file:" + var13 : "file:/" + var13;
      var13 = var13 + "/eroforrasok";
      Hashtable var14 = this.formModel.kvprintht;
      int var15 = -1;
      int var16 = -1;
      if (var14 != null) {
         byte var31 = 106;
         var16 = Integer.parseInt((String)var14.get("kiv_fejlec_width"));
         double var17 = this.getCorrection((double)var16);
         var15 = (int)((double)var31 * var17 / var2);
         var16 = (int)((double)var16 * var17 / var2);
      }

      Vector[] var32 = this.getGenHeaderAndFooter();
      String var18 = null;
      if (PropertyList.getInstance().get("prop.const.db.pdf_gen.nevGenFejlecImage") != null) {
         var18 = (String)PropertyList.getInstance().get("prop.const.db.pdf_gen.nevGenFejlecImage");
      } else {
         var18 = "jar:" + var13 + "/" + var10 + ".jar!/pictures/navgenfejlec.png";
      }

      var6 = "<table width=\"" + var16 + "\" height=\"" + var15 + "\"><tr><td><img src=\"" + var18 + "\"  width=\"" + var16 + "\" height=\"" + var15 + "\" border=\"0\"></td></tr></table>";
      StringBuffer var19 = new StringBuffer("<html><head>");
      var19.append("<STYLE TYPE=\"text/css\">").append("#tdl {text-align:left; padding-left:2px;}").append("#tdr {text-align:right; padding-left:2px;}").append("#tdc {text-align:center;}").append("#ntdl {text-align:left; padding-left:2px; width: ").append(this.one_cell_width).append("px;}").append("#ntdr {text-align:right; padding-left:2px; width: ").append(this.one_cell_width).append("px;}").append("#ntdc {text-align:center; width: 100px;}").append("td {").append(" padding-top: -2px;").append(" padding-bottom: -2px;").append(" padding-left: 2px;").append(" padding-right: 2px;").append(" font-size: ").append(kivPrTdFontSize).append("px;").append(" }").append("table {").append(" border-collapse: collapse;").append("}").append("</STYLE>");
      var19.append("</head><body style=\"font-size: 10px;\"><div align=\"center\">");
      if (var4 == 0) {
         var19.append(var6);
      }

      var19.append("</div><br><br>");
      byte var20 = -1;
      boolean var21 = false;
      String var22 = "";

      for(int var23 = 0; var23 < var1.size(); ++var23) {
         if (var1.get(var23) instanceof String) {
            String var33 = (String)var1.get(var23);
            if (var33.startsWith("#LANDSCAPE#")) {
               var33 = var33.substring("#LANDSCAPE#".length());
               if (!var33.matches("AF-\\d\\d \\( \\d* \\)")) {
                  var19.append("<center><br>").append(var33).append("</center>");
               }
            }

            var21 = true;
            var19.append("<br>");
         } else {
            DefaultTableModel var24 = (DefaultTableModel)var1.get(var23);
            int var25 = var24.getColumnCount();
            var19.append("<br><table border=\"0\" cellpadding=\"1\" cellspacing=\"0\" width=\"100%\">");
            if (var24.getValueAt(0, 0) != null && ((String)var24.getValueAt(0, 0)).startsWith("<th>")) {
               var24.setValueAt(((String)var24.getValueAt(0, 0)).replaceAll("<th>", ""), 0, 0);
               var22 = " bgcolor=\"" + this.thBgColor + "\"";
            }

            var21 = false;

            for(int var27 = 0; var27 < var24.getRowCount(); ++var27) {
               var19.append(this.getHBorder(2 * var25 + 1)).append("<tr>").append(var30);
               Object var26;
               int var28;
               String var34;
               if (var24.getValueAt(var27, 0) != null && var24.getValueAt(var27, 0).toString().startsWith("***")) {
                  if (var24.getValueAt(var27, 0) != null && var24.getValueAt(var27, 0).toString().startsWith("***#")) {
                     for(var28 = 0; var28 < var25; ++var28) {
                        var22 = "";
                        var19.append("<td class=\"").append(var5++).append("\" valign=\"top\"").append(var22).append(">");
                        var26 = var24.getValueAt(var27, var28);
                        if (var26 == null) {
                           var26 = "&nbsp;";
                        } else {
                           if (var28 == 0) {
                              var26 = var26.toString().substring(4);
                           }

                           if (var26.toString().startsWith(" id=\"ntd")) {
                              var19.deleteCharAt(var19.length() - 1);
                              var26 = var26.toString().substring(0, 5) + var26.toString().substring(6);
                           }
                        }

                        var34 = GuiUtil.getPlainLabelText(var26.toString());
                        var19.append(var34.toString().replaceAll("@#@#", "&nbsp;")).append("</td>").append(var28 == var25 - 1 ? var30 : var12);
                     }
                  } else {
                     String var35 = this.handle1CellHeader((Vector)var24.getDataVector().get(var27), var22, var4);
                     if (var35 != null) {
                        var19.append(var35).append(var30);
                     } else {
                        var22 = "";

                        for(int var29 = 0; var29 < var25; ++var29) {
                           var19.append("<td class=\"").append(var5++).append("\" valign=\"top\"").append(var22).append(">");
                           var26 = var24.getValueAt(var27, var29);
                           if (var26 == null) {
                              var26 = "&nbsp;";
                           } else {
                              if (var29 == 0) {
                                 var26 = var26.toString().substring(3);
                              }

                              if (var26.toString().startsWith(" id=\"ntd")) {
                                 var19.deleteCharAt(var19.length() - 1);
                              }
                           }

                           var34 = GuiUtil.getPlainLabelText(var26.toString());
                           var19.append(var34.toString().replaceAll("@#@#", "&nbsp;")).append("</td>").append(var29 == var25 - 1 ? var30 : var12);
                        }
                     }
                  }
               } else if (var27 == 0) {
                  for(var28 = 0; var28 < var25; ++var28) {
                     var19.append("<td class=\"").append(var5++).append("\" valign=\"top\"").append(var22).append(">");
                     var26 = var24.getValueAt(var27, var28);
                     if (var26 == null) {
                        var26 = "&nbsp;";
                     } else if (var26.toString().startsWith(" id=\"ntd")) {
                        var19.deleteCharAt(var19.length() - 1);
                     }

                     var34 = GuiUtil.getPlainLabelText(var26.toString());
                     var19.append(var34.toString().replaceAll("@#@#", "&nbsp;")).append("</td>").append(var30);
                  }
               } else {
                  for(var28 = 0; var28 < var25; ++var28) {
                     var22 = "";
                     var19.append("<td class=\"").append(var5++).append("\" valign=\"top\"").append(var22).append(">");
                     var26 = var24.getValueAt(var27, var28);
                     if (var26 == null) {
                        var26 = "&nbsp;";
                     } else if (var26.toString().startsWith(" id=\"ntd")) {
                        var19.deleteCharAt(var19.length() - 1);
                     }

                     var34 = GuiUtil.getPlainLabelText(var26.toString());
                     var19.append(var34.toString().replaceAll("@#@#", "&nbsp;")).append("</td>").append(var30);
                  }
               }

               var19.append("</tr>");
            }

            var19.append(this.getHBorder(2 * var25 + 1)).append("</table>");
         }
      }

      if (var21 && var20 >= 0) {
         var5 = this.handleSeq(var19, var20, var5);
         var19.delete(var20, var19.length());
      }

      var19.append("</body></html>");
      this.seq[var4] = var5;
      return var19.toString();
   }

   private int handleSeq(StringBuffer var1, int var2, int var3) {
      try {
         String var4 = var1.substring(var2);
         String[] var5 = var4.split("class=");
         var3 -= var5.length - 1;
      } catch (Exception var6) {
         Tools.eLog(var6, 0);
      }

      return var3;
   }

   public static String getFootText() {
      return "Kitöltő verzió:" + "v.3.44.0".substring(2) + " Nyomtatvány verzió:" + sablonVerzio;
   }

   private byte[] getNormalPdfAsBytes() {
      if (this.object4UniquePrint.isNormalPrintAfterKPrint() && this.object4UniquePrint.getUniqueDataPages().size() > 0) {
         for(int var1 = 0; var1 < this.object4UniquePrint.getUniquePrintablePages().size(); ++var1) {
            try {
               this.book = (Book)this.object4UniquePrint.getUniquePrintablePages().elementAt(var1);
               Result var2 = this.doPrint();
               if (var2.isOk()) {
                  return (byte[])((byte[])var2.errorList.get(0));
               }
            } catch (NumberFormatException var3) {
               var3.printStackTrace();
            } catch (Exception var4) {
               var4.printStackTrace();
            }
         }
      }

      this.object4UniquePrint.setNormalPrintAfterKPrint(false);
      return null;
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
}
