package hu.piller.enykp.util.oshandler;

import me.necrocore.abevjava.NecroFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Vector;

public class DefaultWindowsOsHandler extends DefaultOsHandler {
   public static final String os_shell_env_prefix = "%";
   public static final String os_shell_env_postfix = "%";
   public static final String WIN_VBS = "vbs";
   private static final String def_os_shell_param = "/C";
   private static final String def_os_shell_vbs = "wscript";
   private String def_os_shell = null;
   private String os_init_dir = null;
   private String os_application_dir = null;
   private String os_programfiles_dir = null;
   private String os_programfiles_x86_dir = null;
   private String user_home_dir = null;
   public static final String CHARACTER_ENCODING = "ISO-8859-2";
   java.util.logging.Logger log = java.util.logging.Logger.getLogger(DefaultWindowsOsHandler.class.getName());
   private static char[] cv = new char[]{'á', 'Á', 'é', 'É', 'í', 'Í', 'ó', 'Ó', 'ö', 'Ö', 'ő', 'Ő', 'ú', 'Ú', 'ü', 'Ü', 'ű', 'Ű'};
   private static char[] cp852 = new char[]{' ', 'ľ', '\u0082', '\u0090', 'Ą', 'Ö', '˘', 'ŕ', '\u0094', '\u0099', '\u008b', '\u008a', 'Ł', 'é', '\u0081', '\u009a', 'ű', 'ë'};

   public String getOsShell() {
      if (this.def_os_shell == null) {
         this.def_os_shell = this.createOsShell();
      }

      return this.def_os_shell;
   }

   public String createOsShell() {
      return "cmd.exe";
   }

   public String getOsShellParam() {
      return "/C";
   }

   public String getOsShellVbs() {
      return "wscript";
   }

   public boolean getStdErrReadable() {
      return true;
   }

   public String getInitDir() {
      if (this.os_init_dir == null) {
         this.os_init_dir = this.createInitDir();
      }

      return this.os_init_dir;
   }

   public String createInitDir() {
      return this.getEnvironmentVariable("windir");
   }

   public String getApplicationDir() {
      if (this.os_application_dir == null) {
         this.os_application_dir = this.createApplicationDir();
      }

      return this.os_application_dir;
   }

   public String createApplicationDir() {
      return this.getEnvironmentVariable("ProgramFiles");
   }

   public String getProgramFilesDir() {
      if (this.os_programfiles_dir == null) {
         this.os_programfiles_dir = this.createProgramFilesDir();
      }

      return this.os_programfiles_dir;
   }

   public String getProgramFilesx86Dir() {
      if (this.os_programfiles_x86_dir == null) {
         this.os_programfiles_x86_dir = this.getEnvironmentVariable("ProgramFiles(x86)");
      }

      return this.os_programfiles_x86_dir;
   }

   public String createProgramFilesDir() {
      return this.getApplicationDir();
   }

   public String getUserHomeDir() {
      if (this.user_home_dir == null) {
         this.user_home_dir = this.createUserHomeDir();
      }

      return this.user_home_dir;
   }

   public String createUserHomeDir() {
      String var1 = System.getProperty("user.home");
      if (var1 == null || var1.length() == 0) {
         var1 = this.getEnvironmentVariable("USERPROFILE");
      }

      return var1;
   }

   public String getEnvironmentVariable(String envKey) {
      Process var2 = null;

      try {
         String var3 = "%" + envKey + "%";
         this.log.info("getEnvironmentVariable " + envKey + " - start");
         var2 = Runtime.getRuntime().exec(new String[]{this.getOsShell(), this.getOsShellParam(), "echo", var3});
         String var4 = this.getStdInput(var2);
         this.log.info("getEnvironmentVariable " + envKey + " - ok " + var4);
         this.closeProcess(var2);
         var2 = null;
         return var4.indexOf(var3) == 0 ? "" : this.convCP852(var4);
      } catch (IOException var6) {
         this.log.info("getEnvironmentVariable " + envKey + " - error");
         var6.printStackTrace();

         try {
            if (var2 != null) {
               this.closeProcess(var2);
               var2 = null;
            }
         } catch (Exception var5) {
            Logger.log(var6);
         }

         return "";
      }
   }

