package org.bouncycastle.crypto.digests;

public class SHA1Digest extends GeneralDigest {
   private static final int DIGEST_LENGTH = 20;
   private int H1;
   private int H2;
   private int H3;
   private int H4;
   private int H5;
   private int[] X = new int[80];
   private int xOff;
   private static final int Y1 = 1518500249;
   private static final int Y2 = 1859775393;
   private static final int Y3 = -1894007588;
   private static final int Y4 = -899497514;

   public SHA1Digest() {
      this.reset();
   }

   public SHA1Digest(SHA1Digest t) {
      super(t);
      this.H1 = t.H1;
      this.H2 = t.H2;
      this.H3 = t.H3;
      this.H4 = t.H4;
      this.H5 = t.H5;
      System.arraycopy(t.X, 0, this.X, 0, t.X.length);
      this.xOff = t.xOff;
   }

   public String getAlgorithmName() {
      return "SHA-1";
   }

   public int getDigestSize() {
      return 20;
   }

   protected void processWord(byte[] in, int inOff) {
      this.X[this.xOff++] = (in[inOff] & 255) << 24 | (in[inOff + 1] & 255) << 16 | (in[inOff + 2] & 255) << 8 | in[inOff + 3] & 255;
      if (this.xOff == 16) {
         this.processBlock();
      }

   }

   private void unpackWord(int word, byte[] out, int outOff) {
      out[outOff] = (byte)(word >>> 24);
      out[outOff + 1] = (byte)(word >>> 16);
      out[outOff + 2] = (byte)(word >>> 8);
      out[outOff + 3] = (byte)word;
   }

   protected void processLength(long bitLength) {
      if (this.xOff > 14) {
         this.processBlock();
      }

      this.X[14] = (int)(bitLength >>> 32);
      this.X[15] = (int)(bitLength & -1L);
   }

   public int doFinal(byte[] out, int outOff) {
      this.finish();
      this.unpackWord(this.H1, out, outOff);
      this.unpackWord(this.H2, out, outOff + 4);
      this.unpackWord(this.H3, out, outOff + 8);
      this.unpackWord(this.H4, out, outOff + 12);
      this.unpackWord(this.H5, out, outOff + 16);
      this.reset();
      return 20;
   }

   public void reset() {
      super.reset();
      this.H1 = 1732584193;
      this.H2 = -271733879;
      this.H3 = -1732584194;
      this.H4 = 271733878;
      this.H5 = -1009589776;
      this.xOff = 0;

      for(int i = 0; i != this.X.length; ++i) {
         this.X[i] = 0;
      }

   }

   private int f(int u, int v, int w) {
      return u & v | ~u & w;
   }

   private int h(int u, int v, int w) {
      return u ^ v ^ w;
   }

   private int g(int u, int v, int w) {
      return u & v | u & w | v & w;
   }

   private int rotateLeft(int x, int n) {
      return x << n | x >>> 32 - n;
   }

   protected void processBlock() {
      int A;
      for(A = 16; A <= 79; ++A) {
         this.X[A] = this.rotateLeft(this.X[A - 3] ^ this.X[A - 8] ^ this.X[A - 14] ^ this.X[A - 16], 1);
      }

      A = this.H1;
      int B = this.H2;
      int C = this.H3;
      int D = this.H4;
      int E = this.H5;

      int i;
      int t;
      for(i = 0; i <= 19; ++i) {
         t = this.rotateLeft(A, 5) + this.f(B, C, D) + E + this.X[i] + 1518500249;
         E = D;
         D = C;
         C = this.rotateLeft(B, 30);
         B = A;
         A = t;
      }

      for(i = 20; i <= 39; ++i) {
         t = this.rotateLeft(A, 5) + this.h(B, C, D) + E + this.X[i] + 1859775393;
         E = D;
         D = C;
         C = this.rotateLeft(B, 30);
         B = A;
         A = t;
      }

      for(i = 40; i <= 59; ++i) {
         t = this.rotateLeft(A, 5) + this.g(B, C, D) + E + this.X[i] + -1894007588;
         E = D;
         D = C;
         C = this.rotateLeft(B, 30);
         B = A;
         A = t;
      }

      for(i = 60; i <= 79; ++i) {
         t = this.rotateLeft(A, 5) + this.h(B, C, D) + E + this.X[i] + -899497514;
         E = D;
         D = C;
         C = this.rotateLeft(B, 30);
         B = A;
         A = t;
      }

      this.H1 += A;
      this.H2 += B;
      this.H3 += C;
      this.H4 += D;
      this.H5 += E;
      this.xOff = 0;

      for(i = 0; i != this.X.length; ++i) {
         this.X[i] = 0;
      }

   }
}
