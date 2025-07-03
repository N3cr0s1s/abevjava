package hu.piller.enykp.gui.component;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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

public class ENYKMaskFormatter extends DefaultFormatter {
   public static final char MASK_DIGIT = '#';
   public static final char MASK_ESCAPE_CHAR = '\\';
   private String mask;
   private char placeholder;
   private boolean is_value_contains_literal;
   private ENYKMaskFormatter.MaskChar[] mask_chars;
   private int max_length;
   private String irids;
   private ENYKMaskFormatter.ENYKNavigationFilter navigation_filter;
   private ENYKMaskFormatter.ENYKDocumentFilter document_filter;

   public ENYKMaskFormatter() {
      this.max_length = -1;
      this.irids = null;
      this.navigation_filter = new ENYKMaskFormatter.ENYKNavigationFilter();
      this.document_filter = new ENYKMaskFormatter.ENYKDocumentFilter();
      this.setMask("");
      this.setPlaceholderCharacter(' ');
      this.setValueContainsLiteralCharacters(false);
      this.setAllowsInvalid(false);
   }

   public ENYKMaskFormatter(String var1) {
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
      return this.stringToValue_(var1, this.is_value_contains_literal);
   }

   public String valueToString(Object var1) throws ParseException {
      return this.valueToString_(var1, this.is_value_contains_literal);
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

         this.mask_chars = new ENYKMaskFormatter.MaskChar[var5.size()];
         var6 = 0;

         for(int var7 = var5.size(); var6 < var7; ++var6) {
            this.mask_chars[var6] = (ENYKMaskFormatter.MaskChar)var5.get(var6);
         }
      } else {
         this.mask_chars = new ENYKMaskFormatter.MaskChar[0];
      }

   }

   private ENYKMaskFormatter.MaskChar getMaskChar(char var1, boolean var2) {
      switch(var1) {
      case '#':
         return new ENYKMaskFormatter.MaskCharAny(var1);
      default:
         return new ENYKMaskFormatter.MaskCharLiteral(var1);
      }
   }

   private String valueToString_(Object var1, boolean var2) {
      String var3 = var1 == null ? "" : var1.toString();
      String var4 = "";
      int var5 = 0;
      int var8 = 0;
      int var9 = this.mask_chars.length;

      for(int var10 = var3.length(); var8 < var9; ++var8) {
         ENYKMaskFormatter.MaskChar var6 = this.mask_chars[var8];
         if (var5 < var10) {
            char var7 = var3.charAt(var5);
            if (var6 instanceof ENYKMaskFormatter.MaskCharLiteral) {
               var4 = var4 + var6.toString();
               if (var2 && var6.isValid(var7)) {
                  ++var5;
               }
            } else if (!var6.isValid(var7) && var7 != this.placeholder) {
               var4 = var4 + this.placeholder;
            } else {
               var4 = var4 + var7;
               ++var5;
            }
         } else if (var6 instanceof ENYKMaskFormatter.MaskCharLiteral) {
            var4 = var4 + var6.toString();
         } else {
            var4 = var4 + this.placeholder;
         }
      }

      return var4;
   }

   private Object stringToValue_(String var1, boolean var2) throws ParseException {
      String var3 = "";
      String var4 = this.resolveEscapes(var1);
      int var7 = 0;
      int var8 = this.mask_chars.length;

      for(int var9 = var4.length(); var7 < var8; ++var7) {
         ENYKMaskFormatter.MaskChar var6 = this.mask_chars[var7];
         if (var7 < var9) {
            char var5 = var4.charAt(var7);
            if (var6.isValid(var5)) {
               if (var2 || !(var6 instanceof ENYKMaskFormatter.MaskCharLiteral)) {
                  var3 = var3 + var5;
               }
            } else {
               var3 = var3 + this.placeholder;
            }
         } else if (var2 && var6 instanceof ENYKMaskFormatter.MaskCharLiteral) {
            var3 = var3 + this.mask_chars[var7].toString();
         } else {
            var3 = var3 + this.placeholder;
         }
      }

      return super.stringToValue(var3);
   }

   private String resolveEscapes(String var1) {
      String var2 = "";
      int var4 = this.mask_chars.length - 1;
      int var5 = var4 - var1.length();

      for(int var6 = var1.length() - 1; var4 > var5 && var4 >= 0; --var6) {
         char var3 = var1.charAt(var6);
         if (this.mask_chars[var4] instanceof ENYKMaskFormatter.MaskCharLiteral && var6 - 1 >= 0 && var1.charAt(var6 - 1) == '\\') {
            --var6;
            ++var5;
         }

         var2 = var3 + var2;
         --var4;
      }

      return var2;
   }

   public boolean isValidDate(String var1) {
      var1 = var1.replaceAll("\\.", "");
      var1 = var1.replaceAll("-", "");

      try {
         SimpleDateFormat var2;
         if (this.mask.length() == 5) {
            var2 = new SimpleDateFormat("yyyy");
         } else if (this.mask.length() == 8) {
            var2 = new SimpleDateFormat("yyyyMM");
         } else {
            var2 = new SimpleDateFormat("yyyyMMdd");
            if (var1.trim().length() < 8) {
               throw new ParseException("", 0);
            }
         }

         Date var3 = var2.parse(var1);
         if (!var2.format(var3).equals(var1)) {
            throw new ParseException("", 1);
         } else {
            return true;
         }
      } catch (ParseException var4) {
         return false;
      }
   }

   private void removeNumbersFromMask() {
      this.mask = this.mask.replaceAll("\\d", "#");
      this.createMaskChars();
   }

   private class ENYKDocumentFilter extends DocumentFilter implements Serializable {
      private ENYKDocumentFilter() {
      }

      private int offsetToValueOffset(int var1) {
         int var2 = ENYKMaskFormatter.this.mask_chars.length;
         int var3 = -1;

         for(int var4 = 0; var4 <= var2 && var4 <= var1; ++var4) {
            if (var4 < var2) {
               if (!(ENYKMaskFormatter.this.mask_chars[var4] instanceof ENYKMaskFormatter.MaskCharLiteral)) {
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
         int var2 = ENYKMaskFormatter.this.mask_chars.length;
         int var3 = -1;

         for(int var4 = 0; var4 <= var1 && var4 <= var2; ++var4) {
            ++var3;
            if (var3 < var2 && ENYKMaskFormatter.this.mask_chars[var3] instanceof ENYKMaskFormatter.MaskCharLiteral) {
               --var4;
            }
         }

         if (var3 < 0) {
            var3 = 0;
         }

         return var3;
      }

      private String removeLiterals(int var1, String var2) {
         int var4 = ENYKMaskFormatter.this.mask_chars.length;
         String var3 = "";
         int var5 = var1;

         for(int var6 = var1 + var2.length(); var5 >= 0 && var5 < var6 && var5 < var4; ++var5) {
            if (!(ENYKMaskFormatter.this.mask_chars[var5] instanceof ENYKMaskFormatter.MaskCharLiteral)) {
               var3 = var3 + var2.charAt(var5 - var1);
            }
         }

         return var3;
      }

      private int decreasebyLiterals(int var1, int var2) {
         int var4 = ENYKMaskFormatter.this.mask_chars.length;
         int var3 = 0;
         int var5 = var1;

         for(int var6 = var1 + var2; var5 >= 0 && var5 < var6 && var5 < var4; ++var5) {
            if (!(ENYKMaskFormatter.this.mask_chars[var5] instanceof ENYKMaskFormatter.MaskCharLiteral)) {
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
         ENYKMaskFormatter.this.setEditValid(false);

         try {
            if (ENYKMaskFormatter.this.irids != null && !this.removeLiterals(var2, var3).matches(ENYKMaskFormatter.this.irids)) {
               if (!this.removeLiterals(var2, var3).toUpperCase().matches(ENYKMaskFormatter.this.irids)) {
                  return;
               }

               var3 = var3.toUpperCase();
            }

            String var8 = this.removeLiterals(var2, var3);
            JFormattedTextField var5 = ENYKMaskFormatter.this.getFormattedTextField();
            String var6 = var5.getText();
            String var7 = ENYKMaskFormatter.this.stringToValue_(var6, false).toString();
            int var11 = this.offsetToValueOffset(var2);
            int var12 = var8.length();
            String var10;
            int var14;
            if (!ENYKMaskFormatter.this.getOverwriteMode()) {
               var10 = var7.substring(0, var11) + var8 + var7.substring(var11 + var12);
               var14 = var11 + var12;
            } else {
               var10 = var7.substring(0, var11) + var8 + var7.substring(var11);
               var14 = var11 + var12;
            }

            var14 = this.valueOffsetToOffset(var14);
            String var9 = ENYKMaskFormatter.this.valueToString_(var10, false);
            int var13 = this.valueOffsetToOffset(var11);
            var1.replace(var13, var9.length() - var13, var9.substring(var13), (AttributeSet)null);
            var5.getCaret().setDot(var14);
            var5.commitEdit();
         } catch (Exception var15) {
            var15.printStackTrace();
         }

         ENYKMaskFormatter.this.setEditValid(true);
      }

      public void remove(FilterBypass var1, int var2, int var3) throws BadLocationException {
         ENYKMaskFormatter.this.setEditValid(false);

         try {
            JFormattedTextField var4 = ENYKMaskFormatter.this.getFormattedTextField();
            String var5 = var4.getText();
            String var6 = ENYKMaskFormatter.this.stringToValue_(var5, false).toString();
            int var10 = var2 - var4.getSelectionStart();
            int var11 = this.offsetToValueOffset(var2);
            int var12 = this.decreasebyLiterals(var2, var3);
            var12 = this.applayBackDeleteFactor(var12, var10);
            String var9 = "";

            for(int var15 = 0; var15 < var12; ++var15) {
               var9 = var9 + ENYKMaskFormatter.this.placeholder;
            }

            String var8;
            int var14;
            if (!ENYKMaskFormatter.this.getOverwriteMode()) {
               var8 = var6.substring(0, var11) + var9 + var6.substring(var11 + var12);
               var14 = var11 + var12 + var10;
            } else {
               var8 = var6.substring(0, var11) + var6.substring(var11 + var12) + var9;
               var14 = var11;
            }

            var14 = this.valueOffsetToOffset(var14);
            String var7 = ENYKMaskFormatter.this.valueToString_(var8, false);
            int var13 = this.valueOffsetToOffset(var11);
            var1.replace(var13, var7.length() - var13, var7.substring(var13), (AttributeSet)null);
            var4.getCaret().setDot(var14);
            var4.commitEdit();
         } catch (Exception var16) {
            var16.printStackTrace();
         }

         ENYKMaskFormatter.this.setEditValid(true);
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
         if (var2 >= 0 && var2 <= ENYKMaskFormatter.this.mask_chars.length) {
            int var6 = ENYKMaskFormatter.this.mask_chars.length;
            int var7;
            switch(var4) {
            case 3:
               for(var7 = var2 + 1; var7 <= var6; ++var7) {
                  if (var7 >= var6) {
                     return var7;
                  }

                  if (!(ENYKMaskFormatter.this.mask_chars[var7] instanceof ENYKMaskFormatter.MaskCharLiteral)) {
                     return var7;
                  }
               }

               return -1;
            case 7:
               for(var7 = var2 - 1; var7 >= 0; --var7) {
                  if (!(ENYKMaskFormatter.this.mask_chars[var7] instanceof ENYKMaskFormatter.MaskCharLiteral)) {
                     return var7;
                  }
               }
            }
         }

         return -1;
      }

      public void moveDot(javax.swing.text.NavigationFilter.FilterBypass var1, int var2, Bias var3) {
         if (var2 >= 0 && var2 < ENYKMaskFormatter.this.mask_chars.length) {
            if (ENYKMaskFormatter.this.mask_chars[var2] instanceof ENYKMaskFormatter.MaskCharLiteral) {
               int var4 = var2;

               for(int var5 = ENYKMaskFormatter.this.mask_chars.length; var4 <= var5; ++var4) {
                  if (var4 < var5) {
                     if (!(ENYKMaskFormatter.this.mask_chars[var4] instanceof ENYKMaskFormatter.MaskCharLiteral)) {
                        var1.moveDot(var4, var3);
                        return;
                     }
                  } else {
                     var1.moveDot(var2, var3);
                  }
               }
            } else {
               var1.moveDot(var2, var3);
            }
         } else if (var2 == ENYKMaskFormatter.this.mask_chars.length) {
            var1.moveDot(var2, var3);
         }

      }

      public void setDot(javax.swing.text.NavigationFilter.FilterBypass var1, int var2, Bias var3) {
         if (var2 >= 0 && var2 < ENYKMaskFormatter.this.mask_chars.length) {
            if (ENYKMaskFormatter.this.mask_chars[var2] instanceof ENYKMaskFormatter.MaskCharLiteral) {
               int var4 = var2;

               for(int var5 = ENYKMaskFormatter.this.mask_chars.length; var4 <= var5; ++var4) {
                  if (var4 < var5) {
                     if (!(ENYKMaskFormatter.this.mask_chars[var4] instanceof ENYKMaskFormatter.MaskCharLiteral)) {
                        var1.setDot(var4, var3);
                        return;
                     }
                  } else {
                     var1.setDot(var2, var3);
                  }
               }
            } else {
               var1.setDot(var2, var3);
            }
         } else if (var2 == ENYKMaskFormatter.this.mask_chars.length) {
            var1.setDot(var2, var3);
         }

      }

      // $FF: synthetic method
      ENYKNavigationFilter(Object var2) {
         this();
      }
   }

   private class MaskCharPlaceholder extends ENYKMaskFormatter.MaskChar {
      MaskCharPlaceholder(char var2) {
         super(var2);
      }

      boolean isValid(char var1) {
         return this.c == var1;
      }
   }

   private class MaskCharLower extends ENYKMaskFormatter.MaskChar {
      MaskCharLower(char var2) {
         super(var2);
      }

      boolean isValid(char var1) {
         return Character.isLowerCase(var1);
      }
   }

   private class MaskCharUpper extends ENYKMaskFormatter.MaskChar {
      MaskCharUpper(char var2) {
         super(var2);
      }

      boolean isValid(char var1) {
         return Character.isUpperCase(var1);
      }
   }

   private class MaskCharLetter extends ENYKMaskFormatter.MaskChar {
      MaskCharLetter(char var2) {
         super(var2);
      }

      boolean isValid(char var1) {
         return Character.isLetter(var1);
      }
   }

   private class MaskCharDigit extends ENYKMaskFormatter.MaskChar {
      MaskCharDigit(char var2) {
         super(var2);
      }

      boolean isValid(char var1) {
         return Character.isDigit(var1);
      }
   }

   private class MaskCharAny extends ENYKMaskFormatter.MaskChar {
      MaskCharAny(char var2) {
         super(var2);
      }

      boolean isValid(char var1) {
         return true;
      }
   }

   private class MaskCharLiteral extends ENYKMaskFormatter.MaskChar {
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
