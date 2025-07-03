package hu.piller.enykp.gui.model;

import hu.piller.enykp.alogic.orghandler.OrgInfo;
import hu.piller.enykp.alogic.settingspanel.SettingsStore;
import hu.piller.enykp.gui.ExtendedFont;
import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.gui.framework.MainFrame;
import hu.piller.enykp.interfaces.IPropertyList;
import hu.piller.enykp.util.font.FontHandler;
import hu.piller.enykp.util.oshandler.OsFactory;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.TexturePaint;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;
import org.xml.sax.Attributes;

public class VisualFieldModel {
   public int type;
   int x;
   int y;
   int w;
   int h;
   int rx;
   int ry;
   int linewidth;
   public String text;
   String[] textt;
   Color fg;
   Color bg;
   String fontname;
   public int fontstyle;
   int fontsize;
   int zfhe;
   Font font;
   boolean underline;
   boolean middleline;
   boolean aboveline;
   public int alignment;
   public int rotation;
   String imagename;
   ImageIcon image;
   private BufferedImage bi;
   boolean normalimage;
   private Rectangle origbounds;
   private Font origfont;
   private boolean firstTime = true;
   private boolean bwprinting = false;
   public boolean printable;
   public boolean invisible;
   public String patternId;
   public String id;
   public static final int TEXT = 0;
   public static final int FRAME = 1;
   public static final int IMAGE = 2;
   public int role;
   private double zoom = 1.0D;
   private ArrayList<ExtendedFont> fontStack = new ArrayList();

   public VisualFieldModel(Attributes var1, FormModel var2) {
      this.id = var1.getValue("id");
      if (var1.getValue("type").equals("frame")) {
         this.type = 1;
      } else if (var1.getValue("type").equals("label")) {
         this.type = 0;
      } else {
         this.type = 2;
      }

      this.x = this.get_int(var1.getValue("x"), 0);
      this.y = this.get_int(var1.getValue("y"), 0);
      this.w = this.get_int(var1.getValue("w"), 0);
      this.h = this.get_int(var1.getValue("h"), 0);
      this.rx = this.get_int(var1.getValue("rx"), 0);
      this.ry = this.get_int(var1.getValue("ry"), 0);
      this.text = var1.getValue("value");
      if (this.text == null) {
         this.text = "";
      }

      this.textt = this.text.split("#");
      this.fg = this.get_color(var1.getValue("fgcolor"), Color.BLACK);
      this.bg = this.get_color(var1.getValue("bgcolor"), Color.BLACK);
      this.linewidth = this.get_int(var1.getValue("linewidth"), 1);
      if (1 < this.linewidth) {
         int var3 = this.linewidth;
         this.x += var3 / 2;
         this.y += var3 / 2;
         this.w -= var3;
         this.h -= var3;
      }

      this.fontname = var1.getValue("fontname");
      if (this.fontname == null) {
         this.fontname = "Arial";
      }

      String var23 = var1.getValue("fonttype");
      if (var23 == null) {
         var23 = "plain";
      }

      this.fontstyle = 0;
      if (var23.equals("bold")) {
         this.fontstyle = 1;
      } else if (var23.equals("italic")) {
         this.fontstyle = 2;
      } else if (var23.equals("bolditalic")) {
         this.fontstyle = 3;
      } else if (var23.equals("italicbold")) {
         this.fontstyle = 3;
      }

      this.fontsize = this.get_int(var1.getValue("fontsize"), 8);
      this.zfhe = this.fontsize;
      this.getDifferentFont();
      String var4 = var1.getValue("alignment");
      if (var4 == null) {
         var4 = "left";
      }

      this.alignment = 2;
      if (var4.equals("right")) {
         this.alignment = 4;
      }

      if (var4.equals("center")) {
         this.alignment = 0;
      }

      this.rotation = this.get_int(var1.getValue("rotation"), 0);
      this.origbounds = new Rectangle(this.x, this.y, this.w, this.h);
      this.origfont = this.font;
      this.normalimage = true;
      String var5 = var1.getValue("underline");
      if (var5 == null) {
         this.underline = false;
      } else {
         this.underline = true;
      }

      String var6 = var1.getValue("middleline");
      if (var6 == null) {
         this.middleline = false;
      } else {
         this.middleline = true;
      }

      String var7 = var1.getValue("aboveline");
      if (var7 == null) {
         this.aboveline = false;
      } else {
         this.aboveline = true;
      }

      String var8 = var1.getValue("printable");
      this.printable = var8 == null ? true : var8.equals("True");
      String var9 = var1.getValue("invisible");
      this.invisible = this.calculateVisibility(var9, var1.getValue("invisiblePlace"));
      this.patternId = var1.getValue("patternId");
      this.role = this.get_int(var1.getValue("role"), 15);
      String var10 = var1.getValue("picturetype");
      if (var10 != null && var10.equals("colorchange")) {
         this.normalimage = false;
      }

      this.imagename = var1.getValue("name");
      if (this.imagename != null) {
         OrgInfo var11 = OrgInfo.getInstance();
         Hashtable var12 = (Hashtable)var2.images.get(this.imagename);
         String var13 = (String)var12.get("source");
         if (var13.equals("Resource")) {
            String var14 = (String)var12.get("datatype");
            String var15 = (String)var12.get("location");
            String[] var16 = var15.split(";");
            IPropertyList var17 = var11.getResource(var16[0], var16[1]);
            if (var17 != null) {
               Object[] var18 = new Object[]{"getimage", var14, this.imagename};
               Object var19 = var17.get(var18);
               if (var19 != null && var19 instanceof Object[]) {
                  byte[] var20 = (byte[])((byte[])((Object[])((Object[])var19))[0]);
                  ImageIcon var21 = new ImageIcon(var20);
                  int var22 = var21.getImageLoadStatus();
                  if (var22 == 8) {
                     this.image = var21;
                  }
               }
            }
         }
      }

   }

