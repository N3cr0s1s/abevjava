package hu.piller.enykp.print.simpleprint;

import com.java4less.rbarcode.BarCode2D;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.PageSize;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;
import hu.piller.enykp.gui.model.BookModel;
import hu.piller.enykp.print.AbevFontMapper;
import hu.piller.enykp.print.Barkod;
import hu.piller.enykp.print.Jep2Pdf;
import hu.piller.enykp.print.Lap;
import hu.piller.enykp.print.LapMetaAdat;
import hu.piller.enykp.print.MainPrinter;
import hu.piller.enykp.util.base.Sha1Hash;
import hu.piller.enykp.util.base.Tools;
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
import java.awt.print.PrinterException;
import java.io.FileNotFoundException;
import java.util.Vector;
import javax.swing.JEditorPane;
import javax.swing.SwingUtilities;
import javax.swing.text.View;
import javax.swing.text.html.HTML.Attribute;

public class ExtendedJep2Pdf {
   protected int currentPage = -1;
   protected JEditorPane jeditorPane;
   protected double pageEndY = 0.0D;
   protected double pageStartY = 0.0D;
   protected boolean scaleWidthToFit = true;
   protected String timeString = "";
   protected PageFormat pFormat;
   private String barkodString;
   private String barkodStringHash;
   private Sha1Hash sha1Hash;
   private byte[] zippedData;
   private BufferedImage[] bis;
   private int barcodeNeed;
   private int barkodLength = 0;
   private Vector footerVector;
   private Vector headerVector;
   private static final int MARGIN_E = 30;
   private static final int MARGIN_W = 30;
   private static final int MARGIN_N = 55;
   private static final int MARGIN_S = 30;
   private static final int FOOTER_MARGIN = 55;
   private static int SPACE4FOOTER = 60;
   private static final int H_SPACE = 0;
   private static final int V_SPACE = 20;
   private static final int PDF_HEADER_CORRECTION = 40;
   private static int htmlrowDuplicateCorrection = 40;
   private static int paintedObjectCount = 0;
   protected double prevPageStartY = -1.0D;
   private String pdfFileName;
   protected String htmlData;
   protected int lastPartIndex;
   public static final int PAGE_FORMAT_WIDTH = 600;
   public static final int PAGE_FORMAT_HEIGHT = 850;
   Font titleFont;
   Font infoFont;
   Font warningFont;
   Font mainTitleFont;
   Font font4MaxWidthString;
   String titleString;
   String warningString;
   String info;
   String mainTitleString;
   String stringWithMaxWidth;
   int initFontSize = 12;
   int localBrHeight = 150;
   private static final int LANDSCAPE_SPACE4FOOTER = 30;
   private static final int TEMP = 0;

   public void setLastPartIndex(int var1) {
      this.lastPartIndex = var1;
   }

   public int getCurrentPage() {
      return this.currentPage;
   }

   public String getTimeString() {
      return this.timeString;
   }

   public void setFooterVector(Vector var1) {
      this.footerVector = var1;
      SPACE4FOOTER = 60 + var1.size() * 20;
   }

   public void setHeaderVector(Vector var1) {
      this.headerVector = var1;
      htmlrowDuplicateCorrection = 40 - var1.size() * 20;
   }

