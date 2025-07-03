package hu.piller.enykp.alogic.masterdata.sync.download;

import hu.piller.enykp.alogic.masterdata.core.Entity;
import java.util.Map;

public interface IMasterDataDownload {
   void sendMasterDataDownloadRequest(String[] var1) throws MasterDataDownloadException;

   Entity getDownloadedEntity(String var1) throws MasterDataDownloadException;

   Map<String, MasterDataDownloadResultStatus> getResultInfo() throws MasterDataDownloadException;

   void done() throws MasterDataDownloadException;
}
