package hu.piller.enykp.gui.component;

import hu.piller.enykp.interfaces.IDataField;
import hu.piller.enykp.interfaces.IENYKComponent;
import hu.piller.enykp.interfaces.IPropertyList;
import hu.piller.enykp.util.base.Tools;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
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
import javax.swing.border.LineBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.TextUI;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.DefaultFormatter;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;
import javax.swing.text.Position.Bias;

public class ENYKFormattedTaggedTextField extends JFormattedTextField implements IDataField, CaretListener, IENYKComponent {
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
   public static final String FEATURE_DISABLED_FG_COLOR = "ro_fg_color";
   public static final String FEATURE_DISABLED_BG_COLOR = "ro_bg_color";
   public static final String FEATURE_VISIBLE_ON_PRINT = "printable";
   public static final String FEATURE_FONT = "font";
   public static final String FEATURE_FONT_COLOR = "font_color";
   public static final String FEATURE_ALIGNMENT = "alignment";
   public static final String FEATURE_MAX_LENGTH = "max_length";
   public static final String FEATURE_CHAR_RECT_WIDTH = "char_rect_width";
   public static final String FEATURE_DELIMITER_WIDTH = "delimiter_width";
   public static final String FEATURE_DELIMITER_HEIGHT = "delimiter_height";
   public static final String FEATURE_CHAR_RECT_DISTANCE = "char_rect_distance";
   public static final String FEATURE_DRAW_CARET_OVER_LENGTH = "draw_caret_over_length";
   public static final String FEATURE_NOT_FRAMEABLE_CHARS = "not_frameable_chars";
   public static final String FEATURE_OUTER_OPAQUE = "outer_opaque";
   public static final String FEATURE_OUTER_BACKGROUND = "outer_background";
   public static final String FEATURE_RECT_MASK = "rect_mask";
   public static final String FEATURE_ROUND = "round";
   public static final String FEATURE_MASK_COLOR = "mask_color";
   public static final Integer DATATYPE_STRING = new Integer(1);
   public static final Integer DATATYPE_NUMBER = new Integer(2);
   private static final Color focus_bg_color;
   private static final Color ro_bg_color;
   private static final Color ro_fg_color;
   private static final Color rect_color;
   private static final String draw_char_rect_chars = "/-";
   private static final String draw_delimiter_chars = ".";
   private FocusListener enyk_focus_listener;
   private PropertyChangeListener enyk_property_change_listener;
   private KeyListener enyk_key_listener;
   private DocumentListener enyk_document_listener;
   private ENYKTextCaret enyk_caret;
   private InputVerifier enyk_input_verifier;
   private MouseMotionListener enyk_mouse_motion_listener;
   protected Font guessed_font;
   protected Font ori_font;
   protected boolean initialized;
   protected boolean is_painting;
   protected boolean painted;
   protected boolean is_printable;
   protected boolean is_bwmode;
   protected boolean is_printing;
   protected final Hashtable features = new Hashtable(16);
   protected boolean need_value_clear;
   protected Color ori_bg_color;
   protected Color ori_fg_color;
   protected ENYKLineBorder ori_border;
   protected int zoom;
   protected double zoom_f;
   protected boolean is_money;
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
   private int feature_char_rect_width;
   private int feature_delimiter_width;
   private int feature_delimiter_height;
   private int feature_char_rect_distance;
   private boolean feature_draw_caret_over_length;
   private String feature_not_frameable_chars;
   private boolean feature_outer_opaque;
   private Color feature_outer_background;
   private Color feature_mask_color;
   private String feature_rect_mask;
   protected Rectangle graphics_bounds = new Rectangle();
   private final ENYKFormattedTaggedTextField.CharPaintDataList char_paint_data_list = new ENYKFormattedTaggedTextField.CharPaintDataList();
   private int caret_offset = 0;
   private Hashtable not_framable_char_widths = new Hashtable(16);
   private Rectangle zoomed_char_r = new Rectangle();
   private boolean tt = false;
   protected int pontossag = 0;
   private int triangle_ab = 7;
   private int[] tr_x = new int[3];
   private int[] tr_y = new int[3];
   private boolean is_setting_value;
   private boolean is_selection = false;
   private static final String NUMBERS = "0123456789";

   public ENYKFormattedTaggedTextField() {
      this.initialize();
   }

   public ENYKFormattedTaggedTextField(Object var1) {
      super(var1);
      this.initialize();
   }

   public ENYKFormattedTaggedTextField(Format var1) {
      super(var1);
      this.initialize();
   }

   public ENYKFormattedTaggedTextField(AbstractFormatter var1) {
      super(var1);
      this.initialize();
   }

   public ENYKFormattedTaggedTextField(AbstractFormatterFactory var1) {
      super(var1);
      this.initialize();
   }

   public ENYKFormattedTaggedTextField(AbstractFormatterFactory var1, Object var2) {
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
      this.setDocument(new ENYKFormattedTaggedTextField.ENYKMaxLengthIridsDocument(-1));
      this.setFont(this.getFont().deriveFont(1));
      this.guessed_font = this.getFont();
      this.enyk_property_change_listener = new ENYKFormattedTaggedTextField.ENYKPropertyChangeListener();
      this.enyk_focus_listener = new ENYKFormattedTaggedTextField.ENYKFocusListener();
      this.enyk_key_listener = new ENYKFormattedTaggedTextField.ENYKKeyListener();
      this.enyk_document_listener = new ENYKFormattedTaggedTextField.ENYKDocumentListener(this);
      this.enyk_caret = new ENYKTextCaret();
      this.enyk_input_verifier = new ENYKFormattedTaggedTextField.ENYKInputVerifier();
      this.enyk_mouse_motion_listener = new ENYKFormattedTaggedTextField.ENYKMouseMotionListener();
      this.getDocument().addDocumentListener(this.enyk_document_listener);
      this.addPropertyChangeListener(this.enyk_property_change_listener);
      this.addFocusListener(this.enyk_focus_listener);
      this.addKeyListener(this.enyk_key_listener);
      this.setCaret(new ENYKTextCaret());
      this.setInputVerifier(this.enyk_input_verifier);
      this.addMouseMotionListener(this.enyk_mouse_motion_listener);
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
      this.feature_char_rect_width = 15;
      this.feature_delimiter_width = 2;
      this.feature_delimiter_height = 2;
      this.feature_char_rect_distance = 0;
      this.feature_draw_caret_over_length = true;
      this.feature_not_frameable_chars = "";
      this.feature_outer_opaque = true;
      this.feature_outer_background = this.getBackground();
      this.feature_mask_color = this.getBackground();
      this.feature_rect_mask = "";
      this.initialized = true;
   }

