package hu.piller.enykp.alogic.masterdata.sync.ui.response;

import java.awt.Component;
import java.awt.Font;
import java.awt.font.TextAttribute;
import java.util.Hashtable;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.TableCellRenderer;

public class JMDMaintenanceButtonRenderer implements TableCellRenderer {
   private static final Hashtable<TextAttribute, Object> fontProperties = new Hashtable();

   public Component getTableCellRendererComponent(JTable var1, Object var2, boolean var3, boolean var4, int var5, int var6) {
      if (!"".equals(var2) && !"nincs eltérés".equals(var2)) {
         JButton var8 = new JButton();
         var8.setOpaque(true);
         if (var3) {
            var8.setForeground(var1.getSelectionForeground());
            var8.setBackground(var1.getSelectionBackground());
         } else {
            var8.setForeground(var1.getForeground());
            var8.setBackground(UIManager.getColor("Button.background"));
         }

         var8.setText(var2 == null ? "" : var2.toString());
         return var8;
      } else {
         JLabel var7 = new JLabel((String)var2);
         var7.setFont((new Font(var7.getFont().getName(), 0, 11)).deriveFont(fontProperties));
         var7.setHorizontalAlignment(0);
         var7.setOpaque(true);
         if (var3) {
            var7.setForeground(var1.getSelectionForeground());
            var7.setBackground(var1.getSelectionBackground());
         } else {
            var7.setForeground(var1.getForeground());
            var7.setBackground(var1.getBackground());
         }

         return var7;
      }
   }
}
