package org.bouncycastle.jce.provider;

import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.InvalidParameterException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import org.bouncycastle.crypto.BadPaddingException;
import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.BufferedBlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.IllegalBlockSizeException;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.NoSuchPaddingException;
import org.bouncycastle.crypto.SecretKey;
import org.bouncycastle.crypto.ShortBufferException;
import org.bouncycastle.crypto.engines.AESFastEngine;
import org.bouncycastle.crypto.engines.CAST5Engine;
import org.bouncycastle.crypto.engines.CAST6Engine;
import org.bouncycastle.crypto.engines.DESEngine;
import org.bouncycastle.crypto.engines.DESedeEngine;
import org.bouncycastle.crypto.engines.IDEAEngine;
import org.bouncycastle.crypto.engines.RijndaelEngine;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.modes.CFBBlockCipher;
import org.bouncycastle.crypto.modes.CTSBlockCipher;
import org.bouncycastle.crypto.modes.GOFBBlockCipher;
import org.bouncycastle.crypto.modes.OFBBlockCipher;
import org.bouncycastle.crypto.modes.OpenPGPCFBBlockCipher;
import org.bouncycastle.crypto.modes.PGPCFBBlockCipher;
import org.bouncycastle.crypto.paddings.ISO10126d2Padding;
import org.bouncycastle.crypto.paddings.ISO7816d4Padding;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.bouncycastle.crypto.paddings.TBCPadding;
import org.bouncycastle.crypto.paddings.X923Padding;
import org.bouncycastle.crypto.paddings.ZeroBytePadding;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.crypto.spec.IvParameterSpec;
import org.bouncycastle.crypto.spec.PBEParameterSpec;

public class JCEBlockCipher extends WrapCipherSpi implements PBE {
   private Class[] availableSpecs;
   private BlockCipher baseEngine;
   private BufferedBlockCipher cipher;
   private ParametersWithIV ivParam;
   private int ivLength;
   private boolean padded;
   private PBEParameterSpec pbeSpec;
   private String pbeAlgorithm;
   private String modeName;
   // $FF: synthetic field
   static Class class$0;
   // $FF: synthetic field
   static Class class$1;
   // $FF: synthetic field
   static Class class$2;
   // $FF: synthetic field
   static Class class$3;

   protected JCEBlockCipher(BlockCipher engine) {
      Class[] var10001 = new Class[4];
      Class var10004 = class$0;
      if (var10004 == null) {
         try {
            var10004 = Class.forName("org.bouncycastle.crypto.spec.RC2ParameterSpec");
         } catch (ClassNotFoundException var5) {
            throw new NoClassDefFoundError(var5.getMessage());
         }

         class$0 = var10004;
      }

      var10001[0] = var10004;
      var10004 = class$1;
      if (var10004 == null) {
         try {
            var10004 = Class.forName("org.bouncycastle.crypto.spec.RC5ParameterSpec");
         } catch (ClassNotFoundException var4) {
            throw new NoClassDefFoundError(var4.getMessage());
         }

         class$1 = var10004;
      }

      var10001[1] = var10004;
      var10004 = class$2;
      if (var10004 == null) {
         try {
            var10004 = Class.forName("org.bouncycastle.crypto.spec.IvParameterSpec");
         } catch (ClassNotFoundException var3) {
            throw new NoClassDefFoundError(var3.getMessage());
         }

         class$2 = var10004;
      }

      var10001[2] = var10004;
      var10004 = class$3;
      if (var10004 == null) {
         try {
            var10004 = Class.forName("org.bouncycastle.crypto.spec.PBEParameterSpec");
         } catch (ClassNotFoundException var2) {
            throw new NoClassDefFoundError(var2.getMessage());
         }

         class$3 = var10004;
      }

      var10001[3] = var10004;
      this.availableSpecs = var10001;
      this.ivLength = 0;
      this.padded = true;
      this.pbeSpec = null;
      this.pbeAlgorithm = null;
      this.modeName = null;
      this.baseEngine = engine;
      this.cipher = new PaddedBufferedBlockCipher(engine);
   }

