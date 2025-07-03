package hu.piller.enykp.alogic.masterdata.core;

import hu.piller.enykp.alogic.masterdata.repository.MDRepositoryException;
import hu.piller.enykp.alogic.masterdata.repository.MDRepositoryFactory;
import hu.piller.enykp.alogic.masterdata.repository.MDRepositoryMetaFactory;

public class EntityHome {
   private long NOT_SET = -1L * (long)(Math.random() * 1000.0D);
   private final boolean SUCCESS = true;
   private final boolean ERROR = false;
   private boolean session_locked;
   private long session_lock_id;
   private static EntityHome _instance;

   public static synchronized EntityHome getInstance() {
      if (_instance == null) {
         _instance = new EntityHome();
      }

      return _instance;
   }

   public EntityHome() {
      this.session_lock_id = this.NOT_SET;
   }

   public Entity create(String var1) throws EntityException {
      long var2 = this.getLock();
      boolean var4 = true;

      Entity var9;
      try {
         BlockDefinition[] var5 = null;
         String var6 = null;

         try {
            var5 = MDRepositoryMetaFactory.getMDRepositoryMeta().getBlockDefinitionsForEntity(var1);
         } catch (MDRepositoryException var14) {
            var6 = var14.getMessage();
         }

         if (var5 == null) {
            throw new EntityException("Nem definiált egyed típus: '" + var1 + "'" + var6 == null ? "" : " (" + var6 + ")");
         }

         long var7 = MDRepositoryFactory.getRepository().getNextEntityID();
         var9 = new Entity(var1, var5, var7);
      } catch (MDRepositoryException var15) {
         var4 = false;
         throw new EntityException("Egyed azonosító generálási hiba: " + var15.getMessage());
      } finally {
         if (!this.session_locked) {
            this.releaseLock(var2, var4);
         }

      }

      return var9;
   }

   public synchronized void startSession() throws EntityException {
      if (!this.session_locked) {
         EntityException var1 = null;

         try {
            this.session_locked = true;
            this.getLock();
         } catch (EntityException var6) {
            var1 = var6;
         } finally {
            if (var1 != null) {
               this.session_locked = false;
               throw var1;
            }

         }

      } else {
         throw new EntityException("Folyamatban levő adat karbantartásra nem lehet újat nyitni!");
      }
   }

   public synchronized void closeSession() throws EntityException {
      this.releaseSessionLock(true);
   }

   public synchronized void abortSession() throws EntityException {
      this.releaseSessionLock(false);
   }

   public synchronized void remove(Entity var1) throws EntityException {
      long var2 = this.getLock();
      boolean var4 = true;

      try {
         MDRepositoryFactory.getRepository().delete(var1.getId(), var2);
      } catch (MDRepositoryException var9) {
         var4 = false;
         throw new EntityException(var9.getMessage());
      } finally {
         if (!this.session_locked) {
            this.releaseLock(var2, var4);
         }

      }

   }

   public synchronized void remove(Entity[] var1) throws EntityException {
      long var2 = this.getLock();
      boolean var4 = true;

      try {
         Entity[] var5 = var1;
         int var6 = var1.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            Entity var8 = var5[var7];
            MDRepositoryFactory.getRepository().delete(var8.getId(), var2);
         }
      } catch (MDRepositoryException var12) {
         var4 = false;
         throw new EntityException(var12.getMessage());
      } finally {
         if (!this.session_locked) {
            this.releaseLock(var2, var4);
         }

      }

   }

   public synchronized void update(Entity var1) throws EntityException {
      long var2 = this.getLock();
      boolean var4 = true;

      try {
         MDRepositoryFactory.getRepository().store(var1, var2);
      } catch (MDRepositoryException var9) {
         var4 = false;
         throw new EntityException(var9.getMessage());
      } finally {
         if (!this.session_locked) {
            this.releaseLock(var2, var4);
         }

      }

   }

   public synchronized void update(Entity[] var1) throws EntityException {
      long var2 = this.getLock();
      boolean var4 = true;

      try {
         Entity[] var5 = var1;
         int var6 = var1.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            Entity var8 = var5[var7];
            MDRepositoryFactory.getRepository().store(var8, var2);
         }
      } catch (MDRepositoryException var12) {
         var4 = false;
         throw new EntityException(var12.getMessage());
      } finally {
         if (!this.session_locked) {
            this.releaseLock(var2, var4);
         }

      }

   }

   public synchronized Entity findByID(long var1) throws EntityException {
      try {
         return MDRepositoryFactory.getRepository().findById(var1);
      } catch (MDRepositoryException var4) {
         throw new EntityException(var4.getMessage());
      }
   }

   public synchronized Entity[] findByTypeAndMasterData(String var1, MasterData[] var2) throws EntityException {
      try {
         return MDRepositoryFactory.getRepository().findByTypeAndContent(var1, var2);
      } catch (MDRepositoryException var4) {
         throw new EntityException(var4.getMessage());
      }
   }

   private long getLock() throws EntityException {
      if (this.session_locked) {
         if (this.session_lock_id == this.NOT_SET) {
            try {
               this.session_lock_id = MDRepositoryFactory.getRepository().begin();
            } catch (MDRepositoryException var2) {
               throw new EntityException(var2.getMessage());
            }
         }

         return this.session_lock_id;
      } else {
         try {
            return MDRepositoryFactory.getRepository().begin();
         } catch (MDRepositoryException var3) {
            throw new EntityException(var3.getMessage());
         }
      }
   }

   private void releaseLock(long var1, boolean var3) throws EntityException {
      try {
         if (var3) {
            MDRepositoryFactory.getRepository().commit(var1);
         } else {
            MDRepositoryFactory.getRepository().rollback(var1);
         }

      } catch (MDRepositoryException var5) {
         throw new EntityException(var5.getMessage());
      }
   }

   private void releaseSessionLock(boolean var1) throws EntityException {
      if (this.session_locked) {
         EntityException var2 = null;

         try {
            this.releaseLock(this.session_lock_id, var1);
         } catch (EntityException var7) {
            var2 = var7;
         } finally {
            if (var2 != null) {
               throw var2;
            }

            this.session_lock_id = this.NOT_SET;
            this.session_locked = false;
         }

      } else {
         throw new EntityException("Nincs folyamtban adat karbantartás!");
      }
   }
}
