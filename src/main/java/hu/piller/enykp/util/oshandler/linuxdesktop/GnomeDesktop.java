package hu.piller.enykp.util.oshandler.linuxdesktop;

import me.necrocore.abevjava.NecroFile;

import java.io.File;
import java.util.Vector;

public class GnomeDesktop extends DefaultLinuxDesktopHandler {
   public File getDesktopPath() {
      try {
         File var1 = new NecroFile(System.getProperty("user.home") + File.separator + "Asztal");
         if (var1.exists() && var1.isDirectory() && var1.canWrite()) {
            return var1;
         }

         var1 = new NecroFile(System.getProperty("user.home") + File.separator + "Desktop");
         if (var1.exists() && var1.isDirectory() && var1.canWrite()) {
            return var1;
         }
      } catch (Exception var2) {
         var2.printStackTrace();
      }

      return null;
   }

   public File getMenuPath(String var1) {
      return null;
   }

   public Vector buildItem(String var1, String var2, String var3, String var4, String var5, String var6) {
      Vector var7 = new Vector(15);
      var7.add("[Desktop Entry]");
      var7.add("Type=Application");
      var7.add("Name=" + var1);
      var7.add("Comment=" + var2);
      var7.add("Exec=" + var3 + " " + var4);
      var7.add("Path=" + var5);
      var7.add("Icon=" + var6);
      var7.add("Encoding=UTF-8");
      var7.add("Terminal=false");
      var7.add("Categories=Application;");
      return var7;
   }

   public Vector buildExecItem(String var1, String var2, String var3, String var4, String var5, String var6) {
      return null;
   }
}
