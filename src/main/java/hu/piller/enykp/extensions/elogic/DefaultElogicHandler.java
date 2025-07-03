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

public class DefaultElogicHandler implements IELogic {
   private boolean debug_on = false;

   public IELogicResult callELogic(String var1, String var2, IPropertyList var3, IDataStore var4, BookModel var5, MetaInfo var6, IDbHandler var7, IErrorList var8, IEventLog var9, TemplateChecker var10) {
      if (this.debug_on) {
         System.out.println("DefaultElogicHandler.callELogic:" + var1);
      }

      IELogicResult var11 = new IELogicResult() {
         public int getStatus() {
            return 0;
         }

         public String getMessage() {
            return null;
         }
      };
      return var11;
   }

   public String[] getPreCollectData(String var1) {
      return null;
   }

   public Vector<String> getKeyData() {
      return null;
   }

   public Hashtable<String, String> getKeyDatasKM() {
      return null;
   }

   public Vector<String> getErrorCodeList() {
      return null;
   }
}
