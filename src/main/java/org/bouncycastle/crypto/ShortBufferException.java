package org.bouncycastle.crypto;

import java.security.GeneralSecurityException;

public class ShortBufferException extends GeneralSecurityException {
   public ShortBufferException() {
   }

   public ShortBufferException(String msg) {
      super(msg);
   }
}
