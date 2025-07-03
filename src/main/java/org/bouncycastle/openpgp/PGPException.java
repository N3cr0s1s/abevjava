package org.bouncycastle.openpgp;

public class PGPException extends Exception {
   Exception underlying;

   public PGPException(String message) {
      super(message);
   }

   public PGPException(String message, Exception underlying) {
      super(message);
      this.underlying = underlying;
   }

   public Exception getUnderlyingException() {
      return this.underlying;
   }
}
