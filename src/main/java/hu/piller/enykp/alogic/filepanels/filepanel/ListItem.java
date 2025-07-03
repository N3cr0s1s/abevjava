package hu.piller.enykp.alogic.filepanels.filepanel;

import java.io.File;
import javax.swing.Icon;

public class ListItem implements Cloneable {
   private Object item;
   private Object item2;
   private Icon icon;
   private Object text;
   private boolean is_visible = true;

   public ListItem(Object var1, Icon var2) {
      this.setItem(var1);
      this.setIcon(var2);
   }

   public ListItem(Object var1, Icon var2, Object var3) {
      this.setItem(var1);
      this.setIcon(var2);
      this.setText(var3);
   }

   public ListItem(Object var1, Icon var2, Object var3, Object var4) {
      this.setItem(var1);
      this.setSecondItem(var4);
      this.setIcon(var2);
      this.setText(var3);
   }

   public void setItem(Object var1) {
      this.item = var1;
   }

   public Object getItem() {
      return this.item;
   }

   public void setSecondItem(Object var1) {
      this.item2 = var1;
   }

   public Object getSecondItem() {
      return this.item2;
   }

   public void setIcon(Icon var1) {
      this.icon = var1;
   }

   public Icon getIcon() {
      return this.icon;
   }

   public void setText(Object var1) {
      this.text = var1;
   }

   public Object getText() {
      return this.text;
   }

   public void setVisible(boolean var1) {
      this.is_visible = var1;
   }

   public boolean isVisible() {
      return this.is_visible;
   }

   public boolean equals(Object var1) {
      if (this.item == null) {
         return var1 == null || var1.equals(this.item);
      } else if (!(var1 instanceof ListItem)) {
         return this.item.equals(var1);
      } else {
         return var1 == this || this.item.equals(((ListItem)var1).getItem());
      }
   }

   public Object clone() throws CloneNotSupportedException {
      return super.clone();
   }

   public String toString() {
      if (this.text != null) {
         return this.text.toString();
      } else if (this.item instanceof File) {
         File var1 = (File)this.item;
         return var1.getName().equals("") ? var1.getPath() : var1.getName();
      } else {
         return this.item != null ? this.item.toString() : "???";
      }
   }
}
