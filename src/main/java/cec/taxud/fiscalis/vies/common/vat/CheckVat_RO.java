package cec.taxud.fiscalis.vies.common.vat;

public class CheckVat_RO implements ValidationRoutine {
   static final int LENGTH_MIN = 2;
   static final int LENGTH_MAX = 10;

   private boolean checkSumOk(String var1) {
      String var2 = StringUtils.rightJustify(var1, 10, '0');
      int var3 = 7 * StringUtils.digitAt(var2, 0) + 5 * StringUtils.digitAt(var2, 1) + 3 * StringUtils.digitAt(var2, 2) + 2 * StringUtils.digitAt(var2, 3) + 1 * StringUtils.digitAt(var2, 4) + 7 * StringUtils.digitAt(var2, 5) + 5 * StringUtils.digitAt(var2, 6) + 3 * StringUtils.digitAt(var2, 7) + 2 * StringUtils.digitAt(var2, 8);
      var3 = var3 * 10 % 11;
      if (var3 == 10) {
         var3 = 0;
      }

      return StringUtils.digitAt(var2, 9) == var3;
   }

   public boolean check(String var1) {
      return var1.length() >= 2 && var1.length() <= 10 && StringUtils.isNum(var1) && StringUtils.digitAt(var1, 0) != 0 && this.checkSumOk(var1);
   }
}
