package org.bouncycastle.asn1;

import java.io.IOException;

public abstract class ASN1Null extends DERObject {
   public int hashCode() {
      return 0;
   }

   public boolean equals(Object o) {
      return o != null && o instanceof ASN1Null;
   }

   abstract void encode(DEROutputStream var1) throws IOException;
}
