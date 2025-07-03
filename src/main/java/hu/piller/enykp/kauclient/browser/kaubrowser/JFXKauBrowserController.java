package hu.piller.enykp.kauclient.browser.kaubrowser;

import hu.piller.enykp.kauclient.browser.kaubrowser.helper.ResponseHtml;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.util.Iterator;
import org.w3c.dom.Document;

public class JFXKauBrowserController {
   private JFXKauBrowserModel jfxKauBrowserModel;
   private JFXKauBrowserController.KauBrowserCookieHandler kauBrowserCookieHandler;

   public void setJfxKauBrowserModel(JFXKauBrowserModel var1) {
      this.jfxKauBrowserModel = var1;
      this.kauBrowserCookieHandler = new JFXKauBrowserController.KauBrowserCookieHandler();
   }

   public void checkLocation(String var1) {
      if (((String)this.jfxKauBrowserModel.getAuthTokens().get("sp_resp_url")).equals(var1)) {
         this.jfxKauBrowserModel.setPageFlowFinished();
      }

   }

   public void processHtml(Document var1) {
      ResponseHtml var2 = new ResponseHtml(var1);
      if (var2.hasFormActionForUrl((String)this.jfxKauBrowserModel.getAuthTokens().get("sp_resp_url"))) {
         this.jfxKauBrowserModel.setSamlResponse(var2.getSamlResponse());
         this.jfxKauBrowserModel.setRelayState(var2.getRelayState());
      }

   }

   public void onFinishPageFlow() {
      String var1 = this.kauBrowserCookieHandler.getCookieValue(this.jfxKauBrowserModel.getCookieName());
      this.jfxKauBrowserModel.setCookie(var1);
      this.kauBrowserCookieHandler.restore();
      this.done();
   }

   public void done() {
      this.jfxKauBrowserModel.setDone(true);
   }

   class KauBrowserCookieHandler {
      private CookieHandler originalHandler = CookieHandler.getDefault();
      private CookieManager cookieManager = new CookieManager();

      public KauBrowserCookieHandler() {
         CookieHandler.setDefault(this.cookieManager);
      }

      public String getCookieValue(String var1) {
         Iterator var2 = this.cookieManager.getCookieStore().getCookies().iterator();

         HttpCookie var3;
         do {
            if (!var2.hasNext()) {
               return null;
            }

            var3 = (HttpCookie)var2.next();
         } while(!var1.equals(var3.getName()));

         return var3.getValue();
      }

      public void restore() {
         CookieHandler.setDefault(this.originalHandler);
      }
   }
}
