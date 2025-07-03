package cec.taxud.fiscalis.vies.common.vat;

public class CheckVat_LT implements ValidationRoutine {
   static final int LENGTH1 = 9;
   static final int LENGTH2 = 12;

   private boolean checkSum1Ok(String var1) {
      if (StringUtils.digitAt(var1, 7) != 1) {
         return false;
      } else {
         int var2 = 0;

         for(int var3 = 0; var3 < 8; ++var3) {
            var2 += (var3 + 1) * StringUtils.digitAt(var1, var3);
         }

         var2 %= 11;
         if (var2 == 10) {
            var2 = 3 * StringUtils.digitAt(var1, 0) + 4 * StringUtils.digitAt(var1, 1) + 5 * StringUtils.digitAt(var1, 2) + 6 * StringUtils.digitAt(var1, 3) + 7 * StringUtils.digitAt(var1, 4) + 8 * StringUtils.digitAt(var1, 5) + 9 * StringUtils.digitAt(var1, 6) + 1 * StringUtils.digitAt(var1, 7);
            var2 %= 11;
            if (var2 == 10) {
               var2 = 0;
            }
         }

         return StringUtils.digitAt(var1, 8) == var2;
      }
   }

   private boolean checkSum2Ok(String var1) {
      if (StringUtils.digitAt(var1, 10) != 1) {
         return false;
      } else {
         int var2 = 1 * StringUtils.digitAt(var1, 0) + 2 * StringUtils.digitAt(var1, 1) + 3 * StringUtils.digitAt(var1, 2) + 4 * StringUtils.digitAt(var1, 3) + 5 * StringUtils.digitAt(var1, 4) + 6 * StringUtils.digitAt(var1, 5) + 7 * StringUtils.digitAt(var1, 6) + 8 * StringUtils.digitAt(var1, 7) + 9 * StringUtils.digitAt(var1, 8) + 1 * StringUtils.digitAt(var1, 9) + 2 * StringUtils.digitAt(var1, 10);
         var2 %= 11;
         if (var2 == 10) {
            var2 = 3 * StringUtils.digitAt(var1, 0) + 4 * StringUtils.digitAt(var1, 1) + 5 * StringUtils.digitAt(var1, 2) + 6 * StringUtils.digitAt(var1, 3) + 7 * StringUtils.digitAt(var1, 4) + 8 * StringUtils.digitAt(var1, 5) + 9 * StringUtils.digitAt(var1, 6) + 1 * StringUtils.digitAt(var1, 7) + 2 * StringUtils.digitAt(var1, 8) + 3 * StringUtils.digitAt(var1, 9) + 4 * StringUtils.digitAt(var1, 10);
            var2 %= 11;
            if (var2 == 10) {
               var2 = 0;
            }
         }

         return StringUtils.digitAt(var1, 11) == var2;
      }
   }

   public boolean check(String var1) {
      if (!StringUtils.isNum(var1)) {
         return false;
      } else if (var1.length() == 9) {
         return this.checkSum1Ok(var1);
      } else {
         return var1.length() == 12 ? this.checkSum2Ok(var1) : false;
      }
   }
}
