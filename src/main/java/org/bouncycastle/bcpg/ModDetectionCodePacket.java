package org.bouncycastle.bcpg;

import java.io.IOException;

public class ModDetectionCodePacket extends ContainedPacket {
   private byte[] digest;

   ModDetectionCodePacket(BCPGInputStream in) throws IOException {
      this.digest = new byte[20];
      in.readFully(this.digest);
   }

   public ModDetectionCodePacket(byte[] digest) throws IOException {
      this.digest = new byte[digest.length];
      System.arraycopy(digest, 0, this.digest, 0, this.digest.length);
   }

   public byte[] getDigest() {
      byte[] tmp = new byte[this.digest.length];
      System.arraycopy(this.digest, 0, tmp, 0, tmp.length);
      return tmp;
   }

   public void encode(BCPGOutputStream out) throws IOException {
      out.writePacket(19, this.digest, false);
   }
}
