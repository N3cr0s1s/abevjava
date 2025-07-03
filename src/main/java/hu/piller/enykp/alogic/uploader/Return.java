package hu.piller.enykp.alogic.uploader;

public class Return {
   private String causeOfFail = "";
   private FeltoltesValasz[] results = new FeltoltesValasz[0];

   public String getCauseOfFail() {
      return this.causeOfFail;
   }

   public void setCauseOfFail(String var1) {
      this.causeOfFail = var1;
   }

   public FeltoltesValasz[] getResults() {
      return this.results;
   }

   public void setResults(FeltoltesValasz[] var1) {
      this.results = var1;
   }
}
