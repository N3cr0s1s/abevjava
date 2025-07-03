package cec.taxud.fiscalis.vies.common.vat;

public class CheckVat_PL implements ValidationRoutine {
   static final int LENGTH = 10;

   private boolean checkSumOk(String var1) {
      int var2 = 6 * StringUtils.digitAt(var1, 0) + 5 * StringUtils.digitAt(var1, 1) + 7 * StringUtils.digitAt(var1, 2) + 2 * StringUtils.digitAt(var1, 3) + 3 * StringUtils.digitAt(var1, 4) + 4 * StringUtils.digitAt(var1, 5) + 5 * StringUtils.digitAt(var1, 6) + 6 * StringUtils.digitAt(var1, 7) + 7 * StringUtils.digitAt(var1, 8);
      var2 %= 11;
      if (var2 == 10) {
         return false;
      } else {
         return StringUtils.digitAt(var1, 9) == var2;
      }
   }

   public boolean check(String var1) {
      return var1.length() == 10 && StringUtils.isNum(var1) && this.checkSumOk(var1);
   }
}