   private void getFont() {
      Hashtable var1 = new Hashtable();
      var1.put("font_name", this.fontname);
      var1.put("font_style", new Integer(this.fontstyle));
      var1.put("font_size", new Integer(this.zfhe));
      this.font = (Font)FontHandler.getInstance().getFont(var1);
   }

   private void getDifferentFont() {
      this.getFont();
   }

   private Color get_color(Object var1, Color var2) {
      try {
         String var3 = var1.toString();
         return new Color(Integer.parseInt(var3, 16));
      } catch (Exception var4) {
         return var2;
      }
   }

   private int get_int(Object var1, int var2) {
      try {
         return Integer.parseInt(var1.toString());
      } catch (Exception var4) {
         return var2;
      }
   }

   public static int getmask() {
      if (MainFrame.role.equals("0")) {
         return 1;
      } else if (MainFrame.role.equals("1")) {
         return 2;
      } else if (MainFrame.role.equals("2")) {
         return 4;
      } else {
         return MainFrame.role.equals("3") ? 8 : 1;
      }
   }

   public void paint(Graphics var1) {
      if ((this.role & getmask()) != 0) {
         if (!this.invisible) {
            Font var2 = var1.getFont();
            Graphics2D var3 = (Graphics2D)var1;
            var3.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            switch(this.type) {
            case 0:
               Font var4 = this.getMaxFont(this.font, this.text);
               var1.setFont(var4);
               var1.setColor(this.bwprinting ? Color.BLACK : this.fg);
               int var5 = var1.getFontMetrics().getMaxAscent();
               int var6 = var1.getFontMetrics().getHeight();
               var6 -= (var6 - var1.getFontMetrics().getMaxAscent()) / 2;
               int var7 = var5 - 20 * var5 / 100;
               int var8 = 0;
               var1.setFont(this.font);
               int[] var9 = new int[this.textt.length];

               int var18;
               for(var18 = 0; var18 < var9.length; ++var18) {
                  var9[var18] = this.getFormattedStringWidth(var1, this.font, this.textt[var18]);
               }

               for(var18 = 0; var18 < this.textt.length; ++var18) {
                  if (this.rotation == 0) {
                     switch(this.alignment) {
                     case 0:
                        var8 = (this.w - var9[var18]) / 2;
                     case 1:
                     case 3:
                     default:
                        break;
                     case 2:
                        var8 = 0;
                        break;
                     case 4:
                        var8 = this.w - var9[var18];
                     }

                     try {
                        this.drawFormattedString(var1, this.font, this.textt[var18], this.x + var8, this.y + var7 + var18 * var6);
                     } catch (Exception var17) {
                        var17.printStackTrace();
                     }
                  } else {
                     if (OsFactory.getOsHandler().getOsName().equals("mac")) {
                        return;
                     }

                     AffineTransform var19 = this.font.getTransform();
                     AffineTransform var20 = new AffineTransform();
                     var20.rotate(Math.toRadians(this.rotation == 1 ? 90.0D : 270.0D));
                     var19.concatenate(var20);
                     Font var13 = this.font.deriveFont(var19);
                     var1.setFont(var13);
                     var8 = var6;
                     int var14 = 0;
                     switch(this.alignment) {
                     case 0:
                        var14 = var9[var18] + (this.h - var9[var18]) / 2;
                     case 1:
                     case 3:
                     default:
                        break;
                     case 2:
                        var14 = this.h;
                        break;
                     case 4:
                        var14 = var9[var18];
                     }

                     try {
                        this.drawFormattedString(var1, this.font, this.textt[var18], this.x + var8 + var18 * var6, this.y + var14);
                     } catch (Exception var16) {
                        var16.printStackTrace();
                     }
                  }

                  if (this.underline) {
                     var1.drawLine(this.x + var8, this.y + var7 + var18 * var6, this.x + var8 + var9[var18], this.y + var7 + var18 * var6);
                  }

                  if (this.aboveline) {
                     var1.drawLine(this.x + var8, this.y + var7 + var18 * var6 - var6, this.x + var8 + var9[var18], this.y + var7 + var18 * var6 - var6);
                  }

                  if (this.middleline) {
                     var1.drawLine(this.x + var8, this.y + var7 + var18 * var6 - var6 / 2, this.x + var8 + var9[var18], this.y + var7 + var18 * var6 - var6 / 2);
                  }
               }

               var1.setFont(var2);
               break;
            case 1:
               if (!this.bwprinting) {
                  var1.setColor(this.bg);
                  if (this.patternId == null) {
                     var1.fillRoundRect(this.x, this.y, this.w, this.h, this.rx, this.ry);
                  } else {
                     this.paintPatternFilledFrame((Graphics2D)var1, new Rectangle(this.x, this.y, this.w, this.h));
                  }
               }

               var1.setColor(this.bwprinting ? (this.fg.equals(Color.WHITE) ? this.fg : Color.BLACK) : this.fg);
               if (this.linewidth != 0) {
                  ((Graphics2D)var1).setStroke(new BasicStroke((float)this.linewidth));
                  if (this.rx == 0 && this.ry == 0) {
                     if (this.patternId == null) {
                        var1.drawRect(this.x, this.y, this.w - 1, this.h - 1);
                     } else {
                        this.paintPatternFilledFrame((Graphics2D)var1, new Rectangle(this.x, this.y, this.w - 1, this.h - 1));
                     }
                  } else {
                     var1.drawRoundRect(this.x, this.y, this.w - 1, this.h - 1, this.rx, this.ry);
                  }

                  ((Graphics2D)var1).setStroke(new BasicStroke(1.0F));
               }
               break;
            case 2:
               if (this.image != null) {
                  if (this.normalimage) {
                     var1.drawImage(this.image.getImage(), this.x, this.y, this.w, this.h, (ImageObserver)null);
                  } else {
                     if (this.firstTime) {
                        this.bi = new BufferedImage(this.image.getIconWidth(), this.image.getIconHeight(), 2);
                        Graphics2D var10 = this.bi.createGraphics();
                        var10.drawImage(this.image.getImage(), 0, 0, (ImageObserver)null);
                        int var11 = 0;

                        while(true) {
                           if (var11 >= this.bi.getHeight()) {
                              this.firstTime = false;
                              break;
                           }

                           for(int var12 = 0; var12 < this.bi.getWidth(); ++var12) {
                              this.bi.setRGB(var12, var11, this.bi.getRGB(var12, var11) == -16777216 ? this.fg.getRGB() : this.bg.getRGB());
                           }

                           ++var11;
                        }
                     }

                     var1.drawImage(this.bi, this.x, this.y, this.w, this.h, this.bg, (ImageObserver)null);
                  }
               }
            }

         }
      }
   }

