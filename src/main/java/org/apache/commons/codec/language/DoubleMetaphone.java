package org.apache.commons.codec.language;

import java.util.Locale;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;

public class DoubleMetaphone implements StringEncoder {
   private static final String VOWELS = "AEIOUY";
   private static final String[] SILENT_START = new String[]{"GN", "KN", "PN", "WR", "PS"};
   private static final String[] L_R_N_M_B_H_F_V_W_SPACE = new String[]{"L", "R", "N", "M", "B", "H", "F", "V", "W", " "};
   private static final String[] ES_EP_EB_EL_EY_IB_IL_IN_IE_EI_ER = new String[]{"ES", "EP", "EB", "EL", "EY", "IB", "IL", "IN", "IE", "EI", "ER"};
   private static final String[] L_T_K_S_N_M_B_Z = new String[]{"L", "T", "K", "S", "N", "M", "B", "Z"};
   private int maxCodeLen = 4;

   public String doubleMetaphone(String value) {
      return this.doubleMetaphone(value, false);
   }

   public String doubleMetaphone(String value, boolean alternate) {
      value = this.cleanInput(value);
      if (value == null) {
         return null;
      } else {
         boolean slavoGermanic = this.isSlavoGermanic(value);
         int index = this.isSilentStart(value) ? 1 : 0;
         DoubleMetaphone.DoubleMetaphoneResult result = new DoubleMetaphone.DoubleMetaphoneResult(this.getMaxCodeLen());

         while(!result.isComplete() && index <= value.length() - 1) {
            switch(value.charAt(index)) {
            case 'A':
            case 'E':
            case 'I':
            case 'O':
            case 'U':
            case 'Y':
               index = this.handleAEIOUY(result, index);
               break;
            case 'B':
               result.append('P');
               index = this.charAt(value, index + 1) == 'B' ? index + 2 : index + 1;
               break;
            case 'C':
               index = this.handleC(value, result, index);
               break;
            case 'D':
               index = this.handleD(value, result, index);
               break;
            case 'F':
               result.append('F');
               index = this.charAt(value, index + 1) == 'F' ? index + 2 : index + 1;
               break;
            case 'G':
               index = this.handleG(value, result, index, slavoGermanic);
               break;
            case 'H':
               index = this.handleH(value, result, index);
               break;
            case 'J':
               index = this.handleJ(value, result, index, slavoGermanic);
               break;
            case 'K':
               result.append('K');
               index = this.charAt(value, index + 1) == 'K' ? index + 2 : index + 1;
               break;
            case 'L':
               index = this.handleL(value, result, index);
               break;
            case 'M':
               result.append('M');
               index = this.conditionM0(value, index) ? index + 2 : index + 1;
               break;
            case 'N':
               result.append('N');
               index = this.charAt(value, index + 1) == 'N' ? index + 2 : index + 1;
               break;
            case 'P':
               index = this.handleP(value, result, index);
               break;
            case 'Q':
               result.append('K');
               index = this.charAt(value, index + 1) == 'Q' ? index + 2 : index + 1;
               break;
            case 'R':
               index = this.handleR(value, result, index, slavoGermanic);
               break;
            case 'S':
               index = this.handleS(value, result, index, slavoGermanic);
               break;
            case 'T':
               index = this.handleT(value, result, index);
               break;
            case 'V':
               result.append('F');
               index = this.charAt(value, index + 1) == 'V' ? index + 2 : index + 1;
               break;
            case 'W':
               index = this.handleW(value, result, index);
               break;
            case 'X':
               index = this.handleX(value, result, index);
               break;
            case 'Z':
               index = this.handleZ(value, result, index, slavoGermanic);
               break;
            case 'Ç':
               result.append('S');
               ++index;
               break;
            case 'Ñ':
               result.append('N');
               ++index;
               break;
            default:
               ++index;
            }
         }

         return alternate ? result.getAlternate() : result.getPrimary();
      }
   }

   public Object encode(Object obj) throws EncoderException {
      if (!(obj instanceof String)) {
         throw new EncoderException("DoubleMetaphone encode parameter is not of type String");
      } else {
         return this.doubleMetaphone((String)obj);
      }
   }

   public String encode(String value) {
      return this.doubleMetaphone(value);
   }

