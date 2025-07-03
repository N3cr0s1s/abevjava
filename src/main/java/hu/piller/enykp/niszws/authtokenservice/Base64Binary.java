package hu.piller.enykp.niszws.authtokenservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
   name = "base64Binary",
   namespace = "http://www.w3.org/2005/05/xmlmime",
   propOrder = {"value"}
)
public class Base64Binary {
   @XmlValue
   protected byte[] value;
   @XmlAttribute(
      name = "contentType",
      namespace = "http://www.w3.org/2005/05/xmlmime"
   )
   protected String contentType;

   public byte[] getValue() {
      return this.value;
   }

   public void setValue(byte[] var1) {
      this.value = var1;
   }

   public String getContentType() {
      return this.contentType;
   }

   public void setContentType(String var1) {
      this.contentType = var1;
   }
}
