package org.bouncycastle.bcpg;

public interface BCPGKey {
   String getFormat();

   byte[] getEncoded();
}
