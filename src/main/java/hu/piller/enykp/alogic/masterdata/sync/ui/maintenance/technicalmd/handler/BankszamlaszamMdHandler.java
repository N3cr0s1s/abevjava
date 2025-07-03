package hu.piller.enykp.alogic.masterdata.sync.ui.maintenance.technicalmd.handler;

import hu.piller.enykp.alogic.masterdata.core.Block;
import hu.piller.enykp.alogic.masterdata.core.Entity;
import hu.piller.enykp.alogic.masterdata.sync.ui.maintenance.technicalmd.ITechnicalMdHandler;
import hu.piller.enykp.alogic.masterdata.sync.ui.maintenance.technicalmd.bankszamlaszam.BankszamlaszamFormatter;
import hu.piller.enykp.util.base.ErrorList;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class BankszamlaszamMdHandler implements ITechnicalMdHandler {
   public Map<String, List<String>> split(List<String> var1) {
      Map var2 = this.initSplitted("Pénzintézet neve", "Pénzintézet-azonosító", "Számla-azonosító");
      Iterator var3 = var1.iterator();

      while(true) {
         while(var3.hasNext()) {
            String var4 = (String)var3.next();
            String var5 = "";
            String var6 = "";
            String var7 = "";
            String var8 = var4.replaceAll(" ", "").replaceAll("-", "");
            if (var8.length() == 0) {
               ((List)var2.get("Pénzintézet neve")).add("");
               ((List)var2.get("Pénzintézet-azonosító")).add("");
               ((List)var2.get("Számla-azonosító")).add("");
            } else {
               int var9 = var8.length() - 1;

               int var10;
               for(var10 = 0; var9 >= 0 && (var8.length() < 24 && var10 < 16 || var8.length() >= 24 && var10 < 24) && Character.isDigit(var8.charAt(var9)); --var9) {
                  ++var10;
               }

               ++var9;
               if (var10 != 16 && var10 != 24) {
                  throw new IllegalArgumentException("A bankszámlaszám pénzforgalmi jelzőszám részének 16 vagy 24 számjegyet kell tartalmaznia!");
               }

               var6 = var8.substring(var9, var9 + 8);
               var7 = var8.substring(var9 + 8);
               var5 = var4.substring(0, var4.indexOf(var6));
               ((List)var2.get("Pénzintézet neve")).add(BankszamlaszamFormatter.formatAccountName(var5));
               ((List)var2.get("Pénzintézet-azonosító")).add(BankszamlaszamFormatter.formatRoutingCode(var6));
               ((List)var2.get("Számla-azonosító")).add(BankszamlaszamFormatter.formatAccountId(var7));
            }
         }

         return var2;
      }
   }

   public List<String> build(Entity var1) {
      return this.build(var1.getBlock("Egyéb adatok"));
   }

   public List<String> build(Block var1) {
      ArrayList var2 = new ArrayList();
      if (!"Egyéb adatok".equals(var1.getName())) {
         return var2;
      } else {
         List var3 = var1.getMasterData("Pénzintézet neve").getValues();
         List var4 = var1.getMasterData("Pénzintézet-azonosító").getValues();
         List var5 = var1.getMasterData("Számla-azonosító").getValues();
         if (var3.size() != var4.size() || var3.size() != var5.size() || var4.size() != var5.size()) {
            ErrorList.getInstance().writeError("BankszamlaszamMdHandler", "A Bankszámlát alkotó összetartozó törzsadat értéklisták elemszáma eltérő", (Exception)null, (Object)null);
         }

         for(int var6 = 0; var6 < var3.size(); ++var6) {
            String var7 = var6 < var3.size() ? (String)var3.get(var6) : "";
            String var8 = var6 < var4.size() ? (String)var4.get(var6) : "";
            String var9 = var6 < var5.size() ? (String)var5.get(var6) : "";
            var2.add(BankszamlaszamFormatter.buildFormattedBankAccount(var7, var8, var9));
         }

         return var2;
      }
   }

   private Map<String, List<String>> initSplitted(String... var1) {
      HashMap var2 = new HashMap();
      String[] var3 = var1;
      int var4 = var1.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         String var6 = var3[var5];
         var2.put(var6, new ArrayList());
      }

      return var2;
   }
}
