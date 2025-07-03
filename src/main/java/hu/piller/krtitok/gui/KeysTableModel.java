package hu.piller.krtitok.gui;

import hu.piller.kripto.keys.KeyWrapper;
import hu.piller.kripto.keys.StoreWrapper;
import java.awt.Component;
import java.awt.FlowLayout;
import java.security.Key;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.text.SimpleDateFormat;
import java.util.Enumeration;
import java.util.Vector;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.table.AbstractTableModel;

public class KeysTableModel extends AbstractTableModel {
   public static final int COL_SELECTED = 5;
   public static final int COL_TYPE = 1;
   private String[] columnNames = new String[]{"Kép", "Kulcstipus", "Algoritmus", "Azonosító", "Létrehozás", "Kiválaszt"};
   private Vector data;
   private Vector keys;
   int[] indexes;
   private StoreWrapper sw;
   private int selected;
   private int maxSelectedRows = 1;

   public KeysTableModel(StoreWrapper sw, int maxSelectedRows, boolean showPrivate, boolean showPublic) {
      this.sw = sw;
      this.maxSelectedRows = maxSelectedRows;

      try {
         this.keys = sw.listKeys();
      } catch (KeyStoreException var6) {
         var6.printStackTrace();
      } catch (NoSuchAlgorithmException var7) {
         var7.printStackTrace();
      } catch (UnrecoverableKeyException var8) {
         var8.printStackTrace();
      }

      this.listKeys(showPrivate, showPublic);
   }

   public void listKeys(boolean showPrivate, boolean showPublic) {
      this.data = new Vector();
      this.indexes = new int[this.keys.size()];
      int i = 0;
      int k = 0;

      for(Enumeration en = this.keys.elements(); en.hasMoreElements(); ++k) {
         KeyWrapper kw = (KeyWrapper)en.nextElement();
         int type = kw.getType();
         if (type == 0 && showPublic || type == 1 && showPrivate) {
            this.indexes[i++] = k;
            Object[] row = this.convertToRow(kw);
            this.data.add(row);
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
      row[5] = kw.isSelected();
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
      return col == 5;
   }

   public void setValueAt(Object value, int row, int col) {
      KeyWrapper kw = (KeyWrapper)this.keys.elementAt(this.indexes[row]);
      if (col == 5 && (Boolean)value) {
         if (this.selected < this.maxSelectedRows) {
            if (!kw.isSelected()) {
               if (kw.getKey() == null) {
                  if (kw.getType() == 1) {
                     JPasswordField jpf = new JPasswordField(10);
                     JPanel panel = new JPanel(new FlowLayout());
                     panel.add(new JLabel("Kulcsot védő jelszó: "));
                     panel.add(jpf);
                     (new JOptionPane(panel)).createDialog((Component)null, "Jelszó").show();
                     Key key = kw.getKey(jpf.getPassword());
                     if (key != null) {
                        kw.setKey(key);
                        this.keys.setElementAt(kw, row);
                        this.updateRow(row, kw);
                        kw.setSelected(true);
                        ((Object[])this.data.elementAt(row))[col] = Boolean.TRUE;
                        ++this.selected;
                     } else {
                        JOptionPane.showMessageDialog((Component)null, "Sikertelen a kulcs betöltése!", "Üzenet", 2);
                     }
                  }
               } else {
                  kw.setSelected(true);
                  ((Object[])this.data.elementAt(row))[col] = Boolean.TRUE;
                  ++this.selected;
               }
            }
         } else {
            JOptionPane.showMessageDialog((Component)null, "Maximum " + this.maxSelectedRows + " kulcsot választhat!", "Üzenet", 2);
         }
      } else {
         kw.setSelected(false);
         ((Object[])this.data.elementAt(row))[col] = Boolean.FALSE;
         --this.selected;
      }

      this.fireTableCellUpdated(row, col);
   }

   private void selectRow(int row) {
      if (this.selected < this.maxSelectedRows) {
         this.setValueAt(Boolean.TRUE, row, 5);
      } else {
         JOptionPane.showMessageDialog((Component)null, "Maximum " + this.maxSelectedRows + " kulcsot választhat!", "Üzenet", 2);
      }

   }

   private void deselectRow(int row) {
      this.setValueAt(Boolean.FALSE, row, 5);
   }

   public void invertSelection(int row) {
      if ((Boolean)this.getValueAt(row, 5)) {
         this.deselectRow(row);
      } else {
         this.selectRow(row);
      }

   }

   public void updateRow(int index, KeyWrapper kw) {
      this.keys.setElementAt(kw, index);
      this.data.setElementAt(this.convertToRow(kw), index);
      this.fireTableCellUpdated(index, 1);
   }

   public Vector getSelectedKeys() {
      Vector selectedKeys = new Vector();
      Enumeration en = this.keys.elements();

      while(en.hasMoreElements()) {
         KeyWrapper kw = (KeyWrapper)en.nextElement();
         if (kw.isSelected()) {
            selectedKeys.add(kw);
         }
      }

      return selectedKeys;
   }
}
