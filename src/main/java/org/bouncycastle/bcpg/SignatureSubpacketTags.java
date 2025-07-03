package org.bouncycastle.bcpg;

public interface SignatureSubpacketTags {
   int CREATION_TIME = 2;
   int EXPIRE_TIME = 3;
   int EXPORTABLE = 4;
   int TRUST_SIG = 5;
   int REG_EXP = 6;
   int REVOCABLE = 7;
   int KEY_EXPIRE_TIME = 9;
   int PLACEHOLDER = 10;
   int PREFERRED_SYM_ALGS = 11;
   int REVOCATION_KEY = 12;
   int ISSUER_KEY_ID = 16;
   int NOTATION_DATA = 20;
   int PREFERRED_HASH_ALGS = 21;
   int PREFERRED_COMP_ALGS = 22;
   int KEY_SERVER_PREFS = 23;
   int PREFERRED_KEY_SERV = 24;
   int PRIMARY_USER_ID = 25;
   int POLICY_URL = 26;
   int KEY_FLAGS = 27;
   int SIGNER_USER_ID = 28;
   int REVOCATION_REASON = 29;
}
