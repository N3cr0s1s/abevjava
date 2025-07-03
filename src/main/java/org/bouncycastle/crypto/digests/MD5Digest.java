package org.bouncycastle.crypto.digests;

public class MD5Digest extends GeneralDigest {
   private static final int DIGEST_LENGTH = 16;
   private int H1;
   private int H2;
   private int H3;
   private int H4;
   private int[] X = new int[16];
   private int xOff;
   private static final int S11 = 7;
   private static final int S12 = 12;
   private static final int S13 = 17;
   private static final int S14 = 22;
   private static final int S21 = 5;
   private static final int S22 = 9;
   private static final int S23 = 14;
   private static final int S24 = 20;
   private static final int S31 = 4;
   private static final int S32 = 11;
   private static final int S33 = 16;
   private static final int S34 = 23;
   private static final int S41 = 6;
   private static final int S42 = 10;
   private static final int S43 = 15;
   private static final int S44 = 21;

   public MD5Digest() {
      this.reset();
   }

   public MD5Digest(MD5Digest t) {
      super(t);
      this.H1 = t.H1;
      this.H2 = t.H2;
      this.H3 = t.H3;
      this.H4 = t.H4;
      System.arraycopy(t.X, 0, this.X, 0, t.X.length);
      this.xOff = t.xOff;
   }

   public String getAlgorithmName() {
      return "MD5";
   }

   public int getDigestSize() {
      return 16;
   }

   protected void processWord(byte[] in, int inOff) {
      this.X[this.xOff++] = in[inOff] & 255 | (in[inOff + 1] & 255) << 8 | (in[inOff + 2] & 255) << 16 | (in[inOff + 3] & 255) << 24;
      if (this.xOff == 16) {
         this.processBlock();
      }

   }

   protected void processLength(long bitLength) {
      if (this.xOff > 14) {
         this.processBlock();
      }

      this.X[14] = (int)(bitLength & -1L);
      this.X[15] = (int)(bitLength >>> 32);
   }

   private void unpackWord(int word, byte[] out, int outOff) {
      out[outOff] = (byte)word;
      out[outOff + 1] = (byte)(word >>> 8);
      out[outOff + 2] = (byte)(word >>> 16);
      out[outOff + 3] = (byte)(word >>> 24);
   }

   public int doFinal(byte[] out, int outOff) {
      this.finish();
      this.unpackWord(this.H1, out, outOff);
      this.unpackWord(this.H2, out, outOff + 4);
      this.unpackWord(this.H3, out, outOff + 8);
      this.unpackWord(this.H4, out, outOff + 12);
      this.reset();
      return 16;
   }

   public void reset() {
      super.reset();
      this.H1 = 1732584193;
      this.H2 = -271733879;
      this.H3 = -1732584194;
      this.H4 = 271733878;
      this.xOff = 0;

      for(int i = 0; i != this.X.length; ++i) {
         this.X[i] = 0;
      }

   }

   private int rotateLeft(int x, int n) {
      return x << n | x >>> 32 - n;
   }

   private int F(int u, int v, int w) {
      return u & v | ~u & w;
   }

   private int G(int u, int v, int w) {
      return u & w | v & ~w;
   }

   private int H(int u, int v, int w) {
      return u ^ v ^ w;
   }

   private int K(int u, int v, int w) {
      return v ^ (u | ~w);
   }

