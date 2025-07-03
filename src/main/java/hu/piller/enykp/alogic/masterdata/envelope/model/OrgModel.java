package hu.piller.enykp.alogic.masterdata.envelope.model;

public class OrgModel {
   private String id;
   private String orgName;

   public OrgModel(String var1, String var2) {
      this.id = var1;
      this.orgName = var2;
   }

   public String getId() {
      return this.id;
   }

   public String getOrgName() {
      return this.orgName;
   }

   public String toString() {
      return this.orgName;
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 != null && this.getClass() == var1.getClass()) {
         OrgModel var2 = (OrgModel)var1;
         if (this.id != null) {
            if (!this.id.equals(var2.id)) {
               return false;
            }
         } else if (var2.id != null) {
            return false;
         }

         if (this.orgName != null) {
            if (this.orgName.equals(var2.orgName)) {
               return true;
            }
         } else if (var2.orgName == null) {
            return true;
         }

         return false;
      } else {
         return false;
      }
   }

   public int hashCode() {
      int var1 = this.id != null ? this.id.hashCode() : 0;
      var1 = 29 * var1 + (this.orgName != null ? this.orgName.hashCode() : 0);
      return var1;
   }
}
