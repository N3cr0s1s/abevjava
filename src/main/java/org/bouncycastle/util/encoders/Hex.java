package org.bouncycastle.util.encoders;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class Hex {
   private static final Encoder encoder = new HexEncoder();

   public static byte[] encode(byte[] data) {
      return encode(data, 0, data.length);
   }

   public static byte[] encode(byte[] data, int off, int length) {
      ByteArrayOutputStream bOut = new ByteArrayOutputStream();

      try {
         encoder.encode(data, off, length, bOut);
      } catch (IOException var5) {
         throw new RuntimeException("exception encoding Hex string: " + var5);
      }

      return bOut.toByteArray();
   }

   public static int encode(byte[] data, OutputStream out) throws IOException {
      return encoder.encode(data, 0, data.length, out);
   }

   public static int encode(byte[] data, int off, int length, OutputStream out) throws IOException {
      return encoder.encode(data, 0, data.length, out);
   }

   public static byte[] decode(byte[] data) {
      ByteArrayOutputStream bOut = new ByteArrayOutputStream();

      try {
         encoder.decode(data, 0, data.length, bOut);
      } catch (IOException var3) {
         throw new RuntimeException("exception decoding Hex string: " + var3);
      }

      return bOut.toByteArray();
   }

   public static byte[] decode(String data) {
      ByteArrayOutputStream bOut = new ByteArrayOutputStream();

      try {
         encoder.decode(data, bOut);
      } catch (IOException var3) {
         throw new RuntimeException("exception decoding Hex string: " + var3);
      }

      return bOut.toByteArray();
   }

   public static int decode(String data, OutputStream out) throws IOException {
      return encoder.decode(data, out);
   }
}
