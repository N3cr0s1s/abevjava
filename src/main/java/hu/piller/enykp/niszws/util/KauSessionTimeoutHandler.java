package hu.piller.enykp.niszws.util;

import hu.piller.enykp.alogic.settingspanel.SettingsStore;
import hu.piller.enykp.kauclient.KauResult;

public class KauSessionTimeoutHandler {
   private static long DEFAULT_KAU_TIMEOUT = 1200000L;
   private long currentKauTimeout;
   private KauResult kauResult;
   private long lastKauAuthenticationTimeStamp;
   private static KauSessionTimeoutHandler _instance;

   private KauSessionTimeoutHandler() {
      this.currentKauTimeout = DEFAULT_KAU_TIMEOUT;
   }

   public static KauSessionTimeoutHandler getInstance() {
      if (_instance == null) {
         _instance = new KauSessionTimeoutHandler();
      }

      return _instance;
   }

   public void setKauResult(KauResult var1) {
      this.kauResult = var1;
      this.lastKauAuthenticationTimeStamp = System.currentTimeMillis();
   }

   public KauResult getKauResult() {
      return this.kauResult;
   }

   public boolean hasCachedKauResult() {
      return this.kauResult != null;
   }

   public boolean isUploadable(long var1) {
      String var4 = null;

      float var3;
      try {
         var4 = SettingsStore.getInstance().get("gui", "uploadbandwith");
         var3 = Float.valueOf(var4);
      } catch (Exception var11) {
         System.out.println("Érvénytelen kimenő sávszélesség érték : '" + var4 + "', beállítva az alapértelmezett 1 Mbps -ra");
         SettingsStore.getInstance().set("gui", "uploadbandwith", "1");
         var3 = 1.0F;
      }

      long var5 = System.currentTimeMillis();
      long var7 = var5 - this.lastKauAuthenticationTimeStamp;
      long var9 = this.getThreshold(var1, var3);
      System.out.println("currentTime=" + var5 + ", lastKauAuthenticationTimeStamp=" + this.lastKauAuthenticationTimeStamp + ", diff=" + var7 + ", threshold=" + var9 + ", currentKauTime-threshold=" + (this.currentKauTimeout - var9));
      if (var7 >= this.currentKauTimeout - var9) {
         System.out.println("FALSE nem tölthető");
         this.reset();
         return false;
      } else {
         System.out.println("TRUE tölthető");
         return true;
      }
   }

   public void reset() {
      this.kauResult = null;
      this.lastKauAuthenticationTimeStamp = 0L;
   }

   public long getThreshold(long var1, float var3) {
      double var4 = (double)var3 * 1024.0D * 1024.0D / 8.0D;
      double var6 = (double)var1 / var4;
      System.out.println("Becsült feltöltési idő " + var1 + " byte kr fájl feltöltésre " + var3 + " Mbps kapcsolaton : " + var6 + " sec (kerekítve)");
      return (long)(var6 * 1000.0D);
   }
}
