package hu.piller.enykp.alogic.masterdata.sync.logic;

import hu.piller.enykp.alogic.masterdata.core.Block;
import hu.piller.enykp.alogic.masterdata.core.BlockDefinition;
import hu.piller.enykp.alogic.masterdata.core.Entity;
import hu.piller.enykp.alogic.masterdata.repository.MDRepositoryMetaFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

public class SitePairing {
   private Entity e1;
   private Entity e2;
   private List<Integer> e1Sites = new ArrayList();
   private List<Integer> e2Sites = new ArrayList();
   private Map<Integer, Integer> pairedSites = new HashMap();

   public void setEntities(Entity var1, Entity var2) {
      this.e1 = var1;
      this.e2 = var2;
      this.e1Sites.clear();
      this.e2Sites.clear();
      this.pairedSites.clear();
      this.process();
   }

   public Map<Integer, Integer> getPairedSitesSequences() {
      return this.pairedSites;
   }

   public List<Integer> getE1SiteSequences() {
      return this.e1Sites;
   }

   public List<Integer> getE2SiteSequences() {
      return this.e2Sites;
   }

   private void process() {
      try {
         BlockDefinition[] var1 = MDRepositoryMetaFactory.getMDRepositoryMeta().getBlockDefinitionsForEntity(this.e1.getName());
         BlockDefinition[] var2 = var1;
         int var3 = var1.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            BlockDefinition var5 = var2[var4];
            if (var5.getBlockName().equals("Telephelyek")) {
               ArrayList var6 = new ArrayList();
               ArrayList var7 = new ArrayList();
               List var8 = this.sortTelephely(this.e1);
               List var9 = this.sortTelephely(this.e2);
               int[][] var10 = new int[var8.size()][var9.size()];

               int var11;
               int var12;
               for(var11 = 0; var11 < var8.size(); ++var11) {
                  for(var12 = 0; var12 < var9.size(); ++var12) {
                     var10[var11][var12] = this.calcSiteSimilarityScore(this.e1.getBlock("Telephelyek", (Integer)var8.get(var11)), this.e2.getBlock("Telephelyek", (Integer)var9.get(var12)));
                  }
               }

               for(var11 = 0; var11 < var10.length; ++var11) {
                  var12 = this.indexOfMaximumElement(var10[var11]);
                  if (var10[var11][var12] != 0 && this.isMaxIndexInColumn(var10, var11, var12) && !var7.contains(var9.get(var12))) {
                     var6.add(var8.get(var11));
                     var7.add(var9.get(var12));
                     this.pairedSites.put((Integer) var8.get(var11), (Integer) var9.get(var12));
                  }
               }

               var8.removeAll(var6);
               var9.removeAll(var7);

               for(var11 = 0; var11 < var8.size(); ++var11) {
                  this.e1Sites.add((Integer) var8.get(var11));
               }

               for(var11 = 0; var11 < var9.size(); ++var11) {
                  this.e2Sites.add((Integer) var9.get(var11));
               }
            }
         }
      } catch (Exception var13) {
         var13.printStackTrace();
      }

   }

   private boolean isMaxIndexInColumn(int[][] var1, int var2, int var3) {
      int[] var4 = new int[var1.length];

      int var5;
      for(var5 = 0; var5 < var1.length; ++var5) {
         var4[var5] = var1[var5][var3];
      }

      var5 = this.indexOfMaximumElement(var4);
      return var5 == var2;
   }

   private int indexOfMaximumElement(int[] var1) {
      int var2 = 0;
      int var3 = var1[0];

      for(int var4 = 0; var4 < var1.length; ++var4) {
         if (var3 < var1[var4]) {
            var3 = var1[var4];
            var2 = var4;
         }
      }

      return var2;
   }

   private int calcSiteSimilarityScore(Block var1, Block var2) {
      short var3 = 20000;
      short var4 = 5000;
      short var5 = 2500;
      short var6 = 1000;
      byte var7 = 100;
      int var8 = 0;
      if (!var1.getMasterData("T Engedélyszám").getValue().equals("") && !var2.getMasterData("T Engedélyszám").getValue().equals("") && !var1.getMasterData("T Engedélyszám").getValue().equals(var2.getMasterData("T Engedélyszám").getValue())) {
         return var8;
      } else {
         if (!var1.getMasterData("T Engedélyszám").getValue().equals("") && !var2.getMasterData("T Engedélyszám").getValue().equals("") && var1.getMasterData("T Engedélyszám").getValue().equals(var2.getMasterData("T Engedélyszám").getValue())) {
            var8 += var3;
         }

         if (!var1.getMasterData("T Irányítószám").getValue().equals("") && !var2.getMasterData("T Irányítószám").getValue().equals("") && var1.getMasterData("T Irányítószám").getValue().equals(var2.getMasterData("T Irányítószám").getValue())) {
            var8 += var4;
         }

         if (!var1.getMasterData("T Település").getValue().equals("") && !var2.getMasterData("T Település").getValue().equals("") && var1.getMasterData("T Település").getValue().equals(var2.getMasterData("T Település").getValue())) {
            var8 += var5;
         }

         if (!var1.getMasterData("T Közterület neve").getValue().equals("") && !var2.getMasterData("T Közterület neve").getValue().equals("") && var1.getMasterData("T Közterület neve").getValue().equals(var2.getMasterData("T Közterület neve").getValue())) {
            var8 += var6;
         }

         if (!var1.getMasterData("T Házszám").getValue().equals("") && !var2.getMasterData("T Házszám").getValue().equals("") && var1.getMasterData("T Házszám").getValue().equals(var2.getMasterData("T Házszám").getValue())) {
            var8 += var7;
         }

         return var8 <= 1100 ? 0 : var8;
      }
   }

   private List<Integer> sortTelephely(Entity var1) {
      ArrayList var2 = new ArrayList();
      TreeMap var3 = new TreeMap();
      Block[] var4 = var1.getBlocks("Telephelyek");
      int var5 = var4.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         Block var7 = var4[var6];
         SitePairing.SortParams var8 = new SitePairing.SortParams();
         String var9 = var7.getMasterData("T Házszám").getValue().trim().toUpperCase();
         var8.setHzsz(var9.equals("") ? 9999 : Integer.parseInt(var9));
         var9 = var7.getMasterData("T Irányítószám").getValue().trim().toUpperCase();
         var8.setIrsz(var9.equals("") ? 9999 : Integer.parseInt(var9));
         var8.setKoztJell(var7.getMasterData("T Közterület jellege").getValue().trim().toUpperCase());
         var8.setKoztNeve(var7.getMasterData("T Közterület neve").getValue().trim().toUpperCase());
         var8.setTelepules(var7.getMasterData("T Település").getValue().trim().toUpperCase());
         var3.put(var8, var7.getSeq());
      }

      Iterator var10 = var3.entrySet().iterator();

      while(var10.hasNext()) {
         Entry var11 = (Entry)var10.next();
         var2.add(var11.getValue());
      }

      return var2;
   }

   class SortParams implements Comparable<SitePairing.SortParams> {
      private String telepules;
      private String koztNeve;
      private int irsz;
      private String koztJell;
      private int hzsz;

      public int compareTo(SitePairing.SortParams var1) {
         int var2 = this.telepules.compareTo(var1.telepules);
         if (var2 != 0) {
            return var2;
         } else {
            var2 = this.irsz - var1.irsz;
            if (var2 != 0) {
               return var2;
            } else {
               var2 = this.koztNeve.compareTo(this.koztNeve);
               if (var2 != 0) {
                  return var2;
               } else {
                  var2 = this.koztJell.compareTo(var1.getKoztJell());
                  if (var2 != 0) {
                     return var2;
                  } else {
                     var2 = this.hzsz - var1.hzsz;
                     return var2 != 0 ? var2 : 0;
                  }
               }
            }
         }
      }

      String getTelepules() {
         return this.telepules;
      }

      void setTelepules(String var1) {
         this.telepules = var1;
      }

      String getKoztNeve() {
         return this.koztNeve;
      }

      void setKoztNeve(String var1) {
         this.koztNeve = var1;
      }

      int getIrsz() {
         return this.irsz;
      }

      void setIrsz(int var1) {
         this.irsz = var1;
      }

      String getKoztJell() {
         return this.koztJell;
      }

      void setKoztJell(String var1) {
         this.koztJell = var1;
      }

      int getHzsz() {
         return this.hzsz;
      }

      void setHzsz(int var1) {
         this.hzsz = var1;
      }

      public boolean equals(Object var1) {
         if (this == var1) {
            return true;
         } else if (var1 != null && this.getClass() == var1.getClass()) {
            SitePairing.SortParams var2 = (SitePairing.SortParams)var1;
            if (this.hzsz != var2.hzsz) {
               return false;
            } else if (this.irsz != var2.irsz) {
               return false;
            } else if (!this.koztJell.equals(var2.koztJell)) {
               return false;
            } else if (!this.koztNeve.equals(var2.koztNeve)) {
               return false;
            } else {
               return this.telepules.equals(var2.telepules);
            }
         } else {
            return false;
         }
      }

      public int hashCode() {
         int var1 = this.telepules.hashCode();
         var1 = 31 * var1 + this.koztNeve.hashCode();
         var1 = 31 * var1 + this.irsz;
         var1 = 31 * var1 + this.koztJell.hashCode();
         var1 = 31 * var1 + this.hzsz;
         return var1;
      }
   }
}
