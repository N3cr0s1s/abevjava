package hu.piller.enykp.alogic.settingspanel.setenv;

public class SetenvSerializer {
   private static final int NUM_PARAMS = 3;
   private static final int NAME = 0;
   private static final int VALUE = 1;
   String os;

   public SetenvSerializer() {
      String var1 = System.getProperty("os.name").toLowerCase();
      if (var1.indexOf("windows") != -1) {
         this.os = "win";
      } else {
         this.os = "unix";
      }

   }

   public String[] ser(Setenv var1) {
      String[] var2 = new String[3];
      String var3 = this.os.equals("win") ? "@SET " : "";
      String var4 = this.os.equals("win") ? "" : "\"";
      var2[0] = var3 + "MEMORY_OPTS=" + var4 + "-Xms" + var1.getMemMin() + "m -Xmx" + var1.getMemMax() + "m" + var4;
      var2[1] = var3 + "TUNING_OPTS=" + var4 + var1.getTuningOpts() + var4;
      var2[2] = var3 + "XML_OPTS=" + var4 + var1.getXmlOpts() + var4;
      return var2;
   }

   public Setenv deser(String[] var1) throws SetenvException {
      Setenv var2 = new Setenv();
      String[] var3 = var1;
      int var4 = var1.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         String var6 = var3[var5];
         if (this.os.equals("win")) {
            var6 = var6.replaceAll("@SET ", "");
         }

         String[] var7 = var6.trim().split("=", 2);
         if (this.os.equals("unix") && this.hasValue(var7)) {
            if (var7[1].charAt(0) == '"') {
               var7[1] = var7[1].substring(1);
            }

            if (var7[1].charAt(var7[1].length() - 1) == '"') {
               var7[1] = var7[1].substring(0, var7[1].length() - 1);
            }
         }

         if (var7[0].equals("MEMORY_OPTS")) {
            if (this.hasValue(var7)) {
               int var8 = var7[1].indexOf("-Xms");
               int var9 = var7[1].indexOf("m", var8 + 4);
               var2.setMemMin(Integer.parseInt(var7[1].substring(var8 + 4, var9)));
               int var10 = var7[1].indexOf("-Xmx");
               var9 = var7[1].indexOf("m", var10 + 4);
               var2.setMemMax(Integer.parseInt(var7[1].substring(var10 + 4, var9)));
            }
         } else if (var7[0].equals("TUNING_OPTS")) {
            if (this.hasValue(var7)) {
               var2.setTuningOpts(var7[1]);
            }
         } else {
            if (!var7[0].equals("XML_OPTS")) {
               throw new SetenvException("Ismeretlen paraméter: " + var7[0]);
            }

            if (this.hasValue(var7)) {
               var2.setXmlOpts(var7[1]);
            }
         }
      }

      return var2;
   }

   private boolean hasValue(String[] var1) {
      return var1.length > 1 && var1[1] != null && !"".equals(var1[1].trim());
   }
}
