package org.bouncycastle.bcpg.sig;

import org.bouncycastle.bcpg.SignatureSubpacket;

public class KeyExpirationTime extends SignatureSubpacket {
   protected static byte[] timeToBytes(long t) {
      byte[] data = new byte[]{(byte)((int)(t >> 24)), (byte)((int)(t >> 16)), (byte)((int)(t >> 8)), (byte)((int)t)};
      return data;
   }

   public KeyExpirationTime(boolean critical, byte[] data) {
      super(9, critical, data);
   }

   public KeyExpirationTime(boolean critical, long seconds) {
      super(9, critical, timeToBytes(seconds));
   }

   public long getTime() {
      long time = (long)(this.data[0] & 255) << 24 | (long)((this.data[1] & 255) << 16) | (long)((this.data[2] & 255) << 8) | (long)(this.data[3] & 255);
      return time;
   }
}
