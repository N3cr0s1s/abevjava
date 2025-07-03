package hu.piller.enykp.alogic.uploader;

public class UploaderException extends Exception {
   public static final int UGYFELKAPU = 0;
   public static final int HIVATALIKAPU = 1;
   private int type = 0;

   public UploaderException(String var1) {
      super(var1);
   }

   public UploaderException(String var1, int var2) {
      super(var1);
      this.type = var2;
   }

   public int getType() {
      return this.type;
   }
}
