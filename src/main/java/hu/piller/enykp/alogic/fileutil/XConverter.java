package hu.piller.enykp.alogic.fileutil;

import hu.piller.enykp.datastore.Elem;
import hu.piller.enykp.datastore.StoreItem;
import hu.piller.enykp.gui.model.BookModel;
import hu.piller.enykp.gui.model.DataFieldModel;
import hu.piller.enykp.interfaces.IDataStore;
import java.util.Hashtable;
import java.util.Iterator;

public class XConverter {
   public static void convert(BookModel var0) {
      int var1 = var0.cc.size();

      for(int var2 = 0; var2 < var1; ++var2) {
         Elem var3 = (Elem)var0.cc.get(var2);
         String var4 = var3.getType();
         Hashtable var5 = var0.get(var4).fids;
         IDataStore var6 = (IDataStore)var3.getRef();
         Iterator var7 = var6.getCaseIdIterator();

         while(var7.hasNext()) {
            StoreItem var8 = (StoreItem)var7.next();
            if (var8.value != null && !var8.value.equals("") && var8.index >= 0 && ((String)var8.value).toLowerCase().equals("x")) {
               Hashtable var9 = ((DataFieldModel)var5.get(var8.code)).features;
               if (((String)var9.get("datatype")).equalsIgnoreCase("check")) {
                  var6.set(var8.toString(), "true");
               }
            }
         }
      }

   }
}
