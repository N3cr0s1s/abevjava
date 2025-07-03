package hu.piller.enykp.gui.component;

import hu.piller.enykp.util.base.Tools;
import java.io.Serializable;
import java.text.ParseException;
import javax.swing.JFormattedTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultFormatter;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;
import javax.swing.text.DocumentFilter.FilterBypass;

public class ENYKABEVMaskedNumberFormatter extends DefaultFormatter {
   private int max_length = -1;
   private String mask = null;
   private String irids = null;
   private boolean apply_mask = false;
   private final DocumentFilter doc_filter = new ENYKABEVMaskedNumberFormatter.ENYKMaskDocumentFilter();
   private JFormattedTextField ftf;
   private ENYKABEVMaskedNumberFormatter.MaskChar[] mask_chars = null;
   private final ENYKABEVNumberFormatter number_formatter = new ENYKABEVNumberFormatter();
   private final char placeholder = ' ';
   private static final char ESCAPE = '\\';
   private static final char DIGIT = '#';

   public ENYKABEVMaskedNumberFormatter() {
   }

   public ENYKABEVMaskedNumberFormatter(String var1) {
      this.setMask(var1);
   }

   public ENYKABEVMaskedNumberFormatter(String var1, boolean var2) {
      this.setMask(var1, var2);
   }

   public void install(JFormattedTextField var1) {
      super.install(var1);
      this.ftf = var1;
      Object var2;
      if (var1 != null) {
         var2 = var1.getValue();
      } else {
         var2 = null;
      }

      try {
         this.stringToValue(this.valueToString(var2));
      } catch (Exception var4) {
         this.setEditValid(false);
         var4.printStackTrace();
      }

      this.number_formatter.setSubstituteFormattedTextField(var1);
      this.number_formatter.setOverwriteMode(this.getOverwriteMode());
   }

   public void setMask(String var1) {
      this.setMask(var1, false);
   }

   public void setMask(String var1, boolean var2) {
      this.mask = var1;
      if (var1 == null) {
         this.mask_chars = new ENYKABEVMaskedNumberFormatter.MaskChar[0];
      } else if (var1.trim().length() == 0) {
         this.mask = null;
         this.mask_chars = new ENYKABEVMaskedNumberFormatter.MaskChar[0];
      } else {
         this.createMaskChars();
         if (this.max_length <= 0) {
            this.setMaxLength(this.countMask(var1), true);
         }
      }

      this.number_formatter.setThousandTagging(var2);
   }

   public void setIrids(String var1) {
      this.irids = var1;
      this.number_formatter.setIrids(var1);
   }

   public String getMask() {
      return this.mask;
   }

   public void setMaxLength(int var1) {
      this.setMaxLength(var1, false);
   }

   public void setMaxLength(int var1, boolean var2) {
      if (!var2 && var1 != 0 && var1 != -1) {
      }

      if (this.max_length < 1 || var1 < this.max_length) {
         this.max_length = var1;
         this.number_formatter.setMaxLength(var1);
      }

   }

   public void setMaxRound(int var1) {
      this.number_formatter.setMaxRound(var1);
   }

   public int getMaxLength() {
      return this.max_length;
   }

   public void setApplyMask(boolean var1) {
      this.apply_mask = var1;
   }

   public boolean isApplyMask() {
      return this.apply_mask;
   }

   public void setThousandTagging(boolean var1) {
      this.number_formatter.setThousandTagging(var1);
   }

   public String getThousandTagger() {
      return this.number_formatter.getThousandTagger();
   }

