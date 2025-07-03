package hu.piller.enykp.alogic.templateutils.blacklist;

import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.util.base.ErrorList;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;

public class BlacklistStore {
   public static final String BLACKLIST_URL_APP = "https://nav.gov.hu/pfile/file?path=/nyomtatvanyok/letoltesek/blacklist.xml";
   public static final String BLACKLIST_URL_USER = "https://nav.gov.hu/nyomtatvanyok/letoltesek/blacklist.xml";
   private static BlacklistStore instance;
   private static boolean hasData = false;
   HashMap<String, Blacklist.Template> store;

   public static BlacklistStore getInstance() {
      if (instance == null) {
         instance = new BlacklistStore((String)null);
      }

      return instance;
   }

   public static BlacklistStore getInstance(String var0) {
      if (instance == null || !hasData) {
         instance = new BlacklistStore(var0);
         hasData = true;
      }

      return instance;
   }

   private BlacklistStore(String var1) {
      try {
         Blacklist var2;
         if (var1 == null) {
            var2 = Blacklist.create("");
            this.store = var2.getCurrentBlacklistIds();
         } else {
            var2 = Blacklist.create(var1);
            this.store = var2.getCurrentBlacklistIds();
         }
      } catch (Exception var3) {
         if (var1 != null && !"".equals(var1)) {
            ErrorList.getInstance().writeError(new Long(1024L), "Formai hiba az ÁNYK programból kitiltott nyomtatványokat tartalmazó xml állomány feldolgozása során!", var3, (Object)null);
         }

         this.store = new HashMap();
      }

   }

   public boolean isBiztipDisabled(String var1, String var2) {
      try {
         return this.store.containsKey(var2 + "_" + var1);
      } catch (Exception var4) {
         return false;
      }
   }

   public String getUrl(String var1, String var2) {
      return this.store.containsKey(var2 + "_" + var1) ? ((Blacklist.Template)this.store.get(var2 + "_" + var1)).getTargetUrl() : "";
   }

   public String getMessage(String var1, String var2) {
      return this.store.containsKey(var2 + "_" + var1) ? ((Blacklist.Template)this.store.get(var2 + "_" + var1)).getMessage() : "";
   }

   public HashMap<String, Blacklist.Template> getFullList() {
      return this.store;
   }

   public String[] isKrTemplateInBlackList(File var1) throws IOException {
      FileInputStream var2 = new FileInputStream(var1);
      byte[] var3 = new byte[400];
      var2.read(var3);
      var2.close();
      String var4 = new String(var3);
      int var5 = var4.indexOf("<abev:DokTipusAzonosito>");
      int var6 = var4.indexOf("<abev:Cimzett>");
      if (var6 != -1 && var5 != -1) {
         String var7 = var4.substring(var6 + "<abev:Cimzett>".length(), var4.indexOf("</abev:Cimzett>"));
         String var8 = var4.substring(var5 + "<abev:DokTipusAzonosito>".length(), var4.indexOf("</abev:DokTipusAzonosito>"));
         return this.isBiztipDisabled(var8, var7) ? new String[]{var8, var7} : null;
      } else {
         return null;
      }
   }

   public boolean handleGuiMessage(String var1, String var2) {
      if (this.isBiztipDisabled(var1, var2)) {
         this.getText4Dialog(var1, var2);

         try {
            GuiUtil.showExtendedDialog(this.getMessage(var1, var2), this.getUrl(var1, var2));
         } catch (IOException var5) {
            var5.printStackTrace();
         }

         return true;
      } else {
         return false;
      }
   }

   public String[] getErrorListMessage(String var1, String var2) {
      String[] var3 = this.getText4ErrorList(var1, var2);
      return var3;
   }

   private String[] getText4ErrorList(String var1, String var2) {
      String var3 = this.getMessage(var1, var2);
      if ("".equals(var3)) {
         var3 = "A nyomtatvány már csak az Online Nyomtatványkitöltő Alkalmazásban érhető el! ";
      }

      var3 = var3 + " Az alkalmazás indításához a linket másolja egy böngészőbe: ";
      return new String[]{var3, this.getUrl(var1, var2)};
   }

   private String getText4Dialog(String var1, String var2) {
      String var3 = this.getMessage(var1, var2);
      if ("".equals(var3)) {
         var3 = "A nyomtatvány már csak az Online Nyomtatványkitöltő Alkalmazásban érhető el! ";
      }

      String var4 = this.getUrl(var1, var2);
      if (!"".equals(var4)) {
         var3 = var3 + "\n" + var4;
      }

      return var3;
   }
}
