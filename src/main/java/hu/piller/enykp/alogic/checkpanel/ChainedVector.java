package hu.piller.enykp.alogic.checkpanel;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.AbstractList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.RandomAccess;
import java.util.Vector;

public class ChainedVector extends AbstractList implements List, RandomAccess, Cloneable, Serializable {
   private static final int Q = 10;
   private static final int DEFAULT_LINK_SIZE = 1024;
   private static final int DEFAULT_CAPACITY = 10;
   private int link_capacity;
   private Vector links;
   private int size;
   private int last_link_size;

   public ChainedVector(int var1, int var2) {
      this.link_capacity = 1024;
      this.last_link_size = 0;
      this.size = 0;
      this.links = new Vector(4096, 4096);
      this.buildChain(var1);
   }

   public ChainedVector(int var1) {
      this(var1, 1024);
   }

   public ChainedVector() {
      this(10);
   }

   public ChainedVector(Collection var1) {
      this(var1.size());
      this.addAll(var1);
   }

   protected void buildChain(int var1) {
      Vector var2 = this.links;
      int var3 = this.link_capacity;
      int var4 = var2.size();
      int var5 = (var1 - 1 >> 10) + 1;
      if (var4 < var5) {
         for(int var6 = var4; var6 < var5; ++var6) {
            var2.add(new Object[var3]);
         }
      }

   }

   public synchronized void copyInto(Object[] var1) {
      if (this.size > 0) {
         Vector var2 = this.links;
         int var3 = var2.size();
         int var4 = var3 - 1;
         int var6 = 0;
         int var7 = this.link_capacity;

         Object[] var5;
         for(int var8 = 0; var8 < var4; ++var8) {
            var5 = (Object[])((Object[])var2.get(var8));
            System.arraycopy(var5, 0, var1, var6, var7);
            var6 += var7;
         }

         if (var3 > 0) {
            var5 = (Object[])((Object[])var2.get(var4));
            System.arraycopy(var5, 0, var1, var6, this.last_link_size);
         }
      }

   }

   public synchronized void trimToSize() {
      ++this.modCount;
      this.trimToSize(this.size);
   }

   private void trimToSize(int var1) {
      int var2 = this.link_capacity;
      if (var1 > var2) {
         Vector var3 = this.links;
         int var4 = var3.size();

         for(int var5 = (var1 - 1 >> 10) + 1; var5 < var4; ++var5) {
            var3.remove(var5);
         }
      }

   }

   public synchronized void ensureCapacity(int var1) {
      ++this.modCount;
      Vector var2 = this.links;
      int var3 = var2.size();
      byte var4 = 10;
      if (var3 << var4 < var1) {
         int var5 = this.link_capacity;
         int var6 = var3;

         for(int var7 = (var1 - 1 >> var4) + 1; var6 < var7; ++var6) {
            var2.add(new Object[var5]);
         }
      }

   }

   public synchronized void setSize(int var1) {
      ++this.modCount;
      int var2 = this.size;
      if (var2 != var1) {
         if (var1 > var2) {
            this.ensureCapacity(var1);
         } else {
            this.trimToSize(var1);
         }

         int var3 = this.link_capacity;
         this.size = var1;
         this.last_link_size = (var1 - 1) % var3 + 1;
         Vector var5 = this.links;
         Object[] var4 = (Object[])((Object[])var5.get(var5.size() - 1));

         for(int var6 = this.last_link_size; var6 < var3; ++var6) {
            var4[var6] = null;
         }
      }

   }

   public synchronized int capacity() {
      return this.links.size() << 10;
   }

   public int size() {
      return this.size;
   }

   public synchronized boolean isEmpty() {
      return this.size == 0;
   }

