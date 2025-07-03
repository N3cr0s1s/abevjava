package hu.piller.tools.bzip2;

import java.io.IOException;
import java.io.InputStream;

public class CBZip2InputStream extends InputStream implements BZip2Constants {
   private int last;
   private int origPtr;
   private int blockSize100k;
   private boolean blockRandomised;
   private int bsBuff;
   private int bsLive;
   private CRC mCrc = new CRC();
   private boolean[] inUse = new boolean[256];
   private int nInUse;
   private char[] seqToUnseq = new char[256];
   private char[] unseqToSeq = new char[256];
   private char[] selector = new char[18002];
   private char[] selectorMtf = new char[18002];
   private int[] tt = null;
   private char[] ll8 = null;
   private int[] unzftab = new int[256];
   private int[][] limit = new int[6][258];
   private int[][] base = new int[6][258];
   private int[][] perm = new int[6][258];
   private int[] minLens = new int[6];
   private InputStream bsStream;
   private boolean streamEnd = false;
   private int currentChar = -1;
   private static final int START_BLOCK_STATE = 1;
   private static final int RAND_PART_A_STATE = 2;
   private static final int RAND_PART_B_STATE = 3;
   private static final int RAND_PART_C_STATE = 4;
   private static final int NO_RAND_PART_A_STATE = 5;
   private static final int NO_RAND_PART_B_STATE = 6;
   private static final int NO_RAND_PART_C_STATE = 7;
   private int currentState = 1;
   private int storedBlockCRC;
   private int storedCombinedCRC;
   private int computedBlockCRC;
   private int computedCombinedCRC;
   int i2;
   int count;
   int chPrev;
   int ch2;
   int i;
   int tPos;
   int rNToGo = 0;
   int rTPos = 0;
   int j2;
   char z;

   private static void cadvise() {
      System.out.println("CRC Error");
   }

   private static void badBGLengths() {
      cadvise();
   }

   private static void bitStreamEOF() {
      cadvise();
   }

