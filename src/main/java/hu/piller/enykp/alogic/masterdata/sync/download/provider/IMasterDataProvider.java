package hu.piller.enykp.alogic.masterdata.sync.download.provider;

public interface IMasterDataProvider {
   MasterDataDownloadRequestACK sendMasterDataDownloadRequest(MasterDataDownloadRequest var1) throws MasterDataProviderException;

   MasterDataDownloadResponse receiveMasterDataDownloadResponse(String var1) throws MasterDataProviderException;
}
