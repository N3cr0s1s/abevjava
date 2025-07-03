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

public class TitulusVezetekNevMdHandler implements ITechnicalMdHandler {
   public Map<String, List<String>> split(List<String> var1) {
      HashMap var2 = new HashMap();
      var2.put("Név titulus", Arrays.asList(""));
      var2.put("Vezetékneve", Arrays.asList(""));
      String var3 = "";
      if (var1.size() > 0) {
         String var4 = "";
         String var5 = (String)var1.get(0);
         if (var5 != null && !"".equals(var5.trim())) {
            var5 = var5.toUpperCase().trim();
            String[] var6 = MDGUIFieldFactory.OPCIOK_TITULUS;
            int var7 = var6.length;

            for(int var8 = 0; var8 < var7; ++var8) {
               String var9 = var6[var8];
               if (var5.startsWith(var9.toUpperCase()) && var9.length() > var3.length()) {
                  int var10 = ((String)var1.get(0)).toUpperCase().indexOf(var9.toUpperCase());
                  String var11 = ((String)var1.get(0)).substring(var10 + var9.length()).trim();
                  ((List)var2.get("Név titulus")).set(0, var9.trim());
                  ((List)var2.get("Vezetékneve")).set(0, var11.trim());
                  var3 = var9;
               }
            }
         }

         if ("".equals(((List)var2.get("Név titulus")).get(0)) && "".equals(((List)var2.get("Vezetékneve")).get(0))) {
            ((List)var2.get("Vezetékneve")).set(0, ((String)var1.get(0)).trim());
         }
      }

      return var2;
   }

   public List<String> build(Entity var1) {
      ArrayList var2 = new ArrayList();
      if ("Egyéni vállalkozó".equals(var1.getName()) || "Magánszemély".equals(var1.getName())) {
         var2.addAll(this.build(var1.getBlock("Törzsadatok")));
      }

      return var2;
   }

   public List<String> build(Block var1) {
      ArrayList var2 = new ArrayList();
      if (!"Törzsadatok".equals(var1.getName())) {
         return var2;
      } else {
         String var3 = var1.getMasterData("Név titulus").getValue();
         String var4 = var1.getMasterData("Vezetékneve").getValue();
         if (!"".equals(var3.trim()) && !"".equals(var4)) {
            var2.add(var3 + " " + var4);
         } else if ("".equals(var3.trim())) {
            var2.add(var4);
         } else if ("".equals(var4.trim())) {
            var2.add(var3);
         } else {
            var2.add("");
         }

         return var2;
      }
   }
}
