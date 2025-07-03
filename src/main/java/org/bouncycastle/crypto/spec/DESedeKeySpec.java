package org.bouncycastle.crypto.spec;

import java.security.InvalidKeyException;
import java.security.spec.KeySpec;

public class DESedeKeySpec implements KeySpec {
   public static final int DES_EDE_KEY_LEN = 24;
   private byte[] keyBytes = new byte[24];

   public DESedeKeySpec(byte[] key) throws InvalidKeyException {
      if (key.length < 24) {
         throw new InvalidKeyException("DESede key material too short in construction");
      } else {
         System.arraycopy(key, 0, this.keyBytes, 0, this.keyBytes.length);
      }
   }

   public DESedeKeySpec(byte[] key, int offset) throws InvalidKeyException {
      if (key.length - offset < 24) {
         throw new InvalidKeyException("DESede key material too short in construction");
      } else {
         System.arraycopy(key, 0, this.keyBytes, 0, this.keyBytes.length);
      }
   }

   public byte[] getKey() {
      byte[] tmp = new byte[24];
      System.arraycopy(this.keyBytes, 0, tmp, 0, tmp.length);
      return tmp;
   }

   public static boolean isParityAdjusted(byte[] key, int offset) throws InvalidKeyException {
      if (key.length - offset < 24) {
         throw new InvalidKeyException("key material too short in DESedeKeySpec.isParityAdjusted");
      } else {
         return DESKeySpec.isParityAdjusted(key, offset) && DESKeySpec.isParityAdjusted(key, offset + 8) && DESKeySpec.isParityAdjusted(key, offset + 16);
      }
   }
}
