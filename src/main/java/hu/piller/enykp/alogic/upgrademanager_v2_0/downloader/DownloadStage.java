package hu.piller.enykp.alogic.upgrademanager_v2_0.downloader;

import hu.piller.enykp.alogic.upgrademanager_v2_0.components.DownloadableComponents;
import hu.piller.enykp.alogic.upgrademanager_v2_0.versiondataconverters.VersionData;
import hu.piller.enykp.util.base.ErrorList;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

public class DownloadStage extends Stage<VersionData, VersionData> {
   String arch;
   String to;
   CountDownLatch latch;
   DownloadableComponents pipeline;

   public DownloadStage(String var1, String var2, CountDownLatch var3, DownloadableComponents var4, boolean var5, boolean var6, boolean var7) {
      super("Download", 5, 5, var5, var6, var7);
      this.to = var2;
      this.arch = var1;
      this.latch = var3;
      this.pipeline = var4;
      this.prestartAllCoreThreads();
   }

   protected Callable<VersionData> getCallable(final VersionData var1) {
      return new Callable() {
         public VersionData call() throws Exception {
            DownloadStage.this.pipeline.fireComponentProcessedEvent(var1, (byte)2, "");
            FileDownloader var1x = new FileDownloader();
            var1x.setPipeLine(DownloadStage.this.pipeline);
            var1x.download(DownloadStage.this.arch, var1, DownloadStage.this.to);
            return var1;
         }
      };
   }

   protected void onSuccess(VersionData var1, VersionData var2) {
      this.latch.countDown();
   }

   protected void onError(VersionData var1, String var2) {
      try {
         String var3 = (new SimpleDateFormat("[yyyy.MM.dd] [kk:mm:ss.SSS]")).format(new Date());
         this.pipeline.fireComponentProcessedEvent(var1, (byte)1, var2);
         ErrorList.getInstance().store(ErrorList.LEVEL_ERROR, var3 + " Letöltés sikertelen : " + var1.getOrganization() + " " + var1.getName() + ". Részletek a frissítési naplófájlban.", (Exception)null, (Object)null);
      } finally {
         this.latch.countDown();
         this.latch.countDown();
      }

   }
}