   private static void compressedStreamEOF() {
      cadvise();
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

   public CBZip2InputStream(InputStream zStream) {
      this.bsSetStream(zStream);
      this.initialize();
      this.initBlock();
      this.setupBlock();
   }

   public int read() {
      if (this.streamEnd) {
         return -1;
      } else {
         int retChar = this.currentChar;
         switch(this.currentState) {
         case 1:
         case 2:
         case 5:
         default:
            break;
         case 3:
            this.setupRandPartB();
            break;
         case 4:
            this.setupRandPartC();
            break;
         case 6:
            this.setupNoRandPartB();
            break;
         case 7:
            this.setupNoRandPartC();
         }

         return retChar;
      }
   }

   private void initialize() {
      char magic3 = this.bsGetUChar();
      char magic4 = this.bsGetUChar();
      if (magic3 == 'h' && magic4 >= '1' && magic4 <= '9') {
         this.setDecompressStructureSizes(magic4 - 48);
         this.computedCombinedCRC = 0;
      } else {
         this.bsFinishedWithStream();
         this.streamEnd = true;
      }
   }

   private void initBlock() {
      char magic1 = this.bsGetUChar();
      char magic2 = this.bsGetUChar();
      char magic3 = this.bsGetUChar();
      char magic4 = this.bsGetUChar();
      char magic5 = this.bsGetUChar();
      char magic6 = this.bsGetUChar();
      if (magic1 == 23 && magic2 == 'r' && magic3 == 'E' && magic4 == '8' && magic5 == 'P' && magic6 == 144) {
         this.complete();
      } else if (magic1 == '1' && magic2 == 'A' && magic3 == 'Y' && magic4 == '&' && magic5 == 'S' && magic6 == 'Y') {
         this.storedBlockCRC = this.bsGetInt32();
         if (this.bsR(1) == 1) {
            this.blockRandomised = true;
         } else {
            this.blockRandomised = false;
         }

         this.getAndMoveToFrontDecode();
         this.mCrc.initialiseCRC();
         this.currentState = 1;
      } else {
         badBlockHeader();
         this.streamEnd = true;
      }
   }

   private void endBlock() {
      this.computedBlockCRC = this.mCrc.getFinalCRC();
      if (this.storedBlockCRC != this.computedBlockCRC) {
         crcError();
      }

      this.computedCombinedCRC = this.computedCombinedCRC << 1 | this.computedCombinedCRC >>> 31;
      this.computedCombinedCRC ^= this.computedBlockCRC;
   }

   private void complete() {
      this.storedCombinedCRC = this.bsGetInt32();
      if (this.storedCombinedCRC != this.computedCombinedCRC) {
         crcError();
      }

      this.bsFinishedWithStream();
      this.streamEnd = true;
   }

   private static void blockOverrun() {
      cadvise();
   }

   private static void badBlockHeader() {
      cadvise();
   }

   private static void crcError() {
      cadvise();
   }

   private void bsFinishedWithStream() {
      try {
         if (this.bsStream != null && this.bsStream != System.in) {
            this.bsStream.close();
            this.bsStream = null;
         }
      } catch (IOException var2) {
      }

   }

   private void bsSetStream(InputStream f) {
      this.bsStream = f;
      this.bsLive = 0;
      this.bsBuff = 0;
   }

   private int bsR(int n) {
      while(this.bsLive < n) {
         char thech = 0;

         try {
            thech = (char)this.bsStream.read();
         } catch (IOException var6) {
            compressedStreamEOF();
         }

         if (thech == -1) {
            compressedStreamEOF();
         }

         this.bsBuff = this.bsBuff << 8 | thech & 255;
         this.bsLive += 8;
      }

      int v = this.bsBuff >> this.bsLive - n & (1 << n) - 1;
      this.bsLive -= n;
      return v;
   }

   private char bsGetUChar() {
      return (char)this.bsR(8);
   }

   private int bsGetint() {
      int u = 8 | this.bsR(8);
      u = u << 8 | this.bsR(8);
      u = u << 8 | this.bsR(8);
      u = u << 8 | this.bsR(8);
      return u;
   }

   private int bsGetIntVS(int numBits) {
      return this.bsR(numBits);
   }

   private int bsGetInt32() {
      return this.bsGetint();
   }

   private void hbCreateDecodeTables(int[] limit, int[] base, int[] perm, char[] length, int minLen, int maxLen, int alphaSize) {
      int pp = 0;

      int i;
      for(i = minLen; i <= maxLen; ++i) {
         for(int j = 0; j < alphaSize; ++j) {
            if (length[j] == i) {
               perm[pp] = j;
               ++pp;
            }
         }
      }

      for(i = 0; i < 23; ++i) {
         base[i] = 0;
      }

      for(i = 0; i < alphaSize; ++i) {
         ++base[length[i] + 1];
      }

      for(i = 1; i < 23; ++i) {
         base[i] += base[i - 1];
      }

      for(i = 0; i < 23; ++i) {
         limit[i] = 0;
      }

      int vec = 0;

      for(i = minLen; i <= maxLen; ++i) {
         vec += base[i + 1] - base[i];
         limit[i] = vec - 1;
         vec <<= 1;
      }

      for(i = minLen + 1; i <= maxLen; ++i) {
         base[i] = (limit[i - 1] + 1 << 1) - base[i];
      }

   }

   private void recvDecodingTables() {
      char[][] len = new char[6][258];
      boolean[] inUse16 = new boolean[16];

      int i;
      for(i = 0; i < 16; ++i) {
         if (this.bsR(1) == 1) {
            inUse16[i] = true;
         } else {
            inUse16[i] = false;
         }
      }

      for(i = 0; i < 256; ++i) {
         this.inUse[i] = false;
      }

      int j;
      for(i = 0; i < 16; ++i) {
         if (inUse16[i]) {
            for(j = 0; j < 16; ++j) {
               if (this.bsR(1) == 1) {
                  this.inUse[i * 16 + j] = true;
               }
            }
         }
      }

      this.makeMaps();
      int alphaSize = this.nInUse + 2;
      int nGroups = this.bsR(3);
      int nSelectors = this.bsR(15);

      for(i = 0; i < nSelectors; ++i) {
         for(j = 0; this.bsR(1) == 1; ++j) {
         }

         this.selectorMtf[i] = (char)j;
      }

      char[] pos = new char[6];

      char v;
      for(v = 0; v < nGroups; pos[v] = v++) {
      }

      for(i = 0; i < nSelectors; ++i) {
         v = this.selectorMtf[i];

         char tmp;
         for(tmp = pos[v]; v > 0; --v) {
            pos[v] = pos[v - 1];
         }

         pos[0] = tmp;
         this.selector[i] = tmp;
      }

      int t;
      for(t = 0; t < nGroups; ++t) {
         int curr = this.bsR(5);

         for(i = 0; i < alphaSize; ++i) {
            while(this.bsR(1) == 1) {
               if (this.bsR(1) == 0) {
                  ++curr;
               } else {
                  --curr;
               }
            }

            len[t][i] = (char)curr;
         }
      }

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

         this.hbCreateDecodeTables(this.limit[t], this.base[t], this.perm[t], len[t], minLen, maxLen, alphaSize);
         this.minLens[t] = minLen;
      }

   }

