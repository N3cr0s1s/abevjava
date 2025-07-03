package org.bouncycastle.openpgp;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import org.bouncycastle.bcpg.BCPGInputStream;

public class PGPObjectFactory {
   BCPGInputStream in;

   public PGPObjectFactory(InputStream in) {
      this.in = new BCPGInputStream(in);
   }

   public PGPObjectFactory(byte[] bytes) {
      this((InputStream)(new ByteArrayInputStream(bytes)));
   }

   public Object nextObject() throws IOException {
      ArrayList l;
      switch(this.in.nextPacketTag()) {
      case -1:
         return null;
      case 1:
      case 3:
         return new PGPEncryptedDataList(this.in);
      case 2:
         l = new ArrayList();

         while(this.in.nextPacketTag() == 2) {
            try {
               l.add(new PGPSignature(this.in));
            } catch (PGPException var5) {
               throw new IOException("can't create signature object: " + var5);
            }
         }

         return new PGPSignatureList((PGPSignature[])l.toArray(new PGPSignature[l.size()]));
      case 4:
         l = new ArrayList();

         while(this.in.nextPacketTag() == 4) {
            try {
               l.add(new PGPOnePassSignature(this.in));
            } catch (PGPException var4) {
               throw new IOException("can't create signature object: " + var4);
            }
         }

         return new PGPOnePassSignatureList((PGPOnePassSignature[])l.toArray(new PGPOnePassSignature[l.size()]));
      case 5:
         try {
            return new PGPSecretKeyRing(this.in);
         } catch (PGPException var3) {
            throw new IOException("can't create secret key object: " + var3);
         }
      case 6:
         return new PGPPublicKeyRing(this.in);
      case 8:
         return new PGPCompressedData(this.in);
      case 10:
         return new PGPMarker(this.in);
      case 11:
         return new PGPLiteralData(this.in);
      case 60:
      case 61:
      case 62:
      case 63:
         return this.in.readPacket();
      default:
         throw new IOException("unknown object in stream " + this.in.nextPacketTag());
      }
   }
}
