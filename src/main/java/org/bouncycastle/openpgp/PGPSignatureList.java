package org.bouncycastle.openpgp;

public class PGPSignatureList {
   PGPSignature[] sigs;

   public PGPSignatureList(PGPSignature[] sigs) {
      this.sigs = new PGPSignature[sigs.length];
      System.arraycopy(sigs, 0, this.sigs, 0, sigs.length);
   }

   public PGPSignatureList(PGPSignature sig) {
      this.sigs = new PGPSignature[1];
      this.sigs[0] = sig;
   }

   public PGPSignature get(int index) {
      return this.sigs[index];
   }

   public int size() {
      return this.sigs.length;
   }

   public boolean isEmpty() {
      return this.sigs.length == 0;
   }
}
