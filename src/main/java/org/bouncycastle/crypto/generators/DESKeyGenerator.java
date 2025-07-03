package org.bouncycastle.crypto.generators;

import org.bouncycastle.crypto.CipherKeyGenerator;
import org.bouncycastle.crypto.params.DESParameters;

public class DESKeyGenerator extends CipherKeyGenerator {
   public byte[] generateKey() {
      byte[] newKey = new byte[8];

      do {
         this.random.nextBytes(newKey);
         DESParameters.setOddParity(newKey);
      } while(DESParameters.isWeakKey(newKey, 0));

      return newKey;
   }
}