   public void setUI(TextUI var1) {
      super.setUI(new ENYKFormattedTaggedTextUI(var1));
   }

   public void setBorder(Border var1) {
      if (var1 instanceof ENYKLineBorder) {
         super.setBorder(var1);
      } else if (this.initialized) {
         throw new RuntimeException("Invalid border type !");
      }

   }

   public void setDocument(Document var1) {
      if (var1 instanceof ENYKFormattedTaggedTextField.ENYKMaxLengthIridsDocument) {
         super.setDocument(var1);
      } else if (this.initialized) {
         throw new RuntimeException("Invalid document type !");
      }

   }

   public void setFont(Font var1) {
      if (this.char_paint_data_list != null) {
         this.char_paint_data_list.valid = false;
      }

      super.setFont(var1);
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
            super.print(var1);
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

   public void paintComp(Graphics var1) {
      if (var1 != null) {
         Caret var2 = this.getCaret();
         if (this.feature_outer_opaque) {
            this.paintOuterBackground(var1);
         } else {
            this.paintBackground(var1);
         }

         if (this.getDocument() != null) {
            Rectangle var3 = new Rectangle();
            Font var17 = this.getFont().deriveFont((float)(this.zoom_f * (double)this.getFont().getSize2D()));
            char[] var4 = getFormatterMask(this.feature_abev_mask).toCharArray();
            char[] var5 = this.getText().toCharArray();
            int var12 = var4.length;
            int var13 = var5.length;
            int var14 = var12 > var13 ? var12 : var13;
            var1.setFont(var17);
            FontMetrics var7 = var1.getFontMetrics(var17);
            int var9 = this.getSelectionStart();
            int var10 = this.getSelectionEnd();
            var3.setBounds(this.zoomed_char_r);
            int var8 = var3.y + (var3.height - (var7.getMaxAscent() + var7.getMaxDescent())) / 2 + var7.getMaxAscent();
            int var11 = (int)(this.zoom_f * (double)this.feature_char_rect_distance);
            ENYKFormattedTaggedTextField.CharPaintDataList var18 = this.char_paint_data_list;
            char[] var6;
            boolean var16;
            ENYKFormattedTaggedTextField.CharPaintData[] var19;
            int var22;
            if (!var18.valid) {
               var19 = new ENYKFormattedTaggedTextField.CharPaintData[var14];
               var18.char_paint_data = var19;

               for(var22 = 0; var22 < var14; ++var22) {
                  ENYKFormattedTaggedTextField.CharPaintData var20 = new ENYKFormattedTaggedTextField.CharPaintData();
                  if (var22 < var13) {
                     var6 = var5;
                  } else {
                     var6 = var4;
                  }

                  var20.c = var6[var22];
                  var16 = this.isFramable(var6, var22);
                  if (var16) {
                     ENYKFormattedTaggedTextField.FramePaintData var21 = new ENYKFormattedTaggedTextField.FramePaintData();
                     this.calcCharRectangle(var21, var3);
                     var20.fpd = var21;
                  }

                  this.calcChar(var20, var3, var16, var11, var6, var22, var7, var8);
                  var19[var22] = var20;
               }

               if (var19.length > 0) {
                  var18.adjustToWidth(this.getWidth());
               }

               var18.valid = true;
            }

            var19 = var18.char_paint_data;

            for(var22 = 0; var22 < var14; ++var22) {
               byte var25 = 0;
               boolean var15 = var9 < var10 && var22 >= var9 && var22 < var10;
               if (var22 < var13) {
                  var6 = var5;
               } else {
                  var6 = var4;
               }

               var16 = this.isFramable(var6, var22);
               if (!this.is_bwmode && this.feature_abev_mask != null) {
                  try {
                     if (Character.isLetterOrDigit(this.feature_abev_mask.charAt(var22))) {
                        var25 = 1;
                     }
                  } catch (Exception var24) {
                     var25 = 0;
                  }
               }

               this.paintCharRectangle(var19[var22].fpd, var1, var3, var15, var16, var25);
               this.paintChar(var19[var22], var1, var3, var15, var16, var11, var6, var22, var7, var8);
            }

            this.paintSubtitle(var1);
            this.graphics_bounds.x = 0;
            this.graphics_bounds.y = var3.y;
            this.graphics_bounds.height = var3.y;
            if (var2 != null && (var2.getDot() < var5.length || var2.getDot() == var5.length && this.feature_draw_caret_over_length)) {
               var2.paint(var1);
            }
         } else if (var2 != null && this.feature_draw_caret_over_length) {
            this.paintSubtitle(var1);
            var2.paint(var1);
         }
      }

   }

   public void setFeatures(Hashtable var1) {
      this.features.clear();
      this.features.putAll(var1);
      this.tt = false;
      if (var1.containsKey("round")) {
         try {
            this.pontossag = Integer.parseInt((String)var1.get("round"));
            this.tt = true;
         } catch (Exception var3) {
            this.pontossag = 0;
            this.tt = false;
         }
      }

      this.initialized = false;
      this.reinitialize();
      hackFeatures(this.features);
      this.applyFeatures2();
      this.initFeatures();
      this.initialized = true;
      if (this.feature_max_length < this.pontossag + 1) {
         System.out.println("Hibás tizedesjegy szám a sablonban! tjsz = " + (this.feature_max_length - 1) + " beállítva");
         this.pontossag = this.feature_max_length - 1;
      }

      this.repaint();
   }

   protected void reinitialize() {
      AbstractFormatter var1 = this.getFormatter();
      if (var1 instanceof DefaultFormatter) {
         var1.uninstall();
         this.setFormatterFactory((AbstractFormatterFactory)null);
         this.setFormatter((AbstractFormatter)null);
      }

      ENYKFormattedTaggedTextField.ENYKMaxLengthIridsDocument var2 = (ENYKFormattedTaggedTextField.ENYKMaxLengthIridsDocument)this.getDocument();
      var2.setMaxLength(-1);
      var2.setIrid((String)null);
      this.setText("");
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

      if (this.feature_abev_mask != null) {
         this.feature_abev_mask = this.feature_abev_mask.replaceAll("%\\\\", "");
         this.feature_abev_mask = this.feature_abev_mask.replaceAll("%", "");
      }

      int var1 = getFormatterMask(this.feature_abev_mask).length();
      this.feature_max_length = var1 > 0 && var1 > this.feature_max_length ? var1 : this.feature_max_length;
      ((ENYKFormattedTaggedTextField.ENYKMaxLengthIridsDocument)this.getDocument()).setMaxLength(this.feature_max_length);
      if (this.feature_abev_mask != null || this.feature_data_type >= 0) {
         applayMask(this.feature_abev_mask, this, this.feature_data_type);
      }

      this.feature_abev_input_rules_validated = createExtendedPattern(this.feature_abev_input_rules);
      AbstractFormatter var2 = this.getFormatter();
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
            ((ENYKFormattedTaggedTextField.ENYKMaxLengthIridsDocument)this.getDocument()).setIrid(createExtendedIrids(this.feature_abev_input_rules, getFormatterMask(this.feature_abev_mask)));
         } catch (Exception var4) {
            Tools.eLog(var4, 0);
         }
      }

   }

   public Hashtable getFeatures() {
      return this.features;
   }

   public void setZoom(int var1) {
      this.zoom = var1;
      this.zoom_f = (double)var1 / 100.0D;
      this.char_paint_data_list.valid = false;
      this.zoomed_char_r.x = 0;
      this.zoomed_char_r.y = 0;
      this.zoomed_char_r.width = (int)(this.zoom_f * (double)this.feature_char_rect_width);
      this.zoomed_char_r.height = this.getBounds().height;
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
      this.enyk_caret.deinstall(this);
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
      this.feature_not_frameable_chars = null;
      this.feature_outer_background = null;
      this.feature_mask_color = null;
      this.feature_rect_mask = null;
      this.ori_font = null;
      this.features.clear();
      this.not_framable_char_widths = null;
      this.zoomed_char_r = null;
      this.graphics_bounds = null;
   }

   public Object getFieldValue() {
      Object var1;
      if (!this.is_setting_value) {
         this.is_setting_value = true;
         if (this.getFormatter() == null) {
            var1 = super.getText();
         } else {
            try {
               var1 = this.getFormatter().stringToValue(this.getText());
               String var2 = var1 == null ? "" : var1.toString().trim();
               var1 = maskPlus(var2, this.feature_abev_mask, this.feature_abev_input_rules);
            } catch (ParseException var3) {
               var1 = "";
            }
         }

         this.is_setting_value = false;
      } else {
         var1 = super.getValue();
      }

      String var4 = this.commaInTheMask((String)var1);
      return var4;
   }

   public Object getFieldValueWOMask() {
      Object var1 = this.getFieldValue();

      try {
         return maskMinus(var1.toString(), this.feature_abev_mask);
      } catch (Exception var3) {
         return var1;
      }
   }

   public void setValue(Object var1) {
      this.setENYKValue(var1 == null ? null : var1.toString(), false);

      try {
         this.getCaret().setDot(((String)var1).length());
      } catch (Exception var3) {
         this.getCaret().setDot(0);
      }

   }

   public void setENYKValue(String var1, boolean var2) {
      if (this.isVisible()) {
         AbstractFormatter var3 = this.getFormatter();
         String var4;
         if (var3 != null) {
            try {
               var4 = var3.valueToString(var1);
               if (var4.equals(this.getText()) && !this.isVisible()) {
                  return;
               }
            } catch (ParseException var7) {
               Tools.eLog(var7, 0);
            }
         }

         var4 = var2 ? var1 : maskMinus(var1, this.feature_abev_mask);
         super.setValue(var4);
         if (this.getFormatter() == null) {
            this.setText(var4 == null ? "" : var4);
         } else {
            try {
               this.setText(this.getFormatter().valueToString(var4));
            } catch (ParseException var6) {
               this.setText("");
            }
         }

      }
   }

   protected boolean isFramable(char[] var1, int var2) {
      boolean var3;
      if (this.feature_rect_mask.length() > 0) {
         try {
            var3 = this.feature_rect_mask.charAt(var2) == '#';
         } catch (Exception var9) {
            var3 = true;
         }
      } else {
         var3 = true;
      }

      boolean var4;
      if (this.feature_abev_mask.length() > 0) {
         try {
            if (this.feature_abev_mask.charAt(0) == '%') {
               var4 = true;
            } else {
               char var6 = this.feature_abev_mask.charAt(var2);
               if (var6 == '#') {
                  var4 = true;
               } else {
                  var4 = "\\/-.".indexOf(var6) < 0;
               }
            }
         } catch (Exception var8) {
            var4 = true;
         }
      } else {
         var4 = true;
      }

      boolean var5;
      if (this.feature_not_frameable_chars.length() > 0) {
         try {
            var5 = this.feature_not_frameable_chars.indexOf(var1[var2]) < 0;
         } catch (Exception var7) {
            var5 = true;
         }
      } else {
         var5 = true;
      }

      return var3 && var4 && var5;
   }

   protected void paintSubtitle(Graphics var1) {
      Rectangle var2 = getComponentEditorBound(this);
      paintSubtitle(var1, var2.width, var2.height, this.feature_abev_mask, (float)((double)this.feature_abev_subscript_height * this.zoom_f), this.is_bwmode ? Color.BLACK : this.feature_abev_subscript_color);
   }

   protected static void applayFeatures(ENYKFormattedTaggedTextField var0) {
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
      var1 = var0.features.get("ro_fg_color");
      if (var1 instanceof Color) {
         var0.feature_disabled_fg_color = (Color)var1;
      } else {
         var0.feature_disabled_fg_color = var1 == null ? ro_fg_color : new Color(Integer.parseInt(var1.toString(), 16));
      }

      var1 = var0.features.get("ro_bg_color");
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
      var1 = var0.features.get("char_rect_width");
      var0.feature_char_rect_width = var1 == null ? 15 : Integer.parseInt(var1.toString());
      var1 = var0.features.get("delimiter_width");
      var0.feature_delimiter_width = var1 == null ? 2 : Integer.parseInt(var1.toString());
      var1 = var0.features.get("delimiter_height");
      var0.feature_delimiter_height = var1 == null ? 2 : Integer.parseInt(var1.toString());
      var1 = var0.features.get("char_rect_distance");
      var0.feature_char_rect_distance = var1 == null ? 0 : Integer.parseInt(var1.toString());
      var1 = var0.features.get("draw_caret_over_length");
      if (var1 instanceof Boolean) {
         var0.feature_draw_caret_over_length = (Boolean)var1;
      } else {
         var2 = var1 == null ? "" : var1.toString();
         var0.feature_draw_caret_over_length = var2.equalsIgnoreCase("yes") || var2.equalsIgnoreCase("true") || var2.equalsIgnoreCase("igen");
      }

      var1 = var0.features.get("not_frameable_chars");
      var0.feature_not_frameable_chars = var1 == null ? "" : var1.toString();
      var1 = var0.features.get("outer_opaque");
      if (var1 instanceof Boolean) {
         var0.feature_outer_opaque = (Boolean)var1;
      } else {
         var2 = var1 == null ? "" : var1.toString();
         var0.feature_outer_opaque = var2.equalsIgnoreCase("yes") || var2.equalsIgnoreCase("true") || var2.equalsIgnoreCase("igen");
      }

      var1 = var0.features.get("outer_background");
      if (var1 instanceof Color) {
         var0.feature_outer_background = (Color)var1;
      } else {
         var0.feature_outer_background = var1 == null ? var0.getBackground() : new Color(Integer.parseInt(var1.toString(), 16));
      }

      var1 = var0.features.get("mask_color");
      if (var1 instanceof Color) {
         var0.feature_mask_color = (Color)var1;
      } else {
         var0.feature_mask_color = var1 == null ? var0.getBackground() : Color.WHITE;
      }

      var1 = var0.features.get("rect_mask");
      var0.feature_rect_mask = var1 == null ? "" : var1.toString();
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

      var1 = var0.features.get("font");
      if (var1 instanceof Font) {
         var0.setFont((Font)var1);
      }

   }

   protected static void hackFeatures(Hashtable var0) {
      String var1 = "";
      String var2 = "";
      Integer var3 = new Integer(-1);

      try {
         if (var0.get("datatype").equals("date")) {
            var0.put("alignment", "left");
         }
      } catch (Exception var6) {
         Tools.eLog(var6, 0);
      }

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

   protected static DefaultFormatter applayMask(String var0, ENYKFormattedTaggedTextField var1, int var2) {
      switch(var2) {
      case 1:
      default:
         return applayMask(var0, var1, ENYKMaskFormatter.class);
      case 2:
         return applayMask(var0, var1, ENYKABEVMaskedNumberFormatter.class);
      }
   }

   private static DefaultFormatter applayMask(String var0, ENYKFormattedTaggedTextField var1, Class var2) {
      if (var0 != null) {
         AbstractFormatter var4 = var1.getFormatter();
         if (var4 instanceof DefaultFormatter) {
            var4.uninstall();
            var1.setFormatterFactory((AbstractFormatterFactory)null);
            var1.setFormatter((AbstractFormatter)null);
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

                  if (var1.pontossag > 0) {
                     var10.setMaxRound(var1.pontossag);
                  }

                  var10.setCommitsOnValidEdit(true);
                  var10.install(var1);
                  var5 = var10;
               } else if (var2 == ENYKABEVMaskedNumberFormatter.class) {
                  ENYKABEVMaskedNumberFormatter var11 = new ENYKABEVMaskedNumberFormatter(var3, var1.is_money);
                  if (var1.feature_max_length > 0) {
                     var11.setMaxLength(var1.feature_max_length);
                  }

                  if (var1.pontossag > 0) {
                     var11.setMaxRound(var1.pontossag);
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

   public static String maskPlus(String var0, String var1, String var2) {
      if ("".equals(var0)) {
         return var0;
      } else if (var1.indexOf("#") == -1) {
         return var0;
      } else {
         String var4 = var0;
         String var5 = var1;

         int var6;
         for(var6 = 0; var6 < "\\.,-/\\ ".length(); ++var6) {
            var4 = var4.replaceAll("\\" + "\\.,-/\\ ".charAt(var6), "");
            var5 = var5.replaceAll("\\" + "\\.,-/\\ ".charAt(var6), "");
         }

         if (!var5.startsWith("#")) {
            var6 = var5.indexOf("#");
            var0 = var5.substring(0, var6);
            var0 = var0 + var4;
         }

         if (var2 != null && !var0.matches(var2 + "*") && !maskMinus(var0, var1).matches(var2 + "*")) {
            var0 = "";
         }

         return var0;
      }
   }

   protected static String maskMinus(String var0, String var1) {
      if (var0 == null) {
         return null;
      } else if (var1 == null) {
         return var0;
      } else if (!var1.equals("") && !var1.startsWith("%")) {
         StringBuffer var2 = new StringBuffer();
         StringBuffer var3 = new StringBuffer();

         for(int var4 = 0; var4 < var0.length(); ++var4) {
            try {
               if (var0.charAt(var4) != var1.charAt(var4)) {
                  if (var3.length() > 0) {
                     if ("-\\/(),.#".indexOf(var1.charAt(var4)) == -1) {
                        var2.insert(0, var3);
                        var3.delete(0, var3.length());
                     }

                     if (var1.charAt(var4) == '#') {
                        var3.delete(0, var3.length());
                     }
                  }

                  if (var0.charAt(var4) != '.' || var1.charAt(var4) != ',') {
                     var2.append(var0.charAt(var4));
                  }
               } else if ("-\\/(),.".indexOf(var1.charAt(var4)) == -1) {
                  var3.append(var0.charAt(var4));
               }
            } catch (Exception var6) {
               Tools.eLog(var6, 0);
            }
         }

         return var2.toString();
      } else {
         return var0;
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
            var0.drawString(var8, var1 - var7, var2);
            var0.setColor(var11);
            var0.setFont(var9);
         }
      }
   }

   private static Rectangle getComponentEditorBound(ENYKFormattedTaggedTextField var0) {
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

   protected void paintOuterBackground(Graphics var1) {
      Color var2 = var1.getColor();
      Color var3;
      if (this.is_bwmode) {
         var3 = Color.WHITE;
      } else {
         var3 = this.feature_outer_background;
      }

      var1.setColor(var3);
      var1.fillRect(0, 0, this.getWidth(), this.getHeight());
      var1.setColor(var2);
   }

   private void calcChar(ENYKFormattedTaggedTextField.CharPaintData var1, Rectangle var2, boolean var3, int var4, char[] var5, int var6, FontMetrics var7, int var8) {
      var1.setBounds(0, 0, 0, 0);
      if (var3) {
         var1.x = var2.x + (var2.width - var7.charWidth(var5[var6])) / 2;
         var1.y = var8;
         var2.x += var2.width + var4;
      } else {
         char var9 = var5[var6];
         int var10 = var7.charWidth(var9);
         if ("/-".indexOf(var9) >= 0) {
            var1.x = var2.x;
            var1.y = var8;
            var1.width = var2.width;
            var10 = var2.width;
            var2.x += var10 + var4;
         } else {
            var2.x -= var4;
            var2.x = var2.x < 0 ? 0 : var2.x;
            var1.x = var2.x;
            var1.y = var8;
            if (".".indexOf(var9) >= 0) {
               int var11 = (int)(this.zoom_f * (double)this.feature_delimiter_width);
               int var12 = (int)(this.zoom_f * (double)this.feature_delimiter_height);
               var1.y = var2.height - var12;
               var1.height = var12;
               var10 = var11;
            }

            var2.x += var10;
         }

         var1.width = var10;
         this.not_framable_char_widths.put(String.valueOf(var5[var6]), new Integer(var10));
      }

   }

   protected void paintChar(ENYKFormattedTaggedTextField.CharPaintData var1, Graphics var2, Rectangle var3, boolean var4, boolean var5, int var6, char[] var7, int var8, FontMetrics var9, int var10) {
      Color var11 = var2.getColor();
      Color var12;
      if (var5) {
         if (this.is_bwmode) {
            var12 = Color.BLACK;
         } else if (var4) {
            var12 = this.getSelectedTextColor();
         } else {
            var12 = this.feature_font_color;
            if (var12 == null) {
               var12 = this.getForeground();
            }
         }

         var2.setColor(var12);
         var2.drawChars(var7, var8, 1, var1.x, var1.y);
         this.graphics_bounds.width = var1.fpd.x + var1.fpd.width;
      } else {
         char var13 = var1.c;
         if (this.is_bwmode) {
            var12 = Color.BLACK;
         } else {
            var12 = this.feature_border_color;
         }

         var2.setColor(var12);
         if ("/-".indexOf(var13) >= 0) {
            Graphics2D var16 = (Graphics2D)var2;
            int var18 = (int)(this.zoom_f * (double)this.feature_char_rect_width);
            double var19 = (double)var9.charWidth(var13);
            GlyphVector var17;
            int var23;
            int var24;
            if (var13 == '/') {
               var17 = var2.getFont().createGlyphVector(var16.getFontRenderContext(), new char[]{var13});
               int var21 = (int)(-var17.getVisualBounds().getY());
               int var22 = Math.max(var2.getFont().getSize() / 7, 1);
               var23 = var1.x + var18 - 2 - var22;

               for(var24 = 1; var24 <= var22; ++var24) {
                  var2.drawLine(var1.x + var24 + var22, var1.y - 1, var23 + var24 - var22, var1.y - var21 - 1);
               }
            } else {
               double var25 = var19 == 0.0D ? 1.0D : (double)(var18 > 2 ? var18 - 2 : var18) / var19;
               var17 = var2.getFont().createGlyphVector(var16.getFontRenderContext(), new char[]{var13});
               AffineTransform[] var15 = new AffineTransform[var17.getNumGlyphs()];
               var23 = 0;

               for(var24 = var17.getNumGlyphs(); var23 < var24; ++var23) {
                  var15[var23] = var17.getGlyphTransform(var23);
                  AffineTransform var14;
                  if (var15[var23] == null) {
                     var14 = new AffineTransform();
                  } else {
                     var14 = new AffineTransform(var15[var23]);
                  }

                  var14.scale(var25, 1.0D);
                  var17.setGlyphTransform(var23, var14);
               }

               var16.drawGlyphVector(var17, (float)var1.x + (float)(((double)var1.width - var17.getLogicalBounds().getWidth()) / 2.0D), (float)var1.y);
               var23 = 0;

               for(var24 = var17.getNumGlyphs(); var23 < var24; ++var23) {
                  var17.setGlyphTransform(var23, var15[var23]);
               }
            }
         } else if (".".indexOf(var13) >= 0) {
            var2.fillRect(var1.x, var1.y, var1.width, var1.height);
         } else {
            var2.drawChars(var7, var8, 1, var1.x, var1.y);
         }
      }

      var2.setColor(var11);
   }

   private void calcCharRectangle(ENYKFormattedTaggedTextField.FramePaintData var1, Rectangle var2) {
      ENYKLineBorder var4 = (ENYKLineBorder)this.getBorder();
      Rectangle var5 = var1.inner_rect;
      int var3;
      if (var4 != null) {
         var3 = (int)(this.zoom_f * (double)var4.getThickness() + 0.5D);
         var3 = var3 < 1 ? 1 : var3;
      } else {
         var3 = 1;
      }

      var1.x = var2.x;
      var1.y = var2.y;
      var1.width = var2.width;
      var1.height = var2.height;
      var5.x = var2.x + var3;
      var5.y = var2.y + var3;
      var5.width = var2.width - var3 - var3;
      var5.height = var2.height - var3 - var3;
   }

   protected void paintCharRectangle(ENYKFormattedTaggedTextField.FramePaintData var1, Graphics var2, Rectangle var3, boolean var4, boolean var5, int var6) {
      if (var5) {
         ENYKLineBorder var8 = (ENYKLineBorder)this.getBorder();
         Color var7;
         if (this.isEnabled()) {
            if (var8 == null) {
               var7 = rect_color;
            } else {
               var7 = var8.getLineColor();
            }
         } else {
            var7 = this.getForeground();
         }

         if (var8 == null) {
            var2.setColor(var7);
            var2.drawRect(var1.x, var1.y, var1.width, var1.height);
         } else {
            int var9 = var8.getThickness();
            int var10 = (int)(this.zoom_f * (double)var9 + 0.5D);
            var10 = var10 < 1 ? 1 : var10;
            var8.setThickness(var10);
            var8.setBWMode(this.is_bwmode);
            var8.paintBorder(this, var2, var1.x, var1.y, var1.width, var1.height);
            var8.setThickness(var9);
         }

         if (this.is_bwmode) {
            var7 = Color.WHITE;
         } else {
            if (var4) {
               var7 = this.getSelectionColor();
            } else {
               var7 = this.getBackground();
            }

            if (!this.isOpaque() && !var4 && this.isEnabled() && !this.hasFocus()) {
               if (this.getParent() == null) {
                  var7 = this.getBackground();
               } else {
                  var7 = this.getParent().getBackground();
               }
            }
         }

         if (var6 == 0) {
            var2.setColor(var7);
         } else {
            var2.setColor(this.feature_mask_color);
         }

         var2.fillRect(var1.inner_rect.x, var1.inner_rect.y, var1.inner_rect.width, var1.inner_rect.height);
      }

   }

   protected void paintBackground(Graphics var1) {
      Color var2 = var1.getColor();
      Color var3;
      if (this.is_bwmode) {
         var3 = Color.WHITE;
      } else if (this.getParent() == null) {
         var3 = this.getBackground();
      } else {
         var3 = this.getParent().getBackground();
      }

      var1.setColor(var3);
      var1.fillRect(0, 0, this.getWidth(), this.getHeight());
      var1.setColor(var2);
   }

   public Rectangle _modelToView(int var1, Bias var2) throws BadLocationException {
      if (this.getGraphics() != null && this.initialized && this.not_framable_char_widths != null) {
         Document var4 = this.getDocument();
         if (var4 instanceof AbstractDocument) {
            ((AbstractDocument)var4).readLock();
         }

         Rectangle var3;
         try {
            var3 = new Rectangle();
            var3.setBounds(this.zoomed_char_r);
            ENYKFormattedTaggedTextField.CharPaintData[] var6 = this.char_paint_data_list.char_paint_data;
            if (var6 != null) {
               int var7 = var6.length;
               ENYKFormattedTaggedTextField.CharPaintData var8;
               if (var1 < var7) {
                  var8 = var6[var1];
                  if (var8.fpd != null) {
                     var3.x = var8.fpd.x;
                     var3.width = var8.fpd.width;
                  } else {
                     var3.x = var8.x;
                     var3.width = var8.width;
                  }
               } else {
                  int var5 = (int)(this.zoom_f * (double)this.feature_char_rect_distance);
                  var8 = var6[var7 - 1];
                  if (var8.fpd != null) {
                     var3.x = var8.fpd.x + var8.fpd.width + var5;
                  } else {
                     var3.x = var8.x + var8.width + var5;
                  }
               }

               if (var8.fpd != null) {
                  Border var9 = this.getBorder();
                  int var10 = 0;
                  if (var9 instanceof LineBorder) {
                     var10 = var9.getBorderInsets(this).left;
                  }

                  var3.x += (int)(this.zoom_f * (double)this.caret_offset) + var10;
               }
            }
         } finally {
            if (var4 instanceof AbstractDocument) {
               ((AbstractDocument)var4).readUnlock();
            }

         }

         return var3;
      } else {
         return null;
      }
   }

   public int _viewToModel(Point var1, Bias[] var2) {
      if (this.getGraphics() != null && this.initialized && this.not_framable_char_widths != null) {
         Document var5 = this.getDocument();
         if (var5 instanceof AbstractDocument) {
            ((AbstractDocument)var5).readLock();
         }

         char[] var3;
         try {
            Rectangle var4 = new Rectangle();
            var4.setBounds(this.zoomed_char_r);
            var3 = this.getText().toCharArray();
            ENYKFormattedTaggedTextField.CharPaintData[] var7 = this.char_paint_data_list.char_paint_data;
            if (var7 != null) {
               int var8 = var7.length;

               for(int var10 = 0; var10 < var8; ++var10) {
                  ENYKFormattedTaggedTextField.CharPaintData var11 = var7[var10];
                  int var9;
                  if (var11.fpd != null) {
                     var9 = var11.fpd.x + var11.fpd.width;
                  } else {
                     var9 = var11.x + var11.width;
                  }

                  if (var1.x < var9) {
                     int var12 = var10 > 0 ? var10 : 0;
                     return var12;
                  }
               }
            }
         } finally {
            if (var5 instanceof AbstractDocument) {
               ((AbstractDocument)var5).readUnlock();
            }

         }

         int var6 = var3.length;
         return var6;
      } else {
         return -1;
      }
   }

   public int _getNextVisualPositionFrom(int var1, Bias var2, int var3, Bias[] var4) throws BadLocationException {
      switch(var3) {
      case 3:
         int var5 = this.getText().toCharArray().length;
         var1 = var1 < var5 ? var1 + 1 : var5;
         break;
      case 7:
         var1 = var1 > 0 ? var1 - 1 : 0;
      }

      return var1;
   }

   public void setCaret(Caret var1) {
      this.removeCaretListener(this);
      super.setCaret(var1);
      this.addCaretListener(this);
   }

   public void caretUpdate(CaretEvent var1) {
      if (this.is_selection && var1.getDot() == var1.getMark()) {
         this.is_selection = false;
         this.repaint();
      } else if (!this.is_selection && var1.getDot() != var1.getMark()) {
         this.is_selection = true;
      }

   }

   public static String delNaN(String var0) {
      StringBuffer var1 = new StringBuffer();

      for(int var2 = 0; var2 < var0.length(); ++var2) {
         if ("0123456789".indexOf(var0.charAt(var2)) > -1) {
            var1.append(var0.charAt(var2));
         }
      }

      return var1.toString();
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

               var2 = var2.replaceAll("\\\\", "\\\\\\\\");
               var2 = var2.replaceAll("\\.", "\\\\.");
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

   private String commaInTheMask(String var1) {
      if (!this.tt) {
         return var1;
      } else if (this.pontossag == 0) {
         return var1;
      } else if (var1.equals("")) {
         return var1;
      } else {
         int var2 = var1.indexOf(",");
         if (var2 == -1) {
            var2 = var1.indexOf(".");
         }

         if (var2 == -1) {
            if (var1.length() > this.feature_max_length - this.pontossag - 1) {
               var1 = var1.substring(0, this.feature_max_length - this.pontossag - 1);
            }

            return (var1 + ".00000000000000000000000000000000000000000000000000").substring(0, var1.length() + this.pontossag + 1);
         } else {
            int var3 = var1.length() - var2 - 1;
            if (var3 > this.pontossag) {
               return var1.substring(0, var1.length() - (var3 - this.pontossag));
            } else {
               String var5 = var1.substring(0, var2);
               String var4 = var1.substring(var2 + 1);
               var4 = this.fillTr(var4);
               return var5.substring(0, Math.min(var5.length(), this.feature_max_length - var4.length() - 1)) + "." + var4;
            }
         }
      }
   }

   private String fillTr(String var1) {
      int var2 = this.pontossag - var1.length();

      for(int var3 = 0; var3 < var2; ++var3) {
         var1 = var1 + "0";
      }

      return var1;
   }

   static {
      focus_bg_color = Color.YELLOW;
      ro_bg_color = new Color(255, 239, 255);
      ro_fg_color = new Color(100, 100, 100);
      rect_color = Color.BLUE;
   }

   private static class ENYKMouseMotionListener implements MouseMotionListener {
      private ENYKMouseMotionListener() {
      }

      public void mouseDragged(MouseEvent var1) {
         ENYKFormattedTaggedTextField var2 = (ENYKFormattedTaggedTextField)var1.getSource();
         var2.repaint();
      }

      public void mouseMoved(MouseEvent var1) {
      }

      // $FF: synthetic method
      ENYKMouseMotionListener(Object var1) {
         this();
      }
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
         ENYKFormattedTaggedTextField var2 = (ENYKFormattedTaggedTextField)var1;
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
         if (this.max_length <= 0 || var1 < this.max_length) {
            if (this.max_length > 0 && var2 != null && var1 + var2.length() > this.max_length) {
               var2 = var2.substring(0, this.max_length - var1);
            }

            this.insertString2(var1, var2, var3);
         }
      }

      public void setIrid(String var1) {
         if (var1 == null) {
            this.irid = null;
         } else {
            if (var1.equals("")) {
               return;
            }

            if (var1.endsWith("*")) {
               this.irid = var1;
            } else {
               this.irid = var1 + "*";
            }
         }

      }

      private void insertString2(int var1, String var2, AttributeSet var3) throws BadLocationException {
         if (var2 != null) {
            if (this.irid != null && !var2.matches(this.irid)) {
               return;
            }

            if (var2.length() > 1) {
               if (this.modLength(var2) > 0) {
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

   private static class ENYKDocumentListener implements DocumentListener {
      private ENYKFormattedTaggedTextField comp;

      ENYKDocumentListener(ENYKFormattedTaggedTextField var1) {
         this.comp = var1;
      }

      public void changedUpdate(DocumentEvent var1) {
         if (this.comp.char_paint_data_list != null) {
            this.comp.char_paint_data_list.valid = false;
         }

         this.comp.repaint();
      }

      public void insertUpdate(DocumentEvent var1) {
         if (this.comp.char_paint_data_list != null) {
            this.comp.char_paint_data_list.valid = false;
         }

         this.comp.repaint();
      }

      public void removeUpdate(DocumentEvent var1) {
         if (this.comp.char_paint_data_list != null) {
            this.comp.char_paint_data_list.valid = false;
         }

         this.comp.repaint();
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
                  this.changeBorder(var5, (ENYKFormattedTaggedTextField)var1.getSource());
               }
            }
         }

      }

      private void changeBorder(boolean var1, ENYKFormattedTaggedTextField var2) {
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
               var3 = ENYKFormattedTaggedTextField.ro_bg_color;
            }

            if (var4 == null) {
               var4 = ENYKFormattedTaggedTextField.ro_fg_color;
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
         ENYKFormattedTaggedTextField var2 = (ENYKFormattedTaggedTextField)var1.getSource();
         if (var2.need_value_clear && this.isClearKey(var1)) {
            var2.setValue("");
            var2.need_value_clear = false;
            var2.getCaret().setDot(0);
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
         ENYKFormattedTaggedTextField var2 = (ENYKFormattedTaggedTextField)var1.getSource();
         if (var2.feature_abev_override_mode) {
            var2.need_value_clear = true;
         }

         var2.ori_bg_color = var2.getBackground();
         var2.setBackground(ENYKFormattedTaggedTextField.focus_bg_color);
      }

      public void focusLost(FocusEvent var1) {
         ENYKFormattedTaggedTextField var2 = (ENYKFormattedTaggedTextField)var1.getSource();
         AbstractFormatter var3 = var2.getFormatter();
         if (var2.ori_bg_color != null) {
            var2.setBackground(var2.ori_bg_color);
         }

         try {
            if (var3 instanceof ENYKABEVNumberFormatter) {
               var2.setValue(((ENYKABEVNumberFormatter)var3).stringToValue_(var2.getText(), false));
            } else if (var3 instanceof ENYKABEVMaskedNumberFormatter) {
               var2.setValue(((ENYKABEVMaskedNumberFormatter)var3).stringToValue_(var2.getText(), false));
            }
         } catch (ParseException var5) {
            var5.printStackTrace();
         }

      }

      // $FF: synthetic method
      ENYKFocusListener(Object var1) {
         this();
      }
   }

   private static class CharPaintDataList {
      boolean valid;
      ENYKFormattedTaggedTextField.CharPaintData[] char_paint_data;

      private CharPaintDataList() {
      }

      public String toString() {
         if (this.char_paint_data == null) {
            return "";
         } else {
            StringBuffer var1 = new StringBuffer(64);
            int var2 = 0;

            for(int var3 = this.char_paint_data.length; var2 < var3; ++var2) {
               var1.append(this.char_paint_data[var2].c);
            }

            return var1.toString();
         }
      }

      public void adjustToWidth(int var1) {
         if (this.char_paint_data != null && this.char_paint_data.length > 0) {
            ENYKFormattedTaggedTextField.CharPaintData var2 = this.char_paint_data[this.char_paint_data.length - 1];

            while(true) {
               int var4 = 0;
               int var3 = var2.x + var2.width;
               if (var2.fpd != null) {
                  var4 = var2.fpd.x + var2.fpd.width;
               }

               int var5 = var3 > var4 ? var3 : var4;
               if (var5 == var1) {
                  break;
               }

               int var6 = Math.abs(var5 - var1);
               int var7 = var6 > this.char_paint_data.length ? this.char_paint_data.length : var6;
               int var8 = var5 < var1 ? 1 : -1;
               int var9 = var7;

               int var10;
               for(var10 = this.char_paint_data.length - 1; var10 >= 0; --var10) {
                  if (this.char_paint_data[var10].fpd != null) {
                     --var9;
                  }

                  if (var9 == 0) {
                     var10 = this.char_paint_data.length - var10;
                     break;
                  }
               }

               if (var9 == var7) {
                  return;
               }

               if (var10 == -1) {
                  var7 = this.char_paint_data.length;
               } else {
                  var7 = var10;
               }

               int var11 = this.char_paint_data.length - var7;

               for(int var12 = this.char_paint_data.length; var11 < var12; ++var11) {
                  var2 = this.char_paint_data[var11];
                  if (var2.fpd != null) {
                     var2.width += var8;
                     var2.fpd.changeWidthBy(var8);

                     for(int var13 = var11 + 1; var13 < var12; ++var13) {
                        ENYKFormattedTaggedTextField.CharPaintData var10000 = this.char_paint_data[var13];
                        var10000.x += var8;
                        if (this.char_paint_data[var13].fpd != null) {
                           this.char_paint_data[var13].fpd.changeXBy(var8);
                        }
                     }
                  }
               }
            }
         }

      }

      // $FF: synthetic method
      CharPaintDataList(Object var1) {
         this();
      }
   }

   private static class CharPaintData extends Rectangle {
      char c;
      ENYKFormattedTaggedTextField.FramePaintData fpd;

      private CharPaintData() {
      }

      public String toString() {
         return "[" + this.c + ", x:" + this.x + ", y:" + this.y + ", w:" + this.width + ", h:" + this.height + ", " + (this.fpd == null ? "" : "" + this.fpd) + "]";
      }

      // $FF: synthetic method
      CharPaintData(Object var1) {
         this();
      }
   }

   private static class FramePaintData extends Rectangle {
      Rectangle inner_rect;

      private FramePaintData() {
         this.inner_rect = new Rectangle();
      }

      void changeXBy(int var1) {
         this.x += var1;
         Rectangle var10000 = this.inner_rect;
         var10000.x += var1;
      }

      void changeWidthBy(int var1) {
         this.width += var1;
         Rectangle var10000 = this.inner_rect;
         var10000.width += var1;
      }

      public String toString() {
         return "[x:" + this.x + ", y:" + this.y + ", w:" + this.width + ", h:" + this.height + "; ix:" + this.x + ", iy:" + this.y + ", iw:" + this.width + ", ih:" + this.height + "]";
      }

      // $FF: synthetic method
      FramePaintData(Object var1) {
         this();
      }
   }
}
