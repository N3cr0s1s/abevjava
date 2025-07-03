package org.bouncycastle.crypto;

import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Provider;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;

public class KeyGenerator {
   private KeyGeneratorSpi keyGenerator;
   private Provider provider;
   private String algorithm;

   protected KeyGenerator(KeyGeneratorSpi keyGenSpi, Provider provider, String algorithm) {
      this.keyGenerator = keyGenSpi;
      this.provider = provider;
      this.algorithm = algorithm;
   }

   public final String getAlgorithm() {
      return this.algorithm;
   }

   public static final KeyGenerator getInstance(String algorithm) throws NoSuchAlgorithmException {
      try {
         JCEUtil.Implementation imp = JCEUtil.getImplementation("KeyGenerator", algorithm, (String)null);
         if (imp == null) {
            throw new NoSuchAlgorithmException(algorithm + " not found");
         } else {
            KeyGenerator keyGen = new KeyGenerator((KeyGeneratorSpi)imp.getEngine(), imp.getProvider(), algorithm);
            return keyGen;
         }
      } catch (NoSuchProviderException var3) {
         throw new NoSuchAlgorithmException(algorithm + " not found");
      }
   }

   public static final KeyGenerator getInstance(String algorithm, Provider provider) throws NoSuchAlgorithmException {
      if (provider == null) {
         throw new IllegalArgumentException();
      } else {
         JCEUtil.Implementation impl = JCEUtil.getImplementationFromProvider("KeyGenerator", algorithm, provider);
         if (impl == null) {
            throw new NoSuchAlgorithmException();
         } else {
            return new KeyGenerator((KeyGeneratorSpi)impl.getEngine(), provider, algorithm);
         }
      }
   }

   public static final KeyGenerator getInstance(String algorithm, String provider) throws NoSuchAlgorithmException, NoSuchProviderException {
      if (provider == null) {
         throw new IllegalArgumentException("No provider specified to KeyGenerator.getInstance()");
      } else {
         JCEUtil.Implementation imp = JCEUtil.getImplementation("KeyGenerator", algorithm, provider);
         if (imp == null) {
            throw new NoSuchAlgorithmException(algorithm + " not found");
         } else {
            KeyGenerator keyGen = new KeyGenerator((KeyGeneratorSpi)imp.getEngine(), imp.getProvider(), algorithm);
            return keyGen;
         }
      }
   }

   public final Provider getProvider() {
      return this.provider;
   }

   public final void init(SecureRandom random) {
      this.keyGenerator.engineInit(random);
   }

   public final void init(AlgorithmParameterSpec params) throws InvalidAlgorithmParameterException {
      this.keyGenerator.engineInit(params, new SecureRandom());
   }

   public final void init(AlgorithmParameterSpec params, SecureRandom random) throws InvalidAlgorithmParameterException {
      this.keyGenerator.engineInit(params, random);
   }

   public final void init(int keysize) {
      this.keyGenerator.engineInit(keysize, new SecureRandom());
   }

   public final void init(int keysize, SecureRandom random) {
      this.keyGenerator.engineInit(keysize, random);
   }

   public final SecretKey generateKey() {
      return this.keyGenerator.engineGenerateKey();
   }
}
