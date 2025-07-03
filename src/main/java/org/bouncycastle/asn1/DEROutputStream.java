package org.bouncycastle.asn1;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class DEROutputStream extends FilterOutputStream implements DERTags {
   public DEROutputStream(OutputStream os) {
      super(os);
   }

   private void writeLength(int length) throws IOException {
      if (length > 127) {
         int size = 1;

         for(int val = length; (val >>>= 8) != 0; ++size) {
         }

         this.write((byte)(size | 128));

         for(int i = (size - 1) * 8; i >= 0; i -= 8) {
            this.write((byte)(length >> i));
         }
      } else {
         this.write((byte)length);
      }

   }

   void writeEncoded(int tag, byte[] bytes) throws IOException {
      this.write(tag);
      this.writeLength(bytes.length);
      this.write(bytes);
   }

   protected void writeNull() throws IOException {
      this.write(5);
      this.write(0);
   }

   public void writeObject(Object obj) throws IOException {
      if (obj == null) {
         this.writeNull();
      } else if (obj instanceof DERObject) {
         ((DERObject)obj).encode(this);
      } else {
         if (!(obj instanceof DEREncodable)) {
            throw new IOException("object not DEREncodable");
         }

         ((DEREncodable)obj).getDERObject().encode(this);
      }

   }
}
