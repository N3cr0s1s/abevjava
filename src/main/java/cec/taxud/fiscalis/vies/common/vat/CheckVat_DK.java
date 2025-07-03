package cec.taxud.fiscalis.vies.common.vat;

public class CheckVat_DK implements ValidationRoutine {
   static final int LENGTH = 8;

   private boolean checkSumOk(String var1) {
      int var2 = 2 * StringUtils.digitAt(var1, 0);

      for(int var3 = 1; var3 < 8; ++var3) {
         var2 += (8 - var3) * StringUtils.digitAt(var1, var3);
      }

      return var2 % 11 == 0;
   }

   public boolean check(String var1) {
      return var1.length() == 8 && StringUtils.isNum(var1) && var1.charAt(0) != '0' && this.checkSumOk(var1);
   }
}
