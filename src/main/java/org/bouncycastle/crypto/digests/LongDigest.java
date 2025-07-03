package org.bouncycastle.crypto.digests;

import org.bouncycastle.crypto.ExtendedDigest;

public abstract class LongDigest implements ExtendedDigest {
   private static final int BYTE_LENGTH = 128;
   private byte[] xBuf;
   private int xBufOff;
   private long byteCount1;
   private long byteCount2;
   protected long H1;
   protected long H2;
   protected long H3;
   protected long H4;
   protected long H5;
   protected long H6;
   protected long H7;
   protected long H8;
   private long[] W = new long[80];
   private int wOff;
   static final long[] K = new long[]{4794697086780616226L, 8158064640168781261L, -5349999486874862801L, -1606136188198331460L, 4131703408338449720L, 6480981068601479193L, -7908458776815382629L, -6116909921290321640L, -2880145864133508542L, 1334009975649890238L, 2608012711638119052L, 6128411473006802146L, 8268148722764581231L, -9160688886553864527L, -7215885187991268811L, -4495734319001033068L, -1973867731355612462L, -1171420211273849373L, 1135362057144423861L, 2597628984639134821L, 3308224258029322869L, 5365058923640841347L, 6679025012923562964L, 8573033837759648693L, -7476448914759557205L, -6327057829258317296L, -5763719355590565569L, -4658551843659510044L, -4116276920077217854L, -3051310485924567259L, 489312712824947311L, 1452737877330783856L, 2861767655752347644L, 3322285676063803686L, 5560940570517711597L, 5996557281743188959L, 7280758554555802590L, 8532644243296465576L, -9096487096722542874L, -7894198246740708037L, -6719396339535248540L, -6333637450476146687L, -4446306890439682159L, -4076793802049405392L, -3345356375505022440L, -2983346525034927856L, -860691631967231958L, 1182934255886127544L, 1847814050463011016L, 2177327727835720531L, 2830643537854262169L, 3796741975233480872L, 4115178125766777443L, 5681478168544905931L, 6601373596472566643L, 7507060721942968483L, 8399075790359081724L, 8693463985226723168L, -8878714635349349518L, -8302665154208450068L, -8016688836872298968L, -6606660893046293015L, -4685533653050689259L, -4147400797238176981L, -3880063495543823972L, -3348786107499101689L, -1523767162380948706L, -757361751448694408L, 500013540394364858L, 748580250866718886L, 1242879168328830382L, 1977374033974150939L, 2944078676154940804L, 3659926193048069267L, 4368137639120453308L, 4836135668995329356L, 5532061633213252278L, 6448918945643986474L, 6902733635092675308L, 7801388544844847127L};

   protected LongDigest() {
      this.xBuf = new byte[8];
      this.xBufOff = 0;
      this.reset();
   }

   protected LongDigest(LongDigest t) {
      this.xBuf = new byte[t.xBuf.length];
      System.arraycopy(t.xBuf, 0, this.xBuf, 0, t.xBuf.length);
      this.xBufOff = t.xBufOff;
      this.byteCount1 = t.byteCount1;
      this.byteCount2 = t.byteCount2;
      this.H1 = t.H1;
      this.H2 = t.H2;
      this.H3 = t.H3;
      this.H4 = t.H4;
      this.H5 = t.H5;
      this.H6 = t.H6;
      this.H7 = t.H7;
      this.H8 = t.H8;
      System.arraycopy(t.W, 0, this.W, 0, t.W.length);
      this.wOff = t.wOff;
   }

   public void update(byte in) {
      this.xBuf[this.xBufOff++] = in;
      if (this.xBufOff == this.xBuf.length) {
         this.processWord(this.xBuf, 0);
         this.xBufOff = 0;
      }

      ++this.byteCount1;
   }

   public void update(byte[] in, int inOff, int len) {
      while(this.xBufOff != 0 && len > 0) {
         this.update(in[inOff]);
         ++inOff;
         --len;
      }

      while(len > this.xBuf.length) {
         this.processWord(in, inOff);
         inOff += this.xBuf.length;
         len -= this.xBuf.length;
         this.byteCount1 += (long)this.xBuf.length;
      }

      while(len > 0) {
         this.update(in[inOff]);
         ++inOff;
         --len;
      }

   }

