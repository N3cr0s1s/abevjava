package hu.piller.enykp.util.base;

public abstract class List {
   private Object[] list;
   private int index;

   public List(int var1) {
      this.list = new Object[var1];
      this.index = 0;
   }

   protected boolean add(Object var1, boolean var2) {
      if (var2 && this.index == this.list.length) {
         this.makePlace2(0);
         --this.index;
      }

      if (this.index < this.list.length) {
         this.list[this.index++] = var1;
         return true;
      } else {
         return false;
      }
   }

   protected boolean insert(Object var1, int var2, boolean var3) {
      if (var3 && this.index == this.list.length) {
         this.makePlace(--this.index);
      }

      if (this.index < this.list.length && var2 < this.list.length) {
         this.makePlace(var2);
         this.list[var2] = var1;
         ++this.index;
         return true;
      } else {
         return false;
      }
   }

   protected boolean set(Object var1, int var2) {
      if (var2 < this.index) {
         this.list[var2] = var1;
         return true;
      } else {
         return false;
      }
   }

   private void makePlace(int var1) {
      if (this.index - var1 - 1 > 0) {
         System.arraycopy(this.list, var1, this.list, var1 + 1, this.index - var1 - 1);
      }

   }

   private void makePlace2(int var1) {
      if (this.index - var1 - 1 > 0) {
         System.arraycopy(this.list, var1 + 1, this.list, var1, this.index - var1 - 1);
      }

   }

   protected Object remove() {
      return this.index < this.list.length ? this.list[--this.index] : null;
   }

   protected Object remove(int var1) {
      if (this.index <= this.list.length && var1 < this.list.length && var1 >= 0) {
         Object var2 = this.list[var1];
         this.makePlace(var1);
         --this.index;
         this.list[this.index] = null;
         return var2;
      } else {
         return null;
      }
   }

   protected void remove(Object var1) {
      this.remove(this.getIndex(var1));
   }

   protected void clear() {
      int var1 = 0;

      for(int var2 = this.list.length; var1 < var2; ++var1) {
         this.list[var1] = null;
      }

      this.index = 0;
   }

   protected boolean contain(Object var1) {
      return this.getIndex(var1) >= 0;
   }

   protected int getIndex(Object var1) {
      for(int var2 = 0; var2 < this.index; ++var2) {
         if (var1 == this.list[var2]) {
            return var2;
         }
      }

      return -1;
   }

   protected int size() {
      return this.index;
   }

   protected Object[] items() {
      return this.list;
   }

   protected Object[] filter(Object var1) {
      int var3 = 0;
      int var4 = 0;

      int var5;
      for(var5 = 0; var5 < this.index; ++var5) {
         if (this.compare(var1, this.list[var5])) {
            ++var3;
         }
      }

      Object[] var2 = new Object[var3];

      for(var5 = 0; var5 < this.index; ++var5) {
         if (this.compare(var1, this.list[var5])) {
            var2[var4++] = this.list[var5];
         }
      }

      return var2;
   }

   protected Object find(Object var1) {
      for(int var2 = 0; var2 < this.index; ++var2) {
         if (this.compare(var1, this.list[var2])) {
            return this.list[var2];
         }
      }

      return null;
   }

   protected abstract boolean compare(Object var1, Object var2);
}
