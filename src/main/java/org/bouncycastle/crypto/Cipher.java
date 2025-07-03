package org.bouncycastle.crypto;

import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Provider;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.util.StringTokenizer;

public class Cipher {
   private static final int UNINITIALIZED = 0;
   public static final int ENCRYPT_MODE = 1;
   public static final int DECRYPT_MODE = 2;
   public static final int WRAP_MODE = 3;
   public static final int UNWRAP_MODE = 4;
   public static final int PUBLIC_KEY = 1;
   public static final int PRIVATE_KEY = 2;
   public static final int SECRET_KEY = 3;
   private CipherSpi cipherSpi;
   private Provider provider;
   private String transformation;
   private int mode = 0;

   protected Cipher(CipherSpi cipherSpi, Provider provider, String transformation) {
      this.cipherSpi = cipherSpi;
      this.provider = provider;
      this.transformation = transformation;
   }

   public static final Cipher getInstance(String transformation) throws NoSuchAlgorithmException, NoSuchPaddingException {
      try {
         JCEUtil.Implementation imp = JCEUtil.getImplementation("Cipher", transformation, (String)null);
         if (imp != null) {
            return new Cipher((CipherSpi)imp.getEngine(), imp.getProvider(), transformation);
         } else {
            StringTokenizer tok = new StringTokenizer(transformation, "/");
            String algorithm = tok.nextToken();
            imp = JCEUtil.getImplementation("Cipher", algorithm, (String)null);
            if (imp == null) {
               throw new NoSuchAlgorithmException(transformation + " not found");
            } else {
               CipherSpi cipherSpi = (CipherSpi)imp.getEngine();
               if (tok.hasMoreTokens() && !transformation.regionMatches(algorithm.length(), "//", 0, 2)) {
                  cipherSpi.engineSetMode(tok.nextToken());
               }

               if (tok.hasMoreTokens()) {
                  cipherSpi.engineSetPadding(tok.nextToken());
               }

               return new Cipher(cipherSpi, imp.getProvider(), transformation);
            }
         }
      } catch (NoSuchProviderException var5) {
         throw new NoSuchAlgorithmException(transformation + " not found");
      }
   }

   public static final Cipher getInstance(String transformation, Provider provider) throws NoSuchAlgorithmException, NoSuchPaddingException {
      if (provider == null) {
         throw new IllegalArgumentException();
      } else {
         JCEUtil.Implementation impl = JCEUtil.getImplementationFromProvider("Cipher", transformation, provider);
         if (impl != null) {
            return new Cipher((CipherSpi)impl.getEngine(), provider, transformation);
         } else {
            StringTokenizer tok = new StringTokenizer(transformation, "/");
            String algorithm = tok.nextToken();
            impl = JCEUtil.getImplementationFromProvider("Cipher", algorithm, provider);
            if (impl == null) {
               throw new NoSuchAlgorithmException(transformation + " not found");
            } else {
               CipherSpi cipherSpi = (CipherSpi)impl.getEngine();
               if (tok.hasMoreTokens() && !transformation.regionMatches(algorithm.length(), "//", 0, 2)) {
                  cipherSpi.engineSetMode(tok.nextToken());
               }

               if (tok.hasMoreTokens()) {
                  cipherSpi.engineSetPadding(tok.nextToken());
               }

               return new Cipher(cipherSpi, provider, transformation);
            }
         }
      }
   }

   public static final Cipher getInstance(String transformation, String provider) throws NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException {
      JCEUtil.Implementation imp = JCEUtil.getImplementation("Cipher", transformation, provider);
      if (imp != null) {
         return new Cipher((CipherSpi)imp.getEngine(), imp.getProvider(), transformation);
      } else {
         StringTokenizer tok = new StringTokenizer(transformation, "/");
         String algorithm = tok.nextToken();
         imp = JCEUtil.getImplementation("Cipher", algorithm, provider);
         if (imp == null) {
            throw new NoSuchAlgorithmException(transformation + " not found");
         } else {
            CipherSpi cipherSpi = (CipherSpi)imp.getEngine();
            if (tok.hasMoreTokens() && !transformation.regionMatches(algorithm.length(), "//", 0, 2)) {
               cipherSpi.engineSetMode(tok.nextToken());
            }

            if (tok.hasMoreTokens()) {
               cipherSpi.engineSetPadding(tok.nextToken());
            }

            return new Cipher(cipherSpi, imp.getProvider(), transformation);
         }
      }
   }

   public final Provider getProvider() {
      return this.provider;
   }

   public final String getAlgorithm() {
      return this.transformation;
   }

   public final int getBlockSize() {
      return this.cipherSpi.engineGetBlockSize();
   }

