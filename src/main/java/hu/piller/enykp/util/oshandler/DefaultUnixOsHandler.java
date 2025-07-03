package hu.piller.enykp.util.oshandler;

import hu.piller.enykp.util.oshandler.linuxdesktop.GnomeDesktop;
import hu.piller.enykp.util.oshandler.linuxdesktop.ILinuxDesktopHandler;
import hu.piller.enykp.util.oshandler.linuxdesktop.KdeDesktop;
import me.necrocore.abevjava.NecroFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Vector;

public class DefaultUnixOsHandler extends DefaultOsHandler {
   private static final String os_shell = "/bin/bash";
   private static final String os_shell_param = "-c";
   private static final String os_shell_env_prefix = "$";
   private static final String os_init_dir = "/etc";
   private String os_user_profile_name = null;
   public static final String PROF_DEF = ".profile";
   public static final String PROF_BASH = ".bash_profile";
   private String os_application_dir = null;
   private String[] browsers = new String[]{"firefox", "opera", "konqueror", "epiphany", "mozilla", "netscape"};
   private String defaultBrowser = "";
   private ILinuxDesktopHandler KDEHandler = new KdeDesktop();
   private ILinuxDesktopHandler GnomeHandler = new GnomeDesktop();

   public String getInitDir() {
      return "/etc";
   }

   public String getApplicationDir() {
      if (this.os_application_dir == null) {
         this.os_application_dir = this.createApplicationDir();
      }

      return this.os_application_dir;
   }

   public String getProgramFilesDir() {
      return this.getApplicationDir();
   }

   public String getProgramFilesx86Dir() {
      return this.getProgramFilesDir();
   }

   public String createApplicationDir() {
      return "/usr/share";
   }

   public String getEnvironmentVariable(String envKey) {
      Process var2 = null;
      String var3 = "$" + envKey;
      BufferedReader var4 = null;

      try {
         var2 = Runtime.getRuntime().exec(new String[]{"/bin/bash", "-c", "echo " + var3});
         var4 = new BufferedReader(new InputStreamReader(var2.getInputStream()));
         String var10 = var4.readLine();
         var4.close();
         this.closeProcess(var2);
         var2 = null;
         this.showDebugMessage(envKey + "=" + var10);
         return var10;
      } catch (Exception var9) {
         Exception var5 = var9;
         var9.printStackTrace();

         try {
            try {
               if (var4 != null) {
                  var4.close();
               }
            } catch (IOException var7) {
               Logger.log(var5);
            }

            if (var2 != null) {
               this.closeProcess(var2);
               var2 = null;
            }
         } catch (Exception var8) {
            Logger.log(var9);
         }

         return "";
      }
   }

   public boolean createEnvironmentVariable(String var1, String var2, String var3, String var4) {
      try {
         this.setUserProfileName();
         File var5 = new NecroFile(this.getUserHomeDir() + File.separator + this.os_user_profile_name);
         if (!this.createExecFile(var5, false)) {
            return false;
         } else if (!this.includeFile(var5, this.getUserHomeDir() + File.separator + "." + var2)) {
            return false;
         } else {
            File var6 = new NecroFile(this.getUserHomeDir() + File.separator + "." + var2);
            if (!this.createExecFile(var6, false)) {
               return false;
            } else {
               this.execute("echo \"export " + var3 + "=" + "\"" + var4 + "\"" + "\" > " + var6.getAbsolutePath());
               this.execute(". " + var6.getAbsolutePath());
               return true;
            }
         }
      } catch (Exception var7) {
         return false;
      }
   }

   private void setUserProfileName() {
      if (this.os_user_profile_name == null) {
         this.os_user_profile_name = this.createUserProfileName();
      }

   }

   private String createUserProfileName() {
      return this.isFileExists(this.getUserHomeDir() + File.separator + ".bash_profile") ? ".bash_profile" : ".profile";
   }

   private boolean isFileExists(String var1) {
      return (new NecroFile(var1)).exists();
   }

