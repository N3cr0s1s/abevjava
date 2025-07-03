package hu.piller.enykp.gui.model;

import hu.piller.enykp.alogic.calculator.calculator_c.Calculator;
import hu.piller.enykp.alogic.fileutil.DataChecker;
import hu.piller.enykp.alogic.settingspanel.SettingsStore;
import hu.piller.enykp.alogic.templateutils.FieldsGroups;
import hu.piller.enykp.alogic.templateutils.IFieldsGroupModel;
import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.gui.component.ENYKFormattedTextField;
import hu.piller.enykp.gui.framework.MainFrame;
import hu.piller.enykp.interfaces.IDataField;
import hu.piller.enykp.interfaces.IDataStore;
import hu.piller.enykp.util.base.Result;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.JComponent;
import org.xml.sax.Attributes;

public class DataFieldModel {
   public int type;
   public int x;
   public int y;
   public int w;
   public int h;
   public Rectangle r;
   public String key;
   public Object value;
   public Hashtable features;
   public boolean readonly;
   public boolean orig_readonly;
   public boolean visible;
   public boolean writeable;
   public boolean invisible;
   private DataFieldModel.KpForceType kp_force;
   private Rectangle origbounds;
   private Font origfont;
   private Font font;
   int fontsize;
   int zfhe;
   String fontname;
   int fontstyle;
   String mask;
   int len;
   Boolean opaque;
   private boolean bwprinting;
   private int commonprinting;
   private int index;
   private int dynindex;
   public int tabindex;
   public boolean printable;
   public boolean kprintable;
   private Color bgcolor;
   public Color bgc;
   public int role;
   private boolean textAreaContent;
   public static final String COMBO_SPLIT_DELIMITER = ",";
   public static final int MAX_TAB_INDEX = 10000;
   public static final String SPLITTER = "¦";
   public static final String SPLITTER2 = "#13";
   public static final String CHANGESTR = " ";
   public FormModel formmodel;
   public PageModel pagemodel;

   public DataFieldModel() {
      this.kp_force = DataFieldModel.KpForceType.notPresent;
      this.bwprinting = false;
      this.commonprinting = 0;
      this.textAreaContent = false;
   }

