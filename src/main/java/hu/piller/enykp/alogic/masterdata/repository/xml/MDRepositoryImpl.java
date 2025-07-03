package hu.piller.enykp.alogic.masterdata.repository.xml;

import hu.piller.enykp.alogic.masterdata.core.Block;
import hu.piller.enykp.alogic.masterdata.core.Entity;
import hu.piller.enykp.alogic.masterdata.core.MasterData;
import hu.piller.enykp.alogic.masterdata.repository.MDRepository;
import hu.piller.enykp.alogic.masterdata.repository.MDRepositoryException;
import hu.piller.enykp.alogic.masterdata.repository.MDRepositoryMetaFactory;
import hu.piller.enykp.gui.framework.MainFrame;
import hu.piller.enykp.util.base.PropertyList;
import hu.piller.enykp.util.base.Tools;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileLock;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

public class MDRepositoryImpl implements MDRepository {
   private static long NO_LOCK;
   private long lock_id;
   private long sequence;
   private Hashtable<Long, Entity> repository;
   private Hashtable<Long, Entity> undoModifiedDeleted;
   private Hashtable<Long, Entity> undoNew;
   private String pathRepoSchema;
   private String pathRepo;
   private String pathBackup;
   private FileLock fileLock;
   private RandomAccessFile rafRepo;
   private File lockFile;
   private long last_modified;
   private long last_length;
   private boolean dirty;
   public static final int NOLOCK = 0;
   public static final int FILELOCK = 1;
   public static final int NATIVELOCK = 2;

   public MDRepositoryImpl(String var1, String var2) throws MDRepositoryException {
      try {
         this.pathRepoSchema = var1;
         this.pathRepo = var2;
         MDRepositoryMetaFactory.getMDRepositoryMeta();
         this.loadRepository();
         this.undoModifiedDeleted = new Hashtable();
         this.undoNew = new Hashtable();
         NO_LOCK = -1L * (long)(Math.random() * 1.0E8D);
         this.lock_id = NO_LOCK;
      } catch (Exception var5) {
         String var4 = "A  törzsadattár nem betölthető: " + (var5.getMessage() == null ? " rendszerhiba" : var5.getMessage());
         this.writeError(var4);
         throw new MDRepositoryException(var4);
      }
   }

   public synchronized long getNextEntityID() {
      return (long)(this.sequence++);
   }

   public synchronized void sync() throws MDRepositoryException {
      if (this.lock_id == NO_LOCK) {
         File var1 = new File(this.pathRepo);
         if (!var1.exists()) {
            throw new MDRepositoryException("A(z) " + this.pathRepo + " adatfájl nem található!");
         } else {
            if (var1.length() != this.last_length || var1.lastModified() != this.last_modified) {
               try {
                  this.loadRepository();
               } catch (Exception var3) {
                  throw new MDRepositoryException(var3.getMessage());
               }

               this.undoModifiedDeleted.clear();
               this.undoNew.clear();
            }

         }
      }
   }

   public synchronized void delete(long var1, long var3) throws MDRepositoryException {
      this.validateLockId(var3);
      Entity var5 = this.findById(var1);
      if (var5 == null) {
         throw new MDRepositoryException("A(z) '" + var1 + "' elem nincs a tárban!");
      } else {
         if (!this.undoModifiedDeleted.containsKey(var1)) {
            try {
               this.undoModifiedDeleted.put(var1, (Entity)var5.clone());
            } catch (CloneNotSupportedException var7) {
            }
         }

         this.repository.remove(var1);
         this.dirty = true;
      }
   }

