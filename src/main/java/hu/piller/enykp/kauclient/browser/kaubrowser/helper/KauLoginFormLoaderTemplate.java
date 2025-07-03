package hu.piller.enykp.kauclient.browser.kaubrowser.helper;

import hu.piller.enykp.util.FileLoader;
import hu.piller.enykp.util.base.ErrorList;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class KauLoginFormLoaderTemplate {
   private String template;

   public KauLoginFormLoaderTemplate(String var1) {
      this.loadTemplate(var1);
   }

   public String buildLoginFormLoaderWithAuthTokens(Map<String, String> var1) {
      String var2 = this.template;
      Entry var4;
      if (var2 != null && var1 != null) {
         for(Iterator var3 = var1.entrySet().iterator(); var3.hasNext(); var2 = var2.replaceAll("\\{" + (String)var4.getKey() + "\\}", (String)var4.getValue())) {
            var4 = (Entry)var3.next();
         }
      }

      return var2;
   }

   private void loadTemplate(String var1) {
      try {
         this.template = FileLoader.loadTextFileFromJar("/resources/niszws", var1);
      } catch (IOException var3) {
         ErrorList.getInstance().writeError(1000, "A KAĂś azonosĂ\u00adtĂˇst elindĂ\u00adtĂł oldal nem tĂ¶lthetĹ‘ be", var3, (Object)null);
      }

   }
}
