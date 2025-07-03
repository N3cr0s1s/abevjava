package hu.piller.enykp.alogic.ebev.datagate;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class DatagateConfig {
   public static boolean isDatagateTraceEnabled() {
      return System.getProperty("datagate.trace") != null;
   }

   public static Properties getEnvCfg() throws IOException {
      Properties var0 = new Properties();
      DatagateClientMode var1 = getDatageClientMode();
      if (isNonProd(var1)) {
         InputStream var2 = getTestConfiguration(var1);
         Throwable var3 = null;

         try {
            var0.load(var2);
         } catch (Throwable var12) {
            var3 = var12;
            throw var12;
         } finally {
            if (var2 != null) {
               if (var3 != null) {
                  try {
                     var2.close();
                  } catch (Throwable var11) {
                     var3.addSuppressed(var11);
                  }
               } else {
                  var2.close();
               }
            }

         }
      } else {
         var0.put("DATAGATE_URL", "https://partnerdata.nav.gov.hu/api");
         var0.put("SP_RESP_URL", "https://oss.nav.gov.hu/kaulogin");
         var0.put("RELAY_STATE_B64", "aHR0cHM6Ly9vc3MubmF2Lmdvdi5odS9rYXVsb2dpbg==");
         var0.put("KAU_URL", "https://kau.gov.hu/proxy/saml/authnrequest?lang=HU");
      }

      return var0;
   }

   protected static InputStream getTestConfiguration(DatagateClientMode var0) throws IOException {
      Object var1;
      if (System.getProperty("datagate.path_cfg") != null) {
         var1 = new FileInputStream(System.getProperty("datagate.path_cfg"));
      } else {
         String var2 = "resources/datagate-" + var0.toString().toLowerCase() + ".properties";
         var1 = Thread.currentThread().getContextClassLoader().getResourceAsStream(var2);
      }

      return (InputStream)var1;
   }

   protected static boolean isNonProd(DatagateClientMode var0) {
      return DatagateClientMode.DEV.equals(var0) || DatagateClientMode.QT.equals(var0) || DatagateClientMode.NAVT.equals(var0);
   }

   protected static DatagateClientMode getDatageClientMode() {
      DatagateClientMode var0;
      if (System.getProperty("datagate.dev") != null) {
         var0 = DatagateClientMode.DEV;
      } else if (System.getProperty("datagate.qt") != null) {
         var0 = DatagateClientMode.QT;
      } else if (System.getProperty("datagate.navt") != null) {
         var0 = DatagateClientMode.NAVT;
      } else {
         var0 = DatagateClientMode.PROD;
      }

      return var0;
   }
}
