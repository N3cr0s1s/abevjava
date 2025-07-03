package hu.piller.enykp.extensions.elogic;

import hu.piller.enykp.util.base.PropertyList;
import hu.piller.enykp.util.base.Tools;
import java.util.Vector;

public class ELogicFactory {
   private static IELogic elogic;
   public static final String DEFAULT_ELOGIC_HANDLER = "eLogicHandler";

   public static IELogic getELogic() {
      return getELogic((String)null);
   }

   public static IELogic getELogic(String var0) {
      String var1 = null;
      if (elogic == null) {
         try {
            if (var0 != null) {
               var1 = var0;
            } else {
               Vector var2 = (Vector)PropertyList.getInstance().get("prop.const.eLogicHandler");
               if (var2 != null && var2.size() > 0) {
                  var1 = (String)((String)var2.get(0));
               }
            }

            if (var1 != null) {
               Class var4 = Class.forName(var1);
               elogic = (IELogic)var4.newInstance();
            }
         } catch (Exception var3) {
            Tools.eLog(var3, 0);
         }

         if (elogic == null) {
            elogic = new DefaultElogicHandler();
         }

         System.out.println(elogic.getClass());
      }

      return elogic;
   }
}
