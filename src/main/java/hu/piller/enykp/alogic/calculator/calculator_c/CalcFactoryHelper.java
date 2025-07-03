package hu.piller.enykp.alogic.calculator.calculator_c;

import java.util.Iterator;
import java.util.Vector;

public class CalcFactoryHelper {
   public static Iterator getCollectionIterator(Object var0) {
      if (var0 instanceof Object[]) {
         return new CalcFactoryHelper.CollectionIterator(var0);
      } else {
         return (Iterator)(var0 instanceof Vector ? ((Vector)var0).iterator() : new CalcFactoryHelper.CollectionIterator(var0));
      }
   }

   private static final class CollectionIterator implements Iterator {
      private Object[] coll = null;
      private int index;

      CollectionIterator(Object var1) {
         if (var1 instanceof Object[]) {
            this.coll = (Object[])((Object[])var1);
            this.index = 0;
         } else {
            this.coll = null;
            this.index = 0;
         }

      }

      public void remove() {
      }

      public boolean hasNext() {
         if (this.coll != null) {
            return this.index < this.coll.length;
         } else {
            return false;
         }
      }

      public Object next() {
         return this.hasNext() ? this.coll[this.index++] : null;
      }
   }
}
