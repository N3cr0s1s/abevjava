package org.bouncycastle.asn1;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Enumeration;

/** @deprecated */
public class DERConstructedSequence extends ASN1Sequence {
   public void addObject(DEREncodable obj) {
      super.addObject(obj);
   }

   public int getSize() {
      return this.size();
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