   protected JCEBlockCipher(BlockCipher engine, int ivLength) {
      Class[] var10001 = new Class[4];
      Class var10004 = class$0;
      if (var10004 == null) {
         try {
            var10004 = Class.forName("org.bouncycastle.crypto.spec.RC2ParameterSpec");
         } catch (ClassNotFoundException var6) {
            throw new NoClassDefFoundError(var6.getMessage());
         }

         class$0 = var10004;
      }

      var10001[0] = var10004;
      var10004 = class$1;
      if (var10004 == null) {
         try {
            var10004 = Class.forName("org.bouncycastle.crypto.spec.RC5ParameterSpec");
         } catch (ClassNotFoundException var5) {
            throw new NoClassDefFoundError(var5.getMessage());
         }

         class$1 = var10004;
      }

      var10001[1] = var10004;
      var10004 = class$2;
      if (var10004 == null) {
         try {
            var10004 = Class.forName("org.bouncycastle.crypto.spec.IvParameterSpec");
         } catch (ClassNotFoundException var4) {
            throw new NoClassDefFoundError(var4.getMessage());
         }

         class$2 = var10004;
      }

      var10001[2] = var10004;
      var10004 = class$3;
      if (var10004 == null) {
         try {
            var10004 = Class.forName("org.bouncycastle.crypto.spec.PBEParameterSpec");
         } catch (ClassNotFoundException var3) {
            throw new NoClassDefFoundError(var3.getMessage());
         }

         class$3 = var10004;
      }

      var10001[3] = var10004;
      this.availableSpecs = var10001;
      this.ivLength = 0;
      this.padded = true;
      this.pbeSpec = null;
      this.pbeAlgorithm = null;
      this.modeName = null;
      this.baseEngine = engine;
      this.cipher = new PaddedBufferedBlockCipher(engine);
      this.ivLength = ivLength / 8;
   }

   protected int engineGetBlockSize() {
      return this.baseEngine.getBlockSize();
   }

   public byte[] engineGetIV() {
      return this.ivParam != null ? this.ivParam.getIV() : null;
   }

   protected int engineGetKeySize(Key key) {
      return key.getEncoded().length * 8;
   }

   protected int engineGetOutputSize(int inputLen) {
      return this.cipher.getOutputSize(inputLen);
   }

   protected AlgorithmParameters engineGetParameters() {
      if (this.engineParams == null) {
         if (this.pbeSpec != null) {
            try {
               this.engineParams = AlgorithmParameters.getInstance(this.pbeAlgorithm, "BC");
               this.engineParams.init(this.pbeSpec);
            } catch (Exception var4) {
               return null;
            }
         } else if (this.ivParam != null) {
            String name = this.cipher.getUnderlyingCipher().getAlgorithmName();
            if (name.indexOf(47) >= 0) {
               name = name.substring(0, name.indexOf(47));
            }

            try {
               this.engineParams = AlgorithmParameters.getInstance(name, "BC");
               this.engineParams.init(this.ivParam.getIV());
            } catch (Exception var3) {
               throw new RuntimeException(var3.toString());
            }
         }
      }

      return this.engineParams;
   }

   public void engineSetMode(String mode) throws NoSuchAlgorithmException {
      this.modeName = mode.toUpperCase();
      if (this.modeName.equals("ECB")) {
         this.ivLength = 0;
         this.cipher = new PaddedBufferedBlockCipher(this.baseEngine);
      } else if (this.modeName.equals("CBC")) {
         this.ivLength = this.baseEngine.getBlockSize();
         this.cipher = new PaddedBufferedBlockCipher(new CBCBlockCipher(this.baseEngine));
      } else {
         int wordSize;
         if (this.modeName.startsWith("OFB")) {
            this.ivLength = this.baseEngine.getBlockSize();
            if (this.modeName.length() != 3) {
               wordSize = Integer.parseInt(this.modeName.substring(3));
               this.cipher = new PaddedBufferedBlockCipher(new OFBBlockCipher(this.baseEngine, wordSize));
            } else {
               this.cipher = new PaddedBufferedBlockCipher(new OFBBlockCipher(this.baseEngine, 8 * this.baseEngine.getBlockSize()));
            }
         } else if (this.modeName.startsWith("CFB")) {
            this.ivLength = this.baseEngine.getBlockSize();
            if (this.modeName.length() != 3) {
               wordSize = Integer.parseInt(this.modeName.substring(3));
               this.cipher = new PaddedBufferedBlockCipher(new CFBBlockCipher(this.baseEngine, wordSize));
            } else {
               this.cipher = new PaddedBufferedBlockCipher(new CFBBlockCipher(this.baseEngine, 8 * this.baseEngine.getBlockSize()));
            }
         } else if (this.modeName.startsWith("PGP")) {
            if (this.modeName.equalsIgnoreCase("PGPCFBwithIV")) {
               this.ivLength = this.baseEngine.getBlockSize();
               this.cipher = new PaddedBufferedBlockCipher(new PGPCFBBlockCipher(this.baseEngine, true));
            } else {
               this.ivLength = this.baseEngine.getBlockSize();
               this.cipher = new PaddedBufferedBlockCipher(new PGPCFBBlockCipher(this.baseEngine, false));
            }
         } else if (this.modeName.equalsIgnoreCase("OpenPGPCFB")) {
            this.ivLength = 0;
            this.cipher = new PaddedBufferedBlockCipher(new OpenPGPCFBBlockCipher(this.baseEngine));
         } else if (this.modeName.startsWith("GOFB")) {
            this.ivLength = this.baseEngine.getBlockSize();
            this.cipher = new BufferedBlockCipher(new GOFBBlockCipher(this.baseEngine));
         } else {
            if (!this.modeName.startsWith("CTS")) {
               throw new NoSuchAlgorithmException("can't support mode " + mode);
            }

            this.ivLength = this.baseEngine.getBlockSize();
            this.cipher = new CTSBlockCipher(new CBCBlockCipher(this.baseEngine));
         }
      }

   }

