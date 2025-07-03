package hu.piller.enykp.gui.component;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import javax.swing.border.LineBorder;

public class ENYKLineBorder extends LineBorder {
   private int sides = 15;
   private boolean is_bwmode;

   public ENYKLineBorder(Color var1) {
      super(var1);
      this.initialize();
   }

   public ENYKLineBorder(Color var1, int var2) {
      super(var1, var2);
      this.initialize();
   }

   public ENYKLineBorder(Color var1, int var2, int var3) {
      super(var1, var2);
      this.setSides(var3);
      this.initialize();
   }

   public ENYKLineBorder(Color var1, int var2, int var3, boolean var4) {
      super(var1, var2, var4);
      this.setSides(var3);
      this.initialize();
   }

   private void initialize() {
   }

   public void setLineColor(Color var1) {
      this.lineColor = var1;
   }

   public void setThickness(int var1) {
      this.thickness = var1;
   }

   public void setBWMode(boolean var1) {
      this.is_bwmode = var1;
   }

   public boolean getBWMode() {
      return this.is_bwmode;
   }

   public Insets getBorderInsets(Component var1) {
      int var2 = this.getThickness();
      return new Insets(var2, var2, var2, var2);
   }

   public int getSides() {
      return this.sides;
   }

   public void setSides(int var1) {
      this.sides = var1;
   }

   public void paintBorder(Component var1, Graphics var2, int var3, int var4, int var5, int var6) {
      Color var7 = var2.getColor();
      int var13 = this.getThickness();
      if (this.is_bwmode) {
         var2.setColor(Color.BLACK);
      } else {
         var2.setColor(this.lineColor);
      }

      for(int var8 = 0; var8 < var13; ++var8) {
         int var9 = var3 + var8;
         int var10 = var4 + var8;
         int var11 = var9 + var5 - var8 - var8 - 1;
         int var12 = var10 + var6 - var8 - var8 - 1;
         if (!this.roundedCorners) {
            if ((this.sides & 1) == 1) {
               var2.drawLine(var9 - var8, var10, var11 + var8, var10);
            }

            if ((this.sides & 2) == 2) {
               var2.drawLine(var11, var10 - var8, var11, var12 + var8);
            }

            if ((this.sides & 4) == 4) {
               var2.drawLine(var9 - var8, var12, var11 + var8, var12);
            }

            if ((this.sides & 8) == 8) {
               var2.drawLine(var9, var10 - var8, var9, var12 + var8);
            }
         } else {
            if ((this.sides & 1) == 1) {
               var2.drawLine(var9, var10, var11, var10);
            }

            if ((this.sides & 2) == 2) {
               var2.drawLine(var11, var10, var11, var12);
            }

            if ((this.sides & 4) == 4) {
               var2.drawLine(var9, var12, var11, var12);
            }

            if ((this.sides & 8) == 8) {
               var2.drawLine(var9, var10, var9, var12);
            }
         }
      }

      var2.setColor(var7);
   }
}
