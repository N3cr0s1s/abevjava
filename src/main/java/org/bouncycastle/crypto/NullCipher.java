package org.bouncycastle.crypto;

import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;

public class NullCipher extends Cipher {
   public NullCipher() {
      super(new NullCipher.NullCipherSpi((NullCipher.NullCipherSpi)null), (Provider)null, "NULL");
   }

   private static class NullCipherSpi extends CipherSpi {
      private NullCipherSpi() {
      }

      protected void engineSetMode(String mode) throws NoSuchAlgorithmException {
      }

      protected void engineSetPadding(String padding) throws NoSuchPaddingException {
      }

      protected int engineGetBlockSize() {
         return 1;
      }

      protected int engineGetOutputSize(int inputLen) {
         return inputLen;
      }

      protected byte[] engineGetIV() {
         return null;
      }

      protected AlgorithmParameters engineGetParameters() {
         return null;
      }

      protected void engineInit(int opmode, Key key, SecureRandom random) throws InvalidKeyException {
      }

      protected void engineInit(int opmode, Key key, AlgorithmParameterSpec params, SecureRandom random) throws InvalidKeyException, InvalidAlgorithmParameterException {
      }

      protected void engineInit(int opmode, Key key, AlgorithmParameters params, SecureRandom random) throws InvalidKeyException, InvalidAlgorithmParameterException {
      }

      protected byte[] engineUpdate(byte[] input, int inputOffset, int inputLen) {
         if (input == null) {
            return null;
         } else {
            byte[] tmp = new byte[inputLen];
            System.arraycopy(input, inputOffset, tmp, 0, inputLen);
            return tmp;
         }
      }

      protected int engineUpdate(byte[] input, int inputOffset, int inputLen, byte[] output, int outputOffset) throws ShortBufferException {
         if (input == null) {
            return 0;
         } else if (output.length - outputOffset < inputLen) {
            throw new ShortBufferException("output buffer to short for NullCipher");
         } else {
            System.arraycopy(input, inputOffset, output, outputOffset, inputLen);
            return inputLen;
         }
      }

      protected byte[] engineDoFinal(byte[] input, int inputOffset, int inputLen) throws IllegalBlockSizeException, BadPaddingException {
         if (input == null) {
            return new byte[0];
         } else {
            byte[] tmp = new byte[inputLen];
            System.arraycopy(input, inputOffset, tmp, 0, inputLen);
            return tmp;
         }
      }

      protected int engineDoFinal(byte[] input, int inputOffset, int inputLen, byte[] output, int outputOffset) throws ShortBufferException, IllegalBlockSizeException, BadPaddingException {
         if (input == null) {
            return 0;
         } else if (output.length - outputOffset < inputLen) {
            throw new ShortBufferException("output buffer too short for NullCipher");
         } else {
            System.arraycopy(input, inputOffset, output, outputOffset, inputLen);
            return inputLen;
         }
      }

      protected int engineGetKeySize(Key key) throws InvalidKeyException {
         return 0;
      }

      // $FF: synthetic method
      NullCipherSpi(NullCipher.NullCipherSpi var1) {
         this();
      }
   }
}
