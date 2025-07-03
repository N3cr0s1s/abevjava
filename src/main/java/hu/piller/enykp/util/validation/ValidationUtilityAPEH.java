package hu.piller.enykp.util.validation;

import hu.piller.enykp.alogic.calculator.abevfunctionset_v1_0.FunctionBodies;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ValidationUtilityAPEH extends ValidationUtilityCommon {
   public static final String REGEXP_EMIAL = "([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})";
   public static final String EU_MEMBER_LIST = "AT|BE|BG|CY|CZ|CH|DE|DK|EE|EL|ES|FI|FR|GB|HU|IE|IT|LT|LU|LV|MT|NL|PL|PT|RO|SE|SI|SK|HR";
   public static final String REGEXP_EU_MEMBER = "(AT|BE|BG|CY|CZ|CH|DE|DK|EE|EL|ES|FI|FR|GB|HU|IE|IT|LT|LU|LV|MT|NL|PL|PT|RO|SE|SI|SK|HR){1}";
   public static final String REGEXP_SWIFT = "[A-Z]{4}(AT|BE|BG|CY|CZ|CH|DE|DK|EE|EL|ES|FI|FR|GB|HU|IE|IT|LT|LU|LV|MT|NL|PL|PT|RO|SE|SI|SK|HR){1}[A-Z0-9]{2}([A-Z0-9]{3}){0,1}";
   public static final String REGEXP_IBAN = "(AT|BE|BG|CY|CZ|CH|DE|DK|EE|EL|ES|FI|FR|GB|HU|IE|IT|LT|LU|LV|MT|NL|PL|PT|RO|SE|SI|SK|HR){1}[0-9]{2}[0-9,A-Z]{10,30}";
   public static final String REGEXP_IMPORT_HATAROZAT = "(AT|BE|BG|CY|CZ|CH|DE|DK|EE|EL|ES|FI|FR|GB|HU|IE|IT|LT|LU|LV|MT|NL|PL|PT|RO|SE|SI|SK|HR){1}\\p{Print}{0,16}";
   public static final Set<String> SWIFT_HU_POSTFIX = new HashSet();

   public static boolean isValidDatum(String var0) {
      return FunctionBodies.isJoDatum(var0);
   }

   public static boolean isValidAdoszam(String var0) {
      boolean var1;
      if (var0 == null) {
         var1 = false;
      } else {
         String var2 = extractNumbers(var0);
         if (var2.length() == 11 && "1234569".indexOf(var2.substring(8, 9)) != -1 && "08".indexOf(var2.substring(0, 1)) == -1) {
            int var3 = Integer.parseInt(var2.substring(9, 11));
            if ((1 >= var3 || var3 >= 45 || var3 == 21) && var3 != 51) {
               var1 = false;
            } else {
               var1 = FunctionBodies.cdv(var2.substring(0, 8));
            }
         } else {
            var1 = false;
         }
      }

      return var1;
   }

   public static boolean isValidAdoAzonositoJel(String var0) {
      boolean var1;
      if (var0 != null && var0.length() == 10 && isStrNumber(var0) && var0.charAt(0) == '8') {
         int var2 = 0;

         int var3;
         for(var3 = 0; var3 < var0.length() - 1; ++var3) {
            var2 += (var3 + 1) * charToNumber(var0.charAt(var3));
         }

         var3 = var2 % 11;
         if (charToNumber(var0.charAt(9)) == var3) {
            var1 = true;
         } else {
            var1 = false;
         }
      } else {
         var1 = false;
      }

      return var1;
   }

   public static boolean isValidNyilvantartasiSzam(String var0) {
      if (!isValidCDV(var0, 10)) {
         return false;
      } else {
         String var1 = var0.substring(0, 3);
         return "911,910,900,912,902".indexOf(var1) > -1;
      }
   }

   public static boolean isValidCDV(String var0, int var1) {
      if (var0 != null && var0.length() == var1 && isStrNumber(var0)) {
         int var2 = 0;

         int var3;
         for(var3 = 0; var3 < var0.length() - 1; ++var3) {
            var2 += (var3 + 1) * charToNumber(var0.charAt(var3));
         }

         var3 = var2 % (var1 + 1);
         return charToNumber(var0.charAt(var1 - 1)) == (var3 > 9 ? 0 : var3);
      } else {
         return false;
      }
   }

   public static boolean isValidEmail(String var0) {
      return var0 != null && var0.length() != 0 ? var0.matches("([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})") : false;
   }

   public static boolean isValidSWIFT(String var0) {
      if (var0 != null && var0.length() != 0) {
         if (!var0.matches("[A-Z]{4}(AT|BE|BG|CY|CZ|CH|DE|DK|EE|EL|ES|FI|FR|GB|HU|IE|IT|LT|LU|LV|MT|NL|PL|PT|RO|SE|SI|SK|HR){1}[A-Z0-9]{2}([A-Z0-9]{3}){0,1}")) {
            return false;
         } else if (var0.substring(4, 6).toUpperCase().equalsIgnoreCase("HU")) {
            return SWIFT_HU_POSTFIX.contains(var0.toUpperCase().substring(6));
         } else {
            return !SWIFT_HU_POSTFIX.contains(var0.toUpperCase().substring(6));
         }
      } else {
         return false;
      }
   }

   public static boolean isValidIBAN(String var0) {
      return var0 != null && var0.length() != 0 ? var0.matches("(AT|BE|BG|CY|CZ|CH|DE|DK|EE|EL|ES|FI|FR|GB|HU|IE|IT|LT|LU|LV|MT|NL|PL|PT|RO|SE|SI|SK|HR){1}[0-9]{2}[0-9,A-Z]{10,30}") : false;
   }

   public static boolean isValidImportHatarozat(String var0) {
      return var0 != null && var0.length() != 0 ? var0.matches("(AT|BE|BG|CY|CZ|CH|DE|DK|EE|EL|ES|FI|FR|GB|HU|IE|IT|LT|LU|LV|MT|NL|PL|PT|RO|SE|SI|SK|HR){1}\\p{Print}{0,16}") : false;
   }

   public static boolean isValidEUMember(String var0) {
      return var0 != null && var0.length() != 0 ? var0.matches("(AT|BE|BG|CY|CZ|CH|DE|DK|EE|EL|ES|FI|FR|GB|HU|IE|IT|LT|LU|LV|MT|NL|PL|PT|RO|SE|SI|SK|HR){1}") : false;
   }

   public static boolean isLuhn(String var0) {
      if (var0 != null && isStrNumber(var0)) {
         int var1 = 0;
         boolean var3 = false;

         for(int var4 = var0.length() - 1; var4 >= 0; --var4) {
            int var2 = charToNumber(var0.charAt(var4));
            if (var3) {
               var2 *= 2;
               if (var2 > 9) {
                  var2 -= 9;
               }
            }

            var1 += var2;
            var3 = !var3;
         }

         return var1 % 10 == 0;
      } else {
         return false;
      }
   }

   static {
      Collections.addAll(SWIFT_HU_POSTFIX, new String[]{"HA", "HB", "HH", "HS", "HX", "2B", "22", "HBABC", "HXHUF"});
   }
}
