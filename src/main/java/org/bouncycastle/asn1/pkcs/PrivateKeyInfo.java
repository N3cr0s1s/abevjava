package org.bouncycastle.asn1.pkcs;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Enumeration;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1Set;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;

public class PrivateKeyInfo extends ASN1Encodable {
   private DERObject privKey;
   private AlgorithmIdentifier algId;
   private ASN1Set attributes;

   public static PrivateKeyInfo getInstance(ASN1TaggedObject obj, boolean explicit) {
      return getInstance(ASN1Sequence.getInstance(obj, explicit));
   }

   public static PrivateKeyInfo getInstance(Object obj) {
      if (obj instanceof PrivateKeyInfo) {
         return (PrivateKeyInfo)obj;
      } else if (obj instanceof ASN1Sequence) {
         return new PrivateKeyInfo((ASN1Sequence)obj);
      } else {
         throw new IllegalArgumentException("unknown object in factory");
      }
   }

   public PrivateKeyInfo(AlgorithmIdentifier algId, DERObject privateKey) {
      this.privKey = privateKey;
      this.algId = algId;
   }

   public PrivateKeyInfo(ASN1Sequence seq) {
      Enumeration e = seq.getObjects();
      BigInteger version = ((DERInteger)e.nextElement()).getValue();
      if (version.intValue() != 0) {
         throw new IllegalArgumentException("wrong version for private key info");
      } else {
         this.algId = new AlgorithmIdentifier((ASN1Sequence)e.nextElement());

         try {
            ByteArrayInputStream bIn = new ByteArrayInputStream(((ASN1OctetString)e.nextElement()).getOctets());
            ASN1InputStream aIn = new ASN1InputStream(bIn);
            this.privKey = aIn.readObject();
         } catch (IOException var6) {
            throw new IllegalArgumentException("Error recoverying private key from sequence");
         }

         if (e.hasMoreElements()) {
            this.attributes = ASN1Set.getInstance((ASN1TaggedObject)e.nextElement(), false);
         }

      }
   }

   public AlgorithmIdentifier getAlgorithmId() {
      return this.algId;
   }

   public DERObject getPrivateKey() {
      return this.privKey;
   }

   public ASN1Set getAttributes() {
      return this.attributes;
   }

   public DERObject toASN1Object() {
      ASN1EncodableVector v = new ASN1EncodableVector();
      v.add(new DERInteger(0));
      v.add(this.algId);
      v.add(new DEROctetString(this.privKey));
      if (this.attributes != null) {
         v.add(new DERTaggedObject(false, 0, this.attributes));
      }

      return new DERSequence(v);
   }
}