   public boolean isDoubleMetaphoneEqual(String value1, String value2) {
      return this.isDoubleMetaphoneEqual(value1, value2, false);
   }

   public boolean isDoubleMetaphoneEqual(String value1, String value2, boolean alternate) {
      return this.doubleMetaphone(value1, alternate).equals(this.doubleMetaphone(value2, alternate));
   }

   public int getMaxCodeLen() {
      return this.maxCodeLen;
   }

   public void setMaxCodeLen(int maxCodeLen) {
      this.maxCodeLen = maxCodeLen;
   }

   private int handleAEIOUY(DoubleMetaphone.DoubleMetaphoneResult result, int index) {
      if (index == 0) {
         result.append('A');
      }

      return index + 1;
   }

   private int handleC(String value, DoubleMetaphone.DoubleMetaphoneResult result, int index) {
      if (this.conditionC0(value, index)) {
         result.append('K');
         index += 2;
      } else if (index == 0 && contains(value, index, 6, (String)"CAESAR")) {
         result.append('S');
         index += 2;
      } else if (contains(value, index, 2, (String)"CH")) {
         index = this.handleCH(value, result, index);
      } else if (contains(value, index, 2, (String)"CZ") && !contains(value, index - 2, 4, (String)"WICZ")) {
         result.append('S', 'X');
         index += 2;
      } else if (contains(value, index + 1, 3, (String)"CIA")) {
         result.append('X');
         index += 3;
      } else {
         if (contains(value, index, 2, (String)"CC") && (index != 1 || this.charAt(value, 0) != 'M')) {
            return this.handleCC(value, result, index);
         }

         if (contains(value, index, 2, "CK", "CG", "CQ")) {
            result.append('K');
            index += 2;
         } else if (contains(value, index, 2, "CI", "CE", "CY")) {
            if (contains(value, index, 3, "CIO", "CIE", "CIA")) {
               result.append('S', 'X');
            } else {
               result.append('S');
            }

            index += 2;
         } else {
            result.append('K');
            if (contains(value, index + 1, 2, " C", " Q", " G")) {
               index += 3;
            } else if (contains(value, index + 1, 1, "C", "K", "Q") && !contains(value, index + 1, 2, "CE", "CI")) {
               index += 2;
            } else {
               ++index;
            }
         }
      }

      return index;
   }

   private int handleCC(String value, DoubleMetaphone.DoubleMetaphoneResult result, int index) {
      if (contains(value, index + 2, 1, "I", "E", "H") && !contains(value, index + 2, 2, (String)"HU")) {
         if ((index != 1 || this.charAt(value, index - 1) != 'A') && !contains(value, index - 1, 5, "UCCEE", "UCCES")) {
            result.append('X');
         } else {
            result.append("KS");
         }

         index += 3;
      } else {
         result.append('K');
         index += 2;
      }

      return index;
   }

   private int handleCH(String value, DoubleMetaphone.DoubleMetaphoneResult result, int index) {
      if (index > 0 && contains(value, index, 4, (String)"CHAE")) {
         result.append('K', 'X');
         return index + 2;
      } else if (this.conditionCH0(value, index)) {
         result.append('K');
         return index + 2;
      } else if (this.conditionCH1(value, index)) {
         result.append('K');
         return index + 2;
      } else {
         if (index > 0) {
            if (contains(value, 0, 2, (String)"MC")) {
               result.append('K');
            } else {
               result.append('X', 'K');
            }
         } else {
            result.append('X');
         }

         return index + 2;
      }
   }

   private int handleD(String value, DoubleMetaphone.DoubleMetaphoneResult result, int index) {
      if (contains(value, index, 2, (String)"DG")) {
         if (contains(value, index + 2, 1, "I", "E", "Y")) {
            result.append('J');
            index += 3;
         } else {
            result.append("TK");
            index += 2;
         }
      } else if (contains(value, index, 2, "DT", "DD")) {
         result.append('T');
         index += 2;
      } else {
         result.append('T');
         ++index;
      }

      return index;
   }