   protected void engineSetPadding(String padding) throws NoSuchPaddingException {
      String paddingName = padding.toUpperCase();
      if (paddingName.equals("NOPADDING")) {
         this.padded = false;
         if (!(this.cipher instanceof CTSBlockCipher)) {
            this.cipher = new BufferedBlockCipher(this.cipher.getUnderlyingCipher());
         }
      } else if (!paddingName.equals("PKCS5PADDING") && !paddingName.equals("PKCS7PADDING")) {
         if (paddingName.equals("ZEROBYTEPADDING")) {
            this.cipher = new PaddedBufferedBlockCipher(this.cipher.getUnderlyingCipher(), new ZeroBytePadding());
         } else if (!paddingName.equals("ISO10126PADDING") && !paddingName.equals("ISO10126-2PADDING")) {
            if (!paddingName.equals("X9.23PADDING") && !paddingName.equals("X923PADDING")) {
               if (!paddingName.equals("ISO7816-4PADDING") && !paddingName.equals("ISO9797-1PADDING")) {
                  if (paddingName.equals("TBCPADDING")) {
                     this.cipher = new PaddedBufferedBlockCipher(this.cipher.getUnderlyingCipher(), new TBCPadding());
                  } else {
                     if (!paddingName.equals("WITHCTS")) {
                        throw new NoSuchPaddingException("Padding " + padding + " unknown.");
                     }

                     this.padded = false;
                     this.cipher = new CTSBlockCipher(this.cipher.getUnderlyingCipher());
                  }
               } else {
                  this.cipher = new PaddedBufferedBlockCipher(this.cipher.getUnderlyingCipher(), new ISO7816d4Padding());
               }
            } else {
               this.cipher = new PaddedBufferedBlockCipher(this.cipher.getUnderlyingCipher(), new X923Padding());
            }
         } else {
            this.cipher = new PaddedBufferedBlockCipher(this.cipher.getUnderlyingCipher(), new ISO10126d2Padding());
         }
      } else {
         this.cipher = new PaddedBufferedBlockCipher(this.cipher.getUnderlyingCipher());
      }

   }

