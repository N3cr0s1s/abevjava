package org.bouncycastle.jce.provider;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactorySpi;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateCrtKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.pkcs.RSAPrivateKeyStructure;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.crypto.interfaces.DHPrivateKey;
import org.bouncycastle.crypto.interfaces.DHPublicKey;
import org.bouncycastle.crypto.spec.DHPrivateKeySpec;
import org.bouncycastle.crypto.spec.DHPublicKeySpec;

public abstract class JDKKeyFactory extends KeyFactorySpi {
   protected boolean elGamalFactory = false;
   // $FF: synthetic field
   static Class class$0;
   // $FF: synthetic field
   static Class class$1;
   // $FF: synthetic field
   static Class class$2;
   // $FF: synthetic field
   static Class class$3;
   // $FF: synthetic field
   static Class class$4;
   // $FF: synthetic field
   static Class class$5;
   // $FF: synthetic field
   static Class class$6;

   protected KeySpec engineGetKeySpec(Key key, Class spec) throws InvalidKeySpecException {
      Class var10001 = class$0;
      if (var10001 == null) {
         try {
            var10001 = Class.forName("java.security.spec.PKCS8EncodedKeySpec");
         } catch (ClassNotFoundException var10) {
            throw new NoClassDefFoundError(var10.getMessage());
         }

         class$0 = var10001;
      }

      if (spec.isAssignableFrom(var10001) && key.getFormat().equals("PKCS#8")) {
         return new PKCS8EncodedKeySpec(key.getEncoded());
      } else {
         var10001 = class$1;
         if (var10001 == null) {
            try {
               var10001 = Class.forName("java.security.spec.X509EncodedKeySpec");
            } catch (ClassNotFoundException var9) {
               throw new NoClassDefFoundError(var9.getMessage());
            }

            class$1 = var10001;
         }

         if (spec.isAssignableFrom(var10001) && key.getFormat().equals("X.509")) {
            return new X509EncodedKeySpec(key.getEncoded());
         } else {
            var10001 = class$2;
            if (var10001 == null) {
               try {
                  var10001 = Class.forName("java.security.spec.RSAPublicKeySpec");
               } catch (ClassNotFoundException var8) {
                  throw new NoClassDefFoundError(var8.getMessage());
               }

               class$2 = var10001;
            }

            if (spec.isAssignableFrom(var10001) && key instanceof RSAPublicKey) {
               RSAPublicKey k = (RSAPublicKey)key;
               return new RSAPublicKeySpec(k.getModulus(), k.getPublicExponent());
            } else {
               var10001 = class$3;
               if (var10001 == null) {
                  try {
                     var10001 = Class.forName("java.security.spec.RSAPrivateKeySpec");
                  } catch (ClassNotFoundException var7) {
                     throw new NoClassDefFoundError(var7.getMessage());
                  }

                  class$3 = var10001;
               }

               if (spec.isAssignableFrom(var10001) && key instanceof RSAPrivateKey) {
                  RSAPrivateKey k = (RSAPrivateKey)key;
                  return new RSAPrivateKeySpec(k.getModulus(), k.getPrivateExponent());
               } else {
                  var10001 = class$4;
                  if (var10001 == null) {
                     try {
                        var10001 = Class.forName("java.security.spec.RSAPrivateCrtKeySpec");
                     } catch (ClassNotFoundException var6) {
                        throw new NoClassDefFoundError(var6.getMessage());
                     }

                     class$4 = var10001;
                  }

                  if (spec.isAssignableFrom(var10001) && key instanceof RSAPrivateCrtKey) {
                     RSAPrivateCrtKey k = (RSAPrivateCrtKey)key;
                     return new RSAPrivateCrtKeySpec(k.getModulus(), k.getPublicExponent(), k.getPrivateExponent(), k.getPrimeP(), k.getPrimeQ(), k.getPrimeExponentP(), k.getPrimeExponentQ(), k.getCrtCoefficient());
                  } else {
                     var10001 = class$5;
                     if (var10001 == null) {
                        try {
                           var10001 = Class.forName("org.bouncycastle.crypto.spec.DHPrivateKeySpec");
                        } catch (ClassNotFoundException var5) {
                           throw new NoClassDefFoundError(var5.getMessage());
                        }

                        class$5 = var10001;
                     }

                     if (spec.isAssignableFrom(var10001) && key instanceof DHPrivateKey) {
                        DHPrivateKey k = (DHPrivateKey)key;
                        return new DHPrivateKeySpec(k.getX(), k.getParams().getP(), k.getParams().getG());
                     } else {
                        var10001 = class$6;
                        if (var10001 == null) {
                           try {
                              var10001 = Class.forName("org.bouncycastle.crypto.spec.DHPublicKeySpec");
                           } catch (ClassNotFoundException var4) {
                              throw new NoClassDefFoundError(var4.getMessage());
                           }

                           class$6 = var10001;
                        }

                        if (spec.isAssignableFrom(var10001) && key instanceof DHPublicKey) {
                           DHPublicKey k = (DHPublicKey)key;
                           return new DHPublicKeySpec(k.getY(), k.getParams().getP(), k.getParams().getG());
                        } else {
                           throw new RuntimeException("not implemented yet " + key + " " + spec);
                        }
                     }
                  }
               }
            }
         }
      }
   }

