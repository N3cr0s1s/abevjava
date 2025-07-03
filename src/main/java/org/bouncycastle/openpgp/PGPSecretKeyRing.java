package org.bouncycastle.openpgp;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.bouncycastle.bcpg.BCPGInputStream;
import org.bouncycastle.bcpg.SecretKeyPacket;
import org.bouncycastle.bcpg.SecretSubkeyPacket;
import org.bouncycastle.bcpg.SignaturePacket;
import org.bouncycastle.bcpg.TrustPacket;
import org.bouncycastle.bcpg.UserAttributePacket;
import org.bouncycastle.bcpg.UserIDPacket;

public class PGPSecretKeyRing {
   List keys;

   PGPSecretKeyRing(List keys) {
      this.keys = new ArrayList();
      this.keys = keys;
   }

   public PGPSecretKeyRing(byte[] encoding) throws IOException, PGPException {
      this((InputStream)(new ByteArrayInputStream(encoding)));
   }

   public PGPSecretKeyRing(InputStream in) throws IOException, PGPException {
      this.keys = new ArrayList();
      BCPGInputStream pIn;
      if (in instanceof BCPGInputStream) {
         pIn = (BCPGInputStream)in;
      } else {
         pIn = new BCPGInputStream(in);
      }

      int initialTag = pIn.nextPacketTag();
      if (initialTag != 5 && initialTag != 7) {
         throw new IOException("secret key ring doesn't start with secret key tag: tag 0x" + Integer.toHexString(initialTag));
      } else {
         SecretKeyPacket secret = (SecretKeyPacket)pIn.readPacket();
         TrustPacket trust = null;
         List keySigs = new ArrayList();
         List ids = new ArrayList();
         List idTrusts = new ArrayList();
         ArrayList idSigs = new ArrayList();

         MessageDigest sha;
         try {
            sha = MessageDigest.getInstance("SHA1");
         } catch (NoSuchAlgorithmException var16) {
            throw new IOException("can't find SHA1 digest");
         }

         while(pIn.nextPacketTag() == 61) {
            pIn.readPacket();
         }

         if (pIn.nextPacketTag() == 12) {
            trust = (TrustPacket)pIn.readPacket();
         }

         while(pIn.nextPacketTag() == 2) {
            try {
               keySigs.add(new PGPSignature(pIn));
            } catch (PGPException var15) {
               throw new IOException("can't create signature object: " + var15.getMessage() + ", cause: " + var15.getUnderlyingException().toString());
            }
         }

         while(pIn.nextPacketTag() == 13 || pIn.nextPacketTag() == 17) {
            Object obj = pIn.readPacket();
            List sigList = new ArrayList();
            if (obj instanceof UserIDPacket) {
               UserIDPacket id = (UserIDPacket)obj;
               ids.add(id.getID());
            } else {
               UserAttributePacket user = (UserAttributePacket)obj;
               ids.add(new PGPUserAttributeSubpacketVector(user.getSubpackets()));
            }

            if (pIn.nextPacketTag() == 12) {
               idTrusts.add(pIn.readPacket());
            } else {
               idTrusts.add((Object)null);
            }

            idSigs.add(sigList);

            while(pIn.nextPacketTag() == 2) {
               SignaturePacket s = (SignaturePacket)pIn.readPacket();
               if (pIn.nextPacketTag() == 12) {
                  sigList.add(new PGPSignature(s, (TrustPacket)pIn.readPacket()));
               } else {
                  sigList.add(new PGPSignature(s));
               }
            }
         }

         this.keys.add(new PGPSecretKey(secret, trust, sha, keySigs, ids, idTrusts, idSigs));

         while(pIn.nextPacketTag() == 7) {
            SecretSubkeyPacket sub = (SecretSubkeyPacket)pIn.readPacket();
            TrustPacket subTrust = null;
            ArrayList sigList = new ArrayList();

            while(pIn.nextPacketTag() == 61) {
               pIn.readPacket();
            }

            if (pIn.nextPacketTag() == 12) {
               subTrust = (TrustPacket)pIn.readPacket();
            }

            while(pIn.nextPacketTag() == 2) {
               SignaturePacket s = (SignaturePacket)pIn.readPacket();
               if (pIn.nextPacketTag() == 12) {
                  sigList.add(new PGPSignature(s, (TrustPacket)pIn.readPacket()));
               } else {
                  sigList.add(new PGPSignature(s));
               }
            }

            this.keys.add(new PGPSecretKey(sub, subTrust, sha, sigList));
         }

      }
   }

   public PGPPublicKey getPublicKey() {
      return ((PGPSecretKey)this.keys.get(0)).getPublicKey();
   }

   public PGPSecretKey getSecretKey() {
      return (PGPSecretKey)this.keys.get(0);
   }

   public Iterator getSecretKeys() {
      return Collections.unmodifiableList(this.keys).iterator();
   }

   public PGPSecretKey getSecretKey(long keyId) {
      for(int i = 0; i != this.keys.size(); ++i) {
         PGPSecretKey k = (PGPSecretKey)this.keys.get(i);
         if (keyId == k.getKeyID()) {
            return k;
         }
      }

      return null;
   }

   public byte[] getEncoded() throws IOException {
      ByteArrayOutputStream bOut = new ByteArrayOutputStream();
      this.encode(bOut);
      return bOut.toByteArray();
   }

   public void encode(OutputStream outStream) throws IOException {
      for(int i = 0; i != this.keys.size(); ++i) {
         PGPSecretKey k = (PGPSecretKey)this.keys.get(i);
         k.encode(outStream);
      }

   }

   public static PGPSecretKeyRing insertSecretKey(PGPSecretKeyRing secRing, PGPSecretKey secKey) {
      List keys = new ArrayList(secRing.keys);
      boolean found = false;

      for(int i = 0; i != keys.size(); ++i) {
         PGPSecretKey key = (PGPSecretKey)keys.get(i);
         if (key.getKeyID() == secKey.getKeyID()) {
            found = true;
            keys.set(i, secKey);
         }
      }

      if (!found) {
         keys.add(secKey);
      }

      return new PGPSecretKeyRing(keys);
   }

   public static PGPSecretKeyRing removeSecretKey(PGPSecretKeyRing secRing, PGPSecretKey secKey) {
      List keys = new ArrayList(secRing.keys);
      boolean found = false;

      for(int i = 0; i < keys.size(); ++i) {
         PGPSecretKey key = (PGPSecretKey)keys.get(i);
         if (key.getKeyID() == secKey.getKeyID()) {
            found = true;
            keys.remove(i);
         }
      }

      if (!found) {
         return null;
      } else {
         return new PGPSecretKeyRing(keys);
      }
   }
}
