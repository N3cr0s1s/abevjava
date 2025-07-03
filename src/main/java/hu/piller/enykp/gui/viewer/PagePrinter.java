package hu.piller.enykp.gui.viewer;

import hu.piller.enykp.datastore.Elem;
import hu.piller.enykp.datastore.GUI_Datastore;
import hu.piller.enykp.gui.model.DataFieldModel;
import hu.piller.enykp.gui.model.FormModel;
import hu.piller.enykp.gui.model.PageModel;
import hu.piller.enykp.gui.model.VisualFieldModel;
import java.awt.Graphics;
import java.awt.LayoutManager;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.util.Vector;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;

public class PagePrinter extends JPanel implements Printable {
   PageModel PM;
   int dynindex;
   int max;
   Elem elem;
   double z = 1.0D;
   private boolean emptyPrint;

   public PagePrinter(PageModel var1, Elem var2, int var3, boolean var4) {
      this.PM = var1;
      this.elem = var2;
      this.max = var3;
      this.emptyPrint = var4;
      this.setLayout((LayoutManager)null);
   }

   public void setIndex(int var1) {
      this.dynindex = var1;
   }

   public int print(Graphics var1, PageFormat var2, int var3) throws PrinterException {
      if (this.max <= var3) {
         return 1;
      } else {
         this.print(var1);
         return 0;
      }
   }

   public void refresh() {
      Vector var1 = this.PM.y_sorted_df;
      GUI_Datastore var2 = this.getDatastore();

      for(int var3 = 0; var3 < var1.size(); ++var3) {
         DataFieldModel var4 = (DataFieldModel)var1.get(var3);
         String var5 = var2.get(this.dynindex + "_" + var4.key);
         if (var5 == null) {
            var5 = "";
         }

         if (this.emptyPrint) {
            var5 = "";
         }

         var4.value = var5;
      }

      this.repaint();
   }

   private GUI_Datastore getDatastore() {
      GUI_Datastore var1 = (GUI_Datastore)((Elem)this.PM.getFormModel().getBookModel().cc.getActiveObject()).getRef();
      return var1;
   }

   private void myprint(Graphics var1) {
      this.refresh();
      int var2 = this.PM.z_sorted_vf.size();

      int var3;
      for(var3 = 0; var3 < var2; ++var3) {
         ((VisualFieldModel)this.PM.z_sorted_vf.get(var3)).print(var1, (int)(this.z * 100.0D));
      }

      var2 = this.PM.y_sorted_df.size();

      for(var3 = 0; var3 < var2; ++var3) {
         DataFieldModel var4 = (DataFieldModel)this.PM.y_sorted_df.get(var3);
         String var5 = (String)var4.features.get("print_on_flp");
         boolean var6 = false;

         try {
            var6 = var5.equals("True");
         } catch (Exception var13) {
         }

         var5 = (String)var4.features.get("fill_on_fp");
         boolean var7 = false;

         try {
            var7 = var5.equals("True");
         } catch (Exception var12) {
         }

         String var8 = (String)var4.features.get("fill_on_lp");
         boolean var9 = false;

         try {
            var9 = var8.equals("True");
         } catch (Exception var11) {
         }

         if (!var6 || !var9 && !var7 || (!var7 || this.dynindex == 0) && (!var9 || this.dynindex == this.max - 1)) {
            var4.print(var1, (int)(this.z * 100.0D), var3, this.dynindex);
         }
      }

   }

   public void paint(Graphics var1) {
      super.paint(var1);
      this.myprint(var1);
   }

   public void cprint(DefaultTableModel var1) {
      Vector var2 = new Vector();
      var2.add("");
      var2.add("head");
      var2.add(this.PM.title + (this.PM.dynamic ? " ( " + (this.dynindex + 1) + " )" : ""));
      var1.addRow(var2);
      this.refresh();
      int var3 = this.PM.y_sorted_df.size();

      for(int var4 = 0; var4 < var3; ++var4) {
         DataFieldModel var5 = (DataFieldModel)this.PM.y_sorted_df.get(var4);
         String var6 = (String)var5.features.get("print_on_flp");
         boolean var7 = false;

         try {
            var7 = var6.equals("True");
         } catch (Exception var14) {
         }

         var6 = (String)var5.features.get("fill_on_fp");
         boolean var8 = false;

         try {
            var8 = var6.equals("True");
         } catch (Exception var13) {
         }

         String var9 = (String)var5.features.get("fill_on_lp");
         boolean var10 = false;

         try {
            var10 = var9.equals("True");
         } catch (Exception var12) {
         }

         if (!var7 || !var10 && !var8 || (!var8 || this.dynindex == 0) && (!var10 || this.dynindex == this.max - 1)) {
            FormModel var11 = this.PM.getFormModel();
            var2 = var5.cprint(var11, var4, this.dynindex);
            if (var2 != null) {
               var1.addRow(var2);
            }
         }
      }

   }
}
