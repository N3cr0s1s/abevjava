package hu.piller.enykp.alogic.fileloader.db;

import java.util.Vector;

public class OnyaXmlLoadResult {
   private Vector<OnyaErrorListElement> errorList;
   private boolean ok;
   private String errorMsg;
   private String requestMessageId;

   public OnyaXmlLoadResult() {
   }

   public OnyaXmlLoadResult(Vector var1) {
      this.errorList = var1;
   }

   public Vector getErrorList() {
      return this.errorList;
   }

   public void setErrorList(Vector var1) {
      this.errorList = new Vector(var1);
   }

   public boolean isOk() {
      return this.ok;
   }

   public void setOk(boolean var1) {
      this.ok = var1;
   }

   public String getErrorMsg() {
      return this.errorMsg;
   }

   public void setErrorMsg(String var1) {
      this.errorMsg = var1;
   }

   public String getRequestMessageId() {
      return this.requestMessageId;
   }

   public void setRequestMessageId(String var1) {
      this.requestMessageId = var1;
   }
}
