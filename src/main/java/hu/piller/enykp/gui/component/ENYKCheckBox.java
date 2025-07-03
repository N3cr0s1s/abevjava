package hu.piller.enykp.gui.component;

import hu.piller.enykp.interfaces.IDataField;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Hashtable;
import javax.swing.JCheckBox;
import javax.swing.border.Border;

public class ENYKCheckBox extends JCheckBox implements IDataField {
   public static final String FEATURE_ABEV_MASK = "abev_mask";
   public static final String FEATURE_ABEV_SUBSCRIPT_HEIGHT = "abev_subscript_height";
   public static final String FEATURE_ABEV_SUBSCRIPT_COLOR = "abev_subscript_color";
   public static final String FEATURE_ABEV_OVERRIDE_MODE = "abev_override_mode";
   public static final String FEATURE_ABEV_INPUT_RULES = "irids";
   public static final String FEATURE_VISIBLE = "visible";
   public static final String FEATURE_TOOLTIP = "tooltip";
   public static final String FEATURE_ENABLED = "enabled";
   public static final String FEATURE_DATA_TYPE = "data_type";
   public static final String FEATURE_BORDER_COLOR = "border_color";
   public static final String FEATURE_BORDER_WIDTH = "border_width";
   public static final String FEATURE_BORDER_SIDES = "border_sides";
   public static final String FEATURE_DISABLED_FG_COLOR = "disabled_fg_color";
   public static final String FEATURE_DISABLED_BG_COLOR = "disabled_bg_color";
   public static final String FEATURE_VISIBLE_ON_PRINT = "printable";
   public static final String FEATURE_FONT_COLOR = "font_color";
   public static final String FEATURE_ALIGNMENT = "alignment";
   private static final Color focus_bg_color;
   private static final Color ro_bg_color;
   private static final Color ro_fg_color;
   private FocusListener enyk_focus_listener;
   private PropertyChangeListener enyk_property_change_listener;
   protected Color ori_bg_color;
   protected Color ori_fg_color;
   protected ENYKLineBorder ori_border;
   protected int zoom;
   protected double zoom_f;
   protected String feature_abev_mask;
   protected Color feature_font_color;
   protected float feature_abev_subscript_height;
   protected Color feature_abev_subscript_color;
   protected Color feature_disabled_bg_color;
   protected Color feature_disabled_fg_color;
   protected Color feature_border_color;
   protected final Hashtable features = new Hashtable(16);
   protected boolean initialized;
   protected boolean is_printable;
   protected boolean is_bwmode;
   protected boolean is_painting;
   protected boolean is_printing;
   public static final String FEATURE_TRUE = "true";
   public static final String FEATURE_FALSE = "false";
   private int x_bold = 1;
   private int x_d = 2;
   private String c_true = "true";
   private String c_false = "false";

   public ENYKCheckBox() {
      this.setBackground(Color.WHITE);
      this.setForeground(Color.BLUE);
      this.initialize();
   }

   private void initialize() {
      this.is_painting = false;
      this.is_printing = false;
      this.zoom = 100;
      this.zoom_f = 1.0D;
      this.setBorder(new ENYKLineBorder(Color.BLACK, 1));
      this.enyk_property_change_listener = new ENYKCheckBox.ENYKPropertyChangeListener();
      this.enyk_focus_listener = new ENYKCheckBox.ENYKFocusListener();
      this.addPropertyChangeListener(this.enyk_property_change_listener);
      this.addFocusListener(this.enyk_focus_listener);
      this.ori_bg_color = this.getBackground();
      this.ori_fg_color = this.getForeground();
      this.ori_border = (ENYKLineBorder)this.getBorder();
      this.is_printable = true;
      this.feature_abev_mask = null;
      this.feature_font_color = this.getForeground();
      this.feature_abev_subscript_height = 0.0F;
      this.feature_abev_subscript_color = this.getForeground();
      this.feature_disabled_bg_color = ro_bg_color;
      this.feature_disabled_fg_color = ro_fg_color;
      this.feature_border_color = this.getForeground();
      this.initialized = true;
   }

   public Color getBackground() {
      if (this.is_bwmode) {
         return Color.WHITE;
      } else if (this.is_printing) {
         try {
            return new Color(Integer.parseInt((String)this.features.get("outer_bg_color"), 16));
         } catch (Exception var2) {
            return Color.WHITE;
         }
      } else if (this.is_painting) {
         if (this.isEnabled()) {
            try {
               return this.isFocusOwner() ? super.getBackground() : new Color(Integer.parseInt((String)this.features.get("bgcolor"), 16));
            } catch (Exception var3) {
               return super.getBackground();
            }
         } else {
            return this.feature_disabled_bg_color;
         }
      } else {
         try {
            return this.isFocusOwner() ? super.getBackground() : new Color(Integer.parseInt((String)this.features.get("bgcolor"), 16));
         } catch (Exception var4) {
            return super.getBackground();
         }
      }
   }

