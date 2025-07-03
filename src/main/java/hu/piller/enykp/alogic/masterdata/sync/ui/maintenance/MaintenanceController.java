package hu.piller.enykp.alogic.masterdata.sync.ui.maintenance;

import hu.piller.enykp.alogic.masterdata.core.Block;
import hu.piller.enykp.alogic.masterdata.core.BlockDefinition;
import hu.piller.enykp.alogic.masterdata.core.Entity;
import hu.piller.enykp.alogic.masterdata.core.EntityError;
import hu.piller.enykp.alogic.masterdata.repository.MDRepositoryMetaFactory;
import hu.piller.enykp.alogic.masterdata.sync.configuration.ConfigService;
import hu.piller.enykp.alogic.masterdata.sync.ui.PdfExport;
import hu.piller.enykp.alogic.masterdata.sync.ui.maintenance.technicalmd.ITechnicalMdHandler;
import hu.piller.enykp.alogic.masterdata.sync.ui.pdfexport.IMapperCallback;
import hu.piller.enykp.alogic.masterdata.sync.ui.pdfexport.ViewModelToHtml;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Map.Entry;
import javax.swing.table.TableModel;

public class MaintenanceController {
   private Map<String, List<String>> mdWithMultipleValues;
   private Set<String> masterDataEditedWithValueEditor;
   private Properties config;
   private String entityIdentifierData;

   public MaintenanceController(String var1) {
      this.entityIdentifierData = var1;
      HashMap var2 = new HashMap(2);
      var2.put("Bankszámlaszám", Collections.unmodifiableList(this.readDataList("Bankszámlaszám")));
      var2.put("Közösségi adószám", Collections.unmodifiableList(this.readDataList("Közösségi adószám")));
      this.mdWithMultipleValues = Collections.unmodifiableMap(var2);
      HashSet var3 = new HashSet(2);
      var3.add("Bankszámlaszám");
      var3.add("Közösségi adószám");
      this.masterDataEditedWithValueEditor = Collections.unmodifiableSet(var3);
      this.config = this.loadConfig();
   }

   public Set<String> getMasterDataEditedWithValueEditor() {
      return this.masterDataEditedWithValueEditor;
   }

   public Set<String> getMdNamesWithMultipleNavValues() {
      HashSet var1 = new HashSet();
      Iterator var2 = this.mdWithMultipleValues.entrySet().iterator();

      while(var2.hasNext()) {
         Entry var3 = (Entry)var2.next();
         if (((List)var3.getValue()).size() > 1) {
            var1.add(var3.getKey());
         }
      }

      return var1;
   }

   public List<String> getNavDataListForMasterData(String var1) {
      return (List)this.mdWithMultipleValues.get(var1);
   }

   public String getId() {
      return this.entityIdentifierData;
   }

   public List<MDMaintenanceModel> getDataForMaintenance() {
      ArrayList var1 = new ArrayList();

      try {
         Entity var2 = MaintenanceDAO.getInstance().readEntityByIdFromRepository(this.entityIdentifierData);
         Entity var3 = MaintenanceDAO.getInstance().readEntityByIdentifierFromSyncFolder(this.entityIdentifierData);
         BlockDefinition[] var4 = MDRepositoryMetaFactory.getMDRepositoryMeta().getBlockDefinitionsForEntity(var2.getName());
         Properties var5 = this.loadMasterDataNamesToShow();
         BlockDefinition[] var6 = var4;
         int var7 = var4.length;

         for(int var8 = 0; var8 < var7; ++var8) {
            BlockDefinition var9 = var6[var8];
            String var10 = var9.getBlockName();
            if (!"Telephelyek".equals(var10)) {
               Block[] var11 = var2.getBlocks(var10);
               int var12 = var11.length;

               for(int var13 = 0; var13 < var12; ++var13) {
                  Block var14 = var11[var13];
                  HashSet var15 = new HashSet();
                  String[] var16 = var9.getMasterDataNames();
                  int var17 = var16.length;

                  for(int var18 = 0; var18 < var17; ++var18) {
                     String var19 = var16[var18];
                     if (!var15.contains(var19) && this.isEnabled(var10, var19)) {
                        MDMaintenanceModel var20 = new MDMaintenanceModel();
                        var20.setBlockName(var10);
                        var20.setId(var2.getId());
                        var20.setSeq(1);
                        var20.setNavValid(false);
                        if (ConfigService.panidsToTechnicalMd.keySet().contains(var19)) {
                           String var21 = (String)ConfigService.panidsToTechnicalMd.get(var19);
                           var20.setName(var21);
                           var20.setNameToShow(var21);
                           var20.setLocalValue((String)((ITechnicalMdHandler)ConfigService.technicalMdHandlers.get(var21)).build(var2).get(0));
                           List var22 = ((ITechnicalMdHandler)ConfigService.technicalMdHandlers.get(var21)).build(var3);
                           if (var22.contains(var20.getLocalValue())) {
                              var20.setNavValue(var20.getLocalValue());
                           } else {
                              var20.setNavValue((String)var22.get(0));
                           }

                           var15.addAll((Collection)ConfigService.technicalMdToPanids.get(var21));
                        } else {
                           var20.setName(var19);
                           var20.setNameToShow(var5.getProperty(var19));
                           var20.setLocalValue(this.formatValueBeforeShow(var19, var14.getMasterData(var19).getValue()));
                           var20.setNavValue(var3.getBlock(var10).getMasterData(var19).getValue());
                        }

                        var20.setValidValue(var20.getLocalValue());
                        var1.add(var20);
                     }
                  }
               }
            }
         }
      } catch (Exception var23) {
         var23.printStackTrace();
         var1.clear();
      }

      return var1;
   }

