package org.bouncycastle.asn1;

import java.io.IOException;

public class DERUnknownTag extends DERObject {
   int tag;
   byte[] data;

   public DERUnknownTag(int tag, byte[] data) {
      this.tag = tag;
      this.data = data;
   }

   public int getTag() {
      return this.tag;
   }

   public byte[] getData() {
      return this.data;
   }

   void encode(DEROutputStream out) throws IOException {
      out.writeEncoded(this.tag, this.data);
   }

   public boolean equals(Object o) {
      if (o != null && o instanceof DERUnknownTag) {
         DERUnknownTag other = (DERUnknownTag)o;
         if (this.tag != other.tag) {
            return false;
         } else if (this.data.length != other.data.length) {
            return false;
         } else {
            for(int i = 0; i < this.data.length; ++i) {
               if (this.data[i] != other.data[i]) {
                  return false;
               }
            }

            return true;
         }
      } else {
         return false;
      }
   }

   public int hashCode() {
      byte[] b = this.getData();
      int value = 0;

      for(int i = 0; i != b.length; ++i) {
         value ^= (b[i] & 255) << i % 4;
      }

      return value ^ this.getTag();
   }
}
