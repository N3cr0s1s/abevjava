package hu.piller.enykp.alogic.archivemanager.archivemanagerpanel;

import hu.piller.enykp.alogic.archivemanager.ArchiveManagerDialogPanel;
import hu.piller.enykp.alogic.archivemanager.archivemanagerpanel.archivefilepanel.filepanel.FileBusiness;
import hu.piller.enykp.alogic.archivemanager.archivemanagerpanel.archivefilepanel.filepanel.FilePanel;
import hu.piller.enykp.alogic.archivemanager.archivemanagerpanel.archivefilepanel.filepanel.ListItem;
import hu.piller.enykp.interfaces.IFileChooser;
import hu.piller.enykp.util.base.Tools;
import hu.piller.enykp.util.base.eventsupport.IEventSupport;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.Vector;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class ArchiveFileBusiness extends FileBusiness implements IFileChooser, IEventSupport, KeyListener {
   private int selected_rows = 0;
   private boolean enabled = true;
   public static final int FL_EXIST = 0;
   public static final int FL_NOT_EXIST = 1;
   public static final int FL_SELECTED = 2;
   private ArchiveManagerDialogPanel amdp;
   private JProgressBar pbrun = null;
   public static final int KEY_CODE_SPACE = 32;

   public ArchiveFileBusiness(FilePanel var1) {
      super(var1);
   }

   protected void prepareFiles() {
      this.tbl_files.init(this, 0, this.defaultFileColumns);
   }

   protected void prepareButtons2() {
      this.tbl_files.addKeyListener(this);
      this.btn_select_all.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            ArchiveFileBusiness.this.selectAll(true);
         }
      });
      this.btn_unselect_all.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            ArchiveFileBusiness.this.selectAll(false);
         }
      });
   }

   public Object[] getAllConditions() {
      return null;
   }

   public void setAllConditions(Object[] var1) {
   }

   public int getSelectedRows() {
      return this.selected_rows;
   }

   public boolean isEnabled() {
      return this.enabled;
   }

   public void setEnabled(boolean var1) {
      this.enabled = var1;
      this.lst_file_filters.setEnabled(var1);
      this.tbl_files.setEnabled(var1);
      this.filter_business.setEnabled(var1);
      this.buttons_panel.setEnabled(var1);
      this.file_list_panel.setEnabled(var1);
      this.lbl_file_filters.setEnabled(var1);
      this.scp_file_filters.setEnabled(var1);
      this.btn_select_all.setEnabled(var1);
      this.btn_unselect_all.setEnabled(var1);
      if (var1) {
         this.tbl_files.getTable_sorter().attachTable(this.tbl_files);
      } else {
         this.tbl_files.getTable_sorter().detachTable();
      }

   }

   public void selectAll(boolean var1) {
      if (this.tbl_files.isEditing()) {
         this.tbl_files.getCellEditor().stopCellEditing();
      }

      TableModel var2 = this.tbl_files.getModel();
      int var3 = this.tbl_files.getRowCount();

      while(true) {
         --var3;
         if (var3 < 0) {
            this.setSelectedLabelAll(var1);
            this.tbl_files.repaint();
            return;
         }

         var2.setValueAt(var1, var3, 1);
      }
   }

   public void initQuery() {
      this.initFilesVector();
      this.filter_business.clearFilters();
   }

   public void initFilesVector() {
      this.vct_files = new Vector(256, 256);
   }

   protected void setCounters(boolean var1) {
      this.setRowCountLabel();
      this.setSelectedLabelAll(var1);
   }

   private void setRowCountLabel() {
      this.lbl_select_sum_all.setText(String.valueOf(this.tbl_files.getRowCount()));
      this.lbl_select_sum_all.repaint();
   }

   private void setSelectedLabelAll(boolean var1) {
      if (var1) {
         this.selected_rows = this.tbl_files.getRowCount();
      } else {
         this.selected_rows = 0;
      }

      this.lbl_select_sum_sel.setText(String.valueOf(this.selected_rows));
      this.lbl_select_sum_sel.repaint();
   }

   public void updateSelectedLabel(boolean var1) {
      if (var1) {
         ++this.selected_rows;
      } else {
         --this.selected_rows;
      }

      this.lbl_select_sum_sel.setText(String.valueOf(this.selected_rows));
      this.lbl_select_sum_sel.repaint();
   }

   public void addNewFile(Object var1) {
      this.setRowCountLabel();
      int var2 = this.setSelectFlag(var1, true);
      if (var2 != 1) {
         if (var2 == 0) {
            this.updateSelectedLabel(true);
         }

      } else {
         DefaultTableModel var3 = (DefaultTableModel)this.tbl_files.getModel();
         var3.addRow(this.getFileTableRow(var1));
         this.vct_files.add(var1);
         this.setSelectFlag(var1, true);
         this.setRowCountLabel();
         this.updateSelectedLabel(true);
         this.tbl_files.revalidate();
         this.tbl_files.repaint();
      }
   }

   public void removeItem(Object var1) {
      IListItem var2 = (IListItem)var1;
      this.removeItemFromModel(var2);
      this.removeItemFromFileList(var2);
   }

   public void removeSelectFlag(Object var1) {
      this.setSelectFlag(var1, false);
      this.updateSelectedLabel(false);
   }

   public int setSelectFlag(Object var1, boolean var2) {
      IListItem var3 = (IListItem)var1;
      File var4 = (File)var3.getItem();
      DefaultTableModel var5 = (DefaultTableModel)this.tbl_files.getModel();
      int var6 = this.tbl_files.getRowCount();

      while(true) {
         --var6;
         if (var6 < 0) {
            return 1;
         }

         Object var7 = this.tbl_files.getValueAt(var6, 0);
         if (var7 instanceof IListItem) {
            IListItem var8 = (IListItem)var7;
            if (var8.getItem().equals(var4)) {
               if ((Boolean)var5.getValueAt(var6, 1) == var2) {
                  return 2;
               }

               var5.setValueAt(var2, var6, 1);
               if (!this.posScrollPane(this.scp_file_list, var6 + 1)) {
                  this.tbl_files.repaint();
               }

               return 0;
            }
         }
      }
   }

   private void removeItemFromModel(IListItem var1) {
      File var2 = (File)var1.getItem();
      DefaultTableModel var3 = (DefaultTableModel)this.tbl_files.getModel();
      int var4 = this.tbl_files.getRowCount();

      while(true) {
         --var4;
         if (var4 < 0) {
            return;
         }

         Object var5 = this.tbl_files.getValueAt(var4, 0);
         if (var5 instanceof IListItem) {
            IListItem var6 = (IListItem)var5;
            if (var6.getItem().equals(var2)) {
               var3.removeRow(var4);
               if (!this.posScrollPane(this.scp_file_list, var4 + 1)) {
                  this.tbl_files.repaint();
               }

               this.setRowCountLabel();
               this.updateSelectedLabel(false);
               return;
            }
         }
      }
   }

   private void removeItemFromFileList(IListItem var1) {
      int var2 = this.vct_files.size();

      ListItem var3;
      do {
         --var2;
         if (var2 < 0) {
            return;
         }

         var3 = (ListItem)this.vct_files.elementAt(var2);
      } while(!var3.getItem().equals(var1.getItem()));

      this.vct_files.remove(var2);
   }

   private boolean posScrollPane(JScrollPane var1, int var2) {
      int var3 = var1.getVerticalScrollBar().getModel().getValue();
      int var4 = var1.getHeight();
      int var5 = this.tbl_files.getRowHeight();
      if (var2 * var5 <= var3 + var4 - 3 * var5 && var2 * var5 >= var3) {
         return false;
      } else if (var2 * var5 >= var1.getHeight()) {
         return false;
      } else {
         var1.getVerticalScrollBar().getModel().setValue(var2 * var5);
         var1.repaint();
         return true;
      }
   }

   public void selectRow(Point var1) {
      int var2 = this.tbl_files.rowAtPoint(var1);
      this.selectRow(var2);
   }

   private void selectRow(int var1) {
      try {
         boolean var2 = (Boolean)this.tbl_files.getModel().getValueAt(var1, 1);
         this.tbl_files.getModel().setValueAt(!var2, var1, 1);
         this.updateSelectedLabel(!var2);
         this.tbl_files.repaint();
      } catch (Exception var3) {
         Tools.eLog(var3, 1);
      }

   }

   public void keyPressed(KeyEvent var1) {
      if (var1.getKeyCode() == 32) {
         this.selectRow(this.tbl_files.getSelectedRow());
         if (this.tbl_files.getSelectedRow() < this.tbl_files.getRowCount() - 1) {
            this.tbl_files.changeSelection(this.tbl_files.getSelectedRow() + 1, this.tbl_files.getSelectedColumn(), false, false);
         }
      }

   }

   public void keyReleased(KeyEvent var1) {
   }

   public void keyTyped(KeyEvent var1) {
   }

   public Object[] getSelectedFiles() {
      if (this.tbl_files.isEditing()) {
         this.tbl_files.getCellEditor().stopCellEditing();
      }

      int var1 = 0;
      int var2 = 0;

      int var3;
      for(var3 = this.tbl_files.getModel().getRowCount(); var2 < var3; ++var2) {
         if ((Boolean)this.tbl_files.getModel().getValueAt(var2, 1)) {
            ++var1;
         }
      }

      Object[] var5 = new Object[var1];
      var1 = 0;
      var3 = 0;

      for(int var4 = this.tbl_files.getModel().getRowCount(); var3 < var4; ++var3) {
         if ((Boolean)this.tbl_files.getModel().getValueAt(var3, 1)) {
            var5[var1++] = this.tbl_files.getModel().getValueAt(var3, 0);
         }
      }

      return var5;
   }
}
