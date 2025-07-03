package hu.piller.enykp.alogic.masterdata.sync.ui;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.ExceptionConverter;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.Phrase;
import com.lowagie.text.html.simpleparser.HTMLWorker;
import com.lowagie.text.html.simpleparser.StyleSheet;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;
import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.gui.framework.MainFrame;
import hu.piller.enykp.util.base.PropertyList;
import me.necrocore.abevjava.NecroFile;
import me.necrocore.abevjava.NecroFileOutputStream;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.sql.Date;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Locale;
import javax.swing.JFileChooser;

public class PdfExport {
   private static final char NEWLINE = '\n';
   private static final DateFormat HUNGARIAN_DATE_FORMAT = DateFormat.getDateTimeInstance(3, 2, new Locale("hu", "HU"));
   private String errMsg;
   private Document document;
   private HTMLWorker htmlWorker;

   public String getErrMsg() {
      return this.errMsg;
   }

   public boolean print(String var1, String var2) {
      boolean var3 = true;
      JFileChooser var4 = new JFileChooser(PropertyList.getInstance().get("prop.usr.root") + File.separator + "naplo");
      var4.setDialogTitle("Nyomtatás pdf-be");
      var4.setSelectedFile(new NecroFile(var1 + ".pdf"));
      int var5 = var4.showSaveDialog(MainFrame.thisinstance);
      FileOutputStream var6 = null;
      if (var5 == 0) {
         try {
            var6 = new NecroFileOutputStream(var4.getSelectedFile());
            this.printHtmlToPdfStream(var2, var6);
            GuiUtil.showMessageDialog(MainFrame.thisinstance, "A(z) " + var4.getSelectedFile() + " pdf fájl elkészült!", "Tájékoztatás", 1);
         } catch (Exception var16) {
            var3 = false;
            this.errMsg = var16.getMessage();
            GuiUtil.showMessageDialog(MainFrame.thisinstance, "Pdf fájl készítési hiba!\n" + this.errMsg, "Hiba", 0);
         } finally {
            if (var6 != null) {
               try {
                  var6.close();
               } catch (Exception var15) {
               }
            }

         }
      }

      return var3;
   }

   public void openPdfForHtml(String var1) {
      JFileChooser var2 = new JFileChooser(PropertyList.getInstance().get("prop.usr.root") + File.separator + "naplo");
      var2.setDialogTitle("Nyomtatás pdf-be");
      var2.setSelectedFile(new NecroFile(var1 + ".pdf"));
      int var3 = var2.showSaveDialog(MainFrame.thisinstance);
      FileOutputStream var4 = null;
      if (var3 == 0) {
         try {
            var4 = new NecroFileOutputStream(var2.getSelectedFile());
            this.document = new Document();
            PdfWriter var5 = PdfWriter.getInstance(this.document, var4);
            var5.setPageEvent(new PdfExport.TableHeader());
            this.document.open();
            this.htmlWorker = new HTMLWorker(this.document);
         } catch (Exception var14) {
            this.errMsg = var14.getMessage();
            GuiUtil.showMessageDialog(MainFrame.thisinstance, "Pdf fájl készítési hiba!\n" + this.errMsg, "Hiba", 0);
         } finally {
            if (var4 != null) {
               try {
                  var4.close();
               } catch (Exception var13) {
               }
            }

         }
      }

   }

