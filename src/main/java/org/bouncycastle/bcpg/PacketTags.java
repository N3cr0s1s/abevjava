package org.bouncycastle.bcpg;

public interface PacketTags {
   int RESERVED = 0;
   int PUBLIC_KEY_ENC_SESSION = 1;
   int SIGNATURE = 2;
   int SYMMETRIC_KEY_ENC_SESSION = 3;
   int ONE_PASS_SIGNATURE = 4;
   int SECRET_KEY = 5;
   int PUBLIC_KEY = 6;
   int SECRET_SUBKEY = 7;
   int COMPRESSED_DATA = 8;
   int SYMMETRIC_KEY_ENC = 9;
   int MARKER = 10;
   int LITERAL_DATA = 11;
   int TRUST = 12;
   int USER_ID = 13;
   int PUBLIC_SUBKEY = 14;
   int USER_ATTRIBUTE = 17;
   int SYM_ENC_INTEGRITY_PRO = 18;
   int MOD_DETECTION_CODE = 19;
   int EXPERIMENTAL_1 = 60;
   int EXPERIMENTAL_2 = 61;
   int EXPERIMENTAL_3 = 62;
   int EXPERIMENTAL_4 = 63;
}