   public DataFieldModel(Attributes var1) {
      this.kp_force = DataFieldModel.KpForceType.notPresent;
      this.bwprinting = false;
      this.commonprinting = 0;
      this.textAreaContent = false;
      this.features = new Hashtable();
      this.features.put("_df_", this);
      this.attributes_done(var1, this.features);

      String var2;
      try {
         var2 = var1.getValue("datatype");
         this.features.put("datatype", var2);
         if (var2.equals("text")) {
            this.type = 0;
         } else if (var2.equals("ttext")) {
            this.type = 5;
         } else if (var2.equals("check")) {
            this.type = 1;
         } else if (var2.equals("combo")) {
            this.type = 2;
         } else if (var2.equals("date")) {
            this.type = 4;
         } else if (var2.equals("ftext")) {
            this.type = 7;
         } else if (var2.equals("tcombo")) {
            this.type = 6;
         } else if (var2.equals("tatext")) {
            this.type = 3;
         } else if (var2.equals("scrolltatext")) {
            this.type = 8;
         } else {
            this.type = 0;
         }
      } catch (Exception var6) {
         this.type = 0;
      }

      this.x = this.get_int(var1.getValue("x"), 0);
      this.y = this.get_int(var1.getValue("y"), 0);
      this.w = this.get_int(var1.getValue("w"), 0);
      this.h = this.get_int(var1.getValue("h"), 0);
      this.r = new Rectangle(this.x, this.y, this.w, this.h);
      this.tabindex = this.get_int(var1.getValue("tab_index"), 10000);
      this.key = var1.getValue("fid");
      this.value = "";
      this.readonly = var1.getValue("readonly") != null;
      this.orig_readonly = this.readonly;
      var2 = var1.getValue("visible");
      this.visible = var2 == null ? true : var2.equals("yes");
      var2 = var1.getValue("invisible");
      this.invisible = this.calculateVisibility(var2, var1.getValue("invisiblePlace"));
      if (this.invisible) {
         this.readonly = true;
      }

      var2 = var1.getValue("writeable");
      this.writeable = var2 != null;
      var2 = var1.getValue("values");
      String[] var3;
      if (var2 != null) {
         var3 = (String[])((String[])this.specsplit(var2).toArray(new String[0]));
         this.features.put("spvalues", var3);
      }

      var2 = var1.getValue("list");
      if (var2 != null) {
         var3 = (String[])((String[])this.specsplit(var2).toArray(new String[0]));
         this.features.put("splist", var3);
      }

      this.origbounds = new Rectangle(this.x, this.y, this.w, this.h);
      this.fontname = var1.getValue("fontname");
      if (this.fontname == null) {
         this.fontname = "Arial";
      }

      String var7 = var1.getValue("fonttype");
      if (var7 == null) {
         var7 = "plain";
      }

      this.fontstyle = 0;
      if (var7.equals("bold")) {
         this.fontstyle = 1;
      } else if (var7.equals("italic")) {
         this.fontstyle = 2;
      } else if (var7.equals("bolditalic")) {
         this.fontstyle = 3;
      } else if (var7.equals("italicbold")) {
         this.fontstyle = 3;
      }

      this.fontsize = this.get_int(var1.getValue("fontsize"), 8);
      this.zfhe = this.fontsize;
      this.getFont();
      this.origfont = this.font;
      this.mask = var1.getValue("mask");

      try {
         this.len = Integer.parseInt(var1.getValue("max_length"));
      } catch (NumberFormatException var5) {
         this.len = -2;
      }

      if (this.len < -1) {
      }

      if (this.len == 0) {
         this.len = -1;
      }

      this.opaque = Boolean.TRUE;
      String var4 = var1.getValue("outer_opaque");
      if (var4 != null && var4.equals("no")) {
         this.opaque = Boolean.FALSE;
      }

      var2 = var1.getValue("printable");
      this.printable = var2 == null ? true : var2.equals("True");
      var2 = var1.getValue("kp");
      this.kprintable = var2 == null ? this.printable : var2.equals("True");
      this.role = this.get_int(var1.getValue("role"), 15);
      this.bgc = this.get_color(var1.getValue("bgcolor"), Color.WHITE);
      if (this.features.containsKey("kp_force")) {
         this.kp_force = DataFieldModel.KpForceType.valueOf((String)this.features.get("kp_force"));
      }

      this.make_features(var1);
   }

   private void make_features(Attributes var1) {
      this.PUT(this.features, "abev_mask", this.mask);
      this.PUT(this.features, "border_color", this.get_color(var1.getValue("fgcolor"), (Color)null));
      this.PUT(this.features, "border_sides", var1.getValue("frame_sides"));
      this.PUT(this.features, "border_width", var1.getValue("frame_line_width"));
      this.PUT(this.features, "max_length", this.len + "");
      this.PUT(this.features, "outer_opaque", this.opaque);
      this.PUT(this.features, "outer_background", this.get_color(var1.getValue("outer_bg_color"), (Color)null));
      this.PUT(this.features, "disabled_bg_color", this.get_color(var1.getValue("ro_bg_color"), (Color)null));
      this.PUT(this.features, "disabled_fg_color", this.get_color(var1.getValue("ro_fg_color"), (Color)null));
      this.PUT(this.features, "visible_on_print", var1.getValue("printable"));
      this.PUT(this.features, "font", this.origfont);
      this.PUT(this.features, "font_color", this.get_color(var1.getValue("fontcolor"), (Color)null));
      this.PUT(this.features, "alignment", var1.getValue("alignment"));
      this.PUT(this.features, "delimiter_width", var1.getValue("delimiter_width"));
      this.PUT(this.features, "delimiter_height", var1.getValue("delimiter_height"));
      this.PUT(this.features, "abev_subscript_height", var1.getValue("subscript_height"));
      this.PUT(this.features, "abev_subscript_color", this.get_color(var1.getValue("abev_subscript_color"), (Color)null));
      this.PUT(this.features, "char_rect_width", var1.getValue("char_rect_width"));
      this.PUT(this.features, "char_rect_distance", var1.getValue("char_rect_dist_x"));
      this.PUT(this.features, "mask_color", this.get_color(var1.getValue("mask_color"), (Color)null));
   }

