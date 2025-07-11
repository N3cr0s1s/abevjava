package org.bouncycastle.asn1;

import java.io.IOException;
import java.io.OutputStream;

public class ASN1OutputStream extends DEROutputStream {
   public ASN1OutputStream(OutputStream os) {
      super(os);
   }

   public void writeObject(Object obj) throws IOException {
      if (obj == null) {
         this.writeNull();
      } else if (obj instanceof DERObject) {
         ((DERObject)obj).encode(this);
      } else {
         if (!(obj instanceof DEREncodable)) {
            throw new IOException("object not ASN1Encodable");
         }

         ((DEREncodable)obj).getDERObject().encode(this);
      }

   }
}
