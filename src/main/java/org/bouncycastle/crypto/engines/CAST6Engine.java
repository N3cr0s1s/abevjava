package org.bouncycastle.crypto.engines;

public final class CAST6Engine extends CAST5Engine {
   protected static final int ROUNDS = 12;
   protected static final int BLOCK_SIZE = 16;
   protected int[] _Kr = new int[48];
   protected int[] _Km = new int[48];
   protected int[] _Tr = new int[192];
   protected int[] _Tm = new int[192];
   private int[] _workingKey = new int[8];

   public String getAlgorithmName() {
      return "CAST6";
   }

   public void reset() {
   }

   public int getBlockSize() {
      return 16;
   }

   protected void setKey(byte[] key) {
      int Cm = 1518500249;
      int Mm = 1859775393;
      int Cr = 19;
      int Mr = 17;

      int length;
      for(int i = 0; i < 24; ++i) {
         for(length = 0; length < 8; ++length) {
            this._Tm[i * 8 + length] = Cm;
            Cm = Cm + Mm & -1;
            this._Tr[i * 8 + length] = Cr;
            Cr = Cr + Mr & 31;
         }
      }

      byte[] tmpKey = new byte[64];
      length = key.length;

      int i2;
      for(i2 = 0; i2 < length; ++i2) {
         tmpKey[i2] = key[i2];
      }

      for(i2 = 0; i2 < 8; ++i2) {
         this._workingKey[i2] = this.BytesTo32bits(tmpKey, i2 * 4);
      }

      int ix2;

      for(int i = 0; i < 12; ++i) {
         ix2 = i * 2 * 8;
         int[] var10000 = this._workingKey;
         var10000[6] ^= this.F1(this._workingKey[7], this._Tm[ix2 + 0], this._Tr[ix2 + 0]);
         var10000 = this._workingKey;
         var10000[5] ^= this.F2(this._workingKey[6], this._Tm[ix2 + 1], this._Tr[ix2 + 1]);
         var10000 = this._workingKey;
         var10000[4] ^= this.F3(this._workingKey[5], this._Tm[ix2 + 2], this._Tr[ix2 + 2]);
         var10000 = this._workingKey;
         var10000[3] ^= this.F1(this._workingKey[4], this._Tm[ix2 + 3], this._Tr[ix2 + 3]);
         var10000 = this._workingKey;
         var10000[2] ^= this.F2(this._workingKey[3], this._Tm[ix2 + 4], this._Tr[ix2 + 4]);
         var10000 = this._workingKey;
         var10000[1] ^= this.F3(this._workingKey[2], this._Tm[ix2 + 5], this._Tr[ix2 + 5]);
         var10000 = this._workingKey;
         var10000[0] ^= this.F1(this._workingKey[1], this._Tm[ix2 + 6], this._Tr[ix2 + 6]);
         var10000 = this._workingKey;
         var10000[7] ^= this.F2(this._workingKey[0], this._Tm[ix2 + 7], this._Tr[ix2 + 7]);
         ix2 = (i * 2 + 1) * 8;
         var10000 = this._workingKey;
         var10000[6] ^= this.F1(this._workingKey[7], this._Tm[ix2 + 0], this._Tr[ix2 + 0]);
         var10000 = this._workingKey;
         var10000[5] ^= this.F2(this._workingKey[6], this._Tm[ix2 + 1], this._Tr[ix2 + 1]);
         var10000 = this._workingKey;
         var10000[4] ^= this.F3(this._workingKey[5], this._Tm[ix2 + 2], this._Tr[ix2 + 2]);
         var10000 = this._workingKey;
         var10000[3] ^= this.F1(this._workingKey[4], this._Tm[ix2 + 3], this._Tr[ix2 + 3]);
         var10000 = this._workingKey;
         var10000[2] ^= this.F2(this._workingKey[3], this._Tm[ix2 + 4], this._Tr[ix2 + 4]);
         var10000 = this._workingKey;
         var10000[1] ^= this.F3(this._workingKey[2], this._Tm[ix2 + 5], this._Tr[ix2 + 5]);
         var10000 = this._workingKey;
         var10000[0] ^= this.F1(this._workingKey[1], this._Tm[ix2 + 6], this._Tr[ix2 + 6]);
         var10000 = this._workingKey;
         var10000[7] ^= this.F2(this._workingKey[0], this._Tm[ix2 + 7], this._Tr[ix2 + 7]);
         this._Kr[i * 4 + 0] = this._workingKey[0] & 31;
         this._Kr[i * 4 + 1] = this._workingKey[2] & 31;
         this._Kr[i * 4 + 2] = this._workingKey[4] & 31;
         this._Kr[i * 4 + 3] = this._workingKey[6] & 31;
         this._Km[i * 4 + 0] = this._workingKey[7];
         this._Km[i * 4 + 1] = this._workingKey[5];
         this._Km[i * 4 + 2] = this._workingKey[3];
         this._Km[i * 4 + 3] = this._workingKey[1];
      }

   }

