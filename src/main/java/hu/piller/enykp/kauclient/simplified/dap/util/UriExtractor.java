package hu.piller.enykp.kauclient.simplified.dap.util;

public class UriExtractor {
   public static String extractFromJScript(String var0) {
      String var1 = null;
      int var2 = var0.indexOf("let uri = \"");
      if (var2 > 0) {
         int var3 = var2 + "let uri = ".length() + 1;
         int var4 = var0.indexOf("\"", var3 + 1);
         var1 = var0.substring(var3, var4);
      }

      return var1;
   }
}
