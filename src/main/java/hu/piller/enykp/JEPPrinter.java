package hu.piller.enykp;

import hu.piller.enykp.print.Barkod;
import hu.piller.enykp.print.Lap;
import hu.piller.enykp.print.LapMetaAdat;
import hu.piller.enykp.print.MainPrinter;
import hu.piller.enykp.util.base.Sha1Hash;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Vector;
import javax.print.PrintService;
import javax.swing.JEditorPane;
import javax.swing.SwingUtilities;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;
import javax.swing.text.View;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTML.Attribute;

public class JEPPrinter implements Printable {
   protected int currentPage = -1;
   protected JEditorPane jeditorPane;
   protected double pageEndY = 0.0D;
   protected double pageStartY = 0.0D;
   protected boolean scaleWidthToFit = true;
   protected int pageIndexOnPrint = 1;
   protected String timeString = "";
   protected PageFormat pFormat = new PageFormat();
   protected PrinterJob pJob = PrinterJob.getPrinterJob();
   protected PrintService ps;
   public static int simpleMargin = 10;
   private String barkodString;
   private String barkodStringHash;
   private Sha1Hash sha1Hash;
   private byte[] zippedData;
   private BufferedImage[] bis;
   private int barcodeNeed;
   private int barkodLength = 0;
   private Vector footerVector;
   private Vector headerVector;
   private static final int H_SPACE = 0;
   private static final int V_SPACE = 20;
   private static final int BOTTOM_MARGIN;
   private static final int SPACE4HTML;
   public static int folsoMargoKorrekcio;
   public static int laptukorKorrekcio;
   public static int lablecKorrekcio;
   public static int fejlecKorrekcio;
   public static int lapAlsoMargoAHtmlTablahoz;
   private static boolean printHasEnded;
   private static int allHtmlDataPrinted;
   private static int lastHtmlPageIndex;
   private static int paintedObjectCount;
   private static final double BR_CORRECTION = 0.8D;
   private boolean linuxMode = false;

   public int getCurrentPage() {
      return this.currentPage;
   }

   public String getTimeString() {
      return this.timeString;
   }

   public void setBarkodString(String var1) throws Exception {
      this.barkodString = var1;
      this.barkodStringHash = this.sha1Hash.createHash(var1.getBytes());
      this.setBrCount();
      if (this.barcodeNeed > 10) {
         throw new Exception("A lapra maximum 10 pontkódot tudunk nyomtatni.\nA nyomtatványban szereplő adatmennyiség nem fér el ennyi pontkódban. Kérjük nyomtassa ki a hagyományos módon!");
      } else {
         printHasEnded = false;
         allHtmlDataPrinted = 0;
         lastHtmlPageIndex = -1;
         paintedObjectCount = 0;
      }
   }

   public void setFooterVector(Vector var1) {
      this.footerVector = var1;
   }

   public void setHeaderVector(Vector var1) {
      this.headerVector = var1;
   }

   public JEPPrinter(PrintService var1, boolean var2) {
      this.ps = var1;
      this.linuxMode = var2;
      this.sha1Hash = new Sha1Hash();
      this.timeString = Lap.getTimeString();
      printHasEnded = false;
      allHtmlDataPrinted = 0;
      lastHtmlPageIndex = -1;
      paintedObjectCount = 0;
      String var3 = System.getProperty("os.version");
      System.out.println(var3);
   }

   public Document getDocument() {
      return this.jeditorPane != null ? this.jeditorPane.getDocument() : null;
   }

   public boolean getScaleWidthToFit() {
      return this.scaleWidthToFit;
   }

   public void pageDialog() {
      this.pFormat = this.pJob.pageDialog(this.pFormat);
   }

