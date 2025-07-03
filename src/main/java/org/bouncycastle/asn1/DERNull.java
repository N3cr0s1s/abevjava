package org.bouncycastle.asn1;

import java.io.IOException;

public class DERNull extends ASN1Null {
   byte[] zeroBytes = new byte[0];

   void encode(DEROutputStream out) throws IOException {
      out.writeEncoded(5, this.zeroBytes);
   }

   public boolean equals(Object o) {
      return o != null && o instanceof DERNull;
   }

   public int hashCode() {
      return 0;
   }
}
