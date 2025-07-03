package hu.piller.tools.bzip2;

public class InflateFailedException extends Exception {
   public InflateFailedException() {
      super("cannot inflate, invalid bzip2 format");
   }
}
