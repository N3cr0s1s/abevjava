package org.bouncycastle.jce.provider;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.AlgorithmParameterSpec;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1Null;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.nist.NISTObjectIdentifiers;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.DigestInfo;
import org.bouncycastle.asn1.x509.X509ObjectIdentifiers;
import org.bouncycastle.crypto.AsymmetricBlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.MD5Digest;
import org.bouncycastle.crypto.digests.SHA1Digest;
import org.bouncycastle.crypto.digests.SHA224Digest;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.digests.SHA384Digest;
import org.bouncycastle.crypto.digests.SHA512Digest;
import org.bouncycastle.crypto.encodings.PKCS1Encoding;
import org.bouncycastle.crypto.engines.RSAEngine;

public class JDKDigestSignature extends Signature implements PKCSObjectIdentifiers, X509ObjectIdentifiers {
   private Digest digest;
   private AsymmetricBlockCipher cipher;
   private AlgorithmIdentifier algId;

   protected JDKDigestSignature(String name, DERObjectIdentifier objId, Digest digest, AsymmetricBlockCipher cipher) {
      super(name);
      this.digest = digest;
      this.cipher = cipher;
      this.algId = new AlgorithmIdentifier(objId, (DEREncodable)null);
   }

   protected void engineInitVerify(PublicKey publicKey) throws InvalidKeyException {
      if (!(publicKey instanceof RSAPublicKey)) {
         throw new InvalidKeyException("Supplied key (" + this.getType(publicKey) + ") is not a RSAPublicKey instance");
      } else {
         CipherParameters param = RSAUtil.generatePublicKeyParameter((RSAPublicKey)publicKey);
         this.digest.reset();
         this.cipher.init(false, param);
      }
   }

   protected void engineInitSign(PrivateKey privateKey) throws InvalidKeyException {
      if (!(privateKey instanceof RSAPrivateKey)) {
         throw new InvalidKeyException("Supplied key (" + this.getType(privateKey) + ") is not a RSAPrivateKey instance");
      } else {
         CipherParameters param = RSAUtil.generatePrivateKeyParameter((RSAPrivateKey)privateKey);
         this.digest.reset();
         this.cipher.init(true, param);
      }
   }

   private String getType(Object o) {
      return o == null ? null : o.getClass().getName();
   }

   protected void engineUpdate(byte b) throws SignatureException {
      this.digest.update(b);
   }

   protected void engineUpdate(byte[] b, int off, int len) throws SignatureException {
      this.digest.update(b, off, len);
   }

   protected byte[] engineSign() throws SignatureException {
      byte[] hash = new byte[this.digest.getDigestSize()];
      this.digest.doFinal(hash, 0);

      try {
         byte[] bytes = this.derEncode(hash);
         return this.cipher.processBlock(bytes, 0, bytes.length);
      } catch (ArrayIndexOutOfBoundsException var3) {
         throw new SignatureException("key too small for signature type");
      } catch (Exception var4) {
         throw new SignatureException(var4.toString());
      }
   }

   private boolean isNull(DEREncodable o) {
      return o instanceof ASN1Null || o == null;
   }

   protected boolean engineVerify(byte[] sigBytes) throws SignatureException {
      byte[] hash = new byte[this.digest.getDigestSize()];
      this.digest.doFinal(hash, 0);

      DigestInfo digInfo;
      try {
         byte[] sig = this.cipher.processBlock(sigBytes, 0, sigBytes.length);
         digInfo = this.derDecode(sig);
      } catch (Exception var7) {
         return false;
      }

      if (!digInfo.getAlgorithmId().getObjectId().equals(this.algId.getObjectId())) {
         return false;
      } else if (!this.isNull(digInfo.getAlgorithmId().getParameters())) {
         return false;
      } else {
         byte[] sigHash = digInfo.getDigest();
         if (hash.length != sigHash.length) {
            return false;
         } else {
            for(int i = 0; i < hash.length; ++i) {
               if (sigHash[i] != hash[i]) {
                  return false;
               }
            }

            return true;
         }
      }
   }

   protected void engineSetParameter(AlgorithmParameterSpec params) {
      throw new UnsupportedOperationException("engineSetParameter unsupported");
   }

   /** @deprecated */
   protected void engineSetParameter(String param, Object value) {
      throw new UnsupportedOperationException("engineSetParameter unsupported");
   }

   /** @deprecated */
   protected Object engineGetParameter(String param) {
      throw new UnsupportedOperationException("engineSetParameter unsupported");
   }

   private byte[] derEncode(byte[] hash) throws IOException {
      DigestInfo dInfo = new DigestInfo(this.algId, hash);
      return dInfo.getEncoded("DER");
   }

   private DigestInfo derDecode(byte[] encoding) throws IOException {
      if (encoding[0] != 48) {
         throw new IOException("not a digest info object");
      } else {
         ByteArrayInputStream bIn = new ByteArrayInputStream(encoding);
         ASN1InputStream aIn = new ASN1InputStream(bIn);
         return new DigestInfo((ASN1Sequence)aIn.readObject());
      }
   }

   public static class MD5WithRSAEncryption extends JDKDigestSignature {
      public MD5WithRSAEncryption() {
         super("MD5withRSA", md5, new MD5Digest(), new PKCS1Encoding(new RSAEngine()));
      }
   }

   public static class SHA1WithRSAEncryption extends JDKDigestSignature {
      public SHA1WithRSAEncryption() {
         super("SHA1withRSA", id_SHA1, new SHA1Digest(), new PKCS1Encoding(new RSAEngine()));
      }
   }

   public static class SHA224WithRSAEncryption extends JDKDigestSignature {
      public SHA224WithRSAEncryption() {
         super("SHA224withRSA", NISTObjectIdentifiers.id_sha224, new SHA224Digest(), new PKCS1Encoding(new RSAEngine()));
      }
   }

   public static class SHA256WithRSAEncryption extends JDKDigestSignature {
      public SHA256WithRSAEncryption() {
         super("SHA256withRSA", NISTObjectIdentifiers.id_sha256, new SHA256Digest(), new PKCS1Encoding(new RSAEngine()));
      }
   }

   public static class SHA384WithRSAEncryption extends JDKDigestSignature {
      public SHA384WithRSAEncryption() {
         super("SHA384withRSA", NISTObjectIdentifiers.id_sha384, new SHA384Digest(), new PKCS1Encoding(new RSAEngine()));
      }
   }

   public static class SHA512WithRSAEncryption extends JDKDigestSignature {
      public SHA512WithRSAEncryption() {
         super("SHA512withRSA", NISTObjectIdentifiers.id_sha512, new SHA512Digest(), new PKCS1Encoding(new RSAEngine()));
      }
   }
}
