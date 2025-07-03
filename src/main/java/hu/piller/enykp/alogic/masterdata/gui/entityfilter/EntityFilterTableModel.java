package hu.piller.enykp.alogic.masterdata.gui.entityfilter;

import hu.piller.enykp.alogic.masterdata.core.Entity;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;

public class EntityFilterTableModel extends DefaultTableModel implements Cloneable {
   private static final long serialVersionUID = 1L;
   protected Vector<EntitySelector> rows = new Vector();
   protected EntityToEntitySelector e2es = new EntityToEntitySelector();
   protected String[] header;

   public EntityFilterTableModel() {
      this.header = this.e2es.getSelectorPartNames();
   }

   public int getColumnCount() {
      return this.header == null ? 0 : this.header.length;
   }

   public int getRowCount() {
      return this.rows == null ? 0 : this.rows.size();
   }

   public String getColumnName(int var1) {
      String var2 = "";
      if (this.header != null && var1 < this.header.length) {
         var2 = this.header[var1];
      }

      return var2;
   }

   public boolean isCellEditable(int var1, int var2) {
      return false;
   }

   public Object getValueAt(int var1, int var2) {
      String var3 = "";
      if (var1 < this.rows.size() && var2 < this.header.length) {
         var3 = ((EntitySelector)this.rows.get(var1)).getSelectorValue(this.header[var2]);
      }

      return var3;
   }

   public void fillTable(Entity[] var1) {
      this.rows.clear();
      Entity[] var2 = var1;
      int var3 = var1.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Entity var5 = var2[var4];
         this.rows.add(this.e2es.map(var5));
      }

      this.fireTableRowsInserted(0, this.rows.size());
   }

   public long getIdOfSelected(int var1) {
      return ((EntitySelector)this.rows.get(var1)).getEntityId();
   }

   public Vector<EntitySelector> getRows() {
      return this.rows;
   }

   protected Object clone() {
      EntityFilterTableModel var1 = new EntityFilterTableModel();
      var1.rows.addAll(this.rows);
      return var1;
   }
}
