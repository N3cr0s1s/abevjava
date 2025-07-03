package org.bouncycastle.bcpg;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;

public class RSAPublicBCPGKey extends BCPGObject implements BCPGKey {
   MPInteger n;
   MPInteger e;

   public RSAPublicBCPGKey(BCPGInputStream in) throws IOException {
      this.n = new MPInteger(in);
      this.e = new MPInteger(in);
   }

   public RSAPublicBCPGKey(BigInteger n, BigInteger e) {
      this.n = new MPInteger(n);
      this.e = new MPInteger(e);
   }

   public BigInteger getPublicExponent() {
      return this.e.getValue();
   }

   public BigInteger getModulus() {
      return this.n.getValue();
   }

   public String getFormat() {
      return "PGP";
   }

   public byte[] getEncoded() {
      try {
         ByteArrayOutputStream bOut = new ByteArrayOutputStream();
         BCPGOutputStream pgpOut = new BCPGOutputStream(bOut);
         pgpOut.writeObject(this);
         return bOut.toByteArray();
      } catch (IOException var3) {
         return null;
      }
   }

   public void encode(BCPGOutputStream out) throws IOException {
      out.writeObject(this.n);
      out.writeObject(this.e);
   }
}
