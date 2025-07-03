package hu.piller.enykp.util.gitsync;

import hu.piller.enykp.interfaces.IENYKGitSync;
import java.util.Properties;

public class DefaultGitSync implements IENYKGitSync {
   public static final String DONT_NEED_GIT_MESSAGE = "DONT_NEED_GIT";
   public static final String DONT_HAVE_GIT_MESSAGE = "DONT_HAVE_GIT";
   private boolean constructedByError;

   public DefaultGitSync(boolean var1) {
      this.constructedByError = var1;
   }

   public void init(Properties var1) {
   }

   public String syncWithGit(String var1) throws Exception {
      return this.constructedByError ? "DONT_HAVE_GIT" : "DONT_NEED_GIT";
   }
}
