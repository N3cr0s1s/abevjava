package hu.piller.enykp.print;

import java.awt.print.Book;
import java.util.Vector;

public class TempObject4SuperPages {
   private boolean normalPrintAfterKPrint = false;
   private Vector<Lap> uniqueDataPages = new Vector();
   private Vector<Book> uniquePrintablePages = new Vector();

   public boolean isNormalPrintAfterKPrint() {
      return this.normalPrintAfterKPrint;
   }

   public void setNormalPrintAfterKPrint(boolean var1) {
      this.normalPrintAfterKPrint = var1;
   }

   public Vector<Lap> getUniqueDataPages() {
      return this.uniqueDataPages;
   }

   public void setUniqueDataPages(Vector<Lap> var1) {
      this.uniqueDataPages = var1;
   }

   public Vector<Book> getUniquePrintablePages() {
      return this.uniquePrintablePages;
   }

   public void setUniquePrintablePages(Vector<Book> var1) {
      this.uniquePrintablePages = var1;
   }
}
