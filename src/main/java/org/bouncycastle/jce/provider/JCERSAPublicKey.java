package org.bouncycastle.jce.provider;

import java.io.IOException;
import java.math.BigInteger;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPublicKeySpec;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERNull;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.RSAPublicKeyStructure;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.crypto.params.RSAKeyParameters;

public class JCERSAPublicKey implements RSAPublicKey {
   private BigInteger modulus;
   private BigInteger publicExponent;

   JCERSAPublicKey(RSAKeyParameters key) {
      this.modulus = key.getModulus();
      this.publicExponent = key.getExponent();
   }

   JCERSAPublicKey(RSAPublicKeySpec spec) {
      this.modulus = spec.getModulus();
      this.publicExponent = spec.getPublicExponent();
   }

   JCERSAPublicKey(RSAPublicKey key) {
      this.modulus = key.getModulus();
      this.publicExponent = key.getPublicExponent();
   }

   JCERSAPublicKey(SubjectPublicKeyInfo info) {
      try {
         RSAPublicKeyStructure pubKey = new RSAPublicKeyStructure((ASN1Sequence)info.getPublicKey());
         this.modulus = pubKey.getModulus();
         this.publicExponent = pubKey.getPublicExponent();
      } catch (IOException var3) {
         throw new IllegalArgumentException("invalid info structure in RSA public key");
      }
   }

   public BigInteger getModulus() {
      return this.modulus;
   }

   public BigInteger getPublicExponent() {
      return this.publicExponent;
   }

   public String getAlgorithm() {
      return "RSA";
   }

   public String getFormat() {
      return "X.509";
   }

   public byte[] getEncoded() {
      SubjectPublicKeyInfo info = new SubjectPublicKeyInfo(new AlgorithmIdentifier(PKCSObjectIdentifiers.rsaEncryption, new DERNull()), (new RSAPublicKeyStructure(this.getModulus(), this.getPublicExponent())).getDERObject());
      return info.getDEREncoded();
   }

   public boolean equals(Object o) {
      if (!(o instanceof RSAPublicKey)) {
         return false;
      } else if (o == this) {
         return true;
      } else {
         RSAPublicKey key = (RSAPublicKey)o;
         return this.getModulus().equals(key.getModulus()) && this.getPublicExponent().equals(key.getPublicExponent());
      }
   }

   public String toString() {
      StringBuffer buf = new StringBuffer();
      String nl = System.getProperty("line.separator");
      buf.append("RSA Public Key" + nl);
      buf.append("            modulus: " + this.getModulus().toString(16) + nl);
      buf.append("    public exponent: " + this.getPublicExponent().toString(16) + nl);
      return buf.toString();
   }
}
