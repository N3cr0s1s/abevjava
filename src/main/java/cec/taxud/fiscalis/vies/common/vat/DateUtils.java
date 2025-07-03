package cec.taxud.fiscalis.vies.common.vat;

public class DateUtils {
   private static final int JANUARY = 1;
   private static final int FEBRUARY = 2;
   private static final int DECEMBER = 12;

   private static boolean isLeapYear(int var0) {
      return var0 % 4 == 0 && var0 % 100 != 0 || var0 % 400 == 0;
   }

   private static int getLastDayOfMonth(int var0, int var1) {
      int[] var2 = new int[]{0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
      return var0 == 2 && isLeapYear(var1) ? 29 : var2[var0];
   }

   public static boolean validate(int var0, int var1, int var2) {
      if (var1 >= 1 && var1 <= 12) {
         return var2 >= 1 && var2 <= getLastDayOfMonth(var1, var0);
      } else {
         return false;
      }
   }
}
