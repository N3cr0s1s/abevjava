package org.bouncycastle.asn1;

import java.util.Vector;

public class DEREncodableVector {
   private Vector v = new Vector();

   public void add(DEREncodable obj) {
      this.v.addElement(obj);
   }

   public DEREncodable get(int i) {
      return (DEREncodable)this.v.elementAt(i);
   }

   public int size() {
      return this.v.size();
   }
}
