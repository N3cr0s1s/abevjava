package hu.piller.enykp.alogic.calculator.abevfunctionset_v1_0.interval;

public abstract class DefaultInterval implements IInterval {
   protected Object originalStartValue;
   protected Object originalEndValue;

   public Object getOriginalStartValue() {
      return this.originalStartValue;
   }

   public Object getOriginalEndValue() {
      return this.originalEndValue;
   }
}
