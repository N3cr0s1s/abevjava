package org.bouncycastle.openpgp;

import java.security.PrivateKey;

public class PGPPrivateKey {
   private long keyID;
   private PrivateKey privateKey;

   PGPPrivateKey(PrivateKey privateKey, long keyID) {
      this.privateKey = privateKey;
      this.keyID = keyID;
   }

   public long getKeyID() {
      return this.keyID;
   }

   public PrivateKey getKey() {
      return this.privateKey;
   }
}