   public String open(String var1) throws Exception {
      JFileChooser var2 = new JFileChooser(PropertyList.getInstance().get("prop.usr.root") + File.separator + "naplo");
      var2.setMultiSelectionEnabled(false);
      var2.setDialogTitle("Nyomtatás pdf-be");
      var2.setSelectedFile(new NecroFile(var1 + ".pdf"));
      int var3 = var2.showSaveDialog(MainFrame.thisinstance);
      FileOutputStream var4 = null;
      if (var3 == 0) {
         try {
            var4 = new NecroFileOutputStream(var2.getSelectedFile());
         } catch (Exception var6) {
            this.errMsg = var6.getMessage();
            GuiUtil.showMessageDialog(MainFrame.thisinstance, "Pdf fájl készítési hiba!\n" + this.errMsg, "Hiba", 0);
            return null;
         }

         this.document = new Document();
         PdfWriter var5 = PdfWriter.getInstance(this.document, var4);
         var5.setPageEvent(new PdfExport.TableHeader());
         this.document.open();
         this.document.addTitle("NAV törzsadat szinkronizáció egyeztető ív");
         this.document.addAuthor("Nemzeti Adó és Vámhivatal");
         this.document.addCreator("NAV Általános Nyomtatványkitöltő 3.44.0");
         this.document.addCreationDate();
         this.htmlWorker = new HTMLWorker(this.document);
         return var2.getSelectedFile().getAbsolutePath();
      } else {
         return null;
      }
   }

   public void addHtmlFragmentToPdf(String var1) throws Exception {
      HTMLWorker var10000 = this.htmlWorker;
      ArrayList var2 = HTMLWorker.parseToList(new StringReader(var1), this.getStyleSheet());

      for(int var3 = 0; var3 < var2.size(); ++var3) {
         this.document.add((Element)var2.get(var3));
      }

      this.document.newPage();
   }

   public void close() {
      this.document.close();
   }

   private void printHtmlToPdfStream(String var1, OutputStream var2) throws Exception {
      Document var3 = new Document();
      PdfWriter var4 = PdfWriter.getInstance(var3, var2);
      var4.setPageEvent(new PdfExport.TableHeader());
      var3.open();
      new HTMLWorker(var3);
      ArrayList var5 = HTMLWorker.parseToList(new StringReader(var1), this.getStyleSheet());

      for(int var6 = 0; var6 < var5.size(); ++var6) {
         var3.add((Element)var5.get(var6));
      }

      var3.close();
   }

   private StyleSheet getStyleSheet() {
      FontFactory.register("fonts/LiberationSerif-Regular.ttf", "sans");
      StyleSheet var1 = new StyleSheet();
      var1.loadTagStyle("body", "face", "sans");
      var1.loadTagStyle("body", "encoding", "Identity-H");
      var1.loadTagStyle("body", "leading", "10,0");
      return var1;
   }

   class TableHeader extends PdfPageEventHelper {
      String header;
      PdfTemplate total;

      public void setHeader(String var1) {
         this.header = var1;
      }

      public void onOpenDocument(PdfWriter var1, Document var2) {
         this.total = var1.getDirectContent().createTemplate(30.0F, 16.0F);
      }

      public void onEndPage(PdfWriter var1, Document var2) {
         PdfPTable var3 = new PdfPTable(3);

         try {
            var3.setWidths(new int[]{24, 24, 2});
            var3.setTotalWidth(527.0F);
            var3.setLockedWidth(true);
            var3.getDefaultCell().setFixedHeight(20.0F);
            var3.getDefaultCell().setBorder(0);
            var3.addCell(this.header);
            var3.getDefaultCell().setHorizontalAlignment(2);
            var3.addCell(String.format("Készült : %s   oldal: %d / ", PdfExport.HUNGARIAN_DATE_FORMAT.format(new Date(System.currentTimeMillis())), var1.getPageNumber()));
            PdfPCell var4 = new PdfPCell(Image.getInstance(this.total));
            var4.setBorder(0);
            var3.addCell(var4);
            var3.writeSelectedRows(0, -1, 15.0F, 35.0F, var1.getDirectContent());
         } catch (DocumentException var5) {
            throw new ExceptionConverter(var5);
         }
      }

      public void onCloseDocument(PdfWriter var1, Document var2) {
         ColumnText.showTextAligned(this.total, 0, new Phrase(String.valueOf(var1.getPageNumber() - 1)), 2.0F, 2.0F, 0.0F);
      }
   }
}
