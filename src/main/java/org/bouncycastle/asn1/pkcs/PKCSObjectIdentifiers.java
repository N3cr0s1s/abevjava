package org.bouncycastle.asn1.pkcs;

import org.bouncycastle.asn1.DERObjectIdentifier;

public interface PKCSObjectIdentifiers {
   String pkcs_1 = "1.2.840.113549.1.1";
   DERObjectIdentifier rsaEncryption = new DERObjectIdentifier("1.2.840.113549.1.1.1");
   DERObjectIdentifier md2WithRSAEncryption = new DERObjectIdentifier("1.2.840.113549.1.1.2");
   DERObjectIdentifier md4WithRSAEncryption = new DERObjectIdentifier("1.2.840.113549.1.1.3");
   DERObjectIdentifier md5WithRSAEncryption = new DERObjectIdentifier("1.2.840.113549.1.1.4");
   DERObjectIdentifier sha1WithRSAEncryption = new DERObjectIdentifier("1.2.840.113549.1.1.5");
   DERObjectIdentifier srsaOAEPEncryptionSET = new DERObjectIdentifier("1.2.840.113549.1.1.6");
   DERObjectIdentifier id_RSAES_OAEP = new DERObjectIdentifier("1.2.840.113549.1.1.7");
   DERObjectIdentifier id_mgf1 = new DERObjectIdentifier("1.2.840.113549.1.1.8");
   DERObjectIdentifier id_pSpecified = new DERObjectIdentifier("1.2.840.113549.1.1.9");
   DERObjectIdentifier id_RSASSA_PSS = new DERObjectIdentifier("1.2.840.113549.1.1.10");
   DERObjectIdentifier sha256WithRSAEncryption = new DERObjectIdentifier("1.2.840.113549.1.1.11");
   DERObjectIdentifier sha384WithRSAEncryption = new DERObjectIdentifier("1.2.840.113549.1.1.12");
   DERObjectIdentifier sha512WithRSAEncryption = new DERObjectIdentifier("1.2.840.113549.1.1.13");
   DERObjectIdentifier sha224WithRSAEncryption = new DERObjectIdentifier("1.2.840.113549.1.1.14");
   String pkcs_3 = "1.2.840.113549.1.3";
   DERObjectIdentifier dhKeyAgreement = new DERObjectIdentifier("1.2.840.113549.1.3.1");
   String pkcs_5 = "1.2.840.113549.1.5";
   DERObjectIdentifier pbeWithMD2AndDES_CBC = new DERObjectIdentifier("1.2.840.113549.1.5.1");
   DERObjectIdentifier pbeWithMD2AndRC2_CBC = new DERObjectIdentifier("1.2.840.113549.1.5.4");
   DERObjectIdentifier pbeWithMD5AndDES_CBC = new DERObjectIdentifier("1.2.840.113549.1.5.3");
   DERObjectIdentifier pbeWithMD5AndRC2_CBC = new DERObjectIdentifier("1.2.840.113549.1.5.6");
   DERObjectIdentifier pbeWithSHA1AndDES_CBC = new DERObjectIdentifier("1.2.840.113549.1.5.10");
   DERObjectIdentifier pbeWithSHA1AndRC2_CBC = new DERObjectIdentifier("1.2.840.113549.1.5.11");
   DERObjectIdentifier id_PBES2 = new DERObjectIdentifier("1.2.840.113549.1.5.13");
   DERObjectIdentifier id_PBKDF2 = new DERObjectIdentifier("1.2.840.113549.1.5.12");
   String encryptionAlgorithm = "1.2.840.113549.3";
   DERObjectIdentifier des_EDE3_CBC = new DERObjectIdentifier("1.2.840.113549.3.7");
   DERObjectIdentifier RC2_CBC = new DERObjectIdentifier("1.2.840.113549.3.2");
   String digestAlgorithm = "1.2.840.113549.2";
   DERObjectIdentifier md2 = new DERObjectIdentifier("1.2.840.113549.2.2");
   DERObjectIdentifier md4 = new DERObjectIdentifier("1.2.840.113549.2.4");
   DERObjectIdentifier md5 = new DERObjectIdentifier("1.2.840.113549.2.5");
   DERObjectIdentifier id_hmacWithSHA1 = new DERObjectIdentifier("1.2.840.113549.2.7");
   DERObjectIdentifier id_hmacWithSHA224 = new DERObjectIdentifier("1.2.840.113549.2.8");
   DERObjectIdentifier id_hmacWithSHA256 = new DERObjectIdentifier("1.2.840.113549.2.9");
   DERObjectIdentifier id_hmacWithSHA384 = new DERObjectIdentifier("1.2.840.113549.2.10");
   DERObjectIdentifier id_hmacWithSHA512 = new DERObjectIdentifier("1.2.840.113549.2.11");
   String pkcs_7 = "1.2.840.113549.1.7";
   DERObjectIdentifier data = new DERObjectIdentifier("1.2.840.113549.1.7.1");
   DERObjectIdentifier signedData = new DERObjectIdentifier("1.2.840.113549.1.7.2");
   DERObjectIdentifier envelopedData = new DERObjectIdentifier("1.2.840.113549.1.7.3");
   DERObjectIdentifier signedAndEnvelopedData = new DERObjectIdentifier("1.2.840.113549.1.7.4");
   DERObjectIdentifier digestedData = new DERObjectIdentifier("1.2.840.113549.1.7.5");
   DERObjectIdentifier encryptedData = new DERObjectIdentifier("1.2.840.113549.1.7.6");
   String pkcs_9 = "1.2.840.113549.1.9";
   DERObjectIdentifier pkcs_9_at_emailAddress = new DERObjectIdentifier("1.2.840.113549.1.9.1");
   DERObjectIdentifier pkcs_9_at_unstructuredName = new DERObjectIdentifier("1.2.840.113549.1.9.2");
   DERObjectIdentifier pkcs_9_at_contentType = new DERObjectIdentifier("1.2.840.113549.1.9.3");
   DERObjectIdentifier pkcs_9_at_messageDigest = new DERObjectIdentifier("1.2.840.113549.1.9.4");
   DERObjectIdentifier pkcs_9_at_signingTime = new DERObjectIdentifier("1.2.840.113549.1.9.5");
   DERObjectIdentifier pkcs_9_at_counterSignature = new DERObjectIdentifier("1.2.840.113549.1.9.6");
   DERObjectIdentifier pkcs_9_at_challengePassword = new DERObjectIdentifier("1.2.840.113549.1.9.7");
   DERObjectIdentifier pkcs_9_at_unstructuredAddress = new DERObjectIdentifier("1.2.840.113549.1.9.8");
   DERObjectIdentifier pkcs_9_at_extendedCertificateAttributes = new DERObjectIdentifier("1.2.840.113549.1.9.9");
   DERObjectIdentifier pkcs_9_at_signingDescription = new DERObjectIdentifier("1.2.840.113549.1.9.13");
   DERObjectIdentifier pkcs_9_at_extensionRequest = new DERObjectIdentifier("1.2.840.113549.1.9.14");
   DERObjectIdentifier pkcs_9_at_smimeCapabilities = new DERObjectIdentifier("1.2.840.113549.1.9.15");
   DERObjectIdentifier pkcs_9_at_friendlyName = new DERObjectIdentifier("1.2.840.113549.1.9.20");
   DERObjectIdentifier pkcs_9_at_localKeyId = new DERObjectIdentifier("1.2.840.113549.1.9.21");
   DERObjectIdentifier x509certType = new DERObjectIdentifier("1.2.840.113549.1.9.22.1");
   DERObjectIdentifier id_alg_PWRI_KEK = new DERObjectIdentifier("1.2.840.113549.1.9.16.3.9");
   DERObjectIdentifier preferSignedData = new DERObjectIdentifier("1.2.840.113549.1.9.15.1");
   DERObjectIdentifier canNotDecryptAny = new DERObjectIdentifier("1.2.840.113549.1.9.15.2");
   DERObjectIdentifier sMIMECapabilitiesVersions = new DERObjectIdentifier("1.2.840.113549.1.9.15.3");
   String id_ct = "1.2.840.113549.1.9.16.1";
   DERObjectIdentifier id_ct_TSTInfo = new DERObjectIdentifier("1.2.840.113549.1.9.16.1.4");
   DERObjectIdentifier id_ct_compressedData = new DERObjectIdentifier("1.2.840.113549.1.9.16.1.9");
   String id_cti = "1.2.840.113549.1.9.16.6";
   DERObjectIdentifier id_cti_ets_proofOfOrigin = new DERObjectIdentifier("1.2.840.113549.1.9.16.6.1");
   DERObjectIdentifier id_cti_ets_proofOfReceipt = new DERObjectIdentifier("1.2.840.113549.1.9.16.6.2");
   DERObjectIdentifier id_cti_ets_proofOfDelivery = new DERObjectIdentifier("1.2.840.113549.1.9.16.6.3");
   DERObjectIdentifier id_cti_ets_proofOfSender = new DERObjectIdentifier("1.2.840.113549.1.9.16.6.4");
   DERObjectIdentifier id_cti_ets_proofOfApproval = new DERObjectIdentifier("1.2.840.113549.1.9.16.6.5");
   DERObjectIdentifier id_cti_ets_proofOfCreation = new DERObjectIdentifier("1.2.840.113549.1.9.16.6.6");
   String id_aa = "1.2.840.113549.1.9.16.2";
   DERObjectIdentifier id_aa_encrypKeyPref = new DERObjectIdentifier("1.2.840.113549.1.9.16.2.11");
   DERObjectIdentifier id_aa_signingCertificate = new DERObjectIdentifier("1.2.840.113549.1.9.16.2.12");
   DERObjectIdentifier id_aa_contentIdentifier = new DERObjectIdentifier("1.2.840.113549.1.9.16.2.7");
   DERObjectIdentifier id_aa_signatureTimeStampToken = new DERObjectIdentifier("1.2.840.113549.1.9.16.2.14");
   DERObjectIdentifier id_aa_sigPolicyId = new DERObjectIdentifier("1.2.840.113549.1.9.16.2.15");
   DERObjectIdentifier id_aa_commitmentType = new DERObjectIdentifier("1.2.840.113549.1.9.16.2.16");
   DERObjectIdentifier id_aa_signerLocation = new DERObjectIdentifier("1.2.840.113549.1.9.16.2.17");
   DERObjectIdentifier id_aa_otherSigCert = new DERObjectIdentifier("1.2.840.113549.1.9.16.2.19");
   String pkcs_12 = "1.2.840.113549.1.12";
   String bagtypes = "1.2.840.113549.1.12.10.1";
   DERObjectIdentifier keyBag = new DERObjectIdentifier("1.2.840.113549.1.12.10.1.1");
   DERObjectIdentifier pkcs8ShroudedKeyBag = new DERObjectIdentifier("1.2.840.113549.1.12.10.1.2");
   DERObjectIdentifier certBag = new DERObjectIdentifier("1.2.840.113549.1.12.10.1.3");
   DERObjectIdentifier crlBag = new DERObjectIdentifier("1.2.840.113549.1.12.10.1.4");
   DERObjectIdentifier secretBag = new DERObjectIdentifier("1.2.840.113549.1.12.10.1.5");
   DERObjectIdentifier safeContentsBag = new DERObjectIdentifier("1.2.840.113549.1.12.10.1.6");
   String pkcs_12PbeIds = "1.2.840.113549.1.12.1";
   DERObjectIdentifier pbeWithSHAAnd128BitRC4 = new DERObjectIdentifier("1.2.840.113549.1.12.1.1");
   DERObjectIdentifier pbeWithSHAAnd40BitRC4 = new DERObjectIdentifier("1.2.840.113549.1.12.1.2");
   DERObjectIdentifier pbeWithSHAAnd3_KeyTripleDES_CBC = new DERObjectIdentifier("1.2.840.113549.1.12.1.3");
   DERObjectIdentifier pbeWithSHAAnd2_KeyTripleDES_CBC = new DERObjectIdentifier("1.2.840.113549.1.12.1.4");
   DERObjectIdentifier pbeWithSHAAnd128BitRC2_CBC = new DERObjectIdentifier("1.2.840.113549.1.12.1.5");
   DERObjectIdentifier pbewithSHAAnd40BitRC2_CBC = new DERObjectIdentifier("1.2.840.113549.1.12.1.6");
}
