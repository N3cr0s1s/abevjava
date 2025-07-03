package hu.piller.enykp.gui.component;

import hu.piller.enykp.util.base.Tools;
import java.io.Serializable;
import java.text.ParseException;
import java.util.Vector;
import javax.swing.JFormattedTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultFormatter;
import javax.swing.text.DocumentFilter;
import javax.swing.text.JTextComponent;
import javax.swing.text.NavigationFilter;
import javax.swing.text.DocumentFilter.FilterBypass;
import javax.swing.text.Position.Bias;

public class ENYKNumberFormatter extends DefaultFormatter {
   public static final char MASK_DIGIT = '#';
   public static final char MASK_ESCAPE_CHAR = '\\';
   private String mask;
   private char placeholder;
   private boolean is_value_contains_literal;
   private ENYKNumberFormatter.MaskChar[] mask_chars;
   private int max_length;
   private String irids;
   private ENYKNumberFormatter.ENYKNavigationFilter navigation_filter;
   private ENYKNumberFormatter.ENYKDocumentFilter document_filter;
   private int number_start;

   public ENYKNumberFormatter() {
      this.max_length = -1;
      this.irids = null;
      this.number_start = -1;
      this.navigation_filter = new ENYKNumberFormatter.ENYKNavigationFilter();
      this.document_filter = new ENYKNumberFormatter.ENYKDocumentFilter();
      this.setMask("");
      this.setPlaceholderCharacter(' ');
      this.setValueContainsLiteralCharacters(false);
      this.setAllowsInvalid(false);
      this.setOverwriteMode(false);
   }

   public ENYKNumberFormatter(String var1) {
      this();
      this.setMask(var1);
   }

   public void setValueContainsLiteralCharacters(boolean var1) {
      this.is_value_contains_literal = var1;
   }

   public boolean isValueContainsLiteral() {
      return this.is_value_contains_literal;
   }

   public void setPlaceholderCharacter(char var1) {
      this.placeholder = var1;
      this.createMaskChars();
   }

   public char getPlaceholder() {
      return this.placeholder;
   }

   public void setMask(String var1) {
      this.mask = var1 == null ? "" : var1;
      this.createMaskChars();
   }

   public String getMask() {
      return this.mask;
   }

   public void setIrids(String var1) {
      if (var1 != null) {
         if (!var1.startsWith("[^")) {
            boolean var2 = var1.endsWith("]") || var1.endsWith("]*");
            if (var2) {
               var1 = var1.substring(0, var1.lastIndexOf(93)) + "[ ]]";
            } else {
               var1 = "[" + var1 + "[ ]]";
            }

            var1 = var1 + "*";
         } else if (!var1.endsWith("*")) {
            var1 = var1 + "*";
         }
      }

      this.irids = var1;
   }

   public void setMaxLength(int var1) {
      this.max_length = var1;
      this.createMaskChars();
   }

   public int getMaxLength() {
      return this.max_length;
   }

   public void install(JFormattedTextField var1) {
      super.install(var1);
      if (var1 != null) {
         Object var2 = var1.getValue();

         try {
            this.stringToValue(this.valueToString(var2));
         } catch (ParseException var4) {
            this.setEditValid(false);
         }
      }

   }

   public Object stringToValue(String var1) throws ParseException {
      return this.stringToValue_(var1, this.is_value_contains_literal, true);
   }

   public String valueToString(Object var1) throws ParseException {
      return this.valueToString_(var1, this.is_value_contains_literal, false);
   }

   protected DocumentFilter getDocumentFilter() {
      return this.document_filter;
   }

   protected NavigationFilter getNavigationFilter() {
      return this.navigation_filter;
   }

   private void createMaskChars() {
      if (this.mask != null) {
         int var1 = this.mask.length();
         int var2 = this.max_length < 0 ? Integer.MAX_VALUE : this.max_length;
         Vector var5 = new Vector(var1);

         int var6;
         for(var6 = 0; var6 < var1 && var6 < var2; ++var6) {
            char var3 = this.mask.charAt(var6);
            boolean var4;
            if (var3 == '\\') {
               ++var6;
               if (var6 >= var1) {
                  continue;
               }

               var3 = this.mask.charAt(var6);
               var4 = true;
            } else {
               var4 = false;
            }

            var5.add(this.getMaskChar(var3, var4));
         }

         this.mask_chars = new ENYKNumberFormatter.MaskChar[var5.size()];
         var6 = 0;

         for(int var7 = var5.size(); var6 < var7; ++var6) {
            this.mask_chars[var6] = (ENYKNumberFormatter.MaskChar)var5.get(var6);
         }
      } else {
         this.mask_chars = new ENYKNumberFormatter.MaskChar[0];
      }

   }

