package org.bouncycastle.bcpg;

import java.io.IOException;
import java.io.OutputStream;

public class SignatureSubpacket {
   int type;
   boolean critical;
   protected byte[] data;

   protected SignatureSubpacket(int type, boolean critical, byte[] data) {
      this.type = type;
      this.critical = critical;
      this.data = data;
   }

   public int getType() {
      return this.type;
   }

   public boolean isCritical() {
      return this.critical;
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

      if (this.critical) {
         out.write(128 & this.type);
      } else {
         out.write(this.type);
      }

      out.write(this.data);
   }
}
