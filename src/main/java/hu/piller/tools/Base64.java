package hu.piller.tools;

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
import java.nio.file.Files;
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

   private static final byte[] getAlphabet(int options) {
      if ((options & 16) == 16) {
         return _URL_SAFE_ALPHABET;
      } else {
         return (options & 32) == 32 ? _ORDERED_ALPHABET : _STANDARD_ALPHABET;
      }
   }

   private static final byte[] getDecodabet(int options) {
      if ((options & 16) == 16) {
         return _URL_SAFE_DECODABET;
      } else {
         return (options & 32) == 32 ? _ORDERED_DECODABET : _STANDARD_DECODABET;
      }
   }

   private Base64() {
   }

   private static byte[] encode3to4(byte[] b4, byte[] threeBytes, int numSigBytes, int options) {
      encode3to4(threeBytes, 0, numSigBytes, b4, 0, options);
      return b4;
   }

   private static byte[] encode3to4(byte[] source, int srcOffset, int numSigBytes, byte[] destination, int destOffset, int options) {
      byte[] ALPHABET = getAlphabet(options);
      int inBuff = (numSigBytes > 0 ? source[srcOffset] << 24 >>> 8 : 0) | (numSigBytes > 1 ? source[srcOffset + 1] << 24 >>> 16 : 0) | (numSigBytes > 2 ? source[srcOffset + 2] << 24 >>> 24 : 0);
      switch(numSigBytes) {
      case 1:
         destination[destOffset] = ALPHABET[inBuff >>> 18];
         destination[destOffset + 1] = ALPHABET[inBuff >>> 12 & 63];
         destination[destOffset + 2] = 61;
         destination[destOffset + 3] = 61;
         return destination;
      case 2:
         destination[destOffset] = ALPHABET[inBuff >>> 18];
         destination[destOffset + 1] = ALPHABET[inBuff >>> 12 & 63];
         destination[destOffset + 2] = ALPHABET[inBuff >>> 6 & 63];
         destination[destOffset + 3] = 61;
         return destination;
      case 3:
         destination[destOffset] = ALPHABET[inBuff >>> 18];
         destination[destOffset + 1] = ALPHABET[inBuff >>> 12 & 63];
         destination[destOffset + 2] = ALPHABET[inBuff >>> 6 & 63];
         destination[destOffset + 3] = ALPHABET[inBuff & 63];
         return destination;
      default:
         return destination;
      }
   }

   public static void encode(ByteBuffer raw, ByteBuffer encoded) {
      byte[] raw3 = new byte[3];
      byte[] enc4 = new byte[4];

      while(raw.hasRemaining()) {
         int rem = Math.min(3, raw.remaining());
         raw.get(raw3, 0, rem);
         encode3to4(enc4, raw3, rem, 0);
         encoded.put(enc4);
      }

   }

   public static void encode(ByteBuffer raw, CharBuffer encoded) {
      byte[] raw3 = new byte[3];
      byte[] enc4 = new byte[4];

      while(raw.hasRemaining()) {
         int rem = Math.min(3, raw.remaining());
         raw.get(raw3, 0, rem);
         encode3to4(enc4, raw3, rem, 0);

         for(int i = 0; i < 4; ++i) {
            encoded.put((char)(enc4[i] & 255));
         }
      }

   }

   public static String encodeObject(Serializable serializableObject) throws IOException {
      return encodeObject(serializableObject, 0);
   }

   public static String encodeObject(Serializable serializableObject, int options) throws IOException {
      if (serializableObject == null) {
         throw new NullPointerException("Cannot serialize a null object.");
      } else {
         ByteArrayOutputStream baos = null;
         java.io.OutputStream b64os = null;
         ObjectOutputStream oos = null;

         try {
            baos = new ByteArrayOutputStream();
            b64os = new Base64.OutputStream(baos, 1 | options);
            oos = new ObjectOutputStream(b64os);
            oos.writeObject(serializableObject);
         } catch (IOException var21) {
            throw var21;
         } finally {
            try {
               oos.close();
            } catch (Exception var19) {
            }

            try {
               b64os.close();
            } catch (Exception var18) {
            }

            try {
               baos.close();
            } catch (Exception var17) {
            }

         }

         try {
            return new String(baos.toByteArray(), "US-ASCII");
         } catch (UnsupportedEncodingException var20) {
            return new String(baos.toByteArray());
         }
      }
   }

   public static String encodeBytes(byte[] source) {
      String encoded = null;

      try {
         encoded = encodeBytes(source, 0, source.length, 0);
      } catch (IOException var3) {
         assert false : var3.getMessage();
      }

      assert encoded != null;

      return encoded;
   }

   public static String encodeBytes(byte[] source, int options) throws IOException {
      return encodeBytes(source, 0, source.length, options);
   }

   public static String encodeBytes(byte[] source, int off, int len) {
      String encoded = null;

      try {
         encoded = encodeBytes(source, off, len, 0);
      } catch (IOException var5) {
         assert false : var5.getMessage();
      }

      assert encoded != null;

      return encoded;
   }

   public static String encodeBytes(byte[] source, int off, int len, int options) throws IOException {
      byte[] encoded = encodeBytesToBytes(source, off, len, options);

      try {
         return new String(encoded, "US-ASCII");
      } catch (UnsupportedEncodingException var6) {
         return new String(encoded);
      }
   }

   public static byte[] encodeBytesToBytes(byte[] source) {
      byte[] encoded = null;

      try {
         encoded = encodeBytesToBytes(source, 0, source.length, 0);
      } catch (IOException var3) {
         assert false : "IOExceptions only come from GZipping, which is turned off: " + var3.getMessage();
      }

      return encoded;
   }

   public static byte[] encodeBytesToBytes(byte[] source, int off, int len, int options) throws IOException {
      if (source == null) {
         throw new NullPointerException("Cannot serialize a null array.");
      } else if (off < 0) {
         throw new IllegalArgumentException("Cannot have negative offset: " + off);
      } else if (len < 0) {
         throw new IllegalArgumentException("Cannot have length offset: " + len);
      } else if (off + len > source.length) {
         throw new IllegalArgumentException(String.format("Cannot have offset of %d and length of %d with array of length %d", off, len, source.length));
      } else if ((options & 2) > 0) {
         ByteArrayOutputStream baos = null;
         GZIPOutputStream gzos = null;
         Base64.OutputStream b64os = null;

         try {
            baos = new ByteArrayOutputStream();
            b64os = new Base64.OutputStream(baos, 1 | options);
            gzos = new GZIPOutputStream(b64os);
            gzos.write(source, off, len);
            gzos.close();
         } catch (IOException var23) {
            throw var23;
         } finally {
            try {
               gzos.close();
            } catch (Exception var22) {
            }

            try {
               b64os.close();
            } catch (Exception var21) {
            }

            try {
               baos.close();
            } catch (Exception var20) {
            }

         }

         return baos.toByteArray();
      } else {
         boolean breakLines = (options & 8) > 0;
         int encLen = len / 3 * 4 + (len % 3 > 0 ? 4 : 0);
         if (breakLines) {
            encLen += encLen / 76;
         }

         byte[] outBuff = new byte[encLen];
         int d = 0;
         int e = 0;
         int len2 = len - 2;

         for(int lineLength = 0; d < len2; e += 4) {
            encode3to4(source, d + off, 3, outBuff, e, options);
            lineLength += 4;
            if (breakLines && lineLength >= 76) {
               outBuff[e + 4] = 10;
               ++e;
               lineLength = 0;
            }

            d += 3;
         }

         if (d < len) {
            encode3to4(source, d + off, len - d, outBuff, e, options);
            e += 4;
         }

         if (e < outBuff.length - 1) {
            byte[] finalOut = new byte[e];
            System.arraycopy(outBuff, 0, finalOut, 0, e);
            return finalOut;
         } else {
            return outBuff;
         }
      }
   }

   private static int decode4to3(byte[] source, int srcOffset, byte[] destination, int destOffset, int options) {
      if (source == null) {
         throw new NullPointerException("Source array was null.");
      } else if (destination == null) {
         throw new NullPointerException("Destination array was null.");
      } else if (srcOffset >= 0 && srcOffset + 3 < source.length) {
         if (destOffset >= 0 && destOffset + 2 < destination.length) {
            byte[] DECODABET = getDecodabet(options);
            int outBuff;
            if (source[srcOffset + 2] == 61) {
               outBuff = (DECODABET[source[srcOffset]] & 255) << 18 | (DECODABET[source[srcOffset + 1]] & 255) << 12;
               destination[destOffset] = (byte)(outBuff >>> 16);
               return 1;
            } else if (source[srcOffset + 3] == 61) {
               outBuff = (DECODABET[source[srcOffset]] & 255) << 18 | (DECODABET[source[srcOffset + 1]] & 255) << 12 | (DECODABET[source[srcOffset + 2]] & 255) << 6;
               destination[destOffset] = (byte)(outBuff >>> 16);
               destination[destOffset + 1] = (byte)(outBuff >>> 8);
               return 2;
            } else {
               outBuff = (DECODABET[source[srcOffset]] & 255) << 18 | (DECODABET[source[srcOffset + 1]] & 255) << 12 | (DECODABET[source[srcOffset + 2]] & 255) << 6 | DECODABET[source[srcOffset + 3]] & 255;
               destination[destOffset] = (byte)(outBuff >> 16);
               destination[destOffset + 1] = (byte)(outBuff >> 8);
               destination[destOffset + 2] = (byte)outBuff;
               return 3;
            }
         } else {
            throw new IllegalArgumentException(String.format("Destination array with length %d cannot have offset of %d and still store three bytes.", destination.length, destOffset));
         }
      } else {
         throw new IllegalArgumentException(String.format("Source array with length %d cannot have offset of %d and still process four bytes.", source.length, srcOffset));
      }
   }

   public static byte[] decode(byte[] source) {
      byte[] decoded = null;

      try {
         decoded = decode(source, 0, source.length, 0);
      } catch (IOException var3) {
         assert false : "IOExceptions only come from GZipping, which is turned off: " + var3.getMessage();
      }

      return decoded;
   }

   public static byte[] decode(byte[] source, int off, int len, int options) throws IOException {
      if (source == null) {
         throw new NullPointerException("Cannot decode null source array.");
      } else if (off >= 0 && off + len <= source.length) {
         if (len == 0) {
            return new byte[0];
         } else if (len < 4) {
            throw new IllegalArgumentException("Base64-encoded string must have at least four characters, but length specified was " + len);
         } else {
            byte[] DECODABET = getDecodabet(options);
            int len34 = len * 3 / 4;
            byte[] outBuff = new byte[len34];
            int outBuffPosn = 0;
            byte[] b4 = new byte[4];
            int b4Posn = 0;

            for(int i = off; i < off + len; ++i) {
               byte sbiCrop = (byte)(source[i] & 127);
               byte sbiDecode = DECODABET[sbiCrop];
               if (sbiDecode < -5) {
                  throw new IOException(String.format("Bad Base64 input character '%c' in array position %d", source[i], i));
               }

               if (sbiDecode >= -1) {
                  b4[b4Posn++] = sbiCrop;
                  if (b4Posn > 3) {
                     outBuffPosn += decode4to3(b4, 0, outBuff, outBuffPosn, options);
                     b4Posn = 0;
                     if (sbiCrop == 61) {
                        break;
                     }
                  }
               }
            }

            byte[] out = new byte[outBuffPosn];
            System.arraycopy(outBuff, 0, out, 0, outBuffPosn);
            return out;
         }
      } else {
         throw new IllegalArgumentException(String.format("Source array with length %d cannot have offset of %d and process %d bytes.", source.length, off, len));
      }
   }

   public static byte[] decode(String s) throws IOException {
      return decode(s, 0);
   }

   public static byte[] decode(String s, int options) throws IOException {
      if (s == null) {
         throw new NullPointerException("Input string was null.");
      } else {
         byte[] bytes;
         try {
            bytes = s.getBytes("US-ASCII");
         } catch (UnsupportedEncodingException var27) {
            bytes = s.getBytes();
         }

         bytes = decode(bytes, 0, bytes.length, options);
         if (bytes != null && bytes.length >= 4) {
            int head = bytes[0] & 255 | bytes[1] << 8 & '\uff00';
            if (35615 == head) {
               ByteArrayInputStream bais = null;
               GZIPInputStream gzis = null;
               ByteArrayOutputStream baos = null;
               byte[] buffer = new byte[2048];
               boolean var8 = false;

               try {
                  baos = new ByteArrayOutputStream();
                  bais = new ByteArrayInputStream(bytes);
                  gzis = new GZIPInputStream(bais);

                  int length;
                  while((length = gzis.read(buffer)) >= 0) {
                     baos.write(buffer, 0, length);
                  }

                  bytes = baos.toByteArray();
               } catch (IOException var28) {
               } finally {
                  try {
                     baos.close();
                  } catch (Exception var26) {
                  }

                  try {
                     gzis.close();
                  } catch (Exception var25) {
                  }

                  try {
                     bais.close();
                  } catch (Exception var24) {
                  }

               }
            }
         }

         return bytes;
      }
   }

   public static Object decodeToObject(String encodedObject) throws IOException, ClassNotFoundException {
      byte[] objBytes = decode(encodedObject);
      ByteArrayInputStream bais = null;
      ObjectInputStream ois = null;
      Object obj = null;

      try {
         bais = new ByteArrayInputStream(objBytes);
         ois = new ObjectInputStream(bais);
         obj = ois.readObject();
      } catch (IOException var17) {
         throw var17;
      } catch (ClassNotFoundException var18) {
         throw var18;
      } finally {
         try {
            bais.close();
         } catch (Exception var16) {
         }

         try {
            ois.close();
         } catch (Exception var15) {
         }

      }

      return obj;
   }

   public static void encodeToFile(byte[] dataToEncode, String filename) throws IOException {
      if (dataToEncode == null) {
         throw new NullPointerException("Data to encode was null.");
      } else {
         Base64.OutputStream bos = null;

         try {
            bos = new Base64.OutputStream(new NecroFileOutputStream(filename), 1);
            bos.write(dataToEncode);
         } catch (IOException var11) {
            throw var11;
         } finally {
            try {
               bos.close();
            } catch (Exception var10) {
            }

         }

      }
   }

   public static void decodeToFile(String dataToDecode, String filename) throws IOException {
      Base64.OutputStream bos = null;

      try {
         bos = new Base64.OutputStream(new NecroFileOutputStream(filename), 0);
         bos.write(dataToDecode.getBytes("US-ASCII"));
      } catch (IOException var11) {
         throw var11;
      } finally {
         try {
            bos.close();
         } catch (Exception var10) {
         }

      }

   }

   public static byte[] decodeFromFile(String filename) throws IOException {
      Base64.InputStream bis = null;

      try {
         File file = new NecroFile(filename);
         int length = 0;
         if (file.length() > 2147483647L) {
            throw new IOException("File is too big for this convenience method (" + file.length() + " bytes).");
         } else {
            byte[] buffer = new byte[(int)file.length()];

            int numBytes;
            for(bis = new Base64.InputStream(new BufferedInputStream(Files.newInputStream(file.toPath())), 0); (numBytes = bis.read(buffer, length, 4096)) >= 0; length += numBytes) {
            }

            byte[] decodedData = new byte[length];
            System.arraycopy(buffer, 0, decodedData, 0, length);
            return decodedData;
         }
      } catch (IOException var14) {
         throw var14;
      } finally {
         try {
            bis.close();
         } catch (Exception var13) {
         }

      }
   }

   public static String encodeFromFile(String filename) throws IOException {
      String encodedData = null;
      Base64.InputStream bis = null;

      try {
         File file = new NecroFile(filename);
         byte[] buffer = new byte[Math.max((int)((double)file.length() * 1.4D), 40)];
         int length = 0;
         int numBytes;
         for(bis = new Base64.InputStream(new BufferedInputStream(new FileInputStream(file)), 1); (numBytes = bis.read(buffer, length, 4096)) >= 0; length += numBytes) {
         }

         encodedData = new String(buffer, 0, length, "US-ASCII");
         return encodedData;
      } catch (IOException var14) {
         throw var14;
      } finally {
         try {
            bis.close();
         } catch (Exception var13) {
         }

      }
   }

   public static void encodeFileToFile(String infile, String outfile) throws IOException {
      String encoded = encodeFromFile(infile);
      BufferedOutputStream out = null;

      try {
         out = new BufferedOutputStream(new NecroFileOutputStream(outfile));
         out.write(encoded.getBytes("US-ASCII"));
      } catch (IOException var12) {
         throw var12;
      } finally {
         try {
            out.close();
         } catch (Exception var11) {
         }

      }

   }

   public static void decodeFileToFile(String infile, String outfile) throws IOException {
      byte[] decoded = decodeFromFile(infile);
      BufferedOutputStream out = null;

      try {
         out = new BufferedOutputStream(new NecroFileOutputStream(outfile));
         out.write(decoded);
      } catch (IOException var12) {
         throw var12;
      } finally {
         try {
            out.close();
         } catch (Exception var11) {
         }

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

      public InputStream(java.io.InputStream in) {
         this(in, 0);
      }

      public InputStream(java.io.InputStream in, int options) {
         super(in);
         this.options = options;
         this.breakLines = (options & 8) > 0;
         this.encode = (options & 1) > 0;
         this.bufferLength = this.encode ? 4 : 3;
         this.buffer = new byte[this.bufferLength];
         this.position = -1;
         this.lineLength = 0;
         this.alphabet = Base64.getAlphabet(options);
         this.decodabet = Base64.getDecodabet(options);
      }

      public int read() throws IOException {
         if (this.position < 0) {
            byte[] b4;
            int i;
            int b;
            if (this.encode) {
               b4 = new byte[3];
               i = 0;

               for(b = 0; b < 3; ++b) {
                  int ba = this.in.read();
                  if (ba < 0) {
                     break;
                  }

                  b4[b] = (byte)ba;
                  ++i;
               }

               if (i <= 0) {
                  return -1;
               }

               Base64.encode3to4(b4, 0, i, this.buffer, 0, this.options);
               this.position = 0;
               this.numSigBytes = 4;
            } else {
               b4 = new byte[4];

               for(i = 0; i < 4; ++i) {

                  do {
                     b = this.in.read();
                  } while(b >= 0 && this.decodabet[b & 127] <= -5);

                  if (b < 0) {
                     break;
                  }

                  b4[i] = (byte)b;
               }

               if (i != 4) {
                  if (i == 0) {
                     return -1;
                  }

                  throw new IOException("Improperly padded Base64 input.");
               }

               this.numSigBytes = Base64.decode4to3(b4, 0, this.buffer, 0, this.options);
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
               int b = this.buffer[this.position++];
               if (this.position >= this.bufferLength) {
                  this.position = -1;
               }

               return b & 255;
            }
         } else {
            throw new IOException("Error in Base64 code reading stream.");
         }
      }

      public int read(byte[] dest, int off, int len) throws IOException {
         int i;
         for(i = 0; i < len; ++i) {
            int b = this.read();
            if (b < 0) {
               if (i == 0) {
                  return -1;
               }
               break;
            }

            dest[off + i] = (byte)b;
         }

         return i;
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

      public OutputStream(java.io.OutputStream out) {
         this(out, 1);
      }

      public OutputStream(java.io.OutputStream out, int options) {
         super(out);
         this.breakLines = (options & 8) > 0;
         this.encode = (options & 1) > 0;
         this.bufferLength = this.encode ? 3 : 4;
         this.buffer = new byte[this.bufferLength];
         this.position = 0;
         this.lineLength = 0;
         this.suspendEncoding = false;
         this.b4 = new byte[4];
         this.options = options;
         this.alphabet = Base64.getAlphabet(options);
         this.decodabet = Base64.getDecodabet(options);
      }

      public void write(int theByte) throws IOException {
         if (this.suspendEncoding) {
            super.out.write(theByte);
         } else {
            if (this.encode) {
               this.buffer[this.position++] = (byte)theByte;
               if (this.position >= this.bufferLength) {
                  this.out.write(Base64.encode3to4(this.b4, this.buffer, this.bufferLength, this.options));
                  this.lineLength += 4;
                  if (this.breakLines && this.lineLength >= 76) {
                     this.out.write(10);
                     this.lineLength = 0;
                  }

                  this.position = 0;
               }
            } else if (this.decodabet[theByte & 127] > -5) {
               this.buffer[this.position++] = (byte)theByte;
               if (this.position >= this.bufferLength) {
                  int len = Base64.decode4to3(this.buffer, 0, this.b4, 0, this.options);
                  this.out.write(this.b4, 0, len);
                  this.position = 0;
               }
            } else if (this.decodabet[theByte & 127] != -5) {
               throw new IOException("Invalid character in Base64 data.");
            }

         }
      }

      public void write(byte[] theBytes, int off, int len) throws IOException {
         if (this.suspendEncoding) {
            super.out.write(theBytes, off, len);
         } else {
            for(int i = 0; i < len; ++i) {
               this.write(theBytes[off + i]);
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
}
