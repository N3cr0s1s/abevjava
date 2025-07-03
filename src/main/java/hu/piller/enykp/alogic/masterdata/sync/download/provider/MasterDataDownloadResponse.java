package hu.piller.enykp.alogic.masterdata.sync.download.provider;

public class MasterDataDownloadResponse {
   private long pollInterval;
   private byte[] result;

   public long getPollInterval() {
      return this.pollInterval;
   }

   public void setPollInterval(long var1) {
      this.pollInterval = var1;
   }

   public byte[] getResult() {
      return this.result;
   }

   public void setResult(byte[] var1) {
      this.result = var1;
   }

   public boolean hasResult() {
      return this.result != null;
   }
}
