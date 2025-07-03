package hu.piller.tools;

public class GeneralException extends Exception {
   public GeneralException() {
   }

   public GeneralException(String msg) {
      super(msg);
   }

   public GeneralException(String msg, Throwable cause) {
      super(msg, cause);
   }
}
