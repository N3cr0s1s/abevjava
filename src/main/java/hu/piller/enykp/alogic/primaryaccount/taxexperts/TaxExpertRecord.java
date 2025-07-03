package hu.piller.enykp.alogic.primaryaccount.taxexperts;

import hu.piller.enykp.alogic.primaryaccount.common.DefaultRecord;
import java.io.File;

public class TaxExpertRecord extends DefaultRecord {
   public TaxExpertRecord(TaxExpertRecordFactory var1, File var2, TaxExpertEnvelope var3) {
      super(var1, var2, var3);
   }

   public String getName() {
      Object var1 = this.getData().get("te_name");
      return var1 == null ? "" : var1.toString();
   }

   public String toString() {
      return this.getName();
   }
}
