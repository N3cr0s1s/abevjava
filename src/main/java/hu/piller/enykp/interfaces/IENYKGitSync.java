package hu.piller.enykp.interfaces;

import java.util.Properties;

public interface IENYKGitSync {
   void init(Properties var1);

   String syncWithGit(String var1) throws Exception;
}
