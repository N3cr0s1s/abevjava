package hu.piller.enykp.util.base.errordialog;

import hu.piller.enykp.util.base.Tools;
import java.awt.Component;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

public class ErrorListCellRenderer extends JLabel implements ListCellRenderer {
   public Component getListCellRendererComponent(JList var1, Object var2, int var3, boolean var4, boolean var5) {
      if (var2 instanceof TextWithIcon) {
         this.setText(((TextWithIcon)var2).text);
         this.setIcon(((TextWithIcon)var2).ii);
      } else {
         try {
            this.setText(var2.toString());
            this.setIcon((Icon)null);
         } catch (Exception var7) {
            Tools.eLog(var7, 0);
         }
      }

      if (var4) {
         this.setBackground(var1.getSelectionBackground());
         this.setForeground(var1.getSelectionForeground());
      } else {
         this.setBackground(var1.getBackground());
         this.setForeground(var1.getForeground());
      }

      this.setEnabled(var1.isEnabled());
      this.setFont(var1.getFont());
      this.setOpaque(true);
      return this;
   }
}
