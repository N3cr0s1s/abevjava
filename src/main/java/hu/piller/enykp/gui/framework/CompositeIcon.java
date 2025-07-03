package hu.piller.enykp.gui.framework;

import java.awt.Component;
import java.awt.Graphics;
import javax.swing.Icon;
import javax.swing.SwingConstants;

public class CompositeIcon implements Icon, SwingConstants {
   Icon fIcon1;
   Icon fIcon2;
   int fPosition;
   int fHorizontalOrientation;
   int fVerticalOrientation;

   public CompositeIcon(Icon var1, Icon var2) {
      this(var1, var2, 1);
   }

   public CompositeIcon(Icon var1, Icon var2, int var3) {
      this(var1, var2, var3, 0, 0);
   }

   public CompositeIcon(Icon var1, Icon var2, int var3, int var4, int var5) {
      this.fIcon1 = var1;
      this.fIcon2 = var2;
      this.fPosition = var3;
      this.fHorizontalOrientation = var4;
      this.fVerticalOrientation = var5;
   }

   public void paintIcon(Component var1, Graphics var2, int var3, int var4) {
      int var5 = this.getIconWidth();
      int var6 = this.getIconHeight();
      Icon var7;
      Icon var8;
      if (this.fPosition != 2 && this.fPosition != 4) {
         if (this.fPosition != 1 && this.fPosition != 3) {
            this.paintIcon(var1, var2, this.fIcon1, var3, var4, var5, var6, this.fHorizontalOrientation, this.fVerticalOrientation);
            this.paintIcon(var1, var2, this.fIcon2, var3, var4, var5, var6, this.fHorizontalOrientation, this.fVerticalOrientation);
         } else {
            if (this.fPosition == 1) {
               var7 = this.fIcon1;
               var8 = this.fIcon2;
            } else {
               var7 = this.fIcon2;
               var8 = this.fIcon1;
            }

            this.paintIcon(var1, var2, var7, var3, var4, var5, var6, this.fHorizontalOrientation, 1);
            this.paintIcon(var1, var2, var8, var3, var4 + var7.getIconHeight(), var5, var6, this.fHorizontalOrientation, 1);
         }
      } else {
         if (this.fPosition == 2) {
            var7 = this.fIcon1;
            var8 = this.fIcon2;
         } else {
            var7 = this.fIcon2;
            var8 = this.fIcon1;
         }

         this.paintIcon(var1, var2, var7, var3, var4, var5, var6, 2, this.fVerticalOrientation);
         this.paintIcon(var1, var2, var8, var3 + var7.getIconWidth(), var4, var5, var6, 2, this.fVerticalOrientation);
      }

   }

   void paintIcon(Component var1, Graphics var2, Icon var3, int var4, int var5, int var6, int var7, int var8, int var9) {
      int var10;
      switch(var8) {
      case 2:
         var10 = var4;
         break;
      case 4:
         var10 = var4 + var6 - var3.getIconWidth();
         break;
      default:
         var10 = var4 + (var6 - var3.getIconWidth()) / 2;
      }

      int var11;
      switch(var9) {
      case 1:
         var11 = var5;
         break;
      case 3:
         var11 = var5 + var7 - var3.getIconHeight();
         break;
      default:
         var11 = var5 + (var7 - var3.getIconHeight()) / 2;
      }

      var3.paintIcon(var1, var2, var10, var11);
   }

   public int getIconWidth() {
      return this.fPosition != 2 && this.fPosition != 4 ? Math.max(this.fIcon1.getIconWidth(), this.fIcon2.getIconWidth()) : this.fIcon1.getIconWidth() + this.fIcon2.getIconWidth();
   }

   public int getIconHeight() {
      return this.fPosition != 1 && this.fPosition != 3 ? Math.max(this.fIcon1.getIconHeight(), this.fIcon2.getIconHeight()) : this.fIcon1.getIconHeight() + this.fIcon2.getIconHeight();
   }
}
