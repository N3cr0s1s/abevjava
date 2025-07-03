package org.bouncycastle.bcpg;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

public class BCPGInputStream extends InputStream implements PacketTags {
   InputStream in;
   boolean next = false;
   int nextB;

   public BCPGInputStream(InputStream in) {
      this.in = in;
   }

   public int available() throws IOException {
      return this.in.available();
   }

   public int read() throws IOException {
      if (this.next) {
         this.next = false;
         return this.nextB;
      } else {
         return this.in.read();
      }
   }

   public void readFully(byte[] buf, int off, int len) throws IOException {
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

   public void readFully(byte[] buf) throws IOException {
      this.readFully(buf, 0, buf.length);
   }

   public int nextPacketTag() throws IOException {
      if (!this.next) {
         try {
            this.nextB = this.in.read();
         } catch (EOFException var2) {
            this.nextB = -1;
         }
      }

      this.next = true;
      if (this.nextB >= 0) {
         return (this.nextB & 64) != 0 ? this.nextB & 63 : (this.nextB & 63) >> 2;
      } else {
         return this.nextB;
      }
   }

   public Packet readPacket() throws IOException {
      int hdr = this.read();
      if (hdr < 0) {
         return null;
      } else if ((hdr & 128) == 0) {
         throw new IOException("invalid header encountered");
      } else {
         boolean newPacket = (hdr & 64) != 0;
         int bodyLen = 0;
         boolean partial = false;
         int l;
         int tag = 0;
         if (newPacket) {
            tag = hdr & 63;
            l = this.read();
            if (l < 192) {
               bodyLen = l;
            } else if (l <= 223) {
               int b = this.in.read();
               bodyLen = (l - 192 << 8) + b + 192;
            } else if (l == 255) {
               bodyLen = this.in.read() << 24 | this.in.read() << 16 | this.in.read() << 8 | this.in.read();
            } else {
               partial = true;
               bodyLen = 1 << (l & 31);
            }
         } else {
            l = hdr & 3;
            tag = (hdr & 63) >> 2;
            switch(l) {
            case 0:
               bodyLen = this.read();
               break;
            case 1:
               bodyLen = this.read() << 8 | this.read();
               break;
            case 2:
               bodyLen = this.read() << 24 | this.read() << 16 | this.read() << 8 | this.read();
               break;
            case 3:
               partial = true;
               break;
            default:
               throw new IOException("unknown length type encountered");
            }
         }

         BCPGInputStream objStream;
         if (bodyLen == 0 && partial) {
            objStream = this;
         } else {
            objStream = new BCPGInputStream(new BCPGInputStream.PartialInputStream(this, partial, bodyLen));
         }

         switch(tag) {
         case 0:
            return new InputStreamPacket(objStream);
         case 1:
            return new PublicKeyEncSessionPacket(objStream);
         case 2:
            return new SignaturePacket(objStream);
         case 3:
            return new SymmetricKeyEncSessionPacket(objStream);
         case 4:
            return new OnePassSignaturePacket(objStream);
         case 5:
            return new SecretKeyPacket(objStream);
         case 6:
            return new PublicKeyPacket(objStream);
         case 7:
            return new SecretSubkeyPacket(objStream);
         case 8:
            return new CompressedDataPacket(objStream);
         case 9:
            return new SymmetricEncDataPacket(objStream);
         case 10:
            return new MarkerPacket(objStream);
         case 11:
            return new LiteralDataPacket(objStream);
         case 12:
            return new TrustPacket(objStream);
         case 13:
            return new UserIDPacket(objStream);
         case 14:
            return new PublicSubkeyPacket(objStream);
         case 17:
            return new UserAttributePacket(objStream);
         case 18:
            return new SymmetricEncIntegrityPacket(objStream);
         case 19:
            return new ModDetectionCodePacket(objStream);
         case 60:
         case 61:
         case 62:
         case 63:
            return new ExperimentalPacket(tag, objStream);
         default:
            throw new IOException("unknown packet type encountered: " + tag);
         }
      }
   }

   public void close() throws IOException {
      this.in.close();
   }

   private static class PartialInputStream extends InputStream {
      private BCPGInputStream in;
      private boolean partial;
      private int dataLength;

      PartialInputStream(BCPGInputStream in, boolean partial, int dataLength) {
         this.in = in;
         this.partial = partial;
         this.dataLength = dataLength;
      }

      public int available() throws IOException {
         int avail = this.in.available();
         if (avail <= this.dataLength) {
            return avail;
         } else {
            return this.partial && this.dataLength == 0 ? 1 : this.dataLength;
         }
      }

      public int read() throws IOException {
         if (this.dataLength > 0) {
            --this.dataLength;
            return this.in.read();
         } else if (this.partial) {
            int l = this.in.read();
            if (l < 0) {
               return -1;
            } else {
               this.partial = false;
               if (l < 192) {
                  this.dataLength = l;
               } else if (l <= 223) {
                  this.dataLength = (l - 192 << 8) + this.in.read() + 192;
               } else if (l == 255) {
                  this.dataLength = this.in.read() << 24 | this.in.read() << 16 | this.in.read() << 8 | this.in.read();
               } else {
                  this.partial = true;
                  this.dataLength = 1 << (l & 31);
               }

               return this.read();
            }
         } else {
            return -1;
         }
      }
   }
}