   public void print(Graphics var1) {
      if (this.printable) {
         if (!this.invisible) {
            if ((this.role & getmask()) != 0) {
               Font var2 = var1.getFont();
               Graphics2D var3 = (Graphics2D)var1;
               var3.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
               Font var4 = this.origfont;
               int var5 = this.origbounds.x;
               int var6 = this.origbounds.y;
               int var7 = this.origbounds.width;
               int var8 = this.origbounds.height;
               int var22;
               switch(this.type) {
               case 0:
                  Font var9 = this.getMaxFont(var4, this.text);
                  var1.setFont(var9);
                  var1.setColor(this.bwprinting ? Color.BLACK : this.fg);
                  int var10 = var1.getFontMetrics().getMaxAscent();
                  int var11 = var10 - 20 * var10 / 100;
                  int var12 = 0;
                  var1.setFont(var4);

                  for(int var21 = 0; var21 < this.textt.length; ++var21) {
                     var22 = this.getFormattedStringWidth(var1, var4, this.textt[var21]);
                     if (this.rotation == 0) {
                        switch(this.alignment) {
                        case 0:
                           var12 = (var7 - var22) / 2;
                        case 1:
                        case 3:
                        default:
                           break;
                        case 2:
                           var12 = 0;
                           break;
                        case 4:
                           var12 = var7 - var22;
                        }

                        try {
                           this.drawFormattedString(var1, var4, this.textt[var21], var5 + var12, var6 + var11 + var21 * var10);
                        } catch (Exception var19) {
                           var19.printStackTrace();
                        }
                     } else {
                        if (OsFactory.getOsHandler().getOsName().equals("mac")) {
                           return;
                        }

                        AffineTransform var23 = var4.getTransform();
                        AffineTransform var16 = new AffineTransform();
                        var16.rotate(Math.toRadians(this.rotation == 1 ? 90.0D : 270.0D));
                        Font var17 = var4.deriveFont(var23);
                        var1.setFont(var17);
                        var12 = var10;
                        int var18 = 0;
                        switch(this.alignment) {
                        case 0:
                           var18 = var22 + (var8 - var22) / 2;
                        case 1:
                        case 3:
                        default:
                           break;
                        case 2:
                           var18 = var8;
                           break;
                        case 4:
                           var18 = var22;
                        }

                        ((Graphics2D)var1).rotate(1.0D * Math.toRadians(this.rotation == 1 ? 90.0D : 270.0D));
                        var1.drawString(this.textt[var21], -1 * (var6 + var18), var5 + var10 + var21 * var10);
                        ((Graphics2D)var1).rotate(-1.0D * Math.toRadians(this.rotation == 1 ? 90.0D : 270.0D));
                     }

                     if (this.underline) {
                        var1.drawLine(var5 + var12, var6 + var11 + var21 * var10, var5 + var12 + var22, var6 + var11 + var21 * var10);
                     }

                     if (this.aboveline) {
                        var1.drawLine(var5 + var12, var6 + var11 + var21 * var10 - var10, var5 + var12 + var22, var6 + var11 + var21 * var10 - var10);
                     }

                     if (this.middleline) {
                        var1.drawLine(var5 + var12, var6 + var11 + var21 * var10 - var10 / 2, var5 + var12 + var22, var6 + var11 + var21 * var10 - var10 / 2);
                     }
                  }

                  var1.setFont(var2);
                  break;
               case 1:
                  if (!this.bwprinting) {
                     var1.setColor(this.bg);
                     var1.fillRoundRect(var5, var6, var7, var8, this.rx, this.ry);
                  }

                  var1.setColor(this.bwprinting ? (this.fg.equals(Color.WHITE) ? this.fg : Color.BLACK) : this.fg);
                  if (this.linewidth != 0) {
                     ((Graphics2D)var1).setStroke(new BasicStroke((float)this.linewidth));
                     if (this.rx == 0 && this.ry == 0) {
                        var1.drawRect(var5, var6, var7 - 1, var8 - 1);
                     } else {
                        var1.drawRoundRect(var5, var6, var7 - 1, var8 - 1, this.rx, this.ry);
                     }

                     ((Graphics2D)var1).setStroke(new BasicStroke(1.0F));
                  }
                  break;
               case 2:
                  if (this.image != null) {
                     if (this.bwprinting) {
                        BufferedImage var13 = new BufferedImage(var7, var8, 10);
                        Graphics var14 = var13.getGraphics();
                        var14.setColor(Color.WHITE);
                        var13.getGraphics().fillRect(0, 0, var7, var8);
                        var13.getGraphics().drawImage(this.image.getImage(), 0, 0, var7, var8, (ImageObserver)null);
                        var13.getGraphics().dispose();
                        var1.drawImage(var13, var5, var6, var7, var8, (ImageObserver)null);
                     } else if (this.normalimage) {
                        var1.drawImage(this.image.getImage(), var5, var6, var7, var8, (ImageObserver)null);
                     } else {
                        if (this.firstTime) {
                           this.bi = new BufferedImage(this.image.getIconWidth(), this.image.getIconHeight(), 2);
                           Graphics2D var20 = this.bi.createGraphics();
                           var20.drawImage(this.image.getImage(), 0, 0, (ImageObserver)null);
                           var22 = 0;

                           while(true) {
                              if (var22 >= this.bi.getHeight()) {
                                 this.firstTime = false;
                                 break;
                              }

                              for(int var15 = 0; var15 < this.bi.getWidth(); ++var15) {
                                 this.bi.setRGB(var15, var22, this.bi.getRGB(var15, var22) == -16777216 ? this.fg.getRGB() : this.bg.getRGB());
                              }

                              ++var22;
                           }
                        }

                        var1.drawImage(this.bi, var5, var6, var7, var8, this.bg, (ImageObserver)null);
                     }
                  }
               }

            }
         }
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
   }

   public Font getOriginalFont() {
      return this.origfont;
   }

   public void setFontsize(double var1) {
      this.zfhe = (int)((double)this.fontsize * var1);
      this.getDifferentFont();
   }

   public void print(Graphics var1, int var2) {
      if (this.x != 0 || this.y != 0) {
         int var3;
         try {
            var3 = Integer.parseInt(SettingsStore.getInstance().get("printer", "print.settings.colors"));
         } catch (NumberFormatException var7) {
            var3 = 1;
         }

         boolean var4 = var3 == 12;
         if (var4) {
            this.bwprinting = true;
         }

         double var5 = this.zoom;
         this.zoom = 1.0D;
         this.print(var1);
         this.zoom = var5;
         this.bwprinting = false;
      }
   }

   public void drawFormattedString(Graphics var1, Font var2, String var3, int var4, int var5) {
      if (var3.indexOf("{@") == -1) {
         var1.drawString(var3, var4, var5);
      } else {
         ExtendedFont var6 = new ExtendedFont(var2.getName(), var2.getStyle(), var2.getSize(), this.fg);
         this.fontStack.add(var6);
         String var7 = "{@";
         int var8 = var3.indexOf(var7);

         while(true) {
            while(var8 > -1) {
               var1.setFont(var6);
               var1.setColor(var6.getColor());
               var1.drawString(var3.substring(0, var8), var4, var5 - var6.getYTransform());
               var4 += SwingUtilities.computeStringWidth(var1.getFontMetrics(var6), var3.substring(0, var8));
               if (var7.equals("{@")) {
                  String var10 = var3.substring(var8 + 2, var8 + 2 + 2);
                  String var11 = var3.substring(var8 + 4, var3.indexOf(";"));
                  var3 = var3.substring(var3.indexOf(";", var8 + 2) + 1);
                  this.fontStack.add(this.getFormattedFont(var6, GuiUtil.getLabelCommandCode(var10), var11));
                  var6 = (ExtendedFont)this.fontStack.get(this.fontStack.size() - 1);
               } else {
                  var3 = var3.substring(var8 + 2);
                  if (this.fontStack.size() > 1) {
                     var6 = (ExtendedFont)this.fontStack.remove(this.fontStack.size() - 1);
                     var6 = (ExtendedFont)this.fontStack.get(this.fontStack.size() - 1);
                  } else {
                     var6 = new ExtendedFont(var2.getName(), var2.getStyle(), var2.getSize(), this.fg);
                  }
               }

               var8 = var3.indexOf("{@");
               int var9 = var3.indexOf("@}");
               if (var8 > -1 && var8 < var9) {
                  var7 = "{@";
               } else {
                  var8 = var9;
                  var7 = "@}";
               }
            }

            if (this.fontStack.size() > 0) {
               var6 = (ExtendedFont)this.fontStack.remove(this.fontStack.size() - 1);
               var1.setFont(var6);
               var1.setColor(var6.getColor());
            } else {
               var1.setFont(this.font);
               var1.setColor(this.fg);
            }

            if (var3.length() > 0) {
               var1.drawString(var3, var4, var5);
            }

            return;
         }
      }
   }

   public int getFormattedStringWidth(Graphics var1, Font var2, String var3) {
      if (var3.indexOf("{@") == -1) {
         return var1.getFontMetrics().stringWidth(var3);
      } else {
         ExtendedFont var4 = new ExtendedFont(var2.getName(), var2.getStyle(), var2.getSize(), this.fg);
         this.fontStack.add(var4);
         String var5 = "{@";
         int var6 = var3.indexOf(var5);
         int var8 = 0;

         while(true) {
            while(var6 > -1) {
               var1.setFont(var4);
               var1.setColor(var4.getColor());
               var8 += var1.getFontMetrics().stringWidth(var3.substring(0, var6));
               if (var5.equals("{@")) {
                  String var9 = var3.substring(var6 + 2, var6 + 2 + 2);
                  String var10 = var3.substring(var6 + 4, var3.indexOf(";"));
                  var3 = var3.substring(var3.indexOf(";", var6 + 2) + 1);
                  this.fontStack.add(this.getFormattedFont(var4, GuiUtil.getLabelCommandCode(var9), var10));
                  var4 = (ExtendedFont)this.fontStack.get(this.fontStack.size() - 1);
               } else {
                  var3 = var3.substring(var6 + 2);
                  if (this.fontStack.size() > 1) {
                     var4 = (ExtendedFont)this.fontStack.remove(this.fontStack.size() - 1);
                     var4 = (ExtendedFont)this.fontStack.get(this.fontStack.size() - 1);
                  } else {
                     var4 = new ExtendedFont(var2.getName(), var2.getStyle(), var2.getSize(), this.fg);
                  }
               }

               var6 = var3.indexOf("{@");
               int var7 = var3.indexOf("@}");
               if (var6 > -1 && var6 < var7) {
                  var5 = "{@";
               } else {
                  var6 = var7;
                  var5 = "@}";
               }
            }

            if (this.fontStack.size() > 0) {
               var4 = (ExtendedFont)this.fontStack.remove(this.fontStack.size() - 1);
               var1.setFont(var4);
               var1.setColor(var4.getColor());
            } else {
               var1.setFont(this.font);
               var1.setColor(this.fg);
            }

            if (var3.length() > 0) {
               var8 += var1.getFontMetrics().stringWidth(var3.substring(0, var6));
            }

            return var8;
         }
      }
   }

   private ExtendedFont getFormattedFont(ExtendedFont var1, int var2, String var3) {
      switch(var2) {
      case 0:
         return new ExtendedFont(var1.getName(), 0, var1.getSize(), var1.getColor());
      case 1:
         return new ExtendedFont(var1.getName(), 1, var1.getSize(), var1.getColor());
      case 2:
         return new ExtendedFont(var1.getName(), 2, var1.getSize(), var1.getColor());
      case 3:
         return new ExtendedFont(var1.getName(), 3, var1.getSize(), var1.getColor());
      case 4:
         if (this.bwprinting) {
            return new ExtendedFont(var1.getName(), var1.getStyle(), var1.getSize(), Color.BLACK);
         }

         return new ExtendedFont(var1.getName(), var1.getStyle(), var1.getSize(), new Color(Integer.parseInt(var3, 16)));
      case 5:
         return new ExtendedFont(var1.getName(), var1.getStyle(), (int)((double)new Integer(var3) * this.zoom), var1.getColor());
      case 6:
         return new ExtendedFont(var3, var1.getStyle(), var1.getSize(), var1.getColor());
      case 7:
         return new ExtendedFont(var1.getName(), var1.getStyle(), (int)((double)(var1.getSize() - 2) * this.zoom), var1.getColor(), -3);
      case 8:
         return new ExtendedFont(var1.getName(), var1.getStyle(), (int)((double)(var1.getSize() - 2) * this.zoom), var1.getColor(), 4);
      default:
         return new ExtendedFont(var1.getName(), var1.getStyle(), var1.getSize(), var1.getColor());
      }
   }

   public void setZoom(double var1) {
      this.zoom = var1;
   }

   private Font getMaxFont(Font var1, String var2) {
      if (var2.indexOf("{@") == -1) {
         return var1;
      } else {
         int var3 = var2.indexOf("{@fs");
         if (var3 == -1) {
            return var1;
         } else {
            int var4 = var1.getSize();
            boolean var5 = false;

            while(var3 >= 0) {
               String var6 = var2.substring(var3 + 4, var2.indexOf(";", var3));
               int var8 = Integer.parseInt(var6);
               var2 = var2.substring(var3 + 4);
               var3 = var2.indexOf("{@fs");
               if (var8 > var4) {
                  var4 = var8;
               }
            }

            Font var7 = new Font(var1.getName(), var1.getStyle(), var4);
            return var7;
         }
      }
   }

   private void _paintStripedBackground(Graphics var1, Rectangle var2) {
      var1.setColor(Color.GRAY);

      for(int var3 = 1; (double)var3 < var2.getWidth(); var3 += 5) {
         for(int var4 = 1; (double)var4 < var2.getHeight(); var4 += 5) {
            var1.drawOval(var3, var4 + 2 * (var3 % 2), 1, 1);
         }
      }

   }

   private void paintPatternFilledFrame(Graphics2D var1, Rectangle var2) {
      BufferedImage var3 = null;

      try {
         var3 = ImageIO.read(new File("e:\\enykp\\eroforrasok\\pictures\\pattern_02.gif"));
      } catch (IOException var5) {
         var5.printStackTrace();
      }

      TexturePaint var4 = new TexturePaint(var3, new Rectangle(16, 16));
      var1.setPaint(var4);
      var1.fill(var2);
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
}
