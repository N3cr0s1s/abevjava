package org.bouncycastle.crypto.digests;

public class SHA224Digest extends GeneralDigest {
   private static final int DIGEST_LENGTH = 28;
   private int H1;
   private int H2;
   private int H3;
   private int H4;
   private int H5;
   private int H6;
   private int H7;
   private int H8;
   private int[] X = new int[64];
   private int xOff;
   static final int[] K = new int[]{1116352408, 1899447441, -1245643825, -373957723, 961987163, 1508970993, -1841331548, -1424204075, -670586216, 310598401, 607225278, 1426881987, 1925078388, -2132889090, -1680079193, -1046744716, -459576895, -272742522, 264347078, 604807628, 770255983, 1249150122, 1555081692, 1996064986, -1740746414, -1473132947, -1341970488, -1084653625, -958395405, -710438585, 113926993, 338241895, 666307205, 773529912, 1294757372, 1396182291, 1695183700, 1986661051, -2117940946, -1838011259, -1564481375, -1474664885, -1035236496, -949202525, -778901479, -694614492, -200395387, 275423344, 430227734, 506948616, 659060556, 883997877, 958139571, 1322822218, 1537002063, 1747873779, 1955562222, 2024104815, -2067236844, -1933114872, -1866530822, -1538233109, -1090935817, -965641998};

   public SHA224Digest() {
      this.reset();
   }

   public SHA224Digest(SHA224Digest t) {
      super(t);
      this.H1 = t.H1;
      this.H2 = t.H2;
      this.H3 = t.H3;
      this.H4 = t.H4;
      this.H5 = t.H5;
      this.H6 = t.H6;
      this.H7 = t.H7;
      this.H8 = t.H8;
      System.arraycopy(t.X, 0, this.X, 0, t.X.length);
      this.xOff = t.xOff;
   }

   public String getAlgorithmName() {
      return "SHA-224";
   }

   public int getDigestSize() {
      return 28;
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
      this.unpackWord(this.H6, out, outOff + 20);
      this.unpackWord(this.H7, out, outOff + 24);
      this.reset();
      return 28;
   }

   public void reset() {
      super.reset();
      this.H1 = -1056596264;
      this.H2 = 914150663;
      this.H3 = 812702999;
      this.H4 = -150054599;
      this.H5 = -4191439;
      this.H6 = 1750603025;
      this.H7 = 1694076839;
      this.H8 = -1090891868;
      this.xOff = 0;

      for(int i = 0; i != this.X.length; ++i) {
         this.X[i] = 0;
      }

   }

   protected void processBlock() {
      int a;
      for(a = 16; a <= 63; ++a) {
         this.X[a] = this.Theta1(this.X[a - 2]) + this.X[a - 7] + this.Theta0(this.X[a - 15]) + this.X[a - 16];
      }

      a = this.H1;
      int b = this.H2;
      int c = this.H3;
      int d = this.H4;
      int e = this.H5;
      int f = this.H6;
      int g = this.H7;
      int h = this.H8;

      int i;
      for(i = 0; i <= 63; ++i) {
         int T1 = h + this.Sum1(e) + this.Ch(e, f, g) + K[i] + this.X[i];
         int T2 = this.Sum0(a) + this.Maj(a, b, c);
         h = g;
         g = f;
         f = e;
         e = d + T1;
         d = c;
         c = b;
         b = a;
         a = T1 + T2;
      }

      this.H1 += a;
      this.H2 += b;
      this.H3 += c;
      this.H4 += d;
      this.H5 += e;
      this.H6 += f;
      this.H7 += g;
      this.H8 += h;
      this.xOff = 0;

      for(i = 0; i != this.X.length; ++i) {
         this.X[i] = 0;
      }

   }

   private int rotateRight(int x, int n) {
      return x >>> n | x << 32 - n;
   }

   private int Ch(int x, int y, int z) {
      return x & y ^ ~x & z;
   }

   private int Maj(int x, int y, int z) {
      return x & y ^ x & z ^ y & z;
   }

   private int Sum0(int x) {
      return this.rotateRight(x, 2) ^ this.rotateRight(x, 13) ^ this.rotateRight(x, 22);
   }

   private int Sum1(int x) {
      return this.rotateRight(x, 6) ^ this.rotateRight(x, 11) ^ this.rotateRight(x, 25);
   }

   private int Theta0(int x) {
      return this.rotateRight(x, 7) ^ this.rotateRight(x, 18) ^ x >>> 3;
   }

   private int Theta1(int x) {
      return this.rotateRight(x, 17) ^ this.rotateRight(x, 19) ^ x >>> 10;
   }
}
