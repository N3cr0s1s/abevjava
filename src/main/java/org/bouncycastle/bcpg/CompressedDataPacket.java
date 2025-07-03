package org.bouncycastle.bcpg;

import java.io.IOException;

public class CompressedDataPacket extends InputStreamPacket {
   int algorithm;

   CompressedDataPacket(BCPGInputStream in) throws IOException {
      super(in);
      this.algorithm = in.read();
   }

   public int getAlgorithm() {
      return this.algorithm;
   }
}
