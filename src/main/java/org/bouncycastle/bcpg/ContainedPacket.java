package org.bouncycastle.bcpg;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public abstract class ContainedPacket extends Packet {
   public byte[] getEncoded() throws IOException {
      ByteArrayOutputStream bOut = new ByteArrayOutputStream();
      BCPGOutputStream pOut = new BCPGOutputStream(bOut);
      pOut.writePacket(this);
      return bOut.toByteArray();
   }

   public abstract void encode(BCPGOutputStream var1) throws IOException;
}
