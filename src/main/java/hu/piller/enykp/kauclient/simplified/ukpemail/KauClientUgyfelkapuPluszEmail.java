package hu.piller.enykp.kauclient.simplified.ukpemail;

import hu.piller.enykp.gui.framework.MainFrame;
import hu.piller.enykp.kauclient.KauClientException;
import hu.piller.enykp.kauclient.KauResult;
import hu.piller.enykp.kauclient.simplified.BaseKauClient;
import hu.piller.enykp.kauclient.simplified.ukpemail.widget.JEmailTokenDialog;
import hu.piller.enykp.util.httpclient.HttpClientFactory;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class KauClientUgyfelkapuPluszEmail extends BaseKauClient {
   public KauResult authenticate(Map<String, String> var1, String var2, String var3) throws KauClientException {
      HttpClient var4 = null;

      try {
         var4 = HttpClientFactory.createWithAnykConfig();
         ArrayList var5 = new ArrayList();
         var5.add(new BasicNameValuePair("SAMLRequest", (String)var1.get("saml_request_b64")));
         var5.add(new BasicNameValuePair("RelayState", (String)var1.get("relay_state_b64")));
         HttpPost var6 = new HttpPost((String)var1.get("sso_auth_url"));
         var6.setEntity(new UrlEncodedFormEntity(var5, "UTF-8"));
         BasicHttpParams var7 = new BasicHttpParams();
         var7.setParameter("http.protocol.handle-redirects", false);
         var6.setParams(var7);
         HttpResponse var8 = var4.execute(var6);
         int var9 = var8.getStatusLine().getStatusCode();
         if (303 != var9) {
            throw new KauClientException("A KAÜ azonosítási módszer kiválasztás előkészítéskor nem elvárt státuszkód érkezett : " + var9);
         } else {
            Header var10 = var8.getFirstHeader("Location");
            if (var10 != null && var10.getValue() != null && var10.getValue().length() != 0) {
               String var11 = var10.getValue();
               List var12 = ((DefaultHttpClient)var4).getCookieStore().getCookies();
               Iterator var13 = var12.iterator();

               while(var13.hasNext()) {
                  Cookie var14 = (Cookie)var13.next();
                  if ("ROUTEID".equals(var14.getName())) {
                     var11 = "https://" + var14.getDomain() + var11;
                     break;
                  }
               }

               HttpGet var36 = new HttpGet(var11);
               var8 = var4.execute(var36);
               String var37 = new String(this.readAllBytesFromInputStream(var8.getEntity().getContent()));
               Document var15 = Jsoup.parse(var37);
               Elements var16 = var15.select("input[name$=x]");
               if (var16 != null && var16.size() != 0) {
                  String var17 = ((Element)var16.get(0)).attr("value");
                  var16 = var15.select("input[name$=y]");
                  if (var16 != null && var16.size() != 0) {
                     String var18 = ((Element)var16.get(0)).attr("value");
                     String var19 = "urn:eksz.gov.hu:1.0:azonositas:kau:2:uk:email";
                     var11 = var11.substring(0, var11.indexOf("authservice"));
                     var11 = var11 + "authservice";
                     var5 = new ArrayList();
                     var5.add(new BasicNameValuePair("authServiceUri", var19));
                     var5.add(new BasicNameValuePair("x", var17));
                     var5.add(new BasicNameValuePair("y", var18));
                     var6 = new HttpPost(var11);
                     var6.setEntity(new UrlEncodedFormEntity(var5, "UTF-8"));
                     var8 = var4.execute(var6);
                     var37 = new String(this.readAllBytesFromInputStream(var8.getEntity().getContent()));
                     var15 = Jsoup.parse(var37);
                     Elements var20 = var15.select("form[id$=redirectForm]");
                     if (var20 != null && var20.size() != 0) {
                        var11 = ((Element)var20.get(0)).attr("action");
                        var20 = var15.select("input[name$=RelayState]");
                        if (var20 != null && var20.size() != 0) {
                           String var21 = ((Element)var20.get(0)).attr("value");
                           var20 = var15.select("input[name$=SAMLRequest]");
                           if (var20 != null && var20.size() != 0) {
                              String var22 = ((Element)var20.get(0)).attr("value");
                              var5 = new ArrayList();
                              var5.add(new BasicNameValuePair("RelayState", var21));
                              var5.add(new BasicNameValuePair("SAMLRequest", var22));
                              var5.add(new BasicNameValuePair("lang", "HU"));
                              var6 = new HttpPost(var11);
                              var6.setEntity(new UrlEncodedFormEntity(var5, "UTF-8"));
                              var7 = new BasicHttpParams();
                              var7.setParameter("http.protocol.handle-redirects", false);
                              var6.setParams(var7);
                              var8 = var4.execute(var6);
                              var9 = var8.getStatusLine().getStatusCode();
                              if (303 != var9) {
                                 throw new KauClientException("A KAÜ ÜK azonosítás képernyő betöltés előkészítéskor nem elvárt státuszkód érkezett : " + var9);
                              } else {
                                 var10 = var8.getFirstHeader("Location");
                                 if (var10 != null && var10.getValue() != null && var10.getValue().length() != 0) {
                                    var11 = var10.getValue();
                                    var12 = ((DefaultHttpClient)var4).getCookieStore().getCookies();
                                    Iterator var23 = var12.iterator();

                                    while(var23.hasNext()) {
                                       Cookie var24 = (Cookie)var23.next();
                                       if ("ROUTEID".equals(var24.getName())) {
                                          var11 = "https://" + var24.getDomain() + var11;
                                          break;
                                       }
                                    }

                                    var36 = new HttpGet(var11);
                                    var8 = var4.execute(var36);
                                    var37 = new String(this.readAllBytesFromInputStream(var8.getEntity().getContent()));
                                    var15 = Jsoup.parse(var37);
                                    var20 = var15.select("input[name$=x]");
                                    if (var20 != null && var20.size() != 0) {
                                       var17 = ((Element)var20.get(0)).attr("value");
                                       var20 = var15.select("input[name$=y]");
                                       if (var20 != null && var20.size() != 0) {
                                          var18 = ((Element)var20.get(0)).attr("value");
                                          var5 = new ArrayList();
                                          var5.add(new BasicNameValuePair("x", var17));
                                          var5.add(new BasicNameValuePair("y", var18));
                                          var5.add(new BasicNameValuePair("felhasznaloNev", var2));
                                          var5.add(new BasicNameValuePair("jelszo", var3));
                                          var5.add(new BasicNameValuePair("submit", ""));
                                          var11 = var11.substring(0, var11.indexOf("x=") - 1);
                                          var6 = new HttpPost(var11);
                                          var6.setEntity(new UrlEncodedFormEntity(var5, "UTF-8"));
                                          var8 = var4.execute(var6);
                                          var37 = new String(this.readAllBytesFromInputStream(var8.getEntity().getContent()));
                                          var15 = Jsoup.parse(var37);
                                          var20 = var15.select("input[name$=y]");
                                          var18 = ((Element)var20.get(0)).attr("value");
                                          if (this.hasInvalidCredentials(var15)) {
                                             var5 = new ArrayList();
                                             var5.add(new BasicNameValuePair("x", var17));
                                             var5.add(new BasicNameValuePair("y", var18));
                                             var5.add(new BasicNameValuePair("cancel", ""));
                                             var6 = new HttpPost(var11);
                                             var6.setEntity(new UrlEncodedFormEntity(var5, "UTF-8"));
                                             var8 = var4.execute(var6);
                                             EntityUtils.consume(var8.getEntity());
                                             throw new KauClientException("A felhasználónév vagy a jelszó helytelen, vagy Ön nem jogosult a kiválasztott azonosítási szolgáltatás igénybevételére", true);
                                          } else {
                                             String var38 = ((Element)var15.select("input[name$=ocraSessionId]").get(0)).attr("value");
                                             String var39 = (new JEmailTokenDialog(MainFrame.thisinstance, var2, var38)).run();
                                             if (var39 == null) {
                                                throw new KauClientException("Megszakítva");
                                             } else {
                                                var5 = new ArrayList();
                                                var5.add(new BasicNameValuePair("x", var17));
                                                var5.add(new BasicNameValuePair("y", var18));
                                                var5.add(new BasicNameValuePair("felhasznaloNev", var2));
                                                var5.add(new BasicNameValuePair("ocraSessionId", var38));
                                                var5.add(new BasicNameValuePair("token", var39));
                                                var5.add(new BasicNameValuePair("submit", ""));
                                                var6 = new HttpPost(var11);
                                                var6.setEntity(new UrlEncodedFormEntity(var5, "UTF-8"));
                                                var8 = var4.execute(var6);
                                                var37 = new String(this.readAllBytesFromInputStream(var8.getEntity().getContent()));
                                                var15 = Jsoup.parse(var37);
                                                if (this.hasInvalidToken(var15)) {
                                                   var20 = var15.select("input[name$=y]");
                                                   var18 = ((Element)var20.get(0)).attr("value");
                                                   var20 = var15.select("input[name$=x]");
                                                   var17 = ((Element)var20.get(0)).attr("value");
                                                   var5 = new ArrayList();
                                                   var5.add(new BasicNameValuePair("x", var17));
                                                   var5.add(new BasicNameValuePair("y", var18));
                                                   var5.add(new BasicNameValuePair("cancel", ""));
                                                   var6 = new HttpPost(var11);
                                                   var6.setEntity(new UrlEncodedFormEntity(var5, "UTF-8"));
                                                   var8 = var4.execute(var6);
                                                   EntityUtils.consume(var8.getEntity());
                                                   throw new KauClientException("A megadott hitelesítő kód helytelen!", true);
                                                } else {
                                                   var20 = var15.select("form[id$=redirectForm]");
                                                   if (var20 != null && var20.size() != 0) {
                                                      var11 = ((Element)var20.get(0)).attr("action");
                                                      var20 = var15.select("input[name$=RelayState]");
                                                      if (var20 != null && var20.size() != 0) {
                                                         var21 = ((Element)var20.get(0)).attr("value");
                                                         var20 = var15.select("input[name$=SAMLResponse]");
                                                         if (var20 != null && var20.size() != 0) {
                                                            String var25 = ((Element)var20.get(0)).attr("value");
                                                            var5 = new ArrayList();
                                                            var5.add(new BasicNameValuePair("RelayState", var21));
                                                            var5.add(new BasicNameValuePair("SAMLResponse", var25));
                                                            var6 = new HttpPost(var11);
                                                            var6.setEntity(new UrlEncodedFormEntity(var5, "UTF-8"));
                                                            var8 = var4.execute(var6);
                                                            var37 = new String(this.readAllBytesFromInputStream(var8.getEntity().getContent()));
                                                            var15 = Jsoup.parse(var37);
                                                            var20 = var15.select("form[id$=redirectForm]");
                                                            if (var20 != null && var20.size() != 0) {
                                                               var11 = ((Element)var20.get(0)).attr("action");
                                                               if (!((String)var1.get("sp_resp_url")).equalsIgnoreCase(var11)) {
                                                                  throw new KauClientException("A KAÜ ÜK azonosítást lezáró végcím értéke eltér a hitelesítési token értékétől : " + var11 + " " + (String)var1.get("sp_resp_url"));
                                                               } else {
                                                                  var20 = var15.select("input[name$=RelayState]");
                                                                  if (var20 == null || var20.size() == 0) {
                                                                     throw new KauClientException("A KAÜ ÜK azonosítás lezárásához nem adott át RelayState paramétert");
                                                                  } else {
                                                                     var21 = ((Element)var20.get(0)).attr("value");
                                                                     var20 = var15.select("input[name$=SAMLResponse]");
                                                                     if (var20 == null || var20.size() == 0) {
                                                                        throw new KauClientException("A KAÜ ÜK azonosítás lezárásához nem adott át SAMLResponse paramétert");
                                                                     } else {
                                                                        var25 = ((Element)var20.get(0)).attr("value");
                                                                        KauResult var26 = new KauResult();
                                                                        var26.setRelayState(var21);
                                                                        var26.setSamlResponse(var25);
                                                                        Iterator var27 = ((DefaultHttpClient)var4).getCookieStore().getCookies().iterator();

                                                                        while(true) {
                                                                           if (var27.hasNext()) {
                                                                              Cookie var28 = (Cookie)var27.next();
                                                                              if (!var28.getName().equals(var1.get("cookie"))) {
                                                                                 continue;
                                                                              }

                                                                              var26.setCookie(var28.getValue());
                                                                           }

                                                                           var26.setSubjectConfirmationRequired(Boolean.parseBoolean((String)var1.get("subject_confirmation_required")));
                                                                           KauResult var40 = var26;
                                                                           return var40;
                                                                        }
                                                                     }
                                                                  }
                                                               }
                                                            } else {
                                                               throw new KauClientException("A KAÜ ÜK azonosítást lezáró végcímet nem adott át");
                                                            }
                                                         } else {
                                                            throw new KauClientException("A KAÜ ÜK azonosítás befejezéshez nem adott át SAMLResponse paramétert");
                                                         }
                                                      } else {
                                                         throw new KauClientException("A KAÜ ÜK azonosítás befejezéshez nem adott át RelayState paramétert");
                                                      }
                                                   } else {
                                                      throw new KauClientException("A KAÜ ÜK azonosítás befejezéshez nem adott át URL-t");
                                                   }
                                                }
                                             }
                                          }
                                       } else {
                                          throw new KauClientException("A KAÜ ÜK felhasználó azonosítás beviteli képernyőn nem adott át 'y' paramétert");
                                       }
                                    } else {
                                       System.out.println("" + var37);
                                       throw new KauClientException("A KAÜ ÜK felhasználó azonosítás beviteli képernyőn nem adott át 'x' paramétert");
                                    }
                                 } else {
                                    throw new KauClientException("A KAÜ ÜK azonosítás képernyő betöltéshez érvénytelen URL érkezett : " + var10.getValue());
                                 }
                              }
                           } else {
                              throw new KauClientException("A KAÜ ÜK felhasználó azonosítás hívásához nem adott át SAMLRequest paramétert");
                           }
                        } else {
                           throw new KauClientException("A KAÜ ÜK felhasználó azonosítás hívásához nem adott át RelayState paramétert");
                        }
                     } else {
                        throw new KauClientException("A KAÜ ÜK felhasználó azonosítás hívásához nem adott át URL-t");
                     }
                  } else {
                     throw new KauClientException("A KAÜ azonosítási módszer kiválasztás előkészítéskor nem érkezett 'y' paraméter");
                  }
               } else {
                  throw new KauClientException("A KAÜ azonosítási módszer kiválasztás előkészítéskor nem érkezett 'x' paraméter");
               }
            } else {
               throw new KauClientException("A KAÜ azonosítási módszer kiválasztás előkészítéskor érvénytelen cím érkezett : " + var10.getValue());
            }
         }
      } catch (UnsupportedEncodingException var33) {
         throw new KauClientException(var33);
      } catch (Exception var34) {
         throw new KauClientException(var34.getMessage(), true);
      } finally {
         if (var4 != null) {
            var4.getConnectionManager().shutdown();
         }

      }
   }

   protected boolean hasInvalidToken(Document var1) {
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
         if (var4 != null && var4.indexOf("A megadott token nem megfelel") != -1) {
            return true;
         }
      }

      var2 = var1.select("div.feedback.feedback--negative");

      for(var3 = 0; var2 != null && var3 < var2.size(); ++var3) {
         var4 = ((Element)var2.get(var3)).text();
         if (var4 != null && var4.indexOf("helytelen. Ellen") != -1) {
            return true;
         }
      }

      return false;
   }
}
