package org.bouncycastle.crypto;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Provider;
import java.security.spec.AlgorithmParameterSpec;

public class Mac implements Cloneable {
   MacSpi macSpi;
   Provider provider;
   String algorithm;
   private boolean initialised = false;

   protected Mac(MacSpi macSpi, Provider provider, String algorithm) {
      this.macSpi = macSpi;
      this.provider = provider;
      this.algorithm = algorithm;
   }

   public final String getAlgorithm() {
      return this.algorithm;
   }

   public static final Mac getInstance(String algorithm) throws NoSuchAlgorithmException {
      try {
         JCEUtil.Implementation imp = JCEUtil.getImplementation("Mac", algorithm, (String)null);
         if (imp == null) {
            throw new NoSuchAlgorithmException(algorithm + " not found");
         } else {
            Mac mac = new Mac((MacSpi)imp.getEngine(), imp.getProvider(), algorithm);
            return mac;
         }
      } catch (NoSuchProviderException var3) {
         throw new NoSuchAlgorithmException(algorithm + " not found");
      }
   }

   public static final Mac getInstance(String algorithm, Provider provider) throws NoSuchAlgorithmException {
      if (provider == null) {
         throw new IllegalArgumentException();
      } else {
         JCEUtil.Implementation impl = JCEUtil.getImplementationFromProvider("Mac", algorithm, provider);
         if (impl == null) {
            throw new NoSuchAlgorithmException();
         } else {
            return new Mac((MacSpi)impl.getEngine(), provider, algorithm);
         }
      }
   }

   public static final Mac getInstance(String algorithm, String provider) throws NoSuchAlgorithmException, NoSuchProviderException {
      if (provider == null) {
         throw new IllegalArgumentException("No provider specified to Mac.getInstance()");
      } else {
         JCEUtil.Implementation imp = JCEUtil.getImplementation("Mac", algorithm, provider);
         if (imp == null) {
            throw new NoSuchAlgorithmException(algorithm + " not found");
         } else {
            Mac mac = new Mac((MacSpi)imp.getEngine(), imp.getProvider(), algorithm);
            return mac;
         }
      }
   }

   public final Provider getProvider() {
      return this.provider;
   }

   public final int getMacLength() {
      return this.macSpi.engineGetMacLength();
   }

   public final void init(Key key) throws InvalidKeyException {
      try {
         this.macSpi.engineInit(key, (AlgorithmParameterSpec)null);
         this.initialised = true;
      } catch (InvalidAlgorithmParameterException var3) {
         throw new IllegalArgumentException("underlying mac waon't work without an AlgorithmParameterSpec");
      }
   }

   public final void init(Key key, AlgorithmParameterSpec params) throws InvalidKeyException, InvalidAlgorithmParameterException {
      this.macSpi.engineInit(key, params);
      this.initialised = true;
   }

   public final void update(byte input) throws IllegalStateException {
      if (!this.initialised) {
         throw new IllegalStateException("MAC not initialised");
      } else {
         this.macSpi.engineUpdate(input);
      }
   }

   public final void update(byte[] input) throws IllegalStateException {
      if (!this.initialised) {
         throw new IllegalStateException("MAC not initialised");
      } else if (input != null) {
         this.macSpi.engineUpdate(input, 0, input.length);
      }
   }

   public final void update(byte[] input, int offset, int len) throws IllegalStateException {
      if (!this.initialised) {
         throw new IllegalStateException("MAC not initialised");
      } else if (input == null) {
         throw new IllegalArgumentException("Null input passed");
      } else if (len >= 0 && offset >= 0 && len <= input.length - offset) {
         if (input.length != 0) {
            this.macSpi.engineUpdate(input, offset, len);
         }
      } else {
         throw new IllegalArgumentException("Bad offset/len");
      }
   }

   public final byte[] doFinal() throws IllegalStateException {
      if (!this.initialised) {
         throw new IllegalStateException("MAC not initialised");
      } else {
         return this.macSpi.engineDoFinal();
      }
   }

   public final void doFinal(byte[] output, int outOffset) throws ShortBufferException, IllegalStateException {
      if (!this.initialised) {
         throw new IllegalStateException("MAC not initialised");
      } else if (output.length - outOffset < this.macSpi.engineGetMacLength()) {
         throw new ShortBufferException("buffer to short for MAC output");
      } else {
         byte[] mac = this.macSpi.engineDoFinal();
         System.arraycopy(mac, 0, output, outOffset, mac.length);
      }
   }

   public final byte[] doFinal(byte[] input) throws IllegalStateException {
      if (!this.initialised) {
         throw new IllegalStateException("MAC not initialised");
      } else {
         this.macSpi.engineUpdate(input, 0, input.length);
         return this.macSpi.engineDoFinal();
      }
   }

   public final void reset() {
      this.macSpi.engineReset();
   }

   public final Object clone() throws CloneNotSupportedException {
      Mac result = new Mac((MacSpi)this.macSpi.clone(), this.provider, this.algorithm);
      result.initialised = this.initialised;
      return result;
   }
}
