package org.bouncycastle.asn1.x509;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Enumeration;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERBitString;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DERSequence;

public class SubjectPublicKeyInfo extends ASN1Encodable {
   private AlgorithmIdentifier algId;
   private DERBitString keyData;

   public static SubjectPublicKeyInfo getInstance(ASN1TaggedObject obj, boolean explicit) {
      return getInstance(ASN1Sequence.getInstance(obj, explicit));
   }

   public static SubjectPublicKeyInfo getInstance(Object obj) {
      if (obj instanceof SubjectPublicKeyInfo) {
         return (SubjectPublicKeyInfo)obj;
      } else if (obj instanceof ASN1Sequence) {
         return new SubjectPublicKeyInfo((ASN1Sequence)obj);
      } else {
         throw new IllegalArgumentException("unknown object in factory");
      }
   }

   public SubjectPublicKeyInfo(AlgorithmIdentifier algId, DEREncodable publicKey) {
      this.keyData = new DERBitString(publicKey);
      this.algId = algId;
   }

   public SubjectPublicKeyInfo(AlgorithmIdentifier algId, byte[] publicKey) {
      this.keyData = new DERBitString(publicKey);
      this.algId = algId;
   }

   public SubjectPublicKeyInfo(ASN1Sequence seq) {
      Enumeration e = seq.getObjects();
      this.algId = AlgorithmIdentifier.getInstance(e.nextElement());
      this.keyData = (DERBitString)e.nextElement();
   }

   public AlgorithmIdentifier getAlgorithmId() {
      return this.algId;
   }

   public DERObject getPublicKey() throws IOException {
      ByteArrayInputStream bIn = new ByteArrayInputStream(this.keyData.getBytes());
      ASN1InputStream aIn = new ASN1InputStream(bIn);
      return aIn.readObject();
   }

   public DERBitString getPublicKeyData() {
      return this.keyData;
   }

   public DERObject toASN1Object() {
      ASN1EncodableVector v = new ASN1EncodableVector();
      v.add(this.algId);
      v.add(this.keyData);
      return new DERSequence(v);
   }
}