   public Enumeration elements() {
      return new Enumeration() {
         int index = 0;
         Object[] link;
         int link_index;
         int i;

         {
            this.link_index = ChainedVector.this.link_capacity;
            this.i = 0;
         }

         public boolean hasMoreElements() {
            return this.index < ChainedVector.this.size;
         }

         public Object nextElement() {
            synchronized(ChainedVector.this) {
               if (this.index < ChainedVector.this.size) {
                  if (this.link_index >= ChainedVector.this.link_capacity) {
                     this.link = (Object[])((Object[])ChainedVector.this.links.get(this.i));
                     this.link_index = 0;
                  }

                  ++this.index;
                  return this.link[this.link_index++];
               }
            }

            throw new NoSuchElementException("ChainedVector Enumeration");
         }
      };
   }

   public boolean contains(Object var1) {
      return this.indexOf(var1, 0) >= 0;
   }

   public int indexOf(Object var1) {
      return this.indexOf(var1, 0);
   }

   public synchronized int indexOf(Object var1, int var2) {
      Vector var3 = this.links;
      int var5 = var3.size();
      int var6 = var2 >> 10;
      int var7 = this.link_capacity;
      int var8 = var2 % var7;
      Object[] var4;
      int var9;
      int var10;
      if (var1 == null) {
         for(var9 = var6; var9 < var5; ++var9) {
            var4 = (Object[])((Object[])var3.get(var9));

            for(var10 = var8; var10 < var7; ++var10) {
               if (var4[var10] == null) {
                  return (var9 << 10) + var10;
               }
            }

            var8 = 0;
         }
      } else {
         for(var9 = var6; var9 < var5; ++var9) {
            var4 = (Object[])((Object[])var3.get(var9));

            for(var10 = var8; var10 < var7; ++var10) {
               if (var1.equals(var4[var10])) {
                  return (var9 << 10) + var10;
               }
            }

            var8 = 0;
         }
      }

      return -1;
   }

   public synchronized int lastIndexOf(Object var1) {
      return this.lastIndexOf(var1, this.size - 1);
   }

   public synchronized int lastIndexOf(Object var1, int var2) {
      if (var2 >= this.size) {
         throw new IndexOutOfBoundsException(var2 + " >= " + this.size);
      } else {
         Vector var3 = this.links;
         int var5 = var2 >> 10;
         int var6 = this.link_capacity;
         int var7 = var2 % var6;
         int var8 = var6 - 1;
         Object[] var4;
         int var9;
         int var10;
         if (var1 == null) {
            for(var9 = var5; var9 >= 0; --var9) {
               var4 = (Object[])((Object[])var3.get(var9));

               for(var10 = var7; var10 >= 0; --var10) {
                  if (var4[var10] == null) {
                     return (var9 << 10) + var10;
                  }
               }

               var7 = var8;
            }
         } else {
            for(var9 = var5; var9 >= 0; --var9) {
               var4 = (Object[])((Object[])var3.get(var9));

               for(var10 = var7; var10 >= 0; --var10) {
                  if (var1.equals(var4[var10])) {
                     return (var9 << 10) + var10;
                  }
               }

               var7 = var8;
            }
         }

         return -1;
      }
   }

   public synchronized Object elementAt(int var1) {
      if (var1 >= this.size) {
         throw new ArrayIndexOutOfBoundsException(var1 + " >= " + this.size);
      } else {
         Object[] var2 = (Object[])((Object[])this.links.get(var1 >> 10));
         return var2[var1 % this.link_capacity];
      }
   }

   public synchronized Object firstElement() {
      if (this.size == 0) {
         throw new NoSuchElementException();
      } else {
         Object[] var1 = (Object[])((Object[])this.links.get(0));
         return var1[0];
      }
   }

   public synchronized Object lastElement() {
      if (this.size == 0) {
         throw new NoSuchElementException();
      } else {
         Object[] var1 = (Object[])((Object[])this.links.get(this.links.size() - 1));
         return var1[this.last_link_size - 1];
      }
   }

