package hu.piller.enykp.alogic.calculator.abevfunctionset_v1_0.interval;

public interface IIntervalHandler extends Comparable {
   void setInterval(IInterval var1);

   IInterval getInterval();

   void setFid(String var1);

   String getFid();

   void setPageNumber(int var1);

   int getPageNumber();
}
