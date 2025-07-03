package hu.piller.enykp.util.base;

public class Version implements Comparable {
   /** @deprecated */
   public static final String VSEPARATOR = "-v";
   public static final String VPREFIX = "v";
   public static final String NSEPARATOR = "\\.";
   public static final String MINVALUE = "0.0.0.0";
   public static final String MAXVALUE = "999.999.999.999";
   public static final String RELEASE_SEPARATOR = "-";
   private String type;
   private String numstr;
   private long value;
   private boolean has_postfix;
   private String postfix;

   public Version(String var1) throws NumberFormatException {
      if (var1 != null && var1.length() != 0) {
         String[] var2 = var1.split("-");
         this.parseValue(var2[0]);
         if (var2.length > 1) {
            this.has_postfix = true;
            this.postfix = var1.substring(var1.indexOf("-"));
         }

      } else {
         throw new NumberFormatException();
      }
   }

   /** @deprecated */
   public long toValue(String var1) throws NumberFormatException {
      String[] var2 = var1.split("\\.");
      long var3 = 0L;
      int var7 = 1;

      for(int var8 = var2.length - 1; var8 >= 0; --var8) {
         String var9 = var2[var8];
         long var5 = Long.parseLong(var9);
         var3 += var5 * (long)var7;
         var7 *= 1000;
      }

      return var3;
   }

   public boolean isTestVersion() {
      return this.has_postfix;
   }

   /** @deprecated */
   public long getValue() {
      return this.value;
   }

   /** @deprecated */
   public Long getObjValue() {
      return new Long(this.value);
   }

   public String getType() {
      return this.type;
   }

   public String getNumstr() {
      return this.numstr;
   }

   public int compareTo(Object var1) {
      if (var1 != null && var1 instanceof Version) {
         Version var2 = (Version)var1;
         String[] var3 = this.getNumstr().split("\\.");
         String[] var4 = var2.getNumstr().split("\\.");

         int var5;
         for(var5 = 0; var5 < var3.length && var5 < var4.length; ++var5) {
            int var6 = Integer.parseInt(var3[var5]);
            int var7 = Integer.parseInt(var4[var5]);
            if (var6 != var7) {
               return var6 - var7;
            }
         }

         if (var3.length > var4.length) {
            while(var5 < var3.length) {
               if (Integer.parseInt(var3[var5]) > 0) {
                  return 1;
               }

               ++var5;
            }
         } else if (var3.length < var4.length) {
            while(var5 < var4.length) {
               if (Integer.parseInt(var4[var5]) > 0) {
                  return -1;
               }

               ++var5;
            }
         }

         if (this.has_postfix && !var2.has_postfix) {
            return -1;
         } else {
            return !this.has_postfix && var2.has_postfix ? 1 : 0;
         }
      } else {
         throw new ClassCastException();
      }
   }

   public boolean equals(Object var1) {
      if (var1 != null && var1 instanceof Version) {
         return this.compareTo(var1) == 0;
      } else {
         throw new ClassCastException();
      }
   }

   public String toString() {
      if (this.numstr == null) {
         return "";
      } else if (this.type.length() == 0) {
         return "v" + this.numstr;
      } else {
         return !this.has_postfix ? this.type + "-v" + this.numstr : this.type + "-v" + this.numstr + this.postfix;
      }
   }

   private void parseValue(String var1) {
      int var2 = var1.lastIndexOf("-v");
      if (var2 == -1) {
         this.type = "";
         if (var1.substring(0, 1).compareTo("v") == 0) {
            this.numstr = var1.substring(1);
         } else {
            this.numstr = var1;
         }
      } else {
         this.type = var1.substring(0, var2);
         this.numstr = var1.substring(var2 + "-v".length());
      }

      this.value = this.toValue(this.numstr);
   }
}
