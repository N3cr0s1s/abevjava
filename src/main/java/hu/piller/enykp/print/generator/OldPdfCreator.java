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
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.JEditorPane;
import javax.swing.table.DefaultTableModel;

public class OldPdfCreator extends APdfCreator {
   public OldPdfCreator(BookModel var1, boolean var2) {
      this.kivonatolt = var2;
      bookModel = var1;
      this.seq = new int[]{0, 0, 0};
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
         var1.errorList.insertElementAt("Csak nyomtatványképesen nyomtatandó lapj van a nyomtatványnak (EGYSZA)", 0);
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

         String var3 = (String)var2[0];
         String var4 = (String)var2[1];
         String var5 = (String)var2[3];

         try {
            var3 = var3.replaceAll("#13", "<br>");
         } catch (Exception var15) {
            Tools.eLog(var15, 0);
         }

         try {
            var4 = var4.replaceAll("#13", "<br>");
         } catch (Exception var14) {
            Tools.eLog(var14, 0);
         }

         try {
            var5 = var5.replaceAll("#13", "<br>");
         } catch (Exception var13) {
            Tools.eLog(var13, 0);
         }

         this.time = System.currentTimeMillis();

         try {
            Jep2Pdf4Calculation var6 = new Jep2Pdf4Calculation(var3, this.seq);
            var6.setFooterVector(((Vector[])((Vector[])var2[2]))[2]);
            var6.setHeaderVector2(((Vector[])((Vector[])var2[2]))[1]);
            var6.setHeaderVector(((Vector[])((Vector[])var2[2]))[0]);
            ByteArrayOutputStream var7 = new ByteArrayOutputStream();
            this.document = new Document();
            this.writer = PdfWriter.getInstance(this.document, var7);
            this.document.open();
            var6.printJep2Pdf(this.document, this.writer, false, 0);
            if (!"".equals(var4)) {
               var6.reInitPane(var4, 0);
               var6.printLandscapeJep2Pdf(this.document, this.writer, false, 1);
            }

            if (!"".equals(var5)) {
               var6.reInitPane(var5, 1);
               Vector var8 = ((Vector[])((Vector[])var2[2]))[1];

               String var9;
               try {
                  var9 = (String)var8.remove(0);
                  var9 = var9.substring("Adóbevallási tervezet - ".length());
               } catch (Exception var11) {
                  var9 = "";
               }

               var8.insertElementAt(var9, 0);
               var6.setHeaderVector2(var8);
               var6.printJep2Pdf(this.document, this.writer, true, 2);
            }

            this.document.close();
            var1.errorList.add(var7.toByteArray());
            this.document = null;
            var7.close();
            this.writer.close();
            this.writer = null;
            var6 = null;
         } catch (Exception var12) {
            Tools.exception2SOut(var12);
            var1.errorList.insertElementAt("Hiba történt a pdf előállítás során", 0);
            var1.setOk(false);
            return var1;
         }
      } catch (Exception var16) {
         Tools.exception2SOut(var16);
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
         String var4 = this.tableModels2DummyHtmlTable(var3);
         JEditorPane var5 = new JEditorPane("text/html", var4);
         double var6 = this.getCorrection(var5.getMinimumSize().getWidth());
         var5 = null;
         return this.tableModels2HtmlTable(var3, var6);
      }
   }

   private String tableModels2DummyHtmlTable(Vector var1) {
      StringBuffer var2 = new StringBuffer("<html><head>");
      var2.append("<STYLE TYPE=\"text/css\">").append("#tdr {text-align:right; padding-left:2px;}").append("#tdc {text-align:center;}").append("td {").append(" padding-top: -2px;").append(" padding-bottom: -2px;").append(" padding-left: 2px;").append(" padding-right: 2px;").append(" font-size: ").append(kivPrTdFontSize).append("px;").append(" }").append("table {").append(" border-collapse: collapse;").append("}").append("</STYLE>");
      var2.append("</head><body style=\"font-size: 6px;\">");
      byte var3 = -1;
      boolean var4 = false;
      String var5 = "";
      StringBuilder var6 = null;
      StringBuilder var7 = null;
      String var8 = "<td width=\"1\" bgcolor=\"" + this.thBgColor + "\" style=\"width:1px; padding:0;\"><img src=\"" + this.backgroundImage + "\" width=\"1\" height=\"1\" border=\"0\"></td>";
      String var9 = "<td width=\"1\" bgcolor=\"white\" style=\"width:1px; padding:0;\"><img src=\"" + this.backgroundImage + "\" width=\"1\" height=\"1\" border=\"0\"></td>";
      boolean var10 = false;
      boolean var11 = false;

      for(int var12 = 0; var12 < var1.size(); ++var12) {
         if (var1.get(var12) instanceof PageTitle) {
            String var17 = ((PageTitle)var1.get(var12)).getTitleString();
            if (!var17.startsWith("AF") && !var17.startsWith("17SZJA-AF") && !var17.startsWith("18SZJA-AF") && !var17.startsWith("19SZJA-AF") && !var17.startsWith("20SZJA-AF") && !var17.startsWith("21SZJA-AF") && !var17.startsWith("22SZJA-AF") && !var17.startsWith("23SZJA-AF")) {
               if (var10) {
                  var10 = false;
               }
            } else {
               var10 = true;
               var6 = this.getAFPageData(var1.get(var12), var5, var8, var9, var6, true);
            }

            if (!var17.startsWith("JOV") && !var17.startsWith("17SZJA-JOV") && !var17.startsWith("18SZJA-JOV") && !var17.startsWith("19SZJA-JOV") && !var17.startsWith("20SZJA-JOV") && !var17.startsWith("21SZJA-JOV") && !var17.startsWith("22SZJA-JOV") && !var17.startsWith("23SZJA-JOV")) {
               if (var11) {
                  var11 = false;
               }
            } else {
               var11 = false;
               var7 = this.getJOVPageData(var1.get(var12), var5, var8, var9, var7, true);
            }

            var2.append("<br>");
         } else {
            var4 = false;
            DefaultTableModel var13 = (DefaultTableModel)var1.get(var12);
            var2.append("<br><table border=\"1\" cellpadding=\"").append(kivPrTdCellPadding).append("\" cellspacing=\"0\" width=\"100%\">");
            int var14 = var13.getColumnCount();
            if (var10) {
               var6 = this.getAFPageData(var13, var5, var8, var9, var6, true);
            } else if (var11) {
               var7 = this.getJOVPageData(var13, var5, var8, var9, var7, true);
            } else {
               for(int var15 = 0; var15 < var13.getRowCount(); ++var15) {
                  var2.append("<tr>");
                  int var16;
                  if (var15 == 0) {
                     if (var13.getValueAt(0, 0) != null && ((String)var13.getValueAt(0, 0)).startsWith("<th>")) {
                        var5 = " bgcolor=\"" + this.thBgColor + "\"";
                     }

                     var2.append("<td valign=\"top\"").append(var5).append(">").append(var13.getValueAt(0, 0) == null ? "&nbsp;" : var13.getValueAt(0, 0).toString().replaceAll("<th>", "")).append("</td>");

                     for(var16 = 1; var16 < var14; ++var16) {
                        var2.append("<td valign=\"top\"").append(var5).append(">").append(var13.getValueAt(var15, var16) == null ? "&nbsp;" : var13.getValueAt(var15, var16)).append("</td>");
                     }
                  } else {
                     for(var16 = 0; var16 < var14; ++var16) {
                        var5 = "";
                        var2.append("<td valign=\"top\"").append(var5).append(">").append(var13.getValueAt(var15, var16) == null ? "&nbsp;" : var13.getValueAt(var15, var16)).append("</td>");
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

   private Object[] tableModels2HtmlTable(Vector var1, double var2) throws Exception {
      String var4 = "";
      OrgInfo var5 = OrgInfo.getInstance();
      Hashtable var6 = (Hashtable)((Hashtable)var5.getOrgInfo(bookModel.docinfo.get("org"))).get("attributes");
      String var7 = ((OrgResource)((Hashtable)var5.getOrgList()).get(bookModel.docinfo.get("org"))).getVersion().toString();
      String var8 = var6.get("prefix") + "Resources_" + var7;
      if (PropertyList.getInstance().get("prop.const.db.pdf_gen.bgImage") != null) {
         this.backgroundImage = (String)PropertyList.getInstance().get("prop.const.db.pdf_gen.bgImage");
      } else {
         URI var9 = new URI(this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath());
         this.backgroundImage = (new File(var9.getPath())).getParent() + "/abevjava.jar!/resources/print/bg.gif";
         this.backgroundImage = this.backgroundImage.startsWith("/") ? "file:" + this.backgroundImage : "file:/" + this.backgroundImage;
         this.backgroundImage = "jar:" + this.backgroundImage;
      }

      String var31 = "<td width=\"1\" bgcolor=\"" + this.thBgColor + "\" style=\"width:1px; padding:0;\"><img src=\"" + this.backgroundImage + "\" width=\"1\" height=\"1\" border=\"0\"></td>";
      String var10 = "<td width=\"1\" bgcolor=\"white\" style=\"width:1px; padding:0;\"><img src=\"" + this.backgroundImage + "\" width=\"1\" height=\"1\" border=\"0\"></td>";
      String var11 = PropertyList.getInstance().get("prop.sys.root").toString().replaceAll("\\\\", "/");
      var11 = var11.startsWith("/") ? "file:" + var11 : "file:/" + var11;
      var11 = var11 + "/eroforrasok";
      Hashtable var12 = this.formModel.kvprintht;
      int var13 = -1;
      int var14 = -1;
      if (var12 != null) {
         byte var32 = 106;
         var14 = Integer.parseInt((String)var12.get("kiv_fejlec_width"));
         double var15 = this.getCorrection((double)var14);
         var13 = (int)((double)var32 * var15 / var2);
         var14 = (int)((double)var14 * var15 / var2);
      }

      Vector[] var33 = this.getGenHeaderAndFooter();
      String var16 = null;
      if (PropertyList.getInstance().get("prop.const.db.pdf_gen.nevGenFejlecImage") != null) {
         var16 = (String)PropertyList.getInstance().get("prop.const.db.pdf_gen.nevGenFejlecImage");
      } else {
         var16 = "jar:" + var11 + "/" + var8 + ".jar!/pictures/navgenfejlec.png";
      }

      var4 = "<table width=\"" + var14 + "\" height=\"" + var13 + "\"><tr><td><img src=\"" + var16 + "\"  width=\"" + var14 + "\" height=\"" + var13 + "\" border=\"0\"></td></tr></table>";
      StringBuffer var17 = new StringBuffer("<html><head>");
      var17.append("<STYLE TYPE=\"text/css\">").append("#tdl {text-align:left; padding-left:2px;}").append("#tdr {text-align:right; padding-left:2px;}").append("#tdc {text-align:center;}").append("#ntdl {text-align:left; padding-left:2px; width: ").append(this.one_cell_width).append("px;}").append("#ntdr {text-align:right; padding-left:2px; width: ").append(this.one_cell_width).append("px;}").append("#ntdc {text-align:center; width: 100px;}").append("td {").append(" padding-top: -2px;").append(" padding-bottom: -2px;").append(" padding-left: 2px;").append(" padding-right: 2px;").append(" font-size: ").append(kivPrTdFontSize).append("px;").append(" }").append("table {").append(" border-collapse: collapse;").append("}").append("</STYLE>");
      var17.append("</head><body style=\"font-size: 10px;\"><div align=\"center\">");
      var17.append(var4);
      var17.append("</div><br><br>");
      byte var18 = -1;
      boolean var19 = false;
      String var20 = "";
      StringBuilder var21 = null;
      StringBuilder var22 = null;
      boolean var23 = false;
      boolean var24 = false;

      for(int var25 = 0; var25 < var1.size(); ++var25) {
         if (var1.get(var25) instanceof PageTitle) {
            String var34 = ((PageTitle)var1.get(var25)).getTitleString();
            if (!var34.startsWith("AF") && !var34.matches("^\\d{2}SZJA-AF-0.*") && !var34.startsWith("17SZJA-AF") && !var34.startsWith("18SZJA-AF") && !var34.startsWith("19SZJA-AF") && !var34.startsWith("20SZJA-AF") && !var34.startsWith("21SZJA-AF") && !var34.startsWith("22SZJA-AF") && !var34.startsWith("23SZJA-AF")) {
               var23 = false;
            } else {
               var23 = true;
               var21 = this.getAFPageData(var1.get(var25), var20, var31, var10, var21, false);
            }

            if (!var34.startsWith("JOV") && !var34.matches("^\\d{2}SZJA-JOV.*") && !var34.startsWith("17SZJA-JOV") && !var34.startsWith("18SZJA-JOV") && !var34.startsWith("19SZJA-JOV") && !var34.startsWith("20SZJA-JOV") && !var34.startsWith("21SZJA-JOV") && !var34.startsWith("22SZJA-JOV") && !var34.startsWith("23SZJA-JOV")) {
               if (var24) {
                  var24 = false;
               }
            } else {
               var24 = true;
               var22 = this.getJOVPageData(var34, var20, var31, var10, var22, false);
            }

            var17.append("<br>");
         } else {
            var19 = false;
            DefaultTableModel var26 = (DefaultTableModel)var1.get(var25);
            int var27 = var26.getColumnCount();
            if (var23) {
               var21 = this.getAFPageData(var26, var20, var31, var10, var21, false);
            } else if (var24) {
               var22 = this.getJOVPageData(var26, var20, var31, var10, var22, false);
            } else {
               var17.append("<br><table border=\"0\" cellpadding=\"1\" cellspacing=\"0\" width=\"100%\">");
               if (var26.getValueAt(0, 0) != null && ((String)var26.getValueAt(0, 0)).startsWith("<th>")) {
                  var26.setValueAt(((String)var26.getValueAt(0, 0)).replaceAll("<th>", ""), 0, 0);
                  var20 = " bgcolor=\"" + this.thBgColor + "\"";
               }

               for(int var29 = 0; var29 < var26.getRowCount(); ++var29) {
                  var17.append(this.getHBorder(2 * var27 + 1)).append("<tr>").append(var31);
                  Object var28;
                  int var30;
                  String var35;
                  if (var26.getValueAt(var29, 0) != null && var26.getValueAt(var29, 0).toString().startsWith("***")) {
                     if (var26.getValueAt(var29, 0) != null && var26.getValueAt(var29, 0).toString().startsWith("***#")) {
                        for(var30 = 0; var30 < var27; ++var30) {
                           var20 = "";
                           var17.append("<td class=\"").append(this.seq[0]++).append("\" valign=\"top\"").append(var20).append(">");
                           var28 = var26.getValueAt(var29, var30);
                           if (var28 == null) {
                              var28 = "&nbsp;";
                           } else {
                              if (var30 == 0) {
                                 var28 = var28.toString().substring(4);
                              }

                              if (var28.toString().startsWith(" id=\"ntd")) {
                                 var17.deleteCharAt(var17.length() - 1);
                                 var28 = var28.toString().substring(0, 5) + var28.toString().substring(6);
                              }
                           }

                           var35 = GuiUtil.getPlainLabelText(var28.toString());
                           var17.append(var35.toString().replaceAll("@#@#", "&nbsp;")).append("</td>").append(var30 == var27 - 1 ? var31 : var10);
                        }
                     } else {
                        var20 = "";

                        for(var30 = 0; var30 < var27; ++var30) {
                           var17.append("<td class=\"").append(this.seq[0]++).append("\" valign=\"top\"").append(var20).append(">");
                           var28 = var26.getValueAt(var29, var30);
                           if (var28 == null) {
                              var28 = "&nbsp;";
                           } else {
                              if (var30 == 0) {
                                 var28 = var28.toString().substring(3);
                              }

                              if (var28.toString().startsWith(" id=\"ntd")) {
                                 var17.deleteCharAt(var17.length() - 1);
                              }
                           }

                           var35 = GuiUtil.getPlainLabelText(var28.toString());
                           var17.append(var35.toString().replaceAll("@#@#", "&nbsp;")).append("</td>").append(var30 == var27 - 1 ? var31 : var10);
                        }
                     }
                  } else if (var29 == 0) {
                     for(var30 = 0; var30 < var27; ++var30) {
                        var17.append("<td class=\"").append(this.seq[0]++).append("\" valign=\"top\"").append(var20).append(">");
                        var28 = var26.getValueAt(var29, var30);
                        if (var28 == null) {
                           var28 = "&nbsp;";
                        } else if (var28.toString().startsWith(" id=\"ntd")) {
                           var17.deleteCharAt(var17.length() - 1);
                        }

                        var35 = GuiUtil.getPlainLabelText(var28.toString());
                        var17.append(var35.toString().replaceAll("@#@#", "&nbsp;")).append("</td>").append(var31);
                     }
                  } else {
                     for(var30 = 0; var30 < var27; ++var30) {
                        var20 = "";
                        var17.append("<td class=\"").append(this.seq[0]++).append("\" valign=\"top\"").append(var20).append(">");
                        var28 = var26.getValueAt(var29, var30);
                        if (var28 == null) {
                           var28 = "&nbsp;";
                        } else if (var28.toString().startsWith(" id=\"ntd")) {
                           var17.deleteCharAt(var17.length() - 1);
                        }

                        var35 = GuiUtil.getPlainLabelText(var28.toString());
                        var17.append(var35.toString().replaceAll("@#@#", "&nbsp;")).append("</td>").append(var31);
                     }
                  }

                  var17.append("</tr>");
               }

               var17.append(this.getHBorder(2 * var27 + 1)).append("</table>");
            }
         }
      }

      if (var19 && var18 >= 0) {
         this.handleSeq(var17, var18);
         var17.delete(var18, var17.length());
      }

      var17.append("</body></html>");
      if (var21 != null) {
         var21.append("</body></html>");
      } else {
         var21 = new StringBuilder("");
      }

      if (var22 != null) {
         var22.append("</body></html>");
      } else {
         var22 = new StringBuilder("");
      }

      return new Object[]{var17.toString(), var21.toString(), var33, var22.toString()};
   }

   private void handleSeq(StringBuffer var1, int var2) {
      try {
         String var3 = var1.substring(var2);
         String[] var4 = var3.split("class=");
         this.seq[0] -= var4.length - 1;
      } catch (Exception var5) {
         Tools.eLog(var5, 0);
      }

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

   private StringBuilder getAFPageData(Object var1, String var2, String var3, String var4, StringBuilder var5, boolean var6) {
      String var7 = null;
      DefaultTableModel var8 = null;
      if (var1 instanceof PageTitle) {
         var7 = ((PageTitle)var1).getTitleString();
      } else {
         var8 = (DefaultTableModel)var1;
      }

      if (var5 == null) {
         var5 = new StringBuilder("<html><head>");
         var5.append("<STYLE TYPE=\"text/css\">").append("#tdr {text-align:right; padding-left:2px;}").append("#tdc {text-align:center;}").append("td {").append(" padding-top: -2px;").append(" padding-bottom: -2px;").append(" padding-left: 2px;").append(" padding-right: 2px;").append(" font-size: ").append(kivPrTdFontSize).append("px;").append(" }").append("table {").append(" border-collapse: collapse;").append("}").append("</STYLE>");
         var5.append("</head><body style=\"font-size: 10px;\">");
      }

      if (var7 != null) {
         if (!var7.matches("AF-\\d\\d \\( \\d* \\)")) {
            var5.append("<center>").append(var7).append("</center><br>");
         }
      } else {
         int var9 = var8.getColumnCount();
         var5.append("<table border=\"0\" cellpadding=\"1\" cellspacing=\"0\" width=\"100%\">");
         if (var8.getValueAt(0, 0) != null && ((String)var8.getValueAt(0, 0)).startsWith("<th>")) {
            if (!var6) {
               var8.setValueAt(((String)var8.getValueAt(0, 0)).replaceAll("<th>", ""), 0, 0);
            }

            var2 = " bgcolor=\"" + this.thBgColor + "\"";
         }

         for(int var11 = 0; var11 < var8.getRowCount(); ++var11) {
            var5.append(this.getHBorder(2 * var9 + 1)).append("<tr>").append(var3);
            Object var10;
            int var12;
            String var14;
            if (var8.getValueAt(var11, 0) != null && var8.getValueAt(var11, 0).toString().startsWith("***")) {
               if (var8.getValueAt(var11, 0) != null && var8.getValueAt(var11, 0).toString().startsWith("***#")) {
                  for(var12 = 0; var12 < var9; ++var12) {
                     var2 = "";
                     var5.append("<td class=\"").append(this.seq[1]++).append("\" valign=\"top\"").append(var2).append(">");
                     var10 = var8.getValueAt(var11, var12);
                     if (var10 == null) {
                        var10 = "&nbsp;";
                     } else {
                        if (var12 == 0) {
                           var10 = var10.toString().substring(4);
                        }

                        if (var10.toString().startsWith(" id=\"ntd")) {
                           var5.deleteCharAt(var5.length() - 1);
                           var10 = var10.toString().substring(0, 5) + var10.toString().substring(6);
                        }
                     }

                     var14 = GuiUtil.getPlainLabelText(var10.toString());
                     var5.append(var14.toString().replaceAll("@#@#", "&nbsp;")).append("</td>").append(var12 == var9 - 1 ? var3 : var4);
                  }
               } else {
                  String var15 = this.handle1CellHeader((Vector)var8.getDataVector().get(var11), var2, 1);
                  if (var15 != null) {
                     var5.append(var15).append(var3);
                  } else {
                     var2 = "";

                     for(int var13 = 0; var13 < var9; ++var13) {
                        var5.append("<td class=\"").append(this.seq[1]++).append("\" valign=\"top\"").append(var2).append(">");
                        var10 = var8.getValueAt(var11, var13);
                        if (var10 == null) {
                           var10 = "&nbsp;";
                        } else {
                           if (var13 == 0) {
                              var10 = var10.toString().substring(3);
                           }

                           if (var10.toString().startsWith(" id=\"ntd")) {
                              var5.deleteCharAt(var5.length() - 1);
                           }
                        }

                        var14 = GuiUtil.getPlainLabelText(var10.toString());
                        var5.append(var14.toString().replaceAll("@#@#", "&nbsp;")).append("</td>").append(var13 == var9 - 1 ? var3 : var4);
                     }
                  }
               }
            } else if (var11 == 0) {
               for(var12 = 0; var12 < var9; ++var12) {
                  var5.append("<td class=\"").append(this.seq[1]++).append("\" valign=\"top\"").append(var2).append(">");
                  var10 = var8.getValueAt(var11, var12);
                  if (var10 == null) {
                     var10 = "&nbsp;";
                  } else if (var10.toString().startsWith(" id=\"ntd")) {
                     var5.deleteCharAt(var5.length() - 1);
                  }

                  var14 = GuiUtil.getPlainLabelText(var10.toString());
                  var5.append(var14.toString().replaceAll("@#@#", "&nbsp;")).append("</td>").append(var3);
               }
            } else {
               for(var12 = 0; var12 < var9; ++var12) {
                  var2 = "";
                  var5.append("<td class=\"").append(this.seq[1]++).append("\" valign=\"top\"").append(var2).append(">");
                  var10 = var8.getValueAt(var11, var12);
                  if (var10 == null) {
                     var10 = "&nbsp;";
                  } else if (var10.toString().startsWith(" id=\"ntd")) {
                     var5.deleteCharAt(var5.length() - 1);
                  }

                  var14 = GuiUtil.getPlainLabelText(var10.toString());
                  var5.append(var14.toString().replaceAll("@#@#", "&nbsp;")).append("</td>").append(var3);
               }
            }

            var5.append("</tr>");
         }

         var5.append(this.getHBorder(2 * var9 + 1)).append("</table><br>");
      }

      return var5;
   }

   private StringBuilder getJOVPageData(Object var1, String var2, String var3, String var4, StringBuilder var5, boolean var6) {
      String var7 = null;
      DefaultTableModel var8 = null;
      if (var1 instanceof PageTitle) {
         var7 = ((PageTitle)var1).getTitleString();
      } else if (var1 instanceof String) {
         var7 = (String)var1;
      } else {
         var8 = (DefaultTableModel)var1;
      }

      if (var5 == null) {
         var5 = new StringBuilder("<html><head>");
         var5.append("<STYLE TYPE=\"text/css\">").append("#tdr {text-align:right; padding-left:2px;}").append("#tdc {text-align:center;}").append("td {").append(" padding-top: -2px;").append(" padding-bottom: -2px;").append(" padding-left: 2px;").append(" padding-right: 2px;").append(" font-size: ").append(kivPrTdFontSize).append("px;").append(" }").append("table {").append(" border-collapse: collapse;").append("}").append("</STYLE>");
         var5.append("</head><body style=\"font-size: 10px;\">");
      }

      if (var7 != null) {
         var5.append("<center>").append(var7).append("</center><br>");
      } else {
         int var9 = var8.getColumnCount();
         var5.append("<table border=\"0\" cellpadding=\"1\" cellspacing=\"0\" width=\"100%\">");
         if (var8.getValueAt(0, 0) != null && ((String)var8.getValueAt(0, 0)).startsWith("<th>")) {
            if (!var6) {
               var8.setValueAt(((String)var8.getValueAt(0, 0)).replaceAll("<th>", ""), 0, 0);
            }

            var2 = " bgcolor=\"" + this.thBgColor + "\"";
         }

         for(int var11 = 0; var11 < var8.getRowCount(); ++var11) {
            var5.append(this.getHBorder(2 * var9 + 1)).append("<tr>").append(var3);
            Object var10;
            int var12;
            String var13;
            if (var8.getValueAt(var11, 0) != null && var8.getValueAt(var11, 0).toString().startsWith("***")) {
               if (var8.getValueAt(var11, 0) != null && var8.getValueAt(var11, 0).toString().startsWith("***#")) {
                  for(var12 = 0; var12 < var9; ++var12) {
                     var2 = "";
                     var5.append("<td class=\"").append(this.seq[2]++).append("\" valign=\"top\"").append(var2).append(">");
                     var10 = var8.getValueAt(var11, var12);
                     if (var10 == null) {
                        var10 = "&nbsp;";
                     } else {
                        if (var12 == 0) {
                           var10 = var10.toString().substring(4);
                        }

                        if (var10.toString().startsWith(" id=\"ntd")) {
                           var5.deleteCharAt(var5.length() - 1);
                           var10 = var10.toString().substring(0, 5) + var10.toString().substring(6);
                        }
                     }

                     var13 = GuiUtil.getPlainLabelText(var10.toString());
                     var5.append(var13.toString().replaceAll("@#@#", "&nbsp;")).append("</td>").append(var12 == var9 - 1 ? var3 : var4);
                  }
               } else {
                  var2 = "";

                  for(var12 = 0; var12 < var9; ++var12) {
                     var5.append("<td class=\"").append(this.seq[2]++).append("\" valign=\"top\"").append(var2).append(">");
                     var10 = var8.getValueAt(var11, var12);
                     if (var10 == null) {
                        var10 = "&nbsp;";
                     } else {
                        if (var12 == 0) {
                           var10 = var10.toString().substring(3);
                        }

                        if (var10.toString().startsWith(" id=\"ntd")) {
                           var5.deleteCharAt(var5.length() - 1);
                        }
                     }

                     var13 = GuiUtil.getPlainLabelText(var10.toString());
                     var5.append(var13.toString().replaceAll("@#@#", "&nbsp;")).append("</td>").append(var12 == var9 - 1 ? var3 : var4);
                  }
               }
            } else if (var11 == 0) {
               for(var12 = 0; var12 < var9; ++var12) {
                  var5.append("<td class=\"").append(this.seq[2]++).append("\" valign=\"top\"").append(var2).append(">");
                  var10 = var8.getValueAt(var11, var12);
                  if (var10 == null) {
                     var10 = "&nbsp;";
                  } else if (var10.toString().startsWith(" id=\"ntd")) {
                     var5.deleteCharAt(var5.length() - 1);
                  }

                  var13 = GuiUtil.getPlainLabelText(var10.toString());
                  var5.append(var13.toString().replaceAll("@#@#", "&nbsp;")).append("</td>").append(var3);
               }
            } else {
               for(var12 = 0; var12 < var9; ++var12) {
                  var2 = "";
                  var5.append("<td class=\"").append(this.seq[2]++).append("\" valign=\"top\"").append(var2).append(">");
                  var10 = var8.getValueAt(var11, var12);
                  if (var10 == null) {
                     var10 = "&nbsp;";
                  } else if (var10.toString().startsWith(" id=\"ntd")) {
                     var5.deleteCharAt(var5.length() - 1);
                  }

                  var13 = GuiUtil.getPlainLabelText(var10.toString());
                  var5.append(var13.toString().replaceAll("@#@#", "&nbsp;")).append("</td>").append(var3);
               }
            }

            var5.append("</tr>");
         }

         var5.append(this.getHBorder(2 * var9 + 1)).append("</table><br>");
      }

      return var5;
   }
}
