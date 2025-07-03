package cec.taxud.fiscalis.vies.common.vat;

public class CheckVat_FI implements ValidationRoutine {
   static final int LENGTH = 8;

   private boolean checkSumOk(String var1) {
      int var2 = 7 * StringUtils.digitAt(var1, 0) + 9 * StringUtils.digitAt(var1, 1) + 10 * StringUtils.digitAt(var1, 2) + 5 * StringUtils.digitAt(var1, 3) + 8 * StringUtils.digitAt(var1, 4) + 4 * StringUtils.digitAt(var1, 5) + 2 * StringUtils.digitAt(var1, 6);
      int var3 = var2 % 11;
      if (var3 == 1) {
         return false;
      } else {
         if (var3 > 0) {
            var3 = 11 - var3;
         }

         return StringUtils.digitAt(var1, 7) == var3;
      }
   }

   public boolean check(String var1) {
      return var1.length() == 8 && StringUtils.isNum(var1) && this.checkSumOk(var1);
   }
}
