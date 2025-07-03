package hu.piller.enykp.alogic.filepanels.mohu;

import hu.piller.enykp.util.base.Tools;
import java.nio.charset.Charset;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

public class Rejtely {
   static Cipher ecipher;
   static Cipher dcipher;
   byte[] salt = new byte[]{-87, -101, -56, 50, 86, 53, -29, 3};
   int iterationCount = 19;
   private static Rejtely ourInstance = new Rejtely("Adja meg a titkos file-helyet!");

   public static Rejtely getInstance() {
      return ourInstance;
   }

   private Rejtely(String var1) {
      try {
         PBEKeySpec var2 = new PBEKeySpec(var1.toCharArray(), this.salt, this.iterationCount);
         SecretKey var3 = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(var2);
         ecipher = Cipher.getInstance("PBEWithMD5AndDES");
         dcipher = Cipher.getInstance("PBEWithMD5AndDES");
         PBEParameterSpec var4 = new PBEParameterSpec(this.salt, this.iterationCount);
         ecipher.init(1, var3, var4);
         dcipher.init(2, var3, var4);
      } catch (InvalidAlgorithmParameterException var5) {
         var5.printStackTrace();
      } catch (InvalidKeySpecException var6) {
         var6.printStackTrace();
      } catch (NoSuchPaddingException var7) {
         var7.printStackTrace();
      } catch (NoSuchAlgorithmException var8) {
         var8.printStackTrace();
      } catch (InvalidKeyException var9) {
         var9.printStackTrace();
      }

   }

   public String encode(String var1) {
      try {
         byte[] var2 = var1.getBytes("UTF-8");
         byte[] var3 = ecipher.doFinal(var2);
         return new String(Base64.getEncoder().encode(var3), Charset.forName("ISO-8859-1"));
      } catch (Exception var4) {
         Tools.eLog(var4, 0);
         return null;
      }
   }

   public static String decode(String var0) {
      try {
         byte[] var1 = Base64.getDecoder().decode(var0);
         byte[] var2 = dcipher.doFinal(var1);
         return new String(var2, "UTF8");
      } catch (Exception var3) {
         Tools.eLog(var3, 0);
         return null;
      }
   }
}
