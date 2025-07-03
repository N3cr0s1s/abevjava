package hu.piller.enykp.alogic.masterdata.core.validator;

import cec.taxud.fiscalis.vies.common.VATValidation;
import hu.piller.enykp.alogic.masterdata.core.Entity;
import hu.piller.enykp.alogic.masterdata.core.EntityError;
import hu.piller.enykp.alogic.masterdata.core.MasterData;
import hu.piller.enykp.alogic.masterdata.gui.MDGUIFieldFactory;
import hu.piller.enykp.alogic.masterdata.repository.MDRepositoryException;
import hu.piller.enykp.alogic.masterdata.repository.MDRepositoryFactory;
import hu.piller.enykp.util.validation.ValidationUtilityAPEH;
import java.util.Vector;

public class EgyeniVallalkozoValidator extends MaganszemelyValidator implements EntityValidator {
   public EntityError[] isValid(Entity var1) {
      Vector var2 = new Vector();
      EntityError[] var3 = super.isValid(var1);
      if (var3.length > 0) {
         EntityError[] var4 = var3;
         int var5 = var3.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            EntityError var7 = var4[var6];
            var2.add(var7);
         }
      }

      this.checkEUTaxNumber(var2, var1);
      String var9 = var1.getBlock("Törzsadatok", 1).getMasterData("Adózó adószáma").getValue();
      if (var9 != null && !"".equals(var9.trim())) {
         if (!ValidationUtilityAPEH.isValidAdoszam(var9)) {
            var2.add(new EntityError(var1.getName(), "Törzsadatok", 1, "Adózó adószáma", var9, "Az adózó adószám érvénytelen."));
         } else {
            try {
               Entity[] var10 = MDRepositoryFactory.getRepository().findByTypeAndContent("*", new MasterData[]{new MasterData("Adózó adószáma", var9)});
               if (var10.length != 0) {
                  boolean var11 = true;
                  if (var10.length == 1 && var10[0].getId() == var1.getId()) {
                     var11 = false;
                  }

                  if (var11) {
                     var2.add(new EntityError(var1.getName(), "Törzsadatok", 1, "Adózó adószáma", var9, "Az adószám már szerepel az adatbázisban!"));
                  }
               }
            } catch (MDRepositoryException var8) {
               var2.add(new EntityError(var1.getName(), "Törzsadatok", 1, "Adózó adószáma", var9, "Adószám egyediség ellenőrzés hiba: " + var8.getMessage()));
            }
         }
      } else {
         var2.add(new EntityError(var1.getName(), "Törzsadatok", 1, "Adózó adószáma", var9, "Az adószámot meg kell adni."));
      }

      return (EntityError[])var2.toArray(new EntityError[var2.size()]);
   }

   private void checkEUTaxNumber(Vector<EntityError> var1, Entity var2) {
      String var3 = var2.getBlock("Törzsadatok", 1).getMasterData("Közösségi adószám ország kód").getValue();
      String var4 = var2.getBlock("Törzsadatok", 1).getMasterData("Közösségi adószám").getValue();
      if (!"".equals(var3) || !"".equals(var4)) {
         if (!"".equals(var3) && "".equals(var4)) {
            var1.add(new EntityError(var2.getName(), "Törzsadatok", 1, "Közösségi adószám", var4, "Hiányzik a tagállami adóazonosító!"));
         } else if ("".equals(var3) && !"".equals(var4)) {
            var1.add(new EntityError(var2.getName(), "Törzsadatok", 1, "Közösségi adószám ország kód", var3, "Hiányzik a tagállam kódja!"));
         } else {
            boolean var5 = false;
            String[] var6 = MDGUIFieldFactory.OPCIOK_ORSZAG_ISO;
            int var7 = var6.length;

            for(int var8 = 0; var8 < var7; ++var8) {
               String var9 = var6[var8];
               if (var9.equals(var3)) {
                  var5 = true;
                  break;
               }
            }

            if (!var5) {
               var1.add(new EntityError(var2.getName(), "Törzsadatok", 1, "Közösségi adószám ország kód", var3, "Érvénytelen ország azonosító!"));
            }

            if (!VATValidation.check(var4, var3)) {
               var1.add(new EntityError(var2.getName(), "Törzsadatok", 1, "Közösségi adószám ország kód", var3, "Érvénytelen közösségi adószám!"));
               var1.add(new EntityError(var2.getName(), "Törzsadatok", 1, "Közösségi adószám", var4, "Érvénytelen közösségi adószám!"));
            }

            try {
               Entity[] var11 = MDRepositoryFactory.getRepository().findByTypeAndContent("*", new MasterData[]{new MasterData("Közösségi adószám ország kód", var3), new MasterData("Közösségi adószám", var4)});
               if (var11.length != 0) {
                  boolean var12 = true;
                  if (var11.length == 1 && var11[0].getId() == var2.getId()) {
                     var12 = false;
                  }

                  if (var12) {
                     var1.add(new EntityError(var2.getName(), "Törzsadatok", 1, "Közösségi adószám ország kód", var3, "A közösségi adószám már szerepel az adatbázisban!"));
                     var1.add(new EntityError(var2.getName(), "Törzsadatok", 1, "Közösségi adószám", var4, "A közösségi adószám már szerepel az adatbázisban!"));
                  }
               }
            } catch (MDRepositoryException var10) {
               var1.add(new EntityError(var2.getName(), "Törzsadatok", 1, "Közösségi adószám ország kód", var3, "A közösségi adószám már szerepel az adatbázisban!"));
               var1.add(new EntityError(var2.getName(), "Törzsadatok", 1, "Közösségi adószám", var4, "A közösségi adószám már szerepel az adatbázisban!"));
            }
         }

      }
   }
}
