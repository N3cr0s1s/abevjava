package hu.piller.enykp.util.base;

import hu.piller.enykp.alogic.settingspanel.SettingsStore;
import hu.piller.enykp.gui.framework.MainFrame;
import java.awt.Frame;
import javax.swing.JDialog;

public class SizeAndPositonSaveDialog extends JDialog {
   public SizeAndPositonSaveDialog(Frame var1) {
      super(var1);
   }

   public void setLoadedSize(String var1, int var2, int var3) {
      try {
         SettingsStore var4 = SettingsStore.getInstance();
         int var5 = Integer.parseInt(var4.get(var1, "width"));
         int var6 = Integer.parseInt(var4.get(var1, "height"));
         int var7 = Integer.parseInt(var4.get(var1, "xPos"));
         int var8 = Integer.parseInt(var4.get(var1, "yPos"));
         this.setSize(var5, var6);
         this.setLocation(var7, var8);
      } catch (Exception var9) {
         this.setSize(var2, var3);
         this.setLocationRelativeTo(MainFrame.thisinstance);
      }

   }

   public void loadSettings(String var1) {
      SettingsStore var2 = SettingsStore.getInstance();
      var2.set(var1, "width", this.getWidth() + "");
      var2.set(var1, "height", this.getHeight() + "");
      var2.set(var1, "xPos", this.getLocation().x + "");
      var2.set(var1, "yPos", this.getLocation().y + "");
   }
}
