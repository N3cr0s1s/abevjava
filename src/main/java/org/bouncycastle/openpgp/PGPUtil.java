package org.bouncycastle.openpgp;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.Date;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.bcpg.ArmoredInputStream;
import org.bouncycastle.bcpg.HashAlgorithmTags;
import org.bouncycastle.bcpg.MPInteger;
import org.bouncycastle.bcpg.S2K;
import org.bouncycastle.crypto.SecretKey;
import org.bouncycastle.crypto.spec.SecretKeySpec;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;

public class PGPUtil implements HashAlgorithmTags {
   private static String defProvider = "BC";
   private static final int READ_AHEAD = 60;

   public static String getDefaultProvider() {
      return defProvider;
   }

   public static void setDefaultProvider(String provider) {
      defProvider = provider;
   }

   static MPInteger[] dsaSigToMpi(byte[] encoding) throws PGPException {
      ASN1InputStream aIn = new ASN1InputStream(new ByteArrayInputStream(encoding));

      DERInteger i1;
      DERInteger i2;
      try {
         ASN1Sequence s = (ASN1Sequence)aIn.readObject();
         i1 = (DERInteger)s.getObjectAt(0);
         i2 = (DERInteger)s.getObjectAt(1);
      } catch (IOException var5) {
         throw new PGPException("exception encoding signature", var5);
      }

      MPInteger[] values = new MPInteger[]{new MPInteger(i1.getValue()), new MPInteger(i2.getValue())};
      return values;
   }

   static String getDigestName(int hashAlgorithm) throws PGPException {
      switch(hashAlgorithm) {
      case 1:
         return "MD5";
      case 2:
         return "SHA1";
      case 3:
         return "RIPEMD160";
      case 4:
      case 6:
      case 7:
      default:
         throw new PGPException("unknown hash algorithm tag in getDigestName: " + hashAlgorithm);
      case 5:
         return "MD2";
      case 8:
         return "SHA256";
      case 9:
         return "SHA384";
      case 10:
         return "SHA512";
      }
   }

   static String getSignatureName(int keyAlgorithm, int hashAlgorithm) throws PGPException {
      String encAlg;
      switch(keyAlgorithm) {
      case 1:
      case 3:
         encAlg = "RSA";
         break;
      case 16:
      case 20:
         encAlg = "ElGamal";
         break;
      case 17:
         encAlg = "DSA";
         break;
      default:
         throw new PGPException("unknown algorithm tag in signature:" + keyAlgorithm);
      }

      return getDigestName(hashAlgorithm) + "with" + encAlg;
   }

   static String getSymmetricCipherName(int algorithm) throws PGPException {
      switch(algorithm) {
      case 0:
         return null;
      case 1:
         return "IDEA";
      case 2:
         return "DESEDE";
      case 3:
         return "CAST5";
      case 4:
         return "Blowfish";
      case 5:
         return "SAFER";
      case 6:
         return "DES";
      case 7:
         return "AES";
      case 8:
         return "AES";
      case 9:
         return "AES";
      case 10:
         return "Twofish";
      default:
         throw new PGPException("unknown symmetric algorithm: " + algorithm);
      }
   }

   public static SecretKey makeRandomKey(int algorithm, SecureRandom random) throws PGPException {
      String algName = null;
      short keySize;
      switch(algorithm) {
      case 1:
         keySize = 128;
         algName = "IDEA";
         break;
      case 2:
         keySize = 192;
         algName = "DES_EDE";
         break;
      case 3:
         keySize = 128;
         algName = "CAST5";
         break;
      case 4:
         keySize = 128;
         algName = "Blowfish";
         break;
      case 5:
         keySize = 128;
         algName = "SAFER";
         break;
      case 6:
         keySize = 64;
         algName = "DES";
         break;
      case 7:
         keySize = 128;
         algName = "AES";
         break;
      case 8:
         keySize = 192;
         algName = "AES";
         break;
      case 9:
         keySize = 256;
         algName = "AES";
         break;
      case 10:
         keySize = 256;
         algName = "Twofish";
         break;
      default:
         throw new PGPException("unknown symmetric algorithm: " + algorithm);
      }

      byte[] keyBytes = new byte[(keySize + 7) / 8];
      random.nextBytes(keyBytes);
      return new SecretKeySpec(keyBytes, algName);
   }

