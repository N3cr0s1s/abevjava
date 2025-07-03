package hu.piller.enykp.util.validation;

public class ValidationUtilityVPOP extends ValidationUtilityCommon {
   public static boolean isVPid(String var0) {
      int[] var2 = new int[]{1, 3, 7, 9, 1, 3, 7, 9};
      boolean var1;
      if (var0 != null && var0.length() == 12 && var0.substring(0, 3).toUpperCase().equals("HU0")) {
         String var3 = var0.substring(3, var0.length() - 1);
         if (isStrNumber(var3)) {
            int var4 = 0;

            int var5;
            for(var5 = 0; var5 < var3.length(); ++var5) {
               var4 += charToNumber(var3.charAt(var5)) * var2[var5];
            }

            var5 = var4 % 10;
            if (charToNumber(var0.charAt(11)) == var5) {
               var1 = true;
            } else {
               var1 = false;
            }
         } else {
            var1 = false;
         }
      } else {
         var1 = false;
      }

      return var1;
   }

   public static boolean isGln(String var0) {
      boolean var1;
      if (var0 != null && var0.length() == 13 && isStrNumber(var0)) {
         int var2 = 0;
         boolean var3 = true;

         int var4;
         for(var4 = 0; var4 < var0.length() - 1; ++var4) {
            if (var3) {
               var2 += charToNumber(var0.charAt(var4));
               var3 = false;
            } else {
               var2 += charToNumber(var0.charAt(var4)) * 3;
               var3 = true;
            }
         }

         var4 = 10 - var2 % 10;
         if (var4 == 10) {
            var4 = 0;
         }

         if (charToNumber(var0.charAt(var0.length() - 1)) == var4) {
            var1 = true;
         } else {
            var1 = false;
         }
      } else {
         var1 = false;
      }

      return var1;
   }

   public static boolean isRegSzam(String var0) {
      int[] var2 = new int[]{9, 7, 3, 1, 9, 7, 3, 1, 9};
      boolean var1;
      if (var0 != null && var0.length() == 10 && isStrNumber(var0) && charToNumber(var0.charAt(0)) >= 1) {
         int var3 = 0;

         int var4;
         for(var4 = 0; var4 < var0.length() - 1; ++var4) {
            var3 += charToNumber(var0.charAt(var4)) * var2[var4];
         }

         var4 = 9 - var3 % 10;
         if (charToNumber(var0.charAt(9)) == var4) {
            var1 = true;
         } else {
            var1 = false;
         }
      } else {
         var1 = false;
      }

      return var1;
   }

   public static boolean isEngedelySzam(String var0) {
      boolean var1 = true;
      if (var0.startsWith("HU") && var0.length() == 8) {
         try {
            Integer.parseInt(var0.substring(2));
         } catch (NumberFormatException var3) {
            var1 = false;
         }
      } else {
         var1 = false;
      }

      return var1;
   }
}
