package hu.piller.enykp.util.oshandler;

import me.necrocore.abevjava.NecroFile;

import java.io.File;
import java.io.IOException;
import java.util.Vector;

public class Win9xOsHandler extends DefaultWindowsOsHandler {
   protected String root_drive = null;
   protected String init_dir = null;
   protected String os_user_profile_name = "AUTOEXEC.BAT";

   public Win9xOsHandler() {
      this.showDebugMessage(this.getClass().toString());
   }

   public String createOsShell() {
      return "command.com";
   }

   public boolean getStdErrReadable() {
      return false;
   }

   public String createInitDir() {
      if (this.init_dir == null) {
         this.init_dir = this.getEnvironmentVariable("windir");
      }

      return this.init_dir;
   }

   public String createApplicationDir() {
      return this.getRootDrive() + "Program Files";
   }

   public String createUserHomeDir() {
      return this.getRootDrive() + "Dokumentumok";
   }

   public String getRootDrive() {
      if (this.root_drive == null) {
         this.root_drive = this.createInitDir().substring(0, 1) + ":\\";
      }

      return this.root_drive;
   }

   public String getEnvironmentVariable(String envKey) {
      Process var2 = null;

      try {
         String var3 = "%" + envKey + "%";
         var2 = Runtime.getRuntime().exec(new String[]{this.getOsShell(), this.getOsShellParam(), "echo", var3});
         String var4 = this.getStdInput(var2);
         this.closeProcess(var2);
         var2 = null;
         this.showDebugMessage(envKey + "=" + var4);
         if (var4.indexOf(var3) == 0) {
            return "";
         } else {
            return var4.toLowerCase().indexOf("echo") > -1 ? "" : this.convCP852(this.removeWhiteChars(var4));
         }
      } catch (IOException var6) {
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

   private String removeWhiteChars(String var1) {
      StringBuffer var2 = new StringBuffer();
      char[] var3 = var1.toCharArray();

      for(int var4 = 0; var4 < var3.length; ++var4) {
         char var5 = var3[var4];
         if (var5 != '"') {
            var2.append(var5);
         }
      }

      return var2.toString();
   }

   public boolean createEnvironmentVariable(String var1, String var2, String var3, String var4) {
      try {
         String var5 = var4.replace('/', '\\');
         String var6 = var2.substring(0, 8);
         String var7 = this.getEnvironmentVariable(var3);
         if (var7 != null && var7.length() > 0 && var7.equalsIgnoreCase(var5)) {
            return true;
         } else {
            File var8 = new NecroFile(this.getRootDrive() + this.os_user_profile_name);
            String var9 = this.getRootDrive() + var6 + ".bat";
            if (!this.searchStrInFile(var8, var9)) {
               Vector var10 = new Vector(1);
               var10.add(var9);
               this.addToFile(var8, var10);
            }

            File var14 = new NecroFile(var9);
            Vector var11 = new Vector(1);
            String var12 = "SET " + var3 + "=\"" + var5 + "\"";
            var11.add(var12);
            this.reWriteFile(var14, var11);
            this.execute(var14.getAbsolutePath());
            return true;
         }
      } catch (Exception var13) {
         return false;
      }
   }

   private boolean addToFile(File var1, Vector var2) {
      return this.writeFile(var1, var2, "ISO-8859-2", true);
   }

   private boolean reWriteFile(File var1, Vector var2) {
      return this.writeFile(var1, var2, "ISO-8859-2", false);
   }

   public String getDirtyEnvironmentVariable(String var1, String var2) {
      String var3 = this.getEnvironmentVariable(var1);
      return var3.length() > 0 ? var3 : this.getEnvFromFile(var1, this.getRootDrive() + var2.substring(0, 8) + ".bat");
   }
}
