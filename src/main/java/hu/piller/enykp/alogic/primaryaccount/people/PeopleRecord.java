package hu.piller.enykp.alogic.primaryaccount.people;

import hu.piller.enykp.alogic.primaryaccount.common.DefaultRecord;
import java.io.File;
import java.util.Hashtable;

public class PeopleRecord extends DefaultRecord {
   public PeopleRecord(PeopleRecordFactory var1, File var2, PeopleEnvelope var3) {
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
         return "(m) " + this.getName() + " " + this.getBraced(this.getString(var2.get("tax_id")));
      } else {
         return super.getDescription(var1);
      }
   }

   public String toString() {
      return this.getName();
   }
}
