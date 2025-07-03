package hu.piller.enykp.alogic.primaryaccount.common;

import java.util.Hashtable;

public class DefaultRecordFactoryStore {
   private static final Hashtable factories = new Hashtable(8);

   public static void addRecordFactory(IRecordFactory var0) {
      if (var0 != null && factories.get(var0) == null) {
         factories.put(var0, "");
      }

   }

   public static Hashtable getFactories() {
      return factories;
   }
}
