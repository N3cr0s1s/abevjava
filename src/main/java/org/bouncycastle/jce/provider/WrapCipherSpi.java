package org.bouncycastle.jce.provider;

import java.io.ByteArrayInputStream;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.crypto.BadPaddingException;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.CipherSpi;
import org.bouncycastle.crypto.IllegalBlockSizeException;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.NoSuchPaddingException;
import org.bouncycastle.crypto.ShortBufferException;
import org.bouncycastle.crypto.Wrapper;
import org.bouncycastle.crypto.engines.AESWrapEngine;
import org.bouncycastle.crypto.engines.DESedeWrapEngine;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.crypto.spec.IvParameterSpec;
import org.bouncycastle.crypto.spec.PBEParameterSpec;
import org.bouncycastle.crypto.spec.SecretKeySpec;

public abstract class WrapCipherSpi extends CipherSpi implements PBE {
   private Class[] availableSpecs;
   protected int pbeType;
   protected int pbeHash;
   protected int pbeKeySize;
   protected int pbeIvSize;
   protected AlgorithmParameters engineParams;
   protected Wrapper wrapEngine;
   // $FF: synthetic field
   static Class class$0;
   // $FF: synthetic field
   static Class class$1;
   // $FF: synthetic field
   static Class class$2;
   // $FF: synthetic field
   static Class class$3;

   protected WrapCipherSpi() {
      Class[] var10001 = new Class[4];
      Class var10004 = class$0;
      if (var10004 == null) {
         try {
            var10004 = Class.forName("org.bouncycastle.crypto.spec.IvParameterSpec");
         } catch (ClassNotFoundException var4) {
            throw new NoClassDefFoundError(var4.getMessage());
         }

         class$0 = var10004;
      }

      var10001[0] = var10004;
      var10004 = class$1;
      if (var10004 == null) {
         try {
            var10004 = Class.forName("org.bouncycastle.crypto.spec.PBEParameterSpec");
         } catch (ClassNotFoundException var3) {
            throw new NoClassDefFoundError(var3.getMessage());
         }

         class$1 = var10004;
      }

      var10001[1] = var10004;
      var10004 = class$2;
      if (var10004 == null) {
         try {
            var10004 = Class.forName("org.bouncycastle.crypto.spec.RC2ParameterSpec");
         } catch (ClassNotFoundException var2) {
            throw new NoClassDefFoundError(var2.getMessage());
         }

         class$2 = var10004;
      }

      var10001[2] = var10004;
      var10004 = class$3;
      if (var10004 == null) {
         try {
            var10004 = Class.forName("org.bouncycastle.crypto.spec.RC5ParameterSpec");
         } catch (ClassNotFoundException var1) {
            throw new NoClassDefFoundError(var1.getMessage());
         }

         class$3 = var10004;
      }

      var10001[3] = var10004;
      this.availableSpecs = var10001;
      this.pbeType = 2;
      this.pbeHash = 1;
      this.engineParams = null;
      this.wrapEngine = null;
   }

   protected WrapCipherSpi(Wrapper wrapEngine) {
      Class[] var10001 = new Class[4];
      Class var10004 = class$0;
      if (var10004 == null) {
         try {
            var10004 = Class.forName("org.bouncycastle.crypto.spec.IvParameterSpec");
         } catch (ClassNotFoundException var5) {
            throw new NoClassDefFoundError(var5.getMessage());
         }

         class$0 = var10004;
      }

      var10001[0] = var10004;
      var10004 = class$1;
      if (var10004 == null) {
         try {
            var10004 = Class.forName("org.bouncycastle.crypto.spec.PBEParameterSpec");
         } catch (ClassNotFoundException var4) {
            throw new NoClassDefFoundError(var4.getMessage());
         }

         class$1 = var10004;
      }

      var10001[1] = var10004;
      var10004 = class$2;
      if (var10004 == null) {
         try {
            var10004 = Class.forName("org.bouncycastle.crypto.spec.RC2ParameterSpec");
         } catch (ClassNotFoundException var3) {
            throw new NoClassDefFoundError(var3.getMessage());
         }

         class$2 = var10004;
      }

      var10001[2] = var10004;
      var10004 = class$3;
      if (var10004 == null) {
         try {
            var10004 = Class.forName("org.bouncycastle.crypto.spec.RC5ParameterSpec");
         } catch (ClassNotFoundException var2) {
            throw new NoClassDefFoundError(var2.getMessage());
         }

         class$3 = var10004;
      }

      var10001[3] = var10004;
      this.availableSpecs = var10001;
      this.pbeType = 2;
      this.pbeHash = 1;
      this.engineParams = null;
      this.wrapEngine = null;
      this.wrapEngine = wrapEngine;
   }