   private void PUT(Hashtable var1, Object var2, Object var3) {
      if (var3 != null) {
         var1.put(var2, var3);
      }

   }

   private void getFont() {
      Hashtable var1 = new Hashtable();
      var1.put("font_name", this.fontname);
      var1.put("font_style", new Integer(this.fontstyle));
      var1.put("font_size", new Integer(this.zfhe));
      this.font = (Font)GuiUtil.getFont(var1);
   }

   private Color get_color(Object var1, Color var2) {
      try {
         String var3 = var1.toString();
         return new Color(Integer.parseInt(var3, 16));
      } catch (Exception var4) {
         return var2;
      }
   }

   public void paint(Graphics var1, int var2, int var3) {
      if (this.visible) {
         if (!this.invisible) {
            if ((this.role & VisualFieldModel.getmask()) != 0) {
               try {
                  JComponent var4 = GuiUtil.getPainter(this.type);
                  var4.setBounds(this.r);
                  this.features.put("_painter_", Boolean.TRUE);
                  this.features.put("enabled", new Boolean(!this.readonly));
                  this.features.put("bwmode", new Boolean(this.bwprinting));
                  this.features.put("_value_", this.value);
                  this.features.put("_zoom_", new Integer(var2));
                  this.features.put("_isprinting_", Boolean.FALSE);
                  String var5 = SettingsStore.getInstance().get("gui", "felülírásmód");
                  if (var5 == null) {
                     var5 = "false";
                  }

                  this.features.put("abev_override_mode", var5);
                  String var6 = (String)this.features.get("bgcolor");
                  Color var7 = var4.getBackground();
                  Object var8 = null;
                  if (!"0".equals(MainFrame.role)) {
                     if (!this.bgcolor.equals(Color.WHITE)) {
                        this.features.put("bgcolor", this.getColorstr(this.bgcolor));
                        var4.setBackground(this.bgcolor);
                        if (this.readonly) {
                           var8 = this.features.get("disabled_bg_color");
                           this.features.put("disabled_bg_color", this.getColorstr(this.bgcolor));
                        }
                     }
                  } else if (this.bgcolor != null) {
                     this.features.put("bgcolor", this.getColorstr(this.bgcolor));
                     var4.setBackground(this.bgcolor);
                     if (this.readonly) {
                        var8 = this.features.get("disabled_bg_color");
                        this.features.put("disabled_bg_color", this.getColorstr(this.bgcolor));
                     }
                  }

                  ((IDataField)var4).setFeatures(this.features);
                  ((IDataField)var4).setValue(this.value);
                  ((IDataField)var4).setZoom(var2);
                  if (!"0".equals(MainFrame.role) && var4 instanceof ENYKFormattedTextField) {
                     if (this.readonly) {
                        var4.setBackground(new Color(255, 239, 255));
                     } else {
                        var4.setBackground(Color.white);
                     }
                  }

                  if (!"0".equals(MainFrame.role)) {
                     if (!this.bgcolor.equals(Color.WHITE) && !this.readonly) {
                        var4.setBackground(this.bgcolor);
                     }
                  } else if (this.bgcolor != null) {
                     var4.setBackground(this.bgcolor);
                  }

                  var1.translate(this.x, this.y);
                  var4.paint(var1);
                  var1.translate(-this.x, -this.y);
                  if (this.value != null && !this.value.toString().equals("") && !Calculator.getInstance().isCalculatorActive()) {
                     try {
                        Result var9 = DataChecker.getInstance().checkField(this.formmodel.bm, this.formmodel.id, this.key, this.value.toString(), var3);
                        if (!var9.isOk()) {
                           String var10 = this.value.toString();
                           Color var11 = var1.getColor();
                           var1.setColor(Color.RED);
                           var1.drawString(var10, this.x + 2, this.y + this.h / 2 + 5);
                           var1.setColor(var11);
                        }
                     } catch (Exception var12) {
                     }
                  }

                  var4.setBackground(var7);
                  if (var6 != null) {
                     this.features.put("bgcolor", var6);
                  } else {
                     this.features.remove("bgcolor");
                  }

                  if (var8 != null) {
                     this.features.put("disabled_bg_color", var8);
                  } else {
                     this.features.remove("disabled_bg_color");
                  }
               } catch (Exception var13) {
                  var13.printStackTrace();
               }

            }
         }
      }
   }

