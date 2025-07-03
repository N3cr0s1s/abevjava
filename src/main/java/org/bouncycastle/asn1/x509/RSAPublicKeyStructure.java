package org.bouncycastle.asn1.x509;

import java.math.BigInteger;
import java.util.Enumeration;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DERSequence;

public class RSAPublicKeyStructure extends ASN1Encodable {
   private BigInteger modulus;
   private BigInteger publicExponent;

   public static RSAPublicKeyStructure getInstance(ASN1TaggedObject obj, boolean explicit) {
      return getInstance(ASN1Sequence.getInstance(obj, explicit));
   }

   public static RSAPublicKeyStructure getInstance(Object obj) {
      if (obj != null && !(obj instanceof RSAPublicKeyStructure)) {
         if (obj instanceof ASN1Sequence) {
            return new RSAPublicKeyStructure((ASN1Sequence)obj);
         } else {
            throw new IllegalArgumentException("Invalid RSAPublicKeyStructure: " + obj.getClass().getName());
         }
      } else {
         return (RSAPublicKeyStructure)obj;
      }
   }

   public RSAPublicKeyStructure(BigInteger modulus, BigInteger publicExponent) {
      this.modulus = modulus;
      this.publicExponent = publicExponent;
   }

   public RSAPublicKeyStructure(ASN1Sequence seq) {
      Enumeration e = seq.getObjects();
      this.modulus = ((DERInteger)e.nextElement()).getPositiveValue();
      this.publicExponent = ((DERInteger)e.nextElement()).getPositiveValue();
   }

   public BigInteger getModulus() {
      return this.modulus;
   }

   public BigInteger getPublicExponent() {
      return this.publicExponent;
   }

   public DERObject toASN1Object() {
      ASN1EncodableVector v = new ASN1EncodableVector();
      v.add(new DERInteger(this.getModulus()));
      v.add(new DERInteger(this.getPublicExponent()));
      return new DERSequence(v);
   }
}
