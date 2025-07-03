package hu.piller.enykp.alogic.ebev.datagate;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import hu.piller.enykp.alogic.masterdata.sync.download.provider.IMasterDataProvider;
import hu.piller.enykp.alogic.masterdata.sync.download.provider.MasterDataDownloadRequest;
import hu.piller.enykp.alogic.masterdata.sync.download.provider.MasterDataDownloadRequestACK;
import hu.piller.enykp.alogic.masterdata.sync.download.provider.MasterDataDownloadResponse;
import hu.piller.enykp.alogic.masterdata.sync.download.provider.MasterDataProviderException;
import hu.piller.enykp.kauclient.KauResult;
import hu.piller.enykp.util.httpclient.HttpClientFactory;
import hu.piller.enykp.util.httpclient.HttpClientFactoryException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.util.EntityUtils;

public class DatagateClient implements IMasterDataProvider {
   private boolean traceable = DatagateConfig.isDatagateTraceEnabled();
   private Properties envCfg;
   private boolean initialized = false;
   private String accessToken;
   private IDatagateUserAuthKau datagateUserAuthKau;

   public DatagateClient() {
      try {
         this.envCfg = DatagateConfig.getEnvCfg();
         this.initialized = true;
      } catch (Exception var2) {
         var2.printStackTrace(System.err);
      }

      this.setDatagateUserAuthKau(new DatagateUserAuthKau());
   }

   public void setDatagateUserAuthKau(IDatagateUserAuthKau var1) {
      this.datagateUserAuthKau = var1;
   }

