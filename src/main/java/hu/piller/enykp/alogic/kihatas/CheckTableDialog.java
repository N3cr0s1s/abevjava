package hu.piller.enykp.alogic.kihatas;

import hu.piller.enykp.gui.framework.MainFrame;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableRowSorter;

public class CheckTableDialog extends JDialog {
   DefaultTableModel dtm;
   JTable table;

   public CheckTableDialog(DefaultTableModel var1) {
      super(MainFrame.thisinstance, "Kihatáshoz a megállapítások - adónemek kiválasztása", true);
      this.dtm = var1;
      TableRowSorter var2 = new TableRowSorter(var1);
      JPanel var3 = new JPanel(new BorderLayout());
      this.table = new JTable(var1) {
         public Class<?> getColumnClass(int var1) {
            return var1 == 0 ? Boolean.class : super.getColumnClass(var1);
         }

         public boolean isCellEditable(int var1, int var2) {
            return var2 == 0;
         }

         public Component prepareRenderer(TableCellRenderer var1, int var2, int var3) {
            Component var4 = super.prepareRenderer(var1, var2, var3);
            if (var3 == 0) {
               return var4;
            } else {
               DefaultTableCellRenderer var5 = (DefaultTableCellRenderer)var4;
               if (var3 == 1) {
                  var5.setHorizontalAlignment(2);
               } else {
                  var5.setHorizontalAlignment(0);
               }

               return var5;
            }
         }
      };
      this.table.getColumnModel().removeColumn(this.table.getColumnModel().getColumn(3));
      this.table.setRowSorter(var2);
      this.table.getColumnModel().getColumn(0).setMinWidth(40);
      this.table.getColumnModel().getColumn(0).setWidth(40);
      this.table.getColumnModel().getColumn(0).setMaxWidth(40);
      this.table.getColumnModel().getColumn(0).setPreferredWidth(40);
      this.table.getColumnModel().getColumn(0).setResizable(false);
      this.table.getColumnModel().getColumn(2).setMinWidth(100);
      this.table.getColumnModel().getColumn(2).setMaxWidth(100);
      this.table.getColumnModel().getColumn(2).setWidth(100);
      this.table.getColumnModel().getColumn(2).setPreferredWidth(100);
      this.table.getTableHeader().setReorderingAllowed(false);
      this.table.addMouseListener(new MouseAdapter() {
         public void mouseClicked(MouseEvent var1) {
            if (var1.getClickCount() == 2) {
               int var2 = CheckTableDialog.this.table.getSelectedRow();
               CheckTableDialog.this.table.setValueAt(new Boolean(!(Boolean)CheckTableDialog.this.table.getValueAt(var2, 0)), var2, 0);
            }

         }
      });
      var3.add(new JScrollPane(this.table));
      JPanel var4 = new JPanel();
      var4.setLayout(new BoxLayout(var4, 2));
      var4.setBorder(new EmptyBorder(5, 5, 5, 5));
      var4.add(Box.createHorizontalGlue());
      JButton var5 = new JButton("Mégsem");
      JButton var6 = new JButton("Ok");
      var4.add(var6);
      var4.add(Box.createHorizontalStrut(10));
      var4.add(var5);
      var5.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            CheckTableDialog.this.done_cancel();
         }
      });
      var6.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            CheckTableDialog.this.done_ok();
         }
      });
      var3.add(var4, "South");
      this.getContentPane().add(var3);
      this.setDefaultCloseOperation(0);
      this.setSize(490, 400);
      this.setLocationRelativeTo(MainFrame.thisinstance);
      this.setVisible(true);
   }

   private void done_ok() {
      if (this.noone()) {
         JOptionPane.showMessageDialog(this, "Legalább egy sort ki kellválasztani.", "Hibaüzenet", 0);
      } else {
         this.setVisible(false);
      }
   }

   private boolean noone() {
      for(int var1 = 0; var1 < this.dtm.getRowCount(); ++var1) {
         if ((Boolean)this.dtm.getValueAt(var1, 0)) {
            return false;
         }
      }

      return true;
   }

   private void done_cancel() {
      this.dtm.setValueAt((Object)null, 0, 0);
      this.setVisible(false);
   }
}
