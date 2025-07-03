package org.bouncycastle.bcpg;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;

public class RSASecretBCPGKey extends BCPGObject implements BCPGKey {
   MPInteger d;
   MPInteger p;
   MPInteger q;
   MPInteger u;
   BigInteger expP;
   BigInteger expQ;
   BigInteger crt;

   public RSASecretBCPGKey(BCPGInputStream in) throws IOException {
      this.d = new MPInteger(in);
      this.p = new MPInteger(in);
      this.q = new MPInteger(in);
      this.u = new MPInteger(in);
      this.expP = this.d.getValue().remainder(this.p.getValue().subtract(BigInteger.valueOf(1L)));
      this.expQ = this.d.getValue().remainder(this.q.getValue().subtract(BigInteger.valueOf(1L)));
      this.crt = this.q.getValue().modInverse(this.p.getValue());
   }

   public RSASecretBCPGKey(BigInteger d, BigInteger p, BigInteger q) {
      if (p.compareTo(q) > 0) {
         BigInteger tmp = p;
         p = q;
         q = tmp;
      }

      this.d = new MPInteger(d);
      this.p = new MPInteger(p);
      this.q = new MPInteger(q);
      this.u = new MPInteger(p.modInverse(q));
      this.expP = d.remainder(p.subtract(BigInteger.valueOf(1L)));
      this.expQ = d.remainder(q.subtract(BigInteger.valueOf(1L)));
      this.crt = q.modInverse(p);
   }

   public BigInteger getModulus() {
      return this.p.getValue().multiply(this.q.getValue());
   }

   public BigInteger getPrivateExponent() {
      return this.d.getValue();
   }

   public BigInteger getPrimeP() {
      return this.p.getValue();
   }

   public BigInteger getPrimeQ() {
      return this.q.getValue();
   }

   public BigInteger getPrimeExponentP() {
      return this.expP;
   }

   public BigInteger getPrimeExponentQ() {
      return this.expQ;
   }

   public BigInteger getCrtCoefficient() {
      return this.crt;
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
      out.writeObject(this.d);
      out.writeObject(this.p);
      out.writeObject(this.q);
      out.writeObject(this.u);
   }
}
