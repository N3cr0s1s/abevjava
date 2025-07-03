package hu.piller.enykp.niszws.authtokenservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
   name = "officeAuthentication",
   propOrder = {"shortName"}
)
public class OfficeAuthentication {
   @XmlElement(
      required = true
   )
   protected String shortName;

   public String getShortName() {
      return this.shortName;
   }

   public void setShortName(String var1) {
      this.shortName = var1;
   }
}
