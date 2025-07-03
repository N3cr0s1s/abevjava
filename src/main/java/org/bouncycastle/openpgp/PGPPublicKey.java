package org.bouncycastle.openpgp;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.bouncycastle.bcpg.BCPGKey;
import org.bouncycastle.bcpg.BCPGOutputStream;
import org.bouncycastle.bcpg.ContainedPacket;
import org.bouncycastle.bcpg.MPInteger;
import org.bouncycastle.bcpg.PublicKeyAlgorithmTags;
import org.bouncycastle.bcpg.PublicKeyPacket;
import org.bouncycastle.bcpg.RSAPublicBCPGKey;
import org.bouncycastle.bcpg.TrustPacket;
import org.bouncycastle.bcpg.UserAttributePacket;
import org.bouncycastle.bcpg.UserIDPacket;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class PGPPublicKey implements PublicKeyAlgorithmTags {
   private static final int[] MASTER_KEY_CERTIFICATION_TYPES = new int[]{19, 18, 17, 16};
   PublicKeyPacket publicPk;
   TrustPacket trustPk;
   List keySigs = new ArrayList();
   List ids = new ArrayList();
   List idTrusts = new ArrayList();
   List idSigs = new ArrayList();
   List subSigs = null;
   private long keyID;
   private byte[] fingerprint;
   private int keyStrength;

   private void init() throws IOException {
      BCPGKey key = this.publicPk.getKey();
      MessageDigest digest;
      if (this.publicPk.getVersion() <= 3) {
         RSAPublicBCPGKey rK = (RSAPublicBCPGKey)key;
         this.keyID = rK.getModulus().longValue();

         try {
            digest = MessageDigest.getInstance("MD5");
            byte[] bytes = (new MPInteger(rK.getModulus())).getEncoded();
            digest.update(bytes, 2, bytes.length - 2);
            bytes = (new MPInteger(rK.getPublicExponent())).getEncoded();
            digest.update(bytes, 2, bytes.length - 2);
            this.fingerprint = digest.digest();
         } catch (NoSuchAlgorithmException var6) {
            throw new IOException("can't find MD5");
         }

         this.keyStrength = rK.getModulus().bitLength();
      } else {
         byte[] kBytes = this.publicPk.getEncodedContents();

         try {
            digest = MessageDigest.getInstance("SHA1");
            digest.update((byte)-103);
            digest.update((byte)(kBytes.length >> 8));
            digest.update((byte)kBytes.length);
            digest.update(kBytes);
            this.fingerprint = digest.digest();
         } catch (NoSuchAlgorithmException var5) {
            throw new IOException("can't find SHA1");
         }

         this.keyID = (long)(this.fingerprint[this.fingerprint.length - 8] & 255) << 56 | (long)(this.fingerprint[this.fingerprint.length - 7] & 255) << 48 | (long)(this.fingerprint[this.fingerprint.length - 6] & 255) << 40 | (long)(this.fingerprint[this.fingerprint.length - 5] & 255) << 32 | (long)(this.fingerprint[this.fingerprint.length - 4] & 255) << 24 | (long)(this.fingerprint[this.fingerprint.length - 3] & 255) << 16 | (long)(this.fingerprint[this.fingerprint.length - 2] & 255) << 8 | (long)(this.fingerprint[this.fingerprint.length - 1] & 255);
         if (key instanceof RSAPublicBCPGKey) {
            this.keyStrength = ((RSAPublicBCPGKey)key).getModulus().bitLength();
         }
      }

   }

   public PGPPublicKey(int algorithm, PublicKey pubKey, Date time, BouncyCastleProvider provider) throws PGPException, NoSuchProviderException {
      if (pubKey instanceof RSAPublicKey) {
         RSAPublicKey rK = (RSAPublicKey)pubKey;
         PublicKeyPacket pubPk = new PublicKeyPacket(algorithm, time, new RSAPublicBCPGKey(rK.getModulus(), rK.getPublicExponent()));
         this.publicPk = pubPk;
         this.ids = new ArrayList();
         this.idSigs = new ArrayList();

         try {
            this.init();
         } catch (IOException var7) {
            throw new PGPException("exception calculating keyID", var7);
         }
      } else {
         throw new PGPException("unknown key class");
      }
   }

   PGPPublicKey(PublicKeyPacket publicPk, TrustPacket trustPk, List sigs) throws IOException {
      this.publicPk = publicPk;
      this.trustPk = trustPk;
      this.subSigs = sigs;
      this.init();
   }

   PGPPublicKey(PGPPublicKey key, TrustPacket trust, List subSigs) {
      this.publicPk = key.publicPk;
      this.trustPk = trust;
      this.subSigs = subSigs;
      this.fingerprint = key.fingerprint;
      this.keyID = key.keyID;
      this.keyStrength = key.keyStrength;
   }

   PGPPublicKey(PGPPublicKey pubKey) {
      this.publicPk = pubKey.publicPk;
      this.keySigs = new ArrayList(pubKey.keySigs);
      this.ids = new ArrayList(pubKey.ids);
      this.idTrusts = new ArrayList(pubKey.idTrusts);
      this.idSigs = new ArrayList(pubKey.idSigs.size());

      int i;
      for(i = 0; i != pubKey.idSigs.size(); ++i) {
         this.idSigs.add(new ArrayList((ArrayList)pubKey.idSigs.get(i)));
      }

      if (pubKey.subSigs != null) {
         this.subSigs = new ArrayList(pubKey.subSigs.size());

         for(i = 0; i != pubKey.subSigs.size(); ++i) {
            this.subSigs.add(pubKey.subSigs.get(i));
         }
      }

      this.fingerprint = pubKey.fingerprint;
      this.keyID = pubKey.keyID;
      this.keyStrength = pubKey.keyStrength;
   }

   PGPPublicKey(PublicKeyPacket publicPk, TrustPacket trustPk, List keySigs, List ids, List idTrusts, List idSigs) throws IOException {
      this.publicPk = publicPk;
      this.trustPk = trustPk;
      this.keySigs = keySigs;
      this.ids = ids;
      this.idTrusts = idTrusts;
      this.idSigs = idSigs;
      this.init();
   }

   PGPPublicKey(PublicKeyPacket publicPk, List ids, List idSigs) throws IOException {
      this.publicPk = publicPk;
      this.ids = ids;
      this.idSigs = idSigs;
      this.init();
   }

   public int getVersion() {
      return this.publicPk.getVersion();
   }

   public Date getCreationTime() {
      return this.publicPk.getTime();
   }

   public int getValidDays() {
      return this.publicPk.getVersion() > 3 ? (int)(this.getValidSeconds() / 86400L) : this.publicPk.getValidDays();
   }

   public long getValidSeconds() {
      if (this.publicPk.getVersion() <= 3) {
         return (long)this.publicPk.getValidDays() * 24L * 60L * 60L;
      } else {
         if (this.isMasterKey()) {
            for(int i = 0; i != MASTER_KEY_CERTIFICATION_TYPES.length; ++i) {
               long seconds = this.getExpirationTimeFromSig(true, MASTER_KEY_CERTIFICATION_TYPES[i]);
               if (seconds >= 0L) {
                  return seconds;
               }
            }
         } else {
            long seconds = this.getExpirationTimeFromSig(false, 24);
            if (seconds >= 0L) {
               return seconds;
            }
         }

         return 0L;
      }
   }

   private long getExpirationTimeFromSig(boolean selfSigned, int signatureType) {
      Iterator signatures = this.getSignaturesOfType(signatureType);
      if (signatures.hasNext()) {
         PGPSignature sig = (PGPSignature)signatures.next();
         if (!selfSigned || sig.getKeyID() == this.getKeyID()) {
            PGPSignatureSubpacketVector hashed = sig.getHashedSubPackets();
            if (hashed != null) {
               return hashed.getKeyExpirationTime();
            }

            return 0L;
         }
      }

      return -1L;
   }

   public long getKeyID() {
      return this.keyID;
   }

   public byte[] getFingerprint() {
      byte[] tmp = new byte[this.fingerprint.length];
      System.arraycopy(this.fingerprint, 0, tmp, 0, tmp.length);
      return tmp;
   }

   public boolean isEncryptionKey() {
      int algorithm = this.publicPk.getAlgorithm();
      return algorithm == 1 || algorithm == 2 || algorithm == 16 || algorithm == 20;
   }

   public boolean isMasterKey() {
      return this.subSigs == null;
   }

   public int getAlgorithm() {
      return this.publicPk.getAlgorithm();
   }

   public int getBitStrength() {
      return this.keyStrength;
   }

   public PublicKey getKey(BouncyCastleProvider provider) throws PGPException, NoSuchProviderException {
      try {
         switch(this.publicPk.getAlgorithm()) {
         case 1:
         case 2:
         case 3:
            RSAPublicBCPGKey rsaK = (RSAPublicBCPGKey)this.publicPk.getKey();
            RSAPublicKeySpec rsaSpec = new RSAPublicKeySpec(rsaK.getModulus(), rsaK.getPublicExponent());
            KeyFactory fact = KeyFactory.getInstance("RSA", provider);
            return fact.generatePublic(rsaSpec);
         default:
            throw new PGPException("unknown public key algorithm encountered");
         }
      } catch (PGPException var5) {
         throw var5;
      } catch (Exception var6) {
         throw new PGPException("exception constructing public key", var6);
      }
   }

   public Iterator getUserIDs() {
      List temp = new ArrayList();

      for(int i = 0; i != this.ids.size(); ++i) {
         if (this.ids.get(i) instanceof String) {
            temp.add(this.ids.get(i));
         }
      }

      return temp.iterator();
   }

   public Iterator getUserAttributes() {
      List temp = new ArrayList();

      for(int i = 0; i != this.ids.size(); ++i) {
         if (this.ids.get(i) instanceof PGPUserAttributeSubpacketVector) {
            temp.add(this.ids.get(i));
         }
      }

      return temp.iterator();
   }

   public Iterator getSignaturesForID(String id) {
      for(int i = 0; i != this.ids.size(); ++i) {
         if (id.equals(this.ids.get(i))) {
            return ((ArrayList)this.idSigs.get(i)).iterator();
         }
      }

      return null;
   }

   public Iterator getSignaturesForUserAttribute(PGPUserAttributeSubpacketVector userAttributes) {
      for(int i = 0; i != this.ids.size(); ++i) {
         if (userAttributes.equals(this.ids.get(i))) {
            return ((ArrayList)this.idSigs.get(i)).iterator();
         }
      }

      return null;
   }

   public Iterator getSignaturesOfType(int signatureType) {
      List l = new ArrayList();
      Iterator it = this.getSignatures();

      while(it.hasNext()) {
         PGPSignature sig = (PGPSignature)it.next();
         if (sig.getSignatureType() == signatureType) {
            l.add(sig);
         }
      }

      return l.iterator();
   }

   public Iterator getSignatures() {
      if (this.subSigs != null) {
         return this.subSigs.iterator();
      } else {
         List sigs = new ArrayList();
         sigs.addAll(this.keySigs);

         for(int i = 0; i != this.idSigs.size(); ++i) {
            sigs.addAll((Collection)this.idSigs.get(i));
         }

         return sigs.iterator();
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

      out.writePacket(this.publicPk);
      if (this.trustPk != null) {
         out.writePacket(this.trustPk);
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

            List sigs = (List)this.idSigs.get(i);

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

   public boolean isRevoked() {
      int ns = 0;
      boolean revoked = false;
      if (this.isMasterKey()) {
         while(!revoked && ns < this.keySigs.size()) {
            if (((PGPSignature)this.keySigs.get(ns++)).getSignatureType() == 32) {
               revoked = true;
            }
         }
      } else {
         while(!revoked && ns < this.subSigs.size()) {
            if (((PGPSignature)this.subSigs.get(ns++)).getSignatureType() == 40) {
               revoked = true;
            }
         }
      }

      return revoked;
   }

   public static PGPPublicKey addCertification(PGPPublicKey key, String id, PGPSignature certification) {
      PGPPublicKey returnKey = new PGPPublicKey(key);
      List sigList = null;

      for(int i = 0; i != returnKey.ids.size(); ++i) {
         if (id.equals(returnKey.ids.get(i))) {
            sigList = (List<?>)returnKey.idSigs.get(i);
         }
      }

      if (sigList != null) {
         sigList.add(certification);
      } else {
         sigList = new ArrayList();
         sigList.add(certification);
         returnKey.ids.add(id);
         returnKey.idTrusts.add(null);
         returnKey.idSigs.add(sigList);
      }

      return returnKey;
   }

   public static PGPPublicKey removeCertification(PGPPublicKey key, String id) {
      PGPPublicKey returnKey = new PGPPublicKey(key);
      boolean found = false;

      for(int i = 0; i < returnKey.ids.size(); ++i) {
         if (id.equals(returnKey.ids.get(i))) {
            found = true;
            returnKey.ids.remove(i);
            returnKey.idTrusts.remove(i);
            returnKey.idSigs.remove(i);
         }
      }

      if (!found) {
         return null;
      } else {
         return returnKey;
      }
   }

   public static PGPPublicKey removeCertification(PGPPublicKey key, String id, PGPSignature certification) {
      PGPPublicKey returnKey = new PGPPublicKey(key);
      boolean found = false;

      for(int i = 0; i < returnKey.ids.size(); ++i) {
         if (id.equals(returnKey.ids.get(i))) {
            found = ((List)returnKey.idSigs.get(i)).remove(certification);
         }
      }

      if (!found) {
         return null;
      } else {
         return returnKey;
      }
   }

   public static PGPPublicKey addCertification(PGPPublicKey key, PGPSignature certification) {
      if (key.isMasterKey()) {
         if (certification.getSignatureType() == 40) {
            throw new IllegalArgumentException("signature type incorrect for master key revocation.");
         }
      } else if (certification.getSignatureType() == 32) {
         throw new IllegalArgumentException("signature type incorrect for sub-key revocation.");
      }

      PGPPublicKey returnKey = new PGPPublicKey(key);
      if (returnKey.subSigs != null) {
         returnKey.subSigs.add(certification);
      } else {
         returnKey.keySigs.add(certification);
      }

      return returnKey;
   }
}
