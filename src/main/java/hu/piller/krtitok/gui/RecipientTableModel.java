package hu.piller.krtitok.gui;

import hu.piller.kripto.keys.KeyWrapper;
import java.text.SimpleDateFormat;
import java.util.Enumeration;
import java.util.Vector;
import javax.swing.table.AbstractTableModel;

public class RecipientTableModel extends AbstractTableModel {
   public static final int COL_TYPE = 1;
   private String[] columnNames = new String[]{"Kép", "Kulcstipus", "Algoritmus", "Azonosító", "Létrehozás"};
   private Vector data = new Vector();

   public RecipientTableModel(Vector keys) {
      if (keys != null) {
         Enumeration en = keys.elements();

         while(en.hasMoreElements()) {
            this.data.add(this.convertToRow((KeyWrapper)en.nextElement()));
         }
      }

      this.fireTableDataChanged();
   }

   private Object[] convertToRow(KeyWrapper kw) {
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
      Object[] row = new Object[this.getColumnCount()];
      row[0] = new Integer(kw.getType());
      row[1] = new Integer(kw.getType());
      row[2] = kw.getKey() == null ? "" : kw.getKey().getAlgorithm();
      row[3] = kw.getAlias();
      row[4] = sdf.format(kw.getCreationDate());
      return row;
   }

   public int getColumnCount() {
      return this.columnNames.length;
   }

   public String getColumnName(int col) {
      return this.columnNames[col];
   }

   public int getRowCount() {
      return this.data != null ? this.data.size() : 0;
   }

   public Object getValueAt(int rowIndex, int columnIndex) {
      return this.data != null ? ((Object[])this.data.elementAt(rowIndex))[columnIndex] : null;
   }

   public Class getColumnClass(int c) {
      return this.data.size() > 0 ? this.getValueAt(0, c).getClass() : null;
   }

   public boolean isCellEditable(int row, int col) {
      return false;
   }
}
