package cec.taxud.fiscalis.vies.common.vat;

public class CheckVat_AT implements ValidationRoutine {
   static final int LENGTH = 9;

   private boolean checkSumOk(String var1) {
      int var2 = 4;

      int var3;
      for(var3 = 2; var3 <= 6; var3 += 2) {
         int var4 = 2 * StringUtils.digitAt(var1, var3);
         if (var4 >= 10) {
            var4 -= 9;
         }

         var2 += var4;
      }

      for(var3 = 1; var3 <= 7; var3 += 2) {
         var2 += StringUtils.digitAt(var1, var3);
      }

      var3 = var2 % 10;
      if (var3 != 0) {
         var3 = 10 - var3;
      }

      return StringUtils.digitAt(var1, 8) == var3;
   }

   public boolean check(String var1) {
      return var1.length() == 9 && var1.startsWith("U") && StringUtils.isNum(var1.substring(1)) && this.checkSumOk(var1);
   }
}
