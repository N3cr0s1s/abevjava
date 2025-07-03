package hu.piller.enykp.extensions.elogic;

public interface IELogicResult {
   int STATUS_OK = 0;
   int STATUS_STOP_ALL_CHECK = -5;
   int STATUS_STOP_CURRENT_TEMPLATE_TYPE_CHECK = -10;
   int STATUS_STOP_CURRENT_BARKOD_CHECK = -15;
   int STATUS_LEAVE_CURRENT_BARKOD = -20;
   int STATUS_SERVICE_ERROR = -1000;
   int STATUS_AFTER_DATA_LOAD_ERROR = -1001;
   int STATUS_BATCH_BEFORE_DATA_LOAD_ERROR = -1002;

   int getStatus();

   String getMessage();
}
