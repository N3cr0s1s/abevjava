package hu.piller.enykp.alogic.kihatas;

import java.awt.Component;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellRenderer;

public class MultiLabelCellRenderer extends JPanel implements TableCellRenderer {
   public Component getTableCellRendererComponent(JTable var1, Object var2, boolean var3, boolean var4, int var5, int var6) {
      if (var3) {
         this.setForeground(var1.getSelectionForeground());
         this.setBackground(var1.getSelectionBackground());
      } else {
         this.setForeground(var1.getForeground());
         this.setBackground(var1.getBackground());
      }

      if (var4) {
         this.setBorder(UIManager.getBorder("Table.focusCellHighlightBorder"));
         if (var1.isCellEditable(var5, var6)) {
            this.setForeground(UIManager.getColor("Table.focusCellForeground"));
            this.setBackground(UIManager.getColor("Table.focusCellBackground"));
         }
      } else {
         this.setBorder(new EmptyBorder(1, 2, 1, 2));
      }

      this.removeAll();
      if (var2 instanceof MegallapitasVector) {
         MegallapitasVector var7 = (MegallapitasVector)var2;
         int var8 = this.get_nondeleted_elem_number(var7);
         if (var8 == 0) {
            return this;
         }

         this.setLayout(new GridLayout(var8, 1));

         for(int var9 = 0; var9 < var8; ++var9) {
            MegallapitasRecord var10 = (MegallapitasRecord)var7.get(var9);
            String var11 = ((MultiLineTable)var1).megallapitaslista.getDisplayTextByMsvoAzon(var10.getMsvo_azon());
            String var12 = var11 + "(" + var10.getAdonemkod() + ")";
            JLabel var13 = new JLabel(var12);
            var13.setOpaque(false);
            this.add(var13);
         }

         if (var1.getRowHeight(var5) < 20 * var8) {
            var1.setRowHeight(var5, 20 * var8);
         }
      }

      return this;
   }

   private int get_nondeleted_elem_number(MegallapitasVector var1) {
      int var2 = 0;

      for(int var3 = 0; var3 < var1.size(); ++var3) {
         MegallapitasRecord var4 = (MegallapitasRecord)var1.get(var3);
         if (!var4.isDeleted()) {
            ++var2;
         }
      }

      return var2;
   }
}
