package hu.piller.enykp.alogic.masterdata.save;

import hu.piller.enykp.util.validation.ValidationUtilityAPEH;
import java.util.Map;

public class MDType {
   public static String adviseEntityType(Map<String, String> var0) {
      Object var1 = null;
      if (isTarsasag(var0)) {
         return "Társaság";
      } else if (isEgyeniVallalkozo(var0)) {
         return "Egyéni vállalkozó";
      } else {
         return (String)(isMaganszemely(var0) ? "Magánszemély" : var1);
      }
   }

   private static boolean isTarsasag(Map<String, String> var0) {
      boolean var1 = false;
      if (var0.containsKey("Adózó adószáma")) {
         String var2 = (String)var0.get("Adózó adószáma");
         if (ValidationUtilityAPEH.isValidAdoszam(var2) && Integer.parseInt(var2.substring(0, 1)) < 4) {
            var1 = true;
         }
      }

      return var1;
   }

   private static boolean isEgyeniVallalkozo(Map<String, String> var0) {
      boolean var1 = false;
      if (var0.containsKey("Adózó adószáma") && var0.containsKey("Adózó adóazonosító jele")) {
         String var2 = (String)var0.get("Adózó adószáma");
         String var3 = (String)var0.get("Adózó adóazonosító jele");
         if (ValidationUtilityAPEH.isValidAdoszam(var2) && Integer.parseInt(var2.substring(0, 1)) > 3 && ValidationUtilityAPEH.isValidAdoAzonositoJel(var3)) {
            var1 = true;
         }
      }

      return var1;
   }

   private static boolean isMaganszemely(Map<String, String> var0) {
      boolean var1 = false;
      if (var0.containsKey("Adózó adóazonosító jele")) {
         String var2 = (String)var0.get("Adózó adóazonosító jele");
         if (ValidationUtilityAPEH.isValidAdoAzonositoJel(var2)) {
            var1 = true;
         }
      }

      return var1;
   }
}