   public MasterDataDownloadRequestACK sendMasterDataDownloadRequest(MasterDataDownloadRequest var1) throws MasterDataProviderException {
      HttpClient var2 = null;

      MasterDataDownloadRequestACK var11;
      try {
         this.checkAccessToken();
         this.trace(DatagateFunction.MD_DOWNLOAD_REQUEST, "Törzsadat lekérdezés kérelem üzenet objektum összeállítása", false);
         DatagateMasterDataRequest var3 = new DatagateMasterDataRequest();
         var3.getAzonositok().addAll(var1.getAzonositok());
         String var4 = (new Gson()).toJson(var3);
         this.trace(DatagateFunction.MD_DOWNLOAD_REQUEST, "Törzsadat lekérdezés kérelem beküldés POST összeállítása", false);
         HttpPost var5 = new HttpPost(this.envCfg.getProperty("DATAGATE_URL") + "/masterData");
         var5.setHeader("Authorization", "Bearer " + this.accessToken);
         ByteArrayEntity var6 = new ByteArrayEntity(var4.getBytes("UTF-8"));
         var6.setContentType("application/json");
         var6.setContentEncoding("UTF-8");
         var5.setEntity(var6);
         this.trace(DatagateFunction.MD_DOWNLOAD_REQUEST, "Törzsadat lekérdezés kérelem beküldése", false);
         var2 = HttpClientFactory.createWithAnykConfig();
         HttpResponse var7 = var2.execute(var5);
         String var8;
         DatagateMasterDataResponse var9;
         if (var7.getStatusLine().getStatusCode() != 200) {
            if (var7.getStatusLine().getStatusCode() == 400) {
               this.trace(DatagateFunction.MD_DOWNLOAD_REQUEST, "Törzsadat lekérdezés kérelem visszautasítva", true);
               this.trace(DatagateFunction.MD_DOWNLOAD_REQUEST, "Törzsadat lekérdezés elutasítás payload kibontása", true);
               var8 = EntityUtils.toString(var7.getEntity(), "UTF-8");
               this.trace(DatagateFunction.MD_DOWNLOAD_REQUEST, var8, true);
               var9 = null;

               try {
                  this.trace(DatagateFunction.MD_DOWNLOAD_REQUEST, "Üzenet objektum létrehozása a payloadból", true);
                  var9 = (DatagateMasterDataResponse)(new Gson()).fromJson(var8, DatagateMasterDataResponse.class);
               } catch (JsonSyntaxException var18) {
                  this.trace(DatagateFunction.MD_DOWNLOAD_REQUEST, "Üzenet objektum létrehozása sikertelen: " + var18.getMessage() + ", kivétel a hívónak", true);
                  this.dropAccessToken();
                  throw new MasterDataProviderException(DatagateFunction.MD_DOWNLOAD_REQUEST, var18.getMessage());
               }

               StringBuffer var22 = (new StringBuffer("Hiba történt a törzsadat lekérdezési kérelem feldogozása során:\n")).append(var9.getErrmsg()).append(" [").append(var9.getErrcode()).append("]");
               this.trace(DatagateFunction.MD_DOWNLOAD_REQUEST, "Elutasítás oka: " + var22.toString() + ", kivétel a hívónak!", true);
               this.dropAccessToken();
               throw new MasterDataProviderException(DatagateFunction.MD_DOWNLOAD_REQUEST, var22.toString());
            }

            if (var7.getStatusLine().getStatusCode() == 500) {
               this.trace(DatagateFunction.MD_DOWNLOAD_REQUEST, "Elutasítás, kiszolgáló oldali hiba történt a törzsadat letöltés kérelem befogadása során, kivétel a hívónak!", true);
               this.dropAccessToken();
               throw new MasterDataProviderException(DatagateFunction.MD_DOWNLOAD_REQUEST, "Kiszolgáló oldali hiba történt a törzsadat letöltés kérelem befogadása során!");
            }

            if (var7.getStatusLine().getStatusCode() == 401) {
               this.trace(DatagateFunction.MD_DOWNLOAD_REQUEST, "A hitelesítő token érvényessége lejárt", true);
               this.dropAccessToken();
               MasterDataDownloadRequestACK var21 = this.sendMasterDataDownloadRequest(var1);
               return var21;
            }

            this.trace(DatagateFunction.MD_DOWNLOAD_REQUEST, "Elutasítás, érvénytelen HTTP válasz kód " + var7.getStatusLine().getStatusCode() + " az Adatkapu válaszban, kivétel a hívónak!", true);
            this.dropAccessToken();
            throw new MasterDataProviderException(DatagateFunction.MD_DOWNLOAD_REQUEST, "Érvénytelen HTTP válasz kód " + var7.getStatusLine().getStatusCode() + " az Adatkapu válaszban!");
         }

         this.trace(DatagateFunction.MD_DOWNLOAD_REQUEST, "Törzsadat lekérdezés kérelem befogadva", false);
         this.trace(DatagateFunction.MD_DOWNLOAD_REQUEST, "Törzsadat lekérdezés kérelem nyugta kibontása a válaszból", false);
         var8 = EntityUtils.toString(var7.getEntity(), "UTF-8");
         this.trace(DatagateFunction.MD_DOWNLOAD_REQUEST, "Törzsadat lekérdezés kérelem nyugta = " + var8, false);
         this.trace(DatagateFunction.MD_DOWNLOAD_REQUEST, var8, false);
         var9 = null;

         try {
            this.trace(DatagateFunction.MD_DOWNLOAD_REQUEST, "Törzsadat lekérdezés kérelem nyugta üzenet deszerializálása", false);
            var9 = (DatagateMasterDataResponse)(new Gson()).fromJson(var8, DatagateMasterDataResponse.class);
         } catch (JsonSyntaxException var17) {
            this.trace(DatagateFunction.MD_DOWNLOAD_REQUEST, "Törzsadat lekérdezés kérelem nyugta üzenet deszerializálás hiba: " + var17.getMessage() + ", kivétel a hívónak", true);
            this.dropAccessToken();
            throw new MasterDataProviderException(DatagateFunction.MD_DOWNLOAD_REQUEST, var17.getMessage());
         }

         this.trace(DatagateFunction.MD_DOWNLOAD_REQUEST, "Törzsadat lekérdezés kérelem nyugta létrehozása", false);
         MasterDataDownloadRequestACK var10 = new MasterDataDownloadRequestACK();
         var10.setQueryId(var9.getQueryid());
         var10.setPollInterval(var9.getPollinterval());
         var10.getRefusedIds().addAll(var9.getAzonositok());
         this.trace(DatagateFunction.MD_DOWNLOAD_REQUEST, "Törzsadat lekérdezés kérelem nyugta visszaadva a benyújtónak", false);
         var11 = var10;
      } catch (HttpClientFactoryException | IOException var19) {
         var19.printStackTrace(System.err);
         this.trace(DatagateFunction.MD_DOWNLOAD_REQUEST, "Hiba történt: " + var19.getMessage() + ", kivétel a hívónak!", true);
         this.dropAccessToken();
         throw new MasterDataProviderException(DatagateFunction.MD_DOWNLOAD_REQUEST, var19.getMessage());
      } finally {
         if (var2 != null) {
            var2.getConnectionManager().shutdown();
         }

      }

      return var11;
   }

