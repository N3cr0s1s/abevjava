package hu.piller.enykp.alogic.calculator.lookup;

import hu.piller.enykp.alogic.calculator.lookup.lookuplistproviderimpl.LookupListProviderCalculator;
import hu.piller.enykp.alogic.calculator.lookup.lookuplistproviderimpl.LookupListProviderGroup;
import hu.piller.enykp.alogic.calculator.lookup.lookuplistproviderimpl.LookupListProviderItem;
import hu.piller.enykp.alogic.metainfo.MetaInfo;
import java.util.Hashtable;

public class LookupListFactory {
   public static final String FIELD_GROUP_ID = "field_group_id";
   public static final String DIN_ERTEK_LISTA = "din_ertek_lista";
   private static LookupListFactory instance = new LookupListFactory();

   public static LookupListFactory getInstance() {
      return instance;
   }

   private LookupListFactory() {
   }

   public ILookupListProvider createLookupListProvider(String var1, String var2) {
      Hashtable var3 = (Hashtable)MetaInfo.getInstance().getMetaStore(var1).getFieldMetas().get(var2);
      if (var3.containsKey("field_group_id")) {
         return new LookupListProviderGroup(var1, var2, var3);
      } else {
         return (ILookupListProvider)(var3.containsKey("din_ertek_lista") ? new LookupListProviderCalculator(var1, var2, var3) : new LookupListProviderItem(var1, var2, var3));
      }
   }
}
