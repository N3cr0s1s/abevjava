package hu.piller.enykp.alogic.filepanels.mohu;

import hu.piller.enykp.alogic.settingspanel.SettingsStore;
import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.gui.framework.MainFrame;
import hu.piller.enykp.kauclient.KauAuthMethod;
import hu.piller.enykp.kauclient.KauAuthMethods;
import hu.piller.enykp.niszws.util.GateType;
import hu.piller.enykp.util.JavaInfo;

public final class LoginDialogFactory {
   public static LoginDialog create(GateType var0, int var1, boolean var2, String var3) {
      String var4 = "Ügyfélkapu+ azonosítás 1. lépés";

      try {
         String var5 = SettingsStore.getInstance().get("gui", "kaubrowser");
         if (var5 == null) {
            var5 = "";
         }

         if (KauAuthMethod.KAU_UKP.name().equals(var5)) {
            var4 = "Ügyfélkapu+ azonosítás 1. lépés - hitelesítő alkalmazással";
         } else if (KauAuthMethod.KAU_EML.name().equals(var5)) {
            var4 = "Ügyfélkapu+ azonosítás 1. lépés - e-mailes kóddal";
         }
      } catch (Exception var6) {
         var4 = "Ügyfélkapu+ azonosítás 1. lépés";
      }

      KauAuthMethod var7 = KauAuthMethods.getSelected();
      if (var7 == KauAuthMethod.KAU_ALL && !JavaInfo.isJavaFxAvailable()) {
         GuiUtil.showMessageDialog(MainFrame.thisinstance, JavaInfo.getNoJFXMessageLines(), "KAÜ azonosítás", 1);
         return null;
      } else if (var7 == KauAuthMethod.KAU_ALL) {
         return (LoginDialog)(var0 == GateType.UGYFELKAPU ? new UserAndPass4Send(var1) : new OfficeUserAndPass4Send(var1, var2, var3));
      } else if (var7 == KauAuthMethod.KAU_DAP) {
         return new SimpleCegHivataliKapuAzonPanel(var1, var2, var3);
      } else {
         return (LoginDialog)(var0 == GateType.UGYFELKAPU ? new UserAndPassDialog(var1, var4) : new OfficeUserAndPassDialog(var1, var2, var3, var4));
      }
   }
}
