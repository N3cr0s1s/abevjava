package org.bouncycastle.asn1.pkcs;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.BERSequence;
import org.bouncycastle.asn1.BERTaggedObject;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;

public class EncryptedData extends ASN1Encodable {
   ASN1Sequence data;
   DERObjectIdentifier bagId;
   DERObject bagValue;

   public static EncryptedData getInstance(Object obj) {
      if (obj instanceof EncryptedData) {
         return (EncryptedData)obj;
      } else if (obj instanceof ASN1Sequence) {
         return new EncryptedData((ASN1Sequence)obj);
      } else {
         throw new IllegalArgumentException("unknown object in factory");
      }
   }

   public EncryptedData(ASN1Sequence seq) {
      int version = ((DERInteger)seq.getObjectAt(0)).getValue().intValue();
      if (version != 0) {
         throw new IllegalArgumentException("sequence not version 0");
      } else {
         this.data = (ASN1Sequence)seq.getObjectAt(1);
      }
   }

   public EncryptedData(DERObjectIdentifier contentType, AlgorithmIdentifier encryptionAlgorithm, DEREncodable content) {
      ASN1EncodableVector v = new ASN1EncodableVector();
      v.add(contentType);
      v.add(encryptionAlgorithm.getDERObject());
      v.add(new BERTaggedObject(false, 0, content));
      this.data = new BERSequence(v);
   }

   public DERObjectIdentifier getContentType() {
      return (DERObjectIdentifier)this.data.getObjectAt(0);
   }

   public AlgorithmIdentifier getEncryptionAlgorithm() {
      return AlgorithmIdentifier.getInstance(this.data.getObjectAt(1));
   }

   public ASN1OctetString getContent() {
      if (this.data.size() == 3) {
         DERTaggedObject o = (DERTaggedObject)this.data.getObjectAt(2);
         return ASN1OctetString.getInstance(o.getObject());
      } else {
         return null;
      }
   }

   public DERObject toASN1Object() {
      ASN1EncodableVector v = new ASN1EncodableVector();
      v.add(new DERInteger(0));
      v.add(this.data);
      return new BERSequence(v);
   }
}
