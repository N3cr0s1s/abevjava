package hu.piller.enykp.util.content;

import me.necrocore.abevjava.NecroFileOutputStream;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ContentUtil {
   public static final int READ_BUFFER_SIZE = 2048;

   public static String getDefaultEncoding() {
      return System.getProperty("file.encoding");
   }

   public static String getXmlContentEncoding(String var0) {
      Matcher var1 = Pattern.compile("<\\?xml.*encoding.*\\?>", 32).matcher(var0);

      String var2;
      int var3;
      int var4;
      for(var2 = null; var1.find(); var2 = var1.group().substring(var3, var4)) {
         var3 = var1.group().indexOf("encoding");
         var3 += "encoding=\"".length();

         for(var4 = var3; var1.group().charAt(var4) != '"' && var4 < var1.group().length(); ++var4) {
         }
      }

      return var2 == null ? getDefaultEncoding() : var2;
   }

   public static String getXmlFileEncoding(String var0) {
      FileInputStream var1 = null;

      String var3;
      try {
         var1 = new FileInputStream(var0);
         boolean var2 = false;
         boolean var20 = false;
         StringBuffer var4 = new StringBuffer();
         boolean var5 = false;

         int var21;
         while((var21 = var1.read()) != -1) {
            if (var21 == 60) {
               var2 = true;
            } else if (var21 == 62) {
               var20 = true;
            }

            if (var2) {
               var4.append((char)var21);
            }

            if (var20) {
               break;
            }
         }

         String var6 = var4.toString();
         String var7;
         if (var6 == null || "".equals(var6.trim())) {
            var7 = getDefaultEncoding();
            return var7;
         }

         var7 = getXmlContentEncoding(var6);
         return var7;
      } catch (IOException var18) {
         var3 = getDefaultEncoding();
      } finally {
         if (var1 != null) {
            try {
               var1.close();
            } catch (IOException var17) {
            }
         }

      }

      return var3;
   }

   public static byte[] readBytesFromStream(InputStream var0) throws ContentUtilException {
      ByteArrayOutputStream var1 = new ByteArrayOutputStream();

      try {
         byte[] var2 = new byte[2048];
         boolean var3 = false;

         int var5;
         while((var5 = var0.read(var2, 0, var2.length)) != -1) {
            var1.write(var2, 0, var5);
         }

         var1.flush();
         return var1.toByteArray();
      } catch (IOException var4) {
         throw new ContentUtilException(var4);
      }
   }

   public static byte[] readBytesFromFile(String var0) throws ContentUtilException {
      FileInputStream var1 = null;

      byte[] var2;
      try {
         var1 = new FileInputStream(var0);
         var2 = readBytesFromStream(var1);
      } catch (IOException var11) {
         throw new ContentUtilException(var11.getMessage());
      } finally {
         if (var1 != null) {
            try {
               var1.close();
            } catch (IOException var10) {
               throw new ContentUtilException(var10);
            }
         }

      }

      return var2;
   }

   public static String readTextFromFile(String var0) throws ContentUtilException {
      FileInputStream var1 = null;

      String var2;
      try {
         var1 = new FileInputStream(var0);
         var2 = new String(readBytesFromStream(var1));
      } catch (IOException var11) {
         throw new ContentUtilException(var11.getMessage());
      } finally {
         if (var1 != null) {
            try {
               var1.close();
            } catch (IOException var10) {
               throw new ContentUtilException(var10);
            }
         }

      }

      return var2;
   }

   public static String readTextFromFile(String var0, String var1) throws ContentUtilException {
      FileInputStream var2 = null;

      String var3;
      try {
         var2 = new FileInputStream(var0);
         var3 = new String(readBytesFromStream(var2), var1);
      } catch (UnsupportedEncodingException var13) {
         throw new ContentUtilException(var13.getMessage());
      } catch (IOException var14) {
         throw new ContentUtilException(var14.getMessage());
      } finally {
         if (var2 != null) {
            try {
               var2.close();
            } catch (IOException var12) {
               throw new ContentUtilException(var12);
            }
         }

      }

      return var3;
   }

   public static String readTextFromStream(InputStream var0) throws ContentUtilException {
      return new String(readBytesFromStream(var0));
   }

   public static String readTextFromStream(InputStream var0, String var1) throws ContentUtilException {
      try {
         return new String(readBytesFromStream(var0), var1);
      } catch (UnsupportedEncodingException var3) {
         throw new ContentUtilException(var3.getMessage());
      }
   }

   public static void writeBytesToStream(byte[] var0, OutputStream var1) throws ContentUtilException {
      try {
         var1.write(var0);
         var1.flush();
      } catch (IOException var3) {
         throw new ContentUtilException();
      }
   }

   public static void writeBytesToFile(byte[] var0, String var1) throws ContentUtilException {
      FileOutputStream var2 = null;

      try {
         var2 = new NecroFileOutputStream(var1);
         writeBytesToStream(var0, var2);
      } catch (IOException var11) {
         throw new ContentUtilException(var11.getMessage());
      } finally {
         if (var2 != null) {
            try {
               var2.close();
            } catch (IOException var10) {
               throw new ContentUtilException(var10.getMessage());
            }
         }

      }

   }

   public static void writeTextToStream(String var0, OutputStream var1) throws ContentUtilException {
      try {
         var1.write(var0.getBytes());
         var1.flush();
      } catch (IOException var3) {
         throw new ContentUtilException();
      }
   }

   public static void writeTextToStream(String var0, OutputStream var1, String var2) throws ContentUtilException {
      try {
         var1.write(var0.getBytes(var2));
         var1.flush();
      } catch (UnsupportedEncodingException var4) {
         throw new ContentUtilException();
      } catch (IOException var5) {
         throw new ContentUtilException();
      }
   }

   public static void writeTextToFile(String var0, String var1) throws ContentUtilException {
      FileOutputStream var2 = null;

      try {
         var2 = new NecroFileOutputStream(var1);
         writeBytesToStream(var0.getBytes(), var2);
      } catch (IOException var11) {
         throw new ContentUtilException(var11.getMessage());
      } finally {
         if (var2 != null) {
            try {
               var2.close();
            } catch (IOException var10) {
               throw new ContentUtilException(var10.getMessage());
            }
         }

      }

   }

   public static void writeTextToFile(String var0, String var1, String var2) throws ContentUtilException {
      FileOutputStream var3 = null;

      try {
         var3 = new NecroFileOutputStream(var1);
         writeBytesToStream(var0.getBytes(var2), var3);
      } catch (UnsupportedEncodingException var13) {
         throw new ContentUtilException(var13.getMessage());
      } catch (IOException var14) {
         throw new ContentUtilException(var14.getMessage());
      } finally {
         if (var3 != null) {
            try {
               var3.close();
            } catch (IOException var12) {
               throw new ContentUtilException(var12.getMessage());
            }
         }

      }

   }
}
