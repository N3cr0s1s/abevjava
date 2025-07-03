package org.bouncycastle.crypto;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;

public abstract class KeyAgreementSpi {
   protected abstract void engineInit(Key var1, SecureRandom var2) throws InvalidKeyException;

   protected abstract void engineInit(Key var1, AlgorithmParameterSpec var2, SecureRandom var3) throws InvalidKeyException, InvalidAlgorithmParameterException;

   protected abstract Key engineDoPhase(Key var1, boolean var2) throws InvalidKeyException, IllegalStateException;

   protected abstract byte[] engineGenerateSecret() throws IllegalStateException;

   protected abstract int engineGenerateSecret(byte[] var1, int var2) throws IllegalStateException, ShortBufferException;

   protected abstract SecretKey engineGenerateSecret(String var1) throws IllegalStateException, NoSuchAlgorithmException, InvalidKeyException;
}
