package hu.piller.enykp.print;

import com.java4less.rbarcode.BarCode2D;
import hu.piller.enykp.gui.model.BookModel;
import hu.piller.enykp.gui.viewer.FormPrinter;
import hu.piller.enykp.interfaces.IPrintSupport;
import hu.piller.enykp.util.base.ErrorList;
import hu.piller.enykp.util.base.Tools;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import javax.swing.SwingUtilities;

public class Lap implements Printable {
   private HashSet vonalkodosNyomtatvanyok = new HashSet(Arrays.asList("ATVUT17", "2517H", "2417H", "2317H", "2217H", "2117", "2117H", "2017", "2017H", "1917", "1917H", "1817", "1817H", "1717", "1717H", "1617", "1617H", "BEVTERVK", "17H"));
   private BufferedImage bImg = null;
   private LapMetaAdat lma = null;
   private double xArany;
   private double yArany;
   IPrintSupport ips;
   FormPrinter printable;
   PageFormat pf;
   public static final int TEXTHEIGHT = 15;
   public static final int TEXTSHIFT = 10;

   public FormPrinter getPrintable() {
      return this.printable;
   }

   public void setPrintable(FormPrinter var1) {
      this.printable = var1;
   }

   public void setPf(PageFormat var1) {
      this.pf = var1;
   }

   public PageFormat getPf() {
      return this.pf;
   }

   public LapMetaAdat getLma() {
      return this.lma;
   }

   public void setLma(LapMetaAdat var1) {
      this.lma = var1;
   }

   public void setxArany(double var1) {
      this.xArany = var1;
   }

   public void setyArany(double var1) {
      this.yArany = var1;
   }

   public int print(Graphics var1, PageFormat var2, int var3) throws PrinterException {
      byte var4 = 20;

      try {
         boolean var5 = false;
         if (MainPrinter.getBookModel().epost != null && MainPrinter.getBookModel().epost.equalsIgnoreCase("onlyinternet")) {
            var5 = true;
         }

         double var6 = (var2.getImageableWidth() - var2.getImageableX()) / (this.getLma().meret.getWidth() + (double)(2 * MainPrinter.nyomtatoMargo) * 2.8D + (double)var4);
         Graphics2D var8 = (Graphics2D)var1;
         var8.translate((double)MainPrinter.nyomtatoMargo * 2.8D, (double)MainPrinter.nyomtatoMargo * 2.8D);
         if (!MainPrinter.emptyPrint) {
            if (!var5) {
               if (210.0D <= var6 * (var2.getImageableHeight() - this.getLma().meret_mod.getHeight() + (double)(2 * MainPrinter.nyomtatoMargo) * 2.8D + (double)var4)) {
                  this.yArany = var6;
               } else {
                  this.yArany = var2.getImageableHeight() / (this.getLma().meret_mod.getHeight() + 165.0D + 15.0D + 30.0D + (double)(2 * MainPrinter.nyomtatoMargo) * 2.8D + (double)var4);
               }

               if (this.yArany > var6) {
                  this.yArany = var6;
               }
            } else {
               if (45.0D <= var6 * (var2.getImageableHeight() - this.getLma().meret_mod.getHeight() + (double)(2 * MainPrinter.nyomtatoMargo) * 2.8D + (double)var4)) {
                  this.yArany = var6;
               } else {
                  this.yArany = var2.getImageableHeight() / (this.getLma().meret_mod.getHeight() + 15.0D + 30.0D + (double)(2 * MainPrinter.nyomtatoMargo) * 2.8D + (double)var4);
               }

               if (this.yArany > var6) {
                  this.yArany = var6;
               }
            }
         } else {
            this.yArany = var2.getImageableHeight() / (this.getLma().meret.getHeight() + (double)(2 * MainPrinter.nyomtatoMargo) * 2.8D + (double)var4);
         }

         var8.scale(var6, this.yArany);
         FormPrinter var9 = this.getPrintable();
         var9.setPageindex(this.getLma().foLapIndex);
         var9.setDynindex(this.getLma().lapSzam);
         if (!this.getLma().alloLap) {
            var8.translate(10, 0);
         }

         var9.print(var8, var2, 0);
         if (!this.getLma().alloLap) {
            var8.translate(-10, 0);
         }

         var8.scale(1.0D / var6, 1.0D / this.yArany);
         var8.scale(Math.min(var6, this.yArany), Math.min(var6, this.yArany));
         if (!MainPrinter.emptyPrint) {
            try {
               this.extraPrint(var1, Math.max((double)MainPrinter.nyomtatoMargo * 2.8D, this.getLma().meret_mod.getWidth() - this.getLma().meret.getWidth()), var2, var5);
            } catch (Exception var11) {
               if (var11.getMessage().startsWith("*HIBA! ")) {
                  MainPrinter.message4TheMasses = var11.getMessage().substring(1);
                  return 0;
               }

               throw new PrinterException("Barkód nyomtatás hiba!");
            }
         }

         return 0;
      } catch (Exception var12) {
         ErrorList.getInstance().store(MainPrinter.PRINT_EXCEPTION_CODE, "Hiba az oldal nyomtatásakor", var12, (Object)null);
         if (var12 instanceof PrinterException) {
            throw (PrinterException)var12;
         } else {
            throw new PrinterException();
         }
      } catch (Error var13) {
         var13.printStackTrace();
         ErrorList.getInstance().store(MainPrinter.PRINT_ERROR_CODE, "Hiba az oldal nyomtatásakor)", new Exception(var13.getMessage()), (Object)null);
         throw new PrinterException();
      }
   }