   private int handleG(String value, DoubleMetaphone.DoubleMetaphoneResult result, int index, boolean slavoGermanic) {
      if (this.charAt(value, index + 1) == 'H') {
         index = this.handleGH(value, result, index);
      } else if (this.charAt(value, index + 1) == 'N') {
         if (index == 1 && this.isVowel(this.charAt(value, 0)) && !slavoGermanic) {
            result.append("KN", "N");
         } else if (!contains(value, index + 2, 2, (String)"EY") && this.charAt(value, index + 1) != 'Y' && !slavoGermanic) {
            result.append("N", "KN");
         } else {
            result.append("KN");
         }

         index += 2;
      } else if (contains(value, index + 1, 2, (String)"LI") && !slavoGermanic) {
         result.append("KL", "L");
         index += 2;
      } else if (index == 0 && (this.charAt(value, index + 1) == 'Y' || contains(value, index + 1, 2, (String[])ES_EP_EB_EL_EY_IB_IL_IN_IE_EI_ER))) {
         result.append('K', 'J');
         index += 2;
      } else if ((contains(value, index + 1, 2, (String)"ER") || this.charAt(value, index + 1) == 'Y') && !contains(value, 0, 6, "DANGER", "RANGER", "MANGER") && !contains(value, index - 1, 1, "E", "I") && !contains(value, index - 1, 3, "RGY", "OGY")) {
         result.append('K', 'J');
         index += 2;
      } else if (!contains(value, index + 1, 1, "E", "I", "Y") && !contains(value, index - 1, 4, "AGGI", "OGGI")) {
         if (this.charAt(value, index + 1) == 'G') {
            index += 2;
            result.append('K');
         } else {
            ++index;
            result.append('K');
         }
      } else {
         if (!contains(value, 0, 4, "VAN ", "VON ") && !contains(value, 0, 3, (String)"SCH") && !contains(value, index + 1, 2, (String)"ET")) {
            if (contains(value, index + 1, 3, (String)"IER")) {
               result.append('J');
            } else {
               result.append('J', 'K');
            }
         } else {
            result.append('K');
         }

         index += 2;
      }

      return index;
   }

   private int handleGH(String value, DoubleMetaphone.DoubleMetaphoneResult result, int index) {
      if (index > 0 && !this.isVowel(this.charAt(value, index - 1))) {
         result.append('K');
         index += 2;
      } else if (index == 0) {
         if (this.charAt(value, index + 2) == 'I') {
            result.append('J');
         } else {
            result.append('K');
         }

         index += 2;
      } else if (index > 1 && contains(value, index - 2, 1, "B", "H", "D") || index > 2 && contains(value, index - 3, 1, "B", "H", "D") || index > 3 && contains(value, index - 4, 1, "B", "H")) {
         index += 2;
      } else {
         if (index > 2 && this.charAt(value, index - 1) == 'U' && contains(value, index - 3, 1, "C", "G", "L", "R", "T")) {
            result.append('F');
         } else if (index > 0 && this.charAt(value, index - 1) != 'I') {
            result.append('K');
         }

         index += 2;
      }

      return index;
   }

   private int handleH(String value, DoubleMetaphone.DoubleMetaphoneResult result, int index) {
      if ((index == 0 || this.isVowel(this.charAt(value, index - 1))) && this.isVowel(this.charAt(value, index + 1))) {
         result.append('H');
         index += 2;
      } else {
         ++index;
      }

      return index;
   }

   private int handleJ(String value, DoubleMetaphone.DoubleMetaphoneResult result, int index, boolean slavoGermanic) {
      if (!contains(value, index, 4, (String)"JOSE") && !contains(value, 0, 4, (String)"SAN ")) {
         if (index == 0 && !contains(value, index, 4, (String)"JOSE")) {
            result.append('J', 'A');
         } else if (!this.isVowel(this.charAt(value, index - 1)) || slavoGermanic || this.charAt(value, index + 1) != 'A' && this.charAt(value, index + 1) != 'O') {
            if (index == value.length() - 1) {
               result.append('J', ' ');
            } else if (!contains(value, index + 1, 1, (String[])L_T_K_S_N_M_B_Z) && !contains(value, index - 1, 1, "S", "K", "L")) {
               result.append('J');
            }
         } else {
            result.append('J', 'H');
         }

         if (this.charAt(value, index + 1) == 'J') {
            index += 2;
         } else {
            ++index;
         }
      } else {
         if ((index != 0 || this.charAt(value, index + 4) != ' ') && value.length() != 4 && !contains(value, 0, 4, (String)"SAN ")) {
            result.append('J', 'H');
         } else {
            result.append('H');
         }

         ++index;
      }

      return index;
   }

