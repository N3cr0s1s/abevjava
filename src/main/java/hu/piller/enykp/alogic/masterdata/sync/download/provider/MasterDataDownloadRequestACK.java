package hu.piller.enykp.alogic.masterdata.sync.download.provider;

import java.util.HashSet;
import java.util.Set;

public class MasterDataDownloadRequestACK {
   private long pollInterval;
   private String queryId;
   private Set<String> refusedIds = new HashSet();

   public String getQueryId() {
      return this.queryId;
   }

   public void setQueryId(String var1) {
      this.queryId = var1;
   }

   public long getPollInterval() {
      return this.pollInterval;
   }

   public void setPollInterval(long var1) {
      this.pollInterval = var1;
   }

   public Set<String> getRefusedIds() {
      return this.refusedIds;
   }
}
