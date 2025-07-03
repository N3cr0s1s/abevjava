package org.bouncycastle.openpgp;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchProviderException;
import java.security.Signature;
import java.security.SignatureException;
import org.bouncycastle.bcpg.BCPGInputStream;
import org.bouncycastle.bcpg.BCPGOutputStream;
import org.bouncycastle.bcpg.OnePassSignaturePacket;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class PGPOnePassSignature {
   private OnePassSignaturePacket sigPack;
   private int signatureType;
   private Signature sig;

   PGPOnePassSignature(BCPGInputStream pIn) throws IOException, PGPException {
      this((OnePassSignaturePacket)pIn.readPacket());
   }

   PGPOnePassSignature(OnePassSignaturePacket sigPack) throws PGPException {
      this.sigPack = sigPack;
      this.signatureType = sigPack.getSignatureType();

      try {
         this.sig = Signature.getInstance(PGPUtil.getSignatureName(sigPack.getKeyAlgorithm(), sigPack.getHashAlgorithm()), PGPUtil.getDefaultProvider());
      } catch (Exception var3) {
         throw new PGPException("can't set up signature object.", var3);
      }
   }

   public void initVerify(PGPPublicKey pubKey, BouncyCastleProvider provider) throws NoSuchProviderException, PGPException {
      try {
         this.sig.initVerify(pubKey.getKey(provider));
      } catch (InvalidKeyException var4) {
         throw new PGPException("invalid key.", var4);
      }
   }

   public void update(byte b) throws SignatureException {
      if (this.signatureType == 1) {
         if (b == 10) {
            this.sig.update((byte)13);
            this.sig.update((byte)10);
            return;
         }

         if (b == 13) {
            return;
         }
      }

      this.sig.update(b);
   }

   public void update(byte[] bytes) throws SignatureException {
      if (this.signatureType == 1) {
         for(int i = 0; i != bytes.length; ++i) {
            this.update(bytes[i]);
         }
      } else {
         this.sig.update(bytes);
      }

   }

   public void update(byte[] bytes, int off, int length) throws SignatureException {
      if (this.signatureType == 1) {
         int finish = off + length;

         for(int i = off; i != finish; ++i) {
            this.update(bytes[i]);
         }
      } else {
         this.sig.update(bytes, off, length);
      }

   }

   public boolean verify(PGPSignature pgpSig) throws PGPException, SignatureException {
      this.sig.update(pgpSig.getSignatureTrailer());
      return this.sig.verify(pgpSig.getSignature());
   }

   public long getKeyID() {
      return this.sigPack.getKeyID();
   }

   public int getSignatureType() {
      return this.sigPack.getSignatureType();
   }

   public byte[] getEncoded() throws IOException {
      ByteArrayOutputStream bOut = new ByteArrayOutputStream();
      this.encode(bOut);
      return bOut.toByteArray();
   }

   public void encode(OutputStream outStream) throws IOException {
      BCPGOutputStream out;
      if (outStream instanceof BCPGOutputStream) {
         out = (BCPGOutputStream)outStream;
      } else {
         out = new BCPGOutputStream(outStream);
      }

      out.writePacket(this.sigPack);
   }
}
