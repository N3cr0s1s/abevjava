package org.bouncycastle.asn1;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Enumeration;

public class DERSequence extends ASN1Sequence {
   public DERSequence() {
   }

   public DERSequence(DEREncodable obj) {
      this.addObject(obj);
   }

   public DERSequence(DEREncodableVector v) {
      for(int i = 0; i != v.size(); ++i) {
         this.addObject(v.get(i));
      }

   }

   public DERSequence(ASN1Encodable[] a) {
      for(int i = 0; i != a.length; ++i) {
         this.addObject(a[i]);
      }

   }

   void encode(DEROutputStream out) throws IOException {
      ByteArrayOutputStream bOut = new ByteArrayOutputStream();
      DEROutputStream dOut = new DEROutputStream(bOut);
      Enumeration e = this.getObjects();

      while(e.hasMoreElements()) {
         Object obj = e.nextElement();
         dOut.writeObject(obj);
      }

      dOut.close();
      byte[] bytes = bOut.toByteArray();
      out.writeEncoded(48, bytes);
   }
}
