package hu.piller.enykp.alogic.masterdata.core.validator;

import hu.piller.enykp.alogic.masterdata.core.Block;
import hu.piller.enykp.alogic.masterdata.core.Entity;
import hu.piller.enykp.alogic.masterdata.core.EntityError;
import hu.piller.enykp.alogic.masterdata.repository.MDRepositoryException;
import hu.piller.enykp.alogic.masterdata.repository.MDRepositoryMetaFactory;
import hu.piller.enykp.util.validation.ValidationUtilityAPEH;
import hu.piller.enykp.util.validation.ValidationUtilityVPOP;
import java.util.Vector;

public class TypeValidator implements EntityValidator {
   public EntityError[] isValid(Entity var1) {
      Vector var2 = new Vector();
      if (var1 != null) {
         Block[] var3 = var1.getAllBlocks();
         String var4 = null;
         String var5 = null;
         Block[] var6 = var3;
         int var7 = var3.length;

         for(int var8 = 0; var8 < var7; ++var8) {
            Block var9 = var6[var8];

            try {
               String[] var10 = MDRepositoryMetaFactory.getMDRepositoryMeta().getMasterDataForEntityTypeBlockType(var1.getName(), var9.getName());

               for(int var11 = 0; var11 < var10.length; ++var11) {
                  var4 = var10[var11];
                  String var12 = MDRepositoryMetaFactory.getMDRepositoryMeta().getTypeOfMasterData(var4);
                  var5 = var9.getMasterData(var4).getValue();
                  String var13 = this.check(var12, var5);
                  if (!"OK".equals(var13)) {
                     var2.add(new EntityError(var1.getName(), var9.getName(), var9.getSeq(), var4, var5, var13));
                  }
               }
            } catch (MDRepositoryException var14) {
               var2.add(new EntityError(var1.getName(), var9.getName(), var9.getSeq(), var4, var5, var14.getMessage()));
            }
         }
      }

      return (EntityError[])var2.toArray(new EntityError[var2.size()]);
   }

   private String check(String var1, String var2) {
      String var3 = "OK";
      if ("TApehAdoszam".equals(var1)) {
         if (!ValidationUtilityAPEH.isValidAdoszam(var2)) {
            var3 = "Érvénytelen adószám: " + var2;
         }
      } else if ("TApehAdoazonosito".equals(var1)) {
         if (!ValidationUtilityAPEH.isValidAdoAzonositoJel(var2)) {
            var3 = "Érvénytelen adóazonosító jel: " + var2;
         }
      } else if ("TVpopId".equals(var1)) {
         if (!ValidationUtilityVPOP.isVPid(var2)) {
            var3 = "Érvénytelen VP azonosító: " + var2;
         }
      } else if ("TVpopRegisztraciosSzam".equals(var1)) {
         if (!ValidationUtilityVPOP.isRegSzam(var2)) {
            var3 = "Érvénytelen VP regisztrációs szám: " + var2;
         }
      } else if ("TVpopEngedelySzam".equals(var1) && !ValidationUtilityVPOP.isGln(var2)) {
         var3 = "Érvénytelen VP engedélyszám: " + var2;
      }

      return var3;
   }
}
