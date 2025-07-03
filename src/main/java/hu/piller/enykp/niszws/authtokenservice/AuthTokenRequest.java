package hu.piller.enykp.niszws.authtokenservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
   name = "authTokenRequest",
   propOrder = {"id", "audience"}
)
public class AuthTokenRequest {
   @XmlElement(
      required = true
   )
   protected String id;
   @XmlElement(
      required = true
   )
   protected String audience;

   public String getId() {
      return this.id;
   }

   public void setId(String var1) {
      this.id = var1;
   }

   public String getAudience() {
      return this.audience;
   }

   public void setAudience(String var1) {
      this.audience = var1;
   }
}
