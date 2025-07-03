package hu.piller.enykp.alogic.primaryaccount.common;

import java.io.File;
import java.util.Vector;

public interface IRecordFactory {
   Vector loadRecords(File var1, DefaultEnvelope var2) throws Exception;

   void saveRecords(File var1);

   void deleteAll(File var1);

   void reload(File var1, IRecord var2) throws Exception;

   void save(File var1, IRecord var2);

   void delete(File var1, IRecord var2);

   String getNewId();

   Vector getRecords();
}
