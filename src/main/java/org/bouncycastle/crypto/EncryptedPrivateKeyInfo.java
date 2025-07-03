package org.bouncycastle.crypto;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.AlgorithmParameters;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import org.bouncycastle.asn1.DERConstructedSequence;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.DERInputStream;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.DEROutputStream;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;

public class EncryptedPrivateKeyInfo {
   private org.bouncycastle.asn1.pkcs.EncryptedPrivateKeyInfo infoObj;
   private AlgorithmParameters algP;

   public EncryptedPrivateKeyInfo(byte[] encoded) throws NullPointerException, IOException {
      if (encoded == null) {
         throw new NullPointerException("parameters null");
      } else {
         ByteArrayInputStream bIn = new ByteArrayInputStream(encoded);
         DERInputStream dIn = new DERInputStream(bIn);
         this.infoObj = new org.bouncycastle.asn1.pkcs.EncryptedPrivateKeyInfo((DERConstructedSequence)dIn.readObject());

         try {
            this.algP = this.getParameters();
         } catch (NoSuchAlgorithmException var5) {
            throw new IOException("can't create parameters: " + var5.toString());
         }
      }
   }

   public EncryptedPrivateKeyInfo(String algName, byte[] encryptedData) throws NullPointerException, IllegalArgumentException, NoSuchAlgorithmException {
      if (algName != null && encryptedData != null) {
         AlgorithmIdentifier kAlgId = new AlgorithmIdentifier(new DERObjectIdentifier(algName), (DEREncodable)null);
         this.infoObj = new org.bouncycastle.asn1.pkcs.EncryptedPrivateKeyInfo(kAlgId, (byte[])encryptedData.clone());
         this.algP = this.getParameters();
      } else {
         throw new NullPointerException("parameters null");
      }
   }

   public EncryptedPrivateKeyInfo(AlgorithmParameters algParams, byte[] encryptedData) throws NullPointerException, IllegalArgumentException, NoSuchAlgorithmException {
      if (algParams != null && encryptedData != null) {
         AlgorithmIdentifier kAlgId = null;

         try {
            ByteArrayInputStream bIn = new ByteArrayInputStream(algParams.getEncoded());
            DERInputStream dIn = new DERInputStream(bIn);
            kAlgId = new AlgorithmIdentifier(new DERObjectIdentifier(algParams.getAlgorithm()), dIn.readObject());
         } catch (IOException var6) {
            throw new IllegalArgumentException("error in encoding: " + var6.toString());
         }

         this.infoObj = new org.bouncycastle.asn1.pkcs.EncryptedPrivateKeyInfo(kAlgId, (byte[])encryptedData.clone());
         this.algP = this.getParameters();
      } else {
         throw new NullPointerException("parameters null");
      }
   }

   public String getAlgName() {
      return this.infoObj.getEncryptionAlgorithm().getObjectId().getId();
   }

   private AlgorithmParameters getParameters() throws NoSuchAlgorithmException {
      AlgorithmParameters ap = AlgorithmParameters.getInstance(this.getAlgName());
      ByteArrayOutputStream bOut = new ByteArrayOutputStream();
      DEROutputStream dOut = new DEROutputStream(bOut);

      try {
         dOut.writeObject(this.infoObj.getEncryptionAlgorithm().getParameters());
         dOut.close();
         ap.init(bOut.toByteArray());
         return ap;
      } catch (IOException var5) {
         throw new NoSuchAlgorithmException("unable to parse parameters");
      }
   }

   public AlgorithmParameters getAlgParameters() {
      return this.algP;
   }

   public byte[] getEncryptedData() {
      return this.infoObj.getEncryptedData();
   }

   public PKCS8EncodedKeySpec getKeySpec(Cipher c) throws InvalidKeySpecException {
      try {
         return new PKCS8EncodedKeySpec(c.doFinal(this.getEncryptedData()));
      } catch (Exception var3) {
         throw new InvalidKeySpecException("can't get keySpec: " + var3.toString());
      }
   }

   public byte[] getEncoded() throws IOException {
      ByteArrayOutputStream bOut = new ByteArrayOutputStream();
      DEROutputStream dOut = new DEROutputStream(bOut);
      dOut.writeObject(this.infoObj);
      dOut.close();
      return bOut.toByteArray();
   }
}
