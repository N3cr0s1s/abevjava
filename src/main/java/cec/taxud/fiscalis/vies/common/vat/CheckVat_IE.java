package cec.taxud.fiscalis.vies.common.vat;

public class CheckVat_IE implements ValidationRoutine {
   static final int LENGTH_9 = 9;
   static final int LENGTH_8 = 8;
   static final String letterToNumberC9 = "ABCDEFGHI";

   private boolean checkSumOk(String var1, int var2) {
      int var3 = 0;

      int var4;
      for(var4 = 0; var4 < 7; ++var4) {
         var3 += (8 - var4) * StringUtils.digitAt(var1, var4);
      }

      if (var2 == 9) {
         var3 += 9 * ("ABCDEFGHI".indexOf(var1.substring(8)) + 1);
      }

      var4 = var3 % 23;
      return "WABCDEFGHIJKLMNOPQRSTUV".charAt(var4) == var1.charAt(7);
   }

   private boolean checkOldSumOk(String var1) {
      int var2 = 0;

      int var3;
      for(var3 = 0; var3 < 7; ++var3) {
         var2 += (8 - var3) * StringUtils.digitAt(var1, var3);
      }

      var3 = var2 % 23;
      return "WABCDEFGHIJKLMNOPQRSTUV".charAt(var3) == var1.charAt(var1.length() - 1);
   }

   private boolean checkOldStyle(String var1, char var2, char var3, char var4) {
      if ((Character.isLetter(var3) || var3 == '+' || var3 == '*') && var2 >= '7') {
         String var5 = "0" + var1 + var2 + var4;
         return this.checkOldSumOk(var5);
      } else {
         return false;
      }
   }

   public boolean check(String var1) {
      if (var1.length() < 8) {
         return false;
      } else {
         char var2 = var1.charAt(0);
         char var3 = var1.charAt(1);
         char var4;
         String var5;
         if (var1.length() == 8) {
            var4 = var1.charAt(7);
            var5 = var1.substring(2, 7);
            if (Character.isDigit(var2) && StringUtils.isNum(var5) && Character.isLetter(var4)) {
               return !Character.isDigit(var3) ? this.checkOldStyle(var5, var2, var3, var4) : this.checkSumOk(var1, 8);
            } else {
               return false;
            }
         } else if (var1.length() == 9) {
            var4 = var1.charAt(8);
            var5 = var1.substring(2, 7);
            if (Character.isDigit(var2) && StringUtils.isNum(var5) && Character.isLetter(var4)) {
               return Character.isDigit(var3) ? this.checkSumOk(var1, 9) : false;
            } else {
               return false;
            }
         } else {
            return false;
         }
      }
   }
}
