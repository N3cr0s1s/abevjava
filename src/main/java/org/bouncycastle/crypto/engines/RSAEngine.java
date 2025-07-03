package org.bouncycastle.crypto.engines;

import java.math.BigInteger;
import org.bouncycastle.crypto.AsymmetricBlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.params.RSAKeyParameters;
import org.bouncycastle.crypto.params.RSAPrivateCrtKeyParameters;

public class RSAEngine implements AsymmetricBlockCipher {
   private RSAKeyParameters key;
   private boolean forEncryption;
   private int shift;

   public void init(boolean forEncryption, CipherParameters param) {
      this.key = (RSAKeyParameters)param;
      this.forEncryption = forEncryption;
      int bitSize = this.key.getModulus().bitLength();
      if (bitSize % 8 == 0) {
         this.shift = 0;
      } else {
         this.shift = 8 - bitSize % 8;
      }

   }

   public int getInputBlockSize() {
      int bitSize = this.key.getModulus().bitLength();
      return this.forEncryption ? (bitSize + 7) / 8 - 1 : (bitSize + 7) / 8;
   }

   public int getOutputBlockSize() {
      int bitSize = this.key.getModulus().bitLength();
      return this.forEncryption ? (bitSize + 7) / 8 : (bitSize + 7) / 8 - 1;
   }

   public byte[] processBlock(byte[] in, int inOff, int inLen) {
      if (inLen > this.getInputBlockSize() + 1) {
         throw new DataLengthException("input too large for RSA cipher.\n");
      } else if (inLen == this.getInputBlockSize() + 1 && (in[inOff] & 128 >> this.shift) != 0) {
         throw new DataLengthException("input too large for RSA cipher.\n");
      } else {
         byte[] block;
         if (inOff == 0 && inLen == in.length) {
            block = in;
         } else {
            block = new byte[inLen];
            System.arraycopy(in, inOff, block, 0, inLen);
         }

         BigInteger input = new BigInteger(1, block);
         byte[] output;
         if (this.key instanceof RSAPrivateCrtKeyParameters) {
            RSAPrivateCrtKeyParameters crtKey = (RSAPrivateCrtKeyParameters)this.key;
            BigInteger p = crtKey.getP();
            BigInteger q = crtKey.getQ();
            BigInteger dP = crtKey.getDP();
            BigInteger dQ = crtKey.getDQ();
            BigInteger qInv = crtKey.getQInv();
            BigInteger mP = input.remainder(p).modPow(dP, p);
            BigInteger mQ = input.remainder(q).modPow(dQ, q);
            BigInteger h = mP.subtract(mQ);
            h = h.multiply(qInv);
            h = h.mod(p);
            BigInteger m = h.multiply(q);
            m = m.add(mQ);
            output = m.toByteArray();
         } else {
            output = input.modPow(this.key.getExponent(), this.key.getModulus()).toByteArray();
         }

         byte[] tmp;
         if (this.forEncryption) {
            if (output[0] == 0 && output.length > this.getOutputBlockSize()) {
               tmp = new byte[output.length - 1];
               System.arraycopy(output, 1, tmp, 0, tmp.length);
               return tmp;
            }

            if (output.length < this.getOutputBlockSize()) {
               tmp = new byte[this.getOutputBlockSize()];
               System.arraycopy(output, 0, tmp, tmp.length - output.length, output.length);
               return tmp;
            }
         } else if (output[0] == 0) {
            tmp = new byte[output.length - 1];
            System.arraycopy(output, 1, tmp, 0, tmp.length);
            return tmp;
         }

         return output;
      }
   }
}
