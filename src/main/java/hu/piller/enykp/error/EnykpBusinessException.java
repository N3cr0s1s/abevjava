package hu.piller.enykp.error;

public class EnykpBusinessException extends Exception {
   public EnykpBusinessException() {
   }

   public EnykpBusinessException(String var1) {
      super(var1);
   }

   public EnykpBusinessException(String var1, Throwable var2) {
      super(var1, var2);
   }

   public EnykpBusinessException(Throwable var1) {
      super(var1);
   }
}
