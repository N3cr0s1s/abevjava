package org.bouncycastle.openpgp;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import org.bouncycastle.bcpg.BCPGInputStream;
import org.bouncycastle.bcpg.LiteralDataPacket;

public class PGPLiteralData {
   public static final char BINARY = 'b';
   public static final char TEXT = 't';
   public static final String CONSOLE = "_CONSOLE";
   public static final Date NOW = new Date(0L);
   LiteralDataPacket data;

   public PGPLiteralData(BCPGInputStream pIn) throws IOException {
      this.data = (LiteralDataPacket)pIn.readPacket();
   }

   public int getFormat() {
      return this.data.getFormat();
   }

   public String getFileName() {
      return this.data.getFileName();
   }

   public Date getModificationTime() {
      return new Date(this.data.getModificationTime());
   }

   public InputStream getInputStream() {
      return this.data.getInputStream();
   }

   public InputStream getDataStream() {
      return this.getInputStream();
   }
}
