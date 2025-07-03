package org.bouncycastle.jce.provider;

import java.io.ByteArrayOutputStream;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.AlgorithmParameterSpec;
import org.bouncycastle.crypto.AsymmetricBlockCipher;
import org.bouncycastle.crypto.BadPaddingException;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.IllegalBlockSizeException;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.NoSuchPaddingException;
import org.bouncycastle.crypto.digests.MD5Digest;
import org.bouncycastle.crypto.digests.SHA1Digest;
import org.bouncycastle.crypto.digests.SHA224Digest;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.digests.SHA384Digest;
import org.bouncycastle.crypto.digests.SHA512Digest;
import org.bouncycastle.crypto.encodings.ISO9796d1Encoding;
import org.bouncycastle.crypto.encodings.OAEPEncoding;
import org.bouncycastle.crypto.encodings.PKCS1Encoding;
import org.bouncycastle.crypto.engines.RSAEngine;
import org.bouncycastle.crypto.params.ParametersWithRandom;

public class JCERSACipher extends WrapCipherSpi {
   private AsymmetricBlockCipher cipher;
   private AlgorithmParameterSpec paramSpec;
   private AlgorithmParameters engineParams;
   private boolean publicKeyOnly = false;
   private boolean privateKeyOnly = false;
   private ByteArrayOutputStream bOut = new ByteArrayOutputStream();

   public JCERSACipher(AsymmetricBlockCipher engine) {
      this.cipher = engine;
   }

   public JCERSACipher(boolean publicKeyOnly, boolean privateKeyOnly, AsymmetricBlockCipher engine) {
      this.publicKeyOnly = publicKeyOnly;
      this.privateKeyOnly = privateKeyOnly;
      this.cipher = engine;
   }

   protected int engineGetBlockSize() {
      try {
         return this.cipher.getInputBlockSize();
      } catch (NullPointerException var2) {
         throw new IllegalStateException("RSA Cipher not initialised");
      }
   }

   protected byte[] engineGetIV() {
      return null;
   }

   protected int engineGetKeySize(Key key) {
      if (key instanceof RSAPrivateKey) {
         RSAPrivateKey k = (RSAPrivateKey)key;
         return k.getModulus().bitLength();
      } else if (key instanceof RSAPublicKey) {
         RSAPublicKey k = (RSAPublicKey)key;
         return k.getModulus().bitLength();
      } else {
         throw new IllegalArgumentException("not an RSA key!");
      }
   }

   protected int engineGetOutputSize(int inputLen) {
      try {
         return this.cipher.getOutputBlockSize();
      } catch (NullPointerException var3) {
         throw new IllegalStateException("RSA Cipher not initialised");
      }
   }

   protected AlgorithmParameters engineGetParameters() {
      if (this.engineParams == null && this.paramSpec != null) {
         try {
            this.engineParams = AlgorithmParameters.getInstance("OAEP", "BC");
            this.engineParams.init(this.paramSpec);
         } catch (Exception var2) {
            throw new RuntimeException(var2.toString());
         }
      }

      return this.engineParams;
   }

   protected void engineSetMode(String mode) throws NoSuchAlgorithmException {
      String md = mode.toUpperCase();
      if (!md.equals("NONE") && !md.equals("ECB")) {
         if (md.equals("1")) {
            this.privateKeyOnly = true;
            this.publicKeyOnly = false;
         } else if (md.equals("2")) {
            this.privateKeyOnly = false;
            this.publicKeyOnly = true;
         } else {
            throw new NoSuchAlgorithmException("can't support mode " + mode);
         }
      }
   }

   protected void engineSetPadding(String padding) throws NoSuchPaddingException {
      String pad = padding.toUpperCase();
      if (pad.equals("NOPADDING")) {
         this.cipher = new RSAEngine();
      } else if (pad.equals("PKCS1PADDING")) {
         this.cipher = new PKCS1Encoding(new RSAEngine());
      } else if (pad.equals("OAEPPADDING")) {
         this.cipher = new OAEPEncoding(new RSAEngine());
      } else if (pad.equals("ISO9796-1PADDING")) {
         this.cipher = new ISO9796d1Encoding(new RSAEngine());
      } else if (pad.equals("OAEPWITHMD5ANDMGF1PADDING")) {
         this.cipher = new OAEPEncoding(new RSAEngine(), new MD5Digest());
      } else if (pad.equals("OAEPWITHSHA1ANDMGF1PADDING")) {
         this.cipher = new OAEPEncoding(new RSAEngine(), new SHA1Digest());
      } else if (pad.equals("OAEPWITHSHA224ANDMGF1PADDING")) {
         this.cipher = new OAEPEncoding(new RSAEngine(), new SHA224Digest());
      } else if (pad.equals("OAEPWITHSHA256ANDMGF1PADDING")) {
         this.cipher = new OAEPEncoding(new RSAEngine(), new SHA256Digest());
      } else if (pad.equals("OAEPWITHSHA384ANDMGF1PADDING")) {
         this.cipher = new OAEPEncoding(new RSAEngine(), new SHA384Digest());
      } else {
         if (!pad.equals("OAEPWITHSHA512ANDMGF1PADDING")) {
            throw new NoSuchPaddingException(padding + " unavailable with RSA.");
         }

         this.cipher = new OAEPEncoding(new RSAEngine(), new SHA512Digest());
      }

   }

