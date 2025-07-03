package org.bouncycastle.asn1;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;

public class BERConstructedOctetString extends DEROctetString {
   private Vector octs;

   private static byte[] toBytes(Vector octs) {
      ByteArrayOutputStream bOut = new ByteArrayOutputStream();

      for(int i = 0; i != octs.size(); ++i) {
         try {
            DEROctetString o = (DEROctetString)octs.elementAt(i);
            bOut.write(o.getOctets());
         } catch (ClassCastException var4) {
            throw new IllegalArgumentException(octs.elementAt(i).getClass().getName() + " found in input should only contain DEROctetString");
         } catch (IOException var5) {
            throw new IllegalArgumentException("exception converting octets " + var5.toString());
         }
      }

      return bOut.toByteArray();
   }

   public BERConstructedOctetString(byte[] string) {
      super(string);
   }

   public BERConstructedOctetString(Vector octs) {
      super(toBytes(octs));
      this.octs = octs;
   }

   public BERConstructedOctetString(DERObject obj) {
      super((DEREncodable)obj);
   }

   public BERConstructedOctetString(DEREncodable obj) {
      super((DEREncodable)obj.getDERObject());
   }

   public byte[] getOctets() {
      return this.string;
   }

   public Enumeration getObjects() {
      return this.octs == null ? this.generateOcts().elements() : this.octs.elements();
   }

   private Vector generateOcts() {
      int start = 0;
      int end = 0;

      Vector vec;
      byte[] nStr;
      for(vec = new Vector(); end + 1 < this.string.length; ++end) {
         if (this.string[end] == 0 && this.string[end + 1] == 0) {
            nStr = new byte[end - start + 1];
            System.arraycopy(this.string, start, nStr, 0, nStr.length);
            vec.addElement(new DEROctetString(nStr));
            start = end + 1;
         }
      }

      nStr = new byte[this.string.length - start];
      System.arraycopy(this.string, start, nStr, 0, nStr.length);
      vec.addElement(new DEROctetString(nStr));
      return vec;
   }

   public void encode(DEROutputStream out) throws IOException {
      if (!(out instanceof ASN1OutputStream) && !(out instanceof BEROutputStream)) {
         super.encode(out);
      } else {
         out.write(36);
         out.write(128);
         int start;
         if (this.octs != null) {
            for(start = 0; start != this.octs.size(); ++start) {
               out.writeObject(this.octs.elementAt(start));
            }
         } else {
            start = 0;

            byte[] nStr;
            for(int end = 0; end + 1 < this.string.length; ++end) {
               if (this.string[end] == 0 && this.string[end + 1] == 0) {
                  nStr = new byte[end - start + 1];
                  System.arraycopy(this.string, start, nStr, 0, nStr.length);
                  out.writeObject(new DEROctetString(nStr));
                  start = end + 1;
               }
            }

            nStr = new byte[this.string.length - start];
            System.arraycopy(this.string, start, nStr, 0, nStr.length);
            out.writeObject(new DEROctetString(nStr));
         }

         out.write(0);
         out.write(0);
      }

   }
}