   public synchronized void setElementAt(Object var1, int var2) {
      if (var2 >= this.size) {
         throw new ArrayIndexOutOfBoundsException(var2 + " >= " + this.size);
      } else {
         Object[] var3 = (Object[])((Object[])this.links.get(var2 >> 10));
         var3[var2 % this.link_capacity] = var1;
      }
   }

   public synchronized void removeElementAt(int var1) {
      ++this.modCount;
      this.removeElementAt_(var1, 1);
   }

   private Object removeElementAt_(int var1, int var2) {
      int var3 = this.size;
      int var4 = this.link_capacity;
      Vector var5 = this.links;
      int var6 = var1 >> 10;
      int var7 = var1 + var2 >> 10;
      int var8 = var1 % var4;
      int var9 = (var1 + var2) % var4;
      Object[] var10 = (Object[])((Object[])var5.get(var6));
      Object[] var11 = (Object[])((Object[])var5.get(var7));
      Object var12 = var10[var8];
      int var13 = var1;

      for(int var14 = var3 - var2; var13 < var14; ++var13) {
         var10[var8++] = var11[var9++];
         if (var9 == var4) {
            ++var7;
            var11 = (Object[])((Object[])var5.get(var7));
            var9 = 0;
         }

         if (var8 == var4) {
            ++var6;
            var10 = (Object[])((Object[])var5.get(var6));
            var8 = 0;
         }
      }

      var3 -= var2;
      this.size = var3;
      this.last_link_size = (var3 - 1) % var4 + 1;
      return var12;
   }

   public synchronized void insertElementAt(Object var1, int var2) {
      ++this.modCount;
      this.insertElementAt_(var1, var2, false);
   }

   private void insertElementAt_(Object var1, int var2, boolean var3) {
      int var4;
      if (var3 && var1 instanceof Collection) {
         var4 = ((Collection)var1).size();
      } else {
         var4 = 1;
      }

      int var5 = this.size + var4;
      this.ensureCapacity(var5);
      int var6 = var5 - 1;
      int var7 = this.link_capacity;
      int var8 = var7 - 1;
      Vector var9 = this.links;
      int var10 = var6 >> 10;
      int var11 = var6 - var4 >> 10;
      int var12 = var6 % var7;
      int var13 = (var6 - var4) % var7;
      Object[] var14 = (Object[])((Object[])var9.get(var10));
      Object[] var15 = (Object[])((Object[])var9.get(var11));

      for(int var16 = var6 - var4; var16 >= var2; --var16) {
         var14[var12--] = var15[var13--];
         if (var13 < 0) {
            --var11;
            var15 = (Object[])((Object[])var9.get(var11));
            var13 = var8;
         }

         if (var12 < 0) {
            --var10;
            var14 = (Object[])((Object[])var9.get(var10));
            var12 = var8;
         }
      }

      this.size = var5;
      this.last_link_size = (var5 - 1) % var7 + 1;
      if (var3 && var1 instanceof Collection) {
         Collection var19 = (Collection)var1;
         Iterator var17 = var19.iterator();
         var10 = var2 >> 10;
         var14 = (Object[])((Object[])var9.get(var10));
         var12 = var2 % var7;

         for(int var18 = var2; var18 < var5 && var17.hasNext(); ++var18) {
            var14[var12++] = var17.next();
            if (var12 == var7) {
               ++var10;
               var14 = (Object[])((Object[])var9.get(var10));
               var12 = 0;
            }
         }
      } else {
         this.setElementAt(var1, var2);
      }

   }

   public synchronized void addElement(Object var1) {
      ++this.modCount;
      int var2 = this.size;
      int var3 = var2;
      Vector var5 = this.links;
      ++var2;
      this.ensureCapacity(var2);
      Object[] var6 = (Object[])((Object[])var5.get(var3 >> 10));
      int var4 = var3 % this.link_capacity;
      var6[var4] = var1;
      this.last_link_size = var4 + 1;
      this.size = var2;
   }

