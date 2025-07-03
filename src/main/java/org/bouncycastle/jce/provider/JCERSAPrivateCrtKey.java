package org.bouncycastle.jce.provider;

import java.math.BigInteger;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.spec.RSAPrivateCrtKeySpec;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERNull;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.pkcs.RSAPrivateKeyStructure;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.crypto.params.RSAKeyParameters;
import org.bouncycastle.crypto.params.RSAPrivateCrtKeyParameters;

public class JCERSAPrivateCrtKey extends JCERSAPrivateKey implements RSAPrivateCrtKey {
   private BigInteger publicExponent;
   private BigInteger primeP;
   private BigInteger primeQ;
   private BigInteger primeExponentP;
   private BigInteger primeExponentQ;
   private BigInteger crtCoefficient;

   JCERSAPrivateCrtKey(RSAPrivateCrtKeyParameters key) {
      super((RSAKeyParameters)key);
      this.publicExponent = key.getPublicExponent();
      this.primeP = key.getP();
      this.primeQ = key.getQ();
      this.primeExponentP = key.getDP();
      this.primeExponentQ = key.getDQ();
      this.crtCoefficient = key.getQInv();
   }

   JCERSAPrivateCrtKey(RSAPrivateCrtKeySpec spec) {
      this.modulus = spec.getModulus();
      this.publicExponent = spec.getPublicExponent();
      this.privateExponent = spec.getPrivateExponent();
      this.primeP = spec.getPrimeP();
      this.primeQ = spec.getPrimeQ();
      this.primeExponentP = spec.getPrimeExponentP();
      this.primeExponentQ = spec.getPrimeExponentQ();
      this.crtCoefficient = spec.getCrtCoefficient();
   }

   JCERSAPrivateCrtKey(RSAPrivateCrtKey key) {
      this.modulus = key.getModulus();
      this.publicExponent = key.getPublicExponent();
      this.privateExponent = key.getPrivateExponent();
      this.primeP = key.getPrimeP();
      this.primeQ = key.getPrimeQ();
      this.primeExponentP = key.getPrimeExponentP();
      this.primeExponentQ = key.getPrimeExponentQ();
      this.crtCoefficient = key.getCrtCoefficient();
   }

   JCERSAPrivateCrtKey(PrivateKeyInfo info) {
      this(new RSAPrivateKeyStructure((ASN1Sequence)info.getPrivateKey()));
   }

   JCERSAPrivateCrtKey(RSAPrivateKeyStructure key) {
      this.modulus = key.getModulus();
      this.publicExponent = key.getPublicExponent();
      this.privateExponent = key.getPrivateExponent();
      this.primeP = key.getPrime1();
      this.primeQ = key.getPrime2();
      this.primeExponentP = key.getExponent1();
      this.primeExponentQ = key.getExponent2();
      this.crtCoefficient = key.getCoefficient();
   }

   public String getFormat() {
      return "PKCS#8";
   }

   public byte[] getEncoded() {
      PrivateKeyInfo info = new PrivateKeyInfo(new AlgorithmIdentifier(PKCSObjectIdentifiers.rsaEncryption, new DERNull()), (new RSAPrivateKeyStructure(this.getModulus(), this.getPublicExponent(), this.getPrivateExponent(), this.getPrimeP(), this.getPrimeQ(), this.getPrimeExponentP(), this.getPrimeExponentQ(), this.getCrtCoefficient())).getDERObject());
      return info.getDEREncoded();
   }

   public BigInteger getPublicExponent() {
      return this.publicExponent;
   }

   public BigInteger getPrimeP() {
      return this.primeP;
   }

   public BigInteger getPrimeQ() {
      return this.primeQ;
   }

   public BigInteger getPrimeExponentP() {
      return this.primeExponentP;
   }

   public BigInteger getPrimeExponentQ() {
      return this.primeExponentQ;
   }

   public BigInteger getCrtCoefficient() {
      return this.crtCoefficient;
   }

   public boolean equals(Object o) {
      if (!(o instanceof RSAPrivateCrtKey)) {
         return false;
      } else if (o == this) {
         return true;
      } else {
         RSAPrivateCrtKey key = (RSAPrivateCrtKey)o;
         return this.getModulus().equals(key.getModulus()) && this.getPublicExponent().equals(key.getPublicExponent()) && this.getPrivateExponent().equals(key.getPrivateExponent()) && this.getPrimeP().equals(key.getPrimeP()) && this.getPrimeQ().equals(key.getPrimeQ()) && this.getPrimeExponentP().equals(key.getPrimeExponentP()) && this.getPrimeExponentQ().equals(key.getPrimeExponentQ()) && this.getCrtCoefficient().equals(key.getCrtCoefficient());
      }
   }

   public String toString() {
      StringBuffer buf = new StringBuffer();
      String nl = System.getProperty("line.separator");
      buf.append("RSA Private CRT Key" + nl);
      buf.append("            modulus: " + this.getModulus().toString(16) + nl);
      buf.append("    public exponent: " + this.getPublicExponent().toString(16) + nl);
      buf.append("   private exponent: " + this.getPrivateExponent().toString(16) + nl);
      buf.append("             primeP: " + this.getPrimeP().toString(16) + nl);
      buf.append("             primeQ: " + this.getPrimeQ().toString(16) + nl);
      buf.append("     primeExponentP: " + this.getPrimeExponentP().toString(16) + nl);
      buf.append("     primeExponentQ: " + this.getPrimeExponentQ().toString(16) + nl);
      buf.append("     crtCoefficient: " + this.getCrtCoefficient().toString(16) + nl);
      return buf.toString();
   }
}
