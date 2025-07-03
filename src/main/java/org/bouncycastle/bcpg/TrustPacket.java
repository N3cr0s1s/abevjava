package org.bouncycastle.bcpg;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class TrustPacket extends ContainedPacket {
   byte[] levelAndTrustAmount;

   public TrustPacket(BCPGInputStream in) throws IOException {
      ByteArrayOutputStream bOut = new ByteArrayOutputStream();

      int ch;
      while((ch = in.read()) >= 0) {
         bOut.write(ch);
      }

      this.levelAndTrustAmount = bOut.toByteArray();
   }

   public TrustPacket(int trustCode) {
      this.levelAndTrustAmount = new byte[1];
      this.levelAndTrustAmount[0] = (byte)trustCode;
   }

   public void encode(BCPGOutputStream out) throws IOException {
      out.writePacket(12, this.levelAndTrustAmount, true);
   }
}
