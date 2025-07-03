package org.bouncycastle.bcpg.attr;

import org.bouncycastle.bcpg.UserAttributeSubpacket;

public class ImageAttribute extends UserAttributeSubpacket {
   private int hdrLength;
   private int version;
   private int encoding;
   private byte[] imageData;

   public ImageAttribute(byte[] data) {
      super(1, data);
      this.hdrLength = (data[1] & 255) << 8 | data[0] & 255;
      this.version = data[2] & 255;
      this.encoding = data[3] & 255;
      this.imageData = new byte[data.length - this.hdrLength];
      System.arraycopy(data, this.hdrLength, this.imageData, 0, this.imageData.length);
   }

   public int version() {
      return this.version;
   }

   public int getEncoding() {
      return this.encoding;
   }

   public byte[] getImageData() {
      return this.imageData;
   }
}
