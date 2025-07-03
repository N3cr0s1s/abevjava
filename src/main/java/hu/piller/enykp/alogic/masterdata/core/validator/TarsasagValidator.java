package hu.piller.enykp.alogic.masterdata.core.validator;

import cec.taxud.fiscalis.vies.common.VATValidation;
import hu.piller.enykp.alogic.masterdata.core.Entity;
import hu.piller.enykp.alogic.masterdata.core.EntityError;
import hu.piller.enykp.alogic.masterdata.core.MasterData;
import hu.piller.enykp.alogic.masterdata.gui.MDGUIFieldFactory;
import hu.piller.enykp.alogic.masterdata.repository.MDRepositoryException;
import hu.piller.enykp.alogic.masterdata.repository.MDRepositoryFactory;
import hu.piller.enykp.util.validation.ValidationUtilityAPEH;
import hu.piller.enykp.util.validation.ValidationUtilityVPOP;
import java.util.Vector;

public class TarsasagValidator extends TypeValidator implements EntityValidator {
   public EntityError[] isValid(Entity var1) {
      Vector var2 = new Vector();
      this.checkEUTaxNumber(var2, var1);
      this.appendError(var2, this.checkAdoszam(var1));
      this.appendError(var2, this.checkVPID(var1));
      this.appendError(var2, this.checkRegszam(var1));
      this.appendError(var2, this.checkEngedelyszam(var1));
      this.appendError(var2, this.checkBejelentoAdoazonJel(var1));
      this.appendError(var2, this.checkAdozoEmail(var1));
      this.appendError(var2, this.checkUgyintezoEmail(var1));
      return (EntityError[])var2.toArray(new EntityError[var2.size()]);
   }

   private EntityError checkUgyintezoEmail(Entity var1) {
      EntityError var2 = null;
      String var3 = var1.getBlock("Egyéb adatok").getMasterData("Ügyintéző e-mail címe").getValue();
      if (var3 != null && var3.trim().length() > 0 && !ValidationUtilityAPEH.isValidEmail(var3)) {
         var2 = new EntityError(var1.getName(), "Egyéb adatok", 1, "Ügyintéző e-mail címe", var3, "Az ügyintéző e-mail címe érvénytelen.");
      }

      return var2;
   }

   private EntityError checkAdozoEmail(Entity var1) {
      EntityError var2 = null;
      String var3 = var1.getBlock("Törzsadatok").getMasterData("E-mail címe").getValue();
      if (var3 != null && var3.trim().length() > 0 && !ValidationUtilityAPEH.isValidEmail(var3)) {
         var2 = new EntityError(var1.getName(), "Törzsadatok", 1, "E-mail címe", var3, "Az adózó e-mail címe érvénytelen.");
      }

      return var2;
   }

   private EntityError checkAdozoNeve(Entity var1) {
      EntityError var2 = null;
      String var3 = var1.getBlock("Törzsadatok", 1).getMasterData("Adózó neve").getValue();
      if (var3 == null || var3 != null && "".equals(var3.trim())) {
         var2 = new EntityError(var1.getName(), "Törzsadatok", 1, "Adózó neve", var3, "A társaság nevét meg kell adni.");
      }

      return var2;
   }

   private EntityError checkAdoszam(Entity var1) {
      EntityError var2 = null;
      String var3 = var1.getBlock("Törzsadatok", 1).getMasterData("Adózó adószáma").getValue();
      if (var3 != null && !"".equals(var3.trim())) {
         if (!ValidationUtilityAPEH.isValidAdoszam(var3)) {
            var2 = new EntityError(var1.getName(), "Törzsadatok", 1, "Adózó adószáma", var3, "Az adózó adószáma érvénytelen.");
         } else {
            try {
               Entity[] var4 = MDRepositoryFactory.getRepository().findByTypeAndContent("*", new MasterData[]{new MasterData("Adózó adószáma", var3)});
               if (var4.length != 0) {
                  boolean var5 = true;
                  if (var4.length == 1 && var4[0].getId() == var1.getId()) {
                     var5 = false;
                  }

                  if (var5) {
                     var2 = new EntityError(var1.getName(), "Törzsadatok", 1, "Adózó adószáma", var3, "Az adószám már szerepel az adatbázisban!");
                  }
               }
            } catch (MDRepositoryException var6) {
               var2 = new EntityError(var1.getName(), "Törzsadatok", 1, "Adózó adószáma", var3, "Adószám egyediség ellenőrzés hiba: " + var6.getMessage());
            }
         }
      }

      return var2;
   }

   private EntityError checkVPID(Entity var1) {
      EntityError var2 = null;
      String var3 = var1.getBlock("VPOP törzsadatok", 1).getMasterData("VPID").getValue();
      if (!"".equals(var3.trim()) && !"HU".equals(var3.trim()) && !ValidationUtilityVPOP.isVPid(var3)) {
         var2 = new EntityError(var1.getName(), "VPOP törzsadatok", 1, "VPID", var3, "Érvénytelen VP azonosító.");
      }

      return var2;
   }

   private EntityError checkRegszam(Entity var1) {
      EntityError var2 = null;
      String var3 = var1.getBlock("VPOP törzsadatok", 1).getMasterData("Regisztrációs szám").getValue();
      if (!"".equals(var3.trim()) && !ValidationUtilityVPOP.isRegSzam(var3)) {
         var2 = new EntityError(var1.getName(), "VPOP törzsadatok", 1, "Regisztrációs szám", var3, "Érvénytelen regisztrációs szám.");
      }

      return var2;
   }

   private EntityError checkEngedelyszam(Entity var1) {
      EntityError var2 = null;
      String var3 = var1.getBlock("VPOP törzsadatok", 1).getMasterData("Engedélyszám").getValue();
      if (!"".equals(var3.trim()) && !"HU".equals(var3.trim()) && !ValidationUtilityVPOP.isEngedelySzam(var3)) {
         var2 = new EntityError(var1.getName(), "VPOP törzsadatok", 1, "Engedélyszám", var3, "Érvénytelen engedélyszám.");
      }

      return var2;
   }

   private EntityError checkBejelentoAdoazonJel(Entity var1) {
      EntityError var2 = null;
      String var3 = var1.getBlock("VPOP törzsadatok", 1).getMasterData("Bejelentő adóazonosító jele").getValue();
      if (!"".equals(var3.trim()) && !ValidationUtilityAPEH.isValidAdoAzonositoJel(var3)) {
         var2 = new EntityError(var1.getName(), "VPOP törzsadatok", 1, "Bejelentő adóazonosító jele", var3, "A bejelentő adóazonosító jele érvénytelen.");
      }

      return var2;
   }

   private void appendError(Vector<EntityError> var1, EntityError var2) {
      if (var2 != null) {
         var1.add(var2);
      }

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
