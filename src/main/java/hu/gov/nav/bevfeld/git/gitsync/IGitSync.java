package hu.gov.nav.bevfeld.git.gitsync;

import java.util.Properties;

public interface IGitSync {
    void init(Properties properties);

    String syncWithGit(String var1);
}
