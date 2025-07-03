package org.bouncycastle.asn1;

public class OIDTokenizer {
   private String oid;
   private int index;

   public OIDTokenizer(String oid) {
      this.oid = oid;
      this.index = 0;
   }

   public boolean hasMoreTokens() {
      return this.index != -1;
   }

   public String nextToken() {
      if (this.index == -1) {
         return null;
      } else {
         int end = this.oid.indexOf(46, this.index);
         String token;
         if (end == -1) {
            token = this.oid.substring(this.index);
            this.index = -1;
            return token;
         } else {
            token = this.oid.substring(this.index, end);
            this.index = end + 1;
            return token;
         }
      }
   }
}
