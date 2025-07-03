package org.bouncycastle.openpgp;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.bouncycastle.bcpg.BCPGInputStream;
import org.bouncycastle.bcpg.PublicKeyPacket;
import org.bouncycastle.bcpg.SignaturePacket;
import org.bouncycastle.bcpg.TrustPacket;
import org.bouncycastle.bcpg.UserAttributePacket;
import org.bouncycastle.bcpg.UserIDPacket;

public class PGPPublicKeyRing {
   List keys;

   public PGPPublicKeyRing(byte[] encoding) throws IOException {
      this((InputStream)(new ByteArrayInputStream(encoding)));
   }

   PGPPublicKeyRing(List pubKeys) {
      this.keys = new ArrayList();
      this.keys = pubKeys;
   }

   public PGPPublicKeyRing(InputStream in) throws IOException {
      this.keys = new ArrayList();
      BCPGInputStream pIn;
      if (in instanceof BCPGInputStream) {
         pIn = (BCPGInputStream)in;
      } else {
         pIn = new BCPGInputStream(in);
      }

      int initialTag = pIn.nextPacketTag();
      if (initialTag != 6 && initialTag != 14) {
         throw new IOException("public key ring doesn't start with public key tag: tag 0x" + Integer.toHexString(initialTag));
      } else {
         List keySigs = new ArrayList();
         List ids = new ArrayList();
         List idTrust = new ArrayList();
         List idSigs = new ArrayList();
         PublicKeyPacket pubPk = (PublicKeyPacket)pIn.readPacket();
         TrustPacket trustPk = null;
         if (pIn.nextPacketTag() == 12) {
            trustPk = (TrustPacket)pIn.readPacket();
         }

         while(pIn.nextPacketTag() == 2) {
            try {
               SignaturePacket s = (SignaturePacket)pIn.readPacket();
               if (pIn.nextPacketTag() == 12) {
                  keySigs.add(new PGPSignature(s, (TrustPacket)pIn.readPacket()));
               } else {
                  keySigs.add(new PGPSignature(s));
               }
            } catch (PGPException var15) {
               throw new IOException("can't create signature object: " + var15.getMessage() + ", cause: " + var15.getUnderlyingException().toString());
            }
         }

         while(pIn.nextPacketTag() == 13 || pIn.nextPacketTag() == 17) {
            Object obj = pIn.readPacket();
            if (obj instanceof UserIDPacket) {
               UserIDPacket id = (UserIDPacket)obj;
               ids.add(id.getID());
            } else {
               UserAttributePacket user = (UserAttributePacket)obj;
               ids.add(new PGPUserAttributeSubpacketVector(user.getSubpackets()));
            }

            if (pIn.nextPacketTag() == 12) {
               idTrust.add(pIn.readPacket());
            } else {
               idTrust.add((Object)null);
            }

            List sigList = new ArrayList();
            idSigs.add(sigList);

            while(pIn.nextPacketTag() == 2) {
               try {
                  SignaturePacket s = (SignaturePacket)pIn.readPacket();
                  if (pIn.nextPacketTag() == 12) {
                     sigList.add(new PGPSignature(s, (TrustPacket)pIn.readPacket()));
                  } else {
                     sigList.add(new PGPSignature(s));
                  }
               } catch (PGPException var14) {
                  throw new IOException("can't create signature object: " + var14.getMessage() + ", cause: " + var14.getUnderlyingException().toString());
               }
            }
         }

         this.keys.add(new PGPPublicKey(pubPk, trustPk, keySigs, ids, idTrust, idSigs));

         PublicKeyPacket pk;
         TrustPacket kTrust;
         ArrayList sigList;
         for(; pIn.nextPacketTag() == 14; this.keys.add(new PGPPublicKey(pk, kTrust, sigList))) {
            pk = (PublicKeyPacket)pIn.readPacket();
            kTrust = null;
            if (pIn.nextPacketTag() == 12) {
               kTrust = (TrustPacket)pIn.readPacket();
            }

            sigList = new ArrayList();

            try {
               while(pIn.nextPacketTag() == 2) {
                  SignaturePacket s = (SignaturePacket)pIn.readPacket();
                  if (pIn.nextPacketTag() == 12) {
                     sigList.add(new PGPSignature(s, (TrustPacket)pIn.readPacket()));
                  } else {
                     sigList.add(new PGPSignature(s));
                  }
               }
            } catch (PGPException var16) {
               throw new IOException("can't create signature object: " + var16.getMessage() + ", cause: " + var16.getUnderlyingException().toString());
            }
         }

      }
   }

   public PGPPublicKey getPublicKey() {
      return (PGPPublicKey)this.keys.get(0);
   }

   public PGPPublicKey getPublicKey(long keyID) throws PGPException {
      for(int i = 0; i != this.keys.size(); ++i) {
         PGPPublicKey k = (PGPPublicKey)this.keys.get(i);
         if (keyID == k.getKeyID()) {
            return k;
         }
      }

      return null;
   }

   public Iterator getPublicKeys() {
      return Collections.unmodifiableList(this.keys).iterator();
   }

   public byte[] getEncoded() throws IOException {
      ByteArrayOutputStream bOut = new ByteArrayOutputStream();
      this.encode(bOut);
      return bOut.toByteArray();
   }

   public void encode(OutputStream outStream) throws IOException {
      for(int i = 0; i != this.keys.size(); ++i) {
         PGPPublicKey k = (PGPPublicKey)this.keys.get(i);
         k.encode(outStream);
      }

   }

   public static PGPPublicKeyRing insertPublicKey(PGPPublicKeyRing pubRing, PGPPublicKey pubKey) {
      List keys = new ArrayList(pubRing.keys);
      boolean found = false;

      for(int i = 0; i != keys.size(); ++i) {
         PGPPublicKey key = (PGPPublicKey)keys.get(i);
         if (key.getKeyID() == pubKey.getKeyID()) {
            found = true;
            keys.set(i, pubKey);
         }
      }

      if (!found) {
         keys.add(pubKey);
      }

      return new PGPPublicKeyRing(keys);
   }

   public static PGPPublicKeyRing removePublicKey(PGPPublicKeyRing pubRing, PGPPublicKey pubKey) {
      List keys = new ArrayList(pubRing.keys);
      boolean found = false;

      for(int i = 0; i < keys.size(); ++i) {
         PGPPublicKey key = (PGPPublicKey)keys.get(i);
         if (key.getKeyID() == pubKey.getKeyID()) {
            found = true;
            keys.remove(i);
         }
      }

      if (!found) {
         return null;
      } else {
         return new PGPPublicKeyRing(keys);
      }
   }
}
