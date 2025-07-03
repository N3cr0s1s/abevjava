package hu.piller.enykp.alogic.templateutils.blacklist.provider;

import hu.piller.enykp.util.httpclient.HttpClientFactory;
import hu.piller.enykp.util.httpclient.HttpClientFactoryException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

public class BlacklistProviderRemote implements BlacklistProvider {
   private URL url;

   public BlacklistProviderRemote(URL var1) {
      this.url = var1;
   }

   public String get() throws BlacklistProviderException {
      String var1 = null;
      HttpClient var2 = null;
      if (this.url == null) {
         throw new BlacklistProviderException("Az ÁNYK-ból kitiltott nyomtatványok publikálási címe nincsen megadva!");
      } else {
         String var6;
         try {
            String var4;
            try {
               HttpGet var3 = new HttpGet(this.url.toURI());
               var2 = HttpClientFactory.createWithAnykConfig();
               HttpResponse var16 = var2.execute(var3);
               HttpEntity var5 = var16.getEntity();
               if (var5 == null) {
                  var6 = String.format("A(z) %s címen az ÁNYK-ból kitiltott bevallások listája nem áll rendelkezésre", this.url);
                  throw new BlacklistProviderException(var6);
               }

               var1 = this.readContent(var5.getContent());
               EntityUtils.consume(var5);
               var6 = var1;
            } catch (HttpClientFactoryException var12) {
               var4 = String.format("Sikertelen kapcsolatépítés a(z) %s címhez", this.url.toString());
               throw new BlacklistProviderException(var4, var12);
            } catch (URISyntaxException var13) {
               var4 = String.format("Hibás cím %s", this.url.toString());
               throw new BlacklistProviderException(var4, var13);
            } catch (IOException var14) {
               throw new BlacklistProviderException("Adatletöltési hiba", var14);
            }
         } finally {
            if (var2 != null) {
               var2.getConnectionManager().shutdown();
            }

         }

         return var6;
      }
   }

   protected String readContent(InputStream var1) throws IOException {
      ByteArrayOutputStream var2 = new ByteArrayOutputStream();
      Throwable var3 = null;

      try {
         byte[] var4 = new byte[1024];

         int var5;
         while((var5 = var1.read(var4)) != -1) {
            var2.write(var4, 0, var5);
         }

         String var6 = var2.toString("UTF-8");
         return var6;
      } catch (Throwable var15) {
         var3 = var15;
         throw var15;
      } finally {
         if (var2 != null) {
            if (var3 != null) {
               try {
                  var2.close();
               } catch (Throwable var14) {
                  var3.addSuppressed(var14);
               }
            } else {
               var2.close();
            }
         }

      }
   }
}
