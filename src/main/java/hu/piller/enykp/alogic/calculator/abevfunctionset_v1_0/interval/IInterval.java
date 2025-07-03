package hu.piller.enykp.alogic.calculator.abevfunctionset_v1_0.interval;

public interface IInterval<T> extends Comparable {
   T getStartValue();

   Object getOriginalStartValue();

   T getEndValue();

   Object getOriginalEndValue();

   boolean setValues(T var1, T var2);

   int length();

   int distance(IInterval var1);

   void union(IInterval<T> var1);

   boolean isOverLap(IInterval<T> var1);

   boolean isContinual(IInterval<T> var1);
}
