package org.bouncycastle.crypto.params;

import java.security.SecureRandom;
import org.bouncycastle.crypto.CipherParameters;

public class ParametersWithRandom implements CipherParameters {
   private SecureRandom random;
   private CipherParameters parameters;

   public ParametersWithRandom(CipherParameters parameters, SecureRandom random) {
      this.random = random;
      this.parameters = parameters;
   }

   public ParametersWithRandom(CipherParameters parameters) {
      this.random = null;
      this.parameters = parameters;
   }

   public SecureRandom getRandom() {
      if (this.random == null) {
         this.random = new SecureRandom();
      }

      return this.random;
   }

   public CipherParameters getParameters() {
      return this.parameters;
   }
}
