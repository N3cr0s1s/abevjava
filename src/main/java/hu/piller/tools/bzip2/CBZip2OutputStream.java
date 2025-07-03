package hu.piller.tools.bzip2;

import java.io.IOException;
import java.io.OutputStream;

public class CBZip2OutputStream extends OutputStream implements BZip2Constants {
   protected static final int SETMASK = 2097152;
   protected static final int CLEARMASK = -2097153;
   protected static final int GREATER_ICOST = 15;
   protected static final int LESSER_ICOST = 0;
   protected static final int SMALL_THRESH = 20;
   protected static final int DEPTH_THRESH = 10;
   protected static final int QSORT_STACK_SIZE = 1000;
   int last;
   int origPtr;
   int blockSize100k;
   boolean blockRandomised;
   int bytesOut;
   int bsBuff;
   int bsLive;
   CRC mCrc;
   private boolean[] inUse;
   private int nInUse;
   private char[] seqToUnseq;
   private char[] unseqToSeq;
   private char[] selector;
   private char[] selectorMtf;
   private char[] block;
   private int[] quadrant;
   private int[] zptr;
   private short[] szptr;
   private int[] ftab;
   private int nMTF;
   private int[] mtfFreq;
   private int workFactor;
   private int workDone;
   private int workLimit;
   private boolean firstAttempt;
   private int nBlocksRandomised;
   private int currentChar;
   private int runLength;
   boolean closed;
   private int blockCRC;
   private int combinedCRC;
   private int allowableBlockSize;
   private OutputStream bsStream;
   private int[] incs;

   private static void panic() {
      System.out.println("panic");
   }

   private void makeMaps() {
      this.nInUse = 0;

      for(int i = 0; i < 256; ++i) {
         if (this.inUse[i]) {
            this.seqToUnseq[this.nInUse] = (char)i;
            this.unseqToSeq[i] = (char)this.nInUse;
            ++this.nInUse;
         }
      }

   }

   protected static void hbMakeCodeLengths(char[] len, int[] freq, int alphaSize, int maxLen) {
      int[] heap = new int[260];
      int[] weight = new int[516];
      int[] parent = new int[516];

      int i;
      for(i = 0; i < alphaSize; ++i) {
         weight[i + 1] = (freq[i] == 0 ? 1 : freq[i]) << 8;
      }

      while(true) {
         int nNodes = alphaSize;
         int nHeap = 0;
         heap[0] = 0;
         weight[0] = 0;
         parent[0] = -2;

         int zz;
         int yy;
         for(i = 1; i <= alphaSize; ++i) {
            parent[i] = -1;
            ++nHeap;
            heap[nHeap] = i;
            zz = nHeap;

            for(yy = heap[nHeap]; weight[yy] < weight[heap[zz >> 1]]; zz >>= 1) {
               heap[zz] = heap[zz >> 1];
            }

            heap[zz] = yy;
         }

         if (nHeap >= 260) {
            panic();
         }

         while(nHeap > 1) {
            int n1 = heap[1];
            heap[1] = heap[nHeap];
            --nHeap;
            int zzz = 1;
            int tmp = heap[zzz];

            while(true) {
               yy = zzz << 1;
               if (yy > nHeap) {
                  break;
               }

               if (yy < nHeap && weight[heap[yy + 1]] < weight[heap[yy]]) {
                  ++yy;
               }

               if (weight[tmp] < weight[heap[yy]]) {
                  break;
               }

               heap[zzz] = heap[yy];
               zzz = yy;
            }

            heap[zzz] = tmp;
            int n2 = heap[1];
            heap[1] = heap[nHeap];
            --nHeap;
            zzz = 1;
            tmp = heap[zzz];

            while(true) {
               yy = zzz << 1;
               if (yy > nHeap) {
                  break;
               }

               if (yy < nHeap && weight[heap[yy + 1]] < weight[heap[yy]]) {
                  ++yy;
               }

               if (weight[tmp] < weight[heap[yy]]) {
                  break;
               }

               heap[zzz] = heap[yy];
               zzz = yy;
            }

            heap[zzz] = tmp;
            ++nNodes;
            parent[n1] = parent[n2] = nNodes;
            weight[nNodes] = (weight[n1] & -256) + (weight[n2] & -256) | 1 + ((weight[n1] & 255) > (weight[n2] & 255) ? weight[n1] & 255 : weight[n2] & 255);
            parent[nNodes] = -1;
            ++nHeap;
            heap[nHeap] = nNodes;;
            zzz = nHeap;

            for(yy = heap[nHeap]; weight[yy] < weight[heap[zzz >> 1]]; zzz >>= 1) {
               heap[zzz] = heap[zzz >> 1];
            }

            heap[zzz] = yy;
         }

         if (nNodes >= 516) {
            panic();
         }

         boolean tooLong = false;

         int j;
         for(i = 1; i <= alphaSize; ++i) {
            j = 0;

            for(int k = i; parent[k] >= 0; ++j) {
               k = parent[k];
            }

            len[i - 1] = (char)j;
            if (j > maxLen) {
               tooLong = true;
            }
         }

         if (!tooLong) {
            return;
         }

         for(i = 1; i < alphaSize; ++i) {
            j = weight[i] >> 8;
            j = 1 + j / 2;
            weight[i] = j << 8;
         }
      }
   }

