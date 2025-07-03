package hu.piller.krtitok.gui;

import hu.piller.xml.abev.element.DocMetaData;
import java.util.Vector;
import javax.swing.table.AbstractTableModel;

public class MetaDataTableModel extends AbstractTableModel {
   private String[] columnNames = new String[]{"Meta fájl neve", "Címzett", "Dok.tipus azonosító", "Dok.tipus verzió", "Fájl név", "Megjegyzés"};
   private Vector data = new Vector();
   private Vector metaDatas = new Vector();

   public MetaDataTableModel(String krOrMf) {
      if (krOrMf.equalsIgnoreCase("kr")) {
         this.columnNames[0] = "Titkosított fájl neve";
      }

   }

   public int getColumnCount() {
      return this.columnNames.length;
   }

   public int getRowCount() {
      return this.data.size();
   }

   public String getColumnName(int col) {
      return this.columnNames[col];
   }

   public Class getColumnClass(int c) {
      return this.getValueAt(0, c) == null ? Object.class : this.getValueAt(0, c).getClass();
   }

   public boolean isCellEditable(int row, int col) {
      return false;
   }

   public Object getValueAt(int row, int col) {
      Object[] myRow = (Object[])this.data.elementAt(row);
      return myRow[col];
   }

   public void setValueAt(Object value, int row, int col) {
      Object[] myRow = (Object[])this.data.elementAt(row);
      myRow[col] = value;
      this.fireTableCellUpdated(row, col);
   }

   public void addDocMetaData(String fileName, DocMetaData md) {
      this.metaDatas.add(md);
      Object[] newRow = new Object[this.getColumnCount()];
      newRow[0] = fileName;
      newRow[1] = md.getCimzett();
      newRow[2] = md.getDokTipusAzonosito();
      newRow[3] = md.getDokTipusVerzio();
      newRow[4] = md.getFileNev();
      newRow[5] = md.getMegjegyzes();
      this.data.add(newRow);
   }

   public DocMetaData getMetaData(int row) {
      return this.metaDatas.size() >= row ? (DocMetaData)this.metaDatas.elementAt(row) : null;
   }
}
