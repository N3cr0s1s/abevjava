package cec.taxud.fiscalis.vies.common.vat;

public class CheckVat_BE implements ValidationRoutine {
   static final int LENGTH = 10;

   private boolean checkSum(String var1) {
      int var2 = StringUtils.substrToInt(var1, 0, 8);
      int var3 = StringUtils.substrToInt(var1, 8, 10);
      return 97 - var2 % 97 == var3;
   }

   public boolean check(String var1) {
      if (var1.length() != 10) {
         return false;
      } else {
         return (var1.charAt(0) == '0' || var1.charAt(0) == '1') && StringUtils.isNum(var1) && this.checkSum(var1);
      }
   }
}