   public CBZip2OutputStream(OutputStream inStream) throws IOException {
      this(inStream, 9);
   }

   public CBZip2OutputStream(OutputStream inStream, int inBlockSize) throws IOException {
      this.mCrc = new CRC();
      this.inUse = new boolean[256];
      this.seqToUnseq = new char[256];
      this.unseqToSeq = new char[256];
      this.selector = new char[18002];
      this.selectorMtf = new char[18002];
      this.mtfFreq = new int[258];
      this.currentChar = -1;
      this.runLength = 0;
      this.closed = false;
      this.incs = new int[]{1, 4, 13, 40, 121, 364, 1093, 3280, 9841, 29524, 88573, 265720, 797161, 2391484};
      this.block = null;
      this.quadrant = null;
      this.zptr = null;
      this.ftab = null;
      this.bsSetStream(inStream);
      this.workFactor = 50;
      if (inBlockSize > 9) {
         inBlockSize = 9;
      }

      if (inBlockSize < 1) {
         inBlockSize = 1;
      }

      this.blockSize100k = inBlockSize;
      this.allocateCompressStructures();
      this.initialize();
      this.initBlock();
   }

   public void write(int bv) throws IOException {
      int b = 256 + bv & 255;
      if (this.currentChar != -1) {
         if (this.currentChar == b) {
            if (++this.runLength > 254) {
               this.writeRun();
               this.currentChar = -1;
               this.runLength = 0;
            }
         } else {
            this.writeRun();
            this.runLength = 1;
            this.currentChar = b;
         }
      } else {
         this.currentChar = b;
         ++this.runLength;
      }

   }

   private void writeRun() throws IOException {
      if (this.last < this.allowableBlockSize) {
         this.inUse[this.currentChar] = true;

         for(int i = 0; i < this.runLength; ++i) {
            this.mCrc.updateCRC((char)this.currentChar);
         }

         switch(this.runLength) {
         case 1:
            this.block[this.last + 2] = (char)this.currentChar;
            ++this.last;
            break;
         case 2:
            this.block[this.last + 2] = (char)this.currentChar;
            this.block[this.last + 3] = (char)this.currentChar;
            this.last += 2;
            break;
         case 3:
            this.block[this.last + 2] = (char)this.currentChar;
            this.block[this.last + 3] = (char)this.currentChar;
            this.block[this.last + 4] = (char)this.currentChar;
            this.last += 3;
            break;
         default:
            this.inUse[this.runLength - 4] = true;
            this.block[this.last + 2] = (char)this.currentChar;
            this.block[this.last + 3] = (char)this.currentChar;
            this.block[this.last + 4] = (char)this.currentChar;
            this.block[this.last + 5] = (char)this.currentChar;
            this.block[this.last + 6] = (char)(this.runLength - 4);
            this.last += 5;
         }
      } else {
         this.endBlock();
         this.initBlock();
         this.writeRun();
      }

   }

   @SuppressWarnings("removal")
   @Override
   protected void finalize() throws Throwable {
      this.close();
      super.finalize();
   }

   public void close() throws IOException {
      if (!this.closed) {
         if (this.runLength > 0) {
            this.writeRun();
         }

         this.currentChar = -1;
         this.endBlock();
         this.endCompression();
         this.closed = true;
         super.close();
         this.bsStream.close();
      }
   }

   public void flush() throws IOException {
      super.flush();
      this.bsStream.flush();
   }

   private void initialize() throws IOException {
      this.bytesOut = 0;
      this.nBlocksRandomised = 0;
      this.bsPutUChar(104);
      this.bsPutUChar(48 + this.blockSize100k);
      this.combinedCRC = 0;
   }

   private void initBlock() {
      this.mCrc.initialiseCRC();
      this.last = -1;

      for(int i = 0; i < 256; ++i) {
         this.inUse[i] = false;
      }

      this.allowableBlockSize = 100000 * this.blockSize100k - 20;
   }

