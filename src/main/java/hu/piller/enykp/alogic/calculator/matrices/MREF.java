package hu.piller.enykp.alogic.calculator.matrices;

import java.util.Scanner;

public class MREF implements Cloneable {
   public static String NS_BELSO = "belso";
   public static String NS_ALTALANOS = "altalanos";
   private String scope;
   private String formID;
   private String matrixID;

   public MREF() {
   }

   public MREF(String var1) {
      if (var1 != null) {
         this.parseMREF(var1);
      }

   }

   public MREF(String var1, String var2, String var3) {
      this.scope = var1;
      this.formID = var2;
      this.matrixID = var3;
   }

   public String getScope() {
      return this.scope;
   }

   public void setScope(String var1) {
      this.scope = var1;
   }

   public String getFormID() {
      return this.formID;
   }

   public void setFormID(String var1) {
      this.formID = var1;
   }

   public String getMatrixID() {
      return this.matrixID;
   }

   public void setMatrixID(String var1) {
      this.matrixID = var1;
   }

   private void parseMREF(String var1) {
      Scanner var2 = new Scanner(var1);
      var2.useDelimiter(":");

      while(var2.hasNext()) {
         String var3 = var2.next();
         this.scope = var3;
         if (NS_BELSO.equals(var3)) {
            if (var2.hasNext()) {
               this.formID = var2.next();
               if (var2.hasNext()) {
                  this.matrixID = var2.next();
               }
            }
         } else if (var2.hasNext()) {
            this.matrixID = var2.next();
         }
      }

   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      if (this.scope != null) {
         var1.append(this.scope);
      }

      var1.append(":");
      if (NS_BELSO.equals(this.scope)) {
         if (this.formID != null) {
            var1.append(this.formID);
         }

         var1.append(":");
      }

      if (this.matrixID != null) {
         var1.append(this.matrixID);
      }

      return var1.toString();
   }

   public boolean equals(Object var1) {
      return var1 instanceof MREF && this.toString().equals(((MREF)var1).toString());
   }

   public int hashCode() {
      return this.toString().hashCode();
   }

   public MREF clone() {
      MREF var1 = new MREF();
      var1.setFormID(this.getFormID());
      var1.setMatrixID(this.getMatrixID());
      var1.setScope(this.getScope());
      return var1;
   }
}