   protected int encryptBlock(byte[] src, int srcIndex, byte[] dst, int dstIndex) {
      int[] result = new int[4];
      int A = this.BytesTo32bits(src, srcIndex);
      int B = this.BytesTo32bits(src, srcIndex + 4);
      int C = this.BytesTo32bits(src, srcIndex + 8);
      int D = this.BytesTo32bits(src, srcIndex + 12);
      this.CAST_Encipher(A, B, C, D, result);
      this.Bits32ToBytes(result[0], dst, dstIndex);
      this.Bits32ToBytes(result[1], dst, dstIndex + 4);
      this.Bits32ToBytes(result[2], dst, dstIndex + 8);
      this.Bits32ToBytes(result[3], dst, dstIndex + 12);
      return 16;
   }

   protected int decryptBlock(byte[] src, int srcIndex, byte[] dst, int dstIndex) {
      int[] result = new int[4];
      int A = this.BytesTo32bits(src, srcIndex);
      int B = this.BytesTo32bits(src, srcIndex + 4);
      int C = this.BytesTo32bits(src, srcIndex + 8);
      int D = this.BytesTo32bits(src, srcIndex + 12);
      this.CAST_Decipher(A, B, C, D, result);
      this.Bits32ToBytes(result[0], dst, dstIndex);
      this.Bits32ToBytes(result[1], dst, dstIndex + 4);
      this.Bits32ToBytes(result[2], dst, dstIndex + 8);
      this.Bits32ToBytes(result[3], dst, dstIndex + 12);
      return 16;
   }

   protected final void CAST_Encipher(int A, int B, int C, int D, int[] result) {
      int x;
      int i;
      for(i = 0; i < 6; ++i) {
         x = i * 4;
         C ^= this.F1(D, this._Km[x + 0], this._Kr[x + 0]);
         B ^= this.F2(C, this._Km[x + 1], this._Kr[x + 1]);
         A ^= this.F3(B, this._Km[x + 2], this._Kr[x + 2]);
         D ^= this.F1(A, this._Km[x + 3], this._Kr[x + 3]);
      }

      for(i = 6; i < 12; ++i) {
         x = i * 4;
         D ^= this.F1(A, this._Km[x + 3], this._Kr[x + 3]);
         A ^= this.F3(B, this._Km[x + 2], this._Kr[x + 2]);
         B ^= this.F2(C, this._Km[x + 1], this._Kr[x + 1]);
         C ^= this.F1(D, this._Km[x + 0], this._Kr[x + 0]);
      }

      result[0] = A;
      result[1] = B;
      result[2] = C;
      result[3] = D;
   }

   protected final void CAST_Decipher(int A, int B, int C, int D, int[] result) {
      int x;
      int i;
      for(i = 0; i < 6; ++i) {
         x = (11 - i) * 4;
         C ^= this.F1(D, this._Km[x + 0], this._Kr[x + 0]);
         B ^= this.F2(C, this._Km[x + 1], this._Kr[x + 1]);
         A ^= this.F3(B, this._Km[x + 2], this._Kr[x + 2]);
         D ^= this.F1(A, this._Km[x + 3], this._Kr[x + 3]);
      }

      for(i = 6; i < 12; ++i) {
         x = (11 - i) * 4;
         D ^= this.F1(A, this._Km[x + 3], this._Kr[x + 3]);
         A ^= this.F3(B, this._Km[x + 2], this._Kr[x + 2]);
         B ^= this.F2(C, this._Km[x + 1], this._Kr[x + 1]);
         C ^= this.F1(D, this._Km[x + 0], this._Kr[x + 0]);
      }

      result[0] = A;
      result[1] = B;
      result[2] = C;
      result[3] = D;
   }
}
