package org.bouncycastle.openpgp;

import org.bouncycastle.bcpg.UserAttributeSubpacket;
import org.bouncycastle.bcpg.attr.ImageAttribute;

public class PGPUserAttributeSubpacketVector {
   UserAttributeSubpacket[] packets;

   PGPUserAttributeSubpacketVector(UserAttributeSubpacket[] packets) {
      this.packets = packets;
   }

   public UserAttributeSubpacket getSubpacket(int type) {
      for(int i = 0; i != this.packets.length; ++i) {
         if (this.packets[i].getType() == type) {
            return this.packets[i];
         }
      }

      return null;
   }

   public ImageAttribute getImageAttribute() {
      UserAttributeSubpacket p = this.getSubpacket(1);
      return p == null ? null : (ImageAttribute)p;
   }

   UserAttributeSubpacket[] toSubpacketArray() {
      return this.packets;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof PGPUserAttributeSubpacketVector)) {
         return false;
      } else {
         PGPUserAttributeSubpacketVector other = (PGPUserAttributeSubpacketVector)o;
         if (other.packets.length != this.packets.length) {
            return false;
         } else {
            for(int i = 0; i != this.packets.length; ++i) {
               if (!other.packets[i].equals(this.packets[i])) {
                  return false;
               }
            }

            return true;
         }
      }
   }

   public int hashCode() {
      int code = 0;

      for(int i = 0; i != this.packets.length; ++i) {
         code ^= this.packets[i].hashCode();
      }

      return code;
   }
}
