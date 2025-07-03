package org.bouncycastle.crypto.encodings;

import org.bouncycastle.crypto.AsymmetricBlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.crypto.params.RSAKeyParameters;

public class ISO9796d1Encoding implements AsymmetricBlockCipher {
   private static byte[] shadows = new byte[]{14, 3, 5, 8, 9, 4, 2, 15, 0, 13, 11, 6, 7, 10, 12, 1};
   private static byte[] inverse = new byte[]{8, 15, 6, 1, 5, 2, 11, 12, 3, 4, 13, 10, 14, 9, 0, 7};
   private AsymmetricBlockCipher engine;
   private boolean forEncryption;
   private int bitSize;
   private int padBits = 0;

   public ISO9796d1Encoding(AsymmetricBlockCipher cipher) {
      this.engine = cipher;
   }

   public AsymmetricBlockCipher getUnderlyingCipher() {
      return this.engine;
   }

   public void init(boolean forEncryption, CipherParameters param) {
      RSAKeyParameters kParam = null;
      if (param instanceof ParametersWithRandom) {
         ParametersWithRandom rParam = (ParametersWithRandom)param;
         kParam = (RSAKeyParameters)rParam.getParameters();
      } else {
         kParam = (RSAKeyParameters)param;
      }

      this.engine.init(forEncryption, kParam);
      this.bitSize = kParam.getModulus().bitLength();
      this.forEncryption = forEncryption;
   }

   public int getInputBlockSize() {
      int baseBlockSize = this.engine.getInputBlockSize();
      return this.forEncryption ? (baseBlockSize + 1) / 2 : baseBlockSize;
   }

   public int getOutputBlockSize() {
      int baseBlockSize = this.engine.getOutputBlockSize();
      return this.forEncryption ? baseBlockSize : (baseBlockSize + 1) / 2;
   }

   public void setPadBits(int padBits) {
      if (padBits > 7) {
         throw new IllegalArgumentException("padBits > 7");
      } else {
         this.padBits = padBits;
      }
   }

   public int getPadBits() {
      return this.padBits;
   }

   public byte[] processBlock(byte[] in, int inOff, int inLen) throws InvalidCipherTextException {
      return this.forEncryption ? this.encodeBlock(in, inOff, inLen) : this.decodeBlock(in, inOff, inLen);
   }

   private byte[] encodeBlock(byte[] in, int inOff, int inLen) throws InvalidCipherTextException {
      byte[] block = new byte[(this.bitSize + 7) / 8];
      int r = this.padBits + 1;
      int z = inLen;
      int t = (this.bitSize + 13) / 16;

      int maxBit;
      for(maxBit = 0; maxBit < t; maxBit += z) {
         if (maxBit > t - z) {
            System.arraycopy(in, inOff + inLen - (t - maxBit), block, block.length - t, t - maxBit);
         } else {
            System.arraycopy(in, inOff, block, block.length - (maxBit + z), z);
         }
      }

      for(maxBit = block.length - 2 * t; maxBit != block.length; maxBit += 2) {
         byte val = block[block.length - t + maxBit / 2];
         block[maxBit] = (byte)(shadows[(val & 255) >>> 4] << 4 | shadows[val & 15]);
         block[maxBit + 1] = val;
      }

      block[block.length - 2 * z] = (byte)(block[block.length - 2 * z] ^ r);
      block[block.length - 1] = (byte)(block[block.length - 1] << 4 | 6);
      maxBit = 8 - (this.bitSize - 1) % 8;
      int offSet = 0;
      if (maxBit != 8) {
         block[0] = (byte)(block[0] & 255 >>> maxBit);
         block[0] = (byte)(block[0] | 128 >>> maxBit);
      } else {
         block[0] = 0;
         block[1] = (byte)(block[1] | 128);
         offSet = 1;
      }

      return this.engine.processBlock(block, offSet, block.length - offSet);
   }

   private byte[] decodeBlock(byte[] in, int inOff, int inLen) throws InvalidCipherTextException {
      byte[] block = this.engine.processBlock(in, inOff, inLen);
      int r = 1;
      int t = (this.bitSize + 13) / 16;
      if ((block[block.length - 1] & 15) != 6) {
         throw new InvalidCipherTextException("invalid forcing byte in block");
      } else {
         block[block.length - 1] = (byte)((block[block.length - 1] & 255) >>> 4 | inverse[(block[block.length - 2] & 255) >> 4] << 4);
         block[0] = (byte)(shadows[(block[1] & 255) >>> 4] << 4 | shadows[block[1] & 15]);
         boolean boundaryFound = false;
         int boundary = 0;

         int val;
         for(int i = block.length - 1; i >= block.length - 2 * t; i -= 2) {
            val = shadows[(block[i] & 255) >>> 4] << 4 | shadows[block[i] & 15];
            if (((block[i - 1] ^ val) & 255) != 0) {
               if (boundaryFound) {
                  throw new InvalidCipherTextException("invalid tsums in block");
               }

               boundaryFound = true;
               r = (block[i - 1] ^ val) & 255;
               boundary = i - 1;
            }
         }

         block[boundary] = 0;
         byte[] nblock = new byte[(block.length - boundary) / 2];

         for(val = 0; val < nblock.length; ++val) {
            nblock[val] = block[2 * val + boundary + 1];
         }

         this.padBits = r - 1;
         return nblock;
      }
   }
}
