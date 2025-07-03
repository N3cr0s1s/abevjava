package org.bouncycastle.crypto;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Provider;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;

public class KeyAgreement {
   KeyAgreementSpi keyAgreeSpi;
   Provider provider;
   String algorithm;

   protected KeyAgreement(KeyAgreementSpi keyAgreeSpi, Provider provider, String algorithm) {
      this.keyAgreeSpi = keyAgreeSpi;
      this.provider = provider;
      this.algorithm = algorithm;
   }

   public final String getAlgorithm() {
      return this.algorithm;
   }

   public static final KeyAgreement getInstance(String algorithm) throws NoSuchAlgorithmException {
      try {
         JCEUtil.Implementation imp = JCEUtil.getImplementation("KeyAgreement", algorithm, (String)null);
         if (imp == null) {
            throw new NoSuchAlgorithmException(algorithm + " not found");
         } else {
            KeyAgreement keyAgree = new KeyAgreement((KeyAgreementSpi)imp.getEngine(), imp.getProvider(), algorithm);
            return keyAgree;
         }
      } catch (NoSuchProviderException var3) {
         throw new NoSuchAlgorithmException(algorithm + " not found");
      }
   }

   public static final KeyAgreement getInstance(String algorithm, String provider) throws NoSuchAlgorithmException, NoSuchProviderException {
      if (provider == null) {
         throw new IllegalArgumentException("No provider specified to KeyAgreement.getInstance()");
      } else {
         JCEUtil.Implementation imp = JCEUtil.getImplementation("KeyAgreement", algorithm, provider);
         if (imp == null) {
            throw new NoSuchAlgorithmException(algorithm + " not found");
         } else {
            KeyAgreement keyAgree = new KeyAgreement((KeyAgreementSpi)imp.getEngine(), imp.getProvider(), algorithm);
            return keyAgree;
         }
      }
   }

   public static final KeyAgreement getInstance(String algorithm, Provider provider) throws NoSuchAlgorithmException {
      if (provider == null) {
         throw new IllegalArgumentException();
      } else {
         JCEUtil.Implementation impl = JCEUtil.getImplementationFromProvider("KeyAgreement", algorithm, provider);
         if (impl == null) {
            throw new NoSuchAlgorithmException();
         } else {
            return new KeyAgreement((KeyAgreementSpi)impl.getEngine(), provider, algorithm);
         }
      }
   }

   public final Provider getProvider() {
      return this.provider;
   }

   public final void init(Key key) throws InvalidKeyException {
      this.keyAgreeSpi.engineInit(key, (SecureRandom)null);
   }

   public final void init(Key key, SecureRandom random) throws InvalidKeyException {
      this.keyAgreeSpi.engineInit(key, random);
   }

   public final void init(Key key, AlgorithmParameterSpec params) throws InvalidKeyException, InvalidAlgorithmParameterException {
      this.keyAgreeSpi.engineInit(key, params, (SecureRandom)null);
   }

   public final void init(Key key, AlgorithmParameterSpec params, SecureRandom random) throws InvalidKeyException, InvalidAlgorithmParameterException {
      this.keyAgreeSpi.engineInit(key, params, random);
   }

   public final Key doPhase(Key key, boolean lastPhase) throws InvalidKeyException, IllegalStateException {
      return this.keyAgreeSpi.engineDoPhase(key, lastPhase);
   }

   public final byte[] generateSecret() throws IllegalStateException {
      return this.keyAgreeSpi.engineGenerateSecret();
   }

   public final int generateSecret(byte[] sharedSecret, int offset) throws IllegalStateException, ShortBufferException {
      return this.keyAgreeSpi.engineGenerateSecret(sharedSecret, offset);
   }

   public final SecretKey generateSecret(String algorithm) throws IllegalStateException, NoSuchAlgorithmException, InvalidKeyException {
      return this.keyAgreeSpi.engineGenerateSecret(algorithm);
   }
}
