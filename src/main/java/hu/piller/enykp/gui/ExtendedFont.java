package hu.piller.enykp.gui;

import java.awt.Color;
import java.awt.Font;

public class ExtendedFont extends Font {
   private Color color;
   private int yTransform = 0;

   public ExtendedFont(String var1, int var2, int var3, Color var4) {
      super(var1, var2, var3);
      this.setColor(var4);
   }

   public ExtendedFont(String var1, int var2, int var3, Color var4, int var5) {
      super(var1, var2, var3);
      this.setColor(var4);
      this.setYTransform(var5);
   }

   public Color getColor() {
      return this.color;
   }

   public void setColor(Color var1) {
      this.color = var1;
   }

   public int getYTransform() {
      return this.yTransform;
   }

   public void setYTransform(int var1) {
      this.yTransform = var1;
   }
}
