package hu.piller.enykp.util.filelist;

import hu.piller.enykp.datastore.CachedCollection;
import hu.piller.enykp.interfaces.ILoadManager;
import java.io.File;

public class EnykFileInfo implements IFileInfo {
   ILoadManager filter = null;
   boolean newObject = false;

   public EnykFileInfo(ILoadManager var1) {
      this.filter = var1;
      if (var1 instanceof CachedCollection) {
         this.newObject = true;
      }

   }

   public Object getFileInfo(File var1) {
      try {
         if (this.newObject) {
            this.filter = new CachedCollection();
         }

         return this.filter.getHeadData(var1);
      } catch (Exception var3) {
         var3.printStackTrace();
         return null;
      }
   }

   public String getFileInfoId() {
      return this.filter.getId();
   }

   public Object getFileInfoObject() {
      return this.filter;
   }
}
