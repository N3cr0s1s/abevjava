package org.bouncycastle.bcpg.sig;

import org.bouncycastle.bcpg.SignatureSubpacket;

public class PrimaryUserID extends SignatureSubpacket {
   private static final byte[] booleanToByteArray(boolean value) {
      byte[] data = new byte[1];
      if (value) {
         data[0] = 1;
         return data;
      } else {
         return data;
      }
   }

   public PrimaryUserID(boolean critical, byte[] data) {
      super(25, critical, data);
   }

   public PrimaryUserID(boolean critical, boolean isPrimaryUserID) {
      super(25, critical, booleanToByteArray(isPrimaryUserID));
   }

   public boolean isPrimaryUserID() {
      return this.data[0] != 0;
   }
}
