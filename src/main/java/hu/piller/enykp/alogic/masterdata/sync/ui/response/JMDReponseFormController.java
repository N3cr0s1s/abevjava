package hu.piller.enykp.alogic.masterdata.sync.ui.response;

import hu.piller.enykp.alogic.masterdata.core.Entity;
import hu.piller.enykp.alogic.masterdata.core.EntityException;
import hu.piller.enykp.alogic.masterdata.core.EntityHome;
import hu.piller.enykp.alogic.masterdata.core.MasterData;
import hu.piller.enykp.alogic.masterdata.sync.download.MasterDataDownload;
import hu.piller.enykp.alogic.masterdata.sync.download.MasterDataDownloadException;
import hu.piller.enykp.alogic.masterdata.sync.download.MasterDataDownloadResultStatus;
import hu.piller.enykp.alogic.masterdata.sync.logic.EntityComparsion;
import hu.piller.enykp.alogic.masterdata.sync.logic.EntityComparsionException;
import hu.piller.enykp.error.EnykpTechnicalException;
import hu.piller.enykp.util.base.ErrorList;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class JMDReponseFormController {
   private Map<String, Entity> entities;
   private EntityComparsion entityComparator;
   private List<String> missingIds = new ArrayList();
   private boolean hasError;
   private boolean hasNoData;

   public JMDReponseFormController() {
      this.loadLocalEntities();
      this.entityComparator = new EntityComparsion();
   }

   public void loadLocalEntities() {
      try {
         Entity[] var1 = EntityHome.getInstance().findByTypeAndMasterData("*", new MasterData[0]);
         this.entities = new HashMap(var1.length);
         Entity[] var2 = var1;
         int var3 = var1.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            Entity var5 = var2[var4];
            String var6;
            if (!"Társaság".equals(var5.getName()) && !"Egyéni vállalkozó".equals(var5.getName())) {
               if (!"Magánszemély".equals(var5.getName())) {
                  continue;
               }

               var6 = var5.getBlock("Törzsadatok").getMasterData("Adózó adóazonosító jele").getValue();
            } else {
               try {
                  var6 = var5.getBlock("Törzsadatok").getMasterData("Adózó adószáma").getValue().substring(0, 8);
               } catch (Exception var8) {
                  continue;
               }
            }

            this.entities.put(var6, var5);
         }
      } catch (EntityException var9) {
         var9.printStackTrace();
      }

   }

   public Object[][] getResults() {
      this.hasError = false;
      this.hasNoData = false;
      this.missingIds.clear();
      ArrayList var1 = new ArrayList();
      ArrayList var2 = new ArrayList();

      try {
         Map var3 = MasterDataDownload.getInstance().getResultInfo();
         Iterator var4 = var3.entrySet().iterator();

         while(var4.hasNext()) {
            Entry var5 = (Entry)var4.next();
            if (this.entities.containsKey(var5.getKey())) {
               Entity var6 = (Entity)this.entities.get(var5.getKey());
               String var7;
               String var8;
               if ("Társaság".equals(var6.getName())) {
                  var7 = var6.getBlock("Törzsadatok").getMasterData("Adózó neve").getValue();
                  var8 = var6.getBlock("Törzsadatok").getMasterData("Adózó adószáma").getValue();
               } else if ("Egyéni vállalkozó".equals(var6.getName())) {
                  var7 = var6.getBlock("Törzsadatok").getMasterData("Vezetékneve").getValue() + " " + var6.getBlock("Törzsadatok").getMasterData("Keresztneve").getValue();
                  var8 = var6.getBlock("Törzsadatok").getMasterData("Adózó adószáma").getValue();
               } else {
                  var7 = var6.getBlock("Törzsadatok").getMasterData("Vezetékneve").getValue() + " " + var6.getBlock("Törzsadatok").getMasterData("Keresztneve").getValue();
                  var8 = var6.getBlock("Törzsadatok").getMasterData("Adózó adóazonosító jele").getValue();
               }

               Object[] var9 = new Object[]{var5.getValue() == MasterDataDownloadResultStatus.SUCCESS ? "Siker" : "Hiba", var6.getName(), var7, var8, null, null};

               try {
                  if (var5.getValue() == MasterDataDownloadResultStatus.SUCCESS) {
                     try {
                        if (this.entityComparator.hasDifferentData(var6, MasterDataDownload.getInstance().getDownloadedEntity((String)var5.getKey()))) {
                           var9[4] = "Karbantartás";
                        } else if ("Siker".equals(var9[0])) {
                           var9[4] = "nincs eltérés";
                        } else {
                           var9[4] = "";
                        }
                     } catch (EntityComparsionException var11) {
                        var2.add((String)var5.getKey() + " : " + var11.getMessage());
                        var9[4] = "N/A";
                        this.hasError = true;
                     }
                  } else {
                     var9[4] = "";
                  }
               } catch (MasterDataDownloadException var12) {
                  var2.add((String)var5.getKey() + " : " + var12.getMessage());
                  var9[4] = "N/A";
                  this.hasError = true;
               }

               var9[5] = Boolean.FALSE;
               if (!"N/A".equals(var9[4])) {
                  var1.add(var9);
               }
            } else {
               this.missingIds.add((String) var5.getKey());
               if (!this.hasError) {
                  this.hasError = true;
               }
            }
         }

         if (var3.size() - var2.size() == 0) {
            this.hasNoData = true;
         }
      } catch (MasterDataDownloadException var13) {
         var13.printStackTrace();
      }

      if (var1.size() == 0 && !this.hasNoData) {
         this.hasNoData = true;
      }

      this.writeErrors(this.missingIds, "A lokális törzsadattárban nem található adózók azonosítói");
      this.writeErrors(var2, "A karbantartás nem végezhető el az alábbi adózókon");
      return (Object[][])var1.toArray(new Object[var1.size()][6]);
   }

   private void writeErrors(List<String> var1, String var2) {
      if (var1.size() > 0) {
         StringBuffer var3 = (new StringBuffer(var2)).append(":\n\n");

         String var5;
         for(Iterator var4 = var1.iterator(); var4.hasNext(); var3.append(var5).append("\n")) {
            var5 = (String)var4.next();
            if (var5.length() == 8) {
               var5 = var5 + " (Adószám)";
            } else {
               var5 = var5 + " (Adóazonosító jel)";
            }
         }

         ErrorList.getInstance().writeError("Törzsadat karbantartás", var3.toString(), (Exception)null, (Object)null);
      }

   }

   public void closeMaintenance() throws EnykpTechnicalException {
      try {
         MasterDataDownload.getInstance().done();
      } catch (MasterDataDownloadException var2) {
         throw new EnykpTechnicalException(var2.getMessage());
      }
   }

   public boolean hasError() {
      return this.hasError;
   }

   public boolean hasMissing() {
      return this.missingIds.size() > 0;
   }

   public boolean hasNoData() {
      return this.hasNoData;
   }
}