   public void extraPrint(Graphics var1, double var2, PageFormat var4, boolean var5) throws Exception {
      if (var2 < 25.0D) {
         var2 = 25.0D;
      }

      Font var6 = var1.getFont();
      Color var7 = var1.getColor();
      byte var8 = 13;
      Font var9 = new Font("Arial", 1, var8);
      var1.setColor(Color.BLACK);
      String var10 = MainPrinter.getFootText();
      String var11 = var10 + (MainPrinter.nyomtatvanyHibas && MainPrinter.nyomtatvanyEllenorzott ? "  Hibás" : "");
      String var12 = "Nyomtatva: " + getTimeString();
      if (MainPrinter.isAutofillMode()) {
         var11 = "A nyomtatvány jelen kitöltöttség mellett nem küldhető be!";
         var9 = new Font("Arial", 1, 11);
      }

      if (MainPrinter.nemKuldhetoBeSzoveg) {
         var11 = "Ny.v.:" + MainPrinter.sablonVerzio + " A nyomtatvány papír alapon nem küldhető be!";
         var9 = new Font("Arial", 1, 11);
      }

      if (MainPrinter.papirosBekuldesTiltva) {
         var11 = "Ny.v.:" + MainPrinter.sablonVerzio + " A nyomtatvány jelen kitöltöttség mellett papír alapon nem küldhető be!";
         var9 = new Font("Arial", 1, 11);
      }

      if (MainPrinter.betaVersion) {
         var11 = "Ny.v.:" + MainPrinter.sablonVerzio + " A nyomtatvány ebben a verzióban nem küldhető be!";
         var9 = new Font("Arial", 1, 11);
      }

      if (MainPrinter.check_version > -1) {
         var11 = "Ny.v.:" + MainPrinter.sablonVerzio + " " + BookModel.CHECK_VALID_MESSAGES[MainPrinter.check_version];
         var9 = new Font("Arial", 1, 11);
      }

      var1.setFont(var9);
      int var13 = (int)(this.getLma().meret_mod.getHeight() + 10.0D);
      int var14 = var13 + 10 - (int)(2.8D * (double)MainPrinter.nyomtatoMargo);
      if (!var5 && !MainPrinter.notVKPrint) {
         var14 += 165;
      }

      if ((double)var14 < var2) {
         var14 = (int)var2;
      }

      byte var15 = -35;
      if (!MainPrinter.getBookModel().getOperationMode().equals("0")) {
         var11 = "Bárkód: " + MainPrinter.getBookModel().getBarcode();
         var12 = "";
         if (!var5 && !MainPrinter.notVKPrint) {
            var14 -= 165;
         }
      }

      if (this.vonalkodosNyomtatvanyok.contains(MainPrinter.getBookModel().name) && this.getLma().folapE) {
         var1.drawString(var11, (int)var2 + 60, var14);
      } else {
         var1.drawString(var11, (int)var2 + 20, var14);
      }

      var1.drawString(var12, (int)this.getLma().meret.getWidth() - SwingUtilities.computeStringWidth(var1.getFontMetrics(var9), var12) + var15, var14);
      int var17 = (int)((double)var13 + (this.getLma().alloLap ? 0.8D : 1.0D));
      BufferedImage[] var18;
      if (MainPrinter.getBookModel().getOperationMode().equals("0")) {
         if (!MainPrinter.hasFatalError && !var5 && !MainPrinter.notVKPrint && MainPrinter.voltEEllenorzesNyomtatasElott && !MainPrinter.betaVersion && !MainPrinter.papirosBekuldesTiltva && !MainPrinter.isTemplateDisabled) {
            var18 = this.paintBarkod(var14 - var17 - 15 - 10);
            int var16 = (int)(this.getLma().meret_mod.getWidth() - (double)(var18.length * 300));

            for(int var19 = 0; var19 < var18.length; ++var19) {
               var1.drawImage(var18[var19], var16 + var19 * 300, var17, (ImageObserver)null);
            }
         }
      } else if (this.vonalkodosNyomtatvanyok.contains(MainPrinter.getBookModel().name) && this.getLma().folapE) {
         this.paintBarcode1(var1, (int)var2 + 60, var14 + 20);
      }

      var1.setFont(var6);
      var1.setColor(var7);
      ((Graphics2D)var1).setTransform(new AffineTransform());
      var18 = null;
   }

