package org.bouncycastle.openpgp;

import java.util.Date;
import org.bouncycastle.bcpg.SignatureSubpacket;
import org.bouncycastle.bcpg.sig.IssuerKeyID;
import org.bouncycastle.bcpg.sig.KeyExpirationTime;
import org.bouncycastle.bcpg.sig.KeyFlags;
import org.bouncycastle.bcpg.sig.PreferredAlgorithms;
import org.bouncycastle.bcpg.sig.SignatureCreationTime;
import org.bouncycastle.bcpg.sig.SignatureExpirationTime;
import org.bouncycastle.bcpg.sig.SignerUserID;

public class PGPSignatureSubpacketVector {
   SignatureSubpacket[] packets;

   PGPSignatureSubpacketVector(SignatureSubpacket[] packets) {
      this.packets = packets;
   }

   public SignatureSubpacket getSubpacket(int type) {
      for(int i = 0; i != this.packets.length; ++i) {
         if (this.packets[i].getType() == type) {
            return this.packets[i];
         }
      }

      return null;
   }

   public long getIssuerKeyID() {
      SignatureSubpacket p = this.getSubpacket(16);
      return p == null ? 0L : ((IssuerKeyID)p).getKeyID();
   }

   public Date getSignatureCreationTime() {
      SignatureSubpacket p = this.getSubpacket(2);
      return p == null ? null : ((SignatureCreationTime)p).getTime();
   }

   public long getSignatureExpirationTime() {
      SignatureSubpacket p = this.getSubpacket(3);
      return p == null ? 0L : ((SignatureExpirationTime)p).getTime();
   }

   public long getKeyExpirationTime() {
      SignatureSubpacket p = this.getSubpacket(9);
      return p == null ? 0L : ((KeyExpirationTime)p).getTime();
   }

   public int[] getPreferredHashAlgorithms() {
      SignatureSubpacket p = this.getSubpacket(21);
      return p == null ? null : ((PreferredAlgorithms)p).getPreferences();
   }

   public int[] getPreferredSymmetricAlgorithms() {
      SignatureSubpacket p = this.getSubpacket(11);
      return p == null ? null : ((PreferredAlgorithms)p).getPreferences();
   }

   public int[] getPreferredCompressionAlgorithms() {
      SignatureSubpacket p = this.getSubpacket(22);
      return p == null ? null : ((PreferredAlgorithms)p).getPreferences();
   }

   public int getKeyFlags() {
      SignatureSubpacket p = this.getSubpacket(27);
      return p == null ? 0 : ((KeyFlags)p).getFlags();
   }

   public String getSignerUserID() {
      SignatureSubpacket p = this.getSubpacket(28);
      return p == null ? null : ((SignerUserID)p).getID();
   }

   public int[] getCriticalTags() {
      int count = 0;

      for(int i = 0; i != this.packets.length; ++i) {
         if (this.packets[i].isCritical()) {
            ++count;
         }
      }

      int[] list = new int[count];
      count = 0;

      for(int i = 0; i != this.packets.length; ++i) {
         if (this.packets[i].isCritical()) {
            list[count++] = this.packets[i].getType();
         }
      }

      return list;
   }

   public int size() {
      return this.packets.length;
   }

   SignatureSubpacket[] toSubpacketArray() {
      return this.packets;
   }
}
