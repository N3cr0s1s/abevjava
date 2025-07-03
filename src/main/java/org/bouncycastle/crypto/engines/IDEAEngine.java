package org.bouncycastle.crypto.engines;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.params.KeyParameter;

public class IDEAEngine implements BlockCipher {
   protected static final int BLOCK_SIZE = 8;
   private int[] workingKey = null;
   private static final int MASK = 65535;
   private static final int BASE = 65537;

   public void init(boolean forEncryption, CipherParameters params) {
      if (params instanceof KeyParameter) {
         this.workingKey = this.generateWorkingKey(forEncryption, ((KeyParameter)params).getKey());
      } else {
         throw new IllegalArgumentException("invalid parameter passed to IDEA init - " + params.getClass().getName());
      }
   }

   public String getAlgorithmName() {
      return "IDEA";
   }

   public int getBlockSize() {
      return 8;
   }

   public int processBlock(byte[] in, int inOff, byte[] out, int outOff) {
      if (this.workingKey == null) {
         throw new IllegalStateException("IDEA engine not initialised");
      } else if (inOff + 8 > in.length) {
         throw new DataLengthException("input buffer too short");
      } else if (outOff + 8 > out.length) {
         throw new DataLengthException("output buffer too short");
      } else {
         this.ideaFunc(this.workingKey, in, inOff, out, outOff);
         return 8;
      }
   }

   public void reset() {
   }

   private int bytesToWord(byte[] in, int inOff) {
      return (in[inOff] << 8 & '\uff00') + (in[inOff + 1] & 255);
   }

   private void wordToBytes(int word, byte[] out, int outOff) {
      out[outOff] = (byte)(word >>> 8);
      out[outOff + 1] = (byte)word;
   }

   private int mul(int x, int y) {
      if (x == 0) {
         x = 65537 - y;
      } else if (y == 0) {
         x = 65537 - x;
      } else {
         int p = x * y;
         y = p & '\uffff';
         x = p >>> 16;
         x = y - x + (y < x ? 1 : 0);
      }

      return x & '\uffff';
   }

   private void ideaFunc(int[] workingKey, byte[] in, int inOff, byte[] out, int outOff) {
      int keyOff = 0;
      int x0 = this.bytesToWord(in, inOff);
      int x1 = this.bytesToWord(in, inOff + 2);
      int x2 = this.bytesToWord(in, inOff + 4);
      int x3 = this.bytesToWord(in, inOff + 6);

      for(int round = 0; round < 8; ++round) {
         x0 = this.mul(x0, workingKey[keyOff++]);
         x1 += workingKey[keyOff++];
         x1 &= 65535;
         x2 += workingKey[keyOff++];
         x2 &= 65535;
         x3 = this.mul(x3, workingKey[keyOff++]);
         int t0 = x1;
         int t1 = x2;
         x2 ^= x0;
         x1 ^= x3;
         x2 = this.mul(x2, workingKey[keyOff++]);
         x1 += x2;
         x1 &= 65535;
         x1 = this.mul(x1, workingKey[keyOff++]);
         x2 += x1;
         x2 &= 65535;
         x0 ^= x1;
         x3 ^= x2;
         x1 ^= t1;
         x2 ^= t0;
      }

      this.wordToBytes(this.mul(x0, workingKey[keyOff++]), out, outOff);
      this.wordToBytes(x2 + workingKey[keyOff++], out, outOff + 2);
      this.wordToBytes(x1 + workingKey[keyOff++], out, outOff + 4);
      this.wordToBytes(this.mul(x3, workingKey[keyOff]), out, outOff + 6);
   }

   private int[] expandKey(byte[] uKey) {
      int[] key = new int[52];
      if (uKey.length < 16) {
         byte[] tmp = new byte[16];
         System.arraycopy(uKey, 0, tmp, tmp.length - uKey.length, uKey.length);
         uKey = tmp;
      }

      int i;
      for(i = 0; i < 8; ++i) {
         key[i] = this.bytesToWord(uKey, i * 2);
      }

      for(i = 8; i < 52; ++i) {
         if ((i & 7) < 6) {
            key[i] = ((key[i - 7] & 127) << 9 | key[i - 6] >> 7) & '\uffff';
         } else if ((i & 7) == 6) {
            key[i] = ((key[i - 7] & 127) << 9 | key[i - 14] >> 7) & '\uffff';
         } else {
            key[i] = ((key[i - 15] & 127) << 9 | key[i - 14] >> 7) & '\uffff';
         }
      }

      return key;
   }

   private int mulInv(int x) {
      if (x < 2) {
         return x;
      } else {
         int t0 = 1;
         int t1 = 65537 / x;

         int q;
         for(int y = 65537 % x; y != 1; t1 = t1 + t0 * q & '\uffff') {
            q = x / y;
            x %= y;
            t0 = t0 + t1 * q & '\uffff';
            if (x == 1) {
               return t0;
            }

            q = y / x;
            y %= x;
         }

         return 1 - t1 & '\uffff';
      }
   }

   int addInv(int x) {
      return 0 - x & '\uffff';
   }

   private int[] invertKey(int[] inKey) {
      int p = 52;
      int[] key = new int[52];
      int inOff = 0;
      inOff = inOff + 1;
      int t1 = this.mulInv(inKey[inOff]);
      int t2 = this.addInv(inKey[inOff++]);
      int t3 = this.addInv(inKey[inOff++]);
      int t4 = this.mulInv(inKey[inOff++]);
      p = p - 1;
      key[p] = t4;
      --p;
      key[p] = t3;
      --p;
      key[p] = t2;
      --p;
      key[p] = t1;

      for(int round = 1; round < 8; ++round) {
         t1 = inKey[inOff++];
         t2 = inKey[inOff++];
         --p;
         key[p] = t2;
         --p;
         key[p] = t1;
         t1 = this.mulInv(inKey[inOff++]);
         t2 = this.addInv(inKey[inOff++]);
         t3 = this.addInv(inKey[inOff++]);
         t4 = this.mulInv(inKey[inOff++]);
         --p;
         key[p] = t4;
         --p;
         key[p] = t2;
         --p;
         key[p] = t3;
         --p;
         key[p] = t1;
      }

      t1 = inKey[inOff++];
      t2 = inKey[inOff++];
      --p;
      key[p] = t2;
      --p;
      key[p] = t1;
      t1 = this.mulInv(inKey[inOff++]);
      t2 = this.addInv(inKey[inOff++]);
      t3 = this.addInv(inKey[inOff++]);
      t4 = this.mulInv(inKey[inOff]);
      --p;
      key[p] = t4;
      --p;
      key[p] = t3;
      --p;
      key[p] = t2;
      --p;
      key[p] = t1;
      return key;
   }

   private int[] generateWorkingKey(boolean forEncryption, byte[] userKey) {
      return forEncryption ? this.expandKey(userKey) : this.invertKey(this.expandKey(userKey));
   }
}