   private void endBlock() throws IOException {
      this.blockCRC = this.mCrc.getFinalCRC();
      this.combinedCRC = this.combinedCRC << 1 | this.combinedCRC >>> 31;
      this.combinedCRC ^= this.blockCRC;
      this.doReversibleTransformation();
      this.bsPutUChar(49);
      this.bsPutUChar(65);
      this.bsPutUChar(89);
      this.bsPutUChar(38);
      this.bsPutUChar(83);
      this.bsPutUChar(89);
      this.bsPutint(this.blockCRC);
      if (this.blockRandomised) {
         this.bsW(1, 1);
         ++this.nBlocksRandomised;
      } else {
         this.bsW(1, 0);
      }

      this.moveToFrontCodeAndSend();
   }

   private void endCompression() throws IOException {
      this.bsPutUChar(23);
      this.bsPutUChar(114);
      this.bsPutUChar(69);
      this.bsPutUChar(56);
      this.bsPutUChar(80);
      this.bsPutUChar(144);
      this.bsPutint(this.combinedCRC);
      this.bsFinishedWithStream();
   }

   private void hbAssignCodes(int[] code, char[] length, int minLen, int maxLen, int alphaSize) {
      int vec = 0;

      for(int n = minLen; n <= maxLen; ++n) {
         for(int i = 0; i < alphaSize; ++i) {
            if (length[i] == n) {
               code[i] = vec++;
            }
         }

         vec <<= 1;
      }

   }

   private void bsSetStream(OutputStream f) {
      this.bsStream = f;
      this.bsLive = 0;
      this.bsBuff = 0;
      this.bytesOut = 0;
   }

   private void bsFinishedWithStream() throws IOException {
      while(this.bsLive > 0) {
         int ch = this.bsBuff >> 24;

         try {
            this.bsStream.write(ch);
         } catch (IOException var3) {
            throw var3;
         }

         this.bsBuff <<= 8;
         this.bsLive -= 8;
         ++this.bytesOut;
      }

   }

   private void bsW(int n, int v) throws IOException {
      while(this.bsLive >= 8) {
         int ch = this.bsBuff >> 24;

         try {
            this.bsStream.write(ch);
         } catch (IOException var5) {
            throw var5;
         }

         this.bsBuff <<= 8;
         this.bsLive -= 8;
         ++this.bytesOut;
      }

      this.bsBuff |= v << 32 - this.bsLive - n;
      this.bsLive += n;
   }

   private void bsPutUChar(int c) throws IOException {
      this.bsW(8, c);
   }

   private void bsPutint(int u) throws IOException {
      this.bsW(8, u >> 24 & 255);
      this.bsW(8, u >> 16 & 255);
      this.bsW(8, u >> 8 & 255);
      this.bsW(8, u & 255);
   }

   private void bsPutIntVS(int numBits, int c) throws IOException {
      this.bsW(numBits, c);
   }

