package cec.taxud.fiscalis.vies.common.vat;

public class CheckVat_LV implements ValidationRoutine {
   static final int LENGTH = 11;

   private boolean checkRoutine1Ok(String var1) {
      int var2 = 9 * StringUtils.digitAt(var1, 0) + 1 * StringUtils.digitAt(var1, 1) + 4 * StringUtils.digitAt(var1, 2) + 8 * StringUtils.digitAt(var1, 3) + 3 * StringUtils.digitAt(var1, 4) + 10 * StringUtils.digitAt(var1, 5) + 2 * StringUtils.digitAt(var1, 6) + 5 * StringUtils.digitAt(var1, 7) + 7 * StringUtils.digitAt(var1, 8) + 6 * StringUtils.digitAt(var1, 9);
      var2 = 3 - var2 % 11;
      if (var2 < -1) {
         var2 += 11;
      } else if (var2 == -1) {
         return false;
      }

      return StringUtils.digitAt(var1, 10) == var2;
   }

   private boolean checkRoutine2Ok(String var1) {
      int var2 = StringUtils.substrToInt(var1, 0, 2);
      int var3 = StringUtils.substrToInt(var1, 2, 4);
      int var4 = StringUtils.substrToInt(var1, 4, 6);
      var4 += var4 > 15 ? 1900 : 2000;
      return DateUtils.validate(var4, var3, var2);
   }

   private boolean checkRoutine3Ok(String var1) {
      return StringUtils.digitAt(var1, 0) == 3 && StringUtils.digitAt(var1, 1) == 2;
   }

   private boolean checkSumOk(String var1) {
      if (StringUtils.digitAt(var1, 0) > 3) {
         return this.checkRoutine1Ok(var1);
      } else {
         return this.checkRoutine2Ok(var1) || this.checkRoutine3Ok(var1);
      }
   }

   public boolean check(String var1) {
      return var1.length() == 11 && StringUtils.isNum(var1) && this.checkSumOk(var1);
   }
}
