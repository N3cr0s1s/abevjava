package org.bouncycastle.bcpg;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Vector;

public class UserAttributePacket extends ContainedPacket {
   private UserAttributeSubpacket[] subpackets;

   public UserAttributePacket(BCPGInputStream in) throws IOException {
      UserAttributeSubpacketInputStream sIn = new UserAttributeSubpacketInputStream(in);
      Vector v = new Vector();

      UserAttributeSubpacket sub;
      while((sub = sIn.readPacket()) != null) {
         v.addElement(sub);
      }

      this.subpackets = new UserAttributeSubpacket[v.size()];

      for(int i = 0; i != this.subpackets.length; ++i) {
         this.subpackets[i] = (UserAttributeSubpacket)v.elementAt(i);
      }

   }

   public UserAttributePacket(UserAttributeSubpacket[] subpackets) {
      this.subpackets = subpackets;
   }

   public UserAttributeSubpacket[] getSubpackets() {
      return this.subpackets;
   }

   public void encode(BCPGOutputStream out) throws IOException {
      ByteArrayOutputStream bOut = new ByteArrayOutputStream();

      for(int i = 0; i != this.subpackets.length; ++i) {
         this.subpackets[i].encode(bOut);
      }

      out.writePacket(17, bOut.toByteArray(), false);
   }
}
