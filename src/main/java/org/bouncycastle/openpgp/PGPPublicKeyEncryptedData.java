package org.bouncycastle.openpgp;

import java.io.EOFException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.DigestInputStream;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchProviderException;
import java.security.spec.AlgorithmParameterSpec;
import org.bouncycastle.bcpg.BCPGInputStream;
import org.bouncycastle.bcpg.InputStreamPacket;
import org.bouncycastle.bcpg.PublicKeyEncSessionPacket;
import org.bouncycastle.bcpg.SymmetricEncIntegrityPacket;
import org.bouncycastle.crypto.Cipher;
import org.bouncycastle.crypto.CipherInputStream;
import org.bouncycastle.crypto.SecretKey;
import org.bouncycastle.crypto.spec.IvParameterSpec;
import org.bouncycastle.crypto.spec.SecretKeySpec;

public class PGPPublicKeyEncryptedData extends PGPEncryptedData {
   PublicKeyEncSessionPacket keyData;

   PGPPublicKeyEncryptedData(PublicKeyEncSessionPacket keyData, InputStreamPacket encData) {
      super(encData);
      this.keyData = keyData;
   }

   private static Cipher getKeyCipher(int algorithm, String provider) throws NoSuchProviderException, PGPException {
      try {
         switch(algorithm) {
         case 1:
         case 2:
            return Cipher.getInstance("RSA/ECB/PKCS1Padding", provider);
         case 16:
         case 20:
            return Cipher.getInstance("ElGamal/ECB/PKCS1Padding", provider);
         default:
            throw new PGPException("unknown asymmetric algorithm: " + algorithm);
         }
      } catch (NoSuchProviderException var3) {
         throw var3;
      } catch (PGPException var4) {
         throw var4;
      } catch (Exception var5) {
         throw new PGPException("Exception creating cipher", var5);
      }
   }

   private boolean confirmCheckSum(byte[] sessionInfo) {
      int check = 0;

      for(int i = 1; i != sessionInfo.length - 2; ++i) {
         check += sessionInfo[i] & 255;
      }

      return sessionInfo[sessionInfo.length - 2] == (byte)(check >> 8) && sessionInfo[sessionInfo.length - 1] == (byte)check;
   }

   public long getKeyID() {
      return this.keyData.getKeyID();
   }

   public InputStream getDataStream(PGPPrivateKey privKey, String provider) throws PGPException, NoSuchProviderException {
      Cipher c1 = getKeyCipher(this.keyData.getAlgorithm(), provider);

      try {
         c1.init(2, privKey.getKey());
      } catch (InvalidKeyException var15) {
         throw new PGPException("error setting asymmetric cipher", var15);
      }

      BigInteger[] keyD = this.keyData.getEncSessionKey();
      byte[] plain;
      if (this.keyData.getAlgorithm() == 2 || this.keyData.getAlgorithm() == 1) {
         plain = keyD[0].toByteArray();
         if (plain[0] == 0) {
            c1.update(plain, 1, plain.length - 1);
         } else {
            c1.update(plain);
         }
      }

      try {
         plain = c1.doFinal();
      } catch (Exception var14) {
         throw new PGPException("exception decrypting secret key", var14);
      }

      if (!this.confirmCheckSum(plain)) {
         throw new PGPKeyValidationException("key checksum failed");
      } else {
         Cipher c2;
         try {
            if (this.encData instanceof SymmetricEncIntegrityPacket) {
               c2 = Cipher.getInstance(PGPUtil.getSymmetricCipherName(plain[0]) + "/CFB/NoPadding", provider);
            } else {
               c2 = Cipher.getInstance(PGPUtil.getSymmetricCipherName(plain[0]) + "/OpenPGPCFB/NoPadding", provider);
            }
         } catch (NoSuchProviderException var11) {
            throw var11;
         } catch (PGPException var12) {
            throw var12;
         } catch (Exception var13) {
            throw new PGPException("exception creating cipher", var13);
         }

         if (c2 != null) {
            try {
               SecretKey key = new SecretKeySpec(plain, 1, plain.length - 3, PGPUtil.getSymmetricCipherName(plain[0]));
               byte[] iv = new byte[c2.getBlockSize()];
               c2.init(2, key, (AlgorithmParameterSpec)(new IvParameterSpec(iv)));
               this.encStream = new BCPGInputStream(new CipherInputStream(this.encData.getInputStream(), c2));
               if (this.encData instanceof SymmetricEncIntegrityPacket) {
                  this.truncStream = new PGPEncryptedData.TruncatedStream(this.encStream);
                  this.encStream = new DigestInputStream(this.truncStream, MessageDigest.getInstance(PGPUtil.getDigestName(2), provider));
               }

               int i;
               int ch;
               for(i = 0; i != iv.length; ++i) {
                  ch = this.encStream.read();
                  if (ch < 0) {
                     throw new EOFException("unexpected end of stream.");
                  }

                  iv[i] = (byte)ch;
               }

               i = this.encStream.read();
               ch = this.encStream.read();
               if (i >= 0 && ch >= 0) {
                  return this.encStream;
               } else {
                  throw new EOFException("unexpected end of stream.");
               }
            } catch (PGPException var16) {
               throw var16;
            } catch (Exception var17) {
               throw new PGPException("Exception starting decryption", var17);
            }
         } else {
            return this.encData.getInputStream();
         }
      }
   }
}
