package hu.piller.kripto;

import hu.piller.tools.Utils;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import org.bouncycastle.crypto.BadPaddingException;
import org.bouncycastle.crypto.Cipher;
import org.bouncycastle.crypto.IllegalBlockSizeException;
import org.bouncycastle.crypto.NoSuchPaddingException;

public class RSACipher {
   public static byte[] encryptData(Key key, byte[] plainData) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
      Cipher cipher = Cipher.getInstance("RSA", (Provider)Utils.getBCP());
      cipher.init(1, key);
      return cipher.doFinal(plainData);
   }

   public static byte[] decryptData(Key key, byte[] cipherData) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
      Cipher cipher = Cipher.getInstance("RSA", (Provider)Utils.getBCP());
      cipher.init(2, key);
      return cipher.doFinal(cipherData, 0, cipherData.length);
   }
}
