package hu.piller.enykp.util.base;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;

public class Md5Hash {
   public String createHash(byte[] var1) throws Exception {
      String var2 = null;
      DigestInputStream var3 = null;
      ByteArrayInputStream var4 = null;
      Object var5 = null;
      byte[] var8 = new byte[4096];
      var4 = new ByteArrayInputStream(var1);
      var3 = new DigestInputStream(var4, MessageDigest.getInstance("MD5"));

      while(var8.length == var3.read(var8, 0, var8.length)) {
      }

      MessageDigest var6 = var3.getMessageDigest();
      byte[] var7 = var6.digest();
      var2 = this.toHexString(var7);
      var3.close();
      var4.close();
      return this.submd5(var2);
   }

   public String toHexString(byte[] var1) {
      String var2 = "";

      int var4;
      try {
         for(DataInputStream var3 = new DataInputStream(new ByteArrayInputStream(var1)); var3.available() > 0; var2 = var2 + Integer.toHexString(var4)) {
            var4 = var3.readUnsignedByte();
            if (var4 < 16) {
               var2 = var2 + "0";
            }
         }
      } catch (Exception var5) {
         var2 = null;
      }

      return var2;
   }

   private String submd5(String var1) {
      String var2 = "";

      for(int var3 = 0; var3 < var1.length(); ++var3) {
         if (var3 % 4 == 0) {
            var2 = var2 + var1.charAt(var3);
         }
      }

      return var2;
   }
}
