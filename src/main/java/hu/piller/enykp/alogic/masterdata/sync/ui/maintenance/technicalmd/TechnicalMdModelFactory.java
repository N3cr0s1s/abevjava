package hu.piller.enykp.alogic.masterdata.sync.ui.maintenance.technicalmd;

import hu.piller.enykp.alogic.masterdata.sync.ui.maintenance.technicalmd.bankszamlaszam.BankszamlaszamModel;
import hu.piller.enykp.alogic.masterdata.sync.ui.maintenance.technicalmd.kozossegiadoszam.KozossegiAdoszamModel;

public class TechnicalMdModelFactory {
   private static TechnicalMdModelFactory instance;

   private TechnicalMdModelFactory() {
   }

   public static TechnicalMdModelFactory getInstance() {
      if (instance == null) {
         instance = new TechnicalMdModelFactory();
      }

      return instance;
   }

   public ITechnicalMdModel getDataModelForTechnicalMasterData(String var1) {
      if ("Bankszámlaszám".equals(var1)) {
         return new BankszamlaszamModel();
      } else {
         return "Közösségi adószám".equals(var1) ? new KozossegiAdoszamModel() : null;
      }
   }
}
