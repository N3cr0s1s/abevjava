package org.bouncycastle.bcpg;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Vector;
import org.bouncycastle.bcpg.sig.IssuerKeyID;
import org.bouncycastle.bcpg.sig.SignatureCreationTime;

public class SignaturePacket extends ContainedPacket implements PublicKeyAlgorithmTags {
   private int version;
   private int signatureType;
   private long creationTime;
   private long keyID;
   private int keyAlgorithm;
   private int hashAlgorithm;
   private MPInteger[] signature;
   private byte[] fingerPrint;
   private SignatureSubpacket[] hashedData;
   private SignatureSubpacket[] unhashedData;

   SignaturePacket(BCPGInputStream in) throws IOException {
      this.version = in.read();
      int hashedLength;
      if (this.version != 3 && this.version != 2) {
         if (this.version != 4) {
            throw new RuntimeException("unsupported version: " + this.version);
         }

         this.signatureType = in.read();
         this.keyAlgorithm = in.read();
         this.hashAlgorithm = in.read();
         hashedLength = in.read() << 8 | in.read();
         byte[] hashed = new byte[hashedLength];
         in.readFully(hashed);
         SignatureSubpacketInputStream sIn = new SignatureSubpacketInputStream(new ByteArrayInputStream(hashed));
         Vector v = new Vector();

         SignatureSubpacket sub;
         while((sub = sIn.readPacket()) != null) {
            v.addElement(sub);
         }

         this.hashedData = new SignatureSubpacket[v.size()];

         int i;
         for(i = 0; i != this.hashedData.length; ++i) {
            SignatureSubpacket p = (SignatureSubpacket)v.elementAt(i);
            if (p instanceof IssuerKeyID) {
               this.keyID = ((IssuerKeyID)p).getKeyID();
            } else if (p instanceof SignatureCreationTime) {
               this.creationTime = ((SignatureCreationTime)p).getTime().getTime();
            }

            this.hashedData[i] = p;
         }

         i = in.read() << 8 | in.read();
         byte[] unhashed = new byte[i];
         in.readFully(unhashed);
         sIn = new SignatureSubpacketInputStream(new ByteArrayInputStream(unhashed));
         v.removeAllElements();

         while((sub = sIn.readPacket()) != null) {
            v.addElement(sub);
         }

         this.unhashedData = new SignatureSubpacket[v.size()];

         for(int i1 = 0; i1 != this.unhashedData.length; ++i1) {
            SignatureSubpacket p = (SignatureSubpacket)v.elementAt(i1);
            if (p instanceof IssuerKeyID) {
               this.keyID = ((IssuerKeyID)p).getKeyID();
            } else if (p instanceof SignatureCreationTime) {
               this.creationTime = ((SignatureCreationTime)p).getTime().getTime();
            }

            this.unhashedData[i1] = p;
         }
      } else {
         hashedLength = in.read();
         this.signatureType = in.read();
         this.creationTime = ((long)in.read() << 24 | (long)(in.read() << 16) | (long)(in.read() << 8) | (long)in.read()) * 1000L;
         this.keyID |= (long)in.read() << 56;
         this.keyID |= (long)in.read() << 48;
         this.keyID |= (long)in.read() << 40;
         this.keyID |= (long)in.read() << 32;
         this.keyID |= (long)in.read() << 24;
         this.keyID |= (long)in.read() << 16;
         this.keyID |= (long)in.read() << 8;
         this.keyID |= (long)in.read();
         this.keyAlgorithm = in.read();
         this.hashAlgorithm = in.read();
      }

      this.fingerPrint = new byte[2];
      in.readFully(this.fingerPrint);
      switch(this.keyAlgorithm) {
      case 1:
      case 3:
         MPInteger v = new MPInteger(in);
         this.signature = new MPInteger[1];
         this.signature[0] = v;
         break;
      case 16:
      case 20:
         MPInteger p = new MPInteger(in);
         MPInteger g = new MPInteger(in);
         MPInteger y = new MPInteger(in);
         this.signature = new MPInteger[3];
         this.signature[0] = p;
         this.signature[1] = g;
         this.signature[2] = y;
         break;
      case 17:
         MPInteger r = new MPInteger(in);
         MPInteger s = new MPInteger(in);
         this.signature = new MPInteger[2];
         this.signature[0] = r;
         this.signature[1] = s;
         break;
      default:
         throw new IOException("unknown signature key algorithm: " + this.keyAlgorithm);
      }

   }

   public SignaturePacket(int signatureType, long keyID, int keyAlgorithm, int hashAlgorithm, SignatureSubpacket[] hashedData, SignatureSubpacket[] unhashedData, byte[] fingerPrint, MPInteger[] signature) {
      this(4, signatureType, keyID, keyAlgorithm, hashAlgorithm, hashedData, unhashedData, fingerPrint, signature);
   }

   public SignaturePacket(int version, int signatureType, long keyID, int keyAlgorithm, int hashAlgorithm, long creationTime, byte[] fingerPrint, MPInteger[] signature) {
      this(version, signatureType, keyID, keyAlgorithm, hashAlgorithm, (SignatureSubpacket[])null, (SignatureSubpacket[])null, fingerPrint, signature);
      this.creationTime = creationTime;
   }

