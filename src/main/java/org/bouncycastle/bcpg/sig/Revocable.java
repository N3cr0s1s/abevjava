package org.bouncycastle.bcpg.sig;

import org.bouncycastle.bcpg.SignatureSubpacket;

public class Revocable extends SignatureSubpacket {
   private static final byte[] booleanToByteArray(boolean value) {
      byte[] data = new byte[1];
      if (value) {
         data[0] = 1;
         return data;
      } else {
         return data;
      }
   }

   public Revocable(boolean critical, byte[] data) {
      super(7, critical, data);
   }

   public Revocable(boolean critical, boolean isRevocable) {
      super(7, critical, booleanToByteArray(isRevocable));
   }

   public boolean isRevocable() {
      return this.data[0] != 0;
   }
}
