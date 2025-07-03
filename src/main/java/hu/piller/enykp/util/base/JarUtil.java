package hu.piller.enykp.util.base;

import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class JarUtil {
   public static final String ERR_URI_NAME = "Hibás hivatkozás";
   public static final String ERR_JAR_EXTRACT = "Hiba a JAR kicsomagolása során";
   public static final String ERR_JAR_EXTRACT_FILE = "Hiba a file kicsomagolása során";
   public static final String ERR_JAR_LIST = "Hiba a JAR file listázása során";
   public static final String ERR_JAR_FILE_OPEN = "Nincs meg a JAR állomány";
   public static final String ERR_JAR_FILE_IO = "JAR file olvasási hiba";
   public static final String ERR_JAR_FILE_NP = "Belső hiba a JAR file olvasása közben";
   private static boolean debugOn = true;
   private JarFile jf = null;
   private String jarfile = null;
   private String path = null;

   public JarUtil(String var1) {
      this.path = var1;
   }

   private void init() throws Exception {
      try {
         URL var1 = new URL(this.path);
         boolean var2 = var1.toString().indexOf("file") == 0;
         this.jarfile = var1.getPath();

         try {
            if (var2) {
               URL var3 = new URL("jar:" + var1 + "!/");
               JarURLConnection var4 = (JarURLConnection)var3.openConnection();
               var4.setUseCaches(false);
               this.jf = var4.getJarFile();
            } else {
               try {
                  this.jf = new JarFile(this.jarfile);
               } catch (IOException var5) {
                  throw new Exception(this.errAdmin("15001", "Belső hiba a JAR file olvasása közben", var5, this.jarfile));
               }
            }

         } catch (MalformedURLException var6) {
            throw new Exception(this.errAdmin("15001", "Belső hiba a JAR file olvasása közben", var6, this.jarfile));
         } catch (IOException var7) {
            throw new Exception(this.errAdmin("15002", "JAR file olvasási hiba", var7, this.jarfile));
         }
      } catch (Exception var8) {
         throw new Exception(this.errAdmin("15000", "JAR file olvasási hiba", var8, this.jarfile));
      }
   }

   public byte[] getImageResource(String var1) {
      try {
         if (this.jf == null) {
            this.init();
         }

         if (this.jf != null) {
            byte[] var2 = this.getEntry(var1);
            this.close();
            return var2;
         } else {
            return null;
         }
      } catch (Exception var3) {
         this.close();
         this.errAdmin("15010", "Hiba a JAR kicsomagolása során", var3, this.jarfile);
         return null;
      }
   }

   public void close() {
      try {
         if (this.jf != null) {
            this.jf.close();
            this.jf = null;
         }
      } catch (IOException var2) {
         System.out.println("Sikertelen file zárási kisérlet:" + this.path);
      }

   }

   public byte[] getEntry(String var1) {
      byte[] var2 = null;

      try {
         if (this.jf == null) {
            this.init();
         }

         JarEntry var3 = this.jf.getJarEntry(var1);
         if (this.jf != null && var3 != null) {
            var2 = this.readInputStream(this.jf.getInputStream(var3), (int)var3.getSize());
         }
      } catch (Exception var4) {
         this.errAdmin("15004", "JAR file olvasási hiba", var4, this.jarfile);
      }

      return var2;
   }

   private byte[] readInputStream(InputStream var1, int var2) {
      try {
         byte[] var3 = new byte[var2];

         int var4;
         int var5;
         for(var4 = 0; var2 - var4 > 0; var4 += var5) {
            var5 = var1.read(var3, var4, var2 - var4);
            if (var5 == -1) {
               break;
            }
         }

         var1.close();
         return var2 == var4 ? var3 : null;
      } catch (Exception var7) {
         this.errAdmin("15005", "JAR file olvasási hiba", var7, this.jarfile);

         try {
            var1.close();
         } catch (IOException var6) {
            Tools.eLog(var6, 0);
         }

         return null;
      }
   }

   private String errAdmin(String var1, String var2, Exception var3, Object var4) {
      if (debugOn && var3 != null) {
         var3.printStackTrace();
      }

      String var5 = var2 + ":" + (var4 == null ? "" : var4.toString());
      ErrorList.getInstance().writeError(var1, var5, (Exception)null, var4);
      return var1 + " " + var5;
   }
}
