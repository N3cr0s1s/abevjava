package hu.piller.enykp.print;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.FontMapper;
import java.awt.Font;
import java.io.IOException;
import java.util.HashMap;

public class AbevFontMapper implements FontMapper {
   HashMap baseFonts = new HashMap();
   static final String SS_PLAIN = "LiberationSans-Regular";
   static final String SS_BOLD = "LiberationSans-Bold";
   static final String SS_ITALIC = "LiberationSans-Italic";
   static final String SS_BOLDITALIC = "LiberationSans-BoldItalic";
   static final String S_PLAIN = "LiberationSerif-Regular";
   static final String S_BOLD = "LiberationSerif-Bold";
   static final String S_ITALIC = "LiberationSerif-Italic";
   static final String S_BOLDITALIC = "LiberationSerif-BoldItalic";
   static final String M_PLAIN = "LiberationMono-Regular";
   static final String M_BOLD = "LiberationMono-Bold";
   static final String M_ITALIC = "LiberationMono-Italic";
   static final String M_BOLDITALIC = "LiberationMono-BoldItalic";
   static final String N_SS_PLAIN = "Liberation Sans";
   static final String N_SS_BOLD = "Liberation Sans Bold";
   static final String N_SS_ITALIC = "Liberation Sans Italic";
   static final String N_SS_BOLDITALIC = "Liberation Sans Bold Italic";
   static final String N_S_PLAIN = "Liberation Serif";
   static final String N_S_BOLD = "Liberation Serif Bold";
   static final String N_S_ITALIC = "Liberation Serif Italic";
   static final String N_S_BOLDITALIC = "Liberation Serif Bold Italic";
   static final String N_M_PLAIN = "Liberation Mono";
   static final String N_M_BOLD = "Liberation Mono Bold";
   static final String N_M_ITALIC = "Liberation Mono Italic";
   static final String N_M_BOLDITALIC = "Liberation Mono Bold Italic";
   static final String N_ARIAL = "Arial";
   static final String N_SANSERIF = "sanserif";
   static final String N_SANSERIF_2 = "SansSerif";
   static final String N_SERIF = "serif";
   static final String FONT_EXT = ".ttf";
   static final String FONT_DIR = "fonts/";
   static final String[] FONTS = new String[]{"LiberationSans-Regular", "LiberationSans-Bold", "LiberationSans-Italic", "LiberationSans-BoldItalic", "LiberationSerif-Regular", "LiberationSerif-Bold", "LiberationSerif-Italic", "LiberationSerif-BoldItalic", "LiberationMono-Regular", "LiberationMono-Bold", "LiberationMono-Italic", "LiberationMono-BoldItalic", "LiberationSans-Bold", "LiberationSans-Regular", "LiberationSans-Regular", "LiberationSerif-Regular"};
   static final String[] FONTNAMES = new String[]{"Liberation Sans", "Liberation Sans Bold", "Liberation Sans Italic", "Liberation Sans Bold Italic", "Liberation Serif", "Liberation Serif Bold", "Liberation Serif Italic", "Liberation Serif Bold Italic", "Liberation Mono", "Liberation Mono Bold", "Liberation Mono Italic", "Liberation Mono Bold Italic", "Arial", "sanserif", "SansSerif", "serif"};
   private String path;
   private static AbevFontMapper ourInstance = new AbevFontMapper();

   public static AbevFontMapper getInstance() {
      return ourInstance;
   }

   private AbevFontMapper() {
      try {
         this.path = this.getClass().getProtectionDomain().getCodeSource().getLocation().toString();

         for(int var1 = 0; var1 < FONTNAMES.length; ++var1) {
            this.baseFonts.put(FONTNAMES[var1].toLowerCase(), FONTS[var1]);
         }

         this.baseFonts.put("LiberationSans".toLowerCase(), "LiberationSans-Regular");
         this.baseFonts.put("LiberationSerif".toLowerCase(), "LiberationSerif-Regular");
         this.baseFonts.put("LiberationMono".toLowerCase(), "LiberationMono-Regular");
      } catch (Exception var2) {
         var2.printStackTrace();
      }

   }

   public BaseFont awtToPdf(Font var1) {
      if (this.path.toLowerCase().indexOf(".jar") > -1) {
         try {
            if (System.getProperty("os.name").toLowerCase().startsWith("mac")) {
               return this.baseFonts.containsKey(var1.getName().toLowerCase()) ? BaseFont.createFont("jar:" + this.path + "!/" + "fonts/" + this.baseFonts.get(var1.getName().toLowerCase()) + ".ttf", "Identity-H", true) : BaseFont.createFont("jar:" + this.path + "!/" + "fonts/" + var1.getName() + ".ttf", "Identity-H", true);
            } else {
               return BaseFont.createFont("jar:" + this.path + "!/" + "fonts/" + this.baseFonts.get(var1.getName().toLowerCase()) + ".ttf", "Identity-H", true);
            }
         } catch (DocumentException var3) {
            var3.printStackTrace();
            return null;
         } catch (IOException var4) {
            var4.printStackTrace();
            return null;
         }
      } else {
         try {
            return BaseFont.createFont(this.path + "fonts/" + this.baseFonts.get(var1.getName().toLowerCase()) + ".ttf", "Identity-H", true);
         } catch (DocumentException var5) {
            var5.printStackTrace();
            return null;
         } catch (IOException var6) {
            var6.printStackTrace();
            return null;
         }
      }
   }

   public Font pdfToAwt(BaseFont var1, int var2) {
      return null;
   }
}
