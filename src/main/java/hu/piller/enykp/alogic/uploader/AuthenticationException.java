package hu.piller.enykp.alogic.uploader;

public class AuthenticationException extends Exception {
   public static final int UGYFELKAPU = 0;
   public static final int HIVATALIKAPU = 1;
   private int type = 0;

   public AuthenticationException() {
   }

   public AuthenticationException(int var1) {
      this.type = var1;
   }

   public AuthenticationException(String var1) {
      super(var1);
   }

   public int getType() {
      return this.type;
   }
}
