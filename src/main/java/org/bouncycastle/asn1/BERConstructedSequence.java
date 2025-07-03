package org.bouncycastle.asn1;

import java.io.IOException;
import java.util.Enumeration;

/** @deprecated */
public class BERConstructedSequence extends DERConstructedSequence {
   void encode(DEROutputStream out) throws IOException {
      if (!(out instanceof ASN1OutputStream) && !(out instanceof BEROutputStream)) {
         super.encode(out);
      } else {
         out.write(48);
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