   public static String getTimeString() {
      SimpleDateFormat var0 = new SimpleDateFormat("yyyy.MM.dd HH.mm.ss");
      return var0.format(Calendar.getInstance().getTime());
   }

   public void printPreview(BufferedImage var1) throws PrinterException {
      Graphics var2 = var1.getGraphics();
      Color var3 = var2.getColor();
      var2.setColor(Color.WHITE);
      var2.fillRect(0, 0, var1.getWidth(), var1.getHeight());
      var2.setColor(var3);
      if (MainPrinter.emptyPrint) {
         PrintPreviewPanel.brPlus = 0;
      } else {
         PrintPreviewPanel.brPlus = 200;
      }

      ((Graphics2D)var2).scale((double)var1.getWidth() / this.getLma().meret.getWidth(), (double)(var1.getHeight() - PrintPreviewPanel.brPlus) / this.getLma().meret.getHeight());
      FormPrinter var4 = this.getPrintable();
      var4.setPageindex(this.getLma().foLapIndex);
      var4.setDynindex(this.getLma().lapSzam);
      var4.print(var2, this.getPf(), 0);
      boolean var5 = false;
      if (MainPrinter.getBookModel().epost != null && MainPrinter.getBookModel().epost.equalsIgnoreCase("onlyinternet")) {
         var5 = true;
      }

      try {
         if (!var5 && !MainPrinter.emptyPrint && !MainPrinter.notVKPrint && !MainPrinter.betaVersion && !MainPrinter.papirosBekuldesTiltva && MainPrinter.getBookModel().getOperationMode().equals("0") && !MainPrinter.isTemplateDisabled && !MainPrinter.hasFatalError) {
            this.paintBarkod(var2, var1.getHeight() - PrintPreviewPanel.brPlus - 50);
         }
      } catch (Exception var7) {
         if (var7 instanceof PrinterException) {
            throw (PrinterException)var7;
         }

         var7.printStackTrace();
      }

   }

