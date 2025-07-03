package hu.piller.enykp.gui.viewer;

import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.util.icon.ENYKIconSet;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JViewport;

public class WatermarkViewport extends JViewport {
   BufferedImage fgimage;
   int parentWidth;
   boolean showWarningMsg = false;
   TexturePaint texture;

   public WatermarkViewport(PageViewer var1, boolean var2) {
      this.parentWidth = (int)((double)var1.origsize.width * var1.z);
      this.showWarningMsg = var2;
      if (var2) {
         this.fgimage = this.getBI(var1.z);
      }

      var1.setOpaque(!var2);
      super.setView(var1);
   }

   public void paintChildren(Graphics var1) {
      super.paintChildren(var1);
      if (this.showWarningMsg) {
         var1.drawImage(this.fgimage, 0, 0, (ImageObserver)null);
      }

   }

   private BufferedImage getBI(double var1) {
      JLabel var3 = new JLabel("Ez egy tiltott (nem kitölthető) lap, ami kizárólag megjelenítés céljából látható!");
      var3.setFont(var3.getFont().deriveFont(1, (float)(var1 * 16.0D)));
      int var4 = GuiUtil.getW(var3, var3.getText());
      int var5 = var3.getFontMetrics(var3.getFont()).getHeight();
      int var6 = Math.abs((this.parentWidth - var4) / 2);
      var4 = Math.max(var4, this.parentWidth);
      BufferedImage var7 = new BufferedImage(var4, var5 + 20, 2);
      Graphics2D var8 = var7.createGraphics();
      var8.setColor(Color.RED);
      var8.setFont(var3.getFont());
      Graphics2D var9 = (Graphics2D)var8.create();
      var9.drawString(var3.getText(), var6, var5 - 5);
      var9.dispose();
      return var7;
   }

   public void paintComponent(Graphics var1) {
      super.paintComponent(var1);
      if (this.texture != null) {
         Graphics2D var2 = (Graphics2D)var1;
         var2.setPaint(this.texture);
         var1.fillRect(0, 0, this.getWidth(), this.getHeight());
      }

   }

   public void setBackgroundTexture() {
      BufferedImage var1 = this.getBackgroundBI(true);
      Rectangle var2 = new Rectangle(0, 0, var1.getWidth((ImageObserver)null), var1.getHeight((ImageObserver)null));
      this.texture = new TexturePaint(var1, var2) {
         public int getTransparency() {
            return 50;
         }
      };
   }

   private BufferedImage getBackgroundBI(boolean var1) {
      BufferedImage var2 = new BufferedImage(800, 800, 2);
      Graphics2D var3 = var2.createGraphics();
      var3.setColor(new Color(255, 0, 0, 30));
      Font var4 = var3.getFont();
      var3.setFont(var4.deriveFont(32.0F));
      Graphics2D var5 = (Graphics2D)var3.create();
      String var6 = "Ez egy tiltott (nem kitölthető) lap, ami kizárólag megjelenítés céljából látható!";
      FontMetrics var7 = var5.getFontMetrics();
      int var8 = (this.getWidth() - var7.stringWidth(var6)) / 2;
      int var9 = (this.getHeight() - var7.getHeight()) / 2 + var7.getDescent();
      var5.setTransform(AffineTransform.getRotateInstance(Math.toRadians(60.0D), (double)(this.getWidth() / 2), (double)(this.getHeight() / 2)));
      var5.fillOval(100, 100, 200, 50);
      var5.dispose();
      if (var1) {
         ImageIcon var10 = ENYKIconSet.getInstance().get("aaa");
         var3.drawImage(var10.getImage(), 0, 0, (ImageObserver)null);
         var3.setColor(Color.GREEN);
         var3.drawString(var6, 20, 20);

         for(int var11 = 0; var11 < var2.getHeight(); ++var11) {
            for(int var12 = 0; var12 < var2.getWidth(); ++var12) {
               var2.setRGB(var12, var11, var11 > var12 ? Color.RED.getRGB() : Color.ORANGE.getRGB());
            }
         }
      }

      return var2;
   }
}
