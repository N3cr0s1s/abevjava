package org.bouncycastle.bcpg;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public class S2K extends BCPGObject {
   private static final int EXPBIAS = 6;
   public static final int SIMPLE = 0;
   public static final int SALTED = 1;
   public static final int SALTED_AND_ITERATED = 3;
   public static final int GNU_DUMMY_S2K = 101;
   int type;
   int algorithm;
   byte[] iv;
   int itCount = -1;
   int protectionMode = -1;

   S2K(InputStream in) throws IOException {
      DataInputStream dIn = new DataInputStream(in);
      this.type = dIn.read();
      this.algorithm = dIn.read();
      if (this.type != 101) {
         if (this.type != 0) {
            this.iv = new byte[8];
            dIn.readFully(this.iv, 0, this.iv.length);
         }

         if (this.type == 3) {
            this.itCount = dIn.read();
         }
      } else {
         dIn.read();
         dIn.read();
         dIn.read();
         this.protectionMode = dIn.read();
      }

   }

   public S2K(int algorithm) {
      this.type = 0;
      this.algorithm = algorithm;
   }

   public S2K(int algorithm, byte[] iv) {
      this.type = 1;
      this.algorithm = algorithm;
      this.iv = iv;
   }

   public S2K(int algorithm, byte[] iv, int itCount) {
      this.type = 3;
      this.algorithm = algorithm;
      this.iv = iv;
      this.itCount = itCount;
   }

   public int getType() {
      return this.type;
   }

   public int getHashAlgorithm() {
      return this.algorithm;
   }

   public byte[] getIV() {
      return this.iv;
   }

   public long getIterationCount() {
      return (long)(16 + (this.itCount & 15) << (this.itCount >> 4) + 6);
   }

   public int getProtectionMode() {
      return this.protectionMode;
   }

   public void encode(BCPGOutputStream out) throws IOException {
      out.write(this.type);
      out.write(this.algorithm);
      if (this.type != 101) {
         if (this.type != 0) {
            out.write(this.iv);
         }

         if (this.type == 3) {
            out.write(this.itCount);
         }
      } else {
         out.write(71);
         out.write(78);
         out.write(85);
         out.write(this.protectionMode);
      }

   }
}