   private void paintBarkod(Graphics var1, int var2) throws Exception {
      Barkod var3 = new Barkod();
      boolean var4 = false;
      byte[] var5;
      int var11;
      if (this.getLma().folapE) {
         var11 = (MainPrinter.barkod_NyomtatvanyAdatok + this.getLma().barkodString).length();
         var5 = var3.getBZippedData(MainPrinter.barkod_NyomtatvanyAdatok + this.getLma().barkodString);
      } else if (this.getLma().barkodString.length() == 0) {
         var5 = new byte[0];
         var11 = 0;
      } else {
         var5 = var3.getBZippedData(this.getLma().barkodString);
         var11 = this.getLma().barkodString.length();
      }

      int var6 = var5.length / 500;
      if (var6 * 500 != var5.length) {
         ++var6;
      }

      var6 = Math.max(var6, 1);
      if (this.getLma().alloLap) {
         if (var6 > 2) {
            throw new PrinterException("*HIBA! Egy álló lapra csak 2 bárkód fér ki!");
         }
      } else if (var6 > 3) {
         throw new PrinterException("*HIBA! Egy fekvő lapra csak 3 bárkód fér ki!");
      }

      for(int var8 = 0; var8 < var6; ++var8) {
         int var9;
         if (var5.length - (var8 + 1) * 500 > 0) {
            var9 = 500;
         } else {
            var9 = var5.length - var8 * 500;
         }

         String var10 = var3.barkodValtozoFejlec(this, this.getLma().maxLapszam, var6, var8 + 1, var11, var9);
         BarCode2D var7 = var3.getB2dImg(var5, var8, MainPrinter.barkod_FixFejlecAdatok.getBytes(), var10.getBytes());
         var7.leftMarginCM = (this.getLma().meret_mod.getWidth() - (double)(var6 * 300) + (double)(var8 * 300)) / (double)var7.resolution;
         var7.topMarginCM = (double)(var2 / var7.resolution) + (this.getLma().alloLap ? 0.8D : 1.0D);
         var7.paint(var1);
         var7 = null;
      }

      var3 = null;
      Object var12 = null;
   }

   private BufferedImage[] paintBarkod(int var1) throws Exception {
      Barkod var2 = new Barkod();
      boolean var3 = false;
      byte[] var4;
      int var13;
      if (this.getLma().folapE) {
         var4 = var2.getBZippedData(MainPrinter.barkod_NyomtatvanyAdatok + this.getLma().barkodString);
         var13 = (MainPrinter.barkod_NyomtatvanyAdatok + this.getLma().barkodString).length();
      } else if (this.getLma().barkodString.length() == 0) {
         var4 = new byte[0];
         var13 = 0;
      } else {
         var4 = var2.getBZippedData(this.getLma().barkodString);
         var13 = this.getLma().barkodString.length();
      }

      int var5 = var4.length / 500;
      if (var5 * 500 != var4.length) {
         ++var5;
      }

      var5 = Math.max(var5, 1);
      if (this.getLma().alloLap) {
         if (var5 > 2) {
            throw new PrinterException("*HIBA! Egy álló lapra csak 2 bárkód fér ki!");
         }
      } else if (var5 > 3) {
         throw new PrinterException("*HIBA! Egy fekvő lapra csak 3 bárkód fér ki!");
      }

      BufferedImage[] var6 = new BufferedImage[var5];

      for(int var8 = 0; var8 < var5; ++var8) {
         var6[var8] = new BufferedImage(250, var1, 10);
         int var9;
         if (var4.length - (var8 + 1) * 500 > 0) {
            var9 = 500;
         } else {
            var9 = var4.length - var8 * 500;
         }

         String var10 = var2.barkodValtozoFejlec(this, this.getLma().maxLapszam, var5, var8 + 1, var13, var9);
         BarCode2D var7 = var2.getB2dImg(var4, var8, MainPrinter.barkod_FixFejlecAdatok.getBytes(), var10.getBytes());
         var7.leftMarginCM = 0.0D;
         var7.topMarginCM = 0.0D;
         Graphics var11 = var6[var8].getGraphics();
         var11.setColor(Color.WHITE);
         var11.fillRect(0, 0, 250, var1);

         for(int var12 = 0; var6[var8].getRGB(0, 0) == -1 && var12 < 3; ++var12) {
            var7.paint(var11);
         }

         var7 = null;
      }

      var2 = null;
      Object var14 = null;
      return var6;
   }

