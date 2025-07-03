package hu.piller.enykp.alogic.upgrademanager_v2_0.downloader;

import hu.piller.enykp.alogic.upgrademanager_v2_0.components.DownloadableComponents;
import hu.piller.enykp.alogic.upgrademanager_v2_0.versiondataconverters.VersionData;
import hu.piller.enykp.util.base.ErrorList;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

public class ExtractStage extends Stage<VersionData, VersionData> {
   String from;
   String to;
   CountDownLatch latch;
   DownloadableComponents pipeline;

   public ExtractStage(String var1, String var2, CountDownLatch var3, DownloadableComponents var4, boolean var5, boolean var6, boolean var7) {
      super("Extract", 5, 5, var5, var6, var7);
      this.to = var2;
      this.from = var1;
      this.latch = var3;
      this.pipeline = var4;
      this.prestartAllCoreThreads();
   }

   protected Callable<VersionData> getCallable(final VersionData var1) {
      return new Callable() {
         public VersionData call() throws Exception {
            if (ExtractStage.this.isInstallable(var1)) {
               ExtractStage.this.pipeline.fireComponentProcessedEvent(var1, (byte)3, "");
               FileDownloader var1x = new FileDownloader();
               var1x.setPipeLine(ExtractStage.this.pipeline);
               var1x.install(var1, ExtractStage.this.from, ExtractStage.this.to);
            }

            return var1;
         }
      };
   }

   private boolean isInstallable(VersionData var1) {
      boolean var2 = false;
      if ("Template".equals(var1.getCategory()) || "Help".equals(var1.getCategory()) || "Orgresource".equals(var1.getCategory())) {
         var2 = true;
      }

      return var2;
   }

   protected void onSuccess(VersionData var1, VersionData var2) {
      try {
         this.pipeline.fireComponentProcessedEvent(var1, (byte)0, "");
      } finally {
         this.latch.countDown();
      }

   }

   protected void onError(VersionData var1, String var2) {
      try {
         String var3 = (new SimpleDateFormat("[yyyy.MM.dd] [kk:mm:ss.SSS]")).format(new Date());
         ErrorList.getInstance().store(ErrorList.LEVEL_ERROR, var3 + " Telepítés sikertelen : " + var1.getOrganization() + " " + var1.getName() + ". Részletek a frissítési naplófájlban.", (Exception)null, (Object)null);
         this.pipeline.fireComponentProcessedEvent(var1, (byte)1, var2);
      } finally {
         this.latch.countDown();
      }

   }
}
