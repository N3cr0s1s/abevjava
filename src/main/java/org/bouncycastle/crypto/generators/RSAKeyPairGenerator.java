package org.bouncycastle.crypto.generators;

import java.math.BigInteger;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.AsymmetricCipherKeyPairGenerator;
import org.bouncycastle.crypto.KeyGenerationParameters;
import org.bouncycastle.crypto.params.RSAKeyGenerationParameters;
import org.bouncycastle.crypto.params.RSAKeyParameters;
import org.bouncycastle.crypto.params.RSAPrivateCrtKeyParameters;

public class RSAKeyPairGenerator implements AsymmetricCipherKeyPairGenerator {
   private static BigInteger ONE = BigInteger.valueOf(1L);
   private RSAKeyGenerationParameters param;

   public void init(KeyGenerationParameters param) {
      this.param = (RSAKeyGenerationParameters)param;
   }

   public AsymmetricCipherKeyPair generateKeyPair() {
      int pbitlength = (this.param.getStrength() + 1) / 2;
      int qbitlength = this.param.getStrength() - pbitlength;
      BigInteger e = this.param.getPublicExponent();

      BigInteger p;
      do {
         while(true) {
            p = new BigInteger(pbitlength, 1, this.param.getRandom());
            if (p.mod(e).equals(ONE)) {
               continue;
            }
            break;
         }
      } while(!p.isProbablePrime(this.param.getCertainty()) || !e.gcd(p.subtract(ONE)).equals(ONE));

      while(true) {
         while(true) {
            BigInteger q = new BigInteger(qbitlength, 1, this.param.getRandom());
            if (!q.equals(p) && !q.mod(e).equals(ONE) && q.isProbablePrime(this.param.getCertainty()) && e.gcd(q.subtract(ONE)).equals(ONE)) {
               BigInteger n = p.multiply(q);
               if (n.bitLength() == this.param.getStrength()) {
                  BigInteger phi;
                  if (p.compareTo(q) < 0) {
                     phi = p;
                     p = q;
                     q = phi;
                  }

                  BigInteger pSub1 = p.subtract(ONE);
                  BigInteger qSub1 = q.subtract(ONE);
                  phi = pSub1.multiply(qSub1);
                  BigInteger d = e.modInverse(phi);
                  BigInteger dP = d.remainder(pSub1);
                  BigInteger dQ = d.remainder(qSub1);
                  BigInteger qInv = q.modInverse(p);
                  return new AsymmetricCipherKeyPair(new RSAKeyParameters(false, n, e), new RSAPrivateCrtKeyParameters(n, e, d, p, q, dP, dQ, qInv));
               }

               p = p.max(q);
            }
         }
      }
   }
}
