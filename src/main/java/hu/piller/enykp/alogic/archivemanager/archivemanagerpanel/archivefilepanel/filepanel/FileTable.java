package hu.piller.enykp.alogic.archivemanager.archivemanagerpanel.archivefilepanel.filepanel;

import hu.piller.enykp.alogic.filepanels.tablesorter.TableSorter;
import hu.piller.enykp.gui.GuiUtil;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

public class FileTable extends JTable {
   protected final TableSorter table_sorter = new TableSorter();
   protected FileBusiness file_business;
   protected int id_column_id;
   protected TableModel files_model;

   public void setModel(TableModel var1) {
      if (this.files_model != null && !(var1 instanceof FileTable.FilesModel) && var1 instanceof DefaultTableModel) {
         DefaultTableModel var2 = (DefaultTableModel)var1;
         this.file_business.showFileList(var2);
      } else {
         super.setModel(var1);
         this.files_model = var1;
      }
   }

   public boolean getScrollableTracksViewportWidth() {
      if (this.autoResizeMode != 0 && this.getParent() instanceof JViewport) {
         return ((JViewport)this.getParent()).getWidth() > this.getPreferredSize().width;
      } else {
         return false;
      }
   }

   public TableSorter getTable_sorter() {
      return this.table_sorter;
   }

   protected void init(FileBusiness var1, int var2, Object[] var3) {
      this.file_business = var1;
      this.id_column_id = var2;
      this.setModel(new FileTable.FilesModel(var3));
      this.setColumnSelectionAllowed(false);
      this.setRowSelectionAllowed(true);
      this.setAutoResizeMode(0);
      this.getTableHeader().setReorderingAllowed(false);
      this.setSelectionMode(0);
      TableColumnModel var4 = this.getColumnModel();
      TableColumn var5 = var4.getColumn(var2);
      var5.setMinWidth(0);
      var5.setMaxWidth(0);
      var5.setPreferredWidth(0);
      var5.setWidth(0);
      var5.setResizable(false);
      this.addMouseListener(new FileTable.FileMouseListener());
      this.addMouseMotionListener(new MouseMotionListener() {
         private Object v;

         public void mouseDragged(MouseEvent var1) {
            this.v = null;
         }

         public void mouseMoved(MouseEvent var1) {
            Point var2 = var1.getPoint();
            int var3 = FileTable.this.columnAtPoint(var2);

            try {
               Object var4 = FileTable.this.getValueAt(FileTable.this.rowAtPoint(var2), var3);
               if (var4 != this.v) {
                  this.v = var4;
                  TableCellRenderer var5 = FileTable.this.getDefaultRenderer(String.class);
                  if (var5 instanceof JLabel) {
                     JLabel var6 = (JLabel)var5;
                     String var7 = this.v.toString();
                     var6.setText(var7);
                     if (var6.getPreferredSize().getWidth() > (double)FileTable.this.getColumnModel().getColumn(var3).getWidth()) {
                        FileTable.this.setToolTipText(var7);
                     } else {
                        FileTable.this.setToolTipText((String)null);
                     }
                  }
               }
            } catch (Exception var8) {
               FileTable.this.setToolTipText((String)null);
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
            JTableHeader var3 = FileTable.this.getTableHeader();

            try {
               int var4 = var3.columnAtPoint(var2);
               if (var4 != this.i) {
                  this.i = var4;
                  TableCellRenderer var5 = FileTable.this.getDefaultRenderer(String.class);
                  if (var5 instanceof JLabel) {
                     JLabel var6 = (JLabel)var5;
                     String var7 = FileTable.this.getColumnName(this.i);
                     Icon var8 = FileTable.this.table_sorter.getIcon(FileTable.this.table_sorter.getSortOrder());
                     var6.setText(var7);
                     double var9 = var6.getPreferredSize().getWidth();
                     var9 += var8 == null ? 0.0D : (double)var8.getIconWidth();
                     var9 += (double)var6.getIconTextGap();
                     if (var9 > (double)FileTable.this.getColumnModel().getColumn(var4).getWidth()) {
                        var3.setToolTipText(FileTable.this.getColumnName(this.i));
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
      GuiUtil.setTableColWidth(this);
      this.table_sorter.attachTable(this);
   }

   protected class FilesModel extends DefaultTableModel {
      public FilesModel(Object[] var2) {
         for(int var3 = 0; var3 < var2.length; ++var3) {
            this.addColumn(var2[var3]);
         }

      }

      public boolean isCellEditable(int var1, int var2) {
         return false;
      }
   }

   private class FileMouseListener implements MouseListener {
      private FileMouseListener() {
      }

      public void mouseClicked(MouseEvent var1) {
         switch(var1.getClickCount()) {
         case 1:
            FileTable.this.file_business.fireSingleClickOnFiles();
            break;
         case 2:
            FileTable.this.file_business.fireDoubleClickOnFiles();
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
}
