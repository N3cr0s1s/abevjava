package cec.taxud.fiscalis.vies.common.vat;

import java.util.GregorianCalendar;

public class CheckVat_CZ implements ValidationRoutine {
   static final int LENGTH_1 = 8;
   static final int LENGTH_2 = 9;
   static final int LENGTH_3 = 10;

   private boolean checkRoutine1(String var1) {
      if (var1.charAt(0) == '9') {
         return false;
      } else {
         double var2 = 0.0D;

         for(int var4 = 0; var4 < 7; ++var4) {
            var2 += (double)((8 - var4) * StringUtils.digitAt(var1, var4));
         }

         var2 = Math.ceil(var2 / 11.0D) * 11.0D - var2;
         if (var2 == 0.0D) {
            var2 = 1.0D;
         }

         if (var2 == 10.0D) {
            var2 = 0.0D;
         }

         return (double)StringUtils.digitAt(var1, 7) == var2;
      }
   }

   private boolean checkRoutine2(String var1) {
      int var2 = StringUtils.substrToInt(var1, 0, 2);
      int var3 = StringUtils.substrToInt(var1, 2, 4);
      int var4 = StringUtils.substrToInt(var1, 4, 6);
      if (var2 > 53) {
         return false;
      } else {
         if (var3 > 50) {
            var3 -= 50;
         }

         return DateUtils.validate(var2, var3, var4);
      }
   }

   private boolean checkRoutine3(String var1) {
      double var2 = 0.0D;

      int var4;
      for(var4 = 0; var4 < 7; ++var4) {
         var2 += (double)((8 - var4) * StringUtils.digitAt(var1, var4 + 1));
      }

      var2 = Math.ceil(var2 / 11.0D) * 11.0D - var2;
      var4 = (int)var2;
      int[] var5 = new int[]{8, 8, 7, 6, 5, 4, 3, 2, 1, 0, 9, 8};
      if (var4 >= 0 && var4 <= 11) {
         return StringUtils.digitAt(var1, 8) == var5[var4];
      } else {
         return false;
      }
   }

   private boolean checkRoutine4(String var1) {
      int var2 = StringUtils.substrToInt(var1, 0, 2);
      int var3 = StringUtils.substrToInt(var1, 2, 4);
      int var4 = StringUtils.substrToInt(var1, 4, 6);
      GregorianCalendar var5 = new GregorianCalendar();
      int var6 = var5.get(1);
      if (var2 < 54 && var6 < var2 + 2000) {
         return false;
      } else {
         int var7 = var2 + var3 + var4 + StringUtils.substrToInt(var1, 6, 8) + StringUtils.substrToInt(var1, 8, 10);
         var7 %= 11;
         var3 -= var3 > 50 ? 50 : 0;
         var3 -= var3 >= 21 && var3 <= 32 ? 20 : 0;
         var2 += var2 > 53 ? 1900 : 2000;
         return DateUtils.validate(var2, var3, var4) && var7 == 0;
      }
   }

   public boolean check(String var1) {
      if (!StringUtils.isNum(var1)) {
         return false;
      } else {
         switch(var1.length()) {
         case 8:
            return this.checkRoutine1(var1);
         case 9:
            if (var1.charAt(0) != '6') {
               return this.checkRoutine2(var1);
            }

            return this.checkRoutine3(var1);
         case 10:
            return this.checkRoutine4(var1);
         default:
            return false;
         }
      }
   }
}
