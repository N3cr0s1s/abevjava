package hu.piller.enykp.alogic.masterdata.sync.ui.maintenance;

import hu.piller.enykp.alogic.masterdata.core.Entity;
import hu.piller.enykp.alogic.masterdata.core.MasterData;
import hu.piller.enykp.alogic.masterdata.repository.MDRepositoryException;
import hu.piller.enykp.alogic.masterdata.repository.MDRepositoryFactory;
import hu.piller.enykp.alogic.masterdata.sync.download.MasterDataDownload;
import hu.piller.enykp.alogic.masterdata.sync.download.MasterDataDownloadException;

public class MaintenanceDAO {
   private static MaintenanceDAO _this;

   private MaintenanceDAO() {
   }

   public static MaintenanceDAO getInstance() {
      if (_this == null) {
         _this = new MaintenanceDAO();
      }

      return _this;
   }

   public Entity readEntityByIdFromRepository(String var1) throws Exception {
      String var2;
      if (var1.length() == 13 && var1.charAt(8) == '-' && var1.charAt(10) == '-') {
         var2 = "Adózó adószáma";
      } else {
         var2 = "Adózó adóazonosító jele";
      }

      Entity[] var3 = MDRepositoryFactory.getRepository().findByTypeAndContent("*", new MasterData[]{new MasterData(var2, var1)});
      if (var3 != null && var3.length == 1) {
         return var3[0];
      } else {
         throw new Exception("Nincs ilyen adózó : " + var1);
      }
   }

   public Entity readEntityByIdentifierFromSyncFolder(String var1) throws MasterDataDownloadException {
      if (var1.length() == 13 && var1.charAt(8) == '-' && var1.charAt(10) == '-') {
         var1 = var1.substring(0, 8);
      }

      return MasterDataDownload.getInstance().getDownloadedEntity(var1);
   }

   public void writeEntityToRepository(Entity var1) throws MDRepositoryException {
      boolean var2 = false;
      long var3 = -1L;

      try {
         var3 = MDRepositoryFactory.getRepository().begin();
         MDRepositoryFactory.getRepository().store(var1, var3);
         var2 = true;
      } finally {
         if (var2) {
            MDRepositoryFactory.getRepository().commit(var3);
         } else {
            MDRepositoryFactory.getRepository().rollback(var3);
         }

      }

   }
}
