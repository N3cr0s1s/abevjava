package cec.taxud.fiscalis.vies.common.vat;

public class StringUtils {
   public static String rightJustify(String var0, int var1, char var2) {
      StringBuffer var3 = new StringBuffer(var0);

      while(var3.length() < var1) {
         var3.insert(0, var2);
      }

      return var3.toString();
   }

   public static int digitAt(String var0, int var1) {
      return Character.digit(var0.charAt(var1), 10);
   }

   public static int substrToInt(String var0, int var1) {
      String var2 = var0.substring(var1);
      return Integer.parseInt(var2);
   }

   public static int substrToInt(String var0, int var1, int var2) {
      String var3 = var0.substring(var1, var2);
      return Integer.parseInt(var3);
   }

   public static boolean isNumericChar(char var0) {
      return var0 >= '0' && var0 <= '9';
   }

   public static boolean isNum(String var0) {
      if (var0 == null) {
         return false;
      } else if (var0.length() == 0) {
         return false;
      } else {
         for(int var1 = 0; var1 != var0.length(); ++var1) {
            if (!isNumericChar(var0.charAt(var1))) {
               return false;
            }
         }

         return true;
      }
   }
}
