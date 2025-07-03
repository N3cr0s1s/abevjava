package org.bouncycastle.asn1;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

public class ASN1InputStream extends FilterInputStream implements DERTags {
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
   boolean eofFound = false;

   public ASN1InputStream(InputStream is) {
      super(is);
   }

   public ASN1InputStream(byte[] input) {
      super(new ByteArrayInputStream(input));
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
         int len;
         while((len = this.read(bytes, bytes.length - left, left)) > 0) {
            if ((left -= len) == 0) {
               return;
            }
         }

         if (left != 0) {
            throw new EOFException("EOF encountered in middle of object");
         }
      }
   }

   protected DERObject buildObject(int tag, byte[] bytes) throws IOException {
      if ((tag & 64) != 0) {
         return new DERApplicationSpecific(tag, bytes);
      } else {
         ByteArrayInputStream bIn;
         ASN1InputStream aIn;
         ASN1EncodableVector v;
         DERObject obj;
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
            return new DERNull();
         case 6:
            return new DERObjectIdentifier(bytes);
         case 7:
         case 8:
         case 9:
         case 11:
         case 13:
         case 14:
         case 15:
         case 16:
         case 17:
         case 21:
         case 25:
         case 29:
         case 31:
         case 32:
         case 33:
         case 34:
         case 35:
         case 36:
         case 37:
         case 38:
         case 39:
         case 40:
         case 41:
         case 42:
         case 43:
         case 44:
         case 45:
         case 46:
         case 47:
         default:
            if ((tag & 128) == 0) {
               return new DERUnknownTag(tag, bytes);
            } else {
               int tagNo = tag & 31;
               if (tagNo == 31) {
                  int idx = 0;

                  for(tagNo = 0; (bytes[idx] & 128) != 0; tagNo <<= 7) {
                     tagNo |= bytes[idx++] & 127;
                  }

                  tagNo |= bytes[idx] & 127;
                  byte[] tmp = bytes;
                  bytes = new byte[bytes.length - (idx + 1)];
                  System.arraycopy(tmp, idx + 1, bytes, 0, bytes.length);
               }

               if (bytes.length == 0) {
                  if ((tag & 32) == 0) {
                     return new DERTaggedObject(false, tagNo, new DERNull());
                  }

                  return new DERTaggedObject(false, tagNo, new DERSequence());
               } else if ((tag & 32) == 0) {
                  return new DERTaggedObject(false, tagNo, new DEROctetString(bytes));
               } else {
                  bIn = new ByteArrayInputStream(bytes);
                  aIn = new ASN1InputStream(bIn);
                  DEREncodable dObj = aIn.readObject();
                  if (aIn.available() == 0) {
                     return new DERTaggedObject(tagNo, dObj);
                  }

                  for(v = new ASN1EncodableVector(); dObj != null; dObj = aIn.readObject()) {
                     v.add(dObj);
                  }

                  return new DERTaggedObject(false, tagNo, new DERSequence(v));
               }
            }
         case 10:
            return new DEREnumerated(bytes);
         case 12:
            return new DERUTF8String(bytes);
         case 18:
            return new DERNumericString(bytes);
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
            aIn = new ASN1InputStream(bIn);
            v = new ASN1EncodableVector();

            for(obj = aIn.readObject(); obj != null; obj = aIn.readObject()) {
               v.add(obj);
            }

            return new DERSequence(v);
         case 49:
            bIn = new ByteArrayInputStream(bytes);
            aIn = new ASN1InputStream(bIn);
            v = new ASN1EncodableVector();

            for(obj = aIn.readObject(); obj != null; obj = aIn.readObject()) {
               v.add(obj);
            }

            return new DERSet(v, false);
         }
      }
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
         if (this.eofFound) {
            throw new EOFException("attempt to read past end of file.");
         } else {
            this.eofFound = true;
            return null;
         }
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
            ASN1EncodableVector v;
            DERObject obj;
            switch(tag) {
            case 5:
               return new BERNull();
            case 36:
               return this.buildConstructedOctetString();
            case 48:
               v = new ASN1EncodableVector();

               while(true) {
                  obj = this.readObject();
                  if (obj == this.END_OF_STREAM) {
                     return new BERSequence(v);
                  }

                  v.add(obj);
               }
            case 49:
               v = new ASN1EncodableVector();

               while(true) {
                  obj = this.readObject();
                  if (obj == this.END_OF_STREAM) {
                     return new BERSet(v, false);
                  }

                  v.add(obj);
               }
            default:
               if ((tag & 128) == 0) {
                  throw new IOException("unknown BER object encountered");
               } else {
                  int tagNo = tag & 31;
                  if (tagNo == 31) {
                     int b = this.read();

                     for(tagNo = 0; b >= 0 && (b & 128) != 0; b = this.read()) {
                        tagNo |= b & 127;
                        tagNo <<= 7;
                     }

                     tagNo |= b & 127;
                  }

                  if ((tag & 32) == 0) {
                     byte[] bytes = this.readIndefiniteLengthFully();
                     return new BERTaggedObject(false, tagNo, new DEROctetString(bytes));
                  } else {
                     DERObject dObj = this.readObject();
                     if (dObj == this.END_OF_STREAM) {
                        return new DERTaggedObject(tagNo);
                     } else {
                        DERObject next = this.readObject();
                        if (next == this.END_OF_STREAM) {
                           return new BERTaggedObject(tagNo, dObj);
                        } else {
                           v = new ASN1EncodableVector();
                           v.add(dObj);

                           do {
                              v.add(next);
                              next = this.readObject();
                           } while(next != this.END_OF_STREAM);

                           return new BERTaggedObject(false, tagNo, new BERSequence(v));
                        }
                     }
                  }
               }
            }
         }
      }
   }
}
