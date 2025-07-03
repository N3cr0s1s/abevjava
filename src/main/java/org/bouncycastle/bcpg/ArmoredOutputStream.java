package org.bouncycastle.bcpg;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Hashtable;

public class ArmoredOutputStream extends OutputStream {
   private static final byte[] encodingTable = new byte[]{65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 43, 47};
   OutputStream out;
   int[] buf;
   int bufPtr;
   CRC24 crc;
   int chunkCount;
   int lastb;
   boolean start;
   boolean clearText;
   boolean newLine;
   String nl;
   String type;
   String headerStart;
   String headerTail;
   String footerStart;
   String footerTail;
   String version;
   Hashtable headers;

   private void encode(OutputStream out, int[] data, int len) throws IOException {
      int d1;
      int d2;
      switch(len) {
      case 0:
         break;
      case 1:
         d1 = data[0];
         out.write(encodingTable[d1 >>> 2 & 63]);
         out.write(encodingTable[d1 << 4 & 63]);
         out.write(61);
         out.write(61);
         break;
      case 2:
         d1 = data[0];
         d2 = data[1];
         out.write(encodingTable[d1 >>> 2 & 63]);
         out.write(encodingTable[(d1 << 4 | d2 >>> 4) & 63]);
         out.write(encodingTable[d2 << 2 & 63]);
         out.write(61);
         break;
      case 3:
         d1 = data[0];
         d2 = data[1];
         int d3 = data[2];
         out.write(encodingTable[d1 >>> 2 & 63]);
         out.write(encodingTable[(d1 << 4 | d2 >>> 4) & 63]);
         out.write(encodingTable[(d2 << 2 | d3 >>> 6) & 63]);
         out.write(encodingTable[d3 & 63]);
         break;
      default:
         throw new IOException("unknown length in encode");
      }

   }

   public ArmoredOutputStream(OutputStream out) {
      this.buf = new int[3];
      this.bufPtr = 0;
      this.crc = new CRC24();
      this.chunkCount = 0;
      this.start = true;
      this.clearText = false;
      this.newLine = false;
      this.nl = System.getProperty("line.separator");
      this.headerStart = "-----BEGIN PGP ";
      this.headerTail = "-----";
      this.footerStart = "-----END PGP ";
      this.footerTail = "-----";
      this.version = "BCPG v1.33";
      this.headers = new Hashtable();
      this.out = out;
      if (this.nl == null) {
         this.nl = "\r\n";
      }

      this.resetHeaders();
   }

   public ArmoredOutputStream(OutputStream out, Hashtable headers) {
      this(out);
      Enumeration e = headers.keys();

      while(e.hasMoreElements()) {
         Object key = e.nextElement();
         this.headers.put(key, headers.get(key));
      }

   }

   public void setHeader(String name, String value) {
      this.headers.put(name, value);
   }

   public void resetHeaders() {
      this.headers.clear();
      this.headers.put("Version", this.version);
   }

   public void beginClearText(int hashAlgorithm) throws IOException {
      String hash;
      switch(hashAlgorithm) {
      case 1:
         hash = "MD5";
         break;
      case 2:
         hash = "SHA1";
         break;
      case 3:
         hash = "RIPEMD160";
         break;
      case 4:
      case 6:
      case 7:
      default:
         throw new IOException("unknown hash algorithm tag in beginClearText: " + hashAlgorithm);
      case 5:
         hash = "MD2";
         break;
      case 8:
         hash = "SHA256";
         break;
      case 9:
         hash = "SHA384";
         break;
      case 10:
         hash = "SHA512";
      }

      String armorHdr = "-----BEGIN PGP SIGNED MESSAGE-----" + this.nl;
      String hdrs = "Hash: " + hash + this.nl + this.nl;

      int i;
      for(i = 0; i != armorHdr.length(); ++i) {
         this.out.write(armorHdr.charAt(i));
      }

      for(i = 0; i != hdrs.length(); ++i) {
         this.out.write(hdrs.charAt(i));
      }

      this.clearText = true;
      this.newLine = true;
      this.lastb = 0;
   }

