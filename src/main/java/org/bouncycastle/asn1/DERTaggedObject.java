package org.bouncycastle.asn1;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class DERTaggedObject extends ASN1TaggedObject {
   public DERTaggedObject(int tagNo, DEREncodable obj) {
      super(tagNo, obj);
   }

   public DERTaggedObject(boolean explicit, int tagNo, DEREncodable obj) {
      super(explicit, tagNo, obj);
   }

   public DERTaggedObject(int tagNo) {
      super(false, tagNo, new DERSequence());
   }

   void encode(DEROutputStream out) throws IOException {
      if (!this.empty) {
         ByteArrayOutputStream bOut = new ByteArrayOutputStream();
         DEROutputStream dOut = new DEROutputStream(bOut);
         dOut.writeObject(this.obj);
         dOut.close();
         byte[] bytes = bOut.toByteArray();
         if (this.explicit) {
            out.writeEncoded(160 | this.tagNo, bytes);
         } else {
            if ((bytes[0] & 32) != 0) {
               bytes[0] = (byte)(160 | this.tagNo);
            } else {
               bytes[0] = (byte)(128 | this.tagNo);
            }

            out.write(bytes);
         }
      } else {
         out.writeEncoded(160 | this.tagNo, new byte[0]);
      }

   }
}
