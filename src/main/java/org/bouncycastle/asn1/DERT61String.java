package org.bouncycastle.asn1;

import java.io.IOException;

public class DERT61String extends DERObject implements DERString {
   String string;

   public static DERT61String getInstance(Object obj) {
      if (obj != null && !(obj instanceof DERT61String)) {
         if (obj instanceof ASN1OctetString) {
            return new DERT61String(((ASN1OctetString)obj).getOctets());
         } else if (obj instanceof ASN1TaggedObject) {
            return getInstance(((ASN1TaggedObject)obj).getObject());
         } else {
            throw new IllegalArgumentException("illegal object in getInstance: " + obj.getClass().getName());
         }
      } else {
         return (DERT61String)obj;
      }
   }

   public static DERT61String getInstance(ASN1TaggedObject obj, boolean explicit) {
      return getInstance(obj.getObject());
   }

   public DERT61String(byte[] string) {
      char[] cs = new char[string.length];

      for(int i = 0; i != cs.length; ++i) {
         cs[i] = (char)(string[i] & 255);
      }

      this.string = new String(cs);
   }

   public DERT61String(String string) {
      this.string = string;
   }

   public String getString() {
      return this.string;
   }

   void encode(DEROutputStream out) throws IOException {
      out.writeEncoded(20, this.getOctets());
   }

   public byte[] getOctets() {
      char[] cs = this.string.toCharArray();
      byte[] bs = new byte[cs.length];

      for(int i = 0; i != cs.length; ++i) {
         bs[i] = (byte)cs[i];
      }

      return bs;
   }

   public boolean equals(Object o) {
      return o != null && o instanceof DERT61String ? this.getString().equals(((DERT61String)o).getString()) : false;
   }

   public int hashCode() {
      return this.getString().hashCode();
   }
}
