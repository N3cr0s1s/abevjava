package hu.piller.enykp.gui.component;

import hu.piller.enykp.interfaces.IDataField;
import hu.piller.enykp.util.base.Tools;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.JComponent;
import javax.swing.JTextArea;
import javax.swing.JViewport;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.DefaultCaret;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import javax.swing.text.PlainDocument;

public class ENYKTextArea extends JTextArea implements IDataField {
   private static final String EXT_IRID = "[\\x09-\\x20[";
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
   public static final String FEATURE_FONT = "font";
   public static final String FEATURE_FONT_COLOR = "font_color";
   public static final String FEATURE_ALIGNMENT = "alignment";
   public static final String FEATURE_MAX_LENGTH = "max_length";
   public static final String FEATURE_LINE_WRAP = "line_wrap";
   public static final String FEATURE_WRAP_STYLE_WORD = "wrap_style_word";
   private static final Color focus_bg_color;
   private static final Color ro_bg_color;
   private static final Color ro_fg_color;
   private FocusListener enyk_focus_listener;
   private PropertyChangeListener enyk_property_change_listener;
   private KeyListener enyk_key_listener;
   private DocumentListener enyk_document_listener;
   protected Font guessed_font;
   protected boolean initialized;
   protected boolean is_painting;
   protected boolean painted;
   protected boolean is_printable;
   protected final Hashtable features = new Hashtable(16);
   protected boolean need_value_clear;
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
   protected boolean feature_abev_override_mode;
   protected int feature_max_length;
   protected String feature_abev_input_rules;
   protected String tail;
   protected boolean enable_paint = true;
   protected boolean is_bwmode;
   protected static final Object resize_font_sync;
   protected JComponent thisinstance;
   private boolean textLimitationAllowed;
   boolean resizeable;
   boolean readOnly = false;
   static final float MIN_GROWTH_TO_APPLY = 1.2F;
   static final float MAX_SHRINK_TO_APPLY = 0.1F;

   boolean isResizeable() {
      return this.resizeable;
   }

   void setResizeable(boolean var1) {
      this.resizeable = var1;
   }

   public ENYKTextArea() {
      this.initialize();
   }

   public ENYKTextArea(String var1) {
      super(var1);
      this.initialize();
   }

   public void setCaret(Caret var1) {
      super.setCaret(new ENYKTextArea.NoScrollCaret());
   }

   private void initialize() {
      this.is_painting = false;
      this.painted = false;
      this.zoom = 100;
      this.zoom_f = 1.0D;
      this.tail = "";
      this.setBorder(new ENYKLineBorder(Color.BLACK, 1));
      this.setDocument(new ENYKTextArea.ENYKMaxLengthIridsDocument(-1, this));
      this.setFont(this.getFont().deriveFont(1));
      this.guessed_font = this.getFont();
      this.setLineWrap(true);
      this.setWrapStyleWord(true);
      this.enyk_property_change_listener = new ENYKTextArea.ENYKPropertyChangeListener();
      this.enyk_focus_listener = new ENYKTextArea.ENYKFocusListener();
      this.enyk_document_listener = new ENYKTextArea.ENYKDocumentListener(this);
      this.enyk_key_listener = new ENYKTextArea.ENYKKeyListener();
      this.getDocument().addDocumentListener(this.enyk_document_listener);
      this.addPropertyChangeListener(this.enyk_property_change_listener);
      this.addFocusListener(this.enyk_focus_listener);
      this.addKeyListener(this.enyk_key_listener);
      this.ori_bg_color = this.getBackground();
      this.ori_fg_color = this.getForeground();
      this.ori_border = (ENYKLineBorder)this.getBorder();
      this.is_printable = true;
      this.feature_abev_override_mode = false;
      this.feature_abev_mask = null;
      this.feature_font_color = this.getForeground();
      this.feature_abev_subscript_height = 0.0F;
      this.feature_abev_subscript_color = this.getForeground();
      this.feature_disabled_bg_color = ro_bg_color;
      this.feature_disabled_fg_color = ro_fg_color;
      this.feature_border_color = this.getForeground();
      this.feature_max_length = -1;
      this.initialized = true;
      this.thisinstance = this;
      this.textLimitationAllowed = true;
   }

