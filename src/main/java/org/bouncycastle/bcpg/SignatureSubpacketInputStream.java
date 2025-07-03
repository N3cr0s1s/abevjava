package org.bouncycastle.bcpg;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import org.bouncycastle.bcpg.sig.Exportable;
import org.bouncycastle.bcpg.sig.IssuerKeyID;
import org.bouncycastle.bcpg.sig.KeyExpirationTime;
import org.bouncycastle.bcpg.sig.KeyFlags;
import org.bouncycastle.bcpg.sig.PreferredAlgorithms;
import org.bouncycastle.bcpg.sig.PrimaryUserID;
import org.bouncycastle.bcpg.sig.Revocable;
import org.bouncycastle.bcpg.sig.SignatureCreationTime;
import org.bouncycastle.bcpg.sig.SignatureExpirationTime;
import org.bouncycastle.bcpg.sig.SignerUserID;
import org.bouncycastle.bcpg.sig.TrustSignature;

public class SignatureSubpacketInputStream extends InputStream implements SignatureSubpacketTags {
   InputStream in;

   public SignatureSubpacketInputStream(InputStream in) {
      this.in = in;
   }

   public int available() throws IOException {
      return this.in.available();
   }

   public int read() throws IOException {
      return this.in.read();
   }

   private void readFully(byte[] buf, int off, int len) throws IOException {
      int l;
      if (len > 0) {
         l = this.read();
         if (l < 0) {
            throw new EOFException();
         }

         buf[off] = (byte)l;
         ++off;
         --len;
      }

      while(len > 0) {
         l = this.in.read(buf, off, len);
         if (l < 0) {
            throw new EOFException();
         }

         off += l;
         len -= l;
      }

   }

   public SignatureSubpacket readPacket() throws IOException {
      int l = this.read();
      int bodyLen = 0;
      if (l < 0) {
         return null;
      } else {
         if (l < 192) {
            bodyLen = l;
         } else if (l < 223) {
            bodyLen = (l - 192 << 8) + this.in.read() + 192;
         } else if (l == 255) {
            bodyLen = this.in.read() << 24 | this.in.read() << 16 | this.in.read() << 8 | this.in.read();
         }

         int tag = this.in.read();
         if (tag < 0) {
            throw new EOFException("unexpected EOF reading signature sub packet");
         } else {
            byte[] data = new byte[bodyLen - 1];
            this.readFully(data, 0, data.length);
            boolean isCritical = (tag & 128) != 0;
            int type = tag & 127;
            switch(type) {
            case 2:
               return new SignatureCreationTime(isCritical, data);
            case 3:
               return new SignatureExpirationTime(isCritical, data);
            case 4:
               return new Exportable(isCritical, data);
            case 5:
               return new TrustSignature(isCritical, data);
            case 6:
            case 8:
            case 10:
            case 12:
            case 13:
            case 14:
            case 15:
            case 17:
            case 18:
            case 19:
            case 20:
            case 23:
            case 24:
            case 26:
            default:
               return new SignatureSubpacket(type, isCritical, data);
            case 7:
               return new Revocable(isCritical, data);
            case 9:
               return new KeyExpirationTime(isCritical, data);
            case 11:
            case 21:
            case 22:
               return new PreferredAlgorithms(type, isCritical, data);
            case 16:
               return new IssuerKeyID(isCritical, data);
            case 25:
               return new PrimaryUserID(isCritical, data);
            case 27:
               return new KeyFlags(isCritical, data);
            case 28:
               return new SignerUserID(isCritical, data);
            }
         }
      }
   }
}
