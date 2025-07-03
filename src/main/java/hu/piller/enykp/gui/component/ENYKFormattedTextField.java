package hu.piller.enykp.gui.component;

import hu.piller.enykp.interfaces.IDataField;
import hu.piller.enykp.interfaces.IPropertyList;
import hu.piller.enykp.util.base.Tools;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.Toolkit;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.Format;
import java.text.ParseException;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.JFormattedTextField.AbstractFormatterFactory;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultFormatter;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;

public class ENYKFormattedTextField extends JFormattedTextField implements IDataField {
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
   public static final Integer DATATYPE_STRING = new Integer(1);
   public static final Integer DATATYPE_NUMBER = new Integer(2);
   private static final Color focus_bg_color;
   private static final Color ro_bg_color;
   private static final Color ro_fg_color;
   private FocusListener enyk_focus_listener;
   private PropertyChangeListener enyk_property_change_listener;
   private KeyListener enyk_key_listener;
   private DocumentListener enyk_document_listener;
   private InputVerifier enyk_input_verifier;
   protected Font guessed_font;
   protected Font ori_font;
   protected boolean initialized;
   protected boolean is_painting;
   protected boolean painted;
   protected boolean is_printable;
   protected boolean is_printing;
   protected final Hashtable features = new Hashtable(16);
   protected boolean need_value_clear;
   protected Color ori_bg_color;
   protected Color ori_fg_color;
   protected ENYKLineBorder ori_border;
   protected int zoom;
   protected double zoom_f;
   protected String feature_abev_mask;
   protected int feature_data_type;
   protected Color feature_font_color;
   protected float feature_abev_subscript_height;
   protected Color feature_abev_subscript_color;
   protected Color feature_disabled_bg_color;
   protected Color feature_disabled_fg_color;
   protected Color feature_border_color;
   protected boolean feature_abev_override_mode;
   protected int feature_max_length;
   protected String feature_abev_input_rules;
   protected String feature_abev_input_rules_validated;
   protected boolean is_money;
   protected boolean is_bwmode;
   protected int feature_round;
   private int triangle_ab = 7;
   private int[] tr_x = new int[3];
   private int[] tr_y = new int[3];
   private boolean use_super_value;

   public ENYKFormattedTextField() {
      this.initialize();
   }

   public ENYKFormattedTextField(Object var1) {
      super(var1);
      this.initialize();
   }

   public ENYKFormattedTextField(Format var1) {
      super(var1);
      this.initialize();
   }

   public ENYKFormattedTextField(AbstractFormatter var1) {
      super(var1);
      this.initialize();
   }

   public ENYKFormattedTextField(AbstractFormatterFactory var1) {
      super(var1);
      this.initialize();
   }

   public ENYKFormattedTextField(AbstractFormatterFactory var1, Object var2) {
      super(var1, var2);
      this.initialize();
   }

   private void initialize() {
      this.is_painting = false;
      this.is_printing = false;
      this.painted = false;
      this.zoom = 100;
      this.zoom_f = 1.0D;
      this.setBorder(new ENYKLineBorder(Color.BLACK, 1));
      this.setDocument(new ENYKFormattedTextField.ENYKMaxLengthIridsDocument(-1));
      this.setFont(this.getFont().deriveFont(1));
      this.guessed_font = this.getFont();
      this.enyk_property_change_listener = new ENYKFormattedTextField.ENYKPropertyChangeListener();
      this.enyk_focus_listener = new ENYKFormattedTextField.ENYKFocusListener();
      this.enyk_key_listener = new ENYKFormattedTextField.ENYKKeyListener();
      this.enyk_document_listener = new ENYKFormattedTextField.ENYKDocumentListener(this);
      this.enyk_input_verifier = new ENYKFormattedTextField.ENYKInputVerifier();
      this.getDocument().addDocumentListener(this.enyk_document_listener);
      this.addPropertyChangeListener(this.enyk_property_change_listener);
      this.addFocusListener(this.enyk_focus_listener);
      this.addKeyListener(this.enyk_key_listener);
      this.setInputVerifier(this.enyk_input_verifier);
      this.ori_bg_color = this.getBackground();
      this.ori_fg_color = this.getForeground();
      this.ori_border = (ENYKLineBorder)this.getBorder();
      this.is_printable = true;
      this.is_money = false;
      this.feature_abev_override_mode = false;
      this.feature_abev_mask = null;
      this.feature_font_color = this.getForeground();
      this.feature_abev_subscript_height = 0.0F;
      this.feature_abev_subscript_color = this.getForeground();
      this.feature_disabled_bg_color = ro_bg_color;
      this.feature_disabled_fg_color = ro_fg_color;
      this.feature_border_color = this.getForeground();
      this.feature_max_length = -1;
      this.feature_abev_input_rules = null;
      this.feature_round = -1;
      this.initialized = true;
   }