   private void getAndMoveToFrontDecode() {
      char[] yy = new char[256];
      int limitLast = 100000 * this.blockSize100k;
      this.origPtr = this.bsGetIntVS(24);
      this.recvDecodingTables();
      int EOB = this.nInUse + 1;
      int groupNo = -1;
      int groupPos = 0;

      int i;
      for(i = 0; i <= 255; ++i) {
         this.unzftab[i] = 0;
      }

      for(i = 0; i <= 255; ++i) {
         yy[i] = (char)i;
      }

      this.last = -1;
      if (groupPos == 0) {
         ++groupNo;
         groupPos = 50;
      }

      groupPos = groupPos - 1;
      int ch = this.selector[groupNo];
      int s = this.minLens[ch];

      int zn;
      int zvec;
      for(zn = this.bsR(s); zn > this.limit[ch][s]; zn = zn << 1 | zvec) {
         ++s;

         while(this.bsLive < 1) {
            char thech = 0;

            try {
               thech = (char)this.bsStream.read();
            } catch (IOException var21) {
               compressedStreamEOF();
            }

            if (thech == -1) {
               compressedStreamEOF();
            }

            this.bsBuff = this.bsBuff << 8 | thech & 255;
            this.bsLive += 8;
         }

         zvec = this.bsBuff >> this.bsLive - 1 & 1;
         --this.bsLive;
      }

      int nextSym = this.perm[ch][zn - this.base[ch][s]];

      while(true) {
         while(nextSym != EOB) {
            int zj;
            if (nextSym != 0 && nextSym != 1) {
               ++this.last;
               if (this.last >= limitLast) {
                  blockOverrun();
               }

               ch = yy[nextSym - 1];
               int var10002 = this.unzftab[this.seqToUnseq[ch]]++;
               this.ll8[this.last] = this.seqToUnseq[ch];

               int j;
               for(j = nextSym - 1; j > 3; j -= 4) {
                  yy[j] = yy[j - 1];
                  yy[j - 1] = yy[j - 2];
                  yy[j - 2] = yy[j - 3];
                  yy[j - 3] = yy[j - 4];
               }

               while(j > 0) {
                  yy[j] = yy[j - 1];
                  --j;
               }

               yy[0] = (char) ch;
               if (groupPos == 0) {
                  ++groupNo;
                  groupPos = 50;
               }

               --groupPos;
               int zt = this.selector[groupNo];
               zn = this.minLens[zt];

               for(zvec = this.bsR(zn); zvec > this.limit[zt][zn]; zvec = zvec << 1 | zj) {
                  ++zn;

                  while(this.bsLive < 1) {
                     char thech = 0;

                     try {
                        thech = (char)this.bsStream.read();
                     } catch (IOException var19) {
                        compressedStreamEOF();
                     }

                     this.bsBuff = this.bsBuff << 8 | thech & 255;
                     this.bsLive += 8;
                  }

                  zj = this.bsBuff >> this.bsLive - 1 & 1;
                  --this.bsLive;
               }

               nextSym = this.perm[zt][zvec - this.base[zt][zn]];
            } else {
               s = -1;
               zn = 1;

               do {
                  if (nextSym == 0) {
                     s += 1 * zn;
                  } else if (nextSym == 1) {
                     s += 2 * zn;
                  }

                  zn *= 2;
                  if (groupPos == 0) {
                     ++groupNo;
                     groupPos = 50;
                  }

                  --groupPos;
                  int zt = this.selector[groupNo];
                  zj = this.minLens[zt];

                  zj = 0;
                  zvec = 0;
                  for(zvec = this.bsR(zj); zvec > this.limit[zt][zj]; zvec = zvec << 1 | zj) {
                     ++zj;

                     while(this.bsLive < 1) {
                        char thech = 0;

                        try {
                           thech = (char)this.bsStream.read();
                        } catch (IOException var20) {
                           compressedStreamEOF();
                        }

                        if (thech == -1) {
                           compressedStreamEOF();
                        }

                        this.bsBuff = this.bsBuff << 8 | thech & 255;
                        this.bsLive += 8;
                     }

                     zj = this.bsBuff >> this.bsLive - 1 & 1;
                     --this.bsLive;
                  }

                  nextSym = this.perm[zt][zvec - this.base[zt][zj]];
               } while(nextSym == 0 || nextSym == 1);

               ++s;
               ch = this.seqToUnseq[yy[0]];
               int[] var10000 = this.unzftab;

               for(var10000[ch] += s; s > 0; --s) {
                  ++this.last;
                  this.ll8[this.last] = (char) ch;
               }

               if (this.last >= limitLast) {
                  blockOverrun();
               }
            }
         }

         return;
      }
   }