   private void createMaskChars() {
      boolean var2 = false;
      int var3 = this.mask.length();
      int var4 = 0;
      this.mask_chars = new ENYKABEVMaskedNumberFormatter.MaskChar[var3];

      for(int var5 = 0; var5 < var3; ++var5) {
         char var1 = this.mask.charAt(var5);
         if (var2) {
            this.mask_chars[var4++] = new ENYKABEVMaskedNumberFormatter.MaskCharLiteral(var1);
            var2 = false;
         } else if (var1 == '\\') {
            var2 = true;
         } else if (var1 == '#') {
            this.mask_chars[var4++] = new ENYKABEVMaskedNumberFormatter.MaskCharDigit(var1);
         } else {
            this.mask_chars[var4++] = new ENYKABEVMaskedNumberFormatter.MaskCharLiteral(var1);
         }
      }

      if (var3 > var4) {
         ENYKABEVMaskedNumberFormatter.MaskChar[] var6 = this.mask_chars;
         this.mask_chars = new ENYKABEVMaskedNumberFormatter.MaskChar[var4];
         System.arraycopy(var6, 0, this.mask_chars, 0, var4);
      }

   }

   private int countMask(String var1) {
      if (var1 == null) {
         return 0;
      } else {
         int var2 = 0;

         for(int var3 = 0; var3 < var1.length(); ++var3) {
            if (var1.charAt(var3) == '#') {
               ++var2;
            }
         }

         return var2;
      }
   }

   private String unMask(int var1, String var2) {
      if (this.apply_mask && this.mask != null) {
         int var3 = var2.length() + var1;
         if (var3 <= this.mask_chars.length) {
            String var4 = "";
            int var5 = var1;

            for(int var6 = 0; var5 < var3; ++var6) {
               if (this.mask_chars[var5] instanceof ENYKABEVMaskedNumberFormatter.MaskCharDigit) {
                  var4 = var4 + var2.charAt(var6);
               } else if (this.mask_chars[var5] instanceof ENYKABEVMaskedNumberFormatter.MaskCharLiteral && var2.charAt(var6) != this.mask_chars[var5].getChar()) {
                  var4 = var4 + var2.charAt(var6);
               }

               ++var5;
            }

            var2 = var4;
         }
      }

      return var2;
   }

   private String applyMask(Object var1) {
      String var2 = var1.toString();
      if (this.apply_mask && this.mask != null && this.ftf != null) {
         String var3;
         int var4;
         int var5;
         if (this.ftf.getHorizontalAlignment() == 4) {
            var3 = "";
            var4 = var2.length() - 1;

            for(var5 = this.mask_chars.length - 1; var5 >= 0; --var5) {
               if (this.mask_chars[var5] instanceof ENYKABEVMaskedNumberFormatter.MaskCharDigit) {
                  if (var4 >= 0) {
                     var3 = var2.charAt(var4--) + var3;
                  } else {
                     var3 = ' ' + var3;
                  }
               } else {
                  var3 = this.mask_chars[var5] + var3;
               }
            }

            var2 = var3;
         } else {
            var3 = "";
            var4 = 0;
            var5 = var2.length();
            int var6 = 0;

            for(int var7 = this.mask_chars.length - var2.length(); var6 < var7; ++var6) {
               if (this.mask_chars[var6] instanceof ENYKABEVMaskedNumberFormatter.MaskCharDigit) {
                  if (var4 < var5) {
                     var3 = var3 + var2.charAt(var4++);
                  } else {
                     var3 = var3 + ' ';
                  }
               } else {
                  var3 = var3 + this.mask_chars[var6];
               }
            }

            var2 = var3;
         }
      }

      return var2;
   }

   private int literalCount(int var1, int var2, int var3, boolean var4) {
      int var5 = 0;
      int var6 = 0;
      int var7 = this.mask_chars.length;
      boolean var8 = true;
      int var9 = var1;

      for(int var10 = Math.min(var2 + 1, var7); var9 < var10 || var3 == 1 && var9 < var7 && this.mask_chars[var9] instanceof ENYKABEVMaskedNumberFormatter.MaskCharLiteral; ++var9) {
         if (!(this.mask_chars[var9] instanceof ENYKABEVMaskedNumberFormatter.MaskCharLiteral) || (!var4 || !var8) && var4) {
            if (this.mask_chars[var9] instanceof ENYKABEVMaskedNumberFormatter.MaskCharDigit) {
               var8 = true;
            }
         } else {
            ++var5;
            var8 = false;
         }

         if (var3 == -1 && var9 + 1 < var10 && (this.mask_chars[var9] instanceof ENYKABEVMaskedNumberFormatter.MaskCharDigit && this.mask_chars[var9 + 1] instanceof ENYKABEVMaskedNumberFormatter.MaskCharLiteral || this.mask_chars[var9 + 1] instanceof ENYKABEVMaskedNumberFormatter.MaskCharDigit && this.mask_chars[var9] instanceof ENYKABEVMaskedNumberFormatter.MaskCharLiteral)) {
            var6 = var5;
         }
      }

      var5 = var3 == -1 ? var6 : var5;
      return var5;
   }