   public void setBorder(Border var1) {
      if (var1 instanceof ENYKLineBorder) {
         super.setBorder(var1);
      } else if (this.initialized && !System.getProperty("os.name").toLowerCase().startsWith("mac os")) {
         throw new RuntimeException("Invalid border type !");
      }

   }

   public void print(Graphics var1) {
      if (this.is_printable) {
         this.is_printing = true;

         try {
            super.print(var1);
         } finally {
            this.is_printing = false;
         }
      }

   }

   public void paint(Graphics var1) {
      if (this.initialized) {
         this.is_painting = true;
         this.paintcb(var1);
         this.paintSubtitle(var1);
         this.is_painting = false;
      }
   }

   public void setFeatures(Hashtable var1) {
      this.features.clear();
      this.features.putAll(var1);
      this.initialized = false;
      this.applyFeatures2();
      this.initFeatures();
      this.initialized = true;
      this.repaint();
   }

   protected void applyFeatures2() {
      applayFeatures(this);
   }

   protected void initFeatures() {
      if (this.feature_abev_subscript_color == null) {
         this.feature_abev_subscript_color = this.feature_border_color;
      }

   }

   public Hashtable getFeatures() {
      return this.features;
   }

   public void setZoom(int var1) {
      this.zoom = var1;
      this.zoom_f = (double)var1 / 100.0D;
      this.repaint();
   }

   public int getRecordIndex() {
      return -1;
   }

   public int getZoom() {
      return this.zoom;
   }

   public void release() {
      this.removePropertyChangeListener(this.enyk_property_change_listener);
      this.removeFocusListener(this.enyk_focus_listener);
      this.enyk_property_change_listener = null;
      this.enyk_focus_listener = null;
      this.feature_abev_mask = null;
      this.feature_font_color = null;
      this.feature_abev_subscript_color = null;
      this.feature_disabled_bg_color = null;
      this.feature_disabled_fg_color = null;
      this.feature_border_color = null;
      this.features.clear();
      this.c_true = null;
      this.c_false = null;
   }

   public Object getFieldValue() {
      return this.isSelected() ? this.c_true : this.c_false;
   }

   public Object getFieldValueWOMask() {
      return this.getFieldValue();
   }

   public void setValue(Object var1) {
      String var2 = var1 == null ? null : var1.toString();
      if (var2 != null) {
         var2 = var2.trim();
      }

      if (!this.c_true.equalsIgnoreCase(var2) && !"X".equals(var2) && !"x".equals(var2)) {
         if ((this.c_false.equalsIgnoreCase(var2) || "".equals(var2) || var2 == null) && this.isSelected()) {
            this.setSelected(false);
         }
      } else if (!this.isSelected()) {
         this.setSelected(true);
      }

   }

   protected void paintcb(Graphics var1) {
      Rectangle var2 = getComponentEditorBound(this);
      int var3 = 0;
      if (this.isOpaque() || this.hasFocus()) {
         Rectangle var4 = this.getBounds();
         var1.setColor(this.is_bwmode ? Color.WHITE : this.ori_bg_color);
         var1.fillRect(1, 1, var4.width, var4.height);
         var1.setColor(this.getBackground());
         var1.fillRect(var2.x, var2.y, var2.width, var2.height);
      }

      var1.setColor(this.getForeground());
      int var5;
      if (this.getBorder() != null) {
         ENYKLineBorder var12 = (ENYKLineBorder)this.getBorder();
         var5 = var12.getThickness();
         var3 = (int)(this.zoom_f * (double)var5 + 0.5D);
         var3 = var3 == 0 ? 1 : var3;
         var12.setThickness(var3);
         var12.setBWMode(this.is_bwmode);
         this.getBorder().paintBorder(this, var1, var2.x - 1, var2.y - 1, var2.width + 2, var2.height + 2);
         var12.setThickness(var5);
      }

      if (this.isSelected()) {
         int var8 = (int)(this.zoom_f * (double)this.x_d);
         var8 = var8 <= var3 ? var3 + 1 : var8;
         int var13 = var2.x + var8;
         var5 = var2.y + var8;
         int var6 = var2.x + var2.width - var8;
         int var7 = var2.y + var2.height - var8;
         Object var10 = ((Graphics2D)var1).getRenderingHint(RenderingHints.KEY_ANTIALIASING);
         ((Graphics2D)var1).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
         if (this.is_bwmode) {
            var1.setColor(Color.BLACK);
         } else {
            var1.setColor(this.feature_font_color);
         }

         var1.drawLine(var13, var5, var6, var7);
         var1.drawLine(var6, var5, var13, var7);
         int var9 = (int)(this.zoom_f * (double)this.x_bold + (this.zoom_f > 0.83D ? 0.5D : 0.0D));

         for(int var11 = 1; var11 <= var9; ++var11) {
            var1.drawLine(var13 + var11, var5, var6, var7 - var11);
            var1.drawLine(var13, var5 + var11, var6 - var11, var7);
            var1.drawLine(var6 - var11, var5, var13, var7 - var11);
            var1.drawLine(var6, var5 + var11, var13 + var11, var7);
         }

         ((Graphics2D)var1).setRenderingHint(RenderingHints.KEY_ANTIALIASING, var10);
         var1.setColor(this.getForeground());
      }

   }

