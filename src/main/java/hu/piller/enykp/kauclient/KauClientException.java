package hu.piller.enykp.kauclient;

public class KauClientException extends Exception {
   private boolean specialMessage;

   public KauClientException() {
   }

   public KauClientException(String var1) {
      super(var1);
   }

   public KauClientException(String var1, boolean var2) {
      super(var1);
      this.setSpecialMessage(var2);
   }

   public KauClientException(String var1, Throwable var2) {
      super(var1, var2);
   }

   public KauClientException(Throwable var1) {
      super(var1);
   }

   protected KauClientException(String var1, Throwable var2, boolean var3, boolean var4) {
      super(var1, var2, var3, var4);
   }

   public boolean isSpecialMessage() {
      return this.specialMessage;
   }

   public void setSpecialMessage(boolean var1) {
      this.specialMessage = var1;
   }
}