   private String getColorstr(Color var1) {
      try {
         String var2 = Integer.toHexString(var1.getRGB());
         return var2.substring(2).toUpperCase();
      } catch (Exception var3) {
         return "FFFFFF";
      }
   }

   public void print(Graphics var1, int var2) {
      if (this.visible) {
         if (!this.invisible) {
            if (this.printable) {
               if ((this.role & VisualFieldModel.getmask()) != 0) {
                  JComponent var3 = GuiUtil.getPainter(this.type);
                  Rectangle var4 = new Rectangle();
                  var4.x = this.origbounds.x * var2 / 100;
                  var4.y = this.origbounds.y * var2 / 100;
                  var4.width = this.origbounds.width * var2 / 100;
                  var4.height = this.origbounds.height * var2 / 100;
                  var3.setBounds(var4);
                  this.features.put("_painter_", Boolean.TRUE);
                  this.features.put("enabled", new Boolean(!this.readonly));
                  this.features.put("bwmode", new Boolean(this.bwprinting));
                  this.features.put("_value_", this.value);
                  this.features.put("_zoom_", new Integer(var2));
                  this.features.put("_isprinting_", Boolean.TRUE);
                  String var5 = (String)this.features.get("bgcolor");
                  Color var6 = var3.getBackground();
                  ((IDataField)var3).setFeatures(this.features);
                  ((IDataField)var3).setValue(this.value);
                  ((IDataField)var3).setZoom(var2);
                  var1.translate(var4.x, var4.y);
                  var3.print(var1);
                  var1.translate(-var4.x, -var4.y);
                  var3.setBackground(var6);
                  if (var5 != null) {
                     this.features.put("bgcolor", var5);
                  } else {
                     this.features.remove("bgcolor");
                  }

                  if (this.commonprinting != 0) {
                     String var7 = "";
                     if (this.commonprinting == 1) {
                        var7 = this.key;
                        if (this.index != 0) {
                           try {
                              var7 = var7.substring(6);
                           } catch (Exception var10) {
                           }
                        }

                        int var8 = var7.indexOf("XXXX");
                        if (var8 != -1) {
                           String var9 = "000" + this.dynindex;
                           var7 = var7.substring(0, 2) + var9.substring(var9.length() - 4) + var7.substring(6);
                        }
                     }

                     if (this.commonprinting == 2) {
                        var7 = (String)this.features.get("did");
                     }

                     if (this.commonprinting == 3) {
                        var7 = (String)this.features.get("vid");
                     }

                     Font var11 = var1.getFont();
                     var1.setFont(var11.deriveFont(8.0F));
                     Rectangle2D var12 = var1.getFontMetrics().getStringBounds(var7, var1);
                     var1.setColor(Color.WHITE);
                     var1.fillRect(var4.x, var4.y, (int)var12.getWidth() + 4, (int)var12.getHeight() + 4);
                     var1.setColor(Color.BLACK);
                     var1.drawString(var7, var4.x + 2, var4.y + (int)var12.getHeight() + 2);
                     var1.setFont(var11);
                  }

               }
            }
         }
      }
   }

   public boolean contains(Point var1) {
      return this.r.contains(var1);
   }

   public JComponent getJComponent(int var1, int var2) {
      JComponent var3 = GuiUtil.getJComponent(this.type);
      var3.setBounds(this.r);
      this.features.put("_painter_", Boolean.FALSE);
      this.features.put("enabled", new Boolean(!this.readonly));
      this.features.put("bwmode", new Boolean(this.bwprinting));
      this.features.put("_value_", this.value);
      this.features.put("_dynindex_", new Integer(var2));
      this.features.put("_zoom_", new Integer(var1));
      this.features.put("_isprinting_", Boolean.FALSE);
      this.features.put("abev_override_mode", SettingsStore.getInstance().get("gui", "felülírásmód"));
      this.features.put("readonlymode", new Boolean(MainFrame.thisinstance.mp.readonlymode || MainFrame.readonlymodefromubev));
      ((IDataField)var3).setFeatures(this.features);
      ((IDataField)var3).setValue(this.value);
      ((IDataField)var3).setZoom(var1);
      return var3;
   }