   public boolean createEnvironmentVariable(String var1, String var2, String var3, String var4) {
      File var5 = new NecroFile(var1 + File.separator + "createuserenv.vbs");
      Vector var6 = new Vector(15);
      var6.add("set WshShell = WScript.CreateObject(\"WScript.Shell\")");
      var6.add("Set colUsrEnvVars = WshShell.Environment(\"USER\")");
      var6.add("colUsrEnvVars(\"" + var3 + "\") = \"" + var4 + "\"");
      if (!this.writeFile(var5, var6, "ISO-8859-2", false)) {
         this.showMessage("Környezeti változó létrehozása sikertelen");
         return false;
      } else {
         this.showDebugMessage(var6.toString());

         try {
            Thread.sleep(500L);
            this.execute(var5.toString());
            return true;
         } catch (Exception var8) {
            this.showMessage("Hiba a környezeti változó létrehozása során (" + var8.getMessage() + ")");
            return false;
         }
      }
   }

   public void execute(String var1) throws Exception {
      this.execute(var1, (String[])null, (File)null, true);
   }

   public void execute(String var1, String[] var2, File var3) throws Exception {
      this.execute(var1, var2, var3, false);
   }

   public void execute(String var1, String[] var2, File var3, boolean var4) throws Exception {
      if (var1.toLowerCase().startsWith("start microsoft-edge:")) {
         var1 = "start " + var1.substring(21).replaceAll(" ", "%20");
      }

      String[] var5;
      if (var1.toLowerCase().endsWith("vbs")) {
         var5 = new String[]{this.getOsShell(), this.getOsShellParam(), this.getOsShellVbs(), var1};
      } else {
         var5 = new String[]{this.getOsShell(), this.getOsShellParam(), var1};
      }

      String var6 = this.getDebugInfo(var5);
      Process var7 = null;

      try {
         this.showDebugMessage("Start:" + var6);
         this.log.info("execute " + var6 + " - start");
         var7 = Runtime.getRuntime().exec(var5, var2, var3);
         if (var4) {
            String var8 = "";
            if (this.getStdErrReadable()) {
               var8 = this.getStdError(var7);
               if (var8.trim().length() != 0) {
                  this.showMessage(var8);
               }
            }
         }

         this.log.info("execute " + var6 + " - ok");
         this.closeProcess(var7);
         var7 = null;
      } catch (IOException var11) {
         this.log.info("execute " + var6 + " - error");
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

   public String getScriptPostfix() {
      return ".bat";
   }

   public void createDesktopIcon(String var1, String var2, String var3, String var4, String var5, String var6, String var7) {
      File var8 = new NecroFile(var1 + File.separator + "createdesktopshortcut.vbs");
      Vector var9 = new Vector(15);
      var9.add("set WshShell = WScript.CreateObject(\"WScript.Shell\")");
      var9.add("strDest = WshShell.SpecialFolders(\"Desktop\")");
      var9.add("set oShellLink = WshShell.CreateShortcut(strDest & \"\\" + var2 + ".lnk\")");
      var9.add("oShellLink.TargetPath = \"" + var4 + "\"");
      var9.add("oShellLink.Arguments = \"" + var5 + "\"");
      var9.add("oShellLink.WindowStyle = 1");
      var9.add("oShellLink.IconLocation = \"" + var7 + "\"");
      var9.add("oShellLink.Description = \"" + var3 + "\"");
      var9.add("oShellLink.WorkingDirectory = \"" + var6 + "\"");
      var9.add("oShellLink.Save");
      if (!this.writeFile(var8, var9, "ISO-8859-2", false)) {
         this.showMessage("Asztali ikon létrehozása sikertelen");
      } else {
         try {
            Thread.sleep(1000L);
            this.execute(var8.toString());
         } catch (Exception var11) {
            this.showMessage("Hiba az asztali ikon létrehozása során (" + var11.getMessage() + ")");
         }

      }
   }

   public void createMenuItem(String var1, String var2, String var3, String var4, String var5, String var6, String var7) {
      File var8 = new NecroFile(var1 + File.separator + "createmenushortcut.vbs");
      Vector var9 = new Vector(15);
      var9.add("set WshShell = WScript.CreateObject(\"WScript.Shell\")");
      var9.add("strDest = WshShell.SpecialFolders(\"StartMenu\")");
      var9.add("set oShellLink = WshShell.CreateShortcut(strDest & \"\\" + var2 + ".lnk\")");
      var9.add("oShellLink.TargetPath = \"" + var4 + "\"");
      var9.add("oShellLink.Arguments = \"" + var5 + "\"");
      var9.add("oShellLink.WindowStyle = 1");
      var9.add("oShellLink.IconLocation = \"" + var7 + "\"");
      var9.add("oShellLink.Description = \"" + var3 + "\"");
      var9.add("oShellLink.WorkingDirectory = \"" + var6 + "\"");
      var9.add("oShellLink.Save");
      if (!this.writeFile(var8, var9, "ISO-8859-2", false)) {
         this.showMessage("Startmenu elem létrehozása sikertelen");
      } else {
         try {
            Thread.sleep(1000L);
            this.execute(var8.toString());
         } catch (Exception var11) {
            this.showMessage("Hiba a startmenü elem létrehozása során (" + var11.getMessage() + ")");
         }

      }
   }

   public String getSystemBrowserPath() {
      String var1 = this.getProgramFilesDir();
      File var2 = new NecroFile(var1 + "\\Microsoft\\Edge\\Application\\msedge.exe");
      if (var2.exists()) {
         return var2.getAbsolutePath();
      } else {
         var1 = this.getProgramFilesx86Dir();
         var2 = new NecroFile(var1 + "\\Microsoft\\Edge\\Application\\msedge.exe");
         if (var2.exists()) {
            return var2.getAbsolutePath();
         } else {
            var1 = this.getProgramFilesDir();
            var2 = new NecroFile(var1 + "\\Internet Explorer\\iexplore.exe");
            if (var2.exists()) {
               return var2.getAbsolutePath();
            } else {
               var1 = this.getProgramFilesx86Dir();
               var2 = new NecroFile(var1 + "\\Internet Explorer\\iexplore.exe");
               return var2.getAbsolutePath();
            }
         }
      }
   }

   protected String getStdStream(InputStream var1) {
      StringBuffer var2 = new StringBuffer();
      BufferedReader var3 = null;

      try {
         var3 = new BufferedReader(new InputStreamReader(var1, "ISO-8859-2"));

         String var4;
         while((var4 = var3.readLine()) != null) {
            var2.append(var4);
         }

         var3.close();
         return var2.toString();
      } catch (IOException var7) {
         if (var3 != null) {
            try {
               var3.close();
            } catch (IOException var6) {
               Logger.log(var7);
            }
         }

         var7.printStackTrace();
         return var7.getMessage();
      }
   }

   protected String convCP852(String var1) {
      if (var1 == null) {
         return null;
      } else {
         char[] var2 = var1.toCharArray();
         StringBuffer var3 = new StringBuffer(var2.length);

         for(int var4 = 0; var4 < var2.length; ++var4) {
            for(int var5 = 0; var5 < cp852.length; ++var5) {
               if (var2[var4] == cp852[var5]) {
                  var2[var4] = cv[var5];
                  break;
               }
            }

            var3.append(var2[var4]);
         }

         return var3.toString();
      }
   }
}
