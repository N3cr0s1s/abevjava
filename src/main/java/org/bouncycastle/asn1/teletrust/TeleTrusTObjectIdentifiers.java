package org.bouncycastle.asn1.teletrust;

import org.bouncycastle.asn1.DERObjectIdentifier;

public interface TeleTrusTObjectIdentifiers {
   String teleTrusTAlgorithm = "1.3.36.3";
   DERObjectIdentifier ripemd160 = new DERObjectIdentifier("1.3.36.3.2.1");
   DERObjectIdentifier ripemd128 = new DERObjectIdentifier("1.3.36.3.2.2");
   DERObjectIdentifier ripemd256 = new DERObjectIdentifier("1.3.36.3.2.3");
   String teleTrusTRSAsignatureAlgorithm = "1.3.36.3.3.1";
   DERObjectIdentifier rsaSignatureWithripemd160 = new DERObjectIdentifier("1.3.36.3.3.1.2");
   DERObjectIdentifier rsaSignatureWithripemd128 = new DERObjectIdentifier("1.3.36.3.3.1.3");
   DERObjectIdentifier rsaSignatureWithripemd256 = new DERObjectIdentifier("1.3.36.3.3.1.4");
}
