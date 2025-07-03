package org.bouncycastle.asn1;

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/** @deprecated */
public class DERInputStream extends FilterInputStream implements DERTags {
   /** @deprecated */
   public DERInputStream(InputStream is) {
      super(is);
   }

   protected int readLength() throws IOException {
      int length = this.read();
      if (length < 0) {
         throw new IOException("EOF found when length expected");
      } else if (length == 128) {
         return -1;
      } else {
         if (length > 127) {
            int size = length & 127;
            if (size > 4) {
               throw new IOException("DER length more than 4 bytes");
            }

            length = 0;

            for(int i = 0; i < size; ++i) {
               int next = this.read();
               if (next < 0) {
                  throw new IOException("EOF found reading length");
               }

               length = (length << 8) + next;
            }

            if (length < 0) {
               throw new IOException("corrupted steam - negative length found");
            }
         }

         return length;
      }
   }

   protected void readFully(byte[] bytes) throws IOException {
      int left = bytes.length;
      if (left != 0) {
         while(left > 0) {
            int l = this.read(bytes, bytes.length - left, left);
            if (l < 0) {
               throw new EOFException("unexpected end of stream");
            }

            left -= l;
         }

      }
   }

   protected DERObject buildObject(int tag, byte[] bytes) throws IOException {
      ByteArrayInputStream bIn;
      BERInputStream dIn;
      DERConstructedSequence seq;
      switch(tag) {
      case 1:
         return new DERBoolean(bytes);
      case 2:
         return new DERInteger(bytes);
      case 3:
         int padBits = bytes[0];
         byte[] data = new byte[bytes.length - 1];
         System.arraycopy(bytes, 1, data, 0, bytes.length - 1);
         return new DERBitString(data, padBits);
      case 4:
         return new DEROctetString(bytes);
      case 5:
         return null;
      case 6:
         return new DERObjectIdentifier(bytes);
      case 10:
         return new DEREnumerated(bytes);
      case 12:
         return new DERUTF8String(bytes);
      case 19:
         return new DERPrintableString(bytes);
      case 20:
         return new DERT61String(bytes);
      case 22:
         return new DERIA5String(bytes);
      case 23:
         return new DERUTCTime(bytes);
      case 24:
         return new DERGeneralizedTime(bytes);
      case 26:
         return new DERVisibleString(bytes);
      case 27:
         return new DERGeneralString(bytes);
      case 28:
         return new DERUniversalString(bytes);
      case 30:
         return new DERBMPString(bytes);
      case 48:
         bIn = new ByteArrayInputStream(bytes);
         dIn = new BERInputStream(bIn);
         seq = new DERConstructedSequence();

         try {
            while(true) {
               DERObject obj = dIn.readObject();
               seq.addObject(obj);
            }
         } catch (EOFException var12) {
            return seq;
         }
      case 49:
         bIn = new ByteArrayInputStream(bytes);
         dIn = new BERInputStream(bIn);
         ASN1EncodableVector v = new ASN1EncodableVector();

         try {
            while(true) {
               DERObject obj = dIn.readObject();
               v.add(obj);
            }
         } catch (EOFException var11) {
            return new DERConstructedSet(v);
         }
      default:
         if ((tag & 128) != 0) {
            if ((tag & 31) == 31) {
               throw new IOException("unsupported high tag encountered");
            } else if (bytes.length == 0) {
               return (tag & 32) == 0 ? new DERTaggedObject(false, tag & 31, new DERNull()) : new DERTaggedObject(false, tag & 31, new DERConstructedSequence());
            } else if ((tag & 32) == 0) {
               return new DERTaggedObject(false, tag & 31, new DEROctetString(bytes));
            } else {
               bIn = new ByteArrayInputStream(bytes);
               dIn = new BERInputStream(bIn);
               DEREncodable dObj = dIn.readObject();
               if (dIn.available() == 0) {
                  return new DERTaggedObject(tag & 31, dObj);
               } else {
                  seq = new DERConstructedSequence();
                  seq.addObject(dObj);

                  try {
                     while(true) {
                        dObj = dIn.readObject();
                        seq.addObject(dObj);
                     }
                  } catch (EOFException var13) {
                     return new DERTaggedObject(false, tag & 31, seq);
                  }
               }
            }
         } else {
            return new DERUnknownTag(tag, bytes);
         }
      }
   }

   public DERObject readObject() throws IOException {
      int tag = this.read();
      if (tag == -1) {
         throw new EOFException();
      } else {
         int length = this.readLength();
         byte[] bytes = new byte[length];
         this.readFully(bytes);
         return this.buildObject(tag, bytes);
      }
   }
}
