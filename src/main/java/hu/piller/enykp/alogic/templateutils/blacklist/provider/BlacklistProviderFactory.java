package hu.piller.enykp.alogic.templateutils.blacklist.provider;

import hu.piller.enykp.util.base.ErrorList;
import java.io.File;
import java.net.URL;

public final class BlacklistProviderFactory {
   private static final String ID = "30001";

   public static final BlacklistProvider newInstance() {
      String var0 = System.getProperty("blacklist.path");
      Object var1 = null;
      if (var0 != null) {
         var1 = new BlacklistProviderLocal(new File(var0));
      } else {
         try {
            URL var2 = new URL("https://nav.gov.hu/pfile/file?path=/nyomtatvanyok/letoltesek/blacklist.xml");
            var1 = new BlacklistProviderRemote(var2);
         } catch (Exception var4) {
            String var3 = String.format("Sikertelen bevallás kitiltási lista letöltő létrehozás a(z) %s címhez", "https://nav.gov.hu/pfile/file?path=/nyomtatvanyok/letoltesek/blacklist.xml");
            ErrorList.getInstance().store("30001", var3, var4, (Object)null);
            System.err.println("Kitiltási lista letöltő létrehozás hiba! (részletek: Szerviz > Üzenetek)");
         }
      }

      return (BlacklistProvider)var1;
   }

   public static final BlacklistProvider newInstance(File var0) {
      return new BlacklistProviderLocal(var0);
   }

   public static final BlacklistProvider newInstance(URL var0) {
      return new BlacklistProviderRemote(var0);
   }
}
