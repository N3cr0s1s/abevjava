package hu.piller.enykp.gui.component;

import java.util.Comparator;

public class HunCharComparator implements Comparator {
   private static final String ABC = "| !\"0123456789aAáÁbBcCdDeEéÉfFgGhHiIíÍjJkKlLmMnNoOóÓöÖőŐpPqQrRsStTuUúÚüÜűŰvVwWxXyYzZ<>#&@{},;.:-_%/\\=()[]*'$";
   public static final String HCC_DELIMITER = "|#&@&#|";

   public int compare(Object var1, Object var2) {
      String var3 = (String)var1;
      String var4 = (String)var2;
      boolean var5 = false;
      int var6 = 0;
      int var7 = 0;

      for(int var8 = 0; !var5; ++var8) {
         try {
            var6 += "| !\"0123456789aAáÁbBcCdDeEéÉfFgGhHiIíÍjJkKlLmMnNoOóÓöÖőŐpPqQrRsStTuUúÚüÜűŰvVwWxXyYzZ<>#&@{},;.:-_%/\\=()[]*'$".indexOf(var3.substring(var8, var8 + 1)) + 1;
         } catch (Exception var11) {
            var5 = true;
         }

         try {
            var7 += "| !\"0123456789aAáÁbBcCdDeEéÉfFgGhHiIíÍjJkKlLmMnNoOóÓöÖőŐpPqQrRsStTuUúÚüÜűŰvVwWxXyYzZ<>#&@{},;.:-_%/\\=()[]*'$".indexOf(var4.substring(var8, var8 + 1)) + 1;
         } catch (Exception var10) {
            var5 = true;
         }

         if (var6 != var7) {
            var5 = true;
         }
      }

      return var6 - var7;
   }
}
