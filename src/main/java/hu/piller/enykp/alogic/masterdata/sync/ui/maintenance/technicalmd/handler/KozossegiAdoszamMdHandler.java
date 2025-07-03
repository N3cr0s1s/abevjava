package hu.piller.enykp.alogic.masterdata.sync.ui.maintenance.technicalmd.handler;

import hu.piller.enykp.alogic.masterdata.core.Block;
import hu.piller.enykp.alogic.masterdata.core.Entity;
import hu.piller.enykp.alogic.masterdata.gui.MDGUIFieldFactory;
import hu.piller.enykp.alogic.masterdata.sync.ui.maintenance.technicalmd.ITechnicalMdHandler;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KozossegiAdoszamMdHandler implements ITechnicalMdHandler {
   public Map<String, List<String>> split(List<String> var1) {
      HashMap var2 = new HashMap();
      var2.put("Közösségi adószám ország kód", Arrays.asList(""));
      var2.put("Közösségi adószám", Arrays.asList(""));
      if (var1.size() > 0) {
         String var3 = (String)var1.get(0);
         if (var3 != null && !"".equals(var3.trim())) {
            String var4 = "";
            String var5 = "";
            if (var3.length() >= 2) {
               var4 = var3.substring(0, 2);
               boolean var6 = false;
               String[] var7 = MDGUIFieldFactory.OPCIOK_ORSZAG_ISO;
               int var8 = var7.length;

               for(int var9 = 0; var9 < var8; ++var9) {
                  String var10 = var7[var9];
                  if (var10.equals(var4)) {
                     var6 = true;
                     break;
                  }
               }

               if (var6) {
                  var5 = var3.substring(2);
               } else {
                  var4 = "";
                  var5 = var3;
               }
            }

            ((List)var2.get("Közösségi adószám ország kód")).set(0, var4);
            ((List)var2.get("Közösségi adószám")).set(0, var5);
         }
      }

      return var2;
   }

   public List<String> build(Entity var1) {
      Object var2 = new ArrayList();
      if ("Társaság".equals(var1.getName()) || "Egyéni vállalkozó".equals(var1.getName())) {
         var2 = this.build(var1.getBlock("Törzsadatok"));
      }

      return (List)var2;
   }

   public List<String> build(Block var1) {
      ArrayList var2 = new ArrayList();
      if (!"Törzsadatok".equals(var1.getName())) {
         return var2;
      } else {
         List var3 = var1.getMasterData("Közösségi adószám ország kód").getValues();
         List var4 = var1.getMasterData("Közösségi adószám").getValues();
         if (var3.size() == var4.size()) {
            for(int var5 = 0; var5 < var3.size(); ++var5) {
               var2.add((String)var3.get(var5) + (String)var4.get(var5));
            }
         }

         return var2;
      }
   }
}
