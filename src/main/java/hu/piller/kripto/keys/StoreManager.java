package hu.piller.kripto.keys;

import hu.piller.tools.GeneralException;
import hu.piller.tools.Utils;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import javax.security.cert.CertificateException;
import javax.security.cert.X509Certificate;

import me.necrocore.abevjava.NecroFile;
import me.necrocore.abevjava.NecroFileOutputStream;
import org.bouncycastle.bcpg.ArmoredInputStream;
import org.bouncycastle.bcpg.ArmoredOutputStream;
import org.bouncycastle.openpgp.PGPException;
import org.bouncycastle.openpgp.PGPPublicKey;
import org.bouncycastle.openpgp.PGPPublicKeyRing;
import org.bouncycastle.openpgp.PGPPublicKeyRingCollection;
import org.bouncycastle.openpgp.PGPSecretKey;
import org.bouncycastle.openpgp.PGPSecretKeyRing;
import org.bouncycastle.openpgp.PGPSecretKeyRingCollection;
import org.bouncycastle.openpgp.PGPSignatureSubpacketVector;

public class StoreManager {
   public static final Hashtable PEM_HEADERS = new Hashtable();
   public static final Hashtable EXT = new Hashtable();
   public static final Hashtable TYPES = new Hashtable();
   public static final Hashtable DESC = new Hashtable();
   public static final int MAX_STORE_SIZE = 204800;
   public static final String[] EXT_PGP = new String[]{"asc", "pgp", "gpg"};
   public static final String[] EXT_JKS = new String[]{"jks", "ks"};
   public static final String[] EXT_P12 = new String[]{"pfx", "p12"};
   public static final String[] EXT_CER = new String[]{"cer", "der", "p7b"};
   public static final String PEM_HEADER_PGP_SECRETKEY = "-----BEGIN PGP PRIVATE KEY BLOCK-----";
   public static final String PEM_HEADER_PGP_PUBLICKEY = "-----BEGIN PGP PUBLIC KEY BLOCK-----";
   public static final String PEM_HEADER_CERTIFICATE = "-----BEGIN CERTIFICATE-----";
   public static final String PEM_HEADER_PKCS12 = "-----BEGIN PKCS12-----";
   public static final int TYPE_PGP_SECRET_KEYRING = 110;
   public static final int TYPE_PGP_PUBLIC_KEYRING = 120;
   public static final int TYPE_PGP_SECRET_KEYRINGCOLLECTION = 130;
   public static final int TYPE_PGP_PUBLIC_KEYRINGCOLLECTION = 140;
   public static final int TYPE_JKS = 200;
   public static final int TYPE_PKCS12 = 300;
   public static final int TYPE_X509CERT = 400;
   public static final int TYPE_UNKNOWN = -1;
   public static final String DESC_PGP_SECRET_KEYRING = "PGP magán kulcs";
   public static final String DESC_PGP_PUBLIC_KEYRING = "PGP nyílvános kulcs";
   public static final String DESC_PGP_SECRET_KEYRINGCOLLECTION = "PGP magán kulcsok";
   public static final String DESC_PGP_PUBLIC_KEYRINGCOLLECTION = "PGP nyílvános kulcsok";
   public static final String DESC_JKS = "Java kulcstár";
   public static final String DESC_PKCS12 = "Pkcs12 állomány";
   public static final String DESC_X509CERT = "X509 tanúsítvány";
   public static final String DESC_UNKNOWN = "Ismeretlen";
   public static final String STORETYPE_CER = "CER";
   public static final String STORETYPE_P12 = "P12";
   public static final String STORETYPE_PGP = "PGP";
   public static final String STORETYPE_JKS = "JKS";
   private static final String[] STORETYPES = new String[]{"JKS", "PGP"};
   private static final String STORETYPE_DEFAULT = "PGP";

   static {
      EXT.put("120", EXT_PGP);
      EXT.put("110", EXT_PGP);
      EXT.put("140", EXT_PGP);
      EXT.put("130", EXT_PGP);
      EXT.put("200", EXT_JKS);
      EXT.put("300", EXT_P12);
      EXT.put("400", EXT_CER);
      PEM_HEADERS.put("-----BEGIN PGP PRIVATE KEY BLOCK-----", "110");
      PEM_HEADERS.put("-----BEGIN PGP PUBLIC KEY BLOCK-----", "120");
      PEM_HEADERS.put("-----BEGIN CERTIFICATE-----", "400");
      TYPES.put("110", "PGP_SECRET_KEYRING");
      TYPES.put("120", "PGP_PUBLIC_KEYRING");
      TYPES.put("130", "PGP_SECRET_KEYRINGCOLLECTION");
      TYPES.put("140", "PGP_PUBLIC_KEYRINGCOLLECTION");
      TYPES.put("200", "JKS");
      TYPES.put("300", "PKCS12");
      TYPES.put("400", "X509CERT");
      TYPES.put("-1", "UNKNOWN");
      DESC.put("110", "PGP magán kulcs");
      DESC.put("120", "PGP nyílvános kulcs");
      DESC.put("130", "PGP magán kulcsok");
      DESC.put("140", "PGP nyílvános kulcsok");
      DESC.put("200", "Java kulcstár");
      DESC.put("300", "Pkcs12 állomány");
      DESC.put("400", "X509 tanúsítvány");
      DESC.put("-1", "Ismeretlen");
   }

