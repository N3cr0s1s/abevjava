package cec.taxud.fiscalis.vies.common.vat;

public class CheckVat_IT implements ValidationRoutine {
   static final int LENGTH = 11;

   private boolean checkSumOk(String var1) {
      int var2 = StringUtils.substrToInt(var1, 0, 7);
      if (var2 == 0) {
         return false;
      } else {
         int var3 = (StringUtils.substrToInt(var1, 7) - StringUtils.digitAt(var1, 10)) / 10;
         if (var3 != 120 && var3 != 121 && var3 != 999 && var3 != 888 && (var3 > 100 || var3 < 1)) {
            return false;
         } else {
            int var4 = StringUtils.digitAt(var1, 10);
            int var5 = 0;
            int var6 = 0;

            int var7;
            for(var7 = 1; var7 < 10; var7 += 2) {
               int var8 = 2 * StringUtils.digitAt(var1, var7);
               if (var8 > 9) {
                  var8 -= 9;
               }

               var5 += var8;
               var6 += StringUtils.digitAt(var1, var7 - 1);
            }

            var7 = (var5 + var6) % 10;
            if (var7 > 0) {
               var7 = 10 - var7;
            }

            return var7 == var4;
         }
      }
   }

   public boolean check(String var1) {
      return var1.length() == 11 && StringUtils.isNum(var1) && this.checkSumOk(var1);
   }
}
