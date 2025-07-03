package hu.piller.enykp.print;

import com.java4less.rbarcode.BarCode2D;
import com.lowagie.text.Document;
import com.lowagie.text.PageSize;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;
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
import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.PrinterException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.swing.SwingUtilities;

public class PdfPrint {
   private LapMetaAdat lma;
   private double xArany;
   private double yArany;
   IPrintSupport ips;
   FormPrinter printable;
   PageFormat pf;
   Book book;
   String pdfFileName;
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

   public PdfPrint(Book var1, String var2) {
      this.lma = null;
      this.book = var1;
      this.pdfFileName = var2;
   }

   public PdfPrint(BufferedImage var1, LapMetaAdat var2, Book var3) {
      this.lma = var2;
      this.book = var3;
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

   public void extraPrint(Graphics var1, double var2, PageFormat var4, Lap var5, boolean var6) throws Exception {
      if (var2 < 25.0D) {
         var2 = 25.0D;
      }

      Font var7 = var1.getFont();
      Color var8 = var1.getColor();
      byte var9 = 13;
      Font var10 = new Font("Arial", 1, var9);
      var1.setColor(Color.BLACK);
      String var11 = MainPrinter.getFootText();
      String var12 = var11 + (MainPrinter.nyomtatvanyHibas && MainPrinter.nyomtatvanyEllenorzott ? "  Hibás" : "");
      String var13 = "Nyomtatva: " + getTimeString();
      if (MainPrinter.isAutofillMode()) {
         var12 = "A nyomtatvány jelen kitöltöttség mellett nem küldhető be!";
      }

      if (MainPrinter.nemKuldhetoBeSzoveg) {
         var12 = "Ny.v.:" + MainPrinter.sablonVerzio + " A nyomtatvány papír alapon nem küldhető be!";
      }

      if (MainPrinter.papirosBekuldesTiltva) {
         var12 = "Ny.v.:" + MainPrinter.sablonVerzio + " A nyomtatvány jelen kitöltöttség mellett papír alapon nem küldhető be!";
      }

      if (MainPrinter.betaVersion) {
         var12 = "Ny.v.:" + MainPrinter.sablonVerzio + " A nyomtatvány ebben a verzióban nem küldhető be!";
      }

      if (MainPrinter.check_version > -1) {
         var12 = "Ny.v.:" + MainPrinter.sablonVerzio + " " + BookModel.CHECK_VALID_MESSAGES[MainPrinter.check_version];
      }

      if (!MainPrinter.getBookModel().getOperationMode().equals("0")) {
         var12 = "Bárkód: " + MainPrinter.getBookModel().getBarcode();
         var13 = "";
      }

      var1.setFont(var10);
      int var14 = (int)(var5.getLma().meret_mod.getHeight() + 10.0D);
      int var15 = var14 + 10 - (int)(2.8D * (double)MainPrinter.nyomtatoMargo);
      if (!var6 && !MainPrinter.notVKPrint) {
         var15 += 165;
      }

      if ((double)var15 < var2) {
         var15 = (int)var2;
      }

      byte var16 = -35;
      var1.drawString(var12, (int)var2 + 20, var15);
      var1.drawString(var13, (int)var5.getLma().meret.getWidth() - SwingUtilities.computeStringWidth(var1.getFontMetrics(var10), var13) + var16, var15);
      int var18 = (int)((double)var14 + (var5.getLma().alloLap ? 0.8D : 1.0D));
      BufferedImage[] var19;
      if (!MainPrinter.hasFatalError && !var6 && !MainPrinter.notVKPrint && MainPrinter.voltEEllenorzesNyomtatasElott && !MainPrinter.betaVersion && !MainPrinter.papirosBekuldesTiltva && MainPrinter.getBookModel().getOperationMode().equals("0") && !MainPrinter.isTemplateDisabled) {
         var19 = this.paintBarkod(var15 - var18 - 15 - 10, var5);
         int var17 = (int)(var5.getLma().meret_mod.getWidth() - (double)(var19.length * 300));

         for(int var20 = 0; var20 < var19.length; ++var20) {
            var1.drawImage(var19[var20], var17 + var20 * 300, var18, (ImageObserver)null);
         }
      }

      var1.setFont(var7);
      var1.setColor(var8);
      ((Graphics2D)var1).setTransform(new AffineTransform());
      var19 = null;
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
         if (!var5 && !MainPrinter.emptyPrint) {
            this.paintBarkod(var2, var1.getHeight() - PrintPreviewPanel.brPlus - 50, (Lap)null);
         }
      } catch (Exception var7) {
         if (var7 instanceof PrinterException) {
            throw (PrinterException)var7;
         }

         var7.printStackTrace();
      }

   }

