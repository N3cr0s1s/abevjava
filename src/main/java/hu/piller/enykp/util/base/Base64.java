package hu.piller.enykp.util.base;

import me.necrocore.abevjava.NecroFile;
import me.necrocore.abevjava.NecroFileOutputStream;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilterInputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class Base64 {
   public static final int NO_OPTIONS = 0;
   public static final int ENCODE = 1;
   public static final int DECODE = 0;
   public static final int GZIP = 2;
   public static final int DO_BREAK_LINES = 8;
   public static final int URL_SAFE = 16;
   public static final int ORDERED = 32;
   private static final int MAX_LINE_LENGTH = 76;
   private static final byte EQUALS_SIGN = 61;
   private static final byte NEW_LINE = 10;
   private static final String PREFERRED_ENCODING = "US-ASCII";
   private static final byte WHITE_SPACE_ENC = -5;
   private static final byte EQUALS_SIGN_ENC = -1;
   private static final byte[] _STANDARD_ALPHABET = new byte[]{65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 43, 47};
   private static final byte[] _STANDARD_DECODABET = new byte[]{-9, -9, -9, -9, -9, -9, -9, -9, -9, -5, -5, -9, -9, -5, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -5, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 62, -9, -9, -9, 63, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -9, -9, -9, -1, -9, -9, -9, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -9, -9, -9, -9, -9, -9, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -9, -9, -9, -9};
   private static final byte[] _URL_SAFE_ALPHABET = new byte[]{65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 45, 95};
   private static final byte[] _URL_SAFE_DECODABET = new byte[]{-9, -9, -9, -9, -9, -9, -9, -9, -9, -5, -5, -9, -9, -5, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -5, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 62, -9, -9, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -9, -9, -9, -1, -9, -9, -9, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -9, -9, -9, -9, 63, -9, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -9, -9, -9, -9};
   private static final byte[] _ORDERED_ALPHABET = new byte[]{45, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 95, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122};
   private static final byte[] _ORDERED_DECODABET = new byte[]{-9, -9, -9, -9, -9, -9, -9, -9, -9, -5, -5, -9, -9, -5, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -5, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 0, -9, -9, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, -9, -9, -9, -1, -9, -9, -9, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, -9, -9, -9, -9, 37, -9, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, -9, -9, -9, -9};

   private static final byte[] getAlphabet(int var0) {
      if ((var0 & 16) == 16) {
         return _URL_SAFE_ALPHABET;
      } else {
         return (var0 & 32) == 32 ? _ORDERED_ALPHABET : _STANDARD_ALPHABET;
      }
   }

   private static final byte[] getDecodabet(int var0) {
      if ((var0 & 16) == 16) {
         return _URL_SAFE_DECODABET;
      } else {
         return (var0 & 32) == 32 ? _ORDERED_DECODABET : _STANDARD_DECODABET;
      }
   }

   private Base64() {
   }

   private static byte[] encode3to4(byte[] var0, byte[] var1, int var2, int var3) {
      encode3to4(var1, 0, var2, var0, 0, var3);
      return var0;
   }

   private static byte[] encode3to4(byte[] var0, int var1, int var2, byte[] var3, int var4, int var5) {
      byte[] var6 = getAlphabet(var5);
      int var7 = (var2 > 0 ? var0[var1] << 24 >>> 8 : 0) | (var2 > 1 ? var0[var1 + 1] << 24 >>> 16 : 0) | (var2 > 2 ? var0[var1 + 2] << 24 >>> 24 : 0);
      switch(var2) {
      case 1:
         var3[var4] = var6[var7 >>> 18];
         var3[var4 + 1] = var6[var7 >>> 12 & 63];
         var3[var4 + 2] = 61;
         var3[var4 + 3] = 61;
         return var3;
      case 2:
         var3[var4] = var6[var7 >>> 18];
         var3[var4 + 1] = var6[var7 >>> 12 & 63];
         var3[var4 + 2] = var6[var7 >>> 6 & 63];
         var3[var4 + 3] = 61;
         return var3;
      case 3:
         var3[var4] = var6[var7 >>> 18];
         var3[var4 + 1] = var6[var7 >>> 12 & 63];
         var3[var4 + 2] = var6[var7 >>> 6 & 63];
         var3[var4 + 3] = var6[var7 & 63];
         return var3;
      default:
         return var3;
      }
   }

   public static void encode(ByteBuffer var0, ByteBuffer var1) {
      byte[] var2 = new byte[3];
      byte[] var3 = new byte[4];

      while(var0.hasRemaining()) {
         int var4 = Math.min(3, var0.remaining());
         var0.get(var2, 0, var4);
         encode3to4(var3, var2, var4, 0);
         var1.put(var3);
      }

   }

   public static void encode(ByteBuffer var0, CharBuffer var1) {
      byte[] var2 = new byte[3];
      byte[] var3 = new byte[4];

      while(var0.hasRemaining()) {
         int var4 = Math.min(3, var0.remaining());
         var0.get(var2, 0, var4);
         encode3to4(var3, var2, var4, 0);

         for(int var5 = 0; var5 < 4; ++var5) {
            var1.put((char)(var3[var5] & 255));
         }
      }

   }

   public static String encodeObject(Serializable var0) throws IOException {
      return encodeObject(var0, 0);
   }

   public static String encodeObject(Serializable var0, int var1) throws IOException {
      if (var0 == null) {
         throw new NullPointerException("Cannot serialize a null object.");
      } else {
         ByteArrayOutputStream var2 = null;
         Base64.OutputStream var3 = null;
         ObjectOutputStream var4 = null;

         try {
            var2 = new ByteArrayOutputStream();
            var3 = new Base64.OutputStream(var2, 1 | var1);
            var4 = new ObjectOutputStream(var3);
            var4.writeObject(var0);
         } catch (IOException var21) {
            throw var21;
         } finally {
            try {
               var4.close();
            } catch (Exception var19) {
               Tools.eLog(var19, 0);
            }

            try {
               var3.close();
            } catch (Exception var18) {
               Tools.eLog(var18, 0);
            }

            try {
               var2.close();
            } catch (Exception var17) {
               Tools.eLog(var17, 0);
            }

         }

         try {
            return new String(var2.toByteArray(), "US-ASCII");
         } catch (UnsupportedEncodingException var20) {
            return new String(var2.toByteArray());
         }
      }
   }

   public static String encodeBytes(byte[] var0) {
      String var1 = null;

      try {
         var1 = encodeBytes(var0, 0, var0.length, 0);
      } catch (IOException var3) {
         assert false : var3.getMessage();
      }

      assert var1 != null;

      return var1;
   }

   public static String encodeBytes(byte[] var0, int var1) throws IOException {
      return encodeBytes(var0, 0, var0.length, var1);
   }

   public static String encodeBytes(byte[] var0, int var1, int var2) {
      String var3 = null;

      try {
         var3 = encodeBytes(var0, var1, var2, 0);
      } catch (IOException var5) {
         assert false : var5.getMessage();
      }

      assert var3 != null;

      return var3;
   }

   public static String encodeBytes(byte[] var0, int var1, int var2, int var3) throws IOException {
      byte[] var4 = encodeBytesToBytes(var0, var1, var2, var3);

      try {
         return new String(var4, "US-ASCII");
      } catch (UnsupportedEncodingException var6) {
         return new String(var4);
      }
   }

   public static byte[] encodeBytesToBytes(byte[] var0) {
      byte[] var1 = null;

      try {
         var1 = encodeBytesToBytes(var0, 0, var0.length, 0);
      } catch (IOException var3) {
         assert false : "IOExceptions only come from GZipping, which is turned off: " + var3.getMessage();
      }

      return var1;
   }

   public static byte[] encodeBytesToBytes(byte[] var0, int var1, int var2, int var3) throws IOException {
      if (var0 == null) {
         throw new NullPointerException("Cannot serialize a null array.");
      } else if (var1 < 0) {
         throw new IllegalArgumentException("Cannot have negative offset: " + var1);
      } else if (var2 < 0) {
         throw new IllegalArgumentException("Cannot have length offset: " + var2);
      } else if (var1 + var2 > var0.length) {
         throw new IllegalArgumentException(String.format("Cannot have offset of %d and length of %d with array of length %d", var1, var2, var0.length));
      } else if ((var3 & 2) > 0) {
         ByteArrayOutputStream var25 = null;
         GZIPOutputStream var26 = null;
         Base64.OutputStream var27 = null;

         try {
            var25 = new ByteArrayOutputStream();
            var27 = new Base64.OutputStream(var25, 1 | var3);
            var26 = new GZIPOutputStream(var27);
            var26.write(var0, var1, var2);
            var26.close();
         } catch (IOException var23) {
            throw var23;
         } finally {
            try {
               var26.close();
            } catch (Exception var22) {
               Tools.eLog(var22, 0);
            }

            try {
               var27.close();
            } catch (Exception var21) {
               Tools.eLog(var21, 0);
            }

            try {
               var25.close();
            } catch (Exception var20) {
               Tools.eLog(var20, 0);
            }

         }

         return var25.toByteArray();
      } else {
         boolean var4 = (var3 & 8) > 0;
         int var5 = var2 / 3 * 4 + (var2 % 3 > 0 ? 4 : 0);
         if (var4) {
            var5 += var5 / 76;
         }

         byte[] var6 = new byte[var5];
         int var7 = 0;
         int var8 = 0;
         int var9 = var2 - 2;

         for(int var10 = 0; var7 < var9; var8 += 4) {
            encode3to4(var0, var7 + var1, 3, var6, var8, var3);
            var10 += 4;
            if (var4 && var10 >= 76) {
               var6[var8 + 4] = 10;
               ++var8;
               var10 = 0;
            }

            var7 += 3;
         }

         if (var7 < var2) {
            encode3to4(var0, var7 + var1, var2 - var7, var6, var8, var3);
            var8 += 4;
         }

         if (var8 < var6.length - 1) {
            byte[] var11 = new byte[var8];
            System.arraycopy(var6, 0, var11, 0, var8);
            return var11;
         } else {
            return var6;
         }
      }
   }

   private static int decode4to3(byte[] var0, int var1, byte[] var2, int var3, int var4) {
      if (var0 == null) {
         throw new NullPointerException("Source array was null.");
      } else if (var2 == null) {
         throw new NullPointerException("Destination array was null.");
      } else if (var1 >= 0 && var1 + 3 < var0.length) {
         if (var3 >= 0 && var3 + 2 < var2.length) {
            byte[] var5 = getDecodabet(var4);
            int var6;
            if (var0[var1 + 2] == 61) {
               var6 = (var5[var0[var1]] & 255) << 18 | (var5[var0[var1 + 1]] & 255) << 12;
               var2[var3] = (byte)(var6 >>> 16);
               return 1;
            } else if (var0[var1 + 3] == 61) {
               var6 = (var5[var0[var1]] & 255) << 18 | (var5[var0[var1 + 1]] & 255) << 12 | (var5[var0[var1 + 2]] & 255) << 6;
               var2[var3] = (byte)(var6 >>> 16);
               var2[var3 + 1] = (byte)(var6 >>> 8);
               return 2;
            } else {
               var6 = (var5[var0[var1]] & 255) << 18 | (var5[var0[var1 + 1]] & 255) << 12 | (var5[var0[var1 + 2]] & 255) << 6 | var5[var0[var1 + 3]] & 255;
               var2[var3] = (byte)(var6 >> 16);
               var2[var3 + 1] = (byte)(var6 >> 8);
               var2[var3 + 2] = (byte)var6;
               return 3;
            }
         } else {
            throw new IllegalArgumentException(String.format("Destination array with length %d cannot have offset of %d and still store three bytes.", var2.length, var3));
         }
      } else {
         throw new IllegalArgumentException(String.format("Source array with length %d cannot have offset of %d and still process four bytes.", var0.length, var1));
      }
   }

   public static byte[] decode(byte[] var0) {
      byte[] var1 = null;

      try {
         var1 = decode(var0, 0, var0.length, 0);
      } catch (IOException var3) {
         assert false : "IOExceptions only come from GZipping, which is turned off: " + var3.getMessage();
      }

      return var1;
   }

   public static byte[] decode(byte[] var0, int var1, int var2, int var3) throws IOException {
      if (var0 == null) {
         throw new NullPointerException("Cannot decode null source array.");
      } else if (var1 >= 0 && var1 + var2 <= var0.length) {
         if (var2 == 0) {
            return new byte[0];
         } else if (var2 < 4) {
            throw new IllegalArgumentException("Base64-encoded string must have at least four characters, but length specified was " + var2);
         } else {
            byte[] var4 = getDecodabet(var3);
            int var5 = var2 * 3 / 4;
            byte[] var6 = new byte[var5];
            int var7 = 0;
            byte[] var8 = new byte[4];
            int var9 = 0;
            boolean var10 = false;
            boolean var11 = false;
            boolean var12 = false;

            for(int var14 = var1; var14 < var1 + var2; ++var14) {
               byte var15 = (byte)(var0[var14] & 127);
               byte var16 = var4[var15];
               if (var16 < -5) {
                  throw new IOException(String.format("Bad Base64 input character '%c' in array position %d", var0[var14], var14));
               }

               if (var16 >= -1) {
                  var8[var9++] = var15;
                  if (var9 > 3) {
                     var7 += decode4to3(var8, 0, var6, var7, var3);
                     var9 = 0;
                     if (var15 == 61) {
                        break;
                     }
                  }
               }
            }

            byte[] var13 = new byte[var7];
            System.arraycopy(var6, 0, var13, 0, var7);
            return var13;
         }
      } else {
         throw new IllegalArgumentException(String.format("Source array with length %d cannot have offset of %d and process %d bytes.", var0.length, var1, var2));
      }
   }

   public static byte[] decode(String var0) throws IOException {
      return decode(var0, 0);
   }

   public static byte[] decode(String var0, int var1) throws IOException {
      if (var0 == null) {
         throw new NullPointerException("Input string was null.");
      } else {
         byte[] var2;
         try {
            var2 = var0.getBytes("US-ASCII");
         } catch (UnsupportedEncodingException var27) {
            var2 = var0.getBytes();
         }

         var2 = decode(var2, 0, var2.length, var1);
         if (var2 != null && var2.length >= 4) {
            int var3 = var2[0] & 255 | var2[1] << 8 & '\uff00';
            if (35615 == var3) {
               ByteArrayInputStream var4 = null;
               GZIPInputStream var5 = null;
               ByteArrayOutputStream var6 = null;
               byte[] var7 = new byte[2048];
               boolean var8 = false;

               try {
                  var6 = new ByteArrayOutputStream();
                  var4 = new ByteArrayInputStream(var2);
                  var5 = new GZIPInputStream(var4);

                  int var30;
                  while((var30 = var5.read(var7)) >= 0) {
                     var6.write(var7, 0, var30);
                  }

                  var2 = var6.toByteArray();
               } catch (IOException var28) {
                  Tools.eLog(var28, 0);
               } finally {
                  try {
                     var6.close();
                  } catch (Exception var26) {
                     Tools.eLog(var26, 0);
                  }

                  try {
                     var5.close();
                  } catch (Exception var25) {
                     Tools.eLog(var25, 0);
                  }

                  try {
                     var4.close();
                  } catch (Exception var24) {
                     Tools.eLog(var24, 0);
                  }

               }
            }
         }

         return var2;
      }
   }

   public static Object decodeToObject(String var0) throws IOException, ClassNotFoundException {
      byte[] var1 = decode(var0);
      ByteArrayInputStream var2 = null;
      ObjectInputStream var3 = null;
      Object var4 = null;

      try {
         var2 = new ByteArrayInputStream(var1);
         var3 = new ObjectInputStream(var2);
         var4 = var3.readObject();
      } catch (IOException var17) {
         throw var17;
      } catch (ClassNotFoundException var18) {
         throw var18;
      } finally {
         try {
            var2.close();
         } catch (Exception var16) {
            Tools.eLog(var16, 0);
         }

         try {
            var3.close();
         } catch (Exception var15) {
            Tools.eLog(var15, 0);
         }

      }

      return var4;
   }

   public static void encodeToFile(byte[] var0, String var1) throws IOException {
      if (var0 == null) {
         throw new NullPointerException("Data to encode was null.");
      } else {
         Base64.OutputStream var2 = null;

         try {
            var2 = new Base64.OutputStream(new NecroFileOutputStream(var1), 1);
            var2.write(var0);
         } catch (IOException var11) {
            throw var11;
         } finally {
            try {
               var2.close();
            } catch (Exception var10) {
               Tools.eLog(var10, 0);
            }

         }

      }
   }

   public static void decodeToFile(String var0, String var1) throws IOException {
      Base64.OutputStream var2 = null;

      try {
         var2 = new Base64.OutputStream(new NecroFileOutputStream(var1), 0);
         var2.write(var0.getBytes("US-ASCII"));
      } catch (IOException var11) {
         throw var11;
      } finally {
         try {
            var2.close();
         } catch (Exception var10) {
            Tools.eLog(var10, 0);
         }

      }

   }

   public static byte[] decodeFromFile(String var0) throws IOException {
      Object var1 = null;
      Base64.InputStream var2 = null;

      try {
         File var3 = new NecroFile(var0);
         Object var4 = null;
         int var5 = 0;
         boolean var6 = false;
         if (var3.length() > 2147483647L) {
            throw new IOException("File is too big for this convenience method (" + var3.length() + " bytes).");
         } else {
            byte[] var17 = new byte[(int)var3.length()];

            int var18;
            for(var2 = new Base64.InputStream(new BufferedInputStream(new FileInputStream(var3)), 0); (var18 = var2.read(var17, var5, 4096)) >= 0; var5 += var18) {
            }

            byte[] var16 = new byte[var5];
            System.arraycopy(var17, 0, var16, 0, var5);
            return var16;
         }
      } catch (IOException var14) {
         throw var14;
      } finally {
         try {
            var2.close();
         } catch (Exception var13) {
            Tools.eLog(var13, 0);
         }

      }
   }

   public static String encodeFromFile(String var0) throws IOException {
      String var1 = null;
      Base64.InputStream var2 = null;

      try {
         File var3 = new NecroFile(var0);
         byte[] var4 = new byte[Math.max((int)((double)var3.length() * 1.4D), 40)];
         int var5 = 0;
         boolean var6 = false;

         int var16;
         for(var2 = new Base64.InputStream(new BufferedInputStream(new FileInputStream(var3)), 1); (var16 = var2.read(var4, var5, 4096)) >= 0; var5 += var16) {
         }

         var1 = new String(var4, 0, var5, "US-ASCII");
         return var1;
      } catch (IOException var14) {
         throw var14;
      } finally {
         try {
            var2.close();
         } catch (Exception var13) {
            Tools.eLog(var13, 0);
         }

      }
   }

   public static void encodeFileToFile(String var0, String var1) throws IOException {
      String var2 = encodeFromFile(var0);
      BufferedOutputStream var3 = null;

      try {
         var3 = new BufferedOutputStream(new NecroFileOutputStream(var1));
         var3.write(var2.getBytes("US-ASCII"));
      } catch (IOException var12) {
         throw var12;
      } finally {
         try {
            var3.close();
         } catch (Exception var11) {
            Tools.eLog(var11, 0);
         }

      }

   }

   public static void decodeFileToFile(String var0, String var1) throws IOException {
      byte[] var2 = decodeFromFile(var0);
      BufferedOutputStream var3 = null;

      try {
         var3 = new BufferedOutputStream(new NecroFileOutputStream(var1));
         var3.write(var2);
      } catch (IOException var12) {
         throw var12;
      } finally {
         try {
            var3.close();
         } catch (Exception var11) {
            Tools.eLog(var11, 0);
         }

      }

   }

   public static class OutputStream extends FilterOutputStream {
      private boolean encode;
      private int position;
      private byte[] buffer;
      private int bufferLength;
      private int lineLength;
      private boolean breakLines;
      private byte[] b4;
      private boolean suspendEncoding;
      private int options;
      private byte[] alphabet;
      private byte[] decodabet;

      public OutputStream(java.io.OutputStream var1) {
         this(var1, 1);
      }

      public OutputStream(java.io.OutputStream var1, int var2) {
         super(var1);
         this.breakLines = (var2 & 8) > 0;
         this.encode = (var2 & 1) > 0;
         this.bufferLength = this.encode ? 3 : 4;
         this.buffer = new byte[this.bufferLength];
         this.position = 0;
         this.lineLength = 0;
         this.suspendEncoding = false;
         this.b4 = new byte[4];
         this.options = var2;
         this.alphabet = Base64.getAlphabet(var2);
         this.decodabet = Base64.getDecodabet(var2);
      }

      public void write(int var1) throws IOException {
         if (this.suspendEncoding) {
            super.out.write(var1);
         } else {
            if (this.encode) {
               this.buffer[this.position++] = (byte)var1;
               if (this.position >= this.bufferLength) {
                  this.out.write(Base64.encode3to4(this.b4, this.buffer, this.bufferLength, this.options));
                  this.lineLength += 4;
                  if (this.breakLines && this.lineLength >= 76) {
                     this.out.write(10);
                     this.lineLength = 0;
                  }

                  this.position = 0;
               }
            } else if (this.decodabet[var1 & 127] > -5) {
               this.buffer[this.position++] = (byte)var1;
               if (this.position >= this.bufferLength) {
                  int var2 = Base64.decode4to3(this.buffer, 0, this.b4, 0, this.options);
                  this.out.write(this.b4, 0, var2);
                  this.position = 0;
               }
            } else if (this.decodabet[var1 & 127] != -5) {
               throw new IOException("Invalid character in Base64 data.");
            }

         }
      }

      public void write(byte[] var1, int var2, int var3) throws IOException {
         if (this.suspendEncoding) {
            super.out.write(var1, var2, var3);
         } else {
            for(int var4 = 0; var4 < var3; ++var4) {
               this.write(var1[var2 + var4]);
            }

         }
      }

      public void flushBase64() throws IOException {
         if (this.position > 0) {
            if (!this.encode) {
               throw new IOException("Base64 input not properly padded.");
            }

            this.out.write(Base64.encode3to4(this.b4, this.buffer, this.position, this.options));
            this.position = 0;
         }

      }

      public void flush() throws IOException {
         this.flushBase64();
         super.flush();
      }

      public void close() throws IOException {
         this.flush();
         super.close();
         this.buffer = null;
         this.out = null;
      }

      public void suspendEncoding() throws IOException {
         this.flushBase64();
         this.suspendEncoding = true;
      }

      public void resumeEncoding() {
         this.suspendEncoding = false;
      }
   }

   public static class InputStream extends FilterInputStream {
      private boolean encode;
      private int position;
      private byte[] buffer;
      private int bufferLength;
      private int numSigBytes;
      private int lineLength;
      private boolean breakLines;
      private int options;
      private byte[] alphabet;
      private byte[] decodabet;

      public InputStream(java.io.InputStream var1) {
         this(var1, 0);
      }

      public InputStream(java.io.InputStream var1, int var2) {
         super(var1);
         this.options = var2;
         this.breakLines = (var2 & 8) > 0;
         this.encode = (var2 & 1) > 0;
         this.bufferLength = this.encode ? 4 : 3;
         this.buffer = new byte[this.bufferLength];
         this.position = -1;
         this.lineLength = 0;
         this.alphabet = Base64.getAlphabet(var2);
         this.decodabet = Base64.getDecodabet(var2);
      }

      public int read() throws IOException {
         if (this.position < 0) {
            byte[] var1;
            int var2;
            int var3;
            if (this.encode) {
               var1 = new byte[3];
               var2 = 0;

               for(var3 = 0; var3 < 3; ++var3) {
                  int var4 = this.in.read();
                  if (var4 < 0) {
                     break;
                  }

                  var1[var3] = (byte)var4;
                  ++var2;
               }

               if (var2 <= 0) {
                  return -1;
               }

               Base64.encode3to4(var1, 0, var2, this.buffer, 0, this.options);
               this.position = 0;
               this.numSigBytes = 4;
            } else {
               var1 = new byte[4];
               boolean var6 = false;

               for(var2 = 0; var2 < 4; ++var2) {
                  boolean var7 = false;

                  do {
                     var3 = this.in.read();
                  } while(var3 >= 0 && this.decodabet[var3 & 127] <= -5);

                  if (var3 < 0) {
                     break;
                  }

                  var1[var2] = (byte)var3;
               }

               if (var2 != 4) {
                  if (var2 == 0) {
                     return -1;
                  }

                  throw new IOException("Improperly padded Base64 input.");
               }

               this.numSigBytes = Base64.decode4to3(var1, 0, this.buffer, 0, this.options);
               this.position = 0;
            }
         }

         if (this.position >= 0) {
            if (this.position >= this.numSigBytes) {
               return -1;
            } else if (this.encode && this.breakLines && this.lineLength >= 76) {
               this.lineLength = 0;
               return 10;
            } else {
               ++this.lineLength;
               byte var5 = this.buffer[this.position++];
               if (this.position >= this.bufferLength) {
                  this.position = -1;
               }

               return var5 & 255;
            }
         } else {
            throw new IOException("Error in Base64 code reading stream.");
         }
      }

      public int read(byte[] var1, int var2, int var3) throws IOException {
         int var4;
         for(var4 = 0; var4 < var3; ++var4) {
            int var5 = this.read();
            if (var5 < 0) {
               if (var4 == 0) {
                  return -1;
               }
               break;
            }

            var1[var2 + var4] = (byte)var5;
         }

         return var4;
      }
   }
}
