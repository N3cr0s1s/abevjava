package org.bouncycastle.crypto;

import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Provider;
import java.security.spec.AlgorithmParameterSpec;

public class ExemptionMechanism {
   private ExemptionMechanismSpi delegate;
   private Provider provider;
   private String mechanism;

   protected ExemptionMechanism(ExemptionMechanismSpi exmechSpi, Provider provider, String mechanism) {
      this.delegate = exmechSpi;
      this.provider = provider;
      this.mechanism = mechanism;
   }

   public static final ExemptionMechanism getInstance(String mechanism) throws NoSuchAlgorithmException {
      throw new NoSuchAlgorithmException();
   }

   public static final ExemptionMechanism getInstance(String mechanism, String provider) throws NoSuchAlgorithmException, NoSuchProviderException {
      if (provider == null) {
         throw new IllegalArgumentException();
      } else {
         throw new NoSuchAlgorithmException();
      }
   }

   public static final ExemptionMechanism getInstance(String mechanism, Provider provider) throws NoSuchAlgorithmException {
      if (provider == null) {
         throw new IllegalArgumentException();
      } else {
         throw new NoSuchAlgorithmException();
      }
   }

   public final String getName() {
      return this.mechanism;
   }

   public final Provider getProvider() {
      return this.provider;
   }

   public final boolean isCryptoAllowed(Key key) throws ExemptionMechanismException {
      throw new ExemptionMechanismException();
   }

   public final int getOutputSize(int inputLen) throws IllegalStateException {
      throw new IllegalStateException();
   }

   public final void init(Key key) throws InvalidKeyException, ExemptionMechanismException {
      throw new ExemptionMechanismException();
   }

   public final void init(Key key, AlgorithmParameterSpec params) throws InvalidKeyException, InvalidAlgorithmParameterException, ExemptionMechanismException {
      throw new ExemptionMechanismException();
   }

   public final void init(Key key, AlgorithmParameters params) throws InvalidKeyException, InvalidAlgorithmParameterException, ExemptionMechanismException {
      throw new ExemptionMechanismException();
   }

   public final byte[] genExemptionBlob() throws IllegalStateException, ExemptionMechanismException {
      throw new ExemptionMechanismException();
   }

   public final int genExemptionBlob(byte[] output) throws IllegalStateException, ShortBufferException, ExemptionMechanismException {
      throw new ExemptionMechanismException();
   }

   public final int genExemptionBlob(byte[] output, int outputOffset) throws IllegalStateException, ShortBufferException, ExemptionMechanismException {
      throw new ExemptionMechanismException();
   }

   protected void finalize() {
      try {
         super.finalize();
      } catch (Throwable var2) {
         var2.printStackTrace(System.err);
      }

   }
}
