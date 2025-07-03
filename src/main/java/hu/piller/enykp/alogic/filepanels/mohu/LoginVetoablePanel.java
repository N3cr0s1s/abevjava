package hu.piller.enykp.alogic.filepanels.mohu;

import hu.piller.enykp.kauclient.KauAuthMethod;
import hu.piller.enykp.kauclient.KauAuthMethods;
import java.awt.Frame;
import javax.swing.JDialog;
import javax.swing.JFrame;

public class LoginVetoablePanel extends JDialog {
   public LoginVetoablePanel(Frame var1, String var2, boolean var3) {
      super(var1, var2, var3);
   }

   public LoginVetoablePanel(JFrame var1, String var2, boolean var3) {
      super(var1, var2, var3);
   }

   public boolean needAnykLoginDialog() {
      return KauAuthMethods.getSelected() != KauAuthMethod.KAU_DAP;
   }
}
