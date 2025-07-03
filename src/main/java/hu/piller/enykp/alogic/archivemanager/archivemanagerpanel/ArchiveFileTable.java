package hu.piller.enykp.alogic.archivemanager.archivemanagerpanel;

import hu.piller.enykp.alogic.archivemanager.archivemanagerpanel.archivefilepanel.filepanel.FileTable;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.EventObject;
import java.util.Vector;
import javax.swing.Icon;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

public class ArchiveFileTable extends FileTable {
   protected ArchiveFileBusiness file_business;

   public void setModel(TableModel var1) {
      if (this.files_model != null && !(var1 instanceof FileTable.FilesModel) && var1 instanceof DefaultTableModel) {
         DefaultTableModel var2 = (DefaultTableModel)var1;
         this.file_business.showFileList(var2);
      } else {
         super.setModel(var1);
         this.files_model = var1;
      }
   }

   protected void init(ArchiveFileBusiness var1, int var2, Object[] var3) {
      this.file_business = var1;
      this.id_column_id = var2;
      this.setModel(new FileTable.FilesModel(var3));
      this.setAutoResizeMode(0);
      this.getTableHeader().setReorderingAllowed(false);
      this.setColumnSelectionAllowed(false);
      this.setCellSelectionEnabled(false);
      this.setRowSelectionAllowed(true);
      this.setSelectionMode(0);
      this.setDefaultRenderer(Object.class, new ArchiveFileTable.tableCellRenderer());
      TableColumnModel var4 = this.getColumnModel();
      TableColumn var5 = var4.getColumn(var2);
      var5.setMaxWidth(0);
      var5.setMinWidth(0);
      var5.setPreferredWidth(0);
      var5.setResizable(false);
      this.setCheckBoxColumn(this);
      this.addMouseListener(new ArchiveFileTable.FileMouseListener());
      this.addMouseMotionListener(new MouseMotionListener() {
         private Object v;

         public void mouseDragged(MouseEvent var1) {
            this.v = null;
         }

         public void mouseMoved(MouseEvent var1) {
            Point var2 = var1.getPoint();
            int var3 = ArchiveFileTable.this.columnAtPoint(var2);

            try {
               Object var4 = ArchiveFileTable.this.getValueAt(ArchiveFileTable.this.rowAtPoint(var2), var3);
               if (var4 != this.v) {
                  this.v = var4;
                  TableCellRenderer var5 = ArchiveFileTable.this.getDefaultRenderer(String.class);
                  if (var5 instanceof JLabel) {
                     JLabel var6 = (JLabel)var5;
                     String var7 = this.v.toString();
                     var6.setText(var7);
                     if (var6.getPreferredSize().getWidth() > (double)ArchiveFileTable.this.getColumnModel().getColumn(var3).getWidth()) {
                        ArchiveFileTable.this.setToolTipText(var7);
                     } else {
                        ArchiveFileTable.this.setToolTipText((String)null);
                     }
                  }
               }
            } catch (Exception var8) {
               ArchiveFileTable.this.setToolTipText((String)null);
            }

         }
      });
      this.getTableHeader().addMouseMotionListener(new MouseMotionListener() {
         int i = -2;

         public void mouseDragged(MouseEvent var1) {
            this.i = -2;
         }

         public void mouseMoved(MouseEvent var1) {
            Point var2 = var1.getPoint();
            JTableHeader var3 = ArchiveFileTable.this.getTableHeader();

            try {
               int var4 = var3.columnAtPoint(var2);
               if (var4 != this.i) {
                  this.i = var4;
                  TableCellRenderer var5 = ArchiveFileTable.this.getDefaultRenderer(String.class);
                  if (var5 instanceof JLabel) {
                     JLabel var6 = (JLabel)var5;
                     String var7 = ArchiveFileTable.this.getColumnName(this.i);
                     Icon var8 = ArchiveFileTable.this.table_sorter.getIcon(ArchiveFileTable.this.table_sorter.getSortOrder());
                     var6.setText(var7);
                     double var9 = var6.getPreferredSize().getWidth();
                     var9 += var8 == null ? 0.0D : (double)var8.getIconWidth();
                     var9 += (double)var6.getIconTextGap();
                     if (var9 > (double)ArchiveFileTable.this.getColumnModel().getColumn(var4).getWidth()) {
                        var3.setToolTipText(ArchiveFileTable.this.getColumnName(this.i));
                     } else {
                        var3.setToolTipText((String)null);
                     }
                  }
               }
            } catch (Exception var11) {
               var3.setToolTipText((String)null);
            }

         }
      });
      this.table_sorter.attachTable(this);
   }

