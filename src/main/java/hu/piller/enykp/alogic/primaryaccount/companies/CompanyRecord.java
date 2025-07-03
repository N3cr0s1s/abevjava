package hu.piller.enykp.alogic.primaryaccount.companies;

import hu.piller.enykp.alogic.primaryaccount.common.DefaultRecord;
import java.io.File;
import java.util.Hashtable;

public class CompanyRecord extends DefaultRecord {
   public CompanyRecord(CompanyRecordFactory var1, File var2, CompanyEnvelope var3) {
      super(var1, var2, var3);
   }

   public String getName() {
      Object var1 = this.getData().get("name");
      return var1 == null ? "" : var1.toString();
   }

   public String getDescription(String var1) {
      if ("abev_new_panel_description".equalsIgnoreCase(var1)) {
         Hashtable var2 = this.getData();
         return "(t) " + this.getName() + " " + this.getBraced(this.getString(var2.get("tax_number")));
      } else {
         return super.getDescription(var1);
      }
   }

   public String toString() {
      return this.getName();
   }
}
