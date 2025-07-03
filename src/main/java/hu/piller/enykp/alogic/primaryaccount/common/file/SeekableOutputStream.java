package hu.piller.enykp.alogic.primaryaccount.common.file;

import me.necrocore.abevjava.NecroImageOutputStream;

import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.OutputStream;
import javax.imageio.stream.FileImageOutputStream;

public class SeekableOutputStream extends OutputStream {
   private FileImageOutputStream fios;
   private FileDescriptor fd;
   private byte[] lineSeparator;
   private String char_set;

   public SeekableOutputStream(File var1, String var2) throws IOException {
      this.char_set = var2;

      String var3;
      try {
         var3 = System.getProperty("line.separator");
      } catch (Exception var5) {
         var3 = null;
      }

      if (var3 == null) {
         this.lineSeparator = "\n".getBytes(var2);
      } else {
         this.lineSeparator = var3.getBytes(var2);
      }

      this.fios = new NecroImageOutputStream(var1);
      this.fd = new FileDescriptor();
   }

   public void write(int var1) throws IOException {
      this.fios.write(var1);
   }

   public void write(byte[] var1) throws IOException {
      this.fios.write(var1, 0, var1.length);
   }

   public void write(byte[] var1, int var2, int var3) throws IOException {
      this.fios.write(var1, var2, var3);
   }

   public void write(String var1) throws IOException {
      this.write(var1, this.char_set);
   }

   public void write(String var1, String var2) throws IOException {
      if (var2 != null) {
         this.write(var1.getBytes(var2));
      } else if (this.char_set != null) {
         this.write(var1.getBytes(var2));
      } else {
         this.write(var1.getBytes());
      }

   }

   public void flush() throws IOException {
      this.fios.flush();
   }

   public void close() throws IOException {
      this.fios.close();
   }

   public void seek(long var1) throws IOException {
      this.fios.seek(var1);
   }

   public void newLine() throws IOException {
      this.write(this.lineSeparator);
   }
}
