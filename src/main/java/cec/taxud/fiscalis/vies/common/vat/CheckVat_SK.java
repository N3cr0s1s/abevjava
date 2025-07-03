package cec.taxud.fiscalis.vies.common.vat;

public class CheckVat_SK implements ValidationRoutine {
   static final int LENGTH = 10;

   private boolean checkSumOk(String var1) {
      if (var1.charAt(0) != '0' && "234789".indexOf(var1.charAt(2)) != -1) {
         long var2 = (long)StringUtils.substrToInt(var1, 0, 3);
         long var4 = var2 % 11L;
         var4 = var4 * 10000000L + (long)StringUtils.substrToInt(var1, 3);
         return var4 % 11L == 0L;
      } else {
         return false;
      }
   }

   public boolean check(String var1) {
      return var1.length() == 10 && StringUtils.isNum(var1) && this.checkSumOk(var1);
   }
}
