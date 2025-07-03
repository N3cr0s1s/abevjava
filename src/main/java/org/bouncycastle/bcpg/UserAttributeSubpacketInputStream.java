package org.bouncycastle.bcpg;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import org.bouncycastle.bcpg.attr.ImageAttribute;

public class UserAttributeSubpacketInputStream extends InputStream implements UserAttributeSubpacketTags {
   InputStream in;

   public UserAttributeSubpacketInputStream(InputStream in) {
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

   public UserAttributeSubpacket readPacket() throws IOException {
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
            throw new EOFException("unexpected EOF reading user attribute sub packet");
         } else {
            byte[] data = new byte[bodyLen - 1];
            this.readFully(data, 0, data.length);
            switch(tag) {
            case 1:
               return new ImageAttribute(data);
            default:
               return new UserAttributeSubpacket(tag, data);
            }
         }
      }
   }
}
