package hu.piller.enykp.alogic.uploader;

public class FeltoltesValasz {
   private String fileName;
   private boolean stored;
   private String filingNumber;
   private String errorMsg;

   public FeltoltesValasz() {
   }

   public FeltoltesValasz(String var1, boolean var2, String var3, String var4) {
      this.fileName = var1;
      this.stored = var2;
      this.filingNumber = var3;
      this.errorMsg = var4;
   }

   public String getFileName() {
      return this.fileName;
   }

   public boolean isStored() {
      return this.stored;
   }

   public String getFilingNumber() {
      return this.filingNumber;
   }

   public String getErrorMsg() {
      return this.errorMsg;
   }

   public void setFileName(String var1) {
      this.fileName = var1;
   }

   public void setStored(boolean var1) {
      this.stored = var1;
   }

   public void setFilingNumber(String var1) {
      this.filingNumber = var1;
   }

   public void setErrorMsg(String var1) {
      this.errorMsg = var1;
   }

   public String toString() {
      return "[" + "fileName = " + this.fileName + ", " + "stored = " + this.stored + "," + "filingNumber = " + this.filingNumber + ", " + "errorMsg = " + this.errorMsg + "]";
   }
}