   private int get_int(Object var1, int var2) {
      try {
         return Integer.parseInt(var1.toString());
      } catch (Exception var4) {
         return var2;
      }
   }

   public Rectangle getOriginalBounds() {
      return this.origbounds;
   }

   public void setBounds(Rectangle var1) {
      this.x = var1.x;
      this.y = var1.y;
      this.w = var1.width;
      this.h = var1.height;
      this.r = new Rectangle(this.x, this.y, this.w, this.h);
   }

   public Font getOriginalFont() {
      return this.origfont;
   }

   public void setFontsize(double var1) {
      this.zfhe = (int)((double)this.fontsize * var1);
   }

   public void setFont(Font var1) {
      this.font = var1;
   }

   public void setMeta(Hashtable var1, FormModel var2) {
      if (var1 != null) {
         String var3 = var1.get("irids").toString();
         String var4 = var2.irids.get(var3).toString();
         this.features.putAll(var1);
         if (this.features.containsKey("textAreaContent") && var4.contains("x20")) {
            var4 = var4.replace("\\x20", "\\x09");
         }

         this.features.put("irids", var4);
         if (var1.get("type") != null) {
            try {
               Integer var5 = new Integer(Integer.parseInt(var1.get("type").toString()));
               this.features.put("data_type", var5);
            } catch (Exception var7) {
            }
         }

      }
   }

   public void setcombomatrices(FormModel var1) throws Exception {
      IFieldsGroupModel var2 = FieldsGroups.getInstance().getFieldsGroupByFid(FieldsGroups.GroupType.STATIC, var1.id, this.key);
      if (var2 != null) {
         Hashtable var3 = var2.getFidsColumns();
         PageModel var4 = (PageModel)var1.fids_page.get(this.key);
         this.checkGroup(var1, var3, var4);
      }

   }

   private void checkGroup(FormModel var1, Hashtable var2, PageModel var3) throws Exception {
      Enumeration var4 = var2.keys();
      String var5;
      PageModel var6;
      if (!var3.dynamic) {
         do {
            if (!var4.hasMoreElements()) {
               return;
            }

            var5 = (String)var4.nextElement();
            var6 = (PageModel)var1.fids_page.get(var5);
         } while(!var6.dynamic);

         throw new Exception("Dinamikus lapon van az egyik mező!\n" + this.features.get("field_group_id"));
      } else {
         do {
            if (!var4.hasMoreElements()) {
               return;
            }

            var5 = (String)var4.nextElement();
            var6 = (PageModel)var1.fids_page.get(var5);
         } while(var3.equals(var6));

         throw new Exception("Nem egy lapon lévő csoport!\n" + this.features.get("field_group_id"));
      }
   }

   private void attributes_done(Attributes var1, Hashtable var2) {
      for(int var3 = 0; var3 < var1.getLength(); ++var3) {
         var2.put(var1.getQName(var3), var1.getValue(var3));
      }

   }

   private Vector specsplit(String var1) {
      String var2 = var1.replaceAll("//", "\u0000");
      var2 = var2.replaceAll("/,", "\u0001");
      var2 = var2.replaceAll(",", "\u0002");
      var2 = var2.replaceAll("\u0000", "/");
      var2 = var2.replaceAll("\u0001", ",");
      String[] var3 = var2.split("\u0002");
      Vector var4 = new Vector(Arrays.asList(var3));
      return var4;
   }

   public void print(Graphics var1, int var2, int var3, int var4) {
      this.index = var3;
      this.dynindex = var4 + 1;

      int var5;
      try {
         var5 = Integer.parseInt(SettingsStore.getInstance().get("printer", "print.settings.colors"));
      } catch (NumberFormatException var8) {
         var5 = 1;
      }

      this.commonprinting = 0;

      try {
         String var6 = SettingsStore.getInstance().get("printer", "print.settings.kozos");
         this.commonprinting = var6.equals("1") ? 1 : (var6.equals("2") ? 2 : (var6.equals("3") ? 3 : 0));
      } catch (Exception var7) {
         this.commonprinting = 0;
      }

      boolean var9 = var5 == 12;
      if (var9) {
         this.bwprinting = true;
      }

      this.print(var1, var2);
      this.bwprinting = false;
      this.commonprinting = 0;
   }

