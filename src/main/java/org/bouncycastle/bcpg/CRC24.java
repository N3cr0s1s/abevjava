package org.bouncycastle.bcpg;

public class CRC24 {
   private static int CRC24_INIT = 11994318;
   private static int CRC24_POLY = 25578747;
   private int crc;

   public CRC24() {
      this.crc = CRC24_INIT;
   }

   public void update(int b) {
      this.crc ^= b << 16;

      for(int i = 0; i < 8; ++i) {
         this.crc <<= 1;
         if ((this.crc & 16777216) != 0) {
            this.crc ^= CRC24_POLY;
         }
      }

   }

   public int getValue() {
      return this.crc;
   }

   public void reset() {
      this.crc = CRC24_INIT;
   }
}
