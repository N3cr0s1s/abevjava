package org.bouncycastle.openpgp;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.RSAPrivateCrtKeySpec;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.bouncycastle.bcpg.BCPGInputStream;
import org.bouncycastle.bcpg.BCPGObject;
import org.bouncycastle.bcpg.BCPGOutputStream;
import org.bouncycastle.bcpg.ContainedPacket;
import org.bouncycastle.bcpg.PublicKeyPacket;
import org.bouncycastle.bcpg.RSAPublicBCPGKey;
import org.bouncycastle.bcpg.RSASecretBCPGKey;
import org.bouncycastle.bcpg.S2K;
import org.bouncycastle.bcpg.SecretKeyPacket;
import org.bouncycastle.bcpg.SecretSubkeyPacket;
import org.bouncycastle.bcpg.TrustPacket;
import org.bouncycastle.bcpg.UserAttributePacket;
import org.bouncycastle.bcpg.UserIDPacket;
import org.bouncycastle.crypto.Cipher;
import org.bouncycastle.crypto.SecretKey;
import org.bouncycastle.crypto.spec.IvParameterSpec;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class PGPSecretKey {
   SecretKeyPacket secret;
   TrustPacket trust;
   List keySigs;
   List ids;
   List idTrusts;
   List idSigs;
   PGPPublicKey pub;
   List subSigs;

   private PGPSecretKey(SecretKeyPacket secret, TrustPacket trust, List keySigs, List ids, List idTrusts, List idSigs, PGPPublicKey pub) {
      this.subSigs = null;
      this.secret = secret;
      this.trust = trust;
      this.keySigs = keySigs;
      this.ids = ids;
      this.idTrusts = idTrusts;
      this.idSigs = idSigs;
      this.pub = pub;
   }

   private PGPSecretKey(SecretKeyPacket secret, TrustPacket trust, List subSigs, PGPPublicKey pub) {
      this.subSigs = null;
      this.secret = secret;
      this.trust = trust;
      this.subSigs = subSigs;
      this.pub = pub;
   }

   PGPSecretKey(SecretKeyPacket secret, TrustPacket trust, MessageDigest sha, List keySigs, List ids, List idTrusts, List idSigs) throws IOException {
      this.subSigs = null;
      this.secret = secret;
      this.trust = trust;
      this.keySigs = keySigs;
      this.ids = ids;
      this.idTrusts = idTrusts;
      this.idSigs = idSigs;
      this.pub = new PGPPublicKey(secret.getPublicKeyPacket(), trust, keySigs, ids, idTrusts, idSigs);
   }

   PGPSecretKey(SecretKeyPacket secret, TrustPacket trust, MessageDigest sha, List subSigs) throws IOException {
      this.subSigs = null;
      this.secret = secret;
      this.trust = trust;
      this.subSigs = subSigs;
      this.pub = new PGPPublicKey(secret.getPublicKeyPacket(), trust, subSigs);
   }

   PGPSecretKey(PGPKeyPair keyPair, TrustPacket trust, List subSigs, int encAlgorithm, char[] passPhrase, SecureRandom rand, BouncyCastleProvider provider) throws PGPException {
      this(keyPair, encAlgorithm, passPhrase, rand, provider);
      this.secret = new SecretSubkeyPacket(this.secret.getPublicKeyPacket(), this.secret.getEncAlgorithm(), this.secret.getS2K(), this.secret.getIV(), this.secret.getSecretKeyData());
      this.trust = trust;
      this.subSigs = subSigs;
      this.pub = new PGPPublicKey(keyPair.getPublicKey(), trust, subSigs);
   }

   PGPSecretKey(PGPKeyPair keyPair, int encAlgorithm, char[] passPhrase, SecureRandom rand, BouncyCastleProvider provider) throws PGPException {
      this.subSigs = null;
      PublicKeyPacket pubPk = keyPair.getPublicKey().publicPk;
      switch(keyPair.getPublicKey().getAlgorithm()) {
      case 1:
      case 2:
      case 3:
         RSAPrivateCrtKey rsK = (RSAPrivateCrtKey)keyPair.getPrivateKey().getKey();
         BCPGObject secKey = new RSASecretBCPGKey(rsK.getPrivateExponent(), rsK.getPrimeP(), rsK.getPrimeQ());
         String cName = PGPUtil.getSymmetricCipherName(encAlgorithm);
         Cipher c = null;
         if (cName != null) {
            try {
               c = Cipher.getInstance(cName + "/CFB/NoPadding", (Provider)provider);
            } catch (Exception var18) {
               var18.printStackTrace();
               throw new PGPException("Exception creating cipher", var18);
            }
         }

         try {
            ByteArrayOutputStream bOut = new ByteArrayOutputStream();
            BCPGOutputStream pOut = new BCPGOutputStream(bOut);
            pOut.writeObject(secKey);
            byte[] keyData = bOut.toByteArray();
            int checkSum = 0;
            int i = 0;

            while(true) {
               if (i == keyData.length) {
                  pOut.write(checkSum >> 8);
                  pOut.write(checkSum);
                  if (c != null) {
                     byte[] iv = new byte[8];
                     rand.nextBytes(iv);
                     S2K s2k = new S2K(2, iv, 96);
                     SecretKey key = PGPUtil.makeKeyFromPassPhrase(encAlgorithm, s2k, passPhrase, provider);
                     c.init(1, key, (SecureRandom)rand);
                     iv = c.getIV();
                     byte[] encData = c.doFinal(bOut.toByteArray(), 0, bOut.toByteArray().length);
                     this.secret = new SecretKeyPacket(pubPk, encAlgorithm, s2k, iv, encData);
                     this.trust = null;
                  } else {
                     this.secret = new SecretKeyPacket(pubPk, encAlgorithm, (S2K)null, (byte[])null, bOut.toByteArray());
                     this.trust = null;
                  }
                  break;
               }

               checkSum += keyData[i] & 255;
               ++i;
            }
         } catch (PGPException var19) {
            throw var19;
         } catch (Exception var20) {
            throw new PGPException("Exception encrypting key", var20);
         }

         this.keySigs = new ArrayList();
         return;
      default:
         throw new PGPException("unknown key class");
      }
   }

   public PGPSecretKey(int certificationLevel, PGPKeyPair keyPair, String id, int encAlgorithm, char[] passPhrase, PGPSignatureSubpacketVector hashedPcks, PGPSignatureSubpacketVector unhashedPcks, SecureRandom rand, BouncyCastleProvider provider) throws PGPException {
      this(keyPair, encAlgorithm, passPhrase, rand, provider);

      try {
         this.trust = null;
         this.ids = new ArrayList();
         this.ids.add(id);
         this.idTrusts = new ArrayList();
         this.idTrusts.add((Object)null);
         this.idSigs = new ArrayList();
         PGPSignatureGenerator sGen = new PGPSignatureGenerator(keyPair.getPublicKey().getAlgorithm(), 2, provider);
         sGen.initSign(certificationLevel, keyPair.getPrivateKey());
         sGen.setHashedSubpackets(hashedPcks);
         sGen.setUnhashedSubpackets(unhashedPcks);
         PGPSignature certification = sGen.generateCertification(id, keyPair.getPublicKey());
         this.pub = PGPPublicKey.addCertification(keyPair.getPublicKey(), id, certification);
         List sigList = new ArrayList();
         sigList.add(certification);
         this.idSigs.add(sigList);
      } catch (PGPException var13) {
         throw var13;
      } catch (Exception var14) {
         var14.printStackTrace();
         throw new PGPException("Exception encrypting key", var14);
      }
   }

   public PGPSecretKey(int certificationLevel, int algorithm, PublicKey pubKey, PrivateKey privKey, Date time, String id, int encAlgorithm, char[] passPhrase, PGPSignatureSubpacketVector hashedPcks, PGPSignatureSubpacketVector unhashedPcks, SecureRandom rand, BouncyCastleProvider provider) throws PGPException, NoSuchProviderException {
      this(certificationLevel, new PGPKeyPair(algorithm, pubKey, privKey, time, provider), id, encAlgorithm, passPhrase, hashedPcks, unhashedPcks, rand, provider);
   }

   public boolean isSigningKey() {
      int algorithm = this.pub.getAlgorithm();
      return algorithm == 1 || algorithm == 3 || algorithm == 17 || algorithm == 19 || algorithm == 20;
   }

   public boolean isMasterKey() {
      return this.subSigs == null;
   }

   public int getKeyEncryptionAlgorithm() {
      return this.secret.getEncAlgorithm();
   }

   public long getKeyID() {
      return this.pub.getKeyID();
   }

   public PGPPublicKey getPublicKey() {
      return this.pub;
   }

   public Iterator getUserIDs() {
      return this.pub.getUserIDs();
   }

   public Iterator getUserAttributes() {
      return this.pub.getUserAttributes();
   }

   private byte[] extractKeyData(char[] passPhrase, BouncyCastleProvider provider) throws PGPException {
      String cName = PGPUtil.getSymmetricCipherName(this.secret.getEncAlgorithm());
      Cipher c = null;
      if (cName != null) {
         try {
            c = Cipher.getInstance(cName + "/CFB/NoPadding", (Provider)provider);
         } catch (Exception var13) {
            throw new PGPException("Exception creating cipher", var13);
         }
      }

      byte[] encData = this.secret.getSecretKeyData();
      byte[] data = (byte[])null;

      try {
         if (c != null) {
            try {
               if (this.secret.getPublicKeyPacket().getVersion() == 4) {
                  IvParameterSpec ivSpec = new IvParameterSpec(this.secret.getIV());
                  SecretKey key = PGPUtil.makeKeyFromPassPhrase(this.secret.getEncAlgorithm(), this.secret.getS2K(), passPhrase, provider);
                  c.init(2, key, (AlgorithmParameterSpec)ivSpec);
                  data = c.doFinal(encData, 0, encData.length);
               } else {
                  SecretKey key = PGPUtil.makeKeyFromPassPhrase(this.secret.getEncAlgorithm(), this.secret.getS2K(), passPhrase, provider);
                  data = new byte[encData.length];
                  byte[] iv = new byte[this.secret.getIV().length];
                  System.arraycopy(this.secret.getIV(), 0, iv, 0, iv.length);
                  int pos = 0;

                  int cs;
                  int calcCs;
                  for(cs = 0; cs != 4; ++cs) {
                     c.init(2, key, (AlgorithmParameterSpec)(new IvParameterSpec(iv)));
                     calcCs = ((encData[pos] << 8 | encData[pos + 1] & 255) + 7) / 8;
                     data[pos] = encData[pos];
                     data[pos + 1] = encData[pos + 1];
                     c.doFinal(encData, pos + 2, calcCs, data, pos + 2);
                     pos += 2 + calcCs;
                     if (cs != 3) {
                        System.arraycopy(encData, pos - iv.length, iv, 0, iv.length);
                     }
                  }

                  cs = encData[pos] << 8 & '\uff00' | encData[pos + 1] & 255;
                  calcCs = 0;

                  for(int j = 0; j < data.length - 2; ++j) {
                     calcCs += data[j] & 255;
                  }

                  calcCs &= 65535;
                  if (calcCs != cs) {
                     throw new PGPException("checksum mismatch: passphrase wrong, expected " + Integer.toHexString(cs) + " found " + Integer.toHexString(calcCs));
                  }
               }
            } catch (PGPException var14) {
               throw var14;
            } catch (Exception var15) {
               throw new PGPException("Exception decrypting key", var15);
            }
         } else {
            data = encData;
         }

         return data;
      } catch (PGPException var16) {
         throw var16;
      } catch (Exception var17) {
         throw new PGPException("Exception constructing key", var17);
      }
   }

   public PGPPrivateKey extractPrivateKey(char[] passPhrase, BouncyCastleProvider provider) throws PGPException {
      PublicKeyPacket pubPk = this.secret.getPublicKeyPacket();
      if (this.secret.getSecretKeyData() == null) {
         return null;
      } else {
         try {
            byte[] data = this.extractKeyData(passPhrase, provider);
            BCPGInputStream in = new BCPGInputStream(new ByteArrayInputStream(data));
            switch(pubPk.getAlgorithm()) {
            case 1:
            case 2:
            case 3:
               RSAPublicBCPGKey rsaPub = (RSAPublicBCPGKey)pubPk.getKey();
               RSASecretBCPGKey rsaPriv = new RSASecretBCPGKey(in);
               RSAPrivateCrtKeySpec rsaPrivSpec = new RSAPrivateCrtKeySpec(rsaPriv.getModulus(), rsaPub.getPublicExponent(), rsaPriv.getPrivateExponent(), rsaPriv.getPrimeP(), rsaPriv.getPrimeQ(), rsaPriv.getPrimeExponentP(), rsaPriv.getPrimeExponentQ(), rsaPriv.getCrtCoefficient());
               KeyFactory fact = KeyFactory.getInstance("RSA", provider);
               return new PGPPrivateKey(fact.generatePrivate(rsaPrivSpec), this.getKeyID());
            default:
               throw new PGPException("unknown public key algorithm encountered");
            }
         } catch (PGPException var10) {
            throw var10;
         } catch (Exception var11) {
            throw new PGPException("Exception constructing key", var11);
         }
      }
   }

   public byte[] getEncoded() throws IOException {
      ByteArrayOutputStream bOut = new ByteArrayOutputStream();
      this.encode(bOut);
      return bOut.toByteArray();
   }

   public void encode(OutputStream outStream) throws IOException {
      BCPGOutputStream out;
      if (outStream instanceof BCPGOutputStream) {
         out = (BCPGOutputStream)outStream;
      } else {
         out = new BCPGOutputStream(outStream);
      }

      out.writePacket(this.secret);
      if (this.trust != null) {
         out.writePacket(this.trust);
      }

      int i;
      if (this.subSigs == null) {
         for(i = 0; i != this.keySigs.size(); ++i) {
            ((PGPSignature)this.keySigs.get(i)).encode(out);
         }

         for(i = 0; i != this.ids.size(); ++i) {
            if (this.ids.get(i) instanceof String) {
               String id = (String)this.ids.get(i);
               out.writePacket(new UserIDPacket(id));
            } else {
               PGPUserAttributeSubpacketVector v = (PGPUserAttributeSubpacketVector)this.ids.get(i);
               out.writePacket(new UserAttributePacket(v.toSubpacketArray()));
            }

            if (this.idTrusts.get(i) != null) {
               out.writePacket((ContainedPacket)this.idTrusts.get(i));
            }

            List sigs = (ArrayList)this.idSigs.get(i);

            for(int j = 0; j != sigs.size(); ++j) {
               ((PGPSignature)sigs.get(j)).encode(out);
            }
         }
      } else {
         for(i = 0; i != this.subSigs.size(); ++i) {
            ((PGPSignature)this.subSigs.get(i)).encode(out);
         }
      }

   }

   public static PGPSecretKey copyWithNewPassword(PGPSecretKey key, char[] oldPassPhrase, char[] newPassPhrase, int newEncAlgorithm, SecureRandom rand, BouncyCastleProvider provider) throws PGPException, NoSuchProviderException {
      byte[] rawKeyData = key.extractKeyData(oldPassPhrase, provider);
      byte[] iv = (byte[])null;
      S2K s2k = null;
      byte[] keyData = (byte[])null;
      Cipher c;
      if (newEncAlgorithm == 0) {
         keyData = rawKeyData;
      } else {
         c = null;
         String cName = PGPUtil.getSymmetricCipherName(newEncAlgorithm);

         try {
            c = Cipher.getInstance(cName + "/CFB/NoPadding", (Provider)provider);
         } catch (Exception var15) {
            throw new PGPException("Exception creating cipher", var15);
         }

         iv = new byte[8];
         rand.nextBytes(iv);
         s2k = new S2K(2, iv, 96);

         try {
            SecretKey sKey = PGPUtil.makeKeyFromPassPhrase(newEncAlgorithm, s2k, newPassPhrase, provider);
            c.init(1, sKey, (SecureRandom)rand);
            iv = c.getIV();
            keyData = c.doFinal(rawKeyData);
         } catch (PGPException var13) {
            throw var13;
         } catch (Exception var14) {
            throw new PGPException("Exception encrypting key", var14);
         }
      }

      c = null;
      Object secret;
      if (key.secret instanceof SecretSubkeyPacket) {
         secret = new SecretSubkeyPacket(key.secret.getPublicKeyPacket(), newEncAlgorithm, s2k, iv, keyData);
      } else {
         secret = new SecretKeyPacket(key.secret.getPublicKeyPacket(), newEncAlgorithm, s2k, iv, keyData);
      }

      return key.subSigs == null ? new PGPSecretKey((SecretKeyPacket)secret, key.trust, key.keySigs, key.ids, key.idTrusts, key.idSigs, key.pub) : new PGPSecretKey((SecretKeyPacket)secret, key.trust, key.subSigs, key.pub);
   }
}
