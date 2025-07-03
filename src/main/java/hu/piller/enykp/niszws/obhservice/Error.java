package hu.piller.enykp.niszws.obhservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
   name = "error",
   propOrder = {"errorCode", "errorMessage"}
)
public class Error {
   @XmlElement(
      required = true
   )
   protected String errorCode;
   @XmlElement(
      required = true
   )
   protected String errorMessage;

   public String getErrorCode() {
      return this.errorCode;
   }

   public void setErrorCode(String var1) {
      this.errorCode = var1;
   }

   public String getErrorMessage() {
      return this.errorMessage;
   }

   public void setErrorMessage(String var1) {
      this.errorMessage = var1;
   }
}
