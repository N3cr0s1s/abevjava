package org.bouncycastle.bcpg;

import java.io.IOException;
import java.util.Date;

public class PublicSubkeyPacket extends PublicKeyPacket {
   PublicSubkeyPacket(BCPGInputStream in) throws IOException {
      super(in);
   }

   public PublicSubkeyPacket(int algorithm, Date time, BCPGKey key) {
      super(algorithm, time, key);
   }

   public void encode(BCPGOutputStream out) throws IOException {
      out.writePacket(14, this.getEncodedContents(), true);
   }
}