   private void sendMTFValues() throws IOException {
      char[][] len = new char[6][258];
      int nSelectors = 0;
      int alphaSize = this.nInUse + 2;

      int v;
      int t;
      for(t = 0; t < 6; ++t) {
         for(v = 0; v < alphaSize; ++v) {
            len[t][v] = 15;
         }
      }

      if (this.nMTF <= 0) {
         panic();
      }

      byte nGroups;
      if (this.nMTF < 200) {
         nGroups = 2;
      } else if (this.nMTF < 600) {
         nGroups = 3;
      } else if (this.nMTF < 1200) {
         nGroups = 4;
      } else if (this.nMTF < 2400) {
         nGroups = 5;
      } else {
         nGroups = 6;
      }

      int nPart = nGroups;
      int remF = this.nMTF;

      int gs;
      int ge;
      int aFreq;
      for(gs = 0; nPart > 0; remF -= aFreq) {
         int tFreq = remF / nPart;
         ge = gs - 1;

         for(aFreq = 0; aFreq < tFreq && ge < alphaSize - 1; aFreq += this.mtfFreq[ge]) {
            ++ge;
         }

         if (ge > gs && nPart != nGroups && nPart != 1 && (nGroups - nPart) % 2 == 1) {
            aFreq -= this.mtfFreq[ge];
            --ge;
         }

         for(v = 0; v < alphaSize; ++v) {
            if (v >= gs && v <= ge) {
               len[nPart - 1][v] = 0;
            } else {
               len[nPart - 1][v] = 15;
            }
         }

         --nPart;
         gs = ge + 1;
      }

      int[][] rfreq = new int[6][258];
      int[] fave = new int[6];
      short[] cost = new short[6];

      int i;
      for(int iter = 0; iter < 4; ++iter) {
         for(t = 0; t < nGroups; ++t) {
            fave[t] = 0;
         }

         for(t = 0; t < nGroups; ++t) {
            for(v = 0; v < alphaSize; ++v) {
               rfreq[t][v] = 0;
            }
         }

         nSelectors = 0;
         int totc = 0;

         for(gs = 0; gs < this.nMTF; gs = ge + 1) {
            ge = gs + 50 - 1;
            if (ge >= this.nMTF) {
               ge = this.nMTF - 1;
            }

            for(t = 0; t < nGroups; ++t) {
               cost[t] = 0;
            }

            short icv;
            if (nGroups == 6) {
               short cost5 = 0;
               short cost4 = 0;
               short cost3 = 0;
               short cost2 = 0;
               short cost1 = 0;
               icv = 0;

               for(i = gs; i <= ge; ++i) {
                  short icv1 = this.szptr[i];
                  icv1 = (short)(icv1 + len[0][icv1]);
                  cost1 = (short)(cost1 + len[1][icv1]);
                  cost2 = (short)(cost2 + len[2][icv1]);
                  cost3 = (short)(cost3 + len[3][icv1]);
                  cost4 = (short)(cost4 + len[4][icv1]);
                  cost5 = (short)(cost5 + len[5][icv1]);
               }

               cost[0] = icv;
               cost[1] = cost1;
               cost[2] = cost2;
               cost[3] = cost3;
               cost[4] = cost4;
               cost[5] = cost5;
            } else {
               for(i = gs; i <= ge; ++i) {
                  icv = this.szptr[i];

                  for(t = 0; t < nGroups; ++t) {
                     cost[t] = (short)(cost[t] + len[t][icv]);
                  }
               }
            }

            int bc = 999999999;
            int bt = -1;

            for(t = 0; t < nGroups; ++t) {
               if (cost[t] < bc) {
                  bc = cost[t];
                  bt = t;
               }
            }

            totc += bc;
            int var10002 = fave[bt]++;
            this.selector[nSelectors] = (char)bt;
            ++nSelectors;

            for(i = gs; i <= ge; ++i) {
               var10002 = rfreq[bt][this.szptr[i]]++;
            }
         }

         for(t = 0; t < nGroups; ++t) {
            hbMakeCodeLengths(len[t], rfreq[t], alphaSize, 20);
         }
      }

      if (nGroups >= 8) {
         panic();
      }

      if (nSelectors >= 32768 || nSelectors > 18002) {
         panic();
      }

      char[] pos = new char[6];

      for(i = 0; i < nGroups; ++i) {
         pos[i] = (char)i;
      }

      int j;
      for(i = 0; i < nSelectors; ++i) {
         char ll_i = this.selector[i];
         j = 0;

         char tmp2;
         char tmp;
         for(tmp = pos[j]; ll_i != tmp; pos[j] = tmp2) {
            ++j;
            tmp2 = tmp;
            tmp = pos[j];
         }

         pos[0] = tmp;
         this.selectorMtf[i] = (char)j;
      }

      int[][] code = new int[6][258];

      for(t = 0; t < nGroups; ++t) {
         int minLen = ' ';
         int maxLen = 0;

         for(i = 0; i < alphaSize; ++i) {
            if (len[t][i] > maxLen) {
               maxLen = len[t][i];
            }

            if (len[t][i] < minLen) {
               minLen = len[t][i];
            }
         }

         if (maxLen > 20) {
            panic();
         }

         if (minLen < 1) {
            panic();
         }

         this.hbAssignCodes(code[t], len[t], minLen, maxLen, alphaSize);
      }

      boolean[] inUse16 = new boolean[16];

      for(i = 0; i < 16; ++i) {
         inUse16[i] = false;

         for(j = 0; j < 16; ++j) {
            if (this.inUse[i * 16 + j]) {
               inUse16[i] = true;
            }
         }
      }

      int nBytes = this.bytesOut;

      for(i = 0; i < 16; ++i) {
         if (inUse16[i]) {
            this.bsW(1, 1);
         } else {
            this.bsW(1, 0);
         }
      }

      for(i = 0; i < 16; ++i) {
         if (inUse16[i]) {
            for(j = 0; j < 16; ++j) {
               if (this.inUse[i * 16 + j]) {
                  this.bsW(1, 1);
               } else {
                  this.bsW(1, 0);
               }
            }
         }
      }

      nBytes = this.bytesOut;
      this.bsW(3, nGroups);
      this.bsW(15, nSelectors);

      for(i = 0; i < nSelectors; ++i) {
         for(j = 0; j < this.selectorMtf[i]; ++j) {
            this.bsW(1, 1);
         }

         this.bsW(1, 0);
      }

      nBytes = this.bytesOut;

      for(t = 0; t < nGroups; ++t) {
         int curr = len[t][0];
         this.bsW(5, curr);

         for(i = 0; i < alphaSize; ++i) {
            while(curr < len[t][i]) {
               this.bsW(2, 2);
               ++curr;
            }

            while(curr > len[t][i]) {
               this.bsW(2, 3);
               --curr;
            }

            this.bsW(1, 0);
         }
      }

      nBytes = this.bytesOut;
      int selCtr = 0;

      for(gs = 0; gs < this.nMTF; ++selCtr) {
         ge = gs + 50 - 1;
         if (ge >= this.nMTF) {
            ge = this.nMTF - 1;
         }

         for(i = gs; i <= ge; ++i) {
            this.bsW(len[this.selector[selCtr]][this.szptr[i]], code[this.selector[selCtr]][this.szptr[i]]);
         }

         gs = ge + 1;
      }

      if (selCtr != nSelectors) {
         panic();
      }

   }