   protected void engineInit(int opmode, Key key, AlgorithmParameterSpec params, SecureRandom random) throws InvalidKeyException, InvalidAlgorithmParameterException {
      this.pbeSpec = null;
      this.pbeAlgorithm = null;
      this.engineParams = null;
      if (!(key instanceof SecretKey)) {
         throw new InvalidKeyException("Key for algorithm " + key.getAlgorithm() + " not suitable for symmetric enryption.");
      } else if (params == null && this.baseEngine.getAlgorithmName().startsWith("RC5-64")) {
         throw new InvalidAlgorithmParameterException("RC5 requires an RC5ParametersSpec to be passed in.");
      } else {
         Object param;
         if (key instanceof JCEPBEKey) {
            JCEPBEKey k = (JCEPBEKey)key;
            if (k.getOID() != null) {
               this.pbeAlgorithm = k.getOID().getId();
            } else {
               this.pbeAlgorithm = k.getAlgorithm();
            }

            if (k.getParam() != null) {
               param = k.getParam();
               this.pbeSpec = new PBEParameterSpec(k.getSalt(), k.getIterationCount());
            } else {
               if (!(params instanceof PBEParameterSpec)) {
                  throw new InvalidAlgorithmParameterException("PBE requires PBE parameters to be set.");
               }

               this.pbeSpec = (PBEParameterSpec)params;
               param = PBE.Util.makePBEParameters(k, params, this.cipher.getUnderlyingCipher().getAlgorithmName());
            }

            if (param instanceof ParametersWithIV) {
               this.ivParam = (ParametersWithIV)param;
            }
         } else if (params == null) {
            param = new KeyParameter(key.getEncoded());
         } else {
            if (!(params instanceof IvParameterSpec)) {
               throw new InvalidAlgorithmParameterException("unknown parameter type.");
            }

            if (this.ivLength != 0) {
               IvParameterSpec p = (IvParameterSpec)params;
               if (p.getIV().length != this.ivLength) {
                  throw new InvalidAlgorithmParameterException("IV must be " + this.ivLength + " bytes long.");
               }

               param = new ParametersWithIV(new KeyParameter(key.getEncoded()), p.getIV());
               this.ivParam = (ParametersWithIV)param;
            } else {
               if (this.modeName != null && this.modeName.equals("ECB")) {
                  throw new InvalidAlgorithmParameterException("ECB mode does not use an IV");
               }

               param = new KeyParameter(key.getEncoded());
            }
         }

         if (this.ivLength != 0 && !(param instanceof ParametersWithIV)) {
            SecureRandom ivRandom = random;
            if (random == null) {
               ivRandom = new SecureRandom();
            }

            if (opmode != 1 && opmode != 3) {
               if (this.cipher.getUnderlyingCipher().getAlgorithmName().indexOf("PGPCFB") < 0) {
                  throw new InvalidAlgorithmParameterException("no IV set when one expected");
               }
            } else {
               byte[] iv = new byte[this.ivLength];
               ivRandom.nextBytes(iv);
               param = new ParametersWithIV((CipherParameters)param, iv);
               this.ivParam = (ParametersWithIV)param;
            }
         }

         if (random != null && this.padded) {
            param = new ParametersWithRandom((CipherParameters)param, random);
         }

         try {
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
               throw new InvalidParameterException("unknown opmode " + opmode + " passed");
            }

         } catch (Exception var8) {
            throw new InvalidKeyException(var8.getMessage());
         }
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

      this.engineInit(opmode, key, paramSpec, random);
      this.engineParams = params;
   }

   public void engineInit(int opmode, Key key, SecureRandom random) throws InvalidKeyException {
      try {
         this.engineInit(opmode, key, (AlgorithmParameterSpec)null, random);
      } catch (InvalidAlgorithmParameterException var5) {
         throw new InvalidKeyException(var5.getMessage());
      }
   }

   protected byte[] engineUpdate(byte[] input, int inputOffset, int inputLen) {
      int length = this.cipher.getUpdateOutputSize(inputLen);
      if (length > 0) {
         byte[] out = new byte[length];
         int len = this.cipher.processBytes(input, inputOffset, inputLen, out, 0);
         if (len == 0) {
            return null;
         } else if (len != out.length) {
            byte[] tmp = new byte[len];
            System.arraycopy(out, 0, tmp, 0, len);
            return tmp;
         } else {
            return out;
         }
      } else {
         this.cipher.processBytes(input, inputOffset, inputLen, (byte[])null, 0);
         return null;
      }
   }

   protected int engineUpdate(byte[] input, int inputOffset, int inputLen, byte[] output, int outputOffset) throws ShortBufferException {
      try {
         return this.cipher.processBytes(input, inputOffset, inputLen, output, outputOffset);
      } catch (DataLengthException var7) {
         throw new ShortBufferException(var7.getMessage());
      }
   }

