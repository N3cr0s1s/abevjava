package hu.piller.enykp.kauclient.browser.kaubrowser;

import hu.piller.enykp.kauclient.browser.kaubrowser.helper.KauLoginFormLoaderTemplate;
import java.util.Map;

public class JFXKauBrowserModel {
   private Object sync = new Object();
   private String kauLoginFormLoaderHtml;
   private boolean pageflow_finished;
   private Map<String, String> authTokens;
   private String samlResponse;
   private String relayState;
   private String cookie;
   private boolean done;

   public void setAuthTokens(Map<String, String> var1) {
      this.init();
      this.authTokens = var1;
      this.kauLoginFormLoaderHtml = (new KauLoginFormLoaderTemplate("kau_login_form_loader_template.xhtml")).buildLoginFormLoaderWithAuthTokens(var1);
   }

   public Map<String, String> getAuthTokens() {
      return this.authTokens;
   }

   public String getSamlResponse() {
      return this.samlResponse;
   }

   public void setSamlResponse(String var1) {
      this.samlResponse = var1;
   }

   public String getRelayState() {
      return this.relayState;
   }

   public void setRelayState(String var1) {
      this.relayState = var1;
   }

   public String getCookie() {
      return this.cookie;
   }

   public void setCookie(String var1) {
      this.cookie = var1;
   }

   public String getCookieName() {
      return (String)this.authTokens.get("cookie");
   }

   public boolean isSubjectConfirmationRequired() {
      return Boolean.valueOf((String)this.authTokens.get("subject_confirmation_required"));
   }

   public String getKauLoginFormLoaderHtml() {
      return this.kauLoginFormLoaderHtml;
   }

   public void setPageFlowFinished() {
      this.pageflow_finished = true;
   }

   public boolean isPageFlowFinished() {
      return this.pageflow_finished;
   }

   public boolean isDone() {
      return this.done;
   }

   public void setDone(boolean var1) {
      synchronized(this.sync) {
         this.done = var1;
         this.sync.notifyAll();
      }
   }

   public Object getSync() {
      return this.sync;
   }

   public void init() {
      this.samlResponse = null;
      this.relayState = null;
      this.cookie = null;
      this.pageflow_finished = false;
      this.kauLoginFormLoaderHtml = null;
      this.done = false;
   }
}
