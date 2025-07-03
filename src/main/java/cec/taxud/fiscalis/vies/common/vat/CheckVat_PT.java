package cec.taxud.fiscalis.vies.common.vat;

public class CheckVat_PT implements ValidationRoutine {
   static final int LENGTH = 9;

   private boolean checkSumOk(String var1) {
      int var2 = 0;

      int var3;
      for(var3 = 0; var3 < 8; ++var3) {
         var2 += (9 - var3) * StringUtils.digitAt(var1, var3);
      }

      var3 = 11 - var2 % 11;
      if (var3 > 9) {
         var3 = 0;
      }

      return StringUtils.digitAt(var1, 8) == var3;
   }

   public boolean check(String var1) {
      return var1.length() == 9 && StringUtils.isNum(var1) && var1.charAt(0) != '0' && this.checkSumOk(var1);
   }
}
