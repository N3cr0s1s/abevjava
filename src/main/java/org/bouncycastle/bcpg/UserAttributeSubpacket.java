package org.bouncycastle.bcpg;

import java.io.IOException;
import java.io.OutputStream;

public class UserAttributeSubpacket {
   int type;
   protected byte[] data;

   protected UserAttributeSubpacket(int type, byte[] data) {
      this.type = type;
      this.data = data;
   }

   public int getType() {
      return this.type;
   }

   public byte[] getData() {
      return this.data;
   }

   public void encode(OutputStream out) throws IOException {
      int bodyLen = this.data.length + 1;
      if (bodyLen < 192) {
         out.write((byte)bodyLen);
      } else if (bodyLen <= 8383) {
         bodyLen -= 192;
         out.write((byte)((bodyLen >> 8 & 255) + 192));
         out.write((byte)bodyLen);
      } else {
         out.write(255);
         out.write((byte)(bodyLen >> 24));
         out.write((byte)(bodyLen >> 16));
         out.write((byte)(bodyLen >> 8));
         out.write((byte)bodyLen);
      }

      out.write(this.type);
      out.write(this.data);
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof UserAttributeSubpacket)) {
         return false;
      } else {
         UserAttributeSubpacket other = (UserAttributeSubpacket)o;
         if (other.type != this.type) {
            return false;
         } else if (other.data.length != this.data.length) {
            return false;
         } else {
            for(int i = 0; i != this.data.length; ++i) {
               if (this.data[i] != other.data[i]) {
                  return false;
               }
            }

            return true;
         }
      }
   }

   public int hashCode() {
      int code = this.type;

      for(int i = 0; i != this.data.length; ++i) {
         code ^= (this.data[i] & 255) << 8 * (i % 4);
      }

      return code;
   }
}
