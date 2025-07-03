package hu.piller.enykp.alogic.primaryaccount.people;

import hu.piller.enykp.alogic.primaryaccount.common.ABEVEnvelope;
import java.util.Hashtable;

public class PeopleEnvelope extends ABEVEnvelope {
   protected Hashtable getData(int var1) {
      Hashtable var2 = this.envelope_data;
      this.initializeData(var2);
      if (this.record != null) {
         Hashtable var3 = this.record.getData();
         var2.put("f_feladó", this.getValue(var3, "first_name") + " " + this.getValue(var3, "last_name"));
         var2.put("f_utca", this.getStreet(var3, var1));
         if (var1 == 0) {
            var2.put("f_város", this.getValue(var3, "s_settlement"));
            var2.put("f_irsz", this.getValue(var3, "s_zip"));
         } else if (var1 == 1) {
            var2.put("f_város", this.getValue(var3, "m_settlement"));
            var2.put("f_irsz", this.getValue(var3, "m_zip"));
         }
      }

      this.getReceiverData(var2);
      return var2;
   }

   private String getStreet(Hashtable var1, int var2) {
      String var3 = "";
      if (var2 == 0) {
         var3 = this.getDataTag(var1, "s_public_place", var3, (String)null);
         var3 = this.getDataTag(var1, "s_public_place_type", var3, (String)null);
         var3 = this.getDataTag(var1, "s_house_number", var3, ".");
         var3 = this.getDataTag(var1, "s_building", var3, "ép.");
         var3 = this.getDataTag(var1, "s_staircase", var3, "lh.");
         var3 = this.getDataTag(var1, "s_level", var3, "em.");
         var3 = this.getDataTag(var1, "s_door", var3, (String)null);
      } else if (var2 == 1) {
         var3 = this.getDataTag(var1, "m_public_place", var3, (String)null);
         var3 = this.getDataTag(var1, "m_public_place_type", var3, (String)null);
         var3 = this.getDataTag(var1, "m_house_number", var3, ".");
         var3 = this.getDataTag(var1, "m_building", var3, "ép.");
         var3 = this.getDataTag(var1, "m_staircase", var3, "lh.");
         var3 = this.getDataTag(var1, "m_level", var3, "em.");
         var3 = this.getDataTag(var1, "m_door", var3, (String)null);
      }

      return var3;
   }
}
