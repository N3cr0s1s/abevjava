package org.bouncycastle.asn1;

import java.io.IOException;

public class DERVisibleString extends DERObject implements DERString {
   String string;

   public static DERVisibleString getInstance(Object obj) {
      if (obj != null && !(obj instanceof DERVisibleString)) {
         if (obj instanceof ASN1OctetString) {
            return new DERVisibleString(((ASN1OctetString)obj).getOctets());
         } else if (obj instanceof ASN1TaggedObject) {
            return getInstance(((ASN1TaggedObject)obj).getObject());
         } else {
            throw new IllegalArgumentException("illegal object in getInstance: " + obj.getClass().getName());
         }
      } else {
         return (DERVisibleString)obj;
      }
   }

   public static DERVisibleString getInstance(ASN1TaggedObject obj, boolean explicit) {
      return getInstance(obj.getObject());
   }

   public DERVisibleString(byte[] string) {
      char[] cs = new char[string.length];

      for(int i = 0; i != cs.length; ++i) {
         cs[i] = (char)(string[i] & 255);
      }

      this.string = new String(cs);
   }

   public DERVisibleString(String string) {
      this.string = string;
   }

   public String getString() {
      return this.string;
   }

   public byte[] getOctets() {
      char[] cs = this.string.toCharArray();
      byte[] bs = new byte[cs.length];

      for(int i = 0; i != cs.length; ++i) {
         bs[i] = (byte)cs[i];
      }

      return bs;
   }

   void encode(DEROutputStream out) throws IOException {
      out.writeEncoded(26, this.getOctets());
   }

   public boolean equals(Object o) {
      return o != null && o instanceof DERVisibleString ? this.getString().equals(((DERVisibleString)o).getString()) : false;
   }

   public int hashCode() {
      return this.getString().hashCode();
   }
}
