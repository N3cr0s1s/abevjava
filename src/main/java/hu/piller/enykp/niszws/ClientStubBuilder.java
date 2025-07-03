package hu.piller.enykp.niszws;

import com.sun.xml.ws.api.message.Header;
import com.sun.xml.ws.api.message.Headers;
import com.sun.xml.ws.developer.WSBindingProvider;
import java.net.URL;
import java.util.Map;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;
import javax.xml.ws.soap.MTOMFeature;

public class ClientStubBuilder {
   private static final String PATH_WSDL = "/resources/niszws/";

   public <T> T get(String var1, String var2, String var3, Class<T> var4, boolean var5, String var6, String var7, String var8, String var9, String var10, String var11, boolean var12) {
      Service var13 = this.createService(var1, var2, var3);
      Object var14 = this.createPort(var13, var4, var5, var6, var7, var8, var9);

      try {
         SOAPFactory var15 = SOAPFactory.newInstance();
         SOAPElement var16 = var15.createElement("Security", "wsse", "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd");
         var16.addAttribute(new QName("xmlns:wsu"), "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd");
         SOAPElement var17 = var16.addChildElement("Assertion", "saml2", "urn:oasis:names:tc:SAML:2.0:assertion");
         var17.addAttribute(new QName("ID"), "Assertion-043c77ca-bb06-11e7-abc4-cec278b6b50a");
         SOAPElement var18 = var17.addChildElement("Issuer", "saml2");
         var18.addTextNode("anyk");
         SOAPElement var19 = var17.addChildElement("Subject", "saml2");
         SOAPElement var20 = var19.addChildElement("NameID", "saml2");
         var20.addAttribute(new QName("Format"), "urn:oasis:names:tc:SAML:2.0:nameid-format:persistent");
         var20.addAttribute(new QName("SPNameQualifier"), "urn:eksz.gov.hu:1.0:saml-response-base64");
         var20.addTextNode(var10);
         if (var12) {
            SOAPElement var21 = var19.addChildElement("SubjectConfirmation", "saml2");
            var21.addAttribute(new QName("Method"), "urn:oasis:names:tc:SAML:2.0:cm:bearer");
            SOAPElement var22 = var21.addChildElement("SubjectConfirmationData", "saml2");
            var22.addTextNode(var11);
         }

         ((WSBindingProvider)var14).setOutboundHeaders(new Header[]{Headers.create(var16)});
      } catch (SOAPException var23) {
         var23.printStackTrace();
      }

      return (T) var14;
   }

   public <T> T get(String var1, String var2, String var3, Class<T> var4, String var5, String var6, String var7, String var8) {
      return this.get(var1, var2, var3, var4, false, var5, var6, var7, var8);
   }

   public <T> T get(String var1, String var2, String var3, Class<T> var4, boolean var5, String var6, String var7, String var8, String var9) {
      Service var10 = this.createService(var1, var2, var3);
      return this.createPort(var10, var4, var5, var6, var7, var8, var9);
   }

   private <T> T createPort(Service var1, Class<T> var2, boolean var3, String var4, String var5, String var6, String var7) {
      Object var8;
      if (var3) {
         var8 = var1.getPort(var2, new MTOMFeature());
         Map var9 = ((BindingProvider)var8).getRequestContext();
         var9.put("com.sun.xml.internal.ws.transport.http.client.streaming.chunk.size", 8192);
      } else {
         var8 = var1.getPort(var2);
      }

      String var11 = var4 + "://" + var5 + ("".equals(var6) ? "" : ":" + var6) + var7 + "?wsdl";
      Map var10 = ((BindingProvider)var8).getRequestContext();
      var10.put("javax.xml.ws.service.endpoint.address", var11);
      return (T) var8;
   }

   private Service createService(String var1, String var2, String var3) {
      URL var4 = ClientStubBuilder.class.getResource("/resources/niszws/" + var1);
      QName var5 = new QName(var3, var2);
      return Service.create(var4, var5);
   }
}