   public void setBorder(Border var1) {
      if (var1 instanceof ENYKLineBorder) {
         super.setBorder(var1);
      } else if (this.initialized) {
         throw new RuntimeException("Invalid border type !");
      }

   }

   public void setDocument(Document var1) {
      if (var1 instanceof ENYKFormattedTextField.ENYKMaxLengthIridsDocument) {
         super.setDocument(var1);
      } else if (this.initialized) {
         throw new RuntimeException("Invalid document type !");
      }

   }

   public void setFont(Font var1) {
      this.ori_font = var1;
      super.setFont(var1);
   }

   public Font getFont() {
      return this.is_painting ? this.guessed_font : super.getFont();
   }

   public Color getForeground() {
      if (this.is_bwmode) {
         return Color.BLACK;
      } else if (this.isEnabled()) {
         return this.feature_font_color;
      } else if (this.is_printing) {
         return this.features.containsKey("disabled_fg_color") ? this.feature_disabled_fg_color : this.feature_font_color;
      } else {
         return this.feature_disabled_fg_color;
      }
   }

   public Color getBackground() {
      if (this.is_bwmode) {
         return Color.WHITE;
      } else if (this.is_printing) {
         if (this.officeFill()) {
            try {
               return new Color(Integer.parseInt((String)this.features.get("outer_bg_color"), 16));
            } catch (Exception var2) {
               return Color.WHITE;
            }
         } else {
            return this.isEnabled() ? new Color(Integer.parseInt((String)this.features.get("bgcolor"), 16)) : this.feature_disabled_bg_color;
         }
      } else if (this.isEnabled()) {
         try {
            return this.isFocusOwner() ? super.getBackground() : new Color(Integer.parseInt((String)this.features.get("bgcolor"), 16));
         } catch (Exception var3) {
            return super.getBackground();
         }
      } else {
         return this.officeFill() ? new Color(Integer.parseInt((String)this.features.get("outer_bg_color"), 16)) : this.feature_disabled_bg_color;
      }
   }

   private boolean officeFill() {
      if (this.features.containsKey("office_fill")) {
         String var1 = (String)this.features.get("office_fill");
         return "true".equals(var1.toLowerCase());
      } else {
         return false;
      }
   }

   public Color getDisabledTextColor() {
      return this.is_bwmode ? Color.BLACK : super.getDisabledTextColor();
   }

