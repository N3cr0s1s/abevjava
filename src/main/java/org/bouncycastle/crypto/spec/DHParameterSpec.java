package org.bouncycastle.crypto.spec;

import java.math.BigInteger;
import java.security.spec.AlgorithmParameterSpec;

public class DHParameterSpec implements AlgorithmParameterSpec {
   private BigInteger p;
   private BigInteger g;
   private int l;

   public DHParameterSpec(BigInteger p, BigInteger g) {
      this.p = p;
      this.g = g;
   }

   public DHParameterSpec(BigInteger p, BigInteger g, int l) {
      this.p = p;
      this.g = g;
      this.l = l;
   }

   public BigInteger getP() {
      return this.p;
   }

   public BigInteger getG() {
      return this.g;
   }

   public int getL() {
      return this.l;
   }
}
