package hu.piller.enykp.alogic.filepanels.datecombo;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import javax.swing.Icon;

public class DownArrow implements Icon {
   private int w;

   public DownArrow(int var1) {
      this.w = var1;
   }

   public int getIconHeight() {
      return this.w;
   }

   public int getIconWidth() {
      return 2 * this.w;
   }

   public void paintIcon(Component var1, Graphics var2, int var3, int var4) {
      var2.setColor(Color.BLACK);
      var2.fillPolygon(new int[]{var3, var3 + 2 * this.w, var3 + this.w}, new int[]{var4, var4, var4 + this.w}, 3);
   }
}
