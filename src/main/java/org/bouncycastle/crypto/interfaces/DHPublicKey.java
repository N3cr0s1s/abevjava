package org.bouncycastle.crypto.interfaces;

import org.bouncycastle.crypto.spec.DHParameterSpec;

import java.math.BigInteger;
import java.security.PublicKey;

public interface DHPublicKey extends DHKey, PublicKey {
   BigInteger getY();

   /**
    * Returns the standard algorithm name for this key. For
    * example, "DSA" would indicate that this key is a DSA key.
    * See the key related sections (KeyFactory, KeyGenerator,
    * KeyPairGenerator, and SecretKeyFactory) in the <a href=
    * "{@docRoot}/../specs/security/standard-names.html">
    * Java Security Standard Algorithm Names Specification</a>
    * for information about standard key algorithm names.
    *
    * @return the name of the algorithm associated with this key.
    */
   @Override
   default String getAlgorithm() {
      return "";
   }

   /**
    * Returns the name of the primary encoding format of this key,
    * or {@code null} if this key does not support encoding.
    * The primary encoding format is
    * named in terms of the appropriate ASN.1 data format, if an
    * ASN.1 specification for this key exists.
    * For example, the name of the ASN.1 data format for public
    * keys is <I>SubjectPublicKeyInfo</I>, as
    * defined by the X.509 standard; in this case, the returned format is
    * {@code "X.509"}. Similarly,
    * the name of the ASN.1 data format for private keys is
    * <I>PrivateKeyInfo</I>,
    * as defined by the PKCS #8 standard; in this case, the returned format is
    * {@code "PKCS#8"}.
    *
    * @return the primary encoding format of the key.
    */
   @Override
   default String getFormat() {
      return "";
   }

   /**
    * Returns the key in its primary encoding format, or {@code null}
    * if this key does not support encoding.
    *
    * @return the encoded key, or {@code null} if the key does not support
    * encoding.
    */
   @Override
   default byte[] getEncoded() {
      return new byte[0];
   }

   @Override
   default DHParameterSpec getParams() {
      return null;
   }
}
