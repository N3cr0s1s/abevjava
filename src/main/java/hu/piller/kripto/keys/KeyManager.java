package hu.piller.kripto.keys;

import hu.piller.tools.Utils;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.util.Hashtable;
import org.bouncycastle.crypto.KeyGenerator;
import org.bouncycastle.crypto.SecretKey;

public class KeyManager {
   public static final int TYPE_PUB = 0;
   public static final int TYPE_SEC = 1;
   public static final int TYPE_KEYPAIR = 2;
   public static final int TYPE_SYMMETRIC = 3;
   public static final int TYPE_UNKNOWN = 4;
   public static final Hashtable TYPE_NAMES = new Hashtable();
   public static final int AES_128 = 128;
   public static final int AES_256 = 256;
   public static final int RSA_1024 = 1024;
   public static final int RSA_2048 = 2048;

   static {
      TYPE_NAMES.put("0", "publikus");
      TYPE_NAMES.put("1", "privát");
      TYPE_NAMES.put("2", "kulcspár");
      TYPE_NAMES.put("3", "szimmetrikus");
      TYPE_NAMES.put("4", "ismeretlen");
   }

   public static KeyPair generateRSAKeyPair(int keyLength) throws InvalidKeyException, NoSuchAlgorithmException {
      if (keyLength != 1024 && keyLength != 2048) {
         throw new InvalidKeyException();
      } else {
         KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA", Utils.getBCP());
         kpg.initialize(keyLength);
         return kpg.generateKeyPair();
      }
   }

   public static SecretKey generateAESKey(int keyLength) throws NoSuchAlgorithmException, InvalidKeyException {
      if (keyLength != 128 && keyLength != 256) {
         throw new InvalidKeyException();
      } else {
         KeyGenerator keyGen = KeyGenerator.getInstance("AES", (Provider)Utils.getBCP());
         keyGen.init(keyLength);
         SecretKey sk = null;

         do {
            sk = keyGen.generateKey();
         } while(sk.getEncoded()[0] == 0);

         return sk;
      }
   }
}