   protected Key engineTranslateKey(Key key) throws InvalidKeyException {
      if (key instanceof RSAPublicKey) {
         return new JCERSAPublicKey((RSAPublicKey)key);
      } else if (key instanceof RSAPrivateCrtKey) {
         return new JCERSAPrivateCrtKey((RSAPrivateCrtKey)key);
      } else if (key instanceof RSAPrivateKey) {
         return new JCERSAPrivateKey((RSAPrivateKey)key);
      } else {
         throw new InvalidKeyException("key type unknown");
      }
   }

   static PublicKey createPublicKeyFromDERStream(InputStream in) throws IOException {
      return createPublicKeyFromPublicKeyInfo(new SubjectPublicKeyInfo((ASN1Sequence)(new ASN1InputStream(in)).readObject()));
   }

   static PublicKey createPublicKeyFromPublicKeyInfo(SubjectPublicKeyInfo info) {
      DERObjectIdentifier algOid = info.getAlgorithmId().getObjectId();
      if (RSAUtil.isRsaOid(algOid)) {
         return new JCERSAPublicKey(info);
      } else {
         throw new RuntimeException("algorithm identifier " + algOid + " in key not recognised");
      }
   }

   static PrivateKey createPrivateKeyFromDERStream(InputStream in) throws IOException {
      return createPrivateKeyFromPrivateKeyInfo(new PrivateKeyInfo((ASN1Sequence)(new ASN1InputStream(in)).readObject()));
   }

   static PrivateKey createPrivateKeyFromPrivateKeyInfo(PrivateKeyInfo info) {
      DERObjectIdentifier algOid = info.getAlgorithmId().getObjectId();
      if (RSAUtil.isRsaOid(algOid)) {
         return new JCERSAPrivateCrtKey(info);
      } else {
         throw new RuntimeException("algorithm identifier " + algOid + " in key not recognised");
      }
   }

   public static class DH extends JDKKeyFactory {
      protected PrivateKey engineGeneratePrivate(KeySpec keySpec) throws InvalidKeySpecException {
         if (keySpec instanceof PKCS8EncodedKeySpec) {
            try {
               return JDKKeyFactory.createPrivateKeyFromDERStream(new ByteArrayInputStream(((PKCS8EncodedKeySpec)keySpec).getEncoded()));
            } catch (Exception var3) {
               throw new InvalidKeySpecException(var3.toString());
            }
         } else {
            throw new InvalidKeySpecException("Unknown KeySpec type: " + keySpec.getClass().getName());
         }
      }