   public void setBorder(Border var1) {
      if (var1 instanceof ENYKLineBorder) {
         super.setBorder(var1);
      } else if (this.initialized) {
         throw new RuntimeException("Invalid border type !");
      }

   }

   public void setDocument(Document var1) {
      if (var1 instanceof ENYKTextArea.ENYKMaxLengthIridsDocument) {
         super.setDocument(var1);
      } else if (this.initialized) {
         throw new RuntimeException("Invalid document type !");
      }

   }

   public Color getForeground() {
      if (this.is_painting) {
         if (this.is_bwmode) {
            return Color.BLACK;
         } else {
            return this.isEnabled() ? this.feature_font_color : this.feature_disabled_fg_color;
         }
      } else {
         return super.getForeground();
      }
   }

   public Color getBackground() {
      if (this.is_painting) {
         if (this.is_bwmode) {
            return Color.WHITE;
         } else {
            return this.isEnabled() ? super.getBackground() : this.feature_disabled_bg_color;
         }
      } else {
         return super.getBackground();
      }
   }

   public Color getDisabledTextColor() {
      return this.is_bwmode ? Color.BLACK : super.getDisabledTextColor();
   }

   public void print(Graphics var1) {
      if (this.is_printable && this.initialized) {
         Color var2 = var1.getColor();
         var1.setColor(this.feature_font_color);
         Font var3 = var1.getFont();
         var1.setFont(this.getFont());

         try {
            this.paint2pdf(var1);
         } catch (Exception var5) {
            var5.printStackTrace();
         }

         var1.setColor(var2);
         var1.setFont(var3);
      }

   }

   public void paint2pdf(Graphics var1) {
      BufferedImage var2 = Tools.createBI(this);
      var1.drawImage(var2, 0, 0, (ImageObserver)null);
   }

   public void paint(Graphics var1) {
      if (this.initialized) {
         this.is_painting = true;
         Object var2 = ((Graphics2D)var1).getRenderingHint(RenderingHints.KEY_ANTIALIASING);
         ((Graphics2D)var1).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
         this.paintComp(var1);
         ((Graphics2D)var1).setRenderingHint(RenderingHints.KEY_ANTIALIASING, var2);
         this.is_painting = false;
         this.painted = true;
      }
   }

   private void paintComp(Graphics var1) {
      int var2 = -1;
      ENYKLineBorder var3 = (ENYKLineBorder)this.getBorder();
      if (var3 != null) {
         var2 = var3.getThickness();
         int var4 = (int)(this.zoom_f * (double)var2 + 0.5D);
         var4 = var4 == 0 ? 1 : var4;
         var3.setThickness(var4);
         var3.setBWMode(this.is_bwmode);
      }

      if (this.isTextLimitationAllowed() && this.isResizeable()) {
         this.changeSizeNow();
         this.setResizeable(false);
      }

      super.paint(var1);
      if (var3 != null) {
         var3.setThickness(var2);
      }

   }

   public void setFeatures(Hashtable var1) {
      this.readOnly = false;
      this.features.clear();
      this.features.putAll(var1);
      this.initialized = false;
      this.reinitialize();
      hackFeatures(this.features);
      this.applyFeatures2();
      this.initFeatures();
      this.setResizeable(true);
      this.initialized = true;
      this.repaint();
   }

   protected void reinitialize() {
      ENYKTextArea.ENYKMaxLengthIridsDocument var1 = (ENYKTextArea.ENYKMaxLengthIridsDocument)this.getDocument();
      var1.setMaxLength(-1);
      var1.setIrid((String)null);
      this.setEnabled(true);
   }

   protected void applyFeatures2() {
      applayFeatures(this);
   }