   private boolean createExecFile(File var1, boolean var2) {
      if (var1.exists() && !var2) {
         return true;
      } else if (!this.writeFile(var1, new Vector(), "UTF-8", false)) {
         return false;
      } else {
         try {
            this.setExecuteFlag(var1.getAbsolutePath());
            return true;
         } catch (Exception var4) {
            var4.printStackTrace();
            return false;
         }
      }
   }

   private boolean includeFile(File var1, String var2) {
      if (!this.searchStrInFile(var1, var2)) {
         try {
            this.execute("echo \". " + var2 + "\" >> " + var1.getAbsolutePath());
         } catch (Exception var4) {
            return false;
         }
      }

      return true;
   }

   public void execute(String var1) throws Exception {
      this.execute(var1, (String[])null, (File)null, true);
   }

   public void execute(String var1, String[] var2, File var3) throws Exception {
      this.execute(var1, var2, var3, false);
   }

   public void execute(String var1, String[] var2, File var3, boolean var4) throws Exception {
      String[] var5 = new String[]{"/bin/bash", "-c", var1};
      String var6 = this.getDebugInfo(var5);
      Process var7 = null;

      try {
         this.showDebugMessage("Start:" + var6);
         var7 = Runtime.getRuntime().exec(var5, var2, var3);
         if (var4) {
            String var8 = this.getStdError(var7);
            if (var8.trim().length() != 0) {
               this.showMessage(var8);
            }
         }

         this.closeProcess(var7);
         var7 = null;
      } catch (IOException var11) {
         var11.printStackTrace();

         try {
            if (var7 != null) {
               this.closeProcess(var7);
               var7 = null;
            }
         } catch (Exception var10) {
            Logger.log(var11);
         }

         throw var11;
      }
   }

   public void setExecuteFlag(String var1) throws Exception {
      String var2 = "chmod 755 " + var1;
      this.execute(var2);
   }

   public void createDesktopIcon(String var1, String var2, String var3, String var4, String var5, String var6, String var7) {
      this.createItem(this.KDEHandler, this.KDEHandler.getDesktopPath(), var1, var2, var3, var4, var5, var6, var7);
      this.createItem(this.GnomeHandler, this.GnomeHandler.getDesktopPath(), var1, var2, var3, var4, var5, var6, var7);
   }

   public void createMenuItem(String var1, String var2, String var3, String var4, String var5, String var6, String var7) {
   }

   public void createItem(ILinuxDesktopHandler var1, File var2, String var3, String var4, String var5, String var6, String var7, String var8, String var9) {
      if (var2 != null) {
         Vector var10 = var1.buildItem(var4, var5, var6, var7, var8, var9);
         File var11 = new NecroFile(var3 + File.separator + "createdesktopshortcut.lin");
         if (!this.writeFile(var11, var10, "UTF-8", false)) {
            this.showMessage("Indító ikon létrehozása sikertelen");
            return;
         }

         try {
            File var12 = new NecroFile(var2.getAbsolutePath() + File.separator + var4 + ".desktop");
            this.execute("cat \"" + var11.getAbsolutePath() + "\"" + " > " + "\"" + var12.getAbsolutePath() + "\"");
            this.setExecuteFlag("\"" + var12.getAbsolutePath() + "\"");
         } catch (Exception var13) {
            this.showMessage("Hiba az asztali ikon létrehozása során (" + var13.getMessage() + ")");
         }

         var11.delete();
      }

   }

   public String getSystemBrowserPath() {
      if (this.defaultBrowser != null) {
         return this.defaultBrowser;
      } else {
         for(int var1 = 0; var1 < this.browsers.length && this.defaultBrowser == null; this.defaultBrowser = this.executeShowResult("which " + this.browsers[var1++])) {
         }

         return this.defaultBrowser;
      }
   }

   public String getDirtyEnvironmentVariable(String var1, String var2) {
      String var3 = this.getEnvironmentVariable(var1);
      return var3.length() > 0 ? var3 : this.getEnvFromFile(var1, this.getUserHomeDir() + File.separator + "." + var2);
   }

   public boolean canCreateMenuItem() {
      return false;
   }
}
