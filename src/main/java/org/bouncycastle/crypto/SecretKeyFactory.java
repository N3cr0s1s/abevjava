package org.bouncycastle.crypto;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Provider;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

public class SecretKeyFactory {
   SecretKeyFactorySpi keyFacSpi;
   Provider provider;
   String algorithm;

   protected SecretKeyFactory(SecretKeyFactorySpi keyFacSpi, Provider provider, String algorithm) {
      this.keyFacSpi = keyFacSpi;
      this.provider = provider;
      this.algorithm = algorithm;
   }

   public static final SecretKeyFactory getInstance(String algorithm) throws NoSuchAlgorithmException {
      try {
         JCEUtil.Implementation imp = JCEUtil.getImplementation("SecretKeyFactory", algorithm, (String)null);
         if (imp == null) {
            throw new NoSuchAlgorithmException(algorithm + " not found");
         } else {
            SecretKeyFactory keyFact = new SecretKeyFactory((SecretKeyFactorySpi)imp.getEngine(), imp.getProvider(), algorithm);
            return keyFact;
         }
      } catch (NoSuchProviderException var3) {
         throw new NoSuchAlgorithmException(algorithm + " not found");
      }
   }

   public static final SecretKeyFactory getInstance(String algorithm, Provider provider) throws NoSuchAlgorithmException {
      if (provider == null) {
         throw new IllegalArgumentException();
      } else {
         JCEUtil.Implementation impl = JCEUtil.getImplementationFromProvider("SecretKeyFactory", algorithm, provider);
         if (impl == null) {
            throw new NoSuchAlgorithmException();
         } else {
            return new SecretKeyFactory((SecretKeyFactorySpi)impl.getEngine(), provider, algorithm);
         }
      }
   }

   public static final SecretKeyFactory getInstance(String algorithm, String provider) throws NoSuchAlgorithmException, NoSuchProviderException {
      if (provider == null) {
         throw new IllegalArgumentException("No provider specified to SecretKeyFactory.getInstance()");
      } else {
         JCEUtil.Implementation imp = JCEUtil.getImplementation("SecretKeyFactory", algorithm, provider);
         if (imp == null) {
            throw new NoSuchAlgorithmException(algorithm + " not found");
         } else {
            SecretKeyFactory keyFact = new SecretKeyFactory((SecretKeyFactorySpi)imp.getEngine(), imp.getProvider(), algorithm);
            return keyFact;
         }
      }
   }

   public final Provider getProvider() {
      return this.provider;
   }

   public final String getAlgorithm() {
      return this.algorithm;
   }

   public final SecretKey generateSecret(KeySpec keySpec) throws InvalidKeySpecException {
      return this.keyFacSpi.engineGenerateSecret(keySpec);
   }

   public final KeySpec getKeySpec(SecretKey key, Class keySpec) throws InvalidKeySpecException {
      return this.keyFacSpi.engineGetKeySpec(key, keySpec);
   }

   public final SecretKey translateKey(SecretKey key) throws InvalidKeyException {
      return this.keyFacSpi.engineTranslateKey(key);
   }
}