   protected int engineGetBlockSize() {
      return 0;
   }

   protected byte[] engineGetIV() {
      return null;
   }

   protected int engineGetKeySize(Key key) {
      return key.getEncoded().length;
   }

   protected int engineGetOutputSize(int inputLen) {
      return -1;
   }

   protected AlgorithmParameters engineGetParameters() {
      return null;
   }

   protected void engineSetMode(String mode) throws NoSuchAlgorithmException {
      throw new NoSuchAlgorithmException("can't support mode " + mode);
   }

   protected void engineSetPadding(String padding) throws NoSuchPaddingException {
      throw new NoSuchPaddingException("Padding " + padding + " unknown.");
   }

   protected void engineInit(int opmode, Key key, AlgorithmParameterSpec params, SecureRandom random) throws InvalidKeyException, InvalidAlgorithmParameterException {
      Object param;
      if (key instanceof JCEPBEKey) {
         JCEPBEKey k = (JCEPBEKey)key;
         if (params instanceof PBEParameterSpec) {
            param = PBE.Util.makePBEParameters(k, params, this.wrapEngine.getAlgorithmName());
         } else {
            if (k.getParam() == null) {
               throw new InvalidAlgorithmParameterException("PBE requires PBE parameters to be set.");
            }

            param = k.getParam();
         }
      } else {
         param = new KeyParameter(key.getEncoded());
      }

      if (params instanceof IvParameterSpec) {
         IvParameterSpec iv = (IvParameterSpec)params;
         CipherParameters paramPlusIV = new ParametersWithIV((CipherParameters)param, iv.getIV());
         param = paramPlusIV;
      }

      switch(opmode) {
      case 1:
      case 2:
         throw new IllegalArgumentException("engine only valid for wrapping");
      case 3:
         this.wrapEngine.init(true, (CipherParameters)param);
         break;
      case 4:
         this.wrapEngine.init(false, (CipherParameters)param);
         break;
      default:
         System.out.println("eeek!");
      }

   }

   protected void engineInit(int opmode, Key key, AlgorithmParameters params, SecureRandom random) throws InvalidKeyException, InvalidAlgorithmParameterException {
      AlgorithmParameterSpec paramSpec = null;
      if (params != null) {
         int i = 0;

         while(i != this.availableSpecs.length) {
            try {
               paramSpec = params.getParameterSpec(this.availableSpecs[i]);
               break;
            } catch (Exception var8) {
               ++i;
            }
         }

         if (paramSpec == null) {
            throw new InvalidAlgorithmParameterException("can't handle parameter " + params.toString());
         }
      }

      this.engineParams = params;
      this.engineInit(opmode, key, paramSpec, random);
   }

   protected void engineInit(int opmode, Key key, SecureRandom random) throws InvalidKeyException {
      try {
         this.engineInit(opmode, key, (AlgorithmParameterSpec)null, random);
      } catch (InvalidAlgorithmParameterException var5) {
         throw new IllegalArgumentException(var5.getMessage());
      }
   }

   protected byte[] engineUpdate(byte[] input, int inputOffset, int inputLen) {
      throw new RuntimeException("not supported for wrapping");
   }

