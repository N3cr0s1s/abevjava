package hu.piller.enykp.alogic.masterdata.envelope.model;

public class ZipRange {
   private int from;
   private int to;

   public ZipRange() {
   }

   public ZipRange(int var1, int var2) {
      this.from = var1;
      this.to = var2;
   }

   public int getFrom() {
      return this.from;
   }

   public void setFrom(int var1) {
      this.from = var1;
   }

   public int getTo() {
      return this.to;
   }

   public void setTo(int var1) {
      this.to = var1;
   }

   public String toString() {
      return "ZIP: " + this.from + " - " + this.to + "";
   }

   public boolean isInRange(int var1) {
      return this.from <= var1 && var1 <= this.to;
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 != null && this.getClass() == var1.getClass()) {
         ZipRange var2 = (ZipRange)var1;
         if (this.from != var2.from) {
            return false;
         } else {
            return this.to == var2.to;
         }
      } else {
         return false;
      }
   }

   public int hashCode() {
      int var1 = this.from;
      var1 = 29 * var1 + this.to;
      return var1;
   }
}
