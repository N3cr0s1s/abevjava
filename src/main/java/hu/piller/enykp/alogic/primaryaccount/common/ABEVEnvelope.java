package hu.piller.enykp.alogic.primaryaccount.common;

import java.util.Hashtable;

public class ABEVEnvelope extends DefaultEnvelope {
   public static final int STABLE_ADDRESS = 0;
   public static final int MAIL_ADDRESS = 1;
   protected IRecord receiver_record;

   public void setReceiverRecord(IRecord var1) {
      this.receiver_record = var1;
   }

   protected void getReceiverData(Hashtable var1) {
      if (this.receiver_record != null) {
         Hashtable var2 = this.receiver_record.getData();
         var1.put("c_címzett", this.getValue(var2, ""));
         var1.put("c_város", this.getValue(var2, ""));
         var1.put("c_pf", this.getValue(var2, ""));
         var1.put("c_irsz", this.getValue(var2, ""));
      }

   }

   protected String getValue(Hashtable var1, Object var2) {
      return var1 != null ? this.getString(var1.get(var2)) : "";
   }

   protected String getString(Object var1) {
      return var1 == null ? "" : var1.toString();
   }

   protected String getDataTag(Hashtable var1, Object var2, String var3, String var4) {
      String var5 = this.getValue(var1, var2);
      if (var5.length() != 0) {
         var3 = var3 + (var3.trim().length() == 0 ? "" : " ") + var5 + (var4 == null ? "" : var4);
      }

      return var3;
   }

   public Hashtable getEnvelopeData(IRecord var1, int var2) {
      super.setDataId(var2);
      return super.getEnvelopeData(var1, var2);
   }
}
