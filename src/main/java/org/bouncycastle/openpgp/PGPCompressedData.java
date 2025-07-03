package org.bouncycastle.openpgp;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;
import org.bouncycastle.bcpg.BCPGInputStream;
import org.bouncycastle.bcpg.CompressedDataPacket;
import org.bouncycastle.bcpg.CompressionAlgorithmTags;

public class PGPCompressedData implements CompressionAlgorithmTags {
   CompressedDataPacket data;

   public PGPCompressedData(BCPGInputStream pIn) throws IOException {
      this.data = (CompressedDataPacket)pIn.readPacket();
   }

   public int getAlgorithm() {
      return this.data.getAlgorithm();
   }

   public InputStream getInputStream() {
      return this.data.getInputStream();
   }

   public InputStream getDataStream() throws PGPException {
      if (this.getAlgorithm() == 1) {
         return new InflaterInputStream(this.getInputStream(), new Inflater(true)) {
            private boolean eof = false;

            protected void fill() throws IOException {
               if (this.eof) {
                  throw new EOFException("Unexpected end of ZIP input stream");
               } else {
                  this.len = this.in.read(this.buf, 0, this.buf.length);
                  if (this.len == -1) {
                     this.buf[0] = 0;
                     this.len = 1;
                     this.eof = true;
                  }

                  this.inf.setInput(this.buf, 0, this.len);
               }
            }
         };
      } else if (this.getAlgorithm() == 2) {
         return new InflaterInputStream(this.getInputStream());
      } else {
         throw new PGPException("can't recognise compression algorithm: " + this.getAlgorithm());
      }
   }
}
