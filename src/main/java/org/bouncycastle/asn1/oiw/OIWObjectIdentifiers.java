package org.bouncycastle.asn1.oiw;

import org.bouncycastle.asn1.DERObjectIdentifier;

public interface OIWObjectIdentifiers {
   DERObjectIdentifier md4WithRSA = new DERObjectIdentifier("1.3.14.3.2.2");
   DERObjectIdentifier md5WithRSA = new DERObjectIdentifier("1.3.14.3.2.3");
   DERObjectIdentifier md4WithRSAEncryption = new DERObjectIdentifier("1.3.14.3.2.4");
   DERObjectIdentifier desCBC = new DERObjectIdentifier("1.3.14.3.2.7");
   DERObjectIdentifier idSHA1 = new DERObjectIdentifier("1.3.14.3.2.26");
   DERObjectIdentifier dsaWithSHA1 = new DERObjectIdentifier("1.3.14.3.2.27");
   DERObjectIdentifier elGamalAlgorithm = new DERObjectIdentifier("1.3.14.7.2.1.1");
}
