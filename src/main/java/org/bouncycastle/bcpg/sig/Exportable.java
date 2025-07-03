package org.bouncycastle.bcpg.sig;

import org.bouncycastle.bcpg.SignatureSubpacket;

public class Exportable extends SignatureSubpacket {
   private static final byte[] booleanToByteArray(boolean value) {
      byte[] data = new byte[1];
      if (value) {
         data[0] = 1;
         return data;
      } else {
         return data;
      }
   }

   public Exportable(boolean critical, byte[] data) {
      super(4, critical, data);
   }

   public Exportable(boolean critical, boolean isExportable) {
      super(4, critical, booleanToByteArray(isExportable));
   }

   public boolean isExportable() {
      return this.data[0] != 0;
   }
}
