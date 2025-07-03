package org.bouncycastle.asn1;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class DERObjectIdentifier extends DERObject {
   String identifier;

   public static DERObjectIdentifier getInstance(Object obj) {
      if (obj != null && !(obj instanceof DERObjectIdentifier)) {
         if (obj instanceof ASN1OctetString) {
            return new DERObjectIdentifier(((ASN1OctetString)obj).getOctets());
         } else if (obj instanceof ASN1TaggedObject) {
            return getInstance(((ASN1TaggedObject)obj).getObject());
         } else {
            throw new IllegalArgumentException("illegal object in getInstance: " + obj.getClass().getName());
         }
      } else {
         return (DERObjectIdentifier)obj;
      }
   }

   public static DERObjectIdentifier getInstance(ASN1TaggedObject obj, boolean explicit) {
      return getInstance(obj.getObject());
   }

   DERObjectIdentifier(byte[] bytes) {
      StringBuffer objId = new StringBuffer();
      long value = 0L;
      boolean first = true;

      for(int i = 0; i != bytes.length; ++i) {
         int b = bytes[i] & 255;
         value = value * 128L + (long)(b & 127);
         if ((b & 128) == 0) {
            if (first) {
               switch((int)value / 40) {
               case 0:
                  objId.append('0');
                  break;
               case 1:
                  objId.append('1');
                  value -= 40L;
                  break;
               default:
                  objId.append('2');
                  value -= 80L;
               }

               first = false;
            }

            objId.append('.');
            objId.append(Long.toString(value));
            value = 0L;
         }
      }

      this.identifier = objId.toString();
   }

   public DERObjectIdentifier(String identifier) {
      for(int i = identifier.length() - 1; i >= 0; --i) {
         char ch = identifier.charAt(i);
         if (('0' > ch || ch > '9') && ch != '.') {
            throw new IllegalArgumentException("string " + identifier + " not an OID");
         }
      }

      this.identifier = identifier;
   }

   public String getId() {
      return this.identifier;
   }

   private void writeField(OutputStream out, long fieldValue) throws IOException {
      if (fieldValue >= 128L) {
         if (fieldValue >= 16384L) {
            if (fieldValue >= 2097152L) {
               if (fieldValue >= 268435456L) {
                  if (fieldValue >= 34359738368L) {
                     if (fieldValue >= 4398046511104L) {
                        if (fieldValue >= 562949953421312L) {
                           if (fieldValue >= 72057594037927936L) {
                              out.write((int)(fieldValue >> 56) | 128);
                           }

                           out.write((int)(fieldValue >> 49) | 128);
                        }

                        out.write((int)(fieldValue >> 42) | 128);
                     }

                     out.write((int)(fieldValue >> 35) | 128);
                  }

                  out.write((int)(fieldValue >> 28) | 128);
               }

               out.write((int)(fieldValue >> 21) | 128);
            }

            out.write((int)(fieldValue >> 14) | 128);
         }

         out.write((int)(fieldValue >> 7) | 128);
      }

      out.write((int)fieldValue & 127);
   }

   void encode(DEROutputStream out) throws IOException {
      OIDTokenizer tok = new OIDTokenizer(this.identifier);
      ByteArrayOutputStream bOut = new ByteArrayOutputStream();
      DEROutputStream dOut = new DEROutputStream(bOut);
      this.writeField(bOut, (long)(Integer.parseInt(tok.nextToken()) * 40 + Integer.parseInt(tok.nextToken())));

      while(tok.hasMoreTokens()) {
         this.writeField(bOut, Long.parseLong(tok.nextToken()));
      }

      dOut.close();
      byte[] bytes = bOut.toByteArray();
      out.writeEncoded(6, bytes);
   }

   public int hashCode() {
      return this.identifier.hashCode();
   }

   public boolean equals(Object o) {
      return o != null && o instanceof DERObjectIdentifier ? this.identifier.equals(((DERObjectIdentifier)o).identifier) : false;
   }

   public String toString() {
      return this.getId();
   }
}
