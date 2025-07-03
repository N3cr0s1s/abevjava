package hu.piller.enykp.alogic.primaryaccount.envelopeprinter;

import hu.piller.enykp.interfaces.IPrintSupport;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;

public class PrintableEnvelope implements Printable {
   private IPrintSupport print_support;

   public void setPrintSupport(IPrintSupport var1) {
      this.print_support = var1;
   }

   public int print(Graphics var1, PageFormat var2, int var3) throws PrinterException {
      if (this.print_support != null && var3 == 0) {
         try {
            if (((Graphics2D)var1).getDeviceConfiguration().getBounds().width > 1) {
               this.print_support.printTo(var1, var2);
            }

            return 0;
         } catch (Exception var5) {
            EnvelopePrinter.writeError("Hiba történt nyomtatás közben !", var5);
            throw new PrinterException("Hiba történt nyomtatás közben ! (" + var5 + ")");
         }
      } else {
         return 1;
      }
   }
}
