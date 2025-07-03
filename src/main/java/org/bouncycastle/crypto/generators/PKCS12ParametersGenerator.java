package org.bouncycastle.crypto.generators;

import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.ExtendedDigest;
import org.bouncycastle.crypto.PBEParametersGenerator;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;

public class PKCS12ParametersGenerator extends PBEParametersGenerator {
   public static final int KEY_MATERIAL = 1;
   public static final int IV_MATERIAL = 2;
   public static final int MAC_MATERIAL = 3;
   private Digest digest;
   private int u;
   private int v;

   public PKCS12ParametersGenerator(Digest digest) {
      this.digest = digest;
      if (digest instanceof ExtendedDigest) {
         this.u = digest.getDigestSize();
         this.v = ((ExtendedDigest)digest).getByteLength();
      } else {
         throw new IllegalArgumentException("Digest " + digest.getAlgorithmName() + " unsupported");
      }
   }

   private void adjust(byte[] a, int aOff, byte[] b) {
      int x = (b[b.length - 1] & 255) + (a[aOff + b.length - 1] & 255) + 1;
      a[aOff + b.length - 1] = (byte)x;
      x >>>= 8;

      for(int i = b.length - 2; i >= 0; --i) {
         x += (b[i] & 255) + (a[aOff + i] & 255);
         a[aOff + i] = (byte)x;
         x >>>= 8;
      }

   }

   private byte[] generateDerivedKey(int idByte, int n) {
      byte[] D = new byte[this.v];
      byte[] dKey = new byte[n];

      for(int i = 0; i != D.length; ++i) {
         D[i] = (byte)idByte;
      }

      byte[] S;
      if (this.salt != null && this.salt.length != 0) {
         S = new byte[this.v * ((this.salt.length + this.v - 1) / this.v)];

         for(int i = 0; i != S.length; ++i) {
            S[i] = this.salt[i % this.salt.length];
         }
      } else {
         S = new byte[0];
      }

      byte[] P;
      if (this.password != null && this.password.length != 0) {
         P = new byte[this.v * ((this.password.length + this.v - 1) / this.v)];

         for(int i = 0; i != P.length; ++i) {
            P[i] = this.password[i % this.password.length];
         }
      } else {
         P = new byte[0];
      }

      byte[] I = new byte[S.length + P.length];
      System.arraycopy(S, 0, I, 0, S.length);
      System.arraycopy(P, 0, I, S.length, P.length);
      byte[] B = new byte[this.v];
      int c = (n + this.u - 1) / this.u;

      for(int i = 1; i <= c; ++i) {
         byte[] A = new byte[this.u];
         this.digest.update(D, 0, D.length);
         this.digest.update(I, 0, I.length);
         this.digest.doFinal(A, 0);

         int j;
         for(j = 1; j != this.iterationCount; ++j) {
            this.digest.update(A, 0, A.length);
            this.digest.doFinal(A, 0);
         }

         for(j = 0; j != B.length; ++j) {
            B[j] = A[j % A.length];
         }

         for(j = 0; j != I.length / this.v; ++j) {
            this.adjust(I, j * this.v, B);
         }

         if (i == c) {
            System.arraycopy(A, 0, dKey, (i - 1) * this.u, dKey.length - (i - 1) * this.u);
         } else {
            System.arraycopy(A, 0, dKey, (i - 1) * this.u, A.length);
         }
      }

      return dKey;
   }

   public CipherParameters generateDerivedParameters(int keySize) {
      keySize /= 8;
      byte[] dKey = this.generateDerivedKey(1, keySize);
      return new KeyParameter(dKey, 0, keySize);
   }

   public CipherParameters generateDerivedParameters(int keySize, int ivSize) {
      keySize /= 8;
      ivSize /= 8;
      byte[] dKey = this.generateDerivedKey(1, keySize);
      byte[] iv = this.generateDerivedKey(2, ivSize);
      return new ParametersWithIV(new KeyParameter(dKey, 0, keySize), iv, 0, ivSize);
   }

   public CipherParameters generateDerivedMacParameters(int keySize) {
      keySize /= 8;
      byte[] dKey = this.generateDerivedKey(3, keySize);
      return new KeyParameter(dKey, 0, keySize);
   }
}
