package hu.piller.enykp.alogic.masterdata.sync.download.provider;

import hu.piller.enykp.alogic.ebev.datagate.DatagateFunction;

public class MasterDataProviderException extends Exception {
   private DatagateFunction function;
   private String errMsg;

   public MasterDataProviderException() {
   }

   public MasterDataProviderException(DatagateFunction var1, String var2) {
      super(var2);
      this.errMsg = var2;
      this.function = var1;
   }

   public DatagateFunction getFunction() {
      return this.function;
   }

   public String getErrMsg() {
      return this.errMsg;
   }

   public boolean isVerbose() {
      return this.function != null && this.errMsg != null;
   }
}
