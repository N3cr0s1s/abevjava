package org.bouncycastle.asn1;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class DERUTF8String extends DERObject implements DERString {
   String string;

   public static DERUTF8String getInstance(Object obj) {
      if (obj != null && !(obj instanceof DERUTF8String)) {
         if (obj instanceof ASN1OctetString) {
            return new DERUTF8String(((ASN1OctetString)obj).getOctets());
         } else if (obj instanceof ASN1TaggedObject) {
            return getInstance(((ASN1TaggedObject)obj).getObject());
         } else {
            throw new IllegalArgumentException("illegal object in getInstance: " + obj.getClass().getName());
         }
      } else {
         return (DERUTF8String)obj;
      }
   }

   public static DERUTF8String getInstance(ASN1TaggedObject obj, boolean explicit) {
      return getInstance(obj.getObject());
   }

   DERUTF8String(byte[] string) {
      int i = 0;
      int length = 0;

      while(i < string.length) {
         ++length;
         if ((string[i] & 224) == 224) {
            i += 3;
         } else if ((string[i] & 192) == 192) {
            i += 2;
         } else {
            ++i;
         }
      }

      char[] cs = new char[length];
      i = 0;

      char ch;
      for(length = 0; i < string.length; cs[length++] = ch) {
         if ((string[i] & 224) == 224) {
            ch = (char)((string[i] & 31) << 12 | (string[i + 1] & 63) << 6 | string[i + 2] & 63);
            i += 3;
         } else if ((string[i] & 192) == 192) {
            ch = (char)((string[i] & 63) << 6 | string[i + 1] & 63);
            i += 2;
         } else {
            ch = (char)(string[i] & 255);
            ++i;
         }
      }

      this.string = new String(cs);
   }

   public DERUTF8String(String string) {
      this.string = string;
   }

   public String getString() {
      return this.string;
   }

   public int hashCode() {
      return this.getString().hashCode();
   }

   public boolean equals(Object o) {
      if (!(o instanceof DERUTF8String)) {
         return false;
      } else {
         DERUTF8String s = (DERUTF8String)o;
         return this.getString().equals(s.getString());
      }
   }

   void encode(DEROutputStream out) throws IOException {
      char[] c = this.string.toCharArray();
      ByteArrayOutputStream bOut = new ByteArrayOutputStream();

      for(int i = 0; i != c.length; ++i) {
         char ch = c[i];
         if (ch < 128) {
            bOut.write(ch);
         } else if (ch < 2048) {
            bOut.write(192 | ch >> 6);
            bOut.write(128 | ch & 63);
         } else {
            bOut.write(224 | ch >> 12);
            bOut.write(128 | ch >> 6 & 63);
            bOut.write(128 | ch & 63);
         }
      }

      out.writeEncoded(12, bOut.toByteArray());
   }
}
