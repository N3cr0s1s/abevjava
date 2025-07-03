package hu.piller.enykp.alogic.masterdata.envelope.provider.masterdata;

import hu.piller.enykp.alogic.metainfo.MetaInfo;
import hu.piller.enykp.alogic.metainfo.MetaStore;
import hu.piller.enykp.gui.model.BookModel;
import hu.piller.enykp.interfaces.IDataStore;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;

public class MasterDataProvider {
   public HashMap<EnvelopeMasterData, String> getMasterDataFromCurrentForm(BookModel var1) {
      HashMap var2 = new HashMap();
      EnvelopeMasterData[] var3 = EnvelopeMasterData.values();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         EnvelopeMasterData var6 = var3[var5];
         var2.put(var6, "");
      }

      if (var1 != null) {
         MetaStore var15 = MetaInfo.getInstance().getMetaStore(var1.get_main_formmodel().id);
         if (var15 != null) {
            Vector var16 = new Vector();
            Vector var17 = new Vector();
            Vector var7 = var15.getFilteredFieldMetas_And(new Vector(Arrays.asList("panids")));

            int var11;
            Hashtable var14;
            for(int var8 = 0; var8 < var7.size(); ++var8) {
               var17.clear();
               var14 = (Hashtable)var7.get(var8);
               var17.addAll(Arrays.asList(this.getString(var14.get("panids")).split(",")));
               EnvelopeMasterData[] var9 = EnvelopeMasterData.values();
               int var10 = var9.length;

               for(var11 = 0; var11 < var10; ++var11) {
                  EnvelopeMasterData var12 = var9[var11];
                  if (var17.contains(var12.getKey())) {
                     var16.add(var14);
                     break;
                  }
               }
            }

            IDataStore var18 = var1.get_main_document();
            if (var18 == null) {
               var18 = var1.get_datastore();
            }

            if (var18 != null) {
               for(int var19 = 0; var19 < var16.size(); ++var19) {
                  var14 = (Hashtable)var16.get(var19);
                  var17.clear();
                  var17.addAll(Arrays.asList(this.getString(var14.get("panids")).split(",")));
                  EnvelopeMasterData[] var20 = EnvelopeMasterData.values();
                  var11 = var20.length;

                  for(int var21 = 0; var21 < var11; ++var21) {
                     EnvelopeMasterData var13 = var20[var21];
                     if (var17.contains(var13.getKey())) {
                        var2.put(var13, this.getString(var18.get("0_" + var14.get("fid"))));
                     }
                  }
               }
            }
         }
      }

      return var2;
   }

   private String getString(Object var1) {
      return var1 == null ? "" : var1.toString();
   }
}
