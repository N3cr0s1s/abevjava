package hu.piller.kripto;

import hu.piller.tools.Base64;
import hu.piller.tools.Utils;
import hu.piller.tools.bzip2.InflatorThread;
import hu.piller.xml.FinishException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.security.DigestOutputStream;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Provider;
import org.bouncycastle.crypto.BadPaddingException;
import org.bouncycastle.crypto.Cipher;
import org.bouncycastle.crypto.CipherOutputStream;
import org.bouncycastle.crypto.IllegalBlockSizeException;
import org.bouncycastle.crypto.NoSuchPaddingException;
import org.bouncycastle.crypto.SecretKey;

public class AESCipher {
   public static byte[] encryptData(SecretKey secretKey, byte[] plainData) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, BadPaddingException, IllegalBlockSizeException {
      Cipher cipher = Cipher.getInstance("AES", (Provider)Utils.getBCP());
      cipher.init(1, secretKey);
      byte[] cipherData = cipher.doFinal(plainData);
      return cipherData;
   }

   public static byte[] decryptData(SecretKey secretKey, byte[] cipherData) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, BadPaddingException, IllegalBlockSizeException, NoSuchProviderException {
      Cipher cipher = Cipher.getInstance("AES", (Provider)Utils.getBCP());
      cipher.init(2, secretKey);
      byte[] plainData = cipher.doFinal(cipherData);
      return plainData;
   }

   public static byte[] encryptStream(SecretKey secretKey, InputStream in, OutputStream out, boolean useB64Encoding) throws NoSuchProviderException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IOException, BadPaddingException, IllegalBlockSizeException {
      Cipher cipher = Cipher.getInstance("AES", (Provider)Utils.getBCP());
      cipher.init(1, secretKey);
      CipherOutputStream cos = null;
      Base64.OutputStream b64out = null;
      DigestOutputStream dout = new DigestOutputStream(out, MessageDigest.getInstance("SHA"));
      if (useB64Encoding) {
         b64out = new Base64.OutputStream(dout, 1);
         cos = new CipherOutputStream(b64out, cipher);
      } else {
         cos = new CipherOutputStream(dout, cipher);
      }

      byte[] buffer = new byte[4096];

      int count;
      while((count = in.read(buffer)) != -1) {
         cos.write(buffer, 0, count);
      }

      if (useB64Encoding) {
         b64out.write(cipher.doFinal());
         b64out.flush();
      } else {
         dout.write(cipher.doFinal());
         dout.flush();
      }

      in.close();
      return dout.getMessageDigest().digest();
   }

   public static void decryptStream(SecretKey secretKey, InputStream in, OutputStream out, boolean useB64Decoding) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, FinishException {
      Cipher cipher = Cipher.getInstance("AES", (Provider)Utils.getBCP());
      cipher.init(2, secretKey);
      OutputStream dest = null;
      CipherOutputStream cos = new CipherOutputStream(out, cipher);
      if (useB64Decoding) {
         dest = new Base64.OutputStream(cos, 0);
      } else {
         dest = cos;
      }

      byte[] buffer = new byte[4096];

      int count;
      while((count = in.read(buffer)) != -1) {
         ((OutputStream)dest).write(buffer, 0, count);
      }

      out.write(cipher.doFinal());
      out.flush();
   }

   public static void decryptInflateStream(SecretKey secretKey, InputStream in, OutputStream out, boolean useB64Decoding) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IOException, BadPaddingException, IllegalBlockSizeException, InterruptedException {
      long t1 = System.currentTimeMillis();
      PipedOutputStream pout = new PipedOutputStream();
      PipedInputStream pin = new PipedInputStream(pout);
      Cipher cipher = Cipher.getInstance("AES", (Provider)Utils.getBCP());
      cipher.init(2, secretKey);
      CipherOutputStream cos = new CipherOutputStream(pout, cipher);
      Object dest;
      if (useB64Decoding) {
         dest = new Base64.OutputStream(cos, 0);
      } else {
         dest = cos;
      }

      Thread inflator = new Thread(new InflatorThread(pin, out));
      inflator.start();
      byte[] buffer = new byte[4096];

      int count;
      while((count = in.read(buffer)) != -1) {
         ((OutputStream)dest).write(buffer, 0, count);
      }

      ((OutputStream)dest).flush();
      pout.write(cipher.doFinal());
      pout.flush();
      pout.close();
      inflator.join(5000L);
      out.flush();
      long t2 = System.currentTimeMillis();
   }
}
