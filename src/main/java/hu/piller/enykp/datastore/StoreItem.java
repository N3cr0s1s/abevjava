package hu.piller.enykp.datastore;

import java.io.Serializable;
import java.util.Vector;

public class StoreItem implements Serializable {
   public String code;
   public int index;
   public Object value;
   private String sum = null;
   private Vector details = null;

   public StoreItem() {
   }

   public StoreItem(String var1, int var2, Object var3) {
      this.code = var1;
      this.index = var2;
      this.value = var3;
   }

   public StoreItem(Object[] var1) {
      try {
         this.code = (String)var1[0];
         this.index = (Integer)var1[1];
         this.value = var1[2];
      } catch (Exception var3) {
         this.code = null;
         this.index = 0;
         var1 = null;
      }

   }

   public boolean equals(Object var1) {
      if (var1 instanceof StoreItem) {
         StoreItem var2 = (StoreItem)var1;
         if (this.code.equals(var2.code) && this.index == var2.index) {
            return true;
         }
      }

      return false;
   }

   public String toString() {
      return this.index + "_" + this.code;
   }

   public Object[] toArray() {
      Object[] var1 = new Object[]{this.code, new Integer(this.index), this.value};
      return var1;
   }

   public int hashCode() {
      return (this.index + "_" + this.code).hashCode();
   }

   public Vector getDetails() {
      return this.details;
   }

   public String getDetailSum() {
      return this.sum;
   }

   public void setDetailSum(String var1) {
      this.sum = var1;
   }

   public void setDetail(Vector var1) {
      this.details = var1;
   }
}
