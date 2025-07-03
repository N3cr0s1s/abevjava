package hu.piller.enykp.alogic.upgrademanager_v2_0.lookupupgrades;

import hu.piller.enykp.alogic.upgrademanager_v2_0.UpgradeLogger;
import hu.piller.enykp.alogic.upgrademanager_v2_0.components.DownloadableComponents;
import hu.piller.enykp.alogic.upgrademanager_v2_0.components.RevokedComponents;
import hu.piller.enykp.alogic.upgrademanager_v2_0.components.UpgradableComponents;
import hu.piller.enykp.util.base.PropertyList;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

public class LookUpUpgrades implements Runnable {
   private List<ILookUpEventListener> listeners = new ArrayList();

   public void addLookUpEventListener(ILookUpEventListener var1) {
      if (var1 != null) {
         this.listeners.add(var1);
      }

   }

   public void fireLookUpEvent(LookUpEvent var1) {
      UpgradeLogger.getInstance().log(Thread.currentThread().getName() + " ### UpgradeManager : EVENT=" + var1.toString());
      Iterator var2 = this.listeners.iterator();

      while(var2.hasNext()) {
         ILookUpEventListener var3 = (ILookUpEventListener)var2.next();
         var3.handleLookUpEvent(var1);
      }

   }

   private void lookUpUpgrades() {
      this.fireLookUpEvent(LookUpEvent.START_LOOKUP);
      DownloadableComponents var1 = new DownloadableComponents();
      PropertyList.getInstance().set("orgs_notconnected.cache", var1.getOrgsNotConnected());
      UpgradableComponents var2 = new UpgradableComponents(var1);
      PropertyList.getInstance().set("upg.cache", new Vector(var2.getComponents()));
      RevokedComponents var3 = new RevokedComponents(var1);
      PropertyList.getInstance().set("revoked.cache", new Vector(var3.getComponents()));
      this.fireLookUpEvent(LookUpEvent.FINISH_LOOKUP);
      if (!this.isEmpty((Vector)PropertyList.getInstance().get("upg.cache"))) {
         this.fireLookUpEvent(LookUpEvent.UPGRADES_FOUND);
      }

   }

   public void run() {
      this.lookUpUpgrades();
   }

   private boolean isEmpty(Vector var1) {
      boolean var2;
      if (var1 == null) {
         var2 = true;
      } else {
         var2 = var1.isEmpty();
      }

      return var2;
   }
}