   public int print2PDF(Graphics var1, PageFormat var2, int var3) throws PrinterException {
      try {
         boolean var4 = false;
         if (MainPrinter.getBookModel().epost != null && MainPrinter.getBookModel().epost.equalsIgnoreCase("onlyinternet")) {
            var4 = true;
         }

         double var5 = (var2.getImageableWidth() - var2.getImageableX()) / (this.getLma().meret.getWidth() + (double)MainPrinter.nyomtatoMargo * 2.8D);
         Graphics2D var7 = (Graphics2D)var1;
         var7.translate((double)MainPrinter.nyomtatoMargo * 2.8D, (double)MainPrinter.nyomtatoMargo * 2.8D);
         if (!MainPrinter.emptyPrint && !var4) {
            if (210.0D <= var5 * (var2.getImageableHeight() - this.getLma().meret_mod.getHeight() + (double)MainPrinter.nyomtatoMargo * 2.8D)) {
               this.yArany = var5;
            } else {
               this.yArany = var2.getImageableHeight() / (this.getLma().meret_mod.getHeight() + 165.0D + 15.0D + 30.0D + (double)MainPrinter.nyomtatoMargo * 2.8D);
            }

            if (this.yArany > var5) {
               this.yArany = var5;
            }
         } else {
            this.yArany = var2.getImageableHeight() / (this.getLma().meret.getHeight() + (double)MainPrinter.nyomtatoMargo * 2.8D);
         }

         var7.scale(var5, this.yArany);
         FormPrinter var8 = this.getPrintable();
         var8.setPageindex(this.getLma().foLapIndex);
         var8.setDynindex(this.getLma().lapSzam);
         var8.print(var7, var2, 0);
         var7.scale(1.0D / var5, 1.0D / this.yArany);
         var7.scale(Math.min(var5, this.yArany), Math.min(var5, this.yArany));
         if (!MainPrinter.emptyPrint) {
            try {
               this.extraPrint(var1, Math.max((double)MainPrinter.nyomtatoMargo * 2.8D, this.getLma().meret_mod.getWidth() - this.getLma().meret.getWidth()), var2, var4);
            } catch (Exception var12) {
               Exception var9 = var12;

               try {
                  if (var9.getMessage().startsWith("*HIBA! ")) {
                     System.out.println(var9.getMessage());
                     throw new PrinterException(var9.getMessage());
                  }
               } catch (PrinterException var11) {
                  Tools.eLog(var12, 0);
               }

               throw new PrinterException("Barkód nyomtatás hiba!");
            }
         }

         return 0;
      } catch (Exception var13) {
         ErrorList.getInstance().store(MainPrinter.PRINT_EXCEPTION_CODE, "Hiba az oldal nyomtatásakor", var13, (Object)null);
         if (var13 instanceof PrinterException) {
            throw (PrinterException)var13;
         } else {
            throw new PrinterException();
         }
      } catch (Error var14) {
         var14.printStackTrace();
         ErrorList.getInstance().store(MainPrinter.PRINT_ERROR_CODE, "Hiba az oldal nyomtatásakor)", new Exception(var14.getMessage()), (Object)null);
         throw new PrinterException();
      }
   }

   private void paintBarcode1(Graphics var1, int var2, int var3) {
      String var4 = MainPrinter.getBookModel().getBarcode();
      if (var4 != null) {
         short var5 = 200;
         byte var6 = 65;
         BufferedImage var7 = new BufferedImage(var5, var6, 12);
         BarCode2D var8 = new BarCode2D();
         var8.barType = 2;
         var8.barHeightCM = 1.0D;
         var8.X = 0.07D;
         var8.resolution = 40;
         var8.I = 1.0D;
         var8.code = var4;
         var8.leftMarginCM = 0.0D;
         var8.topMarginCM = 0.0D;
         Graphics var9 = var7.getGraphics();
         var9.setColor(Color.WHITE);
         var9.fillRect(0, 0, var5, var6);

         for(int var10 = 0; var7.getRGB(0, 0) == -1 && var10 < 3; ++var10) {
            var8.paint(var9);
         }

         var8 = null;
         var1.drawImage(var7, var2, var3, (ImageObserver)null);
      }
   }

   public String toString() {
      return this.lma == null ? "Nincs beállítva" : this.lma.lapCim + " - Nyomtatandó: " + this.lma.isNyomtatando() + " - dinamikus:" + this.lma.dinamikusE;
   }
}