   protected void paintSubtitle(Graphics var1) {
      Rectangle var2 = getComponentEditorBound(this);
      paintSubtitle(var1, var2.width, var2.height, this.feature_abev_mask, (float)((int)((double)this.feature_abev_subscript_height * this.zoom_f)), this.is_bwmode ? Color.BLACK : this.feature_abev_subscript_color);
   }

   private static void applayFeatures(ENYKCheckBox var0) {
      Object var1 = var0.features.get("abev_mask");
      var0.feature_abev_mask = var1 == null ? null : var1.toString();
      var1 = var0.features.get("abev_subscript_height");
      var0.feature_abev_subscript_height = var1 == null ? 0.0F : pixelToPoint(Integer.parseInt(var1.toString()));
      var1 = var0.features.get("abev_subscript_color");
      if (var1 instanceof Color) {
         var0.feature_abev_subscript_color = (Color)var1;
      } else {
         var0.feature_abev_subscript_color = var1 == null ? null : new Color(Integer.parseInt(var1.toString(), 16));
      }

      var1 = var0.features.get("border_color");
      if (var1 instanceof Color) {
         var0.feature_border_color = (Color)var1;
      } else {
         var0.feature_border_color = var1 == null ? var0.getForeground() : new Color(Integer.parseInt(var1.toString(), 16));
      }

      ((ENYKLineBorder)var0.getBorder()).setLineColor(var0.feature_border_color);
      var1 = var0.features.get("border_width");
      ((ENYKLineBorder)var0.getBorder()).setThickness(var1 == null ? 1 : Integer.parseInt(var1.toString()));
      var1 = var0.features.get("border_sides");
      ((ENYKLineBorder)var0.getBorder()).setSides(var1 == null ? 15 : Integer.parseInt(var1.toString()));
      var1 = var0.features.get("disabled_fg_color");
      if (var1 instanceof Color) {
         var0.feature_disabled_fg_color = (Color)var1;
      } else {
         var0.feature_disabled_fg_color = var1 == null ? ro_fg_color : new Color(Integer.parseInt(var1.toString(), 16));
      }

      var1 = var0.features.get("disabled_bg_color");
      if (var1 instanceof Color) {
         var0.feature_disabled_bg_color = (Color)var1;
      } else {
         var0.feature_disabled_bg_color = var1 == null ? ro_bg_color : new Color(Integer.parseInt(var1.toString(), 16));
      }

      var1 = var0.features.get("printable");
      String var2;
      if (var1 instanceof Boolean) {
         var0.is_printable = (Boolean)var1;
      } else {
         var2 = var1 == null ? "" : var1.toString();
         var0.is_printable = var2.equalsIgnoreCase("yes") || var2.equalsIgnoreCase("true") || var2.equalsIgnoreCase("igen");
      }

      var1 = var0.features.get("font_color");
      if (var1 instanceof Color) {
         var0.feature_font_color = (Color)var1;
      } else {
         var0.feature_font_color = var1 == null ? var0.getForeground() : new Color(Integer.parseInt(var1.toString(), 16));
      }

      var1 = var0.features.get("bwmode");
      if (var1 instanceof Boolean) {
         var0.is_bwmode = (Boolean)var1;
      } else {
         var2 = var1 == null ? "" : var1.toString();
         var0.is_bwmode = var2.equalsIgnoreCase("yes") || var2.equalsIgnoreCase("true") || var2.equalsIgnoreCase("igen");
      }

      var1 = var0.features.get("visible");
      if (var1 == null) {
         var0.setVisible(true);
      } else {
         var2 = var1.toString();
         var0.setVisible(var2.equalsIgnoreCase("yes") || var2.equalsIgnoreCase("true") || var2.equalsIgnoreCase("igen"));
      }

      var1 = var0.features.get("tooltip");
      var0.setToolTipText(var1 == null ? null : var1.toString());
      var1 = var0.features.get("enabled");
      if (var1 == null) {
         var0.setEnabled(true);
      } else {
         var2 = var1.toString();
         var0.setEnabled(var2.equalsIgnoreCase("yes") || var2.equalsIgnoreCase("true") || var2.equalsIgnoreCase("igen"));
      }

   }

