package hu.piller.enykp.print;

import com.lowagie.text.Document;
import com.lowagie.text.pdf.PRStream;
import com.lowagie.text.pdf.PdfName;
import com.lowagie.text.pdf.PdfObject;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;
import hu.piller.enykp.alogic.calculator.CalculatorManager;
import hu.piller.enykp.alogic.metainfo.MetaInfo;
import hu.piller.enykp.alogic.orghandler.OrgInfo;
import hu.piller.enykp.alogic.orghandler.OrgResource;
import hu.piller.enykp.datastore.Elem;
import hu.piller.enykp.gui.framework.MainFrame;
import hu.piller.enykp.gui.model.BookModel;
import hu.piller.enykp.gui.model.DataFieldModel;
import hu.piller.enykp.gui.model.FormModel;
import hu.piller.enykp.gui.model.PageModel;
import hu.piller.enykp.gui.model.SimpleViewModel;
import hu.piller.enykp.gui.viewer.FormPrinter;
import hu.piller.enykp.interfaces.IDataStore;
import hu.piller.enykp.util.base.PropertyList;
import hu.piller.enykp.util.base.Result;
import hu.piller.enykp.util.base.Tools;
import me.necrocore.abevjava.NecroFile;

import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.PrinterException;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.JEditorPane;
import javax.swing.table.DefaultTableModel;

public class PdfCreator {
   private Lap[] lapok;
   private static int cSeq = 0;
   private static int afCSeq = 0;
   private static int jovCSeq = 0;
   private static PageFormat defaultPageFormat4htmlPrint;
   private static String fontName = "Arial";
   private Book book;
   private static String sablonVerzio;
   private static String formId = null;
   private static BookModel bookModel;
   private FormModel formModel;
   private boolean kivonatolt = false;
   private static final String ATTR_KV_PRINT_FONT_SIZE = "kv_fontsize";
   private static final String VALUE_KV_PRINT_FONT_SIZE = "10";
   private static final String ATTR_KV_PRINT_CELLPADDING = "kv_padding";
   private static final String VALUE_KV_PRINT_CELLPADDING = "0";
   public static final String GEN_PARAM_KESZULT_DATUM = "Készítés dátuma";
   private static final String GEN_PARAM_ADOEV = "Adómegállapítási időszak";
   private static final String GEN_PARAM_BARKOD = "Adómegállapítás bárkódja";
   private static final String GEN_PARAM_FELDOLGOZAS_DATUM = "Feldolgozás dátuma";
   private static final String GEN_PARAM_NEV_1 = "Név titulus";
   private static final String GEN_PARAM_NEV_2 = "Vezetékneve";
   private static final String GEN_PARAM_NEV_3 = "Keresztneve";
   private static final String GEN_PARAM_ADOAZONOSITO = "Adózó adóazonosító jele";
   private static final String GEN_PARAM_ADOSZAM = "Adózó adószáma";
   private static String kivPrTdFontSize = "10";
   private static String kivPrTdCellPadding = "0";
   private static String kivPrCenterTdFontSize = "8";
   private Document document;
   private PdfWriter writer;
   private long time;
   private HashSet an = new HashSet(Arrays.asList("0", "2", "3", "6", "8"));
   private TempObject4SuperPages object4UniquePrint = new TempObject4SuperPages();
   private String backgroundImage = "bg.gif";
   private String thBgColor = "#cccccc";
   private String one_cell_width = "200";

   public PdfCreator(BookModel var1, boolean var2) {
      this.kivonatolt = var2;
      bookModel = var1;
      this.formModel = var1.get(((Elem)var1.cc.getActiveObject()).getType());
   }

   public static String getPdfFontName() {
      return fontName;
   }

   public static BookModel getBookModel() {
      return bookModel;
   }