   protected void initFeatures() {
      if (this.feature_abev_subscript_color == null) {
         this.feature_abev_subscript_color = this.feature_border_color;
      }

      int var1;
      if ((var1 = this.feature_abev_mask.indexOf(37)) < 0) {
         this.tail = this.feature_abev_mask;
      } else {
         this.tail = this.feature_abev_mask.substring(var1 + 1);
         this.feature_abev_mask = this.feature_abev_mask.substring(var1 + 1 + this.tail.length());
      }

      int var2 = getFormatterMask(this.feature_abev_mask).length();
      this.feature_max_length = var2 > 0 && var2 > this.feature_max_length ? var2 : this.feature_max_length;
      ((ENYKTextArea.ENYKMaxLengthIridsDocument)this.getDocument()).setMaxLength(this.feature_max_length);

      try {
         ((ENYKTextArea.ENYKMaxLengthIridsDocument)this.getDocument()).setIrid(createValidIrids(this.feature_abev_input_rules, getFormatterMask(this.feature_abev_mask)));
      } catch (Exception var4) {
         Tools.eLog(var4, 0);
      }

      this.tail = this.tail == null ? "" : this.tail;
      this.setText(this.tail);
      if (this.features.get("readonlymode") != null && (Boolean)this.features.get("readonlymode")) {
         this.readOnly = true;
      }

   }

   public Hashtable getFeatures() {
      return this.features;
   }

   public void setZoom(int var1) {
      this.zoom = var1;
      this.zoom_f = (double)var1 / 100.0D;
      resizeFont(this);
      this.setResizeable(true);
      this.revalidate();
      this.repaint();
   }

   public int getRecordIndex() {
      return -1;
   }

   public int getZoom() {
      return this.zoom;
   }

   public void release() {
      this.tail = null;
      this.getDocument().removeDocumentListener(this.enyk_document_listener);
      this.removeFocusListener(this.enyk_focus_listener);
      this.removePropertyChangeListener(this.enyk_property_change_listener);
      this.removeKeyListener(this.enyk_key_listener);
      this.enyk_document_listener = null;
      this.enyk_property_change_listener = null;
      this.enyk_focus_listener = null;
      this.enyk_key_listener = null;
      this.feature_abev_mask = null;
      this.feature_font_color = null;
      this.feature_abev_subscript_color = null;
      this.feature_disabled_bg_color = null;
      this.feature_disabled_fg_color = null;
      this.feature_border_color = null;
      this.features.clear();
   }

   public Object getFieldValue() {
      return this.specreplaceback(this.getText());
   }

   public Object getFieldValueWOMask() {
      return this.getFieldValue();
   }

   public void setValue(Object var1) {
      try {
         this.setText(var1 == null ? this.tail : this.specreplace(var1.toString()) + this.tail);
         this.getCaret().setDot(var1 == null ? 0 : ((String)var1).length());
      } catch (Exception var3) {
         this.getCaret().setDot(0);
      }

   }

   protected void paintSubtitle(Graphics var1) {
      Rectangle var2 = getComponentEditorBound(this);
      paintSubtitle(var1, var2.width, var2.height, this.feature_abev_mask, (float)((int)((double)this.feature_abev_subscript_height * this.zoom_f)), this.is_bwmode ? Color.BLACK : this.feature_abev_subscript_color, this);
   }