   public SignaturePacket(int version, int signatureType, long keyID, int keyAlgorithm, int hashAlgorithm, SignatureSubpacket[] hashedData, SignatureSubpacket[] unhashedData, byte[] fingerPrint, MPInteger[] signature) {
      this.version = version;
      this.signatureType = signatureType;
      this.keyID = keyID;
      this.keyAlgorithm = keyAlgorithm;
      this.hashAlgorithm = hashAlgorithm;
      this.hashedData = hashedData;
      this.unhashedData = unhashedData;
      this.fingerPrint = fingerPrint;
      this.signature = signature;
   }

   public int getVersion() {
      return this.version;
   }

   public int getSignatureType() {
      return this.signatureType;
   }

   public long getKeyID() {
      return this.keyID;
   }

   public byte[] getSignatureTrailer() {
      byte[] trailer = (byte[])null;
      if (this.version != 3 && this.version != 2) {
         ByteArrayOutputStream sOut = new ByteArrayOutputStream();

         try {
            sOut.write((byte)this.getVersion());
            sOut.write((byte)this.getSignatureType());
            sOut.write((byte)this.getKeyAlgorithm());
            sOut.write((byte)this.getHashAlgorithm());
            ByteArrayOutputStream hOut = new ByteArrayOutputStream();
            SignatureSubpacket[] hashed = this.getHashedSubPackets();
            int i = 0;

            while(true) {
               if (i == hashed.length) {
                  byte[] data = hOut.toByteArray();
                  sOut.write((byte)(data.length >> 8));
                  sOut.write((byte)data.length);
                  sOut.write(data);
                  byte[] hData = sOut.toByteArray();
                  sOut.write((byte)this.getVersion());
                  sOut.write(-1);
                  sOut.write((byte)(hData.length >> 24));
                  sOut.write((byte)(hData.length >> 16));
                  sOut.write((byte)(hData.length >> 8));
                  sOut.write((byte)hData.length);
                  break;
               }

               hashed[i].encode(hOut);
               ++i;
            }
         } catch (IOException var7) {
            throw new RuntimeException("exception generating trailer: " + var7);
         }

         trailer = sOut.toByteArray();
      } else {
         trailer = new byte[5];
         long time = this.creationTime / 1000L;
         trailer[0] = (byte)this.signatureType;
         trailer[1] = (byte)((int)(time >> 24));
         trailer[2] = (byte)((int)(time >> 16));
         trailer[3] = (byte)((int)(time >> 8));
         trailer[4] = (byte)((int)time);
      }

      return trailer;
   }

   public int getKeyAlgorithm() {
      return this.keyAlgorithm;
   }

   public int getHashAlgorithm() {
      return this.hashAlgorithm;
   }

   public MPInteger[] getSignature() {
      return this.signature;
   }

   public SignatureSubpacket[] getHashedSubPackets() {
      return this.hashedData;
   }

   public SignatureSubpacket[] getUnhashedSubPackets() {
      return this.unhashedData;
   }

   public long getCreationTime() {
      return this.creationTime;
   }

   public void encode(BCPGOutputStream out) throws IOException {
      ByteArrayOutputStream bOut = new ByteArrayOutputStream();
      BCPGOutputStream pOut = new BCPGOutputStream(bOut);
      pOut.write(this.version);
      if (this.version != 3 && this.version != 2) {
         if (this.version != 4) {
            throw new IOException("unknown version: " + this.version);
         }

         pOut.write(this.signatureType);
         pOut.write(this.keyAlgorithm);
         pOut.write(this.hashAlgorithm);
         ByteArrayOutputStream sOut = new ByteArrayOutputStream();

         for(int i = 0; i != this.hashedData.length; ++i) {
            this.hashedData[i].encode(sOut);
         }

         byte[] data = sOut.toByteArray();
         pOut.write(data.length >> 8);
         pOut.write(data.length);
         pOut.write(data);
         sOut.reset();

         for(int i = 0; i != this.unhashedData.length; ++i) {
            this.unhashedData[i].encode(sOut);
         }

         data = sOut.toByteArray();
         pOut.write(data.length >> 8);
         pOut.write(data.length);
         pOut.write(data);
      } else {
         pOut.write(5);
         long time = this.creationTime / 1000L;
         pOut.write(this.signatureType);
         pOut.write((byte)((int)(time >> 24)));
         pOut.write((byte)((int)(time >> 16)));
         pOut.write((byte)((int)(time >> 8)));
         pOut.write((byte)((int)time));
         pOut.write((byte)((int)(this.keyID >> 56)));
         pOut.write((byte)((int)(this.keyID >> 48)));
         pOut.write((byte)((int)(this.keyID >> 40)));
         pOut.write((byte)((int)(this.keyID >> 32)));
         pOut.write((byte)((int)(this.keyID >> 24)));
         pOut.write((byte)((int)(this.keyID >> 16)));
         pOut.write((byte)((int)(this.keyID >> 8)));
         pOut.write((byte)((int)this.keyID));
         pOut.write(this.keyAlgorithm);
         pOut.write(this.hashAlgorithm);
      }

      pOut.write(this.fingerPrint);

      for(int i = 0; i != this.signature.length; ++i) {
         pOut.writeObject(this.signature[i]);
      }

      out.writePacket(2, bOut.toByteArray(), true);
   }
}