   public int print(Graphics var1, PageFormat var2, int var3) {
      Paper var4 = var2.getPaper();
      var4.setImageableArea((double)simpleMargin * 2.8D, (double)simpleMargin * 2.8D + (double)folsoMargoKorrekcio, var2.getWidth() - (double)(2 * simpleMargin) * 2.8D, var2.getHeight() - (double)simpleMargin * 2.8D);
      var2.setPaper(var4);
      double var5 = 1.0D;
      Graphics2D var7 = (Graphics2D)var1;
      this.jeditorPane.setSize((int)var2.getImageableWidth(), Integer.MAX_VALUE);
      this.jeditorPane.validate();
      View var8 = this.jeditorPane.getUI().getRootView(this.jeditorPane);
      if (this.scaleWidthToFit && this.jeditorPane.getMinimumSize().getWidth() > var2.getImageableWidth()) {
         var5 = var2.getImageableWidth() / this.jeditorPane.getMinimumSize().getWidth();
         if (var5 < 1.0D) {
            var7.scale(var5, var5);
         }
      }

      var7.setClip((int)(var2.getImageableX() / var5), (int)(var2.getImageableY() / var5), (int)(var2.getImageableWidth() / var5), (int)(var2.getImageableHeight() / var5));
      if (var3 > this.currentPage) {
         this.currentPage = var3;
         this.pageStartY += this.pageEndY;
         this.pageEndY = var7.getClipBounds().getHeight();
      }

      var7.translate(var7.getClipBounds().getX(), var7.getClipBounds().getY());
      Rectangle var9 = new Rectangle(0, (int)(-this.pageStartY), (int)this.jeditorPane.getMinimumSize().getWidth(), (int)this.jeditorPane.getPreferredSize().getHeight());
      lapAlsoMargoAHtmlTablahoz = SPACE4HTML + (this.footerVector.size() + 1) * 20;
      if (!printHasEnded && this.printView(var7, var9, var8)) {
         this.extraPrint(var7, var2);
         if (MainPrinter.getSeq() - 1 == paintedObjectCount) {
            ++allHtmlDataPrinted;
         }

         if (allHtmlDataPrinted > 1) {
            printHasEnded = true;
         }

         ++this.pageIndexOnPrint;
         lastHtmlPageIndex = var3 + 1;
         return 0;
      } else {
         this.pageStartY = 0.0D;
         this.pageEndY = 0.0D;
         if (!printHasEnded) {
            lastHtmlPageIndex = var3;
         }

         printHasEnded = true;
         if (!this.linuxMode) {
            if (MainPrinter.kivonatoltBarkoddal) {
               try {
                  this.barkodPrint(var1, var5);
               } catch (Exception var11) {
                  var11.printStackTrace();
               }

               printHasEnded = true;
               if (var3 == lastHtmlPageIndex) {
                  return 0;
               }
            }

            printHasEnded = true;
         }

         return 1;
      }
   }

   public void print(HTMLDocument var1) {
      this.setDocument(var1);
      this.printDialog();
   }

   public void print(JEditorPane var1) {
      this.setDocument(var1);
      this.printDialog();
   }

   public void print(PlainDocument var1) {
      this.setDocument(var1);
      this.printDialog();
   }

   protected void printDialog() {
      try {
         this.pJob.setPrintService(this.ps);
         this.pJob.setPrintable(this, this.pFormat);
         this.pJob.print();
      } catch (PrinterException var2) {
         var2.printStackTrace();
         this.pageStartY = 0.0D;
         this.pageEndY = 0.0D;
         this.currentPage = -1;
         System.out.println("Error Printing Document");
      }

   }