   private void moveToFrontCodeAndSend() throws IOException {
      this.bsPutIntVS(24, this.origPtr);
      this.generateMTFValues();
      this.sendMTFValues();
   }

   private void simpleSort(int lo, int hi, int d) {
      int bigN = hi - lo + 1;
      if (bigN >= 2) {
         int hp;
         for(hp = 0; this.incs[hp] < bigN; ++hp) {
         }

         --hp;

         for(; hp >= 0; --hp) {
            int h = this.incs[hp];
            int i = lo + h;

            while(i <= hi) {
               int v = this.zptr[i];
               int j = i;

               while(this.fullGtU(this.zptr[j - h] + d, v + d)) {
                  this.zptr[j] = this.zptr[j - h];
                  j -= h;
                  if (j <= lo + h - 1) {
                     break;
                  }
               }

               this.zptr[j] = v;
               ++i;
               if (i > hi) {
                  break;
               }

               v = this.zptr[i];
               j = i;

               while(this.fullGtU(this.zptr[j - h] + d, v + d)) {
                  this.zptr[j] = this.zptr[j - h];
                  j -= h;
                  if (j <= lo + h - 1) {
                     break;
                  }
               }

               this.zptr[j] = v;
               ++i;
               if (i > hi) {
                  break;
               }

               v = this.zptr[i];
               j = i;

               while(this.fullGtU(this.zptr[j - h] + d, v + d)) {
                  this.zptr[j] = this.zptr[j - h];
                  j -= h;
                  if (j <= lo + h - 1) {
                     break;
                  }
               }

               this.zptr[j] = v;
               ++i;
               if (this.workDone > this.workLimit && this.firstAttempt) {
                  return;
               }
            }
         }

      }
   }

   private void vswap(int p1, int p2, int n) {
      for(boolean var4 = false; n > 0; --n) {
         int temp = this.zptr[p1];
         this.zptr[p1] = this.zptr[p2];
         this.zptr[p2] = temp;
         ++p1;
         ++p2;
      }

   }

   private char med3(char a, char b, char c) {
      if (a > b) {
         char t = a;
         a = b;
         b = t;
      }

      if (b > c) {
         b = c;
      }

      if (a > b) {
         b = a;
      }

      return b;
   }

