package hu.piller.enykp.alogic.calculator.lookup.lookuplistproviderimpl;

import hu.piller.enykp.alogic.calculator.CalculatorManager;
import hu.piller.enykp.alogic.calculator.calculator_c.LookupListModel;
import hu.piller.enykp.alogic.calculator.lookup.LookupList;
import hu.piller.enykp.alogic.calculator.matrices.defaultMatrixHandler;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

public class LookupListProviderCalculator extends DefaultLookupListProvider {
   public LookupListProviderCalculator(String var1, String var2, Hashtable var3) {
      this.groupId = null;
      this.formId = var1;
      this.fid = var2;
      this.fieldMetas = var3;
   }

   public List<String> getTableView(int var1, String var2) throws Exception {
      this.dynPageNumber = var1;

      try {
         Object[] var6 = this.getMatrix(this.formId, this.matrixId);
         List var3 = this.createView(var6, var2, this.delimiter);
         return var3;
      } catch (Exception var5) {
         String var4 = this.formId + "/" + this.matrixId + "/" + this.delimiter + "/" + var2;
         writeError(ID_ERR_LOOKUP_ITEM_CREATE, "Hiba a tábla nézet létrehozásakor! ", var5, var4);
         throw new Exception("Hiba a tábla nézet létrehozásakor! " + var4 + " - " + var5.getMessage());
      }
   }

   public LookupList getSortedTableView(int var1) throws Exception {
      this.dynPageNumber = var1;

      try {
         Object[] var5 = this.getMatrix(this.formId, this.matrixId);
         LookupList var2 = this.createViewIndex(var5, this.fieldCol, this.fieldList, this.delimiter);
         var2.setOverWrite(this.overWrite);
         return var2;
      } catch (Exception var4) {
         String var3 = this.formId + "/" + this.matrixId + "/" + this.delimiter + "/" + this.fieldList;
         writeError(ID_ERR_LOOKUP_ITEM_INDEX_CREATE, "Hiba a tábla nézet index létrehozásakor! ", var4, var3);
         throw new Exception("Hiba a tábla nézet index létrehozásakor! " + var3 + " - " + var4.getMessage());
      }
   }

   protected List<String> createView(Object[] var1, String var2, String var3) throws Exception {
      try {
         int[] var4 = this.createFieldList(var2);
         ArrayList var5 = new ArrayList(var1.length);

         for(int var6 = 0; var6 < var1.length; ++var6) {
            String[] var7 = (String[])((String[])var1[var6]);
            StringBuilder var8 = new StringBuilder();
            var8.append(var7[var4[0]]);

            for(int var9 = 1; var9 < var4.length; ++var9) {
               var8.append("  -  ");
               var8.append(var7[var4[var9]]);
            }

            var5.add(var8.toString());
         }

         return var5;
      } catch (Exception var10) {
         writeError(ID_ERR_LOOKUP_ITEM_CREATE, "Hiba a tábla nézet létrehozásakor! ", var10, (Object)null);
         throw var10;
      }
   }

   protected Object[] getMatrix(String var1, String var2) throws Exception {
      LookupListModel var3 = CalculatorManager.getInstance().get_field_lookup_create(this.fid, this.dynPageNumber);
      this.groupId = null;
      this.matrixId = var3.getMatrixSearchModel().getMatrixId();
      this.fieldCol = var3.getValueColumnNumber().toString();
      this.fieldList = var3.getListColumnNumbers();
      this.delimiter = var3.getMatrixSearchModel().getDelimiter();
      this.overWrite = var3.isOverWrite();
      Vector var4 = defaultMatrixHandler.getInstance().search(var1, var3.getMatrixSearchModel(), false, false);
      return var4 != null ? var4.toArray() : new Object[0];
   }

   public boolean validate(int var1, String var2) {
      LookupListModel var3 = CalculatorManager.getInstance().get_field_lookup_create(this.fid, var1);
      this.groupId = null;
      this.matrixId = var3.getMatrixSearchModel().getMatrixId();
      this.fieldCol = var3.getValueColumnNumber().toString();
      this.fieldList = var3.getListColumnNumbers();
      this.delimiter = var3.getMatrixSearchModel().getDelimiter();
      this.overWrite = var3.isOverWrite();
      if (var3.isOverWrite()) {
         return true;
      } else {
         Vector var4 = defaultMatrixHandler.getInstance().search(this.formId, var3.getMatrixSearchModel(), false, false);
         if (var4 == null) {
            return false;
         } else {
            for(int var5 = 0; var5 < var4.size(); ++var5) {
               String[] var6 = (String[])((String[])var4.elementAt(var5));
               if (var6[var3.getValueColumnNumber() - 1].equalsIgnoreCase(var2)) {
                  return true;
               }
            }

            return false;
         }
      }
   }
}
