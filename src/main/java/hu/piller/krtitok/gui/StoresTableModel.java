package hu.piller.krtitok.gui;

import javax.swing.table.AbstractTableModel;

public class StoresTableModel extends AbstractTableModel {
   private String[] columnNames = new String[]{"Kép", "Típus", "FileNév", "FileMéret"};
   private Object[][] data;

   public StoresTableModel(Object[][] data) {
      this.data = data;
   }

   public int getColumnCount() {
      return this.columnNames.length;
   }

   public String getColumnName(int col) {
      return this.columnNames[col];
   }

   public int getRowCount() {
      return this.data.length;
   }

   public Object getValueAt(int rowIndex, int columnIndex) {
      return this.data[rowIndex][columnIndex];
   }

   public Class getColumnClass(int c) {
      return this.getValueAt(0, c).getClass();
   }
}
