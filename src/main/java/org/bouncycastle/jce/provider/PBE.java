package org.bouncycastle.jce.provider;

import java.security.spec.AlgorithmParameterSpec;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.PBEParametersGenerator;
import org.bouncycastle.crypto.digests.MD5Digest;
import org.bouncycastle.crypto.digests.SHA1Digest;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.generators.OpenSSLPBEParametersGenerator;
import org.bouncycastle.crypto.generators.PKCS12ParametersGenerator;
import org.bouncycastle.crypto.params.DESParameters;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.crypto.spec.PBEKeySpec;
import org.bouncycastle.crypto.spec.PBEParameterSpec;

public interface PBE {
   int MD5 = 0;
   int SHA1 = 1;
   int RIPEMD160 = 2;
   int TIGER = 3;
   int SHA256 = 4;
   int PKCS5S1 = 0;
   int PKCS5S2 = 1;
   int PKCS12 = 2;
   int OPENSSL = 3;

   public static class Util {
      private static PBEParametersGenerator makePBEGenerator(int type, int hash) {
         Object generator;
         if (type == 2) {
            switch(hash) {
            case 0:
               generator = new PKCS12ParametersGenerator(new MD5Digest());
               break;
            case 1:
               generator = new PKCS12ParametersGenerator(new SHA1Digest());
               break;
            case 2:
            case 3:
            default:
               throw new IllegalStateException("unknown digest scheme for PBE encryption.");
            case 4:
               generator = new PKCS12ParametersGenerator(new SHA256Digest());
            }
         } else {
            generator = new OpenSSLPBEParametersGenerator();
         }

         return (PBEParametersGenerator)generator;
      }

      static CipherParameters makePBEParameters(JCEPBEKey pbeKey, AlgorithmParameterSpec spec, String targetAlgorithm) {
         if (spec != null && spec instanceof PBEParameterSpec) {
            PBEParameterSpec pbeParam = (PBEParameterSpec)spec;
            PBEParametersGenerator generator = makePBEGenerator(pbeKey.getType(), pbeKey.getDigest());
            byte[] key = pbeKey.getEncoded();
            if (pbeKey.shouldTryWrongPKCS12()) {
               key = new byte[2];
            }

            generator.init(key, pbeParam.getSalt(), pbeParam.getIterationCount());
            CipherParameters param;
            if (pbeKey.getIvSize() != 0) {
               param = generator.generateDerivedParameters(pbeKey.getKeySize(), pbeKey.getIvSize());
            } else {
               param = generator.generateDerivedParameters(pbeKey.getKeySize());
            }

            if (targetAlgorithm.startsWith("DES")) {
               KeyParameter kParam;
               if (param instanceof ParametersWithIV) {
                  kParam = (KeyParameter)((ParametersWithIV)param).getParameters();
                  DESParameters.setOddParity(kParam.getKey());
               } else {
                  kParam = (KeyParameter)param;
                  DESParameters.setOddParity(kParam.getKey());
               }
            }

            for(int i = 0; i != key.length; ++i) {
               key[i] = 0;
            }

            return param;
         } else {
            throw new IllegalArgumentException("Need a PBEParameter spec with a PBE key.");
         }
      }

      static CipherParameters makePBEMacParameters(JCEPBEKey pbeKey, AlgorithmParameterSpec spec) {
         if (spec != null && spec instanceof PBEParameterSpec) {
            PBEParameterSpec pbeParam = (PBEParameterSpec)spec;
            PBEParametersGenerator generator = makePBEGenerator(pbeKey.getType(), pbeKey.getDigest());
            byte[] key = pbeKey.getEncoded();
            if (pbeKey.shouldTryWrongPKCS12()) {
               key = new byte[2];
            }

            generator.init(key, pbeParam.getSalt(), pbeParam.getIterationCount());
            CipherParameters param = generator.generateDerivedMacParameters(pbeKey.getKeySize());

            for(int i = 0; i != key.length; ++i) {
               key[i] = 0;
            }

            return param;
         } else {
            throw new IllegalArgumentException("Need a PBEParameter spec with a PBE key.");
         }
      }

      static CipherParameters makePBEParameters(PBEKeySpec keySpec, int type, int hash, int keySize, int ivSize) {
         PBEParametersGenerator generator = makePBEGenerator(type, hash);
         byte[] key;
         if (type == 2) {
            key = PBEParametersGenerator.PKCS12PasswordToBytes(keySpec.getPassword());
         } else {
            key = PBEParametersGenerator.PKCS5PasswordToBytes(keySpec.getPassword());
         }

         generator.init(key, keySpec.getSalt(), keySpec.getIterationCount());
         CipherParameters param;
         if (ivSize != 0) {
            param = generator.generateDerivedParameters(keySize, ivSize);
         } else {
            param = generator.generateDerivedParameters(keySize);
         }

         for(int i = 0; i != key.length; ++i) {
            key[i] = 0;
         }

         return param;
      }

      static CipherParameters makePBEMacParameters(PBEKeySpec keySpec, int type, int hash, int keySize) {
         PBEParametersGenerator generator = makePBEGenerator(type, hash);
         byte[] key;
         if (type == 2) {
            key = PBEParametersGenerator.PKCS12PasswordToBytes(keySpec.getPassword());
         } else {
            key = PBEParametersGenerator.PKCS5PasswordToBytes(keySpec.getPassword());
         }

         generator.init(key, keySpec.getSalt(), keySpec.getIterationCount());
         CipherParameters param = generator.generateDerivedMacParameters(keySize);

         for(int i = 0; i != key.length; ++i) {
            key[i] = 0;
         }

         return param;
      }
   }
}
