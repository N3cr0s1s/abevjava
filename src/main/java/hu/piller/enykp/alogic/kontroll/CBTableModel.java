package hu.piller.enykp.alogic.kontroll;

import javax.swing.table.DefaultTableModel;

public class CBTableModel extends DefaultTableModel {
   private static final Object[] COLNAMES = new Object[]{"", "Adószám", "Nyomtatvány", "Rekordszám", "Készült", "Files"};

   public CBTableModel() {
      super(COLNAMES, 0);
   }

   public Class getColumnClass(int var1) {
      return this.getValueAt(0, var1).getClass();
   }

   public Object getValueAt(int var1, int var2) {
      return super.getValueAt(var1, var2);
   }

   public int getColumnCount() {
      return COLNAMES.length;
   }

   public int getRowCount() {
      return super.getRowCount();
   }

   public boolean isCellEditable(int var1, int var2) {
      return var2 == 0;
   }
}
