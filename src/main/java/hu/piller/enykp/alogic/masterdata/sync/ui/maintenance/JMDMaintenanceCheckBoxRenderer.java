package hu.piller.enykp.alogic.masterdata.sync.ui.maintenance;

import hu.piller.enykp.gui.GuiUtil;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class JMDMaintenanceCheckBoxRenderer extends JRadioButton implements TableCellRenderer {
   public Component getTableCellRendererComponent(JTable var1, Object var2, boolean var3, boolean var4, int var5, int var6) {
      if (var2 == null) {
         JLabel var8 = new JLabel("");
         var8.setOpaque(true);
         return var8;
      } else {
         JCheckBox var7 = GuiUtil.getANYKCheckBox();
         var7.setHorizontalAlignment(0);
         var7.setOpaque(true);
         var7.setBorderPainted(true);
         if (var3) {
            var7.setBackground(var1.getSelectionBackground());
         } else {
            var7.setBackground(Color.WHITE);
         }

         var7.setSelected((Boolean)var2);
         return var7;
      }
   }
}