   protected int engineUpdate(byte[] input, int inputOffset, int inputLen, byte[] output, int outputOffset) throws ShortBufferException {
      throw new RuntimeException("not supported for wrapping");
   }

   protected byte[] engineDoFinal(byte[] input, int inputOffset, int inputLen) throws IllegalBlockSizeException, BadPaddingException {
      return null;
   }

   protected int engineDoFinal(byte[] input, int inputOffset, int inputLen, byte[] output, int outputOffset) throws IllegalBlockSizeException, BadPaddingException {
      return 0;
   }

   protected byte[] engineWrap(Key key) throws IllegalBlockSizeException, InvalidKeyException {
      byte[] encoded = key.getEncoded();
      if (encoded == null) {
         throw new InvalidKeyException("Cannot wrap key, null encoding.");
      } else {
         try {
            return this.wrapEngine == null ? this.engineDoFinal(encoded, 0, encoded.length) : this.wrapEngine.wrap(encoded, 0, encoded.length);
         } catch (BadPaddingException var4) {
            throw new IllegalBlockSizeException(var4.getMessage());
         }
      }
   }

   protected Key engineUnwrap(byte[] wrappedKey, String wrappedKeyAlgorithm, int wrappedKeyType) throws InvalidKeyException {
      byte[] encoded = (byte[])null;

      try {
         if (this.wrapEngine == null) {
            encoded = this.engineDoFinal(wrappedKey, 0, wrappedKey.length);
         } else {
            encoded = this.wrapEngine.unwrap(wrappedKey, 0, wrappedKey.length);
         }
      } catch (InvalidCipherTextException var11) {
         throw new InvalidKeyException(var11.getMessage());
      } catch (BadPaddingException var12) {
         throw new InvalidKeyException(var12.getMessage());
      } catch (IllegalBlockSizeException var13) {
         throw new InvalidKeyException(var13.getMessage());
      }

      if (wrappedKeyType == 3) {
         return new SecretKeySpec(encoded, wrappedKeyAlgorithm);
      } else if (wrappedKeyAlgorithm.equals("") && wrappedKeyType == 2) {
         ASN1InputStream bIn = new ASN1InputStream(new ByteArrayInputStream(encoded));
         JCERSAPrivateCrtKey privKey = null;

         try {
            ASN1Sequence s = (ASN1Sequence)bIn.readObject();
            PrivateKeyInfo in = new PrivateKeyInfo(s);
            DERObjectIdentifier oid = in.getAlgorithmId().getObjectId();
            privKey = new JCERSAPrivateCrtKey(in);
            return privKey;
         } catch (Exception var10) {
            throw new InvalidKeyException("Invalid key encoding.");
         }
      } else {
         try {
            KeyFactory kf = KeyFactory.getInstance(wrappedKeyAlgorithm, "BC");
            if (wrappedKeyType == 1) {
               return kf.generatePublic(new X509EncodedKeySpec(encoded));
            }

            if (wrappedKeyType == 2) {
               return kf.generatePrivate(new PKCS8EncodedKeySpec(encoded));
            }
         } catch (NoSuchProviderException var14) {
            throw new InvalidKeyException("Unknown key type " + var14.getMessage());
         } catch (NoSuchAlgorithmException var15) {
            throw new InvalidKeyException("Unknown key type " + var15.getMessage());
         } catch (InvalidKeySpecException var16) {
            throw new InvalidKeyException("Unknown key type " + var16.getMessage());
         }

         throw new InvalidKeyException("Unknown key type " + wrappedKeyType);
      }
   }

   public static class AESWrap extends WrapCipherSpi {
      public AESWrap() {
         super(new AESWrapEngine());
      }
   }

   public static class DESEDEWrap extends WrapCipherSpi {
      public DESEDEWrap() {
         super(new DESedeWrapEngine());
      }
   }
}
