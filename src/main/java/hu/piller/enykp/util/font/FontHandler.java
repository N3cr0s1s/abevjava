package hu.piller.enykp.util.font;

import hu.piller.enykp.interfaces.IImageUtil;
import hu.piller.enykp.util.base.ErrorList;
import hu.piller.enykp.util.base.ImageUtil;
import hu.piller.enykp.util.base.Tools;
import java.awt.Font;
import java.awt.FontFormatException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Hashtable;
import java.util.Objects;
import java.util.Vector;

public class FontHandler {
   private boolean debugOn = false;
   public static final String PARAM_FILE = "fonts/fonts.xml";
   public static final String PAR_FONT_NAME = "font_name";
   public static final String PAR_FONT_STYLE = "font_style";
   public static final String PAR_FONT_SIZE = "font_size";
   private Hashtable fontparams;
   private Vector etalonFonts = null;
   private Hashtable derivedFontsTable = null;
   private static Integer INT_ZERO = new Integer(0);
   private static Integer INT_ONE = new Integer(1);
   private static FontHandler instance;
   private IImageUtil iu;

   public static FontHandler getInstance() {
      if (instance == null) {
         instance = new FontHandler();
      }

      return instance;
   }

   private FontHandler() {
      this.initialize();
   }

   private void initialize() {
      this.etalonFonts = new Vector();
      this.derivedFontsTable = new Hashtable(50);
      Object var1 = null;
      URL var2 = this.getClass().getProtectionDomain().getCodeSource().getLocation();
      this.iu = new ImageUtil(var2);
      byte[] var10 = this.iu.getImageResource("fonts/fonts.xml");
      this.loadParams(var10);
      Vector var3 = this.getVectorParams(this.fontparams, "fonts", (Vector)null);
      if (var3 != null) {
         for(int var4 = 0; var4 < var3.size(); ++var4) {
            Hashtable var5 = (Hashtable)var3.elementAt(var4);
            String var6 = this.getStringParams(var5, "name", "");
            String var7 = this.getStringParams(var5, "style", "");
            String var8 = this.getStringParams(var5, "filename", "");
            if (var6.length() != 0 && var7.length() != 0 && var8.length() != 0) {
               Font var9 = this.createFont(var8);
               if (var9 != null) {
                  this.etalonFonts.add(new Object[]{var6, this.getFontStyle(var7), new Integer(var9.getSize()), var9});
               }
            } else {
               this.writeError(new Integer(17102), "Hiba a betükészlet paraméterek beolvasásakor", (Exception)null, var6 + "/" + var7 + "/" + var8);
            }
         }

      }
   }

   private Integer getFontStyle(String var1) {
      String var2 = var1.toLowerCase().trim();
      if (var2.equals("plain")) {
         return new Integer(0);
      } else if (var2.indexOf("bold") > -1 && var2.indexOf("italic") > -1) {
         return new Integer(3);
      } else if (var2.equals("bold")) {
         return new Integer(1);
      } else {
         return var2.equals("italic") ? new Integer(2) : new Integer(0);
      }
   }

   private void loadParams(byte[] var1) {
      try {
         FontParser var2 = new FontParser();
         this.fontparams = (Hashtable)var2.parse(var1);
      } catch (Exception var3) {
         this.writeError(new Integer(17100), "A betűkészlet listát nem sikerült betölteni", var3, "fonts/fonts.xml");
      }

   }

   private Font createFont(String var1) {
      ByteArrayInputStream var2 = null;

      try {
         byte[] var3 = this.iu.getImageResource(var1);
         if (var3 == null) {
            throw new IOException("Nincs ilyen állomány");
         } else {
            var2 = new ByteArrayInputStream(var3);
            Font var4 = Font.createFont(0, var2);
            var2.close();
            return var4;
         }
      } catch (FontFormatException var7) {
         this.writeError(new Integer(17100), "A betűkészlet állomány formátuma nem megfelelő", var7, var1);

         try {
            if (var2 != null) {
               var2.close();
            }
         } catch (IOException var5) {
            Tools.eLog(var7, 0);
         }

         return null;
      } catch (Exception var8) {
         this.writeError(new Integer(17101), "Hiba a betűkészlet állomány beolvasásakor", var8, var1);

         try {
            if (var2 != null) {
               var2.close();
            }
         } catch (IOException var6) {
            Tools.eLog(var8, 0);
         }

         return null;
      }
   }

