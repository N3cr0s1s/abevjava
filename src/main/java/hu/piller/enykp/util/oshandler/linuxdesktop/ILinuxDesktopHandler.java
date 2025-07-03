package hu.piller.enykp.util.oshandler.linuxdesktop;

import java.io.File;
import java.util.Vector;

public interface ILinuxDesktopHandler {
   File getDesktopPath();

   File getMenuPath(String var1);

   Vector buildItem(String var1, String var2, String var3, String var4, String var5, String var6);

   Vector buildExecItem(String var1, String var2, String var3, String var4, String var5, String var6);
}
