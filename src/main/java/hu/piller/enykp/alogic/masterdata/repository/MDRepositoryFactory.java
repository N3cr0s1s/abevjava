package hu.piller.enykp.alogic.masterdata.repository;

import hu.piller.enykp.alogic.masterdata.repository.xml.MDRepositoryImpl;
import hu.piller.enykp.alogic.upgrademanager_v2_0.Directories;
import hu.piller.enykp.util.base.PropertyList;
import me.necrocore.abevjava.NecroFile;
import me.necrocore.abevjava.NecroFileWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MDRepositoryFactory {
   private static MDRepository _repository = null;

   public static synchronized MDRepository getRepository() throws MDRepositoryException {
      if (_repository == null) {
         String var0 = Directories.getSchemasPath() + "/Repository.xsd";
         String var1 = PropertyList.getInstance().get("prop.usr.primaryaccounts") + "/repository.xml";
         if (!(new NecroFile(var0)).exists()) {
            throw new MDRepositoryException("Nem található az adattár leíró: " + var0);
         }

         File var2 = new NecroFile(var1);
         if (var2.exists()) {
            if (var2.length() == 0L) {
               var2.delete();
               newRepo(var2);
            }
         } else {
            newRepo(var2);
         }

         _repository = new MDRepositoryImpl(var0, var1);
      } else {
         _repository.sync();
      }

      return _repository;
   }

   private static void newRepo(File var0) throws MDRepositoryException {
      FileWriter var1 = null;

      try {
         var1 = new NecroFileWriter(var0);
         var1.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?><Repository sequence=\"0\"/>");
         var1.flush();
      } catch (IOException var9) {
         throw new MDRepositoryException("Az üres repository.xml nem hozható létre", var9);
      } finally {
         try {
            if (var1 != null) {
               var1.close();
            }
         } catch (IOException var10) {
            throw new MDRepositoryException("Az üres repository.xml létrejött, de nem zárható le", var10);
         }

      }

   }
}
