package hu.piller.enykp.alogic.filesaver.xml;

import hu.piller.enykp.datastore.StoreItem;
import java.util.Comparator;

public class StoreItemComparator implements Comparator {
   private static final String ABC = " 0123456789aAáÁbBcCdDeEéÉfFgGhHiIíÍjJkKlLmMnNoOóÓöÖőŐpPqQrRsStTuUúÚüÜűŰvVwWxXyYzZ";

   public int compare(Object var1, Object var2) {
      StoreItem var3 = (StoreItem)var1;
      StoreItem var4 = (StoreItem)var2;
      String var5 = "000" + (var3.index + 1);
      String var6 = var3.code;
      if (var6.indexOf("XXXX") > -1) {
         var6 = var6.substring(0, 2) + var5.substring(var5.length() - 4) + var6.substring(6);
      }

      var5 = "000" + (var4.index + 1);
      String var7 = var4.code;
      if (var7.indexOf("XXXX") > -1) {
         var7 = var7.substring(0, 2) + var5.substring(var5.length() - 4) + var7.substring(6);
      }

      boolean var8 = false;
      int var9 = 0;
      int var10 = 0;

      for(int var11 = 0; !var8; ++var11) {
         try {
            var9 += " 0123456789aAáÁbBcCdDeEéÉfFgGhHiIíÍjJkKlLmMnNoOóÓöÖőŐpPqQrRsStTuUúÚüÜűŰvVwWxXyYzZ".indexOf(var6.substring(var11, var11 + 1));
         } catch (Exception var14) {
            var8 = true;
         }

         try {
            var10 += " 0123456789aAáÁbBcCdDeEéÉfFgGhHiIíÍjJkKlLmMnNoOóÓöÖőŐpPqQrRsStTuUúÚüÜűŰvVwWxXyYzZ".indexOf(var7.substring(var11, var11 + 1));
         } catch (Exception var13) {
            var8 = true;
         }

         if (var9 != var10) {
            var8 = true;
         }
      }

      return var9 - var10;
   }
}
