package hu.piller.enykp.util.oshandler;

import hu.piller.enykp.interfaces.IOsHandler;
import me.necrocore.abevjava.NecroFile;
import me.necrocore.abevjava.NecroFileOutputStream;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Vector;

public abstract class DefaultOsHandler implements IOsHandler {

   private static final org.slf4j.Logger logger = LoggerFactory.getLogger(DefaultOsHandler.class);

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

      String userProfile = this.getEnvironmentVariable("USERPROFILE");

      if(userProfile == null) {
         return System.getProperty("user.home");
      }

      this.showDebugMessage("user.home.dir=" + userProfile);
      return userProfile;
   }

   public void showDebugMessage(String debugMessage) {
      System.out.println("message = " + debugMessage);
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

   public void closeProcess(Process process) {
      try {
         process.getOutputStream().close();
         process.getInputStream().close();
         process.getErrorStream().close();
      } catch (IOException e) {
         logger.error("Error while closing process: ", e);
         Logger.log(e);
      }

   }

   public String executeShowResult(String var1) {
      Process process = null;

      try {
         logger.info("defaultOSH : {} start", var1);
         process = Runtime.getRuntime().exec(new String[]{this.getOsShell(), this.getOsShellParam(), var1});
         String stdInput = this.getStdInput(process);
         this.closeProcess(process);
         process = null;
         logger.info("defaultOSH : {} ok", var1);
         return stdInput;
      } catch (IOException ioException) {
         logger.error("defaultOSH : {} error", var1);

         try {
            if (process != null) {
               this.closeProcess(process);
               process = null;
            }
         } catch (Exception e) {
            logger.error("Error while closing process: ", e);
            Logger.log(e);
         }

         return "";
      }
   }

   public boolean writeFile(File var1, Vector<?> var2, String var3, boolean var4) {
      PrintWriter printWriter = null;

      try {
         printWriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new NecroFileOutputStream(var1, var4), var3)));

         for(int var6 = 0; var6 < var2.size(); ++var6) {
            printWriter.println(var2.elementAt(var6));
         }

         printWriter.flush();
         printWriter.close();
         Thread.sleep(500L);  // TODO: We really need this?
         return true;
      } catch (Exception var9) {
         try {
            if (printWriter != null) {
               printWriter.close();
            }

            if (!var4 && var1.exists()) {
               var1.delete();
            }
         } catch (Exception e) {
            logger.error("Error while closing printWriter: ", e);
            Logger.log(e);
         }
         logger.error("Error while writing file: ", var9);
         Logger.log(var9);
         this.showMessage("File kiírása sikertelen: " + var1);
         return false;
      }
   }

   public boolean searchStrInFile(File file, String var2) {
      if (!file.exists()) {
         return false;
      }

      BufferedReader bufferedReader = null;

      try {
         bufferedReader = new BufferedReader(new FileReader(file));

         String var4;
         do {
            if ((var4 = bufferedReader.readLine()) == null) {
               bufferedReader.close();
               return false;
            }
         } while(!var4.contains(var2));

         return true;
      } catch (Exception e) {
         try {
            if (bufferedReader != null) {
               bufferedReader.close();
            }
         } catch (Exception var6) {
            logger.error("Error while closing bufferedReader: ", var6);
            Logger.log(var6);
         }

         logger.error("Error while searching str in file: ", e);
         Logger.log(e);

         return false;
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
      File var4 = new NecroFile(var2);
      if (!var4.exists()) {
         return var3;
      } else {
         BufferedReader var5 = null;
         String var6 = "";

         try {
            var5 = new BufferedReader(new FileReader(var4));

            while((var6 = var5.readLine()) != null && var3.isEmpty()) {
               if (var6.toLowerCase().contains(var1.toLowerCase())) {
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
