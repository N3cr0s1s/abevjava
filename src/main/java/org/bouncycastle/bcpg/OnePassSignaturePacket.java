package org.bouncycastle.bcpg;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class OnePassSignaturePacket extends ContainedPacket {
   private int version;
   private int sigType;
   private int hashAlgorithm;
   private int keyAlgorithm;
   private long keyID;
   private int nested;

   OnePassSignaturePacket(BCPGInputStream in) throws IOException {
      this.version = in.read();
      this.sigType = in.read();
      this.hashAlgorithm = in.read();
      this.keyAlgorithm = in.read();
      this.keyID |= (long)in.read() << 56;
      this.keyID |= (long)in.read() << 48;
      this.keyID |= (long)in.read() << 40;
      this.keyID |= (long)in.read() << 32;
      this.keyID |= (long)in.read() << 24;
      this.keyID |= (long)in.read() << 16;
      this.keyID |= (long)in.read() << 8;
      this.keyID |= (long)in.read();
      this.nested = in.read();
   }

   public OnePassSignaturePacket(int sigType, int hashAlgorithm, int keyAlgorithm, long keyID, boolean isNested) {
      this.version = 3;
      this.sigType = sigType;
      this.hashAlgorithm = hashAlgorithm;
      this.keyAlgorithm = keyAlgorithm;
      this.keyID = keyID;
      this.nested = isNested ? 0 : 1;
   }

   public int getSignatureType() {
      return this.sigType;
   }

   public int getKeyAlgorithm() {
      return this.keyAlgorithm;
   }

   public int getHashAlgorithm() {
      return this.hashAlgorithm;
   }

   public long getKeyID() {
      return this.keyID;
   }

   public void encode(BCPGOutputStream out) throws IOException {
      ByteArrayOutputStream bOut = new ByteArrayOutputStream();
      BCPGOutputStream pOut = new BCPGOutputStream(bOut);
      pOut.write(this.version);
      pOut.write(this.sigType);
      pOut.write(this.hashAlgorithm);
      pOut.write(this.keyAlgorithm);
      pOut.write((byte)((int)(this.keyID >> 56)));
      pOut.write((byte)((int)(this.keyID >> 48)));
      pOut.write((byte)((int)(this.keyID >> 40)));
      pOut.write((byte)((int)(this.keyID >> 32)));
      pOut.write((byte)((int)(this.keyID >> 24)));
      pOut.write((byte)((int)(this.keyID >> 16)));
      pOut.write((byte)((int)(this.keyID >> 8)));
      pOut.write((byte)((int)this.keyID));
      pOut.write(this.nested);
      out.writePacket(4, bOut.toByteArray(), true);
   }
}