   public Object getFont(Hashtable var1) {
      String var2 = this.getStringParams(var1, "font_name", (String)null);
      Integer var3 = this.getIntParams(var1, "font_style", INT_ZERO);
      Integer var4 = this.getIntParams(var1, "font_size", INT_ONE);
      Font var5 = null;
      if (this.debugOn) {
         System.out.println("--------------------------------------");
      }

      if (this.debugOn) {
         System.out.println("FontHandler kérés");
      }

      if (this.debugOn) {
         System.out.println("font = " + var2 + "," + var3 + "," + var4);
      }

      if (var2 == null) {
         this.writeError(new Integer(17100), "Hiba a fontname üres", (Exception)null, var1);
         return null;
      } else {
         Hashtable var6 = (Hashtable)this.derivedFontsTable.get(var2);
         if (var6 != null) {
            Hashtable var7 = (Hashtable)var6.get(var3);
            if (var7 != null) {
               var5 = (Font)var7.get(var4);
               if (var5 != null) {
                  return var5;
               }
            }
         }

         Font var8 = this.getFontFamily(var2, var3);
         if (var8 == null) {
            this.writeError(new Integer(17100), "Hiba a fontFamily üres", null, var1);
            return null;
         } else {
            var5 = var8.deriveFont(0, (float)var4);
            this.cacheFonts(var2, new Integer(var3), new Integer(var5.getSize()), var5);
            if (this.debugOn) {
               System.out.println("FontHandler.createfont");
            }

            if (this.debugOn) {
               System.out.println("font = " + var5.getName() + "," + var5.getStyle() + "," + var5.getSize());
            }

            return var5;
         }
      }
   }

   private void cacheFonts(String var1, Integer var2, Integer var3, Font var4) {
      Hashtable var5 = (Hashtable)this.derivedFontsTable.get(var1);
      if (var5 == null) {
         var5 = new Hashtable();
         this.derivedFontsTable.put(var1, var5);
      }

      Hashtable var6 = (Hashtable)var5.get(var2);
      if (var6 == null) {
         var6 = new Hashtable();
         var5.put(var2, var6);
      }

      var6.put(var3, var4);
   }

   private Font getFontFamily(String fontFamily, Integer var2) {
      if (fontFamily != null && var2 != null) {
         return (Font) this.etalonFonts.stream()
                 .filter(x -> {
                    Object[] arr = (Object[])x;
                    String name = arr[0].toString().toLowerCase();
                    return Objects.equals(name, fontFamily.toLowerCase());
                 })
                 .filter(x -> {
                    Object[] arr = (Object[])x;
                    return ((int)arr[1]) == var2;
                 })
                 .map(x -> {
                    Object[] arr = (Object[])x;
                    return (arr[3]);
                 })
                 .findFirst()
                 .orElseGet(() -> {
                    Object[] fonts = (Object[]) this.etalonFonts.get(0);
                    Font myFont = (Font) fonts[3];

                    System.out.printf("fontFamily: %s, var2: %s not found, using fallback %s%n", fontFamily, var2, myFont.getName());
                    return myFont;
                 });
      } else {
         this.writeError(17100, "Hiba a fontstyle üres", null, null);
         return null;
      }
   }

   private String getStringParams(Hashtable var1, String var2, String var3) {
      Object var4 = this.getParams(var1, var2, var3);
      return var4 instanceof String ? (String)var4 : var3;
   }

   private Integer getIntParams(Hashtable var1, String var2, Integer var3) {
      Object var4 = this.getParams(var1, var2, var3);
      return var4 instanceof Integer ? (Integer)var4 : var3;
   }

   private Vector<?> getVectorParams(Hashtable var1, String var2, Vector var3) {
      Object var4 = this.getParams(var1, var2, var3);
      return var4 instanceof Vector ? (Vector)var4 : var3;
   }

   private Object getParams(Hashtable var1, String var2, Object var3) {
      return var1 != null && var1.containsKey(var2) ? var1.get(var2) : var3;
   }

   public void writeError(Object var1, String var2, Exception var3, Object var4) {
      ErrorList.getInstance().writeError(var1, var2, var3, var4);
      System.out.println(var1 + "," + var2 + "," + var3.getMessage() + "," + var4.toString());
   }
}
