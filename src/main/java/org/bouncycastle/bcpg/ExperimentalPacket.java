package org.bouncycastle.bcpg;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ExperimentalPacket extends ContainedPacket implements PublicKeyAlgorithmTags {
   private int tag;
   private byte[] contents;

   ExperimentalPacket(int tag, BCPGInputStream in) throws IOException {
      this.tag = tag;
      if (in.available() != 0) {
         ByteArrayOutputStream bOut = new ByteArrayOutputStream(in.available());

         int b;
         while((b = in.read()) >= 0) {
            bOut.write(b);
         }

         this.contents = bOut.toByteArray();
      } else {
         this.contents = new byte[0];
      }

   }

   public int getTag() {
      return this.tag;
   }

   public byte[] getContents() {
      byte[] tmp = new byte[this.contents.length];
      System.arraycopy(this.contents, 0, tmp, 0, tmp.length);
      return tmp;
   }

   public void encode(BCPGOutputStream out) throws IOException {
      out.writePacket(this.tag, this.contents, true);
   }
}
