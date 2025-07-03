package hu.piller.enykp.alogic.checkpanel;

import java.util.Enumeration;
import javax.swing.AbstractListModel;

public class DefaultListModel2 extends AbstractListModel {
   private ChainedVector delegate = new ChainedVector();

   public int getSize() {
      return this.delegate.size();
   }

   public Object getElementAt(int var1) {
      return this.delegate.elementAt(var1);
   }

   public void copyInto(Object[] var1) {
      this.delegate.copyInto(var1);
   }

   public void trimToSize() {
      this.delegate.trimToSize();
   }

   public void ensureCapacity(int var1) {
      this.delegate.ensureCapacity(var1);
   }

   public void setSize(int var1) {
      int var2 = this.delegate.size();
      this.delegate.setSize(var1);
      if (var2 > var1) {
         this.fireIntervalRemoved(this, var1, var2 - 1);
      } else if (var2 < var1) {
         this.fireIntervalAdded(this, var2, var1 - 1);
      }

   }

   public int capacity() {
      return this.delegate.capacity();
   }

   public int size() {
      return this.delegate.size();
   }

   public boolean isEmpty() {
      return this.delegate.isEmpty();
   }

   public Enumeration elements() {
      return this.delegate.elements();
   }

   public boolean contains(Object var1) {
      return this.delegate.contains(var1);
   }

   public int indexOf(Object var1) {
      return this.delegate.indexOf(var1);
   }

   public int indexOf(Object var1, int var2) {
      return this.delegate.indexOf(var1, var2);
   }

   public int lastIndexOf(Object var1) {
      return this.delegate.lastIndexOf(var1);
   }

   public int lastIndexOf(Object var1, int var2) {
      return this.delegate.lastIndexOf(var1, var2);
   }

   public Object elementAt(int var1) {
      return this.delegate.elementAt(var1);
   }

   public Object firstElement() {
      return this.delegate.firstElement();
   }

   public Object lastElement() {
      return this.delegate.lastElement();
   }

   public void setElementAt(Object var1, int var2) {
      this.delegate.setElementAt(var1, var2);
      this.fireContentsChanged(this, var2, var2);
   }

   public void removeElementAt(int var1) {
      this.delegate.removeElementAt(var1);
      this.fireIntervalRemoved(this, var1, var1);
   }

   public void insertElementAt(Object var1, int var2) {
      this.delegate.insertElementAt(var1, var2);
      this.fireIntervalAdded(this, var2, var2);
   }

   public void addElement(Object var1) {
      int var2 = this.delegate.size();
      this.delegate.addElement(var1);
      this.fireIntervalAdded(this, var2, var2);
   }

   public boolean removeElement(Object var1) {
      int var2 = this.indexOf(var1);
      boolean var3 = this.delegate.removeElement(var1);
      if (var2 >= 0) {
         this.fireIntervalRemoved(this, var2, var2);
      }

      return var3;
   }

   public void removeAllElements() {
      int var1 = this.delegate.size() - 1;
      this.delegate.removeAllElements();
      if (var1 >= 0) {
         this.fireIntervalRemoved(this, 0, var1);
      }

   }

   public String toString() {
      return this.delegate.toString();
   }

   public Object[] toArray() {
      Object[] var1 = new Object[this.delegate.size()];
      this.delegate.copyInto(var1);
      return var1;
   }

   public Object get(int var1) {
      return this.delegate.elementAt(var1);
   }

   public Object set(int var1, Object var2) {
      Object var3 = this.delegate.elementAt(var1);
      this.delegate.setElementAt(var2, var1);
      this.fireContentsChanged(this, var1, var1);
      return var3;
   }

   public void add(int var1, Object var2) {
      this.delegate.insertElementAt(var2, var1);
      this.fireIntervalAdded(this, var1, var1);
   }

   public Object remove(int var1) {
      Object var2 = this.delegate.elementAt(var1);
      this.delegate.removeElementAt(var1);
      this.fireIntervalRemoved(this, var1, var1);
      return var2;
   }

   public void clear() {
      int var1 = this.delegate.size() - 1;
      this.delegate.removeAllElements();
      if (var1 >= 0) {
         this.fireIntervalRemoved(this, 0, var1);
      }

   }

   public void removeRange(int var1, int var2) {
      for(int var3 = var2; var3 >= var1; --var3) {
         this.delegate.removeElementAt(var3);
      }

      this.fireIntervalRemoved(this, var1, var2);
   }
}
