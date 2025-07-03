package org.bouncycastle.crypto.generators;

import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.PBEParametersGenerator;
import org.bouncycastle.crypto.digests.MD5Digest;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;

public class OpenSSLPBEParametersGenerator extends PBEParametersGenerator {
   private Digest digest = new MD5Digest();

   public void init(byte[] password, byte[] salt) {
      super.init(password, salt, 1);
   }

   private byte[] generateDerivedKey(int bytesNeeded) {
      byte[] buf = new byte[this.digest.getDigestSize()];
      byte[] key = new byte[bytesNeeded];
      int offset = 0;

      while(true) {
         this.digest.update(this.password, 0, this.password.length);
         this.digest.update(this.salt, 0, this.salt.length);
         this.digest.doFinal(buf, 0);
         int len = bytesNeeded > buf.length ? buf.length : bytesNeeded;
         System.arraycopy(buf, 0, key, offset, len);
         offset += len;
         bytesNeeded -= len;
         if (bytesNeeded == 0) {
            return key;
         }

         this.digest.reset();
         this.digest.update(buf, 0, buf.length);
      }
   }

   public CipherParameters generateDerivedParameters(int keySize) {
      keySize /= 8;
      byte[] dKey = this.generateDerivedKey(keySize);
      return new KeyParameter(dKey, 0, keySize);
   }

   public CipherParameters generateDerivedParameters(int keySize, int ivSize) {
      keySize /= 8;
      ivSize /= 8;
      byte[] dKey = this.generateDerivedKey(keySize + ivSize);
      return new ParametersWithIV(new KeyParameter(dKey, 0, keySize), dKey, keySize, ivSize);
   }

   public CipherParameters generateDerivedMacParameters(int keySize) {
      return this.generateDerivedParameters(keySize);
   }
}
