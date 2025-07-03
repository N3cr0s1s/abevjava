package hu.piller.enykp.niszws;

import java.util.HashSet;
import java.util.Set;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

public class SecurityHeaderSOAPHandler implements SOAPHandler<SOAPMessageContext> {
   private String samlResponse;
   private String cookie;
   private boolean subject_confirmation_required;

   public SecurityHeaderSOAPHandler(String var1, String var2, boolean var3) {
      this.samlResponse = var1;
      this.cookie = var2;
      this.subject_confirmation_required = var3;
   }

   public boolean handleMessage(SOAPMessageContext var1) {
      boolean var2 = (Boolean)var1.get("javax.xml.ws.handler.message.outbound");
      if (var2) {
         try {
            SOAPEnvelope var3 = var1.getMessage().getSOAPPart().getEnvelope();
            SOAPHeader var4;
            if (var3.getHeader() != null) {
               var4 = var3.getHeader();
            } else {
               var4 = var3.addHeader();
            }

            SOAPElement var5 = var4.addChildElement("Security", "wsse", "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd");
            var5.addAttribute(new QName("xmlns:wsu"), "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd");
            SOAPElement var6 = var5.addChildElement("Assertion", "saml2", "urn:oasis:names:tc:SAML:2.0:assertion");
            var6.addAttribute(new QName("ID"), "Assertion-043c77ca-bb06-11e7-abc4-cec278b6b50a");
            SOAPElement var7 = var6.addChildElement("Issuer", "saml2");
            var7.addTextNode("anyk");
            SOAPElement var8 = var6.addChildElement("Subject", "saml2");
            SOAPElement var9 = var8.addChildElement("NameID", "saml2");
            var9.addAttribute(new QName("Format"), "urn:oasis:names:tc:SAML:2.0:nameid-format:persistent");
            var9.addAttribute(new QName("SPNameQualifier"), "urn:eksz.gov.hu:1.0:saml-response-base64");
            var9.addTextNode(this.samlResponse);
            if (this.subject_confirmation_required) {
               SOAPElement var10 = var8.addChildElement("SubjectConfirmation", "saml2");
               var10.addAttribute(new QName("Method"), "urn:oasis:names:tc:SAML:2.0:cm:bearer");
               SOAPElement var11 = var10.addChildElement("SubjectConfirmationData", "saml2");
               var11.addTextNode(this.cookie);
            }

            var1.getMessage().saveChanges();
         } catch (SOAPException var12) {
            var12.printStackTrace();
         }
      }

      return var2;
   }

   public boolean handleFault(SOAPMessageContext var1) {
      return true;
   }

   public void close(MessageContext var1) {
   }

   public Set<QName> getHeaders() {
      QName var1 = new QName("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd", "Security", "wsse");
      HashSet var2 = new HashSet();
      var2.add(var1);
      return var2;
   }
}
