package org.bouncycastle.crypto;

import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;

public abstract class CipherSpi {
   protected abstract void engineSetMode(String var1) throws NoSuchAlgorithmException;

   protected abstract void engineSetPadding(String var1) throws NoSuchPaddingException;

   protected abstract int engineGetBlockSize();

   protected abstract int engineGetOutputSize(int var1);

   protected abstract byte[] engineGetIV();

   protected abstract AlgorithmParameters engineGetParameters();

   protected abstract void engineInit(int var1, Key var2, SecureRandom var3) throws InvalidKeyException;

   protected abstract void engineInit(int var1, Key var2, AlgorithmParameterSpec var3, SecureRandom var4) throws InvalidKeyException, InvalidAlgorithmParameterException;

   protected abstract void engineInit(int var1, Key var2, AlgorithmParameters var3, SecureRandom var4) throws InvalidKeyException, InvalidAlgorithmParameterException;

   protected abstract byte[] engineUpdate(byte[] var1, int var2, int var3);

   protected abstract int engineUpdate(byte[] var1, int var2, int var3, byte[] var4, int var5) throws ShortBufferException;

   protected abstract byte[] engineDoFinal(byte[] var1, int var2, int var3) throws IllegalBlockSizeException, BadPaddingException;

   protected abstract int engineDoFinal(byte[] var1, int var2, int var3, byte[] var4, int var5) throws ShortBufferException, IllegalBlockSizeException, BadPaddingException;

   protected byte[] engineWrap(Key key) throws IllegalBlockSizeException, InvalidKeyException {
      throw new UnsupportedOperationException("Underlying crypto does not support key wrapping");
   }

   protected Key engineUnwrap(byte[] wrappedKey, String wrappedKeyAlgorithm, int wrappedKeyType) throws InvalidKeyException, NoSuchAlgorithmException {
      throw new UnsupportedOperationException("Underlying crypto does not support key unwrapping");
   }

   protected int engineGetKeySize(Key key) throws InvalidKeyException {
      throw new UnsupportedOperationException("Key size unavailable");
   }
}
