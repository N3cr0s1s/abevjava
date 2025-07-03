package org.bouncycastle.crypto;

import java.security.GeneralSecurityException;

public class ExemptionMechanismException extends GeneralSecurityException {
   public ExemptionMechanismException() {
   }

   public ExemptionMechanismException(String msg) {
      super(msg);
   }
}
