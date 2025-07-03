package cec.taxud.fiscalis.vies.common.vat;

public class CheckVat_LU implements ValidationRoutine {
   static final int LENGTH = 8;

   private boolean checkSumOk(String var1) {
      long var2 = (long)StringUtils.substrToInt(var1, 0, 6);
      int var4 = StringUtils.substrToInt(var1, 6);
      return var2 % 89L == (long)var4;
   }

   public boolean check(String var1) {
      return var1.length() == 8 && StringUtils.isNum(var1) && this.checkSumOk(var1);
   }
}
