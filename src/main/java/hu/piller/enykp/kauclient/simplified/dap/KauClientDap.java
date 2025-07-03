package hu.piller.enykp.kauclient.simplified.dap;

import hu.piller.enykp.gui.framework.MainFrame;
import hu.piller.enykp.kauclient.KauClientException;
import hu.piller.enykp.kauclient.KauResult;
import hu.piller.enykp.kauclient.simplified.BaseKauClient;
import hu.piller.enykp.kauclient.simplified.dap.util.ImageExtractor;
import hu.piller.enykp.kauclient.simplified.dap.util.UriExtractor;
import hu.piller.enykp.kauclient.simplified.dap.widget.JImageDialog;
import hu.piller.enykp.util.httpclient.HttpClientFactory;
import hu.piller.enykp.util.httpclient.HttpClientFactoryException;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.swing.SwingUtilities;
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

public class KauClientDap extends BaseKauClient {
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
            if (var10 == null || var10.getValue() == null || var10.getValue().length() == 0) {
               throw new KauClientException("A KAÜ azonosítási módszer kiválasztás előkészítéskor érvénytelen cím érkezett : " + var10.getValue());
            } else {
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

               HttpGet var45 = new HttpGet(var11);
               var8 = var4.execute(var45);
               String var46 = new String(this.readAllBytesFromInputStream(var8.getEntity().getContent()));
               Document var15 = Jsoup.parse(var46);
               Elements var16 = var15.select("input[name$=x]");
               if (var16 != null && var16.size() != 0) {
                  String var17 = ((Element)var16.get(0)).attr("value");
                  var16 = var15.select("input[name$=y]");
                  if (var16 != null && var16.size() != 0) {
                     String var18 = ((Element)var16.get(0)).attr("value");
                     String var19 = "urn:eksz.gov.hu:1.0:azonositas:kau:3:dap:qr";
                     var11 = var11.substring(0, var11.indexOf("authservice"));
                     var11 = var11 + "authservice";
                     var5 = new ArrayList();
                     var5.add(new BasicNameValuePair("authServiceUri", var19));
                     var5.add(new BasicNameValuePair("x", var17));
                     var5.add(new BasicNameValuePair("y", var18));
                     var6 = new HttpPost(var11);
                     var6.setEntity(new UrlEncodedFormEntity(var5, "UTF-8"));
                     var8 = var4.execute(var6);
                     var46 = new String(this.readAllBytesFromInputStream(var8.getEntity().getContent()));
                     var15 = Jsoup.parse(var46);
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
                              if (302 != var9) {
                                 throw new KauClientException("Az IDP QR kód képernyő betöltés előkészítéskor nem elvárt státuszkód érkezett : " + var9);
                              } else {
                                 var10 = var8.getFirstHeader("Location");
                                 if (var10 == null || var10.getValue() == null || var10.getValue().length() == 0) {
                                    throw new KauClientException("A DÁP azonosítás előkészítéskor érvénytelen cím érkezett : " + var10.getValue());
                                 } else {
                                    var11 = var10.getValue();
                                    var45 = new HttpGet(var11);
                                    var8 = var4.execute(var45);
                                    var46 = new String(this.readAllBytesFromInputStream(var8.getEntity().getContent()));
                                    var15 = Jsoup.parse(var46);
                                    Element var23 = var15.select("body main.login-page section div.dap-login#desktop-view div.qr-container img").first();
                                    BufferedImage var24 = ImageExtractor.extractInlineImageFromImgTag(var23.toString());
                                    Element var25 = (Element)var15.select("html script").get(3);
                                    if (var25 == null) {
                                       throw new KauClientException("A QR kódot felmutató weblapon nem találni a dáp hitelesítés azonosítókódját");
                                    } else {
                                       String var26 = UriExtractor.extractFromJScript(var25.toString());
                                       var11 = var11.replaceAll("/authz.*", "") + "/authz-status?request_uri=" + var26;
                                       var45 = new HttpGet(var11);
                                       var8 = var4.execute(var45);
                                       int var27 = var8.getStatusLine().getStatusCode();
                                       if (Boolean.parseBoolean(System.getProperty("kau.trace"))) {
                                          System.out.println("DÁP QR kód beolvasás státusz pollozás " + var27);
                                       }

                                       EntityUtils.consume(var8.getEntity());
                                       JImageDialog var28 = new JImageDialog(MainFrame.thisinstance, var24);
                                       SwingUtilities.invokeLater(() -> {
                                          var28.setVisible(true);
                                       });

                                       for(; var27 != 200 && var27 != 410 && !var28.isCanceled(); EntityUtils.consume(var8.getEntity())) {
                                          try {
                                             Thread.sleep(500L);
                                          } catch (InterruptedException var42) {
                                          }

                                          var8 = var4.execute(var45);
                                          var27 = var8.getStatusLine().getStatusCode();
                                          if (Boolean.parseBoolean(System.getProperty("kau.trace"))) {
                                             System.out.println("DÁP QR kód beolvasás státusz pollozás " + var27);
                                          }
                                       }

                                       SwingUtilities.invokeLater(() -> {
                                          var28.close();
                                       });
                                       String var29 = null;
                                       if (var28.isCanceled()) {
                                          var29 = "A KAÜ DÁP azonosítást a felhasználó megszakította";
                                       }

                                       if (var9 == 410) {
                                          var29 = "A KAÜ DÁP azonosítást a KAÜ egyoldalúan lezárta";
                                       }

                                       if (!var28.isCanceled() && var9 != 410) {
                                          var11 = var11.replaceAll("/authz-status.*", "") + "/authz-ready?request_uri=" + var26 + "&lang=undefined";
                                          var45 = new HttpGet(var11);
                                          var8 = var4.execute(var45);
                                       } else {
                                          var5 = new ArrayList();
                                          var5.add(new BasicNameValuePair("requestUri", var26));
                                          var5.add(new BasicNameValuePair("lang", "hu"));
                                          var11 = var11.replaceAll("/authz.*", "") + "/authz-cancel;jsessionid=";
                                          Iterator var30 = ((DefaultHttpClient)var4).getCookieStore().getCookies().iterator();

                                          while(var30.hasNext()) {
                                             Cookie var31 = (Cookie)var30.next();
                                             if ("JSESSIONID".equalsIgnoreCase(var31.getName())) {
                                                var11 = var11 + var31.getValue();
                                                break;
                                             }
                                          }

                                          var6 = new HttpPost(var11);
                                          var6.setEntity(new UrlEncodedFormEntity(var5, "UTF-8"));
                                          var7 = new BasicHttpParams();
                                          var7.setParameter("http.protocol.handle-redirects", false);
                                          var6.setParams(var7);
                                          var8 = var4.execute(var6);
                                       }

                                       var46 = new String(this.readAllBytesFromInputStream(var8.getEntity().getContent()));
                                       var15 = Jsoup.parse(var46);
                                       var11 = var15.select("body main form").first() != null ? var15.select("body main form").first().attr("action") : null;
                                       String var47 = var15.getElementById("data") != null ? var15.getElementById("data").attr("value") : null;
                                       String var48 = var15.getElementById("data2") != null ? var15.getElementById("data2").attr("value") : null;
                                       String var32 = var15.getElementById("error") != null ? var15.getElementById("error").attr("value") : null;
                                       var5 = new ArrayList();
                                       var5.add(new BasicNameValuePair("sdJwt", var47));
                                       var5.add(new BasicNameValuePair("lang", var48));
                                       var5.add(new BasicNameValuePair("jws", var32));
                                       var6 = new HttpPost(var11);
                                       var6.setEntity(new UrlEncodedFormEntity(var5, "UTF-8"));
                                       var7 = new BasicHttpParams();
                                       var7.setParameter("http.protocol.handle-redirects", false);
                                       var6.setParams(var7);
                                       var8 = var4.execute(var6);
                                       var9 = var8.getStatusLine().getStatusCode();
                                       if (var9 != 200) {
                                          throw new KauClientException("DÁP QR azonosítás befejezés hiba (IDP válasz)");
                                       } else {
                                          var46 = new String(this.readAllBytesFromInputStream(var8.getEntity().getContent()));
                                          var15 = Jsoup.parse(var46);
                                          var11 = var15.select("body main form").first() != null ? var15.select("body main form").first().attr("action") : null;
                                          String var33 = var15.select("input[name=SAMLResponse]") != null ? var15.select("input[name=SAMLResponse]").first().attr("value") : null;
                                          var21 = var15.select("input[name=RelayState]") != null ? var15.select("input[name=RelayState]").first().attr("value") : null;
                                          String var34 = var15.select("input[name=lang]") != null ? var15.select("input[name=lang]").first().attr("value") : null;
                                          var5 = new ArrayList();
                                          var5.add(new BasicNameValuePair("SAMLResponse", var33));
                                          var5.add(new BasicNameValuePair("RelayState", var21));
                                          var5.add(new BasicNameValuePair("lang", var34));
                                          var6 = new HttpPost(var11);
                                          var6.setEntity(new UrlEncodedFormEntity(var5, "UTF-8"));
                                          var7 = new BasicHttpParams();
                                          var7.setParameter("http.protocol.handle-redirects", false);
                                          var6.setParams(var7);
                                          var8 = var4.execute(var6);
                                          var9 = var8.getStatusLine().getStatusCode();
                                          if (var9 != 200) {
                                             throw new KauClientException("DÁP QR azonosítás befejezés hiba (KAÜ válasz)");
                                          } else {
                                             var46 = new String(this.readAllBytesFromInputStream(var8.getEntity().getContent()));
                                             var15 = Jsoup.parse(var46);
                                             var11 = var15.select("body main form").first() != null ? var15.select("body main form").first().attr("action") : null;
                                             var33 = var15.select("input[name=SAMLResponse]") != null ? var15.select("input[name=SAMLResponse]").first().attr("value") : null;
                                             var21 = var15.select("input[name=RelayState]") != null ? var15.select("input[name=RelayState]").first().attr("value") : null;
                                             var34 = var15.select("input[name=lang]") != null ? var15.select("input[name=lang]").first().attr("value") : null;
                                             if (var29 != null) {
                                                throw new KauClientException(var29);
                                             } else {
                                                if (Boolean.parseBoolean(System.getProperty("kau.trace"))) {
                                                   String var35 = String.format("endurl=%s\nSAMLResponse=%s\nRelayState=%s\nlang=%s", var11, var33, var21, var34);
                                                   System.out.println(var35);
                                                }

                                                KauResult var49 = new KauResult();
                                                var49.setRelayState(var21);
                                                var49.setSamlResponse(var33);
                                                Iterator var36 = ((DefaultHttpClient)var4).getCookieStore().getCookies().iterator();

                                                while(true) {
                                                   if (var36.hasNext()) {
                                                      Cookie var37 = (Cookie)var36.next();
                                                      if (!var37.getName().equals(var1.get("cookie"))) {
                                                         continue;
                                                      }

                                                      var49.setCookie(var37.getValue());
                                                   }

                                                   var49.setSubjectConfirmationRequired(Boolean.parseBoolean((String)var1.get("subject_confirmation_required")));
                                                   KauResult var50 = var49;
                                                   return var50;
                                                }
                                             }
                                          }
                                       }
                                    }
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
            }
         }
      } catch (HttpClientFactoryException | IOException var43) {
         var43.printStackTrace();
         throw new KauClientException("A DÁP-os felhasználóazonosítás folyamathiba miatt megszakadt!");
      } finally {
         if (var4 != null) {
            var4.getConnectionManager().shutdown();
         }

      }
   }
}
