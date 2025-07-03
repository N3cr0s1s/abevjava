package hu.piller.enykp.print;

import hu.piller.enykp.gui.model.BookModel;
import hu.piller.enykp.util.base.Sha1Hash;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.Vector;
import javax.swing.JEditorPane;
import javax.swing.SwingUtilities;
import javax.swing.text.View;

public class HtmlPrinter implements Printable {
   protected int currentPage = -1;
   protected JEditorPane jeditorPane;
   protected double pageEndY = 0.0D;
   protected double pageStartY = 0.0D;
   protected double scale = 1.0D;
   protected int bottom;
   protected int bottomCorrection = 0;
   protected static final int ROW_HEIGHT = 20;
   protected PageFormat pFormat;
   protected PrinterJob pJob;
   private Vector footerVector;
   private Vector headerVector;
   protected String timeString = "";
   private static boolean printHasEnded = false;
   private static int allHtmlDataPrinted = 0;
   private static int lastHtmlPageIndex = -1;
   private static int paintedObjectCount = 0;
   protected BrPrinter bp;
   public static String barkodStringHash;

   public HtmlPrinter(JEditorPane var1, PrinterJob var2, PageFormat var3) {
      this.pJob = var2;
      this.pFormat = var3;
      this.setDocument(var1);
      this.timeString = Lap.getTimeString();
      printHasEnded = false;
      allHtmlDataPrinted = 0;
      lastHtmlPageIndex = -1;
      paintedObjectCount = 0;
   }

   public int print(Graphics var1, PageFormat var2, int var3) {
      var2 = this.pFormat;
      Graphics2D var4 = (Graphics2D)var1;
      this.jeditorPane.setSize((int)var2.getImageableWidth(), Integer.MAX_VALUE);
      this.jeditorPane.validate();
      View var5 = this.jeditorPane.getUI().getRootView(this.jeditorPane);
      if (this.jeditorPane.getMinimumSize().getWidth() > var2.getImageableWidth()) {
         this.scale = var2.getImageableWidth() / this.jeditorPane.getMinimumSize().getWidth();
         var4.scale(this.scale, this.scale);
      }

      if (var3 == 0) {
         var4.setClip((int)(var2.getImageableX() / this.scale), (int)(var2.getImageableY() / this.scale), (int)(var2.getImageableWidth() / this.scale), (int)(var2.getImageableHeight() / this.scale));
      } else {
         var4.setClip((int)(var2.getImageableX() / this.scale), (int)((var2.getImageableY() + (double)((this.headerVector.size() + 1) * 20)) / this.scale), (int)(var2.getImageableWidth() / this.scale), (int)(var2.getImageableHeight() / this.scale));
      }

      if (var3 > this.currentPage) {
         this.currentPage = var3;
         this.pageStartY += this.pageEndY;
         this.pageEndY = var4.getClipBounds().getHeight();
      }

      var4.translate(var4.getClipBounds().getX(), var4.getClipBounds().getY());
      Rectangle var6 = new Rectangle(0, (int)(-this.pageStartY), (int)this.jeditorPane.getMinimumSize().getWidth(), (int)this.jeditorPane.getPreferredSize().getHeight());
      if (var3 == 0) {
         this.bottom = (int)((var2.getImageableHeight() - (double)this.bottomCorrection) / this.scale);
      } else {
         this.bottom = (int)((var2.getImageableHeight() - (double)this.bottomCorrection - (double)((this.headerVector.size() + 1) * 20)) / this.scale);
      }

      if (!printHasEnded && this.printView(var4, var6, var5)) {
         this.extraPrint(var4, this.currentPage, this.timeString, var3);
         if (MainPrinter.getSeq() - 1 == paintedObjectCount) {
            ++allHtmlDataPrinted;
         }

         if (allHtmlDataPrinted > 1) {
            printHasEnded = true;
         }

         lastHtmlPageIndex = var3 + 1;
         return 0;
      } else {
         this.pageStartY = 0.0D;
         this.pageEndY = 0.0D;
         if (!printHasEnded) {
            lastHtmlPageIndex = var3;
         }

         printHasEnded = true;
         if (MainPrinter.kivonatoltBarkoddal && MainPrinter.voltEEllenorzesNyomtatasElott) {
            try {
               if (this.jeditorPane.getMinimumSize().getWidth() > var2.getImageableWidth() && this.scale < 1.0D) {
                  var4.scale(1.0D / this.scale, 1.0D / this.scale);
               }

               this.barkodPrint(var1, this.bp, this.timeString, this.currentPage);
            } catch (Exception var8) {
               var8.printStackTrace();
               return 1;
            }

            printHasEnded = true;
            if (var3 == lastHtmlPageIndex) {
               return 0;
            }
         }

         printHasEnded = true;
         return 1;
      }
   }

   public void print() {
      this.pJob.setPrintable(this, this.pFormat);

      try {
         this.pJob.print();
      } catch (PrinterException var2) {
         this.pageStartY = 0.0D;
         this.pageEndY = 0.0D;
         this.currentPage = -1;
         System.out.println("Hiba a html dokumentum nyomtatásakor");
      }

   }

   protected void printDialog() {
   }

