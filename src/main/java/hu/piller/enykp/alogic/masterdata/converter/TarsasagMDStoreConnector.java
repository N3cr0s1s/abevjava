package hu.piller.enykp.alogic.masterdata.converter;

import hu.piller.enykp.alogic.masterdata.core.Entity;
import hu.piller.enykp.alogic.masterdata.core.EntityException;
import hu.piller.enykp.alogic.masterdata.core.EntityHome;
import hu.piller.enykp.alogic.masterdata.core.MasterData;

public class TarsasagMDStoreConnector implements MDStoreConnector {
   public Entity getEntity(String var1) throws EntityException {
      Entity var2 = null;
      Entity[] var3 = (new EntityHome()).findByTypeAndMasterData("Társaság", new MasterData[]{new MasterData("Adózó adószáma", var1)});
      if (var3.length == 1) {
         var2 = var3[0];
      } else {
         var2 = (new EntityHome()).create("Társaság");
      }

      return var2;
   }

   public String[] map(String var1, boolean var2) {
      String[] var3 = new String[]{"N/A", "N/A"};
      if (!var2) {
         if ("adoszam".equals(var1)) {
            var3[0] = "Törzsadatok";
            var3[1] = "Adózó adószáma";
         } else if ("nev".equals(var1)) {
            var3[0] = "Törzsadatok";
            var3[1] = "Adózó neve";
         } else if ("varos".equals(var1)) {
            var3[0] = "Állandó cím";
            var3[1] = "Település";
         } else if ("utca".equals(var1)) {
            var3[0] = "Állandó cím";
            var3[1] = "Közterület neve";
         } else if ("kozt".equals(var1)) {
            var3[0] = "Állandó cím";
            var3[1] = "Közterület jellege";
         } else if ("hazszam".equals(var1)) {
            var3[0] = "Állandó cím";
            var3[1] = "Házszám";
         } else if ("epulet".equals(var1)) {
            var3[0] = "Állandó cím";
            var3[1] = "Épület";
         } else if ("lepcsohaz".equals(var1)) {
            var3[0] = "Állandó cím";
            var3[1] = "Lépcsőház";
         } else if ("emelet".equals(var1)) {
            var3[0] = "Állandó cím";
            var3[1] = "Emelet";
         } else if ("ajto".equals(var1)) {
            var3[0] = "Állandó cím";
            var3[1] = "Ajtó";
         } else if ("irszam".equals(var1)) {
            var3[0] = "Állandó cím";
            var3[1] = "Irányítószám";
         } else if ("varos1".equals(var1)) {
            var3[0] = "Levelezési cím";
            var3[1] = "L Település";
         } else if ("utca1".equals(var1)) {
            var3[0] = "Levelezési cím";
            var3[1] = "L Közterület neve";
         } else if ("kozt1".equals(var1)) {
            var3[0] = "Levelezési cím";
            var3[1] = "L Közterület jellege";
         } else if ("hazszam1".equals(var1)) {
            var3[0] = "Levelezési cím";
            var3[1] = "L Házszám";
         } else if ("epulet1".equals(var1)) {
            var3[0] = "Levelezési cím";
            var3[1] = "L Épület";
         } else if ("lepcsohaz1".equals(var1)) {
            var3[0] = "Levelezési cím";
            var3[1] = "L Lépcsőház";
         } else if ("emelet1".equals(var1)) {
            var3[0] = "Levelezési cím";
            var3[1] = "L Emelet";
         } else if ("ajto1".equals(var1)) {
            var3[0] = "Levelezési cím";
            var3[1] = "L Ajtó";
         } else if ("irszam1".equals(var1)) {
            var3[0] = "Levelezési cím";
            var3[1] = "L Irányítószám";
         } else if ("ugyi".equals(var1)) {
            var3[0] = "Egyéb adatok";
            var3[1] = "Ügyintéző neve";
         } else if ("ugyitel".equals(var1)) {
            var3[0] = "Egyéb adatok";
            var3[1] = "Ügyintéző telefonszáma";
         } else if ("bankneve".equals(var1)) {
            var3[0] = "Egyéb adatok";
            var3[1] = "Pénzintézet neve";
         } else if ("szamlaszam".equals(var1)) {
            var3[0] = "Egyéb adatok";
            var3[1] = "Számla-azonosító";
         } else if ("vpid".equals(var1)) {
            var3[0] = "VPOP törzsadatok";
            var3[1] = "VPID";
         } else if ("regisztraciosSzam".equals(var1)) {
            var3[0] = "VPOP törzsadatok";
            var3[1] = "Regisztrációs szám";
         } else if ("engedelyszam".equals(var1)) {
            var3[0] = "VPOP törzsadatok";
            var3[1] = "Engedélyszám";
         } else if ("bejelentoadoazon".equals(var1)) {
            var3[0] = "VPOP törzsadatok";
            var3[1] = "Bejelentő adóazonosító jele";
         }
      } else if ("varos".equals(var1)) {
         var3[0] = "Telephelyek";
         var3[1] = "T Település";
      } else if ("utca".equals(var1)) {
         var3[0] = "Telephelyek";
         var3[1] = "T Közterület neve";
      } else if ("kozt".equals(var1)) {
         var3[0] = "Telephelyek";
         var3[1] = "T Közterület jellege";
      } else if ("hazszam".equals(var1)) {
         var3[0] = "Telephelyek";
         var3[1] = "T Házszám";
      } else if ("epulet".equals(var1)) {
         var3[0] = "Telephelyek";
         var3[1] = "T Épület";
      } else if ("lepcsohaz".equals(var1)) {
         var3[0] = "Telephelyek";
         var3[1] = "T Lépcsőház";
      } else if ("emelet".equals(var1)) {
         var3[0] = "Telephelyek";
         var3[1] = "T Emelet";
      } else if ("ajto".equals(var1)) {
         var3[0] = "Telephelyek";
         var3[1] = "T Ajtó";
      } else if ("irszam".equals(var1)) {
         var3[0] = "Telephelyek";
         var3[1] = "T Irányítószám";
      }

      return var3;
   }
}
