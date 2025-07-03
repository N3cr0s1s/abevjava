package hu.piller.enykp.extensions.db;

import hu.piller.enykp.util.base.PropertyList;
import java.util.Vector;

public class DbFactory {
   private static IDbHandler dbhandler;
   public static final String DEFAULT_DB_HANDLER = "dbHandler";

   public static IDbHandler getDbHandler() {
      return getDbHandler((String)null);
   }

   public static IDbHandler getDbHandler(String var0) {
      return getDbHandler(var0, true);
   }

   public static IDbHandler getDbHandler(String var0, boolean var1) {
      String var2 = null;
      if (dbhandler == null) {
         try {
            if (var0 != null) {
               var2 = var0;
            } else {
               Vector var3 = (Vector)PropertyList.getInstance().get("prop.const.dbHandler");
               if (var3 != null && var3.size() > 0) {
                  var2 = (String)((String)var3.get(0));
               }
            }

            if (var2 != null) {
               Class var8 = Class.forName(var2);
               dbhandler = (IDbHandler)var8.newInstance();
            }
         } catch (ClassNotFoundException var4) {
            if (var1) {
               var4.printStackTrace();
            }
         } catch (InstantiationException var5) {
            if (var1) {
               var5.printStackTrace();
            }
         } catch (IllegalAccessException var6) {
            if (var1) {
               var6.printStackTrace();
            }
         } catch (Exception var7) {
            if (var1) {
               var7.printStackTrace();
            }
         }
      }

      return dbhandler;
   }
}
