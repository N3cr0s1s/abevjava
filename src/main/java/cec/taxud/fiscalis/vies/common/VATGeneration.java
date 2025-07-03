package cec.taxud.fiscalis.vies.common;

import java.text.DecimalFormat;

public class VATGeneration {
   public static void GenerateVATNumbers(String var0, String var1) {
      int var2 = 200;
      String var3 = "";

      for(int var4 = 0; var4 < var1.length(); ++var4) {
         var3 = var3 + "0";
      }

      DecimalFormat var8 = new DecimalFormat(var3);

      for(long var5 = Long.parseLong(var1); var5 < Long.MAX_VALUE && var2 > 0; ++var5) {
         String var7 = var8.format(var5);
         if (VATValidation.check(var7, var0)) {
            --var2;
            System.out.println("MS: '" + var0 + " , VATNumber: '" + var7);
         }
      }

   }

   public static void main(String[] var0) {
      GenerateVATNumbers("BE", "0897731535");
   }
}