      protected PublicKey engineGeneratePublic(KeySpec keySpec) throws InvalidKeySpecException {
         if (keySpec instanceof X509EncodedKeySpec) {
            try {
               return JDKKeyFactory.createPublicKeyFromDERStream(new ByteArrayInputStream(((X509EncodedKeySpec)keySpec).getEncoded()));
            } catch (Exception var3) {
               throw new InvalidKeySpecException(var3.toString());
            }
         } else {
            throw new InvalidKeySpecException("Unknown KeySpec type: " + keySpec.getClass().getName());
         }
      }
   }

   public static class DSA extends JDKKeyFactory {
      protected PrivateKey engineGeneratePrivate(KeySpec keySpec) throws InvalidKeySpecException {
         if (keySpec instanceof PKCS8EncodedKeySpec) {
            try {
               return JDKKeyFactory.createPrivateKeyFromDERStream(new ByteArrayInputStream(((PKCS8EncodedKeySpec)keySpec).getEncoded()));
            } catch (Exception var3) {
               throw new InvalidKeySpecException(var3.toString());
            }
         } else {
            throw new InvalidKeySpecException("Unknown KeySpec type: " + keySpec.getClass().getName());
         }
      }

      protected PublicKey engineGeneratePublic(KeySpec keySpec) throws InvalidKeySpecException {
         if (keySpec instanceof X509EncodedKeySpec) {
            try {
               return JDKKeyFactory.createPublicKeyFromDERStream(new ByteArrayInputStream(((X509EncodedKeySpec)keySpec).getEncoded()));
            } catch (Exception var3) {
               throw new InvalidKeySpecException(var3.toString());
            }
         } else {
            throw new InvalidKeySpecException("Unknown KeySpec type: " + keySpec.getClass().getName());
         }
      }
   }

   public static class EC extends JDKKeyFactory {
      String algorithm;

      public EC() {
         this("EC");
      }

      public EC(String algorithm) {
         this.algorithm = algorithm;
      }

      protected PrivateKey engineGeneratePrivate(KeySpec keySpec) throws InvalidKeySpecException {
         if (keySpec instanceof PKCS8EncodedKeySpec) {
            try {
               return JDKKeyFactory.createPrivateKeyFromDERStream(new ByteArrayInputStream(((PKCS8EncodedKeySpec)keySpec).getEncoded()));
            } catch (Exception var3) {
               throw new InvalidKeySpecException(var3.toString());
            }
         } else {
            throw new InvalidKeySpecException("Unknown KeySpec type: " + keySpec.getClass().getName());
         }
      }

      protected PublicKey engineGeneratePublic(KeySpec keySpec) throws InvalidKeySpecException {
         if (keySpec instanceof X509EncodedKeySpec) {
            try {
               return JDKKeyFactory.createPublicKeyFromDERStream(new ByteArrayInputStream(((X509EncodedKeySpec)keySpec).getEncoded()));
            } catch (Exception var3) {
               throw new InvalidKeySpecException(var3.toString());
            }
         } else {
            throw new InvalidKeySpecException("Unknown KeySpec type: " + keySpec.getClass().getName());
         }
      }
   }

   public static class ECDH extends JDKKeyFactory.EC {
      public ECDH() {
         super("ECDH");
      }
   }

   public static class ECDHC extends JDKKeyFactory.EC {
      public ECDHC() {
         super("ECDHC");
      }
   }

   public static class ECDSA extends JDKKeyFactory.EC {
      public ECDSA() {
         super("ECDSA");
      }
   }

   public static class ECGOST3410 extends JDKKeyFactory.EC {
      public ECGOST3410() {
         super("ECGOST3410");
      }
   }

   public static class ElGamal extends JDKKeyFactory {
      public ElGamal() {
         this.elGamalFactory = true;
      }

      protected PrivateKey engineGeneratePrivate(KeySpec keySpec) throws InvalidKeySpecException {
         if (keySpec instanceof PKCS8EncodedKeySpec) {
            try {
               return JDKKeyFactory.createPrivateKeyFromDERStream(new ByteArrayInputStream(((PKCS8EncodedKeySpec)keySpec).getEncoded()));
            } catch (Exception var3) {
               throw new InvalidKeySpecException(var3.toString());
            }
         } else {
            throw new InvalidKeySpecException("Unknown KeySpec type: " + keySpec.getClass().getName());
         }
      }

