package hu.piller.enykp.alogic.primaryaccount.common;

import hu.piller.enykp.alogic.primaryaccount.envelopeprinter.EnvelopePrinter;
import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.interfaces.IPrintSupport;
import hu.piller.enykp.util.base.ErrorList;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.awt.print.PageFormat;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map.Entry;

public class DefaultEnvelope implements IPrintSupport {
   public static final String DATA_F_FELADO = "f_feladó";
   public static final String DATA_F_VAROS = "f_város";
   public static final String DATA_F_UTCA = "f_utca";
   public static final String DATA_F_IRSZ = "f_irsz";
   public static final String DATA_C_CIMZETT = "c_címzett";
   public static final String DATA_C_VAROS = "c_város";
   public static final String DATA_C_PF = "c_pf";
   public static final String DATA_C_IRSZ = "c_irsz";
   private static final String TEXT_F_FELADO = "Feladó: ";
   protected IRecord record;
   protected Component parent;
   protected int data_id;
   protected final Hashtable envelope_data = new Hashtable(8);

   public DefaultEnvelope() {
      this.initializeData(this.envelope_data);
   }

   protected void initializeData(Hashtable var1) {
      if (var1 != null) {
         var1.put("f_feladó", "");
         var1.put("f_város", "");
         var1.put("f_utca", "");
         var1.put("f_irsz", "");
         var1.put("c_címzett", "");
         var1.put("c_város", "");
         var1.put("c_pf", "");
         var1.put("c_irsz", "");
      }

   }

   public void put(Object var1, Object var2) {
      if (var1 != null && var2 != null) {
         this.envelope_data.put(var1, var2);
      }

   }

   protected Hashtable getData(int var1) {
      return this.record == null ? this.envelope_data : this.record.getData();
   }

   public void setDataId(int var1) {
      this.data_id = var1;
   }

   public Hashtable getEnvelopeData(IRecord var1, int var2) {
      this.record = var1;
      return this.getData(var2);
   }

   public void print(IRecord var1, Component var2) {
      this.record = var1;
      this.parent = var2;
      this.printEnvelope();
   }

   public void printEnvelope() {
      if (this.isPrintable()) {
         try {
            EnvelopePrinter var1 = EnvelopePrinter.getInstance();
            var1.print(this, (Object)null);
         } catch (Exception var2) {
            writeError("EP", "Hiba történt boríték nyomtatás közben !", var2, (Object)null);
         }
      } else {
         GuiUtil.showMessageDialog(this.parent, "A boríték címzése hiányos, ezért nem nyomtatható ki!", "Boríték nyomtatás", 2);
      }

   }

   private boolean isPrintable() {
      if (this.record != null) {
         Hashtable var1 = this.envelope_data;
         if (var1 != null) {
            String[] var2 = new String[]{"f_feladó", "f_feladó", "f_irsz", "c_címzett", "c_város", "c_irsz"};
            int var4 = 0;

            for(int var5 = var2.length; var4 < var5; ++var4) {
               Object var3 = var1.get(var2[var4]);
               if (var3 == null) {
                  return false;
               }

               if (var3.toString().trim().length() == 0) {
                  return false;
               }
            }

            return true;
         }
      }

      return false;
   }

   public void printTo(Graphics var1, Object var2) {
      Graphics2D var3 = (Graphics2D)var1;
      PageFormat var5 = var2 instanceof PageFormat ? (PageFormat)var2 : null;
      Rectangle var4;
      if (var5 == null) {
         GraphicsConfiguration var6 = var3.getDeviceConfiguration();
         var4 = var6.getBounds();
      } else {
         var3.translate(var5.getImageableX(), var5.getImageableY());
         var4 = new Rectangle(0, 0, (int)var5.getImageableWidth(), (int)var5.getImageableHeight());
      }

      this.drawEnvelope(var3, var4, this.envelope_data);
   }

