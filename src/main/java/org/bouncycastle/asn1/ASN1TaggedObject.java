package org.bouncycastle.asn1;

import java.io.IOException;

public abstract class ASN1TaggedObject extends DERObject {
   int tagNo;
   boolean empty = false;
   boolean explicit = true;
   DEREncodable obj = null;

   public static ASN1TaggedObject getInstance(ASN1TaggedObject obj, boolean explicit) {
      if (explicit) {
         return (ASN1TaggedObject)obj.getObject();
      } else {
         throw new IllegalArgumentException("implicitly tagged tagged object");
      }
   }

   public ASN1TaggedObject(int tagNo, DEREncodable obj) {
      this.explicit = true;
      this.tagNo = tagNo;
      this.obj = obj;
   }

   public ASN1TaggedObject(boolean explicit, int tagNo, DEREncodable obj) {
      if (obj instanceof ASN1Choice) {
         this.explicit = true;
      } else {
         this.explicit = explicit;
      }

      this.tagNo = tagNo;
      this.obj = obj;
   }

   public boolean equals(Object o) {
      if (o != null && o instanceof ASN1TaggedObject) {
         ASN1TaggedObject other = (ASN1TaggedObject)o;
         if (this.tagNo == other.tagNo && this.empty == other.empty && this.explicit == other.explicit) {
            if (this.obj == null) {
               if (other.obj != null) {
                  return false;
               }
            } else if (!this.obj.equals(other.obj)) {
               return false;
            }

            return true;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public int hashCode() {
      int code = this.tagNo;
      if (this.obj != null) {
         code ^= this.obj.hashCode();
      }

      return code;
   }

   public int getTagNo() {
      return this.tagNo;
   }

   public boolean isExplicit() {
      return this.explicit;
   }

   public boolean isEmpty() {
      return this.empty;
   }

   public DERObject getObject() {
      return this.obj != null ? this.obj.getDERObject() : null;
   }

   abstract void encode(DEROutputStream var1) throws IOException;
}
