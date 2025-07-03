package hu.piller.enykp.alogic.masterdata.sync.ui.maintenance.technicalmd.bankszamlaszam;

public class BankszamlaszamFormatter {
   public static String buildFormattedBankAccount(String var0, String var1, String var2) {
      String var3 = clean(var0) + " " + clean(var1) + "-" + clean(var2);
      if ("".equals(var3.replaceAll("-", "").replaceAll(" ", ""))) {
         return "";
      } else {
         return var3.endsWith("-") ? var3.substring(0, var3.length() - 1) : var3;
      }
   }

   public static String formatAccountName(String var0) {
      return clean(var0);
   }

   public static String formatRoutingCode(String var0) {
      return clean(var0);
   }

   public static String formatAccountId(String var0) {
      var0 = clean(var0);
      if (var0.length() == 16) {
         return var0.substring(0, 8) + "-" + var0.substring(8);
      } else {
         return var0.charAt(var0.length() - 1) == '-' ? var0.substring(0, var0.length() - 1) : var0;
      }
   }

   private static String clean(String var0) {
      return var0 != null ? deleteEmptyNumberPlaceholders(var0.trim()) : "";
   }

   private static String deleteEmptyNumberPlaceholders(String var0) {
      return var0.replaceAll("_", "");
   }
}
