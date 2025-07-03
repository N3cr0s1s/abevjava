package org.bouncycastle.asn1;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class DERBitString extends DERObject implements DERString {
   private static final char[] table = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
   protected byte[] data;
   protected int padBits;

   protected static int getPadBits(int bitString) {
      int val = 0;

      int bits;
      for(bits = 3; bits >= 0; --bits) {
         if (bits != 0) {
            if (bitString >> bits * 8 != 0) {
               val = bitString >> bits * 8 & 255;
               break;
            }
         } else if (bitString != 0) {
            val = bitString & 255;
            break;
         }
      }

      if (val == 0) {
         return 7;
      } else {
         for(bits = 1; ((val <<= 1) & 255) != 0; ++bits) {
         }

         return 8 - bits;
      }
   }

   protected static byte[] getBytes(int bitString) {
      int bytes = 4;

      for(int i = 3; i >= 1 && (bitString & 255 << i * 8) == 0; --i) {
         --bytes;
      }

      byte[] result = new byte[bytes];

      for(int i = 0; i < bytes; ++i) {
         result[i] = (byte)(bitString >> i * 8 & 255);
      }

      return result;
   }

   public static DERBitString getInstance(Object obj) {
      if (obj != null && !(obj instanceof DERBitString)) {
         if (obj instanceof ASN1OctetString) {
            byte[] bytes = ((ASN1OctetString)obj).getOctets();
            int padBits = bytes[0];
            byte[] data = new byte[bytes.length - 1];
            System.arraycopy(bytes, 1, data, 0, bytes.length - 1);
            return new DERBitString(data, padBits);
         } else if (obj instanceof ASN1TaggedObject) {
            return getInstance(((ASN1TaggedObject)obj).getObject());
         } else {
            throw new IllegalArgumentException("illegal object in getInstance: " + obj.getClass().getName());
         }
      } else {
         return (DERBitString)obj;
      }
   }

   public static DERBitString getInstance(ASN1TaggedObject obj, boolean explicit) {
      return getInstance(obj.getObject());
   }

   protected DERBitString(byte data, int padBits) {
      this.data = new byte[1];
      this.data[0] = data;
      this.padBits = padBits;
   }

   public DERBitString(byte[] data, int padBits) {
      this.data = data;
      this.padBits = padBits;
   }

   public DERBitString(byte[] data) {
      this(data, 0);
   }

   public DERBitString(DEREncodable obj) {
      try {
         ByteArrayOutputStream bOut = new ByteArrayOutputStream();
         DEROutputStream dOut = new DEROutputStream(bOut);
         dOut.writeObject(obj);
         dOut.close();
         this.data = bOut.toByteArray();
         this.padBits = 0;
      } catch (IOException var4) {
         throw new IllegalArgumentException("Error processing object : " + var4.toString());
      }
   }

   public byte[] getBytes() {
      return this.data;
   }

   public int getPadBits() {
      return this.padBits;
   }

   public int intValue() {
      int value = 0;

      for(int i = 0; i != this.data.length && i != 4; ++i) {
         value |= (this.data[i] & 255) << 8 * i;
      }

      return value;
   }

   void encode(DEROutputStream out) throws IOException {
      byte[] bytes = new byte[this.getBytes().length + 1];
      bytes[0] = (byte)this.getPadBits();
      System.arraycopy(this.getBytes(), 0, bytes, 1, bytes.length - 1);
      out.writeEncoded(3, bytes);
   }

   public int hashCode() {
      int value = 0;

      for(int i = 0; i != this.data.length; ++i) {
         value ^= (this.data[i] & 255) << i % 4;
      }

      return value;
   }

   public boolean equals(Object o) {
      if (o != null && o instanceof DERBitString) {
         DERBitString other = (DERBitString)o;
         if (this.data.length != other.data.length) {
            return false;
         } else {
            for(int i = 0; i != this.data.length; ++i) {
               if (this.data[i] != other.data[i]) {
                  return false;
               }
            }

            return this.padBits == other.padBits;
         }
      } else {
         return false;
      }
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
}
