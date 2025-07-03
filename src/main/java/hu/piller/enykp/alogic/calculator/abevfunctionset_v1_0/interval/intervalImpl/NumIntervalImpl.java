package hu.piller.enykp.alogic.calculator.abevfunctionset_v1_0.interval.intervalImpl;

import hu.piller.enykp.alogic.calculator.abevfunctionset_v1_0.interval.DefaultInterval;
import hu.piller.enykp.alogic.calculator.abevfunctionset_v1_0.interval.IInterval;

public class NumIntervalImpl extends DefaultInterval {
   private Integer start;
   private Integer end;

   public NumIntervalImpl(Object var1, Object var2) {
      if (getInteger(var2) > getInteger(var1)) {
         this.setValues(var1, var2);
      } else {
         this.setValues(var2, var1);
      }

   }

   public boolean setValues(Object var1, Object var2) {
      this.originalStartValue = var1;
      this.originalEndValue = var2;
      this.start = getInteger(var1);
      this.end = getInteger(var2);
      return true;
   }

   public Integer getStartValue() {
      return this.start;
   }

   public Integer getEndValue() {
      return this.end;
   }

   public int length() {
      return this.end - this.start;
   }

   public int distance(IInterval var1) {
      return (Integer)var1.getStartValue() - this.end;
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
      IInterval var2 = (IInterval)var1;
      if (this.end < (Integer)var2.getStartValue()) {
         return -1;
      } else {
         return this.start > (Integer)var2.getEndValue() ? 1 : 0;
      }
   }

   public static Integer getInteger(Object var0) {
      if (var0 instanceof Integer) {
         return (Integer)var0;
      } else {
         return var0 == null ? null : Integer.valueOf(var0.toString());
      }
   }
}
