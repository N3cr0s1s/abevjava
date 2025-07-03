package hu.piller.enykp.print;

import com.java4less.rbarcode.BarCode2D;
import hu.piller.enykp.gui.model.BookModel;
import hu.piller.enykp.util.base.PropertyList;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.print.PrinterException;
import java.util.Vector;
import javax.swing.SwingUtilities;

public class BrPrinter {
   public static final double BR_CORRECTION = 0.7D;
   private Vector footerVector;
   private Vector headerVector;
   private String barkodString;
   protected String timeString = "";
   public static int simpleMargin = 10;
   private byte[] zippedData;
   private BufferedImage[] bis;
   private int barcodeNeed;
   private int barkodLength = 0;
   public static String fontName = "Arial";
   private int currentPage;
   private static final int H_SPACE = 0;
   private static final int V_SPACE = 20;
   private static int laptukorKorrekcio;
   private static int fejlecKorrekcio;

   public BrPrinter(String var1) throws Exception {
      this.barkodString = var1;
      if (MainPrinter.kivonatoltBarkoddal && (MainPrinter.autoFillPdfPrevFileName == null || !MainPrinter.bookModel.autofill)) {
         this.setBrCount();
      }

      if (this.barcodeNeed > 10) {
         PropertyList.getInstance().set("brCountError", "A lapra maximum 10 pontkódot tudunk nyomtatni.\nA nyomtatványban szereplő adatmennyiség nem fér el ennyi pontkódban. Kérjük nyomtassa ki a hagyományos módon!");
         throw new Exception("A lapra maximum 10 pontkódot tudunk nyomtatni.\nA nyomtatványban szereplő adatmennyiség nem fér el ennyi pontkódban. Kérjük nyomtassa ki a hagyományos módon!");
      }
   }

   public void setParameters(Vector var1, Vector var2, String var3, int var4) {
      this.footerVector = var1;
      this.headerVector = var2;
      this.timeString = var3;
      this.currentPage = var4;
   }

