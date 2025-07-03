package hu.piller.enykp.util.oshandler;

import hu.piller.enykp.util.oshandler.linuxdesktop.ILinuxDesktopHandler;
import hu.piller.enykp.util.oshandler.linuxdesktop.MacDesktopHandler;
import java.io.File;
import java.util.Vector;

public class otherUnixOsHandler extends defaultUnixOsHandler {
   private ILinuxDesktopHandler macDesktopHandler = new MacDesktopHandler();

   public boolean canCreateDesktopIcon() {
      return true;
   }

   public boolean canCreateMenuItem() {
      return false;
   }

   public void createDesktopIcon(String var1, String var2, String var3, String var4, String var5, String var6, String var7) {
      this.createItem(this.macDesktopHandler, this.macDesktopHandler.getDesktopPath(), var1, var2, var3, var4, var5, var6, var7);
   }

   public void createMenuItem(String var1, String var2, String var3, String var4, String var5, String var6, String var7) {
   }

   public void createItem(ILinuxDesktopHandler var1, File var2, String var3, String var4, String var5, String var6, String var7, String var8, String var9) {
      if (var2 != null) {
         Vector var10 = var1.buildExecItem(var4, var5, var6, var7, var8, var9);
         File var11 = new File(var8 + "/abevjava.app/Contents");
         File var12 = new File(var8 + "/abevjava.app/Contents/Resources");
         File var13 = new File(var2 + "/abevjava.app/Contents/Resources");
         File var14 = new File(var2 + "/abevjava.app/Contents");
         File var15 = new File(var2 + "/abevjava.app/Contents/MacOS");
         if (!var15.exists()) {
            var15.mkdirs();
         }

         if (!var13.exists()) {
            var13.mkdirs();
         }

         try {
            this.execute("cp \"" + var12.getAbsolutePath() + "/abevjava.icns" + "\"" + " " + "\"" + var13.getAbsolutePath() + "/abevjava.icns" + "\"");
            this.execute("cp \"" + var11.getAbsolutePath() + "/Info.plist" + "\"" + " " + "\"" + var14.getAbsolutePath() + "/Info.plist" + "\"");
            File var16 = new File(var15 + File.separator + "abevjava");
            if (!this.writeFile(var16, var10, "UTF-8", false)) {
               this.showMessage("Indító ikon létrehozása sikertelen");
               return;
            }

            this.setExecuteFlag("\"" + var16.getAbsolutePath() + "\"");
         } catch (Exception var17) {
            this.showMessage("Hiba az asztali ikon létrehozása során (" + var17.getMessage() + ")");
         }
      }

   }

   public String getSystemBrowserPath() {
      return "/usr/bin/open";
   }
}
