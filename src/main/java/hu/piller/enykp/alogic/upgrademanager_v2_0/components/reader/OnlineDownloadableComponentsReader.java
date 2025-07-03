package hu.piller.enykp.alogic.upgrademanager_v2_0.components.reader;

import hu.piller.enykp.alogic.orghandler.OrgInfo;
import hu.piller.enykp.alogic.upgrademanager_v2_0.UpgradeLogger;
import hu.piller.enykp.alogic.upgrademanager_v2_0.versiondataconverters.VersionData;
import hu.piller.enykp.alogic.upgrademanager_v2_0.versiondataproviders.downloadablecomponents.DownloadableVersionDataProvider;
import hu.piller.enykp.util.base.ErrorList;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.Vector;

public class OnlineDownloadableComponentsReader implements ComponentsReader {
   private static final int UJ = 0;
   private static final int KIEJTO = 1;
   private static final int DUPLIKATUM = 2;
   private String[] orgs;
   private TreeSet<String> orgsNotConnected = new TreeSet();

   public void setOrgs(String[] var1) {
      this.orgs = var1;
   }

   public String[] getOrgsNotConnected() {
      return (String[])this.orgsNotConnected.toArray(new String[this.orgsNotConnected.size()]);
   }

   public Collection getComponents() {
      Vector var1 = new Vector();
      Vector var2 = new Vector();

      try {
         var2.addAll(OrgInfo.getInstance().getUpgradeURLAllOrganizations(this.orgs));
         StringBuffer var3 = new StringBuffer();
         int var4 = 0;

         while(true) {
            if (var4 >= var2.size()) {
               UpgradeLogger.getInstance().log(Thread.currentThread().getName() + " ### UpgradeManager : frissítés leíró ellenőrzés alá vont szervezetek azonosítói " + var3.toString());
               break;
            }

            var3.append(" ").append(((URLOrg)var2.get(var4)).org);
            ++var4;
         }
      } catch (Exception var19) {
         UpgradeLogger.getInstance().log(var19);
         ErrorList.getInstance().writeError("Frissítés", "Hiba a szervezet lista összeállításakor: " + var19.getMessage(), 10001, var19, (Object)null);
         return var1;
      }

      DownloadableVersionDataProvider var20 = new DownloadableVersionDataProvider();
      boolean var21 = false;

      for(int var5 = 0; var5 < var2.size(); ++var5) {
         if (!var21) {
            URLOrg var6 = (URLOrg)var2.get(var5);
            var20.setURL(var6.url);
            long var7 = System.currentTimeMillis();
            boolean var11 = false;

            try {
               UpgradeLogger.getInstance().log(Thread.currentThread().getName() + " ### UpgradeManager : publikálási adatok beszerzése a(z) " + var6.org + " azonosítójú szervezet szerveréről (" + (var5 + 1) + "/" + var2.size() + " feladat) ... ");
               var20.collect();
               Collection var12 = this.orgFilter(var20.getCollection());
               this.checkAndFill(var1, var12);
               var11 = true;
            } catch (Exception var17) {
               this.orgsNotConnected.add(var6.org);
               this.addToErrorList(var17, var6.org);
               if (var17.getCause() != null && var17.getCause() instanceof InterruptedException) {
                  var21 = true;
               }
            } finally {
               long var9 = System.currentTimeMillis();
               if (var11) {
                  UpgradeLogger.getInstance().log(Thread.currentThread().getName() + " ### UpgradeManager : publikálási adatok beszerzése a(z) " + var6.org + " azonosítójú szervezet szerveréről sikeres volt [" + (var9 - var7) + " msec]");
               } else {
                  UpgradeLogger.getInstance().log(Thread.currentThread().getName() + " ### UpgradeManager : publikálási adatok beszerzése a(z) " + var6.org + " azonosítójú szervezet szerveréről SIKERTELEN volt [" + (var9 - var7) + " msec]");
               }

            }
         }
      }

      if (var21) {
         UpgradeLogger.getInstance().log(Thread.currentThread().getName() + " ### UpgradeManager : összetevők verzióinformációnak begyűjtése megszakítva, részeredmények törölve.");
         this.orgsNotConnected.clear();
         var1.clear();
      }

      return var1;
   }

   private void addToErrorList(Exception var1, String var2) {
      Exception var3 = var1.getCause() == null ? var1 : new Exception(var1.getCause());
      String var4 = (new SimpleDateFormat("[yyyy.MM.dd] [kk:mm:ss.SSS]")).format(new Date());
      ErrorList.getInstance().writeError("Frissítés", var4 + " Hiba a(z) '" + var2 + "' szervezet nyomtatványfrissítéseinek lekérdezésekor: " + var1.getMessage(), 10001, var3, (Object)null);
   }

   private void checkAndFill(Collection var1, Collection var2) {
      Vector var3 = new Vector();
      Vector var4 = new Vector();
      Iterator var5 = var2.iterator();

      while(true) {
         VersionData var6;
         boolean var7;
         do {
            if (!var5.hasNext()) {
               var1.addAll(var3);
               var1.removeAll(var4);
               return;
            }

            var6 = (VersionData)var5.next();
            var7 = false;
            Iterator var8 = var1.iterator();

            while(var8.hasNext()) {
               VersionData var9 = (VersionData)var8.next();
               if (var6.getCategory().equals(var9.getCategory()) && var6.getOrganization().equals(var9.getOrganization()) && var6.getName().equals(var9.getName())) {
                  if (var6.getVersion().compareTo(var9.getVersion()) > 0) {
                     var7 = true;
                     var4.add(var9);
                  } else {
                     var7 = true;
                  }
                  break;
               }
            }
         } while(var7 && !var7);

         var3.add(var6);
      }
   }

   private Collection orgFilter(Collection var1) {
      Vector var2 = new Vector();
      Iterator var3 = var1.iterator();

      while(var3.hasNext()) {
         VersionData var4 = (VersionData)var3.next();
         if (this.isOrgIdListed(var4.getOrganization())) {
            var2.add(var4);
         }
      }

      return var2;
   }

   private boolean isOrgIdListed(String var1) {
      if (this.orgs == null) {
         return true;
      } else {
         String[] var2 = this.orgs;
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            String var5 = var2[var4];
            if (var5.equals(var1)) {
               return true;
            }
         }

         return false;
      }
   }
}
