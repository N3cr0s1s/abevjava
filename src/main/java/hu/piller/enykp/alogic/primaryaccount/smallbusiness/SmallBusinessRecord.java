package hu.piller.enykp.alogic.primaryaccount.smallbusiness;

import hu.piller.enykp.alogic.primaryaccount.common.DefaultRecord;
import java.io.File;
import java.util.Hashtable;

public class SmallBusinessRecord extends DefaultRecord {
   public SmallBusinessRecord(SmallBusinessRecordFactory var1, File var2, SmallBusinessEnvelope var3) {
      super(var1, var2, var3);
   }

   public String getName() {
      Object var1 = this.getData().get("first_name");
      Object var2 = this.getData().get("last_name");
      var1 = var1 == null ? "" : var1;
      var2 = var2 == null ? "" : var2;
      return var1.toString() + " " + var2.toString();
   }

   public String getDescription(String var1) {
      if ("abev_new_panel_description".equalsIgnoreCase(var1)) {
         Hashtable var2 = this.getData();
         return "(ev) " + this.getName() + " " + this.getBraced(this.getString(var2.get("tax_number"))) + this.getBraced(this.getString(var2.get("tax_id")));
      } else {
         return super.getDescription(var1);
      }
   }

   public String toString() {
      return this.getName();
   }
}
