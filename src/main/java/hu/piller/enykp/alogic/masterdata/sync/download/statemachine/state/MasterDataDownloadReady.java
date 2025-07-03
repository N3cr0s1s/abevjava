package hu.piller.enykp.alogic.masterdata.sync.download.statemachine.state;

import hu.piller.enykp.alogic.ebev.datagate.DatagateFunction;
import hu.piller.enykp.alogic.masterdata.core.Entity;
import hu.piller.enykp.alogic.masterdata.sync.download.IMasterDataDownload;
import hu.piller.enykp.alogic.masterdata.sync.download.MasterDataDownloadException;
import hu.piller.enykp.alogic.masterdata.sync.download.MasterDataDownloadResultStatus;
import hu.piller.enykp.alogic.masterdata.sync.download.provider.MasterDataDownloadRequest;
import hu.piller.enykp.alogic.masterdata.sync.download.provider.MasterDataDownloadRequestACK;
import hu.piller.enykp.alogic.masterdata.sync.download.provider.MasterDataProviderException;
import hu.piller.enykp.alogic.masterdata.sync.download.provider.MasterDataProviderFactory;
import hu.piller.enykp.alogic.masterdata.sync.download.statemachine.IStateMachine;
import hu.piller.enykp.alogic.masterdata.sync.download.statemachine.State;
import hu.piller.enykp.alogic.masterdata.sync.syncdir.SyncDirException;
import hu.piller.enykp.alogic.masterdata.sync.syncdir.SyncDirHandler;
import hu.piller.enykp.gui.GuiUtil;
import hu.piller.enykp.gui.framework.MainFrame;
import java.util.Map;

public class MasterDataDownloadReady implements IMasterDataDownload {
   private IStateMachine stateMachine;

   public MasterDataDownloadReady(IStateMachine var1) {
      this.stateMachine = var1;
   }

   public void sendMasterDataDownloadRequest(String[] var1) throws MasterDataDownloadException {
      if (var1 != null && var1.length != 0) {
         try {
            MasterDataDownloadRequest var2 = new MasterDataDownloadRequest();
            var2.setAzonositok(var1);
            MasterDataDownloadRequestACK var3 = MasterDataProviderFactory.getService().sendMasterDataDownloadRequest(var2);
            SyncDirHandler.createFolderForRequest(var3.getQueryId());
            SyncDirHandler.createTemporaryResultsForRefusedIds((String[])var3.getRefusedIds().toArray(new String[var3.getRefusedIds().size()]));
            this.stateMachine.getContextParams().put("POLL_INTERVAL", var3.getPollInterval());
            this.stateMachine.setState(State.WAITING);
         } catch (MasterDataProviderException var4) {
            if (var4.getFunction() != DatagateFunction.LOGIN || !"Sikertelen Ügyfélkapu authentikáció a magyarorszag.hu-n!".equals(var4.getErrMsg())) {
               throw new MasterDataDownloadException(var4.getFunction() + ": " + var4.getErrMsg());
            }

            GuiUtil.showMessageDialog(MainFrame.thisinstance, var4.getErrMsg(), "Hiba", 0);
            this.sendMasterDataDownloadRequest(var1);
         } catch (SyncDirException var5) {
            throw new MasterDataDownloadException(var5.getMessage());
         }

      } else {
         throw new MasterDataDownloadException("Nincsen megadva egyetlen adózó sem törzsadat letöltésre!");
      }
   }

   public Entity getDownloadedEntity(String var1) throws MasterDataDownloadException {
      throw new MasterDataDownloadException("Nincsen feldogozatlan törzsadat");
   }

   public Map<String, MasterDataDownloadResultStatus> getResultInfo() throws MasterDataDownloadException {
      throw new MasterDataDownloadException("Nincsen feldogozatlan törzsadat");
   }

   public void done() throws MasterDataDownloadException {
      throw new MasterDataDownloadException("Nincsen feldogozatlan törzsadat");
   }
}
