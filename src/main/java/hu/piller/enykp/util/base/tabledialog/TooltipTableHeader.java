package hu.piller.enykp.util.base.tabledialog;

import java.awt.event.MouseEvent;
import javax.swing.JLabel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;

public class TooltipTableHeader extends JTableHeader {
   public TooltipTableHeader(TableColumnModel var1) {
      super(var1);
   }

   public String getToolTipText(MouseEvent var1) {
      int var2 = this.columnAtPoint(var1.getPoint());

      String var3;
      try {
         Object var4 = this.getColumnModel().getColumn(var2).getHeaderValue();
         if (var4 instanceof String) {
            var3 = (String)var4;
         } else if (var4 instanceof JLabel) {
            var3 = ((JLabel)var4).getText();
         } else {
            var3 = "";
         }
      } catch (Exception var5) {
         var3 = "";
      }

      if (var3.length() < 1) {
         var3 = super.getToolTipText(var1);
      }

      return var3 != null ? var3.replaceAll("\n", " ") : "";
   }
}
