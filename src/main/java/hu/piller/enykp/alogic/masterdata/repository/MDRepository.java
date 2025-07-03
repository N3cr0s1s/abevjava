package hu.piller.enykp.alogic.masterdata.repository;

import hu.piller.enykp.alogic.masterdata.core.Entity;
import hu.piller.enykp.alogic.masterdata.core.MasterData;

public interface MDRepository {
   long getNextEntityID();

   void store(Entity var1, long var2) throws MDRepositoryException;

   void delete(long var1, long var3) throws MDRepositoryException;

   Entity[] findByTypeAndContent(String var1, MasterData[] var2);

   Entity findById(long var1);

   long begin() throws MDRepositoryException;

   void commit(long var1) throws MDRepositoryException;

   void rollback(long var1) throws MDRepositoryException;

   void sync() throws MDRepositoryException;
}
