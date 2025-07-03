package org.bouncycastle.asn1;

import java.io.IOException;
import java.io.OutputStream;

public class BEROutputStream extends DEROutputStream {
   public BEROutputStream(OutputStream os) {
      super(os);
   }

   public void writeObject(Object obj) throws IOException {
      if (obj == null) {
         this.writeNull();
      } else if (obj instanceof DERObject) {
         ((DERObject)obj).encode(this);
      } else {
         if (!(obj instanceof DEREncodable)) {
            throw new IOException("object not BEREncodable");
         }

         ((DEREncodable)obj).getDERObject().encode(this);
      }

   }
}