   private int moveCaretFromLiteral(int var1, int var2) {
      int var3 = var1;
      if (var1 >= 0 && var1 < this.mask_chars.length && !(this.mask_chars[var1] instanceof ENYKABEVMaskedNumberFormatter.MaskCharDigit)) {
         int var4;
         if (var2 == 1) {
            var4 = var1;

            for(int var5 = this.mask_chars.length; var4 < var5 && this.mask_chars[var4] instanceof ENYKABEVMaskedNumberFormatter.MaskCharLiteral; ++var4) {
               ++var3;
            }
         } else if (var2 == -1) {
            for(var4 = var1; var4 >= 0 && this.mask_chars[var4] instanceof ENYKABEVMaskedNumberFormatter.MaskCharLiteral; --var4) {
               --var3;
            }
         }
      }

      return var3;
   }

   private int getFirstNumPos(String var1) {
      int var2 = this.mask_chars.length;
      int var3 = 0;

      for(int var4 = Math.min(var1.length(), this.mask_chars.length); var3 < var4; ++var3) {
         if (this.mask_chars[var3] instanceof ENYKABEVMaskedNumberFormatter.MaskCharDigit && var1.charAt(var3) != ' ') {
            return var3;
         }
      }

      return var2;
   }

   public Object stringToValue(String var1) throws ParseException {
      return this.stringToValue_(var1, true);
   }

   public String valueToString(Object var1) throws ParseException {
      return this.valueToString_(var1);
   }

   public Object stringToValue_(String var1, boolean var2) throws ParseException {
      String var3 = this.unMask(0, var1);
      Object var4 = this.number_formatter.stringToValue_((String)var3, var2);
      return var4;
   }

   public String valueToString_(Object var1) throws ParseException {
      String var2 = this.number_formatter.valueToString_(var1);
      var2 = this.applyMask(var2);
      return var2;
   }

   protected DocumentFilter getDocumentFilter() {
      return this.doc_filter;
   }

   private class ENYKMaskDocumentFilter extends DocumentFilter implements Serializable {
      private ENYKMaskDocumentFilter() {
      }

      public synchronized void remove(FilterBypass var1, int var2, int var3) throws BadLocationException {
         DocumentFilter var4 = ENYKABEVMaskedNumberFormatter.this.number_formatter.getDocumentFilter();
         if (ENYKABEVMaskedNumberFormatter.this.mask != null) {
            int var5 = ENYKABEVMaskedNumberFormatter.this.ftf.getCaretPosition();
            String var6 = this.getDocText();
            var2 = Math.min(var2, var6.length());
            String var7 = ENYKABEVMaskedNumberFormatter.this.unMask(0, var6);
            this.setDocText(var7, var1, (AttributeSet)null);
            int var8;
            if (var3 > 1) {
               var8 = var3 - ENYKABEVMaskedNumberFormatter.this.literalCount(var2, var2 + var3 - 2, 1, false);
            } else {
               var8 = var3;
            }

            try {
               if (var2 < var5) {
                  var4.remove(var1, Math.max(var2 - ENYKABEVMaskedNumberFormatter.this.literalCount(0, var2 - 1, 1, true), 0), var8);
               } else {
                  var4.remove(var1, Math.max(var2 - ENYKABEVMaskedNumberFormatter.this.literalCount(0, var2, -1, true), 0), var8);
               }
            } catch (BadLocationException var13) {
               System.out.println("" + var13);
            }

            String var9 = this.getDocText();
            String var10 = ENYKABEVMaskedNumberFormatter.this.applyMask(var9);
            this.setDocText(var10, var1, (AttributeSet)null);
            int var11;
            if (var2 < var5) {
               var11 = var5;
            } else {
               var11 = Math.min(Math.max(ENYKABEVMaskedNumberFormatter.this.moveCaretFromLiteral(var2, 1) + var3, ENYKABEVMaskedNumberFormatter.this.getFirstNumPos(var10)), ENYKABEVMaskedNumberFormatter.this.ftf.getDocument().getLength());
            }

            var11 = Math.max(0, Math.min(ENYKABEVMaskedNumberFormatter.this.ftf.getDocument().getLength(), var11));
            ENYKABEVMaskedNumberFormatter.this.ftf.setCaretPosition(var11);
         } else {
            try {
               var4.remove(var1, var2, var3);
            } catch (BadLocationException var12) {
               Tools.eLog(var12, 0);
            }
         }

      }