   public void finish() {
      this.adjustByteCounts();
      long lowBitLength = this.byteCount1 << 3;
      long hiBitLength = this.byteCount2;
      this.update((byte)-128);

      while(this.xBufOff != 0) {
         this.update((byte)0);
      }

      this.processLength(lowBitLength, hiBitLength);
      this.processBlock();
   }

   public void reset() {
      this.byteCount1 = 0L;
      this.byteCount2 = 0L;
      this.xBufOff = 0;

      int i;
      for(i = 0; i < this.xBuf.length; ++i) {
         this.xBuf[i] = 0;
      }

      this.wOff = 0;

      for(i = 0; i != this.W.length; ++i) {
         this.W[i] = 0L;
      }

   }

   public int getByteLength() {
      return 128;
   }

   protected void processWord(byte[] in, int inOff) {
      this.W[this.wOff++] = (long)(in[inOff] & 255) << 56 | (long)(in[inOff + 1] & 255) << 48 | (long)(in[inOff + 2] & 255) << 40 | (long)(in[inOff + 3] & 255) << 32 | (long)(in[inOff + 4] & 255) << 24 | (long)(in[inOff + 5] & 255) << 16 | (long)(in[inOff + 6] & 255) << 8 | (long)(in[inOff + 7] & 255);
      if (this.wOff == 16) {
         this.processBlock();
      }

   }

   protected void unpackWord(long word, byte[] out, int outOff) {
      out[outOff] = (byte)((int)(word >>> 56));
      out[outOff + 1] = (byte)((int)(word >>> 48));
      out[outOff + 2] = (byte)((int)(word >>> 40));
      out[outOff + 3] = (byte)((int)(word >>> 32));
      out[outOff + 4] = (byte)((int)(word >>> 24));
      out[outOff + 5] = (byte)((int)(word >>> 16));
      out[outOff + 6] = (byte)((int)(word >>> 8));
      out[outOff + 7] = (byte)((int)word);
   }

   private void adjustByteCounts() {
      if (this.byteCount1 > 2305843009213693951L) {
         this.byteCount2 += this.byteCount1 >>> 61;
         this.byteCount1 &= 2305843009213693951L;
      }

   }

   protected void processLength(long lowW, long hiW) {
      if (this.wOff > 14) {
         this.processBlock();
      }

      this.W[14] = hiW;
      this.W[15] = lowW;
   }

   protected void processBlock() {
      this.adjustByteCounts();

      for(int t = 16; t <= 79; ++t) {
         this.W[t] = this.Sigma1(this.W[t - 2]) + this.W[t - 7] + this.Sigma0(this.W[t - 15]) + this.W[t - 16];
      }

      long a = this.H1;
      long b = this.H2;
      long c = this.H3;
      long d = this.H4;
      long e = this.H5;
      long f = this.H6;
      long g = this.H7;
      long h = this.H8;

      int i;
      for(i = 0; i <= 79; ++i) {
         long T1 = h + this.Sum1(e) + this.Ch(e, f, g) + K[i] + this.W[i];
         long T2 = this.Sum0(a) + this.Maj(a, b, c);
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
      this.wOff = 0;

      for(i = 0; i != this.W.length; ++i) {
         this.W[i] = 0L;
      }

   }

   private long rotateRight(long x, int n) {
      return x >>> n | x << 64 - n;
   }

   private long Ch(long x, long y, long z) {
      return x & y ^ ~x & z;
   }

   private long Maj(long x, long y, long z) {
      return x & y ^ x & z ^ y & z;
   }

   private long Sum0(long x) {
      return this.rotateRight(x, 28) ^ this.rotateRight(x, 34) ^ this.rotateRight(x, 39);
   }

   private long Sum1(long x) {
      return this.rotateRight(x, 14) ^ this.rotateRight(x, 18) ^ this.rotateRight(x, 41);
   }

   private long Sigma0(long x) {
      return this.rotateRight(x, 1) ^ this.rotateRight(x, 8) ^ x >>> 7;
   }

   private long Sigma1(long x) {
      return this.rotateRight(x, 19) ^ this.rotateRight(x, 61) ^ x >>> 6;
   }
}
