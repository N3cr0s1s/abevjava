package hu.piller.enykp.util.validation;

public class ValidationUtilityCommon {
   protected static String extractNumbers(String var0) {
      StringBuffer var1 = new StringBuffer("");

      for(int var2 = 0; var0 != null && var2 < var0.length(); ++var2) {
         if (charToNumber(var0.charAt(var2)) != -1) {
            var1.append(var0.charAt(var2));
         }
      }

      return var1.toString();
   }

   protected static int charToNumber(char var0) {
      byte var1;
      switch(var0) {
      case '0':
         var1 = 0;
         break;
      case '1':
         var1 = 1;
         break;
      case '2':
         var1 = 2;
         break;
      case '3':
         var1 = 3;
         break;
      case '4':
         var1 = 4;
         break;
      case '5':
         var1 = 5;
         break;
      case '6':
         var1 = 6;
         break;
      case '7':
         var1 = 7;
         break;
      case '8':
         var1 = 8;
         break;
      case '9':
         var1 = 9;
         break;
      default:
         var1 = -1;
      }

      return var1;
   }

   protected static boolean isStrNumber(String var0) {
      boolean var1;
      try {
         Long.parseLong(var0);
         var1 = true;
      } catch (NumberFormatException var3) {
         var1 = false;
      }

      return var1;
   }
}
