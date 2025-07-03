package hu.piller.enykp.alogic.masterdata.sync.download;

import hu.piller.enykp.alogic.masterdata.core.Entity;
import hu.piller.enykp.alogic.masterdata.sync.download.statemachine.IStateChangeListener;
import hu.piller.enykp.alogic.masterdata.sync.download.statemachine.IStateMachine;
import hu.piller.enykp.alogic.masterdata.sync.download.statemachine.State;
import hu.piller.enykp.alogic.masterdata.sync.download.statemachine.state.MasterDataDownloadProcessing;
import hu.piller.enykp.alogic.masterdata.sync.download.statemachine.state.MasterDataDownloadReady;
import hu.piller.enykp.alogic.masterdata.sync.download.statemachine.state.MasterDataDownloadWaiting;
import hu.piller.enykp.alogic.masterdata.sync.syncdir.SyncDirException;
import hu.piller.enykp.alogic.masterdata.sync.syncdir.SyncDirHandler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MasterDataDownload implements IMasterDataDownload, IStateMachine {
   private static MasterDataDownload instance;
   private List<IStateChangeListener> stateChangeListeners = new ArrayList();
   private IMasterDataDownload currentDownloader;
   private Map<String, Object> contextParams;

   public static MasterDataDownload getInstance() {
      if (instance == null) {
         instance = new MasterDataDownload();
      }

      return instance;
   }

   private MasterDataDownload() {
      try {
         this.contextParams = new HashMap();
         this.setState(this.calcState());
      } catch (MasterDataDownloadException var2) {
         var2.printStackTrace();
      }

   }

   public void setState(State var1) {
      State var2 = null;
      if (this.currentDownloader instanceof MasterDataDownloadReady) {
         var2 = State.READY;
      } else if (this.currentDownloader instanceof MasterDataDownloadWaiting) {
         var2 = State.WAITING;
      } else if (this.currentDownloader instanceof MasterDataDownloadProcessing) {
         var2 = State.PROCESSING;
      }

      if (var1.equals(State.READY)) {
         this.currentDownloader = new MasterDataDownloadReady(this);
      } else if (var1.equals(State.WAITING)) {
         this.currentDownloader = new MasterDataDownloadWaiting(this);
      } else if (var1.equals(State.PROCESSING)) {
         this.currentDownloader = new MasterDataDownloadProcessing(this);
      }

      Iterator var3 = this.stateChangeListeners.iterator();

      while(var3.hasNext()) {
         IStateChangeListener var4 = (IStateChangeListener)var3.next();
         var4.stateChanged(var2, var1);
      }

   }

   public void addStateChangeListener(IStateChangeListener var1) {
      this.stateChangeListeners.add(var1);
   }

   public void removeStateChangeListener(IStateChangeListener var1) {
      if (this.stateChangeListeners.contains(var1)) {
         this.stateChangeListeners.remove(var1);
      }

   }

   public void removeAllStateChangeListeners() {
      this.stateChangeListeners.clear();
   }

   public Map<String, Object> getContextParams() {
      return this.contextParams;
   }

   public void sendMasterDataDownloadRequest(String[] var1) throws MasterDataDownloadException {
      this.currentDownloader.sendMasterDataDownloadRequest(var1);
   }

   public Entity getDownloadedEntity(String var1) throws MasterDataDownloadException {
      return this.currentDownloader.getDownloadedEntity(var1);
   }

   public Map<String, MasterDataDownloadResultStatus> getResultInfo() throws MasterDataDownloadException {
      return this.currentDownloader.getResultInfo();
   }

   public void done() throws MasterDataDownloadException {
      this.currentDownloader.done();
   }

   public boolean isWaitingForResponse() {
      return this.currentDownloader instanceof MasterDataDownloadWaiting;
   }

   public boolean hasResponse() {
      return this.currentDownloader instanceof MasterDataDownloadProcessing;
   }

   public boolean isReady() {
      return this.currentDownloader instanceof MasterDataDownloadReady;
   }

   public boolean isOperationRunning() {
      return this.currentDownloader instanceof MasterDataDownloadWaiting ? ((MasterDataDownloadWaiting)this.currentDownloader).isRunning() : true;
   }

   public void resumeOperation() {
      if (this.currentDownloader instanceof MasterDataDownloadWaiting) {
         ((MasterDataDownloadWaiting)this.currentDownloader).startPolling();
      }

   }

   private State calcState() throws MasterDataDownloadException {
      try {
         switch(SyncDirHandler.getSyncDirStatus()) {
         case NO_QUERY:
            return State.READY;
         case UNSERVED_QUERY:
            return State.WAITING;
         case SERVED_QUERY:
            return State.PROCESSING;
         default:
            throw new MasterDataDownloadException("A törzsadat szinkronizáció munkakönyvtára érvénytelen állapotba került!");
         }
      } catch (SyncDirException var2) {
         throw new MasterDataDownloadException(var2.getMessage());
      }
   }
}
