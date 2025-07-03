package hu.piller.enykp.alogic.calculator.abevfunctionset_v1_0.interval.intervalImpl;

import hu.piller.enykp.alogic.calculator.abevfunctionset_v1_0.interval.DefaultInterval;
import hu.piller.enykp.alogic.calculator.abevfunctionset_v1_0.interval.IInterval;

public class DummyIntervalImpl extends DefaultInterval {
   public DummyIntervalImpl(Object var1, Object var2) {
      this.originalStartValue = var1;
      this.originalEndValue = var2;
   }

   public Object getStartValue() {
      return null;
   }

   public Object getEndValue() {
      return null;
   }

   public boolean setValues(Object var1, Object var2) {
      return false;
   }

   public int length() {
      return 0;
   }

   public int distance(IInterval var1) {
      return 0;
   }

   public void union(IInterval var1) {
   }

   public boolean isOverLap(IInterval var1) {
      return false;
   }

   public boolean isContinual(IInterval var1) {
      return false;
   }

   public int compareTo(Object var1) {
      return 0;
   }
}
