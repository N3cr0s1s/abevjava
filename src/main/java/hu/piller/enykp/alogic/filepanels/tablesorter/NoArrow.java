package hu.piller.enykp.alogic.filepanels.tablesorter;

import java.awt.Component;
import java.awt.Graphics;
import javax.swing.Icon;

public class NoArrow implements Icon {
   private int f;

   public NoArrow(int var1) {
      this.f = var1;
   }

   public int getIconHeight() {
      return 2 * this.f;
   }

   public int getIconWidth() {
      return 2 * this.f;
   }

   public void paintIcon(Component var1, Graphics var2, int var3, int var4) {
   }
}
