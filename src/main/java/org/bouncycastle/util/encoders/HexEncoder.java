package org.bouncycastle.util.encoders;

import java.io.IOException;
import java.io.OutputStream;

public class HexEncoder implements Encoder {
   protected final byte[] encodingTable = new byte[]{48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 97, 98, 99, 100, 101, 102};
   protected final byte[] decodingTable = new byte[128];

   protected void initialiseDecodingTable() {
      for(int i = 0; i < this.encodingTable.length; ++i) {
         this.decodingTable[this.encodingTable[i]] = (byte)i;
      }

      this.decodingTable[65] = this.decodingTable[97];
      this.decodingTable[66] = this.decodingTable[98];
      this.decodingTable[67] = this.decodingTable[99];
      this.decodingTable[68] = this.decodingTable[100];
      this.decodingTable[69] = this.decodingTable[101];
      this.decodingTable[70] = this.decodingTable[102];
   }

   public HexEncoder() {
      this.initialiseDecodingTable();
   }

   public int encode(byte[] data, int off, int length, OutputStream out) throws IOException {
      for(int i = off; i < off + length; ++i) {
         int v = data[i] & 255;
         out.write(this.encodingTable[v >>> 4]);
         out.write(this.encodingTable[v & 15]);
      }

      return length * 2;
   }

   private boolean ignore(char c) {
      return c == '\n' || c == '\r' || c == '\t' || c == ' ';
   }

   public int decode(byte[] data, int off, int length, OutputStream out) throws IOException {
      int outLen = 0;

      int end;
      for(end = off + length; end > off && this.ignore((char)data[end - 1]); --end) {
      }

      for(int i = off; i < end; ++outLen) {
         while(i < end && this.ignore((char)data[i])) {
            ++i;
         }

         byte b1;
         for(b1 = this.decodingTable[data[i++]]; i < end && this.ignore((char)data[i]); ++i) {
         }

         byte b2 = this.decodingTable[data[i++]];
         out.write(b1 << 4 | b2);
      }

      return outLen;
   }

   public int decode(String data, OutputStream out) throws IOException {
      int length = 0;

      int end;
      for(end = data.length(); end > 0 && this.ignore(data.charAt(end - 1)); --end) {
      }

      for(int i = 0; i < end; ++length) {
         while(i < end && this.ignore(data.charAt(i))) {
            ++i;
         }

         byte b1;
         for(b1 = this.decodingTable[data.charAt(i++)]; i < end && this.ignore(data.charAt(i)); ++i) {
         }

         byte b2 = this.decodingTable[data.charAt(i++)];
         out.write(b1 << 4 | b2);
      }

      return length;
   }
}
