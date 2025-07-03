package hu.piller.enykp.alogic.calculator.lookup.lookuplistproviderimpl;

import hu.piller.enykp.alogic.calculator.matrices.defaultMatrixHandler;
import java.util.Hashtable;

public class LookupListProviderItem extends DefaultLookupListProvider {
   public LookupListProviderItem(String var1, String var2, Hashtable var3) {
      this.formId = var1;
      this.fid = var2;
      this.fieldMetas = var3;
   }

   protected Object[] getMatrix(String var1, String var2) throws Exception {
      return defaultMatrixHandler.getInstance().getMatrix(var1, var2);
   }

   public boolean validate(int var1, String var2) {
      return false;
   }
}