   public void setBgColor(Color var1) {
      this.bgcolor = var1;
   }

   public Vector cprint(FormModel var1, int var2, int var3) {
      if (this.value != null && !this.value.equals("") && !this.value.equals("false")) {
         String var4 = this.getprompt(var1);
         Vector var5 = new Vector();
         String var6 = (String)this.features.get("row");
         var5.add(var6);
         var5.add(var4);
         var5.add(this.value);
         return var5;
      } else {
         return null;
      }
   }

   public String getprompt(FormModel var1) {
      String var2 = (String)this.features.get("prompt");
      if (var2 != null && !var2.equals("")) {
         String[] var3;
         if (var2.indexOf("¦") != -1) {
            var3 = var2.split("¦");
         } else {
            var3 = new String[]{var2};
         }

         String var4 = "";

         for(int var5 = 0; var5 < var3.length; ++var5) {
            if (var3[var5].length() != 0) {
               char var6 = var3[var5].charAt(0);

               try {
                  String var7;
                  String var8;
                  switch(var6) {
                  case 'A':
                     var4 = var4 + var3[var5].substring(2);
                     break;
                  case 'C':
                     if (var3[var5].charAt(1) == '=') {
                        var7 = ((VisualFieldModel)var1.labels.get(var3[var5].substring(2))).text;
                        var8 = var7.toString().replaceAll("#", " ");
                        var4 = var4 + var8;
                     } else {
                        var4 = var4 + var3[var5].substring(3);
                     }
                     break;
                  case 'E':
                     if (var3[var5].charAt(1) == '=') {
                        var7 = ((VisualFieldModel)var1.labels.get(var3[var5].substring(2))).text;
                        var8 = var7.toString().replaceAll("#", " ");
                        var4 = var4 + var8;
                     } else {
                        var4 = var4 + var3[var5].substring(3);
                     }
                     break;
                  case 'O':
                     if (var3[var5].charAt(1) == '=') {
                        var7 = ((VisualFieldModel)var1.labels.get(var3[var5].substring(2))).text;
                        var8 = var7.toString().replaceAll("#", " ");
                        var4 = var4 + var8;
                     } else {
                        var4 = var4 + var3[var5].substring(3);
                     }
                     break;
                  case 'S':
                     if (var3[var5].charAt(1) == '=') {
                        var7 = ((VisualFieldModel)var1.labels.get(var3[var5].substring(2))).text;
                        var8 = var7.toString().replaceAll("#", " ");
                        var4 = var4 + var8;
                     } else {
                        var4 = var4 + var3[var5].substring(3);
                     }
                  }
               } catch (Exception var9) {
               }

               if (var5 < var3.length - 1) {
                  var4 = var4 + " ";
               }
            }
         }

         return var4;
      } else {
         return this.key;
      }
   }

   public String getrowprompt(FormModel var1) {
      String var2 = (String)this.features.get("prompt");
      if (var2 != null && !var2.equals("")) {
         String[] var3;
         if (var2.indexOf("¦") != -1) {
            var3 = var2.split("¦");
         } else {
            var3 = new String[]{var2};
         }

         String var4 = "";

         for(int var5 = 0; var5 < var3.length; ++var5) {
            if (var3[var5].length() != 0) {
               char var6 = var3[var5].charAt(0);

               try {
                  switch(var6) {
                  case 'A':
                  case 'C':
                  case 'E':
                  case 'O':
                  default:
                     break;
                  case 'S':
                     if (var3[var5].charAt(1) == '=') {
                        String[] var7 = var3[var5].substring(2).split("#13");

                        for(int var8 = 0; var8 < var7.length; ++var8) {
                           String var9 = ((VisualFieldModel)var1.labels.get(var7[var8])).text;
                           String var10 = var9.toString().replaceAll("#", " ");
                           var4 = var4 + (var4.length() == 0 ? "" : " ") + var10;
                        }
                     } else {
                        var4 = var4 + var3[var5].substring(3);
                     }
                  }
               } catch (Exception var11) {
               }

               if (var5 < var3.length - 1) {
                  var4 = var4 + " ";
               }
            }
         }

         return var4;
      } else {
         return "";
      }
   }

