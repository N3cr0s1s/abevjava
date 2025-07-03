package hu.piller.enykp.extensions.elogic;

import hu.piller.enykp.alogic.fileutil.TemplateChecker;
import hu.piller.enykp.alogic.metainfo.MetaInfo;
import hu.piller.enykp.extensions.db.IDbHandler;
import hu.piller.enykp.gui.model.BookModel;
import hu.piller.enykp.interfaces.IDataStore;
import hu.piller.enykp.interfaces.IErrorList;
import hu.piller.enykp.interfaces.IEventLog;
import hu.piller.enykp.interfaces.IPropertyList;
import java.util.Hashtable;
import java.util.Vector;

public interface IELogic {
   String EVENT_AFTER_TEMPLATE_LOAD = "nyomtatvany_parameterek_betoltese_utan";
   String EVENT_AFTER_DATA_LOAD = "bizonylat_adatok_betoltese_utan";
   String EVENT_BATCH_BEFORE_DATA_LOAD = "batch_bizonylat_adatok_betoltese_elott";
   String EVENT_BEFORE_CHECK = "bizonylat_ellenorzes_elott";
   String EVENT_AFTER_CHECK = "bizonylat_ellenorzes_utan";
   String EVENT_MAIN_CHECK = "fobizonylat_ellenorzes";
   String EVENT_CHECK = "bizonylat_ellenorzes";
   String EVENT_SERVICE = "db_service_run_silent";
   String EVENT_BEFORE_SAVE = "bizonylat_mentes_elott";
   String EVENT_BATCH_AFTER_CHECK = "batch_bizonylat_ellenorzes_utan";
   String EVENT_BATCH_FILTER_ERRORCODES = "batch_onya_hibakod_filter";
   String COMMMON_CHECK_ERROR_CODE = "p001";

   IELogicResult callELogic(String var1, String var2, IPropertyList var3, IDataStore var4, BookModel var5, MetaInfo var6, IDbHandler var7, IErrorList var8, IEventLog var9, TemplateChecker var10);

   String[] getPreCollectData(String var1);

   Vector<String> getKeyData();

   Hashtable<String, String> getKeyDatasKM();

   Vector<String> getErrorCodeList();
}
