package hu.piller.enykp.kauclient.authtokens;

import hu.piller.enykp.kauclient.IKauAuthTokens;
import hu.piller.enykp.niszws.ClientStubBuilder;
import hu.piller.enykp.niszws.authtokenservice.AuthTokenRequest;
import hu.piller.enykp.niszws.authtokenservice.AuthTokenResponse;
import hu.piller.enykp.niszws.authtokenservice.AuthTokenService;
import hu.piller.enykp.niszws.authtokenservice.ObjectFactory;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class KauAuthTokensAnykgw2 implements IKauAuthTokens {
   private static final String DEFAULT_PROTOCOL = "https";
   private static final String DEFAULT_HOST = "anykgw.gov.hu";
   private static final String DEFAULT_PORT = "";
   private String scheme = System.getProperty("authtoken.scheme", "https");
   private String host = System.getProperty("authtoken.host", "anykgw.gov.hu");
   private String port = System.getProperty("authtoken.port", "");

   public Map<String, String> getTokens() {
      String var1 = UUID.randomUUID().toString();
      AuthTokenRequest var2 = this.createRequestWithId(var1);
      AuthTokenResponse var3 = ((AuthTokenService)(new ClientStubBuilder()).get("AuthTokenService.wsdl", "AuthTokenService", "http://tarhely.gov.hu/anykgw2", AuthTokenService.class, this.scheme, this.host, this.port, "/anykgw2/AuthTokenService")).getAuthToken(var2);
      Map var4 = this.getTokensFromResponse(var3);
      return var4;
   }

   private AuthTokenRequest createRequestWithId(String var1) {
      AuthTokenRequest var2 = (new ObjectFactory()).createAuthTokenRequest();
      var2.setId(var1);
      var2.setAudience("urn:eksz.gov.hu:1.0:azonositas:kau:1");
      return var2;
   }

   private Map<String, String> getTokensFromResponse(AuthTokenResponse var1) {
      HashMap var2 = new HashMap();
      var2.put("id", var1.getId());
      var2.put("cookie", var1.getCookie());
      var2.put("subject_confirmation_required", Boolean.toString(var1.isSubjectConfirmationRequired()));
      var2.put("sso_auth_url", var1.getSsoAuthUrl());
      var2.put("sp_resp_url", var1.getSpRespUrl());
      var2.put("relay_state_b64", var1.getRelayState());
      var2.put("saml_request_b64", var1.getSamlRequest());
      return var2;
   }
}
