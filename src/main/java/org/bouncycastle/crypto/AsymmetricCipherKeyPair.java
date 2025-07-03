package org.bouncycastle.crypto;

public class AsymmetricCipherKeyPair {
   private CipherParameters publicParam;
   private CipherParameters privateParam;

   public AsymmetricCipherKeyPair(CipherParameters publicParam, CipherParameters privateParam) {
      this.publicParam = publicParam;
      this.privateParam = privateParam;
   }

   public CipherParameters getPublic() {
      return this.publicParam;
   }

   public CipherParameters getPrivate() {
      return this.privateParam;
   }
}
