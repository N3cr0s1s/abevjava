package cec.taxud.fiscalis.vies.common.vat;

public class CheckVat_DE implements ValidationRoutine {
   static final int LENGTH = 9;

   private boolean checkSumOk(String var1) {
      int var2 = StringUtils.digitAt(var1, 0);
      if (var2 == 0) {
         return false;
      } else {
         int var3 = 10;
         boolean var4 = false;

         int var6;
         for(int var5 = 0; var5 < 8; ++var5) {
            var3 += StringUtils.digitAt(var1, var5);
            var6 = var3 % 10;
            if (var6 == 0) {
               var6 = 10;
            }

            var3 = 2 * var6 % 11;
         }

         var6 = 11 - var3;
         if (var6 == 10) {
            var6 = 0;
         }

         return var6 == StringUtils.digitAt(var1, 8);
      }
   }

   public boolean check(String var1) {
      return var1.length() == 9 && StringUtils.isNum(var1) && this.checkSumOk(var1);
   }
}
