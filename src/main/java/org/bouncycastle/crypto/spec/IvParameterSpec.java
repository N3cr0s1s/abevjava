package org.bouncycastle.crypto.spec;

import java.security.spec.AlgorithmParameterSpec;

public class IvParameterSpec implements AlgorithmParameterSpec {
   private byte[] iv;

   public IvParameterSpec(byte[] iv) {
      if (iv == null) {
         throw new IllegalArgumentException("null iv passed");
      } else {
         this.iv = new byte[iv.length];
         System.arraycopy(iv, 0, this.iv, 0, iv.length);
      }
   }

   public IvParameterSpec(byte[] iv, int offset, int len) {
      if (iv == null) {
         throw new IllegalArgumentException("Null iv passed");
      } else if (offset >= 0 && len >= 0 && iv.length - offset >= len) {
         this.iv = new byte[len];
         System.arraycopy(iv, offset, this.iv, 0, len);
      } else {
         throw new IllegalArgumentException("Bad offset/len");
      }
   }

   public byte[] getIV() {
      byte[] tmp = new byte[this.iv.length];
      System.arraycopy(this.iv, 0, tmp, 0, this.iv.length);
      return tmp;
   }
}
