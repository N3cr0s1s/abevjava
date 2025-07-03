package org.bouncycastle.bcpg.sig;

import org.bouncycastle.bcpg.SignatureSubpacket;

public class KeyFlags extends SignatureSubpacket {
   private static final byte[] intToByteArray(int v) {
      byte[] data = new byte[]{(byte)v};
      return data;
   }

   public KeyFlags(boolean critical, byte[] data) {
      super(27, critical, data);
   }

   public KeyFlags(boolean critical, int flags) {
      super(27, critical, intToByteArray(flags));
   }

   public int getFlags() {
      return this.data[0] & 255;
   }
}
