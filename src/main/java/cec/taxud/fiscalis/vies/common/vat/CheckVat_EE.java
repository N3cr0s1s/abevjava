package cec.taxud.fiscalis.vies.common.vat;

public class CheckVat_EE implements ValidationRoutine {
   static final int LENGTH = 9;

   private boolean checkSumOk(String var1) {
      double var2 = (double)(3 * StringUtils.digitAt(var1, 0) + 7 * StringUtils.digitAt(var1, 1) + 1 * StringUtils.digitAt(var1, 2) + 3 * StringUtils.digitAt(var1, 3) + 7 * StringUtils.digitAt(var1, 4) + 1 * StringUtils.digitAt(var1, 5) + 3 * StringUtils.digitAt(var1, 6) + 7 * StringUtils.digitAt(var1, 7));
      if (var2 == 0.0D) {
         return false;
      } else {
         var2 = Math.ceil(var2 / 10.0D) * 10.0D - var2;
         return (double)StringUtils.digitAt(var1, 8) == var2;
      }
   }

   public boolean check(String var1) {
      return var1.length() == 9 && StringUtils.isNum(var1) && StringUtils.digitAt(var1, 0) == 1 && StringUtils.digitAt(var1, 1) == 0 && this.checkSumOk(var1);
   }
}
