package hu.piller.enykp.interfaces;

import java.awt.Component;
import java.util.Vector;

public interface IErrorList {
   int IND_ID = 0;
   int IND_MESSAGE = 1;
   int IND_EXCEPTION = 2;
   int IND_DATA = 3;
   int IND_LEVEL = 4;
   Integer LEVEL_MESSAGE = new Integer(0);
   Integer LEVEL_WARNING = new Integer(1);
   Integer LEVEL_ERROR = new Integer(2);
   Integer LEVEL_FATAL_ERROR = new Integer(3);
   Integer LEVEL_SHOW_MESSAGE = new Integer(LEVEL_MESSAGE + 1024);
   Integer LEVEL_SHOW_WARNING = new Integer(LEVEL_WARNING + 1024);
   Integer LEVEL_SHOW_ERROR = new Integer(LEVEL_ERROR + 1024);
   Integer LEVEL_SHOW_FATAL_ERROR = new Integer(LEVEL_FATAL_ERROR + 1024);
   Integer LEVEL_SHOW_GENERIC_ERROR = new Integer(10001);

   boolean store(Object var1, String var2, Exception var3, Object var4);

   boolean store(Object var1, String var2, Integer var3, Exception var4, Object var5, Component var6, String var7);

   boolean store(Object var1, String var2, Integer var3, Exception var4, Object var5, Component var6, String var7, Object var8, Object var9);

   boolean store(Object var1, String var2, Integer var3, Exception var4, Object var5, Component var6, String var7, Object var8, Object var9, boolean var10);

   Object[] getItems();

   Object[] getItems(Object var1);

   Object[] getIdList();

   void clear();

   Object[] removeId(Object var1);

   void remove(Object var1);

   void writeError(Object var1, String var2, Exception var3, Object var4);

   void writeError(Object var1, String var2, Integer var3, Exception var4, Object var5);

   void writeError(Object var1, String var2, Integer var3, Exception var4, Object var5, Object var6, Object var7);

   void writeError(Object var1, String var2, Integer var3, Exception var4, Object var5, Object var6, Object var7, boolean var8);

   void writeError(Object var1, String var2, Integer var3, Exception var4, Object var5, Component var6, String var7);

   Vector<String> getErrorCodeList();

   void setErrorCodes(Vector<String> var1);
}
