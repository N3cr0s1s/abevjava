package hu.piller.enykp.alogic.signer;

import hu.piller.enykp.gui.framework.MainFrame;
import hu.piller.enykp.kauclient.KauAuthMethods;
import hu.piller.enykp.kauclient.KauResult;
import hu.piller.enykp.kauclient.authtokens.KauAuthTokensAnykgw2;
import hu.piller.enykp.kauclient.browser.KauClientWeb;
import hu.piller.enykp.kauclient.simplified.dap.KauClientDap;
import hu.piller.enykp.kauclient.simplified.ukp.KauClientUgyfelkapuPlusz;
import hu.piller.enykp.kauclient.simplified.ukpemail.KauClientUgyfelkapuPluszEmail;
import hu.piller.enykp.niszws.ClientStubBuilder;
import hu.piller.enykp.niszws.obhservice.ObhServicePortType;
import hu.piller.enykp.niszws.util.DapSessionHandler;
import hu.piller.enykp.niszws.util.KauAuthHelper;
import hu.piller.enykp.niszws.util.KauSessionTimeoutHandler;
import hu.piller.enykp.util.JavaInfo;
import java.util.Map;
import javax.swing.SwingUtilities;

public class SignerFactory {
   private SignerFactory() {
   }

   public static ISigner createKrAuthenticatedAnykCsatolmanyLenyomatAsicSigner() throws Exception {
      KauResult var0 = null;
      boolean var1 = KauAuthHelper.getInstance().isSaveAuthData();
      KauResult var2 = KauSessionTimeoutHandler.getInstance().getKauResult();
      String var4;
      if (var2 == null) {
         Map var3;
         try {
            var3 = (new KauAuthTokensAnykgw2()).getTokens();
         } catch (Exception var8) {
            throw new Exception("A KAÜ azonosítás előkészítése sikertelen!", var8);
         }

         var4 = KauAuthMethods.getSelected().name();
         byte var5 = -1;
         switch(var4.hashCode()) {
         case -214411519:
            if (var4.equals("KAU_ALL")) {
               var5 = 0;
            }
            break;
         case -214408973:
            if (var4.equals("KAU_DAP")) {
               var5 = 2;
            }
            break;
         case -214407644:
            if (var4.equals("KAU_EML")) {
               var5 = 3;
            }
            break;
         case -214392326:
            if (var4.equals("KAU_UKP")) {
               var5 = 1;
            }
         }

         KauAuthHelper var7;
         switch(var5) {
         case 0:
            if (!JavaInfo.isJavaFxAvailable()) {
               throw new Exception("ANYK_KAU" + JavaInfo.getNoJFXMessage());
            }

            KauClientWeb var13 = new KauClientWeb(MainFrame.thisinstance, var3);
            SwingUtilities.invokeLater(() -> {
               var13.setVisible(true);
            });
            var0 = var13.getKauResult();
            break;
         case 1:
            KauClientUgyfelkapuPlusz var11 = new KauClientUgyfelkapuPlusz();
            var7 = KauAuthHelper.getInstance();
            var0 = var11.authenticate(var3, var7.getMohuUser(), var7.getMohuPass());
            break;
         case 2:
            KauClientDap var10 = new KauClientDap();
            if (DapSessionHandler.getInstance().isBatchDapUploadInProgress() && DapSessionHandler.getInstance().hasCachedKauResult()) {
               var0 = DapSessionHandler.getInstance().getKauResult();
               break;
            }

            var0 = var10.authenticate(var3, (String)null, (String)null);
            DapSessionHandler.getInstance().setKauResult(var0);
            break;
         case 3:
            KauClientUgyfelkapuPluszEmail var6 = new KauClientUgyfelkapuPluszEmail();
            var7 = KauAuthHelper.getInstance();
            var0 = var6.authenticate(var3, var7.getMohuUser(), var7.getMohuPass());
            break;
         default:
            System.err.println(String.format("Ismeretlen KAÜ felhasználó azonosítási mód %s", KauAuthMethods.getSelected()));
         }

         if (var0 == null) {
            throw new Exception("Hitelesítés sikertelen vagy megszakítva!");
         }

         if (var1) {
            KauSessionTimeoutHandler.getInstance().setKauResult(var0);
         }
      } else {
         var0 = var2;
      }

      String var9 = System.getProperty("avdh.scheme", "https");
      var4 = System.getProperty("avdh.host", "niszavdh.gov.hu");
      String var12 = System.getProperty("avdh.port", "");
      ObhServicePortType var14 = (ObhServicePortType)(new ClientStubBuilder()).get("obhService.wsdl", "ObhService2", "http://nisz.hu/obhservice/1.0", ObhServicePortType.class, true, var9, var4, var12, "/ws/obhService2", var0.getSamlResponse(), var0.getCookie(), true);
      AnykCsatolmanyLenyomatAsicSigner var15 = new AnykCsatolmanyLenyomatAsicSigner();
      var15.setObhService2(var14);
      return var15;
   }
}