   public synchronized boolean removeElement(Object var1) {
      ++this.modCount;
      int var2 = this.indexOf(var1);
      if (var2 >= 0) {
         this.removeElementAt(var2);
         return true;
      } else {
         return false;
      }
   }

   public synchronized void removeAllElements() {
      ++this.modCount;
      this.links.clear();
      this.size = 0;
      this.last_link_size = 0;
   }

   public synchronized Object clone() throws CloneNotSupportedException {
      try {
         ChainedVector var1 = (ChainedVector)super.clone();
         var1.links = new Vector(this.links);
         var1.size = this.size;
         var1.last_link_size = this.last_link_size;
         var1.modCount = 0;
         return var1;
      } catch (CloneNotSupportedException var2) {
         throw new InternalError();
      }
   }

   public synchronized Object[] toArray() {
      Object[] var1 = new Object[this.size];
      this.copyInto(var1);
      return var1;
   }

   public synchronized Object[] toArray(Object[] var1) {
      if (var1.length < this.size) {
         var1 = (Object[])((Object[])Array.newInstance(var1.getClass().getComponentType(), this.size));
      }

      this.copyInto(var1);
      return var1;
   }

   public Object get(int var1) {
      return this.elementAt(var1);
   }

   public synchronized Object set(int var1, Object var2) {
      if (var1 >= this.size) {
         throw new ArrayIndexOutOfBoundsException(var1);
      } else {
         Object[] var3 = (Object[])((Object[])this.links.get(var1 >> 10));
         int var4 = var1 % this.link_capacity;
         Object var5 = var3[var4];
         var3[var4] = var2;
         return var5;
      }
   }

   public synchronized boolean add(Object var1) {
      this.addElement(var1);
      return true;
   }

   public boolean remove(Object var1) {
      return this.removeElement(var1);
   }

   public void add(int var1, Object var2) {
      this.insertElementAt_(var2, var1, false);
   }

   public synchronized Object remove(int var1) {
      ++this.modCount;
      if (var1 >= this.size) {
         throw new ArrayIndexOutOfBoundsException(var1);
      } else {
         return this.removeElementAt_(var1, 1);
      }
   }

   public void clear() {
      this.removeAllElements();
   }

   public synchronized boolean containsAll(Collection var1) {
      return super.containsAll(var1);
   }

   public synchronized boolean addAll(Collection var1) {
      return this.addAll(this.size, var1);
   }

   public synchronized boolean removeAll(Collection var1) {
      Vector var2 = this.links;
      int var3 = var2.size();
      int var5 = this.link_capacity;
      int var6 = this.size;
      int var7 = 0;

      for(int var8 = 0; var8 < var3 && var7 < var6; ++var8) {
         Object[] var4 = (Object[])((Object[])var2.get(var8));

         for(int var9 = 0; var9 < var5 && var7 < var6; ++var9) {
            if (var1.contains(var4[var9])) {
               this.removeElementAt(var7);
               --var9;
               --var7;
            }

            ++var7;
         }
      }

      return true;
   }

   public synchronized boolean retainAll(Collection var1) {
      return super.retainAll(var1);
   }

   public synchronized boolean addAll(int var1, Collection var2) {
      ++this.modCount;
      this.insertElementAt_(var2, var1, true);
      return true;
   }

   public synchronized boolean equals(Object var1) {
      return super.equals(var1);
   }

   public synchronized int hashCode() {
      return super.hashCode();
   }

   public synchronized String toString() {
      return super.toString();
   }

   public synchronized List subList(int var1, int var2) {
      return Collections.synchronizedList(super.subList(var1, var2));
   }

   protected void removeRange(int var1, int var2) {
      ++this.modCount;
      this.removeElementAt_(var1, var2 - var1);
   }

   private synchronized void writeObject(ObjectOutputStream var1) throws IOException {
      var1.defaultWriteObject();
   }
}