   public static int getCSeq() {
      return cSeq;
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
            throw new PdfCreator.NoSelectedPageException();
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
      } catch (PdfCreator.PrintCancelledException var23) {
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

   private void deInit() {
      bookModel = null;
      this.formModel = null;

      try {
         for(int var1 = 0; var1 < this.lapok.length; ++var1) {
            this.lapok[var1].setPrintable((FormPrinter)null);
            this.lapok[var1].setLma((LapMetaAdat)null);
            this.lapok[var1] = null;
         }
      } catch (Exception var2) {
         Tools.eLog(var2, 0);
      }

      this.lapok = null;
      defaultPageFormat4htmlPrint = null;
   }

   private void kulonNyomtatandoLapok(boolean var1) throws Exception {
      this.object4UniquePrint.setUniqueDataPages(new Vector());
      int var2 = 0;

      for(int var3 = 0; var3 < this.lapok.length; ++var3) {
         if (this.lapok[var3].getLma().lapSzam <= 0) {
            try {
               PageModel var4 = this.formModel.get(var2++);
               this.lapMetaAdatokPontositasa(this.lapok[var3], var4.xmlht, var1);
            } catch (Exception var5) {
               Tools.eLog(var5, 0);
            }
         }
      }

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
            throw new PdfCreator.NoSelectedPageException();
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

   private void hiba(String var1, Exception var2, int var3) {
      Tools.log(var1);
   }

   private boolean vankijeloltLap() {
      int var1 = 0;

      boolean var2;
      for(var2 = false; var1 < this.lapok.length && !var2; ++var1) {
         if (this.lapok[var1].getLma().isNyomtatando()) {
            var2 = true;
         }
      }

      return var2;
   }

   private void lapMetaAdatokPontositasa(Lap var1, Hashtable var2, boolean var3) {
      if (var2.containsKey("printable") && var1.getLma().isNyomtatando() && "nem".equalsIgnoreCase((String)var2.get("printable"))) {
         var1.getLma().setNyomtatando(false);
      }

      if (var2.containsKey("kpage_other")) {
         if (var1.getLma().isNyomtatando()) {
            var1.getLma().uniquePrintable = true;
            this.object4UniquePrint.getUniqueDataPages().add(var1);
         }

         if (var3) {
            var1.getLma().setNyomtatando(false);
         }

         this.object4UniquePrint.setNormalPrintAfterKPrint(true);
      }

   }

   private void setKVPrintParams() {
      if (this.formModel.attribs.containsKey("kv_fontsize")) {
         kivPrTdFontSize = (String)this.formModel.attribs.get("kv_fontsize");
      } else {
         kivPrTdFontSize = "10";
      }

      if (this.formModel.attribs.containsKey("kv_padding")) {
         kivPrTdCellPadding = (String)this.formModel.attribs.get("kv_padding");
      } else {
         kivPrTdCellPadding = "0";
      }

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

   private boolean checkIfOnlyUniquePrintablePages() {
      for(int var1 = 0; var1 < this.lapok.length; ++var1) {
         if (this.lapok[var1].getLma().isNyomtatando() && !this.lapok[var1].getLma().disabledByRole && !this.lapok[var1].getLma().uniquePrintable) {
            return false;
         }
      }

      return true;
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
         } catch (Exception var13) {
            Tools.eLog(var13, 0);
         }

         try {
            var4 = var4.replaceAll("#13", "<br>");
         } catch (Exception var12) {
            Tools.eLog(var12, 0);
         }

         try {
            var5 = var5.replaceAll("#13", "<br>");
         } catch (Exception var11) {
            Tools.eLog(var11, 0);
         }

         this.time = System.currentTimeMillis();

         try {
            Jep2Pdf4Calculation var6 = new Jep2Pdf4Calculation(var3);
            var6.setFooterVector(((Vector[])((Vector[])var2[2]))[2]);
            var6.setHeaderVector2(((Vector[])((Vector[])var2[2]))[1]);
            var6.setHeaderVector(((Vector[])((Vector[])var2[2]))[0]);
            ByteArrayOutputStream var7 = new ByteArrayOutputStream();
            this.document = new Document();
            this.writer = PdfWriter.getInstance(this.document, var7);
            this.document.open();
            var6.printJep2Pdf(this.document, this.writer, false);
            cSeq = afCSeq;
            if (!"".equals(var4)) {
               var6.reInitAFPane(var4);
               var6.printLandscapeJep2Pdf(this.document, this.writer, false);
            }

            cSeq = jovCSeq;
            if (!"".equals(var5)) {
               var6.reInitJOVPane(var5);
               Vector var8 = ((Vector[])((Vector[])var2[2]))[1];
               String var9 = (String)var8.remove(0);
               var9 = var9.substring("Adóbevallási tervezet - ".length());
               var8.insertElementAt(var9, 0);
               var6.setHeaderVector2(var8);
               var6.printJep2Pdf(this.document, this.writer, true);
            }

            this.document.close();
            var1.errorList.add(var7.toByteArray());
            this.document = null;
            var6 = null;
         } catch (Exception var10) {
            Tools.exception2SOut(var10);
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
         String var4 = this.tableModels2DummyHtmlTable(var3);
         JEditorPane var5 = new JEditorPane("text/html", var4);
         double var6 = this.getCorrection(var5.getMinimumSize().getWidth());
         var5 = null;
         cSeq = 0;
         afCSeq = 0;
         jovCSeq = 0;
         return this.tableModels2HtmlTable(var3, var6);
      }
   }

   private Hashtable getNyomtatandoLapnevek() {
      Hashtable var1 = new Hashtable();

      for(int var2 = 0; var2 < this.lapok.length; ++var2) {
         if (this.lapok[var2].getLma().isNyomtatando() && !this.lapok[var2].getLma().disabledByRole && !this.object4UniquePrint.getUniqueDataPages().contains(this.lapok[var2])) {
            var1.put(this.lapok[var2].getLma().lapNev, Boolean.TRUE);
         }
      }

      return var1;
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
         if (var1.get(var12) instanceof String) {
            String var17 = (String)var1.get(var12);
            if (!var17.startsWith("AF") && !var17.startsWith("17SZJA-AF")) {
               if (var10) {
                  var10 = false;
               }
            } else {
               var10 = true;
               var6 = this.getAFPageData(var1.get(var12), var5, var8, var9, var6, true);
            }

            if (!var17.startsWith("JOV") && !var17.startsWith("17SZJA-JOV")) {
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
         this.backgroundImage = (new NecroFile(var9.getPath())).getParent() + "/abevjava.jar!/resources/print/bg.gif";
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
         if (var1.get(var25) instanceof String) {
            String var34 = (String)var1.get(var25);
            if (!var34.startsWith("AF") && !var34.startsWith("17SZJA-AF")) {
               if (var23) {
                  var23 = false;
               }
            } else {
               var23 = true;
               var21 = this.getAFPageData(var34, var20, var31, var10, var21, false);
            }

            if (!var34.startsWith("JOV") && !var34.startsWith("17SZJA-JOV")) {
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
                  if (var26.getValueAt(var29, 0) != null && var26.getValueAt(var29, 0).toString().startsWith("***")) {
                     if (var26.getValueAt(var29, 0) != null && var26.getValueAt(var29, 0).toString().startsWith("***#")) {
                        for(var30 = 0; var30 < var27; ++var30) {
                           var20 = "";
                           var17.append("<td class=\"").append(cSeq++).append("\" valign=\"top\"").append(var20).append(">");
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

                           var17.append(var28.toString().replaceAll("@#@#", "&nbsp;")).append("</td>").append(var30 == var27 - 1 ? var31 : var10);
                        }
                     } else {
                        var20 = "";

                        for(var30 = 0; var30 < var27; ++var30) {
                           var17.append("<td class=\"").append(cSeq++).append("\" valign=\"top\"").append(var20).append(">");
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

                           var17.append(var28.toString().replaceAll("@#@#", "&nbsp;")).append("</td>").append(var30 == var27 - 1 ? var31 : var10);
                        }
                     }
                  } else if (var29 == 0) {
                     for(var30 = 0; var30 < var27; ++var30) {
                        var17.append("<td class=\"").append(cSeq++).append("\" valign=\"top\"").append(var20).append(">");
                        var28 = var26.getValueAt(var29, var30);
                        if (var28 == null) {
                           var28 = "&nbsp;";
                        } else if (var28.toString().startsWith(" id=\"ntd")) {
                           var17.deleteCharAt(var17.length() - 1);
                        }

                        var17.append(var28.toString().replaceAll("@#@#", "&nbsp;")).append("</td>").append(var31);
                     }
                  } else {
                     for(var30 = 0; var30 < var27; ++var30) {
                        var20 = "";
                        var17.append("<td class=\"").append(cSeq++).append("\" valign=\"top\"").append(var20).append(">");
                        var28 = var26.getValueAt(var29, var30);
                        if (var28 == null) {
                           var28 = "&nbsp;";
                        } else if (var28.toString().startsWith(" id=\"ntd")) {
                           var17.deleteCharAt(var17.length() - 1);
                        }

                        var17.append(var28.toString().replaceAll("@#@#", "&nbsp;")).append("</td>").append(var31);
                     }
                  }

                  var17.append("</tr>");
               }

               var17.append(this.getHBorder(2 * var27 + 1)).append("</table>");
            }
         }
      }

      if (var19) {
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

   private String getHBorder(int var1) {
      return "<tr><td colspan=\"" + var1 + "\" bgcolor=\"" + this.thBgColor + "\" style=\"height:1px; padding:0;\"><img src=\"" + this.backgroundImage + "\" width=\"1\" height=\"1\" border=\"0\"></td></tr>";
   }

   private double getCorrection(double var1) {
      double var3 = 1.0D;
      if (var1 > defaultPageFormat4htmlPrint.getImageableWidth()) {
         var3 = defaultPageFormat4htmlPrint.getImageableWidth() / var1;
      }

      return Math.min(var3, 1.0D);
   }

   private Vector[] parseRows(Vector[] var1) {
      Vector[] var2 = new Vector[var1.length];

      for(int var3 = 0; var3 < var1.length; ++var3) {
         var2[var3] = new Vector();
         String var4 = var3 == 0 ? "" : "_____________________________";
         Elem var5 = (Elem)bookModel.cc.getActiveObject();
         IDataStore var6 = (IDataStore)var5.getRef();

         for(int var7 = 0; var7 < var1[var3].size(); ++var7) {
            Hashtable var8 = (Hashtable)var1[var3].elementAt(var7);
            String var9 = this.parseText((String)var8.get("txt"), var6, bookModel.get(var5.getType()), var4);
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
         return "date".equalsIgnoreCase(((DataFieldModel)var2.fids.get(var5)).features.get("datatype").toString()) ? var6.substring(0, 4) + "." + var6.substring(4, 6) + "." + var6.substring(6) : var6;
      } catch (Exception var8) {
         return var4;
      }
   }

   private void handleSeq(StringBuffer var1, int var2) {
      try {
         String var3 = var1.substring(var2);
         String[] var4 = var3.split("class=");
         cSeq -= var4.length - 1;
      } catch (Exception var5) {
         Tools.eLog(var5, 0);
      }

   }

   public static String getFootText() {
      return "Kitöltő verzió:" + "v.3.44.0".substring(2) + " Nyomtatvány verzió:" + sablonVerzio;
   }

   private Vector[] getGenHeaderAndFooter() {
      Vector[] var1 = new Vector[3];
      Hashtable var2 = MetaInfo.getInstance().getFieldAttributes(bookModel.get_main_formmodel().id, "panids", true);
      Hashtable var3 = new Hashtable();
      var3.put("Adómegállapítás bárkódja", "");
      var3.put("Adómegállapítási időszak", "");
      var3.put("Készítés dátuma", "");
      var3.put("Feldolgozás dátuma", "");
      var3.put("Név titulus", "");
      var3.put("Vezetékneve", "");
      var3.put("Keresztneve", "");
      var3.put("Adózó adóazonosító jele", "");
      var3.put("Adózó adószáma", "");
      this.getKeyByValue(var2, var3);
      this.getDSValue((Elem)bookModel.cc.get(0), var3);
      var1[0] = new Vector();
      var1[0].add("Adóbevallási tervezet " + var3.get("Adómegállapítási időszak") + ". adóév");
      var1[0].add("(Bárkód: " + var3.get("Adómegállapítás bárkódja") + ")");
      var1[1] = new Vector();
      StringBuilder var4 = new StringBuilder();
      var4.append("Adóbevallási tervezet - Név: ");
      if (var3.get("Név titulus").toString().length() > 0) {
         var4.append(var3.get("Név titulus")).append(" ");
      }

      var4.append(var3.get("Vezetékneve")).append(" ").append(var3.get("Keresztneve")).append("  Adóazonosító jel: ").append(var3.get("Adózó adóazonosító jele"));
      if (!"".equals(var3.get("Adózó adószáma"))) {
         var4.append("  Adószám: ").append(var3.get("Adózó adószáma"));
      }

      var1[1].add(var4.toString());
      var1[2] = new Vector();
      String var5 = (String)var3.get("Készítés dátuma");

      try {
         if (var5.length() < 8) {
            var5 = "<Készítés dátuma>";
         } else {
            var5 = var5.substring(0, 4) + "." + var5.substring(4, 6) + "." + var5.substring(6);
         }
      } catch (Exception var9) {
         var5 = "<HIBA>";
      }

      String var6 = (String)var3.get("Feldolgozás dátuma");

      try {
         if (var6.length() < 8) {
            var6 = "<Feldolgozás dátuma>";
         } else {
            var6 = var6.substring(0, 4) + "." + var6.substring(4, 6) + "." + var6.substring(6);
         }
      } catch (Exception var8) {
         var6 = "<HIBA>";
      }

      var4 = new StringBuilder();
      var4.append("  Készült: ").append(var5).append(this.getAnEn(var5)).append(" a ").append(var6).append("-ig beérkezett és feldolgozott adatok alapján");
      var1[2].add(var4.toString());
      return var1;
   }

   private void getKeyByValue(Hashtable var1, Hashtable var2) {
      Enumeration var3 = var1.keys();

      while(var3.hasMoreElements()) {
         Object var5 = var3.nextElement();
         Enumeration var4 = var2.keys();

         while(var4.hasMoreElements()) {
            String var6 = (String)var4.nextElement();
            if (var1.get(var5).equals(var6)) {
               var2.put(var6, var5);
            }
         }
      }

   }

   private void getDSValue(Elem var1, Hashtable var2) {
      String var4;
      String var5;
      for(Enumeration var3 = var2.keys(); var3.hasMoreElements(); var2.put(var4, var5)) {
         var4 = (String)var3.nextElement();
         var5 = ((IDataStore)var1.getRef()).get("0_" + var2.get(var4));
         if (var5 == null) {
            var5 = "";
         }
      }

   }

   private String getAnEn(String var1) {
      if (var1.endsWith(">")) {
         return "";
      } else if (var1.endsWith("01")) {
         return "-jén";
      } else if (var1.endsWith("10")) {
         return "-én";
      } else if (var1.endsWith("12")) {
         return "-én";
      } else {
         return this.an.contains(var1.substring(9)) ? "-án" : "-én";
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
      if (var1 instanceof String) {
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
            if (var8.getValueAt(var11, 0) != null && var8.getValueAt(var11, 0).toString().startsWith("***")) {
               if (var8.getValueAt(var11, 0) != null && var8.getValueAt(var11, 0).toString().startsWith("***#")) {
                  for(var12 = 0; var12 < var9; ++var12) {
                     var2 = "";
                     var5.append("<td class=\"").append(afCSeq++).append("\" valign=\"top\"").append(var2).append(">");
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

                     var5.append(var10.toString().replaceAll("@#@#", "&nbsp;")).append("</td>").append(var12 == var9 - 1 ? var3 : var4);
                  }
               } else {
                  String var14 = this.handle1CellHeader((Vector)var8.getDataVector().get(var11), var2);
                  if (var14 != null) {
                     var5.append(var14).append(var3);
                  } else {
                     var2 = "";

                     for(int var13 = 0; var13 < var9; ++var13) {
                        var5.append("<td class=\"").append(afCSeq++).append("\" valign=\"top\"").append(var2).append(">");
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

                        var5.append(var10.toString().replaceAll("@#@#", "&nbsp;")).append("</td>").append(var13 == var9 - 1 ? var3 : var4);
                     }
                  }
               }
            } else if (var11 == 0) {
               for(var12 = 0; var12 < var9; ++var12) {
                  var5.append("<td class=\"").append(afCSeq++).append("\" valign=\"top\"").append(var2).append(">");
                  var10 = var8.getValueAt(var11, var12);
                  if (var10 == null) {
                     var10 = "&nbsp;";
                  } else if (var10.toString().startsWith(" id=\"ntd")) {
                     var5.deleteCharAt(var5.length() - 1);
                  }

                  var5.append(var10.toString().replaceAll("@#@#", "&nbsp;")).append("</td>").append(var3);
               }
            } else {
               for(var12 = 0; var12 < var9; ++var12) {
                  var2 = "";
                  var5.append("<td class=\"").append(afCSeq++).append("\" valign=\"top\"").append(var2).append(">");
                  var10 = var8.getValueAt(var11, var12);
                  if (var10 == null) {
                     var10 = "&nbsp;";
                  } else if (var10.toString().startsWith(" id=\"ntd")) {
                     var5.deleteCharAt(var5.length() - 1);
                  }

                  var5.append(var10.toString().replaceAll("@#@#", "&nbsp;")).append("</td>").append(var3);
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
      if (var1 instanceof String) {
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
            if (var8.getValueAt(var11, 0) != null && var8.getValueAt(var11, 0).toString().startsWith("***")) {
               if (var8.getValueAt(var11, 0) != null && var8.getValueAt(var11, 0).toString().startsWith("***#")) {
                  for(var12 = 0; var12 < var9; ++var12) {
                     var2 = "";
                     var5.append("<td class=\"").append(jovCSeq++).append("\" valign=\"top\"").append(var2).append(">");
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

                     var5.append(var10.toString().replaceAll("@#@#", "&nbsp;")).append("</td>").append(var12 == var9 - 1 ? var3 : var4);
                  }
               } else {
                  var2 = "";

                  for(var12 = 0; var12 < var9; ++var12) {
                     var5.append("<td class=\"").append(jovCSeq++).append("\" valign=\"top\"").append(var2).append(">");
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

                     var5.append(var10.toString().replaceAll("@#@#", "&nbsp;")).append("</td>").append(var12 == var9 - 1 ? var3 : var4);
                  }
               }
            } else if (var11 == 0) {
               for(var12 = 0; var12 < var9; ++var12) {
                  var5.append("<td class=\"").append(jovCSeq++).append("\" valign=\"top\"").append(var2).append(">");
                  var10 = var8.getValueAt(var11, var12);
                  if (var10 == null) {
                     var10 = "&nbsp;";
                  } else if (var10.toString().startsWith(" id=\"ntd")) {
                     var5.deleteCharAt(var5.length() - 1);
                  }

                  var5.append(var10.toString().replaceAll("@#@#", "&nbsp;")).append("</td>").append(var3);
               }
            } else {
               for(var12 = 0; var12 < var9; ++var12) {
                  var2 = "";
                  var5.append("<td class=\"").append(jovCSeq++).append("\" valign=\"top\"").append(var2).append(">");
                  var10 = var8.getValueAt(var11, var12);
                  if (var10 == null) {
                     var10 = "&nbsp;";
                  } else if (var10.toString().startsWith(" id=\"ntd")) {
                     var5.deleteCharAt(var5.length() - 1);
                  }

                  var5.append(var10.toString().replaceAll("@#@#", "&nbsp;")).append("</td>").append(var3);
               }
            }

            var5.append("</tr>");
         }

         var5.append(this.getHBorder(2 * var9 + 1)).append("</table><br>");
      }

      return var5;
   }

   private boolean isHeaderWidthOk(byte[] var1, String var2) {
      long var3 = System.currentTimeMillis();
      PdfReader var5 = null;

      try {
         var5 = new PdfReader(var1);
      } catch (IOException var12) {
         System.out.println("Nem tudja olvasni a pdf-et az ellenorzes.");
         return false;
      }

      int var7 = 1;
      boolean var8 = false;

      boolean var9;
      for(var9 = true; var7 <= var5.getXrefSize() && !var8; ++var7) {
         PdfObject var6 = var5.getPdfObject(var7);
         if (var6 != null && var6.isStream()) {
            PRStream var10 = (PRStream)var6;
            PdfObject var11 = var10.get(PdfName.SUBTYPE);
            if (var11 != null && var11.toString().equals(PdfName.IMAGE.toString())) {
               var8 = true;
               var9 = var2.equals(var10.get(PdfName.WIDTH).toString());
            }
         }
      }

      System.out.println("Header image check time: " + (System.currentTimeMillis() - var3));
      return var9;
   }

   private String handle1CellHeader(Vector var1, String var2) {
      int var3 = 0;
      String var4 = "";

      for(int var5 = 0; var5 < var1.size(); ++var5) {
         Object var6 = var1.elementAt(var5);
         if (var6 != null) {
            ++var3;
            var4 = (String)var6;
         }
      }

      if (var3 > 1) {
         return null;
      } else {
         if (var4.startsWith("***")) {
            var4 = var4.substring(3);
         }

         StringBuffer var7 = new StringBuffer();
         var7.append("<td class=\"").append(afCSeq++).append("\" valign=\"top\"").append(var2).append(" colspan=\"").append(2 * var1.size() - 1).append("\">").append(var4).append("</td>");
         return var7.toString();
      }
   }

   private class PrintCancelledException extends Exception {
      public PrintCancelledException() {
         super("Mégsem nyomtatunk!");
      }
   }

   private class NoSelectedPageException extends Exception {
      public NoSelectedPageException() {
         super("Nincs nyomtatásra kijelölt oldal!");
      }
   }
}