   private void setCheckBoxColumn(JTable var1) {
      TableColumn var2 = var1.getColumnModel().getColumn(1);
      var2.setCellRenderer(new ArchiveFileTable.BooleanTableItem());
      var2.setCellEditor(new ArchiveFileTable.BooleanTableItem());
      var1.setEditingColumn(1);
   }

   class BooleanTableItem extends JCheckBox implements TableCellRenderer, TableCellEditor, ActionListener {
      protected transient Vector listeners = new Vector();

      public BooleanTableItem() {
         this.setHorizontalAlignment(0);
         this.addActionListener(this);
      }

      public Component getTableCellRendererComponent(JTable var1, Object var2, boolean var3, boolean var4, int var5, int var6) {
         if (var2 instanceof Boolean) {
            this.setSelected((Boolean)var2);
         } else {
            this.setSelected(false);
         }

         this.setFont(var1.getFont());
         if (var3) {
            this.setForeground(var1.getSelectionForeground());
            this.setBackground(var1.getSelectionBackground());
         } else if ((Boolean)ArchiveFileTable.this.files_model.getValueAt(var5, 1)) {
            this.setBackground(var1.getSelectionBackground());
         } else {
            this.setForeground(var1.getForeground());
            this.setBackground(var1.getBackground());
         }

         this.setEnabled(var1.isEnabled());
         return this;
      }

      public Component getTableCellEditorComponent(JTable var1, Object var2, boolean var3, int var4, int var5) {
         this.getTableCellRendererComponent(var1, var2, var3, true, var4, var5);
         return this;
      }

      public boolean isCellEditable(EventObject var1) {
         return true;
      }

      public boolean shouldSelectCell(EventObject var1) {
         return false;
      }

      public void cancelCellEditing() {
         this.callListeners();
      }

      public boolean stopCellEditing() {
         this.callListeners();
         return true;
      }

      protected void callListeners() {
         ChangeEvent var1 = new ChangeEvent(this);
         int var2 = this.listeners.size();

         while(true) {
            --var2;
            if (var2 < 0) {
               return;
            }

            ((CellEditorListener)this.listeners.elementAt(var2)).editingStopped(var1);
         }
      }

      public Object getCellEditorValue() {
         return this.isSelected();
      }

      public void addCellEditorListener(CellEditorListener var1) {
         this.listeners.addElement(var1);
      }

      public void removeCellEditorListener(CellEditorListener var1) {
         this.listeners.removeElement(var1);
      }

      public void actionPerformed(ActionEvent var1) {
         if (var1.getSource() instanceof ArchiveFileTable.BooleanTableItem) {
            ArchiveFileTable.this.file_business.updateSelectedLabel(((ArchiveFileTable.BooleanTableItem)var1.getSource()).isSelected());
         }

         this.callListeners();
         this.repaint();
      }
   }

   private class FileMouseListener implements MouseListener {
      private FileMouseListener() {
      }

      public void mouseClicked(MouseEvent var1) {
         switch(var1.getClickCount()) {
         case 1:
            ArchiveFileTable.this.file_business.selectRow(var1.getPoint());
         default:
         }
      }

      public void mouseEntered(MouseEvent var1) {
      }

      public void mouseExited(MouseEvent var1) {
      }

      public void mousePressed(MouseEvent var1) {
      }

      public void mouseReleased(MouseEvent var1) {
      }

      // $FF: synthetic method
      FileMouseListener(Object var2) {
         this();
      }
   }

   class tableCellRenderer extends DefaultTableCellRenderer {
      public Component getTableCellRendererComponent(JTable var1, Object var2, boolean var3, boolean var4, int var5, int var6) {
         if ((Boolean)ArchiveFileTable.this.getModel().getValueAt(var5, 1)) {
            this.setBackground(var1.getSelectionBackground());
         } else {
            this.setBackground(var1.getBackground());
         }

         return super.getTableCellRendererComponent(var1, var2, var3, var4, var5, var6);
      }
   }
}
