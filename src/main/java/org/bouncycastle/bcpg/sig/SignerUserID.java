package org.bouncycastle.bcpg.sig;

import org.bouncycastle.bcpg.SignatureSubpacket;

public class SignerUserID extends SignatureSubpacket {
   private static byte[] userIDToBytes(String id) {
      byte[] idData = new byte[id.length()];

      for(int i = 0; i != id.length(); ++i) {
         idData[i] = (byte)id.charAt(i);
      }

      return idData;
   }

   public SignerUserID(boolean critical, byte[] data) {
      super(28, critical, data);
   }

   public SignerUserID(boolean critical, String userID) {
      super(28, critical, userIDToBytes(userID));
   }

   public String getID() {
      char[] chars = new char[this.data.length];

      for(int i = 0; i != chars.length; ++i) {
         chars[i] = (char)(this.data[i] & 255);
      }

      return new String(chars);
   }
}
