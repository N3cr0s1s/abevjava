package org.bouncycastle.asn1.x509;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.DERSequence;

public class AlgorithmIdentifier extends ASN1Encodable {
   private DERObjectIdentifier objectId;
   private DEREncodable parameters;
   private boolean parametersDefined = false;

   public static AlgorithmIdentifier getInstance(ASN1TaggedObject obj, boolean explicit) {
      return getInstance(ASN1Sequence.getInstance(obj, explicit));
   }

   public static AlgorithmIdentifier getInstance(Object obj) {
      if (obj instanceof AlgorithmIdentifier) {
         return (AlgorithmIdentifier)obj;
      } else if (obj instanceof DERObjectIdentifier) {
         return new AlgorithmIdentifier((DERObjectIdentifier)obj);
      } else if (obj instanceof String) {
         return new AlgorithmIdentifier((String)obj);
      } else if (obj instanceof ASN1Sequence) {
         return new AlgorithmIdentifier((ASN1Sequence)obj);
      } else {
         throw new IllegalArgumentException("unknown object in factory");
      }
   }

   public AlgorithmIdentifier(DERObjectIdentifier objectId) {
      this.objectId = objectId;
   }

   public AlgorithmIdentifier(String objectId) {
      this.objectId = new DERObjectIdentifier(objectId);
   }

   public AlgorithmIdentifier(DERObjectIdentifier objectId, DEREncodable parameters) {
      this.parametersDefined = true;
      this.objectId = objectId;
      this.parameters = parameters;
   }

   public AlgorithmIdentifier(ASN1Sequence seq) {
      this.objectId = (DERObjectIdentifier)seq.getObjectAt(0);
      if (seq.size() == 2) {
         this.parametersDefined = true;
         this.parameters = seq.getObjectAt(1);
      } else {
         this.parameters = null;
      }

   }

   public DERObjectIdentifier getObjectId() {
      return this.objectId;
   }

   public DEREncodable getParameters() {
      return this.parameters;
   }

   public DERObject toASN1Object() {
      ASN1EncodableVector v = new ASN1EncodableVector();
      v.add(this.objectId);
      if (this.parametersDefined) {
         v.add(this.parameters);
      }

      return new DERSequence(v);
   }
}
