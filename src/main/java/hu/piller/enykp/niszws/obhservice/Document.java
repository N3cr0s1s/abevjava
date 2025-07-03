package hu.piller.enykp.niszws.obhservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
   name = "document",
   propOrder = {"docType", "docBytes", "fileName", "mimeType", "signerType", "fileInResponse"}
)
public class Document {
   @XmlElement(
      required = true
   )
   protected String docType;
   @XmlElement(
      required = true
   )
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
   protected boolean fileInResponse;

   public String getDocType() {
      return this.docType;
   }

   public void setDocType(String var1) {
      this.docType = var1;
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

   public boolean isFileInResponse() {
      return this.fileInResponse;
   }

   public void setFileInResponse(boolean var1) {
      this.fileInResponse = var1;
   }
}
