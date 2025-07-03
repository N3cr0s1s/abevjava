package org.bouncycastle.openpgp;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import org.bouncycastle.bcpg.BCPGOutputStream;

public class PGPLiteralDataGenerator {
   public static final char BINARY = 'b';
   public static final char TEXT = 't';
   public static final String CONSOLE = "_CONSOLE";
   public static final Date NOW;
   private BCPGOutputStream pkOut;
   private boolean oldFormat = false;

   static {
      NOW = PGPLiteralData.NOW;
   }

   public PGPLiteralDataGenerator() {
   }

   public PGPLiteralDataGenerator(boolean oldFormat) {
      this.oldFormat = oldFormat;
   }

   private void writeHeader(OutputStream out, char format, String name, long modificationTime) throws IOException {
      out.write(format);
      out.write((byte)name.length());

      for(int i = 0; i != name.length(); ++i) {
         out.write(name.charAt(i));
      }

      long modDate = modificationTime / 1000L;
      out.write((byte)((int)(modDate >> 24)));
      out.write((byte)((int)(modDate >> 16)));
      out.write((byte)((int)(modDate >> 8)));
      out.write((byte)((int)modDate));
   }

   public OutputStream open(OutputStream out, char format, String name, long length, Date modificationTime) throws IOException {
      this.pkOut = new BCPGOutputStream(out, 11, length + 2L + (long)name.length() + 4L, this.oldFormat);
      this.writeHeader(this.pkOut, format, name, modificationTime.getTime());
      return this.pkOut;
   }

   public OutputStream open(OutputStream out, char format, String name, Date modificationTime, byte[] buffer) throws IOException {
      this.pkOut = new BCPGOutputStream(out, 11, buffer);
      this.writeHeader(this.pkOut, format, name, modificationTime.getTime());
      return this.pkOut;
   }

   public OutputStream open(OutputStream out, char format, File file) throws IOException {
      this.pkOut = new BCPGOutputStream(out, 11, file.length() + 2L + (long)file.getName().length() + 4L, this.oldFormat);
      this.writeHeader(this.pkOut, format, file.getName(), file.lastModified());
      return this.pkOut;
   }

   public void close() throws IOException {
      this.pkOut.finish();
      this.pkOut.flush();
   }
}