   public void endClearText() {
      this.clearText = false;
   }

   private void writeHeaderEntry(String name, String value) throws IOException {
      int i;
      for(i = 0; i != name.length(); ++i) {
         this.out.write(name.charAt(i));
      }

      this.out.write(58);
      this.out.write(32);

      for(i = 0; i != value.length(); ++i) {
         this.out.write(value.charAt(i));
      }

      for(i = 0; i != this.nl.length(); ++i) {
         this.out.write(this.nl.charAt(i));
      }

   }

   public void write(int b) throws IOException {
      if (this.clearText) {
         this.out.write(b);
         if (this.newLine) {
            if (b != 10 || this.lastb != 13) {
               this.newLine = false;
            }

            if (b == 45) {
               this.out.write(32);
               this.out.write(45);
            }
         }

         if (b == 13 || b == 10 && this.lastb != 13) {
            this.newLine = true;
         }

         this.lastb = b;
      } else {
         if (this.start) {
            boolean newPacket = (b & 64) != 0;
            int tag;
            if (newPacket) {
               tag = b & 63;
            } else {
               tag = (b & 63) >> 2;
            }

            switch(tag) {
            case 2:
               this.type = "SIGNATURE";
               break;
            case 3:
            case 4:
            default:
               this.type = "MESSAGE";
               break;
            case 5:
               this.type = "PRIVATE KEY BLOCK";
               break;
            case 6:
               this.type = "PUBLIC KEY BLOCK";
            }

            int i;
            for(i = 0; i != this.headerStart.length(); ++i) {
               this.out.write(this.headerStart.charAt(i));
            }

            for(i = 0; i != this.type.length(); ++i) {
               this.out.write(this.type.charAt(i));
            }

            for(i = 0; i != this.headerTail.length(); ++i) {
               this.out.write(this.headerTail.charAt(i));
            }

            for(i = 0; i != this.nl.length(); ++i) {
               this.out.write(this.nl.charAt(i));
            }

            this.writeHeaderEntry("Version", (String)this.headers.get("Version"));
            Enumeration e = this.headers.keys();

            while(e.hasMoreElements()) {
               String key = (String)e.nextElement();
               if (!key.equals("Version")) {
                  this.writeHeaderEntry(key, (String)this.headers.get(key));
               }
            }

            for(int ix = 0; ix != this.nl.length(); ++ix) {
               this.out.write(this.nl.charAt(ix));
            }

            this.start = false;
         }

         if (this.bufPtr == 3) {
            this.encode(this.out, this.buf, this.bufPtr);
            this.bufPtr = 0;
            if ((++this.chunkCount & 15) == 0) {
               for(int i = 0; i != this.nl.length(); ++i) {
                  this.out.write(this.nl.charAt(i));
               }
            }
         }

         this.crc.update(b);
         this.buf[this.bufPtr++] = b & 255;
      }
   }

   public void flush() throws IOException {
   }

   public void close() throws IOException {
      if (this.type != null) {
         this.encode(this.out, this.buf, this.bufPtr);

         int crcV;
         for(crcV = 0; crcV != this.nl.length(); ++crcV) {
            this.out.write(this.nl.charAt(crcV));
         }

         this.out.write(61);
         crcV = this.crc.getValue();
         this.buf[0] = crcV >> 16 & 255;
         this.buf[1] = crcV >> 8 & 255;
         this.buf[2] = crcV & 255;
         this.encode(this.out, this.buf, 3);

         int i;
         for(i = 0; i != this.nl.length(); ++i) {
            this.out.write(this.nl.charAt(i));
         }

         for(i = 0; i != this.footerStart.length(); ++i) {
            this.out.write(this.footerStart.charAt(i));
         }

         for(i = 0; i != this.type.length(); ++i) {
            this.out.write(this.type.charAt(i));
         }

         for(i = 0; i != this.footerTail.length(); ++i) {
            this.out.write(this.footerTail.charAt(i));
         }

         for(i = 0; i != this.nl.length(); ++i) {
            this.out.write(this.nl.charAt(i));
         }

         this.out.flush();
         this.type = null;
         this.start = true;
      }

   }
}
