package org.bouncycastle.bcpg;

import java.io.IOException;

public class MarkerPacket extends ContainedPacket {
   byte[] marker = new byte[]{80, 71, 80};

   public MarkerPacket(BCPGInputStream in) throws IOException {
      in.readFully(this.marker);
   }

   public void encode(BCPGOutputStream out) throws IOException {
      out.writePacket(10, this.marker, true);
   }
}
