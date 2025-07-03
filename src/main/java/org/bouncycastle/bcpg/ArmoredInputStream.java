package org.bouncycastle.bcpg;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

public class ArmoredInputStream extends InputStream {
   private static final byte[] decodingTable = new byte[128];
   InputStream in;
   boolean start = true;
   int[] outBuf = new int[3];
   int bufPtr = 3;
   CRC24 crc = new CRC24();
   boolean crcFound = false;
   boolean hasHeaders = true;
   String header = null;
   boolean newLineFound = false;
   boolean clearText = false;
   boolean restart = false;
   Vector headerList = new Vector();
   int lastC = 0;

   static {
      int i;
      for(i = 65; i <= 90; ++i) {
         decodingTable[i] = (byte)(i - 65);
      }

      for(i = 97; i <= 122; ++i) {
         decodingTable[i] = (byte)(i - 97 + 26);
      }

      for(i = 48; i <= 57; ++i) {
         decodingTable[i] = (byte)(i - 48 + 52);
      }

      decodingTable[43] = 62;
      decodingTable[47] = 63;
   }

   private int decode(int in0, int in1, int in2, int in3, int[] out) throws EOFException {
      if (in3 < 0) {
         throw new EOFException("unexpected end of file in armored stream.");
      } else if (in2 == 61) {
         int b1 = decodingTable[in0] & 255;
         int b2 = decodingTable[in1] & 255;
         out[2] = (b1 << 2 | b2 >> 4) & 255;
         return 2;
      } else {
         byte b1;
         byte b2;
         byte b3;
         if (in3 == 61) {
            b1 = decodingTable[in0];
            b2 = decodingTable[in1];
            b3 = decodingTable[in2];
            out[1] = (b1 << 2 | b2 >> 4) & 255;
            out[2] = (b2 << 4 | b3 >> 2) & 255;
            return 1;
         } else {
            b1 = decodingTable[in0];
            b2 = decodingTable[in1];
            b3 = decodingTable[in2];
            int b4 = decodingTable[in3];
            out[0] = (b1 << 2 | b2 >> 4) & 255;
            out[1] = (b2 << 4 | b3 >> 2) & 255;
            out[2] = (b3 << 6 | b4) & 255;
            return 0;
         }
      }
   }

   public ArmoredInputStream(InputStream in) throws IOException {
      this.in = in;
      if (this.hasHeaders) {
         this.parseHeaders();
      }

      this.start = false;
   }

   public ArmoredInputStream(InputStream in, boolean hasHeaders) throws IOException {
      this.in = in;
      this.hasHeaders = hasHeaders;
      if (hasHeaders) {
         this.parseHeaders();
      }

      this.start = false;
   }

   public int available() throws IOException {
      return this.in.available();
   }

   private boolean parseHeaders() throws IOException {
      this.header = null;
      int last = 0;
      boolean headerFound = false;
      this.headerList = new Vector();
      int c;
      if (this.restart) {
         headerFound = true;
      } else {
         while((c = this.in.read()) >= 0) {
            if (c == 45 && (last == 0 || last == 10 || last == 13)) {
               headerFound = true;
               break;
            }

            last = c;
         }
      }

      if (headerFound) {
         StringBuffer buf = new StringBuffer("-");
         boolean eolReached = false;
         boolean crLf = false;
         if (this.restart) {
            buf.append('-');
         }

         for(; (c = this.in.read()) >= 0; last = c) {
            if (last == 13 && c == 10) {
               crLf = true;
            }

            if (eolReached && last != 13 && c == 10 || eolReached && c == 13) {
               break;
            }

            if (c == 13 || last != 13 && c == 10) {
               this.headerList.addElement(buf.toString());
               buf.setLength(0);
            }

            if (c != 10 && c != 13) {
               buf.append((char)c);
               eolReached = false;
            } else if (c == 13 || last != 13 && c == 10) {
               eolReached = true;
            }
         }

         if (crLf) {
            this.in.read();
         }
      }

      if (this.headerList.size() > 0) {
         this.header = (String)this.headerList.elementAt(0);
      }

      this.clearText = "-----BEGIN PGP SIGNED MESSAGE-----".equals(this.header);
      this.newLineFound = true;
      return headerFound;
   }

   public boolean isClearText() {
      return this.clearText;
   }

   public String getArmorHeaderLine() {
      return this.header;
   }

   public String[] getArmorHeaders() {
      if (this.headerList.size() <= 1) {
         return null;
      } else {
         String[] hdrs = new String[this.headerList.size() - 1];

         for(int i = 0; i != hdrs.length; ++i) {
            hdrs[i] = (String)this.headerList.elementAt(i + 1);
         }

         return hdrs;
      }
   }

   private int readIgnoreSpace() throws IOException {
      int c;
      for(c = this.in.read(); c == 32 || c == 9; c = this.in.read()) {
      }

      return c;
   }

   public int read() throws IOException {
      if (this.start) {
         if (this.hasHeaders) {
            this.parseHeaders();
         }

         this.start = false;
      }

      int c;
      if (this.clearText) {
         c = this.in.read();
         if (c == 13 || c == 10 && this.lastC != 13) {
            this.newLineFound = true;
         } else if (this.newLineFound && c == 45) {
            c = this.in.read();
            if (c == 45) {
               this.clearText = false;
               this.start = true;
               this.restart = true;
            } else {
               c = this.in.read();
            }

            this.newLineFound = false;
         } else if (c != 10 && this.lastC != 13) {
            this.newLineFound = false;
         }

         this.lastC = c;
         return c;
      } else {
         if (this.bufPtr > 2 || this.crcFound) {
            c = this.readIgnoreSpace();
            if (c != 13 && c != 10) {
               if (c < 0) {
                  return -1;
               }

               this.bufPtr = this.decode(c, this.readIgnoreSpace(), this.readIgnoreSpace(), this.readIgnoreSpace(), this.outBuf);
            } else {
               for(c = this.readIgnoreSpace(); c == 10 || c == 13; c = this.readIgnoreSpace()) {
               }

               if (c < 0) {
                  return -1;
               }

               if (c == 61) {
                  this.bufPtr = this.decode(this.readIgnoreSpace(), this.readIgnoreSpace(), this.readIgnoreSpace(), this.readIgnoreSpace(), this.outBuf);
                  if (this.bufPtr == 0) {
                     int i = (this.outBuf[0] & 255) << 16 | (this.outBuf[1] & 255) << 8 | this.outBuf[2] & 255;
                     this.crcFound = true;
                     if (i != this.crc.getValue()) {
                        throw new IOException("crc check failed in armored message.");
                     }

                     return -1;
                  }

                  throw new IOException("no crc found in armored message.");
               }

               if (c == 45) {
                  while((c = this.in.read()) >= 0 && c != 10 && c != 13) {
                  }

                  if (!this.crcFound) {
                     throw new IOException("crc check not found.");
                  }

                  this.crcFound = false;
                  this.start = true;
                  this.bufPtr = 3;
                  return -1;
               }

               this.bufPtr = this.decode(c, this.readIgnoreSpace(), this.readIgnoreSpace(), this.readIgnoreSpace(), this.outBuf);
            }
         }

         c = this.outBuf[this.bufPtr++];
         this.crc.update(c);
         return c;
      }
   }

   public void close() throws IOException {
      this.in.close();
   }
}