   private void drawEnvelope(Graphics2D var1, Rectangle var2, Hashtable var3) {
      float var12 = (float)(var2.height / 20);
      float var11 = (float)this.getFontSize(var1, var2, var3);
      Font var14 = var1.getFont().deriveFont(var12 < var11 ? var12 : var11);
      Font var15 = var14.deriveFont(1);
      var1.setFont(var15);
      FontMetrics var13 = var1.getFontMetrics();
      int var4 = (int)((double)(var13.getAscent() + var13.getDescent()) * 1.1D);
      int var6 = var2.x;
      int var9 = (int)((double)var2.width / 2.5D);
      int var10 = (int)((double)var13.getDescent() / 1.5D);
      var1.setColor(Color.WHITE);
      var1.setPaint(Color.WHITE);
      var1.fillRect(var2.x, var2.y, var2.width, var2.height);
      var1.setColor(Color.BLACK);
      var1.setFont(var14);
      var1.drawString("Feladó: ", var6, var4);
      var1.setFont(var15);
      var13 = var1.getFontMetrics();
      int var8 = (int)var13.getStringBounds("Feladó: ", var1).getWidth();
      var1.drawString("" + var3.get("f_feladó"), var6 + var8, var4);
      var1.drawLine(var6 + var8, var4 + var10, var9, var4 + var10);
      var1.drawString("" + var3.get("f_város") + ", " + var3.get("f_utca"), var6, 2 * var4);
      var1.drawLine(var6, 2 * var4 + var10, var9, 2 * var4 + var10);
      var1.drawString("" + var3.get("f_irsz"), var6, 3 * var4);
      int var5 = (int)var13.getStringBounds("" + var3.get("c_címzett"), var1).getWidth() + var6;
      var8 = (int)var13.getStringBounds("" + var3.get("c_város"), var1).getWidth() + var6;
      var5 = var8 > var5 ? var8 : var5;
      var8 = (int)var13.getStringBounds("" + var3.get("c_pf"), var1).getWidth() + var6;
      var5 = var8 > var5 ? var8 : var5;
      var8 = (int)var13.getStringBounds("" + var3.get("c_irsz"), var1).getWidth() + var6;
      var5 = var8 > var5 ? var8 : var5;
      int var7 = var5 / 4;
      var7 = var7 + var5 > var2.width ? 0 : var7;
      var1.drawString("" + var3.get("c_címzett"), var2.width - var5 - var7, var2.height - 5 * var4);
      var1.drawString("" + var3.get("c_város"), var2.width - var5, var2.height - 3 * var4);
      var1.drawLine(var2.width - var5, var2.height - 3 * var4 + var10, var2.width, var2.height - 3 * var4 + var10);
      var1.drawString("" + var3.get("c_pf"), var2.width - var5, var2.height - 2 * var4);
      var1.drawString("" + var3.get("c_irsz"), var2.width - var5, var2.height - var4);
   }

   private int getFontSize(Graphics2D var1, Rectangle var2, Hashtable var3) {
      Font var8 = var1.getFont();
      Font var9 = var8;
      int var10 = var2.width;
      float var14 = 1.0F;

      while(true) {
         var9 = var9.deriveFont(1, var14);
         FontMetrics var12 = var1.getFontMetrics(var9);
         Iterator var4 = var3.entrySet().iterator();

         while(var4.hasNext()) {
            Entry var5 = (Entry)var4.next();
            String var7 = (String)var5.getKey();
            String var6 = (String)var5.getValue();
            if ("f_feladó".equalsIgnoreCase(var7)) {
               var6 = "Feladó: " + var6;
            }

            Rectangle2D var13 = var12.getStringBounds(var6, var1);
            int var11 = (int)var13.getWidth();
            if (var11 > var10) {
               return (int)var14 - 1;
            }
         }

         ++var14;
      }
   }

   public static void writeError(Object var0, String var1, Exception var2, Object var3) {
      ErrorList.getInstance().writeError(var0, var1, var2, var3);
   }
}
