package hu.piller.enykp.niszws.documentsuploadservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
   name = "authTokenResponse",
   propOrder = {"id", "subjectConfirmationRequired", "cookie", "ssoAuthUrl", "spRespUrl", "relayState", "samlRequest"}
)
public class AuthTokenResponse {
   @XmlElement(
      required = true
   )
   protected String id;
   protected boolean subjectConfirmationRequired;
   @XmlElement(
      required = true
   )
   protected String cookie;
   @XmlElement(
      required = true
   )
   protected String ssoAuthUrl;
   @XmlElement(
      required = true
   )
   protected String spRespUrl;
   @XmlElement(
      required = true
   )
   protected String relayState;
   @XmlElement(
      required = true
   )
   protected String samlRequest;

   public String getId() {
      return this.id;
   }

   public void setId(String var1) {
      this.id = var1;
   }

   public boolean isSubjectConfirmationRequired() {
      return this.subjectConfirmationRequired;
   }

   public void setSubjectConfirmationRequired(boolean var1) {
      this.subjectConfirmationRequired = var1;
   }

   public String getCookie() {
      return this.cookie;
   }

   public void setCookie(String var1) {
      this.cookie = var1;
   }

   public String getSsoAuthUrl() {
      return this.ssoAuthUrl;
   }

   public void setSsoAuthUrl(String var1) {
      this.ssoAuthUrl = var1;
   }

   public String getSpRespUrl() {
      return this.spRespUrl;
   }

   public void setSpRespUrl(String var1) {
      this.spRespUrl = var1;
   }

   public String getRelayState() {
      return this.relayState;
   }

   public void setRelayState(String var1) {
      this.relayState = var1;
   }

   public String getSamlRequest() {
      return this.samlRequest;
   }

   public void setSamlRequest(String var1) {
      this.samlRequest = var1;
   }
}
