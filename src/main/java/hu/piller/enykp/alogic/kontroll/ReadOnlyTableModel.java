package hu.piller.enykp.alogic.kontroll;

import javax.swing.table.DefaultTableModel;

public class ReadOnlyTableModel extends DefaultTableModel {
   public ReadOnlyTableModel(Object[][] var1, Object[] var2) {
      super(var1, var2);
   }

   public ReadOnlyTableModel(Object[] var1, int var2) {
      super(var1, var2);
   }

   public boolean isCellEditable(int var1, int var2) {
      return false;
   }
}
