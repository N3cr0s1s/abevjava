package org.bouncycastle.bcpg;

import java.io.IOException;

public class SecretSubkeyPacket extends SecretKeyPacket {
   SecretSubkeyPacket(BCPGInputStream in) throws IOException {
      super(in);
   }

   public SecretSubkeyPacket(PublicKeyPacket pubKeyPacket, int encAlgorithm, S2K s2k, byte[] iv, byte[] secKeyData) {
      super(pubKeyPacket, encAlgorithm, s2k, iv, secKeyData);
   }

   public void encode(BCPGOutputStream out) throws IOException {
      out.writePacket(7, this.getEncodedContents(), true);
   }
}
