package org.bouncycastle.crypto.generators;

import org.bouncycastle.crypto.KeyGenerationParameters;
import org.bouncycastle.crypto.params.DESedeParameters;

public class DESedeKeyGenerator extends DESKeyGenerator {
   public void init(KeyGenerationParameters param) {
      super.init(param);
      if (this.strength != 0 && this.strength != 21) {
         if (this.strength == 14) {
            this.strength = 16;
         } else if (this.strength != 24 && this.strength != 16) {
            throw new IllegalArgumentException("DESede key must be 192 or 128 bits long.");
         }
      } else {
         this.strength = 24;
      }

   }

   public byte[] generateKey() {
      byte[] newKey = new byte[this.strength];

      do {
         this.random.nextBytes(newKey);
         DESedeParameters.setOddParity(newKey);
      } while(DESedeParameters.isWeakKey(newKey, 0, newKey.length));

      return newKey;
   }
}
