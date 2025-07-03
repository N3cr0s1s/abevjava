package org.bouncycastle.crypto.paddings;

import java.security.SecureRandom;
import org.bouncycastle.crypto.InvalidCipherTextException;

public class ISO7816d4Padding implements BlockCipherPadding {
   public void init(SecureRandom random) throws IllegalArgumentException {
   }

   public String getPaddingName() {
      return "ISO7816-4";
   }

   public int addPadding(byte[] in, int inOff) {
      int added = in.length - inOff;
      in[inOff] = -128;
      ++inOff;

      while(inOff < in.length) {
         in[inOff] = 0;
         ++inOff;
      }

      return added;
   }

   public int padCount(byte[] in) throws InvalidCipherTextException {
      int count;
      for(count = in.length - 1; count > 0 && in[count] == 0; --count) {
      }

      if (in[count] != -128) {
         throw new InvalidCipherTextException("pad block corrupted");
      } else {
         return in.length - count;
      }
   }
}
