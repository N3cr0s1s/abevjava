package hu.piller.enykp.alogic.filepanels.mohu;

import hu.piller.enykp.alogic.settingspanel.SettingsStore;
import hu.piller.enykp.niszws.util.KauAuthHelper;
import java.awt.Frame;

public class LoginDialog extends LoginVetoablePanel implements ILoginDialog {
   public static final int INIT_STATE = -1;
   public static final int CANCEL_STATE = 0;
   public static final int OK_ERROR_STATE = 1;
   public static final int NO_AUTH_STATE = 2;
   public static final int OK_OK_STATE = 3;
   public static final int AVDH_NORMAL = 0;
   public static final int AVDH_BATCH = 1;
   public static final int NO_AVDH = 2;
   public static final int AUTH_HEIGHT = 430;
   private boolean groupLogin = false;

   public LoginDialog(Frame var1, String var2, boolean var3) {
      super(var1, var2, var3);
   }

   public boolean showIfNeed() {
      return false;
   }

   public int getState() {
      return 0;
   }

   public void delPass() {
      KauAuthHelper.getInstance().resetOfficeUsername();
   }

   public boolean isGroupLogin() {
      return this.groupLogin;
   }

   public void setGroupLogin(boolean var1) {
      this.groupLogin = var1;
   }

   public String getDefaultCKAzon() {
      String var1 = SettingsStore.getInstance().get("gui", "defaultCKAzon");
      return var1 != null ? var1 : "";
   }

   public boolean useFormDataCKAzon() {
      String var1 = SettingsStore.getInstance().get("gui", "usableCKAzon");
      return var1 == null || var1 != null && var1.equals("1");
   }
}