   public synchronized Entity[] findByTypeAndContent(String var1, MasterData[] var2) {
      Entity[] var3 = new Entity[0];
      if (!this.repository.isEmpty()) {
         Iterator var4 = this.repository.values().iterator();

         while(var4.hasNext()) {
            Entity var5 = (Entity)var4.next();
            Block[] var6 = var5.getAllBlocks();
            boolean[] var7 = new boolean[var2.length];

            int var8;
            for(var8 = 0; var8 < var7.length; ++var8) {
               var7[var8] = false;
            }

            for(var8 = 0; var8 < var6.length; ++var8) {
               for(int var9 = 0; var9 < var2.length; ++var9) {
                  if (!var7[var9] && var6[var8].hasKey(var2[var9].getKey()) && this.matches(var2[var9], var6[var8].getMasterData(var2[var9].getKey()))) {
                     var7[var9] = true;
                  }
               }
            }

            boolean var11 = var1 == null || "*".equals(var1) || var5.getName().equals(var1);
            if (var11 && this.checkMatchesAND(var7)) {
               try {
                  var3 = this.append(var3, (Entity)var5.clone());
               } catch (CloneNotSupportedException var10) {
               }
            }
         }
      }

      return var3;
   }

   public synchronized Entity findById(long var1) {
      Entity var3 = null;
      if (this.repository.containsKey(var1)) {
         try {
            var3 = (Entity)((Entity)this.repository.get(var1)).clone();
         } catch (CloneNotSupportedException var5) {
         }
      }

      return var3;
   }

   public synchronized void store(Entity var1, long var2) throws MDRepositoryException {
      this.validateLockId(var2);
      var1.validate();
      if (var1.getValidityStatus().length != 0) {
         throw new MDRepositoryException("A menteni kívánt törzsadat inkonzisztens!");
      } else {
         try {
            if (!this.repository.containsKey(var1.getId())) {
               this.undoNew.put(var1.getId(), (Entity)var1.clone());
            }

            Entity var4 = (Entity)this.repository.put(var1.getId(), (Entity)var1.clone());
            if (var4 != null && !this.undoModifiedDeleted.containsKey(var4.getId())) {
               this.undoModifiedDeleted.put(var4.getId(), var4);
            }
         } catch (CloneNotSupportedException var5) {
         }

         this.dirty = true;
      }
   }

   public synchronized long begin() throws MDRepositoryException {
      this.validateLockId(NO_LOCK);
      this.lockRepositoryFile();
      this.backupLockedRepositoryFile();
      this.lock_id = (long)(Math.random() * 1.0E9D);
      this.dirty = false;
      return this.lock_id;
   }

   public synchronized void commit(long var1) throws MDRepositoryException {
      boolean var3 = false;
      this.validateLockId(var1);

      try {
         if (this.dirty) {
            String var4 = this.convertRepoContentToXML();
            if ((long)var4.length() < this.rafRepo.length()) {
               this.rafRepo.getChannel().truncate((long)var4.length());
            }

            this.rafRepo.seek(0L);
            this.rafRepo.write(var4.getBytes("UTF-8"));
         }
      } catch (Exception var9) {
         var3 = true;
         StringBuilder var5 = new StringBuilder("Nem sikerült a módosítások mentése.\n");
         if (var9.getMessage() != null) {
            var5.append("Oka: ");
            var5.append(var9.getMessage());
         }

         var5.append("\n");
         var5.append("Próbálkozzon újra. Az adattár utolsó érvényes állapota a(z) \n'");
         var5.append(this.pathBackup);
         var5.append("'\n biztonsági mentés fájlban található.");
         throw new MDRepositoryException(var5.toString());
      } finally {
         if (!var3) {
            this.unlockRepositoryFile();
            if (!this.removeBackupFile()) {
               this.writeError("A(z) '" + this.pathBackup + "' biztonsági mentés nem törölhető, kérem tegye meg kézzel!");
            }

            this.undoModifiedDeleted.clear();
            this.undoNew.clear();
            this.lock_id = NO_LOCK;
         }

      }

   }