   public String getcolprompt(FormModel var1) {
      String var2 = (String)this.features.get("prompt");
      if (var2 != null && !var2.equals("")) {
         String[] var3;
         if (var2.indexOf("¦") != -1) {
            var3 = var2.split("¦");
         } else {
            var3 = new String[]{var2};
         }

         String var4 = "";

         for(int var5 = 0; var5 < var3.length; ++var5) {
            if (var3[var5].length() != 0) {
               char var6 = var3[var5].charAt(0);

               try {
                  switch(var6) {
                  case 'A':
                  case 'C':
                  case 'E':
                  case 'S':
                  default:
                     break;
                  case 'O':
                     if (var3[var5].charAt(1) == '=') {
                        String[] var7 = var3[var5].substring(2).split("#13");

                        for(int var8 = 0; var8 < var7.length; ++var8) {
                           String var9 = ((VisualFieldModel)var1.labels.get(var7[var8])).text;
                           String var10 = var9.toString().replaceAll("#", " ");
                           var4 = var4 + (var4.length() == 0 ? "" : " ") + var10;
                        }
                     } else {
                        var4 = var4 + var3[var5].substring(3);
                     }
                  }
               } catch (Exception var11) {
               }

               if (var5 < var3.length - 1) {
                  var4 = var4 + " ";
               }
            }
         }

         return var4;
      } else {
         return "";
      }
   }

   public String geteprompt(FormModel var1) {
      String var2 = (String)this.features.get("prompt");
      if (var2 != null && !var2.equals("")) {
         String[] var3;
         if (var2.indexOf("¦") != -1) {
            var3 = var2.split("¦");
         } else {
            var3 = new String[]{var2};
         }

         String var4 = "";

         for(int var5 = 0; var5 < var3.length; ++var5) {
            if (var3[var5].length() != 0) {
               char var6 = var3[var5].charAt(0);

               try {
                  switch(var6) {
                  case 'A':
                  case 'C':
                  case 'O':
                  case 'S':
                  default:
                     break;
                  case 'E':
                     if (var3[var5].charAt(1) == '=') {
                        String[] var7 = var3[var5].substring(2).split("#13");

                        for(int var8 = 0; var8 < var7.length; ++var8) {
                           String var9 = ((VisualFieldModel)var1.labels.get(var7[var8])).text;
                           String var10 = var9.replaceAll("#", " ");
                           var4 = var4 + (var4.length() == 0 ? "" : " ") + var10;
                        }
                     } else {
                        var4 = var4 + var3[var5].substring(3);
                     }
                  }
               } catch (Exception var11) {
               }

               if (var5 < var3.length - 1) {
                  var4 = var4 + " ";
               }
            }
         }

         return var4;
      } else {
         return "";
      }
   }

   public String getcprompt(FormModel var1) {
      String var2 = (String)this.features.get("prompt");
      if (var2 != null && !var2.equals("")) {
         String[] var3;
         if (var2.indexOf("¦") != -1) {
            var3 = var2.split("¦");
         } else {
            var3 = new String[]{var2};
         }

         String var4 = "";

         for(int var5 = 0; var5 < var3.length; ++var5) {
            if (var3[var5].length() != 0) {
               char var6 = var3[var5].charAt(0);

               try {
                  switch(var6) {
                  case 'A':
                  case 'E':
                  case 'O':
                  case 'S':
                  default:
                     break;
                  case 'C':
                     if (var3[var5].charAt(1) == '=') {
                        String[] var7 = var3[var5].substring(2).split("#13");

                        for(int var8 = 0; var8 < var7.length; ++var8) {
                           String var9 = ((VisualFieldModel)var1.labels.get(var7[var8])).text;
                           String var10 = var9.toString().replaceAll("#", " ");
                           var4 = var4 + (var4.length() == 0 ? "" : " ") + var10;
                        }
                     } else {
                        var4 = var4 + var3[var5].substring(3);
                     }
                  }
               } catch (Exception var11) {
               }

               if (var5 < var3.length - 1) {
                  var4 = var4 + " ";
               }
            }
         }

         return var4;
      } else {
         return "";
      }
   }

