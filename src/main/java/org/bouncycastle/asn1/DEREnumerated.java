package org.bouncycastle.asn1;

import java.io.IOException;
import java.math.BigInteger;

public class DEREnumerated extends DERObject {
   byte[] bytes;

   public static DEREnumerated getInstance(Object obj) {
      if (obj != null && !(obj instanceof DEREnumerated)) {
         if (obj instanceof ASN1OctetString) {
            return new DEREnumerated(((ASN1OctetString)obj).getOctets());
         } else if (obj instanceof ASN1TaggedObject) {
            return getInstance(((ASN1TaggedObject)obj).getObject());
         } else {
            throw new IllegalArgumentException("illegal object in getInstance: " + obj.getClass().getName());
         }
      } else {
         return (DEREnumerated)obj;
      }
   }

   public static DEREnumerated getInstance(ASN1TaggedObject obj, boolean explicit) {
      return getInstance(obj.getObject());
   }

   public DEREnumerated(int value) {
      this.bytes = BigInteger.valueOf((long)value).toByteArray();
   }

   public DEREnumerated(BigInteger value) {
      this.bytes = value.toByteArray();
   }

   public DEREnumerated(byte[] bytes) {
      this.bytes = bytes;
   }

   public BigInteger getValue() {
      return new BigInteger(this.bytes);
   }

   void encode(DEROutputStream out) throws IOException {
      out.writeEncoded(10, this.bytes);
   }

   public boolean equals(Object o) {
      if (o != null && o instanceof DEREnumerated) {
         DEREnumerated other = (DEREnumerated)o;
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

   public int hashCode() {
      return this.getValue().hashCode();
   }
}
