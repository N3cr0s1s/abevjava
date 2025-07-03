package org.bouncycastle.crypto.engines;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.Wrapper;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;

public class AESWrapEngine implements Wrapper {
   private BlockCipher engine = new AESEngine();
   private KeyParameter param;
   private boolean forWrapping;
   private byte[] iv = new byte[]{-90, -90, -90, -90, -90, -90, -90, -90};

   public void init(boolean forWrapping, CipherParameters param) {
      this.forWrapping = forWrapping;
      if (param instanceof KeyParameter) {
         this.param = (KeyParameter)param;
      } else if (param instanceof ParametersWithIV) {
         this.iv = ((ParametersWithIV)param).getIV();
         this.param = (KeyParameter)((ParametersWithIV)param).getParameters();
         if (this.iv.length != 8) {
            throw new IllegalArgumentException("IV not multiple of 8");
         }
      }

   }

   public String getAlgorithmName() {
      return "AES";
   }

   public byte[] wrap(byte[] in, int inOff, int inLen) {
      if (!this.forWrapping) {
         throw new IllegalStateException("not set for wrapping");
      } else {
         int n = inLen / 8;
         if (n * 8 != inLen) {
            throw new DataLengthException("wrap data must be a multiple of 8 bytes");
         } else {
            byte[] block = new byte[inLen + this.iv.length];
            byte[] buf = new byte[8 + this.iv.length];
            System.arraycopy(this.iv, 0, block, 0, this.iv.length);
            System.arraycopy(in, 0, block, this.iv.length, inLen);
            this.engine.init(true, this.param);

            for(int j = 0; j != 6; ++j) {
               for(int i = 1; i <= n; ++i) {
                  System.arraycopy(block, 0, buf, 0, this.iv.length);
                  System.arraycopy(block, 8 * i, buf, this.iv.length, 8);
                  this.engine.processBlock(buf, 0, buf, 0);
                  int t = n * j + i;

                  for(int k = 1; t != 0; ++k) {
                     byte v = (byte)t;
                     int var10001 = this.iv.length - k;
                     buf[var10001] ^= v;
                     t >>>= 8;
                  }

                  System.arraycopy(buf, 0, block, 0, 8);
                  System.arraycopy(buf, 8, block, 8 * i, 8);
               }
            }

            return block;
         }
      }
   }

   public byte[] unwrap(byte[] in, int inOff, int inLen) throws InvalidCipherTextException {
      if (this.forWrapping) {
         throw new IllegalStateException("not set for unwrapping");
      } else {
         int n = inLen / 8;
         if (n * 8 != inLen) {
            throw new InvalidCipherTextException("unwrap data must be a multiple of 8 bytes");
         } else {
            byte[] block = new byte[inLen - this.iv.length];
            byte[] a = new byte[this.iv.length];
            byte[] buf = new byte[8 + this.iv.length];
            System.arraycopy(in, 0, a, 0, this.iv.length);
            System.arraycopy(in, this.iv.length, block, 0, inLen - this.iv.length);
            this.engine.init(false, this.param);
            --n;

            int i;
            for(i = 5; i >= 0; --i) {
               for(int j = n; j >= 1; --j) {
                  System.arraycopy(a, 0, buf, 0, this.iv.length);
                  System.arraycopy(block, 8 * (j - 1), buf, this.iv.length, 8);
                  int t = n * j + j;

                  for(int k = 1; t != 0; ++k) {
                     byte v = (byte)t;
                     int var10001 = this.iv.length - k;
                     buf[var10001] ^= v;
                     t >>>= 8;
                  }

                  this.engine.processBlock(buf, 0, buf, 0);
                  System.arraycopy(buf, 0, a, 0, 8);
                  System.arraycopy(buf, 8, block, 8 * (j - 1), 8);
               }
            }

            for(i = 0; i != this.iv.length; ++i) {
               if (a[i] != this.iv[i]) {
                  throw new InvalidCipherTextException("checksum failed");
               }
            }

            return block;
         }
      }
   }
}
