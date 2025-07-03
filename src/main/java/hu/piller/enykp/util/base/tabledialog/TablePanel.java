package hu.piller.enykp.util.base.tabledialog;

import hu.piller.enykp.alogic.filepanels.tablesorter.TableSorter;
import hu.piller.enykp.gui.GuiUtil;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Vector;
import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

public class TablePanel extends JPanel {
   private static final String HUN_CHARSET = "ISO-8859-2";
   private File file;
   private String[] head_titles;
   private String field_separator;
   private String charset;
   private boolean is_move_last;
   private boolean is_initialized;
   private JTable table;
   private TableSorter table_sorter;
   private JButton ok_button;

   public TablePanel() {
      this.setFileCharset("ISO-8859-2");
      this.initialize();
   }

   public TablePanel(File var1) {
      this.setFile(var1);
      this.setFileCharset("ISO-8859-2");
      this.initialize();
   }

   public TablePanel(File var1, String[] var2) {
      this.setFile(var1);
      this.setHeadTitles(var2);
      this.setFileCharset("ISO-8859-2");
      this.initialize();
   }

   public TablePanel(File var1, String[] var2, String var3) {
      this.setFile(var1);
      this.setHeadTitles(var2);
      this.setFieldSeparator(var3);
      this.setFileCharset("ISO-8859-2");
      this.initialize();
   }

   public TablePanel(File var1, String[] var2, String var3, String var4) {
      this.setFile(var1);
      this.setHeadTitles(var2);
      this.setFieldSeparator(var3);
      this.setFileCharset(var4);
      this.initialize();
   }

   public TablePanel(File var1, String[] var2, String var3, String var4, boolean var5) {
      this.setFile(var1);
      this.setHeadTitles(var2);
      this.setFieldSeparator(var3);
      this.setFileCharset(var4);
      this.setMoveLast(var5);
      this.initialize();
   }

   private void initialize() {
      this.build();
      this.prepare();
      this.is_initialized = true;
      this.rebuild();
      GuiUtil.setTableColWidth(this.table);
   }

   public void setFile(File var1) {
      if (var1 != null && var1.exists()) {
         this.file = var1;
         this.rebuild();
      }

   }

   public void setHeadTitles(String[] var1) {
      if (var1 != null) {
         this.head_titles = (String[])((String[])var1.clone());
      } else {
         this.head_titles = null;
      }

      this.rebuild();
   }

   public void setFieldSeparator(String var1) {
      this.field_separator = var1;
      this.rebuild();
   }

   public void setFileCharset(String var1) {
      this.charset = var1;
   }

   public void setMoveLast(boolean var1) {
      this.is_move_last = var1;
   }

   public void rebuild() {
      if (this.is_initialized) {
         this.table_sorter.detachTable();
         JTableHeader var1 = this.table.getTableHeader();
         DefaultTableModel var2 = (DefaultTableModel)this.table.getModel();
         if (var1 != null) {
            var1.removeAll();
         }

         if (var2 != null) {
            var2.getDataVector().clear();
         }

         if (var2 == null) {
            var2 = new DefaultTableModel();
         }

         if (this.file != null && this.file.exists()) {
            this.read(var2);
         }

         var2.setColumnIdentifiers(this.head_titles);
         this.table.setModel(var2);
         this.table.getTableHeader().setVisible(true);
         if (this.is_move_last) {
            this.table.scrollRectToVisible(this.table.getCellRect(this.table.getRowCount() - 1, 0, true));
         }

         this.table_sorter.attachTable(this.table);
         this.table_sorter.setSortEnabled(true);
      }

   }

   private void build() {
      JTable var1 = new JTable(new TablePanel.TablePanelTableModel());
      var1.setTableHeader(new TooltipTableHeader(var1.getColumnModel()));
      var1.setAutoResizeMode(0);
      if (GuiUtil.modGui()) {
         var1.setRowHeight(GuiUtil.getCommonItemHeight() + 2);
      }

      JScrollPane var2 = new JScrollPane(var1);
      JButton var3 = new JButton("OK");
      JPanel var4 = new JPanel();
      var4.setLayout(new FlowLayout(2, 3, 5));
      var4.add(var3);
      this.setLayout(new BorderLayout());
      this.add(Box.createHorizontalStrut(3), "West");
      this.add(Box.createHorizontalStrut(3), "East");
      this.add(Box.createVerticalStrut(3), "North");
      this.add(var2, "Center");
      this.add(var4, "South");
      this.table = var1;
      this.ok_button = var3;
   }

