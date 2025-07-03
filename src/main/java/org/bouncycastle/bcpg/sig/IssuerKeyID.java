package org.bouncycastle.bcpg.sig;

import org.bouncycastle.bcpg.SignatureSubpacket;

public class IssuerKeyID extends SignatureSubpacket {
   protected static byte[] keyIDToBytes(long keyId) {
      byte[] data = new byte[]{(byte)((int)(keyId >> 56)), (byte)((int)(keyId >> 48)), (byte)((int)(keyId >> 40)), (byte)((int)(keyId >> 32)), (byte)((int)(keyId >> 24)), (byte)((int)(keyId >> 16)), (byte)((int)(keyId >> 8)), (byte)((int)keyId)};
      return data;
   }

   public IssuerKeyID(boolean critical, byte[] data) {
      super(16, critical, data);
   }

   public IssuerKeyID(boolean critical, long keyID) {
      super(16, critical, keyIDToBytes(keyID));
   }

   public long getKeyID() {
      long keyID = (long)(this.data[0] & 255) << 56 | (long)(this.data[1] & 255) << 48 | (long)(this.data[2] & 255) << 40 | (long)(this.data[3] & 255) << 32 | (long)(this.data[4] & 255) << 24 | (long)((this.data[5] & 255) << 16) | (long)((this.data[6] & 255) << 8) | (long)(this.data[7] & 255);
      return keyID;
   }
}
