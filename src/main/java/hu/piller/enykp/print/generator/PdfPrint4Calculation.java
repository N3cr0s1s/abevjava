package hu.piller.enykp.print.generator;

import com.lowagie.text.Document;
import com.lowagie.text.PageSize;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;
import hu.piller.enykp.gui.viewer.FormPrinter;
import hu.piller.enykp.print.AbevFontMapper;
import hu.piller.enykp.print.Lap;
import hu.piller.enykp.print.LapMetaAdat;
import hu.piller.enykp.util.base.Tools;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.PrinterException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.swing.SwingUtilities;

public class PdfPrint4Calculation {
   private static final int TEXTHEIGHT = 15;
   private static final int TEXTSHIFT = 10;
   private LapMetaAdat lma = null;
   private double yArany;
   private FormPrinter printable;
   private PageFormat pf;
   private Book book;

   public PdfPrint4Calculation(Book var1) {
      this.book = var1;
   }

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

   public void setyArany(double var1) {
      this.yArany = var1;
   }

   public void extraPrint(Graphics var1, double var2, Lap var4) throws Exception {
      if (var2 < 25.0D) {
         var2 = 25.0D;
      }

      Font var5 = var1.getFont();
      Color var6 = var1.getColor();
      byte var7 = 13;
      Font var8 = new Font("Arial", 1, var7);
      var1.setColor(Color.BLACK);
      String var9 = "NAV SZJA ajánlat";
      String var10 = "Nyomtatva: " + getTimeString();
      var1.setFont(var8);
      int var11 = (int)(var4.getLma().meret_mod.getHeight() + 10.0D + 10.0D);
      if ((double)var11 < var2) {
         var11 = (int)var2;
      }

      byte var12 = -35;
      var1.drawString(var9, (int)var2 + 20, var11);
      var1.drawString(var10, (int)var4.getLma().meret.getWidth() - SwingUtilities.computeStringWidth(var1.getFontMetrics(var8), var10) + var12, var11);
      var1.setFont(var5);
      var1.setColor(var6);
      ((Graphics2D)var1).setTransform(new AffineTransform());
   }

   public static String getTimeString() {
      SimpleDateFormat var0 = new SimpleDateFormat("yyyy.MM.dd HH.mm.ss");
      return var0.format(Calendar.getInstance().getTime());
   }

   public int print2PDF(Document var1, PdfWriter var2) throws PrinterException {
      byte var3 = 0;

      try {
         for(int var5 = 0; var5 < this.book.getNumberOfPages(); ++var5) {
            PageFormat var6 = this.book.getPageFormat(var5);
            Lap var7 = (Lap)this.book.getPrintable(var5);
            if (var7.getLma().alloLap) {
               var1.setPageSize(PageSize.A4);
            } else {
               var1.setPageSize(PageSize.A4.rotate());
            }

            var1.newPage();

            try {
               PdfContentByte var8 = var2.getDirectContent();
               PdfTemplate var9 = var8.createTemplate((float)var6.getImageableWidth(), (float)var6.getImageableHeight());
               Graphics2D var4 = var9.createGraphics((float)var6.getImageableWidth(), (float)var6.getImageableHeight(), AbevFontMapper.getInstance());
               double var10 = (var6.getImageableWidth() - var6.getImageableX()) / var7.getLma().meret.getWidth();
               if (45.0D <= var10 * (var6.getImageableHeight() - var7.getLma().meret_mod.getHeight())) {
                  this.yArany = var10;
               } else {
                  this.yArany = var6.getImageableHeight() / (var7.getLma().meret_mod.getHeight() + 15.0D + 30.0D);
               }

               if (this.yArany > var10) {
                  this.yArany = var10;
               }

               var4.scale(var10, this.yArany);
               FormPrinter var12 = var7.getPrintable();
               var12.setPageindex(var7.getLma().foLapIndex);
               var12.setDynindex(var7.getLma().lapSzam);
               var12.print(var4, var6, 0);
               var8.addTemplate(var9, 0.0F, 0.0F);
               var4.scale(1.0D / var10, 1.0D / this.yArany);
               var4.scale(Math.min(var10, this.yArany), Math.min(var10, this.yArany));

               try {
                  this.extraPrint(var4, var7.getLma().meret_mod.getWidth() - var7.getLma().meret.getWidth(), var7);
               } catch (Exception var16) {
                  Exception var13 = var16;

                  try {
                     if (var13.getMessage().startsWith("*HIBA! ")) {
                        throw new PrinterException(var13.getMessage());
                     }
                  } catch (Exception var15) {
                     Tools.eLog(var16, 0);
                  }

                  throw new PrinterException("Barkód nyomtatás hiba!");
               }
            } catch (Exception var17) {
               Tools.eLog(var17, 1);
            }
         }

         return var3;
      } catch (Exception var18) {
         Tools.eLog(var18, 1);
         return -1;
      } catch (Error var19) {
         Tools.eLog(var19, 1);
         return -1;
      }
   }
}
