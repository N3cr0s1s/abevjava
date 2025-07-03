package org.bouncycastle.bcpg;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class SecretKeyPacket extends ContainedPacket implements PublicKeyAlgorithmTags {
   private PublicKeyPacket pubKeyPacket;
   private byte[] secKeyData;
   private int stringToKey;
   private int encAlgorithm;
   private S2K s2k;
   private byte[] iv;

   SecretKeyPacket(BCPGInputStream in) throws IOException {
      this.pubKeyPacket = new PublicKeyPacket(in);
      this.stringToKey = in.read();
      if (this.stringToKey != 255 && this.stringToKey != 254) {
         this.encAlgorithm = this.stringToKey;
      } else {
         this.encAlgorithm = in.read();
         this.s2k = new S2K(in);
      }

      if ((this.s2k == null || this.s2k.getType() != 101 || this.s2k.getProtectionMode() != 1) && this.stringToKey != 0) {
         if (this.encAlgorithm < 7) {
            this.iv = new byte[8];
         } else {
            this.iv = new byte[16];
         }

         in.readFully(this.iv, 0, this.iv.length);
      }

      if (in.available() != 0) {
         ByteArrayOutputStream bOut = new ByteArrayOutputStream(in.available());

         int b;
         while((b = in.read()) >= 0) {
            bOut.write(b);
         }

         this.secKeyData = bOut.toByteArray();
      }

   }

   public SecretKeyPacket(PublicKeyPacket pubKeyPacket, int encAlgorithm, S2K s2k, byte[] iv, byte[] secKeyData) {
      this.pubKeyPacket = pubKeyPacket;
      this.encAlgorithm = encAlgorithm;
      if (encAlgorithm != 0) {
         this.stringToKey = 255;
      } else {
         this.stringToKey = 0;
      }

      this.s2k = s2k;
      this.iv = iv;
      this.secKeyData = secKeyData;
   }

   public int getEncAlgorithm() {
      return this.encAlgorithm;
   }

   public byte[] getIV() {
      return this.iv;
   }

   public S2K getS2K() {
      return this.s2k;
   }

   public PublicKeyPacket getPublicKeyPacket() {
      return this.pubKeyPacket;
   }

   public byte[] getSecretKeyData() {
      return this.secKeyData;
   }

   public byte[] getEncodedContents() throws IOException {
      ByteArrayOutputStream bOut = new ByteArrayOutputStream();
      BCPGOutputStream pOut = new BCPGOutputStream(bOut);
      pOut.write(this.pubKeyPacket.getEncodedContents());
      pOut.write(this.stringToKey);
      if (this.stringToKey == 255 || this.stringToKey == 254) {
         pOut.write(this.encAlgorithm);
         pOut.writeObject(this.s2k);
      }

      if (this.iv != null) {
         pOut.write(this.iv);
      }

      if (this.secKeyData != null) {
         pOut.write(this.secKeyData);
      }

      return bOut.toByteArray();
   }

   public void encode(BCPGOutputStream out) throws IOException {
      out.writePacket(5, this.getEncodedContents(), true);
   }
}
