package hu.piller.enykp.alogic.masterdata.sync.ui.maintenance;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.swing.table.AbstractTableModel;

public class MDMaintenanceTableModel extends AbstractTableModel {
   private String mdSum = "";
   private List<MDMaintenanceModel> data = new ArrayList();
   private Set<String> mdNamesWithMultipleValues;
   private String[] columnNames = new String[]{"Adatnév\n", "Érték az ÁNYK-ban\n(törzsadattár adata)", "Érték a NAV-nál\n(lekérdezett adat)", "NAV értéket\nelfogadja?", "Mentésre kerülő érték\n(módosítható)"};

   public MDMaintenanceTableModel(List<MDMaintenanceModel> var1, Set<String> var2) {
      this.data.addAll(var1);
      this.mdNamesWithMultipleValues = var2;
      this.mdSum = this.calcMdSum();
   }

   public void updateTableAfterSave(List<MDMaintenanceModel> var1) {
      for(int var2 = 0; var2 < var1.size(); ++var2) {
         ((MDMaintenanceModel)this.data.get(var2)).setLocalValue(((MDMaintenanceModel)var1.get(var2)).getLocalValue());
         ((MDMaintenanceModel)this.data.get(var2)).setNavValid(false);
      }

      this.fireTableRowsUpdated(0, this.getRowCount() - 1);
      this.mdSum = this.calcMdSum();
   }

   public int getColumnCount() {
      return this.columnNames.length;
   }

   public String getColumnName(int var1) {
      return this.columnNames[var1];
   }

   public Class getColumnClass(int var1) {
      return var1 == 3 ? Boolean.class : String.class;
   }

   public int getRowCount() {
      return this.data.size();
   }

   public void setValueAt(Object var1, int var2, int var3) {
      MDMaintenanceModel var4 = (MDMaintenanceModel)this.data.get(var2);
      switch(var3) {
      case 2:
         var4.setNavValue((String)var1);
         if (var4.isNavValid()) {
            var4.setValidValue(var4.getNavValue());
         } else if ("".equals(var4.getValidValue().trim())) {
            var4.setValidValue(var4.getLocalValue());
         }
         break;
      case 3:
         var4.setNavValid(Boolean.parseBoolean(String.valueOf(var1)));
         if (var4.isNavValid()) {
            var4.setValidValue(var4.getNavValue());
         } else {
            var4.setValidValue(var4.getLocalValue());
         }
         break;
      case 4:
         boolean var5 = !String.valueOf(var1).equals(var4.getValidValue());
         var4.setValidValue(String.valueOf(var1));
         if (var5 && var4.isNavValid()) {
            var4.setNavValid(false);
         }
      }

      this.fireTableRowsUpdated(0, this.getRowCount() - 1);
   }

   public Object getValueAt(int var1, int var2) {
      MDMaintenanceModel var3 = (MDMaintenanceModel)this.data.get(var1);
      if (var3.getType() == 0) {
         switch(var2) {
         case 0:
            return var3.getNameToShow();
         case 1:
            return var3.getLocalValue();
         case 2:
            return var3.getNavValue();
         case 3:
            return var3.isNavValid();
         case 4:
            return var3.getValidValue();
         default:
            return null;
         }
      } else {
         return var2 == 0 ? var3.getBlockName() : null;
      }
   }

   public boolean isCellEditable(int var1, int var2) {
      if (((MDMaintenanceModel)this.data.get(var1)).getType() == 1) {
         return false;
      } else if (var2 == 2 && this.mdNamesWithMultipleValues.contains(((MDMaintenanceModel)this.data.get(var1)).getName())) {
         return true;
      } else {
         return var2 == 3 || var2 == 4;
      }
   }

   public void toggleAll(boolean var1) {
      Iterator var2 = this.data.iterator();

      while(var2.hasNext()) {
         MDMaintenanceModel var3 = (MDMaintenanceModel)var2.next();
         var3.setNavValid(var1);
         if (var3.isNavValid()) {
            var3.setValidValue(var3.getNavValue());
         } else {
            var3.setValidValue(var3.getLocalValue());
         }
      }

      this.fireTableRowsUpdated(0, this.getRowCount());
   }

   public boolean hasDifferentValues(int var1) {
      if (((MDMaintenanceModel)this.data.get(var1)).getType() == 0) {
         String var2 = ((MDMaintenanceModel)this.data.get(var1)).getLocalValue();
         String var3 = ((MDMaintenanceModel)this.data.get(var1)).getNavValue();
         return !var2.trim().equalsIgnoreCase(var3.trim());
      } else {
         return false;
      }
   }

   public List<MDMaintenanceModel> getData() {
      return this.data;
   }

   private String calcMdSum() {
      StringBuilder var1 = new StringBuilder();
      Iterator var2 = this.data.iterator();

      while(var2.hasNext()) {
         MDMaintenanceModel var3 = (MDMaintenanceModel)var2.next();
         var1.append(var3.getValidValue()).append(";");
      }

      return var1.toString();
   }

   public boolean hasChanged() {
      return !this.mdSum.equals(this.calcMdSum());
   }
}
