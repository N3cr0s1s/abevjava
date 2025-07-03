package hu.piller.enykp.gui.component;

import hu.piller.enykp.interfaces.IENYKComponent;
import hu.piller.enykp.util.base.Tools;
import java.awt.ComponentOrientation;
import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.plaf.TextUI;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;

public class ENYKTextCaret extends DefaultCaret {
   public static final int SIDE_LEFT = 0;
   public static final int SIDE_RIGHT = 1;
   private int caret_width;
   private int caret_side;
   private transient int[] flagXPoints = new int[3];
   private transient int[] flagYPoints = new int[3];

   public ENYKTextCaret() {
      this.setBlinkRate(500);
      this.setCaretWidth(2);
      this.setCaretSide(0);
   }

   public ENYKTextCaret(int var1) {
      this.setBlinkRate(500);
      this.setCaretWidth(2);
      this.setCaretSide(var1);
   }

   public int getCaretSide() {
      return this.caret_side;
   }

   public void setCaretSide(int var1) {
      if (var1 == 0 || var1 == 1) {
         this.caret_side = var1;
      }

   }

   public void setCaretWidth(int var1) {
      if (var1 < 1) {
         var1 = 1;
      }

      this.caret_width = var1;
   }

   public int getCaretWidth() {
      return this.caret_width;
   }

   protected synchronized void damage(Rectangle var1) {
      JTextComponent var2 = this.getComponent();
      double var3 = (double)(((IENYKComponent)var2).getZoom() / 100);
      int var5 = (int)(var3 * (double)this.caret_width);
      var5 = var5 < 1 ? 1 : var5;
      if (var1 != null) {
         this.x = var1.x;
         this.y = var1.y;
         this.width = var1.width + var5 + 1;
         this.height = var1.height;
         this.repaint();
      }

   }

   public void paint(Graphics var1) {
      if (this.isVisible()) {
         try {
            JTextComponent var2 = this.getComponent();
            double var3 = (double)(((IENYKComponent)var2).getZoom() / 100);
            int var5 = (int)(var3 * (double)this.caret_width);
            var5 = var5 < 1 ? 1 : var5;
            int var6 = this.getDot();
            TextUI var7 = var2.getUI();
            Rectangle var8 = var7.modelToView(var2, var6);
            if (var8 == null || var8.width == 0 && var8.height == 0) {
               return;
            }

            if (this.width > 0 && this.height > 0 && !this._contains(var8.x, var8.y, var8.width, var8.height)) {
               Rectangle var9 = var1.getClipBounds();
               if (var9 != null && !var9.contains(this)) {
                  this.repaint();
               }

               this.damage(var8);
            }

            var1.setColor(var2.getCaretColor());
            int var14 = this.caret_side == 0 ? var8.x : var8.x + var8.width - var5;

            for(int var10 = 0; var10 < var5; ++var10) {
               var1.drawLine(var14 + var10, var8.y, var14 + var10, var8.y + var8.height - 1);
            }

            Document var15 = var2.getDocument();
            if (var15 instanceof AbstractDocument) {
               Element var11 = ((AbstractDocument)var15).getBidiRootElement();
               if (var11 != null && var11.getElementCount() > 1) {
                  boolean var12 = var2.getComponentOrientation() == ComponentOrientation.LEFT_TO_RIGHT;
                  this.flagXPoints[0] = var8.x;
                  this.flagYPoints[0] = var8.y;
                  this.flagXPoints[1] = var8.x;
                  this.flagYPoints[1] = var8.y + 4;
                  this.flagYPoints[2] = var8.y;
                  this.flagXPoints[2] = var12 ? var8.x + 5 : var8.x - 4 - var5 + 1;
                  var1.fillPolygon(this.flagXPoints, this.flagYPoints, 3);
               }
            }
         } catch (BadLocationException var13) {
            Tools.eLog(var13, 0);
         }
      }

   }

   private boolean _contains(int var1, int var2, int var3, int var4) {
      int var5 = this.width;
      int var6 = this.height;
      if ((var5 | var6 | var3 | var4) < 0) {
         return false;
      } else {
         int var7 = this.x;
         int var8 = this.y;
         if (var1 >= var7 && var2 >= var8) {
            if (var3 > 0) {
               var5 += var7;
               var3 += var1;
               if (var3 <= var1) {
                  if (var5 >= var7 || var3 > var5) {
                     return false;
                  }
               } else if (var5 >= var7 && var3 > var5) {
                  return false;
               }
            } else if (var7 + var5 < var1) {
               return false;
            }

            if (var4 > 0) {
               var6 += var8;
               var4 += var2;
               if (var4 <= var2) {
                  if (var6 >= var8 || var4 > var6) {
                     return false;
                  }
               } else if (var6 >= var8 && var4 > var6) {
                  return false;
               }
            } else if (var8 + var6 < var2) {
               return false;
            }

            return true;
         } else {
            return false;
         }
      }
   }
}
