package hu.piller.enykp.alogic.masterdata.sync.ui.entityfilter;

import hu.piller.enykp.alogic.masterdata.core.Entity;
import hu.piller.enykp.alogic.masterdata.gui.entityfilter.EntitySelector;
import hu.piller.enykp.alogic.masterdata.gui.entityfilter.EntityToEntitySelector;
import hu.piller.enykp.util.base.ErrorList;
import java.util.ArrayList;
import java.util.HashSet;
import javax.swing.table.DefaultTableModel;

public class SyncEntityFilterTableModel extends DefaultTableModel implements Cloneable {
   private static final long serialVersionUID = 1L;
   protected EntityToEntitySelector e2es = new EntityToEntitySelector();
   private Object[][] data = new Object[0][];
   private String[] header;

   public SyncEntityFilterTableModel() {
      String[] var1 = this.e2es.getSelectorPartNames();
      this.header = new String[var1.length + 1];
      System.arraycopy(var1, 0, this.header, 0, var1.length);
      this.header[var1.length] = "Letöltés";
   }

   public int getColumnCount() {
      return this.header == null ? 0 : this.header.length;
   }

   public String[] getSelectedIds() {
      ArrayList var1 = new ArrayList();
      HashSet var2 = new HashSet();

      for(int var3 = 0; var3 < this.data.length; ++var3) {
         if ((Boolean)this.data[var3][5]) {
            String var4 = ((String)this.data[var3][0]).trim();
            String var5;
            if (!"Egyéni vállalkozó".equals(var4) && !"Társaság".equals(var4)) {
               var5 = (String)this.data[var3][4];
            } else {
               var5 = ((String)this.data[var3][3]).substring(0, 8);
            }

            if (var1.contains(var5)) {
               var2.add(var5);
            }

            var1.add(var5);
         }
      }

      if (!var2.isEmpty()) {
         ErrorList.getInstance().writeError(1000, "Törzsadatok szinkronizálása: a következő duplikált azonosítók törlése a szinkronizációs listáról " + var2.toString(), (Exception)null, (Object)null);
         var1.removeAll(var2);
      }

      return (String[])var1.toArray(new String[var1.size()]);
   }

   public int getRowCount() {
      return this.data == null ? 0 : this.data.length;
   }

   public String getColumnName(int var1) {
      return this.header[var1];
   }

   public boolean isCellEditable(int var1, int var2) {
      return var2 == this.header.length - 1;
   }

   public Class getColumnClass(int var1) {
      return var1 == this.header.length - 1 ? Boolean.class : String.class;
   }

   public Object getValueAt(int var1, int var2) {
      return this.data[var1][var2];
   }

   public void setValueAt(Object var1, int var2, int var3) {
      this.data[var2][var3] = var1;
      this.fireTableCellUpdated(var2, var3);
   }

   public Object[] getRow(int var1) {
      return this.data[var1];
   }

   public void addRow(Object[] var1) {
      Object[][] var2 = new Object[this.data.length + 1][];

      int var3;
      for(var3 = 0; var3 < this.data.length; ++var3) {
         var2[var3] = this.data[var3];
      }

      var3 = this.data.length == 0 ? 0 : var2.length - 1;
      var2[var3] = var1;
      this.data = var2;
      this.fireTableRowsInserted(0, this.data.length);
   }

   public void fillTable(Entity[] var1) {
      this.data = new Object[var1.length][];

      for(int var2 = 0; var2 < var1.length; ++var2) {
         EntitySelector var3 = this.e2es.map(var1[var2]);
         this.data[var2] = new Object[this.header.length];

         for(int var4 = 0; var4 < this.header.length; ++var4) {
            Object var5;
            if (var4 < this.header.length - 1) {
               var5 = var3.getSelectorValue(this.header[var4]);
            } else {
               var5 = Boolean.FALSE;
            }

            this.data[var2][var4] = var5;
         }
      }

      this.fireTableRowsInserted(0, this.data.length);
   }

   protected Object clone() {
      SyncEntityFilterTableModel var1 = new SyncEntityFilterTableModel();
      var1.data = this.data;
      return var1;
   }
}
