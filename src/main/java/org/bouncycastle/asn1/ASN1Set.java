package org.bouncycastle.asn1;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;

public abstract class ASN1Set extends DERObject {
   protected Vector set = new Vector();

   public static ASN1Set getInstance(Object obj) {
      if (obj != null && !(obj instanceof ASN1Set)) {
         throw new IllegalArgumentException("unknown object in getInstance");
      } else {
         return (ASN1Set)obj;
      }
   }

   public static ASN1Set getInstance(ASN1TaggedObject obj, boolean explicit) {
      if (explicit) {
         if (!obj.isExplicit()) {
            throw new IllegalArgumentException("object implicit - explicit expected.");
         } else {
            return (ASN1Set)obj.getObject();
         }
      } else if (obj.isExplicit()) {
         ASN1Set set = new DERSet(obj.getObject());
         return set;
      } else if (obj.getObject() instanceof ASN1Set) {
         return (ASN1Set)obj.getObject();
      } else {
         ASN1EncodableVector v = new ASN1EncodableVector();
         if (!(obj.getObject() instanceof ASN1Sequence)) {
            throw new IllegalArgumentException("unknown object in getInstanceFromTagged");
         } else {
            ASN1Sequence s = (ASN1Sequence)obj.getObject();
            Enumeration e = s.getObjects();

            while(e.hasMoreElements()) {
               v.add((DEREncodable)e.nextElement());
            }

            return new DERSet(v, false);
         }
      }
   }

   public Enumeration getObjects() {
      return this.set.elements();
   }

   public DEREncodable getObjectAt(int index) {
      return (DEREncodable)this.set.elementAt(index);
   }

   public int size() {
      return this.set.size();
   }

   public int hashCode() {
      Enumeration e = this.getObjects();

      int hashCode;
      for(hashCode = 0; e.hasMoreElements(); hashCode ^= e.nextElement().hashCode()) {
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
         if (!(dObj instanceof ASN1Set)) {
            return false;
         } else {
            ASN1Set other = (ASN1Set)dObj;
            if (this.size() != other.size()) {
               return false;
            } else {
               Enumeration s1 = this.getObjects();
               Enumeration s2 = other.getObjects();

               while(s1.hasMoreElements()) {
                  if (!s1.nextElement().equals(s2.nextElement())) {
                     return false;
                  }
               }

               return true;
            }
         }
      }
   }

   private boolean lessThanOrEqual(byte[] a, byte[] b) {
      int i;
      int l;
      int r;
      if (a.length <= b.length) {
         for(i = 0; i != a.length; ++i) {
            l = a[i] & 255;
            r = b[i] & 255;
            if (r > l) {
               return true;
            }

            if (l > r) {
               return false;
            }
         }

         return true;
      } else {
         for(i = 0; i != b.length; ++i) {
            l = a[i] & 255;
            r = b[i] & 255;
            if (r > l) {
               return true;
            }

            if (l > r) {
               return false;
            }
         }

         return false;
      }
   }

   private byte[] getEncoded(DEREncodable obj) {
      ByteArrayOutputStream bOut = new ByteArrayOutputStream();
      ASN1OutputStream aOut = new ASN1OutputStream(bOut);

      try {
         aOut.writeObject(obj);
      } catch (IOException var5) {
         throw new IllegalArgumentException("cannot encode object added to SET");
      }

      return bOut.toByteArray();
   }

   protected void sort() {
      if (this.set.size() > 1) {
         boolean swapped = true;

         while(swapped) {
            int index = 0;
            byte[] a = this.getEncoded((DEREncodable)this.set.elementAt(0));

            for(swapped = false; index != this.set.size() - 1; ++index) {
               byte[] b = this.getEncoded((DEREncodable)this.set.elementAt(index + 1));
               if (this.lessThanOrEqual(a, b)) {
                  a = b;
               } else {
                  Object o = this.set.elementAt(index);
                  this.set.setElementAt(this.set.elementAt(index + 1), index);
                  this.set.setElementAt(o, index + 1);
                  swapped = true;
               }
            }
         }
      }

   }

   protected void addObject(DEREncodable obj) {
      this.set.addElement(obj);
   }

   abstract void encode(DEROutputStream var1) throws IOException;
}
