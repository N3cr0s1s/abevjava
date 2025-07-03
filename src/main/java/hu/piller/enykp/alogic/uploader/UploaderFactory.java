package hu.piller.enykp.alogic.uploader;

import hu.piller.enykp.gui.framework.MainFrame;
import hu.piller.enykp.kauclient.KauAuthMethods;
import hu.piller.enykp.kauclient.KauResult;
import hu.piller.enykp.kauclient.authtokens.KauAuthTokensAnykgw2;
import hu.piller.enykp.kauclient.browser.KauClientWeb;
import hu.piller.enykp.kauclient.simplified.dap.KauClientDap;
import hu.piller.enykp.kauclient.simplified.ukp.KauClientUgyfelkapuPlusz;
import hu.piller.enykp.kauclient.simplified.ukpemail.KauClientUgyfelkapuPluszEmail;
import hu.piller.enykp.niszws.ClientStubBuilder;
import hu.piller.enykp.niszws.documentsuploadservice.DocumentsUploadService;
import hu.piller.enykp.niszws.util.DapSessionHandler;
import hu.piller.enykp.niszws.util.KauAuthHelper;
import hu.piller.enykp.niszws.util.KauSessionTimeoutHandler;
import hu.piller.enykp.util.JavaInfo;
import java.util.Map;
import javax.swing.SwingUtilities;

public final class UploaderFactory {
   private static final String DEFAULT_PROTOCOL = "https";
   private static final String DEFAULT_HOST = "anykgw.gov.hu";
   private static final String DEFAULT_PORT = "";

   private UploaderFactory() {
   }

   public static IUploader createUploaderForUgyfelkapuHivatalikapu() throws Exception {
      String var0 = System.getProperty("krwsfe.scheme", "https");
      String var1 = System.getProperty("krwsfe.host", "anykgw.gov.hu");
      String var2 = System.getProperty("krwsfe.port", "");
      return _create(var0, var1, var2);
   }

   private static IUploader _create(String var0, String var1, String var2) throws Exception {
      KauResult var3 = null;
      boolean var4 = KauAuthHelper.getInstance().isSaveAuthData();
      KauResult var5 = KauSessionTimeoutHandler.getInstance().getKauResult();
      if (var5 == null) {
         Map var6;
         try {
            var6 = (new KauAuthTokensAnykgw2()).getTokens();
         } catch (Exception var11) {
            throw new Exception("ANYK_KAUA KAÜ azonosítás előkészítése sikertelen!", var11);
         }

         String var7 = KauAuthMethods.getSelected().name();
         byte var8 = -1;
         switch(var7.hashCode()) {
         case -214411519:
            if (var7.equals("KAU_ALL")) {
               var8 = 0;
            }
            break;
         case -214408973:
            if (var7.equals("KAU_DAP")) {
               var8 = 2;
            }
            break;
         case -214407644:
            if (var7.equals("KAU_EML")) {
               var8 = 3;
            }
            break;
         case -214392326:
            if (var7.equals("KAU_UKP")) {
               var8 = 1;
            }
         }

         KauAuthHelper var10;
         switch(var8) {
         case 0:
            if (!JavaInfo.isJavaFxAvailable()) {
               throw new Exception("ANYK_KAU" + JavaInfo.getNoJFXMessage());
            }

            KauClientWeb var16 = new KauClientWeb(MainFrame.thisinstance, var6);
            SwingUtilities.invokeLater(() -> {
               var16.setVisible(true);
            });
            var3 = var16.getKauResult();
            break;
         case 1:
            KauClientUgyfelkapuPlusz var15 = new KauClientUgyfelkapuPlusz();
            var10 = KauAuthHelper.getInstance();
            var3 = var15.authenticate(var6, var10.getMohuUser(), var10.getMohuPass());
            break;
         case 2:
            KauClientDap var14 = new KauClientDap();
            if (DapSessionHandler.getInstance().isBatchDapUploadInProgress() && DapSessionHandler.getInstance().hasCachedKauResult()) {
               var3 = DapSessionHandler.getInstance().getKauResult();
               break;
            }

            var3 = var14.authenticate(var6, (String)null, (String)null);
            DapSessionHandler.getInstance().setKauResult(var3);
            break;
         case 3:
            KauClientUgyfelkapuPluszEmail var9 = new KauClientUgyfelkapuPluszEmail();
            var10 = KauAuthHelper.getInstance();
            var3 = var9.authenticate(var6, var10.getMohuUser(), var10.getMohuPass());
            break;
         default:
            System.err.println(String.format("Ismeretlen KAÜ felhasználó azonosítási mód %s", KauAuthMethods.getSelected()));
         }

         if (var4 && var3 != null) {
            KauSessionTimeoutHandler.getInstance().setKauResult(var3);
         }

         if (var3 == null) {
            throw new Exception("ANYK_KAU Hitelesítés sikertelen vagy megszakítva!");
         }
      } else {
         var3 = var5;
      }

      DocumentsUploadService var12 = (DocumentsUploadService)(new ClientStubBuilder()).get("DocumentsUploadService.wsdl", "DocumentsUploadService", "http://tarhely.gov.hu/anykgw2", DocumentsUploadService.class, true, var0, var1, var2, "/anykgw2/DocumentsUploadService", var3.getSamlResponse(), var3.getCookie(), var3.isSubjectConfirmationRequired());
      IUploaderImpl var13 = new IUploaderImpl();
      var13.setDocumentsUploadService(var12);
      return var13;
   }
}
