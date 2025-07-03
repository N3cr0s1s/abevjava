package hu.piller.enykp.util.oshandler.linuxdesktop;

import me.necrocore.abevjava.NecroFile;

import java.io.File;
import java.util.Vector;

public class MacDesktopHandler implements ILinuxDesktopHandler {
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
      try {
         File var2 = new NecroFile(System.getProperty("user.home") + File.separator + ".gnome" + File.separator + "apps");
         File var3 = new NecroFile(var2, var1);
         if (!var3.exists()) {
            if (var3.mkdirs()) {
               return var3;
            }
         } else if (var3.isDirectory()) {
            return var3;
         }
      } catch (Exception var4) {
         var4.printStackTrace();
      }

      return null;
   }

   public Vector buildItem(String var1, String var2, String var3, String var4, String var5, String var6) {
      return null;
   }

   public Vector buildExecItem(String var1, String var2, String var3, String var4, String var5, String var6) {
      Vector var7 = new Vector(15);
      var7.add("#!/bin/sh");
      var7.add("cd \"" + var5 + "\"");
      var7.add("./abevjava_start");
      return var7;
   }
}