   private int handleL(String value, DoubleMetaphone.DoubleMetaphoneResult result, int index) {
      if (this.charAt(value, index + 1) == 'L') {
         if (this.conditionL0(value, index)) {
            result.appendPrimary('L');
         } else {
            result.append('L');
         }

         index += 2;
      } else {
         ++index;
         result.append('L');
      }

      return index;
   }

   private int handleP(String value, DoubleMetaphone.DoubleMetaphoneResult result, int index) {
      if (this.charAt(value, index + 1) == 'H') {
         result.append('F');
         index += 2;
      } else {
         result.append('P');
         index = contains(value, index + 1, 1, "P", "B") ? index + 2 : index + 1;
      }

      return index;
   }

   private int handleR(String value, DoubleMetaphone.DoubleMetaphoneResult result, int index, boolean slavoGermanic) {
      if (index == value.length() - 1 && !slavoGermanic && contains(value, index - 2, 2, (String)"IE") && !contains(value, index - 4, 2, "ME", "MA")) {
         result.appendAlternate('R');
      } else {
         result.append('R');
      }

      return this.charAt(value, index + 1) == 'R' ? index + 2 : index + 1;
   }

   private int handleS(String value, DoubleMetaphone.DoubleMetaphoneResult result, int index, boolean slavoGermanic) {
      if (contains(value, index - 1, 3, "ISL", "YSL")) {
         ++index;
      } else if (index == 0 && contains(value, index, 5, (String)"SUGAR")) {
         result.append('X', 'S');
         ++index;
      } else if (contains(value, index, 2, (String)"SH")) {
         if (contains(value, index + 1, 4, "HEIM", "HOEK", "HOLM", "HOLZ")) {
            result.append('S');
         } else {
            result.append('X');
         }

         index += 2;
      } else if (!contains(value, index, 3, "SIO", "SIA") && !contains(value, index, 4, (String)"SIAN")) {
         if ((index != 0 || !contains(value, index + 1, 1, "M", "N", "L", "W")) && !contains(value, index + 1, 1, (String)"Z")) {
            if (contains(value, index, 2, (String)"SC")) {
               index = this.handleSC(value, result, index);
            } else {
               if (index == value.length() - 1 && contains(value, index - 2, 2, "AI", "OI")) {
                  result.appendAlternate('S');
               } else {
                  result.append('S');
               }

               index = contains(value, index + 1, 1, "S", "Z") ? index + 2 : index + 1;
            }
         } else {
            result.append('S', 'X');
            index = contains(value, index + 1, 1, (String)"Z") ? index + 2 : index + 1;
         }
      } else {
         if (slavoGermanic) {
            result.append('S');
         } else {
            result.append('S', 'X');
         }

         index += 3;
      }

      return index;
   }

   private int handleSC(String value, DoubleMetaphone.DoubleMetaphoneResult result, int index) {
      if (this.charAt(value, index + 2) == 'H') {
         if (contains(value, index + 3, 2, "OO", "ER", "EN", "UY", "ED", "EM")) {
            if (contains(value, index + 3, 2, "ER", "EN")) {
               result.append("X", "SK");
            } else {
               result.append("SK");
            }
         } else if (index == 0 && !this.isVowel(this.charAt(value, 3)) && this.charAt(value, 3) != 'W') {
            result.append('X', 'S');
         } else {
            result.append('X');
         }
      } else if (contains(value, index + 2, 1, "I", "E", "Y")) {
         result.append('S');
      } else {
         result.append("SK");
      }

      return index + 3;
   }

   private int handleT(String value, DoubleMetaphone.DoubleMetaphoneResult result, int index) {
      if (contains(value, index, 4, (String)"TION")) {
         result.append('X');
         index += 3;
      } else if (contains(value, index, 3, "TIA", "TCH")) {
         result.append('X');
         index += 3;
      } else if (!contains(value, index, 2, (String)"TH") && !contains(value, index, 3, (String)"TTH")) {
         result.append('T');
         index = contains(value, index + 1, 1, "T", "D") ? index + 2 : index + 1;
      } else {
         if (!contains(value, index + 2, 2, "OM", "AM") && !contains(value, 0, 4, "VAN ", "VON ") && !contains(value, 0, 3, (String)"SCH")) {
            result.append('0', 'T');
         } else {
            result.append('T');
         }

         index += 2;
      }

      return index;
   }

