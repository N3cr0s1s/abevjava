package org.bouncycastle.asn1;

import java.io.IOException;

public class DERGeneralString extends DERObject implements DERString {
   private String string;

   public static DERGeneralString getInstance(Object obj) {
      if (obj != null && !(obj instanceof DERGeneralString)) {
         if (obj instanceof ASN1OctetString) {
            return new DERGeneralString(((ASN1OctetString)obj).getOctets());
         } else if (obj instanceof ASN1TaggedObject) {
            return getInstance(((ASN1TaggedObject)obj).getObject());
         } else {
            throw new IllegalArgumentException("illegal object in getInstance: " + obj.getClass().getName());
         }
      } else {
         return (DERGeneralString)obj;
      }
   }

   public static DERGeneralString getInstance(ASN1TaggedObject obj, boolean explicit) {
      return getInstance(obj.getObject());
   }

   public DERGeneralString(byte[] string) {
      char[] cs = new char[string.length];

      for(int i = 0; i != cs.length; ++i) {
         cs[i] = (char)(string[i] & 255);
      }

      this.string = new String(cs);
   }

   public DERGeneralString(String string) {
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
      out.writeEncoded(27, this.getOctets());
   }

   public int hashCode() {
      return this.getString().hashCode();
   }

   public boolean equals(Object o) {
      if (!(o instanceof DERGeneralString)) {
         return false;
      } else {
         DERGeneralString s = (DERGeneralString)o;
         return this.getString().equals(s.getString());
      }
   }
}