   public void print(Graphics var1) {
      if (this.is_printable && this.initialized) {
         this.is_printing = true;

         try {
            boolean var2 = false;
            byte var3 = 0;
            int var4 = Integer.parseInt((String)this.features.get("w"));
            int var5 = Integer.parseInt((String)this.features.get("h"));
            int var6 = Integer.parseInt((String)this.features.get("frame_line_width"));
            var1.setColor(this.is_bwmode ? Color.WHITE : this.getBackground());
            var1.fillRect(1, 1, var4 - 2, var5 - 2);
            var1.setColor(this.is_bwmode ? Color.BLACK : this.getForeground());
            Font var7 = this.getFont();
            var1.setFont(var7);
            FontMetrics var8 = var1.getFontMetrics();
            int var9 = var8.stringWidth(this.getText());
            int var10 = var8.getAscent();
            int var11 = (int)this.getBounds().getWidth();
            if (var9 > var11) {
               resizeFont(this);
               var1.setFont(this.guessed_font);
               var8 = var1.getFontMetrics();
               var9 = var8.stringWidth(this.getText());
               var10 = var8.getAscent();
            }

            String var12 = (String)this.features.get("alignment");
            int var23;
            if ("right".equals(var12)) {
               var23 = var4 - var9 - (1 + var6);
            } else if ("center".equals(var12)) {
               var23 = (var4 - var9) / 2;
            } else {
               var23 = 1 + var6;
            }

            int var24 = var3 + var10 + (var5 - var10) / 2;
            var1.drawString(this.getText(), var23, var24);
            boolean var13 = false;
            boolean var14 = false;
            boolean var15 = false;
            if (this.features.get("underline") != null) {
               var13 = true;
               var24 = var10 + 2;
            }

            if (this.features.get("middleline") != null) {
               var14 = true;
               var24 = var10 / 2 + 2;
            }

            if (this.features.get("aboveline") != null) {
               var15 = true;
               var24 = 2;
            }

            if (var13 || var14 || var15) {
               var1.drawLine(var23, var24, var4, var24);
            }

            int var16 = Integer.parseInt((String)this.features.get("frame_sides"));
            Color var17 = var1.getColor();
            var1.setColor(this.is_bwmode ? Color.BLACK : (Color)this.features.get("border_color"));
            Stroke var18 = ((Graphics2D)var1).getStroke();
            if (var6 > 0) {
               BasicStroke var19 = new BasicStroke((float)var6);
               ((Graphics2D)var1).setStroke(var19);
            }

            if ((var16 & 1) == 1) {
               var1.drawLine(0, 0, var4, 0);
            }

            if ((var16 & 2) == 2) {
               var1.drawLine(var4 - var6, 0, var4 - var6, var5);
            }

            if ((var16 & 4) == 4) {
               var1.drawLine(0, var5, var4, var5);
            }

            if ((var16 & 8) == 8) {
               var1.drawLine(0, 0, 0, var5);
            }

            var1.setColor(var17);
            ((Graphics2D)var1).setStroke(var18);
            this.paintSubtitle(var1);
            var1.setFont(var7);
         } finally {
            this.is_printing = false;
         }
      }

   }

   public void paint(Graphics var1) {
      if (this.initialized) {
         this.is_painting = true;
         Object var2 = ((Graphics2D)var1).getRenderingHint(RenderingHints.KEY_ANTIALIASING);
         ((Graphics2D)var1).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
         this.paintComp(var1);
         this.paintSubtitle(var1);
         ((Graphics2D)var1).setRenderingHint(RenderingHints.KEY_ANTIALIASING, var2);

         try {
            if ("1".equals(this.features.get("details").toString())) {
               this.paintTriangle(var1);
            }
         } catch (Exception var4) {
         }

         this.is_painting = false;
         this.painted = true;
      }
   }