   protected void engineInit(int opmode, Key key, AlgorithmParameterSpec params, SecureRandom random) throws InvalidKeyException {
      if (params == null) {
         Object param;
         if (key instanceof RSAPublicKey) {
            if (this.privateKeyOnly) {
               throw new InvalidKeyException("mode 1 requires RSAPrivateKey");
            }

            param = RSAUtil.generatePublicKeyParameter((RSAPublicKey)key);
         } else {
            if (!(key instanceof RSAPrivateKey)) {
               throw new InvalidKeyException("unknown key type passed to RSA");
            }

            if (this.publicKeyOnly) {
               throw new InvalidKeyException("mode 2 requires RSAPublicKey");
            }

            param = RSAUtil.generatePrivateKeyParameter((RSAPrivateKey)key);
         }

         if (!(this.cipher instanceof RSAEngine)) {
            if (random != null) {
               param = new ParametersWithRandom((CipherParameters)param, random);
            } else {
               param = new ParametersWithRandom((CipherParameters)param, new SecureRandom());
            }
         }

         switch(opmode) {
         case 1:
         case 3:
            this.cipher.init(true, (CipherParameters)param);
            break;
         case 2:
         case 4:
            this.cipher.init(false, (CipherParameters)param);
            break;
         default:
            System.out.println("eeek!");
         }

      } else {
         throw new IllegalArgumentException("unknown parameter type.");
      }
   }

   protected void engineInit(int opmode, Key key, AlgorithmParameters params, SecureRandom random) throws InvalidKeyException, InvalidAlgorithmParameterException {
      throw new InvalidAlgorithmParameterException("can't handle parameters in RSA");
   }

   protected void engineInit(int opmode, Key key, SecureRandom random) throws InvalidKeyException {
      this.engineInit(opmode, key, (AlgorithmParameterSpec)null, random);
   }

   protected byte[] engineUpdate(byte[] input, int inputOffset, int inputLen) {
      this.bOut.write(input, inputOffset, inputLen);
      if (this.cipher instanceof RSAEngine) {
         if (this.bOut.size() > this.cipher.getInputBlockSize() + 1) {
            throw new ArrayIndexOutOfBoundsException("too much data for RSA block");
         }
      } else if (this.bOut.size() > this.cipher.getInputBlockSize()) {
         throw new ArrayIndexOutOfBoundsException("too much data for RSA block");
      }

      return null;
   }

   protected int engineUpdate(byte[] input, int inputOffset, int inputLen, byte[] output, int outputOffset) {
      this.bOut.write(input, inputOffset, inputLen);
      if (this.cipher instanceof RSAEngine) {
         if (this.bOut.size() > this.cipher.getInputBlockSize() + 1) {
            throw new ArrayIndexOutOfBoundsException("too much data for RSA block");
         }
      } else if (this.bOut.size() > this.cipher.getInputBlockSize()) {
         throw new ArrayIndexOutOfBoundsException("too much data for RSA block");
      }

      return 0;
   }

   protected byte[] engineDoFinal(byte[] input, int inputOffset, int inputLen) throws IllegalBlockSizeException, BadPaddingException {
      if (input != null) {
         this.bOut.write(input, inputOffset, inputLen);
      }

      if (this.cipher instanceof RSAEngine) {
         if (this.bOut.size() > this.cipher.getInputBlockSize() + 1) {
            throw new ArrayIndexOutOfBoundsException("too much data for RSA block");
         }
      } else if (this.bOut.size() > this.cipher.getInputBlockSize()) {
         throw new ArrayIndexOutOfBoundsException("too much data for RSA block");
      }

      try {
         byte[] bytes = this.bOut.toByteArray();
         this.bOut.reset();
         return this.cipher.processBlock(bytes, 0, bytes.length);
      } catch (InvalidCipherTextException var5) {
         throw new BadPaddingException(var5.getMessage());
      }
   }

   protected int engineDoFinal(byte[] input, int inputOffset, int inputLen, byte[] output, int outputOffset) throws IllegalBlockSizeException, BadPaddingException {
      if (input != null) {
         this.bOut.write(input, inputOffset, inputLen);
      }

      if (this.cipher instanceof RSAEngine) {
         if (this.bOut.size() > this.cipher.getInputBlockSize() + 1) {
            throw new ArrayIndexOutOfBoundsException("too much data for RSA block");
         }
      } else if (this.bOut.size() > this.cipher.getInputBlockSize()) {
         throw new ArrayIndexOutOfBoundsException("too much data for RSA block");
      }

      byte[] out;
      try {
         byte[] bytes = this.bOut.toByteArray();
         this.bOut.reset();
         out = this.cipher.processBlock(bytes, 0, bytes.length);
      } catch (InvalidCipherTextException var8) {
         throw new BadPaddingException(var8.getMessage());
      }

      for(int i = 0; i != out.length; ++i) {
         output[outputOffset + i] = out[i];
      }

      return out.length;
   }

   public static class ISO9796d1Padding extends JCERSACipher {
      public ISO9796d1Padding() {
         super(new ISO9796d1Encoding(new RSAEngine()));
      }
   }

   public static class NoPadding extends JCERSACipher {
      public NoPadding() {
         super(new RSAEngine());
      }
   }

   public static class OAEPPadding extends JCERSACipher {
      public OAEPPadding() {
         super(new OAEPEncoding(new RSAEngine()));
      }
   }

   public static class PKCS1v1_5Padding extends JCERSACipher {
      public PKCS1v1_5Padding() {
         super(new PKCS1Encoding(new RSAEngine()));
      }
   }

   public static class PKCS1v1_5Padding_PrivateOnly extends JCERSACipher {
      public PKCS1v1_5Padding_PrivateOnly() {
         super(false, true, new PKCS1Encoding(new RSAEngine()));
      }
   }

   public static class PKCS1v1_5Padding_PublicOnly extends JCERSACipher {
      public PKCS1v1_5Padding_PublicOnly() {
         super(true, false, new PKCS1Encoding(new RSAEngine()));
      }
   }
}
