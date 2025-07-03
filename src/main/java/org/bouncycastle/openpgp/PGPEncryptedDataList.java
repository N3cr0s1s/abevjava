package org.bouncycastle.openpgp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.bouncycastle.bcpg.BCPGInputStream;
import org.bouncycastle.bcpg.InputStreamPacket;
import org.bouncycastle.bcpg.PublicKeyEncSessionPacket;
import org.bouncycastle.bcpg.SymmetricKeyEncSessionPacket;

public class PGPEncryptedDataList {
   List list = new ArrayList();
   InputStreamPacket data;

   public PGPEncryptedDataList(BCPGInputStream pIn) throws IOException {
      while(pIn.nextPacketTag() == 1 || pIn.nextPacketTag() == 3) {
         this.list.add(pIn.readPacket());
      }

      this.data = (InputStreamPacket)pIn.readPacket();

      for(int i = 0; i != this.list.size(); ++i) {
         if (this.list.get(i) instanceof SymmetricKeyEncSessionPacket) {
            this.list.set(i, new PGPPBEEncryptedData((SymmetricKeyEncSessionPacket)this.list.get(i), this.data));
         } else {
            this.list.set(i, new PGPPublicKeyEncryptedData((PublicKeyEncSessionPacket)this.list.get(i), this.data));
         }
      }

   }

   public Object get(int index) {
      return this.list.get(index);
   }

   public int size() {
      return this.list.size();
   }

   public boolean isEmpty() {
      return this.list.isEmpty();
   }

   /** @deprecated */
   public Iterator getEncyptedDataObjects() {
      return this.list.iterator();
   }

   public Iterator getEncryptedDataObjects() {
      return this.list.iterator();
   }
}
