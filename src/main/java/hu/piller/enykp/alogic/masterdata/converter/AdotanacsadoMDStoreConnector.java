package hu.piller.enykp.alogic.masterdata.converter;

import hu.piller.enykp.alogic.masterdata.core.Entity;
import hu.piller.enykp.alogic.masterdata.core.EntityException;
import hu.piller.enykp.alogic.masterdata.core.EntityHome;
import hu.piller.enykp.alogic.masterdata.core.MasterData;

public class AdotanacsadoMDStoreConnector implements MDStoreConnector {
   public Entity getEntity(String var1) throws EntityException {
      Entity var2 = null;
      Entity[] var3 = (new EntityHome()).findByTypeAndMasterData("Adótanácsadó", new MasterData[]{new MasterData("Adótanácsadó azonositószáma", var1)});
      if (var3.length == 1) {
         var2 = var3[0];
      } else {
         var2 = (new EntityHome()).create("Adótanácsadó");
      }

      return var2;
   }

   public String[] map(String var1, boolean var2) {
      String[] var3 = new String[]{"N/A", "N/A"};
      if ("neve".equals(var1)) {
         var3[0] = "Név";
         var3[1] = "Adótanácsadó neve";
      } else if ("bizonyitvany".equals(var1)) {
         var3[0] = "Név";
         var3[1] = "Adótanácsadó Bizonyítvány";
      } else if ("azonosito".equals(var1)) {
         var3[0] = "Név";
         var3[1] = "Adótanácsadó azonosítószáma";
      }

      return var3;
   }
}
