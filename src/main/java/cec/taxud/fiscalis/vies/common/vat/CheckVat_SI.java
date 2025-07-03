package cec.taxud.fiscalis.vies.common.vat;

public class CheckVat_SI implements ValidationRoutine {
   static final int LENGTH = 8;

   private boolean checkSumOk(String var1) {
      int var2 = 0;

      int var3;
      for(var3 = 0; var3 < 7; ++var3) {
         var2 += (8 - var3) * StringUtils.digitAt(var1, var3);
      }

      var3 = 11 - var2 % 11;
      int var4 = StringUtils.digitAt(var1, 7);
      return (var3 != 10 || var4 == 0) && (var3 == 10 || var3 != 11 && var4 == var3);
   }

   public boolean check(String var1) {
      return var1.length() == 8 && StringUtils.isNum(var1) && StringUtils.digitAt(var1, 0) > 0 && this.checkSumOk(var1);
   }
}
