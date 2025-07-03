package hu.piller.enykp.util.gitsync;

import hu.piller.enykp.interfaces.IENYKGitSync;
import hu.piller.enykp.util.base.PropertyList;

public class GitSyncFactory {
   public static IENYKGitSync getENYKGitSync() throws Exception {
      System.out.println("GIT_TESZT_MESSAGE : CHECK MODE");
      if (PropertyList.getInstance().get("prop.usr.git_init_data") == null) {
         System.out.println(" - no git property data, OUTSIDE");
         return new DefaultGitSync(false);
      } else {
         try {
            return new UbevGitSync();
         } catch (Exception var1) {
            return new DefaultGitSync(true);
         }
      }
   }
}