   private ENYKNumberFormatter.MaskChar getMaskChar(char var1, boolean var2) {
      switch(var1) {
      case '#':
         return new ENYKNumberFormatter.MaskCharDigit(var1);
      default:
         return new ENYKNumberFormatter.MaskCharLiteral(var1);
      }
   }

   private String valueToString_(Object var1, boolean var2, boolean var3) {
      String var4 = var1 == null ? "" : var1.toString();
      String var5 = "";
      var4 = var4.replaceAll(" ", "");
      int var8 = this.mask_chars.length - 1;

      for(int var9 = var4.length() - 1; var8 >= 0; --var8) {
         ENYKNumberFormatter.MaskChar var6 = this.mask_chars[var8];
         if (var9 < 0) {
            if (var6 instanceof ENYKNumberFormatter.MaskCharLiteral) {
               var5 = var6.toString() + var5;
            } else {
               var5 = this.placeholder + var5;
            }
         } else {
            char var7 = var4.charAt(var9);
            if (var6 instanceof ENYKNumberFormatter.MaskCharLiteral) {
               var5 = var6.toString() + var5;
               if (var2 && var6.isValid(var7)) {
                  --var9;
               }
            } else if (!var6.isValid(var7) && var7 != this.placeholder) {
               var5 = this.placeholder + var5;
            } else {
               var5 = var7 + var5;
               --var9;
            }

            if (var9 < 0) {
               this.number_start = var8;
            }
         }
      }

      if (var3) {
         var5 = var5.trim();
      }

      return var5;
   }

   private Object stringToValue_(String var1, boolean var2, boolean var3) throws ParseException {
      String var4 = "";
      String var5 = this.resolveEscapes(var1);
      int var9 = this.mask_chars.length - 1;

      for(int var10 = var9 - var5.length(); var9 > var10; --var9) {
         ENYKNumberFormatter.MaskChar var7 = this.mask_chars[var9];
         char var6 = var5.charAt(var9);
         if (var7.isValid(var6)) {
            if (var2 || !(var7 instanceof ENYKNumberFormatter.MaskCharLiteral)) {
               var4 = var6 + var4;
            }
         } else {
            var4 = this.placeholder + var4;
         }
      }

      if (var3) {
         var4 = var4.trim();
      }

      Object var8 = super.stringToValue(var4);
      return var8;
   }

   private String resolveEscapes(String var1) {
      String var2 = "";
      int var4 = this.mask_chars.length - 1;
      int var5 = var4 - var1.length();

      for(int var6 = var1.length() - 1; var4 > var5 && var4 >= 0; --var6) {
         char var3 = var1.charAt(var6);
         if (this.mask_chars[var4] instanceof ENYKNumberFormatter.MaskCharLiteral && var4 - 1 >= 0 && var1.charAt(var6 - 1) == '\\') {
            --var6;
            ++var5;
         }

         var2 = var3 + var2;
         --var4;
      }

      return var2;
   }

   private class ENYKDocumentFilter extends DocumentFilter implements Serializable {
      private ENYKDocumentFilter() {
      }

      private int offsetToValueOffset(int var1) {
         int var2 = ENYKNumberFormatter.this.mask_chars.length;
         int var3 = -1;

         for(int var4 = 0; var4 <= var2 && var4 <= var1; ++var4) {
            if (var4 < var2) {
               if (!(ENYKNumberFormatter.this.mask_chars[var4] instanceof ENYKNumberFormatter.MaskCharLiteral)) {
                  ++var3;
               }
            } else {
               ++var3;
            }
         }

         if (var3 < 0) {
            var3 = 0;
         }

         return var3;
      }

      private int valueOffsetToOffset(int var1) {
         int var2 = ENYKNumberFormatter.this.mask_chars.length;
         int var3 = -1;

         for(int var4 = 0; var4 <= var1 && var4 <= var2; ++var4) {
            ++var3;
            if (var3 < var2 && ENYKNumberFormatter.this.mask_chars[var3] instanceof ENYKNumberFormatter.MaskCharLiteral) {
               --var4;
            }
         }

         if (var3 < 0) {
            var3 = 0;
         }

         return var3;
      }

      private String removeLiterals(int var1, String var2) {
         int var5 = ENYKNumberFormatter.this.mask_chars.length;
         String var3 = "";
         int var6 = var1;

         for(int var7 = var1 - var2.length() + 1; var6 >= 0 && var6 >= var7 && var6 < var5; --var6) {
            char var4 = var2.charAt(var6 - var7);
            if (!(ENYKNumberFormatter.this.mask_chars[var6] instanceof ENYKNumberFormatter.MaskCharLiteral) || !ENYKNumberFormatter.this.mask_chars[var6].isValid(var4)) {
               var3 = var3 + var4;
            }
         }

         return var3;
      }

