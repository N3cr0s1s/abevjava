package org.bouncycastle.asn1;

import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

/** @deprecated */
public class BERInputStream extends DERInputStream {
   private DERObject END_OF_STREAM = new DERObject() {
      void encode(DEROutputStream out) throws IOException {
         throw new IOException("Eeek!");
      }

      public int hashCode() {
         return 0;
      }

      public boolean equals(Object o) {
         return o == this;
      }
   };

   public BERInputStream(InputStream is) {
      super(is);
   }

   private byte[] readIndefiniteLengthFully() throws IOException {
      ByteArrayOutputStream bOut = new ByteArrayOutputStream();

      int b;
      for(int b1 = this.read(); (b = this.read()) >= 0 && (b1 != 0 || b != 0); b1 = b) {
         bOut.write(b1);
      }

      return bOut.toByteArray();
   }

   private BERConstructedOctetString buildConstructedOctetString() throws IOException {
      Vector octs = new Vector();

      while(true) {
         DERObject o = this.readObject();
         if (o == this.END_OF_STREAM) {
            return new BERConstructedOctetString(octs);
         }

         octs.addElement(o);
      }
   }

   public DERObject readObject() throws IOException {
      int tag = this.read();
      if (tag == -1) {
         throw new EOFException();
      } else {
         int length = this.readLength();
         if (length >= 0) {
            if (tag == 0 && length == 0) {
               return this.END_OF_STREAM;
            } else {
               byte[] bytes = new byte[length];
               this.readFully(bytes);
               return this.buildObject(tag, bytes);
            }
         } else {
            BERConstructedSequence seq;
            DERObject dObj;
            switch(tag) {
            case 5:
               return null;
            case 36:
               return this.buildConstructedOctetString();
            case 48:
               seq = new BERConstructedSequence();

               while(true) {
                  DERObject obj = this.readObject();
                  if (obj == this.END_OF_STREAM) {
                     return seq;
                  }

                  seq.addObject(obj);
               }
            case 49:
               ASN1EncodableVector v = new ASN1EncodableVector();

               while(true) {
                  dObj = this.readObject();
                  if (dObj == this.END_OF_STREAM) {
                     return new BERSet(v);
                  }

                  v.add(dObj);
               }
            default:
               if ((tag & 128) == 0) {
                  throw new IOException("unknown BER object encountered");
               } else if ((tag & 31) == 31) {
                  throw new IOException("unsupported high tag encountered");
               } else if ((tag & 32) == 0) {
                  byte[] bytes = this.readIndefiniteLengthFully();
                  return new BERTaggedObject(false, tag & 31, new DEROctetString(bytes));
               } else {
                  dObj = this.readObject();
                  if (dObj == this.END_OF_STREAM) {
                     return new DERTaggedObject(tag & 31);
                  } else {
                     DERObject next = this.readObject();
                     if (next == this.END_OF_STREAM) {
                        return new BERTaggedObject(tag & 31, dObj);
                     } else {
                        seq = new BERConstructedSequence();
                        seq.addObject(dObj);

                        do {
                           seq.addObject(next);
                           next = this.readObject();
                        } while(next != this.END_OF_STREAM);

                        return new BERTaggedObject(false, tag & 31, seq);
                     }
                  }
               }
            }
         }
      }
   }
}
