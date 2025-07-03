package org.bouncycastle.asn1;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;

public abstract class ASN1OctetString extends DERObject {
   byte[] string;

   public static ASN1OctetString getInstance(ASN1TaggedObject obj, boolean explicit) {
      return getInstance(obj.getObject());
   }

   public static ASN1OctetString getInstance(Object obj) {
      if (obj != null && !(obj instanceof ASN1OctetString)) {
         if (obj instanceof ASN1TaggedObject) {
            return getInstance(((ASN1TaggedObject)obj).getObject());
         } else if (!(obj instanceof ASN1Sequence)) {
            throw new IllegalArgumentException("illegal object in getInstance: " + obj.getClass().getName());
         } else {
            Vector v = new Vector();
            Enumeration e = ((ASN1Sequence)obj).getObjects();

            while(e.hasMoreElements()) {
               v.addElement(e.nextElement());
            }

            return new BERConstructedOctetString(v);
         }
      } else {
         return (ASN1OctetString)obj;
      }
   }

   public ASN1OctetString(byte[] string) {
      this.string = string;
   }

   public ASN1OctetString(DEREncodable obj) {
      try {
         ByteArrayOutputStream bOut = new ByteArrayOutputStream();
         DEROutputStream dOut = new DEROutputStream(bOut);
         dOut.writeObject(obj);
         dOut.close();
         this.string = bOut.toByteArray();
      } catch (IOException var4) {
         throw new IllegalArgumentException("Error processing object : " + var4.toString());
      }
   }

   public byte[] getOctets() {
      return this.string;
   }

   public int hashCode() {
      byte[] b = this.getOctets();
      int value = 0;

      for(int i = 0; i != b.length; ++i) {
         value ^= (b[i] & 255) << i % 4;
      }

      return value;
   }

   public boolean equals(Object o) {
      if (o != null && o instanceof DEROctetString) {
         DEROctetString other = (DEROctetString)o;
         byte[] b1 = other.getOctets();
         byte[] b2 = this.getOctets();
         if (b1.length != b2.length) {
            return false;
         } else {
            for(int i = 0; i != b1.length; ++i) {
               if (b1[i] != b2[i]) {
                  return false;
               }
            }

            return true;
         }
      } else {
         return false;
      }
   }

   abstract void encode(DEROutputStream var1) throws IOException;
}
