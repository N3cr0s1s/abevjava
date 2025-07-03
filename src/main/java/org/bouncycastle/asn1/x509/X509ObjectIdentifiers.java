package org.bouncycastle.asn1.x509;

import org.bouncycastle.asn1.DERObjectIdentifier;

public interface X509ObjectIdentifiers {
   String id = "2.5.4";
   DERObjectIdentifier commonName = new DERObjectIdentifier("2.5.4.3");
   DERObjectIdentifier countryName = new DERObjectIdentifier("2.5.4.6");
   DERObjectIdentifier localityName = new DERObjectIdentifier("2.5.4.7");
   DERObjectIdentifier stateOrProvinceName = new DERObjectIdentifier("2.5.4.8");
   DERObjectIdentifier organization = new DERObjectIdentifier("2.5.4.10");
   DERObjectIdentifier organizationalUnitName = new DERObjectIdentifier("2.5.4.11");
   DERObjectIdentifier id_SHA1 = new DERObjectIdentifier("1.3.14.3.2.26");
   DERObjectIdentifier ripemd160 = new DERObjectIdentifier("1.3.36.3.2.1");
   DERObjectIdentifier ripemd160WithRSAEncryption = new DERObjectIdentifier("1.3.36.3.3.1.2");
   DERObjectIdentifier id_ea_rsa = new DERObjectIdentifier("2.5.8.1.1");
   DERObjectIdentifier ocspAccessMethod = new DERObjectIdentifier("1.3.6.1.5.5.7.48.1");
}
