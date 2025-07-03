package org.bouncycastle.asn1;

import java.io.IOException;

public class DERNumericString extends DERObject implements DERString {
   String string;

   public static DERNumericString getInstance(Object obj) {
      if (obj != null && !(obj instanceof DERNumericString)) {
         if (obj instanceof ASN1OctetString) {
            return new DERNumericString(((ASN1OctetString)obj).getOctets());
         } else if (obj instanceof ASN1TaggedObject) {
            return getInstance(((ASN1TaggedObject)obj).getObject());
         } else {
            throw new IllegalArgumentException("illegal object in getInstance: " + obj.getClass().getName());
         }
      } else {
         return (DERNumericString)obj;
      }
   }

   public static DERNumericString getInstance(ASN1TaggedObject obj, boolean explicit) {
      return getInstance(obj.getObject());
   }

   public DERNumericString(byte[] string) {
      char[] cs = new char[string.length];

      for(int i = 0; i != cs.length; ++i) {
         cs[i] = (char)(string[i] & 255);
      }

      this.string = new String(cs);
   }

   public DERNumericString(String string) {
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
      out.writeEncoded(18, this.getOctets());
   }

   public int hashCode() {
      return this.getString().hashCode();
   }

   public boolean equals(Object o) {
      if (!(o instanceof DERNumericString)) {
         return false;
      } else {
         DERNumericString s = (DERNumericString)o;
         return this.getString().equals(s.getString());
      }
   }
}