   public static void exportKeyPairPGP(String secretOutFileName, String publicOutFileName, KeyPair kp, String identity, char[] passPhrase) throws GeneralException, IOException, NoSuchProviderException, PGPException {
      PublicKey publicKey = kp.getPublic();
      PrivateKey privateKey = kp.getPrivate();
      File secretOutFile = new NecroFile(secretOutFileName);
      if (!secretOutFile.createNewFile()) {
         throw new GeneralException("Sikertelen az állomány létrehozása: '" + secretOutFileName + "'");
      } else {
         OutputStream secretOut = new ArmoredOutputStream(new NecroFileOutputStream(secretOutFile));
         PGPSecretKey secretKey = new PGPSecretKey(16, 1, publicKey, privateKey, new Date(), identity, 3, passPhrase, (PGPSignatureSubpacketVector)null, (PGPSignatureSubpacketVector)null, new SecureRandom(), Utils.getBCP());
         secretKey.encode(secretOut);
         secretOut.close();
         if (publicOutFileName != null) {
            File publicOutFile = new NecroFile(publicOutFileName);
            if (!publicOutFile.createNewFile()) {
               throw new GeneralException("Sikertelen az állomány létrehozása: '" + secretOutFileName + "'");
            }

            OutputStream publicOut = new ArmoredOutputStream(new NecroFileOutputStream(publicOutFileName));
            PGPPublicKey key = secretKey.getPublicKey();
            key.encode(publicOut);
            publicOut.close();
         }

      }
   }

   public static int getStoreType(String fileName) throws NoSuchAlgorithmException, FileNotFoundException {
      return getStoreType((InputStream)(new FileInputStream(fileName)));
   }

   public static int getStoreType(InputStream in) throws NoSuchAlgorithmException {
      StoreWrapper storeWrapper = loadStore((InputStream)in, (char[])null);
      return storeWrapper != null ? storeWrapper.getType() : -1;
   }

   public static StoreWrapper loadStore(String fileName, char[] storePass) throws NoSuchAlgorithmException, FileNotFoundException {
      return loadStore((String)fileName, storePass, -1);
   }

   public static StoreWrapper loadStore(InputStream in, char[] storePass) throws NoSuchAlgorithmException {
      return loadStore((InputStream)in, storePass, -1);
   }

   public static StoreWrapper loadStore(String fileName, char[] storePass, int type) throws NoSuchAlgorithmException, FileNotFoundException {
      InputStream in = new FileInputStream(fileName);
      StoreWrapper sw = loadStore((InputStream)in, storePass, type);
      if (sw != null) {
         sw.setLocation(fileName);
      }

      return sw;
   }

   public static StoreWrapper loadStore(InputStream in, char[] storePass, int type) throws NoSuchAlgorithmException {
      int markLimit = 1024;
      BufferedInputStream bstore = new BufferedInputStream(in);

      try {
         bstore.mark(markLimit);
         byte[] begin = new byte[512];
         bstore.read(begin);
         int pemType = isPEM(new String(begin));
         StoreWrapper storeWrapper;
         if (pemType != -1) {
            bstore.reset();
            switch(pemType) {
            case 110:
               if ((storeWrapper = testStoreType((InputStream)(new ArmoredInputStream(bstore)), storePass, 110)) != null) {
                  return storeWrapper;
               }
               break;
            case 120:
               if ((storeWrapper = testStoreType((InputStream)(new ArmoredInputStream(bstore)), storePass, 120)) != null) {
                  return storeWrapper;
               }
               break;
            case 130:
               if ((storeWrapper = testStoreType((InputStream)(new ArmoredInputStream(bstore)), storePass, 130)) != null) {
                  return storeWrapper;
               }
               break;
            case 140:
               if ((storeWrapper = testStoreType((InputStream)(new ArmoredInputStream(bstore)), storePass, 140)) != null) {
                  return storeWrapper;
               }
               break;
            case 200:
               if ((storeWrapper = testStoreType((InputStream)bstore, storePass, 200)) != null) {
                  return storeWrapper;
               }
               break;
            case 300:
               if ((storeWrapper = testStoreType((InputStream)bstore, storePass, 300)) != null) {
                  return storeWrapper;
               }
               break;
            case 400:
               if ((storeWrapper = testStoreType((InputStream)bstore, storePass, 400)) != null) {
                  return storeWrapper;
               }
            }
         } else {
            if (type != -1) {
               return testStoreType((InputStream)bstore, storePass, type);
            }

            bstore.reset();
            if ((storeWrapper = testStoreType((InputStream)bstore, storePass, 300)) != null) {
               return storeWrapper;
            }

            bstore.reset();
            if ((storeWrapper = testStoreType((InputStream)bstore, storePass, 400)) != null) {
               return storeWrapper;
            }

            bstore.reset();
            if ((storeWrapper = testStoreType((InputStream)bstore, storePass, 200)) != null) {
               return storeWrapper;
            }

            bstore.reset();
            if ((storeWrapper = testStoreType((InputStream)bstore, storePass, 130)) != null) {
               return storeWrapper;
            }

            bstore.reset();
            if ((storeWrapper = testStoreType((InputStream)bstore, storePass, 140)) != null) {
               return storeWrapper;
            }

            bstore.reset();
            if ((storeWrapper = testStoreType((InputStream)bstore, storePass, 120)) != null) {
               return storeWrapper;
            }

            bstore.reset();
            if ((storeWrapper = testStoreType((InputStream)bstore, storePass, 110)) != null) {
               return storeWrapper;
            }
         }
      } catch (IOException var8) {
      }

      return null;
   }

