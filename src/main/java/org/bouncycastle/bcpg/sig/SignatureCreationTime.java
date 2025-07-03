package org.bouncycastle.bcpg.sig;

import java.util.Date;
import org.bouncycastle.bcpg.SignatureSubpacket;

public class SignatureCreationTime extends SignatureSubpacket {
   protected static byte[] timeToBytes(Date date) {
      byte[] data = new byte[4];
      long t = date.getTime() / 1000L;
      data[0] = (byte)((int)(t >> 24));
      data[1] = (byte)((int)(t >> 16));
      data[2] = (byte)((int)(t >> 8));
      data[3] = (byte)((int)t);
      return data;
   }

   public SignatureCreationTime(boolean critical, byte[] data) {
      super(2, critical, data);
   }

   public SignatureCreationTime(boolean critical, Date date) {
      super(2, critical, timeToBytes(date));
   }

   public Date getTime() {
      long time = (long)(this.data[0] & 255) << 24 | (long)((this.data[1] & 255) << 16) | (long)((this.data[2] & 255) << 8) | (long)(this.data[3] & 255);
      return new Date(time * 1000L);
   }
}