      protected PublicKey engineGeneratePublic(KeySpec keySpec) throws InvalidKeySpecException {
         if (keySpec instanceof X509EncodedKeySpec) {
            try {
               return JDKKeyFactory.createPublicKeyFromDERStream(new ByteArrayInputStream(((X509EncodedKeySpec)keySpec).getEncoded()));
            } catch (Exception var3) {
               throw new InvalidKeySpecException(var3.toString());
            }
         } else {
            throw new InvalidKeySpecException("Unknown KeySpec type: " + keySpec.getClass().getName());
         }
      }
   }

   public static class GOST3410 extends JDKKeyFactory {
      protected PrivateKey engineGeneratePrivate(KeySpec keySpec) throws InvalidKeySpecException {
         if (keySpec instanceof PKCS8EncodedKeySpec) {
            try {
               return JDKKeyFactory.createPrivateKeyFromDERStream(new ByteArrayInputStream(((PKCS8EncodedKeySpec)keySpec).getEncoded()));
            } catch (Exception var3) {
               throw new InvalidKeySpecException(var3.toString());
            }
         } else {
            throw new InvalidKeySpecException("Unknown KeySpec type: " + keySpec.getClass().getName());
         }
      }

      protected PublicKey engineGeneratePublic(KeySpec keySpec) throws InvalidKeySpecException {
         if (keySpec instanceof X509EncodedKeySpec) {
            try {
               return JDKKeyFactory.createPublicKeyFromDERStream(new ByteArrayInputStream(((X509EncodedKeySpec)keySpec).getEncoded()));
            } catch (Exception var3) {
               throw new InvalidKeySpecException(var3.toString());
            }
         } else {
            throw new InvalidKeySpecException("Unknown KeySpec type: " + keySpec.getClass().getName());
         }
      }
   }

   public static class RSA extends JDKKeyFactory {
      protected PrivateKey engineGeneratePrivate(KeySpec keySpec) throws InvalidKeySpecException {
         if (keySpec instanceof PKCS8EncodedKeySpec) {
            try {
               return JDKKeyFactory.createPrivateKeyFromDERStream(new ByteArrayInputStream(((PKCS8EncodedKeySpec)keySpec).getEncoded()));
            } catch (Exception var5) {
               try {
                  return new JCERSAPrivateCrtKey(new RSAPrivateKeyStructure((ASN1Sequence)(new ASN1InputStream(new ByteArrayInputStream(((PKCS8EncodedKeySpec)keySpec).getEncoded()))).readObject()));
               } catch (Exception var4) {
                  throw new InvalidKeySpecException(var4.toString());
               }
            }
         } else if (keySpec instanceof RSAPrivateCrtKeySpec) {
            return new JCERSAPrivateCrtKey((RSAPrivateCrtKeySpec)keySpec);
         } else if (keySpec instanceof RSAPrivateKeySpec) {
            return new JCERSAPrivateKey((RSAPrivateKeySpec)keySpec);
         } else {
            throw new InvalidKeySpecException("Unknown KeySpec type: " + keySpec.getClass().getName());
         }
      }

      protected PublicKey engineGeneratePublic(KeySpec keySpec) throws InvalidKeySpecException {
         if (keySpec instanceof X509EncodedKeySpec) {
            try {
               return JDKKeyFactory.createPublicKeyFromDERStream(new ByteArrayInputStream(((X509EncodedKeySpec)keySpec).getEncoded()));
            } catch (Exception var3) {
               throw new InvalidKeySpecException(var3.toString());
            }
         } else if (keySpec instanceof RSAPublicKeySpec) {
            return new JCERSAPublicKey((RSAPublicKeySpec)keySpec);
         } else {
            throw new InvalidKeySpecException("Unknown KeySpec type: " + keySpec.getClass().getName());
         }
      }
   }
}