   public void save(MDMaintenanceTableModel var1) throws Exception {
      Entity var2 = MaintenanceDAO.getInstance().readEntityByIdFromRepository(this.entityIdentifierData);
      Iterator var3 = var1.getData().iterator();

      while(true) {
         while(true) {
            MDMaintenanceModel var4;
            do {
               if (!var3.hasNext()) {
                  var2.validate();
                  if (var2.getValidityStatus() != null && var2.getValidityStatus().length > 0) {
                     StringBuffer var10 = new StringBuffer("Az adózó a megadott adatokkal nem menthető!\n");
                     EntityError[] var11 = var2.getValidityStatus();
                     int var13 = var11.length;

                     for(int var14 = 0; var14 < var13; ++var14) {
                        EntityError var15 = var11[var14];
                        var10.append(var15.getMDKey()).append("=").append(var15.getMDVal()).append(" : ").append(var15.getErrorMsg()).append("\n");
                     }

                     throw new Exception(var10.toString());
                  }

                  MaintenanceDAO.getInstance().writeEntityToRepository(var2);
                  if (!"Egyéni vállalkozó".equals(var2.getName()) && !"Társaság".equals(var2.getName())) {
                     if ("Magánszemély".equals(var2.getName())) {
                        this.entityIdentifierData = var2.getBlock("Törzsadatok").getMasterData("Adózó adóazonosító jele").getValue();
                     } else {
                        System.out.println("Ismeretlen típus: " + var2.getName() + " (" + this.entityIdentifierData + ")");
                     }
                  } else {
                     this.entityIdentifierData = var2.getBlock("Törzsadatok").getMasterData("Adózó adószáma").getValue();
                  }

                  return;
               }

               var4 = (MDMaintenanceModel)var3.next();
            } while(var4.getType() != 0);

            if (ConfigService.technicalMdHandlers.keySet().contains(var4.getName())) {
               ArrayList var12 = new ArrayList();
               var12.add(var4.getValidValue());
               Map var6 = ((ITechnicalMdHandler)ConfigService.technicalMdHandlers.get(var4.getName())).split(var12);
               Iterator var7 = var6.entrySet().iterator();

               while(var7.hasNext()) {
                  Entry var8 = (Entry)var7.next();
                  String var9 = this.formatValueBeforeSave((String)var8.getKey(), (String)((List)var8.getValue()).get(0));
                  var2.getBlock(var4.getBlockName(), var4.getSeq()).getMasterData((String)var8.getKey()).setValue(var9);
               }
            } else {
               String var5 = this.formatValueBeforeSave(var4.getName(), var4.getValidValue());
               var2.getBlock(var4.getBlockName(), var4.getSeq()).getMasterData(var4.getName()).setValue(var5);
            }
         }
      }
   }

   private boolean isEnabled(String var1, String var2) {
      return "y".equals(this.config.getProperty(var1 + "." + var2));
   }

   private Properties loadMasterDataNamesToShow() {
      Properties var1 = new Properties();

      try {
         var1.load(ClassLoader.getSystemClassLoader().getResourceAsStream("resources/masterdata/syncmasterdatalabel.properties"));
      } catch (Exception var3) {
         var3.printStackTrace();
      }

      return var1;
   }

   public void pdfPrint(TableModel var1, String var2) {
      String var3 = ViewModelToHtml.tableModelAsHtml(var1, new IMapperCallback() {
         public boolean isColumnEnabled(int var1) {
            return var1 != 3;
         }

         public Object mapValue(int var1, Object var2) {
            return var2;
         }
      });
      String var4 = "";
      if (var2.indexOf("-") != -1) {
         var4 = "adószám";
      } else {
         var4 = "adóazonosító jel";
      }

      var3 = "<html><body>" + var4 + ":&nbsp;" + var2 + "<br/><br/>" + var3 + "</body></html>";
      PdfExport var5 = new PdfExport();
      var5.print(var2, var3);
   }

   private Properties loadConfig() {
      Properties var1;
      try {
         String var2 = MaintenanceDAO.getInstance().readEntityByIdFromRepository(this.entityIdentifierData).getName();
         var1 = (new ConfigService()).loadConfig(var2);
      } catch (Exception var3) {
         var1 = new Properties();
         var3.printStackTrace();
      }

      return var1;
   }

   private List<String> readDataList(String var1) {
      Object var2;
      try {
         Entity var3 = MaintenanceDAO.getInstance().readEntityByIdentifierFromSyncFolder(this.entityIdentifierData);
         var2 = ((ITechnicalMdHandler)ConfigService.technicalMdHandlers.get(var1)).build(var3);
      } catch (Exception var4) {
         var4.printStackTrace();
         var2 = new ArrayList();
      }

      return (List)var2;
   }

   private String formatValueBeforeShow(String var1, String var2) {
      return ("VPID".equals(var1) || "Engedélyszám".equals(var1)) && "HU".equals(var2.trim()) ? "" : var2;
   }

   private String formatValueBeforeSave(String var1, String var2) {
      return ("VPID".equals(var1) || "Engedélyszám".equals(var1)) && "".equals(var2) ? "HU" : var2;
   }
}
