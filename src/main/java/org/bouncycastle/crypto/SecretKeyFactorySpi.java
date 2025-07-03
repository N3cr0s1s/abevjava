package org.bouncycastle.crypto;

import java.security.InvalidKeyException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

public abstract class SecretKeyFactorySpi {
   protected abstract SecretKey engineGenerateSecret(KeySpec var1) throws InvalidKeySpecException;

   protected abstract KeySpec engineGetKeySpec(SecretKey var1, Class var2) throws InvalidKeySpecException;

   protected abstract SecretKey engineTranslateKey(SecretKey var1) throws InvalidKeyException;
}
