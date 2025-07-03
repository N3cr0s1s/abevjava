package org.bouncycastle.openpgp;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchProviderException;
import java.security.Signature;
import java.security.SignatureException;
import java.util.Date;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1OutputStream;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.bcpg.BCPGInputStream;
import org.bouncycastle.bcpg.BCPGOutputStream;
import org.bouncycastle.bcpg.MPInteger;
import org.bouncycastle.bcpg.SignaturePacket;
import org.bouncycastle.bcpg.SignatureSubpacket;
import org.bouncycastle.bcpg.TrustPacket;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class PGPSignature {
   public static final int BINARY_DOCUMENT = 0;
   public static final int CANONICAL_TEXT_DOCUMENT = 1;
   public static final int STAND_ALONE = 2;
   public static final int DEFAULT_CERTIFICATION = 16;
   public static final int NO_CERTIFICATION = 17;
   public static final int CASUAL_CERTIFICATION = 18;
   public static final int POSITIVE_CERTIFICATION = 19;
   public static final int SUBKEY_BINDING = 24;
   public static final int DIRECT_KEY = 31;
   public static final int KEY_REVOCATION = 32;
   public static final int SUBKEY_REVOCATION = 40;
   public static final int CERTIFICATION_REVOCATION = 48;
   public static final int TIMESTAMP = 64;
   private SignaturePacket sigPck;
   private Signature sig;
   private int signatureType;
   private TrustPacket trustPck;
   private byte lastb;

   PGPSignature(BCPGInputStream pIn) throws IOException, PGPException {
      this((SignaturePacket)pIn.readPacket());
   }

   PGPSignature(SignaturePacket sigPacket) throws PGPException {
      this.sigPck = sigPacket;
      this.signatureType = this.sigPck.getSignatureType();
      this.trustPck = null;
   }

   PGPSignature(SignaturePacket sigPacket, TrustPacket trustPacket) throws PGPException {
      this(sigPacket);
      this.trustPck = trustPacket;
   }

   private void getSig(BouncyCastleProvider provider) throws PGPException {
      try {
         this.sig = Signature.getInstance(PGPUtil.getSignatureName(this.sigPck.getKeyAlgorithm(), this.sigPck.getHashAlgorithm()), provider);
      } catch (Exception var3) {
         throw new PGPException("can't set up signature object.", var3);
      }
   }

   public int getVersion() {
      return this.sigPck.getVersion();
   }

   public void initVerify(PGPPublicKey pubKey, BouncyCastleProvider provider) throws NoSuchProviderException, PGPException {
      if (this.sig == null) {
         this.getSig(provider);
      }

      try {
         this.sig.initVerify(pubKey.getKey(provider));
      } catch (InvalidKeyException var4) {
         throw new PGPException("invalid key.", var4);
      }

      this.lastb = 0;
   }

   public void update(byte b) throws SignatureException {
      if (this.signatureType == 1) {
         if (b == 13) {
            this.sig.update((byte)13);
            this.sig.update((byte)10);
         } else if (b == 10) {
            if (this.lastb != 13) {
               this.sig.update((byte)13);
               this.sig.update((byte)10);
            }
         } else {
            this.sig.update(b);
         }

         this.lastb = b;
      } else {
         this.sig.update(b);
      }

   }

   public void update(byte[] bytes) throws SignatureException {
      this.update(bytes, 0, bytes.length);
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

   public boolean verify() throws PGPException, SignatureException {
      this.sig.update(this.getSignatureTrailer());
      return this.sig.verify(this.getSignature());
   }

   public boolean verifyCertification(String id, PGPPublicKey key) throws PGPException, SignatureException {
      byte[] keyBytes = this.getEncodedPublicKey(key);
      this.update((byte)-103);
      this.update((byte)(keyBytes.length >> 8));
      this.update((byte)keyBytes.length);
      this.update(keyBytes);
      byte[] idBytes = new byte[id.length()];

      for(int i = 0; i != idBytes.length; ++i) {
         idBytes[i] = (byte)id.charAt(i);
      }

      this.update((byte)-76);
      this.update((byte)(idBytes.length >> 24));
      this.update((byte)(idBytes.length >> 16));
      this.update((byte)(idBytes.length >> 8));
      this.update((byte)idBytes.length);
      this.update(idBytes);
      this.update(this.sigPck.getSignatureTrailer());
      return this.sig.verify(this.getSignature());
   }

   public boolean verifyCertification(PGPPublicKey masterKey, PGPPublicKey pubKey) throws SignatureException, PGPException {
      byte[] keyBytes = this.getEncodedPublicKey(masterKey);
      this.update((byte)-103);
      this.update((byte)(keyBytes.length >> 8));
      this.update((byte)keyBytes.length);
      this.update(keyBytes);
      keyBytes = this.getEncodedPublicKey(pubKey);
      this.update((byte)-103);
      this.update((byte)(keyBytes.length >> 8));
      this.update((byte)keyBytes.length);
      this.update(keyBytes);
      this.update(this.sigPck.getSignatureTrailer());
      return this.sig.verify(this.getSignature());
   }

   public boolean verifyCertification(PGPPublicKey pubKey) throws SignatureException, PGPException {
      if (this.getSignatureType() != 32 && this.getSignatureType() != 40) {
         throw new IllegalStateException("signature is not a key signature");
      } else {
         byte[] keyBytes = this.getEncodedPublicKey(pubKey);
         this.update((byte)-103);
         this.update((byte)(keyBytes.length >> 8));
         this.update((byte)keyBytes.length);
         this.update(keyBytes);
         this.update(this.sigPck.getSignatureTrailer());
         return this.sig.verify(this.getSignature());
      }
   }

   public int getSignatureType() {
      return this.sigPck.getSignatureType();
   }

   public long getKeyID() {
      return this.sigPck.getKeyID();
   }

   public Date getCreationTime() {
      return new Date(this.sigPck.getCreationTime());
   }

   public byte[] getSignatureTrailer() {
      return this.sigPck.getSignatureTrailer();
   }

   public PGPSignatureSubpacketVector getHashedSubPackets() {
      return this.createSubpacketVector(this.sigPck.getHashedSubPackets());
   }

   public PGPSignatureSubpacketVector getUnhashedSubPackets() {
      return this.createSubpacketVector(this.sigPck.getUnhashedSubPackets());
   }

   private PGPSignatureSubpacketVector createSubpacketVector(SignatureSubpacket[] pcks) {
      return pcks != null ? new PGPSignatureSubpacketVector(pcks) : null;
   }

   public byte[] getSignature() throws PGPException {
      MPInteger[] sigValues = this.sigPck.getSignature();
      byte[] signature;
      if (sigValues.length == 1) {
         byte[] sBytes = sigValues[0].getValue().toByteArray();
         if (sBytes[0] == 0) {
            signature = new byte[sBytes.length - 1];
            System.arraycopy(sBytes, 1, signature, 0, signature.length);
         } else {
            signature = sBytes;
         }
      } else {
         ByteArrayOutputStream bOut = new ByteArrayOutputStream();
         ASN1OutputStream aOut = new ASN1OutputStream(bOut);

         try {
            ASN1EncodableVector v = new ASN1EncodableVector();
            v.add(new DERInteger(sigValues[0].getValue()));
            v.add(new DERInteger(sigValues[1].getValue()));
            aOut.writeObject(new DERSequence(v));
         } catch (IOException var6) {
            throw new PGPException("exception encoding DSA sig.", var6);
         }

         signature = bOut.toByteArray();
      }

      return signature;
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

      out.writePacket(this.sigPck);
      if (this.trustPck != null) {
         out.writePacket(this.trustPck);
      }

   }

   private byte[] getEncodedPublicKey(PGPPublicKey pubKey) throws PGPException {
      try {
         byte[] keyBytes = pubKey.publicPk.getEncodedContents();
         return keyBytes;
      } catch (IOException var4) {
         throw new PGPException("exception preparing key.", var4);
      }
   }
}
