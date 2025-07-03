package cec.taxud.fiscalis.vies.common.vat;

public class CheckVat_CY implements ValidationRoutine {
   static final int LENGTH = 9;

   private int compute(int var1) {
      int[] var2 = new int[]{1, 0, 5, 7, 9, 13, 15, 17, 19, 21};
      return var2[var1];
   }

   private char computeCheckDigit(int var1) {
      return var1 >= 0 && var1 <= "ABCDEFGHIJKLMNOPQRSTUVWXYZ".length() - 1 ? "ABCDEFGHIJKLMNOPQRSTUVWXYZ".charAt(var1) : '?';
   }

   private boolean checkSumOk(String var1, char var2) {
      if (StringUtils.digitAt(var1, 0) == 1 && StringUtils.digitAt(var1, 1) == 2) {
         return false;
      } else {
         int var3 = this.compute(StringUtils.digitAt(var1, 0)) + StringUtils.digitAt(var1, 1) + this.compute(StringUtils.digitAt(var1, 2)) + StringUtils.digitAt(var1, 3) + this.compute(StringUtils.digitAt(var1, 4)) + StringUtils.digitAt(var1, 5) + this.compute(StringUtils.digitAt(var1, 6)) + StringUtils.digitAt(var1, 7);
         var3 %= 26;
         return this.computeCheckDigit(var3) == var2;
      }
   }

   public boolean check(String var1) {
      if (var1.length() != 9) {
         return false;
      } else {
         char var2 = var1.charAt(8);
         if (!Character.isLetter(var2)) {
            return false;
         } else {
            String var3 = var1.substring(0, 8);
            return StringUtils.isNum(var3) && "01234569".indexOf(var1.charAt(0)) != -1 ? this.checkSumOk(var3, var2) : false;
         }
      }
   }
}
