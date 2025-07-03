package org.bouncycastle.openpgp;

public class PGPOnePassSignatureList {
   PGPOnePassSignature[] sigs;

   public PGPOnePassSignatureList(PGPOnePassSignature[] sigs) {
      this.sigs = new PGPOnePassSignature[sigs.length];
      System.arraycopy(sigs, 0, this.sigs, 0, sigs.length);
   }

   public PGPOnePassSignatureList(PGPOnePassSignature sig) {
      this.sigs = new PGPOnePassSignature[1];
      this.sigs[0] = sig;
   }

   public PGPOnePassSignature get(int index) {
      return this.sigs[index];
   }

   public int size() {
      return this.sigs.length;
   }

   public boolean isEmpty() {
      return this.sigs.length == 0;
   }
}
