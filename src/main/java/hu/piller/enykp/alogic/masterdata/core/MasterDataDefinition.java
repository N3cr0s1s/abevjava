package hu.piller.enykp.alogic.masterdata.core;

public class MasterDataDefinition {
   private String key;
   private String org;
   private String type;

   public MasterDataDefinition(String var1, String var2, String var3) {
      this.key = var1;
      this.org = var2;
      this.type = var3;
   }

   public String getKey() {
      return this.key;
   }

   public String getOrg() {
      return this.org;
   }

   public String getType() {
      return this.type;
   }

   public void setOrg(String var1) {
      this.org = var1;
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append("[");
      var1.append("key: ");
      var1.append(this.key);
      var1.append(" org: ");
      var1.append(this.org);
      var1.append(" type: ");
      var1.append(this.type);
      var1.append("]");
      return var1.toString();
   }
}
