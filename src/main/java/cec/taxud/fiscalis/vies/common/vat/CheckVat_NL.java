package cec.taxud.fiscalis.vies.common.vat;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class CheckVat_NL implements ValidationRoutine {
   static final int LENGTH = 12;
   private static final Map<String, String> CAPITAL_LETTERS_TO_INT = new HashMap();

   private boolean modulo11Check(String var1) {
      if (!StringUtils.isNum(var1.substring(0, 9))) {
         return false;
      } else if (var1.charAt(9) != 'B') {
         return false;
      } else if (Character.isDigit(var1.charAt(10)) && Character.isDigit(var1.charAt(11))) {
         if (StringUtils.substrToInt(var1, 10) < 1) {
            return false;
         } else {
            int var2 = 0;

            int var3;
            for(var3 = 0; var3 < 8; ++var3) {
               var2 += (9 - var3) * StringUtils.digitAt(var1, var3);
            }

            var3 = var2 % 11;
            if (var3 == 10) {
               return false;
            } else {
               return StringUtils.digitAt(var1, 8) == var3;
            }
         }
      } else {
         return false;
      }
   }

   private boolean modulo97Check(String var1) {
      if (!var1.startsWith("NL")) {
         return false;
      } else {
         String var2 = var1.substring(12, 13);
         if (!StringUtils.isNum(var2)) {
            return false;
         } else {
            int var3 = StringUtils.digitAt(var1, 12) * 10 + StringUtils.digitAt(var1, 13);
            if (2 <= var3 && var3 <= 98) {
               String var4 = var1;

               String var7;
               String var8;
               for(Iterator var5 = CAPITAL_LETTERS_TO_INT.entrySet().iterator(); var5.hasNext(); var4 = var4.replace(var7, var8)) {
                  Entry var6 = (Entry)var5.next();
                  var7 = (String)var6.getKey();
                  var8 = (String)var6.getValue();
               }

               return !StringUtils.isNum(var4) ? false : (new BigInteger(var4)).mod(new BigInteger("97")).equals(new BigInteger("1"));
            } else {
               return false;
            }
         }
      }
   }

   public boolean check(String var1) {
      String var2 = "NL" + var1;
      return var1.length() == 12 && (this.modulo11Check(var1) || this.modulo97Check(var2));
   }

   static {
      CAPITAL_LETTERS_TO_INT.put("A", "10");
      CAPITAL_LETTERS_TO_INT.put("B", "11");
      CAPITAL_LETTERS_TO_INT.put("C", "12");
      CAPITAL_LETTERS_TO_INT.put("D", "13");
      CAPITAL_LETTERS_TO_INT.put("E", "14");
      CAPITAL_LETTERS_TO_INT.put("F", "15");
      CAPITAL_LETTERS_TO_INT.put("G", "16");
      CAPITAL_LETTERS_TO_INT.put("H", "17");
      CAPITAL_LETTERS_TO_INT.put("I", "18");
      CAPITAL_LETTERS_TO_INT.put("J", "19");
      CAPITAL_LETTERS_TO_INT.put("K", "20");
      CAPITAL_LETTERS_TO_INT.put("L", "21");
      CAPITAL_LETTERS_TO_INT.put("M", "22");
      CAPITAL_LETTERS_TO_INT.put("N", "23");
      CAPITAL_LETTERS_TO_INT.put("O", "24");
      CAPITAL_LETTERS_TO_INT.put("P", "25");
      CAPITAL_LETTERS_TO_INT.put("Q", "26");
      CAPITAL_LETTERS_TO_INT.put("R", "27");
      CAPITAL_LETTERS_TO_INT.put("S", "28");
      CAPITAL_LETTERS_TO_INT.put("T", "29");
      CAPITAL_LETTERS_TO_INT.put("U", "30");
      CAPITAL_LETTERS_TO_INT.put("V", "31");
      CAPITAL_LETTERS_TO_INT.put("W", "32");
      CAPITAL_LETTERS_TO_INT.put("X", "33");
      CAPITAL_LETTERS_TO_INT.put("Y", "34");
      CAPITAL_LETTERS_TO_INT.put("Z", "35");
      CAPITAL_LETTERS_TO_INT.put("+", "36");
      CAPITAL_LETTERS_TO_INT.put("*", "37");
   }
}
