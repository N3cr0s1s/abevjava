package org.bouncycastle.crypto.spec;

import java.security.InvalidKeyException;
import java.security.spec.KeySpec;

public class DESKeySpec implements KeySpec {
   public static final int DES_KEY_LEN = 8;
   private byte[] keyBytes = new byte[8];
   private static final int N_DES_WEAK_KEYS = 16;
   private static byte[] DES_weak_keys = new byte[]{1, 1, 1, 1, 1, 1, 1, 1, 31, 31, 31, 31, 14, 14, 14, 14, -32, -32, -32, -32, -15, -15, -15, -15, -2, -2, -2, -2, -2, -2, -2, -2, 1, -2, 1, -2, 1, -2, 1, -2, 31, -32, 31, -32, 14, -15, 14, -15, 1, -32, 1, -32, 1, -15, 1, -15, 31, -2, 31, -2, 14, -2, 14, -2, 1, 31, 1, 31, 1, 14, 1, 14, -32, -2, -32, -2, -15, -2, -15, -2, -2, 1, -2, 1, -2, 1, -2, 1, -32, 31, -32, 31, -15, 14, -15, 14, -32, 1, -32, 1, -15, 1, -15, 1, -2, 31, -2, 31, -2, 14, -2, 14, 31, 1, 31, 1, 14, 1, 14, 1, -2, -32, -2, -32, -2, -15, -2, -15};

   public DESKeySpec(byte[] key) throws InvalidKeyException {
      if (key.length < 8) {
         throw new InvalidKeyException("DES key material too short in construction");
      } else {
         System.arraycopy(key, 0, this.keyBytes, 0, this.keyBytes.length);
      }
   }

   public DESKeySpec(byte[] key, int offset) throws InvalidKeyException {
      if (key.length - offset < 8) {
         throw new InvalidKeyException("DES key material too short in construction");
      } else {
         System.arraycopy(key, offset, this.keyBytes, 0, this.keyBytes.length);
      }
   }

   public byte[] getKey() {
      byte[] tmp = new byte[8];
      System.arraycopy(this.keyBytes, 0, tmp, 0, tmp.length);
      return tmp;
   }

   public static boolean isParityAdjusted(byte[] key, int offset) throws InvalidKeyException {
      if (key.length - offset < 8) {
         throw new InvalidKeyException("key material too short in DESKeySpec.isParityAdjusted");
      } else {
         for(int i = 0; i < 8; ++i) {
            byte keyByte = key[i + offset];

            int count;
            for(count = 0; keyByte != 0; keyByte = (byte)(keyByte >>> 1)) {
               if ((keyByte & 1) != 0) {
                  ++count;
               }
            }

            if ((count & 1) == 1) {
               if ((key[i + offset] & 1) == 1) {
                  return false;
               }
            } else if ((key[i + offset] & 1) != 1) {
               return false;
            }
         }

         return true;
      }
   }

   public static boolean isWeak(byte[] key, int offset) throws InvalidKeyException {
      if (key.length - offset < 8) {
         throw new InvalidKeyException("key material too short in DESKeySpec.isWeak");
      } else {
         label28:
         for(int i = 0; i < 16; ++i) {
            for(int j = 0; j < 8; ++j) {
               if (key[j + offset] != DES_weak_keys[i * 8 + j]) {
                  continue label28;
               }
            }

            return true;
         }

         return false;
      }
   }
}