   public synchronized void rollback(long var1) throws MDRepositoryException {
      this.validateLockId(var1);
      this.unlockRepositoryFile();
      if (!this.removeBackupFile()) {
         this.writeError("A " + this.pathBackup + " biztonsági mentés nem törölhető, kérem tegye meg kézzel!");
      }

      Iterator var3;
      long var4;
      if (!this.undoModifiedDeleted.isEmpty()) {
         var3 = this.undoModifiedDeleted.keySet().iterator();

         while(var3.hasNext()) {
            var4 = (Long)var3.next();
            Entity var6 = (Entity)this.undoModifiedDeleted.get(var4);
            this.repository.put(var4, var6);
         }

         this.undoModifiedDeleted.clear();
      }

      if (!this.undoNew.isEmpty()) {
         var3 = this.undoNew.keySet().iterator();

         while(var3.hasNext()) {
            var4 = (Long)var3.next();
            this.repository.remove(var4);
         }

         this.undoNew.clear();
      }

      this.lock_id = NO_LOCK;
   }

   private void loadRepository() throws Exception {
      File var1 = new File(this.pathRepo);
      if (var1.exists()) {
         SAXParserFactory var2 = SAXParserFactory.newInstance();
         var2.setNamespaceAware(true);
         var2.setValidating(false);
         SchemaFactory var3 = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
         var2.setSchema(var3.newSchema(new Source[]{new StreamSource(new File(this.pathRepoSchema))}));
         SAXParser var4 = var2.newSAXParser();
         XMLReader var5 = var4.getXMLReader();
         EntityHandler var6 = new EntityHandler();
         var5.setContentHandler(var6);
         var5.parse(new InputSource(new FileInputStream(var1)));
         if (!"".equals(var6.getErrorMsg())) {
            this.writeError(var6.getErrorMsg());
            throw new Exception("inkonzisztens adatbázis, nézze meg a naplót!");
         }

         this.repository = var6.getEntities();
         this.sequence = var6.getSequence();
         this.last_modified = var1.lastModified();
         this.last_length = var1.length();
      } else {
         this.repository = new Hashtable();
      }

   }

   private File checkRepositoryAvailableForTransaction() throws MDRepositoryException {
      String var1 = "";
      File var2 = new File(this.pathRepo);
      if (!var2.exists()) {
         return var2;
      } else {
         if (!var2.canWrite()) {
            var1 = "Az adattár nem módosítható, lehet hogy más szerkeszti, vagy nincs megfelelő jogosultsága!";
         }

         if (!"".equals(var1)) {
            throw new MDRepositoryException(var1);
         } else {
            return var2;
         }
      }
   }

   private void lockRepositoryFile() throws MDRepositoryException {
      File var1 = this.checkRepositoryAvailableForTransaction();
      boolean var2 = false;

      try {
         switch(getLockPolicy()) {
         case 1:
            if ((new File(getLockfileName())).exists()) {
               throw new Exception("az adattár zárolt");
            }

            (new File(getLockfileName())).createNewFile();
            MainFrame.masterDataLockCleanUpNeeded = true;
            this.rafRepo = new RandomAccessFile(var1, "rw");
            break;
         case 2:
            this.rafRepo = new RandomAccessFile(var1, "rw");
            this.fileLock = this.rafRepo.getChannel().tryLock();
            if (this.fileLock == null) {
               var2 = true;
               throw new Exception("az adattár zárolt!");
            }
            break;
         default:
            this.rafRepo = new RandomAccessFile(var1, "rw");
         }
      } catch (Exception var11) {
         var2 = true;
         throw new MDRepositoryException("Az adattár zárolása nem végezhető el: " + var11.getMessage());
      } finally {
         if (var2) {
            MainFrame.masterDataLockCleanUpNeeded = false;
            if (this.rafRepo != null) {
               try {
                  this.rafRepo.close();
               } catch (IOException var10) {
                  Tools.eLog(var10, 0);
               }

               this.rafRepo = null;
            }
         }

      }

   }

