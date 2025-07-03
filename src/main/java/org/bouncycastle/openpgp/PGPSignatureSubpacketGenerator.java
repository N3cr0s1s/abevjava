package org.bouncycastle.openpgp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.bouncycastle.bcpg.SignatureSubpacket;
import org.bouncycastle.bcpg.sig.Exportable;
import org.bouncycastle.bcpg.sig.KeyExpirationTime;
import org.bouncycastle.bcpg.sig.KeyFlags;
import org.bouncycastle.bcpg.sig.PreferredAlgorithms;
import org.bouncycastle.bcpg.sig.PrimaryUserID;
import org.bouncycastle.bcpg.sig.Revocable;
import org.bouncycastle.bcpg.sig.SignatureCreationTime;
import org.bouncycastle.bcpg.sig.SignatureExpirationTime;
import org.bouncycastle.bcpg.sig.SignerUserID;
import org.bouncycastle.bcpg.sig.TrustSignature;

public class PGPSignatureSubpacketGenerator {
   List list = new ArrayList();

   public void setRevocable(boolean isCritical, boolean isRevocable) {
      this.list.add(new Revocable(isCritical, isRevocable));
   }

   public void setExportable(boolean isCritical, boolean isExportable) {
      this.list.add(new Exportable(isCritical, isExportable));
   }

   public void setTrust(boolean isCritical, int depth, int trustAmount) {
      this.list.add(new TrustSignature(isCritical, depth, trustAmount));
   }

   public void setKeyExpirationTime(boolean isCritical, long seconds) {
      this.list.add(new KeyExpirationTime(isCritical, seconds));
   }

   public void setSignatureExpirationTime(boolean isCritical, long seconds) {
      this.list.add(new SignatureExpirationTime(isCritical, seconds));
   }

   public void setSignatureCreationTime(boolean isCritical, Date date) {
      this.list.add(new SignatureCreationTime(isCritical, date));
   }

   public void setPreferredHashAlgorithms(boolean isCritical, int[] algorithms) {
      this.list.add(new PreferredAlgorithms(21, isCritical, algorithms));
   }

   public void setPreferredSymmetricAlgorithms(boolean isCritical, int[] algorithms) {
      this.list.add(new PreferredAlgorithms(11, isCritical, algorithms));
   }

   public void setPreferredCompressionAlgorithms(boolean isCritical, int[] algorithms) {
      this.list.add(new PreferredAlgorithms(22, isCritical, algorithms));
   }

   public void setKeyFlags(boolean isCritical, int flags) {
      this.list.add(new KeyFlags(isCritical, flags));
   }

   public void setSignerUserID(boolean isCritical, String userID) {
      if (userID == null) {
         throw new IllegalArgumentException("attempt to set null SignerUserID");
      } else {
         this.list.add(new SignerUserID(isCritical, userID));
      }
   }

   public void setPrimaryUserID(boolean isCritical, boolean isPrimaryUserID) {
      this.list.add(new PrimaryUserID(isCritical, isPrimaryUserID));
   }

   public PGPSignatureSubpacketVector generate() {
      return new PGPSignatureSubpacketVector((SignatureSubpacket[])this.list.toArray(new SignatureSubpacket[this.list.size()]));
   }
}
