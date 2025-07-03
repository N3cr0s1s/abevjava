package hu.piller.enykp.gui.framework;

import java.awt.Component;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.Icon;

public class VTextIcon implements Icon, PropertyChangeListener {
   String fLabel;
   String[] fCharStrings;
   int[] fCharWidths;
   int[] fPosition;
   int fWidth;
   int fHeight;
   int fCharHeight;
   int fDescent;
   int fRotation;
   Component fComponent;
   static final int POSITION_NORMAL = 0;
   static final int POSITION_TOP_RIGHT = 1;
   static final int POSITION_FAR_TOP_RIGHT = 2;
   public static final int ROTATE_DEFAULT = 0;
   public static final int ROTATE_NONE = 1;
   public static final int ROTATE_LEFT = 2;
   public static final int ROTATE_RIGHT = 4;
   static final int DEFAULT_CJK = 1;
   static final int LEGAL_ROMAN = 7;
   static final int DEFAULT_ROMAN = 4;
   static final int LEGAL_MUST_ROTATE = 6;
   static final int DEFAULT_MUST_ROTATE = 2;
   static final double NINETY_DEGREES = Math.toRadians(90.0D);
   static final int kBufferSpace = 5;

   public VTextIcon(Component var1, String var2) {
      this(var1, var2, 0);
   }

   public VTextIcon(Component var1, String var2, int var3) {
      this.fComponent = var1;
      this.fLabel = var2;
      this.fRotation = var3;
      this.calcDimensions();
      this.fComponent.addPropertyChangeListener(this);
   }

   public void setLabel(String var1) {
      this.fLabel = var1;
      this.recalcDimensions();
   }

   public void propertyChange(PropertyChangeEvent var1) {
      String var2 = var1.getPropertyName();
      if ("font".equals(var2)) {
         this.recalcDimensions();
      }

   }

   void recalcDimensions() {
      int var1 = this.getIconWidth();
      int var2 = this.getIconHeight();
      this.calcDimensions();
      if (var1 != this.getIconWidth() || var2 != this.getIconHeight()) {
         this.fComponent.invalidate();
      }

   }

   void calcDimensions() {
      FontMetrics var1 = this.fComponent.getFontMetrics(this.fComponent.getFont());
      this.fCharHeight = var1.getAscent() + var1.getDescent();
      this.fDescent = var1.getDescent();
      if (this.fRotation == 1) {
         int var2 = this.fLabel.length();
         char[] var3 = new char[var2];
         this.fLabel.getChars(0, var2, var3, 0);
         this.fWidth = 0;
         this.fCharStrings = new String[var2];
         this.fCharWidths = new int[var2];
         this.fPosition = new int[var2];

         for(int var5 = 0; var5 < var2; ++var5) {
            char var4 = var3[var5];
            this.fCharWidths[var5] = var1.charWidth(var4);
            if (this.fCharWidths[var5] > this.fWidth) {
               this.fWidth = this.fCharWidths[var5];
            }

            this.fCharStrings[var5] = new String(var3, var5, 1);
            this.fPosition[var5] = 0;
         }

         this.fHeight = this.fCharHeight * var2 + this.fDescent;
      } else {
         this.fWidth = this.fCharHeight;
         this.fHeight = var1.stringWidth(this.fLabel) + 10;
      }

   }

   public void paintIcon(Component var1, Graphics var2, int var3, int var4) {
      var2.setColor(var1.getForeground());
      var2.setFont(var1.getFont());
      if (this.fRotation == 1) {
         int var5 = var4 + this.fCharHeight;

         for(int var6 = 0; var6 < this.fCharStrings.length; ++var6) {
            int var7;
            switch(this.fPosition[var6]) {
            case 0:
               var2.drawString(this.fCharStrings[var6], var3 + (this.fWidth - this.fCharWidths[var6]) / 2, var5);
               break;
            case 1:
               var7 = this.fCharHeight / 3;
               var2.drawString(this.fCharStrings[var6], var3 + var7 / 2, var5 - var7);
               break;
            case 2:
               var7 = this.fCharHeight - this.fCharHeight / 3;
               var2.drawString(this.fCharStrings[var6], var3 + var7 / 2, var5 - var7);
            }

            var5 += this.fCharHeight;
         }
      } else if (this.fRotation == 2) {
         var2.translate(var3 + this.fWidth, var4 + this.fHeight);
         ((Graphics2D)var2).rotate(-NINETY_DEGREES);
         var2.drawString(this.fLabel, 5, -this.fDescent);
         ((Graphics2D)var2).rotate(NINETY_DEGREES);
         var2.translate(-(var3 + this.fWidth), -(var4 + this.fHeight));
      } else if (this.fRotation == 4) {
         var2.translate(var3, var4);
         ((Graphics2D)var2).rotate(NINETY_DEGREES);
         var2.drawString(this.fLabel, 5, -this.fDescent);
         ((Graphics2D)var2).rotate(-NINETY_DEGREES);
         var2.translate(-var3, -var4);
      }

   }

   public int getIconWidth() {
      return this.fWidth;
   }

   public int getIconHeight() {
      return this.fHeight;
   }
}
