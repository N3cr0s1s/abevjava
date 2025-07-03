package hu.piller.enykp.alogic.filepanels.folderpanel;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import javax.swing.Icon;

public class LeftArrow implements Icon {
   private int f;

   public LeftArrow(int var1) {
      this.f = var1;
   }

   public int getIconHeight() {
      return 2 * this.f;
   }

   public int getIconWidth() {
      return this.f;
   }

   public void paintIcon(Component var1, Graphics var2, int var3, int var4) {
      int var5 = this.getIconWidth();
      int var6 = this.getIconHeight();
      int[] var7 = new int[]{var3 + var5, var3, var3 + var5};
      int[] var8 = new int[]{var4, var4 + this.f, var4 + var6};
      var2.setColor(Color.BLACK);
      var2.fillPolygon(var7, var8, 3);
   }
}