   private void paintBarkod(Graphics var1, int var2, Lap var3) throws Exception {
      Barkod var4 = new Barkod();
      boolean var5 = false;
      byte[] var6;
      int var12;
      if (var3.getLma().folapE) {
         var12 = (MainPrinter.barkod_NyomtatvanyAdatok + var3.getLma().barkodString).length();
         var6 = var4.getBZippedData(MainPrinter.barkod_NyomtatvanyAdatok + var3.getLma().barkodString);
      } else if (var3.getLma().barkodString.length() == 0) {
         var12 = 0;
         var6 = new byte[0];
      } else {
         var12 = var3.getLma().barkodString.length();
         var6 = var4.getBZippedData(var3.getLma().barkodString);
      }

      int var7 = var6.length / 500;
      if (var7 * 500 != var6.length) {
         ++var7;
      }

      var7 = Math.max(var7, 1);
      if (var3.getLma().alloLap) {
         if (var7 > 2) {
            throw new PrinterException("*HIBA! Egy álló lapra csak 2 bárkód fér ki!");
         }
      } else if (var7 > 3) {
         throw new PrinterException("*HIBA! Egy fekvő lapra csak 3 bárkód fér ki!");
      }

      for(int var9 = 0; var9 < var7; ++var9) {
         int var10;
         if (var6.length - (var9 + 1) * 500 > 0) {
            var10 = 500;
         } else {
            var10 = var6.length - var9 * 500;
         }

         String var11 = var4.barkodValtozoFejlec(var3, var3.getLma().maxLapszam, var7, var9 + 1, var12, var10);
         BarCode2D var8 = var4.getB2dImg(var6, var9, MainPrinter.barkod_FixFejlecAdatok.getBytes(), var11.getBytes());
         var8.leftMarginCM = (var3.getLma().meret_mod.getWidth() - (double)(var7 * 300) + (double)(var9 * 300)) / (double)var8.resolution;
         var8.topMarginCM = (double)(var2 / var8.resolution) + (var3.getLma().alloLap ? 0.8D : 1.0D);
         var8.paint(var1);
         var8 = null;
      }

      var4 = null;
      Object var13 = null;
   }

   private BufferedImage[] paintBarkod(int var1, Lap var2) throws Exception {
      Barkod var3 = new Barkod();
      boolean var4 = false;
      byte[] var5;
      int var14;
      if (var2.getLma().folapE) {
         var14 = (MainPrinter.barkod_NyomtatvanyAdatok + var2.getLma().barkodString).length();
         var5 = var3.getBZippedData(MainPrinter.barkod_NyomtatvanyAdatok + var2.getLma().barkodString);
      } else if (var2.getLma().barkodString.length() == 0) {
         var14 = 0;
         var5 = new byte[0];
      } else {
         var14 = var2.getLma().barkodString.length();
         var5 = var3.getBZippedData(var2.getLma().barkodString);
      }

      int var6 = var5.length / 500;
      if (var6 * 500 != var5.length) {
         ++var6;
      }

      var6 = Math.max(var6, 1);
      if (var2.getLma().alloLap) {
         if (var6 > 2) {
            throw new PrinterException("*HIBA! Egy álló lapra csak 2 bárkód fér ki!");
         }
      } else if (var6 > 3) {
         throw new PrinterException("*HIBA! Egy fekvő lapra csak 3 bárkód fér ki!");
      }

      BufferedImage[] var7 = new BufferedImage[var6];

      for(int var9 = 0; var9 < var6; ++var9) {
         var7[var9] = new BufferedImage(250, var1, 10);
         int var10;
         if (var5.length - (var9 + 1) * 500 > 0) {
            var10 = 500;
         } else {
            var10 = var5.length - var9 * 500;
         }

         String var11 = var3.barkodValtozoFejlec(var2, var2.getLma().maxLapszam, var6, var9 + 1, var14, var10);
         BarCode2D var8 = var3.getB2dImg(var5, var9, MainPrinter.barkod_FixFejlecAdatok.getBytes(), var11.getBytes());
         var8.leftMarginCM = 0.0D;
         var8.topMarginCM = 0.0D;
         Graphics var12 = var7[var9].getGraphics();
         var12.setColor(Color.WHITE);
         var12.fillRect(0, 0, 250, var1);
         byte var13 = 0;

         while(var7[var9].getRGB(0, 0) == -1 && var13 < 3) {
            var8.paint(var12);
         }

         var8 = null;
      }

      var3 = null;
      Object var15 = null;
      return var7;
   }

