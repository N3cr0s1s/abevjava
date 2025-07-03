package hu.piller.enykp.alogic.panels;

import hu.piller.enykp.gui.framework.MainFrame;
import hu.piller.enykp.util.base.PropertyList;
import hu.piller.enykp.util.base.tabledialog.TableDialog;
import java.io.File;

public class SendLog {
   public static void show() {
      String var0 = (String)PropertyList.getInstance().get("prop.usr.krdir");
      String var1 = "feladas_naplo.log";
      TableDialog.showTableDialog(MainFrame.thisinstance, "Megjelölés, átadás napló", new File(var0, var1), new String[]{"Időpont", "Állomány", "Felhasználó", "Tevékenység", "Információ"}, ";");
   }
}