   protected boolean printView(Graphics2D var1, Shape var2, View var3) {
      boolean var4 = false;
      Rectangle var5 = var1.getClipBounds();
      if (var3.getViewCount() > 0 && !var3.getElement().getName().equalsIgnoreCase("td")) {
         if (var3.getElement().getName().equalsIgnoreCase("html")) {
            new Rectangle(0, 0, 0, 0);
         }

         for(int var8 = 0; var8 < var3.getViewCount(); ++var8) {
            Shape var6 = var3.getChildAllocation(var8, var2);
            if (var6 != null) {
               View var7 = var3.getView(var8);
               if (this.printView(var1, var6, var7)) {
                  var4 = true;
               }
            }
         }
      } else if (var2.getBounds().getMaxY() >= var5.getY() - (double)lapAlsoMargoAHtmlTablahoz) {
         var4 = true;
         if (var2.getBounds().getHeight() > var5.getHeight() - (double)lapAlsoMargoAHtmlTablahoz && var2.intersects(var5)) {
            if (var3.getElement().getAttributes().getAttribute(Attribute.CLASS) != null) {
               try {
                  paintedObjectCount = Integer.parseInt(var3.getElement().getAttributes().getAttribute(Attribute.CLASS).toString());
               } catch (Exception var10) {
               }
            }

            var3.paint(var1, var2);
         } else if (var2.getBounds().getY() >= var5.getY() - (double)lapAlsoMargoAHtmlTablahoz) {
            if (var2.getBounds().getMaxY() <= var5.getMaxY() - (double)lapAlsoMargoAHtmlTablahoz) {
               if (var3.getElement().getAttributes().getAttribute(Attribute.CLASS) != null) {
                  try {
                     paintedObjectCount = Integer.parseInt(var3.getElement().getAttributes().getAttribute(Attribute.CLASS).toString());
                  } catch (Exception var9) {
                  }
               }

               var3.paint(var1, var2);
            } else if (var2.getBounds().getY() < this.pageEndY) {
               this.pageEndY = var2.getBounds().getY();
            }
         }
      }

      return var4;
   }

   protected void setContentType(String var1) {
      this.jeditorPane.setContentType(var1);
   }

   public void setDocument(HTMLDocument var1) {
      this.jeditorPane = new JEditorPane();
      this.setDocument("text/html", var1);
   }

   public void setDocument(JEditorPane var1) {
      this.jeditorPane = new JEditorPane();
      this.setDocument(var1.getContentType(), var1.getDocument());
   }

   public void setDocument(PlainDocument var1) {
      this.jeditorPane = new JEditorPane();
      this.setDocument("text/plain", var1);
   }

   protected void setDocument(String var1, Document var2) {
      this.setContentType(var1);
      this.jeditorPane.setDocument(var2);
   }

   public void setScaleWidthToFit(boolean var1) {
      this.scaleWidthToFit = var1;
   }

   private void extraPrint(Graphics var1, PageFormat var2) {
      byte var3 = 8;
      Shape var4 = var1.getClip();
      lablecKorrekcio = BOTTOM_MARGIN + (this.footerVector.size() + 1) * 20;
      var1.setClip((int)var4.getBounds().getX(), (int)(var4.getBounds().getY() - (double)laptukorKorrekcio), (int)var4.getBounds().getWidth(), (int)var4.getBounds().getHeight());
      Font var5 = var1.getFont();
      Color var6 = var1.getColor();
      Font var7 = new Font("Arial", 0, var3);
      var1.setColor(Color.BLACK);
      String var8 = "Kitöltő verzió:" + "v.3.44.0".substring(2) + " Nyomtatvány verzió:" + MainPrinter.sablonVerzio + (MainPrinter.nyomtatvanyHibas && MainPrinter.nyomtatvanyEllenorzott ? " Hibás" : "") + "    Ellenőrző kód:" + this.barkodStringHash;
      String var9 = "      " + (this.currentPage + 1) + ".lap - Nyomtatva: " + this.timeString;
      if (MainPrinter.nemKuldhetoBeSzoveg) {
         var8 = "Kitöltő verzió:" + "v.3.44.0".substring(2) + " Nyomtatvány verzió:" + MainPrinter.sablonVerzio + " A nyomtatvány ebben a formában nem küldhető be!";
      }

      var1.setFont(var7);
      if (!MainPrinter.getBookModel().getOperationMode().equals("0")) {
         var8 = "Bárkód: " + MainPrinter.getBookModel().getBarcode();
         var9 = "";
      }

      if (MainPrinter.kellFejlec) {
         if (this.pageIndexOnPrint > 2 && this.headerVector.size() > 0) {
            var1.drawString((String)this.headerVector.elementAt(0), 0, fejlecKorrekcio);
         }

         if (this.currentPage < 1) {
            for(int var10 = 0; var10 < this.footerVector.size(); ++var10) {
               var1.drawString((String)this.footerVector.elementAt(var10), 0, (int)var1.getClip().getBounds().getHeight() - lablecKorrekcio + (var10 + 1) * 20);
            }
         }
      }

      var7 = this.getFont4LastRow(var1, var7, var8 + var9);
      var1.setFont(var7);
      var1.drawString(var8 + var9, 0, (int)var1.getClip().getBounds().getHeight() - lablecKorrekcio + (this.footerVector.size() + 1) * 20);
      var1.setFont(var5);
      var1.setColor(var6);
      var1.setClip(var4);
   }