   protected void processBlock() {
      int a = this.H1;
      int b = this.H2;
      int c = this.H3;
      int d = this.H4;
      a = this.rotateLeft(a + this.F(b, c, d) + this.X[0] + -680876936, 7) + b;
      d = this.rotateLeft(d + this.F(a, b, c) + this.X[1] + -389564586, 12) + a;
      c = this.rotateLeft(c + this.F(d, a, b) + this.X[2] + 606105819, 17) + d;
      b = this.rotateLeft(b + this.F(c, d, a) + this.X[3] + -1044525330, 22) + c;
      a = this.rotateLeft(a + this.F(b, c, d) + this.X[4] + -176418897, 7) + b;
      d = this.rotateLeft(d + this.F(a, b, c) + this.X[5] + 1200080426, 12) + a;
      c = this.rotateLeft(c + this.F(d, a, b) + this.X[6] + -1473231341, 17) + d;
      b = this.rotateLeft(b + this.F(c, d, a) + this.X[7] + -45705983, 22) + c;
      a = this.rotateLeft(a + this.F(b, c, d) + this.X[8] + 1770035416, 7) + b;
      d = this.rotateLeft(d + this.F(a, b, c) + this.X[9] + -1958414417, 12) + a;
      c = this.rotateLeft(c + this.F(d, a, b) + this.X[10] + -42063, 17) + d;
      b = this.rotateLeft(b + this.F(c, d, a) + this.X[11] + -1990404162, 22) + c;
      a = this.rotateLeft(a + this.F(b, c, d) + this.X[12] + 1804603682, 7) + b;
      d = this.rotateLeft(d + this.F(a, b, c) + this.X[13] + -40341101, 12) + a;
      c = this.rotateLeft(c + this.F(d, a, b) + this.X[14] + -1502002290, 17) + d;
      b = this.rotateLeft(b + this.F(c, d, a) + this.X[15] + 1236535329, 22) + c;
      a = this.rotateLeft(a + this.G(b, c, d) + this.X[1] + -165796510, 5) + b;
      d = this.rotateLeft(d + this.G(a, b, c) + this.X[6] + -1069501632, 9) + a;
      c = this.rotateLeft(c + this.G(d, a, b) + this.X[11] + 643717713, 14) + d;
      b = this.rotateLeft(b + this.G(c, d, a) + this.X[0] + -373897302, 20) + c;
      a = this.rotateLeft(a + this.G(b, c, d) + this.X[5] + -701558691, 5) + b;
      d = this.rotateLeft(d + this.G(a, b, c) + this.X[10] + 38016083, 9) + a;
      c = this.rotateLeft(c + this.G(d, a, b) + this.X[15] + -660478335, 14) + d;
      b = this.rotateLeft(b + this.G(c, d, a) + this.X[4] + -405537848, 20) + c;
      a = this.rotateLeft(a + this.G(b, c, d) + this.X[9] + 568446438, 5) + b;
      d = this.rotateLeft(d + this.G(a, b, c) + this.X[14] + -1019803690, 9) + a;
      c = this.rotateLeft(c + this.G(d, a, b) + this.X[3] + -187363961, 14) + d;
      b = this.rotateLeft(b + this.G(c, d, a) + this.X[8] + 1163531501, 20) + c;
      a = this.rotateLeft(a + this.G(b, c, d) + this.X[13] + -1444681467, 5) + b;
      d = this.rotateLeft(d + this.G(a, b, c) + this.X[2] + -51403784, 9) + a;
      c = this.rotateLeft(c + this.G(d, a, b) + this.X[7] + 1735328473, 14) + d;
      b = this.rotateLeft(b + this.G(c, d, a) + this.X[12] + -1926607734, 20) + c;
      a = this.rotateLeft(a + this.H(b, c, d) + this.X[5] + -378558, 4) + b;
      d = this.rotateLeft(d + this.H(a, b, c) + this.X[8] + -2022574463, 11) + a;
      c = this.rotateLeft(c + this.H(d, a, b) + this.X[11] + 1839030562, 16) + d;
      b = this.rotateLeft(b + this.H(c, d, a) + this.X[14] + -35309556, 23) + c;
      a = this.rotateLeft(a + this.H(b, c, d) + this.X[1] + -1530992060, 4) + b;
      d = this.rotateLeft(d + this.H(a, b, c) + this.X[4] + 1272893353, 11) + a;
      c = this.rotateLeft(c + this.H(d, a, b) + this.X[7] + -155497632, 16) + d;
      b = this.rotateLeft(b + this.H(c, d, a) + this.X[10] + -1094730640, 23) + c;
      a = this.rotateLeft(a + this.H(b, c, d) + this.X[13] + 681279174, 4) + b;
      d = this.rotateLeft(d + this.H(a, b, c) + this.X[0] + -358537222, 11) + a;
      c = this.rotateLeft(c + this.H(d, a, b) + this.X[3] + -722521979, 16) + d;
      b = this.rotateLeft(b + this.H(c, d, a) + this.X[6] + 76029189, 23) + c;
      a = this.rotateLeft(a + this.H(b, c, d) + this.X[9] + -640364487, 4) + b;
      d = this.rotateLeft(d + this.H(a, b, c) + this.X[12] + -421815835, 11) + a;
      c = this.rotateLeft(c + this.H(d, a, b) + this.X[15] + 530742520, 16) + d;
      b = this.rotateLeft(b + this.H(c, d, a) + this.X[2] + -995338651, 23) + c;
      a = this.rotateLeft(a + this.K(b, c, d) + this.X[0] + -198630844, 6) + b;
      d = this.rotateLeft(d + this.K(a, b, c) + this.X[7] + 1126891415, 10) + a;
      c = this.rotateLeft(c + this.K(d, a, b) + this.X[14] + -1416354905, 15) + d;
      b = this.rotateLeft(b + this.K(c, d, a) + this.X[5] + -57434055, 21) + c;
      a = this.rotateLeft(a + this.K(b, c, d) + this.X[12] + 1700485571, 6) + b;
      d = this.rotateLeft(d + this.K(a, b, c) + this.X[3] + -1894986606, 10) + a;
      c = this.rotateLeft(c + this.K(d, a, b) + this.X[10] + -1051523, 15) + d;
      b = this.rotateLeft(b + this.K(c, d, a) + this.X[1] + -2054922799, 21) + c;
      a = this.rotateLeft(a + this.K(b, c, d) + this.X[8] + 1873313359, 6) + b;
      d = this.rotateLeft(d + this.K(a, b, c) + this.X[15] + -30611744, 10) + a;
      c = this.rotateLeft(c + this.K(d, a, b) + this.X[6] + -1560198380, 15) + d;
      b = this.rotateLeft(b + this.K(c, d, a) + this.X[13] + 1309151649, 21) + c;
      a = this.rotateLeft(a + this.K(b, c, d) + this.X[4] + -145523070, 6) + b;
      d = this.rotateLeft(d + this.K(a, b, c) + this.X[11] + -1120210379, 10) + a;
      c = this.rotateLeft(c + this.K(d, a, b) + this.X[2] + 718787259, 15) + d;
      b = this.rotateLeft(b + this.K(c, d, a) + this.X[9] + -343485551, 21) + c;
      this.H1 += a;
      this.H2 += b;
      this.H3 += c;
      this.H4 += d;
      this.xOff = 0;

      for(int i = 0; i != this.X.length; ++i) {
         this.X[i] = 0;
      }

   }
}
