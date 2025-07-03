package org.bouncycastle.asn1;

import java.io.IOException;

public class BERNull extends DERNull {
   void encode(DEROutputStream out) throws IOException {
      if (!(out instanceof ASN1OutputStream) && !(out instanceof BEROutputStream)) {
         super.encode(out);
      } else {
         out.write(5);
      }

   }
}
