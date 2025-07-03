package hu.piller.enykp.alogic.calculator.calculator_c;

public class Integer2 {
   int i;

   public Integer2() {
   }

   public Integer2(int var1) {
      this.i = var1;
   }

   public String toString() {
      return String.valueOf(this.i);
   }

   public int hashCode() {
      return this.i;
   }

   public boolean equals(Object var1) {
      if (var1 instanceof Integer2) {
         return ((Integer2)var1).i == this.i;
      } else {
         return super.equals(var1);
      }
   }
}
