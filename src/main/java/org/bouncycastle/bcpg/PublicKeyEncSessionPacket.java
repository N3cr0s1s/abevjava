package org.bouncycastle.bcpg;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;

public class PublicKeyEncSessionPacket extends ContainedPacket implements PublicKeyAlgorithmTags {
   private int version;
   private long keyID;
   private int algorithm;
   private BigInteger[] data;

   PublicKeyEncSessionPacket(BCPGInputStream in) throws IOException {
      this.version = in.read();
      this.keyID |= (long)in.read() << 56;
      this.keyID |= (long)in.read() << 48;
      this.keyID |= (long)in.read() << 40;
      this.keyID |= (long)in.read() << 32;
      this.keyID |= (long)in.read() << 24;
      this.keyID |= (long)in.read() << 16;
      this.keyID |= (long)in.read() << 8;
      this.keyID |= (long)in.read();
      this.algorithm = in.read();
      switch(this.algorithm) {
      case 1:
      case 2:
         this.data = new BigInteger[1];
         this.data[0] = (new MPInteger(in)).getValue();
         break;
      case 16:
      case 20:
         this.data = new BigInteger[2];
         this.data[0] = (new MPInteger(in)).getValue();
         this.data[1] = (new MPInteger(in)).getValue();
         break;
      default:
         throw new IOException("unknown PGP public key algorithm encountered");
      }

   }

   public PublicKeyEncSessionPacket(long keyID, int algorithm, BigInteger[] data) {
      this.version = 3;
      this.keyID = keyID;
      this.algorithm = algorithm;
      this.data = data;
   }

   public int getVersion() {
      return this.version;
   }

   public long getKeyID() {
      return this.keyID;
   }

   public int getAlgorithm() {
      return this.algorithm;
   }

   public BigInteger[] getEncSessionKey() {
      return this.data;
   }

   public void encode(BCPGOutputStream out) throws IOException {
      ByteArrayOutputStream bOut = new ByteArrayOutputStream();
      BCPGOutputStream pOut = new BCPGOutputStream(bOut);
      pOut.write(this.version);
      pOut.write((byte)((int)(this.keyID >> 56)));
      pOut.write((byte)((int)(this.keyID >> 48)));
      pOut.write((byte)((int)(this.keyID >> 40)));
      pOut.write((byte)((int)(this.keyID >> 32)));
      pOut.write((byte)((int)(this.keyID >> 24)));
      pOut.write((byte)((int)(this.keyID >> 16)));
      pOut.write((byte)((int)(this.keyID >> 8)));
      pOut.write((byte)((int)this.keyID));
      pOut.write(this.algorithm);

      for(int i = 0; i != this.data.length; ++i) {
         pOut.writeObject(new MPInteger(this.data[i]));
      }

      out.writePacket(1, bOut.toByteArray(), true);
   }
}