   private void qSort3(int loSt, int hiSt, int dSt) {
      CBZip2OutputStream.StackElem[] stack = new CBZip2OutputStream.StackElem[1000];

      int temp;
      for(temp = 0; temp < 1000; ++temp) {
         stack[temp] = new CBZip2OutputStream.StackElem((CBZip2OutputStream.StackElem)null);
      }

      int sp = 0;
      stack[sp].ll = loSt;
      stack[sp].hh = hiSt;
      stack[sp].dd = dSt;
      sp = sp + 1;

      while(true) {
         label59:
         while(sp > 0) {
            if (sp >= 1000) {
               panic();
            }

            --sp;
            int lo = stack[sp].ll;
            int hi = stack[sp].hh;
            int d = stack[sp].dd;
            if (hi - lo >= 20 && d <= 10) {
               int med = this.med3(this.block[this.zptr[lo] + d + 1], this.block[this.zptr[hi] + d + 1], this.block[this.zptr[lo + hi >> 1] + d + 1]);
               int ltLo = lo;
               int unLo = lo;
               int gtHi = hi;
               int unHi = hi;

               while(true) {
                  while(true) {
                     int n;
                     int myTmp = 0;
                     if (unLo <= unHi) {
                        n = this.block[this.zptr[unLo] + d + 1] - med;
                        if (n == 0) {
                           myTmp = 0;
                           myTmp = this.zptr[unLo];
                           this.zptr[unLo] = this.zptr[ltLo];
                           this.zptr[ltLo] = myTmp;
                           ++ltLo;
                           ++unLo;
                           continue;
                        }

                        if (n <= 0) {
                           ++unLo;
                           continue;
                        }
                     }

                     while(unLo <= unHi) {
                        n = this.block[this.zptr[unHi] + d + 1] - med;
                        if (n == 0) {
                           myTmp = 0;
                           myTmp = this.zptr[unHi];
                           this.zptr[unHi] = this.zptr[gtHi];
                           this.zptr[gtHi] = myTmp;
                           --gtHi;
                           --unHi;
                        } else {
                           if (n < 0) {
                              break;
                           }

                           --unHi;
                        }
                     }

                     if (unLo > unHi) {
                        if (gtHi < ltLo) {
                           stack[sp].ll = lo;
                           stack[sp].hh = hi;
                           stack[sp].dd = d + 1;
                           ++sp;
                        } else {
                           n = ltLo - lo < unLo - ltLo ? ltLo - lo : unLo - ltLo;
                           this.vswap(lo, unLo - n, n);
                           int m = hi - gtHi < gtHi - unHi ? hi - gtHi : gtHi - unHi;
                           this.vswap(unLo, hi - m + 1, m);
                           n = lo + unLo - ltLo - 1;
                           m = hi - (gtHi - unHi) + 1;
                           stack[sp].ll = lo;
                           stack[sp].hh = n;
                           stack[sp].dd = d;
                           ++sp;
                           stack[sp].ll = n + 1;
                           stack[sp].hh = m - 1;
                           stack[sp].dd = d + 1;
                           ++sp;
                           stack[sp].ll = m;
                           stack[sp].hh = hi;
                           stack[sp].dd = d;
                           ++sp;
                        }
                        continue label59;
                     }

                     myTmp = 0;
                     myTmp = this.zptr[unLo];
                     this.zptr[unLo] = this.zptr[unHi];
                     this.zptr[unHi] = myTmp;
                     ++unLo;
                     --unHi;
                  }
               }
            } else {
               this.simpleSort(lo, hi, d);
               if (this.workDone > this.workLimit && this.firstAttempt) {
                  return;
               }
            }
         }

         return;
      }
   }

   private void mainSort() {
      int[] runningOrder = new int[256];
      int[] copy = new int[256];
      boolean[] bigDone = new boolean[256];

      int i;
      for(i = 0; i < 20; ++i) {
         this.block[this.last + i + 2] = this.block[i % (this.last + 1) + 1];
      }

      for(i = 0; i <= this.last + 20; ++i) {
         this.quadrant[i] = 0;
      }

      this.block[0] = this.block[this.last + 1];
      if (this.last < 4000) {
         for(i = 0; i <= this.last; this.zptr[i] = i++) {
         }

         this.firstAttempt = false;
         this.workDone = this.workLimit = 0;
         this.simpleSort(0, this.last, 0);
      } else {
         int numQSorted = 0;

         for(i = 0; i <= 255; ++i) {
            bigDone[i] = false;
         }

         for(i = 0; i <= 65536; ++i) {
            this.ftab[i] = 0;
         }

         int c1 = this.block[0];

         char c2;
         int var10002;
         for(i = 0; i <= this.last; ++i) {
            c2 = this.block[i + 1];
            var10002 = this.ftab[(c1 << 8) + c2]++;
            c1 = c2;
         }

         int[] var10000;
         for(i = 1; i <= 65536; ++i) {
            var10000 = this.ftab;
            var10000[i] += this.ftab[i - 1];
         }

         c1 = this.block[1];

         int j;
         for(i = 0; i < this.last; this.zptr[this.ftab[j]] = i++) {
            c2 = this.block[i + 2];
            j = (c1 << 8) + c2;
            c1 = c2;
            var10002 = this.ftab[j]--;
         }

         j = (this.block[this.last + 1] << 8) + this.block[1];
         var10002 = this.ftab[j]--;
         this.zptr[this.ftab[j]] = this.last;

         for(i = 0; i <= 255; runningOrder[i] = i++) {
         }

         int bbSize = 1;

         do {
            bbSize = 3 * bbSize + 1;
         } while(bbSize <= 256);

         int bbStart;
         do {
            bbSize /= 3;

            for(i = bbSize; i <= 255; ++i) {
               bbStart = runningOrder[i];
               j = i;

               while(this.ftab[runningOrder[j - bbSize] + 1 << 8] - this.ftab[runningOrder[j - bbSize] << 8] > this.ftab[bbStart + 1 << 8] - this.ftab[bbStart << 8]) {
                  runningOrder[j] = runningOrder[j - bbSize];
                  j -= bbSize;
                  if (j <= bbSize - 1) {
                     break;
                  }
               }

               runningOrder[j] = bbStart;
            }
         } while(bbSize != 1);

         for(i = 0; i <= 255; ++i) {
            int ss = runningOrder[i];

            for(j = 0; j <= 255; ++j) {
               int sb = (ss << 8) + j;
               if ((this.ftab[sb] & 2097152) != 2097152) {
                  bbStart = this.ftab[sb] & -2097153;
                  bbSize = (this.ftab[sb + 1] & -2097153) - 1;
                  if (bbSize > bbStart) {
                     this.qSort3(bbStart, bbSize, 2);
                     numQSorted += bbSize - bbStart + 1;
                     if (this.workDone > this.workLimit && this.firstAttempt) {
                        return;
                     }
                  }

                  var10000 = this.ftab;
                  var10000[sb] |= 2097152;
               }
            }

            bigDone[ss] = true;
            if (i < 255) {
               bbStart = this.ftab[ss << 8] & -2097153;
               bbSize = (this.ftab[ss + 1 << 8] & -2097153) - bbStart;

               int shifts;
               for(shifts = 0; bbSize >> shifts > 65534; ++shifts) {
               }

               for(j = 0; j < bbSize; ++j) {
                  int a2update = this.zptr[bbStart + j];
                  int qVal = j >> shifts;
                  this.quadrant[a2update] = qVal;
                  if (a2update < 20) {
                     this.quadrant[a2update + this.last + 1] = qVal;
                  }
               }

               if (bbSize - 1 >> shifts > 65535) {
                  panic();
               }
            }

            for(j = 0; j <= 255; ++j) {
               copy[j] = this.ftab[(j << 8) + ss] & -2097153;
            }

            for(j = this.ftab[ss << 8] & -2097153; j < (this.ftab[ss + 1 << 8] & -2097153); ++j) {
               c1 = this.block[this.zptr[j]];
               if (!bigDone[c1]) {
                  this.zptr[copy[c1]] = this.zptr[j] == 0 ? this.last : this.zptr[j] - 1;
                  var10002 = copy[c1]++;
               }
            }

            for(j = 0; j <= 255; ++j) {
               var10000 = this.ftab;
               var10000[(j << 8) + ss] |= 2097152;
            }
         }
      }

   }

