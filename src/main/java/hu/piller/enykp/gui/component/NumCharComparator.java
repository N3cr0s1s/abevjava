package hu.piller.enykp.gui.component;

import hu.piller.enykp.util.base.Tools;
import java.util.Comparator;

public class NumCharComparator implements Comparator {
   public static final String HCC_DELIMITER = "|#&@&#|";
   private String numDelimiter = "";
   public boolean hasNumberException = false;

   public int compare(Object var1, Object var2) {
      String var3 = (String)var1;
      String var4 = (String)var2;

      try {
         int var5;
         if (var3.indexOf("|#&@&#|") > -1) {
            var5 = this.startsWithNumber(var3.substring(0, var3.indexOf("|#&@&#|")));
         } else {
            var5 = this.startsWithNumber(var3);
         }

         int var6;
         if (var4.indexOf("|#&@&#|") > -1) {
            var6 = this.startsWithNumber(var4.substring(0, var4.indexOf("|#&@&#|")));
         } else {
            var6 = this.startsWithNumber(var4);
         }

         return var5 - var6;
      } catch (Exception var7) {
         if (var7 instanceof NumberFormatException) {
            this.hasNumberException = true;
         }

         return 0;
      }
   }

   public boolean setNumDelimiter(String var1) {
      if (var1.equals("")) {
         this.numDelimiter = "";
         return true;
      } else {
         if (var1.indexOf("|#&@&#|") > -1) {
            var1 = var1.substring(0, var1.indexOf("|#&@&#|"));
         }

         int var2;
         try {
            var2 = Integer.parseInt(var1);
            this.numDelimiter = "";
            return true;
         } catch (NumberFormatException var8) {
            Tools.eLog(var8, 0);
            var1 = var1.trim();
            var2 = var1.indexOf(" ");
            String var3;
            if (var2 > -1) {
               var3 = var1.substring(0, var2);

               try {
                  var2 = Integer.parseInt(var3);
                  this.numDelimiter = " ";
                  return true;
               } catch (NumberFormatException var7) {
                  Tools.eLog(var7, 0);
               }
            }

            var2 = var1.indexOf("-");
            if (var2 > -1) {
               var3 = var1.substring(0, var2);

               try {
                  var2 = Integer.parseInt(var3);
                  this.numDelimiter = "-";
                  return true;
               } catch (NumberFormatException var6) {
                  Tools.eLog(var6, 0);
               }
            }

            var2 = var1.indexOf("=");
            if (var2 > -1) {
               var3 = var1.substring(0, var2);

               try {
                  var2 = Integer.parseInt(var3);
                  this.numDelimiter = "=";
                  return true;
               } catch (NumberFormatException var5) {
                  Tools.eLog(var5, 0);
               }
            }

            return false;
         }
      }
   }

   private int startsWithNumber(String var1) {
      if (var1.equals("")) {
         return -1;
      } else {
         var1 = var1.trim();
         int var2 = this.numDelimiter.equals("") ? var1.length() : var1.indexOf(this.numDelimiter);
         var1 = var1.substring(0, var2);
         var2 = Integer.parseInt(var1);
         return var2;
      }
   }
}