   public byte[] engineDoFinal(byte[] input, int inputOffset, int inputLen) throws IllegalBlockSizeException, BadPaddingException {
      int len = 0;
      byte[] tmp = new byte[this.engineGetOutputSize(inputLen)];
      if (inputLen != 0) {
         len = this.cipher.processBytes(input, inputOffset, inputLen, tmp, 0);
      }

      try {
         len += this.cipher.doFinal(tmp, len);
      } catch (DataLengthException var7) {
         throw new IllegalBlockSizeException(var7.getMessage());
      } catch (InvalidCipherTextException var8) {
         throw new BadPaddingException(var8.getMessage());
      }

      byte[] out = new byte[len];
      System.arraycopy(tmp, 0, out, 0, len);
      return out;
   }

   protected int engineDoFinal(byte[] input, int inputOffset, int inputLen, byte[] output, int outputOffset) throws IllegalBlockSizeException, BadPaddingException {
      int len = 0;
      if (inputLen != 0) {
         len = this.cipher.processBytes(input, inputOffset, inputLen, output, outputOffset);
      }

      try {
         return len + this.cipher.doFinal(output, outputOffset + len);
      } catch (DataLengthException var8) {
         throw new IllegalBlockSizeException(var8.getMessage());
      } catch (InvalidCipherTextException var9) {
         throw new BadPaddingException(var9.getMessage());
      }
   }

   public static class AES extends JCEBlockCipher {
      public AES() {
         super(new AESFastEngine());
      }
   }

   public static class AESCBC extends JCEBlockCipher {
      public AESCBC() {
         super(new CBCBlockCipher(new AESFastEngine()), 128);
      }
   }

   public static class AESCFB extends JCEBlockCipher {
      public AESCFB() {
         super(new CFBBlockCipher(new AESFastEngine(), 128), 128);
      }
   }

   public static class AESOFB extends JCEBlockCipher {
      public AESOFB() {
         super(new OFBBlockCipher(new AESFastEngine(), 128), 128);
      }
   }

   public static class CAST5 extends JCEBlockCipher {
      public CAST5() {
         super(new CAST5Engine());
      }
   }

   public static class CAST5CBC extends JCEBlockCipher {
      public CAST5CBC() {
         super(new CBCBlockCipher(new CAST5Engine()), 64);
      }
   }

   public static class CAST6 extends JCEBlockCipher {
      public CAST6() {
         super(new CAST6Engine());
      }
   }

   public static class DES extends JCEBlockCipher {
      public DES() {
         super(new DESEngine());
      }
   }

   public static class DESCBC extends JCEBlockCipher {
      public DESCBC() {
         super(new CBCBlockCipher(new DESEngine()), 64);
      }
   }

   public static class DESede extends JCEBlockCipher {
      public DESede() {
         super(new DESedeEngine());
      }
   }

   public static class DESedeCBC extends JCEBlockCipher {
      public DESedeCBC() {
         super(new CBCBlockCipher(new DESedeEngine()), 64);
      }
   }

   public static class IDEA extends JCEBlockCipher {
      public IDEA() {
         super(new IDEAEngine());
      }
   }

   public static class IDEACBC extends JCEBlockCipher {
      public IDEACBC() {
         super(new CBCBlockCipher(new IDEAEngine()), 64);
      }
   }

   public static class PBEWithAESCBC extends JCEBlockCipher {
      public PBEWithAESCBC() {
         super(new CBCBlockCipher(new AESFastEngine()));
      }
   }

   public static class PBEWithMD5AndDES extends JCEBlockCipher {
      public PBEWithMD5AndDES() {
         super(new CBCBlockCipher(new DESEngine()));
      }
   }

   public static class PBEWithSHA1AndDES extends JCEBlockCipher {
      public PBEWithSHA1AndDES() {
         super(new CBCBlockCipher(new DESEngine()));
      }
   }

   public static class PBEWithSHAAndDES2Key extends JCEBlockCipher {
      public PBEWithSHAAndDES2Key() {
         super(new CBCBlockCipher(new DESedeEngine()));
      }
   }

   public static class PBEWithSHAAndDES3Key extends JCEBlockCipher {
      public PBEWithSHAAndDES3Key() {
         super(new CBCBlockCipher(new DESedeEngine()));
      }
   }

   public static class PBEWithSHAAndIDEA extends JCEBlockCipher {
      public PBEWithSHAAndIDEA() {
         super(new CBCBlockCipher(new IDEAEngine()));
      }
   }

   public static class Rijndael extends JCEBlockCipher {
      public Rijndael() {
         super(new RijndaelEngine());
      }
   }
}
