package hu.piller.enykp.alogic.masterdata.core.validator;

import hu.piller.enykp.alogic.masterdata.core.Entity;
import hu.piller.enykp.alogic.masterdata.core.EntityError;
import hu.piller.enykp.util.validation.ValidationUtilityAPEH;
import java.util.Vector;

public class AdotanacsadoValidator extends TypeValidator implements EntityValidator {
   public EntityError[] isValid(Entity var1) {
      Vector var2 = new Vector();
      String var3 = var1.getBlock("Név", 1).getMasterData("Adótanácsadó neve").getValue();
      if (var3 == null || "".equals(var3.trim())) {
         var2.add(new EntityError(var1.getName(), "Név", 1, "Adótanácsadó neve", var3, "Meg kell adni az adótanácsadó nevét."));
      }

      var3 = var1.getBlock("Név", 1).getMasterData("Adótanácsadó Bizonyítvány").getValue();
      if (var3 == null || "".equals(var3.trim())) {
         var2.add(new EntityError(var1.getName(), "Név", 1, "Adótanácsadó Bizonyítvány", var3, "Meg kell adni az adótanácsadó bizonyítványszámát."));
      }

      var3 = var1.getBlock("Név", 1).getMasterData("Adótanácsadó azonosítószáma").getValue();
      if (var3 == null || "".equals(var3.trim())) {
         var2.add(new EntityError(var1.getName(), "Név", 1, "Adótanácsadó azonosítószáma", var3, "Meg kell adni az adótanácsadó azonosítószámát!"));
      }

      if (var3.length() != 0 && !"".equals(var3.trim())) {
         if (var3.length() > 10) {
            if (!ValidationUtilityAPEH.isValidAdoszam(var3)) {
               var2.add(new EntityError(var1.getName(), "Név", 1, "Adótanácsadó azonosítószáma", var3, "Az adótanácsadót azonosító adószám érvénytelen!"));
            }
         } else if (!ValidationUtilityAPEH.isValidAdoAzonositoJel(var3)) {
            var2.add(new EntityError(var1.getName(), "Név", 1, "Adótanácsadó azonosítószáma", var3, "Az adótanácsadót azonosító adóazonosító jel érvénytelen!"));
         }
      }

      return (EntityError[])var2.toArray(new EntityError[var2.size()]);
   }
}
