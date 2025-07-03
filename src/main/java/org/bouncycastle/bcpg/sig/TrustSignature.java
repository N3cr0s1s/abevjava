package org.bouncycastle.bcpg.sig;

import org.bouncycastle.bcpg.SignatureSubpacket;

public class TrustSignature extends SignatureSubpacket {
   private static final byte[] intToByteArray(int v1, int v2) {
      byte[] data = new byte[]{(byte)v1, (byte)v2};
      return data;
   }

   public TrustSignature(boolean critical, byte[] data) {
      super(5, critical, data);
   }

   public TrustSignature(boolean critical, int depth, int trustAmount) {
      super(5, critical, intToByteArray(depth, trustAmount));
   }

   public int getDepth() {
      return this.data[0] & 255;
   }

   public int getTrustAmount() {
      return this.data[1] & 255;
   }
}