   private void randomiseBlock() {
      int rNToGo = 0;
      int rTPos = 0;

      int i;
      for(i = 0; i < 256; ++i) {
         this.inUse[i] = false;
      }

      for(i = 0; i <= this.last; ++i) {
         if (rNToGo == 0) {
            rNToGo = (char)rNums[rTPos];
            ++rTPos;
            if (rTPos == 512) {
               rTPos = 0;
            }
         }

         --rNToGo;
         char[] var10000 = this.block;
         var10000[i + 1] = (char)(var10000[i + 1] ^ (rNToGo == 1 ? 1 : 0));
         var10000 = this.block;
         var10000[i + 1] = (char)(var10000[i + 1] & 255);
         this.inUse[this.block[i + 1]] = true;
      }

   }

   private void doReversibleTransformation() {
      this.workLimit = this.workFactor * this.last;
      this.workDone = 0;
      this.blockRandomised = false;
      this.firstAttempt = true;
      this.mainSort();
      if (this.workDone > this.workLimit && this.firstAttempt) {
         this.randomiseBlock();
         this.workLimit = this.workDone = 0;
         this.blockRandomised = true;
         this.firstAttempt = false;
         this.mainSort();
      }

      this.origPtr = -1;

      for(int i = 0; i <= this.last; ++i) {
         if (this.zptr[i] == 0) {
            this.origPtr = i;
            break;
         }
      }

      if (this.origPtr == -1) {
         panic();
      }

   }

