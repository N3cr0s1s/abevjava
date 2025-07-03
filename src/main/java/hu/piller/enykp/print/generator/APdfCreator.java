package hu.piller.enykp.print.generator;

import com.lowagie.text.Document;
import com.lowagie.text.pdf.PRStream;
import com.lowagie.text.pdf.PdfName;
import com.lowagie.text.pdf.PdfObject;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;
import hu.piller.enykp.alogic.metainfo.MetaInfo;
import hu.piller.enykp.datastore.Elem;
import hu.piller.enykp.gui.model.BookModel;
import hu.piller.enykp.gui.model.DataFieldModel;
import hu.piller.enykp.gui.model.FormModel;
import hu.piller.enykp.gui.model.PageModel;
import hu.piller.enykp.gui.viewer.FormPrinter;
import hu.piller.enykp.interfaces.IDataStore;
import hu.piller.enykp.print.Lap;
import hu.piller.enykp.print.LapMetaAdat;
import hu.piller.enykp.print.TempObject4SuperPages;
import hu.piller.enykp.util.base.Result;
import hu.piller.enykp.util.base.Tools;
import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.io.IOException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Vector;

public class APdfCreator {
   protected static final String ATTR_KV_PRINT_FONT_SIZE = "kv_fontsize";
   protected static final String VALUE_KV_PRINT_FONT_SIZE = "10";
   protected static final String ATTR_KV_PRINT_CELLPADDING = "kv_padding";
   protected static final String VALUE_KV_PRINT_CELLPADDING = "0";
   public static final String GEN_PARAM_KESZULT_DATUM = "Készítés dátuma";
   protected static final String GEN_PARAM_ADOEV = "Adómegállapítási időszak";
   protected static final String GEN_PARAM_BARKOD = "Adómegállapítás bárkódja";
   protected static final String GEN_PARAM_FELDOLGOZAS_DATUM = "Feldolgozás dátuma";
   protected static final String GEN_PARAM_NEV_1 = "Név titulus";
   protected static final String GEN_PARAM_NEV_2 = "Vezetékneve";
   protected static final String GEN_PARAM_NEV_3 = "Keresztneve";
   protected static final String GEN_PARAM_ADOAZONOSITO = "Adózó adóazonosító jele";
   protected static final String GEN_PARAM_ADOSZAM = "Adózó adószáma";
   protected static String kivPrTdFontSize = "10";
   protected static String kivPrTdCellPadding = "0";
   protected static String kivPrCenterTdFontSize = "8";
   protected int[] seq;
   protected Lap[] lapok;
   protected static PageFormat defaultPageFormat4htmlPrint;
   private static String fontName = "Liberation Serif";
   protected Book book;
   protected static String sablonVerzio;
   protected static String formId = null;
   protected static BookModel bookModel;
   protected FormModel formModel;
   protected boolean kivonatolt = false;
   protected Document document;
   protected PdfWriter writer;
   protected long time;
   protected static final String LANDSCAPE_PAGE_SEPARATOR = "#LANDSCAPE#";
   protected String backgroundImage = "bg.gif";
   protected String thBgColor = "#cccccc";
   protected String one_cell_width = "200";
   protected HashSet an = new HashSet(Arrays.asList("0", "2", "3", "6", "8"));
   protected TempObject4SuperPages object4UniquePrint = new TempObject4SuperPages();

   public static String getPdfFontName() {
      return fontName;
   }

   public static BookModel getBookModel() {
      return bookModel;
   }

   public Result createAndCheck() throws Exception {
      return null;
   }

   protected boolean isHeaderWidthOk(byte[] var1, String var2) {
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

   protected String handle1CellHeader(Vector var1, String var2, int var3) {
      int var4 = 0;
      String var5 = "";

      for(int var6 = 0; var6 < var1.size(); ++var6) {
         Object var7 = var1.elementAt(var6);
         if (var7 != null) {
            ++var4;
            var5 = (String)var7;
         }
      }

      if (var4 > 1) {
         return null;
      } else {
         if (var5.startsWith("***")) {
            var5 = var5.substring(3);
         }

         StringBuffer var8 = new StringBuffer();
         var8.append("<td class=\"").append(this.seq[var3]++).append("\" valign=\"top\"").append(var2).append(" colspan=\"").append(2 * var1.size() - 1).append("\">").append(var5).append("</td>");
         return var8.toString();
      }
   }

   protected void kulonNyomtatandoLapok(boolean var1) throws Exception {
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

   protected void lapMetaAdatokPontositasa(Lap var1, Hashtable var2, boolean var3) {
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

   protected void hiba(String var1, Exception var2, int var3) {
      Tools.log(var1);
   }

   protected boolean vankijeloltLap() {
      int var1 = 0;

      boolean var2;
      for(var2 = false; var1 < this.lapok.length && !var2; ++var1) {
         if (this.lapok[var1].getLma().isNyomtatando()) {
            var2 = true;
         }
      }

      return var2;
   }

   protected boolean checkIfOnlyUniquePrintablePages() {
      for(int var1 = 0; var1 < this.lapok.length; ++var1) {
         if (this.lapok[var1].getLma().isNyomtatando() && !this.lapok[var1].getLma().disabledByRole && !this.lapok[var1].getLma().uniquePrintable) {
            return false;
         }
      }

      return true;
   }

   protected Hashtable getNyomtatandoLapnevek() {
      Hashtable var1 = new Hashtable();

      for(int var2 = 0; var2 < this.lapok.length; ++var2) {
         if (this.lapok[var2].getLma().isNyomtatando() && !this.lapok[var2].getLma().disabledByRole && !this.object4UniquePrint.getUniqueDataPages().contains(this.lapok[var2])) {
            var1.put(this.lapok[var2].getLma().lapNev, Boolean.TRUE);
         }
      }

      return var1;
   }

   protected void setKVPrintParams() {
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

   protected void deInit() {
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

   protected String getHBorder(int var1) {
      return "<tr><td colspan=\"" + var1 + "\" bgcolor=\"" + this.thBgColor + "\" style=\"height:1px; padding:0;\"><img src=\"" + this.backgroundImage + "\" width=\"1\" height=\"1\" border=\"0\"></td></tr>";
   }

   protected double getCorrection(double var1) {
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

   protected String getAnEn(String var1) {
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

   protected Vector[] getGenHeaderAndFooter() {
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

   protected class PrintCancelledException extends Exception {
      public PrintCancelledException() {
         super("Mégsem nyomtatunk!");
      }
   }

   protected class NoSelectedPageException extends Exception {
      public NoSelectedPageException() {
         super("Nincs nyomtatásra kijelölt oldal!");
      }
   }
}
