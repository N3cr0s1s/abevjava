package hu.piller.enykp.util.base;

public class DoubleListValue {
   public Object value;
   public int index;

   public DoubleListValue(Object var1, int var2) {
      this.value = var1;
      this.index = var2;
   }

   public DoubleListValue() {
   }

   public String toString() {
      return this.value == null ? "" : this.value.toString();
   }
}
