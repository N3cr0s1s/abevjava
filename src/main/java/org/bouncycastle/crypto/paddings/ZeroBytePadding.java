package org.bouncycastle.crypto.paddings;

import java.security.SecureRandom;
import org.bouncycastle.crypto.InvalidCipherTextException;

public class ZeroBytePadding implements BlockCipherPadding {
   public void init(SecureRandom random) throws IllegalArgumentException {
   }

   public String getPaddingName() {
      return "ZeroByte";
   }

   public int addPadding(byte[] in, int inOff) {
      int added;
      for(added = in.length - inOff; inOff < in.length; ++inOff) {
         in[inOff] = 0;
      }

      return added;
   }

   public int padCount(byte[] in) throws InvalidCipherTextException {
      int count;
      for(count = in.length; count > 0 && in[count - 1] == 0; --count) {
      }

      return in.length - count;
   }
}