   private void setupBlock() {
      int[] cftab = new int[257];
      cftab[0] = 0;

      for(this.i = 1; this.i <= 256; ++this.i) {
         cftab[this.i] = this.unzftab[this.i - 1];
      }

      for(this.i = 1; this.i <= 256; ++this.i) {
         int var10001 = this.i;
         cftab[var10001] += cftab[this.i - 1];
      }

      for(this.i = 0; this.i <= this.last; ++this.i) {
         char ch = this.ll8[this.i];
         this.tt[cftab[ch]] = this.i;
         int var10002 = cftab[ch]++;
      }

      this.tPos = this.tt[this.origPtr];
      this.count = 0;
      this.i2 = 0;
      this.ch2 = 256;
      if (this.blockRandomised) {
         this.rNToGo = 0;
         this.rTPos = 0;
         this.setupRandPartA();
      } else {
         this.setupNoRandPartA();
      }

   }

   private void setupRandPartA() {
      if (this.i2 <= this.last) {
         this.chPrev = this.ch2;
         this.ch2 = this.ll8[this.tPos];
         this.tPos = this.tt[this.tPos];
         if (this.rNToGo == 0) {
            this.rNToGo = rNums[this.rTPos];
            ++this.rTPos;
            if (this.rTPos == 512) {
               this.rTPos = 0;
            }
         }

         --this.rNToGo;
         this.ch2 ^= this.rNToGo == 1 ? 1 : 0;
         ++this.i2;
         this.currentChar = this.ch2;
         this.currentState = 3;
         this.mCrc.updateCRC(this.ch2);
      } else {
         this.endBlock();
         this.initBlock();
         this.setupBlock();
      }

   }

   private void setupNoRandPartA() {
      if (this.i2 <= this.last) {
         this.chPrev = this.ch2;
         this.ch2 = this.ll8[this.tPos];
         this.tPos = this.tt[this.tPos];
         ++this.i2;
         this.currentChar = this.ch2;
         this.currentState = 6;
         this.mCrc.updateCRC(this.ch2);
      } else {
         this.endBlock();
         this.initBlock();
         this.setupBlock();
      }

   }

   private void setupRandPartB() {
      if (this.ch2 != this.chPrev) {
         this.currentState = 2;
         this.count = 1;
         this.setupRandPartA();
      } else {
         ++this.count;
         if (this.count >= 4) {
            this.z = this.ll8[this.tPos];
            this.tPos = this.tt[this.tPos];
            if (this.rNToGo == 0) {
               this.rNToGo = rNums[this.rTPos];
               ++this.rTPos;
               if (this.rTPos == 512) {
                  this.rTPos = 0;
               }
            }

            --this.rNToGo;
            this.z = (char)(this.z ^ (this.rNToGo == 1 ? 1 : 0));
            this.j2 = 0;
            this.currentState = 4;
            this.setupRandPartC();
         } else {
            this.currentState = 2;
            this.setupRandPartA();
         }
      }

   }

   private void setupRandPartC() {
      if (this.j2 < this.z) {
         this.currentChar = this.ch2;
         this.mCrc.updateCRC(this.ch2);
         ++this.j2;
      } else {
         this.currentState = 2;
         ++this.i2;
         this.count = 0;
         this.setupRandPartA();
      }

   }

   private void setupNoRandPartB() {
      if (this.ch2 != this.chPrev) {
         this.currentState = 5;
         this.count = 1;
         this.setupNoRandPartA();
      } else {
         ++this.count;
         if (this.count >= 4) {
            this.z = this.ll8[this.tPos];
            this.tPos = this.tt[this.tPos];
            this.currentState = 7;
            this.j2 = 0;
            this.setupNoRandPartC();
         } else {
            this.currentState = 5;
            this.setupNoRandPartA();
         }
      }

   }

   private void setupNoRandPartC() {
      if (this.j2 < this.z) {
         this.currentChar = this.ch2;
         this.mCrc.updateCRC(this.ch2);
         ++this.j2;
      } else {
         this.currentState = 5;
         ++this.i2;
         this.count = 0;
         this.setupNoRandPartA();
      }

   }

   private void setDecompressStructureSizes(int newSize100k) {
      if (newSize100k >= 0 && newSize100k <= 9 && this.blockSize100k >= 0) {
      }

      this.blockSize100k = newSize100k;
      if (newSize100k != 0) {
         int n = 100000 * newSize100k;
         this.ll8 = new char[n];
         this.tt = new int[n];
      }
   }
}
