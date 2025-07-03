package hu.piller.enykp.alogic.filepanels.mohu;

import javax.swing.JComboBox;
import javax.swing.table.DefaultTableModel;

public class ComboTableModel extends DefaultTableModel {
   private int columnCount = 0;
   private int editableColumnIndex = 0;
   private int defaultCKAzonIndex = 0;
   private boolean editable = true;

   public ComboTableModel(Object[] var1, int var2) {
      super(var1, var2);
      this.columnCount = var1.length;
      this.editable = true;
   }

   public Class getColumnClass(int var1) {
      return var1 != this.editableColumnIndex ? super.getColumnClass(var1) : (new JComboBox()).getClass();
   }

   public Object getValueAt(int var1, int var2) {
      Object var3 = super.getValueAt(var1, var2);
      if (var2 != this.editableColumnIndex) {
         return var3;
      } else if (var3 instanceof String[]) {
         return this.defaultCKAzonIndex < ((String[])((String[])var3)).length ? ((String[])((String[])var3))[this.defaultCKAzonIndex] : ((String[])((String[])var3))[0];
      } else {
         return var3;
      }
   }

   public int getColumnCount() {
      return this.columnCount;
   }

   public int getRowCount() {
      return super.getRowCount();
   }

   public boolean isCellEditable(int var1, int var2) {
      return var2 == this.editableColumnIndex ? this.editable : false;
   }

   public void setEditableColumnIndex(int var1) {
      this.editableColumnIndex = var1;
   }

   public void setDefaultCKAzonIndex(int var1) {
      this.defaultCKAzonIndex = var1;
   }

   public void setEditable(boolean var1) {
      this.editable = var1;
   }
}
