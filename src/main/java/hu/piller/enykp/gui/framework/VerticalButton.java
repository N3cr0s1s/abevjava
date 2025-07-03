package hu.piller.enykp.gui.framework;

import hu.piller.enykp.gui.GuiUtil;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.border.Border;

public class VerticalButton extends JButton {
   private Color bg;
   private Color frc;
   private int frround;
   private int width;
   private String ttext;

   public VerticalButton(String var1, Icon var2) {
      this.ttext = var1;
      this.width = GuiUtil.getCommonItemHeight() + 6;
      this.bg = Color.white;
      this.frc = Color.black;
      this.frround = 10;
      VTextIcon var3 = new VTextIcon(this, var1, 4);
      if (var2 == null) {
         this.setIcon(var3);
      } else {
         CompositeIcon var4 = new CompositeIcon(var2, var3);
         this.setIcon(var4);
      }

      this.setBorder((Border)null);
      this.setOpaque(false);
      this.setMinimumSize(new Dimension(this.width, 10));
      this.setPreferredSize(this.getMinimumSize());
      this.setSize(this.getMinimumSize());
      this.setAlignmentX(0.5F);
      this.setFocusable(false);
      this.setName(var1);
   }

   protected void paintComponent(Graphics var1) {
      var1.setColor(this.bg);
      var1.fillRoundRect(2, 2, this.getWidth() - 5, this.getHeight() - 5, this.frround, this.frround);
      super.paintComponent(var1);
      var1.setColor(this.frc);
      var1.drawRoundRect(2, 2, this.getWidth() - 5, this.getHeight() - 5, this.frround, this.frround);
   }

   public String getTText() {
      return this.ttext;
   }
}
