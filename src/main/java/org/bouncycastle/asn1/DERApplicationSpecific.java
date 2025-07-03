package org.bouncycastle.asn1;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class DERApplicationSpecific extends DERObject {
   private int tag;
   private byte[] octets;

   public DERApplicationSpecific(int tag, byte[] octets) {
      this.tag = tag;
      this.octets = octets;
   }

   public DERApplicationSpecific(int tag, DEREncodable object) throws IOException {
      this.tag = tag | 32;
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      DEROutputStream dos = new DEROutputStream(baos);
      dos.writeObject(object);
      this.octets = baos.toByteArray();
   }

   public boolean isConstructed() {
      return (this.tag & 32) != 0;
   }

   public byte[] getContents() {
      return this.octets;
   }

   public int getApplicationTag() {
      return this.tag & 31;
   }

   public DERObject getObject() throws IOException {
      return (new ASN1InputStream(new ByteArrayInputStream(this.getContents()))).readObject();
   }

   void encode(DEROutputStream out) throws IOException {
      out.writeEncoded(64 | this.tag, this.octets);
   }

   public boolean equals(Object o) {
      if (o != null && o instanceof DERApplicationSpecific) {
         DERApplicationSpecific other = (DERApplicationSpecific)o;
         if (this.tag != other.tag) {
            return false;
         } else if (this.octets.length != other.octets.length) {
            return false;
         } else {
            for(int i = 0; i < this.octets.length; ++i) {
               if (this.octets[i] != other.octets[i]) {
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
      byte[] b = this.getContents();
      int value = 0;

      for(int i = 0; i != b.length; ++i) {
         value ^= (b[i] & 255) << i % 4;
      }

      return value ^ this.getApplicationTag();
   }
}
