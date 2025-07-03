package hu.piller.enykp.alogic.masterdata.sync.ui.maintenance.technicalmd;

import hu.piller.enykp.gui.GuiUtil;
import java.awt.Component;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class ValueListMaintenanceRenderer implements TableCellRenderer {
   public Component getTableCellRendererComponent(JTable var1, Object var2, boolean var3, boolean var4, int var5, int var6) {
      JLabel var7 = new JLabel();
      var7.setFont(new Font("Dialog", 0, GuiUtil.getCommonFontSize()));
      var7.setText("<html><body><font color=\"RED\"><b>&nbsp;! </b></font>" + var1.getModel().getValueAt(var5, var6) + "</body></html>");
      var7.setToolTipText("Értéklista érkezett a NAV-tól, kérem kattintson!");
      var7.setOpaque(true);
      return var7;
   }
}
