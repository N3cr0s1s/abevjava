package hu.piller.enykp.gui.component;

import java.io.Serializable;
import java.text.ParseException;
import javax.swing.JFormattedTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultFormatter;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;
import javax.swing.text.DocumentFilter.FilterBypass;

public class ENYKABEVNumberFormatter extends DefaultFormatter {
   public int max_length = -1;
   private int max_round = -1;
   private final DocumentFilter doc_filter = new ENYKABEVNumberFormatter.ENYKNumberDocumentFilter();
   private boolean thousand_tagging = false;
   private JFormattedTextField subst_ftf = null;
   private String irids = null;
   private String thousand_tagger = " ";
   private final String visible_point = ",";
   private final String point = ".";
   private final String negative_sign = "-";
   private final String positive_sign = "";
   private final String ps_ns = "-";
   private final String negative_sign_key = "-";
   private final String positive_sign_key = "+";
   private final String[] sign_keys = new String[]{"+", "-"};
   private final String[] signs = new String[]{"", "-"};

   public void install(JFormattedTextField var1) {
      super.install(var1);
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

   }

   protected JFormattedTextField getFormattedTextField() {
      JFormattedTextField var1 = super.getFormattedTextField();
      if (var1 == null) {
         var1 = this.subst_ftf;
      }

      return var1;
   }

   public void setSubstituteFormattedTextField(JFormattedTextField var1) {
      this.subst_ftf = var1;
   }

   public JFormattedTextField getSubstituteFormattedTextField() {
      return this.subst_ftf;
   }

   public void setMaxLength(int var1) {
      this.max_length = var1;
   }

   public int getMaxLength() {
      return this.max_length;
   }

   public void setMaxRound(int var1) {
      this.max_round = var1;
   }

   public int getMaxRound() {
      return this.max_round;
   }

   public boolean hasMaxRound() {
      return this.max_round != -1;
   }

   public void setIrids(String var1) {
      if (var1 != null) {
         if (!var1.startsWith("[^")) {
            boolean var2 = var1.endsWith("]") || var1.endsWith("]*");
            String var3 = "";
            if (this.thousand_tagger.indexOf(32) < 0) {
               var3 = " ";
            }

            if (var2) {
               var1 = var1.substring(0, var1.lastIndexOf(93)) + "[" + this.thousand_tagger + var3 + "]]";
            } else {
               var1 = "[" + var1 + "[" + this.thousand_tagger + var3 + "]]";
            }

            var1 = var1 + "*";
         } else if (!var1.endsWith("*")) {
            var1 = var1 + "*";
         }
      }

      this.irids = var1;
   }

   public void setThousandTagging(boolean var1) {
      this.thousand_tagging = var1;
   }

   public boolean isThousandTagging() {
      return this.thousand_tagging;
   }

   public String getThousandTagger() {
      return this.thousand_tagger;
   }

   public int getLastInsertCount() {
      return ((ENYKABEVNumberFormatter.ENYKNumberDocumentFilter)this.doc_filter).getLastInsertCount();
   }

   public boolean isEnabledPoint() {
      if (this.irids == null) {
         return true;
      } else {
         return this.irids.indexOf(".") >= 0 || this.irids.indexOf(",") >= 0;
      }
   }

   protected String unMask(String var1) {
      String var2 = var1.trim();
      if (this.thousand_tagging) {
         var2 = var2.replaceAll(this.thousand_tagger, "");
      }

      return var2;
   }