      private int decreasebyLiterals(int var1, int var2) {
         int var4 = ENYKNumberFormatter.this.mask_chars.length;
         int var3 = 0;
         int var5 = var1;

         for(int var6 = var1 + var2; var5 >= 0 && var5 < var6 && var5 < var4; ++var5) {
            if (!(ENYKNumberFormatter.this.mask_chars[var5] instanceof ENYKNumberFormatter.MaskCharLiteral)) {
               ++var3;
            }
         }

         return var3;
      }

      private int applayBackDeleteFactor(int var1, int var2) {
         if (var1 == 0 && var2 < 0) {
            ++var1;
         }

         return var1;
      }

      public void insertString(FilterBypass var1, int var2, String var3, AttributeSet var4) throws BadLocationException {
         ENYKNumberFormatter.this.setEditValid(false);

         try {
            if (ENYKNumberFormatter.this.irids != null && !var3.matches(ENYKNumberFormatter.this.irids)) {
               return;
            }

            var2 -= var3.length();
            String var8 = this.removeLiterals(var2, var3);
            JFormattedTextField var5 = ENYKNumberFormatter.this.getFormattedTextField();
            String var6 = var5.getText();
            String var7 = ENYKNumberFormatter.this.stringToValue_(var6, false, false).toString();
            int var11 = this.offsetToValueOffset(var2);
            int var12 = var8.length();
            String var10;
            int var14;
            if (!ENYKNumberFormatter.this.getOverwriteMode()) {
               var10 = var7.substring(var12, var11 + var12) + var8 + var7.substring(var11 + var12);
               var14 = var11 + var12;
            } else {
               var10 = var7.substring(0, Math.max(var11 - var12 + 1, 0)) + var8 + var7.substring(var11 + var12);
               var14 = var11;
            }

            var14 = this.valueOffsetToOffset(var14);
            String var9 = ENYKNumberFormatter.this.valueToString_(var10, false, false);
            byte var13 = 0;
            var1.replace(var13, var9.length() - var13, var9.substring(var13), (AttributeSet)null);
            var5.getCaret().setDot(var14);
            var5.commitEdit();
         } catch (Exception var15) {
            Tools.eLog(var15, 0);
         }

         ENYKNumberFormatter.this.setEditValid(true);
      }

      public void remove(FilterBypass var1, int var2, int var3) throws BadLocationException {
         ENYKNumberFormatter.this.setEditValid(false);

         try {
            if (var2 >= ENYKNumberFormatter.this.mask_chars.length) {
               return;
            }

            JFormattedTextField var4 = ENYKNumberFormatter.this.getFormattedTextField();
            String var5 = var4.getText();
            String var6 = ENYKNumberFormatter.this.stringToValue_(var5, false, false).toString();
            int var10 = var2 - var4.getSelectionStart();
            int var11 = this.offsetToValueOffset(var2);
            int var12 = this.decreasebyLiterals(var2, var3);
            var12 = this.applayBackDeleteFactor(var12, var10);
            String var9 = "";

            for(int var15 = 0; var15 < var12; ++var15) {
               var9 = var9 + ENYKNumberFormatter.this.placeholder;
            }

            String var8;
            int var14;
            if (!ENYKNumberFormatter.this.getOverwriteMode()) {
               var8 = var6.substring(0, var11) + var9 + var6.substring(var11 + var12);
               var14 = var11 + var12;
            } else {
               var8 = var6.substring(0, var11) + var6.substring(var11 + var12) + var9;
               var14 = var11 + var12;
            }

            var14 = this.valueOffsetToOffset(var14);
            String var7 = ENYKNumberFormatter.this.valueToString_(var8, false, false);
            byte var13 = 0;
            var1.replace(var13, var7.length() - var13, var7.substring(var13), (AttributeSet)null);
            var4.getCaret().setDot(var14);
            var4.commitEdit();
         } catch (Exception var16) {
            Tools.eLog(var16, 0);
         }

         ENYKNumberFormatter.this.setEditValid(true);
      }

      public void replace(FilterBypass var1, int var2, int var3, String var4, AttributeSet var5) throws BadLocationException {
         this.remove(var1, var2, var3);
         this.insertString(var1, var2, var4, var5);
      }

      // $FF: synthetic method
      ENYKDocumentFilter(Object var2) {
         this();
      }
   }

   private class ENYKNavigationFilter extends NavigationFilter implements Serializable {
      private ENYKNavigationFilter() {
      }

