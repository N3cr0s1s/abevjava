package org.bouncycastle.bcpg;

import java.io.IOException;

public class UserIDPacket extends ContainedPacket {
   private byte[] idData;

   public UserIDPacket(BCPGInputStream in) throws IOException {
      this.idData = new byte[in.available()];
      in.readFully(this.idData);
   }

   public UserIDPacket(String id) {
      this.idData = new byte[id.length()];

      for(int i = 0; i != id.length(); ++i) {
         this.idData[i] = (byte)id.charAt(i);
      }

   }

   public String getID() {
      char[] chars = new char[this.idData.length];

      for(int i = 0; i != chars.length; ++i) {
         chars[i] = (char)(this.idData[i] & 255);
      }

      return new String(chars);
   }

   public void encode(BCPGOutputStream out) throws IOException {
      out.writePacket(13, this.idData, true);
   }
}
