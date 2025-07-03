package org.bouncycastle.jce.provider;

import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.RSAKeyGenParameterSpec;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.generators.RSAKeyPairGenerator;
import org.bouncycastle.crypto.params.RSAKeyGenerationParameters;
import org.bouncycastle.crypto.params.RSAKeyParameters;
import org.bouncycastle.crypto.params.RSAPrivateCrtKeyParameters;

public abstract class JDKKeyPairGenerator extends KeyPairGenerator {
   public JDKKeyPairGenerator(String algorithmName) {
      super(algorithmName);
   }

   public abstract void initialize(int var1, SecureRandom var2);

   public abstract KeyPair generateKeyPair();

   public static class RSA extends JDKKeyPairGenerator {
      static final BigInteger defaultPublicExponent = BigInteger.valueOf(65537L);
      static final int defaultTests = 8;
      RSAKeyGenerationParameters param;
      RSAKeyPairGenerator engine = new RSAKeyPairGenerator();

      public RSA() {
         super("RSA");
         this.param = new RSAKeyGenerationParameters(defaultPublicExponent, new SecureRandom(), 2048, 8);
         this.engine.init(this.param);
      }

      public void initialize(int strength, SecureRandom random) {
         this.param = new RSAKeyGenerationParameters(defaultPublicExponent, random, strength, 8);
         this.engine.init(this.param);
      }

      public void initialize(AlgorithmParameterSpec params, SecureRandom random) throws InvalidAlgorithmParameterException {
         if (!(params instanceof RSAKeyGenParameterSpec)) {
            throw new InvalidAlgorithmParameterException("parameter object not a RSAKeyGenParameterSpec");
         } else {
            RSAKeyGenParameterSpec rsaParams = (RSAKeyGenParameterSpec)params;
            this.param = new RSAKeyGenerationParameters(rsaParams.getPublicExponent(), random, rsaParams.getKeysize(), 8);
            this.engine.init(this.param);
         }
      }

      public KeyPair generateKeyPair() {
         AsymmetricCipherKeyPair pair = this.engine.generateKeyPair();
         RSAKeyParameters pub = (RSAKeyParameters)pair.getPublic();
         RSAPrivateCrtKeyParameters priv = (RSAPrivateCrtKeyParameters)pair.getPrivate();
         return new KeyPair(new JCERSAPublicKey(pub), new JCERSAPrivateCrtKey(priv));
      }
   }
}
