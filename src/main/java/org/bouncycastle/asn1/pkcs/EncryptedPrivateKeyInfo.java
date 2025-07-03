package org.bouncycastle.asn1.pkcs;

import java.util.Enumeration;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;

public class EncryptedPrivateKeyInfo extends ASN1Encodable {
   private AlgorithmIdentifier algId;
   private ASN1OctetString data;

   public EncryptedPrivateKeyInfo(ASN1Sequence seq) {
      Enumeration e = seq.getObjects();
      this.algId = AlgorithmIdentifier.getInstance(e.nextElement());
      this.data = (ASN1OctetString)e.nextElement();
   }

   public EncryptedPrivateKeyInfo(AlgorithmIdentifier algId, byte[] encoding) {
      this.algId = algId;
      this.data = new DEROctetString(encoding);
   }

   public static EncryptedPrivateKeyInfo getInstance(Object obj) {
      if (obj instanceof EncryptedData) {
         return (EncryptedPrivateKeyInfo)obj;
      } else if (obj instanceof ASN1Sequence) {
         return new EncryptedPrivateKeyInfo((ASN1Sequence)obj);
      } else {
         throw new IllegalArgumentException("unknown object in factory");
      }
   }

   public AlgorithmIdentifier getEncryptionAlgorithm() {
      return this.algId;
   }

   public byte[] getEncryptedData() {
      return this.data.getOctets();
   }

   public DERObject toASN1Object() {
      ASN1EncodableVector v = new ASN1EncodableVector();
      v.add(this.algId);
      v.add(this.data);
      return new DERSequence(v);
   }
}