   public MasterDataDownloadResponse receiveMasterDataDownloadResponse(String var1) throws MasterDataProviderException {
      HttpClient var2 = null;

      MasterDataDownloadResponse var8;
      try {
         this.checkAccessToken();
         MasterDataDownloadResponse var3 = new MasterDataDownloadResponse();
         this.trace(DatagateFunction.MD_DOWNLOAD_RESPONSE, "Letöltés státusz lekérdezés előkészítése (requestId=" + var1 + ")", false);
         HttpGet var4 = new HttpGet(this.envCfg.getProperty("DATAGATE_URL") + "/masterData/" + var1);
         var4.setHeader("Authorization", "Bearer " + this.accessToken);
         this.trace(DatagateFunction.MD_DOWNLOAD_RESPONSE, "Letöltés státusz lekérdezése", false);
         var2 = HttpClientFactory.createWithAnykConfig();
         HttpResponse var5 = var2.execute(var4);
         int var6 = var5.getStatusLine().getStatusCode();
         if (var6 != 200) {
            if (var6 == 401) {
               this.trace(DatagateFunction.MD_DOWNLOAD_RESPONSE, "A hitelesítő token érvényessége lejárt", true);
               this.dropAccessToken();
               MasterDataDownloadResponse var18 = this.receiveMasterDataDownloadResponse(var1);
               return var18;
            }

            if (var6 == 400) {
               this.trace(DatagateFunction.MD_DOWNLOAD_RESPONSE, "A kérelem még nincsen kiszolgálva", false);
               this.trace(DatagateFunction.MD_DOWNLOAD_RESPONSE, "Státusz payload kibontása a válaszból", false);
               String var17 = EntityUtils.toString(var5.getEntity());
               this.trace(DatagateFunction.MD_DOWNLOAD_RESPONSE, var17, false);
               var8 = null;

               DatagateMasterDataResponse var20;
               try {
                  this.trace(DatagateFunction.MD_DOWNLOAD_RESPONSE, "Válasz objektum létrehozása a payloadból", false);
                  var20 = (DatagateMasterDataResponse)(new Gson()).fromJson(var17, DatagateMasterDataResponse.class);
               } catch (JsonSyntaxException var14) {
                  this.trace(DatagateFunction.MD_DOWNLOAD_RESPONSE, "Válasz objektum létrehozása sikertelen: " + var14.getMessage() + ", kivétel a hívónak!", false);
                  this.dropAccessToken();
                  throw new MasterDataProviderException(DatagateFunction.MD_DOWNLOAD_RESPONSE, var14.getMessage());
               }

               if ("500".equals(var20.getErrcode())) {
                  this.trace(DatagateFunction.MD_DOWNLOAD_RESPONSE, "Előírt pollozási intervallum: " + var20.getPollinterval() + " sec", false);
                  var3.setPollInterval(var20.getPollinterval());
                  this.trace(DatagateFunction.MD_DOWNLOAD_RESPONSE, "Valasz a hívónak: pollozás folytatása", false);
                  MasterDataDownloadResponse var21 = var3;
                  return var21;
               }

               StringBuffer var9 = (new StringBuffer("Hiba történt a törzsadat lekérdezési kérelem státusz lekérdezése során:\n")).append(var20.getErrmsg()).append(" [").append(var20.getErrcode()).append("]");
               this.trace(DatagateFunction.MD_DOWNLOAD_RESPONSE, var9.toString() + ", kivétel a hívónak", true);
               this.dropAccessToken();
               throw new MasterDataProviderException(DatagateFunction.MD_DOWNLOAD_RESPONSE, var9.toString());
            }

            if (var6 == 500) {
               this.trace(DatagateFunction.MD_DOWNLOAD_RESPONSE, "Kiszolgáló oldali hiba történt a törzsadat letöltés kérelem státusz lekérdezése során, kivétel a hívónak", true);
               this.dropAccessToken();
               throw new MasterDataProviderException(DatagateFunction.MD_DOWNLOAD_RESPONSE, "Kiszolgáló oldali hiba történt a törzsadat letöltés kérelem státusz lekérdezése során!");
            }

            this.trace(DatagateFunction.MD_DOWNLOAD_RESPONSE, "Érvénytelen HTTP válasz kód " + var6 + " az Adatkapu válaszban, kivétel a hívónak!", true);
            this.dropAccessToken();
            throw new MasterDataProviderException(DatagateFunction.MD_DOWNLOAD_RESPONSE, "Érvénytelen HTTP válasz kód " + var6 + " az Adatkapu válaszban!");
         }

         this.trace(DatagateFunction.MD_DOWNLOAD_RESPONSE, "Kérelem kiszolgálva", false);
         HttpEntity var7 = var5.getEntity();
         if (var7 == null) {
            String var19 = "Adatfájl nélküli válasz érkezett a feldolgozottnak jelölt " + var1 + " azonosítójú kérelem lekérdezésére";
            this.trace(DatagateFunction.MD_DOWNLOAD_RESPONSE, var19 + ", kivétel a hívónak", true);
            this.dropAccessToken();
            throw new MasterDataProviderException(DatagateFunction.MD_DOWNLOAD_RESPONSE, var19);
         }

         this.trace(DatagateFunction.MD_DOWNLOAD_RESPONSE, "Válasz archívum előkészítése mentésre", false);
         var3.setResult(this.readAllBytesFromInputStream(var7.getContent()));
         this.trace(DatagateFunction.MD_DOWNLOAD_RESPONSE, "Válasz archívum visszaadása a hívónak", false);
         this.dropAccessToken();
         var8 = var3;
      } catch (HttpClientFactoryException | IOException var15) {
         var15.printStackTrace(System.err);
         this.trace(DatagateFunction.MD_DOWNLOAD_RESPONSE, "Hiba történt: " + var15.getMessage() + ", kivétel a hívónak!", true);
         this.dropAccessToken();
         throw new MasterDataProviderException(DatagateFunction.MD_DOWNLOAD_RESPONSE, var15.getMessage());
      } finally {
         if (var2 != null) {
            var2.getConnectionManager().shutdown();
         }

      }

      return var8;
   }