   protected String applyMask(String var1) {
      String var2 = var1.trim();
      if (this.thousand_tagging) {
         int var5 = var2.indexOf(",");
         String var4;
         if (var5 >= 0) {
            var4 = var2.substring(0, var5);
         } else {
            var4 = var2;
         }

         for(int var6 = var4.length() - 3; var6 >= 0; var6 -= 3) {
            String var3 = var4.substring(0, var6);
            if ("-".indexOf(var3) < 0) {
               var4 = var3 + this.thousand_tagger + var4.substring(var6);
            }
         }

         if (var5 >= 0) {
            var2 = var4 + var2.substring(var5);
         } else {
            var2 = var4;
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
      String var3 = var1 == null ? "" : var1;
      var3 = this.unMask(var3);
      if (!var2) {
         int var4 = var3.indexOf(",");
         if (var4 == 0) {
            var3 = "0" + var3;
            ++var4;
         }

         if (var4 >= 0) {
            if (this.getFraPart(var3, ",").replaceAll("0", "").length() == 0) {
               var3 = var3.substring(0, var4);
               var4 = -1;
            }

            String var5 = this.getSign(var3, this.signs);
            if (this.getIntPart(var3, ",").length() == var5.length()) {
               var3 = var3.replaceFirst(var5, var5 + "0");
               if (var4 >= 0) {
                  ++var4;
               }
            }
         }

         if (var3.length() == var4) {
            var3 = var3.substring(0, var3.length() - 1);
         }

         if (var3.length() > this.max_length) {
            var3 = var3.substring(0, this.max_length);
         }
      }

      var3 = var3.replaceFirst("\\,", ".");
      if (var3.startsWith("-")) {
         if ("-".equals(var3)) {
            var3 = "";
         } else if (this.hasDigit(var3)) {
            if (this.isNull(var3)) {
               var3 = "0";
            }
         } else {
            var3 = "";
         }
      }

      return super.stringToValue(var3);
   }

   private boolean hasDigit(String var1) {
      boolean var2 = false;
      if (var1.indexOf("0") != -1 || var1.indexOf("1") != -1 || var1.indexOf("2") != -1 || var1.indexOf("3") != -1 || var1.indexOf("4") != -1 || var1.indexOf("5") != -1 || var1.indexOf("6") != -1 || var1.indexOf("7") != -1 || var1.indexOf("8") != -1 || var1.indexOf("9") != -1) {
         var2 = true;
      }

      return var2;
   }

   private boolean isNull(String var1) {
      boolean var2 = true;

      for(int var3 = 0; var3 < var1.length(); ++var3) {
         char var4 = var1.charAt(var3);
         if (var4 == '1' || var4 == '2' || var4 == '3' || var4 == '4' || var4 == '5' || var4 == '6' || var4 == '7' || var4 == '8' || var4 == '9') {
            var2 = false;
            break;
         }
      }

      return var2;
   }

   public String valueToString_(Object var1) throws ParseException {
      String var2 = var1 == null ? "" : var1.toString();
      var2 = var2.replaceFirst("\\.", ",");
      return this.applyMask(var2);
   }

   protected DocumentFilter getDocumentFilter() {
      return this.doc_filter;
   }

   private String getIntPart(String var1, String var2) {
      int var3 = var1.indexOf(var2);
      return var3 < 0 ? var1 : var1.substring(0, var3);
   }

   private String getFraPart(String var1, String var2) {
      int var3 = var1.indexOf(var2);
      return var3 < 0 ? "" : var1.substring(var3 + 1);
   }

   private String getSign(String var1, String[] var2) {
      int var3 = 0;

      for(int var4 = var2.length; var3 < var4; ++var3) {
         if (var2[var3].length() > 0 && var1.startsWith(var2[var3])) {
            return var2[var3];
         }
      }

      return "";
   }

   private class ENYKNumberDocumentFilter extends DocumentFilter implements Serializable {
      private static final int FLOW_TO_BEGIN = 0;
      private static final int TAKE_LAST = 1;
      private static final int TAKE_FIRST = 2;
      private static final int TAKE_FROM_LAST = 3;
      private static final int REMOVE_FROM_BEGIN = 4;
      private final String[] sign_keys;
      private final String[] signs;
      private int last_insert_count;

      private ENYKNumberDocumentFilter() {
         this.sign_keys = new String[]{"+", "-"};
         this.signs = new String[]{"", "-"};
      }

      private String replaceAll(String var1, String var2, String var3) {
         var2 = var2.replaceAll("\\\\", "\\\\\\\\");
         var2 = var2.replaceAll("\\+", "\\\\+");
         var2 = var2.replaceAll("\\.", "\\\\.");
         var2 = var2.replaceAll("\\-", "\\\\-");
         return var1.replaceAll(var2, var3);
      }

      private String normalize(String var1, String var2, int var3) {
         String var4 = var1;
         int var5;
         int var7;
         int var8;
         switch(var3) {
         case 0:
            var7 = 0;
            var8 = 0;

            for(int var9 = var1.length(); var8 < var9; ++var8) {
               char var6;
               if (var2.indexOf(var6 = var4.charAt(var8)) >= 0) {
                  var4 = var4.substring(0, var7) + var6 + var4.substring(var7++, var8) + var4.substring(var8 + 1);
               }
            }

            return var4;
         case 1:
            var5 = var1.lastIndexOf(var2);
            if (var5 >= 0) {
               var4 = this.replaceAll(var1, var2, "");
               var4 = var4.substring(0, var5) + var2 + var4.substring(var5);
            }
            break;
         case 2:
            var5 = var1.indexOf(var2);
            if (var5 >= 0) {
               var4 = this.replaceAll(var1, var2, "");
               var4 = var4.substring(0, var5) + var2 + var4.substring(var5);
            }
            break;
         case 3:
            var5 = 0;
            var7 = 0;

            for(var8 = var2.length(); var7 < var8; ++var7) {
               var5 = Math.max(var5, var1.indexOf(var2.charAt(var7)));
            }

            var4 = var1.substring(var5);
            break;
         case 4:
            var5 = var2.length();
            if (var5 > 0) {
               while(var4.startsWith(var2)) {
                  var4 = var4.substring(var5);
               }
            }
         }

         return var4;
      }

      private String getSign(String var1, String[] var2) {
         int var3 = 0;

         for(int var4 = var2.length; var3 < var4; ++var3) {
            if (var2[var3].length() > 0 && var1.startsWith(var2[var3])) {
               return var2[var3];
            }
         }

         return "";
      }

      private String getNumber(String var1, String[] var2) {
         int var3 = 0;

         for(int var4 = var2.length; var3 < var4; ++var3) {
            if (var2[var3].length() > 0 && var1.startsWith(var2[var3])) {
               var1 = var1.substring(var2[var3].length());
            }
         }

         return var1;
      }

      private String getIntPart(String var1, String var2) {
         int var3 = var1.indexOf(var2);
         return var3 < 0 ? var1 : var1.substring(0, var3);
      }

      private String getFraPart(String var1, String var2) {
         int var3 = var1.indexOf(var2);
         return var3 < 0 ? "" : var1.substring(var3 + 1);
      }

      private String getStringQueue(String var1, int var2) {
         String var3 = null;
         if (var1 != null) {
            int var4 = var1.length();
            if (var4 > 0 && var2 > 0) {
               StringBuffer var5 = new StringBuffer(var1.length() * var2);

               for(int var6 = 0; var6 < var2; ++var6) {
                  var5.append(var1);
               }

               var3 = var5.toString();
            } else {
               var3 = "";
            }
         }

         return var3;
      }

      private int getIndexOf(String var1, String[] var2) {
         if (var2 != null) {
            int var3 = 0;

            for(int var4 = var2.length; var3 < var4; ++var3) {
               if (var2[var3].equals(var1)) {
                  return var3;
               }
            }
         }

         return -1;
      }

      public int getLastInsertCount() {
         return this.last_insert_count;
      }

      public synchronized void insertString(FilterBypass var1, int var2, String var3, AttributeSet var4) throws BadLocationException {
         ENYKABEVNumberFormatter.this.setEditValid(false);
         this.last_insert_count = 0;
         if (ENYKABEVNumberFormatter.this.irids != null && !var3.matches(ENYKABEVNumberFormatter.this.irids)) {
            ENYKABEVNumberFormatter.this.setEditValid(true);
         } else {
            try {
               JFormattedTextField var5 = ENYKABEVNumberFormatter.this.getFormattedTextField();
               int var6 = var5.getCaretPosition();
               var3 = this.replaceAll(var3, " ", "");
               var3 = this.normalize(var3, "", 2);
               var3 = this.normalize(var3, "-", 2);
               var3 = this.normalize(var3, "-", 0);
               var3 = this.normalize(var3, "-", 3);
               var3 = this.replaceAll(var3, ".", ",");
               var3 = this.normalize(var3, ",", 1);
               String var7 = this.getSign(var3, this.sign_keys);
               Document var8 = var5.getDocument();
               String var9 = var8.getText(0, var8.getLength());
               String var10 = ENYKABEVNumberFormatter.this.unMask(var9);
               if ((var10.indexOf(",") != -1 || var10.indexOf(".") != -1) && (var3.indexOf(",") != -1 || var3.indexOf(",") != -1)) {
                  return;
               }

               int var11 = var10.length() - ENYKABEVNumberFormatter.this.unMask(var9.substring(var2)).length();
               boolean var12 = var3.indexOf(",") >= 0 && var10.indexOf(",") >= 0;
               if (ENYKABEVNumberFormatter.this.max_length >= 0) {
                  int var13 = var12 ? -1 : 0;
                  var13 += var7.length() > 0 ? -1 : 0;
                  if (var10.length() + var3.length() + var13 > ENYKABEVNumberFormatter.this.max_length) {
                     var3 = var3.substring(0, Math.max(0, ENYKABEVNumberFormatter.this.max_length - var10.length() - var13));
                     if (var3.length() == 0) {
                        return;
                     }
                  }
               }

               boolean var29 = var3.indexOf(",") >= 0 || var10.indexOf(",") >= 0;
               if (var12) {
                  var10 = this.replaceAll(var10, ",", this.getStringQueue(" ", ",".length()));
               }

               var3 = this.getNumber(var3, this.sign_keys);
               String var14 = this.getSign(var10, this.signs);
               var10 = this.getNumber(var10, this.signs);
               var11 = Math.max(var11 - var14.length(), 0);
               String var15 = var10.substring(0, var11) + var3 + var10.substring(var11);
               var15 = ENYKABEVNumberFormatter.this.unMask(var15.trim());
               String var16 = var14;
               int var17 = this.getIndexOf(var7, this.sign_keys);
               String var18;
               if (var17 >= 0) {
                  var18 = this.signs[var17];
                  if (!var14.equals(var18)) {
                     var16 = var18;
                  }
               }

               var18 = this.getIntPart(var15, ",");
               String var19 = this.getFraPart(var15, ",");
               if (ENYKABEVNumberFormatter.this.hasMaxRound() && var19.length() > ENYKABEVNumberFormatter.this.getMaxRound()) {
                  return;
               }

               int var20 = var18.length();
               boolean var21 = var18.startsWith("0");
               var18 = this.normalize(var18, "0", 4);
               if (var29 && var18.length() == 0 && var21) {
                  var18 = "0";
               }

               if ((var10.length() == 0 || var10.equals("0")) && var18.length() == 0 && var20 > 0) {
                  var18 = "0";
               }

               var15 = var16 + ENYKABEVNumberFormatter.this.applyMask(var18);
               if (var29) {
                  var15 = var15 + "," + var19;
               }

               var1.remove(0, var8.getLength());
               var1.insertString(0, var15, var4);
               int var22;
               if (var3.equals(",")) {
                  var22 = var15.indexOf(",") + var3.length() - var3.indexOf(",");
               } else {
                  var22 = Math.max(0, var6 + (var15.length() - var9.length()));
               }

               var22 = Math.max(0, Math.min(var8.getLength(), var22));
               var5.setCaretPosition(var22);
               var5.commitEdit();
            } catch (Exception var27) {
               var27.printStackTrace();
               if (var27 instanceof BadLocationException) {
                  throw (BadLocationException)var27;
               }
            } finally {
               ENYKABEVNumberFormatter.this.setEditValid(true);
            }

         }
      }

      public synchronized void remove(FilterBypass var1, int var2, int var3) throws BadLocationException {
         if (var3 != 0) {
            ENYKABEVNumberFormatter.this.setEditValid(false);

            try {
               JFormattedTextField var4 = ENYKABEVNumberFormatter.this.getFormattedTextField();
               Document var5 = var4.getDocument();
               String var6 = var5.getText(0, var5.getLength());
               if (var5.getLength() == 0) {
                  return;
               }

               boolean var7 = var4.getSelectionStart() - var4.getSelectionEnd() != 0;
               boolean var8 = ENYKABEVNumberFormatter.this.unMask(var5.getText(var2, var3)).length() > 0;
               boolean var9 = var4.getCaretPosition() > var2;
               int var10 = var7 ? 0 : (var8 ? 0 : (var9 ? -1 : 0));
               String var11 = ENYKABEVNumberFormatter.this.unMask(var5.getText(0, var5.getLength()));
               int var12 = var11.length();
               int var13 = var12 - ENYKABEVNumberFormatter.this.unMask(var6.substring(var2)).length() + var10;
               int var14 = var7 ? var12 - var13 - ENYKABEVNumberFormatter.this.unMask(var6.substring(var2 + var3)).length() : var3;
               int var15 = var12 - ENYKABEVNumberFormatter.this.unMask(var6.substring(var4.getCaretPosition())).length();
               var13 = var13 < 0 ? 0 : var13;
               var14 = var13 + var14 > var11.length() ? var11.length() - var13 : var14;
               if (var14 <= 0) {
                  return;
               }

               String var16 = var11.substring(0, var13) + var11.substring(var13 + var14);
               String var17 = var16.replaceAll("0", "").trim();
               if (!var17.equals("") && !var17.equals("-") && !var17.equals("+") && !var17.equals(",") && !var17.equals("-,") && !var17.equals("+,")) {
                  var15 = var15 > var13 ? var15 - var14 : var15;
                  var17 = this.getNumber(var16, this.signs);
                  String var18 = this.getSign(var16, this.signs);
                  if (!var17.startsWith("0,")) {
                     while(var17.startsWith("0")) {
                        var17 = var17.substring(1);
                     }
                  }

                  var17 = var18 + var17;
                  String var19 = ENYKABEVNumberFormatter.this.applyMask(var17);
                  super.remove(var1, 0, var5.getLength());
                  super.insertString(var1, 0, var19, (AttributeSet)null);
                  int var20 = var17.indexOf(",");
                  String var21 = var20 < 0 ? "" : (var20 <= var15 ? "," : "");
                  int var22 = var19.length() - ENYKABEVNumberFormatter.this.applyMask(var21 + var17.substring(var15)).length();
                  var22 += var21.length() > 0 ? ",".length() : 0;
                  var22 = Math.max(0, Math.min(var5.getLength(), var22));
                  var4.setCaretPosition(var22);
               } else {
                  super.remove(var1, 0, var5.getLength());
                  var4.setCaretPosition(0);
               }

               var4.commitEdit();
            } catch (Exception var26) {
               var26.printStackTrace();
            } finally {
               ENYKABEVNumberFormatter.this.setEditValid(true);
            }

         }
      }

      public synchronized void replace(FilterBypass var1, int var2, int var3, String var4, AttributeSet var5) throws BadLocationException {
         JFormattedTextField var6 = ENYKABEVNumberFormatter.this.getFormattedTextField();
         this.remove(var1, var2, var3);
         this.insertString(var1, Math.min(var2, var6.getDocument().getLength()), var4, var5);
      }

      // $FF: synthetic method
      ENYKNumberDocumentFilter(Object var2) {
         this();
      }
   }
}
