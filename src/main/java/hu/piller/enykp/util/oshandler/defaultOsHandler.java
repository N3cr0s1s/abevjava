package hu.piller.enykp.util.oshandler;

import hu.piller.enykp.interfaces.IOsHandler;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Vector;

public abstract class defaultOsHandler implements IOsHandler {
   java.util.logging.Logger log = java.util.logging.Logger.getLogger(defaultOsHandler.class.getName());
   public static final boolean onDebug = true;

   public String getOsShell() {
      return "";
   }

   public String getOsShellParam() {
      return "";
   }

   public String getOsName() {
      return OS_NAME;
   }

   public String getOsPrefix() {
      return OS_PREFIX;
   }

   public boolean isOs(String var1) {
      return var1.equalsIgnoreCase(OS_NAME);
   }

   public void setExecuteFlag(String var1) throws Exception {
   }

   public String getScriptPostfix() {
      return "";
   }

   public void showMessage(String var1) {
      System.out.println("OsHandler üzenet: " + var1);
   }

   public String getUserHomeDir() {
      this.showDebugMessage("user.home=" + System.getProperty("user.home"));
      String var1 = System.getProperty("user.home");
      if (var1 == null || var1.length() == 0) {
         var1 = this.getEnvironmentVariable("USERPROFILE");
      }

      return var1;
   }

   public void showDebugMessage(String var1) {
      System.out.println("message = " + var1);
   }

   public String getDebugInfo(String[] var1) {
      StringBuffer var2 = new StringBuffer();

      for(int var3 = 0; var3 < var1.length; ++var3) {
         String var4 = var1[var3];
         var2.append(var4);
         var2.append(" ");
      }

      return var2.toString();
   }

   public String getStdError(Process var1) {
      return this.getStdStream(var1.getErrorStream());
   }

   public String getStdInput(Process var1) {
      return this.getStdStream(var1.getInputStream());
   }

   protected String getStdStream(InputStream var1) {
      StringBuffer var2 = new StringBuffer();
      BufferedReader var3 = null;

      try {
         var3 = new BufferedReader(new InputStreamReader(var1));

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

   public void closeProcess(Process var1) {
      try {
         var1.getOutputStream().close();
         var1.getInputStream().close();
         var1.getErrorStream().close();
      } catch (IOException var3) {
         Logger.log(var3);
      }

   }

   public String executeShowResult(String var1) {
      Process var2 = null;

      try {
         this.log.info("defaultOSH : " + var1 + " start");
         var2 = Runtime.getRuntime().exec(new String[]{this.getOsShell(), this.getOsShellParam(), var1});
         String var3 = this.getStdInput(var2);
         this.closeProcess(var2);
         var2 = null;
         this.log.info("defaultOSH : " + var1 + " ok");
         return var3;
      } catch (IOException var6) {
         this.log.info("defaultOSH : " + var1 + " error");
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

   public boolean writeFile(File var1, Vector var2, String var3, boolean var4) {
      PrintWriter var5 = null;

      try {
         var5 = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(var1, var4), var3)));

         for(int var6 = 0; var6 < var2.size(); ++var6) {
            var5.println(var2.elementAt(var6));
         }

         var5.flush();
         var5.close();
         Thread.sleep(500L);
         return true;
      } catch (Exception var9) {
         try {
            if (var5 != null) {
               var5.close();
            }

            if (!var4 && var1.exists()) {
               var1.delete();
            }
         } catch (Exception var8) {
            Logger.log(var9);
         }

         this.showMessage("File kiírása sikertelen: " + var1);
         return false;
      }
   }

   public boolean searchStrInFile(File var1, String var2) {
      if (!var1.exists()) {
         return false;
      } else {
         BufferedReader var3 = null;

         try {
            var3 = new BufferedReader(new FileReader(var1));

            String var4;
            do {
               if ((var4 = var3.readLine()) == null) {
                  var3.close();
                  return false;
               }
            } while(var4.indexOf(var2) == -1);

            return true;
         } catch (Exception var7) {
            try {
               if (var3 != null) {
                  var3.close();
               }
            } catch (Exception var6) {
               Logger.log(var7);
            }

            return false;
         }
      }
   }

   public boolean canCreateDesktopIcon() {
      return true;
   }

   public boolean canCreateMenuItem() {
      return true;
   }

   public String getDirtyEnvironmentVariable(String var1, String var2) {
      return this.getEnvironmentVariable(var1);
   }

   public String getEnvFromFile(String var1, String var2) {
      String var3 = "";
      File var4 = new File(var2);
      if (!var4.exists()) {
         return var3;
      } else {
         BufferedReader var5 = null;
         String var6 = "";

         try {
            var5 = new BufferedReader(new FileReader(var4));

            while((var6 = var5.readLine()) != null && var3.length() == 0) {
               if (var6.toLowerCase().indexOf(var1.toLowerCase()) != -1) {
                  String[] var7 = var6.split("=");
                  if (var7.length > 1) {
                     var3 = var7[1].trim();
                  }
               }
            }

            var5.close();
            return var3;
         } catch (Exception var10) {
            try {
               if (var5 != null) {
                  var5.close();
               }
            } catch (IOException var9) {
               Logger.log(var10);
            }

            return var3;
         }
      }
   }
}
