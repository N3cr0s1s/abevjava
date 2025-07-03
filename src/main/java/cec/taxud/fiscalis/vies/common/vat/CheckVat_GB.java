package cec.taxud.fiscalis.vies.common.vat;

public class CheckVat_GB implements ValidationRoutine {
   static final int LENGTH = 9;
   static final int LENGTH_GOV = 5;

   private boolean checkSumOk(String var1) {
      int var2 = StringUtils.substrToInt(var1, 7, 9);
      int var3 = StringUtils.substrToInt(var1, 0, 7);
      if (var2 > 96) {
         return false;
      } else {
         int var4 = 0;

         for(int var5 = 0; var5 < 7; ++var5) {
            var4 += (8 - var5) * StringUtils.digitAt(var1, var5);
         }

         boolean var7 = (var4 + var2) % 97 == 0 && (var3 < 100000 || var3 > 999999) && (var3 < 9490001 || var3 > 9700000) && (var3 < 9990001 || var3 > 9999999);
         boolean var6 = (var4 + var2 + 55) % 97 == 0 && (var3 < 1 || var3 > 32768) && (var3 < 32769 || var3 > 1000000);
         return var7 ^ var6;
      }
   }

   private boolean checkSumGovOk(String var1) {
      int var2 = StringUtils.substrToInt(var1, 2);
      if (var1.startsWith("GD")) {
         return var2 >= 0 && var2 <= 499;
      } else if (!var1.startsWith("HA")) {
         return false;
      } else {
         return var2 >= 500 && var2 <= 999;
      }
   }

   public boolean checkIsleOfMan(String var1) {
      if (var1.charAt(0) == '0') {
         return var1.charAt(1) == '0';
      } else {
         return true;
      }
   }

   public boolean check(String var1) {
      if (var1.length() == 5) {
         return StringUtils.isNum(var1.substring(2)) && this.checkSumGovOk(var1);
      } else if (var1.length() != 9 && (var1.length() != 12 || var1.endsWith("000"))) {
         return false;
      } else if (var1.startsWith("000000000")) {
         return false;
      } else {
         return StringUtils.isNum(var1) && this.checkIsleOfMan(var1) && this.checkSumOk(var1);
      }
   }
}
