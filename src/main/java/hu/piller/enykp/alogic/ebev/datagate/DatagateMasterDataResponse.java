package hu.piller.enykp.alogic.ebev.datagate;

import java.util.ArrayList;
import java.util.List;

public class DatagateMasterDataResponse {
   private String queryid = "";
   private List<String> azonositok = new ArrayList();
   private long pollinterval = 3L;
   private String errcode = "";
   private String errmsg = "";

   public String getQueryid() {
      return this.queryid;
   }

   public void setQueryid(String var1) {
      this.queryid = var1;
   }

   public List<String> getAzonositok() {
      return this.azonositok;
   }

   public long getPollinterval() {
      return this.pollinterval;
   }

   public void setPollinterval(long var1) {
      this.pollinterval = var1;
   }

   public String getErrcode() {
      return this.errcode;
   }

   public void setErrcode(String var1) {
      this.errcode = var1;
   }

   public String getErrmsg() {
      return this.errmsg;
   }

   public void setErrmsg(String var1) {
      this.errmsg = var1;
   }
}
