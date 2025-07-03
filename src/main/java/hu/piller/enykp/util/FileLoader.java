package hu.piller.enykp.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class FileLoader {
   public static String loadTextFile(String var0, String var1) throws IOException {
      StringBuffer var2 = new StringBuffer();
      BufferedReader var3 = new BufferedReader(new FileReader(var0 + File.separator + var1));
      Throwable var4 = null;

      try {
         String var5;
         try {
            while((var5 = var3.readLine()) != null) {
               var2.append(var5).append("\n");
            }
         } catch (Throwable var13) {
            var4 = var13;
            throw var13;
         }
      } finally {
         if (var3 != null) {
            if (var4 != null) {
               try {
                  var3.close();
               } catch (Throwable var12) {
                  var4.addSuppressed(var12);
               }
            } else {
               var3.close();
            }
         }

      }

      return var2.toString();
   }

   public static String loadTextFileFromJar(String var0, String var1) throws IOException {
      StringBuffer var2 = new StringBuffer();
      BufferedReader var3 = new BufferedReader(new InputStreamReader(FileLoader.class.getResourceAsStream(var0 + "/" + var1)));
      Throwable var4 = null;

      try {
         String var5;
         try {
            while((var5 = var3.readLine()) != null) {
               var2.append(var5).append("\n");
            }
         } catch (Throwable var13) {
            var4 = var13;
            throw var13;
         }
      } finally {
         if (var3 != null) {
            if (var4 != null) {
               try {
                  var3.close();
               } catch (Throwable var12) {
                  var4.addSuppressed(var12);
               }
            } else {
               var3.close();
            }
         }

      }

      return var2.toString();
   }
}
