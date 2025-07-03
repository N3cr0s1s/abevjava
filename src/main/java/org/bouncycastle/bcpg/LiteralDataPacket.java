package org.bouncycastle.bcpg;

import java.io.IOException;

public class LiteralDataPacket extends InputStreamPacket {
   int format;
   char[] fileName;
   long modDate;

   LiteralDataPacket(BCPGInputStream in) throws IOException {
      super(in);
      this.format = in.read();
      int l = in.read();
      this.fileName = new char[l];

      for(int i = 0; i != this.fileName.length; ++i) {
         this.fileName[i] = (char)in.read();
      }

      this.modDate = (long)in.read() << 24 | (long)(in.read() << 16) | (long)(in.read() << 8) | (long)in.read();
   }

   public int getFormat() {
      return this.format;
   }

   public long getModificationTime() {
      return this.modDate * 1000L;
   }

   public String getFileName() {
      return new String(this.fileName);
   }
}
