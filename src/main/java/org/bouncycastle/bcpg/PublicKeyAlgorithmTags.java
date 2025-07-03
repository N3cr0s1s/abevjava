package org.bouncycastle.bcpg;

public interface PublicKeyAlgorithmTags {
   int RSA_GENERAL = 1;
   int RSA_ENCRYPT = 2;
   int RSA_SIGN = 3;
   int ELGAMAL_ENCRYPT = 16;
   int DSA = 17;
   int EC = 18;
   int ECDSA = 19;
   int ELGAMAL_GENERAL = 20;
   int DIFFIE_HELLMAN = 21;
}
