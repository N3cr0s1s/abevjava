package hu.piller.enykp.alogic.masterdata.repository;

import hu.piller.enykp.alogic.masterdata.repository.xml.MDRepositoryMetaImpl;
import hu.piller.enykp.alogic.orghandler.OrgInfo;
import hu.piller.enykp.alogic.upgrademanager_v2_0.Directories;
import java.io.File;
import java.io.InputStream;
import java.util.Vector;

public class MDRepositoryMetaFactory {
   private static MDRepositoryMeta _meta;

   public static synchronized MDRepositoryMeta getMDRepositoryMeta() throws MDRepositoryException {
      if (_meta == null) {
         String var0 = Directories.getSchemasPath() + "/MasterDataDef.xsd";
         if (!(new File(var0)).exists()) {
            throw new MDRepositoryException("Nem található a törzsadat szerkezet leíró: " + var0);
         }

         Vector var1 = (Vector)((Object[])((Object[])OrgInfo.getInstance().getOrgNames()))[0];
         String[] var2 = null;
         if (var1 != null) {
            var2 = new String[var1.size() + 1];
            var2[0] = "ALL";

            for(int var3 = 1; var3 < var1.size() + 1; ++var3) {
               var2[var3] = (String)var1.get(var3 - 1);
            }
         }

         String var5 = Directories.getSchemasPath() + "/EntityDef.xsd";
         if (!(new File(var5)).exists()) {
            throw new MDRepositoryException("Nem található az egyed struktúra leíró: " + var5);
         }

         InputStream var4 = ClassLoader.getSystemClassLoader().getResourceAsStream("resources/masterdata/mdm_entitydef.xml");
         if (var4 == null) {
            throw new MDRepositoryException("Nem található az egyed típus definíciós fájl: mdm_entitydef.xml");
         }

         _meta = new MDRepositoryMetaImpl(var0, var2, var5, var4);
      }

      return _meta;
   }
}