   public void print(Graphics var1, String var2) throws Exception {
      double var3 = 0.7D;
      byte var5 = 5;
      Font var6 = var1.getFont();
      Color var7 = var1.getColor();
      byte var8 = 8;
      byte var9 = 11;
      int var10 = var9;
      Font var11 = new Font(MainPrinter.fontName, 0, var8);
      Font var12 = new Font(MainPrinter.fontName, 1, var9);
      Font var13 = new Font(MainPrinter.fontName, 1, var9);
      var1.setColor(Color.BLACK);
      String var16 = MainPrinter.mainLabel;
      String var17 = "Figyelem! Ha papíron adja be a nyomtatványát, akkor ezt a lapot mindenképpen küldje be vele együtt!";
      String var18 = MainPrinter.getFootText();
      String var19 = var18 + (MainPrinter.nyomtatvanyHibas && MainPrinter.nyomtatvanyEllenorzott ? " Hibás" : "") + "    Ellenőrző kód:" + var2;
      String var20 = "   " + (this.currentPage + 1) + ".lap - Nyomtatva: " + this.timeString;
      int var21 = SwingUtilities.computeStringWidth(var1.getFontMetrics(var12), var16) / 2;
      int var22 = SwingUtilities.computeStringWidth(var1.getFontMetrics(var12), var17) / 2;

      Font var14;
      for(var14 = new Font(MainPrinter.fontName, 1, var9); (double)var21 > (var1.getClip().getBounds().getWidth() - (double)(2 * simpleMargin) * 2.8D) / 2.0D && var10 > var5; var21 = SwingUtilities.computeStringWidth(var1.getFontMetrics(var14), var16) / 2) {
         --var10;
         var14 = new Font(MainPrinter.fontName, 1, var10);
      }

      var10 = var9;

      Font var15;
      for(var15 = new Font(MainPrinter.fontName, 1, var9); (double)var22 > (var1.getClip().getBounds().getWidth() - (double)(2 * simpleMargin) * 2.8D) / 2.0D && var10 > var5; var22 = SwingUtilities.computeStringWidth(var1.getFontMetrics(var15), var17) / 2) {
         --var10;
         var15 = new Font(MainPrinter.fontName, 1, var10);
      }

      Shape var23 = var1.getClip();
      var1.setClip((int)var23.getBounds().getX(), (int)(var23.getBounds().getY() - (double)laptukorKorrekcio), (int)var23.getBounds().getWidth(), (int)var23.getBounds().getHeight());
      if (this.headerVector.size() > 0) {
         var13 = this.getFont4LastRow(var1, var13, (String)this.headerVector.elementAt(0));
         var1.setFont(var13);
         int var24 = SwingUtilities.computeStringWidth(var1.getFontMetrics(var13), (String)this.headerVector.elementAt(0)) / 2;
         var1.drawString((String)this.headerVector.elementAt(0), (int)((var1.getClip().getBounds().getWidth() - (double)(2 * simpleMargin) * 2.8D) / 2.0D) - var24, fejlecKorrekcio - (var13.getSize() > 9 ? 0 : 7));
      }

      var1.setFont(var14);
      var1.setClip(var23);
      var21 = SwingUtilities.computeStringWidth(var1.getFontMetrics(var14), var16) / 2;
      var1.drawString(var16, Math.max(0, (int)((var1.getClip().getBounds().getWidth() - (double)(2 * simpleMargin) * 2.8D) / 2.0D) - var21), 10);
      var1.setFont(var15);
      if (!MainPrinter.papirosBekuldesTiltva) {
         var1.drawString(var17, (int)((var1.getClip().getBounds().getWidth() - (double)(2 * simpleMargin) * 2.8D) / 2.0D) - var22, 25);
      }

      byte var30 = 50;
      int var26;
      if (MainPrinter.getBookModel().getOperationMode().equals("0") && !MainPrinter.betaVersion && !MainPrinter.papirosBekuldesTiltva && !MainPrinter.isTemplateDisabled) {
         ((Graphics2D)var1).scale(var3, var3);

         try {
            byte var25 = 40;

            for(var26 = 0; var26 < this.bis.length; ++var26) {
               var1.drawImage(this.bis[var26], (int)((double)var25 + (double)(var26 / 5 * 300) * 1.4285714285714286D), var26 % 5 * 165 + var30, (ImageObserver)null);
            }
         } catch (Exception var29) {
            if (var29 instanceof PrinterException) {
               var1.drawString(var29.getMessage(), 0, var30 + 100);
            } else {
               var29.printStackTrace();
            }

            var1.setFont(var6);
            var1.setColor(var7);
            ((Graphics2D)var1).setTransform(new AffineTransform());
            this.bis = null;
            return;
         }

         ((Graphics2D)var1).scale(1.0D / var3, 1.0D / var3);
      }

      var26 = var30 + 550;
      String var27 = Jep2Pdf.getMaxLength(this.footerVector, new Vector());
      var11 = this.getFont4LastRow(var1, var11, var27);
      var1.setFont(var11);
      if (this.footerVector.size() > 0) {
         for(int var28 = 0; var28 < this.footerVector.size(); ++var28) {
            var1.drawString((String)this.footerVector.elementAt(var28), 0, var26 + (var28 + 1) * (20 - (var28 == this.footerVector.size() - 1 ? 0 : 5)));
         }
      }

      if (MainPrinter.isAutofillMode()) {
         var19 = "A nyomtatvány jelen kitöltöttség mellett nem küldhető be!";
      }

      if (MainPrinter.nemKuldhetoBeSzoveg) {
         var19 = "Ny.v.:" + MainPrinter.sablonVerzio + " A nyomtatvány papír alapon nem küldhető be!";
      }

      if (!MainPrinter.kivonatoltanBekuldheto) {
         var19 = "Ny.v.:" + MainPrinter.sablonVerzio + " A nyomtatvány kivonatolt nyomtatási formában nem küldhető be!";
      }

      if (MainPrinter.papirosBekuldesTiltva) {
         var19 = "Ny.v.:" + MainPrinter.sablonVerzio + " A nyomtatvány jelen kitöltöttség mellett papír alapon nem küldhető be!";
      }

      if (MainPrinter.betaVersion) {
         var19 = "Ny.v.:" + MainPrinter.sablonVerzio + " A nyomtatvány ebben a verzióban nem küldhető be!";
      }

      if (MainPrinter.check_version > -1) {
         var19 = "Ny.v.:" + MainPrinter.sablonVerzio + " " + BookModel.CHECK_VALID_MESSAGES[MainPrinter.check_version];
      }

      if (!MainPrinter.getBookModel().getOperationMode().equals("0")) {
         var19 = "Bárkód: " + MainPrinter.getBookModel().getBarcode();
         var20 = "";
      }

      var11 = this.getFont4LastRow(var1, var11, var19 + var20);
      var1.setFont(var11);
      var1.drawString(var19 + var20, 0, var26 + (this.footerVector.size() + 1) * 20);
      var1.setFont(var6);
      var1.setColor(var7);
   }