      public synchronized void insertString(FilterBypass var1, int var2, String var3, AttributeSet var4) throws BadLocationException {
         DocumentFilter var5 = ENYKABEVMaskedNumberFormatter.this.number_formatter.getDocumentFilter();
         if (ENYKABEVMaskedNumberFormatter.this.mask != null) {
            int var6 = ENYKABEVMaskedNumberFormatter.this.ftf.getCaretPosition();
            String var7 = this.getDocText();
            var7 = ENYKABEVMaskedNumberFormatter.this.unMask(0, var7);
            this.setDocText(var7, var1, (AttributeSet)null);
            var3 = ENYKABEVMaskedNumberFormatter.this.unMask(var2, var3);
            var5.insertString(var1, Math.max(var2 - ENYKABEVMaskedNumberFormatter.this.literalCount(0, var2, -1, true), 0), var3, var4);
            var7 = this.getDocText();
            var7 = ENYKABEVMaskedNumberFormatter.this.applyMask(var7);
            this.setDocText(var7, var1, (AttributeSet)null);
            int var8 = ENYKABEVMaskedNumberFormatter.this.number_formatter.getLastInsertCount();
            int var9;
            if (var8 > 0) {
               int var10 = ENYKABEVMaskedNumberFormatter.this.getFirstNumPos(var7) + var8;
               var9 = var2 < var10 ? var10 : var2;
            } else {
               var9 = var6;
            }

            var9 = Math.max(0, Math.min(ENYKABEVMaskedNumberFormatter.this.ftf.getDocument().getLength(), var9));
            ENYKABEVMaskedNumberFormatter.this.ftf.setCaretPosition(var9);
         } else {
            var5.insertString(var1, var2, var3, var4);
         }

      }

      public synchronized void replace(FilterBypass var1, int var2, int var3, String var4, AttributeSet var5) throws BadLocationException {
         int var6 = this.getDocText().length();
         this.remove(var1, var2, var3);
         int var7 = this.getDocText().length();
         if (var6 == var7 && var3 > 0 && var3 < var6) {
            this.insertString(var1, var2 + var3, var4, var5);
         } else {
            this.insertString(var1, Math.min(var2, var7), var4, var5);
         }

      }

      private String getDocText() throws BadLocationException {
         Document var1 = ENYKABEVMaskedNumberFormatter.this.ftf.getDocument();
         return var1.getText(0, var1.getLength());
      }

      private void setDocText(String var1, FilterBypass var2, AttributeSet var3) throws BadLocationException {
         super.remove(var2, 0, this.getDocText().length());
         super.insertString(var2, 0, var1, var3);
      }

      // $FF: synthetic method
      ENYKMaskDocumentFilter(Object var2) {
         this();
      }
   }

   private class MaskCharDigit extends ENYKABEVMaskedNumberFormatter.MaskChar {
      MaskCharDigit(char var2) {
         super(var2);
      }

      boolean isValid(char var1) {
         boolean var2 = Character.isDigit(var1);
         var2 |= "-, ".indexOf(var1) >= 0;
         return var2;
      }
   }

   private class MaskCharLiteral extends ENYKABEVMaskedNumberFormatter.MaskChar {
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

      public char getChar() {
         return this.c;
      }
   }
}
