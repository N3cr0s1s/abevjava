package hu.piller.enykp.alogic.templateutils.blacklist.provider;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class BlacklistProviderLocal implements BlacklistProvider {
   private File path;

   public BlacklistProviderLocal(File var1) {
      this.path = var1;
   }

   public String get() throws BlacklistProviderException {
      if (this.path != null && this.path.exists()) {
         StringBuffer var1 = new StringBuffer();

         try {
            BufferedReader var2 = new BufferedReader(new InputStreamReader(new FileInputStream(this.path), "utf-8"));
            Throwable var3 = null;

            try {
               String var4;
               try {
                  while((var4 = var2.readLine()) != null) {
                     var1.append(var4).append("\n");
                  }
               } catch (Throwable var13) {
                  var3 = var13;
                  throw var13;
               }
            } finally {
               if (var2 != null) {
                  if (var3 != null) {
                     try {
                        var2.close();
                     } catch (Throwable var12) {
                        var3.addSuppressed(var12);
                     }
                  } else {
                     var2.close();
                  }
               }

            }
         } catch (IOException var15) {
            throw new BlacklistProviderException(var15.getMessage());
         }

         return var1.toString();
      } else {
         throw new BlacklistProviderException(String.format("Érvénytelen fájl útvonal %s", this.path == null ? "null" : this.path.toString()));
      }
   }
}