   protected boolean printView(Graphics2D var1, Shape var2, View var3) {
      boolean var4 = false;
      Rectangle var5 = var1.getClipBounds();
      if (var3.getViewCount() > 0 && !var3.getElement().getName().equalsIgnoreCase("td")) {
         for(int var8 = 0; var8 < var3.getViewCount(); ++var8) {
            Shape var6 = var3.getChildAllocation(var8, var2);
            if (var6 != null) {
               View var7 = var3.getView(var8);
               if (this.printView(var1, var6, var7)) {
                  var4 = true;
               }
            }
         }
      } else if (var2.getBounds().getMaxY() >= var5.getY()) {
         var4 = true;
         if (var2.getBounds().getHeight() > var5.getHeight() && var2.intersects(var5)) {
            var3.paint(var1, var2);
         } else if (var2.getBounds().getY() >= var5.getY()) {
            if (var2.getBounds().getMaxY() <= (double)this.bottom) {
               var3.paint(var1, var2);
            } else if (var2.getBounds().getY() < this.pageEndY) {
               this.pageEndY = var2.getBounds().getY();
            }
         }
      }

      return var4;
   }

   protected void setDocument(JEditorPane var1) {
      this.jeditorPane = new JEditorPane();
      this.jeditorPane.setContentType(var1.getContentType());
      this.jeditorPane.setDocument(var1.getDocument());
   }

   public void extraPrint(Graphics2D var1, int var2, String var3, int var4) {
      if (this.scale < 1.0D) {
         var1.scale(1.0D / this.scale, 1.0D / this.scale);
      } else {
         var1.scale(this.scale, this.scale);
      }

      byte var5 = 8;
      Font var6 = var1.getFont();
      Color var7 = var1.getColor();
      Font var8 = new Font("Arial", 0, var5);
      var1.setColor(Color.BLACK);
      String var9 = MainPrinter.getFootText();
      String var10 = var9 + (MainPrinter.nyomtatvanyHibas && MainPrinter.nyomtatvanyEllenorzott ? " Hibás" : "") + "    Ellenőrző kód:" + barkodStringHash;
      String var11 = "      " + (var2 + 1) + ".lap - Nyomtatva: " + var3;
      var1.setFont(var8);
      if (MainPrinter.isAutofillMode()) {
         var10 = "A nyomtatvány jelen kitöltöttség mellett nem küldhető be!";
      }

      if (MainPrinter.nemKuldhetoBeSzoveg) {
         var10 = "Ny.v.:" + MainPrinter.sablonVerzio + " A nyomtatvány papír alapon nem küldhető be!";
      }

      if (!MainPrinter.kivonatoltanBekuldheto) {
         var10 = "Ny.v.:" + MainPrinter.sablonVerzio + " A nyomtatvány kivonatolt nyomtatási formában nem küldhető be!";
      }

      if (MainPrinter.papirosBekuldesTiltva) {
         var10 = "Ny.v.:" + MainPrinter.sablonVerzio + " A nyomtatvány jelen kitöltöttség mellett papír alapon nem küldhető be!";
      }

      if (MainPrinter.betaVersion) {
         var10 = "Ny.v.:" + MainPrinter.sablonVerzio + " A nyomtatvány ebben a verzióban nem küldhető be!";
      }

      if (MainPrinter.check_version > -1) {
         var10 = "Ny.v.:" + MainPrinter.sablonVerzio + " " + BookModel.CHECK_VALID_MESSAGES[MainPrinter.check_version];
      }

      int var12 = 0;
      if (var4 > 0) {
         var12 = -((int)((double)((this.headerVector.size() + 1) * 20) / this.scale));
      }

      var1.setClip(-((int)this.pFormat.getImageableX()), var12, (int)this.pFormat.getImageableWidth(), (int)this.pFormat.getImageableHeight());
      int var13 = (int)var1.getClipBounds().getMaxY();
      if (MainPrinter.kellFejlec) {
         int var14;
         if (var4 > 0 && this.headerVector.size() > 0) {
            var14 = SwingUtilities.computeStringWidth(var1.getFontMetrics(var8), (String)this.headerVector.elementAt(0)) / 2;
            var1.drawString((String)this.headerVector.elementAt(0), (int)(var1.getClip().getBounds().getWidth() / 2.0D) - var14, -this.headerVector.size() * 20);
         }

         if (var2 < 1) {
            for(var14 = 0; var14 < this.footerVector.size(); ++var14) {
               var1.drawString((String)this.footerVector.elementAt(var14), 0, var13 - (this.footerVector.size() - var14) * 20 - 10);
            }
         }
      }

      var8 = getFont4LastRow(var1, var8, var10 + var11);
      var1.setFont(var8);
      var1.drawString(var10 + var11, 0, var13 - 10);
      var1.setFont(var6);
      var1.setColor(var7);
   }

   public void barkodPrint(Graphics var1, BrPrinter var2, String var3, int var4) throws Exception {
      var2.setParameters(this.footerVector, this.headerVector, var3, var4);
      var2.print(var1, barkodStringHash);
   }

   public void setFooterVector(Vector var1) {
      this.footerVector = var1;
      this.bottomCorrection = (var1.size() + 1) * 20;
   }

   public void setHeaderVector(Vector var1) {
      this.headerVector = var1;
   }

   public void setBp(BrPrinter var1) throws Exception {
      this.bp = var1;
      Sha1Hash var2 = new Sha1Hash();
      barkodStringHash = var2.createHash(var1.getBarkodString().getBytes());
      printHasEnded = false;
      allHtmlDataPrinted = 0;
      lastHtmlPageIndex = -1;
      paintedObjectCount = 0;
   }

   public static Font getFont4LastRow(Graphics var0, Font var1, String var2) {
      int var3 = SwingUtilities.computeStringWidth(var0.getFontMetrics(var1), var2);

      for(int var4 = var1.getSize(); (double)var3 > var0.getClip().getBounds().getWidth() && var4 > 5; var3 = SwingUtilities.computeStringWidth(var0.getFontMetrics(var1), var2)) {
         --var4;
         var1 = new Font(MainPrinter.fontName, 1, var4);
      }

      return var1;
   }
}
