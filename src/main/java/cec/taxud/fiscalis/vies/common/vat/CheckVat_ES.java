package cec.taxud.fiscalis.vies.common.vat;

public class CheckVat_ES implements ValidationRoutine {
   static final int LENGTH = 9;

   public int modFor1and2(String var1) {
      int var2 = 0;

      for(int var3 = 0; var3 <= 6; var3 += 2) {
         int var4 = 2 * StringUtils.digitAt(var1, var3);
         if (var4 >= 10) {
            var4 -= 9;
         }

         var2 += var4;
      }

      return (StringUtils.digitAt(var1, 1) + StringUtils.digitAt(var1, 3) + StringUtils.digitAt(var1, 5) + var2) % 10;
   }

   public boolean checkRoutine1(String var1) {
      int var2 = this.modFor1and2(var1);
      if (var2 > 0) {
         var2 = 10 - var2;
      }

      return var2 == StringUtils.digitAt(var1, var1.length() - 1);
   }

   public boolean checkRoutine2(String var1, char var2) {
      int var3 = Integer.parseInt(var1);
      int var4 = var3 % 23;
      return "TRWAGMYFPDXBNJZSQVHLCKE".charAt(var4) == var2;
   }

   public boolean checkRoutine3(String var1, char var2) {
      int var3 = 9 - this.modFor1and2(var1);
      return "ABCDEFGHIJ".charAt(var3) == var2;
   }

   public boolean lastCharIsDigitOk(char var1, char var2, String var3) {
      return "ABCDEFGHJUV".indexOf(var1) != -1 ? this.checkRoutine1(var3 + var2) : false;
   }

   public boolean lastCharIsAlphaOk(char var1, char var2, String var3) {
      if ("ABCDEFGHNPQRSW".indexOf(var1) != -1) {
         return this.checkRoutine3(var3, var2);
      } else {
         if ("Y".indexOf(var1) != -1) {
            var1 = '1';
         }

         if ("Z".indexOf(var1) != -1) {
            var1 = '2';
         }

         if (Character.isDigit(var1)) {
            return this.checkRoutine2(var1 + var3, var2);
         } else {
            return "KLMX".indexOf(var1) != -1 ? this.checkRoutine2(var3, var2) : false;
         }
      }
   }

   public boolean checkSumOk(char var1, char var2, String var3) {
      return Character.isDigit(var2) ? this.lastCharIsDigitOk(var1, var2, var3) : this.lastCharIsAlphaOk(var1, var2, var3);
   }

   public boolean check(String var1) {
      if (var1.length() != 9) {
         return false;
      } else {
         char var2 = var1.charAt(0);
         char var3 = var1.charAt(8);
         String var4 = var1.substring(1, 8);
         return StringUtils.isNum(var4) && Integer.parseInt(var4) != 0 && this.checkSumOk(var2, var3, var4);
      }
   }
}