   public static SecretKey makeKeyFromPassPhrase(int algorithm, char[] passPhrase, BouncyCastleProvider provider) throws NoSuchProviderException, PGPException {
      return makeKeyFromPassPhrase(algorithm, (S2K)null, passPhrase, provider);
   }

   public static SecretKey makeKeyFromPassPhrase(int algorithm, S2K s2k, char[] passPhrase, BouncyCastleProvider provider) throws PGPException, NoSuchProviderException {
      String algName = null;
      short keySize;
      switch(algorithm) {
      case 1:
         keySize = 128;
         algName = "IDEA";
         break;
      case 2:
         keySize = 192;
         algName = "DES_EDE";
         break;
      case 3:
         keySize = 128;
         algName = "CAST5";
         break;
      case 4:
         keySize = 128;
         algName = "Blowfish";
         break;
      case 5:
         keySize = 128;
         algName = "SAFER";
         break;
      case 6:
         keySize = 64;
         algName = "DES";
         break;
      case 7:
         keySize = 128;
         algName = "AES";
         break;
      case 8:
         keySize = 192;
         algName = "AES";
         break;
      case 9:
         keySize = 256;
         algName = "AES";
         break;
      case 10:
         keySize = 256;
         algName = "Twofish";
         break;
      default:
         throw new PGPException("unknown symmetric algorithm: " + algorithm);
      }

      byte[] pBytes = new byte[passPhrase.length];

      for(int i = 0; i != passPhrase.length; ++i) {
         pBytes[i] = (byte)passPhrase[i];
      }

      byte[] keyBytes = new byte[(keySize + 7) / 8];
      int generatedBytes = 0;

      int i;
      for(int loopCount = 0; generatedBytes < keyBytes.length; ++loopCount) {
         MessageDigest digest;
         if (s2k != null) {
            String digestName = getS2kDigestName(s2k);

            try {
               digest = getDigestInstance(digestName, provider);
            } catch (NoSuchAlgorithmException var15) {
               throw new PGPException("can't find S2K digest", var15);
            }

            for(int ix = 0; ix != loopCount; ++ix) {
               digest.update((byte)0);
            }

            byte[] iv = s2k.getIV();
            switch(s2k.getType()) {
            case 0:
               digest.update(pBytes);
               break;
            case 1:
               digest.update(iv);
               digest.update(pBytes);
               break;
            case 2:
            default:
               throw new PGPException("unknown S2K type: " + s2k.getType());
            case 3:
               long count = s2k.getIterationCount();
               digest.update(iv);
               digest.update(pBytes);
               count -= (long)(iv.length + pBytes.length);

               while(count > 0L) {
                  if (count < (long)iv.length) {
                     digest.update(iv, 0, (int)count);
                     break;
                  }

                  digest.update(iv);
                  count -= (long)iv.length;
                  if (count < (long)pBytes.length) {
                     digest.update(pBytes, 0, (int)count);
                     count = 0L;
                  } else {
                     digest.update(pBytes);
                     count -= (long)pBytes.length;
                  }
               }
            }
         } else {
            try {
               digest = getDigestInstance("MD5", provider);
            } catch (NoSuchAlgorithmException var16) {
               throw new PGPException("can't find MD5 digest", var16);
            }

            for(i = 0; i != loopCount; ++i) {
               digest.update((byte)0);
            }

            digest.update(pBytes);
         }

         byte[] dig = digest.digest();
         if (dig.length > keyBytes.length - generatedBytes) {
            System.arraycopy(dig, 0, keyBytes, generatedBytes, keyBytes.length - generatedBytes);
         } else {
            System.arraycopy(dig, 0, keyBytes, generatedBytes, dig.length);
         }

         generatedBytes += dig.length;
      }

      for(i = 0; i != pBytes.length; ++i) {
         pBytes[i] = 0;
      }

      return new SecretKeySpec(keyBytes, algName);
   }