   public int print2PDF(Document var1, PdfWriter var2) throws PrinterException {
      byte var3 = 0;

      try {
         boolean var4 = false;
         if (MainPrinter.getBookModel().epost != null && MainPrinter.getBookModel().epost.equalsIgnoreCase("onlyinternet")) {
            var4 = true;
         }

         for(int var6 = 0; var6 < this.book.getNumberOfPages(); ++var6) {
            PageFormat var7 = this.book.getPageFormat(var6);
            Lap var8 = (Lap)this.book.getPrintable(var6);
            if (var8.getLma().alloLap) {
               var1.setPageSize(PageSize.A4);
            } else {
               var1.setPageSize(PageSize.A4.rotate());
            }

            var1.newPage();

            try {
               PdfContentByte var9 = var2.getDirectContent();
               PdfTemplate var10 = var9.createTemplate((float)var7.getImageableWidth(), (float)var7.getImageableHeight());
               boolean var11 = false;
               float var12 = 1.0F;
               Graphics2D var5 = var10.createGraphics((float)var7.getImageableWidth(), (float)var7.getImageableHeight(), AbevFontMapper.getInstance());
               double var13 = (var7.getImageableWidth() - var7.getImageableX()) / (var8.getLma().meret.getWidth() + (double)MainPrinter.nyomtatoMargo * 2.8D);
               var5.translate((double)MainPrinter.nyomtatoMargo * 2.8D, (double)MainPrinter.nyomtatoMargo * 2.8D);
               if (!MainPrinter.emptyPrint) {
                  if (!var4) {
                     if (210.0D <= var13 * (var7.getImageableHeight() - var8.getLma().meret_mod.getHeight() + (double)MainPrinter.nyomtatoMargo * 2.8D)) {
                        this.yArany = var13;
                     } else {
                        this.yArany = var7.getImageableHeight() / (var8.getLma().meret_mod.getHeight() + 165.0D + 15.0D + 30.0D + (double)MainPrinter.nyomtatoMargo * 2.8D);
                     }

                     if (this.yArany > var13) {
                        this.yArany = var13;
                     }
                  } else {
                     if (45.0D <= var13 * (var7.getImageableHeight() - var8.getLma().meret_mod.getHeight() + (double)MainPrinter.nyomtatoMargo * 2.8D)) {
                        this.yArany = var13;
                     } else {
                        this.yArany = var7.getImageableHeight() / (var8.getLma().meret_mod.getHeight() + 15.0D + 30.0D + (double)MainPrinter.nyomtatoMargo * 2.8D);
                     }

                     if (this.yArany > var13) {
                        this.yArany = var13;
                     }
                  }
               } else {
                  this.yArany = var7.getImageableHeight() / (var8.getLma().meret.getHeight() + (double)MainPrinter.nyomtatoMargo * 2.8D);
               }

               var5.scale(var13, this.yArany);
               FormPrinter var15 = var8.getPrintable();
               var15.setPageindex(var8.getLma().foLapIndex);
               var15.setDynindex(var8.getLma().lapSzam);
               var15.print(var5, var7, 0);
               var9.addTemplate(var10, 0.0F, 0.0F);
               var5.scale(1.0D / var13, 1.0D / this.yArany);
               var5.scale(Math.min(var13, this.yArany), Math.min(var13, this.yArany));
               if (!MainPrinter.emptyPrint) {
                  try {
                     this.extraPrint(var5, Math.max((double)MainPrinter.nyomtatoMargo * 2.8D, var8.getLma().meret_mod.getWidth() - var8.getLma().meret.getWidth()), var7, var8, var4);
                  } catch (Exception var19) {
                     Exception var16 = var19;

                     try {
                        if (var16.getMessage().startsWith("*HIBA! ")) {
                           MainPrinter.message4TheMasses = var16.getMessage().substring(1);
                           boolean var23 = true;
                           ErrorList.getInstance().writeError(new Integer(2000), var16.getMessage().substring(1), var16, (Object)null);
                           throw new PrinterException(var16.getMessage());
                        }
                     } catch (Exception var18) {
                        Tools.eLog(var19, 0);
                     }

                     throw new PrinterException("Barkód nyomtatás hiba!");
                  }
               }
            } catch (Exception var20) {
               Tools.eLog(var20, 0);
            }
         }

         return var3;
      } catch (Exception var21) {
         ErrorList.getInstance().store(MainPrinter.PRINT_EXCEPTION_CODE, "Hiba az oldal nyomtatásakor", var21, (Object)null);
         return -1;
      } catch (Error var22) {
         var22.printStackTrace();
         ErrorList.getInstance().store(MainPrinter.PRINT_ERROR_CODE, "Hiba az oldal nyomtatásakor)", new Exception(var22.getMessage()), (Object)null);
         return -1;
      }
   }
}
