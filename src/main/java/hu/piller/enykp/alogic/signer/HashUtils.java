package hu.piller.enykp.alogic.signer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashUtils {
   public static Hash calcHash(File var0, HashType var1) throws NoSuchAlgorithmException, IOException {
      FileInputStream var2 = null;

      Hash var4;
      try {
         var2 = new FileInputStream(var0);
         String var3 = calcHash((InputStream)var2, var1);
         var4 = Hash.create(var3, var1);
      } finally {
         if (var2 != null) {
            var2.close();
         }

      }

      return var4;
   }

   private static String calcHash(InputStream var0, HashType var1) throws NoSuchAlgorithmException, IOException {
      MessageDigest var2 = MessageDigest.getInstance(var1.toString());
      byte[] var3 = new byte[1024];
      boolean var4 = true;

      int var6;
      while((var6 = var0.read(var3)) != -1) {
         var2.update(var3, 0, var6);
      }

      byte[] var5 = var2.digest();
      return convertByteArrayToHexString(var5);
   }

   private static String convertByteArrayToHexString(byte[] var0) {
      StringBuffer var1 = new StringBuffer();

      for(int var2 = 0; var2 < var0.length; ++var2) {
         var1.append(Integer.toString((var0[var2] & 255) + 256, 16).substring(1));
      }

      return var1.toString();
   }
}