   private void prepare() {
      this.table_sorter = new TableSorter();
      this.table_sorter.attachTable(this.table);
      this.table_sorter.setSortEnabled(true);
      this.table.addMouseMotionListener(new MouseMotionListener() {
         private Object v;

         public void mouseDragged(MouseEvent var1) {
            this.v = null;
         }

         public void mouseMoved(MouseEvent var1) {
            Point var2 = var1.getPoint();
            int var3 = TablePanel.this.table.columnAtPoint(var2);

            try {
               Object var4 = TablePanel.this.table.getValueAt(TablePanel.this.table.rowAtPoint(var2), var3);
               if (var4 != this.v) {
                  this.v = var4;
                  TableCellRenderer var5 = TablePanel.this.table.getDefaultRenderer(String.class);
                  if (var5 instanceof JLabel) {
                     JLabel var6 = (JLabel)var5;
                     String var7;
                     if (this.v instanceof JButton) {
                        var7 = ((JButton)this.v).getText();
                     } else {
                        var7 = this.v.toString();
                     }

                     if (var7 != null && !(this.v instanceof Icon)) {
                        var6.setText(var7);
                        double var8 = var6.getPreferredSize().getWidth();
                        double var10 = (double)TablePanel.this.table.getColumnModel().getColumn(var3).getWidth();
                        var10 -= 2.0D;
                        if (this.v instanceof JButton) {
                           Insets var12 = ((JButton)this.v).getMargin();
                           Icon var13 = ((JButton)this.v).getIcon();
                           int var14 = ((JButton)this.v).getIconTextGap();
                           var10 -= (double)(var12.left + var12.right + var14 + (var13 == null ? 0 : var13.getIconWidth()));
                        }

                        if (var8 > var10) {
                           TablePanel.this.table.setToolTipText(var7);
                        } else {
                           TablePanel.this.table.setToolTipText((String)null);
                        }
                     } else {
                        TablePanel.this.table.setToolTipText((String)null);
                     }
                  }
               }
            } catch (Exception var15) {
               TablePanel.this.table.setToolTipText((String)null);
            }

         }
      });
      this.table.getInputMap(1).getParent().remove(KeyStroke.getKeyStroke(27, 0));
      this.ok_button.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            Component var2 = SwingUtilities.getRoot(TablePanel.this);
            if (var2 instanceof JDialog) {
               ((JDialog)var2).dispose();
            } else if (var2 instanceof JFrame) {
               ((JFrame)var2).dispose();
            }

         }
      });
   }

   private void read(DefaultTableModel var1) {
      BufferedReader var2 = null;

      try {
         if (this.charset == null) {
            var2 = new BufferedReader(new InputStreamReader(new FileInputStream(this.file)));
         } else {
            var2 = new BufferedReader(new InputStreamReader(new FileInputStream(this.file), this.charset));
         }

         String var5 = this.field_separator == null ? "\n" : this.field_separator;

         String var3;
         String[] var4;
         for(String var6 = "\\" + var5; (var3 = var2.readLine()) != null; var1.addRow(var4)) {
            if (var3.endsWith(var5)) {
               var3 = var3.substring(0, var3.length() - var5.length());
            }

            var4 = var3.split(var6);
            if (var1.getColumnCount() < var4.length) {
               var1.setColumnCount(var4.length);
            }
         }
      } catch (Exception var8) {
         var8.printStackTrace();
      }

      if (var2 != null) {
         try {
            var2.close();
         } catch (IOException var7) {
            var7.printStackTrace();
         }
      }

   }

   private static class TablePanelTableModel extends DefaultTableModel {
      private TablePanelTableModel() {
      }

      public boolean isCellEditable(int var1, int var2) {
         return false;
      }

      public void setColumnIdentifiers(Object[] var1) {
         Vector var2 = new Vector();
         int var3 = 0;

         int var4;
         for(var4 = Math.min(var1.length, this.getColumnCount()); var3 < var4; ++var3) {
            var2.add(var1[var3]);
         }

         var3 = Math.min(var1.length, this.getColumnCount());

         for(var4 = this.getColumnCount(); var3 < var4; ++var3) {
            var2.add("");
         }

         this.setDataVector(this.dataVector, var2);
      }

      // $FF: synthetic method
      TablePanelTableModel(Object var1) {
         this();
      }
   }
}
