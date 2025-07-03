package hu.piller.enykp.util.oshandler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Vector;

public class winMeOsHandler extends win9xOsHandler {
   public static final char FILE_EXT_CHAR = '.';

   public boolean createEnvironmentVariable(String var1, String var2, String var3, String var4) {
      try {
         String var5 = var4.replace('/', '\\');
         String var6 = this.getRootDrive() + this.os_user_profile_name;
         File var7 = new File(var6);
         String var8 = "SET " + var3 + "=" + var5;
         if (!this.searchStrInFile(var7, var3)) {
            Vector var9 = new Vector(1);
            var9.add(var8);
            this.addToFile(var7, var9);
         } else if (!this.searchStrInFile(var7, var8)) {
            File var11 = new File(this.replaceFileExtension(var6, "BAK"));
            this.replaceInFile(var7, var11, (String)null, (String)null);
            this.replaceInFile(var11, var7, var3, var8);
         }

         return true;
      } catch (Exception var10) {
         return false;
      }
   }

   private boolean addToFile(File var1, Vector var2) {
      return this.writeFile(var1, var2, "ISO-8859-2", true);
   }

   private boolean replaceInFile(File var1, File var2, String var3, String var4) {
      if (!var1.exists()) {
         return false;
      } else {
         BufferedReader var5 = null;
         PrintWriter var6 = null;

         try {
            var5 = new BufferedReader(new FileReader(var1));
            var6 = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(var2, false), "ISO-8859-2")));

            while(true) {
               String var7;
               while((var7 = var5.readLine()) != null) {
                  if (var3 != null && var7.indexOf(var3) != -1) {
                     var6.println(var4);
                  } else {
                     var6.println(var7);
                  }
               }

               var6.flush();
               var5.close();
               var6.close();
               return false;
            }
         } catch (Exception var12) {
            try {
               if (var5 != null) {
                  var5.close();
               }
            } catch (Exception var11) {
               Logger.log(var12);
            }

            try {
               if (var6 != null) {
                  var6.close();
               }
            } catch (Exception var10) {
               Logger.log(var12);
            }

            try {
               if (var2.exists()) {
                  var2.delete();
               }
            } catch (Exception var9) {
               Logger.log(var12);
            }

            return false;
         }
      }
   }

   private String replaceFileExtension(String var1, String var2) {
      return var1.substring(0, var1.indexOf(46)) + '.' + var2;
   }

   public String getDirtyEnvironmentVariable(String var1, String var2) {
      String var3 = this.getEnvironmentVariable(var1);
      return var3.length() > 0 ? var3 : this.getEnvFromFile(var1, this.getRootDrive() + this.os_user_profile_name);
   }
}
