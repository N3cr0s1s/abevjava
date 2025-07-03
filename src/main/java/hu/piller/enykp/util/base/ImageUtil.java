package hu.piller.enykp.util.base;

import hu.piller.enykp.interfaces.IImageUtil;
import java.io.File;
import java.io.FileInputStream;
import java.net.URL;

public class ImageUtil implements IImageUtil {
   IImageUtil iu;

   public ImageUtil(URL var1) {
      if (var1.toString().endsWith("jar")) {
         this.iu = new ImageUtil.JarUtilGrab(var1.toString());
      } else {
         this.iu = new ImageUtil.FileUtil(var1.getPath());
      }

   }

   public byte[] getImageResource(String var1) {
      return this.iu.getImageResource(var1);
   }

   private class FileUtil implements IImageUtil {
      String path;

      public FileUtil(String var2) {
         this.path = var2;
      }

      public byte[] getImageResource(String var1) {
         FileInputStream var2 = null;

         try {
            File var3 = new File(this.getClass().getProtectionDomain().getClassLoader().getResource(var1).getFile());
            if (!var3.exists()) {
               return null;
            } else {
               var2 = new FileInputStream(var3);
               int var4 = (int)var3.length();
               byte[] var5 = new byte[var4];

               int var6;
               int var7;
               for(var6 = 0; var4 - var6 > 0; var6 += var7) {
                  var7 = var2.read(var5, var6, var4 - var6);
                  if (var7 == -1) {
                     break;
                  }
               }

               var2.close();
               return var4 == var6 ? var5 : null;
            }
         } catch (Exception var9) {
            try {
               var2.close();
            } catch (Exception var8) {
               Tools.eLog(var8, 0);
            }

            return null;
         }
      }
   }

   private class JarUtilGrab extends JarUtil implements IImageUtil {
      public JarUtilGrab(String var2) {
         super(var2);
      }
   }
}
