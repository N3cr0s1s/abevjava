package org.bouncycastle.bcpg;

public class InputStreamPacket extends Packet {
   private BCPGInputStream in;

   public InputStreamPacket(BCPGInputStream in) {
      this.in = in;
   }

   public BCPGInputStream getInputStream() {
      return this.in;
   }
}