   public void barkodPrint(Graphics var1, double var2) throws Exception {
      double var4 = var2 < 1.0D ? 0.8D : 0.7200000000000001D;
      Font var6 = var1.getFont();
      Color var7 = var1.getColor();
      byte var8 = 8;
      byte var9 = 12;
      int var10 = var9;
      Font var11 = new Font("Arial", 0, var8);
      Font var12 = new Font("Arial", 1, var9);
      var1.setColor(Color.BLACK);
      String var15 = MainPrinter.mainLabel;
      String var16 = "Figyelem! Ha papíron adja be a nyomtatványát, akkor ezt a lapot mindenképpen küldje be vele együtt!";
      String var17 = "Kitöltő verzió:" + "v.3.44.0".substring(2) + " Nyomtatvány verzió:" + MainPrinter.sablonVerzio + (MainPrinter.nyomtatvanyHibas && MainPrinter.nyomtatvanyEllenorzott ? " Hibás" : "") + "    Ellenőrző kód:" + this.barkodStringHash;
      String var18 = "   " + (this.currentPage + 1) + ".lap - Nyomtatva: " + this.timeString;
      if (!MainPrinter.getBookModel().getOperationMode().equals("0")) {
         var17 = "Bárkód: " + MainPrinter.getBookModel().getBarcode();
         var18 = "";
      }

      int var19 = SwingUtilities.computeStringWidth(var1.getFontMetrics(var12), var15) / 2;
      int var20 = SwingUtilities.computeStringWidth(var1.getFontMetrics(var12), var16) / 2;

      Font var13;
      for(var13 = new Font("Arial", 1, var9); (double)var19 > (var1.getClip().getBounds().getWidth() - (double)(2 * simpleMargin) * 2.8D) / 2.0D && var10 > 5; var19 = SwingUtilities.computeStringWidth(var1.getFontMetrics(var13), var15) / 2) {
         --var10;
         var13 = new Font("Arial", 1, var10);
      }

      var10 = var9;

      Font var14;
      for(var14 = new Font("Arial", 1, var9); (double)var20 > (var1.getClip().getBounds().getWidth() - (double)(2 * simpleMargin) * 2.8D) / 2.0D && var10 > 5; var20 = SwingUtilities.computeStringWidth(var1.getFontMetrics(var14), var16) / 2) {
         --var10;
         var14 = new Font("Arial", 1, var10);
      }

      var1.setFont(var11);
      Shape var21 = var1.getClip();
      var1.setClip((int)var21.getBounds().getX(), (int)(var21.getBounds().getY() - (double)laptukorKorrekcio), (int)var21.getBounds().getWidth(), (int)var21.getBounds().getHeight());
      ((Graphics2D)var1).scale(var2, var2);
      if (this.headerVector.size() > 0) {
         var1.drawString((String)this.headerVector.elementAt(0), 0, fejlecKorrekcio);
      }

      ((Graphics2D)var1).scale(1.0D / var2, 1.0D / var2);
      var1.setFont(var13);
      var1.setClip(var21);
      var1.drawString(var15, (int)((var1.getClip().getBounds().getWidth() - (double)(2 * simpleMargin) * 2.8D) / 2.0D) - var19, 10);
      var1.setFont(var14);
      var1.drawString(var16, (int)((var1.getClip().getBounds().getWidth() - (double)(2 * simpleMargin) * 2.8D) / 2.0D) - var20, 40);
      byte var22 = 70;
      int var24 = 0;
      int var25;
      if (MainPrinter.getBookModel().getOperationMode().equals("0")) {
         ((Graphics2D)var1).scale(var4, var4);

         try {
            var24 = Math.max(this.bis.length + 1, 10);
            byte var23 = 40;

            for(var25 = 0; var25 < this.bis.length; ++var25) {
               var1.drawImage(this.bis[var25], (int)((double)var23 + (double)(var25 / 5 * 350) * 1.25D), var25 % 5 * 165 + var22, (ImageObserver)null);
            }
         } catch (Exception var27) {
            if (var27 instanceof PrinterException) {
               var1.drawString(var27.getMessage(), 0, var22 + 100);
            } else {
               var27.printStackTrace();
            }

            var1.setFont(var6);
            var1.setColor(var7);
            ((Graphics2D)var1).setTransform(new AffineTransform());
            this.bis = null;
            return;
         }

         ((Graphics2D)var1).scale(1.0D / var4, 1.0D / var4);
      }

      ((Graphics2D)var1).scale(var2, var2);
      var1.setFont(var11);
      var25 = var24 / 2 * 150;
      if (this.footerVector.size() > 0) {
         for(int var26 = 0; var26 < this.footerVector.size(); ++var26) {
            var1.drawString((String)this.footerVector.elementAt(var26), 0, var25 + (var26 + 1) * 20);
         }
      }

      var11 = this.getFont4LastRow(var1, var11, var17 + var18);
      var1.setFont(var11);
      var1.drawString(var17 + var18, 0, var25 + (this.footerVector.size() + 1) * 20);
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
//         BarCode2D var4 = var2.getB2dImg(this.zippedData, var5, MainPrinter.barkod_FixFejlecAdatok.getBytes(), var9.getBytes());
//         var4.leftMarginCM = 0.0D;
//         var4.topMarginCM = 0.0D;
         Graphics var10 = var3[var5].getGraphics();
         var10.setColor(Color.WHITE);
         var10.fillRect(0, 0, 250, var1);
         byte var11 = 0;

//         while(var3[var5].getRGB(0, 0) == -1 && var11 < 3) {
//            var4.paint(var10);
//         }

//         var4 = null;
      }