   public Hashtable getgprompt(FormModel var1) {
      Hashtable var2 = new Hashtable();
      var2.put("gprompt", "");
      var2.put("index", new Integer(-1));
      String var3 = (String)this.features.get("prompt");
      if (var3 != null && !var3.equals("")) {
         String[] var4;
         if (var3.indexOf("¦") != -1) {
            var4 = var3.split("¦");
         } else {
            var4 = new String[]{var3};
         }

         String var5 = "";
         int var6 = -1;

         for(int var7 = 0; var7 < var4.length; ++var7) {
            if (var4[var7].length() != 0) {
               char var8 = var4[var7].charAt(0);

               try {
                  switch(var8) {
                  case 'A':
                  case 'E':
                  case 'O':
                  case 'S':
                  default:
                     break;
                  case 'G':
                     int var9 = var4[var7].indexOf("=");
                     int var10 = var4[var7].indexOf("#");
                     String var11 = var4[var7].substring(var9 + 1, var10);
                     var4[var7] = var4[var7].replaceFirst(var11, "");

                     try {
                        var6 = Integer.parseInt(var11);
                     } catch (NumberFormatException var15) {
                        var6 = -1;
                     }

                     if (var4[var7].charAt(1) == '=') {
                        try {
                           String var12 = ((VisualFieldModel)var1.labels.get(var4[var7].substring(var4[var7].indexOf("#") + 1))).text;
                           String var13 = var12.toString().replaceAll("#", " ");
                           var5 = var5 + var13;
                        } catch (Exception var14) {
                           var5 = "";
                        }
                     } else {
                        var5 = var5 + var4[var7].substring(4);
                     }
                  }
               } catch (Exception var16) {
               }

               if (var7 < var4.length - 1) {
                  var5 = var5 + " ";
               }
            }
         }

         var2.put("gprompt", var5);
         var2.put("index", new Integer(var6));
         return var2;
      } else {
         return var2;
      }
   }

   public String getKprintformat(String var1) {
      if (this.type == 4) {
         return var1.length() == 8 ? var1.substring(0, 4) + "." + var1.substring(4, 6) + "." + var1.substring(6, 8) + "." : var1;
      } else {
         String var2 = (String)this.features.get("mask");
         int var3 = var2.indexOf("!");
         return var3 != -1 ? var1 + " " + var2.substring(var3 + 1) : var1;
      }
   }

   public String getAdonem(IDataStore var1, int var2) {
      String var3 = (String)this.features.get("adonem");
      if (var3 == null) {
         var3 = "";
         String var4 = (String)this.features.get("hiv_adonem");
         if (var4 != null) {
            var3 = var1.get(var2 + "_" + var4);
         }
      }

      return var3;
   }

   private String get1000tagolas(String var1) {
      if (var1.length() < 4) {
         return var1;
      } else {
         StringBuffer var2 = new StringBuffer();
         int var3 = var1.length() % 3;
         if (var3 == 0) {
            var3 = 3;
         }

         var2.append(var1.substring(0, var3));

         for(int var4 = 0; var4 < (var1.length() - var3) / 3; ++var4) {
            var2.append("@#@#").append(var1.substring(var3 + var4 * 3, var3 + (var4 + 1) * 3));
         }

         return var2.toString();
      }
   }

   public String getTolerance() {
      return "0";
   }

   private boolean calculateVisibility(String var1, String var2) {
      if (var1 == null) {
         return false;
      } else if (var2 == null) {
         return true;
      } else if ("WEB".equalsIgnoreCase(var2)) {
         return false;
      } else if (var2.contains("ANYK")) {
         return true;
      } else {
         boolean var3 = true;
         if (var2.contains("A")) {
            if ("0".equals(MainFrame.role)) {
               return true;
            }

            var3 = false;
         }

         if (var2.contains("J")) {
            if ("1".equals(MainFrame.role)) {
               return true;
            }

            var3 = false;
         }

         if (var2.contains("R")) {
            if ("2".equals(MainFrame.role)) {
               return true;
            }

            var3 = false;
         }

         if (var2.contains("U")) {
            if ("3".equals(MainFrame.role)) {
               return true;
            }

            var3 = false;
         }

         return var3;
      }
   }

   public DataFieldModel.KpForceType getKp_force() {
      return this.kp_force;
   }

   public void setKp_force(DataFieldModel.KpForceType var1) {
      this.kp_force = var1;
   }

   public static enum KpForceType {
      copy,
      empty,
      notPresent;
   }
}
