package hu.piller.enykp.alogic.ebev.datagate;

import hu.piller.enykp.alogic.filepanels.mohu.UserAndPassDialog;
import hu.piller.enykp.alogic.masterdata.sync.download.provider.MasterDataProviderException;
import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.gui.framework.MainFrame;
import hu.piller.enykp.kauclient.IKauClient;
import hu.piller.enykp.kauclient.KauAuthMethod;
import hu.piller.enykp.kauclient.KauAuthMethods;
import hu.piller.enykp.kauclient.KauClientException;
import hu.piller.enykp.kauclient.KauResult;
import hu.piller.enykp.kauclient.browser.KauClientWeb;
import hu.piller.enykp.kauclient.simplified.dap.KauClientDap;
import hu.piller.enykp.kauclient.simplified.ukp.KauClientUgyfelkapuPlusz;
import hu.piller.enykp.kauclient.simplified.ukpemail.KauClientUgyfelkapuPluszEmail;
import hu.piller.enykp.niszws.util.KauAuthHelper;
import hu.piller.enykp.util.JavaInfo;
import java.util.Map;
import javax.swing.SwingUtilities;

public final class DatagateUserAuthKau implements IDatagateUserAuthKau {
   public KauResult authenticate(Map<String, String> var1) throws Exception {
      KauResult var2;
      UserAndPassDialog var3;
      switch(KauAuthMethods.getSelected()) {
      case KAU_ALL:
         if (!JavaInfo.isJavaFxAvailable()) {
            GuiUtil.showMessageDialog(MainFrame.thisinstance, JavaInfo.getNoJFXMessageLines(), "KAÜ azonosítás", 1);
            throw new Exception(JavaInfo.getNoJFXMessageLines());
         }

         KauClientWeb var4 = new KauClientWeb(MainFrame.thisinstance, var1);
         SwingUtilities.invokeLater(() -> {
            var4.setVisible(true);
         });
         var2 = var4.getKauResult();
         break;
      case KAU_DAP:
         KauClientDap var5 = new KauClientDap();
         var2 = var5.authenticate(var1, (String)null, (String)null);
         break;
      case KAU_UKP:
         var3 = new UserAndPassDialog(0, KauAuthMethods.getSelected().getText());
         SwingUtilities.invokeAndWait(() -> {
            var3.setLocationRelativeTo(MainFrame.thisinstance);
            var3.showIfNeed();
         });
         if (var3.getState() == 0) {
            throw new MasterDataProviderException(DatagateFunction.LOGIN, "Az azonosítást a felhasználó megszakította");
         }

         var2 = this.getKauClientUgyfelkapu(KauAuthMethods.getSelected()).authenticate(var1, KauAuthHelper.getInstance().getMohuUser(), KauAuthHelper.getInstance().getMohuPass());
         break;
      case KAU_EML:
         var3 = new UserAndPassDialog(0, KauAuthMethods.getSelected().getText());
         SwingUtilities.invokeAndWait(() -> {
            var3.setLocationRelativeTo(MainFrame.thisinstance);
            var3.showIfNeed();
         });
         if (var3.getState() == 0) {
            throw new MasterDataProviderException(DatagateFunction.LOGIN, "Az azonosítást a felhasználó megszakította");
         }

         var2 = this.getKauClientEmail(KauAuthMethods.getSelected()).authenticate(var1, KauAuthHelper.getInstance().getMohuUser(), KauAuthHelper.getInstance().getMohuPass());
         break;
      default:
         String var6 = String.format("Nem kezelt KAÜ azonosítási mód %s", KauAuthMethods.getSelected());
         throw new MasterDataProviderException(DatagateFunction.LOGIN, var6);
      }

      return var2;
   }

   protected IKauClient getKauClientUgyfelkapu(KauAuthMethod var1) throws KauClientException {
      if (KauAuthMethod.KAU_UKP.equals(var1)) {
         return new KauClientUgyfelkapuPlusz();
      } else {
         throw new KauClientException("Hibás azonosítási metódus : " + var1);
      }
   }

   protected IKauClient getKauClientEmail(KauAuthMethod var1) throws KauClientException {
      if (KauAuthMethod.KAU_EML.equals(var1)) {
         return new KauClientUgyfelkapuPluszEmail();
      } else {
         throw new KauClientException("Hibás azonosítási metódus : " + var1);
      }
   }
}
