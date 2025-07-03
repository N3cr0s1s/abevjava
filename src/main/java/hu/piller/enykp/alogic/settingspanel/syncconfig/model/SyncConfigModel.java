package hu.piller.enykp.alogic.settingspanel.syncconfig.model;

import java.util.Map;
import java.util.Observable;

public class SyncConfigModel extends Observable implements ISysConfig {
   private Map<String, ConfigState> config;

   public void setConfig(Map<String, ConfigState> var1) {
      this.config = var1;
      this.setChanged();
      this.notifyObservers();
      this.clearChanged();
   }

   public Map<String, ConfigState> getConfig() {
      return this.config;
   }
}
