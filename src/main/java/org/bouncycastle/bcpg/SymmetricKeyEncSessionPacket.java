package org.bouncycastle.bcpg;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class SymmetricKeyEncSessionPacket extends ContainedPacket {
   private int version;
   private int encAlgorithm;
   private S2K s2k;
   private byte[] secKeyData;

   public SymmetricKeyEncSessionPacket(BCPGInputStream in) throws IOException {
      this.version = in.read();
      this.encAlgorithm = in.read();
      this.s2k = new S2K(in);
      if (in.available() != 0) {
         this.secKeyData = new byte[in.available()];
         in.readFully(this.secKeyData, 0, this.secKeyData.length);
      }

   }

   public SymmetricKeyEncSessionPacket(int encAlgorithm, S2K s2k, byte[] secKeyData) {
      this.version = 4;
      this.encAlgorithm = encAlgorithm;
      this.s2k = s2k;
      this.secKeyData = secKeyData;
   }

   public int getEncAlgorithm() {
      return this.encAlgorithm;
   }

   public S2K getS2K() {
      return this.s2k;
   }

   public byte[] getSecKeyData() {
      return this.secKeyData;
   }

   public int getVersion() {
      return this.version;
   }

   public void encode(BCPGOutputStream out) throws IOException {
      ByteArrayOutputStream bOut = new ByteArrayOutputStream();
      BCPGOutputStream pOut = new BCPGOutputStream(bOut);
      pOut.write(this.version);
      pOut.write(this.encAlgorithm);
      pOut.writeObject(this.s2k);
      if (this.secKeyData != null) {
         pOut.write(this.secKeyData);
      }

      out.writePacket(3, bOut.toByteArray(), true);
   }
}