   private boolean fullGtU(int i1, int i2) {
      char c1 = this.block[i1 + 1];
      char c2 = this.block[i2 + 1];
      if (c1 != c2) {
         return c1 > c2;
      } else {
         ++i1;
         ++i2;
         c1 = this.block[i1 + 1];
         c2 = this.block[i2 + 1];
         if (c1 != c2) {
            return c1 > c2;
         } else {
            ++i1;
            ++i2;
            c1 = this.block[i1 + 1];
            c2 = this.block[i2 + 1];
            if (c1 != c2) {
               return c1 > c2;
            } else {
               ++i1;
               ++i2;
               c1 = this.block[i1 + 1];
               c2 = this.block[i2 + 1];
               if (c1 != c2) {
                  return c1 > c2;
               } else {
                  ++i1;
                  ++i2;
                  c1 = this.block[i1 + 1];
                  c2 = this.block[i2 + 1];
                  if (c1 != c2) {
                     return c1 > c2;
                  } else {
                     ++i1;
                     ++i2;
                     c1 = this.block[i1 + 1];
                     c2 = this.block[i2 + 1];
                     if (c1 != c2) {
                        return c1 > c2;
                     } else {
                        ++i1;
                        ++i2;
                        int k = this.last + 1;

                        do {
                           c1 = this.block[i1 + 1];
                           c2 = this.block[i2 + 1];
                           if (c1 != c2) {
                              return c1 > c2;
                           }

                           int s1 = this.quadrant[i1];
                           int s2 = this.quadrant[i2];
                           if (s1 != s2) {
                              if (s1 > s2) {
                                 return true;
                              }

                              return false;
                           }

                           ++i1;
                           ++i2;
                           c1 = this.block[i1 + 1];
                           c2 = this.block[i2 + 1];
                           if (c1 != c2) {
                              if (c1 > c2) {
                                 return true;
                              }

                              return false;
                           }

                           s1 = this.quadrant[i1];
                           s2 = this.quadrant[i2];
                           if (s1 != s2) {
                              if (s1 > s2) {
                                 return true;
                              }

                              return false;
                           }

                           ++i1;
                           ++i2;
                           c1 = this.block[i1 + 1];
                           c2 = this.block[i2 + 1];
                           if (c1 != c2) {
                              if (c1 > c2) {
                                 return true;
                              }

                              return false;
                           }

                           s1 = this.quadrant[i1];
                           s2 = this.quadrant[i2];
                           if (s1 != s2) {
                              if (s1 > s2) {
                                 return true;
                              }

                              return false;
                           }

                           ++i1;
                           ++i2;
                           c1 = this.block[i1 + 1];
                           c2 = this.block[i2 + 1];
                           if (c1 != c2) {
                              if (c1 > c2) {
                                 return true;
                              }

                              return false;
                           }

                           s1 = this.quadrant[i1];
                           s2 = this.quadrant[i2];
                           if (s1 != s2) {
                              if (s1 > s2) {
                                 return true;
                              }

                              return false;
                           }

                           ++i1;
                           ++i2;
                           if (i1 > this.last) {
                              i1 -= this.last;
                              --i1;
                           }

                           if (i2 > this.last) {
                              i2 -= this.last;
                              --i2;
                           }

                           k -= 4;
                           ++this.workDone;
                        } while(k >= 0);

                        return false;
                     }
                  }
               }
            }
         }
      }
   }

   private void allocateCompressStructures() {
      int n = 100000 * this.blockSize100k;
      this.block = new char[n + 1 + 20];
      this.quadrant = new int[n + 20];
      this.zptr = new int[n];
      this.ftab = new int[65537];
      if (this.block != null && this.quadrant != null && this.zptr != null) {
      }

      this.szptr = new short[2 * n];
   }

   private void generateMTFValues() {
      char[] yy = new char[256];
      this.makeMaps();
      int EOB = this.nInUse + 1;

      int i;
      for(i = 0; i <= EOB; ++i) {
         this.mtfFreq[i] = 0;
      }

      int wr = 0;
      int zPend = 0;

      for(i = 0; i < this.nInUse; ++i) {
         yy[i] = (char)i;
      }

      int var10002;
      for(i = 0; i <= this.last; ++i) {
         char ll_i = this.unseqToSeq[this.block[this.zptr[i]]];
         int j = 0;

         char tmp;
         char tmp2;
         for(tmp = yy[j]; ll_i != tmp; yy[j] = tmp2) {
            ++j;
            tmp2 = tmp;
            tmp = yy[j];
         }

         yy[0] = tmp;
         if (j == 0) {
            ++zPend;
         } else {
            if (zPend > 0) {
               --zPend;

               while(true) {
                  switch(zPend % 2) {
                  case 0:
                     this.szptr[wr] = 0;
                     ++wr;
                     var10002 = this.mtfFreq[0]++;
                     break;
                  case 1:
                     this.szptr[wr] = 1;
                     ++wr;
                     var10002 = this.mtfFreq[1]++;
                  }

                  if (zPend < 2) {
                     zPend = 0;
                     break;
                  }

                  zPend = (zPend - 2) / 2;
               }
            }

            this.szptr[wr] = (short)(j + 1);
            ++wr;
            var10002 = this.mtfFreq[j + 1]++;
         }
      }

      if (zPend > 0) {
         --zPend;

         while(true) {
            switch(zPend % 2) {
            case 0:
               this.szptr[wr] = 0;
               ++wr;
               var10002 = this.mtfFreq[0]++;
               break;
            case 1:
               this.szptr[wr] = 1;
               ++wr;
               var10002 = this.mtfFreq[1]++;
            }

            if (zPend < 2) {
               break;
            }

            zPend = (zPend - 2) / 2;
         }
      }

      this.szptr[wr] = (short)EOB;
      ++wr;
      var10002 = this.mtfFreq[EOB]++;
      this.nMTF = wr;
   }

   private static class StackElem {
      int ll;
      int hh;
      int dd;

      private StackElem() {
      }

      // $FF: synthetic method
      StackElem(CBZip2OutputStream.StackElem var1) {
         this();
      }
   }
}
