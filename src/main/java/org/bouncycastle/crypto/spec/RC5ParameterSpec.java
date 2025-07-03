package org.bouncycastle.crypto.spec;

import java.security.spec.AlgorithmParameterSpec;

public class RC5ParameterSpec implements AlgorithmParameterSpec {
   private int version;
   private int rounds;
   private int wordSize;
   private byte[] iv;

   public RC5ParameterSpec(int version, int rounds, int wordSize) {
      this.version = version;
      this.rounds = rounds;
      this.wordSize = wordSize;
      this.iv = null;
   }

   public RC5ParameterSpec(int version, int rounds, int wordSize, byte[] iv) {
      this(version, rounds, wordSize, iv, 0);
   }

   public RC5ParameterSpec(int version, int rounds, int wordSize, byte[] iv, int offset) {
      this.version = version;
      this.rounds = rounds;
      this.wordSize = wordSize;
      this.iv = new byte[2 * (wordSize / 8)];
      System.arraycopy(iv, offset, this.iv, 0, this.iv.length);
   }

   public int getVersion() {
      return this.version;
   }

   public int getRounds() {
      return this.rounds;
   }

   public int getWordSize() {
      return this.wordSize;
   }

   public byte[] getIV() {
      if (this.iv == null) {
         return null;
      } else {
         byte[] tmp = new byte[this.iv.length];
         System.arraycopy(this.iv, 0, tmp, 0, this.iv.length);
         return tmp;
      }
   }

   public boolean equals(Object obj) {
      if (obj != null && obj instanceof RC5ParameterSpec) {
         RC5ParameterSpec spec = (RC5ParameterSpec)obj;
         if (this.version != spec.version) {
            return false;
         } else if (this.rounds != spec.rounds) {
            return false;
         } else if (this.wordSize != spec.wordSize) {
            return false;
         } else {
            if (this.iv != null) {
               if (spec.iv == null || spec.iv.length != this.iv.length) {
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
      int code = this.version ^ this.rounds ^ this.wordSize;
      if (this.iv != null) {
         for(int i = 0; i != this.iv.length; ++i) {
            code ^= this.iv[i] << 8 * (i % 4);
         }
      }

      return code;
   }
}
