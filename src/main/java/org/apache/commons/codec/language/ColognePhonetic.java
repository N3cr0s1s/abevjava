package org.apache.commons.codec.language;

import java.util.Locale;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;

public class ColognePhonetic implements StringEncoder {
   private static final char[][] PREPROCESS_MAP = new char[][]{{'Ä', 'A'}, {'Ü', 'U'}, {'Ö', 'O'}, {'ß', 'S'}};

   private static boolean arrayContains(char[] arr, char key) {
      char[] arr$ = arr;
      int len$ = arr.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         char element = arr$[i$];
         if (element == key) {
            return true;
         }
      }

      return false;
   }

   public String colognePhonetic(String text) {
      if (text == null) {
         return null;
      } else {
         text = this.preprocess(text);
         ColognePhonetic.CologneOutputBuffer output = new ColognePhonetic.CologneOutputBuffer(text.length() * 2);
         ColognePhonetic.CologneInputBuffer input = new ColognePhonetic.CologneInputBuffer(text.toCharArray());
         char lastChar = '-';
         char lastCode = '/';
         int rightLength = input.length();

         while(true) {
            char code;
            char chr;
            while(true) {
               if (rightLength <= 0) {
                  return output.toString();
               }

               chr = input.removeNext();
               char nextChar;
               if ((rightLength = input.length()) > 0) {
                  nextChar = input.getNextChar();
               } else {
                  nextChar = '-';
               }

               if (arrayContains(new char[]{'A', 'E', 'I', 'J', 'O', 'U', 'Y'}, chr)) {
                  code = '0';
                  break;
               }

               if (chr != 'H' && chr >= 'A' && chr <= 'Z') {
                  if (chr != 'B' && (chr != 'P' || nextChar == 'H')) {
                     if ((chr == 'D' || chr == 'T') && !arrayContains(new char[]{'S', 'C', 'Z'}, nextChar)) {
                        code = '2';
                        break;
                     }

                     if (arrayContains(new char[]{'W', 'F', 'P', 'V'}, chr)) {
                        code = '3';
                        break;
                     }

                     if (arrayContains(new char[]{'G', 'K', 'Q'}, chr)) {
                        code = '4';
                        break;
                     }

                     if (chr == 'X' && !arrayContains(new char[]{'C', 'K', 'Q'}, lastChar)) {
                        code = '4';
                        input.addLeft('S');
                        ++rightLength;
                        break;
                     }

                     if (chr != 'S' && chr != 'Z') {
                        if (chr == 'C') {
                           if (lastCode == '/') {
                              if (arrayContains(new char[]{'A', 'H', 'K', 'L', 'O', 'Q', 'R', 'U', 'X'}, nextChar)) {
                                 code = '4';
                              } else {
                                 code = '8';
                              }
                              break;
                           }

                           if (!arrayContains(new char[]{'S', 'Z'}, lastChar) && arrayContains(new char[]{'A', 'H', 'O', 'U', 'K', 'Q', 'X'}, nextChar)) {
                              code = '4';
                              break;
                           }

                           code = '8';
                           break;
                        }

                        if (arrayContains(new char[]{'T', 'D', 'X'}, chr)) {
                           code = '8';
                           break;
                        }

                        if (chr == 'R') {
                           code = '7';
                           break;
                        }

                        if (chr == 'L') {
                           code = '5';
                           break;
                        }

                        if (chr != 'M' && chr != 'N') {
                           code = chr;
                           break;
                        }

                        code = '6';
                        break;
                     }

                     code = '8';
                     break;
                  }

                  code = '1';
                  break;
               }

               if (lastCode != '/') {
                  code = '-';
                  break;
               }
            }

            if (code != '-' && (lastCode != code && (code != '0' || lastCode == '/') || code < '0' || code > '8')) {
               output.addRight(code);
            }

            lastChar = chr;
            lastCode = code;
         }
      }
   }

   public Object encode(Object object) throws EncoderException {
      if (!(object instanceof String)) {
         throw new EncoderException("This method's parameter was expected to be of the type " + String.class.getName() + ". But actually it was of the type " + object.getClass().getName() + ".");
      } else {
         return this.encode((String)object);
      }
   }

   public String encode(String text) {
      return this.colognePhonetic(text);
   }

   public boolean isEncodeEqual(String text1, String text2) {
      return this.colognePhonetic(text1).equals(this.colognePhonetic(text2));
   }

   private String preprocess(String text) {
      text = text.toUpperCase(Locale.GERMAN);
      char[] chrs = text.toCharArray();

      for(int index = 0; index < chrs.length; ++index) {
         if (chrs[index] > 'Z') {
            char[][] arr$ = PREPROCESS_MAP;
            int len$ = arr$.length;

            for(int i$ = 0; i$ < len$; ++i$) {
               char[] element = arr$[i$];
               if (chrs[index] == element[0]) {
                  chrs[index] = element[1];
                  break;
               }
            }
         }
      }

      return new String(chrs);
   }

   private class CologneInputBuffer extends ColognePhonetic.CologneBuffer {
      public CologneInputBuffer(char[] data) {
         super(data);
      }

      public void addLeft(char ch) {
         ++this.length;
         this.data[this.getNextPos()] = ch;
      }

      protected char[] copyData(int start, int length) {
         char[] newData = new char[length];
         System.arraycopy(this.data, this.data.length - this.length + start, newData, 0, length);
         return newData;
      }

      public char getNextChar() {
         return this.data[this.getNextPos()];
      }

      protected int getNextPos() {
         return this.data.length - this.length;
      }

      public char removeNext() {
         char ch = this.getNextChar();
         --this.length;
         return ch;
      }
   }

   private class CologneOutputBuffer extends ColognePhonetic.CologneBuffer {
      public CologneOutputBuffer(int buffSize) {
         super(buffSize);
      }

      public void addRight(char chr) {
         this.data[this.length] = chr;
         ++this.length;
      }

      protected char[] copyData(int start, int length) {
         char[] newData = new char[length];
         System.arraycopy(this.data, start, newData, 0, length);
         return newData;
      }
   }

   private abstract class CologneBuffer {
      protected final char[] data;
      protected int length = 0;

      public CologneBuffer(char[] data) {
         this.data = data;
         this.length = data.length;
      }

      public CologneBuffer(int buffSize) {
         this.data = new char[buffSize];
         this.length = 0;
      }

      protected abstract char[] copyData(int var1, int var2);

      public int length() {
         return this.length;
      }

      public String toString() {
         return new String(this.copyData(0, this.length));
      }
   }
}
