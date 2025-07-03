package hu.piller.enykp.alogic.settingspanel.syncconfig.model;

import java.util.Map;

public interface ISysConfig {
   void setConfig(Map<String, ConfigState> var1);

   Map<String, ConfigState> getConfig();
}
