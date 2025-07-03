package hu.piller.enykp.gui;

import java.awt.Component;
import java.awt.Container;
import javax.swing.Spring;
import javax.swing.SpringLayout;
import javax.swing.SpringLayout.Constraints;

public class SpringUtilities {
   public static void printSizes(Component var0) {
      System.out.println("minimumSize = " + var0.getMinimumSize());
      System.out.println("preferredSize = " + var0.getPreferredSize());
      System.out.println("maximumSize = " + var0.getMaximumSize());
   }

   public static void makeGrid(Container var0, int var1, int var2, int var3, int var4, int var5, int var6) {
      SpringLayout var7;
      try {
         var7 = (SpringLayout)var0.getLayout();
      } catch (ClassCastException var19) {
         System.err.println("The first argument to makeGrid must use SpringLayout.");
         return;
      }

      Spring var8 = Spring.constant(var5);
      Spring var9 = Spring.constant(var6);
      Spring var10 = Spring.constant(var3);
      Spring var11 = Spring.constant(var4);
      int var12 = var1 * var2;
      Spring var13 = var7.getConstraints(var0.getComponent(0)).getWidth();
      Spring var14 = var7.getConstraints(var0.getComponent(0)).getHeight();

      int var15;
      Constraints var16;
      for(var15 = 1; var15 < var12; ++var15) {
         var16 = var7.getConstraints(var0.getComponent(var15));
         var13 = Spring.max(var13, var16.getWidth());
         var14 = Spring.max(var14, var16.getHeight());
      }

      for(var15 = 0; var15 < var12; ++var15) {
         var16 = var7.getConstraints(var0.getComponent(var15));
         var16.setWidth(var13);
         var16.setHeight(var14);
      }

      Constraints var20 = null;
      var16 = null;

      for(int var17 = 0; var17 < var12; ++var17) {
         Constraints var18 = var7.getConstraints(var0.getComponent(var17));
         if (var17 % var2 == 0) {
            var16 = var20;
            var18.setX(var10);
         } else {
            var18.setX(Spring.sum(var20.getConstraint("East"), var8));
         }

         if (var17 / var2 == 0) {
            var18.setY(var11);
         } else {
            var18.setY(Spring.sum(var16.getConstraint("South"), var9));
         }

         var20 = var18;
      }

      Constraints var21 = var7.getConstraints(var0);
      var21.setConstraint("South", Spring.sum(Spring.constant(var6), var20.getConstraint("South")));
      var21.setConstraint("East", Spring.sum(Spring.constant(var5), var20.getConstraint("East")));
   }

   private static Constraints getConstraintsForCell(int var0, int var1, Container var2, int var3) {
      SpringLayout var4 = (SpringLayout)var2.getLayout();
      Component var5 = var2.getComponent(var0 * var3 + var1);
      return var4.getConstraints(var5);
   }

   public static void makeCompactGrid(Container var0, int var1, int var2, int var3, int var4, int var5, int var6) {
      SpringLayout var7;
      try {
         var7 = (SpringLayout)var0.getLayout();
      } catch (ClassCastException var14) {
         System.err.println("The first argument to makeCompactGrid must use SpringLayout.");
         return;
      }

      Spring var8 = Spring.constant(var3);

      for(int var9 = 0; var9 < var2; ++var9) {
         Spring var10 = Spring.constant(0);

         int var11;
         for(var11 = 0; var11 < var1; ++var11) {
            var10 = Spring.max(var10, getConstraintsForCell(var11, var9, var0, var2).getWidth());
         }

         for(var11 = 0; var11 < var1; ++var11) {
            Constraints var12 = getConstraintsForCell(var11, var9, var0, var2);
            var12.setX(var8);
            var12.setWidth(var10);
         }

         var8 = Spring.sum(var8, Spring.sum(var10, Spring.constant(var5)));
      }

      Spring var15 = Spring.constant(var4);

      for(int var16 = 0; var16 < var1; ++var16) {
         Spring var18 = Spring.constant(0);

         int var19;
         for(var19 = 0; var19 < var2; ++var19) {
            var18 = Spring.max(var18, getConstraintsForCell(var16, var19, var0, var2).getHeight());
         }

         for(var19 = 0; var19 < var2; ++var19) {
            Constraints var13 = getConstraintsForCell(var16, var19, var0, var2);
            var13.setY(var15);
            var13.setHeight(var18);
         }

         var15 = Spring.sum(var15, Spring.sum(var18, Spring.constant(var6)));
      }

      Constraints var17 = var7.getConstraints(var0);
      var17.setConstraint("South", var15);
      var17.setConstraint("East", var8);
   }
}