   public ExtendedJep2Pdf(String var1, String var2, String var3) throws Exception {
      this.pdfFileName = var1;
      this.htmlData = var2;
      this.barkodString = var3;
      this.jeditorPane = new JEditorPane("text/html", var2);
      this.setFooterVector(new Vector());
      this.setHeaderVector(new Vector());
      this.pFormat = new PageFormat();
      this.sha1Hash = new Sha1Hash();
      this.timeString = Lap.getTimeString();
      paintedObjectCount = 0;
      this.barkodStringHash = this.sha1Hash.createHash(var3.getBytes());
      if (MainPrinter.kivonatoltBarkoddal && (MainPrinter.autoFillPdfPrevFileName == null || !MainPrinter.bookModel.autofill)) {
         this.setBrCount();
      }

      if (this.barcodeNeed > 10) {
         throw new NumberFormatException("A lapra maximum 10 pontkódot tudunk nyomtatni.\nA nyomtatványban szereplő adatmennyiség nem fér el ennyi pontkódban. Kérjük nyomtassa ki a hagyományos módon!");
      } else {
         Paper var4 = this.pFormat.getPaper();
         var4.setImageableArea(30.0D, 55.0D, 570.0D, 820.0D);
         this.pFormat.setPaper(var4);
         this.jeditorPane.setSize((int)this.pFormat.getImageableWidth(), Integer.MAX_VALUE);
         this.jeditorPane.validate();
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
      } else if (var2.getBounds().getMaxY() >= var5.getY() - (double)SPACE4FOOTER - 25.0D) {
         var4 = true;
         if (var2.getBounds().getHeight() > var5.getHeight() - (double)SPACE4FOOTER - 25.0D && var2.intersects(var5)) {
            if (var3.getElement().getAttributes().getAttribute(Attribute.CLASS) != null) {
               try {
                  paintedObjectCount = Integer.parseInt(var3.getElement().getAttributes().getAttribute(Attribute.CLASS).toString());
               } catch (Exception var12) {
                  Tools.eLog(var12, 0);
               }
            }

            if (var2.getBounds().getHeight() > 150.0D) {
               try {
                  Thread.sleep(500L);
               } catch (Exception var11) {
                  Tools.eLog(var11, 0);
               }
            }

            var3.paint(var1, var2);
         } else if (var2.getBounds().getY() >= var5.getY() - (double)SPACE4FOOTER - 25.0D) {
            if (var2.getBounds().getMaxY() <= var5.getMaxY() - (double)SPACE4FOOTER - 25.0D) {
               if (var3.getElement().getAttributes().getAttribute(Attribute.CLASS) != null) {
                  try {
                     paintedObjectCount = Integer.parseInt(var3.getElement().getAttributes().getAttribute(Attribute.CLASS).toString());
                  } catch (Exception var10) {
                     Tools.eLog(var10, 0);
                  }
               }

               if (var2.getBounds().getHeight() > 150.0D) {
                  try {
                     Thread.sleep(500L);
                  } catch (Exception var9) {
                     Tools.eLog(var9, 0);
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

   public void setDocument(JEditorPane var1) {
      this.jeditorPane = new JEditorPane();
      this.jeditorPane.setContentType("text/html");
      this.jeditorPane.setDocument(var1.getDocument());
   }

   public void setScaleWidthToFit(boolean var1) {
      this.scaleWidthToFit = var1;
   }

   private void extraPrint(Graphics var1, int var2, double var3) {
      int var5 = 0;
      int var6 = (int)(40.0D / var3);
      byte var7 = 8;
      Shape var8 = var1.getClip();
      var1.setClip((int)var8.getBounds().getX(), (int)var8.getBounds().getY() - 40, (int)var8.getBounds().getWidth(), (int)var8.getBounds().getHeight());
      Font var9 = var1.getFont();
      Color var10 = var1.getColor();
      Font var11 = new Font(MainPrinter.fontName, 0, var7);
      var1.setColor(Color.BLACK);
      this.getFootText(var2);
      if (MainPrinter.kellFejlec) {
         if (var2 > 0 && this.headerVector.size() > 0) {
            var11 = this.getFont4String(var1, var11, (String)this.headerVector.elementAt(0));
            var1.setFont(var11);
            int var12 = SwingUtilities.computeStringWidth(var1.getFontMetrics(var11), (String)this.headerVector.elementAt(0)) / 2;
            var1.drawString((String)this.headerVector.elementAt(0), (int)((var1.getClip().getBounds().getWidth() - 40.0D) / 2.0D) - var12, -25);
            ++var5;
         }

         if (var2 == 0) {
            String var14 = getMaxLength(this.footerVector, new Vector());
            var11 = this.getFont4String(var1, var11, var14);
            var1.setFont(var11);

            for(int var13 = 0; var13 < this.footerVector.size(); ++var13) {
               var1.drawString((String)this.footerVector.elementAt(var13), 0, (int)var1.getClip().getBounds().getHeight() - SPACE4FOOTER + var13 * 20);
               ++var5;
            }
         }
      }

      var11 = this.getFont4String(var1, var11, this.info);
      var1.setFont(var11);
      if (var2 == 0) {
         var1.drawString(this.info, 0, (int)var1.getClip().getBounds().getHeight() - 30 - 10 * var5);
      } else {
         var1.drawString(this.info, 0, (int)var1.getClip().getBounds().getHeight() - 30 - 55 - 10 * var5);
      }

      var1.setFont(var9);
      var1.setColor(var10);
      var1.setClip(var8);
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

   private Font getFont4String(Graphics var1, Font var2, String var3) {
      return this.getFont4String(var1, var2, var3, 0);
   }

   private Font getFont4String(Graphics var1, Font var2, String var3, int var4) {
      int var5 = SwingUtilities.computeStringWidth(var1.getFontMetrics(var2), var3);

      for(int var6 = var2.getSize(); (double)var5 > var1.getClip().getBounds().getWidth() - (double)var4 && var6 > 5; var5 = SwingUtilities.computeStringWidth(var1.getFontMetrics(var2), var3)) {
         --var6;
         var2 = new Font(MainPrinter.fontName, 1, var6);
      }

      return var2;
   }

   public int printJep2Pdf(Document var1, PdfWriter var2, boolean var3, int var4, int var5) throws DocumentException, FileNotFoundException {
      int var7 = var4;

      Graphics2D var6;
      try {
         View var8 = this.jeditorPane.getUI().getRootView(this.jeditorPane);
         double var9 = 1.0D;
         double var11 = Math.max((double)this.jeditorPane.getWidth(), this.jeditorPane.getMinimumSize().getWidth());
         if (this.scaleWidthToFit && var11 > this.pFormat.getImageableWidth() - this.pFormat.getImageableX()) {
            var9 = (this.pFormat.getImageableWidth() - this.pFormat.getImageableX()) / var11;
         }

         if (ExtendedPdfHandler.getSeq(var4) == 1) {
            --paintedObjectCount;
         }

         while(paintedObjectCount < ExtendedPdfHandler.getSeq(var4) - 1) {
            var1.setPageSize(PageSize.A4);
            var1.newPage();
            PdfContentByte var13 = var2.getDirectContent();
            PdfTemplate var14;
            if (var7 == 0) {
               var14 = var13.createTemplate((float)this.pFormat.getImageableWidth(), (float)(this.pFormat.getImageableHeight() + 40.0D));
               var6 = var14.createGraphics((float)this.pFormat.getImageableWidth(), (float)(this.pFormat.getImageableHeight() + 40.0D), AbevFontMapper.getInstance());
            } else {
               var14 = var13.createTemplate((float)this.pFormat.getImageableWidth(), (float)(this.pFormat.getImageableHeight() + (double)(40 - 20 * this.headerVector.size()) / var9));
               var6 = var14.createGraphics((float)this.pFormat.getImageableWidth(), (float)(this.pFormat.getImageableHeight() + (double)(40 - 20 * this.headerVector.size()) / var9), AbevFontMapper.getInstance());
            }

            if (var9 < 1.0D) {
               var6.scale(var9, var9);
            }

            var13 = var2.getDirectContent();
            if (var7 == 0) {
               var6.setClip((int)(this.pFormat.getImageableX() / var9), (int)(this.pFormat.getImageableY() / var9), (int)(this.pFormat.getImageableWidth() / var9), (int)(this.pFormat.getImageableHeight() / var9));
            } else {
               var6.setClip((int)(this.pFormat.getImageableX() / var9), (int)(this.pFormat.getImageableY() / var9), (int)(this.pFormat.getImageableWidth() / var9), (int)(this.pFormat.getImageableHeight() / var9));
            }

            if (var7 > this.currentPage) {
               this.currentPage = var7;
               this.pageStartY += this.pageEndY;
               this.pageEndY = var6.getClipBounds().getHeight();
            }

            if (var7 == 0) {
               var6.translate(var6.getClipBounds().getX(), var6.getClipBounds().getY());
            } else {
               var6.translate(var6.getClipBounds().getX(), (double)(40 - 20 * this.headerVector.size()) + var6.getClipBounds().getY());
            }

            Rectangle var15 = new Rectangle(0, (int)(-this.pageStartY), (int)this.jeditorPane.getMinimumSize().getWidth(), (int)this.jeditorPane.getPreferredSize().getHeight());
            if (this.printView(var6, var15, var8)) {
               this.extraPrint(var6, var5 + var7 - var4, var9);
               this.pageStartY += (double)htmlrowDuplicateCorrection;
            }

            ++var7;
            var13.addTemplate(var14, 0.0F, 0.0F);
            if (ExtendedPdfHandler.getSeq(var4) == 1) {
               ++paintedObjectCount;
            }
         }
      } catch (Exception var17) {
         var17.printStackTrace();
      }

      if (var3) {
         var6 = null;

         try {
            this.jeditorPane.setText("<html><body></body><html>");
         } catch (Exception var16) {
            Tools.eLog(var16, 0);
         }

         this.jeditorPane = null;
         this.htmlData = null;
         this.barkodString = null;
         this.zippedData = null;
         this.bis = null;
      }

      return var7;
   }

   public int printLandscapeJep2Pdf(Document var1, PdfWriter var2, boolean var3, int var4, int var5) throws DocumentException, FileNotFoundException {
      int var7 = var4;

      Graphics2D var6;
      try {
         View var8 = this.jeditorPane.getUI().getRootView(this.jeditorPane);
         double var9 = 1.0D;
         double var11 = Math.max((double)this.jeditorPane.getWidth(), this.jeditorPane.getMinimumSize().getWidth()) + 30.0D;
         if (this.scaleWidthToFit && var11 > this.pFormat.getImageableWidth() - this.pFormat.getImageableX()) {
            var9 = (this.pFormat.getImageableWidth() - this.pFormat.getImageableX()) / var11;
         }

         if (var9 > 1.0D) {
            var9 = 1.0D;
         }

         if (ExtendedPdfHandler.getSeq(var4) == 1) {
            --paintedObjectCount;
         }

         PdfContentByte var13;
         PdfTemplate var14;
         while(paintedObjectCount < ExtendedPdfHandler.getSeq(var4) - 1) {
            var1.setPageSize(PageSize.A4.rotate());
            var1.newPage();
            var13 = var2.getDirectContent();
            if (var7 == 0) {
               var14 = var13.createTemplate((float)this.pFormat.getImageableWidth(), (float)(this.pFormat.getImageableHeight() + 40.0D));
               var6 = var14.createGraphics((float)this.pFormat.getImageableWidth(), (float)(this.pFormat.getImageableHeight() + 40.0D), AbevFontMapper.getInstance());
            } else {
               var14 = var13.createTemplate((float)this.pFormat.getImageableWidth(), (float)(this.pFormat.getImageableHeight() + (double)(40 - 20 * this.headerVector.size())));
               var6 = var14.createGraphics((float)this.pFormat.getImageableWidth(), (float)(this.pFormat.getImageableHeight() + (double)(40 - 20 * this.headerVector.size())), AbevFontMapper.getInstance());
            }

            if (var9 < 1.0D) {
               var6.scale(var9, var9);
            }

            var13 = var2.getDirectContent();
            var6.setClip((int)(this.pFormat.getImageableX() / var9), (int)(this.pFormat.getImageableY() / var9), (int)(this.pFormat.getImageableWidth() / var9), (int)(this.pFormat.getImageableHeight() / var9));
            if (var7 > this.currentPage) {
               this.currentPage = var7;
               this.pageStartY += this.pageEndY;
               this.pageEndY = var6.getClipBounds().getHeight();
            }

            var6.translate(30, 70);
            Rectangle var15 = new Rectangle(0, (int)(-this.pageStartY), (int)this.jeditorPane.getMinimumSize().getWidth(), (int)this.jeditorPane.getPreferredSize().getHeight());
            if (this.printLandscapeView(var6, var15, var8)) {
               this.extraLandscapePrint(var6, var5 + var7 - var4, var9);
            }

            ++var7;
            var13.addTemplate(var14, 0.0F, 0.0F);
            if (ExtendedPdfHandler.getSeq(var4) == 1) {
               ++paintedObjectCount;
            }
         }

         var13 = null;
         var14 = null;
      } catch (Exception var17) {
         var17.printStackTrace();
      }

      if (var3) {
         var6 = null;

         try {
            this.jeditorPane.setText("<html><body></body><html>");
         } catch (Exception var16) {
            Tools.eLog(var16, 0);
         }

         this.jeditorPane = null;
         this.htmlData = null;
         this.barkodString = null;
         this.zippedData = null;
         this.bis = null;
      }

      return var7;
   }

   public void printBarcode(Document var1, PdfWriter var2, int var3) {
      PdfContentByte var4;
      PdfTemplate var5;
      if (MainPrinter.kivonatoltBarkoddal && (MainPrinter.autoFillPdfPrevFileName == null || !MainPrinter.bookModel.autofill)) {
         this.currentPage = -1;
         this.pageEndY = 0.0D;
         this.pageStartY = 0.0D;
         this.jeditorPane.setText("");
         paintedObjectCount = 0;
         this.pFormat.setOrientation(1);
         Paper var6 = this.pFormat.getPaper();
         var6.setImageableArea(30.0D, 55.0D, 570.0D, 820.0D);
         this.pFormat.setPaper(var6);
         this.jeditorPane.setSize((int)this.pFormat.getImageableWidth(), Integer.MAX_VALUE);
         this.jeditorPane.validate();
         var1.setPageSize(PageSize.A4);
         var1.newPage();
         var4 = var2.getDirectContent();
         var5 = var4.createTemplate((float)this.pFormat.getImageableWidth(), (float)this.pFormat.getImageableHeight());
         Graphics2D var7 = var5.createGraphics((float)this.pFormat.getImageableWidth(), (float)this.pFormat.getImageableHeight(), AbevFontMapper.getInstance());

         try {
            this.barkodPrint(var7, var3);
         } catch (Exception var9) {
            var9.printStackTrace();
         }

         var4.addTemplate(var5, 0.0F, 0.0F);
      }

      var4 = null;
      var5 = null;
   }

   public static String getMaxLength(Vector var0, Vector var1) {
      String var2 = "";

      int var3;
      String var4;
      for(var3 = 0; var3 < var0.size(); ++var3) {
         var4 = (String)var0.elementAt(var3);
         if (var2.length() < var4.length()) {
            var2 = var4;
         }
      }

      for(var3 = 0; var3 < var1.size(); ++var3) {
         var4 = (String)var1.elementAt(var3);
         if (var2.length() < var4.length()) {
            var2 = var4;
         }
      }

      return var2;
   }

   public void barkodPrint(Graphics var1, int var2) throws Exception {
      ((Graphics2D)var1).scale(0.8D, 0.8D);
      this.titleString = MainPrinter.mainLabel;
      this.warningString = "Figyelem! Ha papíron adja be a nyomtatványát, akkor ezt a lapot mindenképpen küldje be vele együtt!";
      this.getFootText(var2);
      this.stringWithMaxWidth = getMaxLength(this.footerVector, this.headerVector);
      int var3 = (int)var1.getClipBounds().getX();
      int var4 = (int)var1.getClipBounds().getY();
      int var5 = (int)var1.getClipBounds().getMaxX();
      int var6 = (int)var1.getClipBounds().getMaxY();
      byte var8 = 40;
      int var9 = var4 + 35;
      int var7 = this.initFontSize;
      this.infoFont = new Font(MainPrinter.fontName, 0, var7);
      this.titleFont = new Font(MainPrinter.fontName, 1, var7);
      this.warningFont = new Font(MainPrinter.fontName, 1, var7);
      this.mainTitleFont = new Font(MainPrinter.fontName, 1, var7);
      this.font4MaxWidthString = new Font(MainPrinter.fontName, 1, var7);
      boolean var10 = true;
      if (this.headerVector.size() > 0) {
         int var19 = SwingUtilities.computeStringWidth(var1.getFontMetrics(this.mainTitleFont), (String)this.headerVector.elementAt(0)) / 2;
      }

      this.titleFont = this.getFont4String(var1, this.infoFont, this.titleString, var8);
      this.warningFont = this.getFont4String(var1, this.infoFont, this.warningString, var8);
      this.infoFont = this.getFont4String(var1, this.infoFont, this.info, var8);
      this.font4MaxWidthString = this.getFont4String(var1, this.font4MaxWidthString, this.stringWithMaxWidth, var8);
      var1.setColor(Color.BLACK);
      int var11;
      if (this.headerVector.size() > 0) {
         this.mainTitleFont = this.getFont4String(var1, this.mainTitleFont, (String)this.headerVector.elementAt(0), var8);
         var1.setFont(this.mainTitleFont);
         var11 = SwingUtilities.computeStringWidth(var1.getFontMetrics(this.mainTitleFont), (String)this.headerVector.elementAt(0)) / 2;
         var1.drawString((String)this.headerVector.elementAt(0), Math.max(0, (int)(var1.getClip().getBounds().getWidth() / 2.0D) - var11), var9);
      }

      int var12;
      int var13;
      if (this.headerVector.size() > 1) {
         String var20 = getMaxLength(this.headerVector, new Vector());
         this.font4MaxWidthString = this.getFont4String(var1, this.font4MaxWidthString, var20, var8);
         var1.setFont(this.font4MaxWidthString);

         for(var13 = 1; var13 < this.headerVector.size(); ++var13) {
            var12 = SwingUtilities.computeStringWidth(var1.getFontMetrics(this.font4MaxWidthString), (String)this.headerVector.elementAt(var13)) / 2;
            var1.drawString((String)this.headerVector.elementAt(var13), Math.max(0, (int)(var1.getClip().getBounds().getWidth() / 2.0D) - var12), var9 + var13 * 20);
         }
      }

      var1.setFont(this.titleFont);
      var11 = SwingUtilities.computeStringWidth(var1.getFontMetrics(this.titleFont), this.titleString) / 2;
      var1.drawString(this.titleString, Math.max(0, (var5 - var3) / 2 - var11), var9 + this.headerVector.size() * 20);
      var1.setFont(this.warningFont);
      var12 = SwingUtilities.computeStringWidth(var1.getFontMetrics(this.warningFont), this.warningString) / 2;
      if (!MainPrinter.papirosBekuldesTiltva && !MainPrinter.isTemplateDisabled) {
         var1.drawString(this.warningString, (var5 - var3) / 2 - var12, var9 + (this.headerVector.size() + 1) * 20);
      }

      var13 = var9 + (this.headerVector.size() + 2) * 20;
      int var14 = 0;
      int var15;
      if (MainPrinter.getBookModel().getOperationMode().equals("0") && !MainPrinter.betaVersion && !MainPrinter.papirosBekuldesTiltva && !MainPrinter.isTemplateDisabled) {
         try {
            var14 = Math.max(this.bis.length + 1, 10);

            for(var15 = 0; var15 < this.bis.length; ++var15) {
               var1.drawImage(this.bis[var15], (int)((double)var8 + (double)(var15 / 5 * 320) * 1.25D), var15 % 5 * 165 + var13, (ImageObserver)null);
            }
         } catch (Exception var18) {
            if (var18 instanceof PrinterException) {
               var1.drawString(var18.getMessage(), var8, var13 + 100);
            } else {
               var18.printStackTrace();
            }

            ((Graphics2D)var1).setTransform(new AffineTransform());
            this.bis = null;
            return;
         }
      }

      var15 = var14 / 2 * 165 + var13;
      String var16 = Jep2Pdf.getMaxLength(this.footerVector, new Vector());
      this.infoFont = this.getFont4String(var1, this.infoFont, var16, var8);
      var1.setFont(this.infoFont);
      if (this.footerVector.size() > 0) {
         for(int var17 = 0; var17 < this.footerVector.size(); ++var17) {
            var1.drawString((String)this.footerVector.elementAt(var17), var8, var15 + (var17 + 1) * (20 - (var17 == this.footerVector.size() - 1 ? 0 : 5)));
         }
      }

      this.infoFont = this.getFont4String(var1, this.infoFont, this.info, var8);
      var1.setFont(this.infoFont);
      var1.drawString(this.info, var8, var15 + (this.footerVector.size() + 1) * 20);
   }

   private void getFootText(int var1) {
      String var2 = MainPrinter.getFootText();
      this.info = var2 + (MainPrinter.nyomtatvanyHibas && MainPrinter.nyomtatvanyEllenorzott ? " Hibás" : "") + "    Ellenőrző kód:" + this.barkodStringHash;
      this.info = this.info + "      " + (var1 + 1) + ".lap - Nyomtatva: " + this.timeString;
      if (MainPrinter.isAutofillMode()) {
         this.info = "A nyomtatvány jelen kitöltöttség mellett nem küldhető be!     " + (var1 + 1) + ".lap - Nyomtatva: " + this.timeString;
      }

      if (MainPrinter.nemKuldhetoBeSzoveg) {
         this.info = "Ny.v.:" + MainPrinter.sablonVerzio + " A nyomtatvány papír alapon nem küldhető be!     " + (var1 + 1) + ".lap - Nyomtatva: " + this.timeString;
      }

      if (!MainPrinter.kivonatoltanBekuldheto) {
         this.info = "Ny.v.:" + MainPrinter.sablonVerzio + " A nyomtatvány kivonatolt nyomtatási formában nem küldhető be!   " + (var1 + 1) + ".lap - Nyomtatva: " + this.timeString;
      }

      if (MainPrinter.papirosBekuldesTiltva) {
         this.info = "Ny.v.:" + MainPrinter.sablonVerzio + " A nyomtatvány jelen kitöltöttség mellett papír alapon nem küldhető be!     " + (var1 + 1) + ".lap - Nyomtatva: " + this.timeString;
      }

      if (MainPrinter.betaVersion) {
         this.info = "Ny.v.:" + MainPrinter.sablonVerzio + " A nyomtatvány ebben a verzióban nem küldhető be!     " + (var1 + 1) + ".lap - Nyomtatva: " + this.timeString;
      }

      if (MainPrinter.check_version > -1) {
         this.info = "Ny.v.:" + MainPrinter.sablonVerzio + " " + BookModel.CHECK_VALID_MESSAGES[MainPrinter.check_version];
      }

      if (!MainPrinter.getBookModel().getOperationMode().equals("0")) {
         this.info = "Bárkód: " + MainPrinter.getBookModel().getBarcode();
      }

   }

   public void reInitPane(String var1, int var2) {
      this.currentPage = -1;
      this.pageEndY = 0.0D;
      this.pageStartY = 0.0D;
      this.htmlData = var1;
      this.jeditorPane.setText(var1);
      paintedObjectCount = 0;
      this.pFormat.setOrientation(var2);
      Paper var3 = this.pFormat.getPaper();
      if (var2 == 0) {
         com.lowagie.text.Rectangle var4 = PageSize.A4.rotate();
         var3.setSize((double)(var4.getHeight() - 30.0F), (double)(var4.getWidth() - 30.0F));
         var3.setImageableArea(0.0D, 0.0D, (double)(var4.getHeight() - 30.0F), (double)(var4.getWidth() - 30.0F));
         this.pFormat.setPaper(var3);
         this.jeditorPane.setSize((int)var4.getWidth(), Integer.MAX_VALUE);
      } else {
         var3.setImageableArea(30.0D, 55.0D, 570.0D, 820.0D);
         this.pFormat.setPaper(var3);
         this.jeditorPane.setSize((int)this.pFormat.getImageableWidth(), Integer.MAX_VALUE);
      }

      this.jeditorPane.validate();
   }

   protected boolean printLandscapeView(Graphics2D var1, Shape var2, View var3) {
      boolean var4 = false;
      Rectangle var5 = var1.getClipBounds();
      if (var3.getViewCount() > 0 && !"td".equalsIgnoreCase(var3.getElement().getName())) {
         if ("html".equalsIgnoreCase(var3.getElement().getName())) {
            new Rectangle(0, 0, 0, 0);
         }

         for(int var8 = 0; var8 < var3.getViewCount(); ++var8) {
            Shape var6 = var3.getChildAllocation(var8, var2);
            if (var6 != null) {
               View var7 = var3.getView(var8);
               if (this.printLandscapeView(var1, var6, var7)) {
                  var4 = true;
               }
            }
         }
      } else if (var2.getBounds().getMaxY() >= var5.getY() - 30.0D - 0.0D) {
         var4 = true;
         if (var2.getBounds().getHeight() > var5.getHeight() - 30.0D - 0.0D && var2.intersects(var5)) {
            if (var3.getElement().getAttributes().getAttribute(Attribute.CLASS) != null) {
               try {
                  paintedObjectCount = Integer.parseInt(var3.getElement().getAttributes().getAttribute(Attribute.CLASS).toString());
               } catch (Exception var12) {
                  Tools.eLog(var12, 0);
               }
            }

            if (var2.getBounds().getHeight() > 150.0D) {
               try {
                  Thread.sleep(500L);
               } catch (Exception var11) {
                  Tools.eLog(var11, 0);
               }
            }

            if (var2.getBounds().getY() >= 0.0D) {
               var3.paint(var1, var2);
            }
         } else if (var2.getBounds().getY() >= var5.getY() - 30.0D - 0.0D) {
            if (var2.getBounds().getMaxY() <= var5.getMaxY() - 30.0D - 0.0D) {
               if (var3.getElement().getAttributes().getAttribute(Attribute.CLASS) != null) {
                  try {
                     paintedObjectCount = Integer.parseInt(var3.getElement().getAttributes().getAttribute(Attribute.CLASS).toString());
                  } catch (Exception var10) {
                     Tools.eLog(var10, 0);
                  }
               }

               if (var2.getBounds().getHeight() > 150.0D) {
                  try {
                     Thread.sleep(500L);
                  } catch (Exception var9) {
                     Tools.eLog(var9, 0);
                  }
               }

               if (var2.getBounds().getY() >= 0.0D) {
                  var3.paint(var1, var2);
               }
            } else if (var2.getBounds().getY() < this.pageEndY) {
               this.pageEndY = var2.getBounds().getY();
            }
         }
      }

      return var4;
   }

   private void extraLandscapePrint(Graphics var1, int var2, double var3) {
      int var5 = (int)(9.0D / var3);
      Shape var6 = var1.getClip();
      var1.setClip((int)var6.getBounds().getX(), (int)var6.getBounds().getY(), (int)var6.getBounds().getWidth(), (int)var6.getBounds().getHeight());
      Font var7 = var1.getFont();
      Color var8 = var1.getColor();
      Font var9 = new Font(MainPrinter.fontName, 0, var5);
      var1.setColor(Color.BLACK);
      this.getFootText(var2);
      if (MainPrinter.kellFejlec) {
         if (var2 > 0 && this.headerVector.size() > 0) {
            var9 = this.getFont4String(var1, var9, (String)this.headerVector.elementAt(0));
            var1.setFont(var9);
            int var10 = SwingUtilities.computeStringWidth(var1.getFontMetrics(var9), (String)this.headerVector.elementAt(0)) / 2;
            var1.drawString((String)this.headerVector.elementAt(0), (int)((var1.getClip().getBounds().getWidth() - 40.0D) / 2.0D) - var10, -((int)(16.0D / var3)));
         }

         if (var2 == 0) {
            String var12 = getMaxLength(this.footerVector, new Vector());
            var9 = this.getFont4String(var1, var9, var12);
            var1.setFont(var9);

            for(int var11 = 0; var11 < this.footerVector.size(); ++var11) {
               var1.drawString((String)this.footerVector.elementAt(var11), 0, (int)var1.getClip().getBounds().getHeight() - SPACE4FOOTER + var11 * 20);
            }
         }
      }

      if (this.footerVector.size() > 0) {
         var9 = this.getFont4String(var1, var9, (String)this.footerVector.get(0));
      }

      var1.setFont(var9);
      var1.drawString(this.info, 0, (int)var1.getClip().getBounds().getHeight() - (int)(70.0D / var3));
      var1.setFont(var7);
      var1.setColor(var8);
      var1.setClip(var6);
   }
}
