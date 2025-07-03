package hu.piller.enykp.gui.viewer.slidercombo;

import hu.piller.enykp.gui.GuiUtil;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D.Float;
import javax.swing.JComponent;
import javax.swing.JSlider;
import javax.swing.plaf.basic.BasicSliderUI;

public class SizeableSliderUI extends BasicSliderUI {
   private static final int TRACK_HEIGHT = 8;
   private static final int TRACK_WIDTH = 8;
   private static final int TRACK_ARC = 5;
   private static final Dimension THUMB_SIZE = new Dimension(Math.max(12, GuiUtil.getCommonItemHeight() / 2), Math.max(12, GuiUtil.getCommonItemHeight() / 2));
   private final Float trackShape = new Float();

   public SizeableSliderUI(JSlider var1) {
      super(var1);
   }

   protected void calculateTrackRect() {
      super.calculateTrackRect();
      if (this.isHorizontal()) {
         this.trackRect.y += (this.trackRect.height - 8) / 2;
         this.trackRect.height = 8;
      } else {
         this.trackRect.x += (this.trackRect.width - 8) / 2;
         this.trackRect.width = 8;
      }

      this.trackShape.setRoundRect((float)this.trackRect.x, (float)this.trackRect.y, (float)this.trackRect.width, (float)this.trackRect.height, 5.0F, 5.0F);
   }

   protected void calculateThumbLocation() {
      super.calculateThumbLocation();
      if (this.isHorizontal()) {
         this.thumbRect.y = this.trackRect.y + (this.trackRect.height - this.thumbRect.height) / 2;
      } else {
         this.thumbRect.x = this.trackRect.x + (this.trackRect.width - this.thumbRect.width) / 2;
      }

   }

   protected Dimension getThumbSize() {
      return THUMB_SIZE;
   }

   private boolean isHorizontal() {
      return this.slider.getOrientation() == 0;
   }

   public void paint(Graphics var1, JComponent var2) {
      ((Graphics2D)var1).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      super.paint(var1, var2);
   }

   public void paintTrack(Graphics var1) {
      Graphics2D var2 = (Graphics2D)var1;
      Shape var3 = var2.getClip();
      boolean var4 = this.isHorizontal();
      boolean var5 = this.slider.getInverted();
      var2.setColor(new Color(170, 170, 170));
      var2.fill(this.trackShape);
      var2.setColor(new Color(200, 200, 200));
      var2.setClip(this.trackShape);
      ++this.trackShape.y;
      var2.fill(this.trackShape);
      this.trackShape.y = (float)this.trackRect.y;
      var2.setClip(var3);
      if (var4) {
         boolean var6 = this.slider.getComponentOrientation().isLeftToRight();
         if (var6) {
            var5 = !var5;
         }

         int var7 = this.thumbRect.x + this.thumbRect.width / 2;
         if (var5) {
            var2.clipRect(0, 0, var7, this.slider.getHeight());
         } else {
            var2.clipRect(var7, 0, this.slider.getWidth() - var7, this.slider.getHeight());
         }
      } else {
         int var8 = this.thumbRect.y + this.thumbRect.height / 2;
         if (var5) {
            var2.clipRect(0, 0, this.slider.getHeight(), var8);
         } else {
            var2.clipRect(0, var8, this.slider.getWidth(), this.slider.getHeight() - var8);
         }
      }

      var2.setColor(GuiUtil.getHighLightColor());
      var2.fill(this.trackShape);
      var2.setClip(var3);
   }

   public void paintThumb(Graphics var1) {
      var1.setColor(GuiUtil.getHighLightColor());
      var1.fillOval(this.thumbRect.x, this.thumbRect.y, this.thumbRect.width, this.thumbRect.height);
   }

   public void paintFocus(Graphics var1) {
   }
}