      public int getNextVisualPositionFrom(JTextComponent var1, int var2, Bias var3, int var4, Bias[] var5) throws BadLocationException {
         if (var2 >= 0 && var2 <= ENYKNumberFormatter.this.mask_chars.length) {
            int var6 = ENYKNumberFormatter.this.mask_chars.length;
            if (ENYKNumberFormatter.this.number_start < 0) {
               return var6;
            }

            if (var2 < ENYKNumberFormatter.this.number_start) {
               var2 = ENYKNumberFormatter.this.number_start;
            }

            int var7;
            switch(var4) {
            case 3:
               for(var7 = var2 + 1; var7 <= var6; ++var7) {
                  if (var7 >= var6) {
                     return var7;
                  }

                  if (!(ENYKNumberFormatter.this.mask_chars[var7] instanceof ENYKNumberFormatter.MaskCharLiteral)) {
                     return var7;
                  }
               }

               return -1;
            case 7:
               for(var7 = var2 - 1; var7 >= 0 && var7 >= ENYKNumberFormatter.this.number_start; --var7) {
                  if (!(ENYKNumberFormatter.this.mask_chars[var7] instanceof ENYKNumberFormatter.MaskCharLiteral)) {
                     return var7;
                  }
               }
            }
         }

         return -1;
      }

      public void moveDot(javax.swing.text.NavigationFilter.FilterBypass var1, int var2, Bias var3) {
         if (var2 >= 0 && var2 < ENYKNumberFormatter.this.mask_chars.length) {
            int var4 = ENYKNumberFormatter.this.mask_chars.length;
            if (ENYKNumberFormatter.this.number_start < 0 || ENYKNumberFormatter.this.number_start == ENYKNumberFormatter.this.mask_chars.length) {
               var1.moveDot(var4, var3);
               return;
            }

            if (var2 < ENYKNumberFormatter.this.number_start) {
               var2 = ENYKNumberFormatter.this.number_start;
            }

            if (ENYKNumberFormatter.this.mask_chars[var2] instanceof ENYKNumberFormatter.MaskCharLiteral) {
               int var5 = var2;

               for(int var6 = ENYKNumberFormatter.this.mask_chars.length; var5 <= var6; ++var5) {
                  if (var5 < var6) {
                     if (!(ENYKNumberFormatter.this.mask_chars[var5] instanceof ENYKNumberFormatter.MaskCharLiteral)) {
                        var1.moveDot(var5, var3);
                        return;
                     }
                  } else {
                     var1.moveDot(var2, var3);
                  }
               }
            } else {
               var1.moveDot(var2, var3);
            }
         } else if (var2 == ENYKNumberFormatter.this.mask_chars.length) {
            var1.moveDot(var2, var3);
         }

      }

      public void setDot(javax.swing.text.NavigationFilter.FilterBypass var1, int var2, Bias var3) {
         if (var2 >= 0 && var2 < ENYKNumberFormatter.this.mask_chars.length) {
            int var4 = ENYKNumberFormatter.this.mask_chars.length;
            if (ENYKNumberFormatter.this.number_start < 0 || ENYKNumberFormatter.this.number_start == ENYKNumberFormatter.this.mask_chars.length) {
               var1.moveDot(var4, var3);
               return;
            }

            if (var2 < ENYKNumberFormatter.this.number_start) {
               var2 = ENYKNumberFormatter.this.number_start;
            }

            if (ENYKNumberFormatter.this.mask_chars[var2] instanceof ENYKNumberFormatter.MaskCharLiteral) {
               for(int var5 = var2; var5 <= var4; ++var5) {
                  if (var5 < var4) {
                     if (!(ENYKNumberFormatter.this.mask_chars[var5] instanceof ENYKNumberFormatter.MaskCharLiteral)) {
                        var1.setDot(var5, var3);
                        return;
                     }
                  } else {
                     var1.setDot(var2, var3);
                  }
               }
            } else {
               var1.setDot(var2, var3);
            }
         } else if (var2 == ENYKNumberFormatter.this.mask_chars.length) {
            var1.setDot(var2, var3);
         }

      }

      // $FF: synthetic method
      ENYKNavigationFilter(Object var2) {
         this();
      }
   }

   private class MaskCharPlaceholder extends ENYKNumberFormatter.MaskChar {
      MaskCharPlaceholder(char var2) {
         super(var2);
      }

      boolean isValid(char var1) {
         return this.c == var1;
      }
   }

   private class MaskCharDigit extends ENYKNumberFormatter.MaskChar {
      MaskCharDigit(char var2) {
         super(var2);
      }

      boolean isValid(char var1) {
         return Character.isDigit(var1);
      }
   }

   private class MaskCharLiteral extends ENYKNumberFormatter.MaskChar {
      MaskCharLiteral(char var2) {
         super(var2);
      }

      boolean isValid(char var1) {
         return this.c == var1;
      }
   }

   private class MaskChar {
      protected char c;

      MaskChar(char var2) {
         this.c = var2;
      }

      boolean isValid(char var1) {
         return false;
      }

      public String toString() {
         return String.valueOf(this.c);
      }
   }
}
