package cec.taxud.fiscalis.vies.common.vat;

public class CheckVat_EU implements ValidationRoutine {
   static final int LENGTH = 9;
   static final int FROM = 0;
   static final int LENGTH_ISO = 3;

   private boolean checkSumOk(String var1) {
      int var2 = 0;

      int var3;
      for(var3 = 0; var3 < 8; ++var3) {
         var2 += (8 - var3) * StringUtils.digitAt(var1, var3);
      }

      var3 = var2 % 11;
      int var4 = StringUtils.digitAt(var1, 8);
      return var3 == 10 && var4 == 1 || var3 != 10 && var4 == var3;
   }

   public boolean check(String var1) {
      return var1.length() == 9 && StringUtils.isNum(var1) && ISOUtils.isISO3166(var1.substring(0, 3)) && this.checkSumOk(var1);
   }
}
