package cec.taxud.fiscalis.vies.common.vat;

public class CheckVat_FR implements ValidationRoutine {
   static final int LENGTH = 11;
   static final String checkChars = "0123456789ABCDEFGHJKLMNPQRSTUVWXYZ";

   private boolean checkSumOk(String var1) {
      int var2 = StringUtils.substrToInt(var1, 2, 11);
      if (var2 == 0) {
         return false;
      } else {
         String var3 = var1.substring(0, 2);
         String var4 = var1.substring(2);
         if (StringUtils.isNum(var3)) {
            double var15 = Double.parseDouble(var3);
            double var16 = Double.parseDouble(var4 + "12");
            return var16 % 97.0D == var15;
         } else {
            long var5 = (long)"0123456789ABCDEFGHJKLMNPQRSTUVWXYZ".indexOf(var1.charAt(0));
            long var7 = (long)"0123456789ABCDEFGHJKLMNPQRSTUVWXYZ".indexOf(var1.charAt(1));
            long var9 = 0L;
            if (var5 > 9L) {
               var9 = 34L * var5 + var7 - 100L;
            } else {
               var9 = 24L * var5 + var7 - 10L;
            }

            double var11 = (double)(var9 % 11L);
            double var13 = Double.parseDouble(var1.substring(2));
            var13 += (double)(var9 / 11L + 1L);
            return var13 % 11.0D == var11;
         }
      }
   }

   public boolean check(String var1) {
      return var1.length() == 11 && StringUtils.isNum(var1.substring(2)) && "0123456789ABCDEFGHJKLMNPQRSTUVWXYZ".indexOf(var1.charAt(0)) != -1 && "0123456789ABCDEFGHJKLMNPQRSTUVWXYZ".indexOf(var1.charAt(1)) != -1 && this.checkSumOk(var1);
   }
}
