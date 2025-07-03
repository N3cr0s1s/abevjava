package hu.piller.enykp.print;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.PageSize;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;
import hu.piller.enykp.util.base.Tools;
import me.necrocore.abevjava.NecroFile;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Vector;
import javax.swing.JEditorPane;
import javax.swing.SwingUtilities;
import javax.swing.text.View;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.HTML.Attribute;

public class Jep2Pdf4Calculation {
   private static final int PAGE_FORMAT_WIDTH = 600;
   private static final int PAGE_FORMAT_HEIGHT = 850;
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
   protected String htmlData;
   protected int currentPage = -1;
   protected JEditorPane jeditorPane;
   protected double pageEndY = 0.0D;
   protected double pageStartY = 0.0D;
   protected boolean scaleWidthToFit = true;
   protected String timeString = "";
   protected PageFormat pFormat;
   private Vector footerVector;
   private Vector headerVector;
   private Vector headerVector2;
   private int index;
   private String info;
   int landscapeStartAt = 0;
   private static final int TEMP = 0;
   private static final int LANDSCAPE_SPACE4FOOTER = 30;

   public Jep2Pdf4Calculation(String var1) throws Exception {
      this.htmlData = var1;
      this.jeditorPane = new JEditorPane((new HTMLEditorKit()).getContentType(), var1);
      this.jeditorPane.setText(var1);
      Font var2 = this.readFont();
      GraphicsEnvironment var3 = GraphicsEnvironment.getLocalGraphicsEnvironment();
      var3.registerFont(var2);
      String var4 = "body {font-family: " + var2.getFamily() + "; font-size: 12pt;}";
      ((HTMLDocument)this.jeditorPane.getDocument()).getStyleSheet().addRule(var4);
      this.setFooterVector(new Vector());
      this.setHeaderVector(new Vector());
      this.setHeaderVector2(new Vector());
      this.pFormat = new PageFormat();
      this.timeString = Lap.getTimeString();
      paintedObjectCount = 0;
      Paper var5 = this.pFormat.getPaper();
      var5.setImageableArea(30.0D, 55.0D, 570.0D, 820.0D);
      this.pFormat.setPaper(var5);
      this.jeditorPane.setSize((int)this.pFormat.getImageableWidth(), Integer.MAX_VALUE);
      this.jeditorPane.validate();
      this.index = 0;
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

   public void setHeaderVector2(Vector var1) {
      this.headerVector2 = var1;
   }

   protected boolean printView(Graphics2D var1, Shape var2, View var3) {
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

   private Font getFont4String(Graphics var1, Font var2, String var3) {
      return this.getFont4String(var1, var2, var3, 0);
   }

   private Font getFont4String(Graphics var1, Font var2, String var3, int var4) {
      int var5 = SwingUtilities.computeStringWidth(var1.getFontMetrics(var2), var3);

      for(int var6 = var2.getSize(); (double)var5 > var1.getClip().getBounds().getWidth() - (double)var4 && var6 > 5; var5 = SwingUtilities.computeStringWidth(var1.getFontMetrics(var2), var3)) {
         --var6;
         var2 = new Font(PdfCreator.getPdfFontName(), 1, var6);
      }

      return var2;
   }

   public void printJep2Pdf(Document var1, PdfWriter var2, boolean var3) throws DocumentException, FileNotFoundException {
      Graphics2D var4;
      try {
         View var5 = this.jeditorPane.getUI().getRootView(this.jeditorPane);
         double var6 = 1.0D;
         double var8 = Math.max((double)this.jeditorPane.getWidth(), this.jeditorPane.getMinimumSize().getWidth());
         if (this.scaleWidthToFit && var8 > this.pFormat.getImageableWidth() - this.pFormat.getImageableX()) {
            var6 = (this.pFormat.getImageableWidth() - this.pFormat.getImageableX()) / var8;
         }

         if (PdfCreator.getCSeq() == 1) {
            --paintedObjectCount;
         }

         PdfContentByte var10;
         PdfTemplate var11;
         while(paintedObjectCount < PdfCreator.getCSeq() - 1) {
            var1.setPageSize(PageSize.A4);
            var1.newPage();
            var10 = var2.getDirectContent();
            if (this.index == 0) {
               var11 = var10.createTemplate((float)this.pFormat.getImageableWidth(), (float)(this.pFormat.getImageableHeight() + 40.0D));
               var4 = var11.createGraphics((float)this.pFormat.getImageableWidth(), (float)(this.pFormat.getImageableHeight() + 40.0D), AbevFontMapper.getInstance());
            } else {
               var11 = var10.createTemplate((float)this.pFormat.getImageableWidth(), (float)(this.pFormat.getImageableHeight() + (double)(40 - 20 * this.headerVector.size()) / var6));
               var4 = var11.createGraphics((float)this.pFormat.getImageableWidth(), (float)(this.pFormat.getImageableHeight() + (double)(40 - 20 * this.headerVector.size()) / var6), AbevFontMapper.getInstance());
            }

            if (var6 < 1.0D) {
               var4.scale(var6, var6);
            }

            var10 = var2.getDirectContent();
            if (this.index == 0) {
               var4.setClip((int)(this.pFormat.getImageableX() / var6), (int)(this.pFormat.getImageableY() / var6), (int)(this.pFormat.getImageableWidth() / var6), (int)(this.pFormat.getImageableHeight() / var6));
            } else {
               var4.setClip((int)(this.pFormat.getImageableX() / var6), (int)(this.pFormat.getImageableY() / var6), (int)(this.pFormat.getImageableWidth() / var6), (int)((this.pFormat.getImageableHeight() - (double)(20 * (this.headerVector2.size() + 1))) / var6));
            }

            if (this.index > this.currentPage) {
               this.currentPage = this.index;
               this.pageStartY += this.pageEndY;
               this.pageEndY = var4.getClipBounds().getHeight();
            }

            if (this.index == 0) {
               var4.translate(var4.getClipBounds().getX(), var4.getClipBounds().getY());
            } else {
               var4.translate(var4.getClipBounds().getX(), (double)(40 - 20 * this.headerVector.size()) + var4.getClipBounds().getY());
            }

            Rectangle var12 = new Rectangle(0, (int)(-this.pageStartY), (int)this.jeditorPane.getMinimumSize().getWidth(), (int)this.jeditorPane.getPreferredSize().getHeight());
            if (this.printView(var4, var12, var5)) {
               this.extraPrint(var4, this.index, var6);
               this.pageStartY += (double)htmlrowDuplicateCorrection;
            }

            ++this.index;
            var10.addTemplate(var11, 0.0F, 0.0F);
            if (PdfCreator.getCSeq() == 1) {
               ++paintedObjectCount;
            }
         }

         var10 = null;
         var11 = null;
      } catch (Exception var14) {
         Tools.exception2SOut(var14);
      }

      if (var3) {
         var4 = null;

         try {
            this.jeditorPane.setText("<html><body></body><html>");
         } catch (Exception var13) {
            Tools.eLog(var13, 0);
         }

         this.jeditorPane = null;
         this.htmlData = null;
      }

   }

   private void extraPrint(Graphics var1, int var2, double var3) {
      int var5 = (int)(40.0D / var3);
      int var6 = (int)(60.0D / var3);
      byte var7 = 10;
      Shape var8 = var1.getClip();
      var1.setClip((int)var8.getBounds().getX(), (int)var8.getBounds().getY() - 40, (int)var8.getBounds().getWidth(), (int)var8.getBounds().getHeight());
      Font var9 = var1.getFont();
      Color var10 = var1.getColor();
      Font var11 = new Font(PdfCreator.getPdfFontName(), 0, var7);
      var1.setColor(Color.BLACK);
      int var12;
      if (var2 > 0 && this.headerVector2.size() > 0) {
         var11 = this.getFont4String(var1, var11, (String)this.headerVector2.elementAt(0));
         var1.setFont(var11);
         var12 = SwingUtilities.computeStringWidth(var1.getFontMetrics(var11), (String)this.headerVector2.elementAt(0)) / 2;
         var1.drawString((String)this.headerVector2.elementAt(0), (int)((var1.getClip().getBounds().getWidth() - 40.0D) / 2.0D) - var12, -25);
      }

      if (var2 == 0) {
         var11 = this.getFont4String(var1, var11, (String)this.headerVector.elementAt(0));
         var1.setFont(var11);
         var12 = SwingUtilities.computeStringWidth(var1.getFontMetrics(var11), (String)this.headerVector.elementAt(0)) / 2;
         var1.drawString((String)this.headerVector.elementAt(0), (int)((var1.getClip().getBounds().getWidth() - 40.0D) / 2.0D) - var12, 115);
         var12 = SwingUtilities.computeStringWidth(var1.getFontMetrics(var11), (String)this.headerVector.elementAt(1)) / 2;
         var1.drawString((String)this.headerVector.elementAt(1), (int)((var1.getClip().getBounds().getWidth() - 40.0D) / 2.0D) - var12, 130);
      }

      var11 = this.getFont4String(var1, var11, (String)this.footerVector.get(0));
      var1.setFont(var11);
      var1.drawString((String)this.footerVector.elementAt(0), 0, (int)var1.getClip().getBounds().getHeight() - (var2 == 0 ? SPACE4FOOTER : var5));
      var1.drawString(var2 + 1 + ".lap", var1.getClip().getBounds().width - var6, (int)var1.getClip().getBounds().getHeight() - (var2 == 0 ? SPACE4FOOTER : var5));
      var1.setFont(var9);
      var1.setColor(var10);
      var1.setClip(var8);
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

   private Font readFont() throws Exception {
      String var1 = this.getClass().getProtectionDomain().getCodeSource().getLocation().toString();
      if (var1.toLowerCase().indexOf(".jar") > -1) {
         String var2 = "jar:" + var1 + "!/fonts/LiberationSerif-Regular.ttf";
         URL var3 = new URL(var2);
         JarURLConnection var4 = (JarURLConnection)var3.openConnection();
         InputStream var5 = var4.getInputStream();
         return Font.createFont(0, var5);
      } else {
         return Font.createFont(0, new NecroFile((var1 + "fonts/LiberationSerif-Regular.ttf").substring(6)));
      }
   }

   public void reInitAFPane(String var1) {
      this.currentPage = -1;
      this.pageEndY = 0.0D;
      this.pageStartY = 0.0D;
      this.htmlData = var1;
      this.jeditorPane.setText(var1);
      paintedObjectCount = 0;
      this.pFormat.setOrientation(0);
      com.lowagie.text.Rectangle var2 = PageSize.A4.rotate();
      Paper var3 = this.pFormat.getPaper();
      var3.setSize((double)(var2.getHeight() - 30.0F), (double)(var2.getWidth() - 30.0F));
      var3.setImageableArea(0.0D, 0.0D, (double)(var2.getHeight() - 30.0F), (double)(var2.getWidth() - 30.0F));
      this.pFormat.setPaper(var3);
      this.jeditorPane.setSize((int)var2.getWidth(), Integer.MAX_VALUE);
      this.jeditorPane.validate();
      this.landscapeStartAt = this.index;
   }

   public void reInitJOVPane(String var1) {
      this.currentPage = -1;
      this.pageEndY = 0.0D;
      this.pageStartY = 0.0D;
      this.htmlData = var1;
      this.jeditorPane.setText(var1);
      paintedObjectCount = 0;
      this.pFormat.setOrientation(1);
      Paper var2 = this.pFormat.getPaper();
      var2.setImageableArea(30.0D, 55.0D, 570.0D, 820.0D);
      this.pFormat.setPaper(var2);
      this.jeditorPane.setSize((int)this.pFormat.getImageableWidth(), Integer.MAX_VALUE);
      this.jeditorPane.validate();
   }

   public void printLandscapeJep2Pdf(Document var1, PdfWriter var2, boolean var3) throws DocumentException, FileNotFoundException {
      Graphics2D var4;
      try {
         View var5 = this.jeditorPane.getUI().getRootView(this.jeditorPane);
         double var6 = 1.0D;
         double var8 = Math.max((double)this.jeditorPane.getWidth(), this.jeditorPane.getMinimumSize().getWidth()) + 30.0D;
         if (this.scaleWidthToFit && var8 > this.pFormat.getImageableWidth() - this.pFormat.getImageableX()) {
            var6 = (this.pFormat.getImageableWidth() - this.pFormat.getImageableX()) / var8;
         }

         if (var6 > 1.0D) {
            var6 = 1.0D;
         }

         if (PdfCreator.getCSeq() == 1) {
            --paintedObjectCount;
         }

         PdfContentByte var10;
         PdfTemplate var11;
         while(paintedObjectCount < PdfCreator.getCSeq() - 1) {
            var1.setPageSize(PageSize.A4.rotate());
            var1.newPage();
            var10 = var2.getDirectContent();
            var11 = var10.createTemplate((float)this.pFormat.getImageableWidth(), (float)(this.pFormat.getImageableHeight() + 40.0D));
            var4 = var11.createGraphics((float)this.pFormat.getImageableWidth(), (float)(this.pFormat.getImageableHeight() + 40.0D), AbevFontMapper.getInstance());
            if (var6 < 1.0D) {
               var4.scale(var6, var6);
            }

            var4.setClip((int)(this.pFormat.getImageableX() / var6), (int)(this.pFormat.getImageableY() / var6), (int)(this.pFormat.getImageableWidth() / var6), (int)(this.pFormat.getImageableHeight() / var6));
            if (this.index > this.currentPage) {
               this.currentPage = this.index;
               this.pageStartY += this.pageEndY;
               this.pageEndY = var4.getClipBounds().getHeight();
            }

            if (this.index == this.landscapeStartAt) {
               var4.translate(30, 70);
            } else {
               var4.translate(30, 70);
            }

            Rectangle var12 = new Rectangle(0, (int)(-this.pageStartY), (int)this.jeditorPane.getMinimumSize().getWidth(), (int)this.jeditorPane.getPreferredSize().getHeight());
            if (this.printLandscapeView(var4, var12, var5)) {
               this.extraLandscapePrint(var4, this.index, var6);
            }

            ++this.index;
            var10.addTemplate(var11, 0.0F, 0.0F);
            if (PdfCreator.getCSeq() == 1) {
               ++paintedObjectCount;
            }
         }

         var10 = null;
         var11 = null;
      } catch (Exception var14) {
         Tools.exception2SOut(var14);
      }

      if (var3) {
         var4 = null;

         try {
            this.jeditorPane.setText("<html><body></body><html>");
         } catch (Exception var13) {
            Tools.eLog(var13, 0);
         }

         this.jeditorPane = null;
         this.htmlData = null;
      }

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
      Font var9 = new Font(PdfCreator.getPdfFontName(), 0, var5);
      var1.setColor(Color.BLACK);
      if (this.headerVector2.size() > 0) {
         var9 = this.getFont4String(var1, var9, (String)this.headerVector2.elementAt(0));
         var1.setFont(var9);
         int var10 = SwingUtilities.computeStringWidth(var1.getFontMetrics(var9), (String)this.headerVector2.elementAt(0)) / 2;
         var1.drawString((String)this.headerVector2.elementAt(0), (int)((var1.getClip().getBounds().getWidth() - 40.0D) / 2.0D) - var10, -((int)(16.0D / var3)));
      }

      var9 = this.getFont4String(var1, var9, (String)this.footerVector.get(0));
      var1.setFont(var9);
      var1.drawString((String)this.footerVector.elementAt(0), 0, (int)var1.getClip().getBounds().getHeight() - (int)(70.0D / var3));
      var1.drawString(var2 + 1 + ".lap", var1.getClip().getBounds().width - (int)(65.0D / var3), (int)var1.getClip().getBounds().getHeight() - (int)(70.0D / var3));
      var1.setFont(var7);
      var1.setColor(var8);
      var1.setClip(var6);
   }

   public void printLandscapeJep2PdfRotate(Document var1, PdfWriter var2, boolean var3) throws DocumentException, FileNotFoundException {
      Graphics2D var4;
      try {
         View var5 = this.jeditorPane.getUI().getRootView(this.jeditorPane);
         double var6 = 1.0D;
         double var8 = Math.max((double)this.jeditorPane.getWidth(), this.jeditorPane.getMinimumSize().getWidth()) + 30.0D;
         if (this.scaleWidthToFit && var8 > this.pFormat.getImageableWidth() - this.pFormat.getImageableX()) {
            var6 = (this.pFormat.getImageableWidth() - this.pFormat.getImageableX()) / var8;
         }

         if (var6 > 1.0D) {
            var6 = 1.0D;
         }

         if (PdfCreator.getCSeq() == 1) {
            --paintedObjectCount;
         }

         PdfContentByte var10;
         PdfTemplate var11;
         while(paintedObjectCount < PdfCreator.getCSeq() - 1) {
            var1.newPage();
            var10 = var2.getDirectContent();
            var11 = var10.createTemplate((float)this.pFormat.getImageableWidth(), (float)(this.pFormat.getImageableHeight() + 40.0D));
            var4 = var11.createGraphics((float)this.pFormat.getImageableWidth(), (float)(this.pFormat.getImageableHeight() + 40.0D), AbevFontMapper.getInstance());
            var10.transform(AffineTransform.getTranslateInstance(this.pFormat.getImageableHeight() + 20.0D, 0.0D));
            var10.transform(AffineTransform.getRotateInstance(1.5707963267948966D));
            if (var6 < 1.0D) {
               var4.scale(var6, var6);
            }

            var4.setClip((int)(this.pFormat.getImageableX() / var6), (int)(this.pFormat.getImageableY() / var6), (int)(this.pFormat.getImageableWidth() / var6), (int)(this.pFormat.getImageableHeight() / var6));
            if (this.index > this.currentPage) {
               this.currentPage = this.index;
               this.pageStartY += this.pageEndY;
               this.pageEndY = var4.getClipBounds().getHeight();
            }

            if (this.index == this.landscapeStartAt) {
               var4.translate(30, 90);
            } else {
               var4.translate(30, 90);
            }

            Rectangle var12 = new Rectangle(0, (int)(-this.pageStartY), (int)this.jeditorPane.getMinimumSize().getWidth(), (int)this.jeditorPane.getPreferredSize().getHeight());
            if (this.printLandscapeViewRotate(var4, var12, var5)) {
               this.extraLandscapePrintRotate(var4, this.index, var6);
            }

            ++this.index;
            var10.addTemplate(var11, 0.0F, 0.0F);
            if (PdfCreator.getCSeq() == 1) {
               ++paintedObjectCount;
            }
         }

         var10 = null;
         var11 = null;
      } catch (Exception var14) {
         Tools.exception2SOut(var14);
      }

      if (var3) {
         var4 = null;

         try {
            this.jeditorPane.setText("<html><body></body><html>");
         } catch (Exception var13) {
            Tools.eLog(var13, 0);
         }

         this.jeditorPane = null;
         this.htmlData = null;
      }

   }

   protected boolean printLandscapeViewRotate(Graphics2D var1, Shape var2, View var3) {
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
               if (this.printLandscapeViewRotate(var1, var6, var7)) {
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

   private void extraLandscapePrintRotate(Graphics var1, int var2, double var3) {
      int var5 = (int)(9.0D / var3);
      Shape var6 = var1.getClip();
      var1.setClip((int)var6.getBounds().getX(), (int)var6.getBounds().getY(), (int)var6.getBounds().getWidth(), (int)var6.getBounds().getHeight());
      Font var7 = var1.getFont();
      Color var8 = var1.getColor();
      Font var9 = new Font(PdfCreator.getPdfFontName(), 0, var5);
      var1.setColor(Color.BLACK);
      if (this.headerVector2.size() > 0) {
         var9 = this.getFont4String(var1, var9, (String)this.headerVector2.elementAt(0));
         var1.setFont(var9);
         int var10 = SwingUtilities.computeStringWidth(var1.getFontMetrics(var9), (String)this.headerVector2.elementAt(0)) / 2;
         var1.drawString((String)this.headerVector2.elementAt(0), (int)((var1.getClip().getBounds().getWidth() - 40.0D) / 2.0D) - var10, -20);
      }

      var9 = this.getFont4String(var1, var9, (String)this.footerVector.get(0));
      var1.setFont(var9);
      var1.drawString((String)this.footerVector.elementAt(0), 0, (int)var1.getClip().getBounds().getHeight() - 100);
      var1.drawString(var2 + 1 + ".lap", var1.getClip().getBounds().width - 80, (int)var1.getClip().getBounds().getHeight() - 100);
      var1.setFont(var7);
      var1.setColor(var8);
      var1.setClip(var6);
   }
}