   static MessageDigest getDigestInstance(String digestName, BouncyCastleProvider provider) throws NoSuchAlgorithmException {
      try {
         return MessageDigest.getInstance(digestName, provider);
      } catch (NoSuchAlgorithmException var3) {
         return MessageDigest.getInstance(digestName);
      }
   }

   private static String getS2kDigestName(S2K s2k) throws PGPException {
      switch(s2k.getHashAlgorithm()) {
      case 1:
         return "MD5";
      case 2:
         return "SHA1";
      default:
         throw new PGPException("unknown hash algorithm: " + s2k.getHashAlgorithm());
      }
   }

   public static void writeFileToLiteralData(OutputStream out, char fileType, File file) throws IOException {
      PGPLiteralDataGenerator lData = new PGPLiteralDataGenerator();
      OutputStream pOut = lData.open(out, fileType, file.getName(), file.length(), new Date(file.lastModified()));
      FileInputStream in = new FileInputStream(file);
      byte[] buf = new byte[4096];

      int len;
      while((len = in.read(buf)) > 0) {
         pOut.write(buf, 0, len);
      }

      lData.close();
      in.close();
   }

   public static void writeFileToLiteralData(OutputStream out, char fileType, File file, byte[] buffer) throws IOException {
      PGPLiteralDataGenerator lData = new PGPLiteralDataGenerator();
      OutputStream pOut = lData.open(out, fileType, file.getName(), new Date(file.lastModified()), buffer);
      FileInputStream in = new FileInputStream(file);
      byte[] buf = new byte[buffer.length];

      int len;
      while((len = in.read(buf)) > 0) {
         pOut.write(buf, 0, len);
      }

      lData.close();
      in.close();
   }

   private static boolean isPossiblyBase64(int ch) {
      return ch >= 65 && ch <= 90 || ch >= 97 && ch <= 122 || ch >= 48 && ch <= 57 || ch == 43 || ch == 47 || ch == 13 || ch == 10;
   }

   public static InputStream getDecoderStream(InputStream in) throws IOException {
      if (!((InputStream)in).markSupported()) {
         in = new BufferedInputStream((InputStream)in);
      }

      ((InputStream)in).mark(60);
      int ch = ((InputStream)in).read();
      if ((ch & 128) != 0) {
         ((InputStream)in).reset();
         return (InputStream)in;
      } else if (!isPossiblyBase64(ch)) {
         ((InputStream)in).reset();
         return new ArmoredInputStream((InputStream)in);
      } else {
         byte[] buf = new byte[60];
         int count = 1;
         int index = 1;

         for(buf[0] = (byte)ch; count != 60 && (ch = ((InputStream)in).read()) >= 0; ++count) {
            if (!isPossiblyBase64(ch)) {
               ((InputStream)in).reset();
               return new ArmoredInputStream((InputStream)in);
            }

            if (ch != 10 && ch != 13) {
               buf[index++] = (byte)ch;
            }
         }

         ((InputStream)in).reset();
         if (count < 4) {
            return new ArmoredInputStream((InputStream)in);
         } else {
            byte[] firstBlock = new byte[8];
            System.arraycopy(buf, 0, firstBlock, 0, firstBlock.length);
            byte[] decoded = Base64.decode(firstBlock);
            return (decoded[0] & 128) != 0 ? new ArmoredInputStream((InputStream)in, false) : new ArmoredInputStream((InputStream)in);
         }
      }
   }
}
