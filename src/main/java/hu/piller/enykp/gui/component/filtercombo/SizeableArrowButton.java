package hu.piller.enykp.gui.component.filtercombo;

import hu.piller.enykp.gui.GuiUtil;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.plaf.basic.BasicArrowButton;

public class SizeableArrowButton extends BasicArrowButton {
   private int fontSize;
   private Dimension dimension;
   private int[] tr_x = new int[3];
   private int[] tr_y = new int[3];

   public SizeableArrowButton(int var1, Color var2, Color var3, Color var4, Color var5, int var6) {
      super(var1, var2, var3, var4, var5);
      this.fontSize = var6;
      this.init();
   }

   public SizeableArrowButton(int var1, int var2) {
      super(var1);
      this.fontSize = var2;
      this.init();
   }

   private void init() {
      this.dimension = new Dimension(GuiUtil.getW("W"), GuiUtil.getCommonItemHeight() + 4);
   }

   public void paintTriangle(Graphics var1, int var2, int var3, int var4, int var5, boolean var6) {
      super.paintTriangle(var1, var2, var3, var4, var5, var6);
      if (this.fontSize >= 15) {
         if (this.isEnabled()) {
            int var7 = (int)Math.max(4.0D, this.dimension.getWidth() * 0.2D);
            int var8 = (int)Math.max(8.0D, (double)super.getHeight() * 0.4D);
            int var9 = (int)this.dimension.getWidth() - var7 - 1;
            int var10 = super.getHeight() - 6;
            this.tr_x[0] = var7;
            this.tr_y[0] = var8;
            this.tr_x[1] = var9;
            this.tr_y[1] = var8;
            this.tr_x[2] = var7 + (var9 - var7) / 2;
            this.tr_y[2] = var10;
            var1.setColor(GuiUtil.getModifiedBGColor());
            var1.fillPolygon(this.tr_x, this.tr_y, 3);
         }
      }
   }

   public Dimension getPreferredSize() {
      return this.dimension;
   }
}
