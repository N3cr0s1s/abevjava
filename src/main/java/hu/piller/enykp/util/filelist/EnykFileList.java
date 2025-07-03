package hu.piller.enykp.util.filelist;

import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.interfaces.ILoadManager;
import hu.piller.enykp.interfaces.ISaveManager;
import java.util.Hashtable;

public class EnykFileList {
   public static final String FILE_LIST_FILE = "enyk_dirlist";
   public static final int UNKNOWN_FILE_MANAGER = -1;
   public static final int READ_FILE_MANAGER = 1;
   public static final int SAVE_FILE_MANAGER = 2;
   private FileList fileList;
   private IFileInfo[] allFileInfos;
   private static EnykFileList instance;
   public static ILoadManager[] readfileInfos = GuiUtil.getLoadManagers();
   private static Hashtable readfileInfoTable;
   private static ISaveManager[] savefileInfos;
   private static Hashtable savefileInfoTable;

   public static EnykFileList getInstance() {
      if (instance == null) {
         for(int var0 = 0; var0 < readfileInfos.length; ++var0) {
            ILoadManager var1 = readfileInfos[var0];
            readfileInfoTable.put(var1.getId(), var1);
         }

         instance = new EnykFileList();
      }

      return instance;
   }

   private EnykFileList() {
      this.allFileInfos = this.createFileInfoItems(readfileInfos);
      this.fileList = new FileList("enyk_dirlist", this.allFileInfos);
   }

   public Object getFileManagerInstance(String var1, int var2) {
      return var2 == 1 ? readfileInfoTable.get(var1) : savefileInfoTable.get(var1);
   }

   public int getFilterType(String var1) {
      if (readfileInfoTable.containsKey(var1)) {
         return 1;
      } else {
         return savefileInfoTable.containsKey(var1) ? 2 : -1;
      }
   }

   public synchronized Object[] list(String var1, Object var2) {
      return this.fileList.list(var1, this.createFileInfoItems(var2));
   }

   private IFileInfo[] createFileInfoItems(Object var1) {
      IFileInfo[] var2 = null;
      if (var1 == null) {
         return var2;
      } else {
         if (var1 instanceof Object[]) {
            Object[] var3 = (Object[])((Object[])var1);
            if (var3.length > 0) {
               var2 = new IFileInfo[var3.length];

               for(int var4 = 0; var4 < var3.length; ++var4) {
                  ILoadManager var5;
                  if (var3[var4] instanceof ILoadManager) {
                     var5 = (ILoadManager)var3[var4];
                     var2[var4] = new EnykFileInfo(var5);
                  } else if (var3[var4] instanceof String) {
                     var5 = (ILoadManager)readfileInfoTable.get(var3[var4]);
                     var2[var4] = new EnykFileInfo(var5);
                  }
               }
            }
         }

         return var2;
      }
   }

   static {
      readfileInfoTable = new Hashtable(readfileInfos.length);
      savefileInfos = new ISaveManager[0];
      savefileInfoTable = new Hashtable(savefileInfos.length);
   }
}
