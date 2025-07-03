package hu.piller.enykp.interfaces;

import java.io.File;

public interface IOsHandler {
   String OS_NAME = System.getProperty("os.name").toLowerCase();
   String OS_PATCH = System.getProperty("sun.os.patch.level");
   String OS_PREFIX = OS_NAME.substring(0, 3);
   String ROOT_INIT_DIR = "";
   String ROOT_APPLICATION_DIR = "";

   String getOsName();

   String getOsPrefix();

   boolean isOs(String var1);

   String getInitDir();

   String getProgramFilesDir();

   String getProgramFilesx86Dir();

   String getApplicationDir();

   String getUserHomeDir();

   String getEnvironmentVariable(String envKey);

   String getDirtyEnvironmentVariable(String var1, String var2);

   boolean createEnvironmentVariable(String var1, String var2, String var3, String var4);

   void execute(String var1, String[] var2, File var3) throws Exception;

   void execute(String var1) throws Exception;

   String executeShowResult(String var1) throws Exception;

   void setExecuteFlag(String var1) throws Exception;

   String getScriptPostfix();

   boolean canCreateDesktopIcon();

   void createDesktopIcon(String var1, String var2, String var3, String var4, String var5, String var6, String var7);

   boolean canCreateMenuItem();

   void createMenuItem(String var1, String var2, String var3, String var4, String var5, String var6, String var7);

   String getSystemBrowserPath();
}
