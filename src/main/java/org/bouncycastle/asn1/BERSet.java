package org.bouncycastle.asn1;

import java.io.IOException;
import java.util.Enumeration;

public class BERSet extends DERSet {
   public BERSet() {
   }

   public BERSet(DEREncodable obj) {
      super(obj);
   }

   public BERSet(DEREncodableVector v) {
      super(v, false);
   }

   BERSet(DEREncodableVector v, boolean needsSorting) {
      super(v, needsSorting);
   }

   void encode(DEROutputStream out) throws IOException {
      if (!(out instanceof ASN1OutputStream) && !(out instanceof BEROutputStream)) {
         super.encode(out);
      } else {
         out.write(49);
         out.write(128);
         Enumeration e = this.getObjects();

         while(e.hasMoreElements()) {
            out.writeObject(e.nextElement());
         }

         out.write(0);
         out.write(0);
      }

   }
}
