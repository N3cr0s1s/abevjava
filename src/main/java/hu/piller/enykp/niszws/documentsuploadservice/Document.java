package hu.piller.enykp.niszws.documentsuploadservice;

import javax.activation.DataHandler;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlMimeType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
   name = "document",
   propOrder = {"name", "copyIntoThePermanentStorage", "encryptReply", "returnReceiptMode", "systemMessage", "data", "dataHandler"}
)
public class Document {
   @XmlElement(
      required = true
   )
   protected String name;
   protected Boolean copyIntoThePermanentStorage;
   protected Boolean encryptReply;
   protected String returnReceiptMode;
   protected Boolean systemMessage;
   protected byte[] data;
   @XmlMimeType("application/octet-stream")
   protected DataHandler dataHandler;

   public String getName() {
      return this.name;
   }

   public void setName(String var1) {
      this.name = var1;
   }

   public Boolean isCopyIntoThePermanentStorage() {
      return this.copyIntoThePermanentStorage;
   }

   public void setCopyIntoThePermanentStorage(Boolean var1) {
      this.copyIntoThePermanentStorage = var1;
   }

   public Boolean isEncryptReply() {
      return this.encryptReply;
   }

   public void setEncryptReply(Boolean var1) {
      this.encryptReply = var1;
   }

   public String getReturnReceiptMode() {
      return this.returnReceiptMode;
   }

   public void setReturnReceiptMode(String var1) {
      this.returnReceiptMode = var1;
   }

   public Boolean isSystemMessage() {
      return this.systemMessage;
   }

   public void setSystemMessage(Boolean var1) {
      this.systemMessage = var1;
   }

   public byte[] getData() {
      return this.data;
   }

   public void setData(byte[] var1) {
      this.data = var1;
   }

   public DataHandler getDataHandler() {
      return this.dataHandler;
   }

   public void setDataHandler(DataHandler var1) {
      this.dataHandler = var1;
   }
}
