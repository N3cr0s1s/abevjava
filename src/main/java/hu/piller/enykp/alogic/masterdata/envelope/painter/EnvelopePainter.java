package hu.piller.enykp.alogic.masterdata.envelope.painter;

import hu.piller.enykp.alogic.masterdata.envelope.model.AddressOpt;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.TreeSet;

public class EnvelopePainter {
   public void paintEnvelope(Graphics2D var1, Rectangle var2, AddressOpt var3, AddressOpt var4) {
      var1.setColor(Color.WHITE);
      var1.setPaint(Color.WHITE);
      var1.fillRect(var2.x, var2.y, var2.width, var2.height);
      Font var5 = var1.getFont().deriveFont(var2.height / 20);
      Font var6 = var5.deriveFont(1);
      var1.setFont(var6);
      FontMetrics var7 = var1.getFontMetrics();
      int var8 = (int)((double)(var7.getAscent() + var7.getDescent()) * 1.1D);
      int var9 = var2.x + 10;
      var1.setColor(Color.BLACK);
      if (!"".equals(var3.getTitle()[0].trim())) {
         var1.drawString(var3.getTitle()[0].trim(), var9, var8);
      }

      int var10 = 2;
      String var11 = "";
      if (!"".equals(var3.getSettlement().trim())) {
         var11 = var3.getSettlement();
         var1.drawString(var11, var9, var10++ * var8);
      }

      if ("".equals(var3.getPoBox())) {
         var11 = var3.getKozteruletCim();
      } else {
         var11 = var3.getPoBox();
      }

      if (!"".equals(var11.trim())) {
         var1.drawString(var11, var9, var10++ * var8);
      }

      if (!"".equals(var3.getFormattedZip())) {
         var1.drawString(var3.getFormattedZip(), var9, var10 * var8);
      }

      TreeSet var12 = new TreeSet();
      String[] var13 = var4.getTitle();
      int var14 = var13.length;

      int var15;
      for(var15 = 0; var15 < var14; ++var15) {
         String var16 = var13[var15];
         var12.add((int)var7.getStringBounds(var16, var1).getWidth() + var9);
      }

      var12.add((int)var7.getStringBounds(var4.getSettlement(), var1).getWidth() + var9);
      var11 = "".equals(var4.getPoBox()) ? var4.getKozteruletCim() : var4.getPoBox();
      var12.add((int)var7.getStringBounds(var11, var1).getWidth() + var9);
      var12.add((int)var7.getStringBounds(var4.getFormattedZip(), var1).getWidth() + var9);
      int var17 = (Integer)var12.last();
      var14 = var17 / 4;
      if (var14 + var17 > var2.width) {
         var14 = 0;
      }

      var15 = 4 + var4.getNumOfAddressParts();

      for(int var18 = 0; var15 > 4; --var15) {
         var1.drawString(var4.getTitle()[var18], var2.width - var17 - var14, var2.height - var15 * var8);
         ++var18;
      }

      --var15;
      var1.drawString(var4.getSettlement(), var2.width - var17, var2.height - var15 * var8);
      --var15;
      var1.drawString(var11, var2.width - var17, var2.height - var15 * var8);
      --var15;
      var1.drawString(var4.getFormattedZip(), var2.width - var17, var2.height - var15 * var8);
   }
}
