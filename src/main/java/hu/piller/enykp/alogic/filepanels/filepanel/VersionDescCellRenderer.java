package hu.piller.enykp.alogic.filepanels.filepanel;

import hu.piller.enykp.alogic.fileutil.DatastoreKeyToXml;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class VersionDescCellRenderer implements TableCellRenderer {
   public Component getTableCellRendererComponent(JTable var1, Object var2, boolean var3, boolean var4, int var5, int var6) {
      JLabel var7 = new JLabel();
      var7.setFont(var1.getFont());
      if (var3) {
         var7.setOpaque(true);
         var7.setBackground(new Color(184, 207, 229));
      }

      String var8 = (String)var1.getModel().getValueAt(var5, 12);
      if (var8 != null && !"".equals(var8)) {
         var7.setForeground(Color.BLUE);
         String var9 = null;

         try {
            var9 = DatastoreKeyToXml.htmlConvert(var8);
         } catch (Exception var11) {
            var9 = var8;
         }

         var7.setText("<HTML><FONT face=Dialog><U>" + var9 + "</U></FONT></HTML>");
      } else {
         var7.setText("Nincs megadva");
      }

      return var7;
   }
}
