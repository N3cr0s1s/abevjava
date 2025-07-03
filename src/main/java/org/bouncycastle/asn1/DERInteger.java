package org.bouncycastle.asn1;

import java.io.IOException;
import java.math.BigInteger;

public class DERInteger extends DERObject {
   byte[] bytes;

   public static DERInteger getInstance(Object obj) {
      if (obj != null && !(obj instanceof DERInteger)) {
         if (obj instanceof ASN1OctetString) {
            return new DERInteger(((ASN1OctetString)obj).getOctets());
         } else if (obj instanceof ASN1TaggedObject) {
            return getInstance(((ASN1TaggedObject)obj).getObject());
         } else {
            throw new IllegalArgumentException("illegal object in getInstance: " + obj.getClass().getName());
         }
      } else {
         return (DERInteger)obj;
      }
   }

   public static DERInteger getInstance(ASN1TaggedObject obj, boolean explicit) {
      return getInstance(obj.getObject());
   }

   public DERInteger(int value) {
      this.bytes = BigInteger.valueOf((long)value).toByteArray();
   }

   public DERInteger(BigInteger value) {
      this.bytes = value.toByteArray();
   }

   public DERInteger(byte[] bytes) {
      this.bytes = bytes;
   }

   public BigInteger getValue() {
      return new BigInteger(this.bytes);
   }

   public BigInteger getPositiveValue() {
      return new BigInteger(1, this.bytes);
   }

   void encode(DEROutputStream out) throws IOException {
      out.writeEncoded(2, this.bytes);
   }

   public int hashCode() {
      int value = 0;

      for(int i = 0; i != this.bytes.length; ++i) {
         value ^= (this.bytes[i] & 255) << i % 4;
      }

      return value;
   }

   public boolean equals(Object o) {
      if (o != null && o instanceof DERInteger) {
         DERInteger other = (DERInteger)o;
         if (this.bytes.length != other.bytes.length) {
            return false;
         } else {
            for(int i = 0; i != this.bytes.length; ++i) {
               if (this.bytes[i] != other.bytes[i]) {
                  return false;
               }
            }

            return true;
         }
      } else {
         return false;
      }
   }
}
