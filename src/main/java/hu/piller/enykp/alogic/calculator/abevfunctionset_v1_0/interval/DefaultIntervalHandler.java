package hu.piller.enykp.alogic.calculator.abevfunctionset_v1_0.interval;

public class DefaultIntervalHandler implements IIntervalHandler {
   private IInterval interval;
   private String fid;
   private int pageNumber;

   public DefaultIntervalHandler(IInterval var1, String var2, int var3) {
      this.interval = var1;
      this.fid = var2;
      this.pageNumber = var3;
   }

   public IInterval getInterval() {
      return this.interval;
   }

   public void setInterval(IInterval var1) {
      this.interval = var1;
   }

   public String getFid() {
      return this.fid;
   }

   public void setFid(String var1) {
      this.fid = var1;
   }

   public int getPageNumber() {
      return this.pageNumber;
   }

   public void setPageNumber(int var1) {
      this.pageNumber = var1;
   }

   public int compareTo(Object var1) {
      return this.interval.compareTo(((IIntervalHandler)var1).getInterval());
   }
}
