package org.bouncycastle.asn1;

import java.io.IOException;

public class DERBoolean extends DERObject {
   byte value;
   public static final DERBoolean FALSE = new DERBoolean(false);
   public static final DERBoolean TRUE = new DERBoolean(true);

   public static DERBoolean getInstance(Object obj) {
      if (obj != null && !(obj instanceof DERBoolean)) {
         if (obj instanceof ASN1OctetString) {
            return new DERBoolean(((ASN1OctetString)obj).getOctets());
         } else if (obj instanceof ASN1TaggedObject) {
            return getInstance(((ASN1TaggedObject)obj).getObject());
         } else {
            throw new IllegalArgumentException("illegal object in getInstance: " + obj.getClass().getName());
         }
      } else {
         return (DERBoolean)obj;
      }
   }

   public static DERBoolean getInstance(boolean value) {
      return value ? TRUE : FALSE;
   }

   public static DERBoolean getInstance(ASN1TaggedObject obj, boolean explicit) {
      return getInstance(obj.getObject());
   }

   public DERBoolean(byte[] value) {
      this.value = value[0];
   }

   public DERBoolean(boolean value) {
      this.value = (byte)(value ? -1 : 0);
   }

   public boolean isTrue() {
      return this.value != 0;
   }

   void encode(DEROutputStream out) throws IOException {
      byte[] bytes = new byte[]{this.value};
      out.writeEncoded(1, bytes);
   }

   public boolean equals(Object o) {
      if (o != null && o instanceof DERBoolean) {
         return this.value == ((DERBoolean)o).value;
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.value;
   }
}
