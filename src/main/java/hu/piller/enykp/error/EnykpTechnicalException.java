package hu.piller.enykp.error;

public class EnykpTechnicalException extends Exception {
   public EnykpTechnicalException() {
   }

   public EnykpTechnicalException(String var1) {
      super(var1);
   }

   public EnykpTechnicalException(String var1, Throwable var2) {
      super(var1, var2);
   }

   public EnykpTechnicalException(Throwable var1) {
      super(var1);
   }
}
