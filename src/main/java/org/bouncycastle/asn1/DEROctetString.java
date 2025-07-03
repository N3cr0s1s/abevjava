package org.bouncycastle.asn1;

import java.io.IOException;

public class DEROctetString extends ASN1OctetString {
   public DEROctetString(byte[] string) {
      super(string);
   }

   public DEROctetString(DEREncodable obj) {
      super(obj);
   }

   void encode(DEROutputStream out) throws IOException {
      out.writeEncoded(4, this.string);
   }
}
