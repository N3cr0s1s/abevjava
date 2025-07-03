package hu.piller.enykp.alogic.masterdata.envelope.print;

import hu.piller.enykp.alogic.masterdata.envelope.model.AddressOpt;
import hu.piller.enykp.alogic.masterdata.envelope.painter.EnvelopePainter;
import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.gui.framework.MainFrame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Rectangle;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.standard.Chromaticity;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.OrientationRequested;
import javax.swing.SwingUtilities;

public class EnvelopePrintJob implements Runnable, Printable {
   private AddressOpt felado;
   private AddressOpt cimzett;

   public EnvelopePrintJob(AddressOpt var1, AddressOpt var2) {
      this.felado = var1;
      this.cimzett = var2;
   }

   public void run() {
      this.doPrint();
   }

   public void doPrint() {
      boolean var1 = false;
      final String[] var2 = new String[1];
      HashPrintRequestAttributeSet var3 = new HashPrintRequestAttributeSet();
      var3.add(MediaSizeName.ISO_C5);
      var3.add(OrientationRequested.LANDSCAPE);
      var3.add(Chromaticity.MONOCHROME);
      PrinterJob var4 = PrinterJob.getPrinterJob();

      try {
         var4.setPrintable(this);
         var4.pageDialog(var4.getPageFormat(var3));
         if (var4.printDialog()) {
            var4.print(var3);
         }
      } catch (Exception var6) {
         var4.cancel();
         var1 = true;
         var2[0] = "Nyomtatási hiba: " + var6.getMessage();
      }

      if (var1) {
         SwingUtilities.invokeLater(new Runnable() {
            public void run() {
               GuiUtil.showMessageDialog(MainFrame.thisinstance, var2, "Nyomtatási hiba", 0);
            }
         });
      }

   }

   public int print(Graphics var1, PageFormat var2, int var3) throws PrinterException {
      if (var3 == 0) {
         Graphics2D var5 = (Graphics2D)var1;
         Rectangle var4;
         if (var2 == null) {
            GraphicsConfiguration var6 = var5.getDeviceConfiguration();
            var4 = var6.getBounds();
         } else {
            var5.translate(var2.getImageableX(), var2.getImageableY());
            var4 = new Rectangle(0, 0, (int)var2.getImageableWidth(), (int)var2.getImageableHeight());
         }

         EnvelopePainter var7 = new EnvelopePainter();
         var7.paintEnvelope(var5, var4, this.felado, this.cimzett);
         return 0;
      } else {
         return 1;
      }
   }
}
