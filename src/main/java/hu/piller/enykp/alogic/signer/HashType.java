package hu.piller.enykp.alogic.signer;

public enum HashType {
   SHA_256("SHA-256");

   private String sType;

   private HashType(String var3) {
      this.sType = var3;
   }

   public String toString() {
      return this.sType;
   }
}
