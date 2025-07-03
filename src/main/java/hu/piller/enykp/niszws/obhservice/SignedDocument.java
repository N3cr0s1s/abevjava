package hu.piller.enykp.niszws.obhservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
   name = "signedDocument",
   propOrder = {"originalFileName", "docBytes", "fileName", "mimeType", "signerType", "documentId", "documentUrl"}
)
public class SignedDocument {
   @XmlElement(
      required = true
   )
   protected String originalFileName;
   protected byte[] docBytes;
   @XmlElement(
      required = true
   )
   protected String fileName;
   @XmlElement(
      required = true
   )
   protected String mimeType;
   @XmlElement(
      required = true
   )
   @XmlSchemaType(
      name = "string"
   )
   protected SignerTypeEnum signerType;
   @XmlElement(
      required = true
   )
   protected String documentId;
   @XmlElement(
      required = true
   )
   protected String documentUrl;

   public String getOriginalFileName() {
      return this.originalFileName;
   }

   public void setOriginalFileName(String var1) {
      this.originalFileName = var1;
   }

   public byte[] getDocBytes() {
      return this.docBytes;
   }

   public void setDocBytes(byte[] var1) {
      this.docBytes = var1;
   }

   public String getFileName() {
      return this.fileName;
   }

   public void setFileName(String var1) {
      this.fileName = var1;
   }

   public String getMimeType() {
      return this.mimeType;
   }

   public void setMimeType(String var1) {
      this.mimeType = var1;
   }

   public SignerTypeEnum getSignerType() {
      return this.signerType;
   }

   public void setSignerType(SignerTypeEnum var1) {
      this.signerType = var1;
   }

   public String getDocumentId() {
      return this.documentId;
   }

   public void setDocumentId(String var1) {
      this.documentId = var1;
   }

   public String getDocumentUrl() {
      return this.documentUrl;
   }

   public void setDocumentUrl(String var1) {
      this.documentUrl = var1;
   }
}
