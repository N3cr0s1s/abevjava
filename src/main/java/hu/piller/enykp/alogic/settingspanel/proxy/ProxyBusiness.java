package hu.piller.enykp.alogic.settingspanel.proxy;

import hu.piller.enykp.util.proxy.ProxySettings;
import java.util.Hashtable;

public class ProxyBusiness {
   public static final String PROXY_INFO_FILE_ID = "upgrade_manager";
   public static final String PROXY_INFO_FILE = "upgrade_manager_proxy.enyk";
   public static final String PAR_LOAD_ERROR = "Paraméter file olvasási hiba";
   public static final String PAR_WRITE_ERROR = "Paraméter file írási hiba";
   public static final String RES_SETTINGS_HANDLER = "SettingsHandler";
   Hashtable params;
   Hashtable dataprovider = new Hashtable();
   private String err;

   public ProxyBusiness() {
      this.load();
   }

   public Hashtable load() {
      this.params = this.loadValues();
      this.dataprovider.putAll(this.params);
      return this.params;
   }

   private Hashtable loadValues() {
      Hashtable var1 = new Hashtable();
      var1.put("proxy.host", ProxySettings.getInstance().getHost());
      var1.put("proxy.port", "" + (ProxySettings.getInstance().getPort() == -1 ? "" : ProxySettings.getInstance().getPort()));
      var1.put("proxy.user", ProxySettings.getInstance().getUsername());
      var1.put("proxy.pwd", ProxySettings.getInstance().getPassword());
      var1.put("proxy.enabled", ProxySettings.getInstance().isProxyEnabled() ? "true" : "false");
      var1.put("proxy.authentication", ProxySettings.getInstance().isProxyAuthenticated() ? "true" : "false");
      return var1;
   }

   public String getErr() {
      return this.err;
   }

   public void save() {
      String var1 = this.isValid();
      if ("".equals(var1)) {
         ProxySettings.getInstance().save(this.dataprovider);
         ProxySettings.getInstance().activate();
         this.err = "";
      } else {
         this.err = var1;
      }

   }

   public void setDataprovider(Hashtable var1) {
      this.dataprovider = var1;
   }

   public Hashtable getDataprovider() {
      this.load();
      return this.dataprovider;
   }

   public String get(String var1) {
      if (this.params == null) {
         this.load();
      }

      return (String)this.dataprovider.get(var1);
   }

   private String isValid() {
      StringBuilder var1 = new StringBuilder("");
      if ("true".equals((String)this.dataprovider.get("proxy.enabled"))) {
         if ("".equals((String)this.dataprovider.get("proxy.host"))) {
            var1.append("A Proxy beállítások 'Hoszt' paramétere nincs kitöltve!\n");
         }

         if ("".equals((String)this.dataprovider.get("proxy.port"))) {
            var1.append("A Proxy beállítások 'Port' paramétere nincs kitöltve!\n");
         } else {
            try {
               Integer.parseInt((String)this.dataprovider.get("proxy.port"));
            } catch (NumberFormatException var3) {
               var1.append("A Proxy beállítások 'Port' paraméterül számot kell megadni!\n");
            }
         }

         if ("true".equals((String)this.dataprovider.get("proxy.authentication"))) {
            if ("".equals((String)this.dataprovider.get("proxy.user"))) {
               var1.append("A Proxy beállítások 'Felhasználó' paramétere nincs kitöltve!\n");
            }

            if ("".equals((String)this.dataprovider.get("proxy.pwd"))) {
               var1.append("A Proxy beállítások 'Jelszó' paramétere nincs kitöltve!\n");
            }
         }
      }

      return var1.toString();
   }
}
