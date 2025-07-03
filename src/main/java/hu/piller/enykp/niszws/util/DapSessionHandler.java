package hu.piller.enykp.niszws.util;

import hu.piller.enykp.kauclient.KauResult;

public class DapSessionHandler {
   private KauResult kauResult;
   private boolean batchDapUploadInProgress = false;
   private static DapSessionHandler _instance;

   private DapSessionHandler() {
   }

   public static DapSessionHandler getInstance() {
      if (_instance == null) {
         _instance = new DapSessionHandler();
      }

      return _instance;
   }

   public void setKauResult(KauResult var1) {
      this.kauResult = var1;
   }

   public KauResult getKauResult() {
      return this.kauResult;
   }

   public boolean hasCachedKauResult() {
      return this.kauResult != null;
   }

   public void reset() {
      this.kauResult = null;
      this.batchDapUploadInProgress = false;
   }

   public boolean isBatchDapUploadInProgress() {
      return this.batchDapUploadInProgress;
   }

   public void setBatchDapUploadInProgress(boolean var1) {
      this.batchDapUploadInProgress = var1;
   }
}