      var2 = null;
      return var3;
   }

   private String[] writeBr2File(BufferedImage[] var1) {
      return null;
   }

   private void writeData2File(byte[] var1, String var2) throws IOException {
      FileOutputStream var3 = new FileOutputStream("D:\\temp\\zb2d.zip");
      var3.write(var1);
      var3.close();
      var3 = new FileOutputStream("D:\\temp\\p2d.txt");
      var3.write(var2.getBytes());
      var3.close();
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
         var2 = new Font("Arial", 1, var5);
      }

      return var2;
   }

   static {
      BOTTOM_MARGIN = 110 + (int)((double)MainPrinter.nyomtatoMargo * 2.8D);
      SPACE4HTML = 110 + (int)((double)MainPrinter.nyomtatoMargo * 2.8D);
      folsoMargoKorrekcio = 20;
      laptukorKorrekcio = 10 + (int)((double)MainPrinter.nyomtatoMargo * 2.8D);
      lablecKorrekcio = BOTTOM_MARGIN;
      fejlecKorrekcio = -10;
      lapAlsoMargoAHtmlTablahoz = SPACE4HTML;
      printHasEnded = false;
      allHtmlDataPrinted = 0;
      lastHtmlPageIndex = -1;
      paintedObjectCount = 0;
   }
}
