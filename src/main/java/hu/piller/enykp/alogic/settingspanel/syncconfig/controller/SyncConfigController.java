package hu.piller.enykp.alogic.settingspanel.syncconfig.controller;

import hu.piller.enykp.alogic.masterdata.sync.configuration.ConfigService;
import hu.piller.enykp.alogic.settingspanel.syncconfig.model.ConfigState;
import hu.piller.enykp.alogic.settingspanel.syncconfig.model.SyncConfigModel;
import hu.piller.enykp.alogic.settingspanel.syncconfig.view.JEntityTypeSyncConfigPanel;
import hu.piller.enykp.alogic.settingspanel.syncconfig.view.JSyncConfigView;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Properties;
import java.util.Map.Entry;

public class SyncConfigController implements Observer {
   private JSyncConfigView view;
   private SyncConfigModel model;
   private ConfigService configService;

   public void setConfigService(ConfigService var1) {
      this.configService = var1;
   }

   public void setModel(SyncConfigModel var1) {
      this.model = var1;
   }

   public void setView(JSyncConfigView var1) {
      this.view = var1;
   }

   public void update(Observable var1, Object var2) {
      if (this.isConfigTabIndex(this.view.getOldTabIndex())) {
         this.saveConfigSettingsOnTab(this.view.getOldTabIndex());
      }

      if (this.isConfigTabIndex(this.view.getSelectedIndex())) {
         Map var3 = this.loadConfigSettingsForTab(this.view.getSelectedIndex());
         this.model.setConfig(var3);
      }

   }

   public void afterStart() {
      this.model.setConfig(this.loadConfigSettingsForTab(0));
   }

   private boolean isConfigTabIndex(int var1) {
      return var1 >= 0 && var1 <= 2;
   }

   private void saveConfigSettingsOnTab(int var1) {
      Map var2 = ((JEntityTypeSyncConfigPanel)this.view.getComponentAt(var1)).getConfigSettings();
      Properties var3 = new Properties();
      Iterator var4 = var2.entrySet().iterator();

      while(true) {
         while(var4.hasNext()) {
            Entry var5 = (Entry)var4.next();
            String[] var6 = ((String)var5.getKey()).split("\\.");
            String var8;
            if (ConfigService.technicalMdToPanids.containsKey(var6[1])) {
               Iterator var11 = ((List)ConfigService.technicalMdToPanids.get(var6[1])).iterator();

               while(var11.hasNext()) {
                  var8 = (String)var11.next();
                  String var9 = var6[0] + "." + var8;
                  String var10 = ConfigState.ENABLED.equals(var5.getValue()) ? "y" : "n";
                  var3.put(var9, var10);
               }
            } else {
               String var7 = (String)var5.getKey();
               var8 = ConfigState.ENABLED.equals(var5.getValue()) ? "y" : "n";
               var3.put(var7, var8);
            }
         }

         this.configService.storeConfig(this.view.getEntityTypeByTabIndex(var1), var3);
         return;
      }
   }

   private Map<String, ConfigState> loadConfigSettingsForTab(int var1) {
      String var2 = this.view.getEntityTypeByTabIndex(var1);
      Properties var3 = this.configService.loadConfig(var2);
      HashSet var4 = new HashSet();
      HashMap var5 = new HashMap();
      Iterator var6 = var3.stringPropertyNames().iterator();

      while(var6.hasNext()) {
         String var7 = (String)var6.next();
         String[] var8 = var7.split("\\.");
         if (!var4.contains(var8[1])) {
            if (ConfigService.panidsToTechnicalMd.containsKey(var8[1])) {
               String var9 = (String)ConfigService.panidsToTechnicalMd.get(var8[1]);
               var5.put(var8[0] + "." + var9, "y".equals(String.class.cast(var3.getProperty(var7))) ? ConfigState.ENABLED : ConfigState.DISABLED);
               var4.addAll((Collection)ConfigService.technicalMdToPanids.get(var9));
            } else {
               var5.put(var7, "y".equals(String.class.cast(var3.getProperty(var7))) ? ConfigState.ENABLED : ConfigState.DISABLED);
            }
         }
      }

      return var5;
   }
}
