package hu.piller.enykp.util.proxy;

import hu.piller.enykp.alogic.settingspanel.SettingsStore;
import hu.piller.enykp.util.base.Tools;
import java.net.Authenticator;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map.Entry;

public class ProxySettings {
   private static ProxySettings _instance;
   private boolean enabled;
   private boolean authenticated;
   private String host;
   private int port;
   private String user;
   private String pwd;

   private ProxySettings() {
      this.setUp();
   }

   public static synchronized ProxySettings getInstance() {
      if (_instance == null) {
         _instance = new ProxySettings();
      }

      return _instance;
   }

   public synchronized void reset() {
      this.setUp();
   }

   private void setUp() {
      this.setProxyEnabled();
      this.setProxyAuthenticated();
      this.setHost();
      this.setPort();
      this.setUsername();
      this.setPassword();
      this.activate();
   }

   public void setProxyEnabled() {
      String var1 = SettingsStore.getInstance().get("upgrade", "proxy.enabled");
      if ("true".equals(var1)) {
         this.enabled = true;
      } else {
         this.enabled = false;
      }

   }

   public boolean isProxyEnabled() {
      return this.enabled;
   }

   public void setProxyAuthenticated() {
      String var1 = SettingsStore.getInstance().get("upgrade", "proxy.authentication");
      if ("true".equals(var1)) {
         this.authenticated = true;
      } else {
         this.authenticated = false;
      }

   }

   public boolean isProxyAuthenticated() {
      return this.authenticated;
   }

   public void setHost() {
      String var1 = SettingsStore.getInstance().get("upgrade", "proxy.host");
      if (var1 != null) {
         this.host = var1;
      } else {
         this.host = "";
      }

   }

   public String getHost() {
      return this.host;
   }

   public void setPort() {
      String var1 = SettingsStore.getInstance().get("upgrade", "proxy.port");
      if (var1 != null) {
         try {
            this.port = Integer.parseInt(var1);
         } catch (NumberFormatException var3) {
            this.port = -1;
         }
      } else {
         this.port = -1;
      }

   }

   public int getPort() {
      return this.port;
   }

   public void setUsername() {
      String var1 = SettingsStore.getInstance().get("upgrade", "proxy.user");
      if (var1 != null) {
         this.user = var1;
      } else {
         this.user = "";
      }

   }

   public String getUsername() {
      return this.user;
   }

   public void setPassword() {
      String var1 = SettingsStore.getInstance().get("upgrade", "proxy.pwd");
      if (var1 != null) {
         this.pwd = Tools.decrypt(var1);
      } else {
         this.pwd = "";
      }

   }

   public String getPassword() {
      return this.pwd;
   }

   public String toString() {
      return "Enabled: " + this.enabled + ", Authenticated: " + this.authenticated + ", host: " + this.host + ", port: " + this.port + ", user: " + this.user + ", password: " + this.pwd;
   }

   public void activate() {
      if (this.isProxyEnabled()) {
         System.setProperty("http.proxyHost", this.getHost());
         System.setProperty("http.proxyPort", Integer.toString(this.getPort()));
         System.setProperty("https.proxyHost", this.getHost());
         System.setProperty("https.proxyPort", Integer.toString(this.getPort()));
         System.setProperty("ftp.proxyHost", this.getHost());
         System.setProperty("ftp.proxyPort", Integer.toString(this.getPort()));
         if (this.isProxyAuthenticated()) {
            Authenticator.setDefault(new ABEVProxyPasswordAuthenticator(this.getUsername(), this.getPassword().toCharArray()));
         } else {
            Authenticator.setDefault((Authenticator)null);
         }
      } else {
         System.getProperties().remove("http.proxyHost");
         System.getProperties().remove("http.proxyPort");
         System.getProperties().remove("https.proxyHost");
         System.getProperties().remove("https.proxyPort");
         System.getProperties().remove("ftp.proxyHost");
         System.getProperties().remove("ftp.proxyPort");
         Authenticator.setDefault((Authenticator)null);
      }

   }

   public void save(Hashtable var1) {
      Entry var3;
      String var4;
      for(Iterator var2 = var1.entrySet().iterator(); var2.hasNext(); SettingsStore.getInstance().set("upgrade", (String)var3.getKey(), var4)) {
         var3 = (Entry)var2.next();
         var4 = "";
         if (var3.getKey().equals("proxy.user")) {
            this.user = var3.getValue().toString();
            var4 = this.user;
         } else if (var3.getKey().equals("proxy.pwd")) {
            this.pwd = var3.getValue().toString();
            var4 = Tools.encrypt(this.pwd);
         } else if (var3.getKey().equals("proxy.host")) {
            this.host = var3.getValue().toString();
            var4 = this.host;
         } else if (var3.getKey().equals("proxy.port")) {
            try {
               this.port = Integer.parseInt(var3.getValue().toString());
            } catch (NumberFormatException var6) {
               this.port = -1;
            }

            var4 = "" + this.port;
         } else if (var3.getKey().equals("proxy.enabled")) {
            this.enabled = "true".equals((String)var3.getValue());
            var4 = Boolean.toString(this.enabled);
         } else if (var3.getKey().equals("proxy.authentication")) {
            this.authenticated = "true".equals((String)var3.getValue());
            var4 = Boolean.toString(this.authenticated);
         }
      }

   }
}