   private int handleW(String value, DoubleMetaphone.DoubleMetaphoneResult result, int index) {
      if (contains(value, index, 2, (String)"WR")) {
         result.append('R');
         index += 2;
      } else if (index == 0 && (this.isVowel(this.charAt(value, index + 1)) || contains(value, index, 2, (String)"WH"))) {
         if (this.isVowel(this.charAt(value, index + 1))) {
            result.append('A', 'F');
         } else {
            result.append('A');
         }

         ++index;
      } else if ((index != value.length() - 1 || !this.isVowel(this.charAt(value, index - 1))) && !contains(value, index - 1, 5, "EWSKI", "EWSKY", "OWSKI", "OWSKY") && !contains(value, 0, 3, (String)"SCH")) {
         if (contains(value, index, 4, "WICZ", "WITZ")) {
            result.append("TS", "FX");
            index += 4;
         } else {
            ++index;
         }
      } else {
         result.appendAlternate('F');
         ++index;
      }

      return index;
   }

   private int handleX(String value, DoubleMetaphone.DoubleMetaphoneResult result, int index) {
      if (index == 0) {
         result.append('S');
         ++index;
      } else {
         if (index != value.length() - 1 || !contains(value, index - 3, 3, "IAU", "EAU") && !contains(value, index - 2, 2, "AU", "OU")) {
            result.append("KS");
         }

         index = contains(value, index + 1, 1, "C", "X") ? index + 2 : index + 1;
      }

      return index;
   }

   private int handleZ(String value, DoubleMetaphone.DoubleMetaphoneResult result, int index, boolean slavoGermanic) {
      if (this.charAt(value, index + 1) == 'H') {
         result.append('J');
         index += 2;
      } else {
         if (!contains(value, index + 1, 2, "ZO", "ZI", "ZA") && (!slavoGermanic || index <= 0 || this.charAt(value, index - 1) == 'T')) {
            result.append('S');
         } else {
            result.append("S", "TS");
         }

         index = this.charAt(value, index + 1) == 'Z' ? index + 2 : index + 1;
      }

      return index;
   }

   private boolean conditionC0(String value, int index) {
      if (contains(value, index, 4, (String)"CHIA")) {
         return true;
      } else if (index <= 1) {
         return false;
      } else if (this.isVowel(this.charAt(value, index - 2))) {
         return false;
      } else if (!contains(value, index - 1, 3, (String)"ACH")) {
         return false;
      } else {
         char c = this.charAt(value, index + 2);
         return c != 'I' && c != 'E' || contains(value, index - 2, 6, "BACHER", "MACHER");
      }
   }

   private boolean conditionCH0(String value, int index) {
      if (index != 0) {
         return false;
      } else if (!contains(value, index + 1, 5, "HARAC", "HARIS") && !contains(value, index + 1, 3, "HOR", "HYM", "HIA", "HEM")) {
         return false;
      } else {
         return !contains(value, 0, 5, (String)"CHORE");
      }
   }

   private boolean conditionCH1(String value, int index) {
      return contains(value, 0, 4, "VAN ", "VON ") || contains(value, 0, 3, (String)"SCH") || contains(value, index - 2, 6, "ORCHES", "ARCHIT", "ORCHID") || contains(value, index + 2, 1, "T", "S") || (contains(value, index - 1, 1, "A", "O", "U", "E") || index == 0) && (contains(value, index + 2, 1, (String[])L_R_N_M_B_H_F_V_W_SPACE) || index + 1 == value.length() - 1);
   }

   private boolean conditionL0(String value, int index) {
      if (index == value.length() - 3 && contains(value, index - 1, 4, "ILLO", "ILLA", "ALLE")) {
         return true;
      } else {
         return (contains(value, value.length() - 2, 2, "AS", "OS") || contains(value, value.length() - 1, 1, "A", "O")) && contains(value, index - 1, 4, (String)"ALLE");
      }
   }

   private boolean conditionM0(String value, int index) {
      if (this.charAt(value, index + 1) == 'M') {
         return true;
      } else {
         return contains(value, index - 1, 3, (String)"UMB") && (index + 1 == value.length() - 1 || contains(value, index + 2, 2, (String)"ER"));
      }
   }