   public final int getOutputSize(int inputLen) throws IllegalStateException {
      if (this.mode != 1 && this.mode != 2) {
         throw new IllegalStateException("Cipher is uninitialised");
      } else {
         return this.cipherSpi.engineGetOutputSize(inputLen);
      }
   }

   public final byte[] getIV() {
      return this.cipherSpi.engineGetIV();
   }

   public final AlgorithmParameters getParameters() {
      return this.cipherSpi.engineGetParameters();
   }

   public final ExemptionMechanism getExemptionMechanism() {
      return null;
   }

   public final void init(int opmode, Key key) throws InvalidKeyException {
      this.cipherSpi.engineInit(opmode, key, new SecureRandom());
      this.mode = opmode;
   }

   public final void init(int opmode, Key key, SecureRandom random) throws InvalidKeyException {
      this.cipherSpi.engineInit(opmode, key, random);
      this.mode = opmode;
   }

   public final void init(int opmode, Key key, AlgorithmParameterSpec params) throws InvalidKeyException, InvalidAlgorithmParameterException {
      this.cipherSpi.engineInit(opmode, key, params, new SecureRandom());
      this.mode = opmode;
   }

   public final void init(int opmode, Key key, AlgorithmParameterSpec params, SecureRandom random) throws InvalidKeyException, InvalidAlgorithmParameterException {
      this.cipherSpi.engineInit(opmode, key, params, random);
      this.mode = opmode;
   }

   public final void init(int opmode, Key key, AlgorithmParameters params) throws InvalidKeyException, InvalidAlgorithmParameterException {
      this.cipherSpi.engineInit(opmode, key, params, new SecureRandom());
      this.mode = opmode;
   }

   public final void init(int opmode, Key key, AlgorithmParameters params, SecureRandom random) throws InvalidKeyException, InvalidAlgorithmParameterException {
      this.cipherSpi.engineInit(opmode, key, params, random);
      this.mode = opmode;
   }

   public final byte[] update(byte[] input) throws IllegalStateException {
      if (this.mode != 1 && this.mode != 2) {
         throw new IllegalStateException("Cipher is uninitialised");
      } else if (input == null) {
         throw new IllegalArgumentException("Null input buffer");
      } else {
         return input.length == 0 ? null : this.cipherSpi.engineUpdate(input, 0, input.length);
      }
   }

   public final byte[] update(byte[] input, int inputOffset, int inputLen) throws IllegalStateException {
      if (this.mode != 1 && this.mode != 2) {
         throw new IllegalStateException("Cipher is uninitialised");
      } else if (input == null) {
         throw new IllegalArgumentException("Null input passed");
      } else if (inputLen >= 0 && inputOffset >= 0 && inputLen <= input.length - inputOffset) {
         return inputLen == 0 ? null : this.cipherSpi.engineUpdate(input, inputOffset, inputLen);
      } else {
         throw new IllegalArgumentException("Bad inputOffset/inputLen");
      }
   }

   public final int update(byte[] input, int inputOffset, int inputLen, byte[] output) throws IllegalStateException, ShortBufferException {
      if (this.mode != 1 && this.mode != 2) {
         throw new IllegalStateException("Cipher is uninitialised");
      } else if (input == null) {
         throw new IllegalArgumentException("Null input passed");
      } else if (inputLen >= 0 && inputOffset >= 0 && inputLen <= input.length - inputOffset) {
         if (output == null) {
            throw new IllegalArgumentException("Null output passed");
         } else {
            return inputLen == 0 ? 0 : this.cipherSpi.engineUpdate(input, inputOffset, inputLen, output, 0);
         }
      } else {
         throw new IllegalArgumentException("Bad inputOffset/inputLen");
      }
   }

   public final int update(byte[] input, int inputOffset, int inputLen, byte[] output, int outputOffset) throws IllegalStateException, ShortBufferException {
      if (this.mode != 1 && this.mode != 2) {
         throw new IllegalStateException("Cipher is uninitialised");
      } else if (input == null) {
         throw new IllegalArgumentException("Null input passed");
      } else if (inputLen >= 0 && inputOffset >= 0 && inputLen <= input.length - inputOffset) {
         if (output == null) {
            throw new IllegalArgumentException("Null output passed");
         } else if (outputOffset >= 0 && outputOffset < output.length) {
            return inputLen == 0 ? 0 : this.cipherSpi.engineUpdate(input, inputOffset, inputLen, output, outputOffset);
         } else {
            throw new IllegalArgumentException("Bad outputOffset");
         }
      } else {
         throw new IllegalArgumentException("Bad inputOffset/inputLen");
      }
   }

   public final byte[] doFinal() throws IllegalStateException, IllegalBlockSizeException, BadPaddingException {
      if (this.mode != 1 && this.mode != 2) {
         throw new IllegalStateException("Cipher is uninitialised");
      } else {
         return this.cipherSpi.engineDoFinal((byte[])null, 0, 0);
      }
   }

