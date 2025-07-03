package cec.taxud.fiscalis.vies.common.vat;

public class CheckVat_MT implements ValidationRoutine {
   static final int LENGTH = 8;

   private boolean checkSumOk(String var1) {
      int var2 = 3 * StringUtils.digitAt(var1, 0) + 4 * StringUtils.digitAt(var1, 1) + 6 * StringUtils.digitAt(var1, 2) + 7 * StringUtils.digitAt(var1, 3) + 8 * StringUtils.digitAt(var1, 4) + 9 * StringUtils.digitAt(var1, 5);
      var2 = 37 - var2 % 37;
      if (var2 == 0) {
         var2 = 37;
      }

      int var3 = StringUtils.substrToInt(var1, 6, 8);
      return var3 == var2;
   }

   public boolean check(String var1) {
      return var1.length() == 8 && var1.charAt(0) != '0' && !var1.startsWith("100000") && StringUtils.isNum(var1) && this.checkSumOk(var1);
   }
}
