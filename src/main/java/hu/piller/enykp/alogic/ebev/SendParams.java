package hu.piller.enykp.alogic.ebev;

import hu.piller.enykp.alogic.settingspanel.SettingsStore;
import hu.piller.enykp.interfaces.IOsHandler;
import hu.piller.enykp.interfaces.IPropertyList;
import hu.piller.enykp.util.base.Tools;
import hu.piller.enykp.util.oshandler.OsFactory;

public class SendParams {
   public String root;
   public String browserPath;
   public String krdir;
   public String srcPath;
   public String destPath;
   public String sentPath;
   public String importPath;
   public String dataPath;

   public SendParams(IPropertyList var1) {
      IOsHandler var2 = OsFactory.getOsHandler();
      if (var1.get("prop.internet_browser.path") != null) {
         if (!((String)var1.get("prop.internet_browser.path")).equals("")) {
            this.browserPath = (String)var1.get("prop.internet_browser.path");
         } else {
            this.browserPath = var2.getSystemBrowserPath();
         }
      } else {
         this.browserPath = var2.getSystemBrowserPath();
      }

      this.krdir = Tools.fillPath((String)var1.get("prop.usr.krdir"));
      SettingsStore var3 = SettingsStore.getInstance();
      if (var3.get("gui", "digitális_aláírás") != null) {
         if (!var3.get("gui", "digitális_aláírás").equals("")) {
            this.srcPath = Tools.fillPath(var3.get("gui", "digitális_aláírás"));
         } else {
            try {
               this.srcPath = Tools.fillPath(this.krdir + var1.get("prop.usr.ds_src"));
            } catch (Exception var6) {
               Tools.eLog(var6, 0);
            }
         }
      } else {
         try {
            this.srcPath = Tools.fillPath(this.krdir + var1.get("prop.usr.ds_src"));
         } catch (Exception var5) {
            Tools.eLog(var5, 0);
         }
      }

      this.destPath = Tools.fillPath(this.krdir + var1.get("prop.usr.ds_dest"));
      this.sentPath = Tools.fillPath(this.krdir + var1.get("prop.usr.ds_sent"));
      this.root = Tools.fillPath((String)var1.get("prop.usr.root"));
      this.dataPath = this.root + Tools.fillPath((String)var1.get("prop.usr.saves"));
      this.importPath = this.root + Tools.fillPath((String)var1.get("prop.usr.import"));
   }
}
