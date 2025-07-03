package hu.piller.enykp.alogic.calculator.lookup;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class LookupListHandler {
   private static LookupListHandler instance = new LookupListHandler();
   private Map<String, ILookupListProvider> catalog = new HashMap();

   public static LookupListHandler getInstance() {
      return instance;
   }

   private LookupListHandler() {
   }

   public ILookupListProvider getLookupListProvider(String var1, String var2) {
      ILookupListProvider var3 = (ILookupListProvider)this.catalog.get(this.getHash(var1, var2));
      if (var3 == null) {
         var3 = LookupListFactory.getInstance().createLookupListProvider(var1, var2);
         this.catalog.put(this.getHash(var1, var2), var3);
      }

      return var3;
   }

   private String getHash(String var1, String var2) {
      return var1 + "##" + var2;
   }

   public void release() {
      Iterator var1 = this.catalog.keySet().iterator();

      while(var1.hasNext()) {
         String var2 = (String)var1.next();
         ((ILookupListProvider)this.catalog.get(var2)).release();
      }

      this.catalog = new HashMap();
   }
}
