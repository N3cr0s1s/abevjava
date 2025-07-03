package org.bouncycastle.bcpg;

import java.io.IOException;
import java.io.OutputStream;

public class BCPGOutputStream extends OutputStream implements PacketTags, CompressionAlgorithmTags {
   OutputStream out;
   private byte[] partialBuffer;
   private int partialBufferLength;
   private int partialPower;
   private int partialOffset;
   private static final int BUF_SIZE_POWER = 16;

   public BCPGOutputStream(OutputStream out) {
      this.out = out;
   }

   public BCPGOutputStream(OutputStream out, int tag) throws IOException {
      this.out = out;
      this.writeHeader(tag, true, true, 0L);
   }

   public BCPGOutputStream(OutputStream out, int tag, long length, boolean oldFormat) throws IOException {
      this.out = out;
      if (length > 4294967295L) {
         this.writeHeader(tag, false, true, 0L);
         this.partialBufferLength = 65536;
         this.partialBuffer = new byte[this.partialBufferLength];
         this.partialOffset = 0;
      } else {
         this.writeHeader(tag, oldFormat, false, length);
      }

   }

   public BCPGOutputStream(OutputStream out, int tag, long length) throws IOException {
      this.out = out;
      this.writeHeader(tag, false, false, length);
   }

   public BCPGOutputStream(OutputStream out, int tag, byte[] buffer) throws IOException {
      this.out = out;
      this.writeHeader(tag, false, true, 0L);
      this.partialBuffer = buffer;
      int length = this.partialBuffer.length;

      for(this.partialPower = 0; length != 1; ++this.partialPower) {
         length >>>= 1;
      }

      if (this.partialPower > 30) {
         throw new IOException("Buffer cannot be greater than 2^30 in length.");
      } else {
         this.partialBufferLength = 1 << this.partialPower;
         this.partialOffset = 0;
      }
   }

   private void writeNewPacketLength(long bodyLen) throws IOException {
      if (bodyLen < 192L) {
         this.out.write((byte)((int)bodyLen));
      } else if (bodyLen <= 8383L) {
         bodyLen -= 192L;
         this.out.write((byte)((int)((bodyLen >> 8 & 255L) + 192L)));
         this.out.write((byte)((int)bodyLen));
      } else {
         this.out.write(255);
         this.out.write((byte)((int)(bodyLen >> 24)));
         this.out.write((byte)((int)(bodyLen >> 16)));
         this.out.write((byte)((int)(bodyLen >> 8)));
         this.out.write((byte)((int)bodyLen));
      }

   }

   private void writeHeader(int tag, boolean oldPackets, boolean partial, long bodyLen) throws IOException {
      if (this.partialBuffer != null) {
         this.partialFlush(true);
         this.partialBuffer = null;
      }

      int hdr = 128;
      if (oldPackets) {
         hdr = hdr | tag << 2;
         if (partial) {
            this.write(hdr | 3);
         } else if (bodyLen <= 255L) {
            this.write(hdr);
            this.write((byte)((int)bodyLen));
         } else if (bodyLen <= 65535L) {
            this.write(hdr | 1);
            this.write((byte)((int)(bodyLen >> 8)));
            this.write((byte)((int)bodyLen));
         } else {
            this.write(hdr | 2);
            this.write((byte)((int)(bodyLen >> 24)));
            this.write((byte)((int)(bodyLen >> 16)));
            this.write((byte)((int)(bodyLen >> 8)));
            this.write((byte)((int)bodyLen));
         }
      } else {
         hdr = hdr | 64 | tag;
         this.write(hdr);
         if (partial) {
            this.partialOffset = 0;
         } else {
            this.writeNewPacketLength(bodyLen);
         }
      }

   }

   private void partialFlush(boolean isLast) throws IOException {
      if (isLast) {
         this.writeNewPacketLength((long)this.partialOffset);
         this.out.write(this.partialBuffer, 0, this.partialOffset);
      } else {
         this.out.write(224 | this.partialPower);
         this.out.write(this.partialBuffer, 0, this.partialBufferLength);
      }

      this.partialOffset = 0;
   }

   private void writePartial(byte b) throws IOException {
      if (this.partialOffset == this.partialBufferLength) {
         this.partialFlush(false);
      }

      this.partialBuffer[this.partialOffset++] = b;
   }

   private void writePartial(byte[] buf, int off, int len) throws IOException {
      if (this.partialOffset == this.partialBufferLength) {
         this.partialFlush(false);
      }

      if (len <= this.partialBufferLength - this.partialOffset) {
         System.arraycopy(buf, off, this.partialBuffer, this.partialOffset, len);
         this.partialOffset += len;
      } else {
         System.arraycopy(buf, off, this.partialBuffer, this.partialOffset, this.partialBufferLength - this.partialOffset);
         off += this.partialBufferLength - this.partialOffset;
         len -= this.partialBufferLength - this.partialOffset;
         this.partialFlush(false);

         while(len > this.partialBufferLength) {
            System.arraycopy(buf, off, this.partialBuffer, 0, this.partialBufferLength);
            off += this.partialBufferLength;
            len -= this.partialBufferLength;
            this.partialFlush(false);
         }

         System.arraycopy(buf, off, this.partialBuffer, 0, len);
         this.partialOffset += len;
      }

   }

   public void write(int b) throws IOException {
      if (this.partialBuffer != null) {
         this.writePartial((byte)b);
      } else {
         this.out.write(b);
      }

   }

   public void write(byte[] bytes, int off, int len) throws IOException {
      if (this.partialBuffer != null) {
         this.writePartial(bytes, off, len);
      } else {
         this.out.write(bytes, off, len);
      }

   }

   public void writePacket(ContainedPacket p) throws IOException {
      p.encode(this);
   }

   void writePacket(int tag, byte[] body, boolean oldFormat) throws IOException {
      this.writeHeader(tag, oldFormat, false, (long)body.length);
      this.write(body);
   }

   public void writeObject(BCPGObject o) throws IOException {
      o.encode(this);
   }

   public void flush() throws IOException {
      this.out.flush();
   }

   public void finish() throws IOException {
      if (this.partialBuffer != null) {
         this.partialFlush(true);
         this.partialBuffer = null;
      }

   }

   public void close() throws IOException {
      this.finish();
      this.out.flush();
      this.out.close();
   }
}
