package org.bouncycastle.bcpg;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;

public class PublicKeyPacket extends ContainedPacket implements PublicKeyAlgorithmTags {
   private int version;
   private long time;
   private int validDays;
   private int algorithm;
   private BCPGKey key;

   PublicKeyPacket(BCPGInputStream in) throws IOException {
      this.version = in.read();
      this.time = (long)in.read() << 24 | (long)(in.read() << 16) | (long)(in.read() << 8) | (long)in.read();
      if (this.version <= 3) {
         this.validDays = in.read() << 8 | in.read();
      }

      this.algorithm = (byte)in.read();
      switch(this.algorithm) {
      case 1:
      case 2:
      case 3:
         this.key = new RSAPublicBCPGKey(in);
         return;
      default:
         throw new IOException("unknown PGP public key algorithm encountered");
      }
   }

   public PublicKeyPacket(int algorithm, Date time, BCPGKey key) {
      this.version = 4;
      this.time = time.getTime() / 1000L;
      this.algorithm = algorithm;
      this.key = key;
   }

   public int getVersion() {
      return this.version;
   }

   public int getAlgorithm() {
      return this.algorithm;
   }

   public int getValidDays() {
      return this.validDays;
   }

   public Date getTime() {
      return new Date(this.time * 1000L);
   }

   public BCPGKey getKey() {
      return this.key;
   }

   public byte[] getEncodedContents() throws IOException {
      ByteArrayOutputStream bOut = new ByteArrayOutputStream();
      BCPGOutputStream pOut = new BCPGOutputStream(bOut);
      pOut.write(this.version);
      pOut.write((byte)((int)(this.time >> 24)));
      pOut.write((byte)((int)(this.time >> 16)));
      pOut.write((byte)((int)(this.time >> 8)));
      pOut.write((byte)((int)this.time));
      if (this.version <= 3) {
         pOut.write((byte)(this.validDays >> 8));
         pOut.write((byte)this.validDays);
      }

      pOut.write(this.algorithm);
      pOut.writeObject((BCPGObject)this.key);
      return bOut.toByteArray();
   }

   public void encode(BCPGOutputStream out) throws IOException {
      out.writePacket(6, this.getEncodedContents(), true);
   }
}
