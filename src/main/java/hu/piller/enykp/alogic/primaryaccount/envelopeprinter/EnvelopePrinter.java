package hu.piller.enykp.alogic.primaryaccount.envelopeprinter;

import hu.piller.enykp.interfaces.IPrint;
import hu.piller.enykp.interfaces.IPrintSupport;
import hu.piller.enykp.util.base.ErrorList;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.standard.Chromaticity;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.OrientationRequested;

public class EnvelopePrinter implements IPrint {
   static final String RESOURCE_NAME = "ABEV Boríték nyomtató";
   static final Long RESOURCE_ERROR_ID = new Long(0L);
   private static EnvelopePrinter instance;

   public static EnvelopePrinter getInstance() {
      if (instance == null) {
         instance = new EnvelopePrinter();
      }

      return instance;
   }

   private EnvelopePrinter() {
   }

   public void initialize(Object var1) {
   }

   public void print(IPrintSupport var1, Object var2) {
      PrintableEnvelope var3 = new PrintableEnvelope();
      HashPrintRequestAttributeSet var4 = new HashPrintRequestAttributeSet();
      var4.add(MediaSizeName.ISO_C5);
      var4.add(OrientationRequested.LANDSCAPE);
      var4.add(Chromaticity.MONOCHROME);
      PrinterJob var5 = PrinterJob.getPrinterJob();

      try {
         var5.setPrintable(var3);
         var5.pageDialog(var5.getPageFormat(var4));
         if (var5.printDialog()) {
            var5.print(var4);
         }
      } catch (PrinterException var7) {
         var5.cancel();
         writeError("Hiba történt nyomtatás közben !", var7);
      }

   }

   public static void writeError(String var0, Exception var1) {
      ErrorList.getInstance().writeError(RESOURCE_ERROR_ID, "ABEV Boríték nyomtató: " + var0, var1, (Object)null);
   }
}