   public Map<String, String> getAuthRequestTokensFromDatagate() throws HttpClientFactoryException, IOException {
      HttpClient var1 = null;

      HashMap var8;
      try {
         if (!this.initialized) {
            throw new IOException("A törzsadat lekérdező szolgálátatás technikai hiba miatt nem használható!");
         }

         String var2 = this.envCfg.getProperty("DATAGATE_URL") + "/request";
         HttpGet var3 = new HttpGet(var2);
         var1 = HttpClientFactory.createWithAnykConfig();
         HttpResponse var4 = var1.execute(var3);
         String var5 = new String(this.readAllBytesFromInputStream(var4.getEntity().getContent()));
         JsonObject var6 = (JsonObject)(new Gson()).fromJson(var5, JsonObject.class);
         HashMap var7 = new HashMap(2);
         var7.put("samlRequest", var6.get("samlRequest").getAsString());
         var7.put("msgID", var6.get("msgID").getAsString());
         var8 = var7;
      } finally {
         if (var1 != null) {
            var1.getConnectionManager().shutdown();
         }

      }

      return var8;
   }

   public String getAccessToken(String var1) throws MasterDataProviderException, HttpClientFactoryException, IOException {
      HttpClient var2 = null;

      try {
         String var3 = this.envCfg.getProperty("DATAGATE_URL") + "/auth";
         HttpPost var4 = new HttpPost(var3);
         DatagateClient.AccountReqDto var5 = new DatagateClient.AccountReqDto();
         var5.samlResponse = var1;
         String var6 = (new Gson()).toJson(var5);
         ByteArrayEntity var7 = new ByteArrayEntity(var6.getBytes("UTF-8"));
         var7.setContentType("application/json");
         var7.setContentEncoding("UTF-8");
         var4.setEntity(var7);
         var2 = HttpClientFactory.createWithAnykConfig();
         HttpResponse var8 = var2.execute(var4);
         if (var8.getStatusLine().getStatusCode() != 200) {
            throw new MasterDataProviderException(DatagateFunction.LOGIN, "NAV beléptetés nem sikerült (válasz státusz " + var8.getStatusLine().getStatusCode() + ")");
         } else {
            Header[] var9 = var8.getAllHeaders();
            int var10 = var9.length;

            for(int var11 = 0; var11 < var10; ++var11) {
               Header var12 = var9[var11];
               if ("Authorization".equals(var12.getName())) {
                  String var13 = var12.getValue().substring(7);
                  return var13;
               }
            }

            String var19 = new String(this.readAllBytesFromInputStream(var8.getEntity().getContent()));
            if (var19 != null) {
               JsonObject var17 = (JsonObject)(new Gson()).fromJson(var19, JsonObject.class);
               String var18 = var17.get("errcode").getAsString() + " " + var17.get("errmsg").getAsString();
               throw new MasterDataProviderException(DatagateFunction.LOGIN, var18);
            } else {
               throw new MasterDataProviderException(DatagateFunction.LOGIN, "NAV beléptetés hibás szakrendszer válasz");
            }
         }
      } finally {
         if (var2 != null) {
            var2.getConnectionManager().shutdown();
         }

      }
   }