   private void paintTriangle(Graphics var1) {
      if (!this.is_printing) {
         int var5;
         int var6;
         if (this.getBorder() != null) {
            Insets var7 = this.getBorder().getBorderInsets(this);
            if (var7 != null) {
               var5 = var7.right;
               var6 = var7.top;
            } else {
               var5 = 0;
               var6 = 0;
            }
         } else {
            var5 = 0;
            var6 = 0;
         }

         int var4 = (int)(this.zoom_f * (double)this.triangle_ab);
         this.tr_x[0] = var5;
         this.tr_y[0] = var6;
         this.tr_x[1] = var5 + var4;
         this.tr_y[1] = var6;
         this.tr_x[2] = var5;
         this.tr_y[2] = var6 + var4;
         var1.setColor(new Color(79, 182, 232));
         var1.fillPolygon(this.tr_x, this.tr_y, 3);
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

      super.paint(var1);
      if (var3 != null) {
         var3.setThickness(var2);
      }

   }

   public void setFeatures(Hashtable var1) {
      this.features.clear();
      this.features.putAll(var1);
      this.initialized = false;
      this.reinitialize();
      hackFeatures(this.features);
      this.applyFeatures2();
      this.initFeatures();
      this.initialized = true;
      this.repaint();
   }

   protected void reinitialize() {
      ENYKFormattedTextField.ENYKMaxLengthIridsDocument var1 = (ENYKFormattedTextField.ENYKMaxLengthIridsDocument)this.getDocument();
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

      switch(this.feature_data_type) {
      case 1:
      default:
         if (this.getHorizontalAlignment() != 2) {
            this.setHorizontalAlignment(2);
         }
         break;
      case 2:
         if (this.getHorizontalAlignment() != 4) {
            this.setHorizontalAlignment(4);
         }
      }

      int var1;
      if (this.feature_abev_mask != null) {
         if ((var1 = this.feature_abev_mask.indexOf("!")) >= 0) {
            this.is_money = this.feature_abev_mask.substring(0, var1).indexOf("%\\") >= 0;
         } else {
            this.is_money = this.feature_abev_mask.indexOf("%\\") >= 0;
         }
      } else {
         this.is_money = false;
      }

      if (this.feature_abev_mask != null) {
         this.feature_abev_mask = this.feature_abev_mask.replaceAll("%\\\\", "");
         this.feature_abev_mask = this.feature_abev_mask.replaceAll("%", "");
      }

      var1 = getFormatterMask(this.feature_abev_mask).length();
      this.feature_max_length = var1 > 0 && var1 > this.feature_max_length ? var1 : this.feature_max_length;
      ((ENYKFormattedTextField.ENYKMaxLengthIridsDocument)this.getDocument()).setMaxLength(this.feature_max_length);
      if (this.feature_abev_mask != null || this.feature_data_type >= 0) {
         applayMask(this.feature_abev_mask, this, this.feature_data_type);
      }

      AbstractFormatter var2 = this.getFormatter();
      this.feature_abev_input_rules_validated = createExtendedPattern(this.feature_abev_input_rules);
      if (var2 instanceof ENYKMaskFormatter) {
         ((ENYKMaskFormatter)var2).setIrids(this.feature_abev_input_rules_validated);
      } else if (var2 instanceof ENYKNumberFormatter) {
         ((ENYKNumberFormatter)var2).setIrids(this.feature_abev_input_rules_validated);
      } else if (var2 instanceof ENYKABEVNumberFormatter) {
         ((ENYKABEVNumberFormatter)var2).setIrids(this.feature_abev_input_rules_validated);
      } else if (var2 instanceof ENYKABEVMaskedNumberFormatter) {
         ((ENYKABEVMaskedNumberFormatter)var2).setIrids(this.feature_abev_input_rules_validated);
      } else {
         try {
            ((ENYKFormattedTextField.ENYKMaxLengthIridsDocument)this.getDocument()).setIrid(createExtendedIrids(this.feature_abev_input_rules, getFormatterMask(this.feature_abev_mask)));
         } catch (Exception var4) {
            var4.printStackTrace();
         }
      }

   }

   public Hashtable getFeatures() {
      return this.features;
   }

   public void setZoom(int var1) {
      this.zoom = var1;
      this.zoom_f = (double)var1 / 100.0D;
      resizeFont(this);
      this.repaint();
   }

   public int getRecordIndex() {
      return -1;
   }

   public int getZoom() {
      return this.zoom;
   }

   public void release() {
      this.getDocument().removeDocumentListener(this.enyk_document_listener);
      this.removePropertyChangeListener(this.enyk_property_change_listener);
      this.removeFocusListener(this.enyk_focus_listener);
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
      this.feature_abev_input_rules = null;
      this.feature_abev_input_rules_validated = null;
      this.ori_font = null;
      this.features.clear();
   }

   public Object getFieldValue() {
      Object var1;
      if (!this.use_super_value) {
         this.use_super_value = true;
         if (this.getFormatter() == null) {
            var1 = super.getText();
         } else {
            try {
               var1 = this.getFormatter().stringToValue(this.getText());
            } catch (ParseException var4) {
               var1 = "";
            }
         }

         this.use_super_value = false;
      } else {
         var1 = super.getValue();
      }

      try {
         return this.feature_round > 0 ? this.getValueWithRound(var1.toString()) : var1;
      } catch (Exception var3) {
         return var1;
      }
   }

   private String getValueWithRound(String var1) {
      if ("".equals(var1)) {
         return var1;
      } else {
         if (var1.indexOf(".") == -1) {
            var1 = var1.concat(".");
         }

         for(int var2 = 0; var2 < this.feature_round; ++var2) {
            var1 = var1.concat("0");
         }

         return var1.substring(0, var1.indexOf(".") + this.feature_round + 1);
      }
   }

   public Object getFieldValueWOMask() {
      return this.getFieldValue();
   }

   public void setValue(Object var1) {
      super.setValue(var1);
      if (this.getFormatter() == null) {
         this.setText(var1 == null ? "" : var1.toString());
      } else {
         try {
            this.setText(this.getFormatter().valueToString(var1));
         } catch (ParseException var4) {
            this.setText("");
         }
      }

      try {
         this.getCaret().setDot(((String)var1).length());
      } catch (Exception var3) {
         this.getCaret().setDot(0);
      }

   }

   protected void paintSubtitle(Graphics var1) {
      Rectangle var2 = getComponentEditorBound(this);
      paintSubtitle(var1, var2.width, var2.height, this.feature_abev_mask, (float)((int)((double)this.feature_abev_subscript_height * this.zoom_f)), this.is_bwmode ? Color.BLACK : this.feature_abev_subscript_color);
   }

   private static void resizeFont(ENYKFormattedTextField var0) {
      Font var1 = var0.getFont();
      float var2 = 0.5F;
      float var3 = 5.0F;
      Rectangle var4 = getComponentEditorBound(var0);
      Font var5 = null;
      FontMetrics var6 = var0.getFontMetrics(var1);
      String var7 = var0.getText();
      int var8 = var6.stringWidth(var7);
      int var9 = var6.getHeight();

      for(float var10 = (float)(var0.zoom_f * (double)var0.ori_font.getSize2D()); var4.width > var8 || var4.height > var9; var9 = var6.getHeight()) {
         var5 = var1;
         var1 = var1.deriveFont(var1.getSize2D() + var2);
         if (var1.getSize2D() > var10) {
            break;
         }

         var6 = var0.getFontMetrics(var1);
         var8 = var6.stringWidth(var7);
      }

      var1 = var5 == null ? var1 : var5;

      for(var1 = (float)var1.getSize() > var3 ? var1 : var1.deriveFont(var3); var4.width < var8 || var4.height < var9; var9 = var6.getHeight()) {
         var1 = var1.deriveFont(var1.getSize2D() - var2);
         if (var1.getSize2D() < var3) {
            break;
         }

         var6 = var0.getFontMetrics(var1);
         var8 = var6.stringWidth(var7);
      }

      if (!var1.equals(var0.guessed_font)) {
         var0.guessed_font = var1;
      }

   }

   private static synchronized void applayFeatures(ENYKFormattedTextField var0) {
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
      var1 = var0.features.get("data_type");
      var0.feature_data_type = var1 == null ? -1 : Integer.parseInt(var1.toString());
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

      var1 = var0.features.get("max_length");
      var0.feature_max_length = var1 == null ? -1 : Integer.parseInt(var1.toString());
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
            var0.setHorizontalAlignment(2);
         } else if ("right".equalsIgnoreCase(var2)) {
            var0.setHorizontalAlignment(4);
         } else if ("center".equalsIgnoreCase(var2)) {
            var0.setHorizontalAlignment(0);
         } else {
            try {
               var0.setHorizontalAlignment(var1 == null ? var0.getHorizontalAlignment() : Integer.parseInt(var1.toString()));
            } catch (NumberFormatException var5) {
               Tools.eLog(var5, 0);
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

      var1 = var0.features.get("font");
      if (var1 instanceof Font) {
         var0.setFont((Font)var1);
      }

      var1 = var0.features.get("round");
      if (var1 != null) {
         try {
            var0.feature_round = Integer.parseInt((String)var1);
         } catch (Exception var4) {
            Tools.eLog(var4, 0);
         }
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

   private static DefaultFormatter applayMask(String var0, ENYKFormattedTextField var1, int var2) {
      switch(var2) {
      case 1:
      default:
         return applayMask(var0, var1, ENYKMaskFormatter.class);
      case 2:
         return applayMask(var0, var1, ENYKABEVMaskedNumberFormatter.class);
      }
   }

   private static DefaultFormatter applayMask(String var0, ENYKFormattedTextField var1, Class var2) {
      if (var0 != null) {
         AbstractFormatter var4 = var1.getFormatter();
         if (var4 instanceof DefaultFormatter) {
            var4.uninstall();
            var1.setFormatterFactory((AbstractFormatterFactory)null);
         }

         String var3 = getFormatterMask(var0);
         if (var3.length() <= 0 && var2 != ENYKABEVNumberFormatter.class && var2 != ENYKABEVMaskedNumberFormatter.class) {
            DefaultFormatterFactory var9 = new DefaultFormatterFactory();
            var9.setDefaultFormatter((AbstractFormatter)null);
            var9.setDisplayFormatter((AbstractFormatter)null);
            var9.setEditFormatter((AbstractFormatter)null);
            var9.setNullFormatter((AbstractFormatter)null);
            var1.setFormatterFactory(var9);
         } else {
            try {
               Object var5;
               if (var2 == ENYKNumberFormatter.class) {
                  ENYKNumberFormatter var7 = new ENYKNumberFormatter(var3);
                  if (var1.feature_max_length > 0) {
                     var7.setMaxLength(var1.feature_max_length);
                  }

                  var7.setValueContainsLiteralCharacters(false);
                  var7.setPlaceholderCharacter(' ');
                  var7.setAllowsInvalid(false);
                  var7.setCommitsOnValidEdit(true);
                  var7.install(var1);
                  var5 = var7;
               } else if (var2 == ENYKABEVNumberFormatter.class) {
                  ENYKABEVNumberFormatter var10 = new ENYKABEVNumberFormatter();
                  if (var1.feature_max_length > 0) {
                     var10.setMaxLength(var1.feature_max_length);
                  }

                  if (var1.feature_round > 0) {
                     var10.setMaxRound(var1.feature_round);
                  }

                  var10.setCommitsOnValidEdit(true);
                  var10.install(var1);
                  var5 = var10;
               } else if (var2 == ENYKABEVMaskedNumberFormatter.class) {
                  ENYKABEVMaskedNumberFormatter var11 = new ENYKABEVMaskedNumberFormatter(var3, var1.is_money);
                  if (var1.feature_round > 0) {
                     var11.setMaxRound(var1.feature_round);
                  }

                  if (var1.feature_max_length > 0) {
                     var11.setMaxLength(var1.feature_max_length);
                  }

                  var11.setApplyMask(true);
                  var11.setCommitsOnValidEdit(true);
                  var11.install(var1);
                  var5 = var11;
               } else {
                  ENYKMaskFormatter var12 = new ENYKMaskFormatter(var3);
                  if (var1.feature_max_length > 0) {
                     var12.setMaxLength(var1.feature_max_length);
                  }

                  var12.setValueContainsLiteralCharacters(false);
                  var12.setPlaceholderCharacter(' ');
                  var12.setAllowsInvalid(false);
                  var12.setCommitsOnValidEdit(true);
                  var12.install(var1);
                  var5 = var12;
               }

               DefaultFormatterFactory var6 = new DefaultFormatterFactory();
               var6.setDefaultFormatter((AbstractFormatter)var5);
               var6.setDisplayFormatter((AbstractFormatter)var5);
               var6.setEditFormatter((AbstractFormatter)var5);
               var6.setNullFormatter((AbstractFormatter)var5);
               ((DefaultFormatter)var5).setOverwriteMode(false);
               var1.setFormatterFactory(var6);
               var1.setFocusLostBehavior(0);
               return (DefaultFormatter)var5;
            } catch (Exception var8) {
               var8.printStackTrace();
            }
         }
      }

      return null;
   }

   public static String getFormatterMask(String var0) {
      int var1;
      if ((var1 = var0.indexOf(33)) > 0) {
         return var0.substring(0, var1);
      } else {
         return var1 < 0 ? var0 : "";
      }
   }

   private static synchronized void paintSubtitle(Graphics var0, int var1, int var2, String var3, float var4, Color var5) {
      if (var3 != null && var5 != null) {
         int var6;
         if ((var6 = var3.indexOf(33)) >= 0) {
            String var8 = var3.substring(var6 + 1).trim();
            Color var11 = var0.getColor();

            Font var9;
            for(var9 = var0.getFont(); (float)var9.getSize() / var4 < 2.0F; --var4) {
            }

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

   private void paintDottedBackground(Graphics var1) {
      var1.setColor(Color.GRAY);
      Rectangle var2 = getComponentEditorBound(this);

      for(int var3 = 1; (double)var3 < var2.getWidth(); var3 += 5) {
         for(int var4 = 1; (double)var4 < var2.getHeight(); var4 += 5) {
            var1.drawOval(var3, var4 + 2 * (var3 % 2), 1, 1);
         }
      }

   }

   private void paintStripedBackground(Graphics var1) {
      var1.setColor(Color.GRAY);
      Rectangle var2 = getComponentEditorBound(this);

      int var3;
      for(var3 = 1; (double)var3 < var2.getWidth() - var2.getHeight() + 8.0D; var3 += 8) {
         var1.drawLine(var3, 0, var3 + (int)var2.getHeight(), (int)var2.getHeight());
      }

      for(var3 = (int)var2.getHeight(); (double)var3 < var2.getWidth() + 8.0D; var3 += 8) {
         var1.drawLine(var3, 0, var3 - (int)var2.getHeight(), (int)var2.getHeight());
      }

   }

   private static Rectangle getComponentEditorBound(ENYKFormattedTextField var0) {
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

   private static String createExtendedPattern(String var0) {
      if (var0 != null && !var0.endsWith("*") && var0.length() > 0) {
         var0 = var0 + "*";
      }

      return var0;
   }

   private static String createExtendedIrids(String var0, String var1) {
      if (var0 != null) {
         if (!var0.startsWith("[^")) {
            String var2 = "";
            if (var1 != null) {
               int var3 = 0;

               for(int var4 = var1.length(); var3 < var4; ++var3) {
                  if (var2.indexOf(var1.charAt(var3)) < 0) {
                     var2 = var2 + var1.charAt(var3);
                  }
               }

               var2 = createExtendedPattern(var2);
               var2 = var2.replaceAll("#", " ");
            }

            if (var0.endsWith("]")) {
               if (var2.length() > 0) {
                  return var0.substring(0, var0.length() - 1) + "[" + var2 + "]]*";
               }

               return var0 + "*";
            }
         } else if (!var0.endsWith("*")) {
            var0 = var0 + "*";
         }
      }

      return var0;
   }

   static {
      focus_bg_color = Color.YELLOW;
      ro_bg_color = new Color(255, 239, 255);
      ro_fg_color = new Color(100, 100, 100);
   }

   private static class ENYKInputVerifier extends InputVerifier implements IPropertyList {
      private ENYKInputVerifier() {
      }

      public boolean verify(JComponent var1) {
         return true;
      }

      public boolean set(Object var1, Object var2) {
         return false;
      }

      public Object get(Object var1) {
         ENYKFormattedTextField var2 = (ENYKFormattedTextField)var1;
         AbstractFormatter var3 = var2.getFormatter();

         try {
            if (var3 instanceof ENYKABEVNumberFormatter) {
               var2.setValue(((ENYKABEVNumberFormatter)var3).stringToValue_(var2.getText(), false));
            } else if (var3 instanceof ENYKABEVMaskedNumberFormatter) {
               var2.setValue(((ENYKABEVMaskedNumberFormatter)var3).stringToValue_(var2.getText(), false));
            }
         } catch (ParseException var5) {
            var5.printStackTrace();
         }

         return null;
      }

      // $FF: synthetic method
      ENYKInputVerifier(Object var1) {
         this();
      }
   }

   private static class ENYKMaxLengthIridsDocument extends PlainDocument {
      private int max_length = -1;
      private String irid = null;
      private static final String NUMBERS = "0123456789";

      ENYKMaxLengthIridsDocument(int var1) {
         this.setMaxLength(var1);
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

      public void insertString(int var1, String var2, AttributeSet var3) throws BadLocationException {
         if (this.max_length > 0) {
            if (var1 >= this.max_length) {
               return;
            }

            if (this.modLength(this.getText(0, this.getLength())) > -1) {
               return;
            }

            if (var2 != null && var1 + var2.length() > this.max_length && this.modLength(var2) > 0) {
               try {
                  var2 = var2.substring(0, this.max_length - var1);
               } catch (Exception var5) {
                  return;
               }
            }
         }

         this.insertString2(var1, var2, var3);
      }

      public void setIrid(String var1) {
         if (var1 == null) {
            this.irid = null;
         } else if (var1.equals("")) {
            this.irid = null;
         } else if (var1.endsWith("*")) {
            this.irid = var1;
         } else {
            this.irid = var1 + "*";
         }

      }

      private void insertString2(int var1, String var2, AttributeSet var3) throws BadLocationException {
         if (var2 != null) {
            if (this.irid != null && !var2.matches(this.irid)) {
               if (!var2.toUpperCase().matches(this.irid)) {
                  return;
               }

               var2 = var2.toUpperCase();
            }

            if (var2.length() >= 1) {
               super.insertString(var1, var2, var3);
            } else {
               System.out.println("ILYEN IS VAN ??? " + var2);
               if (this.modLength(this.getText(0, this.getLength())) < 0) {
                  super.insertString(var1, var2, var3);
               } else {
                  Toolkit.getDefaultToolkit().beep();
               }
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

   private static class ENYKDocumentListener implements DocumentListener {
      private ENYKFormattedTextField comp;

      ENYKDocumentListener(ENYKFormattedTextField var1) {
         this.comp = var1;
      }

      public void changedUpdate(DocumentEvent var1) {
         ENYKFormattedTextField.resizeFont(this.comp);
      }

      public void insertUpdate(DocumentEvent var1) {
         ENYKFormattedTextField.resizeFont(this.comp);
      }

      public void removeUpdate(DocumentEvent var1) {
         ENYKFormattedTextField.resizeFont(this.comp);
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
                  this.changeBorder(var5, (ENYKFormattedTextField)var1.getSource());
               }
            }
         }

      }

      private void changeBorder(boolean var1, ENYKFormattedTextField var2) {
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
            Color var5 = null;
            Color var6 = var2.feature_disabled_bg_color;
            if (var6 != null) {
               var3 = var6;
            }

            var6 = var2.feature_disabled_fg_color;
            Color var4;
            if (var6 != null) {
               var4 = var6;
            } else {
               var4 = var2.feature_border_color;
            }

            var6 = var2.feature_font_color;
            if (var6 != null) {
               var5 = var6;
            }

            if (var3 == null) {
               var3 = ENYKFormattedTextField.ro_bg_color;
            }

            if (var4 == null) {
               var4 = ENYKFormattedTextField.ro_fg_color;
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

   private static class ENYKKeyListener extends KeyAdapter {
      private ENYKKeyListener() {
      }

      public void keyPressed(KeyEvent var1) {
         ENYKFormattedTextField var2 = (ENYKFormattedTextField)var1.getSource();
         if (var2.need_value_clear && this.isClearKey(var1)) {
            var2.setValue("");
            var2.need_value_clear = false;
         }

      }

      public void keyTyped(KeyEvent var1) {
         if (var1.getKeyChar() != '\b') {
            ENYKFormattedTextField var2 = (ENYKFormattedTextField)var1.getSource();
            if (var2.feature_abev_input_rules != null) {
               char[] var3 = new char[]{var1.getKeyChar()};
               String var4 = new String(var3);
               if (!var4.matches(var2.feature_abev_input_rules_validated) && !var4.toUpperCase().matches(var2.feature_abev_input_rules_validated)) {
                  var1.consume();
               }

               try {
                  if (var2.feature_data_type == 2 && (var2.getText().indexOf(".") > -1 || var2.getText().indexOf(",") > -1) && (var4.equals(".") || var4.equals(","))) {
                     var1.consume();
                  }
               } catch (Exception var6) {
                  Tools.eLog(var6, 0);
               }

            }
         }
      }

      private boolean isClearKey(KeyEvent var1) {
         int var2 = var1.getKeyCode();
         return (var2 < 112 || var2 > 123) && (var2 < 61440 || var2 > 61451) && (var2 < 154 || var2 > 157) && (var2 < 3 || var2 > 39) && var2 != 225 && var2 != 40 && (var2 < 224 || var2 > 227);
      }

      // $FF: synthetic method
      ENYKKeyListener(Object var1) {
         this();
      }
   }

   private static class ENYKFocusListener implements FocusListener {
      private ENYKFocusListener() {
      }

      public void focusGained(FocusEvent var1) {
         ENYKFormattedTextField var2 = (ENYKFormattedTextField)var1.getSource();
         if (var2.feature_abev_override_mode) {
            var2.need_value_clear = true;
         }

         var2.ori_bg_color = var2.getBackground();
         var2.setBackground(ENYKFormattedTextField.focus_bg_color);
      }

      public void focusLost(FocusEvent var1) {
         ENYKFormattedTextField var2 = (ENYKFormattedTextField)var1.getSource();
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
