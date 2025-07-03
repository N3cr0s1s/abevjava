package hu.piller.enykp.alogic.masterdata.sync.logic;

import hu.piller.enykp.alogic.masterdata.core.Block;
import hu.piller.enykp.alogic.masterdata.core.BlockDefinition;
import hu.piller.enykp.alogic.masterdata.core.Entity;
import hu.piller.enykp.alogic.masterdata.repository.MDRepositoryMetaFactory;
import hu.piller.enykp.alogic.masterdata.sync.configuration.ConfigService;
import hu.piller.enykp.alogic.masterdata.sync.ui.maintenance.technicalmd.ITechnicalMdHandler;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class EntityComparsion {
   private static final boolean IS_DIFFERENT = true;
   private static final boolean IS_NOT_DIFFERENT = false;
   private BlockDefinition[] maganszemelyDef;
   private BlockDefinition[] egyeniVallalkozoDef;
   private BlockDefinition[] szervezetDef;
   private Properties config;

   public EntityComparsion() {
      try {
         this.szervezetDef = MDRepositoryMetaFactory.getMDRepositoryMeta().getBlockDefinitionsForEntity("Társaság");
         this.egyeniVallalkozoDef = MDRepositoryMetaFactory.getMDRepositoryMeta().getBlockDefinitionsForEntity("Egyéni vállalkozó");
         this.maganszemelyDef = MDRepositoryMetaFactory.getMDRepositoryMeta().getBlockDefinitionsForEntity("Magánszemély");
      } catch (Exception var2) {
         var2.printStackTrace();
      }

   }

   private BlockDefinition[] getBlockDefinitionsForEntityType(String var1) throws EntityComparsionException {
      if ("Magánszemély".equals(var1)) {
         return this.maganszemelyDef;
      } else if ("Egyéni vállalkozó".equals(var1)) {
         return this.egyeniVallalkozoDef;
      } else if ("Társaság".equals(var1)) {
         return this.szervezetDef;
      } else {
         throw new EntityComparsionException("Ismeretlen adóalany kategória: " + var1);
      }
   }

   public boolean hasDifferentData(Entity var1, Entity var2) throws EntityComparsionException {
      boolean var3 = false;
      if (!var1.getName().equals(var2.getName())) {
         throw new EntityComparsionException("e1.nev=" + var1.getName() + " nem egyezik e2.nev=" + var2.getName());
      } else {
         this.config = (new ConfigService()).loadConfig(var1.getName());
         BlockDefinition[] var4 = this.getBlockDefinitionsForEntityType(var1.getName());
         int var5 = var4.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            BlockDefinition var7 = var4[var6];
            String[] var8 = var7.getMasterDataNames();
            Block var9 = var1.getBlock(var7.getBlockName());
            Block var10 = var2.getBlock(var7.getBlockName());
            if (this.isDifferent(var8, var9, var10)) {
               var3 = true;
               break;
            }
         }

         return var3;
      }
   }

   private boolean isDifferent(String[] var1, Block var2, Block var3) {
      ArrayList var4 = new ArrayList();
      String[] var5 = var1;
      int var6 = var1.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         String var8 = var5[var7];
         if ("y".equals(this.config.getProperty(var2.getName() + "." + var8)) && !var4.contains(var8)) {
            String var9 = (String)ConfigService.panidsToTechnicalMd.get(var8);
            if (var9 == null) {
               var9 = var8;
            }

            List var10 = (List)ConfigService.technicalMdToPanids.get(var9);
            if (var10 != null) {
               var4.addAll(var10);
               List var13 = ((ITechnicalMdHandler)ConfigService.technicalMdHandlers.get(var9)).build(var2);
               List var14 = ((ITechnicalMdHandler)ConfigService.technicalMdHandlers.get(var9)).build(var3);
               if (var13.size() != var14.size()) {
                  return true;
               }

               var14.removeAll(var13);
               if (var14.size() > 0) {
                  return true;
               }
            } else {
               if (var3.getMasterData(var9).getValues().size() > 1) {
                  return true;
               }

               String var11 = this.processLocalValue(var9, var2.getMasterData(var9).getValue());

               try {
                  String var12 = var3.getMasterData(var9).getValue();
                  if (!var11.replaceAll(" ", "").equalsIgnoreCase(var12.replaceAll(" ", ""))) {
                     return true;
                  }
               } catch (Exception var15) {
                  System.out.println();
               }
            }
         }
      }

      return false;
   }

   private String processLocalValue(String var1, String var2) {
      return ("VPID".equals(var1) || "Engedélyszám".equals(var1)) && "HU".equals(var2.trim()) ? "" : var2;
   }
}
