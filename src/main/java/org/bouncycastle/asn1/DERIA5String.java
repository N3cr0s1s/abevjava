package org.bouncycastle.asn1;

import java.io.IOException;

public class DERIA5String extends DERObject implements DERString {
   String string;

   public static DERIA5String getInstance(Object obj) {
      if (obj != null && !(obj instanceof DERIA5String)) {
         if (obj instanceof ASN1OctetString) {
            return new DERIA5String(((ASN1OctetString)obj).getOctets());
         } else if (obj instanceof ASN1TaggedObject) {
            return getInstance(((ASN1TaggedObject)obj).getObject());
         } else {
            throw new IllegalArgumentException("illegal object in getInstance: " + obj.getClass().getName());
         }
      } else {
         return (DERIA5String)obj;
      }
   }

   public static DERIA5String getInstance(ASN1TaggedObject obj, boolean explicit) {
      return getInstance(obj.getObject());
   }

   public DERIA5String(byte[] string) {
      char[] cs = new char[string.length];

      for(int i = 0; i != cs.length; ++i) {
         cs[i] = (char)(string[i] & 255);
      }

      this.string = new String(cs);
   }

   public DERIA5String(String string) {
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
      out.writeEncoded(22, this.getOctets());
   }

   public int hashCode() {
      return this.getString().hashCode();
   }

   public boolean equals(Object o) {
      if (!(o instanceof DERIA5String)) {
         return false;
      } else {
         DERIA5String s = (DERIA5String)o;
         return this.getString().equals(s.getString());
      }
   }
}
