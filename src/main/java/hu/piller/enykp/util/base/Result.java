package hu.piller.enykp.util.base;

import java.util.Vector;

public class Result {
   private boolean isOk = true;
   private boolean specialAvdhUniqueAtcNameError = false;
   public Vector errorList = new Vector();

   public boolean isOk() {
      return this.isOk;
   }

   public void setOk(boolean var1) {
      this.isOk = var1;
   }

   public void setErrorList(Vector var1) {
      this.errorList = var1;
   }

   public boolean isSpecialAvdhUniqueAtcNameError() {
      return this.specialAvdhUniqueAtcNameError;
   }

   public void setSpecialAvdhUniqueAtcNameError(boolean var1) {
      this.specialAvdhUniqueAtcNameError = var1;
   }
}