   private boolean isSlavoGermanic(String value) {
      return value.indexOf(87) > -1 || value.indexOf(75) > -1 || value.indexOf("CZ") > -1 || value.indexOf("WITZ") > -1;
   }

   private boolean isVowel(char ch) {
      return "AEIOUY".indexOf(ch) != -1;
   }

   private boolean isSilentStart(String value) {
      boolean result = false;
      String[] arr$ = SILENT_START;
      int len$ = arr$.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         String element = arr$[i$];
         if (value.startsWith(element)) {
            result = true;
            break;
         }
      }

      return result;
   }

   private String cleanInput(String input) {
      if (input == null) {
         return null;
      } else {
         input = input.trim();
         return input.length() == 0 ? null : input.toUpperCase(Locale.ENGLISH);
      }
   }

   protected char charAt(String value, int index) {
      return index >= 0 && index < value.length() ? value.charAt(index) : '\u0000';
   }

   private static boolean contains(String value, int start, int length, String criteria) {
      return contains(value, start, length, new String[]{criteria});
   }

   private static boolean contains(String value, int start, int length, String criteria1, String criteria2) {
      return contains(value, start, length, new String[]{criteria1, criteria2});
   }

   private static boolean contains(String value, int start, int length, String criteria1, String criteria2, String criteria3) {
      return contains(value, start, length, new String[]{criteria1, criteria2, criteria3});
   }

   private static boolean contains(String value, int start, int length, String criteria1, String criteria2, String criteria3, String criteria4) {
      return contains(value, start, length, new String[]{criteria1, criteria2, criteria3, criteria4});
   }

   private static boolean contains(String value, int start, int length, String criteria1, String criteria2, String criteria3, String criteria4, String criteria5) {
      return contains(value, start, length, new String[]{criteria1, criteria2, criteria3, criteria4, criteria5});
   }

   private static boolean contains(String value, int start, int length, String criteria1, String criteria2, String criteria3, String criteria4, String criteria5, String criteria6) {
      return contains(value, start, length, new String[]{criteria1, criteria2, criteria3, criteria4, criteria5, criteria6});
   }

   protected static boolean contains(String value, int start, int length, String[] criteria) {
      boolean result = false;
      if (start >= 0 && start + length <= value.length()) {
         String target = value.substring(start, start + length);
         String[] arr$ = criteria;
         int len$ = criteria.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            String element = arr$[i$];
            if (target.equals(element)) {
               result = true;
               break;
            }
         }
      }

      return result;
   }

   public class DoubleMetaphoneResult {
      private final StringBuilder primary = new StringBuilder(DoubleMetaphone.this.getMaxCodeLen());
      private final StringBuilder alternate = new StringBuilder(DoubleMetaphone.this.getMaxCodeLen());
      private final int maxLength;

      public DoubleMetaphoneResult(int maxLength) {
         this.maxLength = maxLength;
      }

      public void append(char value) {
         this.appendPrimary(value);
         this.appendAlternate(value);
      }

      public void append(char primary, char alternate) {
         this.appendPrimary(primary);
         this.appendAlternate(alternate);
      }

      public void appendPrimary(char value) {
         if (this.primary.length() < this.maxLength) {
            this.primary.append(value);
         }

      }

      public void appendAlternate(char value) {
         if (this.alternate.length() < this.maxLength) {
            this.alternate.append(value);
         }

      }

      public void append(String value) {
         this.appendPrimary(value);
         this.appendAlternate(value);
      }

      public void append(String primary, String alternate) {
         this.appendPrimary(primary);
         this.appendAlternate(alternate);
      }

      public void appendPrimary(String value) {
         int addChars = this.maxLength - this.primary.length();
         if (value.length() <= addChars) {
            this.primary.append(value);
         } else {
            this.primary.append(value.substring(0, addChars));
         }

      }

      public void appendAlternate(String value) {
         int addChars = this.maxLength - this.alternate.length();
         if (value.length() <= addChars) {
            this.alternate.append(value);
         } else {
            this.alternate.append(value.substring(0, addChars));
         }

      }

      public String getPrimary() {
         return this.primary.toString();
      }

      public String getAlternate() {
         return this.alternate.toString();
      }

      public boolean isComplete() {
         return this.primary.length() >= this.maxLength && this.alternate.length() >= this.maxLength;
      }
   }
}
