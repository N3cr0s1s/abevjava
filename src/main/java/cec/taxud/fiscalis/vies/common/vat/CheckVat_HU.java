package cec.taxud.fiscalis.vies.common.vat;

public class CheckVat_HU implements ValidationRoutine {
   static final int LENGTH = 8;

   private boolean checkSumOk(String var1) {
      double var2 = (double)(9 * StringUtils.digitAt(var1, 0) + 7 * StringUtils.digitAt(var1, 1) + 3 * StringUtils.digitAt(var1, 2) + 1 * StringUtils.digitAt(var1, 3) + 9 * StringUtils.digitAt(var1, 4) + 7 * StringUtils.digitAt(var1, 5) + 3 * StringUtils.digitAt(var1, 6));
      if (var2 == 0.0D) {
         return false;
      } else {
         var2 = Math.ceil(var2 / 10.0D) * 10.0D - var2;
         return (double)StringUtils.digitAt(var1, 7) == var2;
      }
   }

   public boolean check(String var1) {
      return var1.length() == 8 && StringUtils.isNum(var1) && this.checkSumOk(var1);
   }
}
