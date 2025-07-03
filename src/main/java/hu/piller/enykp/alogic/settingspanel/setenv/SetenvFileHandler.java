package hu.piller.enykp.alogic.settingspanel.setenv;

import hu.piller.enykp.util.base.PropertyList;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class SetenvFileHandler {
   private String HEADER = "";
   private String postfix = "";

   public SetenvFileHandler() {
      String var1 = System.getProperty("os.name").toLowerCase();
      if (var1.indexOf("windows") != -1) {
         this.HEADER = "@REM ECHO OFF";
         this.postfix = ".bat";
      } else {
         this.postfix = "";
      }

   }

   public String[] read() throws SetenvException {
      ArrayList var1 = new ArrayList();
      BufferedReader var2 = null;

      try {
         var2 = new BufferedReader(new FileReader(this.getAbevrootPath() + File.separator + "setenv" + this.postfix));
         String var3 = "";

         while((var3 = var2.readLine()) != null) {
            if (var3.indexOf("@REM ECHO OFF") == -1) {
               var1.add(var3);
            }
         }
      } catch (IOException var11) {
         throw new SetenvException(var11.getMessage());
      } finally {
         if (var2 != null) {
            try {
               var2.close();
            } catch (Exception var10) {
            }
         }

      }

      return (String[])var1.toArray(new String[var1.size()]);
   }

   public void write(String[] var1) throws SetenvException {
      BufferedWriter var2 = null;

      try {
         var2 = new BufferedWriter(new FileWriter(this.getAbevrootPath() + File.separator + "setenv" + this.postfix));
         if (!"".equals(this.HEADER)) {
            var2.write(this.HEADER);
            var2.newLine();
         }

         String[] var3 = var1;
         int var4 = var1.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            String var6 = var3[var5];
            var2.write(var6);
            var2.newLine();
         }

         var2.flush();
      } catch (IOException var14) {
         throw new SetenvException(var14.getMessage());
      } finally {
         if (var2 != null) {
            try {
               var2.close();
            } catch (IOException var13) {
            }
         }

      }
   }

   public String getAbevrootPath() {
      return this.replaceBackslash((String)PropertyList.getInstance().get("prop.sys.root"));
   }

   private String replaceBackslash(String var1) {
      return var1 != null && var1.trim().length() != 0 ? var1.replaceAll("\\\\", "/") : var1;
   }
}
