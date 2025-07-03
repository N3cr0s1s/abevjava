package org.bouncycastle.crypto;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidParameterException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;

public abstract class KeyGeneratorSpi {
   protected abstract void engineInit(SecureRandom var1);

   protected abstract void engineInit(AlgorithmParameterSpec var1, SecureRandom var2) throws InvalidAlgorithmParameterException;

   protected abstract void engineInit(int var1, SecureRandom var2) throws InvalidParameterException;

   protected abstract SecretKey engineGenerateKey();
}
