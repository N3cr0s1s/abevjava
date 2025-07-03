package org.bouncycastle.crypto.spec;

import java.security.spec.AlgorithmParameterSpec;

public class PBEParameterSpec implements AlgorithmParameterSpec {
   private byte[] salt;
   private int iterationCount;

   public PBEParameterSpec(byte[] salt, int iterationCount) {
      this.salt = new byte[salt.length];
      System.arraycopy(salt, 0, this.salt, 0, salt.length);
      this.iterationCount = iterationCount;
   }

   public byte[] getSalt() {
      byte[] tmp = new byte[this.salt.length];
      System.arraycopy(this.salt, 0, tmp, 0, this.salt.length);
      return tmp;
   }

   public int getIterationCount() {
      return this.iterationCount;
   }
}
