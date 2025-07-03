package hu.piller.enykp.util.base;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;

public class Sha1Hash {
   public String createHash(byte[] var1) throws Exception {
      String var2 = null;
      DigestInputStream var3 = null;
      ByteArrayInputStream var4 = null;
      Object var5 = null;
      byte[] var8 = new byte[4096];
      var4 = new ByteArrayInputStream(var1);
      var3 = new DigestInputStream(var4, MessageDigest.getInstance("SHA"));

      while(var8.length == var3.read(var8, 0, var8.length)) {
      }

      MessageDigest var6 = var3.getMessageDigest();
      byte[] var7 = var6.digest();
      var2 = this.toHexString(var7);
      var3.close();
      var4.close();
      return var2;
   }

   public byte[] createByteHash(byte[] var1) throws Exception {
      DigestInputStream var2 = null;
      ByteArrayInputStream var3 = null;
      Object var4 = null;
      byte[] var7 = new byte[4096];
      var3 = new ByteArrayInputStream(var1);
      var2 = new DigestInputStream(var3, MessageDigest.getInstance("SHA"));

      while(var7.length == var2.read(var7, 0, var7.length)) {
      }

      MessageDigest var5 = var2.getMessageDigest();
      byte[] var6 = var5.digest();
      var2.close();
      var3.close();
      return var6;
   }

   public String createHash(String var1) throws Exception {
      FileInputStream var2 = null;
      DigestInputStream var3 = null;
      Object var4 = null;
      String var5 = null;
      var2 = new FileInputStream(var1);
      byte[] var8 = new byte[4096];
      var3 = new DigestInputStream(var2, MessageDigest.getInstance("SHA"));

      while(var8.length == var3.read(var8, 0, var8.length)) {
      }

      MessageDigest var6 = var3.getMessageDigest();
      byte[] var7 = var6.digest();
      var5 = this.toHexString(var7);
      var3.close();
      var2.close();
      return var5;
   }

   public byte[] createByteHash(String var1) throws Exception {
      FileInputStream var2 = null;
      DigestInputStream var3 = null;
      Object var4 = null;
      var2 = new FileInputStream(var1);
      byte[] var7 = new byte[4096];
      var3 = new DigestInputStream(var2, MessageDigest.getInstance("SHA"));

      while(var7.length == var3.read(var7, 0, var7.length)) {
      }

      MessageDigest var5 = var3.getMessageDigest();
      byte[] var6 = var5.digest();
      var3.close();
      var2.close();
      return var6;
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
}
