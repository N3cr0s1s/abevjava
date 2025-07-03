package cec.taxud.fiscalis.vies.common.vat;

public class CheckVat_SE implements ValidationRoutine {
   static final int LENGTH = 12;

   private boolean checkSumOk(String var1) {
      int var2 = StringUtils.substrToInt(var1, 10);
      if (var2 >= 1 && var2 <= 94) {
         int var3 = 0;

         int var4;
         int var5;
         for(var5 = 0; var5 <= 8; var5 += 2) {
            var4 = 2 * StringUtils.digitAt(var1, var5);
            if (var4 > 9) {
               var4 -= 9;
            }

            var3 += var4;
         }

         for(var5 = 1; var5 <= 7; var5 += 2) {
            var3 += StringUtils.digitAt(var1, var5);
         }

         var4 = 10 - var3 % 10;
         if (var4 == 10) {
            var4 = 0;
         }

         return StringUtils.digitAt(var1, 9) == var4;
      } else {
         return false;
      }
   }

   public boolean check(String var1) {
      return var1.length() == 12 && StringUtils.isNum(var1) && !var1.endsWith("00") && this.checkSumOk(var1);
   }
}