   private void unlockRepositoryFile() throws MDRepositoryException {
      if (this.rafRepo != null) {
         try {
            if (this.rafRepo.length() == 0L) {
               String var1 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><Repository sequence=\"0\"/>";
               this.rafRepo.seek(0L);
               this.rafRepo.write(var1.getBytes("UTF-8"));
            }

            this.rafRepo.close();
            this.rafRepo = null;
            File var7 = new File(this.pathRepo);
            this.last_length = var7.length();
            this.last_modified = var7.lastModified();
         } catch (IOException var5) {
            var5.printStackTrace();
            throw new MDRepositoryException("A törzsadattár zárolás nem oldható fel: " + var5.getMessage());
         } finally {
            if (getLockPolicy() == 1 && (new File(getLockfileName())).exists()) {
               (new File(getLockfileName())).delete();
               MainFrame.masterDataLockCleanUpNeeded = false;
            }

         }
      }

   }

   private void backupLockedRepositoryFile() throws MDRepositoryException {
      StringBuilder var1 = new StringBuilder();
      StringBuilder var2 = new StringBuilder("");

      try {
         byte[] var3 = new byte[(int)this.rafRepo.length()];
         this.rafRepo.readFully(var3);
         var2.append(new String(var3));
         var1.append(this.pathRepo);
         var1.append(".");
         var1.append(System.currentTimeMillis() + (long)(Math.random() * 1000.0D));
         File var4 = new File(var1.toString());
         FileOutputStream var5 = new FileOutputStream(var4);
         var5.write(var3, 0, var3.length);
         var5.flush();
         var5.close();
         this.pathBackup = var4.getAbsolutePath();
      } catch (IOException var7) {
         try {
            this.rafRepo.close();
         } catch (IOException var6) {
            this.writeError(var6.getMessage());
         }

         throw new MDRepositoryException("A biztonsági mentés nem hozható létre: " + var7.getMessage());
      }
   }

   private boolean removeBackupFile() {
      boolean var1 = false;
      File var2 = new File(this.pathBackup);
      if (var2.exists() && var2.isFile()) {
         var1 = var2.delete();
      }

      return var1;
   }

   private String convertRepoContentToXML() {
      StringBuilder var1 = new StringBuilder();
      var1.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
      var1.append("<Repository");
      if (this.sequence > 0L) {
         var1.append(" sequence=\"");
         var1.append(this.sequence);
         var1.append("\"");
      }

      var1.append(">");
      Iterator var2 = this.repository.values().iterator();

      while(var2.hasNext()) {
         Entity var3 = (Entity)var2.next();
         var1.append(var3.toXmlString());
      }

      var1.append("</Repository>");
      return var1.toString();
   }

   private void validateLockId(long var1) throws MDRepositoryException {
      if (this.lock_id != var1) {
         throw new MDRepositoryException("Az adattárat más zárolta!");
      }
   }

   private boolean matches(MasterData var1, MasterData var2) {
      return var1.getValue().equals("*") ? true : var1.getValue().trim().equals(var2.getValue().trim());
   }

   private Entity[] append(Entity[] var1, Entity var2) {
      Entity[] var3 = new Entity[var1.length + 1];

      int var4;
      for(var4 = 0; var4 < var1.length; ++var4) {
         var3[var4] = var1[var4];
      }

      var3[var4] = var2;
      return var3;
   }

   private boolean checkMatchesAND(boolean[] var1) {
      for(int var2 = 0; var2 < var1.length; ++var2) {
         if (!var1[var2]) {
            return false;
         }
      }

      return true;
   }

   private void writeError(String var1) {
      System.err.println(var1);
   }

   public static int getLockPolicy() {
      Vector var0 = (Vector)PropertyList.getInstance().get("prop.const.mdlock");
      if (var0 == null) {
         return 1;
      } else {
         String var1 = (String)var0.get(0);
         if (var1 != null && !"".equals(var1) && !"F".equalsIgnoreCase(var1)) {
            return "N".equalsIgnoreCase(var1) ? 2 : 0;
         } else {
            return 1;
         }
      }
   }

   public static String getLockfileName() {
      return PropertyList.getInstance().get("prop.usr.primaryaccounts") + File.separator + "repo.lck";
   }
}
