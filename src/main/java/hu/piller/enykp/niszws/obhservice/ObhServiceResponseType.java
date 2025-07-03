package hu.piller.enykp.niszws.obhservice;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
   name = "obhServiceResponseType",
   propOrder = {"originalUid", "documents", "error"}
)
public class ObhServiceResponseType {
   @XmlElement(
      required = true
   )
   protected String originalUid;
   protected ObhServiceResponseType.Documents documents;
   protected Error error;

   public String getOriginalUid() {
      return this.originalUid;
   }

   public void setOriginalUid(String var1) {
      this.originalUid = var1;
   }

   public ObhServiceResponseType.Documents getDocuments() {
      return this.documents;
   }

   public void setDocuments(ObhServiceResponseType.Documents var1) {
      this.documents = var1;
   }

   public Error getError() {
      return this.error;
   }

   public void setError(Error var1) {
      this.error = var1;
   }

   @XmlAccessorType(XmlAccessType.FIELD)
   @XmlType(
      name = "",
      propOrder = {"document"}
   )
   public static class Documents {
      @XmlElement(
         required = true
      )
      protected List<SignedDocument> document;

      public List<SignedDocument> getDocument() {
         if (this.document == null) {
            this.document = new ArrayList();
         }

         return this.document;
      }
   }
}
