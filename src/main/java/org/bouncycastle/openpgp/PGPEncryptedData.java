package org.bouncycastle.openpgp;

import java.io.IOException;
import java.io.InputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import org.bouncycastle.bcpg.InputStreamPacket;
import org.bouncycastle.bcpg.SymmetricEncIntegrityPacket;
import org.bouncycastle.bcpg.SymmetricKeyAlgorithmTags;

public abstract class PGPEncryptedData implements SymmetricKeyAlgorithmTags {
   InputStreamPacket encData;
   InputStream encStream;
   PGPEncryptedData.TruncatedStream truncStream;

   PGPEncryptedData(InputStreamPacket encData) {
      this.encData = encData;
   }

   public InputStream getInputStream() {
      return this.encData.getInputStream();
   }

   public boolean isIntegrityProtected() {
      return this.encData instanceof SymmetricEncIntegrityPacket;
   }

   public boolean verify() throws PGPException, IOException {
      if (!this.isIntegrityProtected()) {
         throw new PGPException("data not integrity protected.");
      } else {
         DigestInputStream dIn = (DigestInputStream)this.encStream;
         boolean var2 = false;

         while(this.encStream.read() >= 0) {
         }

         MessageDigest hash = dIn.getMessageDigest();
         int[] lookAhead = this.truncStream.getLookAhead();
         hash.update((byte)lookAhead[0]);
         hash.update((byte)lookAhead[1]);
         byte[] digest = hash.digest();
         byte[] streamDigest = new byte[digest.length];

         for(int i = 0; i != streamDigest.length; ++i) {
            streamDigest[i] = (byte)lookAhead[i + 2];
         }

         return MessageDigest.isEqual(digest, streamDigest);
      }
   }

   protected class TruncatedStream extends InputStream {
      int[] lookAhead = new int[22];
      int bufPtr;
      InputStream in;

      TruncatedStream(InputStream in) throws IOException {
         for(int i = 0; i != this.lookAhead.length; ++i) {
            this.lookAhead[i] = in.read();
         }

         this.bufPtr = 0;
         this.in = in;
      }

      public int read() throws IOException {
         int ch = this.in.read();
         if (ch >= 0) {
            int c = this.lookAhead[this.bufPtr];
            this.lookAhead[this.bufPtr] = ch;
            this.bufPtr = (this.bufPtr + 1) % this.lookAhead.length;
            return c;
         } else {
            return -1;
         }
      }

      int[] getLookAhead() {
         int[] tmp = new int[this.lookAhead.length];
         int count = 0;

         int i;
         for(i = this.bufPtr; i != this.lookAhead.length; ++i) {
            tmp[count++] = this.lookAhead[i];
         }

         for(i = 0; i != this.bufPtr; ++i) {
            tmp[count++] = this.lookAhead[i];
         }

         return tmp;
      }
   }
}
