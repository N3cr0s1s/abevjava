package org.bouncycastle.openpgp;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Signature;
import java.security.SignatureException;
import java.util.Date;
import org.bouncycastle.bcpg.MPInteger;
import org.bouncycastle.bcpg.OnePassSignaturePacket;
import org.bouncycastle.bcpg.SignaturePacket;
import org.bouncycastle.bcpg.SignatureSubpacket;
import org.bouncycastle.bcpg.sig.IssuerKeyID;
import org.bouncycastle.bcpg.sig.SignatureCreationTime;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class PGPSignatureGenerator {
   private int keyAlgorithm;
   private int hashAlgorithm;
   private PGPPrivateKey privKey;
   private Signature sig;
   private MessageDigest dig;
   private int signatureType;
   private boolean creationTimeFound;
   private boolean issuerKeyIDFound;
   private byte lastb;
   SignatureSubpacket[] unhashed = new SignatureSubpacket[0];
   SignatureSubpacket[] hashed = new SignatureSubpacket[2];

   public PGPSignatureGenerator(int keyAlgorithm, int hashAlgorithm, BouncyCastleProvider provider) throws NoSuchAlgorithmException, NoSuchProviderException, PGPException {
      this.keyAlgorithm = keyAlgorithm;
      this.hashAlgorithm = hashAlgorithm;
      this.dig = PGPUtil.getDigestInstance(PGPUtil.getDigestName(hashAlgorithm), provider);
      this.sig = Signature.getInstance(PGPUtil.getSignatureName(keyAlgorithm, hashAlgorithm), provider);
   }

   public void initSign(int signatureType, PGPPrivateKey key) throws PGPException {
      this.privKey = key;
      this.signatureType = signatureType;

      try {
         this.sig.initSign(key.getKey());
      } catch (InvalidKeyException var4) {
         throw new PGPException("invalid key.", var4);
      }

      this.dig.reset();
      this.lastb = 0;
   }

   public void update(byte b) throws SignatureException {
      if (this.signatureType == 1) {
         if (b == 13) {
            this.sig.update((byte)13);
            this.sig.update((byte)10);
            this.dig.update((byte)13);
            this.dig.update((byte)10);
         } else if (b == 10) {
            if (this.lastb != 13) {
               this.sig.update((byte)13);
               this.sig.update((byte)10);
               this.dig.update((byte)13);
               this.dig.update((byte)10);
            }
         } else {
            this.sig.update(b);
            this.dig.update(b);
         }

         this.lastb = b;
      } else {
         this.sig.update(b);
         this.dig.update(b);
      }

   }

   public void update(byte[] b) throws SignatureException {
      this.update(b, 0, b.length);
   }

   public void update(byte[] b, int off, int len) throws SignatureException {
      if (this.signatureType == 1) {
         int finish = off + len;

         for(int i = off; i != finish; ++i) {
            this.update(b[i]);
         }
      } else {
         this.sig.update(b, off, len);
         this.dig.update(b, off, len);
      }

   }

   public void setHashedSubpackets(PGPSignatureSubpacketVector hashedPcks) {
      this.creationTimeFound = false;
      this.issuerKeyIDFound = false;
      if (hashedPcks == null) {
         this.hashed = new SignatureSubpacket[2];
      } else {
         SignatureSubpacket[] tmp = hashedPcks.toSubpacketArray();

         for(int i = 0; i != tmp.length; ++i) {
            if (tmp[i].getType() == 2) {
               this.creationTimeFound = true;
            } else if (tmp[i].getType() == 16) {
               this.issuerKeyIDFound = true;
            }
         }

         if (this.creationTimeFound && this.issuerKeyIDFound) {
            this.hashed = tmp;
         } else if (!this.creationTimeFound && !this.issuerKeyIDFound) {
            this.hashed = new SignatureSubpacket[tmp.length + 2];
            System.arraycopy(tmp, 0, this.hashed, 2, tmp.length);
         } else {
            this.hashed = new SignatureSubpacket[tmp.length + 1];
            System.arraycopy(tmp, 0, this.hashed, 1, tmp.length);
         }

      }
   }

   public void setUnhashedSubpackets(PGPSignatureSubpacketVector unhashedPcks) {
      if (unhashedPcks == null) {
         this.unhashed = new SignatureSubpacket[0];
      } else {
         this.unhashed = unhashedPcks.toSubpacketArray();
      }
   }

   public PGPOnePassSignature generateOnePassVersion(boolean isNested) throws PGPException {
      return new PGPOnePassSignature(new OnePassSignaturePacket(this.signatureType, this.hashAlgorithm, this.keyAlgorithm, this.privKey.getKeyID(), isNested));
   }

   public PGPSignature generate() throws PGPException, SignatureException {
      int version = 4;
      ByteArrayOutputStream sOut = new ByteArrayOutputStream();
      int index = 0;
      if (!this.creationTimeFound) {
         this.hashed[index++] = new SignatureCreationTime(false, new Date());
      }

      if (!this.issuerKeyIDFound) {
         this.hashed[index++] = new IssuerKeyID(false, this.privKey.getKeyID());
      }

      byte[] trailer;
      try {
         sOut.write((byte)version);
         sOut.write((byte)this.signatureType);
         sOut.write((byte)this.keyAlgorithm);
         sOut.write((byte)this.hashAlgorithm);
         ByteArrayOutputStream hOut = new ByteArrayOutputStream();
         int i = 0;

         while(true) {
            if (i == this.hashed.length) {
               trailer = hOut.toByteArray();
               sOut.write((byte)(trailer.length >> 8));
               sOut.write((byte)trailer.length);
               sOut.write(trailer);
               break;
            }

            this.hashed[i].encode(hOut);
            ++i;
         }
      } catch (IOException var9) {
         throw new PGPException("exception encoding hashed data.", var9);
      }

      byte[] hData = sOut.toByteArray();
      sOut.write((byte)version);
      sOut.write(-1);
      sOut.write((byte)(hData.length >> 24));
      sOut.write((byte)(hData.length >> 16));
      sOut.write((byte)(hData.length >> 8));
      sOut.write((byte)hData.length);
      trailer = sOut.toByteArray();
      this.sig.update(trailer);
      this.dig.update(trailer);
      MPInteger[] sigValues;
      if (this.keyAlgorithm != 3 && this.keyAlgorithm != 1) {
         sigValues = PGPUtil.dsaSigToMpi(this.sig.sign());
      } else {
         sigValues = new MPInteger[]{new MPInteger(new BigInteger(1, this.sig.sign()))};
      }

      byte[] digest = this.dig.digest();
      byte[] fingerPrint = new byte[]{digest[0], digest[1]};
      return new PGPSignature(new SignaturePacket(this.signatureType, this.privKey.getKeyID(), this.keyAlgorithm, this.hashAlgorithm, this.hashed, this.unhashed, fingerPrint, sigValues));
   }

   public PGPSignature generateCertification(String id, PGPPublicKey pubKey) throws SignatureException, PGPException {
      byte[] keyBytes = this.getEncodedPublicKey(pubKey);
      this.update((byte)-103);
      this.update((byte)(keyBytes.length >> 8));
      this.update((byte)keyBytes.length);
      this.update(keyBytes);
      byte[] idBytes = new byte[id.length()];

      for(int i = 0; i != idBytes.length; ++i) {
         idBytes[i] = (byte)id.charAt(i);
      }

      this.update((byte)-76);
      this.update((byte)(idBytes.length >> 24));
      this.update((byte)(idBytes.length >> 16));
      this.update((byte)(idBytes.length >> 8));
      this.update((byte)idBytes.length);
      this.update(idBytes);
      return this.generate();
   }

   public PGPSignature generateCertification(PGPPublicKey masterKey, PGPPublicKey pubKey) throws SignatureException, PGPException {
      byte[] keyBytes = this.getEncodedPublicKey(masterKey);
      this.update((byte)-103);
      this.update((byte)(keyBytes.length >> 8));
      this.update((byte)keyBytes.length);
      this.update(keyBytes);
      keyBytes = this.getEncodedPublicKey(pubKey);
      this.update((byte)-103);
      this.update((byte)(keyBytes.length >> 8));
      this.update((byte)keyBytes.length);
      this.update(keyBytes);
      return this.generate();
   }

   public PGPSignature generateCertification(PGPPublicKey pubKey) throws SignatureException, PGPException {
      byte[] keyBytes = this.getEncodedPublicKey(pubKey);
      this.update((byte)-103);
      this.update((byte)(keyBytes.length >> 8));
      this.update((byte)keyBytes.length);
      this.update(keyBytes);
      return this.generate();
   }

   private byte[] getEncodedPublicKey(PGPPublicKey pubKey) throws PGPException {
      try {
         byte[] keyBytes = pubKey.publicPk.getEncodedContents();
         return keyBytes;
      } catch (IOException var4) {
         throw new PGPException("exception preparing key.", var4);
      }
   }
}
