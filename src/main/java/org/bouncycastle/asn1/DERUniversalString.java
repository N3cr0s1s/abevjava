package org.bouncycastle.asn1;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class DERUniversalString extends DERObject implements DERString {
   private static final char[] table = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
   private byte[] string;

   public static DERUniversalString getInstance(Object obj) {
      if (obj != null && !(obj instanceof DERUniversalString)) {
         if (obj instanceof ASN1OctetString) {
            return new DERUniversalString(((ASN1OctetString)obj).getOctets());
         } else {
            throw new IllegalArgumentException("illegal object in getInstance: " + obj.getClass().getName());
         }
      } else {
         return (DERUniversalString)obj;
      }
   }

   public static DERUniversalString getInstance(ASN1TaggedObject obj, boolean explicit) {
      return getInstance(obj.getObject());
   }

   public DERUniversalString(byte[] string) {
      this.string = string;
   }

   public String getString() {
      StringBuffer buf = new StringBuffer("#");
      ByteArrayOutputStream bOut = new ByteArrayOutputStream();
      ASN1OutputStream aOut = new ASN1OutputStream(bOut);

      try {
         aOut.writeObject(this);
      } catch (IOException var6) {
         throw new RuntimeException("internal error encoding BitString");
      }

      byte[] string = bOut.toByteArray();

      for(int i = 0; i != string.length; ++i) {
         buf.append(table[(string[i] >>> 4) % 15]);
         buf.append(table[string[i] & 15]);
      }

      return buf.toString();
   }

   public byte[] getOctets() {
      return this.string;
   }

   void encode(DEROutputStream out) throws IOException {
      out.writeEncoded(28, this.getOctets());
   }

   public boolean equals(Object o) {
      return o != null && o instanceof DERUniversalString ? this.getString().equals(((DERUniversalString)o).getString()) : false;
   }

   public int hashCode() {
      return this.getString().hashCode();
   }
}
