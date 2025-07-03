package hu.piller.enykp.kauclient;

import java.io.Serializable;

public class KauResult implements Serializable {
   private static final long serialVersionUID = 1L;
   private String samlResponse;
   private String relayState;
   private String cookie;
   private boolean subject_confirmation_required;

   public String getSamlResponse() {
      return this.samlResponse;
   }

   public void setSamlResponse(String var1) {
      this.samlResponse = var1;
   }

   public String getRelayState() {
      return this.relayState;
   }

   public void setRelayState(String var1) {
      this.relayState = var1;
   }

   public String getCookie() {
      return this.cookie;
   }

   public void setCookie(String var1) {
      this.cookie = var1;
   }

   public boolean isSubjectConfirmationRequired() {
      return this.subject_confirmation_required;
   }

   public void setSubjectConfirmationRequired(boolean var1) {
      this.subject_confirmation_required = var1;
   }

   public String toString() {
      return "KauResult [samlResponse=" + this.samlResponse + ", relayState=" + this.relayState + ", cookie=" + this.cookie + ", subject_confirmation_required=" + this.subject_confirmation_required + "]";
   }
}
