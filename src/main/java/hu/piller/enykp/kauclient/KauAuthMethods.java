package hu.piller.enykp.kauclient;

import hu.piller.enykp.alogic.settingspanel.SettingsStore;

public final class KauAuthMethods {
   public static KauAuthMethod getSelected() {
      String var0 = getSelectedAuthMethod();
      byte var2 = -1;
      switch(var0.hashCode()) {
      case -2085125866:
         if (var0.equals("KAU_UK")) {
            var2 = 4;
         }
         break;
      case -214411519:
         if (var0.equals("KAU_ALL")) {
            var2 = 0;
         }
         break;
      case -214408973:
         if (var0.equals("KAU_DAP")) {
            var2 = 1;
         }
         break;
      case -214407644:
         if (var0.equals("KAU_EML")) {
            var2 = 2;
         }
         break;
      case -214392326:
         if (var0.equals("KAU_UKP")) {
            var2 = 3;
         }
      }

      switch(var2) {
      case 0:
         return KauAuthMethod.KAU_ALL;
      case 1:
         return KauAuthMethod.KAU_DAP;
      case 2:
         return KauAuthMethod.KAU_EML;
      case 3:
      case 4:
      default:
         return KauAuthMethod.KAU_UKP;
      }
   }

   private static String getSelectedAuthMethod() {
      String var0 = SettingsStore.getInstance().get("gui", "kaubrowser");
      if (var0 == null) {
         return "KAU_UKP";
      } else if ("true".equalsIgnoreCase(var0)) {
         return "KAU_UKP";
      } else {
         return "false".equalsIgnoreCase(var0) ? "KAU_UKP" : var0;
      }
   }
}
