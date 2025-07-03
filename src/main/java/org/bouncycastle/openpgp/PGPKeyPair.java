package org.bouncycastle.openpgp;

import java.security.KeyPair;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class PGPKeyPair {
   PGPPublicKey pub;
   PGPPrivateKey priv;

   public PGPKeyPair(int algorithm, KeyPair keyPair, Date time, BouncyCastleProvider provider) throws PGPException, NoSuchProviderException {
      this(algorithm, keyPair.getPublic(), keyPair.getPrivate(), time, provider);
   }

   public PGPKeyPair(int algorithm, PublicKey pubKey, PrivateKey privKey, Date time, BouncyCastleProvider provider) throws PGPException, NoSuchProviderException {
      this.pub = new PGPPublicKey(algorithm, pubKey, time, provider);
      this.priv = new PGPPrivateKey(privKey, this.pub.getKeyID());
   }

   public PGPKeyPair(PGPPublicKey pub, PGPPrivateKey priv) {
      this.pub = pub;
      this.priv = priv;
   }

   public long getKeyID() {
      return this.pub.getKeyID();
   }

   public PGPPublicKey getPublicKey() {
      return this.pub;
   }

   public PGPPrivateKey getPrivateKey() {
      return this.priv;
   }
}
