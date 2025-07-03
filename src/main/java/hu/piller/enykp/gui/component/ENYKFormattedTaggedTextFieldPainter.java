package hu.piller.enykp.gui.component;

import hu.piller.enykp.gui.framework.MainFrame;
import hu.piller.enykp.util.base.Tools;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.util.Hashtable;

public class ENYKFormattedTaggedTextFieldPainter extends ENYKFormattedTaggedTextField {
   public static final String DINAMIC_FEATURES_ZOOM = "_zoom_";
   public static final String DINAMIC_FEATURES_VALUE = "_value_";
   public static final long MINMEMORY = 5000000L;
   private boolean painterToScreen = true;
   private String[] featuresHashIDs = new String[]{"printable", "fill_on_lp", "abev_mask", "frame_sides", "data_type", "w", "border_sides", "fontname", "row", "vid", "border_width", "border_color", "h", "abev_subscript_height", "font_color", "outer_background", "help", "did", "frame_line_width", "subscript_height", "alignment", "delimiter_width", "bwmode", "print_on_flp", "char_rect_width", "fill_on_fp", "req", "fontcolor", "copy_fld", "fonttype", "char_rect_distance", "bgcolor", "enabled", "_zoom_", "char_rect_dist_x", "outer_bg_color", "_painter_", "delimiter_height", "_value_", "datatype", "visible_on_print", "fgcolor", "irids", "fontsize", "mask", "outer_opaque", "max_length", "col", "type"};
   private static Hashtable bufferedImages = new Hashtable(100);

   public void setFeatures(Hashtable var1) {
      try {
         this.freeMemory();
         this.features.clear();
         this.features.putAll(var1);
         this.initialized = false;
         if (bufferedImages.containsKey(this.getFeaturesHash())) {
            Boolean var2 = (Boolean)var1.get("_isprinting_");
            if (!var2) {
               this.initialized = true;
               return;
            }
         }

         this.reinitialize();
         hackFeatures(this.features);
         this.applyFeatures2();
         this.initFeatures();
         this.setZoom_();
         this.setValue_();
         this.initialized = true;
         this.createImage(this.getFeaturesHash());
         this.repaint();
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }

   public void setValue_() {
      Object var1 = this.features.get("_value_");
      super.setValue(var1);
   }

   public void setValue(Object var1) {
   }

   public void setZoom_() {
      Integer var1 = (Integer)this.features.get("_zoom_");
      if (var1 != null) {
         int var2 = var1;
         super.setZoom(var2);
      }
   }

   public void setZoom(int var1) {
   }

   private String getFeaturesHash() {
      return Tools.ht2string(this.features, this.featuresHashIDs);
   }

   public void clearImages() {
      bufferedImages = new Hashtable(100);
   }

   private void freeMemory() {
      if (bufferedImages.size() > 400) {
         this.clearImages();
      }

      if (Runtime.getRuntime().freeMemory() < 5000000L) {
         this.clearImages();
      }

   }

   private void createImage(String var1) {
      if (MainFrame.role.equalsIgnoreCase("0") && !bufferedImages.containsKey(var1)) {
         bufferedImages.put(var1, Tools.createBI(this));
      }

   }

   public void paint(Graphics var1) {
      if (var1 != null) {
         if (this.painterToScreen) {
            ((Graphics2D)var1).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            BufferedImage var2 = (BufferedImage)bufferedImages.get(this.getFeaturesHash());
            if (var2 != null) {
               var1.drawImage(var2, 0, 0, (ImageObserver)null);
               return;
            }
         }

         super.paint(var1);
      }

   }

   public void print(Graphics var1) {
      this.painterToScreen = false;
      super.is_printing = true;

      try {
         super.paint(var1);
      } finally {
         super.is_printing = false;
      }

      this.painterToScreen = true;
   }
}