   public void changeSizeNow() {
      try {
         ENYKTextArea var1 = this;
         String var2 = this.getText();
         if (var2 == null || "".equals(var2.trim())) {
            return;
         }

         Font var3 = (Font)this.features.get("font");
         Rectangle var4 = this.getVisibleRect();
         int var5 = Integer.parseInt((String)this.features.get("h"));
         float var6;
         if (this.isTextLimitationAllowed()) {
            if (var4.getHeight() > (double)var5) {
               var6 = (float)(var4.getHeight() / (double)var5);
               this.guessed_font = var3.deriveFont(var3.getSize2D() * var6);
            } else {
               this.guessed_font = var3;
            }
         } else if (var4.getHeight() != (double)var5) {
            var6 = (float)(var4.getHeight() / (double)var5);
            this.guessed_font = var3.deriveFont(var3.getSize2D() * var6);
         } else {
            this.guessed_font = var3;
         }

         this.setFont(this.guessed_font);
         if (this.isTextLimitationAllowed()) {
            Rectangle var11 = this.getBounds();
            Rectangle var7 = this.modelToView(this.getDocument().getLength());
            int var8 = 0;

            while(var11.getHeight() < var7.getY() + var7.getHeight()) {
               float var9 = (float)(var11.getHeight() / (var7.getY() + var7.getHeight()));
               this.guessed_font = var3.deriveFont((float)((double)this.guessed_font.getSize() * Math.floor((double)(var9 * 10.0F)) / 10.0D));
               this.setFont(this.guessed_font);
               var7 = var1.modelToView(var1.getDocument().getLength());
               ++var8;
               if (var8 == 15) {
                  break;
               }
            }
         }
      } catch (Exception var10) {
         var10.printStackTrace();
      }

   }

   private static void resizeFont(ENYKTextArea var0) {
      if (!var0.isTextLimitationAllowed()) {
         synchronized(resize_font_sync) {
            var0.enable_paint = false;
            Font var2 = (Font)var0.features.get("font");
            if (!var0.painted) {
               var0.guessed_font = var2;
            } else {
               float var3 = 5.0F;
               var2 = var2.deriveFont((float)(var0.zoom_f * (double)var2.getSize2D()));
               var2 = (float)var2.getSize() > var3 ? var2 : var2.deriveFont(var3);
               if (var0.zoom_f < 1.0D) {
                  var2 = var2.deriveFont((float)(var0.zoom_f * (double)var2.getSize2D()));
               }

               if (!var2.equals(var0.guessed_font)) {
                  var0.guessed_font = var2;
               }
            }

            var0.setFont(var0.guessed_font);
            var0.enable_paint = true;
         }
      }
   }

