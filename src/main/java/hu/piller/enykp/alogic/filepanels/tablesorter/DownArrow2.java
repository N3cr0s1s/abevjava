package hu.piller.enykp.alogic.filepanels.tablesorter;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import javax.swing.Icon;

public class DownArrow2 implements Icon {
   private int f;

   public DownArrow2(int var1) {
      this.f = var1;
   }

   public int getIconHeight() {
      return 2 * this.f;
   }

   public int getIconWidth() {
      return 2 * this.f;
   }

   public void paintIcon(Component var1, Graphics var2, int var3, int var4) {
      int var5 = this.getIconWidth();
      int var6 = this.getIconHeight();
      int[] var7 = new int[]{var3, var3 + var5, var3 + this.f};
      int[] var8 = new int[]{var4, var4, var4 + var6};
      var2.setColor(Color.ORANGE);
      var2.fillArc(var3 - 3, var4 - 3, 12, 12, 0, 360);
      var2.setColor(Color.WHITE);
      var2.fillPolygon(var7, var8, 3);
      var2.drawPolygon(var7, var8, 3);
   }
}