   private BufferedImage[] paintBarkod(int var1) throws Exception {
      Barkod var2 = new Barkod();
      BufferedImage[] var3 = new BufferedImage[this.barcodeNeed];

      for(int var5 = 0; var5 < this.barcodeNeed; ++var5) {
         var3[var5] = new BufferedImage(250, var1, 10);
         int var6;
         if (this.zippedData.length - (var5 + 1) * 500 > 0) {
            var6 = 500;
         } else {
            var6 = this.zippedData.length - var5 * 500;
         }

         Lap var7 = new Lap();
         LapMetaAdat var8 = new LapMetaAdat();
         var8.foLapIndex = 0;
         var8.lapSzam = 0;
         var7.setLma(var8);
         String var9 = var2.barkodValtozoFejlec(var7, 1, this.barcodeNeed, var5 + 1, this.barkodLength, var6);
         BarCode2D var4 = var2.getB2dImg(this.zippedData, var5, MainPrinter.barkod_FixFejlecAdatok.getBytes(), var9.getBytes());
         var4.leftMarginCM = 0.0D;
         var4.topMarginCM = 0.0D;
         Graphics var10 = var3[var5].getGraphics();
         var10.setColor(Color.WHITE);
         var10.fillRect(0, 0, 250, var1);

         for(int var11 = 0; var3[var5].getRGB(0, 0) == -1 && var11 < 3; ++var11) {
            var4.paint(var10);
         }

         var4 = null;
      }

      var2 = null;
      return var3;
   }

   private void setBrCount() throws Exception {
      try {
         Barkod var1 = new Barkod();
         this.zippedData = var1.getBZippedData(MainPrinter.barkod_NyomtatvanyAdatok + this.barkodString);
         if (this.zippedData == null) {
            throw new Exception();
         } else {
            this.barkodLength = (MainPrinter.barkod_NyomtatvanyAdatok + this.barkodString).length();
            int var2 = this.zippedData.length / 500;
            if (var2 * 500 != this.zippedData.length) {
               ++var2;
            }

            this.barcodeNeed = Math.max(var2, 1);
            this.bis = this.paintBarkod(150);
         }
      } catch (Exception var3) {
         var3.printStackTrace();
         throw new Exception("Nem sikerült megállapítani a szükséges bárkódok számát");
      }
   }

   private Font getFont4LastRow(Graphics var1, Font var2, String var3) {
      int var4 = SwingUtilities.computeStringWidth(var1.getFontMetrics(var2), var3);

      for(int var5 = var2.getSize(); (double)var4 > var1.getClip().getBounds().getWidth() - (double)(2 * simpleMargin) * 2.8D && var5 > 5; var4 = SwingUtilities.computeStringWidth(var1.getFontMetrics(var2), var3)) {
         --var5;
         var2 = new Font(fontName, 1, var5);
      }

      return var2;
   }

   public String getBarkodString() {
      return this.barkodString;
   }

   static {
      laptukorKorrekcio = 10 + (int)((double)MainPrinter.nyomtatoMargo * 2.8D);
      fejlecKorrekcio = 0;
   }
}
