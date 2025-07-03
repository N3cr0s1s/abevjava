package cec.taxud.fiscalis.vies.common.vat;

public class CheckVat_BG implements ValidationRoutine {
   static final int LENGTH_LEGAL_ENTITIES = 9;
   static final int LENGTH_OTHER_ENTITIES = 10;

   private boolean checkSumAlgo1Ok(String var1) {
      int var2 = 1 * StringUtils.digitAt(var1, 0) + 2 * StringUtils.digitAt(var1, 1) + 3 * StringUtils.digitAt(var1, 2) + 4 * StringUtils.digitAt(var1, 3) + 5 * StringUtils.digitAt(var1, 4) + 6 * StringUtils.digitAt(var1, 5) + 7 * StringUtils.digitAt(var1, 6) + 8 * StringUtils.digitAt(var1, 7);
      var2 %= 11;
      if (var2 == 10) {
         var2 = 3 * StringUtils.digitAt(var1, 0) + 4 * StringUtils.digitAt(var1, 1) + 5 * StringUtils.digitAt(var1, 2) + 6 * StringUtils.digitAt(var1, 3) + 7 * StringUtils.digitAt(var1, 4) + 8 * StringUtils.digitAt(var1, 5) + 9 * StringUtils.digitAt(var1, 6) + 10 * StringUtils.digitAt(var1, 7);
         var2 %= 11;
         if (var2 == 10) {
            var2 = 0;
         }

         return var2 == StringUtils.digitAt(var1, 8);
      } else {
         return var2 == StringUtils.digitAt(var1, 8);
      }
   }

   private boolean checkSumAlgo2Ok(String var1) {
      int var2 = 10 * StringUtils.digitAt(var1, 0) + StringUtils.digitAt(var1, 1);
      int var3 = 10 * StringUtils.digitAt(var1, 2) + StringUtils.digitAt(var1, 3);
      if (var3 >= 21 && var3 <= 32) {
         var2 += 1800;
         var3 -= 20;
      } else if (var3 >= 41 && var3 <= 52) {
         var2 += 2000;
         var3 -= 40;
      } else {
         if (var3 < 1 || var3 > 12) {
            return false;
         }

         var2 += 1900;
      }

      int var4 = 10 * StringUtils.digitAt(var1, 4) + StringUtils.digitAt(var1, 5);
      if (!DateUtils.validate(var2, var3, var4)) {
         return false;
      } else {
         int var5 = 2 * StringUtils.digitAt(var1, 0) + 4 * StringUtils.digitAt(var1, 1) + 8 * StringUtils.digitAt(var1, 2) + 5 * StringUtils.digitAt(var1, 3) + 10 * StringUtils.digitAt(var1, 4) + 9 * StringUtils.digitAt(var1, 5) + 7 * StringUtils.digitAt(var1, 6) + 3 * StringUtils.digitAt(var1, 7) + 6 * StringUtils.digitAt(var1, 8);
         var5 %= 11;
         if (var5 == 10) {
            var5 = 0;
         }

         return var5 == StringUtils.digitAt(var1, 9);
      }
   }

   private boolean checkSumAlgo3Ok(String var1) {
      int var2 = 21 * StringUtils.digitAt(var1, 0) + 19 * StringUtils.digitAt(var1, 1) + 17 * StringUtils.digitAt(var1, 2) + 13 * StringUtils.digitAt(var1, 3) + 11 * StringUtils.digitAt(var1, 4) + 9 * StringUtils.digitAt(var1, 5) + 7 * StringUtils.digitAt(var1, 6) + 3 * StringUtils.digitAt(var1, 7) + 1 * StringUtils.digitAt(var1, 8);
      var2 %= 10;
      return var2 == StringUtils.digitAt(var1, 9);
   }

   private boolean checkSumAlgo4Ok(String var1) {
      int var2 = 4 * StringUtils.digitAt(var1, 0) + 3 * StringUtils.digitAt(var1, 1) + 2 * StringUtils.digitAt(var1, 2) + 7 * StringUtils.digitAt(var1, 3) + 6 * StringUtils.digitAt(var1, 4) + 5 * StringUtils.digitAt(var1, 5) + 4 * StringUtils.digitAt(var1, 6) + 3 * StringUtils.digitAt(var1, 7) + 2 * StringUtils.digitAt(var1, 8);
      var2 = 11 - var2 % 11;
      if (var2 == 11) {
         var2 = 0;
      }

      if (var2 == 10) {
         return false;
      } else {
         return var2 == StringUtils.digitAt(var1, 9);
      }
   }

   public boolean check(String var1) {
      if (!StringUtils.isNum(var1)) {
         return false;
      } else if (var1.length() != 9 && var1.length() != 10) {
         return false;
      } else if (var1.length() == 9) {
         return this.checkSumAlgo1Ok(var1);
      } else if (this.checkSumAlgo2Ok(var1)) {
         return true;
      } else if (this.checkSumAlgo3Ok(var1)) {
         return true;
      } else {
         return this.checkSumAlgo4Ok(var1);
      }
   }
}
