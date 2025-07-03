package org.bouncycastle.crypto.generators;

import org.bouncycastle.crypto.CipherKeyGenerator;
import org.bouncycastle.crypto.KeyGenerationParameters;

public class AESKeyGenerator extends CipherKeyGenerator {
   public void init(KeyGenerationParameters param) {
      super.init(param);
      if (this.strength == 0) {
         this.strength = 128;
      } else if (this.strength != 128 && this.strength != 192 && this.strength != 256) {
         throw new IllegalArgumentException("AES key must be 128 or 192 or 256 bits long.");
      }

   }

   public byte[] generateKey() {
      byte[] newKey = new byte[this.strength];
      this.random.nextBytes(newKey);
      return newKey;
   }
}
