package hu.piller.enykp.alogic.filepanels.tablesorter;

import java.util.Vector;

public class MultiColumnTableSortObject {
   public String primarySortField;
   public String secondarySortField;
   public int intFieldToSave;
   public Vector vectorFieldToSave;

   public MultiColumnTableSortObject(Object var1, Object var2, int var3, Vector var4) {
      this.primarySortField = var1 == null ? "" : var1.toString();
      this.secondarySortField = var2 == null ? "" : var2.toString();
      this.intFieldToSave = var3;
      this.vectorFieldToSave = var4;
   }

   public void handleNumber1(int var1) {
      if (!"".equals(this.primarySortField)) {
         StringBuilder var2 = new StringBuilder();

         for(int var3 = 0; var3 < var1; ++var3) {
            var2.append("0");
         }

         var2.append(this.primarySortField);
         this.primarySortField = var2.substring(var2.length() - var1);
      }
   }

   public void handleNumber2(int var1) {
      if (!"".equals(this.secondarySortField)) {
         StringBuilder var2 = new StringBuilder();

         for(int var3 = 0; var3 < var1; ++var3) {
            var2.append("0");
         }

         var2.append(this.secondarySortField);
         this.secondarySortField = var2.substring(var2.length() - var1);
      }
   }
}
