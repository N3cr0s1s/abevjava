package org.bouncycastle.crypto.spec;

import java.security.spec.AlgorithmParameterSpec;

public class RC2ParameterSpec implements AlgorithmParameterSpec {
   private int effectiveKeyBits;
   private byte[] iv;

   public RC2ParameterSpec(int effectiveKeyBits) {
      this.iv = new byte[8];
      this.effectiveKeyBits = effectiveKeyBits;
   }

   public RC2ParameterSpec(int effectiveKeyBits, byte[] iv) {
      this(effectiveKeyBits, iv, 0);
   }

   public RC2ParameterSpec(int effectiveKeyBits, byte[] iv, int offset) {
      this.iv = new byte[8];
      this.effectiveKeyBits = effectiveKeyBits;
      this.iv = new byte[8];
      System.arraycopy(iv, offset, this.iv, 0, this.iv.length);
   }

   public int getEffectiveKeyBits() {
      return this.effectiveKeyBits;
   }

   public byte[] getIV() {
      if (this.iv == null) {
         return null;
      } else {
         byte[] tmp = new byte[this.iv.length];
         System.arraycopy(this.iv, 0, tmp, 0, tmp.length);
         return tmp;
      }
   }

   public boolean equals(Object obj) {
      if (obj != null && obj instanceof RC2ParameterSpec) {
         RC2ParameterSpec spec = (RC2ParameterSpec)obj;
         if (this.effectiveKeyBits != spec.effectiveKeyBits) {
            return false;
         } else {
            if (this.iv != null) {
               if (spec.iv == null) {
                  return false;
               }

               for(int i = 0; i != this.iv.length; ++i) {
                  if (this.iv[i] != spec.iv[i]) {
                     return false;
                  }
               }
            } else if (spec.iv != null) {
               return false;
            }

            return true;
         }
      } else {
         return false;
      }
   }

   public int hashCode() {
      throw new RuntimeException("Not yet implemented");
   }
}
