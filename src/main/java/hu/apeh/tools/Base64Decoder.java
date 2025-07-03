package hu.apeh.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

public final class Base64Decoder {

   private final static Logger logger = LoggerFactory.getLogger(Base64Decoder.class);

   private static final int BUFFER_SIZE = 511;
   InputStream in = null;
   OutputStream out = null;
   boolean stringp = false;

   private final int get1(byte[] buf, int off) {
      return (buf[off] & 63) << 2 | (buf[off + 1] & 48) >>> 4;
   }

   private final int get2(byte[] buf, int off) {
      return (buf[off + 1] & 15) << 4 | (buf[off + 2] & 60) >>> 2;
   }

   private final int get3(byte[] buf, int off) {
      return (buf[off + 2] & 3) << 6 | buf[off + 3] & 63;
   }

   private final int check(int ch) {
      if (ch >= 65 && ch <= 90) {
         return ch - 65;
      } else if (ch >= 97 && ch <= 122) {
         return ch - 97 + 26;
      } else if (ch >= 48 && ch <= 57) {
         return ch - 48 + 52;
      } else {
         switch(ch) {
         case 43:
            return 62;
         case 47:
            return 63;
         case 61:
            return 65;
         default:
            return -1;
         }
      }
   }

   public void process() throws IOException {
      byte[] buffer = new byte[511];
      byte[] chunk = new byte[4];
      int got = 0;
      int ready = 0;

      int cGot;
      label44:
      while((cGot = this.in.read(buffer)) > 0) {
         for(int skiped = 0; skiped < cGot; ready = 0) {
            while(ready < 4) {
               if (skiped >= cGot) {
                  continue label44;
               }

               int ch = this.check(buffer[skiped++]);
               if (ch >= 0) {
                  chunk[ready++] = (byte)ch;
               }
            }

            if (chunk[2] == 65) {
               this.out.write(this.get1(chunk, 0));
               return;
            }

            if (chunk[3] == 65) {
               this.out.write(this.get1(chunk, 0));
               this.out.write(this.get2(chunk, 0));
               return;
            }

            this.out.write(this.get1(chunk, 0));
            this.out.write(this.get2(chunk, 0));
            this.out.write(this.get3(chunk, 0));
         }
      }

      if (ready != 0) {
         throw new IOException("Base64Decode: Invalid length.");
      } else {
         this.out.flush();
         this.in.close();
         this.out.close();
      }
   }

   public String processString() throws IOException {
      if (!this.stringp) {
         throw new RuntimeException(this.getClass().getName() + "[processString]" + "invalid call (not a String)");
      } else {
         this.process();

         try {
             return ((ByteArrayOutputStream) this.out).toString("ISO-8859-2");
         } catch (UnsupportedEncodingException var3) {
            throw new RuntimeException(this.getClass().getName() + "[processString] Unable to convert" + "properly char to bytes");
         }
      }
   }

   public Base64Decoder(String input) {
      byte[] bytes;
      try {
         bytes = input.getBytes("ISO-8859-2");
      } catch (UnsupportedEncodingException var4) {
         throw new RuntimeException(this.getClass().getName() + "[Constructor] Unable to convert" + "properly char to bytes");
      }

      this.stringp = true;
      this.in = new ByteArrayInputStream(bytes);
      this.out = new ByteArrayOutputStream();
   }

   public Base64Decoder(InputStream in, OutputStream out) {
      this.in = in;
      this.out = out;
      this.stringp = false;
   }
}
