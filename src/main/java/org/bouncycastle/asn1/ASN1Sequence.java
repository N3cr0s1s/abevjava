package org.bouncycastle.asn1;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;

public abstract class ASN1Sequence extends DERObject {
   private Vector seq = new Vector();

   public static ASN1Sequence getInstance(Object obj) {
      if (obj != null && !(obj instanceof ASN1Sequence)) {
         throw new IllegalArgumentException("unknown object in getInstance");
      } else {
         return (ASN1Sequence)obj;
      }
   }

   public static ASN1Sequence getInstance(ASN1TaggedObject obj, boolean explicit) {
      if (explicit) {
         if (!obj.isExplicit()) {
            throw new IllegalArgumentException("object implicit - explicit expected.");
         } else {
            return (ASN1Sequence)obj.getObject();
         }
      } else if (obj.isExplicit()) {
         return (ASN1Sequence)(obj instanceof BERTaggedObject ? new BERSequence(obj.getObject()) : new DERSequence(obj.getObject()));
      } else if (obj.getObject() instanceof ASN1Sequence) {
         return (ASN1Sequence)obj.getObject();
      } else {
         throw new IllegalArgumentException("unknown object in getInstanceFromTagged");
      }
   }

   public Enumeration getObjects() {
      return this.seq.elements();
   }

   public DEREncodable getObjectAt(int index) {
      return (DEREncodable)this.seq.elementAt(index);
   }

   public int size() {
      return this.seq.size();
   }

   public int hashCode() {
      Enumeration e = this.getObjects();
      int hashCode = 0;

      while(e.hasMoreElements()) {
         Object o = e.nextElement();
         if (o != null) {
            hashCode ^= o.hashCode();
         }
      }

      return hashCode;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof DEREncodable)) {
         return false;
      } else {
         DERObject dObj = ((DEREncodable)o).getDERObject();
         if (!(dObj instanceof ASN1Sequence)) {
            return false;
         } else {
            ASN1Sequence other = (ASN1Sequence)dObj;
            if (this.size() != other.size()) {
               return false;
            } else {
               Enumeration s1 = this.getObjects();
               Enumeration s2 = other.getObjects();

               Object o1;
               Object o2;
               label45:
               do {
                  do {
                     if (!s1.hasMoreElements()) {
                        return true;
                     }

                     o1 = s1.nextElement();
                     o2 = s2.nextElement();
                     if (o1 == null || o2 == null) {
                        continue label45;
                     }
                  } while(o1.equals(o2));

                  return false;
               } while(o1 == null && o2 == null);

               return false;
            }
         }
      }
   }

   protected void addObject(DEREncodable obj) {
      this.seq.addElement(obj);
   }

   abstract void encode(DEROutputStream var1) throws IOException;
}