   public final int doFinal(byte[] output, int outputOffset) throws IllegalStateException, IllegalBlockSizeException, ShortBufferException, BadPaddingException {
      if (this.mode != 1 && this.mode != 2) {
         throw new IllegalStateException("Cipher is uninitialised");
      } else if (output == null) {
         throw new IllegalArgumentException("Null output passed");
      } else if (outputOffset >= 0 && outputOffset < output.length) {
         return this.cipherSpi.engineDoFinal((byte[])null, 0, 0, output, outputOffset);
      } else {
         throw new IllegalArgumentException("Bad outputOffset");
      }
   }

   public final byte[] doFinal(byte[] input) throws IllegalStateException, IllegalBlockSizeException, BadPaddingException {
      if (this.mode != 1 && this.mode != 2) {
         throw new IllegalStateException("Cipher is uninitialised");
      } else if (input == null) {
         throw new IllegalArgumentException("Null input passed");
      } else {
         return this.cipherSpi.engineDoFinal(input, 0, input.length);
      }
   }

   public final byte[] doFinal(byte[] input, int inputOffset, int inputLen) throws IllegalStateException, IllegalBlockSizeException, BadPaddingException {
      if (this.mode != 1 && this.mode != 2) {
         throw new IllegalStateException("Cipher is uninitialised");
      } else if (input == null) {
         throw new IllegalArgumentException("Null input passed");
      } else if (inputLen >= 0 && inputOffset >= 0 && inputLen <= input.length - inputOffset) {
         return this.cipherSpi.engineDoFinal(input, inputOffset, inputLen);
      } else {
         throw new IllegalArgumentException("Bad inputOffset/inputLen");
      }
   }

   public final int doFinal(byte[] input, int inputOffset, int inputLen, byte[] output) throws IllegalStateException, ShortBufferException, IllegalBlockSizeException, BadPaddingException {
      if (this.mode != 1 && this.mode != 2) {
         throw new IllegalStateException("Cipher is uninitialised");
      } else if (input == null) {
         throw new IllegalArgumentException("Null input passed");
      } else if (inputLen >= 0 && inputOffset >= 0 && inputLen <= input.length - inputOffset) {
         if (output == null) {
            throw new IllegalArgumentException("Null output passed");
         } else {
            return this.cipherSpi.engineDoFinal(input, inputOffset, inputLen, output, 0);
         }
      } else {
         throw new IllegalArgumentException("Bad inputOffset/inputLen");
      }
   }

   public final int doFinal(byte[] input, int inputOffset, int inputLen, byte[] output, int outputOffset) throws IllegalStateException, ShortBufferException, IllegalBlockSizeException, BadPaddingException {
      if (this.mode != 1 && this.mode != 2) {
         throw new IllegalStateException("Cipher is uninitialised");
      } else if (input == null) {
         throw new IllegalArgumentException("Null input passed");
      } else if (inputLen >= 0 && inputOffset >= 0 && inputLen <= input.length - inputOffset) {
         if (output == null) {
            throw new IllegalArgumentException("Null output passed");
         } else if (outputOffset >= 0 && outputOffset < output.length) {
            return this.cipherSpi.engineDoFinal(input, inputOffset, inputLen, output, outputOffset);
         } else {
            throw new IllegalArgumentException("Bad outputOffset");
         }
      } else {
         throw new IllegalArgumentException("Bad inputOffset/inputLen");
      }
   }

   public final byte[] wrap(Key key) throws IllegalStateException, IllegalBlockSizeException, InvalidKeyException {
      if (this.mode != 3) {
         throw new IllegalStateException("Cipher is not initialised for wrapping");
      } else if (key == null) {
         throw new IllegalArgumentException("Null key passed");
      } else {
         return this.cipherSpi.engineWrap(key);
      }
   }

   public final Key unwrap(byte[] wrappedKey, String wrappedKeyAlgorithm, int wrappedKeyType) throws IllegalStateException, InvalidKeyException, NoSuchAlgorithmException {
      if (this.mode != 4) {
         throw new IllegalStateException("Cipher is not initialised for unwrapping");
      } else if (wrappedKeyType != 3 && wrappedKeyType != 1 && wrappedKeyType != 2) {
         throw new IllegalArgumentException("Invalid key type argument");
      } else if (wrappedKey == null) {
         throw new IllegalArgumentException("Null wrappedKey passed");
      } else if (wrappedKeyAlgorithm == null) {
         throw new IllegalArgumentException("Null wrappedKeyAlgorithm string passed");
      } else {
         return this.cipherSpi.engineUnwrap(wrappedKey, wrappedKeyAlgorithm, wrappedKeyType);
      }
   }
}
