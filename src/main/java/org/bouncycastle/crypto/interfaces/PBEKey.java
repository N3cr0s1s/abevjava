package org.bouncycastle.crypto.interfaces;

import org.bouncycastle.crypto.SecretKey;

public interface PBEKey extends SecretKey {
   int getIterationCount();

   char[] getPassword();

   byte[] getSalt();
}
