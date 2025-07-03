package cec.taxud.fiscalis.vies.common.vat;

public class CheckVat_EL implements ValidationRoutine {
   static final int LENGTH = 9;

   private boolean checkSumOk(String var1) {
      if (Integer.parseInt(var1) == 0) {
         return false;
      } else {
         int var2 = 0;

         int var3;
         for(var3 = 0; var3 < 8; ++var3) {
            var2 = (int)((double)var2 + Math.pow(2.0D, (double)(8 - var3)) * (double)StringUtils.digitAt(var1, var3));
         }

         var3 = var2 % 11;
         if (var3 == 10) {
            var3 = 0;
         }

         return var3 == StringUtils.digitAt(var1, 8);
      }
   }

   public boolean check(String var1) {
      return var1.length() == 9 && StringUtils.isNum(var1) && this.checkSumOk(var1);
   }
}
