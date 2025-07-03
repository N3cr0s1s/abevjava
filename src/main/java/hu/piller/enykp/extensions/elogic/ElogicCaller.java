package hu.piller.enykp.extensions.elogic;

import hu.piller.enykp.alogic.calculator.calculator_c.Calculator;
import hu.piller.enykp.alogic.fileutil.TemplateChecker;
import hu.piller.enykp.alogic.metainfo.MetaInfo;
import hu.piller.enykp.extensions.db.DbFactory;
import hu.piller.enykp.gui.framework.MainFrame;
import hu.piller.enykp.gui.model.BookModel;
import hu.piller.enykp.interfaces.IDataStore;
import hu.piller.enykp.util.base.ErrorList;
import hu.piller.enykp.util.base.EventLog;
import hu.piller.enykp.util.base.PropertyList;

public class ElogicCaller {
   private static boolean permitEcall = true;

   public static boolean isPermitEcall() {
      return permitEcall;
   }

   public static void setPermitEcall(boolean var0) {
      permitEcall = var0;
   }

   public static IELogicResult exec(String var0, BookModel var1) {
      if (permitEcall && !MainFrame.role.equals("0") && !MainFrame.opmode.equals("0") && (!MainFrame.readonlymodefromubev || "nyomtatvany_parameterek_betoltese_utan".equalsIgnoreCase(var0))) {
         try {
            return ELogicFactory.getELogic().callELogic(var0, (String)null, PropertyList.getInstance(), var1.get_datastore(), var1, MetaInfo.getInstance(), DbFactory.getDbHandler(), ErrorList.getInstance(), EventLog.getInstance(), TemplateChecker.getInstance());
         } catch (Exception var4) {
            ErrorList.getInstance().writeError(Calculator.ERR_ID_FIELD_CHECK, "[p001] " + var4.getMessage(), ErrorList.LEVEL_FATAL_ERROR, (Exception)null, (Object)null, (Object)"p001", (Object)var4.getMessage());
            IELogicResult var3 = new IELogicResult() {
               public int getStatus() {
                  return -5;
               }

               public String getMessage() {
                  return null;
               }
            };
            return var3;
         }
      } else {
         return new IELogicResult() {
            public int getStatus() {
               return 0;
            }

            public String getMessage() {
               return null;
            }
         };
      }
   }

   public static void service(String var0) {
      try {
         ELogicFactory.getELogic().callELogic("db_service_run_silent", var0, PropertyList.getInstance(), (IDataStore)null, (BookModel)null, (MetaInfo)null, DbFactory.getDbHandler(), ErrorList.getInstance(), EventLog.getInstance(), TemplateChecker.getInstance());
      } catch (Exception var2) {
         var2.printStackTrace();
      }

   }

   public static IELogicResult eventAfterDataLoad(BookModel var0) {
      return exec("bizonylat_adatok_betoltese_utan", var0);
   }

   public static IELogicResult eventBatchBeforeDataLoad(BookModel var0) {
      return exec("batch_bizonylat_adatok_betoltese_elott", var0);
   }

   public static IELogicResult eventBeforeSave(BookModel var0) {
      return exec("bizonylat_mentes_elott", var0);
   }

   public static IELogicResult eventBatchAfterCheck(BookModel var0) {
      return exec("batch_bizonylat_ellenorzes_utan", var0);
   }

   public static IELogicResult eventBatchFilterErrorCodes(BookModel var0) {
      return exec("batch_onya_hibakod_filter", var0);
   }

   public static IELogicResult teszt(String var0) {
      IELogicResult var1 = new IELogicResult() {
         public int getStatus() {
            return -15;
         }

         public String getMessage() {
            return "Hiba történt a nem tudom mi miatt...";
         }
      };
      return var1;
   }
}
