package org.bouncycastle.crypto;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

public class SealedObject implements Serializable {
   private static final long serialVersionUID = 4482838265551344752L;
   protected byte[] encodedParams;
   private byte[] encryptedContent;
   private String paramsAlg;
   private String sealAlg;

   public SealedObject(Serializable object, Cipher c) throws IOException, IllegalBlockSizeException {
      ByteArrayOutputStream bOut = new ByteArrayOutputStream();
      ObjectOutputStream oOut = new ObjectOutputStream(bOut);
      oOut.writeObject(object);
      oOut.close();
      byte[] encodedObject = bOut.toByteArray();
      if (c == null) {
         throw new IllegalArgumentException("crypto object is null!");
      } else {
         try {
            this.encryptedContent = c.doFinal(encodedObject);
         } catch (BadPaddingException var7) {
            throw new IOException(var7.getMessage());
         }

         this.sealAlg = c.getAlgorithm();
         AlgorithmParameters params = c.getParameters();
         if (params != null) {
            this.encodedParams = params.getEncoded();
            this.paramsAlg = params.getAlgorithm();
         }

      }
   }

   protected SealedObject(SealedObject so) {
      if (so.encodedParams != null) {
         this.encodedParams = (byte[])so.encodedParams.clone();
      }

      if (so.encryptedContent != null) {
         this.encryptedContent = (byte[])so.encryptedContent.clone();
      }

      this.paramsAlg = so.paramsAlg;
      this.sealAlg = so.sealAlg;
   }

   public final String getAlgorithm() {
      return this.sealAlg;
   }

   public final Object getObject(Key key) throws IOException, ClassNotFoundException, NoSuchAlgorithmException, InvalidKeyException {
      if (key == null) {
         throw new IllegalArgumentException("key object is null!");
      } else {
         try {
            return this.getObject(key, (String)null);
         } catch (NoSuchProviderException var3) {
            throw new NoSuchAlgorithmException(var3.getMessage());
         }
      }
   }

   public final Object getObject(Cipher c) throws IOException, ClassNotFoundException, IllegalBlockSizeException, BadPaddingException {
      if (c == null) {
         throw new IllegalArgumentException("crypto object is null!");
      } else {
         byte[] encodedObject = c.doFinal(this.encryptedContent);
         ObjectInputStream oIn = new ObjectInputStream(new ByteArrayInputStream(encodedObject));
         return oIn.readObject();
      }
   }

   public final Object getObject(Key key, String provider) throws IOException, ClassNotFoundException, NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException {
      if (key == null) {
         throw new IllegalArgumentException("key object is null!");
      } else {
         Cipher cipher = null;

         try {
            if (provider != null) {
               cipher = Cipher.getInstance(this.sealAlg, provider);
            } else {
               cipher = Cipher.getInstance(this.sealAlg);
            }
         } catch (NoSuchPaddingException var9) {
            throw new NoSuchAlgorithmException(var9.getMessage());
         }

         if (this.paramsAlg == null) {
            cipher.init(2, key);
         } else {
            AlgorithmParameters algParams = AlgorithmParameters.getInstance(this.paramsAlg);
            algParams.init(this.encodedParams);

            try {
               cipher.init(2, key, (AlgorithmParameters)algParams);
            } catch (InvalidAlgorithmParameterException var8) {
               throw new IOException(var8.getMessage());
            }
         }

         try {
            return this.getObject(cipher);
         } catch (BadPaddingException var6) {
            throw new IOException(var6.getMessage());
         } catch (IllegalBlockSizeException var7) {
            throw new IOException(var7.getMessage());
         }
      }
   }
}
