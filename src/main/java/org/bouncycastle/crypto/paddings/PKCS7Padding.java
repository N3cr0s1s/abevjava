package org.bouncycastle.crypto.paddings;

import java.security.SecureRandom;
import org.bouncycastle.crypto.InvalidCipherTextException;

public class PKCS7Padding implements BlockCipherPadding {
   public void init(SecureRandom random) throws IllegalArgumentException {
   }

   public String getPaddingName() {
      return "PKCS7";
   }

   public int addPadding(byte[] in, int inOff) {
      byte code;
      for(code = (byte)(in.length - inOff); inOff < in.length; ++inOff) {
         in[inOff] = code;
      }

      return code;
   }

   public int padCount(byte[] in) throws InvalidCipherTextException {
      int count = in[in.length - 1] & 255;
      if (count > in.length) {
         throw new InvalidCipherTextException("pad block corrupted");
      } else {
         for(int i = 1; i <= count; ++i) {
            if (in[in.length - i] != count) {
               throw new InvalidCipherTextException("pad block corrupted");
            }
         }

         return count;
      }
   }
}