   private static void paintSubtitle(Graphics var0, int var1, int var2, String var3, float var4, Color var5) {
      if (var3 != null && var5 != null) {
         int var6;
         if ((var6 = var3.indexOf(33)) >= 0) {
            String var8 = var3.substring(var6 + 1).trim();
            Color var11 = var0.getColor();
            Font var9 = var0.getFont();
            Font var10 = var9.deriveFont(var4);
            int var7 = var0.getFontMetrics(var10).stringWidth(var8);
            var0.setColor(var5);
            var0.setFont(var10);
            var0.drawString(var8, var1 - var7, var2 - 1);
            var0.setColor(var11);
            var0.setFont(var9);
         }
      }
   }

   private static Rectangle getComponentEditorBound(ENYKCheckBox var0) {
      Rectangle var1 = var0.getBounds();
      Insets var2 = var0.getInsets();
      if (var1.width > 0 && var1.height > 0) {
         var1.x = var1.y = 0;
         var1.x += var2.left;
         var1.y += var2.top;
         var1.width -= var2.left + var2.right;
         var1.height -= var2.top + var2.bottom;
         return var1;
      } else {
         return null;
      }
   }

   private static float pixelToPoint(int var0) {
      return (float)var0;
   }

   static {
      focus_bg_color = Color.YELLOW;
      ro_bg_color = new Color(255, 239, 255);
      ro_fg_color = new Color(100, 100, 100);
   }

   private static class ENYKPropertyChangeListener implements PropertyChangeListener {
      private ENYKPropertyChangeListener() {
      }

      public void propertyChange(PropertyChangeEvent var1) {
         String var2 = var1.getPropertyName();
         if (var2.equals("enabled")) {
            Object var3 = var1.getNewValue();
            Object var4 = var1.getOldValue();
            if (var3 instanceof Boolean && var4 instanceof Boolean) {
               boolean var5 = (Boolean)var3;
               boolean var6 = (Boolean)var4;
               if (var5 != var6) {
                  this.changeBorder(var5, (ENYKCheckBox)var1.getSource());
               }
            }
         }

      }

      public void changeBorder(boolean var1, ENYKCheckBox var2) {
         if (var1) {
            if (var2.ori_bg_color != null) {
               var2.setBackground(var2.ori_bg_color);
            }

            if (var2.ori_fg_color != null) {
               var2.setForeground(var2.ori_fg_color);
            }

            if (var2.ori_border != null) {
               var2.ori_border.setSides(((ENYKLineBorder)var2.getBorder()).getSides());
            }
         } else {
            Color var3 = null;
            Color var4 = null;
            Object var5 = var2.getFeatures().get("disabled_bg_color");
            if (var5 instanceof Color) {
               var3 = (Color)var5;
            }

            var5 = var2.getFeatures().get("disabled_fg_color");
            if (var5 instanceof Color) {
               var4 = (Color)var5;
            } else {
               var5 = var2.getFeatures().get("border_color");
               if (var5 instanceof Color) {
                  var4 = (Color)var5;
               }
            }

            if (var3 == null) {
               var3 = ENYKCheckBox.ro_bg_color;
            }

            if (var4 == null) {
               var4 = ENYKCheckBox.ro_fg_color;
            }

            var2.ori_bg_color = var2.getBackground();
            var2.ori_fg_color = var2.getForeground();
            var2.setBackground(var3);
            var2.setForeground(var4);
         }

      }

      // $FF: synthetic method
      ENYKPropertyChangeListener(Object var1) {
         this();
      }
   }

   private static class ENYKFocusListener implements FocusListener {
      private ENYKFocusListener() {
      }

      public void focusGained(FocusEvent var1) {
         ENYKCheckBox var2 = (ENYKCheckBox)var1.getSource();
         var2.ori_bg_color = var2.getBackground();
         var2.setBackground(ENYKCheckBox.focus_bg_color);
      }

      public void focusLost(FocusEvent var1) {
         ENYKCheckBox var2 = (ENYKCheckBox)var1.getSource();
         if (var2.ori_bg_color != null) {
            var2.setBackground(var2.ori_bg_color);
         }

      }

      // $FF: synthetic method
      ENYKFocusListener(Object var1) {
         this();
      }
   }
}
