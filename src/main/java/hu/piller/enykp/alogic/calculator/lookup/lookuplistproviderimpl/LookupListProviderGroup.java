package hu.piller.enykp.alogic.calculator.lookup.lookuplistproviderimpl;

import hu.piller.enykp.alogic.calculator.matrices.defaultMatrixHandler;
import java.util.Hashtable;

public class LookupListProviderGroup extends DefaultLookupListProvider {
   public LookupListProviderGroup(String var1, String var2, Hashtable var3) {
      this.formId = var1;
      this.fid = var2;
      this.fieldMetas = var3;

      try {
         this.groupId = (String)var3.get("field_group_id");
         this.matrixId = (String)var3.get("matrix_id");
         this.fieldCol = (String)var3.get("matrix_field_col_num");
         this.fieldList = (String)var3.get("matrix_field_list");
         this.delimiter = (String)var3.get("matrix_delimiter");
         this.overWrite = false;
         if (var3.containsKey("validation") && ((String)var3.get("validation")).equalsIgnoreCase("false")) {
            this.overWrite = true;
         }
      } catch (Exception var5) {
         var5.printStackTrace();
      }

   }

   protected Object[] getMatrix(String var1, String var2) throws Exception {
      return defaultMatrixHandler.getInstance().getMatrix(var1, var2);
   }

   public boolean validate(int var1, String var2) {
      return false;
   }
}
