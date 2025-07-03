package hu.piller.krtitok.gui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager2;
import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;

public class AbsoluteLayout implements LayoutManager2, Serializable {
   static final long serialVersionUID = -1919857869177070440L;
   protected Hashtable constraints = new Hashtable();

   public void addLayoutComponent(String name, Component comp) {
      throw new IllegalArgumentException();
   }

   public void removeLayoutComponent(Component comp) {
      this.constraints.remove(comp);
   }

   public Dimension preferredLayoutSize(Container parent) {
      int maxWidth = 0;
      int maxHeight = 0;
      Enumeration e = this.constraints.keys();

      while(e.hasMoreElements()) {
         Component comp = (Component)e.nextElement();
         AbsoluteConstraints ac = (AbsoluteConstraints)this.constraints.get(comp);
         Dimension size = comp.getPreferredSize();
         int width = ac.getWidth();
         if (width == -1) {
            width = size.width;
         }

         int height = ac.getHeight();
         if (height == -1) {
            height = size.height;
         }

         if (ac.x + width > maxWidth) {
            maxWidth = ac.x + width;
         }

         if (ac.y + height > maxHeight) {
            maxHeight = ac.y + height;
         }
      }

      return new Dimension(maxWidth, maxHeight);
   }

   public Dimension minimumLayoutSize(Container parent) {
      int maxWidth = 0;
      int maxHeight = 0;
      Enumeration e = this.constraints.keys();

      while(e.hasMoreElements()) {
         Component comp = (Component)e.nextElement();
         AbsoluteConstraints ac = (AbsoluteConstraints)this.constraints.get(comp);
         Dimension size = comp.getMinimumSize();
         int width = ac.getWidth();
         if (width == -1) {
            width = size.width;
         }

         int height = ac.getHeight();
         if (height == -1) {
            height = size.height;
         }

         if (ac.x + width > maxWidth) {
            maxWidth = ac.x + width;
         }

         if (ac.y + height > maxHeight) {
            maxHeight = ac.y + height;
         }
      }

      return new Dimension(maxWidth, maxHeight);
   }

   public void layoutContainer(Container parent) {
      Component comp;
      AbsoluteConstraints ac;
      int width;
      int height;
      for(Enumeration e = this.constraints.keys(); e.hasMoreElements(); comp.setBounds(ac.x, ac.y, width, height)) {
         comp = (Component)e.nextElement();
         ac = (AbsoluteConstraints)this.constraints.get(comp);
         Dimension size = comp.getPreferredSize();
         width = ac.getWidth();
         if (width == -1) {
            width = size.width;
         }

         height = ac.getHeight();
         if (height == -1) {
            height = size.height;
         }
      }

   }

   public void addLayoutComponent(Component comp, Object constr) {
      if (!(constr instanceof AbsoluteConstraints)) {
         throw new IllegalArgumentException();
      } else {
         this.constraints.put(comp, constr);
      }
   }

   public Dimension maximumLayoutSize(Container target) {
      return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
   }

   public float getLayoutAlignmentX(Container target) {
      return 0.0F;
   }

   public float getLayoutAlignmentY(Container target) {
      return 0.0F;
   }

   public void invalidateLayout(Container target) {
   }
}
