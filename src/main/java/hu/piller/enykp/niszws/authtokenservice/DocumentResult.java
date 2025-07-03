package hu.piller.enykp.niszws.authtokenservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
   name = "documentResult",
   propOrder = {"name", "stored", "filingNumber", "causeOfFail"}
)
public class DocumentResult {
   @XmlElement(
      required = true
   )
   protected String name;
   protected boolean stored;
   protected String filingNumber;
   protected String causeOfFail;

   public String getName() {
      return this.name;
   }

   public void setName(String var1) {
      this.name = var1;
   }

   public boolean isStored() {
      return this.stored;
   }

   public void setStored(boolean var1) {
      this.stored = var1;
   }

   public String getFilingNumber() {
      return this.filingNumber;
   }

   public void setFilingNumber(String var1) {
      this.filingNumber = var1;
   }

   public String getCauseOfFail() {
      return this.causeOfFail;
   }

   public void setCauseOfFail(String var1) {
      this.causeOfFail = var1;
   }
}
