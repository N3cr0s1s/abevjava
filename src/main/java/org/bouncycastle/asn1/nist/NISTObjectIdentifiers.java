package org.bouncycastle.asn1.nist;

import org.bouncycastle.asn1.DERObjectIdentifier;

public interface NISTObjectIdentifiers {
   String nistAlgorithm = "2.16.840.1.101.3.4";
   DERObjectIdentifier id_sha256 = new DERObjectIdentifier("2.16.840.1.101.3.4.2.1");
   DERObjectIdentifier id_sha384 = new DERObjectIdentifier("2.16.840.1.101.3.4.2.2");
   DERObjectIdentifier id_sha512 = new DERObjectIdentifier("2.16.840.1.101.3.4.2.3");
   DERObjectIdentifier id_sha224 = new DERObjectIdentifier("2.16.840.1.101.3.4.2.4");
   String aes = "2.16.840.1.101.3.4.1";
   DERObjectIdentifier id_aes128_ECB = new DERObjectIdentifier("2.16.840.1.101.3.4.1.1");
   DERObjectIdentifier id_aes128_CBC = new DERObjectIdentifier("2.16.840.1.101.3.4.1.2");
   DERObjectIdentifier id_aes128_OFB = new DERObjectIdentifier("2.16.840.1.101.3.4.1.3");
   DERObjectIdentifier id_aes128_CFB = new DERObjectIdentifier("2.16.840.1.101.3.4.1.4");
   DERObjectIdentifier id_aes128_wrap = new DERObjectIdentifier("2.16.840.1.101.3.4.1.5");
   DERObjectIdentifier id_aes192_ECB = new DERObjectIdentifier("2.16.840.1.101.3.4.1.21");
   DERObjectIdentifier id_aes192_CBC = new DERObjectIdentifier("2.16.840.1.101.3.4.1.22");
   DERObjectIdentifier id_aes192_OFB = new DERObjectIdentifier("2.16.840.1.101.3.4.1.23");
   DERObjectIdentifier id_aes192_CFB = new DERObjectIdentifier("2.16.840.1.101.3.4.1.24");
   DERObjectIdentifier id_aes192_wrap = new DERObjectIdentifier("2.16.840.1.101.3.4.1.25");
   DERObjectIdentifier id_aes256_ECB = new DERObjectIdentifier("2.16.840.1.101.3.4.1.41");
   DERObjectIdentifier id_aes256_CBC = new DERObjectIdentifier("2.16.840.1.101.3.4.1.42");
   DERObjectIdentifier id_aes256_OFB = new DERObjectIdentifier("2.16.840.1.101.3.4.1.43");
   DERObjectIdentifier id_aes256_CFB = new DERObjectIdentifier("2.16.840.1.101.3.4.1.44");
   DERObjectIdentifier id_aes256_wrap = new DERObjectIdentifier("2.16.840.1.101.3.4.1.45");
}
