package hu.piller.enykp.alogic.masterdata.sync.download.statemachine.state;

import hu.piller.enykp.alogic.ebev.datagate.DatagateFunction;
import hu.piller.enykp.alogic.masterdata.core.Entity;
import hu.piller.enykp.alogic.masterdata.sync.download.IMasterDataDownload;
import hu.piller.enykp.alogic.masterdata.sync.download.MasterDataDownloadException;
import hu.piller.enykp.alogic.masterdata.sync.download.MasterDataDownloadResultStatus;
import hu.piller.enykp.alogic.masterdata.sync.download.provider.MasterDataDownloadResponse;
import hu.piller.enykp.alogic.masterdata.sync.download.provider.MasterDataProviderException;
import hu.piller.enykp.alogic.masterdata.sync.download.provider.MasterDataProviderFactory;
import hu.piller.enykp.alogic.masterdata.sync.download.statemachine.IStateMachine;
import hu.piller.enykp.alogic.masterdata.sync.download.statemachine.State;
import hu.piller.enykp.alogic.masterdata.sync.syncdir.SyncDirException;
import hu.piller.enykp.alogic.masterdata.sync.syncdir.SyncDirHandler;
import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.gui.framework.MainFrame;
import hu.piller.enykp.gui.framework.StatusPane;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class MasterDataDownloadWaiting implements IMasterDataDownload {
   private IStateMachine stateMachine;
   private boolean isRunning = false;
   private Timer timer;
   private long waitTime;
   private final Object o = new Object();

   public MasterDataDownloadWaiting(IStateMachine var1) {
      this.stateMachine = var1;
   }

   public void sendMasterDataDownloadRequest(String[] var1) throws MasterDataDownloadException {
      throw new MasterDataDownloadException("Leadott kérelem válasz várunk, új kérelem nem adható be!");
   }

   public Entity getDownloadedEntity(String var1) throws MasterDataDownloadException {
      throw new MasterDataDownloadException("Leadott kérelem válasz várunk, nincsen megjeleníthető eredmény!");
   }

   public Map<String, MasterDataDownloadResultStatus> getResultInfo() throws MasterDataDownloadException {
      throw new MasterDataDownloadException("Leadott kérelem válasz várunk, nincsen megjeleníthető eredmény!");
   }

   public boolean isRunning() {
      return this.isRunning;
   }

   public void startPolling() {
      this.isRunning = true;
      if (this.stateMachine.getContextParams().containsKey("POLL_INTERVAL")) {
         this.waitTime = (Long)Long.class.cast(this.stateMachine.getContextParams().get("POLL_INTERVAL"));
         this.stateMachine.getContextParams().remove("POLL_INTERVAL");
      } else {
         this.waitTime = 2L;
      }

      this.timer = new Timer();
      this.checkResponse();
   }

   public void terminate() {
      this.isRunning = false;
      if (this.timer != null) {
         this.timer.cancel();
      }

      StatusPane.thisinstance.syncMessage.setText("<html><body><font color=\"blue\"><u>Kiszolgálatlan törzsadatletöltési kérelme van!</u></font></body></html>");
   }

   private void checkResponse() {
      Thread var1 = new Thread(new Runnable() {
         public void run() {
            while(MasterDataDownloadWaiting.this.isRunning) {
               MasterDataDownloadWaiting.this.timer.schedule(new TimerTask() {
                  public void run() {
                     try {
                        String var1 = SyncDirHandler.getQueryId();
                        MasterDataDownloadResponse var2 = null;

                        while(true) {
                           try {
                              var2 = MasterDataProviderFactory.getService().receiveMasterDataDownloadResponse(var1);
                              break;
                           } catch (MasterDataProviderException var6) {
                              if (var6.getFunction() != DatagateFunction.LOGIN || !"Sikertelen Ügyfélkapu authentikáció a magyarorszag.hu-n!".equals(var6.getErrMsg())) {
                                 throw var6;
                              }

                              MasterDataDownloadWaiting.this.showErrorMessage(var6.getErrMsg());
                           }
                        }

                        if (var2.hasResult()) {
                           boolean var3 = SyncDirHandler.saveResult(var2.getResult());
                           if (var3) {
                              MasterDataDownloadWaiting.this.showErrorMessage("A törzsadat letöltési kérelem fogadása a NAV-tól egy vagy több adózóra sikertelen:\nKérem, nézze meg a hibát a Szerviz\\Üzenetek menüpontban.");
                           }

                           MasterDataDownloadWaiting.this.stateMachine.setState(State.PROCESSING);
                        } else {
                           MasterDataDownloadWaiting.this.waitTime = var2.getPollInterval();
                           synchronized(MasterDataDownloadWaiting.this.o) {
                              MasterDataDownloadWaiting.this.o.notify();
                           }
                        }
                     } catch (SyncDirException var7) {
                        MasterDataDownloadWaiting.this.showErrorMessage("A törzsadat letöltési kérelem kiszolgálása helyi hiba miatt megszakadt:\n" + var7.getMessage());
                        MasterDataDownloadWaiting.this.terminate();
                     } catch (MasterDataProviderException var8) {
                        MasterDataDownloadWaiting.this.showErrorMessage("A törzsadat letöltési kérelem kiszolgálása megszakadt:\nHiba helye: " + var8.getFunction() + "\n" + "Hiba: " + var8.getErrMsg());
                        MasterDataDownloadWaiting.this.terminate();
                     }

                  }
               }, MasterDataDownloadWaiting.this.waitTime * 1000L);

               try {
                  synchronized(MasterDataDownloadWaiting.this.o) {
                     MasterDataDownloadWaiting.this.o.wait();
                  }
               } catch (InterruptedException var4) {
                  MasterDataDownloadWaiting.this.showErrorMessage("A törzsadat letöltési kérelem kiszolgálása megszakadt:\n" + var4.getMessage());
                  MasterDataDownloadWaiting.this.terminate();
               }
            }

         }
      }, "MasterDataDownload polling");
      var1.start();
   }

   public void done() throws MasterDataDownloadException {
      throw new MasterDataDownloadException("A válaszra várakozás alatt a szinkronizációs folyamat nem zárható le!");
   }

   private void showErrorMessage(String var1) {
      GuiUtil.showMessageDialog(MainFrame.thisinstance, var1, "Hiba", 0);
   }
}
