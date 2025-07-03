package hu.piller.enykp.alogic.masterdata.sync.ui.maintenance;

import java.awt.Component;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Vector;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;
import javax.swing.table.TableCellRenderer;

public class JMDMultiLineHeaderRenderer extends JList implements TableCellRenderer {
   public JMDMultiLineHeaderRenderer() {
      this.setOpaque(true);
      this.setForeground(UIManager.getColor("TableHeader.foreground"));
      this.setBackground(UIManager.getColor("TableHeader.background"));
      this.setBorder(UIManager.getBorder("TableHeader.cellBorder"));
      ListCellRenderer var1 = this.getCellRenderer();
      ((JLabel)var1).setHorizontalAlignment(0);
      this.setCellRenderer(var1);
   }

   public Component getTableCellRendererComponent(JTable var1, Object var2, boolean var3, boolean var4, int var5, int var6) {
      this.setFont(var1.getFont());
      String var7 = var2 == null ? "" : var2.toString();
      BufferedReader var8 = new BufferedReader(new StringReader(var7));
      Vector var10 = new Vector();

      String var9;
      try {
         while((var9 = var8.readLine()) != null) {
            var10.addElement(var9);
         }
      } catch (IOException var12) {
         var12.printStackTrace();
      }

      this.setListData(var10);
      return this;
   }
}
