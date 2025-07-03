package org.bouncycastle.crypto.encodings;

import java.security.SecureRandom;
import org.bouncycastle.crypto.AsymmetricBlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.digests.SHA1Digest;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.ParametersWithRandom;

public class OAEPEncoding implements AsymmetricBlockCipher {
   private byte[] defHash;
   private Digest hash;
   private AsymmetricBlockCipher engine;
   private SecureRandom random;
   private boolean forEncryption;

   public OAEPEncoding(AsymmetricBlockCipher cipher) {
      this(cipher, new SHA1Digest(), (byte[])null);
   }

   public OAEPEncoding(AsymmetricBlockCipher cipher, Digest hash) {
      this(cipher, hash, (byte[])null);
   }

   public OAEPEncoding(AsymmetricBlockCipher cipher, Digest hash, byte[] encodingParams) {
      this.engine = cipher;
      this.hash = hash;
      this.defHash = new byte[hash.getDigestSize()];
      if (encodingParams != null) {
         hash.update(encodingParams, 0, encodingParams.length);
      }

      hash.doFinal(this.defHash, 0);
   }

   public AsymmetricBlockCipher getUnderlyingCipher() {
      return this.engine;
   }

   public void init(boolean forEncryption, CipherParameters param) {
      AsymmetricKeyParameter kParam;
      if (param instanceof ParametersWithRandom) {
         ParametersWithRandom rParam = (ParametersWithRandom)param;
         this.random = rParam.getRandom();
         kParam = (AsymmetricKeyParameter)rParam.getParameters();
      } else {
         this.random = new SecureRandom();
         kParam = (AsymmetricKeyParameter)param;
      }

      this.engine.init(forEncryption, kParam);
      this.forEncryption = forEncryption;
   }

   public int getInputBlockSize() {
      int baseBlockSize = this.engine.getInputBlockSize();
      return this.forEncryption ? baseBlockSize - 1 - 2 * this.defHash.length : baseBlockSize;
   }

   public int getOutputBlockSize() {
      int baseBlockSize = this.engine.getOutputBlockSize();
      return this.forEncryption ? baseBlockSize : baseBlockSize - 1 - 2 * this.defHash.length;
   }

   public byte[] processBlock(byte[] in, int inOff, int inLen) throws InvalidCipherTextException {
      return this.forEncryption ? this.encodeBlock(in, inOff, inLen) : this.decodeBlock(in, inOff, inLen);
   }

   public byte[] encodeBlock(byte[] in, int inOff, int inLen) throws InvalidCipherTextException {
      byte[] block = new byte[this.getInputBlockSize() + 1 + 2 * this.defHash.length];
      System.arraycopy(in, inOff, block, block.length - inLen, inLen);
      block[block.length - inLen - 1] = 1;
      System.arraycopy(this.defHash, 0, block, this.defHash.length, this.defHash.length);
      byte[] seed = new byte[this.defHash.length];
      this.random.nextBytes(seed);
      byte[] mask = this.maskGeneratorFunction1(seed, 0, seed.length, block.length - this.defHash.length);

      int i;
      for(i = this.defHash.length; i != block.length; ++i) {
         block[i] ^= mask[i - this.defHash.length];
      }

      System.arraycopy(seed, 0, block, 0, this.defHash.length);
      mask = this.maskGeneratorFunction1(block, this.defHash.length, block.length - this.defHash.length, this.defHash.length);

      for(i = 0; i != this.defHash.length; ++i) {
         block[i] ^= mask[i];
      }

      return this.engine.processBlock(block, 0, block.length);
   }

   public byte[] decodeBlock(byte[] in, int inOff, int inLen) throws InvalidCipherTextException {
      byte[] data = this.engine.processBlock(in, inOff, inLen);
      byte[] block = (byte[])null;
      if (data.length < this.engine.getOutputBlockSize()) {
         block = new byte[this.engine.getOutputBlockSize()];
         System.arraycopy(data, 0, block, block.length - data.length, data.length);
      } else {
         block = data;
      }

      if (block.length < 2 * this.defHash.length + 1) {
         throw new InvalidCipherTextException("data too short");
      } else {
         byte[] mask = this.maskGeneratorFunction1(block, this.defHash.length, block.length - this.defHash.length, this.defHash.length);

         int start;
         for(start = 0; start != this.defHash.length; ++start) {
            block[start] ^= mask[start];
         }

         mask = this.maskGeneratorFunction1(block, 0, this.defHash.length, block.length - this.defHash.length);

         for(start = this.defHash.length; start != block.length; ++start) {
            block[start] ^= mask[start - this.defHash.length];
         }

         for(start = 0; start != this.defHash.length; ++start) {
            if (this.defHash[start] != block[this.defHash.length + start]) {
               throw new InvalidCipherTextException("data hash wrong");
            }
         }

         for(start = 2 * this.defHash.length; start != block.length && block[start] != 1 && block[start] == 0; ++start) {
         }

         if (start < block.length - 1 && block[start] == 1) {
            ++start;
            byte[] output = new byte[block.length - start];
            System.arraycopy(block, start, output, 0, output.length);
            return output;
         } else {
            throw new InvalidCipherTextException("data start wrong " + start);
         }
      }
   }

   private void ItoOSP(int i, byte[] sp) {
      sp[0] = (byte)(i >>> 24);
      sp[1] = (byte)(i >>> 16);
      sp[2] = (byte)(i >>> 8);
      sp[3] = (byte)(i >>> 0);
   }

   private byte[] maskGeneratorFunction1(byte[] Z, int zOff, int zLen, int length) {
      byte[] mask = new byte[length];
      byte[] hashBuf = new byte[this.defHash.length];
      byte[] C = new byte[4];
      int counter = 0;
      this.hash.reset();

      do {
         this.ItoOSP(counter, C);
         this.hash.update(Z, zOff, zLen);
         this.hash.update(C, 0, C.length);
         this.hash.doFinal(hashBuf, 0);
         System.arraycopy(hashBuf, 0, mask, counter * this.defHash.length, this.defHash.length);
         ++counter;
      } while(counter < length / this.defHash.length);

      if (counter * this.defHash.length < length) {
         this.ItoOSP(counter, C);
         this.hash.update(Z, zOff, zLen);
         this.hash.update(C, 0, C.length);
         this.hash.doFinal(hashBuf, 0);
         System.arraycopy(hashBuf, 0, mask, counter * this.defHash.length, mask.length - counter * this.defHash.length);
      }

      return mask;
   }
}
