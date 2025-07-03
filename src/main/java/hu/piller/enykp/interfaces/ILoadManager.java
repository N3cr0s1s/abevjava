package hu.piller.enykp.interfaces;

import hu.piller.enykp.gui.model.BookModel;
import java.io.File;
import java.util.Hashtable;

public interface ILoadManager {
   int DATA_TYPE_ALL = 0;
   int DATA_TYPE_HEAD = 1;

   String getId();

   String getDescription();

   Hashtable getHeadData(File var1);

   BookModel load(String var1, String var2, String var3, String var4);

   BookModel load(String var1, String var2, String var3, String var4, BookModel var5);

   String getFileNameSuffix();

   String createFileName(String var1);
}
