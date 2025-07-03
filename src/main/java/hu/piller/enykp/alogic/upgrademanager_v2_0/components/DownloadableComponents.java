package hu.piller.enykp.alogic.upgrademanager_v2_0.components;

import hu.piller.enykp.alogic.orghandler.OrgInfo;
import hu.piller.enykp.alogic.upgrademanager_v2_0.Directories;
import hu.piller.enykp.alogic.upgrademanager_v2_0.UpgradeBusinessException;
import hu.piller.enykp.alogic.upgrademanager_v2_0.UpgradeLogger;
import hu.piller.enykp.alogic.upgrademanager_v2_0.UpgradeTechnicalException;
import hu.piller.enykp.alogic.upgrademanager_v2_0.components.event.ComponentProcessingEvent;
import hu.piller.enykp.alogic.upgrademanager_v2_0.components.event.ComponentProcessingEventListener;
import hu.piller.enykp.alogic.upgrademanager_v2_0.components.reader.ComponentsReader;
import hu.piller.enykp.alogic.upgrademanager_v2_0.components.reader.ComponentsReaderFactory;
import hu.piller.enykp.alogic.upgrademanager_v2_0.downloader.DownloadStage;
import hu.piller.enykp.alogic.upgrademanager_v2_0.downloader.ExtractStage;
import hu.piller.enykp.alogic.upgrademanager_v2_0.downloader.FileDownloader;
import hu.piller.enykp.alogic.upgrademanager_v2_0.versiondataconverters.VersionData;
import hu.piller.enykp.interfaces.IErrorList;
import hu.piller.enykp.util.base.ErrorList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class DownloadableComponents extends Components {
   public static final byte UPG_SUCCESS = 0;
   public static final byte UPG_FAILURE = 1;
   public static final byte UPG_DOWNLOAD = 2;
   public static final byte UPG_EXTRACT = 3;
   private ReentrantLock lockProcessing;
   private String preferredFormat;
   private Vector<ComponentProcessingEventListener> listeners;
   private Thread controlThread;
   private ReentrantReadWriteLock lock_CT;
   private ReentrantReadWriteLock fireLock;
   private String[] orgsNotConnected;

   public DownloadableComponents() {
      this.preferredFormat = "jar";
      this.listeners = new Vector();
      this.lockProcessing = new ReentrantLock();
      this.lock_CT = new ReentrantReadWriteLock();
      this.fireLock = new ReentrantReadWriteLock();
      ComponentsReader var1 = ComponentsReaderFactory.getDownloadableComponentsReader();
      this.components.addAll(var1.getComponents());
      this.orgsNotConnected = var1.getOrgsNotConnected();
   }

   public DownloadableComponents(String[] var1) {
      this.preferredFormat = "jar";
      this.listeners = new Vector();
      this.lockProcessing = new ReentrantLock();
      this.lock_CT = new ReentrantReadWriteLock();
      this.fireLock = new ReentrantReadWriteLock();
      ComponentsReader var2 = ComponentsReaderFactory.getDownloadableComponentsReader(var1);
      this.components.addAll(var2.getComponents());
      this.orgsNotConnected = var2.getOrgsNotConnected();
   }

   public DownloadableComponents(DownloadableComponents var1) {
      this.preferredFormat = var1.getPreferredFormat();
      this.listeners = new Vector();
      this.lockProcessing = new ReentrantLock();
      this.lock_CT = new ReentrantReadWriteLock();
      this.fireLock = new ReentrantReadWriteLock();
      this.components.addAll(var1.getComponents());
      this.orgsNotConnected = new String[0];
   }

   public DownloadableComponents(Collection<VersionData> var1) {
      this.preferredFormat = "jar";
      this.listeners = new Vector();
      this.lockProcessing = new ReentrantLock();
      this.lock_CT = new ReentrantReadWriteLock();
      this.fireLock = new ReentrantReadWriteLock();
      this.components.addAll(var1);
      this.orgsNotConnected = new String[0];
   }

   public String[] getOrgsNotConnected() {
      return this.orgsNotConnected;
   }

   public void addComponentProcessedEventListener(ComponentProcessingEventListener var1) {
      try {
         this.fireLock.writeLock().lock();
         this.listeners.add(var1);
      } finally {
         this.fireLock.writeLock().unlock();
      }

   }

   public void removeComponentProcessedEventListener(ComponentProcessingEventListener var1) {
      try {
         this.fireLock.writeLock().lock();
         this.listeners.remove(var1);
      } finally {
         this.fireLock.writeLock().unlock();
      }

   }

   public void fireComponentProcessedEvent(VersionData var1, byte var2, String var3) {
      try {
         this.fireLock.readLock().lock();
         ComponentProcessingEvent var4 = new ComponentProcessingEvent(this.getClass().getName(), var1.getOrganization(), var1.getName(), var1.getCategory(), var1.getVersion().toString(), var2, var3);
         Iterator var5 = this.listeners.iterator();

         while(var5.hasNext()) {
            ComponentProcessingEventListener var6 = (ComponentProcessingEventListener)var5.next();
            var6.componentProcessed(var4);
         }
      } finally {
         this.fireLock.readLock().unlock();
      }

   }

   public void install() throws UpgradeBusinessException {
      try {
         this.lockProcessing.lock();

         try {
            this.lock_CT.writeLock().lock();
            this.controlThread = Thread.currentThread();
         } finally {
            this.lock_CT.writeLock().unlock();
         }

         FileDownloader.cleanDirectory(Directories.getUpgradePath(), false);
         CountDownLatch var1 = new CountDownLatch(this.components.size() * 2);
         ExtractStage var2 = new ExtractStage(Directories.getUpgradePath(), Directories.getAbevrootPath(), var1, this, true, true, true);
         DownloadStage var3 = new DownloadStage("jar", Directories.getUpgradePath(), var1, this, true, false, true);
         var3.setOutputQueue(var2.getInputQueue());
         var2.startStage();
         var3.startStage();
         var3.addTasks(this.components);

         try {
            var1.await();
         } catch (InterruptedException var12) {
            var3.stopStage();
            var2.stopStage();
         }

         Vector var4 = (Vector)this.getComponents();
         var4.removeAll(var2.getOutputQueue());
         var4.addAll(var3.getErrorQueue());
         var4.addAll(var2.getErrorQueue());
         this.setComponents(var4);
         OrgInfo.getInstance().mountDir(OrgInfo.getInstance().getResourcePath());
      } finally {
         this.lockProcessing.unlock();
      }

   }

   public void replicate() throws UpgradeBusinessException {
      Vector var1 = new Vector();

      try {
         this.lockProcessing.lock();
         CountDownLatch var2 = new CountDownLatch(this.components.size());
         DownloadStage var3 = new DownloadStage(this.getPreferredFormat(), Directories.getDownloadPath(), var2, this, true, true, true);
         var3.startStage();
         var3.addTasks(this.components);

         try {
            var2.await();
         } catch (InterruptedException var10) {
            var3.stopStage();
         }

         DownloadableComponents.VersionXml var4 = new DownloadableComponents.VersionXml(Directories.getDownloadPath());
         Iterator var5 = var3.getOutputQueue().iterator();

         while(var5.hasNext()) {
            VersionData var6 = (VersionData)var5.next();
            var4.appendDescriptor(var6);
         }

         var4.persist();
         this.components.removeAll(var1);
      } finally {
         this.lockProcessing.unlock();
      }
   }

   public void abortProcessing() {
      try {
         this.lock_CT.writeLock().lock();
         this.controlThread.interrupt();
      } finally {
         this.lock_CT.writeLock().unlock();
      }

   }

   public boolean isProcessingAborted() {
      boolean var1;
      try {
         this.lock_CT.readLock().lock();
         var1 = this.controlThread.isInterrupted();
      } finally {
         this.lock_CT.readLock().unlock();
      }

      return var1;
   }

   public void setPreferredFormat(String var1) {
      this.preferredFormat = var1;
   }

   public String getPreferredFormat() {
      return this.preferredFormat;
   }

   private Vector getOrgNames() {
      return (Vector)((Object[])((Object[])OrgInfo.getInstance().getOrgNames()))[1];
   }

   private class VersionXml {
      static final String VERSION_XML_FILE = "enyk_gen.xml";
      static final String FRAMEWORK = "Framework";
      static final String TEMPLATE = "Template";
      static final String HELP = "Help";
      static final String RESOURCE = "Orgresource";
      private Hashtable<String, StringBuffer> elements = new Hashtable();
      private String downloadDir;
      private boolean has_content;

      VersionXml(String var2) {
         this.elements.put("Framework", new StringBuffer(""));
         this.elements.put("Template", new StringBuffer(""));
         this.elements.put("Help", new StringBuffer(""));
         this.elements.put("Orgresource", new StringBuffer(""));
         this.downloadDir = var2;
      }

      public void appendDescriptor(VersionData var1) {
         if (!"Orgresource".equals(var1.getCategory())) {
            StringBuffer var2 = (StringBuffer)this.elements.get(var1.getCategory());
            if ("Framework".equals(var1.getCategory())) {
               var2.append("<keretprogram>");
            } else if ("Template".equals(var1.getCategory())) {
               var2.append("<nyomtatvany>");
            } else {
               var2.append("<utmutato>");
            }

            var2.append("<kategoria>");
            var2.append(var1.getCategory());
            var2.append("</kategoria>");
            var2.append("<szervezet>");
            var2.append(var1.getOrganization());
            var2.append("</szervezet>");
            var2.append("<rovidnev>");
            var2.append(var1.getName());
            var2.append("</rovidnev>");
            var2.append("<verzio>");
            var2.append(var1.getVersion());
            var2.append("</verzio>");
            var2.append("<elnevezes>");
            var2.append(var1.getDescription());
            var2.append("</elnevezes>");
            var2.append("<url>");
            var2.append("file:/");
            var2.append(this.downloadDir);
            var2.append("</url>");
            var2.append("<files>");
            var2.append("<file>");

            try {
               var2.append(var1.getFileNameByType(DownloadableComponents.this.preferredFormat));
            } catch (UpgradeBusinessException var4) {
            }

            var2.append("</file>");
            var2.append("</files>");
            if ("Framework".equals(var1.getCategory())) {
               var2.append("</keretprogram>");
            } else if ("Template".equals(var1.getCategory())) {
               var2.append("</nyomtatvany>");
            } else {
               var2.append("</utmutato>");
            }

            if (!this.has_content) {
               this.has_content = true;
            }

         }
      }

      private String getXml() {
         StringBuffer var1 = new StringBuffer();
         var1.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
         var1.append("<adat>");
         if (!"".equals(this.elements.get("Framework"))) {
            var1.append(((StringBuffer)this.elements.get("Framework")).toString());
         }

         if (!"".equals(this.elements.get("Orgresource"))) {
            var1.append(((StringBuffer)this.elements.get("Orgresource")).toString());
         }

         if (!"".equals(this.elements.get("Template"))) {
            var1.append(((StringBuffer)this.elements.get("Template")).toString());
         }

         if (!"".equals(this.elements.get("Help"))) {
            var1.append(((StringBuffer)this.elements.get("Help")).toString());
         }

         var1.append("</adat>");
         return var1.toString();
      }

      public void persist() {
         try {
            if (this.has_content) {
               FileDownloader.writeContent(this.getXml(), this.downloadDir, "enyk_gen.xml");
            }
         } catch (UpgradeTechnicalException var2) {
            UpgradeLogger.getInstance().log((Exception)var2);
            ErrorList.getInstance().writeError("Frissítés", "Frissítés: " + var2.getMessage() == null ? "verzióleíró fájl készítése sikertelen" : var2.getMessage(), IErrorList.LEVEL_ERROR, var2, (Object)null);
         }

      }
   }
}
