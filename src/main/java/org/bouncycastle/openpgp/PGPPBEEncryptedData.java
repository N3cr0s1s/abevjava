package org.bouncycastle.openpgp;

import java.io.EOFException;
import java.io.InputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.Provider;
import java.security.spec.AlgorithmParameterSpec;
import org.bouncycastle.bcpg.BCPGInputStream;
import org.bouncycastle.bcpg.InputStreamPacket;
import org.bouncycastle.bcpg.SymmetricEncIntegrityPacket;
import org.bouncycastle.bcpg.SymmetricKeyEncSessionPacket;
import org.bouncycastle.crypto.Cipher;
import org.bouncycastle.crypto.CipherInputStream;
import org.bouncycastle.crypto.SecretKey;
import org.bouncycastle.crypto.spec.IvParameterSpec;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class PGPPBEEncryptedData extends PGPEncryptedData {
   SymmetricKeyEncSessionPacket keyData;

   PGPPBEEncryptedData(SymmetricKeyEncSessionPacket keyData, InputStreamPacket encData) {
      super(encData);
      this.keyData = keyData;
   }

   public InputStream getInputStream() {
      return this.encData.getInputStream();
   }

   public InputStream getDataStream(char[] passPhrase, BouncyCastleProvider provider) throws PGPException {
      Cipher c;
      try {
         if (this.encData instanceof SymmetricEncIntegrityPacket) {
            c = Cipher.getInstance(PGPUtil.getSymmetricCipherName(this.keyData.getEncAlgorithm()) + "/CFB/NoPadding", (Provider)provider);
         } else {
            c = Cipher.getInstance(PGPUtil.getSymmetricCipherName(this.keyData.getEncAlgorithm()) + "/OpenPGPCFB/NoPadding", (Provider)provider);
         }
      } catch (PGPException var8) {
         throw var8;
      } catch (Exception var9) {
         throw new PGPException("exception creating cipher", var9);
      }

      if (c != null) {
         try {
            SecretKey key = PGPUtil.makeKeyFromPassPhrase(this.keyData.getEncAlgorithm(), this.keyData.getS2K(), passPhrase, provider);
            byte[] iv = new byte[c.getBlockSize()];
            c.init(2, key, (AlgorithmParameterSpec)(new IvParameterSpec(iv)));
            this.encStream = new BCPGInputStream(new CipherInputStream(this.encData.getInputStream(), c));
            if (this.encData instanceof SymmetricEncIntegrityPacket) {
               this.truncStream = new PGPEncryptedData.TruncatedStream(this.encStream);
               this.encStream = new DigestInputStream(this.truncStream, MessageDigest.getInstance(PGPUtil.getDigestName(2), provider));
            }

            int v1;
            int v2;
            for(v1 = 0; v1 != iv.length; ++v1) {
               v2 = this.encStream.read();
               if (v2 < 0) {
                  throw new EOFException("unexpected end of stream.");
               }

               iv[v1] = (byte)v2;
            }

            v1 = this.encStream.read();
            v2 = this.encStream.read();
            if (v1 >= 0 && v2 >= 0) {
               if (iv[iv.length - 2] != (byte)v1 && v1 != 0) {
                  throw new PGPDataValidationException("data check failed.");
               } else if (iv[iv.length - 1] != (byte)v2 && v2 != 0) {
                  throw new PGPDataValidationException("data check failed.");
               } else {
                  return this.encStream;
               }
            } else {
               throw new EOFException("unexpected end of stream.");
            }
         } catch (PGPException var10) {
            throw var10;
         } catch (Exception var11) {
            throw new PGPException("Exception creating cipher", var11);
         }
      } else {
         return this.encData.getInputStream();
      }
   }
}
