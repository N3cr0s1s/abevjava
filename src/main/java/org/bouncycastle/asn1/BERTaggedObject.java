package org.bouncycastle.asn1;

import java.io.IOException;
import java.util.Enumeration;

public class BERTaggedObject extends DERTaggedObject {
   public BERTaggedObject(int tagNo, DEREncodable obj) {
      super(tagNo, obj);
   }

   public BERTaggedObject(boolean explicit, int tagNo, DEREncodable obj) {
      super(explicit, tagNo, obj);
   }

   public BERTaggedObject(int tagNo) {
      super(false, tagNo, new BERSequence());
   }

   void encode(DEROutputStream out) throws IOException {
      if (!(out instanceof ASN1OutputStream) && !(out instanceof BEROutputStream)) {
         super.encode(out);
      } else {
         out.write(160 | this.tagNo);
         out.write(128);
         if (!this.empty) {
            if (!this.explicit) {
               Enumeration e;
               if (this.obj instanceof ASN1OctetString) {
                  if (this.obj instanceof BERConstructedOctetString) {
                     e = ((BERConstructedOctetString)this.obj).getObjects();
                  } else {
                     ASN1OctetString octs = (ASN1OctetString)this.obj;
                     BERConstructedOctetString berO = new BERConstructedOctetString(octs.getOctets());
                     e = berO.getObjects();
                  }

                  while(e.hasMoreElements()) {
                     out.writeObject(e.nextElement());
                  }
               } else if (this.obj instanceof ASN1Sequence) {
                  e = ((ASN1Sequence)this.obj).getObjects();

                  while(e.hasMoreElements()) {
                     out.writeObject(e.nextElement());
                  }
               } else {
                  if (!(this.obj instanceof ASN1Set)) {
                     throw new RuntimeException("not implemented: " + this.obj.getClass().getName());
                  }

                  e = ((ASN1Set)this.obj).getObjects();

                  while(e.hasMoreElements()) {
                     out.writeObject(e.nextElement());
                  }
               }
            } else {
               out.writeObject(this.obj);
            }
         }

         out.write(0);
         out.write(0);
      }

   }
}
