package hu.piller.enykp.gui.viewer;

import hu.piller.enykp.datastore.Elem;
import hu.piller.enykp.gui.model.BookModel;
import hu.piller.enykp.gui.model.FormModel;
import java.awt.Graphics;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;

public class FormPrinter implements Printable {
   BookModel bm;
   int index;
   int pageindex;
   int dynindex;
   FormModel fm;
   Elem elem;
   DefaultTableModel dtm;

   public FormPrinter(BookModel var1) {
      this.bm = var1;
      this.setIndex(0);
   }

   public void setIndex(int var1) {
      this.index = var1;
      this.elem = (Elem)this.bm.cc.get(var1);
      this.fm = this.bm.get(this.elem.getType());
      Vector var2 = new Vector();
      var2.add("number");
      var2.add("prompt");
      var2.add("value");
      this.dtm = new DefaultTableModel(var2, 0);
   }

   public void setPageindex(int var1) {
      this.pageindex = var1;
   }

   public void setDynindex(int var1) {
      this.dynindex = var1;
   }

   public int print(Graphics var1, PageFormat var2, int var3) throws PrinterException {
      try {
         int[] var4 = (int[])((int[])this.elem.getEtc().get("pagecounts"));
         int var5 = var4[this.pageindex];
         PagePrinter var6 = new PagePrinter(this.fm.get(this.pageindex), this.elem, var5, this.bm.emptyprint);
         var6.setIndex(this.dynindex);
         return var6.print(var1, var2, this.dynindex);
      } catch (PrinterException var7) {
         return 1;
      }
   }

   public DefaultTableModel cprint(int var1, int var2) {
      int[] var3 = (int[])((int[])this.elem.getEtc().get("pagecounts"));
      int var4 = var3[var1];
      PagePrinter var5 = new PagePrinter(this.fm.get(var1), this.elem, var4, this.bm.emptyprint);
      var5.setIndex(var2);
      var5.cprint(this.dtm);
      return this.dtm;
   }

   public FormModel getFormModel() {
      return this.fm;
   }

   public Elem getElem() {
      return this.elem;
   }
}