   protected void dropAccessToken() {
      this.trace(DatagateFunction.LOGOUT, "access token érvénytelenítése", false);
      this.accessToken = null;
   }

   protected void checkAccessToken() throws MasterDataProviderException {
      if (this.accessToken == null) {
         try {
            Map var1 = this.getAuthRequestTokensFromDatagate();
            HashMap var2 = new HashMap();
            var2.put("sp_resp_url", this.envCfg.getProperty("SP_RESP_URL"));
            var2.put("sso_auth_url", this.envCfg.getProperty("KAU_URL"));
            var2.put("relay_state_b64", this.envCfg.getProperty("RELAY_STATE_B64"));
            var2.put("saml_request_b64", var1.get("samlRequest"));
            var2.put("id", var1.get("msgID"));
            var2.put("cookie", "IDS_SSO_ID");
            var2.put("subject_confirmation_required", "true");
            KauResult var3 = this.authenticateWithKau(var2);
            this.accessToken = this.getAccessToken(var3.getSamlResponse());
         } catch (Exception var4) {
            this.dropAccessToken();
            var4.printStackTrace(System.err);
            if (var4 instanceof MasterDataProviderException) {
               throw (MasterDataProviderException)var4;
            }

            throw new MasterDataProviderException(DatagateFunction.LOGIN, var4.getMessage());
         }
      }

   }

   private KauResult authenticateWithKau(Map<String, String> var1) throws Exception {
      try {
         KauResult var2 = this.datagateUserAuthKau.authenticate(var1);
         return var2;
      } catch (Exception var4) {
         throw new Exception("KAÜ hitelesítés hiba " + var4.getMessage(), var4);
      }
   }

   private byte[] readAllBytesFromInputStream(InputStream var1) throws IOException {
      ByteArrayOutputStream var2 = new ByteArrayOutputStream();
      byte[] var3 = new byte['\uffff'];

      int var4;
      while((var4 = var1.read(var3)) != -1) {
         var2.write(var3, 0, var4);
      }

      var2.flush();
      return var2.toByteArray();
   }

   private void trace(DatagateFunction var1, String var2, boolean var3) {
      if (this.traceable) {
         String var4 = var1 + ": " + var2;
         if (var3) {
            System.err.println(var4);
         } else {
            System.out.println(var4);
         }
      }

   }

   class AccountReqDto {
      private String samlResponse;
   }
}