   private static StoreWrapper testStoreType(String fileName, char[] storePass, int type) {
      try {
         InputStream in = new FileInputStream(fileName);
         StoreWrapper sw = testStoreType((InputStream)in, storePass, type);
         if (sw != null) {
            sw.setLocation(fileName);
         }

         return sw;
      } catch (FileNotFoundException var5) {
         var5.printStackTrace();
         return null;
      }
   }

   private static StoreWrapper testStoreType(InputStream in, char[] storePass, int type) {
      StoreWrapper storeWrapper = new StoreWrapper();
      X509Certificate store = null;

      try {
         switch(type) {
         case 110:
            PGPSecretKeyRing pgpStore = new PGPSecretKeyRing(in);
            (pgpStore).getSecretKeys();
            storeWrapper.setStore(pgpStore);
            storeWrapper.setType(110);
            return storeWrapper;
         case 120:
            PGPPublicKeyRing pgpPublicKeyRing = new PGPPublicKeyRing(in);
            (pgpPublicKeyRing).getPublicKeys();
            storeWrapper.setStore(pgpPublicKeyRing);
            storeWrapper.setType(120);
            return storeWrapper;
         case 130:
            PGPSecretKeyRingCollection pgpSecretKeyRingCollection = new PGPSecretKeyRingCollection(in);
            if ((pgpSecretKeyRingCollection).size() > 0) {
               storeWrapper.setStore(pgpSecretKeyRingCollection);
               storeWrapper.setType(130);
               return storeWrapper;
            }
            break;
         case 140:
            PGPPublicKeyRingCollection pgpPublicKeyRingCollection = new PGPPublicKeyRingCollection(in);
            if ((pgpPublicKeyRingCollection).size() > 0) {
               storeWrapper.setStore(pgpPublicKeyRingCollection);
               storeWrapper.setType(140);
               return storeWrapper;
            }
            break;
         case 200:
            KeyStore keyStore = KeyStore.getInstance("JKS");
            (keyStore).load(in, storePass);
            storeWrapper.setStore(keyStore);
            storeWrapper.setType(200);
            return storeWrapper;
         case 300:
            KeyStore keyStore1 = KeyStore.getInstance("PKCS12");
            (keyStore1).load(in, storePass);
            storeWrapper.setStore(keyStore1);
            storeWrapper.setType(300);
            return storeWrapper;
         case 400:
            store = X509Certificate.getInstance(in);
            storeWrapper.setStore(store);
            storeWrapper.setType(400);
            return storeWrapper;
         }
      } catch (Exception var6) {
         var6.printStackTrace();
      }

      return null;
   }

   public static X509Certificate loadCertificate(InputStream in) throws CertificateException {
      X509Certificate cert = X509Certificate.getInstance(in);
      return cert;
   }

   public static X509Certificate loadCertificate(String fileName) throws IOException, CertificateException {
      InputStream inStream = new FileInputStream(fileName);
      X509Certificate cert = X509Certificate.getInstance(inStream);
      inStream.close();
      return cert;
   }

   /** @deprecated */
   public static Key loadKey(String storeType, String storeFileName, String alias, String storePass, String keyPass) throws KeyStoreException, IOException, NoSuchAlgorithmException, java.security.cert.CertificateException, UnrecoverableKeyException {
      StoreWrapper sw = loadStore(storeFileName, storePass == null ? null : storePass.toCharArray());
      KeyWrapperFilter filter = new KeyWrapperFilter();
      filter.setAlias(alias);
      filter.setType(1);
      Vector keys = sw.listKeys(filter);
      return keys.size() > 0 ? ((KeyWrapper)keys.firstElement()).getKey(keyPass.toCharArray()) : null;
   }

   private static int isPEM(String str) {
      Enumeration en = PEM_HEADERS.keys();

      while(en.hasMoreElements()) {
         String pemHeader = (String)en.nextElement();
         if (str.lastIndexOf(pemHeader) != -1) {
            return Integer.parseInt((String)PEM_HEADERS.get(pemHeader));
         }
      }

      return -1;
   }

   public static String getTypeDesc(String type) {
      return (String)(DESC.get(type) == null ? "Ismeretlen" : DESC.get(type));
   }
}
