package hu.piller.enykp.niszws.documentsuploadservice;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
   name = "documentsUploadResponse",
   propOrder = {"results", "causeOfFail"}
)
public class DocumentsUploadResponse {
   protected List<DocumentResult> results;
   protected String causeOfFail;

   public List<DocumentResult> getResults() {
      if (this.results == null) {
         this.results = new ArrayList();
      }

      return this.results;
   }

   public String getCauseOfFail() {
      return this.causeOfFail;
   }

   public void setCauseOfFail(String var1) {
      this.causeOfFail = var1;
   }
}
