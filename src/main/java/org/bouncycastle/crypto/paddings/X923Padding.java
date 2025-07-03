package org.bouncycastle.crypto.paddings;

import java.security.SecureRandom;
import org.bouncycastle.crypto.InvalidCipherTextException;

public class X923Padding implements BlockCipherPadding {
   SecureRandom random = null;

   public void init(SecureRandom random) throws IllegalArgumentException {
      this.random = random;
   }

   public String getPaddingName() {
      return "X9.23";
   }

   public int addPadding(byte[] in, int inOff) {
      byte code;
      for(code = (byte)(in.length - inOff); inOff < in.length - 1; ++inOff) {
         if (this.random == null) {
            in[inOff] = 0;
         } else {
            in[inOff] = (byte)this.random.nextInt();
         }
      }

      in[inOff] = code;
      return code;
   }

   public int padCount(byte[] in) throws InvalidCipherTextException {
      int count = in[in.length - 1] & 255;
      if (count > in.length) {
         throw new InvalidCipherTextException("pad block corrupted");
      } else {
         return count;
      }
   }
}
