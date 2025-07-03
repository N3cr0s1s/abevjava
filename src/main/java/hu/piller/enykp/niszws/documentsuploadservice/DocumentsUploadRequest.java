package hu.piller.enykp.niszws.documentsuploadservice;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
   name = "documentsUploadRequest",
   propOrder = {"officeAuthentication", "documents"}
)
public class DocumentsUploadRequest {
   protected OfficeAuthentication officeAuthentication;
   @XmlElement(
      required = true
   )
   protected List<Document> documents;

   public OfficeAuthentication getOfficeAuthentication() {
      return this.officeAuthentication;
   }

   public void setOfficeAuthentication(OfficeAuthentication var1) {
      this.officeAuthentication = var1;
   }

   public List<Document> getDocuments() {
      if (this.documents == null) {
         this.documents = new ArrayList();
      }

      return this.documents;
   }
}
