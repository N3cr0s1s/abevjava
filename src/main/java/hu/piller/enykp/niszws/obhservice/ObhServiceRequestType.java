package hu.piller.enykp.niszws.obhservice;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
   name = "obhServiceRequestType",
   propOrder = {"uid", "sender", "documents"}
)
public class ObhServiceRequestType {
   @XmlElement(
      required = true
   )
   protected String uid;
   @XmlElement(
      required = true
   )
   protected String sender;
   @XmlElement(
      required = true
   )
   protected ObhServiceRequestType.Documents documents;

   public String getUid() {
      return this.uid;
   }

   public void setUid(String var1) {
      this.uid = var1;
   }

   public String getSender() {
      return this.sender;
   }

   public void setSender(String var1) {
      this.sender = var1;
   }

   public ObhServiceRequestType.Documents getDocuments() {
      return this.documents;
   }

   public void setDocuments(ObhServiceRequestType.Documents var1) {
      this.documents = var1;
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
      protected List<Document> document;

      public List<Document> getDocument() {
         if (this.document == null) {
            this.document = new ArrayList();
         }

         return this.document;
      }
   }
}