   private static void applayFeatures(ENYKTextArea var0) {
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

      var1 = var0.features.get("abev_override_mode");
      String var2;
      if (var1 instanceof Boolean) {
         var0.feature_abev_override_mode = (Boolean)var1;
      } else {
         var2 = var1 == null ? "" : var1.toString();
         var0.feature_abev_override_mode = var2.equalsIgnoreCase("yes") || var2.equalsIgnoreCase("true") || var2.equalsIgnoreCase("igen");
      }

      var1 = var0.features.get("irids");
      var0.feature_abev_input_rules = var1 == null ? null : var1.toString();
      var1 = var0.features.get("max_length");
      var0.feature_max_length = var1 == null ? -1 : Integer.parseInt(var1.toString());
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

      var1 = var0.features.get("alignment");
      if (var1 != null) {
         var2 = var1.toString();
         if ("left".equalsIgnoreCase(var2)) {
            var0.setAlignmentX(0.0F);
         } else if ("right".equalsIgnoreCase(var2)) {
            var0.setAlignmentX(1.0F);
         } else if ("center".equalsIgnoreCase(var2)) {
            var0.setAlignmentX(0.5F);
         } else {
            try {
               var0.setAlignmentX(var1 == null ? var0.getAlignmentX() : Float.parseFloat(var1.toString()));
            } catch (NumberFormatException var4) {
               Tools.eLog(var4, 0);
            }
         }
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

      var1 = var0.features.get("line_wrap");
      if (var1 == null) {
         var0.setEnabled(true);
      } else {
         var2 = var1.toString();
         var0.setLineWrap(var2.equalsIgnoreCase("yes") || var2.equalsIgnoreCase("true") || var2.equalsIgnoreCase("igen"));
      }

      var1 = var0.features.get("wrap_style_word");
      if (var1 == null) {
         var0.setEnabled(true);
      } else {
         var2 = var1.toString();
         var0.setWrapStyleWord(var2.equalsIgnoreCase("yes") || var2.equalsIgnoreCase("true") || var2.equalsIgnoreCase("igen"));
      }

      var1 = var0.features.get("font");
      if (var1 instanceof Font) {
         var0.setFont((Font)var1);
      }

   }

   private static void hackFeatures(Hashtable var0) {
      String var1 = "";
      String var2 = "";
      Integer var3 = new Integer(-1);
      if (var0.containsKey("data_type")) {
         var3 = (Integer)var0.get("data_type");
      }

      if (var3 == 2) {
         if (var0.containsKey("irids")) {
            var2 = (String)var0.get("irids");
         }

         if (var2.startsWith("[0-9") || var0.containsKey("combo_data")) {
            if (var0.containsKey("abev_mask")) {
               var1 = (String)var0.get("abev_mask");
            }

            if (!var1.startsWith("%\\") && (!var1.startsWith("#") || var1.indexOf("!") <= -1)) {
               try {
                  if (var0.get("alignment").equals("left")) {
                     var0.put("data_type", new Integer(1));
                  } else if (var0.containsKey("combo_data") && checkValues(var0.get("combo_data"))) {
                     var0.put("data_type", new Integer(1));
                  }
               } catch (Exception var5) {
                  Tools.eLog(var5, 0);
               }
            }

         }
      }
   }

   private String specreplace(String var1) {
      return var1;
   }

   private String specreplaceback(String var1) {
      return var1;
   }

   private static boolean checkValues(Object var0) {
      if (!(var0 instanceof Object[])) {
         return false;
      } else {
         Object[] var1 = (Object[])((Object[])var0);
         Vector var2;
         if (var1.length == 1) {
            var2 = (Vector)var1[0];
         } else {
            var2 = (Vector)var1[1];
         }

         StringBuffer var3 = new StringBuffer();

         for(int var4 = 0; var4 < var2.size(); ++var4) {
            var3.append(var2.get(var4));
         }

         boolean var7 = true;

         int var5;
         for(var5 = 0; var5 < var3.length() && var7; ++var5) {
            if ("0123456789".indexOf(var3.substring(var5, var5 + 1)) == -1) {
               var7 = false;
            }
         }

         if (!var7) {
            return true;
         } else {
            for(int var6 = 0; var6 < var2.size(); ++var6) {
               var5 = Integer.parseInt((String)var2.get(var6));
               if (!var2.get(var6).equals(String.valueOf(var5))) {
                  return true;
               }
            }

            return false;
         }
      }
   }

   protected static void paintSubtitle(Graphics var0, int var1, int var2, String var3, float var4, Color var5, Component var6) {
      if (var3 != null && var5 != null) {
         int var7;
         if ((var7 = var3.indexOf(33)) >= 0) {
            String var9 = var3.substring(var7 + 1).trim();
            Color var12 = var0.getColor();
            Font var10 = var0.getFont();
            Font var11 = var10.deriveFont(var4);
            int var8 = var0.getFontMetrics(var11).stringWidth(var9);
            var0.setColor(var5);
            var0.setFont(var11);
            var0.drawString(var9, var1 - var8, var2 - 1);
            var0.setColor(var12);
            var0.setFont(var10);
         }
      }
   }

   protected static Rectangle getComponentEditorBound(ENYKTextArea var0) {
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

   public static String getFormatterMask(String var0) {
      int var1;
      if ((var1 = var0.indexOf(33)) > 0) {
         return var0.substring(0, var1);
      } else {
         return var1 < 0 ? var0 : "";
      }
   }

   public static String createValidIrids(String var0, String var1) {
      String var2 = var0;
      if (var0 != null) {
         String var3 = "";
         if (var1 != null) {
            int var4 = 0;

            for(int var5 = var1.length(); var4 < var5; ++var4) {
               if (var3.indexOf(var1.charAt(var4)) < 0) {
                  var3 = var3 + var1.charAt(var4);
               }
            }

            var3 = var3.replaceAll("\\\\", "\\\\\\\\");
            var3 = var3.replaceAll("\\.", "\\\\.");
            var3 = var3.replaceAll("#", " ");
         }

         if (var0.endsWith("]")) {
            var0 = var0.replace("[", "[\\x09-\\x20[");
            if (var3.length() > 0) {
               var2 = var0.substring(0, var0.length() - 1) + "][" + var3 + "]]*";
            } else {
               var2 = var0.substring(0, var0.length() - 1) + "]]*";
            }
         }
      }

      return var2;
   }

   public void setTextLimitationAllowed(boolean var1) {
      this.textLimitationAllowed = var1;
   }

   public boolean isTextLimitationAllowed() {
      return this.textLimitationAllowed;
   }

   static {
      focus_bg_color = Color.YELLOW;
      ro_bg_color = new Color(255, 239, 255);
      ro_fg_color = new Color(100, 100, 100);
      resize_font_sync = new Object();
   }

   private static class ENYKMaxLengthIridsDocument extends PlainDocument {
      private int max_length = -1;
      private String irid = null;
      ENYKTextArea parent;
      boolean docheck = true;
      private static final String NUMBERS = "0123456789";

      ENYKMaxLengthIridsDocument(int var1, ENYKTextArea var2) {
         this.setMaxLength(var1);
         this.parent = var2;
      }

      public int getMaxLength() {
         return this.max_length;
      }

      public void setMaxLength(int var1) {
         if (var1 < 0) {
            var1 = -1;
         }

         this.max_length = var1;
      }

      private boolean acceptText(int var1, String var2, AttributeSet var3) {
         boolean var4 = true;

         try {
            Rectangle var5 = this.parent.getUI().modelToView(this.parent, this.parent.getDocument().getLength());
            if (var2.length() == 1 && this.parent.getHeight() - var5.y <= 2 * var5.height && var2.indexOf("\n") != -1) {
               var4 = false;
            }
         } catch (Exception var6) {
            var6.printStackTrace();
            var4 = false;
         }

         return var4;
      }

      public void insertString(int var1, String var2, AttributeSet var3) throws BadLocationException {
         if (!this.parent.isTextLimitationAllowed() || this.acceptText(var1, var2, var3)) {
            if (this.max_length <= 0 || var1 < this.max_length) {
               if (this.max_length > 0 && var2 != null && var1 + var2.length() > this.max_length) {
                  var2 = var2.substring(0, this.max_length - var1);
               }

               this.insertString2(var1, var2, var3);
            }
         }
      }

      public void setIrid(String var1) {
         this.irid = var1;
      }

      private void insertString2(int var1, String var2, AttributeSet var3) throws BadLocationException {
         if (var2 != null) {
            if (this.irid != null && !var2.matches(this.irid)) {
               return;
            }

            if (var2.length() > 1) {
               if (this.modLength(var2) > 0) {
                  throw new Error("?????" + var2);
               }

               super.insertString(var1, var2, var3);
            } else if (this.modLength(this.getText(0, this.getLength())) < 0) {
               super.insertString(var1, var2, var3);
            } else {
               Toolkit.getDefaultToolkit().beep();
            }
         }

      }

      private int modLength(String var1) {
         if (this.max_length <= 0) {
            return -1;
         } else if (var1.length() < this.max_length) {
            return -1;
         } else if (var1.length() == this.max_length) {
            return 0;
         } else {
            var1 = var1.replaceAll(" ", "");
            var1 = var1.replaceAll("-", "");

            for(int var2 = 0; var2 < var1.length(); ++var2) {
               if ("0123456789".indexOf(var1.charAt(var2)) < 0) {
                  return 1;
               }
            }

            return var1.length() - this.max_length;
         }
      }
   }

   private static class ENYKKeyListener extends KeyAdapter {
      private ENYKKeyListener() {
      }

      public void keyPressed(KeyEvent var1) {
         ENYKTextArea var2 = (ENYKTextArea)var1.getSource();
         if (var2.need_value_clear && this.isClearKey(var1)) {
            var2.setValue("");
            var2.need_value_clear = false;
         }

      }

      private boolean isClearKey(KeyEvent var1) {
         int var2 = var1.getKeyCode();
         return (var2 < 112 || var2 > 123) && (var2 < 61440 || var2 > 61451) && (var2 < 154 || var2 > 157) && (var2 < 3 || var2 > 39) && (var2 < 224 || var2 > 227);
      }

      // $FF: synthetic method
      ENYKKeyListener(Object var1) {
         this();
      }
   }

   private static class ENYKDocumentListener implements DocumentListener {
      private ENYKTextArea comp;

      ENYKDocumentListener(ENYKTextArea var1) {
         this.comp = var1;
      }

      public void changedUpdate(DocumentEvent var1) {
         if (this.comp.isTextLimitationAllowed()) {
            this.comp.setResizeable(true);
         }

      }

      public void insertUpdate(DocumentEvent var1) {
         if (this.comp.isTextLimitationAllowed()) {
            this.comp.setResizeable(true);
         }

      }

      public void removeUpdate(DocumentEvent var1) {
         if (this.comp.isTextLimitationAllowed()) {
            this.comp.setResizeable(true);
         }

      }
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
                  this.changeBorder(var5, (ENYKTextArea)var1.getSource());
               }
            }
         }

      }

      public void changeBorder(boolean var1, ENYKTextArea var2) {
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
            Color var5 = null;
            Object var6 = var2.getFeatures().get("disabled_bg_color");
            if (var6 instanceof Color) {
               var3 = (Color)var6;
            }

            var6 = var2.getFeatures().get("disabled_fg_color");
            if (var6 instanceof Color) {
               var4 = (Color)var6;
            } else {
               var6 = var2.getFeatures().get("border_color");
               if (var6 instanceof Color) {
                  var4 = (Color)var6;
               }
            }

            var6 = var2.getFeatures().get("font_color");
            if (var6 instanceof Color) {
               var5 = (Color)var6;
            }

            if (var3 == null) {
               var3 = ENYKTextArea.ro_bg_color;
            }

            if (var4 == null) {
               var4 = ENYKTextArea.ro_fg_color;
            }

            var2.ori_bg_color = var2.getBackground();
            var2.ori_fg_color = var2.getForeground();
            var2.setBackground(var3);
            var2.setForeground(var4);
            if (var5 != null) {
               var2.setDisabledTextColor(var5);
            }
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
         ENYKTextArea var2 = (ENYKTextArea)var1.getSource();
         Object var3 = var2.getFeatures().get("abev_override_mode");
         boolean var4 = var3 instanceof Boolean && (Boolean)var3;
         if (var4) {
            var2.need_value_clear = true;
         }

         var2.ori_bg_color = var2.getBackground();
         if (!var2.readOnly) {
            var2.setBackground(ENYKTextArea.focus_bg_color);
         }

      }

      public void focusLost(FocusEvent var1) {
         ENYKTextArea var2 = (ENYKTextArea)var1.getSource();
         if (var2.ori_bg_color != null) {
            var2.setBackground(var2.ori_bg_color);
         }

      }

      // $FF: synthetic method
      ENYKFocusListener(Object var1) {
         this();
      }
   }

   private static class NoScrollCaret extends DefaultCaret {
      private NoScrollCaret() {
      }

      protected void adjustVisibility(Rectangle var1) {
         try {
            JTextComponent var2 = this.getComponent();
            if (var2.getParent().getClass() == JViewport.class) {
               super.adjustVisibility(var1);
            }
         } catch (Exception var3) {
            Tools.eLog(var3, 0);
         }

      }

      // $FF: synthetic method
      NoScrollCaret(Object var1) {
         this();
      }
   }
}
