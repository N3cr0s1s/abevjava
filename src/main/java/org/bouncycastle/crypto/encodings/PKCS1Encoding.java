package org.bouncycastle.crypto.encodings;

import java.security.SecureRandom;
import org.bouncycastle.crypto.AsymmetricBlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.ParametersWithRandom;

public class PKCS1Encoding implements AsymmetricBlockCipher {
   private static int HEADER_LENGTH = 10;
   private SecureRandom random;
   private AsymmetricBlockCipher engine;
   private boolean forEncryption;
   private boolean forPrivateKey;

   public PKCS1Encoding(AsymmetricBlockCipher cipher) {
      this.engine = cipher;
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
      this.forPrivateKey = kParam.isPrivate();
      this.forEncryption = forEncryption;
   }

   public int getInputBlockSize() {
      int baseBlockSize = this.engine.getInputBlockSize();
      return this.forEncryption ? baseBlockSize - HEADER_LENGTH : baseBlockSize;
   }

   public int getOutputBlockSize() {
      int baseBlockSize = this.engine.getOutputBlockSize();
      return this.forEncryption ? baseBlockSize : baseBlockSize - HEADER_LENGTH;
   }

   public byte[] processBlock(byte[] in, int inOff, int inLen) throws InvalidCipherTextException {
      return this.forEncryption ? this.encodeBlock(in, inOff, inLen) : this.decodeBlock(in, inOff, inLen);
   }

   private byte[] encodeBlock(byte[] in, int inOff, int inLen) throws InvalidCipherTextException {
      byte[] block = new byte[this.engine.getInputBlockSize()];
      int i;
      if (this.forPrivateKey) {
         block[0] = 1;

         for(i = 1; i != block.length - inLen - 1; ++i) {
            block[i] = -1;
         }
      } else {
         this.random.nextBytes(block);
         block[0] = 2;

         for(i = 1; i != block.length - inLen - 1; ++i) {
            while(block[i] == 0) {
               block[i] = (byte)this.random.nextInt();
            }
         }
      }

      block[block.length - inLen - 1] = 0;
      System.arraycopy(in, inOff, block, block.length - inLen, inLen);
      return this.engine.processBlock(block, 0, block.length);
   }

   private byte[] decodeBlock(byte[] in, int inOff, int inLen) throws InvalidCipherTextException {
      byte[] block = this.engine.processBlock(in, inOff, inLen);
      if (block.length < this.getOutputBlockSize()) {
         throw new InvalidCipherTextException("block truncated");
      } else if (block[0] != 1 && block[0] != 2) {
         throw new InvalidCipherTextException("unknown block type");
      } else {
         int start;
         for(start = 1; start != block.length && block[start] != 0; ++start) {
         }

         ++start;
         if (start < block.length && start >= HEADER_LENGTH) {
            byte[] result = new byte[block.length - start];
            System.arraycopy(block, start, result, 0, result.length);
            return result;
         } else {
            throw new InvalidCipherTextException("no data in block");
         }
      }
   }
}
