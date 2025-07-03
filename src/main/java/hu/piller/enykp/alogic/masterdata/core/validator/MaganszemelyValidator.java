package hu.piller.enykp.alogic.masterdata.core.validator;

import hu.piller.enykp.alogic.masterdata.core.Entity;
import hu.piller.enykp.alogic.masterdata.core.EntityError;
import hu.piller.enykp.alogic.masterdata.core.MasterData;
import hu.piller.enykp.alogic.masterdata.repository.MDRepositoryException;
import hu.piller.enykp.alogic.masterdata.repository.MDRepositoryFactory;
import hu.piller.enykp.util.validation.ValidationUtilityAPEH;
import hu.piller.enykp.util.validation.ValidationUtilityVPOP;
import java.util.Vector;

public class MaganszemelyValidator extends TypeValidator implements EntityValidator {
   public EntityError[] isValid(Entity var1) {
      Vector var2 = new Vector();
      this.appendError(var2, this.checkAdoazonositoJel(var1));
      this.appendError(var2, this.checkVezetekNev(var1));
      this.appendError(var2, this.checkKeresztNev(var1));
      this.appendError(var2, this.checkVPID(var1));
      this.appendError(var2, this.checkRegszam(var1));
      this.appendError(var2, this.checkEngedelyszam(var1));
      this.appendError(var2, this.checkBejelentoAdoazonJel(var1));
      this.appendError(var2, this.checkAdozoEmail(var1));
      this.appendError(var2, this.checkUgyintezoEmail(var1));
      this.appendError(var2, this.checkSzuletesiDatum(var1));
      return (EntityError[])var2.toArray(new EntityError[var2.size()]);
   }

   private EntityError checkSzuletesiDatum(Entity var1) {
      EntityError var2 = null;
      String var3 = var1.getBlock("Születési adatok").getMasterData("Születési időpont").getValue();
      if (var3 != null) {
         var3 = var3.replaceAll("-", "").trim();
         if (var3.length() > 0 && !ValidationUtilityAPEH.isValidDatum(var3)) {
            var2 = new EntityError(var1.getName(), "Születési adatok", 1, "Születési időpont", var3, "A megadott dátum érvénytelen.");
         }
      }

      return var2;
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

   private EntityError checkAdoazonositoJel(Entity var1) {
      EntityError var2 = null;
      String var3 = var1.getBlock("Törzsadatok", 1).getMasterData("Adózó adóazonosító jele").getValue();
      if (var3 != null && !"".equals(var3.trim())) {
         if (!ValidationUtilityAPEH.isValidAdoAzonositoJel(var3)) {
            var2 = new EntityError(var1.getName(), "Törzsadatok", 1, "Adózó adóazonosító jele", var3, "Az adóazonosító jel érvénytelen.");
         } else {
            try {
               Entity[] var4 = MDRepositoryFactory.getRepository().findByTypeAndContent("*", new MasterData[]{new MasterData("Adózó adóazonosító jele", var3)});
               if (var4.length != 0) {
                  boolean var5 = true;
                  if (var4.length == 1 && var4[0].getId() == var1.getId()) {
                     var5 = false;
                  }

                  if (var5) {
                     var2 = new EntityError(var1.getName(), "Törzsadatok", 1, "Adózó adóazonosító jele", var3, "Az adóazonosító jel már szerepel az adatbázisban!");
                  }
               }
            } catch (MDRepositoryException var6) {
               var2 = new EntityError(var1.getName(), "Törzsadatok", 1, "Adózó adóazonosító jele", var3, "Adóazonosító jel egyediség ellenőrzés hiba: " + var6.getMessage());
            }
         }
      } else {
         var2 = new EntityError(var1.getName(), "Törzsadatok", 1, "Adózó adóazonosító jele", var3, "Az adóazonosító jelet meg kell adni.");
      }

      return var2;
   }

   private EntityError checkVezetekNev(Entity var1) {
      EntityError var2 = null;
      String var3 = var1.getBlock("Törzsadatok", 1).getMasterData("Vezetékneve").getValue();
      if (var3 == null || "".equals(var3.trim())) {
         var2 = new EntityError(var1.getName(), "Törzsadatok", 1, "Vezetékneve", var3, "A vezetéknevet meg kell adni.");
      }

      return var2;
   }

   private EntityError checkKeresztNev(Entity var1) {
      EntityError var2 = null;
      String var3 = var1.getBlock("Törzsadatok", 1).getMasterData("Keresztneve").getValue();
      if (var3 == null || "".equals(var3.trim())) {
         var2 = new EntityError(var1.getName(), "Törzsadatok", 1, "Keresztneve", var3, "A keresztnevet meg kell adni.");
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
}
