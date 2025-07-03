package hu.piller.enykp.kauclient.simplified;

import hu.piller.enykp.kauclient.IKauClient;
import hu.piller.enykp.kauclient.KauClientException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public abstract class BaseKauClient implements IKauClient {
   protected byte[] readAllBytesFromInputStream(InputStream var1) throws IOException {
      ByteArrayOutputStream var2 = new ByteArrayOutputStream();
      byte[] var3 = new byte['\uffff'];

      int var4;
      while((var4 = var1.read(var3)) != -1) {
         var2.write(var3, 0, var4);
      }

      var2.flush();
      return var2.toByteArray();
   }

   protected String extractHostFromUrl(String var1) throws KauClientException {
      try {
         return (new URL(var1)).getHost();
      } catch (MalformedURLException var4) {
         String var3 = String.format("hibás felépítésű URL '%s'", var1);
         throw new KauClientException(var3);
      }
   }

   protected boolean hasInvalidCredentials(Document var1) {
      Elements var2 = var1.select("p[class$=help-block]");

      int var3;
      String var4;
      for(var3 = 0; var2 != null && var3 < var2.size(); ++var3) {
         var4 = ((Element)var2.get(var3)).text();
         if (var4 != null && var4.indexOf("Nem megfelel") != -1) {
            return true;
         }
      }

      var2 = var1.select("dap-ds-feedback[type$=negative]");

      for(var3 = 0; var2 != null && var3 < var2.size(); ++var3) {
         var4 = ((Element)var2.get(var3)).text();
         if (var4 != null && var4.indexOf("Nem megfelel") != -1) {
            return true;
         }
      }

      var2 = var1.select("div.feedback.feedback--negative");

      for(var3 = 0; var2 != null && var3 < var2.size(); ++var3) {
         var4 = ((Element)var2.get(var3)).text();
         if (var4 != null && var4.indexOf("helytelen, vagy") != -1) {
            return true;
         }
      }

      return false;
   }
}
